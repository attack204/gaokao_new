<template>
  <div class="app-container">
    <!--列表部分-->
    <el-table :data="starsList" v-loading="loading" border highlight-current-row stripe height="600"
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
      <el-pagination layout="prev, pager, next" @current-change="fetchPage" :page-size="query.size"
                     :page-count="query.total" style="text-align:center;margin:10px">
      </el-pagination>
    </el-col>
  </div>
</template>

<script src="./stars.js"></script>


<style>
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
