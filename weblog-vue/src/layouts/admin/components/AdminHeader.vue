<template>
  <div class="header-shell">
    <div class="header-card">
      <div class="header-left">
        <button class="icon-button" type="button" @click="handleMenuWidth" aria-label="切换侧边栏">
          <el-icon>
            <Fold v-if="menuStore.menuWidth === '250px'" />
            <Expand v-else />
          </el-icon>
        </button>

        <div class="header-copy">
          <span class="header-eyebrow">Admin</span>
          <h2>内容管理后台</h2>
        </div>
      </div>

      <div class="header-actions">
        <el-tooltip effect="dark" content="刷新页面" placement="bottom">
          <button class="icon-button" type="button" @click="handleRefresh" aria-label="刷新页面">
            <el-icon><Refresh /></el-icon>
          </button>
        </el-tooltip>

        <el-tooltip effect="dark" content="前往前台" placement="bottom">
          <button class="icon-button" type="button" @click="goToMyBlog" aria-label="前往前台">
            <el-icon><Monitor /></el-icon>
          </button>
        </el-tooltip>

        <el-tooltip effect="dark" :content="isFullscreen ? '退出全屏' : '进入全屏'" placement="bottom">
          <button class="icon-button" type="button" @click="toggle" :aria-label="isFullscreen ? '退出全屏' : '进入全屏'">
            <el-icon>
              <Aim v-if="isFullscreen" />
              <FullScreen v-else />
            </el-icon>
          </button>
        </el-tooltip>

        <el-dropdown class="user-dropdown" @command="handleCommand">
          <button class="user-button" type="button">
            <el-avatar :size="34" :src="avatarSrc" @error="handleAvatarError" />
            <div class="user-copy">
              <span class="user-name">{{ userStore.userInfo.username }}</span>
              <span class="user-role">管理员</span>
            </div>
            <el-icon class="user-arrow"><ArrowDown /></el-icon>
          </button>

          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="updatePassword">修改密码</el-dropdown-item>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </div>

  <FormDialog ref="formDialogRef" title="修改密码" destroyOnClose @submit="onSubmit">
    <el-form ref="formRef" :rules="rules" :model="form">
      <el-form-item label="用户名" prop="username" label-width="120px" size="large">
        <el-input v-model="form.username" placeholder="请输入用户名" clearable disabled />
      </el-form-item>
      <el-form-item label="新密码" prop="password" label-width="120px" size="large">
        <el-input v-model="form.password" type="password" placeholder="请输入新密码" clearable show-password />
      </el-form-item>
      <el-form-item label="确认新密码" prop="rePassword" label-width="120px" size="large">
        <el-input v-model="form.rePassword" type="password" placeholder="请再次输入新密码" clearable show-password />
      </el-form-item>
    </el-form>
  </FormDialog>
</template>

<script setup>
import adminAvatar from '@/assets/avatar.png.jpg'
import { computed, reactive, ref, watch } from 'vue'
import { useMenuStore } from '@/stores/menu'
import { useUserStore } from '@/stores/user'
import { useFullscreen } from '@vueuse/core'
import { updateAdminPassword } from '@/api/admin/user'
import { getBlogSettingsDetail } from '@/api/fronted/blogsettings'
import { showMessage, showModel } from '@/composables/util'
import { useRouter } from 'vue-router'
import FormDialog from '@/components/FormDialog.vue'
import { Aim, ArrowDown, Expand, Fold, FullScreen, Monitor, Refresh } from '@element-plus/icons-vue'

const router = useRouter()
const { isFullscreen, toggle } = useFullscreen()
const menuStore = useMenuStore()
const userStore = useUserStore()
const adminBlogAvatar = ref('')
const avatarLoadFailed = ref(false)

const avatarSrc = computed(() => {
  if (avatarLoadFailed.value) return adminAvatar

  const ownBlogAvatar = adminBlogAvatar.value
  if (typeof ownBlogAvatar === 'string' && ownBlogAvatar.trim()) {
    return ownBlogAvatar
  }

  const userAvatar = userStore.userInfo?.avatar
  if (typeof userAvatar === 'string' && userAvatar.trim()) {
    return userAvatar
  }

  return adminAvatar
})

const handleAvatarError = () => {
  avatarLoadFailed.value = true
}

const loadAdminBlogAvatar = () => {
  const username = userStore.userInfo?.username
  if (!username) {
    adminBlogAvatar.value = ''
    return
  }

  getBlogSettingsDetail({ blogUsername: username })
    .then((res) => {
      if (res.success) {
        adminBlogAvatar.value = res.data?.avatar || ''
      }
    })
    .catch(() => {
      adminBlogAvatar.value = ''
    })
}

