<template>
  <div class="poster">
    <vue-particles
      class="particles-js"
      color="#fff"
      :particleOpacity="0.7"
      :particlesNumber="80"
      shapeType="triangle"
      :particleSize="4"
      linesColor="#fff"
      :linesWidth="1"
      :lineLinked="true"
      :lineOpacity="0.4"
      :linesDistance="200"
      :moveSpeed="3"
      :hoverEffect="true"
      hoverMode="grab"
      :clickEffect="true"
      clickMode="push">
    </vue-particles>
    <el-form class="register-container" ref="regForm" :model="regForm" :rules="regRules" label-position="left"
             label-width="80px">
      <h3 class="register-title"> 志愿推荐系统注册 </h3>
      <!-- <lang-select class="set-language"/> -->
      <el-form-item prop="nickname" label="昵称">
        <el-input
          v-model="regForm.nickname"
          name="nickname"
          type="text"
        />
      </el-form-item>

      <el-form-item prop="phone" label="手机号">
        <el-input
          v-model="regForm.phone"
          name="phone"
          type="text"
        />
      </el-form-item>

      <el-form-item prop="password" label="密码">
        <el-input
          :type="pwdType"
          v-model="regForm.password"
          name="password"
        />
        <!-- <span class="show-pwd" @click="showPwd">
          <svg-icon icon-class="eye" />
        </span> -->
      </el-form-item>

      <el-form-item prop="confirmpwd" label="确认密码">
        <el-input
          :type="pwdType"
          v-model="regForm.confirmpwd"
          name="confirmpwd"
        />
      </el-form-item>

      <el-form-item prop="veryCode" label="验证码">
        <el-input
          v-model="regForm.veryCode"
          name="veryCode"
        >
          <template slot="append">
            <el-button type="primary" @click="getVeryCode">
              获取验证码
            </el-button>
          </template>
        </el-input>
      </el-form-item>

      <el-form-item label="成绩信息">
        <el-input
          v-model="userInfoStr"
        >
          <template slot="append">
            <el-button type="primary" @click="showInfoDialog">点击修改</el-button>
          </template>
        </el-input>
      </el-form-item>

      <el-form-item>
        <div class="router-link-style">
          <router-link to="/login">已有帐号，前往登录</router-link>
        </div>
      </el-form-item>
      <el-button type="primary" :loading="loading" style="width:100%;margin-bottom:30px;"
                 @click.native.prevent="handleRegister"> 注册
      </el-button>
    </el-form>
    <user-info-dialog></user-info-dialog>
  </div>
</template>

<script src="./register.js"></script>
<style rel="stylesheet" lang="scss" src="../../styles/loginAndRegister.scss"></style>
