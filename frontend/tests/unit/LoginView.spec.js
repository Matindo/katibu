import { shallowMount } from '@vue/test-utils'
import LoginView from '@/views/auth/LoginView.vue'

// Mock api module
jest.mock('@/api/index', () => ({
  auth: {
    login: jest.fn()
  }
}))

import api from '@/api/index'

const mockRoute = { query: {} }
const mockRouter = { push: jest.fn() }
const mockStore = { dispatch: jest.fn() }
const mockToast = { success: jest.fn(), error: jest.fn() }

function mountLogin () {
  return shallowMount(LoginView, {
    mocks: {
      $route: mockRoute,
      $router: mockRouter,
      $store: mockStore,
      $toast: mockToast
    }
  })
}

beforeEach(() => {
  jest.clearAllMocks()
})

describe('LoginView', () => {
  it('renders email and password fields', () => {
    const wrapper = mountLogin()
    expect(wrapper.find('input[type="email"]').exists()).toBe(true)
    expect(wrapper.find('input[type="password"]').exists()).toBe(true)
  })

  it('renders the Sign In button', () => {
    const wrapper = mountLogin()
    expect(wrapper.find('button[type="submit"]').text()).toContain('Sign In')
  })

  it('shows an error message when login fails', async () => {
    api.auth.login.mockRejectedValue(new Error('Invalid email or password'))
    const wrapper = mountLogin()
    wrapper.vm.form.email = 'bad@example.com'
    wrapper.vm.form.password = 'wrong'
    await wrapper.find('form').trigger('submit')
    await wrapper.vm.$nextTick()
    await new Promise(r => setTimeout(r, 0))
    expect(wrapper.find('.alert-error').text()).toBe('Invalid email or password')
  })

  it('dispatches login and redirects on success', async () => {
    api.auth.login.mockResolvedValue({
      token: 'tok123',
      userId: 'u1',
      email: 'user@example.com',
      fullName: 'Test User'
    })
    const wrapper = mountLogin()
    wrapper.vm.form.email = 'user@example.com'
    wrapper.vm.form.password = 'password123'
    await wrapper.find('form').trigger('submit')
    await new Promise(r => setTimeout(r, 0))
    expect(mockStore.dispatch).toHaveBeenCalledWith('login', {
      token: 'tok123',
      user: { email: 'user@example.com', fullName: 'Test User', id: 'u1' }
    })
    expect(mockRouter.push).toHaveBeenCalledWith('/projects')
  })

  it('redirects to the redirect query param on success', async () => {
    api.auth.login.mockResolvedValue({ token: 't', userId: 'u', email: 'e@e.com', fullName: 'E' })
    const wrapper = shallowMount(LoginView, {
      mocks: {
        $route: { query: { redirect: '/profile' } },
        $router: mockRouter,
        $store: mockStore,
        $toast: mockToast
      }
    })
    await wrapper.find('form').trigger('submit')
    await new Promise(r => setTimeout(r, 0))
    expect(mockRouter.push).toHaveBeenCalledWith('/profile')
  })
})
