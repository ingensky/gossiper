<template>
    <v-app>
        <v-toolbar app>
            <v-toolbar-title>Gossiper</v-toolbar-title>
            <v-btn flat
                   v-if="profile"
                   @click="showMessages"
                   :disabled="$route.path === '/'">
                Messages
            </v-btn>
            <v-spacer/>
            <v-btn flat
                   v-if="profile"
                   @click="showProfile"
                   :disabled="$route.path === '/profile'">
                {{profile.name}}
            </v-btn>&nbsp;
            <v-btn v-if="profile" icon href="/logout">
                <v-icon>exit_to_app</v-icon>
            </v-btn>
        </v-toolbar>
        <v-content>
            <router-view>

            </router-view>
        </v-content>
    </v-app>
</template>

<script>
    import {mapState, mapMutations} from 'vuex'
    import {addHandler} from "util/ws"

    export default {
        computed: mapState(["profile"]),
        methods: {
            ...mapMutations([
                'addMessageMutation',
                'updateMessageMutation',
                'removeMessageMutation',
                'addCommentMutation'
            ]),
            showMessages(){
                this.$router.push("/")
            },
            showProfile(){
                this.$router.push("/profile")
            },
        },
        created() {
            this.$vuetify.theme.primary = "#FF00FF"


            addHandler(data => {
                if (data.objectType === 'MESSAGE') {
                    switch (data.eventType) {
                        case 'CREATE':
                            this.addMessageMutation(data.body)
                            break
                        case 'UPDATE':
                            this.updateMessageMutation(data.body)
                            break;
                        case 'REMOVE':
                            this.removeMessageMutation(data.body)
                            break;
                        default:
                            console.log(`EventType "${data.eventType}" is unknown`)
                    }
                } else if (data.objectType === 'COMMENT') {
                    switch (data.eventType) {
                        case 'CREATE':
                            this.addCommentMutation(data.body)
                            break
                        default:
                            console.log(`EventType "${data.eventType}" is unknown`)
                    }
                } else {
                    console.log(`ObjectType "${data.objectType}" is unknown`)
                }
            })
        },
        beforeMount() {
            if (!this.profile) {
                this.$router.replace('/auth')
            }
        }
    }
</script>

<style>

</style>