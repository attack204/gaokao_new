import request from '@/utils/request'
export function policy() {
  return request({
    url:'xhr/v1/aliyun/oss/policy',
    method:'get',
  })
}

export function upload(file) {
  return request({
    url:'xhr/v1/aliyun/oss/upload',
    method:'post',
    data: file,
    headers: {
      "Content-Type": "multipart/form-data"
    }
  })
}
