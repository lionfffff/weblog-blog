<template>
  <Header />
  <main class="list-shell">
    <div class="content-grid">
      <section>
        <FrontNavCard />
        <div class="hero-card">
          <span class="hero-card__eyebrow">Tags</span>
          <h1>标签目录</h1>
          <p>从主题标签切入当前博客内容。</p>
        </div>

        <div class="tag-grid">
          <button v-for="tag in tags" :key="tag.id" class="tag-card" @click="router.push({ path: `/u/${currentBlogStore.username}/tag/article/list`, query: { id: tag.id, name: tag.name } })">
            <h2># {{ tag.name }}</h2>
            <p>{{ tag.articlesTotal || 0 }} 篇文章</p>
          </button>
        </div>
      </section>

      <aside class="side-column">
        <UserInfoCard />
        <CategoryListCard />
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
import ScrollToTopButton from '@/layouts/frontend/components/ScrollToTopButton.vue'
import { getTagList } from '@/api/fronted/tag'
import { useCurrentBlogStore } from '@/stores/currentBlog'

const route = useRoute()
const router = useRouter()
const currentBlogStore = useCurrentBlogStore()
const tags = ref([])

const loadTags = () => {
  getTagList({
    blogUsername: currentBlogStore.username,
  }).then((res) => {
    if (res.success) tags.value = res.data || []
  })
}

onMounted(loadTags)
watch(() => route.params.username, loadTags)
</script>

<style scoped>
.list-shell { max-width: 1180px; margin: 0 auto; padding: 24px 20px 48px; }
.content-grid { display: grid; grid-template-columns: minmax(0, 1fr) 300px; gap: 18px; }
.hero-card,.tag-card{ border-radius: 28px; border: 1px solid rgba(229,231,235,.95); background: rgba(255,255,255,.92); box-shadow: 0 18px 44px rgba(15,23,42,.05); }
.hero-card{ padding:28px; margin-bottom:16px; }
.hero-card__eyebrow{ color:#2563eb; font-size:12px; font-weight:600; letter-spacing:.08em; text-transform:uppercase; }
.hero-card h1{ margin:10px 0 12px; font-size:clamp(32px,5vw,46px); }
.tag-grid{ display:grid; grid-template-columns:repeat(2,minmax(0,1fr)); gap:16px; }
.tag-card{ padding:24px; text-align:left; }
.tag-card h2{ margin:0 0 12px; color:#065f46; font-size:24px; }
.tag-card p{ margin:0; color:#6b7280; }
.side-column { position: sticky; top: 96px; align-self: start; }
@media (max-width: 1024px) { .content-grid{ grid-template-columns:1fr; } .side-column{ position:static; } }
@media (max-width: 768px) { .tag-grid{ grid-template-columns:1fr; } }
</style>
