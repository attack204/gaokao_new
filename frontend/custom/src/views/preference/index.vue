<template>
  <div class="body">
    <el-row>
      <h1>{{ currentFormName }}</h1>
      <h2>{{ userInfoStr }}</h2>
      <el-button
        @click="
          mode = true;
          dialogVisible = true;
        "
      >
        <i class="el-icon-circle-plus-outline" />
        新建志愿表
      </el-button>

      <el-button
        @click="
          mode = false;
          dialogVisible = true;
        "

        v-if="isExist"
      >
        <i class="el-icon-edit" />
        修改表名称
      </el-button>
    </el-row>
    <br />
    <el-row v-if="isExist">
      <el-tabs v-model="activeTable" type="card" @tab-click="changeCurrentData">
        <el-tab-pane label="一段志愿表" name="first"></el-tab-pane>
        <el-tab-pane label="二段志愿表" name="second"></el-tab-pane>
      </el-tabs>
      <el-table :data="tableData" border style="width: 100%" v-loading="loading">
        <el-table-column prop="num" label="志愿序号" align="center">
        </el-table-column>
        <!-- <el-table-column label="录取概率" prop="rate" align="center">
          <template slot-scope="scope">
            <el-tag type="danger" v-if="scope.row.rate <= 50"
              >&lt;50%(难录取)</el-tag
            >
            <el-tag
              type="warning"
              v-if="scope.row.rate > 50 && scope.row.rate <= 60"
              >{{ scope.row.rate }}%(可冲击)
            </el-tag>
            <el-tag
              type="success"
              v-if="scope.row.rate > 60 && scope.row.rate <= 80"
              >{{ scope.row.rate }}%(较稳妥)
            </el-tag>
            <el-tag v-if="scope.row.rate > 80 && scope.row.rate < 95"
              >{{ scope.row.rate }}%(可保底)</el-tag
            >
            <el-tag type="info" v-if="scope.row.rate >= 95"
              >&gt;95%(浪费分)</el-tag
            >
          </template>
        </el-table-column> -->
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
                    <div class="grid-content bg-purple-light">
                      {{ scope.row.category }}
                    </div>
                  </el-col>
                </el-row>
              </div>
              <div class="sign-text-second">
                <el-row v-if="scope.row.isExist">
                  <el-col :span="24">
                    <div class="grid-content bg-purple"></div>
                    院校代码{{ scope.row.universityCode }}
                  </el-col>
                </el-row>
              </div>
            </template>
          </el-table-column>
          <el-table-column
            label="招生专业"
            align="left"
            prop="profession"
            fit
          >
            <template slot-scope="scope">
              <div class="important-text">
                {{ scope.row.professionalName }}
              </div>
              <div class="sign-text">
                <el-row v-if="scope.row.isExist">
                  <el-col :span="12">
                    <div class="grid-content bg-purple"></div>
                    学费{{ scope.row.fee }}
                  </el-col>
                  <el-col :span="12">
                    <div class="grid-content bg-purple-light">
                      学制{{ scope.row.time }}
                    </div>
                  </el-col>
                </el-row>
              </div>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column
          label="2020历史录取"
          align="center"
          class-name="small-padding fixed-width"
        >
          <el-table-column label="最低分" align="center" prop="lowestScore">
            <template slot-scope="scope">
              {{ scope.row.lowestScore }}
            </template>
          </el-table-column>
          <el-table-column
            label="最低位次"
            align="center"
            prop="lowestPosition"
          >
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
        <el-table-column
          label="操作"
          align="center"
          width="350"
          class-name="small-padding fixed-width"
        >
          <template slot-scope="scope">
            <div v-if="tableData[scope.$index].isExist">
              <el-button @click="upVol(scope.$index)"> 上移 </el-button>
              <el-button @click="downVol(scope.$index)"> 下移 </el-button>
              <!-- <el-button @click="swapVol(scope.$index)"> 交换 </el-button> -->
              <el-popover
                placement="bottom"
                width="200"
                v-model="tableData[scope.$index].swapVisible"
                @after-leave="toSwapIndex = undefined"
              >
                <p>交换至第几志愿(1~96):</p>
                <el-input
                  type="number"
                  :min="0"
                  :max="96"
                  v-model="toSwapIndex"
                  placeholder=""
                />
                <div style="text-align: right; margin: 0">
                  <el-button
                    size="mini"
                    type="text"
                    @click="tableData[scope.$index].swapVisible = false"
                    >取消</el-button
                  >
                  <el-button
                    type="primary"
                    size="mini"
                    @click="swapVol(scope.$index)"
                    >确定</el-button
                  >
                </div>
                <el-button slot="reference">交换</el-button>
              </el-popover>

              <el-button @click="deleteVol(scope.$index)"> 删除 </el-button>
            </div>
            <div v-else>
              <center>
                <el-button round @click="toScreen"> 添加 </el-button>
              </center>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-row>
    <el-dialog
      :title="mode ? '新建志愿表' : '修改当前志愿表名称'"
      :visible.sync="dialogVisible"
      width="50%"
    >
      <el-form ref="createVolForm" :model="createData" :rules="createRules">
        <el-form-item label="志愿表名称" prop="name">
          <el-input v-model="createData.name"></el-input>
        </el-form-item>
        <el-form-item v-if="mode" label="成绩信息">
          <el-input v-model="userInfoStr"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button v-if="mode" type="primary" @click="createForm"
            >立即创建</el-button
          >
          <el-button v-else type="primary" @click="updateName"
            >确认修改</el-button
          >
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script src="./preference.js">
</script>

<style lang="scss" scoped>
.body {
  width: 100%;
  height: 100%;
  font-family: "Lucida Console", "Courier New", monospace;
  font-size: 12px;
  font-weight: bold;
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
