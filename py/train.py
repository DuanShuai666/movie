import os
import torch
from transformers import AutoTokenizer, AutoModel, AutoModelForCausalLM
import numpy as np
import pickle
from db_utils import MovieDatabase, QADataGenerator
from config import MODEL_CONFIG, ENCODER_TRAIN_CONFIG, GENERATOR_TRAIN_CONFIG
from torch.utils.data import Dataset
import torch.nn as nn
import torch.nn.functional as F
from transformers import TrainingArguments, Trainer
import time

print("=" * 60)
print("RAG 电影问答系统 - 模型训练")
print("=" * 60)

# 初始化数据库操作对象
db = MovieDatabase()

print("正在从数据库加载电影数据...")
try:
    movies = db.load_movies(limit=1000)
    print(f"成功加载 {len(movies)} 部电影")
except Exception as e:
    print(f"错误：无法从数据库加载电影数据 - {str(e)}")
    exit(1)

if len(movies) == 0:
    print("错误：数据库中未找到电影数据")
    exit(1)

print("正在从本地加载预训练模型...")

# 检查本地模型是否存在
encoder_path = MODEL_CONFIG['local_encoder_path']
generator_path = MODEL_CONFIG['local_generator_path']

if not os.path.exists(encoder_path):
    print(f"错误：本地编码器模型不存在：{encoder_path}")
    print(f"请先运行：python download_models.py")
    exit(1)

if not os.path.exists(generator_path):
    print(f"错误：本地生成器模型不存在：{generator_path}")
    print(f"请先运行：python download_models.py")
    exit(1)

# 确保输出目录存在且可写
os.makedirs(ENCODER_TRAIN_CONFIG['output_dir'], exist_ok=True)
os.makedirs(GENERATOR_TRAIN_CONFIG['output_dir'], exist_ok=True)
os.makedirs(MODEL_CONFIG['fine_tuned_models_root'], exist_ok=True)

# 从本地加载模型
sentence_tokenizer = AutoTokenizer.from_pretrained(encoder_path)
sentence_model = AutoModel.from_pretrained(encoder_path)
print(f"✓ 编码器已加载：{encoder_path}")

if sentence_tokenizer.pad_token is None:
    # 优先使用 eos_token，如果没有则使用 '[PAD]'
    sentence_tokenizer.pad_token = sentence_tokenizer.eos_token if sentence_tokenizer.eos_token else '[PAD]'

generator_tokenizer = AutoTokenizer.from_pretrained(generator_path)
generator_model = AutoModelForCausalLM.from_pretrained(generator_path)
print(f"✓ 生成器已加载：{generator_path}")

if generator_tokenizer.pad_token is None:
    generator_tokenizer.pad_token = generator_tokenizer.eos_token if generator_tokenizer.eos_token else '[PAD]'

print("正在生成训练数据...")

questions, answers = QADataGenerator.generate_qa_pairs(movies)

