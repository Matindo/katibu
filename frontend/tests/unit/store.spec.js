import Vue from 'vue'
import Vuex from 'vuex'
import store from '@/store/index'

Vue.use(Vuex)

beforeEach(() => {
  localStorage.clear()
  // Reset store state between tests
  store.commit('CLEAR_AUTH')
  store.commit('SET_CURRENT_PROJECT', null)
})

describe('Vuex store', () => {
  describe('isAuthenticated getter', () => {
    it('returns false when there is no token', () => {
      expect(store.getters.isAuthenticated).toBe(false)
    })

    it('returns true after login', async () => {
      await store.dispatch('login', { token: 'abc', user: { email: 'a@b.com', fullName: 'Test', id: '1' } })
      expect(store.getters.isAuthenticated).toBe(true)
    })
  })

  describe('login action', () => {
    it('sets token and user in state and localStorage', async () => {
      const user = { email: 'a@b.com', fullName: 'Test', id: '1' }
      await store.dispatch('login', { token: 'tok123', user })

      expect(store.state.token).toBe('tok123')
      expect(store.state.user).toEqual(user)
      expect(localStorage.getItem('katibu_token')).toBe('tok123')
      expect(JSON.parse(localStorage.getItem('katibu_user'))).toEqual(user)
    })
  })

  describe('logout action', () => {
    it('clears token and user from state and localStorage', async () => {
      await store.dispatch('login', { token: 'tok', user: { email: 'a@b.com', fullName: 'A', id: '1' } })
      await store.dispatch('logout')

      expect(store.state.token).toBeNull()
      expect(store.state.user).toBeNull()
      expect(localStorage.getItem('katibu_token')).toBeNull()
      expect(localStorage.getItem('katibu_user')).toBeNull()
    })
  })

  describe('updateUser action', () => {
    it('merges user fields without replacing the whole object', async () => {
      await store.dispatch('login', { token: 'tok', user: { email: 'old@b.com', fullName: 'Old Name', id: '1' } })
      await store.dispatch('updateUser', { fullName: 'New Name' })

      expect(store.state.user.fullName).toBe('New Name')
      expect(store.state.user.email).toBe('old@b.com')
    })
  })

  describe('setCurrentProject action', () => {
    it('stores the project in state', async () => {
      const proj = { id: 'p1', name: 'Test Project' }
      await store.dispatch('setCurrentProject', proj)
      expect(store.getters.currentProject).toEqual(proj)
    })
  })
})
