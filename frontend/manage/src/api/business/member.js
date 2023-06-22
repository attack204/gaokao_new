import request from '@/utils/request'

/**
 * 获取会员列表
 */
export function getMemberList(params) {
  return request({
    url: '/xhr/v1/usermembers/',
    method: 'get',
    params: params
  })
}

/**
 * 锁定会员
 */
export function lockMember(id) {
  return request({
    url: '/xhr/v1/usermembers/lock/'+id,
    method: 'post'
  })
}

/**
 * 解锁会员
 */
export function unlockMember(id) {
  return request({
    url: '/xhr/v1/usermembers/unlock/'+id,
    method: 'post'
  })
}
