import { getOverview } from '@/api/dashboard/dashboard'

export default {
  data() {
    return {
      allRegisteredUser: '暂无数据',
      allRegisteredVip: '暂无数据'
    }
  },
  methods: {
    initData() {
      // getOverview().then(response => {
      //   const vo = response.data
      //   if (vo) {
      //     this.allRegisteredUser = vo.allRegisteredUser
      //     this.allRegisteredVip = vo.allRegisteredVip
      //   }
      // })
    }
  },
  mounted() {
    console.log('mounted')
    this.initData()
  }
}
