import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getBlogSettingsDetail } from '@/api/fronted/blogsettings.js'

export const useBlogSettingsStore = defineStore('blogsettings', () => {
  const blogSettings = ref({})

  function getBlogSettings(blogUsername) {
    if (!blogUsername) {
      blogSettings.value = {}
      return Promise.resolve({ success: false })
    }

    return getBlogSettingsDetail({ blogUsername }).then((res) => {
      if (res.success) {
        blogSettings.value = res.data || {}
      }
      return res
    })
  }

  function clearBlogSettings() {
    blogSettings.value = {}
  }

  function setBlogSettings(data) {
    blogSettings.value = { ...(data || {}) }
  }

  return { blogSettings, getBlogSettings, clearBlogSettings, setBlogSettings }
})
