<template>
  <div>
    <el-row type="flex" justify="space-around">
      <el-col :span="0.5"></el-col>
      <el-col :span="12">
        <el-card
          :body-style="{ padding: '0px', backgroundColor: '#F5F7FA' }"
          shadow="hover"
        >
          <el-row type="flex" justify="space-around" align="center">
            <el-col :span="21">
              <div class="form-container">

                <el-form :model="form" :rules="validateRules" ref="form" label-width="80px">

                  <el-form-item label="成绩信息">
                    <el-input
                      v-model="userInfoStr"
                    >
                    </el-input>
                  </el-form-item>

                  <el-form-item label="大学类型" prop="school">
                    <el-cascader
                      v-model="form.school"
                      :options="classifyOptions"
                      :props="multiProps"
                      clearable
                      filterable
                      collapse-tags
                      placeholder="默认全选"
                    ></el-cascader>
                  </el-form-item>

                  <el-form-item label="意向地区" prop="region">
                    <el-cascader
                      v-model="form.region"
                      clearable
                      filterable
                      :options="locationOptions"
                      :props="multiProps"
                      collapse-tags
                      placeholder="默认全选"
                    >
                    </el-cascader>
                  </el-form-item>

                  <el-form-item label="填报方案" prop="plan">
                    <el-form-item>
                      <el-button :type="this.activeTab == 1 ? 'primary' : 'default' " @click="Click1">冲击型</el-button>
                      <el-button :type="this.activeTab == 2 ? 'primary' : 'default' " @click="Click2">稳妥型</el-button>
                      <el-button :type="this.activeTab == 3 ? 'primary' : 'default' " @click="Click3">保底型</el-button>
                      <el-button :type="this.activeTab == 4 ? 'primary' : 'default' " @click="Click4">自定义</el-button>
                    </el-form-item>
                  </el-form-item>

                  <el-form-item>
                    <el-form-item label="冲击">
                      <el-input v-model="plan[0]" type="number" placeholder="请输入冲击数" v-bind:disabled="disabled"></el-input>
                    </el-form-item>
                    <el-form-item label="稳妥">
                      <el-input v-model="plan[1]" type="number" placeholder="请输入稳妥数" v-bind:disabled="disabled"></el-input>
                    </el-form-item>
                    <el-form-item label="保底">
                      <el-input v-model="plan[2]" type="number" placeholder="请输入保底数" v-bind:disabled="disabled"></el-input>
                    </el-form-item>
                  </el-form-item>

                  <el-form-item label="填报批次" prop="batch">
                    <!--展示更多信息可能需要更换组件-->
                    <el-select v-model="form.batch" placeholder="请选择">
                      <el-option
                        v-for="item in levelOptions"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                      />
                    </el-select>
                  </el-form-item>

                  <el-form-item label="系统推荐">
                    是否允许系统推荐志愿：
                    <el-switch v-model="isAutoRecommand"></el-switch>
                    <span v-if="isAutoRecommand">&nbsp;&nbsp;允许</span>
                    <span v-if="!isAutoRecommand">&nbsp;不允许</span>
                  </el-form-item>

                  <el-row type="flex" justify="space-around">
                    <el-col :span="8">
                      <el-button type="primary" @click="onSubmit()">立即生成推荐表</el-button>
                    </el-col>
                    <vip :visible="vipDialog" v-if="vipDialog" @closeDialog="closeDialog"></vip>
                  </el-row>
                </el-form>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
      <el-col :span="10">
        <div class="right-container">
          <!--轮播图/宣传图留空-->
          <el-image
            src="https://cdn.attack204.com/gaokao2.jpg"
          >
          </el-image>
        </div>
      </el-col>
      <el-col :span="0.5"></el-col>
    </el-row>

    <!----自定义对话框--->
    <el-dialog title="自定义填报方案" :close-on-click-modal="false" :visible.sync="dialogFormVisible" width="400px">
      <el-form :model="Input_form" ref="Input_form">
        <el-form-item v-for="(item,index) in Input_form.PlanArr" :key="index" :label="item.title"
                      :prop="'PlanArr.'+index+'.value'" :rules="checkRules">
          <el-input type="number" v-model.trim="item.value" :placeholder="item.placeholder" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('Input_form')">取 消</el-button>
        <el-button type="primary" @click="submitForm('Input_form')">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script src="./recommand.js">


</script>


<style lang="scss" scoped>
.form-container {
  padding: 5% 0;
  width: 100%;
  /*background-color: aqua;*/
}
</style>
