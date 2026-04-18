import axios from '@/axios'

export function login(username, password) {
  return axios.post('/login', { username, password })
}

export function register(data) {
  return axios.post('/register', data)
}

export function getUserInfo() {
  return axios.post('/admin/user/info')
}

export function updateAdminPassword(data) {
  return axios.post('/admin/password/update', data)
}
