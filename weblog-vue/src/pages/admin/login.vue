<template>
  <AuthSplitLayout
    panel-label="管理员登录"
    title="登录到 WeBlog"
    description="输入管理员账号后，将直接进入后台工作台。"
    footer-text="还没有账号？"
    footer-action-text="立即注册"
    footer-action-to="/register"
  >
    <el-form ref="formRef" :model="form" :rules="rules" class="auth-form" label-position="top">
      <el-form-item prop="username" label="用户名">
        <el-input v-model="form.username" size="large" placeholder="请输入用户名" class="auth-input" />
      </el-form-item>

      <el-form-item prop="password" label="密码">
        <el-input
          v-model="form.password"
          size="large"
          type="password"
          show-password
          placeholder="请输入密码"
          class="auth-input"
          @keyup.enter="handleLogin"
        />
      </el-form-item>

      <el-button class="primary-button" :loading="loading" @click="handleLogin">
        {{ loading ? '登录中...' : '登录' }}
      </el-button>
    </el-form>
  </AuthSplitLayout>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { login } from '@/api/admin/user'
import AuthSplitLayout from '@/components/AuthSplitLayout.vue'
import { setToken } from '@/composables/auth'
import { showMessage } from '@/composables/util'
import { useUserStore } from '@/stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)
const formRef = ref(null)

const form = reactive({
  username: '',
  password: '',
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
}

const sleep = (ms) => new Promise((resolve) => setTimeout(resolve, ms))

const waitForBackend = async (attempts = 4, delayMs = 1200) => {
  for (let index = 0; index < attempts; index += 1) {
    try {
      const response = await fetch('/api/health', {
        method: 'GET',
        cache: 'no-store',
      })

      if (response.ok) {
        return true
      }
    } catch {
      // ignore and retry
    }

    if (index < attempts - 1) {
      await sleep(delayMs)
    }
  }

  return false
}

const loginWithRetry = async (attempts = 4, delayMs = 1200) => {
  let lastMessage = '登录失败，请稍后重试'

  for (let index = 0; index < attempts; index += 1) {
    try {
      const res = await login(form.username, form.password)
      if (res?.success) {
        return res
      }

      lastMessage = res?.message || lastMessage
    } catch (error) {
      lastMessage = error?.response?.data?.message || lastMessage
    }

    if (index < attempts - 1) {
      await sleep(delayMs)
    }
  }

  throw new Error(lastMessage)
}

const handleLogin = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return

    loading.value = true
    try {
      await waitForBackend()
      const res = await loginWithRetry()

      setToken(res.data.token)
      userStore.setUserInfoPartial({ username: form.username })
      userStore.setUserInfo()
      showMessage('登录成功', 'success')

      const redirect = typeof route.query.redirect === 'string' ? route.query.redirect : ''
      if (!redirect || redirect === '/blogs') {
        router.replace(`/u/${form.username}`)
        return
      }

      router.replace(redirect)
    } catch (error) {
      showMessage(error?.message || '登录失败，请稍后重试', 'error')
    } finally {
      loading.value = false
    }
  })
}
</script>
