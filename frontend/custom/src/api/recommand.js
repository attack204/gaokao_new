import request from '@/utils/request'

export function getRank(score) {
  return request({
    url: '/xhr/v1/advise/getrank/' + score,
    method: 'get'
  })
}

export function autoGenerateForm(params) {
  return request({
    url: '/xhr/v1/advise/autoGenerateForm',
    method: 'post',
    data: params
  })
}