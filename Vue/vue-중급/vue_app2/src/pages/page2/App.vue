<template>
  <div id="app">
    <h1>페이지2</h1>
    <Counter2 title="카운터2" description="첫째 컴포넌트" color="#ffb"  v-on:send="showValue" />
    <Counter2 title="카운터2" color="#afa"  v-on:send="showValue" />
  </div>
</template>

<script>
  import Counter2 from './Counter2.vue'

  export default {
    name: "App",
    methods: {
      showValue(value) {
        alert(value);
      }
    },
    components: { Counter2 }
  }
</script>

<style scoped>
  div#app { padding: 0 30px 30px 30px; margin: 30px auto; max-width: 400px;
    border: 1px solid #ccc; box-shadow: 3px 3px 3px #aaa; }
</style>