const handleMenuWidth = () => {
  menuStore.handleMenuWidth()
}

const handleRefresh = () => location.reload()

const goToMyBlog = () => {
  const username = userStore.userInfo?.username
  if (username) {
    router.push(`/u/${username}`)
    return
  }

  router.push('/blogs')
}

const formDialogRef = ref(false)
const formRef = ref(null)

const handleCommand = (command) => {
  if (command === 'updatePassword') {
    formDialogRef.value.open()
  } else if (command === 'logout') {
    logout()
  }
}

function logout() {
  showModel('是否确认要退出登录？').then(() => {
    userStore.logout()
    showMessage('退出登录成功')
    router.push('/login')
  })
}

const form = reactive({
  username: userStore.userInfo.username || '',
  password: '',
  rePassword: '',
})

watch(
  () => userStore.userInfo.username,
  (newValue) => {
    form.username = newValue
    avatarLoadFailed.value = false
    loadAdminBlogAvatar()
  },
  { immediate: true },
)

const rules = {
  username: [{ required: true, message: '用户名不能为空', trigger: 'blur' }],
  password: [{ required: true, message: '密码不能为空', trigger: 'blur' }],
  rePassword: [{ required: true, message: '确认密码不能为空', trigger: 'blur' }],
}

const onSubmit = () => {
  formRef.value.validate((valid) => {
    if (!valid) {
      return false
    }

    if (form.password !== form.rePassword) {
      showMessage('两次输入的新密码不一致，请检查', 'warning')
      return
    }

    formDialogRef.value.showBtnLoading()
    updateAdminPassword(form)
      .then((res) => {
        if (res.success === true) {
          showMessage('密码修改成功，请重新登录')
          userStore.logout()
          formDialogRef.value.close()
          router.push('/login')
        } else {
          showMessage(res.message, 'error')
        }
      })
      .finally(() => formDialogRef.value.closeBtnLoading())
  })
}
</script>

<style scoped>
.header-shell {
  width: 100%;
}

.header-card {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  width: 100%;
  min-height: var(--admin-header-height, 72px);
  padding: 14px 16px 14px 18px;
  border-radius: 28px;
  overflow: hidden;
  isolation: isolate;
  background: #ffffff;
  box-shadow: 0 18px 42px rgba(15, 23, 42, 0.08);
}

.header-card::before {
  content: '';
  position: absolute;
  inset: 0;
  border: 1px solid rgba(226, 232, 240, 0.94);
  border-radius: inherit;
  pointer-events: none;
}

.header-card::after {
  content: '';
  position: absolute;
  left: 18px;
  right: 18px;
  top: 0;
  height: 1px;
  background: rgba(241, 245, 249, 0.98);
  pointer-events: none;
}

.header-left,
.header-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.header-copy {
  display: flex;
  flex-direction: column;
}

.header-eyebrow {
  color: #94a3b8;
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.16em;
  text-transform: uppercase;
}

.header-copy h2 {
  margin: 3px 0 0;
  color: #0f172a;
  font-size: 20px;
  font-weight: 700;
  letter-spacing: -0.03em;
}

.icon-button,
.user-button {
  border: 0;
  outline: 0;
  background: none;
  cursor: pointer;
}

.icon-button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 42px;
  height: 42px;
  border: 1px solid rgba(226, 232, 240, 0.9);
  border-radius: 14px;
  background: rgba(248, 250, 252, 0.92);
  color: #334155;
  transition: all 0.2s ease;
}

.icon-button:hover {
  background: #fff;
  color: #0f172a;
  transform: translateY(-1px);
  box-shadow: 0 12px 24px rgba(15, 23, 42, 0.08);
}

.user-button {
  display: inline-flex;
  align-items: center;
  gap: 12px;
  min-width: 180px;
  padding: 6px 10px 6px 8px;
  border: 1px solid rgba(226, 232, 240, 0.9);
  border-radius: 18px;
  background: rgba(255, 255, 255, 0.9);
  color: #0f172a;
  transition: all 0.2s ease;
}

.user-button:hover {
  box-shadow: 0 14px 28px rgba(15, 23, 42, 0.08);
}

.user-copy {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  line-height: 1.2;
}

.user-name {
  color: #0f172a;
  font-size: 14px;
  font-weight: 700;
}

.user-role {
  margin-top: 3px;
  color: #64748b;
  font-size: 11px;
}

.user-arrow {
  margin-left: auto;
  color: #94a3b8;
}

@media (max-width: 900px) {
  .header-card {
    flex-direction: column;
    align-items: stretch;
  }

  .header-left,
  .header-actions {
    justify-content: space-between;
  }

  .user-button {
    min-width: 0;
    width: 100%;
  }
}
</style>

