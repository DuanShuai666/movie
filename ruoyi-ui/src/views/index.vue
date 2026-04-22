<template>
  <div class="app-container home">
    <section class="bert-hero">
      <div class="hero-left">
        <div class="eyebrow">🎬 基于BERT微调和稠密检索的电影知识问答系统</div>
        <h1>开启你的电影知识探索之旅</h1>
        <p>融合BERT微调模型与稠密检索技术，构建电影知识问答系统，支持导演、演员、剧情、奖项、台词等多维度精准问答。</p>
        <div class="hero-actions">
          <el-button type="primary" size="large" @click="goChat">进入问答</el-button>
          <el-button type="primary" size="large" plain class="feature-button" @click="scrollToSection('features')">了解功能</el-button>
        </div>
        <div class="hero-metrics">
          <div><span>模型引擎</span><strong>BERT微调 + 稠密检索</strong></div>
          <div><span>问答覆盖</span><strong>剧情 / 导演 / 演员</strong></div>
          <div><span>版本</span><strong>v{{ version }}</strong></div>
        </div>
      </div>
      <div class="hero-right">
        <div class="chat-preview">
          <div class="chat-header">示例问答</div>
          <div class="chat-message bot">
            <span>你好，我可以回答电影相关问题，例如导演、演员、剧情、奖项等。</span>
          </div>
          <div class="chat-message user">
            <span>《泰坦尼克号》的导演是谁？</span>
          </div>
          <div class="chat-message bot">
            <span>导演是詹姆斯·卡梅隆，1997年上映。</span>
          </div>
          <div class="chat-cta">点击“进入问答”开始提问电影问题。</div>
        </div>
      </div>
    </section>

    <section class="bert-features" ref="features">
      <div class="section-title">系统亮点</div>
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="8">
          <el-card class="feature-card" shadow="hover">
            <div class="feature-icon">🧠</div>
            <h3>BERT微调模型</h3>
            <p>基于电影领域数据微调BERT模型，精准理解电影相关问题的语义特征。</p>
          </el-card>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8">
          <el-card class="feature-card" shadow="hover">
            <div class="feature-icon">🔍</div>
            <h3>稠密检索技术</h3>
            <p>采用稠密向量检索技术，从海量电影知识中快速定位相关信息。</p>
          </el-card>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8">
          <el-card class="feature-card" shadow="hover">
            <div class="feature-icon">🎬</div>
            <h3>电影知识库</h3>
            <p>覆盖电影剧情、演员、导演、台词、获奖记录等核心内容。</p>
          </el-card>
        </el-col>
      </el-row>
    </section>

    <section class="bert-details">
      <el-row :gutter="20">
        <el-col :xs="24" :md="12">
          <el-card class="detail-card" shadow="never">
            <div class="detail-title">为何选择本系统</div>
            <ul>
              <li>BERT微调模型精准理解电影领域问题。</li>
              <li>稠密检索技术从海量知识中快速定位答案。</li>
              <li>面向电影爱好者、影评创作者与学习者。</li>
            </ul>
          </el-card>
        </el-col>
        <el-col :xs="24" :md="12">
          <el-card class="detail-card" shadow="never">
            <div class="detail-title">适用场景</div>
            <ul>
              <li>查询电影导演、主演、上映年份。</li>
              <li>了解电影剧情、经典台词与奖项信息。</li>
              <li>辅助影评写作与影视知识学习。</li>
            </ul>
          </el-card>
        </el-col>
      </el-row>
    </section>

  </div>
</template>

<script>
export default {
  name: "Index",
  data() {
    return {
      version: "1.0.0"
    };
  },
  methods: {
    goChat() {
      if (this.$router) {
        this.$router.push({ path: "/movie/chat" }).catch(() => {});
      } else {
        window.open("/movie/chat", "_blank");
      }
    },
    scrollToSection(refName) {
      const target = this.$refs[refName];
      if (target && target.scrollIntoView) {
        target.scrollIntoView({ behavior: "smooth", block: "start" });
      }
    }
  }
};
</script>

