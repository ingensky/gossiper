<template>
    <v-card class="my-2">
        <v-card-text primary-title>
            <i>({{message.id}})</i>
            {{message.text}}
            {{message.creationTimestamp}}
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