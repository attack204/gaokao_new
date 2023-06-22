import request from '@/utils/request';

export function getList(params) {
  return request({
    url: '/xhr/v1/roles/',
    method: 'get',
    params: params
  });
}

export function getRoleDropDown() {
  return request({
    url: '/xhr/v1/roles/dropdown',
    method: 'get',
  });
}

export function save(params, isAdd) {
  if (isAdd) {
    return request({
      url: '/xhr/v1/roles/',
      method: 'post',
      data: params
    });
  } else {
    return request({
      url: '/xhr/v1/roles/' + params.id,
      method: 'post',
      data: params
    });
  }
}

export function remove(roleId) {
  return request({
    url: '/xhr/v1/roles/' + roleId,
    method: 'delete'
  });
}

export function permTreeListByRoleId(params) {
  return request({
    url: '/xhr/v1/roles/queryPerms',
    method: 'post',
    data: {
      roleIds: [params]
    }
  });
}

export function savePermissons(params) {
  return request({
    url: '/xhr/v1/roles/allocPerms',
    method: 'post',
    data: params
  });
}
