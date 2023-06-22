import request from '@/utils/request'

export function login(username, password) {
  return request({
    url: '/xhr/v1/users/login',
    method: 'post',
    data: {
      "username": username,
      "password": password
    },
  })
}

export function getInfo() {
  return request({
    url: '/xhr/v1/users/info',
    method: 'get'
  })
}

export function logout() {
  console.log('logout')
  return request({
    url: '/xhr/v1/users/logout',
    method: 'get'
  })
}

export function updatePwd(params) {
  return request({
    url: '/xhr/v1/users/changePwd',
    method: 'post',
    data: {
      originPwd: params.oldPassword,
      newPwd: params.password
    }
  })
}
