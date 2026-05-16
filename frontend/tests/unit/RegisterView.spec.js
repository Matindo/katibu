import { shallowMount } from '@vue/test-utils'
import RegisterView from '@/views/auth/RegisterView.vue'

jest.mock('@/api/index', () => ({
  auth: {
    register: jest.fn()
  }
}))

import api from '@/api/index'

const mockRouter = { push: jest.fn() }
const mockStore = { dispatch: jest.fn() }
const mockToast = { success: jest.fn(), error: jest.fn() }

function mountRegister () {
  return shallowMount(RegisterView, {
    mocks: { $router: mockRouter, $store: mockStore, $toast: mockToast }
  })
}

beforeEach(() => jest.clearAllMocks())

describe('RegisterView', () => {
  it('renders name, email, and password fields', () => {
    const wrapper = mountRegister()
    expect(wrapper.find('input[autocomplete="name"]').exists()).toBe(true)
    expect(wrapper.find('input[type="email"]').exists()).toBe(true)
    expect(wrapper.find('input[autocomplete="new-password"]').exists()).toBe(true)
  })

  it('shows an error when passwords do not match', async () => {
    const wrapper = mountRegister()
    wrapper.vm.form.password = 'password123'
    wrapper.vm.form.confirm = 'different'
    wrapper.vm.form.fullName = 'Test'
    wrapper.vm.form.email = 'test@test.com'
    await wrapper.find('form').trigger('submit')
    expect(wrapper.find('.alert-error').text()).toBe('Passwords do not match.')
    expect(api.auth.register).not.toHaveBeenCalled()
  })

  it('calls register API and logs in on success', async () => {
    api.auth.register.mockResolvedValue({
      token: 'tok',
      userId: 'u1',
      email: 'new@test.com',
      fullName: 'New User'
    })
    const wrapper = mountRegister()
    wrapper.vm.form = { fullName: 'New User', email: 'new@test.com', password: 'pass1234', confirm: 'pass1234' }
    await wrapper.find('form').trigger('submit')
    await new Promise(r => setTimeout(r, 0))
    expect(api.auth.register).toHaveBeenCalledWith({ fullName: 'New User', email: 'new@test.com', password: 'pass1234' })
    expect(mockStore.dispatch).toHaveBeenCalledWith('login', {
      token: 'tok',
      user: { email: 'new@test.com', fullName: 'New User', id: 'u1' }
    })
    expect(mockRouter.push).toHaveBeenCalledWith('/projects')
  })

  it('shows an error message when registration fails', async () => {
    api.auth.register.mockRejectedValue(new Error('Email already registered'))
    const wrapper = mountRegister()
    wrapper.vm.form = { fullName: 'A', email: 'a@a.com', password: 'pass1234', confirm: 'pass1234' }
    await wrapper.find('form').trigger('submit')
    await new Promise(r => setTimeout(r, 0))
    expect(wrapper.find('.alert-error').text()).toBe('Email already registered')
  })
})
