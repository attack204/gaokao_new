<template>
  <div class="app-container">
    <!--搜索栏部分-->
    <el-form :model="listQuery" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="填报批次" prop="level">
        <el-select
          v-model="level"
          placeholder="请选择批次"
          style="width: 240px">
          <el-option
            v-for="item in levelOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="就读地区" prop="location">
        <el-cascader
          v-model="location"
          placeholder="不限"
          style="width: 240px"
          clearable
          filterable
          :options="locationOptions"
          :props="multiProps"
          collapse-tags
          @change="changeLocation">
        </el-cascader>
      </el-form-item>
      <el-form-item label="大学类型" prop="classification">
        <el-cascader
          v-model="classification"
          style="width: 240px"
          :options="classifyOptions"
          :props="multiProps"
          collapse-tags
          clearable
          filterable
          @change="changeClassify"></el-cascader>
      </el-form-item>
      <!-- 爬虫尚未获取数据
             <el-form-item label="专业类型" prop="majorType">
              <el-cascader
                v-model="majorType"
                style="width: 240px"
                :options="majorOptions"
                :props="multiProps"
                collapse-tags
                clearable
                filterable
                @change="changeClassify"></el-cascader>
            </el-form-item>-->
      <el-form-item label="搜索大学" prop="universityName">
        <el-input
          v-model="listQuery.universityName"
          placeholder="请输入大学名称"
          style="width: 240px"
          clearable></el-input>
      </el-form-item>
      <el-form-item label="搜索专业" prop="majorName">
        <el-input
          v-model="listQuery.majorName"
          placeholder="请输入专业名称"
          style="width: 240px"
          clearable></el-input>
      </el-form-item>
      <el-form-item>
        <div class="niceButton">
          <el-button type="primary" icon="el-icon-search" @click.native="handleQuery(0)">搜索</el-button>
        </div>
      </el-form-item>
      <el-divider content-position="right"/>
    </el-form>

    <!--列表部分-->
    <el-button-group>
      <el-button type="primary" plain @click.native="handleQuery(0)">全部</el-button>
      <el-tooltip class="item" effect="light" content="依据成绩信息结合招生计划、历史录取和填报规则推荐可以填报的冲一冲大学，这类大学一般填在志愿表中的前段。"
                  placement="top">
        <el-button type="primary" plain @click.native="handleQuery(1)">冲刺</el-button>
      </el-tooltip>
      <el-tooltip class="item" effect="light" content="依据成绩信息结合招生计划、历史录取和填报规则推荐可以填报的稳一稳大学，这类大学一般填在志愿表中的中间段。"
                  placement="top">
        <el-button type="primary" plain @click.native="handleQuery(2)">稳妥</el-button>
      </el-tooltip>
      <el-tooltip class="item" effect="light" content="依据成绩信息结合招生计划、历史录取和填报规则推荐可以填报的保一保大学，这类大学一般填在志愿表中的后段。"
                  placement="top">
        <el-button type="primary" plain @click.native="handleQuery(3)">保底</el-button>
      </el-tooltip>
    </el-button-group>
    <el-table :data="volunteerList" v-loading="loading" border highlight-current-row stripe height="600"
              :header-cell-style="{'text-align':'center'}">
      <el-table-column label="录取概率" prop="rate" align="center">
        <template slot-scope="scope">
          <el-tag type="danger" v-if="scope.row.rate<=50">&lt;50%(难录取)</el-tag>
          <el-tag type="warning" v-if="scope.row.rate>50 && scope.row.rate<=60">{{ scope.row.rate }}%(可冲击)
          </el-tag>
          <el-tag type="success" v-if="scope.row.rate>60 && scope.row.rate<=80">{{ scope.row.rate }}%(较稳妥)
          </el-tag>
          <el-tag v-if="scope.row.rate>80 && scope.row.rate<95">{{ scope.row.rate }}%(可保底)</el-tag>
          <el-tag type="info" v-if="scope.row.rate>=95">&gt;95%(浪费分)</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="2021招生计划" align="center">
        <el-table-column label="招生院校" prop="name" align="left">
          <template slot-scope="scope">
            <div class="important-text">
              {{ scope.row.name }}
            </div>
            <div class="sign-text">
              <el-row>
                <el-col :span="8">
                  <div class="grid-content bg-purple"></div>
                  {{ scope.row.province }}
                </el-col>
                <el-col :span="8">
                  <div class="grid-content bg-purple-light">{{ scope.row.category }}</div>
                </el-col>
              </el-row>
            </div>
            <div class="sign-text-second">
              <el-row>
                <el-col :span="24">
                  <div class="grid-content bg-purple"></div>
                  院校代码{{ scope.row.universityCode }}
                </el-col>
              </el-row>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="招生专业" align="left" prop="professionalName" fit>
          <template slot-scope="scope">
            <div class="important-text">
              {{ scope.row.professionalName }}
            </div>
            <div class="sign-text">
              <el-row>
                <el-col :span="12">
                  <div class="grid-content bg-purple"></div>
                  学费{{ scope.row.fee }}
                </el-col>
                <el-col :span="12">
                  <div class="grid-content bg-purple-light">学制{{ scope.row.time }}</div>
                </el-col>
              </el-row>
            </div>
            <!--            <div class="sign-text-second">
                          <el-row>
                            <el-col :span="8"><div class="grid-content bg-purple"></div>专业代码{{scope.row.majorCode}}</el-col>
                            <el-col :span="8"><div class="grid-content bg-purple-light">招生人数。</div></el-col>
                            <el-col :span="8"><div class="grid-content bg-purple-light">选科信息。</div></el-col>
                          </el-row>
                        </div>-->
          </template>
        </el-table-column>
      </el-table-column>
      <el-table-column label="2020历史录取" align="center" class-name="small-padding fixed-width">
        <el-table-column label="最低分" align="center" prop="lowestScore">
          <template slot-scope="scope">
            {{ scope.row.lowestScore }}
          </template>
        </el-table-column>
        <el-table-column label="最低位次" align="center" prop="lowestPosition">
          <template slot-scope="scope">
            {{ scope.row.lowestPosition }}
          </template>
        </el-table-column>
        <el-table-column label="录取人数" align="center" prop="enrollment">
          <template slot-scope="scope">
            {{ scope.row.enrollment }}
          </template>
        </el-table-column>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-row>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-circle-plus-outline"
              @click="handleFill(scope.row)"
            >填报为
            </el-button>
          </el-row>
          <el-row :gutter="16">
            <div v-if="scope.row.myStar===true">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-star-on"
                @click="handleStar(scope.row)"
                circle
              >已收藏
              </el-button>
            </div>
            <div v-else>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-star-off"
                @click="handleStar(scope.row)"
                circle
              >未收藏
              </el-button>
            </div>
          </el-row>
        </template>
      </el-table-column>
    </el-table>
    <br>
    <el-dialog :title="title" :close-on-click-modal="false" :visible.sync="dialogFormVisible" width="500px">
      <el-form ref="form" :model="form" label-width="100px" :rules="rules">
        <el-form-item label="招生院校" prop="name">
          <el-input v-model="form.name" :disabled="edit" style="width: 360px"/>
        </el-form-item>
        <el-form-item label="招生专业" prop="professionalName">
          <el-input v-model="form.professionalName" :disabled="edit" style="width: 360px"/>
        </el-form-item>
        <el-form-item label="志愿批次" prop="section">
          <el-select
            v-model="form.section"
            placeholder="请选择志愿批次"
            style="width: 360px">
            <el-option
              v-for="item in boolOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="志愿序号" prop="volunteerPosition">
          <el-tooltip class="item" effect="light" content="志愿序号：指填报为第几志愿"
                      placement="bottom">
            <el-input-number v-model="form.volunteerPosition" placeholder="请输入志愿顺序号(1~96)" :min="1" :max="96"
                             style="width: 360px"/>
          </el-tooltip>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="cancel">取消</el-button>
        <el-button type="primary" @click.native="submitForm">提交</el-button>
      </div>
    </el-dialog>
    <!--工具条-->
    <el-col :span="24" class="toolbar">
      <el-pagination layout="prev, pager, next" @current-change="fetchPage" :page-size="listQuery.limit"
                     :page-count="listQuery.total" style="text-align:center;margin:10px">
      </el-pagination>
    </el-col>
  </div>
</template>

<script src="./screen.js"></script>


<style>

.niceButton {
  position: relative;
  left: 20px;
  border-radius: 4px;
  bottom: 0;
}

.niceButton:hover {
  background-color: #1795bb;
}

.niceButton:active {
  background-color: #1795bb;
  box-shadow: 0 5px #666;
  transform: translateY(4px);
}

.important-text {
  font-size: 14px;
  font-weight: bold;
}

.sign-text {
  color: #8c939d;
  font-size: smaller;
  font-family: 幼圆;
  font-weight: bold;
}

.sign-text-second {
  color: #8c939d;
  font-size: smaller;
  font-family: 幼圆;
  font-weight: bold;
}
</style>
