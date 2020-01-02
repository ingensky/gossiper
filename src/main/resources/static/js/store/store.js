import Vue from 'vue'
import Vuex from 'vuex'
import messagesApi from "../api/messages";
import commentApi from "../api/comment"

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        messages: messages,
        profile: frontData.profile
    },
    getters: {
        sortedMessages: state => (state.messages || []).sort((a, b) => (b.id - a.id))
    },
    mutations: {
        addMessageMutation(state, message) {
            state.messages = [
                ...state.messages, message
            ]
        },
        updateMessageMutation(state, message) {
            const updateIndex = state.messages.findIndex(item => item.id === message.id)
            state.messages = [
                ...state.messages.slice(0, updateIndex),
                message,
                ...state.messages.slice(updateIndex + 1)
            ]
        },
        removeMessageMutation(state, message) {
            const deleteIndex = state.messages.findIndex(item => item.id === message.id)
            if (deleteIndex > -1) {
                state.messages = [
                    ...state.messages.slice(0, deleteIndex), ...state.messages.slice(deleteIndex + 1)
                ]
            }
        },
        addCommentMutation(state, comment) {
            const updateIndex = state.messages.findIndex(item => item.id === comment.message.id)
            const message = state.messages[updateIndex];

            state.messages = [
                ...state.messages.slice(0, updateIndex),
                {
                    ...message,
                    comments: [
                        ...message.comments,
                        comment
                    ]
                },
                ...state.messages.slice(updateIndex + 1)
            ]
        }
    },
    actions: {
        async addMessageAction({commit, state}, message) {
            const result = await messagesApi.add(message)
            const data = await result.json()
            const index = state.messages.findIndex(item => item.id === data.id);

            if (index > -1) {
                // this.messages.splice(index, 1, data)
                commit("updateMessageMutation", data)
            } else {
                // this.messages.push(data);
                commit("addMessageMutation", data)

            }

        },
        async updateMessageAction({commit}, message) {
            const result = await messagesApi.update(message)
            const data = await result.json()
            // this.messages.splice(index, 1, data)
            commit("updateMessageMutation", data)

        },
        async removeMessageAction({commit}, message) {
            const result = await messagesApi.remove(message.id)
                if (result.ok){
                    commit('removeMessageMutation', message)
                }
        },
        async addCommentAction({commit, state}, comment) {
            const response = await commentApi.add(comment);
            const data = response.json();
            commit("addCommentMutation", comment)
        }
    }
})