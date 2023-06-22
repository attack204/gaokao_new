<template>
  <div class="app-container">
    <el-form :inline="true" :model="listQuery">
      <el-form-item>
        <el-input v-model="listQuery.keyword" size="small" clearable placeholder="用户名"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="mini" icon="el-icon-search" @click="search()" v-perm="'role:view'">搜索
        </el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="mini" icon="el-icon-plus" @click="add()" v-perm="'role:add'">新增</el-button>
      </el-form-item>
    </el-form>

    <el-card class="box-card">
      <!--内容展示-->
      <el-table
        :data="list"
        v-loading="listLoading"
        element-loading-text="Loading"
        border
        highlight-current-row
        @current-change="handleCurrentChange"
        height="calc(100vh - 250px)"
      >
        <el-table-column label="序号" type="index" width="50" align="center">
        </el-table-column>
        <el-table-column label="名称" align="center">
          <template slot-scope="scope">
            {{ scope.row.name }}
          </template>
        </el-table-column>
        <el-table-column label="创建者" align="center">
          <template slot-scope="scope">
            {{ scope.row.creator }}
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center">
          <template slot-scope="scope">
            {{ new Date(scope.row.createTime).Format("yyyy-MM-dd hh:mm:ss") }}
          </template>
        </el-table-column>
        <el-table-column label="更新者" align="center">
          <template slot-scope="scope">
            {{ scope.row.updater }}
          </template>
        </el-table-column>
        <el-table-column label="更新时间" align="center">
          <template slot-scope="scope">
            {{ new Date(scope.row.updateTime).Format("yyyy-MM-dd hh:mm:ss") }}
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click.native.prevent="edit(scope.row)" v-perm="'role:update'">编辑
            </el-button>
            <el-button type="text" size="small"
                       @click.native.prevent="remove(scope.row)" v-perm="'role:delete'">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-col :span="24" class="toolbar">
        <el-pagination layout="prev, pager, next" @current-change="fetchPage" :page-size="listQuery.size"
                       :page-count="total" style="text-align:center;margin:10px">
        </el-pagination>
      </el-col>

      <!--添加修改对话框-->
      <el-dialog
        :title="formTitle"
        :visible.sync="formVisible"
        @opened="onDialogOpened"
        width="500px">
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
          <el-form-item label="名称" prop="name">
            <el-input v-model="form.name" minlength=1></el-input>
          </el-form-item>
          <el-form-item label="权限" prop="permIds">
            <el-tree
              ref="permTree"
              :data="permList"
              show-checkbox
              check-strictly="true"
              node-key="id"
              :props="permTree.defaultProps"
              @check="checkParent">
            </el-tree>
          </el-form-item>
          <el-form-item label="提示信息" prop="Hints">
            编辑完成后需要手动退出并重新登录才会生效
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click.native="formVisible = false">取消</el-button>
          <el-button type="primary" @click="saveChange">提交</el-button>
        </div>
      </el-dialog>

    </el-card>
  </div>
</template>

<script src="./role.js"></script>

