<template>
  <div class="blogs-shell">
    <section class="blogs-panel">
      <div class="blogs-head">
        <div>
          <span class="blogs-eyebrow">博客列表</span>
          <h1>选择要浏览的博客</h1>
          <p>当前登录账号：{{ userStore.userInfo?.username || '未登录' }}。你可以先进入自己的博客，也可以切换查看其他管理员的内容。</p>
        </div>

        <div class="blogs-actions">
          <button class="ghost-button" @click="router.push('/admin/index')">进入后台</button>
          <button class="ghost-button" @click="handleLogout">退出登录</button>
        </div>
      </div>

      <div v-if="myBlog" class="blogs-section">
        <div class="section-title">我的博客</div>
        <article class="blog-card blog-card--mine" @click="enterBlog(myBlog.username)">
          <div class="blog-card__head">
            <img class="blog-card__avatar" :src="myBlog.avatar || fallbackAvatar" />
            <div>
              <div class="blog-card__title">
                <h2>{{ myBlog.blogName }}</h2>
                <span class="blog-badge">当前账号</span>
              </div>
              <p>@{{ myBlog.username }}</p>
            </div>
          </div>
          <p class="blog-card__intro">{{ myBlog.introduction || '这是你自己的独立博客。' }}</p>
          <div class="blog-card__meta">
            <span>{{ myBlog.author }}</span>
            <span>{{ myBlog.articleTotal || 0 }} 篇文章</span>
          </div>
        </article>
      </div>

      <div v-if="otherBlogs.length" class="blogs-section">
        <div class="section-title">其他博客</div>
        <section class="blogs-grid">
          <article v-for="blog in otherBlogs" :key="blog.username" class="blog-card" @click="enterBlog(blog.username)">
            <div class="blog-card__head">
              <img class="blog-card__avatar" :src="blog.avatar || fallbackAvatar" />
              <div>
                <h2>{{ blog.blogName }}</h2>
                <p>@{{ blog.username }}</p>
              </div>
            </div>
            <p class="blog-card__intro">{{ blog.introduction || '这个管理员暂时还没有填写博客介绍。' }}</p>
            <div class="blog-card__meta">
              <span>{{ blog.author }}</span>
              <span>{{ blog.articleTotal || 0 }} 篇文章</span>
            </div>
          </article>
        </section>
      </div>

      <section v-if="!blogs.length" class="blogs-empty">
        <h2>还没有可用的博客</h2>
        <p>当前系统里暂时没有可展示的博客，请先注册账号或在后台初始化内容。</p>
      </section>
    </section>
  </div>
</template>

<script setup>
import fallbackAvatar from '@/assets/avatar.png.jpg'
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { getBlogPortalList } from '@/api/fronted/blogsettings'
import { showMessage } from '@/composables/util'
import { useCurrentBlogStore } from '@/stores/currentBlog'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const blogs = ref([])
const currentBlogStore = useCurrentBlogStore()
const userStore = useUserStore()

const myBlog = computed(() => blogs.value.find((blog) => blog.username === userStore.userInfo?.username) || null)
const otherBlogs = computed(() => blogs.value.filter((blog) => blog.username !== userStore.userInfo?.username))

onMounted(() => {
  getBlogPortalList().then((res) => {
    if (res.success) {
      blogs.value = res.data || []
    }
  })
})

const enterBlog = (username) => {
  currentBlogStore.setCurrentBlog(username)
  router.push(`/u/${username}`)
}

const handleLogout = () => {
  userStore.logout()
  currentBlogStore.clearCurrentBlog()
  showMessage('已退出登录', 'success')
  router.push('/login')
}
</script>

