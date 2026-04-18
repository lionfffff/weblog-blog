<template>
  <Header />
  <main class="list-shell">
    <div class="content-grid">
      <section>
        <FrontNavCard />
        <div class="hero-card">
          <span class="hero-card__eyebrow">Category</span>
          <h1>{{ route.query.name || '分类文章' }}</h1>
          <p>这里只展示当前管理员博客中属于这个分类的文章。</p>
        </div>

        <div class="simple-list">
          <button v-for="article in articles" :key="article.id" class="list-card" @click="router.push(`/u/${currentBlogStore.username}/article/${article.id}`)">
            <img :src="article.cover" />
            <div>
              <h2>{{ article.title }}</h2>
              <p>{{ article.createDate }}</p>
            </div>
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
import { getCategoryArticlePageList } from '@/api/fronted/category'
import { useCurrentBlogStore } from '@/stores/currentBlog'

const route = useRoute()
const router = useRouter()
const currentBlogStore = useCurrentBlogStore()
const articles = ref([])

const loadArticles = () => {
  getCategoryArticlePageList({
    current: 1,
    size: 20,
    id: Number(route.query.id),
    blogUsername: route.params.username,
  }).then((res) => {
    if (res.success) articles.value = res.data || []
  })
}

onMounted(loadArticles)
watch(() => route.query.id, loadArticles)
watch(() => route.params.username, loadArticles)
</script>

<style scoped>
.list-shell { max-width: 1180px; margin: 0 auto; padding: 24px 20px 48px; }
.content-grid { display: grid; grid-template-columns: minmax(0, 1fr) 300px; gap: 18px; }
.hero-card,.list-card{ border-radius: 28px; border: 1px solid rgba(229,231,235,.95); background: rgba(255,255,255,.92); box-shadow: 0 18px 44px rgba(15,23,42,.05); }
.hero-card{ padding: 28px; margin-bottom: 16px; }
.hero-card__eyebrow{ color:#2563eb; font-size:12px; font-weight:600; letter-spacing:.08em; text-transform:uppercase; }
.hero-card h1{ margin:10px 0 12px; font-size:clamp(32px,5vw,46px); }
.simple-list{ display:grid; gap:16px; }
.list-card{ display:grid; grid-template-columns:160px 1fr; gap:16px; padding:16px; text-align:left; }
.list-card img{ width:160px; height:110px; object-fit:cover; border-radius:18px; }
.list-card h2{ margin:0 0 10px; color:#111827; font-size:22px; }
.list-card p{ margin:0; color:#6b7280; }
.side-column { position: sticky; top: 96px; align-self: start; }
@media (max-width: 1024px) { .content-grid{ grid-template-columns:1fr; } .side-column{ position:static; } }
@media (max-width: 640px) { .list-card{ grid-template-columns:1fr; } .list-card img{ width:100%; height:180px; } }
</style>
