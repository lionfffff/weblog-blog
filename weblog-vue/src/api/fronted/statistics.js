import axios from '@/axios'

export function getStatisticsInfo(data) {
  return axios.post('/statistics/info', data)
}

export function getStatisticsPVTrend(data) {
  return axios.post('/statistics/pv/trend', data)
}
