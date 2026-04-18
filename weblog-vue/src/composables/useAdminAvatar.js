import adminAvatar from '@/assets/avatar.png.jpg'
import { useBlogSettingsStore } from '@/stores/blogsettings'
import { useUserStore } from '@/stores/user'
import { computed, ref, watch } from 'vue'

export function useAdminAvatar() {
  const blogSettingsStore = useBlogSettingsStore()
  const userStore = useUserStore()
  const avatarLoadFailed = ref(false)

  const avatarSrc = computed(() => {
    if (avatarLoadFailed.value) return adminAvatar

    const blogAvatar = blogSettingsStore.blogSettings?.avatar
    if (typeof blogAvatar === 'string' && blogAvatar.trim()) {
      return blogAvatar
    }

    const userAvatar = userStore.userInfo?.avatar
    if (typeof userAvatar === 'string' && userAvatar.trim()) {
      return userAvatar
    }

    return adminAvatar
  })

  watch(
    () => [blogSettingsStore.blogSettings?.avatar, userStore.userInfo?.avatar],
    () => {
      avatarLoadFailed.value = false
    },
  )

  const handleAvatarError = () => {
    avatarLoadFailed.value = true
  }

  return {
    avatarSrc,
    handleAvatarError,
  }
}
