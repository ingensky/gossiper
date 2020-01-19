<template>

</template>

<script>
    import {mapActions} from 'vuex'

    export default {
        name: "LazyLoader",
        methods: mapActions(['loadPageAction']),
        mounted() {  //https://ru.vuejs.org/v2/guide/instance.html#Хуки-жизненного-цикла-экземпляра
            window.onscroll = () => {
                const el = document.documentElement
                const isBottomOfScreen = el.scrollTop + window.innerHeight === el.offsetHeight

                if (isBottomOfScreen) {
                    this.loadPageAction()
                }
            }
        },
        beforeDestroy() {
            window.onscroll = null
        }
    }
</script>

<style scoped>

</style>