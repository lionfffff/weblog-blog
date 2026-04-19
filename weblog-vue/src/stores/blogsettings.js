import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getBlogSettingsDetail } from '@/api/fronted/blogsettings.js'
import { getCachedValue, removeCachedValue, setCachedValue } from '@/composables/frontendCache'

const pendingRequests = new Map()
const BLOG_SETTINGS_CACHE_MS = 5 * 60 * 1000

export const useBlogSettingsStore = defineStore('blogsettings', () => {
  const blogSettings = ref({})

  function getBlogSettings(blogUsername) {
    if (!blogUsername) {
      blogSettings.value = {}
      return Promise.resolve({ success: false })
    }

    const cacheKey = `blog-settings:${blogUsername}`
    const cached = getCachedValue(cacheKey, BLOG_SETTINGS_CACHE_MS)
    if (cached) {
      blogSettings.value = { ...cached }
      return Promise.resolve({ success: true, data: cached, cached: true })
    }

    if (pendingRequests.has(cacheKey)) {
      return pendingRequests.get(cacheKey)
    }

    const request = getBlogSettingsDetail({ blogUsername })
      .then((res) => {
        if (res.success) {
          blogSettings.value = res.data || {}
          setCachedValue(cacheKey, blogSettings.value)
        }
        return res
      })
      .finally(() => {
        pendingRequests.delete(cacheKey)
      })

    pendingRequests.set(cacheKey, request)
    return request
  }

  function clearBlogSettings() {
    blogSettings.value = {}
  }

  function setBlogSettings(data) {
    blogSettings.value = { ...(data || {}) }
  }

  function invalidateBlogSettings(blogUsername) {
    if (!blogUsername) return
    removeCachedValue(`blog-settings:${blogUsername}`)
  }

  return { blogSettings, getBlogSettings, clearBlogSettings, setBlogSettings, invalidateBlogSettings }
})
