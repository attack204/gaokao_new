<template>
  <div class="app-container">
    <el-form :inline="true" :model="listQuery">
      <el-form-item>
        <el-input v-model="listQuery.name" size="small" clearable placeholder="请输入任务名"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="mini" icon="el-icon-search" @click.native="search" v-perm="'task:view'">搜索
        </el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="mini" icon="el-icon-plus" @click.native="add" v-perm="'task:add'">新增</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="dataList" v-loading="listLoading" element-loading-text="Loading" border fit highlight-current-row
              @current-change="handleCurrentChange">
      <el-table-column label="任务名">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column label="执行类" width="300">
        <template slot-scope="scope">
          {{ scope.row.jobClass }}
        </template>
      </el-table-column>
      <el-table-column label="参数">
        <template slot-scope="scope">
          {{ scope.row.params }}
        </template>
      </el-table-column>
      <el-table-column label="定时规则">
        <template slot-scope="scope">
          {{ scope.row.cron }}
        </template>
      </el-table-column>
      <el-table-column label="说明">
        <template slot-scope="scope">
          {{ scope.row.note }}
        </template>
      </el-table-column>
      <el-table-column label="状态" :formatter="formatStatus">
      </el-table-column>

      <el-table-column label="最近执行时间" :formatter="formatExecTime">
      </el-table-column>

      <el-table-column label="最近执行结果" :formatter="formatResult">
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
          <!--          <el-button type="text" size="mini" @click.native="viewLog(scope.row.id)">查看日志</el-button>-->
          <el-button type="text" size="mini" @click.native.prevent="edit(scope.row)" v-perm="'task:update'">编辑
          </el-button>
          <el-button type="text" size="mini" @click.native="enable(scope.row.id)"
                     v-if="scope.row.status===-1" v-perm="'task:update'">启用
          </el-button>
          <el-button type="text" size="mini" @click.native="disable(scope.row.id)"
                     v-if="scope.row.status===0" v-perm="'task:update'">禁用
          </el-button>
          <el-button type="text" size="mini" @click.native="remove(scope.row.id)" v-perm="'task:delete'">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
      :title="formTitle"
      :visible.sync="formVisible"
      width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="任务名" prop="name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="定时规则" prop="cron">
          <el-input v-model="form.cron"></el-input>
        </el-form-item>
        <el-form-item label="执行类" prop="jobClass">
          <el-input v-model="form.jobClass" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="允许并发" prop="concurrent">
          <el-radio-group v-model="form.concurrent">
            <el-radio :label="0">否</el-radio>
            <el-radio :label="1">是</el-radio>
          </el-radio-group>

        </el-form-item>
        <el-form-item label="执行参数" prop="concurrent">
          <el-input v-model="form.params" type="textarea" placeholder="使用Key=Value的格式，不同Key之间使用&进行分隔"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="note">
          <el-input v-model="form.note" type="textarea"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="formVisible = false">取消</el-button>
        <el-button type="primary" @click="save">提交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script src="./task.js"></script>
<style rel="stylesheet/scss" lang="scss" scoped>
</style>

