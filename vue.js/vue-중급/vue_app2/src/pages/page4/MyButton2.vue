<template>
    <button type="button" v-on:click="$emit('click')">
        <font-awesome-icon :icon="icon" v-if="icon" />
        {{ text }}
    </button>
</template>
<script>
    export default {
        name: 'MyButton2',
        props: [ "icon", "text" ],
    }
</script>
<style scoped>
    button { padding: 0.4em 1.5em; margin-right: 4px; }
</style>
