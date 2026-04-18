<template>
  <Header />

  <main class="page-shell">
    <div class="content-grid">
      <section class="main-column">
        <FrontNavCard />
        <div class="hero-card">
          <span class="hero-card__eyebrow">Blog Home</span>
          <h1>{{ blogSettingsStore.blogSettings.name || `${currentBlogStore.username} 的博客` }}</h1>
          <p>{{ blogSettingsStore.blogSettings.introduction || '欢迎来到这个管理员的独立博客。' }}</p>
        </div>

        <div class="article-grid">
          <article v-for="article in articles" :key="article.id" class="article-card">
            <img class="article-card__cover" :src="article.cover" />
            <div class="article-card__body">
              <div class="article-card__tags">
                <button
                  v-for="tag in article.tags"
                  :key="tag.id"
                  class="tag"
                  @click="router.push({ path: `/u/${currentBlogStore.username}/tag/article/list`, query: { id: tag.id, name: tag.name } })"
                >
                  {{ tag.name }}
                </button>
              </div>
              <h2 @click="router.push(`/u/${currentBlogStore.username}/article/${article.id}`)">{{ article.title }}</h2>
              <p>{{ article.summary }}</p>
              <div class="article-card__meta">
                <span>{{ article.createDate }}</span>
                <button @click="router.push({ path: `/u/${currentBlogStore.username}/category/article/list`, query: { id: article.category?.id, name: article.category?.name } })">
                  {{ article.category?.name }}
                </button>
              </div>
            </div>
          </article>
        </div>

        <div class="pager">
          <button :disabled="current <= 1" @click="getArticles(current - 1)">上一页</button>
          <span>{{ current }} / {{ pages || 1 }}</span>
          <button :disabled="current >= pages" @click="getArticles(current + 1)">下一页</button>
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
import { getArticlePageList } from '@/api/fronted/article'
import { useCurrentBlogStore } from '@/stores/currentBlog'
import { useBlogSettingsStore } from '@/stores/blogsettings'

const route = useRoute()
const router = useRouter()
const currentBlogStore = useCurrentBlogStore()
const blogSettingsStore = useBlogSettingsStore()
const articles = ref([])
const current = ref(1)
const size = ref(6)
const pages = ref(1)

const getArticles = (pageNo) => {
  if (pageNo < 1 || (pages.value && pageNo > pages.value)) return

  getArticlePageList({
    current: pageNo,
    size: size.value,
    blogUsername: currentBlogStore.username,
  }).then((res) => {
    if (res.success) {
      articles.value = res.data || []
      current.value = res.current || pageNo
      pages.value = res.pages || 1
    }
  })
}

onMounted(() => {
  getArticles(1)
})

watch(
  () => route.params.username,
  () => {
    current.value = 1
    getArticles(1)
  },
)
</script>

<style scoped>
.page-shell {
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
.article-card {
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
  line-height: 1.05;
  letter-spacing: -0.05em;
}

.hero-card p {
  margin: 0;
  color: #6b7280;
  line-height: 1.8;
}

.article-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 16px;
}

.article-card {
  overflow: hidden;
}

.article-card__cover {
  width: 100%;
  height: 220px;
  object-fit: cover;
}

.article-card__body {
  padding: 20px;
}

.article-card__tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tag {
  padding: 6px 10px;
  border-radius: 999px;
  border: 1px solid rgba(209, 250, 229, 0.9);
  background: rgba(236, 253, 245, 0.98);
  color: #047857;
  font-size: 12px;
  font-weight: 600;
}

.article-card h2 {
  margin: 14px 0 10px;
  color: #111827;
  font-size: 24px;
  line-height: 1.2;
  cursor: pointer;
}

.article-card p {
  margin: 0;
  color: #6b7280;
  line-height: 1.8;
}

.article-card__meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 8px;
  margin-top: 18px;
  color: #9ca3af;
  font-size: 14px;
}

.article-card__meta button {
  border: none;
  background: transparent;
  color: #2563eb;
  font-weight: 600;
}

.pager {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 14px;
  margin-top: 20px;
}

.pager button {
  height: 42px;
  padding: 0 18px;
  border-radius: 999px;
  border: 1px solid rgba(209, 213, 219, 0.95);
  background: rgba(255, 255, 255, 0.94);
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
  .article-grid {
    grid-template-columns: 1fr;
  }
}
</style>