# ==================== 微调 BERT 编码器（如果启用） ====================
if MODEL_CONFIG.get('use_fine_tuned_encoder', True):
    print("\n" + "=" * 60)
    print("正在微调 BERT 编码器...")
    print("=" * 60)

    # 显示硬件信息
    device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
    print(f"\n硬件检测:")
    print(f"  - 设备：{device}")

    if torch.cuda.is_available():
        gpu_name = torch.cuda.get_device_name(0)
        gpu_memory = torch.cuda.get_device_properties(0).total_memory / 1024**3
        print(f"  - GPU: {gpu_name}")
        print(f"  - 显存：{gpu_memory:.2f} GB")
        print(f"  - CUDA 版本：{torch.version.cuda}")
        
        # 根据显存给出建议
        if gpu_memory < 4:
            print(f"\n⚠️  警告：显存较小 ({gpu_memory:.1f}GB)，建议使用较小的 batch size (4-8)")
        elif gpu_memory < 6:
            print(f"\n✓ 显存适中 ({gpu_memory:.1f}GB)，当前配置应该可以运行")
        else:
            print(f"\n✓ 显存充足 ({gpu_memory:.1f}GB)，可以尝试更大的 batch size")
    else:
        print("\n⚠️  警告：未检测到 GPU，将使用 CPU 训练（非常缓慢！）")
        print("建议：降低 batch_size 到 4，或考虑使用 GPU")

    # 显示训练配置
    print(f"\n训练配置:")
    print(f"  - 训练数据：{len(questions)} 个问答对")
    print(f"  - 训练轮数：{ENCODER_TRAIN_CONFIG['num_train_epochs']} epochs")
    print(f"  - Batch size: {ENCODER_TRAIN_CONFIG['per_device_train_batch_size']}")
    print(f"  - 学习率：{ENCODER_TRAIN_CONFIG['learning_rate']}")
    print(f"  - 最大长度：{ENCODER_TRAIN_CONFIG['max_length']} tokens")
    print(f"  - 日志间隔：每 {ENCODER_TRAIN_CONFIG['logging_steps']} 步")

    # 估算训练时间
    if torch.cuda.is_available():
        # GPU 模式：RTX 3050 大约 20-30 样本/秒
        samples_per_second = 25
        total_samples = len(questions) * ENCODER_TRAIN_CONFIG['num_train_epochs']
        estimated_minutes = total_samples / samples_per_second / 60
        print(f"\n预计训练时间：{estimated_minutes:.1f} 分钟 (GPU 模式)")
    else:
        print(f"\n预计训练时间：30-60 分钟 (CPU 模式)")

    print("\n开始训练...")
    print("-" * 60)
    
    # 创建对比学习数据集类（双塔架构）
    class ContrastiveDataset(Dataset):
        """用于对比学习的数据集 - 双塔架构"""
        def __init__(self, questions, answers, tokenizer, max_length=None):
            self.questions = questions
            self.answers = answers
            self.tokenizer = tokenizer
            # 从配置文件读取最大长度
            self.max_length = max_length if max_length else ENCODER_TRAIN_CONFIG['max_length']
        
        def __len__(self):
            return len(self.questions)
        
        def __getitem__(self, idx):
            question = self.questions[idx]
            answer = self.answers[idx]
            
            # 分别编码问题和答案（双塔输入）
            q_encoding = self.tokenizer(
                question,
                truncation=True,
                padding='max_length',
                max_length=self.max_length,
                return_tensors='pt'
            )
            
            a_encoding = self.tokenizer(
                answer,
                truncation=True,
                padding='max_length',
                max_length=self.max_length,
                return_tensors='pt'
            )
            
            return {
                'q_input_ids': q_encoding['input_ids'].squeeze(0),
                'q_attention_mask': q_encoding['attention_mask'].squeeze(0),
                'a_input_ids': a_encoding['input_ids'].squeeze(0),
                'a_attention_mask': a_encoding['attention_mask'].squeeze(0)
            }
    
    # 创建数据集
    contrastive_dataset = ContrastiveDataset(questions, answers, sentence_tokenizer)
    
    # 定义 InfoNCE 损失函数（对比学习常用）- 双塔版本
    class InfoNCELoss(nn.Module):
        def __init__(self, temperature=None):
            super().__init__()
            # 从配置文件读取温度参数
            self.temperature = temperature if temperature else ENCODER_TRAIN_CONFIG['temperature']
            self.cross_entropy = nn.CrossEntropyLoss()
        
        def forward(self, q_embeddings, a_embeddings):
            """
            计算问题和答案 embedding 之间的对比损失
            
            Args:
                q_embeddings: [batch_size, dim] 问题 embeddings
                a_embeddings: [batch_size, dim] 答案 embeddings
            """
            batch_size = q_embeddings.shape[0]
            
            # 计算所有问题 - 答案对的相似度矩阵 [batch_size, batch_size]
            # 对角线是正样本对，非对角线是负样本对
            similarities = F.cosine_similarity(q_embeddings.unsqueeze(1), a_embeddings.unsqueeze(0), dim=2)
            similarities = similarities / self.temperature
            
            # 构造标签（对角线是正样本）
            labels = torch.arange(batch_size).to(q_embeddings.device)
            
            # 计算交叉熵损失
            loss = self.cross_entropy(similarities, labels)
            return loss
    
    # 从配置文件读取训练参数
    encoder_training_args = TrainingArguments(
        output_dir=ENCODER_TRAIN_CONFIG['output_dir'],
        num_train_epochs=ENCODER_TRAIN_CONFIG['num_train_epochs'],
        per_device_train_batch_size=ENCODER_TRAIN_CONFIG['per_device_train_batch_size'],
        learning_rate=ENCODER_TRAIN_CONFIG['learning_rate'],
        weight_decay=ENCODER_TRAIN_CONFIG['weight_decay'],
        warmup_ratio=ENCODER_TRAIN_CONFIG['warmup_ratio'],
        logging_steps=ENCODER_TRAIN_CONFIG['logging_steps'],
        save_steps=ENCODER_TRAIN_CONFIG['save_steps'],
        save_total_limit=ENCODER_TRAIN_CONFIG['save_total_limit'],
        save_strategy=ENCODER_TRAIN_CONFIG['save_strategy'],
        load_best_model_at_end=ENCODER_TRAIN_CONFIG['load_best_model_at_end'],
        fp16=ENCODER_TRAIN_CONFIG.get('fp16', False),  # 启用混合精度
    )
    
    # 自定义 Trainer 用于对比学习
    class ContrastiveTrainer(Trainer):
        def __init__(self, *args, **kwargs):
            super().__init__(*args, **kwargs)
            # 初始化时创建 loss_fn，避免重复实例化
            self.loss_fn = InfoNCELoss(ENCODER_TRAIN_CONFIG['temperature'])
        
        def compute_loss(self, model, inputs, return_outputs=False, num_items_in_batch=None):
            # 分别编码问题和答案（双塔架构）
            q_outputs = model(
                input_ids=inputs['q_input_ids'],
                attention_mask=inputs['q_attention_mask']
            )
            
            a_outputs = model(
                input_ids=inputs['a_input_ids'],
                attention_mask=inputs['a_attention_mask']
            )
            
            # 使用均值池化获取句子向量
            q_embeddings = q_outputs.last_hidden_state.mean(dim=1)
            a_embeddings = a_outputs.last_hidden_state.mean(dim=1)
            
            # 计算对比损失
            loss = self.loss_fn(q_embeddings, a_embeddings)
            
            # 修复：返回 None 而不是未定义的 outputs
            return (loss, None) if return_outputs else loss

    # 创建 Trainer
    contrastive_trainer = ContrastiveTrainer(
        model=sentence_model,
        args=encoder_training_args,
        train_dataset=contrastive_dataset,
    )
    
    # 开始微调（Trainer.train() 会自动设置模型为 train 模式）
    
    # 检查是否存在 checkpoint 并恢复训练
    checkpoints = [d for d in os.listdir(ENCODER_TRAIN_CONFIG['output_dir']) 
                   if d.startswith('checkpoint') and os.path.isdir(os.path.join(ENCODER_TRAIN_CONFIG['output_dir'], d))]
    
    resume_from_checkpoint = None
    if checkpoints:
        print(f"\n发现 {len(checkpoints)} 个已保存的 checkpoint:")
        print(f"  - {', '.join(sorted(checkpoints))}")
        
        # 找到最新的 checkpoint
        latest_checkpoint = sorted(checkpoints)[-1]
        resume_from_checkpoint = os.path.join(ENCODER_TRAIN_CONFIG['output_dir'], latest_checkpoint)
        print(f"\n⚠️  检测到未完成的训练，将从 checkpoint 恢复：{latest_checkpoint}")
        print("提示：如果要重新开始训练，请手动删除旧的 checkpoint 目录")
        print(f"自动从 checkpoint 恢复...")
    
    contrastive_trainer.train(resume_from_checkpoint=resume_from_checkpoint)

    # 保存微调后的编码器
    print("保存微调后的 BERT 编码器...")
    sentence_model.save_pretrained(ENCODER_TRAIN_CONFIG['output_dir'])
    sentence_tokenizer.save_pretrained(ENCODER_TRAIN_CONFIG['output_dir'])
    
    print("BERT 编码器微调完成！")
