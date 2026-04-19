import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getUserInfo } from '@/api/admin/user'
import { removeToken } from '@/composables/auth'

export const useUserStore = defineStore(
  'user',
  () => {
    const userInfo = ref({})

    function setUserInfoPartial(data) {
      userInfo.value = {
        ...(userInfo.value || {}),
        ...(data || {}),
      }
    }

    function setUserInfo() {
      return getUserInfo()
        .then((res) => {
          if (res.success === true) {
            userInfo.value = res.data || {}
          }
          return res
        })
        .catch(() => {
          return { success: false }
        })
    }

    function logout() {
      removeToken()
      userInfo.value = {}
    }

    return { userInfo, setUserInfo, setUserInfoPartial, logout }
  }
)
