import { chartOrder30Api, chartOrderWeekApi, chartOrderMonthApi, chartOrderYearApi }  from "../../../../api/dashboard/dashboard";
import echartsFrom from "../../../../components/echarts/index";
export default {
  components: {echartsFrom},
  data() {
    return {
      visitDate: 'last30',
      series: [],
      xAxis: [],
      info: {},
      legendData: [],
      yAxisData: []
    }
  },
  mounted(){
    this.yAxisData = [
      {
        type: 'value',
        name: '数量',
        axisLine: {
          show: false
        },
        axisTick: {
          show: false
        },
        axisLabel: {
          textStyle: {
            color: '#7F8B9C'
          }
        },
        splitLine: {
          show: true,
          lineStyle: {
            color: '#F5F7F9'
          }
        }
      }
    ]
  },
  methods: {
    // 时间改变
    handleChangeVisitType() {
      this.xAxis = []
      this.legendData = []
      chartOrder30Api().then(async res => {
        this.info = res
        let qualitys = []
        for (let key  in res.quality) {
          qualitys.push(Number(res.quality[key]))
        }
        this.legendData = ['订单数']
        this.series = [
          {
            name:"订单数",
            type:"line",
            itemStyle:{
              "normal":{
                "color":{
                  "x":0,
                  "y":0,
                  "x2":0,
                  "y2":1,
                  "colorStops":[
                    {
                      "offset":0,
                      "color":"#6fdeab"
                    },
                    {
                      "offset":0.5,
                      "color":"#44d693"
                    },
                    {
                      "offset":1,
                      "color":"#2cc981"
                    }
                  ]
                }
              }
            },
            yAxisIndex: 1,
            data: qualitys
          }]
      })
    },
    handleChangeWeek() {
      this.xAxis = []
      this.legendData = []
      chartOrderWeekApi().then(async res => {
        this.info = res
        this.legendData = ["上周订单数", "本周订单数"]
        let qualitys = []
        let preQuality = []
        for (let key  in res.preQuality) {
          preQuality.push(Number(res.preQuality[key]))
        }
        for (let key  in res.quality) {
          qualitys.push(Number(res.quality[key]))
        }
        this.series = [
          {
            name:"上周订单数",
            type:"line",
            itemStyle:{
              "normal":{
                "color":{
                  "x":0,
                  "y":0,
                  "x2":0,
                  "y2":1,
                  "colorStops":[
                    {
                      "offset":0,
                      "color":"#6fdeab"
                    },
                    {
                      "offset":0.5,
                      "color":"#44d693"
                    },
                    {
                      "offset":1,
                      "color":"#2cc981"
                    }
                  ]
                }
              }
            },
            yAxisIndex: 1,
            data: preQuality
          },
          {
            name:"本周订单数",
            type:"line",
            itemStyle:{
              "normal":{
                "color":{
                  "x":0,
                  "y":0,
                  "x2":0,
                  "y2":1,
                  "colorStops":[
                    {
                      "offset":0,
                      "color":"#6fdeab"
                    },
                    {
                      "offset":0.5,
                      "color":"#44d693"
                    },
                    {
                      "offset":1,
                      "color":"#2cc981"
                    }
                  ]
                }
              }
            },
            yAxisIndex: 1,
            data: qualitys
          }
        ]
      })
    },
    handleChangeMonth() {
      this.xAxis = []
      this.legendData = []
      chartOrderMonthApi().then(async res => {
        this.info = res
        this.legendData = ["上月订单数", "本月订单数"]
        let qualitys = []
        let preQuality = []
        for (let key  in res.preQuality) {
          preQuality.push(Number(res.preQuality[key]))
        }
        for (let key  in res.quality) {
          qualitys.push(Number(res.quality[key]))
        }
        this.series = [
          {
            name:"上月订单数",
            type:"line",
            itemStyle:{
              "normal":{
                "color":{
                  "x":0,
                  "y":0,
                  "x2":0,
                  "y2":1,
                  "colorStops":[
                    {
                      "offset":0,
                      "color":"#6fdeab"
                    },
                    {
                      "offset":0.5,
                      "color":"#44d693"
                    },
                    {
                      "offset":1,
                      "color":"#2cc981"
                    }
                  ]
                }
              }
            },
            yAxisIndex: 1,
            data: preQuality
          },
          {
            name:"本月订单数",
            type:"line",
            itemStyle:{
              "normal":{
                "color":{
                  "x":0,
                  "y":0,
                  "x2":0,
                  "y2":1,
                  "colorStops":[
                    {
                      "offset":0,
                      "color":"#6fdeab"
                    },
                    {
                      "offset":0.5,
                      "color":"#44d693"
                    },
                    {
                      "offset":1,
                      "color":"#2cc981"
                    }
                  ]
                }
              }
            },
            yAxisIndex: 1,
            data: qualitys
          }
        ]
      })
    },
    handleChangeYear() {
      this.xAxis = []
      this.legendData = []
      chartOrderYearApi().then(async res => {
        this.info = res
        this.legendData =  ["去年订单数", "今年订单数"]
        let qualitys = []
        let preQuality = []
        for (let key  in res.preQuality) {
          preQuality.push(Number(res.preQuality[key]))
        }
        for (let key  in res.quality) {
          qualitys.push(Number(res.quality[key]))
        }
        this.series = [
          {
            name:"去年订单数",
            type:"line",
            itemStyle:{
              "normal":{
                "color":{
                  "x":0,
                  "y":0,
                  "x2":0,
                  "y2":1,
                  "colorStops":[
                    {
                      "offset":0,
                      "color":"#6fdeab"
                    },
                    {
                      "offset":0.5,
                      "color":"#44d693"
                    },
                    {
                      "offset":1,
                      "color":"#2cc981"
                    }
                  ]
                }
              }
            },
            yAxisIndex: 1,
            data: preQuality
          },
          {
            name:"今年订单数",
            type:"line",
            itemStyle:{
              "normal":{
                "color":{
                  "x":0,
                  "y":0,
                  "x2":0,
                  "y2":1,
                  "colorStops":[
                    {
                      "offset":0,
                      "color":"#6fdeab"
                    },
                    {
                      "offset":0.5,
                      "color":"#44d693"
                    },
                    {
                      "offset":1,
                      "color":"#2cc981"
                    }
                  ]
                }
              }
            },
            yAxisIndex: 1,
            data: qualitys
          }
        ]
      })
    },
    // 监听页面宽度变化，刷新表格
    handleResize() {
      if (this.infoList) this.$refs.visitChart.handleResize();
    }
  },
  created() {
  //  this.handleChangeVisitType();
  }
}
