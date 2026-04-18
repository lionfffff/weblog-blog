import axios from '@/axios'

export function getArticlePageList(data) {
  return axios.post('/article/list', data)
}

export function getArticleDetail(data) {
  return axios.post('/article/detail', data)
}
