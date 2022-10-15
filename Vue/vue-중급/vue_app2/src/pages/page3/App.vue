<template>
  <div id="app">
    <h1>페이지3</h1>
    <Clock v-if="showTimer" class="clockStyle1" format="YYYY-MM-DD HH:mm:ss"></Clock>
    <Clock v-if="showTimer" class="clockStyle2"></Clock>
    <div>
      <button type="button" @click="showTimer = true"
              :disabled="showTimer">시계 보이기</button>
      <button type="button" @click="showTimer = false"
              :disabled="!showTimer">시계 감추기</button>
    </div>
  </div>
</template>

<script>
import Clock from './Clock.vue'

export default {
  name: 'App',
  data: function() {
    return {
      showTimer: true
    }
  },
  components: { Clock }
}
</script>

<style scoped>
    div#app { padding: 0 30px 30px 30px; margin: 30px auto; max-width: 400px;
        border: 1px solid #ccc; box-shadow: 3px 3px 3px #aaa; }
    button { padding: 0.5em 1.5em; margin: 20px 5px 0 0; }
    .clockStyle1, .clockStyle2 { font-size: 15pt; border: 1px solid gray; }
    .clockStyle1 { padding: 3px 12px; margin-right: 5px; background-color: #ffa; }
    .clockStyle2 { padding: 3px 30px; background-color: #bfb; }
</style>
