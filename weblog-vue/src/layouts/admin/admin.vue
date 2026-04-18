<template>
  <div class="admin-shell" :style="shellStyle">
    <el-container class="admin-shell__container">
      <el-aside :width="menuStore.menuWidth" class="admin-shell__aside transition-all">
        <AdminMenu />
      </el-aside>

      <el-container class="admin-shell__main-wrap">
        <el-header class="admin-shell__header">
          <div class="admin-shell__chrome">
            <AdminHeader />
            <AdminTagList />
          </div>
        </el-header>

        <el-main class="admin-shell__main">
          <router-view v-slot="{ Component }">
            <Transition>
              <KeepAlive :max="10">
                <component :is="Component" />
              </KeepAlive>
            </Transition>
          </router-view>
        </el-main>

        <el-footer class="admin-shell__footer">
          <AdminFooter />
        </el-footer>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import AdminFooter from './components/AdminFooter.vue'
import AdminHeader from './components/AdminHeader.vue'
import AdminMenu from './components/AdminMenu.vue'
import AdminTagList from './components/AdminTagList.vue'
import { useBlogSettingsStore } from '@/stores/blogsettings'
import { useMenuStore } from '@/stores/menu'

const menuStore = useMenuStore()
const blogSettingsStore = useBlogSettingsStore()
const shellStyle = computed(() => ({
  '--admin-aside-width': menuStore.menuWidth,
}))

onMounted(() => {
  if (!blogSettingsStore.blogSettings?.avatar) {
    blogSettingsStore.getBlogSettings()
  }
})
</script>

<style scoped>
.admin-shell {
  --admin-aside-width: 250px;
  height: 100vh;
  overflow: hidden;
  background:
    radial-gradient(circle at top left, rgba(191, 219, 254, 0.32), transparent 26%),
    radial-gradient(circle at top right, rgba(226, 232, 240, 0.72), transparent 30%),
    linear-gradient(180deg, #f8fafc 0%, #eef4ff 48%, #f8fafc 100%);
}

.admin-shell__container {
  height: 100vh;
  overflow: hidden;
  background: transparent;
}

.admin-shell__aside {
  height: 100vh;
  flex-shrink: 0;
  box-sizing: border-box;
  padding: 18px 0 18px 18px;
  overflow: visible;
}

.admin-shell__main-wrap {
  display: flex;
  flex-direction: column;
  height: 100vh;
  min-width: 0;
  background: transparent;
  overflow-x: hidden;
  overflow-y: auto;
  -webkit-overflow-scrolling: touch;
}

.admin-shell__header {
  position: sticky;
  top: 0;
  z-index: 60;
  flex-shrink: 0;
  height: auto;
  padding: 18px 22px 14px;
  background: transparent;
}

.admin-shell__chrome {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.admin-shell__main {
  flex: 1 0 auto;
  padding: 4px 22px 28px;
  background: transparent;
  overflow: visible;
}

.admin-shell__footer {
  flex-shrink: 0;
  padding: 0 22px 22px;
  background: transparent;
}

@media (max-width: 900px) {
  .admin-shell__aside {
    padding-left: 12px;
  }

  .admin-shell__header,
  .admin-shell__main,
  .admin-shell__footer {
    padding-left: 14px;
    padding-right: 14px;
  }
}
</style>
