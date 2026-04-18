<template>
  <div v-if="tags.length" class="side-card">
    <div class="side-card__header">
      <h2>标签</h2>
      <button @click="router.push(`/u/${currentBlogStore.username}/tag/list`)">全部</button>
    </div>

    <div class="chip-list">
      <button
        v-for="tag in tags"
        :key="tag.id"
        class="tag-chip"
        @click="router.push({ path: `/u/${currentBlogStore.username}/tag/article/list`, query: { id: tag.id, name: tag.name } })"
      >
        {{ tag.name }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { getTagList } from '@/api/fronted/tag'
import { useCurrentBlogStore } from '@/stores/currentBlog'

const router = useRouter()
const currentBlogStore = useCurrentBlogStore()
const tags = ref([])

const loadTags = () => {
  getTagList({
    blogUsername: currentBlogStore.username,
  }).then((res) => {
    if (res.success) {
      tags.value = res.data || []
    }
  })
}

onMounted(loadTags)
watch(() => currentBlogStore.username, loadTags)
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

.tag-chip {
  padding: 9px 12px;
  border-radius: 999px;
  border: 1px solid rgba(209, 250, 229, 0.9);
  background: rgba(236, 253, 245, 0.98);
  color: #047857;
  font-size: 13px;
  font-weight: 600;
}
</style>
