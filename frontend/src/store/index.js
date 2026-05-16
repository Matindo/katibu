import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token: localStorage.getItem('katibu_token') || null,
    user: JSON.parse(localStorage.getItem('katibu_user') || 'null'),
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
      localStorage.setItem('katibu_user', JSON.stringify(user))
    },
    CLEAR_AUTH (state) {
      state.token = null
      state.user = null
      localStorage.removeItem('katibu_token')
      localStorage.removeItem('katibu_user')
    },
    SET_USER (state, user) {
      state.user = { ...state.user, ...user }
      localStorage.setItem('katibu_user', JSON.stringify(state.user))
    },
    SET_CURRENT_PROJECT (state, project) {
      state.currentProject = project
    }
  },
  actions: {
    login ({ commit }, authData) { commit('SET_AUTH', authData) },
    logout ({ commit }) { commit('CLEAR_AUTH') },
    updateUser ({ commit }, user) { commit('SET_USER', user) },
    setCurrentProject ({ commit }, project) { commit('SET_CURRENT_PROJECT', project) }
  }
})
