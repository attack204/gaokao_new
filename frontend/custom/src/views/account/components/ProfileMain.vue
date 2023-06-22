<template>
<section class="app-main">
    <!-- <transition name="fade" mode="out-in">
      <router-view></router-view>
    </transition> -->
  <div class="app-container">
    <el-row type="flex" justify="space-around">
      <el-col :span="24">
        <!--这个tab不好看的话考虑手写-->
        <el-tabs v-model="activeTabName" tab-position="left" @tab-click="onTabClick">
          <el-tab-pane label="高考信息" name="profile">
              <router-view></router-view>
          </el-tab-pane>
          <el-tab-pane label="我的志愿表" name="myPreferenceList">
            <router-view></router-view>
          </el-tab-pane>
          <el-tab-pane label="我的订单" name="myOrder">
            <router-view></router-view>
          </el-tab-pane>
          <el-tab-pane label="修改密码" name="updatePwd">
            <router-view></router-view>
          </el-tab-pane>
        </el-tabs>
      </el-col>
    </el-row>
  </div>

</section>
</template>

<script>
export default {
  name: 'ProfileMain',
  data() {
    return {
      activeTabName: 'profile'
    }
  },
  methods: {
    onTabClick(tab, event) {
      console.log(tab, event, tab.name)
      sessionStorage.setItem('accountActiveTab', this.activeTabName)
      this.$router.push(tab.name)
    }
  },
  mounted() {
    if (sessionStorage.getItem('accountActiveTab') === null) {
      sessionStorage.setItem('accountActiveTab', 'profile')
    } else {
      this.activeTabName = sessionStorage.getItem('accountActiveTab')
    }
    console.log(this.activeTabName)
  },
}
</script>

<style scoped>
.app-main {
  /*50 = navbar  */
  min-height: calc(100vh - 50px);
  position: relative;
  overflow: hidden;
}
.app-container {
  max-width: 1500px;
  margin-left: auto;
  margin-right: auto;
  padding: 20px;
  background-color: #ffffff;
}
/* .tab-container {
} */
</style>
