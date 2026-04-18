import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useCurrentBlogStore = defineStore(
  'currentBlog',
  () => {
    const username = ref('')

    function setCurrentBlog(blogUsername) {
      username.value = blogUsername || ''
    }

    function clearCurrentBlog() {
      username.value = ''
    }

    return {
      username,
      setCurrentBlog,
      clearCurrentBlog,
    }
  }
)
