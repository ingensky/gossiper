<template>
    <v-container>
        <v-layout justify-space-around>
            <v-flex :xs6="!$vuetify.breakpoint.xsOnly">
                <div class="title mb-3">User profile</div>
                <v-layout row justify-space-beetwen>
                    <v-flex class="px-2">
                        <v-img :src="profile.userpic"/>
                    </v-flex>
                    <v-flex class="px-2">
                        <v-layout column>
                            <v-flex>{{profile.name}}</v-flex>
                            <v-flex>{{profile.email}}</v-flex>
                            <v-flex>{{profile.locale}}</v-flex>
                            <v-flex>{{profile.gender}}</v-flex>
                            <v-flex>{{profile.lastVisit}}</v-flex>
                        </v-layout>
                    </v-flex>
                </v-layout>
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script>
    import {mapState} from 'vuex'

    export default {
        name: "Profile",
        computed: mapState(['profile'])
    }
</script>

<style scoped>
    /*img {*/
    /*    width: 100px;*/
    /*    height: 100px;*/
    /*}*/

</style>