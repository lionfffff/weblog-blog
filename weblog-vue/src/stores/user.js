import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getUserInfo } from '@/api/admin/user'
import { removeToken } from '@/composables/auth'

export const useUserStore = defineStore(
  'user',
  () => {
    const userInfo = ref({})

    function setUserInfo() {
      return getUserInfo().then((res) => {
        if (res.success === true) {
          userInfo.value = res.data || {}
        }
        return res
      })
    }

    function logout() {
      removeToken()
      userInfo.value = {}
    }

    return { userInfo, setUserInfo, logout }
  }
)
