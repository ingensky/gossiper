import Vue from 'vue'
import Vuetify from 'vuetify'
import '@babel/polyfill'
import 'api/resources'
import App from 'pages/App.vue'
import store from 'store/store'
import {connect} from './util/ws'
import 'vuetify/dist/vuetify.min.css' // Ensure you are using css-loader

if (frontData.profile) {

    connect();
}

Vue.use(Vuetify)

new Vue({
    el:'#app',
    store: store,
    render: a => a(App)
})

