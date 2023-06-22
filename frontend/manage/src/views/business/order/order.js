import { search, setOrderStatus, handleOrder, refund, doDelivery, rejectRefund } from '@/api/business/order'
import { ORDER_STATUS } from '@/enum/OrderStatus.js'
import OrderType from '@/enum/OrderType.js'
import date from '../../../utils/date'
import { REFUND_TYPE } from '@/api/refundType'

export default {
  data() {
    return {
      ORDER_STATUS,
      OrderType,
      listLoading: false,
      totalOrders: 0,
      dataList: [],
      step: 0,
      manageOrderRejectVisible: false,
      manageOrderRefundVisible: false,
      manageOrderRejectSelectVisible: false,
      manageOrderRefundSelectVisible: false,
      setOrderStatusLoading: false,
      setOrderRefundStatusLoading: false,
      form: {
        orderId: 1,
        opt: 'YES',
        userId: 0,
        reasons: '',
        refundType: REFUND_TYPE.WX_REFUND
      },
      refundForm: {
        orderId: 1,
        userId: 0,
        refundType: REFUND_TYPE.WX_REFUND
      },
      deliveryForm: {
        orderId: null
      },
      rejectRefundForm: {
        orderId: 1,
        reasons: ''
      },
      page: {
        orderId: null,
        userId: null,
        size: 30,
        page: 1
      }
    }
  },
  methods: {

    manageOrder(row) {
      this.manageOrderRejectVisible = true
      this.form.opt = 'YES'
      this.form.orderId = row.id
      this.form.reasons = ''
      this.form.userId = row.userId
      this.form.refundType = REFUND_TYPE.WX_REFUND
    },
    manageRefund(row) {
      this.manageOrderRefundVisible = true
      this.form.opt = 'YES'
      this.form.orderId = row.id
      this.form.reasons = ''
      this.form.userId = row.userId
    },

    handleOrder() {
      if (this.form.orderId !== undefined) {
        handleOrder(this.form).then(response => {
          if (response.code === 200) {
            this.$message({
              type: 'success',
              message: '操作成功'
            })
          }
          if (response) {
            this.$refs.form.resetFields()
          }
          this.manageOrderRejectVisible = false
          this.manageOrderRejectSelectVisible = false
          this.manageOrderRefundVisible = false
          this.manageOrderRefundSelectVisible = false
          this.listOrderByPage(1)
        })
      }
    },

    rejectRefund() {
      if (this.form.opt === 'YES') {
        this.refundForm.userId = this.form.userId
        this.refundForm.orderId = this.form.orderId
        refund(this.refundForm).then(response => {
          if (response.code === 200) {
            this.$message({
              type: 'success',
              message: '退款成功'
            })
          }
          if (response) {
            this.$refs.form.resetFields()
          }
          this.manageOrderRefundVisible = false
          this.manageOrderRefundSelectVisible = false
          this.listOrderByPage(1)
        })
      }
      if (this.form.opt === 'NO') {
        this.rejectRefundForm.orderId = this.form.orderId
        this.rejectRefundForm.reasons = this.form.reasons
        rejectRefund(this.rejectRefundForm).then(response => {
          if (response.code === 200) {
            this.$message({
              type: 'success',
              message: '已拒绝退款'
            })
          }
          if (response) {
            this.$refs.form.resetFields()
          }
          this.manageOrderRejectVisible = false
          this.manageOrderRejectSelectVisible = false
          this.manageOrderRefundVisible = false
          this.manageOrderRefundSelectVisible = false
          this.listOrderByPage(1)
        })
      }
    },
    // 发货
    toDelivery(row) {
      this.$confirm('是否确认开始服务?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return doDelivery(row.id)
      }).then(() => {
        this.listOrderByPage(1)
        this.$message({
          type: 'success',
          message: '已开始服务，服务完成后请提醒用户点击完成',
          duration: 5000
        })
      })
      // doDelivery(row.id).then(response => {
      //   if (response.code === 200) {
      //     this.$message({
      //       type: 'success',
      //       message: '操作成功'
      //     })
      //   }
      //   this.listByPage(1);
      // })
    },

    formatAppointmentTime(row) {
      if (row.appointmentDate === 0) {
        return '无'
      }
      const day = date.formatDate.format(new Date(row.appointmentDate), 'yyyy-MM-dd')
      const times = row.appointmentPeriod.split('-')
      return day + ' ' + times[0] + '时至' + times[1] + '时'
    },
    formatCreateTime: function(row) {
      return date.formatDate.format(new Date(row.createTime), 'yyyy-MM-dd hh:mm')
    },
    formatPayTime: function(row) {
      if (row.payTime === 0) {
        return '无'
      } else {
        return date.formatDate.format(new Date(row.payTime), 'yyyy-MM-dd hh:mm')
      }
    },
    handleCurrentChange(page) {
      this.listOrderByPage(page)
    },
    listOrderByPage(page) {
      if (page !== undefined) {
        this.page.page = page
      } else {
        this.page.page = 1
      }
      this.listLoading = true
      search(this.page).then(res => {
        console.log(res);
        if (res) {
          this.totalOrders = res.data.totalElements;
          this.dataList = res.data.content
          this.dataList.forEach((item) => {
            item.receiverAddress = item.receiverProvince + item.receiverCity + item.receiverRegion + item.receiverDetailAddress
            item.skuDesc = this.createSkuDescFromGoodVOList(item.goodsVOList)
          })
        }
        this.listLoading = false
      })
    },
    createSkuDescFromGoodVOList(goodVOList) {
      let desc = ''
      if (goodVOList != null) {
        goodVOList.forEach(vo => {
          vo.skuSpecs.forEach(spec => {
            desc = desc + ',' + spec.name + ':' + spec.value
          })
        })
        return desc.substr(1, desc.length - 1)
      }
      return '无'
    }
  },

  mounted() {
    this.listOrderByPage(1)
  }
}
