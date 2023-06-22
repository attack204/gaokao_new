<template>
  <div class="divBox">
    <el-row :gutter="24">
      <el-col san="24" class="ivu-mb">
        <el-card :bordered="false" class="dashboard-console-visit">
          <div slot="header">
            <div class="acea-row row-between-wrapper">
              <div class="acea-row row-middle">
                <el-avatar icon="el-icon-s-data" size="small" style="color:#1890ff;background:#e6f7ff;font-size: 13px" />
                <span class="ivu-pl-8">订单</span>
              </div>
<!--              下面的四个想写在右边-->
<!--              <div class="checkTime">-->
<!--                <el-radio-group v-model="visitDate" class="ivu-mr-8">-->
<!--                  <el-radio label="last30" @change="handleChangeVisitType">30天</el-radio>-->
<!--                  <el-radio label="week" @change="handleChangeWeek">周</el-radio>-->
<!--                  <el-radio label="month" @change="handleChangeMonth">月</el-radio>-->
<!--                  <el-radio label="year" @change="handleChangeYear">年</el-radio>-->
<!--                </el-radio-group>-->
<!--              </div>-->
            </div>
          </div>
          <h4>订单量趋势</h4>
          <echarts-from ref="visitChart" :yAxisData="yAxisData" :seriesData="series" :xAxis="xAxis" v-if="info" :legendData="legendData"></echarts-from>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script src="./orderChart.js"></script>

<style lang="scss" scoped>
/* 上下两边居中对齐 */
.acea-row.row-between-wrapper {
  -webkit-box-align: center;
  -moz-box-align: center;
  -o-box-align: center;
  -ms-flex-align: center;
  -webkit-align-items: center;
  align-items: center;
  -webkit-box-pack: justify;
  -moz-box-pack: justify;
  -o-box-pack: justify;
  -ms-flex-pack: justify;
  -webkit-justify-content: space-between;
  justify-content: space-between;
}
.acea-row.row-middle {
  -webkit-box-align: center;
  -moz-box-align: center;
  -o-box-align: center;
  -ms-flex-align: center;
  -webkit-align-items: center;
  align-items: center;
}
.acea-row{
  /deep/.el-avatar--small {
    width: 22px;
    height: 22px;
    line-height: 22px;
  }
}
.checkTime{
  /deep/.el-radio__input{
    display: none;
  }
}
.ivu-pl-8{
  margin-left: 8px;
  font-size: 14px;
}
.divBox {
  padding: 0 20px !important;
}
.dashboard-console-visit {
  /deep/.el-card__header{
    padding: 14px 20px !important;
  }
  ul {
    li {
      list-style-type: none;
      margin-top: 12px;
    }
  }
}
.ivu-mb{
  margin-bottom: 10px;
}
</style>