else:
    print("\n跳过 BERT 编码器微调（使用预训练模型）")

# ==================== 微调 GPT 生成器（如果启用） ====================
if MODEL_CONFIG.get('use_fine_tuned_generator', False):
    print("\n" + "=" * 60)
    print("正在微调 GPT 生成器...")
    print("=" * 60)
    
    # 使用解耦后的方法生成训练数据
    generator_training_texts = QADataGenerator.generate_generator_training_data(movies)
    print(f"准备了 {len(generator_training_texts)} 条生成训练数据")
    
    # 编码为 Token IDs
    generator_inputs = generator_tokenizer(
        generator_training_texts,
        truncation=True,
        padding=True,
        max_length=GENERATOR_TRAIN_CONFIG['max_length'],
        return_tensors='pt'
    )
    
    # 创建数据集
    class GeneratorDataset(Dataset):
        def __init__(self, encodings, pad_token_id):
            self.encodings = encodings
            self.pad_token_id = pad_token_id
        
        def __getitem__(self, idx):
            item = {key: val[idx] for key, val in self.encodings.items()}
            # 关键修改：为因果语言模型添加 labels
            item['labels'] = item['input_ids'].clone()
            # 将 padding 位置的 label 设为 -100，忽略这些位置的损失
            item['labels'][item['labels'] == self.pad_token_id] = -100
            return item
        
        def __len__(self):
            return self.encodings.input_ids.shape[0]
    
    generator_dataset = GeneratorDataset(generator_inputs, generator_tokenizer.pad_token_id)

    # 从配置文件读取生成器训练参数
    generator_training_args = TrainingArguments(
        output_dir=GENERATOR_TRAIN_CONFIG['output_dir'],
        num_train_epochs=GENERATOR_TRAIN_CONFIG['num_train_epochs'],
        per_device_train_batch_size=GENERATOR_TRAIN_CONFIG['per_device_train_batch_size'],
        learning_rate=GENERATOR_TRAIN_CONFIG['learning_rate'],
        weight_decay=GENERATOR_TRAIN_CONFIG['weight_decay'],
        warmup_ratio=GENERATOR_TRAIN_CONFIG['warmup_ratio'],
        logging_steps=GENERATOR_TRAIN_CONFIG['logging_steps'],
        save_steps=GENERATOR_TRAIN_CONFIG['save_steps'],
        save_total_limit=GENERATOR_TRAIN_CONFIG['save_total_limit'],
        fp16=GENERATOR_TRAIN_CONFIG.get('fp16', False),
        gradient_accumulation_steps=GENERATOR_TRAIN_CONFIG.get('gradient_accumulation_steps', 1),
        save_strategy=GENERATOR_TRAIN_CONFIG.get('save_strategy', 'steps'),
        load_best_model_at_end=GENERATOR_TRAIN_CONFIG.get('load_best_model_at_end', False),
    )
    
    # 创建 Trainer
    generator_trainer = Trainer(
        model=generator_model,
        args=generator_training_args,
        train_dataset=generator_dataset,
    )
    
    # 检查是否存在 checkpoint 并恢复训练
    gen_checkpoints = [d for d in os.listdir(GENERATOR_TRAIN_CONFIG['output_dir']) 
                       if d.startswith('checkpoint') and os.path.isdir(os.path.join(GENERATOR_TRAIN_CONFIG['output_dir'], d))]
    
    gen_resume_from_checkpoint = None
    if gen_checkpoints:
        print(f"\n发现 {len(gen_checkpoints)} 个已保存的 checkpoint:")
        print(f"  - {', '.join(sorted(gen_checkpoints))}")
        
        latest_gen_checkpoint = sorted(gen_checkpoints)[-1]
        gen_resume_from_checkpoint = os.path.join(GENERATOR_TRAIN_CONFIG['output_dir'], latest_gen_checkpoint)
        print(f"\n⚠️  检测到未完成的训练，将从 checkpoint 恢复：{latest_gen_checkpoint}")
        print("提示：如果要重新开始训练，请手动删除旧的 checkpoint 目录")
        print(f"自动从 checkpoint 恢复...")
    
    # 开始微调（Trainer.train() 会自动设置模型为 train 模式）
    generator_trainer.train(resume_from_checkpoint=gen_resume_from_checkpoint)

    # 保存微调后的生成器
    print("保存微调后的 GPT 生成器...")
    generator_model.save_pretrained(GENERATOR_TRAIN_CONFIG['output_dir'])
    generator_tokenizer.save_pretrained(GENERATOR_TRAIN_CONFIG['output_dir'])
    
    print("GPT 生成器微调完成！")
