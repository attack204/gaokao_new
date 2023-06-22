import { lockMember, unlockMember, getMemberList } from '@/api/business/member'
import date from '../../../utils/date'

export default {
  data() {
    return {
      dataList: [],
      page: {
        size: 10,
        page: 1,
        keyword: ''
      },
      // 查询参数
      pos: {
        keyword: '',
        page: 1,
        nickname: '',
        phone: '',
        location: ''
      },
      params: {},
      loading: true
    }
  },
  methods: {
    /** 查询岗位列表 */
    getList() {
      this.loading = true
      getMemberList(this.page).then(response => {
        this.dataList = response.data.content
        // this.total = response.total
        this.loading = false
      }).catch()
    },
    //
    listByPage(page) {
      if (page !== undefined) {
        this.page.page = page
      } else {
        this.page.page = 1
      }
      this.loading = true
      getMemberList(this.page).then(res => {
        if (res) {
          // this.page.total = res.data.totalPages
          this.dataList = res.data.content
        }
        this.loading = false
      })
    },
    // 锁定状态格式化
    statusFormatter(row, column) {
      const status = row.status
      if (status === 0) {
        return '正常'
      } else {
        return '锁定'
      }
    },
    // 时间格式化
    formatDate1: function(row) {
      return date.formatDate.format(
        new Date(row.createTime),
        'yyyy-MM-dd hh:mm'
      )
    },
    formatDate2: function(row) {
      if (row.updateTime === 0) {
        return date.formatDate.format(
          new Date(row.createTime),
          'yyyy-MM-dd hh:mm'
        )
      } else {
        return date.formatDate.format(
          new Date(row.updateTime),
          'yyyy-MM-dd hh:mm'
        )
      }
    },
    // 搜索功能实现
    handleQuery() {
      this.page.page = 1
      this.getList(this.page)
    },
    // 重置功能
    resetDate() {
      this.pos = ''
      this.dataList = this.getMemberList()
    },

    handleCurrentChange(page) {
      this.listByPage(page)
    },
    // 开关滑块
    changeSwitch(row) {
      if (row.status === 1) {
        lockMember(row.id).then(response => {
          if (response) {
            // this.$refs.datalist.resetFields();
            // row.status=1
            row.status = 1
          }
        })
      } else {
        unlockMember(row.id).then(response => {
          if (response) {
            // this.$refs.datalist.resetFields();
            row.status = 0
          }
        })
      }
    }
  },

  mounted() {
    this.listByPage()
  },
  // created(){
  //   this.getList()
  // }

}
