import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token: localStorage.getItem('katibu_token') || null,
    user: null,
    currentProject: null
  },
  getters: {
    isAuthenticated: state => !!state.token,
    currentUser: state => state.user,
    currentProject: state => state.currentProject
  },
  mutations: {
    SET_AUTH (state, { token, user }) {
      state.token = token
      state.user = user
      localStorage.setItem('katibu_token', token)
    },
    CLEAR_AUTH (state) {
      state.token = null
      state.user = null
      localStorage.removeItem('katibu_token')
    },
    SET_CURRENT_PROJECT (state, project) {
      state.currentProject = project
    }
  },
  actions: {
    login ({ commit }, authData) {
      commit('SET_AUTH', authData)
    },
    logout ({ commit }) {
      commit('CLEAR_AUTH')
    },
    setCurrentProject ({ commit }, project) {
      commit('SET_CURRENT_PROJECT', project)
    }
  }
})
