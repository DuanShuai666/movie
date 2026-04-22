import os
import torch
import pickle
import random
import numpy as np
from transformers import AutoTokenizer, AutoModel, AutoModelForCausalLM
from sklearn.metrics.pairwise import cosine_similarity
from db_utils import MovieDatabase, QADataGenerator
from config import MODEL_CONFIG, QUERY_CONFIG, ENCODER_TRAIN_CONFIG
import warnings

warnings.filterwarnings("ignore", message=".*flash attention.*")

RANDOM_SEED = 42
torch.manual_seed(RANDOM_SEED)
if torch.cuda.is_available():
    torch.cuda.manual_seed_all(RANDOM_SEED)
random.seed(RANDOM_SEED)
np.random.seed(RANDOM_SEED)


class RAGInference:
    """RAG 推理服务类"""

    def __init__(self):
        self.device = None
        self.sentence_tokenizer = None
        self.sentence_model = None
        self.generator_tokenizer = None
        self.generator_model = None
        self.index_data = None
        self.initialized = False

    def initialize(self):
        """初始化模型和索引数据"""
        if self.initialized:
            return True

        use_fine_tuned_encoder = MODEL_CONFIG.get('use_fine_tuned_encoder', False)
        use_fine_tuned_generator = MODEL_CONFIG.get('use_fine_tuned_generator', False)

        if use_fine_tuned_encoder and os.path.exists(MODEL_CONFIG['fine_tuned_encoder_path']):
            encoder_path = MODEL_CONFIG['fine_tuned_encoder_path']
        else:
            encoder_path = MODEL_CONFIG.get('local_encoder_path', MODEL_CONFIG['sentence_model'])

        if use_fine_tuned_generator and os.path.exists(MODEL_CONFIG['fine_tuned_generator_path']):
            generator_path = MODEL_CONFIG['fine_tuned_generator_path']
        else:
            generator_path = MODEL_CONFIG.get('local_generator_path', MODEL_CONFIG['generator_model'])

        try:
            self.sentence_tokenizer = AutoTokenizer.from_pretrained(encoder_path)
            self.sentence_model = AutoModel.from_pretrained(encoder_path)

            if self.sentence_tokenizer.pad_token is None:
                self.sentence_tokenizer.pad_token = self.sentence_tokenizer.eos_token

            self.generator_tokenizer = AutoTokenizer.from_pretrained(generator_path)
            self.generator_model = AutoModelForCausalLM.from_pretrained(generator_path)

            if self.generator_tokenizer.pad_token is None:
                self.generator_tokenizer.pad_token = self.generator_tokenizer.eos_token

            index_path = MODEL_CONFIG.get('index_path', './fine_tuned_models/index.pkl')
            if not os.path.exists(index_path):
                return False

            with open(index_path, 'rb') as f:
                self.index_data = pickle.load(f)

            required_keys = ['question_embeddings', 'questions', 'answers']
            if not all(key in self.index_data for key in required_keys):
                return False

            self.device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
            self.sentence_model.eval()
            self.generator_model.eval()
            self.sentence_model.to(self.device)
            self.generator_model.to(self.device)

            self.initialized = True
            return True

        except Exception:
            return False

    def _retrieve(self, query, top_k=None):
        """检索与查询最相关的文档"""
        if top_k is None:
            top_k = QUERY_CONFIG['top_k_results']

        query_inputs = self.sentence_tokenizer(
            [query],
            padding=True,
            truncation=True,
            max_length=256,
            return_tensors='pt'
        ).to(self.device)

        with torch.no_grad():
            query_outputs = self.sentence_model(**query_inputs)
            query_embedding = query_outputs.last_hidden_state.mean(dim=1).cpu().numpy()

        similarities = cosine_similarity(query_embedding, self.index_data['question_embeddings'])[0]
        valid_indices = np.where(similarities > QUERY_CONFIG['similarity_threshold'])[0]

        if len(valid_indices) == 0:
            return []

        top_indices = valid_indices[np.argsort(similarities[valid_indices])[-top_k:][::-1]]

        results = []
        added_answers = set()

        for idx in top_indices:
            answer = self.index_data['answers'][idx]
            if answer not in added_answers:
                results.append({
                    'question': self.index_data['questions'][idx],
                    'answer': answer,
                    'similarity': similarities[idx]
                })
                added_answers.add(answer)

        return results

    def _generate_response(self, context, query):
        """基于上下文和查询生成回答"""
        if not context or not context.strip():
            return "抱歉，我找到了相关信息，但无法生成有效回答。"

        if len(context) > QUERY_CONFIG['max_context_length']:
            sentences = context.split('.')
            truncated_context = ''
            for sentence in sentences:
                if len(truncated_context + sentence) <= QUERY_CONFIG['max_context_length']:
                    truncated_context += sentence + '.'
                else:
                    break
            context = truncated_context if truncated_context else context[:QUERY_CONFIG['max_context_length']]

        input_text = QUERY_CONFIG['input_template'].format(context=context, query=query)

        inputs = self.generator_tokenizer(
            input_text,
            return_tensors='pt',
            truncation=True,
            max_length=512
        ).to(self.device)

        with torch.no_grad():
            outputs = self.generator_model.generate(
                inputs.input_ids,
                attention_mask=inputs.attention_mask,
                max_new_tokens=QUERY_CONFIG['max_generated_tokens'],
                num_return_sequences=1,
                do_sample=True,
                temperature=QUERY_CONFIG['generation_temperature'],
                pad_token_id=self.generator_tokenizer.pad_token_id,
                eos_token_id=self.generator_tokenizer.eos_token_id,
                no_repeat_ngram_size=2,
                repetition_penalty=1.2,
                use_cache=True
            )

        response = self.generator_tokenizer.decode(outputs[0], skip_special_tokens=True)

        if '回答:' in response:
            last_answer_idx = response.rfind('回答:')
            if last_answer_idx != -1:
                response = response[last_answer_idx + 3:].strip()

        import re
        response = re.sub(r'[.!?]{2,}', lambda m: m.group(0)[0], response)
        response = re.sub(r'\n+', '\n', response).strip()

        if len(outputs[0]) >= QUERY_CONFIG['max_generated_tokens']:
            response += "..."

        return response

    def ask_question(self, question):
        """
        回答用户问题

        参数:
            question: 问题字符串

        返回:
            答案字符串
        """
        if not self.initialized:
            if not self.initialize():
                return "系统初始化失败，请检查模型文件是否存在"

        retrieved_docs = self._retrieve(question)

        if not retrieved_docs:
            return "抱歉，我没有找到相关信息。"

        top_similarity = retrieved_docs[0]['similarity']

        if top_similarity < QUERY_CONFIG['min_similarity_for_answer']:
            if top_similarity > 0.3:
                return f"我找到了一些相关信息，但匹配度不高（{top_similarity:.0%}）。最接近的问题是：'{retrieved_docs[0]['question']}'，答案是：{retrieved_docs[0]['answer']}"
            else:
                return f"抱歉，我的数据库中没有关于这个问题的信息。（最相似的结果匹配度仅为 {top_similarity:.0%}）"

        use_generator = MODEL_CONFIG.get('use_fine_tuned_generator', False)

        if use_generator:
            try:
                top_docs = retrieved_docs[:min(3, len(retrieved_docs))]
                context = " ".join([doc['answer'] for doc in top_docs])

                generated_response = self._generate_response(context, question)

                if generated_response and len(generated_response.strip()) > 10:
                    return generated_response
            except Exception:
                pass

        return retrieved_docs[0]['answer']


rag_service = RAGInference()


def get_answer(question: str) -> str:
    """
    对外提供的问答接口

    参数:
        question: 问题字符串

    返回:
        答案字符串
    """
    return rag_service.ask_question(question)


if __name__ == "__main__":
    from flask import Flask, request, jsonify

    app = Flask(__name__)


    @app.route('/api/ask', methods=['POST'])
    def api_ask():
        data = request.get_json()
        if not data or 'question' not in data:
            return jsonify({'error': '缺少问题参数'}), 400

        question = data['question']
        answer = get_answer(question)

        return jsonify({
            'question': question,
            'answer': answer
        })


    @app.route('/api/health', methods=['GET'])
    def health_check():
        return jsonify({
            'status': 'ok',
            'initialized': rag_service.initialized
        })


    print("启动 RAG 服务...")
    if not rag_service.initialize():
        print("初始化失败！")
        exit(1)

    print("服务已启动，监听端口 5000")
    app.run(host='0.0.0.0', port=5000, debug=False)
