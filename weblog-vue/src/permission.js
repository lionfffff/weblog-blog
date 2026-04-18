import router from '@/router/index'
import { getToken } from '@/composables/auth'
import { hidePageLoading, showMessage, showPageLoading } from '@/composables/util'
import { useBlogSettingsStore } from '@/stores/blogsettings'
import { useCurrentBlogStore } from '@/stores/currentBlog'
import { useUserStore } from '@/stores/user'

const isProtectedPath = (path) => path.startsWith('/admin') || path.startsWith('/blogs') || path.startsWith('/u/')

router.beforeEach(async (to, from, next) => {
  showPageLoading()

  const token = getToken()
  const userStore = useUserStore()
  const blogSettingsStore = useBlogSettingsStore()
  const currentBlogStore = useCurrentBlogStore()

  if (!token && isProtectedPath(to.path)) {
    showMessage('请先登录', 'warning')
    next({ path: '/login', query: { redirect: to.fullPath } })
    return
  }

  if (token && (to.path === '/login' || to.path === '/register')) {
    const redirect = typeof to.query.redirect === 'string' ? to.query.redirect : ''
    next(redirect || '/admin/index')
    return
  }

  if (token && !userStore.userInfo?.username) {
    await userStore.setUserInfo()
  }

  if (to.params.username) {
    const username = to.params.username
    currentBlogStore.setCurrentBlog(username)
    await blogSettingsStore.getBlogSettings(username)
  } else if (to.path === '/blogs') {
    blogSettingsStore.clearBlogSettings()
  }

  next()
})

router.afterEach((to) => {
  document.title = to.meta.title || ''
  hidePageLoading()
})
