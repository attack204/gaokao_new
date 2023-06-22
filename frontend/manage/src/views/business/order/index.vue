<template>
  <div class="app-container">

    <el-form :inline="true" :model="page">
      <el-form-item>
        <el-input v-model="page.orderId" size="small" placeholder="订单Id" clearable/>
      </el-form-item>
      <el-form-item>
        <el-input v-model="page.userId" size="small" placeholder="会员Id" clearable/>
      </el-form-item>
      <!-- <el-form-item label="类型" prop="orderType">
        <el-select
          v-model="page.orderType"
          placeholder="请选择服务类型"
          size="small"
          style="width: 200px">
          <el-option
            v-for="item in OrderType"
            :key="item.code"
            :label="item.desc"
            :value="item.code"/>
        </el-select>
      </el-form-item> -->
      <!-- <el-form-item label="状态" prop="status">
        <el-select
          v-model="page.status"
          placeholder="请选择状态"
          size="small"
          style="width: 240px">
          <el-option
            v-for="item in ORDER_STATUS"
            :key="item.value"
            :label="item.desc"
            :value="item.value"/>
        </el-select>
      </el-form-item> -->
      <el-form-item>
        <el-button type="primary" size="mini" icon="el-icon-search" @click="listOrderByPage(1)">搜索</el-button>
      </el-form-item>
    </el-form>

    <el-card class="box-card">
      <el-table :data="dataList" highlight-current-row v-loading="listLoading" border height="calc(100vh - 250px)"
                style="width: 100%;">
        <el-table-column prop="id" align="center" label="订单Id" width="120"/>
        <el-table-column prop="userId" label="用户Id" align="center" width="120"/>
        <!-- <el-table-column prop="goodsNames" label="服务" align="center" width="120" show-overflow-tooltip/>
        <el-table-column prop="skuDesc" label="规格" align="center" width="120"/>-->
        <el-table-column prop="totalPrice" label="总金额(元)" align="center" width="150"/>
        <el-table-column prop="realPrice" label="实付金额(元)" align="center" width="150"/>
        <el-table-column prop="status" label="订单状态" align="center" width="150">
          <template slot-scope="scope">
            <span v-if="scope.row.status=== 1">待支付</span>
            <span v-if="scope.row.status=== 2">已取消</span>
            <el-tag type="warning" v-if="scope.row.status=== 5">待接单</el-tag>
            <span v-if="scope.row.status=== 8">已拒绝接单</span>
            <el-tag v-if="scope.row.status=== 11">待服务</el-tag>
            <span v-if="scope.row.status=== 12">进行中</span>
            <span v-if="scope.row.status=== 14">退款申请中</span>
            <span v-if="scope.row.status=== 15">已退款，待确认</span>
            <span v-if="scope.row.status=== 17">拒绝退款</span>
            <span v-if="scope.row.status=== 20">退款成功</span>
            <span v-if="scope.row.status=== 25">已关闭</span>
            <span v-if="scope.row.status=== 30">服务已完成</span>
            <span v-if="scope.row.status=== 35">已评价</span>
          </template>
        </el-table-column>
        <!-- <el-table-column prop="receiverName" label="用户姓名" align="center" width="120"/>
        <el-table-column prop="receiverPhone" label="用户电话" align="center" width="120"/>
        <el-table-column prop="receiverAddress" label="用户地址" align="center" width="120"/>
        <el-table-column prop="note" label="备注" align="center" width="120"/> -->
        <el-table-column prop="payType" label="支付方式" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.payType=== 1">微信支付</span>
            <span v-else>未支付</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" :formatter="formatCreateTime" align="center"/>
        <el-table-column prop="payTime" label="支付时间" :formatter="formatPayTime" align="center"/>
        <!-- <el-table-column label="操作" align="center" width="120">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click.native.prevent="manageRefund(scope.row)"
                       v-perm="'order:changeStatus'" v-if="scope.row.status === 14">退款管理
            </el-button>
          </template>
        </el-table-column> -->
      </el-table>
    </el-card>

    <!-- <el-dialog title="退款管理"
               :visible.sync="manageOrderRefundVisible">
      <el-form ref="form" :model="form" label-width="100px">
        <el-radio v-model="form.opt" label="YES">允许退款</el-radio>
        <el-radio v-model="form.opt" label="NO">拒绝退款</el-radio>
        <br>
        <el-input v-if="form.radio === 'NO'"
                  :close-on-click-modal="false"
                  :rows="5"
                  type="textarea"
                  placeholder="请输入拒绝退款理由"
                  v-model="form.reasons">
        </el-input>
      </el-form>
      <div slot="footer" class="dialog-footer" align="center">
        <el-button @click.native="manageOrderRefundVisible = false">取消</el-button>
        <el-button type="primary" @click.native="rejectRefund" :loading="setOrderRefundStatusLoading">提交
        </el-button>
      </div>
    </el-dialog> -->

    <!--工具条-->
    <el-col :span="24" class="toolbar">
      <!-- <el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :page-size="page.size"
                     :page-count="page.total" style="text-align:center;margin:10px">
      </el-pagination> -->
      <el-pagination
        @size-change="listOrderByPage(1)"
        @current-change="handleCurrentChange"
        :current-page.sync="page.page"
        :page-sizes="[10, 20, 50]"
        :page-size="page.size"
        layout="sizes, prev, pager, next"
        :total="totalOrders"
        style="text-align:center;margin:10px"/>
    </el-col>
  </div>
</template>

<script src="./order.js"></script>

<style scoped>
.box-card {
  height: calc(100vh - 210px);
}
</style>
