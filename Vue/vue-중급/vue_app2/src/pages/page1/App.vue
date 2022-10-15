<template>
  <div id="app">
    <h1>페이지1</h1>
    <Counter1 title="카운터1" description="첫째 컴포넌트" />
    <Counter1 title="카운터1" description="둘째 컴포넌트" />
  </div>
</template>

<script>
  import Counter1 from './Counter1.vue'

  export default {
    name: "App",
    components: { Counter1 }
  }
</script>

<style scoped>
  div#app { padding: 0 30px 30px 30px; margin: 30px auto; max-width: 400px;
    border: 1px solid #ccc; box-shadow: 3px 3px 3px #aaa; }
</style>
