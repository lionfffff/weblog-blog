import axios from '@/axios'

export function getTagList(data) {
  return axios.post('/tag/list', data)
}

export function getTagArticlePageList(data) {
  return axios.post('/tag/article/list', data)
}
