import date from "../../../utils/date";
import {getSpiderList, startSpider} from "../../../api/business/spider";

export default {
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      //列表
      spiderList: [],
      //总条数
      total: 0,
      //请求分类列表的参数
      listQuery: {
        page: 1,
        limit: 5,
        total: 0,
      },
      form: {},
    }
  },

  created() {
    this.fetchData();
  },

  methods: {
    //请求列表数据
    fetchData() {
      this.loading = true;
      getSpiderList(this.listQuery).then(response => {
        this.spiderList = response.data.content;
        this.loading = false;
        this.total = response.data.totalElements;
        this.listQuery.total = response.data.totalPages;
      })
    },
    /** 更新按钮操作 */
    handleFresh() {
      this.listQuery.page = 1;
      this.fetchData();
    },
    /** 爬取按钮操作 */
    handleSpider() {
      startSpider().then(response => {
        if (response.code === 200) {
          this.$message({
            type: 'success',
            message: '爬取信息成功'
          })
          this.fetchData();
        } else {
          this.$message({
            type: 'error',
            message: '爬取信息失败'
          })
        }
      })
    },
    // 日期格式化
    parseTime(time, pattern) {
      if (arguments.length === 0 || !time) {
        return null
      }
      if (time === -1) {
        return "尚未完成"
      }
      const format = pattern || '{y}-{m}-{d} {h}:{i}:{s}'
      let date
      if (typeof time === 'object') {
        date = time
      } else {
        if ((typeof time === 'string') && (/^[0-9]+$/.test(time))) {
          time = parseInt(time)
        } else if (typeof time === 'string') {
          time = time.replace(new RegExp(/-/gm), '/');
        }
        if ((typeof time === 'number') && (time.toString().length === 10)) {
          time = time * 1000
        }
        date = new Date(time)
      }
      const formatObj = {
        y: date.getFullYear(),
        m: date.getMonth() + 1,
        d: date.getDate(),
        h: date.getHours(),
        i: date.getMinutes(),
        s: date.getSeconds(),
        a: date.getDay()
      }
      const time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key) => {
        let value = formatObj[key]
        // Note: getDay() returns 0 on Sunday
        if (key === 'a') {
          return ['日', '一', '二', '三', '四', '五', '六'][value]
        }
        if (result.length > 0 && value < 10) {
          value = '0' + value
        }
        return value || 0
      })
      return time_str
    },

    // 分页需要的4个方法
    fetchNext() {
      this.listQuery.page = this.listQuery.page + 1
      this.fetchData()
    },
    fetchPrev() {
      this.listQuery.page = this.listQuery.page - 1
      this.fetchData()
    },
    fetchPage(page) {
      this.listQuery.page = page
      this.fetchData()
    },
    changeSize(limit) {
      this.listQuery.limit = limit
      this.fetchData()
    },
  }
}
