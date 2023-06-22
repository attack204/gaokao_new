const OrderType = Object.freeze({
  ALL: {
    code: -1,
    desc: '全部'
  },
  TO_SHOP: {
    code: 0,
    desc: '到店'
  },
  TO_DOOR: {
    code: 1,
    desc: '上门'
  }
});

export default OrderType
