<template>
<div class="user-content">
  <div class="app-container">
    <el-row class="user-content">
      <el-col :span="6" class="profile">
        <el-avatar class="avatar" :src="require('@/assets/img/avatar.gif')" shape="square" :size="120" fit="fill"></el-avatar>
        <!-- <img class="user-avatar" src="@/assets/img/avatar.gif"><br>
        <p><span class="title"><i class="el-icon-phone"></i>&nbsp;&nbsp;{{ user.phone }}</span></p>
        <p><span class="title"><i class="el-icon-message"></i>&nbsp;&nbsp;{{ user.email }}</span></p>
        <p><span class="title"><i class="el-icon-open"></i>&nbsp;&nbsp;{{ user.status == '1'?'启用' : '禁用' }}</span></p>
        <p><span class="title"><i class="el-icon-location-outline"></i>&nbsp;&nbsp;上海市浦东大道290弄</span></p> -->
        <br>

        <el-form class="base-info">
          <el-form-item label="用户名">
            <el-input v-if="edit" v-model="user.username" ref="username"></el-input>
            <span v-else>{{ user.username }}</span>
          </el-form-item>
          <el-form-item label="昵称">
            <el-input v-if="edit" v-model="user.nickname"></el-input>
            <span v-else class="form-nickname">{{ user.nickname }}</span>
          </el-form-item>
          <el-form-item label="VIP">
            <span class="form-vip">{{ user.vipIsOrNot ? "会员用户" : "您还不是会员" }}</span>
          </el-form-item>
          <el-button v-if="edit" class="base-info-button" plain @click="onClickEdit()">保存修改</el-button>
          <el-button v-else class="base-info-button" plain @click="onClickEdit()">完善信息</el-button>
        </el-form>

      </el-col>

      <el-col :span="18" style="padding-left:50px;">
        <el-form class="form1"  label-width="80px">
          <h4>基本信息</h4>
          <el-form-item label="高考分数">
            <el-input v-if="!user.vipIsOrNot && edit" v-model="user.score" ref="score" @blur="getRank(user.score)"></el-input>
            <span v-else>{{ user.score }}</span>
          </el-form-item>
          <el-form-item label="排名预测">
            <el-input v-if="!user.vipIsOrNot && edit" v-model="user.provinceRank" ref="provinceRank"></el-input>
            <span v-else>{{ user.provinceRank }}</span>
          </el-form-item>
          <el-form-item label="选课">
            <el-checkbox-group v-model="subjectList"  v-if="!user.vipIsOrNot && edit" :max="3">
              <el-checkbox label="1" v-model="subject">物理</el-checkbox>
              <el-checkbox label="2" v-model="subject">化学</el-checkbox>
              <el-checkbox label="3" v-model="subject">生物</el-checkbox>
              <el-checkbox label="4" v-model="subject">历史</el-checkbox>
              <el-checkbox label="5" v-model="subject">地理</el-checkbox>
              <el-checkbox label="6" v-model="subject">政治</el-checkbox>
            </el-checkbox-group>
            <span v-else>{{ user.subject[0] }}{{user.subject[1]}},{{ user.subject[2] }}{{user.subject[3]}},{{ user.subject[4] }}{{user.subject[5]}}</span>
          </el-form-item>
        </el-form>
        <el-divider></el-divider>

        <el-form class="form2" label-width="80px">
          <h4>额外信息</h4>
          <el-form-item label="手机">
            <span>{{ user.phone }}</span>
          </el-form-item>
          <el-form-item label="邮箱">
            <span>{{ user.email }}</span>
          </el-form-item>
          <el-form-item label="其它">
            <span> 暂时不知道还有什么信息</span>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>

  </div>

     <div style="margin-left: 35%">
        <div>Copyright@2021-2021</div>
        <div>山东鲁济名师教育科技股份有限公司</div>
      </div>

</div>

</template>

<script src="./profile.js">
</script>


<style rel="stylesheet/scss" lang="scss" scoped>
  @import "src/styles/common.scss";
  .profile{
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
  }
  .avatar{
    margin-top: 10px;
    margin-left: auto;
    margin-right: auto;
  }
  .base-info{
    margin-left: 40px;
    width: 150px;
    line-height: 30px;
  }
  .form-nickname{
    margin-left: 13px;
  }
  .form-vip{
    margin-left: 16px;
  }
  .base-info-button{
    margin-top: 50px;
  }
  .form1{
    margin-bottom: 25px ;
  }
  .form2{
    margin-top: 40px;
  }
</style>

