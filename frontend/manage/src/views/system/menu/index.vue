<template>
  <div class="app-container">
    <!-- 这部分是左侧的权限树 -->
    <el-card class="fullTreeCard" style="width: 400px !important; float: left">
      <el-tree
        :data="data"
        node-key="id"
        :default-expand-all="true"
        :expand-on-click-node="false"
        :loading="loading"
        @node-click="handleNodeClick"
        :props="defaultProps"
      >
      </el-tree>
    </el-card>

    <!-- 这部分是右侧显示权限具体信息的部分 -->
    <el-card class="box-card" id="permZone">
      <el-row class="permRow">
        <el-col :span="8">
          <div class="permLabel">权限id:</div>
        </el-col>
        <el-col :span="16">
          <div class="permContent">{{ permNode.id }}</div>
        </el-col>
      </el-row>
      <el-row class="permRow">
        <el-col :span="8">
          <div class="permLabel">父权限id:</div>
        </el-col>
        <el-col :span="16">
          <div class="permContent">{{ permNode.pid }}</div>
        </el-col>
      </el-row>
      <el-row class="permRow">
        <el-col :span="8">
          <div class="permLabel">权限名称:</div>
        </el-col>
        <el-col :span="16">
          <div class="permContent">{{ permNode.name }}</div>
        </el-col>
      </el-row>
      <el-row class="permRow">
        <el-col :span="8">
          <div class="permLabel">权限表达式:</div>
        </el-col>
        <el-col :span="16">
          <div class="permContent">{{ permNode.code }}</div>
        </el-col>
      </el-row>
      <el-row style="margin-top: 40px">
        <el-col :span="8">
          <el-button type="primary" size="mini" @click="save(true)">新增子权限</el-button>
        </el-col>
        <el-col :span="8">
          <el-button type="primary" size="mini" @click="save(false)">编辑</el-button>
        </el-col>
        <el-col :span="8">
          <el-button type="primary" size="mini" @click="del">删除</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!--新增界面-->

    <el-dialog
      :title="currentDialog ? '新增权限' : '编辑权限'"
      :visible.sync="addFormVisible"
      :close-on-click-modal="false"
      width="30%"
    >
      <el-form
        :model="addForm"
        label-width="100px"
        :rules="addFormRules"
        ref="addForm"
      >
        <el-form-item label="权限Id" prop="id">
          <el-input type="input" v-model="addForm.id"></el-input>
        </el-form-item>
        <el-form-item label="父权限ID" prop="pid">
          <el-input type="input" v-model="addForm.pid"></el-input>
        </el-form-item>
        <el-form-item label="权限名称" prop="name">
          <el-input type="input" v-model="addForm.name"></el-input>
        </el-form-item>
        <el-form-item label="权限表达式" prop="code">
          <el-input type="input" v-model="addForm.code"></el-input>
        </el-form-item>
        <el-card>
          <div>提示：</div>
          <div>1. 根节点的权限id必须为0，否则无法展示数据</div>
          <div>2. 示例：</div>
          <div>权限id: 1</div>
          <div>父权限id: 0</div>
          <div>资源名称: 管理员</div>
          <div>权限表达式: admin</div>
        </el-card>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="addFormVisible = false">取消</el-button>
        <el-button
          type="primary"
          @click.native.prevent="addSubmit"
          :loading="addLoading"
        >提交
        </el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script src="./menu.js"></script>
<style rel="stylesheet/scss" lang="scss" scoped>
.permLabel {
  text-align: right;
  padding-right: 24px;
}

.permContent {
  text-align: left;
}

#permZone {
  float: left;
  margin-left: 32px;
  width: 500px;
  height: 560px;
  text-align: center;
}

#treeCard {
  width: 380px;
  height: calc(100vh - 190px);
}

.permRow {
  margin-top: 16px;
}

.card {
  width: 400px;
  height: inherit;
  float: left;
}
</style>
