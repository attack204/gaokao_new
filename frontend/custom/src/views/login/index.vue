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

    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="register-container" auto-complete="on"
             label-position="left">

      <h3 class="register-title"> 高考志愿推荐系统登录 </h3>
      <!-- <lang-select class="set-language"/> -->

      <el-form-item prop="username">
        <el-row :gutter="20">
          <h5>测试用户名：12345678910</h5>
          <h5>测试密码：12345678</h5>
          <el-col :span="2">
            <svg-icon icon-class="user"/>
          </el-col>
          <el-col :span="22">
            <el-input
              v-model="loginForm.username"
              :placeholder="$t('login.username')"
              name="username"
              type="text"
              auto-complete="on"
            />
          </el-col>
        </el-row>
      </el-form-item>

      <el-form-item prop="password">
        <el-row :gutter="20">
          <el-col :span="2">
            <svg-icon icon-class="password"/>
          </el-col>
          <el-col :span="20">
            <el-input
              :type="pwdType"
              v-model="loginForm.password"
              :placeholder="$t('login.password')"
              name="password"
              auto-complete="on"
              @keyup.enter.native="handleLogin"/>
          </el-col>
          <el-col :span="2">
            <span class="show-pwd" @click="showPwd">
              <svg-icon icon-class="eye"/>
            </span>
          </el-col>
        </el-row>
      </el-form-item>

      <el-form-item>
        <div class="router-link-style">
          <router-link to="/register">
            &nbsp;没有账号？点击注册
          </router-link>
        </div>
      </el-form-item>

      <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;"
                 @click.native.prevent="handleLogin">{{ $t('login.logIn') }}
      </el-button>
    </el-form>


  </div>
</template>

<script src="./login.js"></script>
<style rel="stylesheet" lang="scss" src="../../styles/loginAndRegister.scss"></style>
