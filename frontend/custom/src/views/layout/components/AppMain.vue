<template>
<section class="app-main">
    <!-- <transition name="fade" mode="out-in">
      <router-view></router-view>
    </transition> -->
  <div class="app-container">
    <el-card>
    <el-row>
      <el-col :span="24">
        <div class="tab-container">
          <el-tabs v-model="activeTabName" @tab-click="onTabClick">
           <el-tab-pane label="智能推荐" name="/recommand"></el-tab-pane>
            <el-tab-pane label="手动填报" name="/screen"></el-tab-pane>
            <el-tab-pane label="我的志愿表" name="/preference"></el-tab-pane>
           <el-tab-pane label="我的收藏" name="/stars"></el-tab-pane>
          </el-tabs>
        </div>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <div class="main-container">
          <router-view></router-view>
        </div>
      </el-col>
    </el-row>
    </el-card>
  </div>

</section>
</template>

<script>
export default {
  name: 'AppMain',
  data() {
    return {
    }
  },
  methods: {
    onTabClick(tab, event) {
      console.log(tab, event, tab.name);
      this.$router.push(tab.name);
    }
  },
  mounted() {
    console.log(this.activeTabName);
  },
  computed: {
    activeTabName() {
      return this.$route.path;
    }
  }
}
</script>

<style lang="scss" scoped>
.app-main {
  /*50 = navbar  */
  min-height: calc(100vh - 70px);
  position: relative;
  overflow: hidden;
  //background-color: white;
}
.app-container {
  margin-top: 20px;
  max-width: 1200px;
  margin-left: auto;
  margin-right: auto;
  //background-color: white;
}
/* .tab-container {
} */
.main-container {
  border: black;
  border-width: thick;
}
</style>
