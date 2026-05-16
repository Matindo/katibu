import Vue from 'vue'
import ToastPlugin, { toastState, removeToast } from '@/plugins/toast'

Vue.use(ToastPlugin)

beforeEach(() => {
  // Clear all toasts between tests
  toastState.toasts.splice(0)
})

describe('ToastPlugin', () => {
  it('installs $toast on Vue instances', () => {
    const vm = new Vue()
    expect(typeof vm.$toast.success).toBe('function')
    expect(typeof vm.$toast.error).toBe('function')
    expect(typeof vm.$toast.warning).toBe('function')
    expect(typeof vm.$toast.info).toBe('function')
  })

  it('adds a success toast to toastState', () => {
    const vm = new Vue()
    vm.$toast.success('Saved!')
    expect(toastState.toasts).toHaveLength(1)
    expect(toastState.toasts[0].type).toBe('success')
    expect(toastState.toasts[0].message).toBe('Saved!')
  })

  it('adds an error toast to toastState', () => {
    const vm = new Vue()
    vm.$toast.error('Something went wrong')
    expect(toastState.toasts[0].type).toBe('error')
  })

  it('adds a warning toast to toastState', () => {
    const vm = new Vue()
    vm.$toast.warning('Check your input')
    expect(toastState.toasts[0].type).toBe('warning')
  })

  it('adds an info toast to toastState', () => {
    const vm = new Vue()
    vm.$toast.info('Link copied')
    expect(toastState.toasts[0].type).toBe('info')
  })

  it('assigns unique ids to each toast', () => {
    const vm = new Vue()
    vm.$toast.success('First')
    vm.$toast.success('Second')
    const [a, b] = toastState.toasts
    expect(a.id).not.toBe(b.id)
  })

  it('removes a toast by id', () => {
    const vm = new Vue()
    vm.$toast.success('To remove')
    const id = toastState.toasts[0].id
    removeToast(id)
    expect(toastState.toasts).toHaveLength(0)
  })

  it('auto-dismisses after 5 seconds', () => {
    jest.useFakeTimers()
    const vm = new Vue()
    vm.$toast.success('Auto dismiss')
    expect(toastState.toasts).toHaveLength(1)
    jest.advanceTimersByTime(5000)
    expect(toastState.toasts).toHaveLength(0)
    jest.useRealTimers()
  })
})