else:
    print("\n跳过 GPT 生成器微调（使用预训练模型）")

# ==================== 使用微调后的编码器重新编码所有问题（如果可用） ====================
print("\n" + "=" * 60)
print("正在构建索引...")
print("=" * 60)

# 直接使用内存中的模型（已在前面加载或微调）
enc_model = sentence_model
enc_tokenizer = sentence_tokenizer

# 设置设备
device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
enc_model.to(device)
print(f"\n✓ 编码器已加载到：{device}")

if torch.cuda.is_available():
    print(f"  GPU: {torch.cuda.get_device_name(0)}")

# 显示使用的模型信息
if MODEL_CONFIG.get('use_fine_tuned_encoder', False) and os.path.exists(ENCODER_TRAIN_CONFIG['output_dir']):
    print("使用微调后的编码器构建索引...")
else:
    print("使用预训练编码器构建索引...")

# 显示编码配置
print(f"\n编码配置:")
print(f"  - 问答对数量：{len(questions)}")
print(f"  - Batch size: 64 (GPU 加速)")
print(f"  - 最大长度：{ENCODER_TRAIN_CONFIG['max_length']} tokens")

# 估算编码时间
if torch.cuda.is_available():
    samples_per_second = 25
    estimated_minutes = len(questions) / samples_per_second / 60
    print(f"  - 预计用时：{estimated_minutes:.1f} 分钟 (GPU 模式)")
