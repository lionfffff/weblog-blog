<template>
  <div class="dashboard-page">
    <section class="dashboard-hero">
      <div class="dashboard-hero-copy">
        <span class="dashboard-eyebrow">总览</span>
        <h1 class="dashboard-title">仪表盘</h1>
        <p class="dashboard-subtitle">用更安静、克制的方式查看内容规模与最近趋势，进入后台后先看到最重要的几项数据。</p>
      </div>
      <div class="dashboard-hero-note">数据来源：文章、分类、标签与访问统计</div>
    </section>

    <section class="metric-grid">
      <article class="metric-card">
        <div class="metric-head">
          <span class="metric-icon metric-icon-article">
            <el-icon><Document /></el-icon>
          </span>
          <span class="metric-label">文章</span>
        </div>
        <CountTo :value="articleTotalCount" customClass="metric-value" />
        <p class="metric-note">当前已发布内容总量</p>
      </article>

      <article class="metric-card">
        <div class="metric-head">
          <span class="metric-icon metric-icon-category">
            <el-icon><FolderOpened /></el-icon>
          </span>
          <span class="metric-label">分类</span>
        </div>
        <CountTo :value="categoryTotalCount" customClass="metric-value" />
        <p class="metric-note">已整理好的栏目结构</p>
      </article>

      <article class="metric-card">
        <div class="metric-head">
          <span class="metric-icon metric-icon-tag">
            <el-icon><CollectionTag /></el-icon>
          </span>
          <span class="metric-label">标签</span>
        </div>
        <CountTo :value="tagTotalCount" customClass="metric-value" />
        <p class="metric-note">文章检索与聚合入口</p>
      </article>

      <article class="metric-card">
        <div class="metric-head">
          <span class="metric-icon metric-icon-pv">
            <el-icon><TrendCharts /></el-icon>
          </span>
          <span class="metric-label">总访问量</span>
        </div>
        <CountTo :value="pvTotalCount" customClass="metric-value" />
        <p class="metric-note">累计浏览趋势总览</p>
      </article>
    </section>

    <section class="chart-grid">
      <article class="dashboard-panel">
        <div class="panel-header">
          <h2>半年发布热度</h2>
          <p>观察最近半年文章发布密度，快速判断内容节奏是否稳定。</p>
        </div>
        <div class="panel-content">
          <ArticlePublishCalendar :value="articlePublishInfo" />
        </div>
      </article>

      <article class="dashboard-panel">
        <div class="panel-header">
          <h2>最近一周访问趋势</h2>
          <p>关注短周期 PV 波动，方便判断文章更新后的反馈情况。</p>
        </div>
        <div class="panel-content">
          <ArticlePVLineChat :value="articlePVInfo" />
        </div>
      </article>
    </section>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { CollectionTag, Document, FolderOpened, TrendCharts } from '@element-plus/icons-vue'
import { getArticlePVStatisticsInfo, getBaseStatisticsInfo, getPublishArticleStatisticsInfo } from '@/api/admin/dashboard'
import CountTo from '@/components/CountTo.vue'
import ArticlePublishCalendar from '@/components/ArticlePublishCalendar.vue'
import ArticlePVLineChat from '@/components/ArticlePVLineChat.vue'

const articlePublishInfo = ref({})
const articleTotalCount = ref(0)
const categoryTotalCount = ref(0)
const tagTotalCount = ref(0)
const pvTotalCount = ref(0)
const articlePVInfo = ref({})

getPublishArticleStatisticsInfo().then((res) => {
  if (res.success) {
    articlePublishInfo.value = res.data
  }
})

getBaseStatisticsInfo().then((res) => {
  if (res.success) {
    articleTotalCount.value = res.data.articleTotalCount
    categoryTotalCount.value = res.data.categoryTotalCount
    tagTotalCount.value = res.data.tagTotalCount
    pvTotalCount.value = res.data.pvTotalCount
  }
})

getArticlePVStatisticsInfo().then((res) => {
  if (res.success) {
    articlePVInfo.value = res.data
  }
})
</script>

<style scoped>
.dashboard-page {
  display: flex;
  flex-direction: column;
  gap: 24px;
  color: #0f172a;
}

.dashboard-hero {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  gap: 18px;
}

.dashboard-eyebrow {
  display: inline-block;
  margin-bottom: 10px;
  color: #64748b;
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 0.14em;
  text-transform: uppercase;
}

.dashboard-title {
  margin: 0;
  font-size: 34px;
  line-height: 1.1;
  font-weight: 700;
}

.dashboard-subtitle {
  max-width: 760px;
  margin: 10px 0 0;
  color: #64748b;
  font-size: 15px;
  line-height: 1.7;
}

.dashboard-hero-note {
  padding: 14px 18px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.86);
  box-shadow: 0 16px 36px rgba(15, 23, 42, 0.06);
  color: #475569;
  font-size: 13px;
  white-space: nowrap;
}

.metric-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 18px;
}

.metric-card,
.dashboard-panel {
  border: 1px solid rgba(148, 163, 184, 0.2);
  border-radius: 28px;
  background: rgba(255, 255, 255, 0.88);
  box-shadow: 0 24px 60px rgba(15, 23, 42, 0.06);
  backdrop-filter: blur(22px);
}

.metric-card {
  padding: 22px 24px;
}

.metric-head {
  display: flex;
  align-items: center;
  gap: 12px;
}

.metric-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 44px;
  height: 44px;
  border-radius: 16px;
  font-size: 18px;
}

.metric-icon-article {
  background: rgba(37, 99, 235, 0.1);
  color: #1d4ed8;
}

.metric-icon-category {
  background: rgba(15, 118, 110, 0.12);
  color: #0f766e;
}

.metric-icon-tag {
  background: rgba(124, 58, 237, 0.12);
  color: #7c3aed;
}

.metric-icon-pv {
  background: rgba(234, 88, 12, 0.12);
  color: #ea580c;
}

.metric-label {
  color: #334155;
  font-size: 15px;
  font-weight: 600;
}

.metric-card :deep(.metric-value) {
  margin-top: 18px;
  font-size: 36px;
  line-height: 1;
  font-weight: 700;
  color: #0f172a;
}

.metric-note {
  margin: 14px 0 0;
  color: #64748b;
  font-size: 13px;
  line-height: 1.7;
}

.chart-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 20px;
}

.dashboard-panel {
  padding: 24px 26px;
}

.panel-header h2 {
  margin: 0;
  font-size: 22px;
  font-weight: 700;
}

.panel-header p {
  margin: 8px 0 0;
  color: #64748b;
  font-size: 14px;
  line-height: 1.7;
}

.panel-content {
  margin-top: 18px;
  min-width: 0;
}

@media (max-width: 1200px) {
  .metric-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 900px) {
  .chart-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .dashboard-hero {
    flex-direction: column;
    align-items: flex-start;
  }

  .dashboard-hero-note {
    white-space: normal;
  }

  .metric-grid {
    grid-template-columns: 1fr;
  }

  .dashboard-panel,
  .metric-card {
    padding: 20px;
  }
}
</style>
