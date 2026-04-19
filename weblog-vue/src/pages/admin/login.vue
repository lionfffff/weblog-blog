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

const handleLogin = () => {
  formRef.value.validate((valid) => {
    if (!valid) return

    loading.value = true
    login(form.username, form.password)
      .then(async (res) => {
        if (!res.success) {
          showMessage(res.message || '登录失败', 'error')
          return
        }

        setToken(res.data.token)
        await userStore.setUserInfo()
        showMessage('登录成功', 'success')

        const redirect = typeof route.query.redirect === 'string' ? route.query.redirect : ''
        if (!redirect || redirect === '/blogs') {
          router.replace(`/u/${userStore.userInfo?.username || form.username}`)
          return
        }

        router.replace(redirect)
      })
      .finally(() => {
        loading.value = false
      })
  })
}
</script>