else:
    print(f"  - 预计用时：5-10 分钟 (CPU 模式)")

print("\n开始批量编码...")
print("-" * 60)

# 使用改进的批量编码方式（替代 QADataGenerator.encode_questions 的单条编码）
enc_model.eval()

# 批量编码配置（从配置文件读取）
inference_batch_size = ENCODER_TRAIN_CONFIG.get('inference_batch_size', 64)
all_embeddings = []

start_time = time.time()

# 优化：先分词，但不立即移到设备，分批处理
all_encodings = enc_tokenizer(
    questions, 
    padding=True, 
    truncation=True, 
    max_length=ENCODER_TRAIN_CONFIG['max_length'], 
    return_tensors='pt'
)

total_batches = (len(questions) + inference_batch_size - 1) // inference_batch_size

# 分批处理（带进度显示）
for batch_idx in range(total_batches):
    start_idx = batch_idx * inference_batch_size
    end_idx = min((batch_idx + 1) * inference_batch_size, len(questions))
    
    # 只在需要时才将当前批次移到设备
    batch_inputs = {k: v[start_idx:end_idx].to(device) for k, v in all_encodings.items()}
    
    with torch.no_grad():
        outputs = enc_model(**batch_inputs)
        batch_embeddings = outputs.last_hidden_state.mean(dim=1)
        all_embeddings.append(batch_embeddings.cpu().numpy())
    
    # 显示进度
    progress = ((batch_idx + 1) / total_batches) * 100
    elapsed = time.time() - start_time
    remaining = (total_batches - batch_idx - 1) * elapsed / (batch_idx + 1)
    
    if batch_idx % 10 == 0 or batch_idx == total_batches - 1:
        print(f"进度：{progress:.1f}% ({end_idx}/{len(questions)}) - "
              f"已用：{elapsed:.0f}s - 剩余：{remaining:.0f}s")
    
    # 可选：清理 CUDA 缓存（如果显存紧张）
    if torch.cuda.is_available() and batch_idx % 50 == 0:
        torch.cuda.empty_cache()

fine_tuned_question_embeddings = np.vstack(all_embeddings)

total_time = time.time() - start_time
print(f"\n✓ 编码完成！总用时：{total_time:.1f}秒 ({total_time/60:.1f}分钟)")
print(f"平均速度：{len(questions)/total_time:.1f} 样本/秒")

# 不再使用 rag_models，所有东西都放到 fine_tuned_models
output_dir = MODEL_CONFIG['fine_tuned_models_root']
os.makedirs(output_dir, exist_ok=True)

# 保存索引文件到 fine_tuned_models（精简版，只保留必要字段）
index_data = {
    'question_embeddings': fine_tuned_question_embeddings,
    'questions': questions,
    'answers': answers,
    'metadata': {
        'movie_count': len(movies),
        'qa_pair_count': len(questions),
        'encoder_used': ENCODER_TRAIN_CONFIG['output_dir'] if MODEL_CONFIG.get('use_fine_tuned_encoder', False) else 'pre-trained',
        'training_date': time.strftime('%Y-%m-%d %H:%M:%S'),
        'movie_titles': [movie['title'] for movie in movies],  # 只保留标题
        'movie_ids': [movie.get('id', i) for i, movie in enumerate(movies)]  # 只保留 ID
    }
}

index_path = MODEL_CONFIG.get('index_path', f"{output_dir}/index.pkl")
with open(index_path, 'wb') as f:
    pickle.dump(index_data, f)

print(f"\n所有文件已保存到：{output_dir}")
print(f"  - 编码器：{output_dir}/encoder (如果微调)")
print(f"  - 生成器：{output_dir}/generator (如果微调)")
print(f"  - 索引：{index_path}")
print("训练完成！")
