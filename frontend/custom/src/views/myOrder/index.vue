<template>
  <div class="user-content">
    <el-table
      :data="tableData"
      border
      style="width: 100%">
      <el-table-column
        prop="date"
        label="日期"
        width="180">
      </el-table-column>
      <el-table-column
        prop="amount"
        label="金额"
        width="180">
      </el-table-column>
      <el-table-column
        prop="type"
        label="付款方式"
        width="180">
      </el-table-column>
      <el-table-column
        prop="remark"
        label="备注">
      </el-table-column>
    </el-table>
  </div>
</template>

<script src="./myOrder.js"></script>

<style rel="stylesheet/scss" lang="scss" scoped>
@import "src/styles/common.scss";

</style>