<style scoped>
.home {
  background: linear-gradient(180deg, #f8fbff 0%, #e7eff9 100%);
  color: #111827;
  min-height: calc(100vh - 84px);
  padding: 40px 30px;
}
.bert-hero {
  display: flex;
  flex-wrap: wrap;
  align-items: flex-start;
  justify-content: space-between;
  gap: 24px;
  padding: 40px;
  border-radius: 24px;
  background: rgba(255,255,255,0.95);
  box-shadow: 0 16px 36px rgba(15, 23, 42, 0.12);
  margin-bottom: 32px;
}
.hero-left {
  flex: 1 1 520px;
  max-width: 720px;
}
.eyebrow {
  display: inline-block;
  padding: 8px 16px;
  border-radius: 999px;
  border: 1px solid rgba(15, 23, 42, 0.15);
  color: #111827;
  margin-bottom: 20px;
  font-size: 14px;
}
.hero-left h1 {
  font-size: 44px;
  font-weight: 700;
  line-height: 1.1;
  margin-bottom: 20px;
}
.hero-left p {
  max-width: 620px;
  line-height: 2;
  color: rgba(17, 24, 39, 0.88);
  font-size: 16px;
  margin-bottom: 30px;
}
.hero-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  margin-bottom: 30px;
}
.hero-actions .feature-button {
  color: #111827;
  border-color: rgba(15, 23, 42, 0.18);
  background: rgba(248, 250, 252, 0.92);
}
.hero-actions .feature-button:hover {
  background: rgba(241, 245, 249, 0.96);
}
.hero-actions .el-button--primary {
  color: #111827 !important;
  background: #dbe5f2 !important;
  border-color: #b8c5dd !important;
}
.hero-actions .el-button--primary:hover {
  background: #c8d4e8 !important;
}
.hero-metrics {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}
.hero-metrics div {
  min-width: 150px;
  padding: 16px 20px;
  border-radius: 16px;
  background: #ffffff;
  border: 1px solid rgba(15, 23, 42, 0.08);
  color: #111827;
}
.hero-metrics span {
  display: block;
  color: rgba(17, 24, 39, 0.6);
  margin-bottom: 8px;
}
.hero-metrics strong {
  font-size: 18px;
}
.hero-right {
  flex: 0 0 360px;
  min-width: 320px;
}
.chat-preview {
  padding: 24px;
  border-radius: 24px;
  background: #ffffff;
  border: 1px solid rgba(15, 23, 42, 0.08);
}
.chat-header {
  font-size: 18px;
  font-weight: 700;
  margin-bottom: 20px;
}
.chat-message {
  display: flex;
  padding: 14px 18px;
  border-radius: 18px;
  margin-bottom: 14px;
  background: rgba(243, 244, 246, 0.95);
  color: #111827;
}
.chat-message.bot {
  justify-content: flex-start;
}
.chat-message.user {
  justify-content: flex-end;
  background: rgba(15, 23, 42, 0.07);
}
.chat-message span {
  max-width: 100%;
  line-height: 1.8;
}
.chat-cta {
  margin-top: 16px;
  padding: 16px;
  border-radius: 16px;
  color: #111827;
  background: rgba(241, 245, 249, 0.95);
  text-align: center;
}
.section-title {
  font-size: 28px;
  font-weight: 700;
  color: #111827;
  margin-bottom: 24px;
}
.feature-card {
  min-height: 220px;
  background: #ffffff;
  border: 1px solid rgba(15, 23, 42, 0.08);
  color: #111827;
}
.feature-icon {
  font-size: 28px;
  margin-bottom: 16px;
}
.feature-card h3 {
  margin-bottom: 12px;
  color: #111827;
}
.feature-card p {
  color: rgba(17, 24, 39, 0.8);
  line-height: 1.8;
}
.detail-card {
  background: #ffffff;
  border: 1px solid rgba(15, 23, 42, 0.08);
  color: #111827;
}
.detail-title {
  font-size: 20px;
  font-weight: 700;
  margin-bottom: 18px;
}
.detail-card ul {
  list-style: inside disc;
  color: rgba(17, 24, 39, 0.88);
  line-height: 2;
  padding-left: 20px;
}
.detail-card li {
  margin-bottom: 12px;
}
.el-button--success {
  color: #111827 !important;
}
@media (max-width: 900px) {
  .bert-hero {
    flex-direction: column;
  }
  .hero-left,
  .hero-right {
    max-width: 100%;
  }
}
</style>

