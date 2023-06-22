import request from '@/utils/request'

export function getOverview() {
  return request({
    url: '/xhr/v1/dashboard/',
    method: 'GET'
  })
}
