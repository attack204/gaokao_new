<template>
  <div class="app-container">
    <!--搜索栏部分-->
    <el-form :model="listQuery" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="高校名称" prop="name">
        <el-input
          v-model="listQuery.name"
          placeholder="请输入高校名称"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click.native="handleQuery" v-perm="'goods:view'">
          搜索
        </el-button>
      </el-form-item>
    </el-form>

    <!--按钮部分-->
    <el-row :gutter="20" class="mb8">
      <el-col :span="1.5">
        <el-dropdown split-button type="primary" @click="handleAdd" @command="changeItem">
          新增高校信息
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item icon="el-icon-plus" :command="beforeHandleCommand('guide',formItem)">招生简章</el-dropdown-item>
            <el-dropdown-item icon="el-icon-plus" :command="beforeHandleCommand('university',formItem)">院校库</el-dropdown-item>
            <el-dropdown-item icon="el-icon-plus" :command="beforeHandleCommand('major',formItem)">专业库</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-col>
      <el-col :span="1.5">
        <el-dropdown split-button @click="handleDel" @command="changeItem">
          删除高校信息
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item icon="el-icon-delete" :command="beforeHandleCommand('guide',formItem)">招生简章</el-dropdown-item>
            <el-dropdown-item icon="el-icon-delete" :command="beforeHandleCommand('university',formItem)">院校库</el-dropdown-item>
            <el-dropdown-item icon="el-icon-delete" :command="beforeHandleCommand('major',formItem)">专业库</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-col>
      <el-col :span="1.5">
        <el-dropdown split-button type="danger" @click="handleEdit" @command="changeItem">
          修改高校信息
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item icon="el-icon-edit" :command="beforeHandleCommand('guide',formItem)">招生简章</el-dropdown-item>
            <el-dropdown-item icon="el-icon-edit" :command="beforeHandleCommand('university',formItem)">院校库</el-dropdown-item>
            <el-dropdown-item icon="el-icon-edit" :command="beforeHandleCommand('major',formItem)">专业库</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-download" @click="handleFetch">爬取</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-upload" @click="upModel">更新</el-button>
      </el-col>


      <div class="top-right-btn">
        <el-row>
          <el-tooltip class="item" effect="dark" content="刷新" placement="top">
            <el-button size="mini" circle icon="el-icon-refresh" @click="handleQuery"/>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="显隐列" placement="top" v-if="columns">
            <el-button size="mini" circle icon="el-icon-menu" @click="showColumn()"/>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" :content="showSearch ? '隐藏搜索' : '显示搜索'" placement="top">
            <el-button size="mini" circle icon="el-icon-search" @click="showSearch=!showSearch"/>
          </el-tooltip>
        </el-row>
        <el-dialog :title="toolbarTitle" :visible.sync="open" append-to-body>
          <el-transfer
            :titles="['显示', '隐藏']"
            v-model="visibleValue"
            :data="columns"
            @change="dataChange"
          ></el-transfer>
        </el-dialog>
      </div>
    </el-row>
    <br>

    <!--列表部分-->
    <el-table :data="goodList" height="250" v-loading="loading" @selection-change="handleSelectionChange" border
              highlight-current-row>
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="高校名称" align="center" prop="name" key="name" fixed v-if="columns[0].visible">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column label="所在省份" align="center" prop="province" key="province" v-if="columns[1].visible">
        <template slot-scope="scope">
          {{scope.row.province}}
        </template>
      </el-table-column>
      <el-table-column label="所在市" align="center" prop="city" key="city" v-if="columns[2].visible">
        <template slot-scope="scope">
          {{scope.row.city}}
        </template>
      </el-table-column>
      <el-table-column label="所在区/县" align="center" prop="county" key="county" v-if="columns[3].visible">
        <template slot-scope="scope">
          {{scope.row.county}}
        </template>
      </el-table-column>
      <el-table-column label="详细地址" align="center" prop="address" show-overflow-tooltip key="address" 
                        v-if="columns[4].visible">
        <template slot-scope="scope">
          {{scope.row.address}}
        </template>
      </el-table-column>
      <el-table-column label="官方网址" align="center" prop="website" show-overflow-tooltip key="website" 
                        v-if="columns[5].visible">
        <template slot-scope="scope">
          {{scope.row.website}}
        </template>
      </el-table-column>
      <el-table-column label="官方电话" align="center" prop="phonenumber" show-overflow-tooltip key="phonenumber" 
                        v-if="columns[6].visible">
        <template slot-scope="scope">
          {{scope.row.address}}
        </template>
      </el-table-column>
      <el-table-column label="官方邮箱" align="center" prop="email" show-overflow-tooltip key="email"
                        v-if="columns[7].visible">
        <template slot-scope="scope">
          {{ scope.row.email }}
        </template>
      </el-table-column>
      <el-table-column label="学校档次" align="center" prop="level" key="level" v-if="columns[8].visible">
        <template slot-scope="scope">
          {{ scope.row.level }}
        </template>
      </el-table-column>
      <el-table-column label="人气值" align="center" prop="popularity" key="popularity" v-if="columns[9].visible">
        <template slot-scope="scope">
          {{ scope.row.popularity }}
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit"
                     @click="handleUpdate(scope.row)" v-perm="'goods:update'">修改
          </el-button>
          <div v-if="scope.row.status===0">
            <el-button size="mini" type="text" icon="el-icon-view"
                       @click="handleAudit(scope.row)" v-perm="'goods:audit'">审核
            </el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <br>

    <!--工具条-->
    <el-col :span="24" class="toolbar">
      <el-pagination layout="prev, pager, next" @current-change="fetchPage" :page-size="listQuery.size"
                     :page-count="listQuery.total" style="text-align:center;margin:10px">
      </el-pagination>
    </el-col>

    <!-- 审核通过对话框 -->
    <el-dialog :title="passTitle" :close-on-click-modal="false" :visible.sync="passFormVisible" width="650px">
      <el-form ref="passForm" :model="passForm" label-width="100px" :rules="passFormRules">
        <el-form-item label="服务标签" prop="tags">
          <el-select v-model="passForm.tagIds" multiple collapse-tags placeholder="请选择分组（可多选）">
            <el-option
              v-for="tag in dynamicTags"
              :key="tag.id"
              :label="tag.name"
              :value="tag.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="审核意见" prop="auditOpinion">
          <el-input v-model="passForm.auditOpinion" placeholder="请输入审核意见" maxlength=100 style="width: 360px"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="passCancel">取消</el-button>
        <el-button type="primary" @click.native="passSubmit">提交</el-button>
      </div>
    </el-dialog>

    <!-- 驳回信息对话框 -->
    <el-dialog :title="auditTitle" :close-on-click-modal="false" :visible.sync="notPassFormVisible" width="650px">
      <el-form ref="notPassForm" :model="notPassForm" label-width="100px" :rules="notPassFormRules">
        <el-form-item label="审核意见" prop="auditOpinion">
          <el-input v-model="notPassForm.auditOpinion" placeholder="请输入审核意见" maxlength=100 style="width: 360px"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="auditCancel">取消</el-button>
        <el-button type="primary" @click.native="auditSubmit">提交</el-button>
      </div>
    </el-dialog>

    <!-- 添加/删除/修改服务对话框 -->
    <el-dialog :title="title" :close-on-click-modal="false" :visible.sync="dialogFormVisible" width="350px">
      <el-form :model="form" :inline="true">
        <el-form-item label="高校名称" :label-width="formLabelWidth">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" native-type="submit" @click="dialogFormVisible = false">确 定</el-button>
      </div>
    </el-dialog>

    <!--更新数据库服务对话框 -->
    <el-dialog title="上传文件" :visible.sync="uploadDialogVisible" class="uploadDialog">
      <el-form ref="uploadForm" class="uploadDialogForm" id="uploadForm">
        <el-form-item label="请上传文件">
          <el-upload
            class="upload-file"
            ref="upload"
            action="/xhr/vl/university/update"
            :http-request="uploadFile"
            :before-upload="beforeUpload"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :before-remove="beforeRemove"
            :on-exceed="handleExceed"
            :file-list="fileList"
            :auto-upload="false"
            :limit="3"
            multiple
          >
            <el-button slot="trigger" size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传Excel文件，且不超过500kb</div>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeFile">取消</el-button>
        <el-button type="primary" @click="postFile" :disabled="uploading">确定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script src="./good.js"></script>

<style>
/*解决表头与数据不对齐的问题*/
.el-table th.gutter {
  display: table-cell !important;
}

.top-right-btn {
  position: relative;
  float: right;
}
</style>

