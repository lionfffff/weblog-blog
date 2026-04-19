<template>
  <Header />
  <main class="list-shell">
    <div class="content-grid">
      <section>
        <FrontNavCard />
        <div class="hero-card">
          <span class="hero-card__eyebrow">Categories</span>
          <h1>分类目录</h1>
          <p>浏览当前管理员博客下的全部分类内容。</p>
        </div>

        <div class="simple-grid">
          <button
            v-for="category in categories"
            :key="category.id"
            class="simple-card"
            @click="router.push({ path: `/u/${currentBlogStore.username}/category/article/list`, query: { id: category.id, name: category.name } })"
          >
            <h2>{{ category.name }}</h2>
            <p>{{ category.articlesTotal || 0 }} 篇文章</p>
          </button>
        </div>
      </section>

      <aside class="side-column">
        <UserInfoCard />
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
import TagListCard from '@/layouts/frontend/components/TagListCard.vue'
import ScrollToTopButton from '@/layouts/frontend/components/ScrollToTopButton.vue'
import { getCategoryList } from '@/api/fronted/category'
import { getCachedValue, setCachedValue } from '@/composables/frontendCache'
import { useCurrentBlogStore } from '@/stores/currentBlog'

const route = useRoute()
const router = useRouter()
const currentBlogStore = useCurrentBlogStore()
const categories = ref([])
const CATEGORY_PAGE_CACHE_MS = 5 * 60 * 1000

const loadCategories = () => {
  const username = currentBlogStore.username
  if (!username) {
    categories.value = []
    return
  }

  const cacheKey = `category-page:${username}`
  const cached = getCachedValue(cacheKey, CATEGORY_PAGE_CACHE_MS)
  if (cached) {
    categories.value = cached
  }

  getCategoryList({
    blogUsername: username,
  }).then((res) => {
    if (res.success) {
      categories.value = res.data || []
      setCachedValue(cacheKey, categories.value)
    }
  })
}

onMounted(loadCategories)
watch(() => route.params.username, loadCategories)
</script>

<style scoped>
.list-shell {
  max-width: 1180px;
  margin: 0 auto;
  padding: 24px 20px 48px;
}
.content-grid {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 300px;
  gap: 18px;
}
.hero-card,
.simple-card {
  border-radius: 28px;
  border: 1px solid rgba(229, 231, 235, 0.95);
  background: rgba(255, 255, 255, 0.92);
  box-shadow: 0 18px 44px rgba(15, 23, 42, 0.05);
}
.hero-card {
  padding: 28px;
  margin-bottom: 16px;
}
.hero-card__eyebrow {
  color: #2563eb;
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 0.08em;
  text-transform: uppercase;
}
.hero-card h1 {
  margin: 10px 0 12px;
  font-size: clamp(32px, 5vw, 46px);
}
.simple-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 16px;
}
.simple-card {
  padding: 24px;
  text-align: left;
}
.simple-card h2 {
  margin: 0 0 12px;
  color: #111827;
  font-size: 22px;
}
.simple-card p {
  margin: 0;
  color: #6b7280;
}
.side-column {
  position: sticky;
  top: 96px;
  align-self: start;
}
@media (max-width: 1024px) {
  .content-grid {
    grid-template-columns: 1fr;
  }
  .side-column {
    position: static;
  }
}
@media (max-width: 768px) {
  .simple-grid {
    grid-template-columns: 1fr;
  }
}
</style>
