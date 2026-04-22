<template>
  <div class="app-container chat-container">
    <el-card class="chat-card">
      <div slot="header" class="chat-header">
        <span class="title">🎬 电影知识问答</span>
        <el-tag :type="serviceStatus === 'online' ? 'success' : 'danger'" size="small">
          {{ serviceStatus === 'online' ? '服务在线' : '服务离线' }}
        </el-tag>
      </div>

      <div class="chat-messages" ref="messageContainer">
        <div v-for="(msg, index) in messages" :key="index"
             :class="['message', msg.type === 'user' ? 'user-message' : 'bot-message']">
          <div class="message-avatar">
            <i :class="msg.type === 'user' ? 'el-icon-user' : 'el-icon-s-custom'"></i>
          </div>
          <div class="message-content">
            <div class="message-text">{{ msg.content }}</div>
            <div class="message-time">{{ msg.time }}</div>
          </div>
        </div>

        <div v-if="loading" class="message bot-message">
          <div class="message-avatar">
            <i class="el-icon-s-custom"></i>
          </div>
          <div class="message-content">
            <div class="message-text">
              <i class="el-icon-loading"></i> 正在思考...
            </div>
          </div>
        </div>
      </div>

      <div class="chat-input">
        <el-input
          v-model="currentQuestion"
          type="textarea"
          :rows="3"
          placeholder="请输入您的问题，例如：肖申克的救赎的导演是谁？"
          @keyup.enter.native="handleSend"
          :disabled="loading"
        >
        </el-input>
        <el-button
          type="primary"
          @click="handleSend"
          :loading="loading"
          :disabled="!currentQuestion.trim()"
          icon="el-icon-s-promotion"
        >
          发送
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import { askQuestion, healthCheck } from "@/api/movie/chat";

export default {
  name: "MovieChat",
  data() {
    return {
      messages: [
        {
          type: 'bot',
          content: '你好！我是电影知识助手，可以问我关于电影的问题哦~',
          time: this.getCurrentTime()
        }
      ],
      currentQuestion: '',
      loading: false,
      serviceStatus: 'offline'
    };
  },
  created() {
    this.loadChatState();
    this.checkServiceHealth();
  },
  mounted() {
    this.$nextTick(this.scrollToBottom);
  },
  methods: {
    async handleSend() {
      if (!this.currentQuestion.trim() || this.loading) {
        return;
      }

      const question = this.currentQuestion.trim();

      this.messages.push({
        type: 'user',
        content: question,
        time: this.getCurrentTime()
      });

      this.currentQuestion = '';
      this.loading = true;
      this.scrollToBottom();

      try {
        const response = await askQuestion(question);

        if (response.code === 200) {
          const answer = response.data.answer || '抱歉，我没有找到答案';
          this.messages.push({
            type: 'bot',
            content: answer,
            time: this.getCurrentTime()
          });
        } else {
          this.messages.push({
            type: 'bot',
            content: response.msg || '回答失败，请稍后重试',
            time: this.getCurrentTime()
          });
        }
      } catch (error) {
        this.messages.push({
          type: 'bot',
          content: '网络错误，请检查服务是否正常运行',
          time: this.getCurrentTime()
        });
      } finally {
        this.loading = false;
        this.scrollToBottom();
      }
    },

    async checkServiceHealth() {
      try {
        const response = await healthCheck();
        if (response.code === 200 && response.data.status === 'ok') {
          this.serviceStatus = 'online';
        }
      } catch (error) {
        this.serviceStatus = 'offline';
      }
    },

    loadChatState() {
      const saved = window.localStorage.getItem('movieChatState');
      if (!saved) {
        return;
      }
      try {
        const parsed = JSON.parse(saved);
        if (parsed.messages && Array.isArray(parsed.messages)) {
          this.messages = parsed.messages;
        }
        if (typeof parsed.currentQuestion === 'string') {
          this.currentQuestion = parsed.currentQuestion;
        }
      } catch (error) {
        console.warn('加载聊天记录失败', error);
      }
    },

    saveChatState() {
      const payload = {
        messages: this.messages,
        currentQuestion: this.currentQuestion
      };
      window.localStorage.setItem('movieChatState', JSON.stringify(payload));
    },

    getCurrentTime() {
      const now = new Date();
      const hours = String(now.getHours()).padStart(2, '0');
      const minutes = String(now.getMinutes()).padStart(2, '0');
      return `${hours}:${minutes}`;
    },

    scrollToBottom() {
      this.$nextTick(() => {
        const container = this.$refs.messageContainer;
        if (container) {
          container.scrollTop = container.scrollHeight;
        }
      });
    }
  },
  watch: {
    messages: {
      handler() {
        this.saveChatState();
      },
      deep: true
    },
    currentQuestion() {
      this.saveChatState();
    }
  }
};
</script>

<style scoped>
.chat-container {
  height: calc(100vh - 84px);
  display: flex;
  flex-direction: column;
}

.chat-card {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.chat-card ::v-deep .el-card__body {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  padding: 0;
}

.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chat-header .title {
  font-size: 18px;
  font-weight: bold;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background-color: #f5f7fa;
}

.message {
  display: flex;
  margin-bottom: 20px;
  animation: fadeIn 0.3s ease-in;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.user-message {
  flex-direction: row-reverse;
}

.bot-message {
  flex-direction: row;
}

.message-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: #409EFF;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  flex-shrink: 0;
}

.user-message .message-avatar {
  background-color: #67C23A;
}

.message-content {
  max-width: 60%;
  margin: 0 15px;
}

.message-text {
  background-color: white;
  padding: 12px 16px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  line-height: 1.6;
  word-wrap: break-word;
}

.user-message .message-text {
  background-color: #67C23A;
  color: white;
}

.message-time {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
  text-align: right;
}

.chat-input {
  padding: 20px;
  border-top: 1px solid #EBEEF5;
  background-color: white;
}

.chat-input .el-textarea {
  margin-bottom: 10px;
}

.chat-input .el-button {
  width: 100%;
}
</style>
