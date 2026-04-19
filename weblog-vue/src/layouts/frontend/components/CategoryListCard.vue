<template>
  <div v-if="categories.length" class="side-card">
    <div class="side-card__header">
      <h2>分类</h2>
      <button @click="router.push(`/u/${currentBlogStore.username}/category/list`)">全部</button>
    </div>

    <div class="chip-list">
      <button
        v-for="category in categories"
        :key="category.id"
        class="chip"
        @click="router.push({ path: `/u/${currentBlogStore.username}/category/article/list`, query: { id: category.id, name: category.name } })"
      >
        <span>{{ category.name }}</span>
        <strong>{{ category.articlesTotal }}</strong>
      </button>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { getCategoryList } from '@/api/fronted/category'
import { getCachedValue, setCachedValue } from '@/composables/frontendCache'
import { useCurrentBlogStore } from '@/stores/currentBlog'

const router = useRouter()
const currentBlogStore = useCurrentBlogStore()
const categories = ref([])
const CATEGORY_CACHE_MS = 5 * 60 * 1000

const loadCategories = () => {
  const username = currentBlogStore.username
  if (!username) {
    categories.value = []
    return
  }

  const cacheKey = `categories:${username}`
  const cached = getCachedValue(cacheKey, CATEGORY_CACHE_MS)
  if (cached) {
    categories.value = cached
    return
  }

  getCategoryList({
    blogUsername: currentBlogStore.username,
    size: 10,
  }).then((res) => {
    if (res.success) {
      categories.value = res.data || []
      setCachedValue(cacheKey, categories.value)
    }
  })
}

onMounted(loadCategories)
watch(() => currentBlogStore.username, loadCategories)
</script>

<style scoped>
.side-card {
  margin-bottom: 14px;
  padding: 20px;
  border-radius: 24px;
  border: 1px solid rgba(229, 231, 235, 0.95);
  background: rgba(255, 255, 255, 0.92);
  box-shadow: 0 14px 36px rgba(15, 23, 42, 0.05);
}

.side-card__header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 14px;
}

.side-card__header h2 {
  margin: 0;
  color: #111827;
  font-size: 18px;
}

.side-card__header button {
  border: none;
  background: transparent;
  color: #2563eb;
  font-weight: 600;
}

.chip-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.chip {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 10px 12px;
  border-radius: 16px;
  border: 1px solid rgba(229, 231, 235, 0.9);
  background: #fff;
  color: #374151;
}

.chip strong {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 22px;
  height: 22px;
  padding: 0 6px;
  border-radius: 999px;
  background: rgba(219, 234, 254, 0.95);
  color: #2563eb;
  font-size: 12px;
}
</style>
