<template>
<div class="profile-main-container">
  <navbar></navbar>
  <profile-main></profile-main>
  <footer></footer>
</div>
</template>

<script>
import { Navbar, ProfileMain } from './components'
import ResizeMixin from './mixin/ResizeHandler'

export default {
  name: 'layout',
  components: {
    Navbar,
    ProfileMain
  },
  mixins: [ResizeMixin],
  computed: {
    sidebar() {
      return this.$store.state.app.sidebar
    },
    device() {
      return this.$store.state.app.device
    },
    classObj() {
      return {
        hideSidebar: !this.sidebar.opened,
        openSidebar: this.sidebar.opened,
        withoutAnimation: this.sidebar.withoutAnimation,
        mobile: this.device === 'mobile'
      }
    }
  },
  methods: {
  }
}
</script>

<style scoped>
.profile-main-container {
  background-color:bisque;
}
</style>