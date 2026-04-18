<template>
  <div class="nav-card">
    <div class="nav-card__inner">
      <div class="nav-card__intro">
        <span class="nav-card__eyebrow">页面导航</span>
      </div>

      <div class="nav-card__track">
        <nav class="nav-links">
          <button :class="linkClass(`/u/${currentBlogStore.username}`)" @click="goHome">首页</button>
          <button :class="linkClass(`/u/${currentBlogStore.username}/category/list`)" @click="router.push(`/u/${currentBlogStore.username}/category/list`)">
            分类
          </button>
          <button :class="linkClass(`/u/${currentBlogStore.username}/tag/list`)" @click="router.push(`/u/${currentBlogStore.username}/tag/list`)">
            标签
          </button>
          <button :class="linkClass(`/u/${currentBlogStore.username}/archive/list`)" @click="router.push(`/u/${currentBlogStore.username}/archive/list`)">
            归档
          </button>
          <button :class="linkClass(`/u/${currentBlogStore.username}/statistics`)" @click="router.push(`/u/${currentBlogStore.username}/statistics`)">
            浏览统计
          </button>
        </nav>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router'
import { useCurrentBlogStore } from '@/stores/currentBlog'

const route = useRoute()
const router = useRouter()
const currentBlogStore = useCurrentBlogStore()

const goHome = () => {
  router.push(`/u/${currentBlogStore.username}`)
}

const linkClass = (path) => (route.path === path ? 'nav-link nav-link--active' : 'nav-link')
</script>

<style scoped>
.nav-card {
  position: relative;
  overflow: hidden;
  margin-bottom: 16px;
  padding: 14px 16px;
  border-radius: 22px;
  border: 1px solid rgba(229, 231, 235, 0.95);
  background: linear-gradient(180deg, rgba(255, 255, 255, 0.98) 0%, rgba(248, 250, 252, 0.94) 100%);
  box-shadow:
    inset 0 1px 0 rgba(255, 255, 255, 0.96),
    inset 0 -1px 0 rgba(226, 232, 240, 0.42),
    0 18px 46px rgba(15, 23, 42, 0.05);
}

.nav-card::before {
  content: '';
  position: absolute;
  inset: 0;
  background: linear-gradient(180deg, rgba(255, 255, 255, 0.65) 0%, rgba(255, 255, 255, 0) 42%);
  pointer-events: none;
}

.nav-card__inner {
  position: relative;
  z-index: 1;
  display: grid;
  grid-template-columns: 132px minmax(0, 1fr);
  gap: 18px;
  align-items: center;
}

.nav-card__intro {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  min-width: 0;
  padding-right: 18px;
  border-right: 1px solid rgba(226, 232, 240, 0.78);
}

.nav-card__eyebrow {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 88px;
  height: 34px;
  padding: 0 16px;
  border-radius: 999px;
  background: linear-gradient(180deg, rgba(59, 130, 246, 0.16) 0%, rgba(96, 165, 250, 0.24) 100%);
  border: 1px solid rgba(147, 197, 253, 0.9);
  color: #1d4ed8;
  font-size: 13px;
  font-weight: 800;
  letter-spacing: 0.04em;
  box-shadow:
    inset 0 1px 0 rgba(255, 255, 255, 0.92),
    0 8px 18px rgba(59, 130, 246, 0.08);
}

.nav-card__track {
  min-width: 0;
  padding: 6px;
  border-radius: 18px;
  border: 1px solid rgba(226, 232, 240, 0.82);
  background: rgba(248, 250, 252, 0.92);
  box-shadow:
    inset 0 1px 0 rgba(255, 255, 255, 0.9),
    inset 0 -1px 0 rgba(226, 232, 240, 0.35);
}

.nav-links {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  min-width: 0;
  width: 100%;
  overflow-x: auto;
  overflow-y: hidden;
  scrollbar-width: none;
  -ms-overflow-style: none;
}

.nav-links::-webkit-scrollbar {
  display: none;
}

.nav-link {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: auto;
  height: 42px;
  min-width: 92px;
  padding: 0 18px;
  border: 1px solid transparent;
  border-radius: 14px;
  background: transparent;
  color: #6b7280;
  font-size: 14px;
  font-weight: 650;
  letter-spacing: -0.01em;
  text-align: center;
  white-space: nowrap;
  transition:
    border-color 0.2s ease,
    background-color 0.2s ease,
    box-shadow 0.2s ease,
    color 0.2s ease,
    transform 0.2s ease;
}

.nav-link:hover {
  border-color: rgba(226, 232, 240, 0.88);
  background: rgba(255, 255, 255, 0.85);
  color: #475569;
  box-shadow:
    inset 0 1px 0 rgba(255, 255, 255, 0.9),
    0 8px 16px rgba(15, 23, 42, 0.04);
  transform: translateY(-1px);
}

.nav-link--active {
  border-color: rgba(191, 219, 254, 0.88);
  background: linear-gradient(180deg, rgba(219, 234, 254, 0.96) 0%, rgba(212, 228, 255, 0.92) 100%);
  color: #2563eb;
  box-shadow:
    inset 0 1px 0 rgba(255, 255, 255, 0.9),
    0 10px 18px rgba(37, 99, 235, 0.08);
}

@media (max-width: 1080px) {
  .nav-card {
    padding: 12px 14px;
  }

  .nav-card__inner {
    grid-template-columns: 118px minmax(0, 1fr);
    gap: 14px;
  }

  .nav-link {
    min-width: 84px;
    padding: 0 14px;
  }
}

@media (max-width: 820px) {
  .nav-card__inner {
    grid-template-columns: 1fr;
    gap: 12px;
  }

  .nav-card__intro {
    justify-content: center;
    padding-right: 0;
    padding-bottom: 12px;
    border-right: none;
    border-bottom: 1px solid rgba(226, 232, 240, 0.78);
  }

  .nav-links {
    justify-content: flex-start;
  }

  .nav-link {
    min-width: 96px;
  }
}

@media (max-width: 640px) {
  .nav-card {
    padding: 10px 14px;
  }

  .nav-card__track {
    padding: 4px;
  }

  .nav-link {
    min-width: 88px;
    padding: 0 14px;
    font-size: 13px;
  }
}
</style>
