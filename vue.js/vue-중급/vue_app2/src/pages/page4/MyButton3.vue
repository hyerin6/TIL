<template>
    <button type="button" v-on:click="$emit('click')">
        <font-awesome-icon :icon="icon" v-if="icon" />
        <slot></slot>
    </button>
</template>
<script>
    export default {
        name: 'MyButton3',
        props: [ "icon" ],
    }
</script>
<style scoped>
    button { padding: 0.4em 1.5em; margin-right: 4px; }
</style>
