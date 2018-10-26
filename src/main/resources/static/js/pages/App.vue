<template>
    <div>
        <div v-if="!profile">Need authorization in
            <a href="/login">Google</a>
        </div>
        <div v-else>
            <div>{{profile.name}}&nbsp;<a href="/logout">Logout</a></div>
            <messages-list :messages="messages"/>
        </div>
    </div>
</template>

<script>
    import MessagesList from 'components/messages/MessagesList.vue'
    export default {
        components: {
            MessagesList
        },
        data() {
            return {
                messages: frontData.messages,
                profile: frontData.profile
            }
        }
    }
</script>

<style>

</style>