<template>
  <AuthSplitLayout
    panel-label="账号注册"
    title="创建管理员账号"
    description="注册成功后会自动生成独立博客设置，登录即可进入后台开始管理。"
    footer-text="已经有账号了？"
    footer-action-text="去登录"
    footer-action-to="/login"
  >
    <el-form ref="formRef" :model="form" :rules="rules" class="auth-form" label-position="top">
      <el-form-item prop="username" label="用户名">
        <el-input v-model="form.username" size="large" placeholder="3 到 20 位字符" class="auth-input" />
      </el-form-item>

      <el-form-item prop="password" label="密码">
        <el-input
          v-model="form.password"
          size="large"
          type="password"
          show-password
          placeholder="6 到 32 位字符"
          class="auth-input"
        />
      </el-form-item>

      <el-form-item prop="confirmPassword" label="确认密码">
        <el-input
          v-model="form.confirmPassword"
          size="large"
          type="password"
          show-password
          placeholder="请再次输入密码"
          class="auth-input"
          @keyup.enter="handleRegister"
        />
      </el-form-item>

      <el-button class="primary-button" :loading="loading" @click="handleRegister">
        {{ loading ? '注册中...' : '注册账号' }}
      </el-button>
    </el-form>
  </AuthSplitLayout>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { register } from '@/api/admin/user'
import AuthSplitLayout from '@/components/AuthSplitLayout.vue'
import { showMessage } from '@/composables/util'

const router = useRouter()
const loading = ref(false)
const formRef = ref(null)
const form = reactive({
  username: '',
  password: '',
  confirmPassword: '',
})

const validateConfirmPassword = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请再次输入密码'))
    return
  }

  if (value !== form.password) {
    callback(new Error('两次输入的密码不一致'))
    return
  }

  callback()
}

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  confirmPassword: [{ validator: validateConfirmPassword, trigger: 'blur' }],
}

const handleRegister = () => {
  formRef.value.validate((valid) => {
    if (!valid) return

    loading.value = true
    register({
      username: form.username,
      password: form.password,
    })
      .then((res) => {
        if (res.success) {
          showMessage('注册成功，请登录', 'success')
          router.replace('/login')
          return
        }

        showMessage(res.message || '注册失败', 'error')
      })
      .finally(() => {
        loading.value = false
      })
  })
}
</script>
