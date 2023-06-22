import request from '@/utils/request'

export function getList() {
  return request({
    url: '/xhr/v1/perms/',
    method: 'get'
  })
}

export function delPerm(id) {
  return request({
    url: 'xhr/v1/perms/remove/' + id,
    method: 'delete',
  })
}

export function updatePerm(data, id, flag) {
  if(flag == 1) {

    //新增
    return request({
      url: 'xhr/v1/perms/',
      data: data,
      method: 'post'
    })

  } else {

    //编辑
    return request({
      url: 'xhr/v1/perms/update/' + id,
      data: data,
      method: 'post'
    })
  }


}

// export function save(params) {
//   return request({
//     url: '/menu',
//     method: 'post',
//     params: params
//   })
// }

// export function delMenu(id) {
//   return request({
//     url: '/menu',
//     method: 'delete',
//     params: {
//       id: id
//     }
//   })
// }
export function menuTreeListByRoleId(roleId) {
  return request({
    url: '/menu/menuTreeListByRoleId',
    method: 'get',
    params: {
      roleId: roleId
    }
  })
}
