import axios from '@/axios'

export function getBlogSettingsDetail(data) {
  return axios.post('/blog/settings/detail', data)
}

export function getBlogPortalList() {
  return axios.post('/blog/portal/list')
}
