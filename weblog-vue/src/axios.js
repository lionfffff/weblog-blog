import axios from 'axios'
import { getToken } from './composables/auth'
import { showMessage } from '@/composables/util.js'
import { useUserStore } from '@/stores/user.js'

const SILENT_FRONTEND_ENDPOINTS = [
  '/blog/settings/detail',
  '/blog/portal/list',
  '/article/list',
  '/search/list',
  '/category/list',
  '/tag/list',
  '/archive/list',
  '/statistics/info',
  '/statistics/pv/trend',
]

function resolveBaseURL() {
  if (typeof window === 'undefined') return '/api'

  const { hostname } = window.location
  if (hostname === 'myweblog.site' || hostname === 'www.myweblog.site') {
    return 'https://api.myweblog.site'
  }

  return '/api'
}

const instance = axios.create({
  baseURL: resolveBaseURL(),
  timeout: 20000,
})

instance.interceptors.request.use(
  function (config) {
    const token = getToken()

    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }

    return config
  },
  function (error) {
    return Promise.reject(error)
  },
)

instance.interceptors.response.use(
  function (response) {
    return response.data
  },
  function (error) {
    const response = error.response
    const status = response?.status

    if (status === 401) {
      const userStore = useUserStore()
      userStore.logout()
      location.reload()
    }

    const requestUrl = error.config?.url || ''
    const shouldSilence = SILENT_FRONTEND_ENDPOINTS.some((prefix) => requestUrl.startsWith(prefix))
    if (shouldSilence) {
      return Promise.reject(error)
    }

    const errorMsg =
      response?.data?.message ||
      (error.code === 'ECONNABORTED' ? '请求超时，请稍后重试' : '请求失败')

    showMessage(errorMsg, 'error')
    return Promise.reject(error)
  },
)

export default instance
