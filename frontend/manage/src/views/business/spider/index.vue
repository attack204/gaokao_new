<template>
  <div class="app-container">
    <!--搜索栏部分-->
    <el-form :model="listQuery" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item>
        <el-button type="primary" icon="el-icon-download" size="mini" @click.native="handleSpider" v-perm="'spider:start'">爬取</el-button>
        <el-button type="success" icon="el-icon-refresh" size="mini" @click.native="handleFresh" v-perm="'spider:view'">更新</el-button>
      </el-form-item>
    </el-form>

    <!--列表部分-->
    <el-table :data="spiderList" v-loading="loading"  border highlight-current-row>
      <el-table-column label="开始时间" align="center" prop="startTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endTime" >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime) }}</span>
        </template>
      </el-table-column>

      <el-table-column prop="state" label="状态" align="center">
        <template slot-scope="scope">
          <div v-if="scope.row.state===0">
            <el-tag type="danger">运行中</el-tag>
          </div>
          <div v-else>
            <el-tag type="success">已完成</el-tag>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <br>

    <!--工具条-->
    <el-col :span="24" class="toolbar">
      <el-pagination layout="prev, pager, next" @current-change="fetchPage" :page-size="listQuery.limit"
                     :page-count="listQuery.total" style="text-align:center;margin:10px">
      </el-pagination>
    </el-col>

  </div>
</template>

<script src="./spider.js"></script>
