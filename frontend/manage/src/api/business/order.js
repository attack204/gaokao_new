import request from '@/utils/request'

export function search(params) {
  console.log(params);
  return request({
    url: '/xhr/v1/orders/',
    method: 'get',
    params
  })
}

export function handleOrder(params) {
  return request({
    url: '/xhr/v1/orders/handleOrder',
    method: 'post',
    data: params
  })
}

export function rejectRefund(params) {
  return request({
    url: '/xhr/v1/orders/rejectRefund',
    method: 'post',
    params
  })
}

export function doDelivery(orderId) {
  return request({
    url: '/xhr/v1/orders/doDelivery',
    method: 'post',
    params: {orderId: orderId}
  })
}

export function refund(data){
  return request({
    url:'/xhr/v1/orders/refund',
    method:'post',
    data: data
  })
}
