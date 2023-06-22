<template>
  <div class="app-container">
    <el-form :inline="true" :model="page">
      <el-form-item>
        <el-input v-model="page.keyword" size="small"  style="width: 240px" placeholder="请输入手机号"
                  @keyup.enter.native="handleQuery()"
                   clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="mini" icon="el-icon-search" @click="handleQuery()" v-perm="'usermember:view'">
          搜索
        </el-button>
      </el-form-item>
    </el-form>

    <el-card class="box-card">
      <el-table v-loading="loading" :header-cell-style="{background:'#eef1f6',color:'#606266'}" :data="dataList" highlight-current-row  border height="calc(100vh - 250px)"
                style="width: 100%;">

        <el-table-column type="index" align="center" label="序号" width="50">
        </el-table-column>
        <el-table-column prop="nickname" label="昵称" align="center" width="80">
        </el-table-column>
        <el-table-column prop="username" label="用户名" align="center" width="120">
        </el-table-column>
        <el-table-column prop="phone" label="手机号" align="center" width="120">
        </el-table-column>
        <el-table-column prop="location" label="地理位置" align="center">
        </el-table-column>
        <el-table-column prop="createTime" label="注册时间" :formatter='formatDate1' align="center">
<!--        <el-table-column label="注册时间" align="center">-->
<!--        <template slot-scope="scope">-->
<!--            {{formatDate1(scope.row)}}-->
<!--          </template>-->
        </el-table-column>
        <el-table-column prop="updateTime" label="更新时间" :formatter="formatDate2" align="center">
        </el-table-column>
        <el-table-column label="锁定用户" align="center"  width="80">
          <template slot-scope="scope">
            <el-switch
              v-perm="'usermember:update'"
              v-model="scope.row.status"
              :active-value="1"
              :inactive-value="0"
              @change="changeSwitch(scope.row)"
            ></el-switch>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <!--工具条-->
    <el-col :span="24" class="toolbar">
      <el-pagination layout="prev, pager, next" @current-change="handleCurrentChange()" :page-size="page.size"
                     :page-count="page.total" style="text-align:center;margin:10px">
      </el-pagination>
    </el-col>


    <div v-if='mask' class="offline-mask">
      <h2 class="offline-mask-title"> {{ offlineTitle }} </h2>
    </div>
  </div>
</template>

<script src="./member.js"></script>

<style scoped>
.box-card {
  height: calc(100vh - 210px);
}
</style>
