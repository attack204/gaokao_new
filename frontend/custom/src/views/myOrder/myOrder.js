export default {
  data() {
    return {
      tableData: [{
        date: '2016-05-03',
        amount: '233.33',
        type: 'VX',
        remark: '没有备注'
      },
      {
        date: '2016-05-03',
        amount: '4.66',
        type: 'ZFB',
        remark: '没有备注qwq'
      }]
    }
  },
  methods: {
    initData(){
      
    }
  },
  mounted() {
    this.initData()
  },
}