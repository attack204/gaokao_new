import axios from 'axios'
import { Message } from 'element-ui'
import { getToken } from '@/utils/auth'
import { STATUS_CODE } from '@/api/statusCode'

const ui = require('element-ui')

const service = axios.create({
  baseURL: process.env.BASE_API,
  withCredentials: true,
  timeout: 20000
})

// request拦截器
service.interceptors.request.use(
  config => {
    const token = getToken()
    if (token) {
      config.headers['Authorization'] = token
    }
    return config
  },
  error => {
    console.log('error', error)
    return Promise.reject(error)
  }
)

// response拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code === STATUS_CODE.SUCCESS) {
      return response.data
    } else if (res.code === STATUS_CODE.UNAUTHORIZED) {
      if (getToken()) {
        Message({
          message: 'Token过期，请重新登录',
          type: 'error',
          duration: 3 * 1000
        })
      } else {
        Message({
          message: '请先登录',
          type: 'error',
          duration: 3 * 1000
        })
      }
    } else {
      ui.MessageBox({
        title: '错误提示',
        message: res.msg || '页面加载失败',
        type: 'error'
      }).then(r => {})
      return Promise.reject(response)
    }
  },
  error => {
    if (error.response && error.response.data.errors) {
      Message({
        message: error.response.data.errors[0].defaultMessage,
        type: 'error',
        duration: 5 * 1000
      })
    } else {
      if (error.response && error.response.data.message) {
        Message({
          message: error.response.data.message,
          type: 'error',
          duration: 5 * 1000
        })
      } else {
        Message({
          message: error.message,
          type: 'error',
          duration: 5 * 1000
        })
      }
    }
    return Promise.reject(error)
  }
)

export default service
