<template>
  <Header />
  <main class="list-shell">
    <div class="content-grid">
      <section>
        <FrontNavCard />
        <div class="hero-card">
          <span class="hero-card__eyebrow">Archive</span>
          <h1>时间归档</h1>
          <p>按月份回看当前博客的内容更新节奏。</p>
        </div>

        <div class="archive-group" v-for="group in archives" :key="group.month">
          <div class="archive-group__month">{{ group.month }}</div>
          <button v-for="article in group.articles" :key="article.id" class="archive-item" @click="router.push(`/u/${currentBlogStore.username}/article/${article.id}`)">
            <span>{{ article.title }}</span>
            <small>{{ article.createDate }}</small>
          </button>
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
import { onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import Header from '@/layouts/frontend/components/Header.vue'
import Footer from '@/layouts/frontend/components/Footer.vue'
import FrontNavCard from '@/layouts/frontend/components/FrontNavCard.vue'
import UserInfoCard from '@/layouts/frontend/components/UserInfoCard.vue'
import CategoryListCard from '@/layouts/frontend/components/CategoryListCard.vue'
import TagListCard from '@/layouts/frontend/components/TagListCard.vue'
import ScrollToTopButton from '@/layouts/frontend/components/ScrollToTopButton.vue'
import { getArchivePageList } from '@/api/fronted/archive'
import { getCachedValue, setCachedValue } from '@/composables/frontendCache'
import { useCurrentBlogStore } from '@/stores/currentBlog'

const route = useRoute()
const router = useRouter()
const currentBlogStore = useCurrentBlogStore()
const archives = ref([])
const ARCHIVE_PAGE_CACHE_MS = 5 * 60 * 1000

const loadArchives = () => {
  const username = currentBlogStore.username
  if (!username) {
    archives.value = []
    return
  }

  const cacheKey = `archive-page:${username}`
  const cached = getCachedValue(cacheKey, ARCHIVE_PAGE_CACHE_MS)
  if (cached) {
    archives.value = cached
  }

  getArchivePageList({
    current: 1,
    size: 50,
    blogUsername: username,
  }).then((res) => {
    if (res.success) {
      archives.value = res.data || []
      setCachedValue(cacheKey, archives.value)
    }
  })
}

onMounted(loadArchives)
watch(() => route.params.username, loadArchives)
</script>

<style scoped>
.list-shell { max-width: 1180px; margin: 0 auto; padding: 24px 20px 48px; }
.content-grid { display: grid; grid-template-columns: minmax(0, 1fr) 300px; gap: 18px; }
.hero-card,.archive-group{ border-radius: 28px; border: 1px solid rgba(229,231,235,.95); background: rgba(255,255,255,.92); box-shadow: 0 18px 44px rgba(15,23,42,.05); }
.hero-card{ padding:28px; margin-bottom:16px; }
.hero-card__eyebrow{ color:#2563eb; font-size:12px; font-weight:600; letter-spacing:.08em; text-transform:uppercase; }
.hero-card h1{ margin:10px 0 12px; font-size:clamp(32px,5vw,46px); }
.archive-group{ padding:22px; margin-bottom:16px; }
.archive-group__month{ margin-bottom:14px; color:#111827; font-size:24px; font-weight:700; letter-spacing:-.04em; }
.archive-item{ display:flex; justify-content:space-between; align-items:center; width:100%; padding:14px 0; border-top:1px solid rgba(229,231,235,.8); background:transparent; text-align:left; }
.archive-item:first-of-type{ border-top:none; }
.archive-item span{ color:#111827; font-weight:600; }
.archive-item small{ color:#9ca3af; }
.side-column { position: sticky; top: 96px; align-self: start; }
@media (max-width: 1024px) { .content-grid{ grid-template-columns:1fr; } .side-column{ position:static; } }
</style>
