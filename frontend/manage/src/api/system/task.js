import request from '@/utils/request'

export function list(params) {
  return request({
    url: '/xhr/v1/tasks/list',
    method: 'get',
    params
  })
}

export function create(params) {
  return request({
    url: '/xhr/v1/tasks/create',
    method: 'post',
    data: params
  })
}

export function update(params) {
  return request({
    url: '/xhr/v1/tasks/update',
    method: 'post',
    data: params
  })
}

export function unlock(id) {
  return request({
    url: '/xhr/v1/tasks/unlock',
    method: 'get',
    params: {
      id: id
    }
  })
}

export function lock(id) {
  return request({
    url: '/xhr/v1/tasks/lock',
    method: 'get',
    params: {
      id: id
    }
  })
}
export function remove(id) {
  return request({
    url: '/xhr/v1/tasks/delete',
    method: 'get',
    params: {
      id: id
    }
  })
}

export function logList(params) {
  return request({
    url: '/task/logList',
    method: 'get',
    params
  })
}


