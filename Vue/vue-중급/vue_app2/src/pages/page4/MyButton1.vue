<template>
    <button type="button" @click="$emit('click')">
        <font-awesome-icon icon="check" />
        {{ text }}
    </button>
</template>
<script>
    export default {
        name: "MyButton1",
        props: [ "text" ],
    }
</script>
<style scoped>
    button { padding: 0.4em 1.5em; margin-right: 4px; }
</style>