<style scoped>
.blogs-shell {
  min-height: 100vh;
  padding: 32px 24px 48px;
  background:
    radial-gradient(circle at top left, rgba(59, 130, 246, 0.08), transparent 24%),
    linear-gradient(180deg, #f3f4f6 0%, #eef2f7 100%);
}

.blogs-panel {
  max-width: 1180px;
  margin: 0 auto;
  padding: 30px;
  border: 1px solid rgba(229, 231, 235, 0.95);
  border-radius: 30px;
  background: rgba(255, 255, 255, 0.88);
  box-shadow: 0 24px 70px rgba(15, 23, 42, 0.08);
}

.blogs-head {
  display: flex;
  justify-content: space-between;
  gap: 24px;
}

.blogs-eyebrow,
.section-title {
  display: inline-flex;
  color: #94a3b8;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.1em;
  text-transform: uppercase;
}

.blogs-head h1 {
  margin: 12px 0 10px;
  color: #111827;
  font-size: clamp(32px, 4vw, 44px);
  letter-spacing: -0.05em;
}

.blogs-head p {
  max-width: 720px;
  margin: 0;
  color: #6b7280;
  line-height: 1.8;
}

.blogs-actions {
  display: flex;
  gap: 12px;
  align-items: flex-start;
}

.ghost-button {
  height: 46px;
  padding: 0 18px;
  border-radius: 999px;
  border: 1px solid rgba(209, 213, 219, 0.9);
  background: rgba(255, 255, 255, 0.92);
  color: #111827;
  font-weight: 600;
}

.blogs-section {
  margin-top: 28px;
}

.blogs-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 18px;
  margin-top: 12px;
}

.blog-card {
  margin-top: 12px;
  padding: 24px;
  border-radius: 24px;
  border: 1px solid rgba(229, 231, 235, 0.95);
  background: rgba(255, 255, 255, 0.96);
  box-shadow: 0 16px 42px rgba(15, 23, 42, 0.05);
  transition:
    transform 0.2s ease,
    box-shadow 0.2s ease,
    border-color 0.2s ease;
  cursor: pointer;
}

.blog-card:hover {
  transform: translateY(-3px);
  border-color: rgba(147, 197, 253, 0.95);
  box-shadow: 0 20px 44px rgba(37, 99, 235, 0.12);
}

.blog-card--mine {
  border-color: rgba(96, 165, 250, 0.55);
  background: linear-gradient(180deg, rgba(239, 246, 255, 0.84) 0%, rgba(255, 255, 255, 0.98) 100%);
}

.blog-card__head {
  display: flex;
  align-items: center;
  gap: 14px;
}

.blog-card__avatar {
  width: 56px;
  height: 56px;
  border-radius: 16px;
  object-fit: cover;
}

.blog-card__title {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
}

.blog-card__head h2 {
  margin: 0;
  color: #111827;
  font-size: 20px;
  font-weight: 700;
}

.blog-card__head p {
  margin: 4px 0 0;
  color: #6b7280;
}

.blog-badge {
  display: inline-flex;
  align-items: center;
  height: 24px;
  padding: 0 8px;
  border-radius: 999px;
  background: rgba(219, 234, 254, 0.95);
  color: #2563eb;
  font-size: 11px;
  font-weight: 700;
}

.blog-card__intro {
  min-height: 72px;
  margin: 18px 0;
  color: #4b5563;
  line-height: 1.75;
}

.blog-card__meta {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  color: #6b7280;
  font-size: 14px;
  font-weight: 600;
}

.blogs-empty {
  margin-top: 28px;
  padding: 48px 24px;
  text-align: center;
  border-radius: 28px;
  border: 1px solid rgba(229, 231, 235, 0.95);
  background: rgba(255, 255, 255, 0.82);
}

.blogs-empty h2 {
  margin: 0;
  color: #111827;
  font-size: 28px;
}

.blogs-empty p {
  margin: 12px 0 0;
  color: #6b7280;
  line-height: 1.8;
}

@media (max-width: 768px) {
  .blogs-panel {
    padding: 22px;
  }

  .blogs-head {
    flex-direction: column;
  }

  .blogs-actions {
    flex-wrap: wrap;
  }
}
</style>
