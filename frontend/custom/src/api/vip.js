import request from '@/utils/request'
/**
 * 生成订单
 *
 */
export function generateOrder(userId) {
  return request({
    url: '/xhr/v1/users/orders/generateOrder?userId=' + userId,
    method: 'get'
  })
}

/**
 * 生成二维码
 *
 */
export function saveOrder(orderId) {
  return request({
    url: '/xhr/v1/users/orders/saveOrder?outTradeNo=' + orderId,
    method: 'get'
  })
}
/**
 * 取消订单
 */
export function cancelOrder(params) {
  return request({
    url: '/xhr/v1/orders/cancel',
    method: 'post',
    data: {
      orderId: params.orderId
    }
  })
}

/**
 * 充值成功
 */
export function success(orderId) {
  return request({
    url: '/xhr/v1/users/orders/success?orderId=' + orderId,
    method: 'post'
  })
}

/**
 * 查询订单状态
 */
export function getOrder(orderId) {
  return request({
    url: '/xhr/v1/users/orders/getStatus?orderId=' + orderId,
    method: 'post'
  })
}

