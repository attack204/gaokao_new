const ORDER_STATUS = Object.freeze({
  ALL: {
    value: -1,
    desc: '所有订单'
  },

  READY_FOR_PAY: {
    value: 1,
    desc: '待支付'
  },

  CANCELED: {
    value: 2,
    desc: '已取消'
  },

  PAID_SUCCESS: {
    value: 5,
    desc: '支付成功,待商家接单'
  },

  REJECT_ORDER: {
    value: 8,
    desc: '商家拒绝接单'
  },

  WAIT_FOR_DELIVER: {
    value: 11,
    desc: '待发货'
  },

  DELIVERED: {
    value: 12,
    desc: '已发货'
  },

  APPLY_FOR_REFUND: {
    value: 14,
    desc: '退款申请中'
  },

  REFUND_WAITING_NOTIFY: {
    value: 15,
    desc: '已退款，待确认'
  },

  REFUND_REJECT: {
    value: 17,
    desc: '商家拒绝退款'
  },

  REFUND_SUCCESS: {
    value: 20,
    desc: '退款成功'
  },

  CLOSED: {
    value: 25,
    desc: '已关闭'
  },

  COMPLETED: {
    value: 30,
    desc: '确认收货，已完成'
  },

  EVALUATED: {
    value: 35,
    desc: '已评价'
  }
})

function getDescFromStatus(orderStatus) {
  let orderKey
  for (orderKey in ORDER_STATUS) {
    if (ORDER_STATUS[orderKey].value === orderStatus) {
      return ORDER_STATUS[orderKey].desc
    }
  }
  return '未知订单状态'
}
export {
  ORDER_STATUS,
  getDescFromStatus
}
