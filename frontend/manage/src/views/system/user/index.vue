<template>
  <div class="app-container">

    <el-form :inline="true" :model="page">
      <el-form-item>
        <el-input v-model="page.keyword" size="small" clearable placeholder="用户名"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="mini" icon="el-icon-search" @click="listByPage()" v-perm="'user:view'">搜索
        </el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="save()" v-perm="'user:add'">新增</el-button>
      </el-form-item>
    </el-form>

    <el-card class="box-card">
      <el-table :data="dataList" highlight-current-row v-loading="listLoading" border height="calc(100vh - 250px)"
                style="width: 100%;">

        <el-table-column type="index" align="center" label="序号" width="50">
        </el-table-column>
        <el-table-column prop="corpName" label="所属公司" align="center">
        </el-table-column>
        <el-table-column prop="username" label="用户名" align="center">
        </el-table-column>
        <el-table-column prop="phone" label="手机号" align="center">
        </el-table-column>
        <el-table-column prop="roles" label="角色" align="center">
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click.native.prevent="updateSave(scope.row)" v-perm="'user:update'">编辑
            </el-button>
            <el-button type="text" size="small"
                       @click.native.prevent="handleDelete(scope.$index, scope.row)" v-perm="'user:delete'">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <!--工具条-->
    <el-col :span="24" class="toolbar">
      <el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :page-size="page.size"
                     :page-count="page.total" style="text-align:center;margin:10px">
      </el-pagination>
    </el-col>

    <el-dialog title="新增" :visible.sync="addFormVisible" :close-on-click-modal="false" width="500px">
      <el-form :model="addForm" label-width="80px" :rules="addFormRules" ref="addForm">

        <el-form-item label="所属公司" prop="corp">
          <el-select v-model="addForm.corp" placeholder="请选择"
                     remote
                     filterable
                     :remote-method="getShopDropdown" style="width: 360px;">
            <el-option
              v-for="item in corpOptions"
              :key="item.value"
              :label="item.name"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="用户名" prop="username">
          <el-input type="input" v-model="addForm.username" style="width: 360px;"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input type="input" v-model="addForm.phone" style="width: 360px;"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="input" v-model="addForm.password" style="width: 360px;" disabled></el-input>
        </el-form-item>
        <el-form-item label="角色" prop="roleIds" style="width: 400px;">
          <el-checkbox-group v-model="addForm.roleIds">
            <template v-for="role in roleOptions">
              <el-checkbox :key="role.value" :label="role.value">{{ role.name }}</el-checkbox>
            </template>
          </el-checkbox-group>
        </el-form-item>
        <div>
          <p style="text-align: center;color: red">请及时修改默认密码</p>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="addFormVisible = false">取消</el-button>
        <el-button type="primary" @click.native="addUserSubmit" :loading="addLoading">提交</el-button>
      </div>
    </el-dialog>

    <el-dialog title="编辑" :visible.sync="updateFormVisible" :close-on-click-modal="false" width="500px">
      <el-form :model="updateForm" label-width="80px" :rules="updateFormRules" ref="updateForm">

        <el-form-item label="所属公司" prop="corp" v-if="isPlatformUser()">
          <el-select v-model="updateForm.corp" placeholder="请选择"
                     remote
                     filterable
                     :remote-method="getShopDropdown" style="width: 360px;">
            <el-option
              v-for="item in corpOptions"
              :key="item.value"
              :label="item.name"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="用户名" prop="username">
          <el-input type="input" v-model="updateForm.username" style="width: 360px;"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input type="input" v-model="updateForm.phone" style="width: 360px;"></el-input>
        </el-form-item>
        <el-form-item label="角色" prop="roleIds">
          <el-checkbox-group v-model="updateForm.roleIds" style="width: 360px;">
            <template v-for="role in roleOptions">
              <el-checkbox :key="role.value" :label="role.value">{{ role.name }}</el-checkbox>
            </template>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="updateFormVisible = false">取消</el-button>
        <el-button type="primary" @click.native="updateSubmit" :loading="updateLoading">提交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script src="./user.js"></script>

<style scoped>
.box-card {
  height: calc(100vh - 210px);
}
</style>
