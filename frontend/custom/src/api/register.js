import request from '@/utils/request'

export function register(formdata) {
  return request({
    url: '/xhr/v1/userMember/reg',
    method: 'post',
    data: formdata
    // data: {
    //   "password": password,
    //   "phone": phone,
    //   "veryCode": veryCode,
    //   "nickname": "qwq",
    //   "score": 233,
    //   "subject": "1231",
    //   "provinceRank": 1322
    // }
  });
}

export function sendVeryCode() {
  return request({
    url: '/xhr/v1/userMember/sendVerifyCode',
    method: 'get'
  });
}