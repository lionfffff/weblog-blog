<template>
  <Header />
  <main class="stats-shell">
    <div class="content-grid">
      <section>
        <FrontNavCard />
        <div class="hero-card">
          <span class="hero-card__eyebrow">Statistics</span>
          <h1>浏览统计</h1>
          <p>查看当前博客最近 7 天的浏览量变化和累计浏览总数。</p>
        </div>

        <div class="summary-card">
          <div>
            <span class="summary-card__label">总浏览量</span>
            <strong>{{ pvTotalCount }}</strong>
          </div>
          <p>统计口径与首页一致，按当前博客文章阅读次数累计。</p>
        </div>

        <div class="chart-card">
          <div class="chart-card__head">
            <div>
              <h2>最近 7 天趋势</h2>
              <p>按天查看当前博客的浏览量变化。</p>
            </div>
          </div>
          <ArticlePVLineChat :value="pvTrendInfo" />
        </div>

        <div class="daily-card">
          <div class="daily-card__head">
            <h2>每日明细</h2>
            <p>对应每天的浏览量记录。</p>
          </div>

          <div class="daily-list">
            <div v-for="item in dailyRows" :key="item.date" class="daily-item">
              <span>{{ item.date }}</span>
              <strong>{{ item.count }}</strong>
            </div>
          </div>
        </div>
      </section>

      <aside class="side-column">
        <UserInfoCard />
        <CategoryListCard />
        <TagListCard />
      </aside>
    </div>
  </main>
  <ScrollToTopButton />
  <Footer />
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getStatisticsInfo, getStatisticsPVTrend } from '@/api/fronted/statistics'
import ArticlePVLineChat from '@/components/ArticlePVLineChat.vue'
import ScrollToTopButton from '@/layouts/frontend/components/ScrollToTopButton.vue'
import CategoryListCard from '@/layouts/frontend/components/CategoryListCard.vue'
import Footer from '@/layouts/frontend/components/Footer.vue'
import FrontNavCard from '@/layouts/frontend/components/FrontNavCard.vue'
import Header from '@/layouts/frontend/components/Header.vue'
import TagListCard from '@/layouts/frontend/components/TagListCard.vue'
import UserInfoCard from '@/layouts/frontend/components/UserInfoCard.vue'
import { getCachedValue, setCachedValue } from '@/composables/frontendCache'
import { useCurrentBlogStore } from '@/stores/currentBlog'

const route = useRoute()
const currentBlogStore = useCurrentBlogStore()
const pvTotalCount = ref(0)
const pvTrendInfo = ref({
  pvDates: [],
  pvCounts: [],
})
const STATISTICS_PAGE_CACHE_MS = 5 * 60 * 1000

const loadStatistics = () => {
  const username = currentBlogStore.username
  if (!username) {
    pvTotalCount.value = 0
    pvTrendInfo.value = { pvDates: [], pvCounts: [] }
    return
  }

  const infoCacheKey = `statistics-page-info:${username}`
  const trendCacheKey = `statistics-page-trend:${username}`
  const cachedInfo = getCachedValue(infoCacheKey, STATISTICS_PAGE_CACHE_MS)
  const cachedTrend = getCachedValue(trendCacheKey, STATISTICS_PAGE_CACHE_MS)

  if (cachedInfo) {
    pvTotalCount.value = cachedInfo.pvTotalCount || 0
  }

  if (cachedTrend) {
    pvTrendInfo.value = cachedTrend
    if (typeof cachedTrend.pvTotalCount === 'number') {
      pvTotalCount.value = cachedTrend.pvTotalCount
    }
  }

  getStatisticsInfo({
    blogUsername: username,
  }).then((res) => {
    if (res.success) {
      pvTotalCount.value = res.data?.pvTotalCount || 0
      setCachedValue(infoCacheKey, res.data || {})
    }
  })

  getStatisticsPVTrend({
    blogUsername: username,
  }).then((res) => {
    if (res.success) {
      pvTrendInfo.value = {
        pvDates: res.data?.pvDates || [],
        pvCounts: res.data?.pvCounts || [],
        pvTotalCount: res.data?.pvTotalCount || 0,
      }
      if (typeof res.data?.pvTotalCount === 'number') {
        pvTotalCount.value = res.data.pvTotalCount
      }
      setCachedValue(trendCacheKey, pvTrendInfo.value)
    }
  })
}

const dailyRows = computed(() =>
  (pvTrendInfo.value.pvDates || []).map((date, index) => ({
    date,
    count: pvTrendInfo.value.pvCounts?.[index] || 0,
  })),
)

onMounted(loadStatistics)
watch(() => route.params.username, loadStatistics)
</script>

<style scoped>
.stats-shell { max-width: 1180px; margin: 0 auto; padding: 24px 20px 48px; }
.content-grid { display: grid; grid-template-columns: minmax(0, 1fr) 300px; gap: 18px; }
.hero-card,.summary-card,.chart-card,.daily-card { border-radius: 28px; border: 1px solid rgba(229,231,235,.95); background: rgba(255,255,255,.92); box-shadow: 0 18px 44px rgba(15,23,42,.05); }
.hero-card { padding: 28px; margin-bottom: 16px; }
.hero-card__eyebrow { color:#2563eb; font-size:12px; font-weight:600; letter-spacing:.08em; text-transform:uppercase; }
.hero-card h1 { margin:10px 0 12px; font-size:clamp(32px,5vw,46px); }
.hero-card p { margin:0; color:#6b7280; line-height:1.8; }
.summary-card { display:flex; justify-content:space-between; gap:18px; align-items:flex-end; padding:24px 28px; margin-bottom:16px; }
.summary-card__label { display:inline-flex; color:#6b7280; font-size:13px; font-weight:700; letter-spacing:.08em; text-transform:uppercase; }
.summary-card strong { display:block; margin-top:10px; color:#111827; font-size:48px; line-height:1; letter-spacing:-.06em; }
.summary-card p { max-width:360px; margin:0; color:#6b7280; line-height:1.8; }
.chart-card,.daily-card { padding:24px 28px; margin-bottom:16px; }
.chart-card__head,.daily-card__head { margin-bottom:18px; }
.chart-card__head h2,.daily-card__head h2 { margin:0; color:#111827; font-size:22px; }
.chart-card__head p,.daily-card__head p { margin:8px 0 0; color:#6b7280; line-height:1.7; }
.daily-list { display:grid; gap:12px; }
.daily-item { display:flex; justify-content:space-between; align-items:center; padding:16px 0; border-top:1px solid rgba(229,231,235,.8); }
.daily-item:first-of-type { border-top:none; }
.daily-item span { color:#6b7280; font-weight:600; }
.daily-item strong { color:#111827; font-size:20px; }
.side-column { position: sticky; top: 96px; align-self: start; }
@media (max-width: 1024px) { .content-grid { grid-template-columns:1fr; } .side-column { position:static; } .summary-card { flex-direction:column; align-items:flex-start; } }
</style>
