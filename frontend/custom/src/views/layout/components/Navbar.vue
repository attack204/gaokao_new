<template>
  <el-menu class="navbar" mode="horizontal">
    <breadcrumb></breadcrumb>
    <div class="right-menu">
      <div v-if="isLogin">
        <div class="international right-menu-item">
          欢迎您，{{ userName }}
        </div>
        <el-dropdown class="avatar-container right-menu-item" trigger="click">
          <div class="avatar-wrapper">
            <img class="user-avatar" src="@/assets/img/avatar.gif">
            <i class="el-icon-caret-bottom"/>
          </div>
          <el-dropdown-menu slot="dropdown">
            <router-link to="/account/profile">
            <el-dropdown-item>
                {{ $t('navbar.profile') }}
              </el-dropdown-item>
            </router-link>
            <router-link to="/account/updatePwd">
              <el-dropdown-item>
                {{ $t('navbar.updatePwd') }}
              </el-dropdown-item>
            </router-link>
            <el-dropdown-item divided>
              <span style="display:block;" @click="logout">{{ $t('navbar.logOut') }}</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
      <div v-else>
        <router-link to="/login">
          <el-button type="primary">登录/注册</el-button>
        </router-link>
      </div>
    </div>
  </el-menu>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import {logOut, getUserInfo, getToken} from '@/utils/auth';
export default {
  data() {
    return {
      userName: "",
      isLogin: false
    }
  },
  components: {
    Breadcrumb,
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar'
    ])
  },
  mounted() {
    this.initData();
  },
  methods: {
    initData() {
      if (!getToken()) {
        return;
      }
      this.isLogin = true;
      //util里的sessionStorage不好使qwq?(也许可以考虑删掉owo
      let userInfo = JSON.parse(localStorage.getItem('userGaoKaoInfo'));
      console.log("qwq", userInfo);
      this.userName = userInfo.nickname === "" ? userInfo.phone : userInfo.nickname;
    },
    toggleSideBar() {
      this.$store.dispatch('ToggleSideBar')
    },
    logout() {
      logOut()
      this.$router.push({path: '/login'})
      window.location.reload()
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>

  .navbar {
    height: 70px;
    padding: 10px 5px;
    line-height: 50px;
    border-radius: 0px !important;
    .breadcrumb-container{
      float: left;
    }
    .errLog-container {
      display: inline-block;
      vertical-align: top;
    }
    .right-menu {
      float: right;
      height: 100%;
      &:focus{
        outline: none;
      }
      .right-menu-item {
        display: inline-block;
        margin: 0 8px;
      }
      .screenfull {
        height: 20px;
      }
      .international{
        vertical-align: top;
      }
      .theme-switch {
        vertical-align: 15px;
      }
      .avatar-container {
        height: 50px;
        margin-right: 30px;
        .avatar-wrapper {
          margin-top: 5px;
          position: relative;
          .user-avatar {
            cursor: pointer;
            width: 40px;
            height: 40px;
            border-radius: 10px;
          }
          .el-icon-caret-bottom {
            cursor: pointer;
            position: absolute;
            right: -20px;
            top: 25px;
            font-size: 12px;
          }
        }
      }
    }
  }
</style>
