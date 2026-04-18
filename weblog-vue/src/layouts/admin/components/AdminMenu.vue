<template>
  <div class="menu-shell">
    <div class="menu-card" :class="{ collapsed: isCollapse }">
      <div class="brand-block">
        <div class="brand-mark">
          <img src="@/assets/blog.png" alt="WeBlog" class="brand-logo" />
        </div>
        <div v-if="!isCollapse" class="brand-copy">
          <h1>WeBlog</h1>
          <p>后台工作台</p>
        </div>
      </div>

      <div class="menu-section-label" v-if="!isCollapse">导航</div>

      <el-menu
        class="apple-menu"
        :default-active="defaultActive"
        :collapse="isCollapse"
        :collapse-transition="false"
        @select="handleSelect"
      >
        <el-menu-item v-for="item in menus" :key="item.path" :index="item.path">
          <el-icon>
            <component :is="item.icon" />
          </el-icon>
          <span>{{ item.name }}</span>
        </el-menu-item>
      </el-menu>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useMenuStore } from '@/stores/menu'
import { Document, FolderOpened, Monitor, PriceTag, Setting } from '@element-plus/icons-vue'

const menuStore = useMenuStore()
const route = useRoute()
const router = useRouter()

const isCollapse = computed(() => menuStore.menuWidth !== '250px')
const defaultActive = computed(() => route.path)

const handleSelect = (path) => {
  router.push(path)
}

const menus = [
  { name: '仪表盘', icon: Monitor, path: '/admin/index' },
  { name: '文章管理', icon: Document, path: '/admin/article/list' },
  { name: '分类管理', icon: FolderOpened, path: '/admin/category/list' },
  { name: '标签管理', icon: PriceTag, path: '/admin/tag/list' },
  { name: '博客设置', icon: Setting, path: '/admin/blog/setting' },
]
</script>

<style scoped>
.menu-shell {
  position: sticky;
  top: 0;
  height: calc(100vh - 36px);
  padding-right: 14px;
  box-sizing: border-box;
}

.menu-card {
  height: 100%;
  padding: 14px 12px;
  border: 1px solid rgba(226, 232, 240, 0.95);
  border-radius: 30px;
  background: rgba(255, 255, 255, 0.88);
  box-shadow: 0 26px 60px rgba(15, 23, 42, 0.10);
  backdrop-filter: blur(24px);
  overflow: hidden;
}

.menu-card.collapsed {
  padding-left: 8px;
  padding-right: 8px;
}

.brand-block {
  display: flex;
  align-items: center;
  gap: 12px;
  min-height: 70px;
  margin-bottom: 14px;
  padding: 10px 10px 14px;
}

.brand-mark {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 48px;
  height: 48px;
  border-radius: 16px;
  background: linear-gradient(180deg, #f8fafc 0%, #eef2ff 100%);
  box-shadow: inset 0 0 0 1px rgba(226, 232, 240, 0.9);
}

.brand-logo {
  width: 36px;
  height: 36px;
  object-fit: contain;
}

.brand-copy h1 {
  margin: 0;
  color: #0f172a;
  font-size: 18px;
  font-weight: 700;
  letter-spacing: -0.03em;
}

.brand-copy p {
  margin: 4px 0 0;
  color: #64748b;
  font-size: 12px;
}

.menu-section-label {
  margin: 0 12px 12px;
  color: #94a3b8;
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.16em;
  text-transform: uppercase;
}

.apple-menu {
  border: 0;
  background: transparent;
}

.apple-menu :deep(.el-menu-item),
.apple-menu :deep(.el-sub-menu__title) {
  height: 50px;
  margin-bottom: 8px;
  border-radius: 16px;
  color: #334155;
  font-size: 14px;
  font-weight: 600;
  transition: all 0.2s ease;
}

.apple-menu :deep(.el-menu-item:hover),
.apple-menu :deep(.el-sub-menu__title:hover) {
  background: rgba(241, 245, 249, 0.96);
  color: #0f172a;
}

.apple-menu :deep(.el-menu-item.is-active) {
  background: linear-gradient(180deg, #0f172a 0%, #1e293b 100%);
  color: #fff;
  box-shadow: 0 12px 24px rgba(15, 23, 42, 0.18);
}

.apple-menu :deep(.el-menu-item .el-icon) {
  margin-right: 12px;
  font-size: 16px;
}

.menu-card.collapsed .brand-block {
  justify-content: center;
  padding-left: 0;
  padding-right: 0;
}

.menu-card.collapsed .apple-menu :deep(.el-menu-item) {
  padding: 0 !important;
  justify-content: center;
}

.menu-card.collapsed .apple-menu :deep(.el-menu-item .el-icon) {
  margin-right: 0;
}
</style>
