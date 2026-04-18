<template>
  <div class="profile-card">
    <div class="profile-card__top">
      <div class="profile-card__media">
        <span class="profile-card__label">博客作者</span>
        <img class="profile-card__avatar" :src="avatarSrc" @error="handleAvatarError" />
      </div>
      <div class="profile-card__copy">
        <h3>{{ blogSettingsStore.blogSettings.author || currentBlogStore.username }}</h3>
        <p>{{ blogSettingsStore.blogSettings.introduction || '欢迎来到这个管理员的独立博客。' }}</p>
      </div>
    </div>

    <div class="profile-card__stats">
      <button
        v-for="card in statCards"
        :key="card.key"
        class="stat-link"
        @click="router.push(card.to)"
      >
        <div class="stat-link__head">
          <span class="stat-link__icon">
            <el-icon><component :is="card.icon" /></el-icon>
          </span>
          <span class="stat-link__arrow">
            <el-icon><ArrowRight /></el-icon>
          </span>
        </div>
        <div class="stat-link__body">
          <span class="stat-link__label">{{ card.label }}</span>
          <strong class="stat-link__value">{{ card.value }}</strong>
        </div>
      </button>
    </div>
  </div>
</template>

<script setup>
import {
  ArrowRight,
  Calendar,
  Collection,
  DataAnalysis,
  House,
  PriceTag,
} from '@element-plus/icons-vue'
import { computed, onMounted, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { getStatisticsInfo } from '@/api/fronted/statistics'
import { useAdminAvatar } from '@/composables/useAdminAvatar'
import { useBlogSettingsStore } from '@/stores/blogsettings'
import { useCurrentBlogStore } from '@/stores/currentBlog'

const router = useRouter()
const currentBlogStore = useCurrentBlogStore()
const blogSettingsStore = useBlogSettingsStore()
const statisticsInfo = ref({})
const { avatarSrc, handleAvatarError } = useAdminAvatar()

const archiveTotal = computed(() => statisticsInfo.value.articleTotalCount || 0)

const statCards = computed(() => [
  {
    key: 'pv',
    label: '总浏览量',
    value: statisticsInfo.value.pvTotalCount || 0,
    icon: DataAnalysis,
    to: `/u/${currentBlogStore.username}/statistics`,
  },
  {
    key: 'home',
    label: '首页',
    value: statisticsInfo.value.articleTotalCount || 0,
    icon: House,
    to: `/u/${currentBlogStore.username}`,
  },
  {
    key: 'category',
    label: '分类',
    value: statisticsInfo.value.categoryTotalCount || 0,
    icon: Collection,
    to: `/u/${currentBlogStore.username}/category/list`,
  },
  {
    key: 'tag',
    label: '标签',
    value: statisticsInfo.value.tagTotalCount || 0,
    icon: PriceTag,
    to: `/u/${currentBlogStore.username}/tag/list`,
  },
  {
    key: 'archive',
    label: '归档',
    value: archiveTotal.value,
    icon: Calendar,
    to: `/u/${currentBlogStore.username}/archive/list`,
  },
])

const loadStatistics = () => {
  getStatisticsInfo({
    blogUsername: currentBlogStore.username,
  }).then((res) => {
    if (res.success) {
      statisticsInfo.value = res.data || {}
    }
  })
}

onMounted(loadStatistics)
watch(() => currentBlogStore.username, loadStatistics)
</script>

<style scoped>
.profile-card {
  margin-bottom: 14px;
  padding: 22px;
  border-radius: 26px;
  border: 1px solid rgba(229, 231, 235, 0.95);
  background: rgba(255, 255, 255, 0.92);
  box-shadow: 0 18px 40px rgba(15, 23, 42, 0.05);
}

.profile-card__top {
  display: grid;
  grid-template-columns: 60px 1fr;
  gap: 14px;
  align-items: center;
}

.profile-card__media {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.profile-card__avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  object-fit: cover;
}

.profile-card__copy h3 {
  margin: 0;
  color: #111827;
  font-size: 20px;
}

.profile-card__label {
  display: inline-flex;
  width: 72px;
  height: 24px;
  align-items: center;
  justify-content: center;
  padding: 0 10px;
  border-radius: 999px;
  background: rgba(219, 234, 254, 0.95);
  color: #2563eb;
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  white-space: nowrap;
}

.profile-card__copy p {
  margin: 6px 0 0;
  color: #6b7280;
  line-height: 1.7;
}

.profile-card__stats {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 10px 16px;
  margin-top: 20px;
}

.stat-link {
  display: grid;
  gap: 12px;
  min-height: 108px;
  padding: 12px 10px;
  border-radius: 18px;
  border: 1px solid transparent;
  background: transparent;
  text-align: left;
  transition:
    transform 0.2s ease,
    border-color 0.2s ease,
    background-color 0.2s ease,
    box-shadow 0.2s ease;
}

.stat-link:hover {
  transform: translateY(-1px);
  border-color: rgba(226, 232, 240, 0.9);
  background: rgba(248, 250, 252, 0.72);
  box-shadow: 0 12px 24px rgba(15, 23, 42, 0.04);
}

.stat-link:hover .stat-link__arrow {
  transform: translateX(3px);
  color: #2563eb;
}

.stat-link__head {
  display: flex;
  align-items: center;
  gap: 6px;
}

.stat-link__icon,
.stat-link__arrow {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  color: #0f172a;
}

.stat-link__icon {
  font-size: 20px;
}

.stat-link__arrow {
  font-size: 18px;
  color: #64748b;
  transition:
    transform 0.2s ease,
    color 0.2s ease;
}

.stat-link__body {
  display: flex;
  align-items: baseline;
  flex-wrap: wrap;
  gap: 2px;
}

.stat-link__label,
.stat-link__value {
  color: #111827;
  font-size: 24px;
  font-weight: 700;
  letter-spacing: -0.04em;
  line-height: 1.15;
}

@media (max-width: 480px) {
  .profile-card__stats {
    grid-template-columns: 1fr;
  }

  .stat-link {
    min-height: 92px;
  }
}
</style>
