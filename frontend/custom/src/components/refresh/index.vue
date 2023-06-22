<template>
  <div></div>
</template>

<script>
export default {
  created() {
    let path = this.$route.query.path;
    this.$router.replace(path);
  }
}
</script>


<style scoped>

</style>