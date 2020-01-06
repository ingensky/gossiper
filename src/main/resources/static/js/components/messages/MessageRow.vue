<template>
    <v-card class="my-2">
        <v-card-text primary-title>
            <div>
                <v-avatar v-if="message.author && message.author.userpic">
                    <v-img
                            :src="message.author.userpic"
                            :alt="message.author.name"
                    />
                </v-avatar>
                <v-avatar v-else color="indigo">
                    <v-icon dark>account_circle</v-icon>
                </v-avatar>
                <span class="pl-3">{{authorName}}</span>
            </div>
            <div class="pt-3">
                {{message.text}}
                <i>{{message.creationTimestamp}}</i>
            </div>
        </v-card-text>
        <media
                v-if="message.link"
                :message="message"
        ></media>
        <v-card-actions>
            <v-btn icon @click="edit" small>
                <v-icon>edit</v-icon>
            </v-btn>
            <v-btn icon @click="del" small>
                <v-icon>delete_forever</v-icon>
            </v-btn>
        </v-card-actions>
        <comment-list :comments="message.comments" :message-id="message.id"/>
    </v-card>
</template>

<script>
    import {mapActions} from 'vuex';
    import Media from "components/media/Media.vue";
    import CommentList from "../comment/CommentList.vue";

    export default {
        props: ['message', 'editMessage'],
        components: {CommentList, Media},
        computed: {
            authorName() {
                return this.message.author ? this.message.author.name : "unknown";
            }
        },
        methods: {
            ...mapActions(['removeMessageAction']),
            edit() {
                this.editMessage(this.message)

            },
            del() {
                this.removeMessageAction(this.message)
            }
        }
    }
</script>

<style scoped>

</style>