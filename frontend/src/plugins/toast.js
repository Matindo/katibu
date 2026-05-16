import Vue from 'vue'

// Reactive singleton that AppToast listens to
const state = Vue.observable({ toasts: [] })
let nextId = 1

function add (type, message) {
  const id = nextId++
  state.toasts.push({ id, type, message })
  setTimeout(() => remove(id), 5000)
}

function remove (id) {
  const i = state.toasts.findIndex(t => t.id === id)
  if (i !== -1) state.toasts.splice(i, 1)
}

export const toastState = state
export const removeToast = remove

const ToastPlugin = {
  install (Vue) {
    Vue.prototype.$toast = {
      success (msg) { add('success', msg) },
      error (msg) { add('error', msg) },
      warning (msg) { add('warning', msg) },
      info (msg) { add('info', msg) }
    }
  }
}

export default ToastPlugin
