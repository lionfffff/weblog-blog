<template>
  <header class="front-header">
    <div class="front-header__inner">
      <div class="front-header__top">
        <button class="brand" @click="goHome">
          <img class="brand__logo" :src="blogSettingsStore.blogSettings.logo || currentBlogAvatar" />
          <div class="brand__copy">
            <span class="brand__eyebrow">正在浏览</span>
            <strong class="brand__title">{{ displayBlogName }}</strong>
          </div>
        </button>

        <div class="actions">
          <button class="capsule capsule--soft" @click="searchOpen = true">搜索</button>

          <el-popover v-model:visible="switcherVisible" trigger="click" placement="bottom-end" :width="380">
            <template #reference>
              <button class="capsule capsule--soft">
                切换博客
                <el-icon class="capsule__icon"><ArrowDown /></el-icon>
              </button>
            </template>

            <div class="switcher-panel">
              <div class="switcher-head">
                <div>
                  <h3>切换博客</h3>
                  <p>{{ currentAccountText }}</p>
                </div>
              </div>

              <div v-if="myBlog" class="switcher-section">
                <span class="switcher-section__title">我的博客</span>
                <button
                  class="switcher-item switcher-item--mine"
                  :class="{ 'switcher-item--current': isCurrentBlog(myBlog.username) }"
                  @click="switchBlog(myBlog.username)"
                >
                  <img class="switcher-item__avatar" :src="myBlog.avatar || fallbackAvatar" />
                  <div class="switcher-item__body">
                    <div class="switcher-item__top">
                      <strong>{{ myBlog.blogName }}</strong>
                      <span class="switcher-badge switcher-badge--mine">当前账号</span>
                      <span v-if="isCurrentBlog(myBlog.username)" class="switcher-badge switcher-badge--current">正在浏览</span>
                    </div>
                    <p>@{{ myBlog.username }} · {{ myBlog.articleTotal || 0 }} 篇文章</p>
                    <small>{{ myBlog.introduction || '这是你自己的独立博客。' }}</small>
                  </div>
                </button>
              </div>

              <div v-if="otherBlogs.length" class="switcher-section">
                <span class="switcher-section__title">其他博客</span>
                <div class="switcher-list">
                  <button
                    v-for="blog in otherBlogs"
                    :key="blog.username"
                    class="switcher-item"
                    :class="{ 'switcher-item--current': isCurrentBlog(blog.username) }"
                    @click="switchBlog(blog.username)"
                  >
                    <img class="switcher-item__avatar" :src="blog.avatar || fallbackAvatar" />
                    <div class="switcher-item__body">
                      <div class="switcher-item__top">
                        <strong>{{ blog.blogName }}</strong>
                        <span v-if="isCurrentBlog(blog.username)" class="switcher-badge switcher-badge--current">正在浏览</span>
                      </div>
                      <p>@{{ blog.username }} · {{ blog.articleTotal || 0 }} 篇文章</p>
                      <small>{{ blog.introduction || '这个管理员暂时还没有填写博客介绍。' }}</small>
                    </div>
                  </button>
                </div>
              </div>

              <div v-if="!portalBlogs.length" class="switcher-empty">暂时没有可切换的博客。</div>
            </div>
          </el-popover>

          <button class="capsule capsule--soft" @click="goAdmin">后台</button>
          <button class="capsule capsule--soft capsule--danger" @click="logout">退出登录</button>
          <div class="capsule capsule--user">
            <img class="capsule__avatar" :src="accountAvatar" @error="handleAccountAvatarError" />
            <span class="capsule__identity">{{ currentAccountText }}</span>
          </div>
        </div>
      </div>
    </div>

    <div v-if="searchOpen" class="search-mask" @click.self="closeSearch">
      <div class="search-panel">
        <div class="search-panel__header">
          <div>
            <span class="search-panel__eyebrow">Lucene Search</span>
            <h3>搜索当前博客内容</h3>
          </div>
          <button class="search-close" @click="closeSearch">关闭</button>
        </div>

        <input v-model="searchWord" class="search-input" placeholder="输入关键词搜索文章标题或摘要" />

        <div v-if="searchArticles.length" class="search-results">
          <button
            v-for="article in searchArticles"
            :key="article.id"
            class="search-result"
            @click="jumpToArticleDetailPage(article.id)"
          >
            <img :src="article.cover" />
            <div>
              <h4 v-html="article.title"></h4>
              <p>{{ article.summary }}</p>
              <span>{{ article.createDate }}</span>
            </div>
          </button>
        </div>

        <div v-else class="search-empty">
          <h4>{{ searchWord ? '没有搜索到匹配内容' : '开始搜索当前博客' }}</h4>
          <p>{{ searchWord ? '可以换一个关键词再试试。' : '搜索结果只会来自当前正在浏览的博客。' }}</p>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup>
