import { shallowMount } from '@vue/test-utils'
import AppToast from '@/components/AppToast.vue'
import { toastState, removeToast } from '@/plugins/toast'

beforeEach(() => {
  toastState.toasts.splice(0)
})

describe('AppToast component', () => {
  it('renders nothing when there are no toasts', () => {
    const wrapper = shallowMount(AppToast)
    expect(wrapper.findAll('.toast')).toHaveLength(0)
  })

  it('renders one toast per entry in toastState', async () => {
    toastState.toasts.push({ id: 1, type: 'success', message: 'Done' })
    toastState.toasts.push({ id: 2, type: 'error', message: 'Failed' })
    const wrapper = shallowMount(AppToast)
    await wrapper.vm.$nextTick()
    expect(wrapper.findAll('.toast')).toHaveLength(2)
  })

  it('applies the correct class based on toast type', async () => {
    toastState.toasts.push({ id: 1, type: 'success', message: 'OK' })
    const wrapper = shallowMount(AppToast)
    await wrapper.vm.$nextTick()
    expect(wrapper.find('.toast-success').exists()).toBe(true)
  })

  it('renders the toast message text', async () => {
    toastState.toasts.push({ id: 1, type: 'info', message: 'Hello world' })
    const wrapper = shallowMount(AppToast)
    await wrapper.vm.$nextTick()
    expect(wrapper.find('.toast-message').text()).toBe('Hello world')
  })

  it('calls removeToast when close button is clicked', async () => {
    toastState.toasts.push({ id: 99, type: 'warning', message: 'Watch out' })
    const wrapper = shallowMount(AppToast)
    await wrapper.vm.$nextTick()
    await wrapper.find('.toast-close').trigger('click')
    expect(toastState.toasts).toHaveLength(0)
  })
})
