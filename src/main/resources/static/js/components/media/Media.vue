<template>
    <v-card>
        <v-flex v-if="type === 'href'" xs12 sm6 offset-sm3>
            <v-img
                    v-if="message.linkCover"
                    :src="message.linkCover"
                    aspect-ratio="2.75"
            />
            <v-card-title>
                <div>
                    <h3>
                        <a :href="message.link">{{message.linkTitle || message.link}}</a>
                    </h3>
                    <div v-if="message.linkDescription">{{message.linkDescription}}</div>
                </div>
            </v-card-title>
        </v-flex>
        <v-flex v-if="type === 'image'" xs12 sm6 offset-sm3>
            <a :href="message.link">
                <v-img
                        v-if="message.linkCover"
                        :src="message.linkCover"
                        aspect-ratio="2.75"
                />
            </a>
        </v-flex>
        <v-flex v-if="type === 'youtube'" xs12 sm6 offset-sm3>
            <youtube :src="message.link"/>
        </v-flex>

    </v-card>
</template>

<script>
    import Youtube from "./Youtube.vue";
    export default {
        name: "Media",
        components: {Youtube},
        props: ["message"],
        data() {
            return {
                type: 'href'
            };
        },
        beforeMount() {
            if (this.message.link.indexOf("youtu") > -1) {
                this.type = "youtube"
            } else if (this.message.link.match(/\.(jpg|png|bmp|gif)$/) !== null) {
                this.type = 'image';
            } else {
                this.type = "href"
            }
        }
    }
</script>

<style scoped>

</style>