import fallbackAvatar from '@/assets/avatar.png.jpg'
import adminAvatar from '@/assets/avatar.png.jpg'
import { ArrowDown } from '@element-plus/icons-vue'
import { computed, onMounted, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { getBlogPortalList, getBlogSettingsDetail } from '@/api/fronted/blogsettings'
import { getArticleSearchPageList } from '@/api/fronted/search'
import { showMessage } from '@/composables/util'
import { useBlogSettingsStore } from '@/stores/blogsettings'
import { useCurrentBlogStore } from '@/stores/currentBlog'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const blogSettingsStore = useBlogSettingsStore()
const currentBlogStore = useCurrentBlogStore()
const userStore = useUserStore()

const searchOpen = ref(false)
const searchWord = ref('')
const searchArticles = ref([])
const switcherVisible = ref(false)
const portalBlogs = ref([])
const accountAvatarRaw = ref('')
const accountAvatarFailed = ref(false)

const currentBlogAvatar = computed(() => {
  const blogAvatar = blogSettingsStore.blogSettings?.avatar
  if (typeof blogAvatar === 'string' && blogAvatar.trim()) {
    return blogAvatar
  }

  return fallbackAvatar
})

const displayBlogName = computed(() => {
  return blogSettingsStore.blogSettings.name || `${currentBlogStore.username} 的博客`
})

const currentAccountText = computed(() => {
  return `当前登录：${userStore.userInfo?.username || '未登录'}`
})

const accountAvatar = computed(() => {
  if (accountAvatarFailed.value) return adminAvatar

  if (typeof accountAvatarRaw.value === 'string' && accountAvatarRaw.value.trim()) {
    return accountAvatarRaw.value
  }

  return adminAvatar
})

const myBlog = computed(() => portalBlogs.value.find((blog) => blog.username === userStore.userInfo?.username) || null)
const otherBlogs = computed(() => portalBlogs.value.filter((blog) => blog.username !== userStore.userInfo?.username))

const loadPortalBlogs = () => {
  getBlogPortalList().then((res) => {
    if (res.success) {
      portalBlogs.value = res.data || []
    }
  })
}

const loadAccountAvatar = () => {
  const username = userStore.userInfo?.username
  if (!username) {
    accountAvatarRaw.value = ''
    return
  }

  getBlogSettingsDetail({ blogUsername: username })
    .then((res) => {
      if (res.success) {
        accountAvatarRaw.value = res.data?.avatar || ''
      }
    })
    .catch(() => {
      accountAvatarRaw.value = ''
    })
}

onMounted(loadPortalBlogs)
onMounted(loadAccountAvatar)

watch(searchWord, (word) => {
  if (!word) {
    searchArticles.value = []
    return
  }

  getArticleSearchPageList({
    current: 1,
    size: 8,
    word,
    blogUsername: currentBlogStore.username,
  }).then((res) => {
    if (res.success) {
      searchArticles.value = res.data || []
    }
  })
})

watch(
  () => userStore.userInfo?.username,
  () => {
    accountAvatarFailed.value = false
    loadAccountAvatar()
  },
  { immediate: true },
)

const isCurrentBlog = (username) => username === currentBlogStore.username

const goHome = () => {
  router.push(`/u/${currentBlogStore.username}`)
}

const switchBlog = (username) => {
  switcherVisible.value = false
  if (!username) return

  currentBlogStore.setCurrentBlog(username)
  router.push(`/u/${username}`)
}

const jumpToArticleDetailPage = (articleId) => {
  closeSearch()
  router.push(`/u/${currentBlogStore.username}/article/${articleId}`)
}

const closeSearch = () => {
  searchOpen.value = false
  searchWord.value = ''
  searchArticles.value = []
}

const handleAccountAvatarError = () => {
  accountAvatarFailed.value = true
}

const logout = () => {
  userStore.logout()
  currentBlogStore.clearCurrentBlog()
  router.push('/login')
}

const goAdmin = () => {
  const currentUsername = currentBlogStore.username
  const loginUsername = userStore.userInfo?.username

  if (currentUsername && loginUsername && currentUsername === loginUsername) {
    router.push('/admin/index')
    return
  }

  showMessage('当前正在浏览其他管理员的博客，无权限进入后台', 'warning')
}

</script>

<style scoped>
.front-header {
  position: sticky;
  top: 0;
  z-index: 40;
  padding: 16px 20px 0;
  background: linear-gradient(180deg, rgba(245, 245, 247, 0.95) 0%, rgba(245, 245, 247, 0.62) 100%);
  backdrop-filter: blur(16px);
}

.front-header__inner {
  max-width: 1180px;
  margin: 0 auto;
  padding: 16px 18px 14px;
  border-radius: 22px;
  border: 1px solid rgba(229, 231, 235, 0.95);
  background: rgba(255, 255, 255, 0.84);
  box-shadow: 0 18px 46px rgba(15, 23, 42, 0.06);
}

.front-header__top {
  display: grid;
  grid-template-columns: minmax(0, 1fr) auto;
  align-items: center;
  gap: 16px;
}

.brand {
  display: flex;
  align-items: center;
  gap: 12px;
  min-width: 0;
  width: 100%;
  border: none;
  background: transparent;
  text-align: left;
}

.brand__logo {
  width: 42px;
  height: 42px;
  border-radius: 14px;
  object-fit: cover;
}

.brand__copy {
  min-width: 0;
  overflow: hidden;
}

.brand__eyebrow {
  display: block;
  color: #94a3b8;
  font-size: 11px;
  letter-spacing: 0.08em;
  text-transform: uppercase;
}

.brand__title {
  display: block;
  width: 100%;
  overflow: hidden;
  color: #111827;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-size: 15px;
}

.actions {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 8px;
  flex-wrap: wrap;
  min-width: 0;
}

.capsule {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  height: 40px;
  padding: 0 16px;
  border-radius: 999px;
  border: 1px solid rgba(229, 231, 235, 0.92);
  font-weight: 600;
  flex: 0 0 auto;
  white-space: nowrap;
}

.capsule--soft {
  background: rgba(255, 255, 255, 0.88);
  color: #111827;
}

.capsule--user {
  background: linear-gradient(180deg, rgba(236, 253, 245, 0.98) 0%, rgba(220, 252, 231, 0.96) 100%);
  border-color: rgba(134, 239, 172, 0.9);
  color: #166534;
}

.capsule--danger {
  color: #b91c1c;
}

.capsule__avatar {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  object-fit: cover;
}

.capsule__identity {
  color: #166534;
  font-size: 14px;
  font-weight: 700;
  white-space: nowrap;
}

.capsule__icon {
  color: #94a3b8;
  font-size: 12px;
}

.switcher-panel {
  display: grid;
  gap: 16px;
}

.switcher-head h3 {
  margin: 0;
  color: #111827;
  font-size: 18px;
}

.switcher-head p {
  margin: 6px 0 0;
  color: #6b7280;
  font-size: 13px;
}

.switcher-section {
  display: grid;
  gap: 10px;
}

.switcher-section__title {
  color: #94a3b8;
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.1em;
  text-transform: uppercase;
}

.switcher-list {
  display: grid;
  gap: 10px;
}

.switcher-item {
  display: grid;
  grid-template-columns: 42px 1fr;
  gap: 12px;
  width: 100%;
  padding: 14px;
  border: 1px solid rgba(226, 232, 240, 0.9);
  border-radius: 18px;
  background: #fff;
  text-align: left;
  transition:
    border-color 0.2s ease,
    box-shadow 0.2s ease,
    transform 0.2s ease;
}

.switcher-item:hover {
  transform: translateY(-1px);
  border-color: rgba(148, 163, 184, 0.9);
  box-shadow: 0 12px 22px rgba(15, 23, 42, 0.06);
}

.switcher-item--mine {
  border-color: rgba(96, 165, 250, 0.5);
  background: linear-gradient(180deg, rgba(239, 246, 255, 0.88) 0%, rgba(255, 255, 255, 0.98) 100%);
}

.switcher-item--current {
  border-color: rgba(37, 99, 235, 0.72);
  box-shadow: 0 0 0 4px rgba(96, 165, 250, 0.12);
}

.switcher-item__avatar {
  width: 42px;
  height: 42px;
  border-radius: 14px;
  object-fit: cover;
}

.switcher-item__body {
  min-width: 0;
}

.switcher-item__top {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.switcher-item__top strong {
  color: #111827;
  font-size: 14px;
}

.switcher-item__body p {
  margin: 6px 0 4px;
  color: #6b7280;
  font-size: 13px;
}

.switcher-item__body small {
  display: block;
  color: #94a3b8;
  line-height: 1.6;
}

.switcher-badge {
  display: inline-flex;
  align-items: center;
  height: 22px;
  padding: 0 8px;
  border-radius: 999px;
  font-size: 11px;
  font-weight: 700;
}

.switcher-badge--mine {
  background: rgba(219, 234, 254, 0.95);
  color: #2563eb;
}

.switcher-badge--current {
  background: rgba(237, 233, 254, 0.95);
  color: #7c3aed;
}

.switcher-empty {
  padding: 18px;
  border-radius: 18px;
  background: #f8fafc;
  color: #6b7280;
  text-align: center;
}

.search-mask {
  position: fixed;
  inset: 0;
  display: flex;
  align-items: flex-start;
  justify-content: center;
  padding: 88px 16px 16px;
  background: rgba(15, 23, 42, 0.24);
}

.search-panel {
  width: min(100%, 760px);
  padding: 24px;
  border-radius: 28px;
  background: rgba(255, 255, 255, 0.96);
  box-shadow: 0 24px 80px rgba(15, 23, 42, 0.18);
}

.search-panel__header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.search-panel__eyebrow {
  display: inline-flex;
  color: #2563eb;
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 0.08em;
  text-transform: uppercase;
}

.search-panel__header h3 {
  margin: 8px 0 0;
  color: #111827;
  font-size: 28px;
  letter-spacing: -0.03em;
}

.search-close {
  border: none;
  background: transparent;
  color: #6b7280;
  font-weight: 600;
}

.search-input {
  width: 100%;
  height: 54px;
  margin-top: 18px;
  padding: 0 18px;
  border: 1px solid rgba(209, 213, 219, 0.95);
  border-radius: 18px;
  background: rgba(255, 255, 255, 0.94);
}

.search-results {
  display: grid;
  gap: 12px;
  margin-top: 18px;
}

.search-result {
  display: grid;
  grid-template-columns: 92px 1fr;
  gap: 14px;
  padding: 14px;
  border: 1px solid rgba(229, 231, 235, 0.95);
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.94);
  text-align: left;
}

.search-result img {
  width: 92px;
  height: 72px;
  border-radius: 14px;
  object-fit: cover;
}

.search-result h4 {
  margin: 0 0 8px;
  color: #111827;
  font-size: 16px;
}

.search-result p {
  margin: 0 0 8px;
  color: #6b7280;
  line-height: 1.6;
}

.search-result span,
.search-empty p {
  color: #9ca3af;
  font-size: 13px;
}

.search-empty {
  padding: 28px 0 8px;
  text-align: center;
}

.search-empty h4 {
  margin: 0;
  color: #111827;
  font-size: 18px;
}

@media (max-width: 1120px) {
  .front-header__top {
    grid-template-columns: 1fr;
    align-items: flex-start;
  }

  .actions {
    justify-content: flex-start;
  }
}

@media (max-width: 640px) {
  .front-header {
    padding: 14px 12px 0;
  }

  .front-header__inner,
  .front-header__nav-card {
    padding: 14px;
  }

  .nav-link,
  .capsule {
    font-size: 13px;
  }

  .capsule__identity {
    font-size: 13px;
  }
}
</style>
