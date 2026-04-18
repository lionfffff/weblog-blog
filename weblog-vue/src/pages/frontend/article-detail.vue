<template>
  <Header />

  <main class="detail-shell">
    <div class="detail-grid">
      <section class="detail-main">
        <FrontNavCard />
        <article class="detail-card">
          <div class="detail-tags">
            <button
              v-for="tag in article.tags || []"
              :key="tag.id"
              class="tag"
              @click="router.push({ path: `/u/${currentBlogStore.username}/tag/article/list`, query: { id: tag.id, name: tag.name } })"
            >
              {{ tag.name }}
            </button>
          </div>

          <h1>{{ article.title }}</h1>

          <div class="detail-meta">
            <span>{{ article.createTime }}</span>
            <button @click="router.push({ path: `/u/${currentBlogStore.username}/category/article/list`, query: { id: article.categoryId, name: article.categoryName } })">
              {{ article.categoryName }}
            </button>
            <span>{{ article.readNum }} 阅读</span>
            <span>{{ article.totalWords }} 字</span>
          </div>

          <div class="article-content" v-html="article.content"></div>

          <div class="detail-pager">
            <button v-if="article.preArticle" @click="router.push(`/u/${currentBlogStore.username}/article/${article.preArticle.articleId}`)">
              ← {{ article.preArticle.articleTitle }}
            </button>
            <button v-if="article.nextArticle" @click="router.push(`/u/${currentBlogStore.username}/article/${article.nextArticle.articleId}`)">
              {{ article.nextArticle.articleTitle }} →
            </button>
          </div>
        </article>
      </section>

      <aside class="detail-side">
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
import { getArticleDetail } from '@/api/fronted/article'
import { useCurrentBlogStore } from '@/stores/currentBlog'

const route = useRoute()
const router = useRouter()
const currentBlogStore = useCurrentBlogStore()
const article = ref({})

const loadArticle = () => {
  getArticleDetail({
    blogUsername: route.params.username,
    articleId: route.params.articleId,
  }).then((res) => {
    if (res.success) {
      article.value = res.data || {}
    } else {
      router.push({ name: 'NotFound' })
    }
  })
}

onMounted(loadArticle)
watch(() => route.params.articleId, loadArticle)
watch(() => route.params.username, loadArticle)
</script>

<style scoped>
.detail-shell {
  max-width: 1180px;
  margin: 0 auto;
  padding: 24px 20px 48px;
}

.detail-grid {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 300px;
  gap: 18px;
}

.detail-card {
  padding: 28px;
  border-radius: 28px;
  border: 1px solid rgba(229, 231, 235, 0.95);
  background: rgba(255, 255, 255, 0.92);
  box-shadow: 0 18px 44px rgba(15, 23, 42, 0.05);
}

.detail-tags {
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

.detail-card h1 {
  margin: 18px 0 14px;
  color: #111827;
  font-size: clamp(32px, 5vw, 52px);
  line-height: 1.06;
  letter-spacing: -0.05em;
}

.detail-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  color: #6b7280;
}

.detail-meta button {
  border: none;
  background: transparent;
  color: #2563eb;
  font-weight: 600;
}

.article-content {
  margin-top: 28px;
  color: #374151;
  line-height: 1.9;
}

:deep(.article-content img) {
  max-width: 100%;
  border-radius: 20px;
}

:deep(.article-content pre) {
  overflow: auto;
  padding: 18px;
  border-radius: 18px;
  background: #0f172a;
  color: #e5e7eb;
}

.detail-pager {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  margin-top: 30px;
}

.detail-pager button {
  flex: 1;
  min-height: 56px;
  padding: 0 16px;
  border-radius: 18px;
  border: 1px solid rgba(229, 231, 235, 0.95);
  background: rgba(249, 250, 251, 0.95);
  color: #111827;
  font-weight: 600;
}

.detail-side {
  position: sticky;
  top: 96px;
  align-self: start;
}

@media (max-width: 1024px) {
  .detail-grid {
    grid-template-columns: 1fr;
  }

  .detail-side {
    position: static;
  }
}
</style>
