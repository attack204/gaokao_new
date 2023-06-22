import request from '@/utils/request'

// 获取列表
export function getList(keyword, page, size) {
  return request({
    url: '/xhr/v1/users/',
    method: 'get',
    params: {
      keyword: keyword,
      page: page,
      size: size
    }
  })
}

// 新增
export function addUser(data) {
  return request({
    url: '/xhr/v1/users/create/',
    data: data,
    method: 'post'
  })
}

// 编辑，不可能更改角色信息
export function updateUser(data, id) {
  return request({
    url: '/xhr/v1/users/update/' + id,
    data: data,
    method: 'post'
  })
}

// 删除
export function delUser(id) {
  return request({
    url: '/xhr/v1/users/remove/' + id,
    method: 'delete'
  })
}


//更改角色信息

export function allocRoles(id, rolesList) {
  return request({
    url: '/xhr/v1/users/allocRoles',
    data: {
      userId: id,
      roleIds: rolesList
    },
    method: 'POST'
  })
}

/**
 * 默认角色信息小于10个，否则要进一步封装
 * @returns 
 */
export function getAllRoles() {
  return request({
    url: '/xhr/v1/roles/',
    method: 'GET'
  })
}