<template>
  <div class="toast-container" aria-live="polite" aria-atomic="false">
    <transition-group name="toast" tag="div">
      <div
        v-for="t in toasts"
        :key="t.id"
        :class="['toast', `toast-${t.type}`]"
        role="alert"
      >
        <span class="toast-icon" aria-hidden="true">
          <svg v-if="t.type === 'success'" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M20 6L9 17l-5-5"/></svg>
          <svg v-else-if="t.type === 'error'" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/></svg>
          <svg v-else-if="t.type === 'warning'" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"/><line x1="12" y1="9" x2="12" y2="13"/><line x1="12" y1="17" x2="12.01" y2="17"/></svg>
          <svg v-else viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/></svg>
        </span>
        <span class="toast-message">{{ t.message }}</span>
        <button class="toast-close" @click="dismiss(t.id)" aria-label="Dismiss">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" width="14" height="14"><path d="M6 18L18 6M6 6l12 12"/></svg>
        </button>
      </div>
    </transition-group>
  </div>
</template>

<script>
import { toastState, removeToast } from '../plugins/toast'

export default {
  name: 'AppToast',
  computed: {
    toasts () { return toastState.toasts }
  },
  methods: {
    dismiss (id) { removeToast(id) }
  }
}
</script>

<style scoped>
.toast-container {
  position: fixed;
  top: 80px;
  right: 20px;
  z-index: 9999;
  display: flex;
  flex-direction: column;
  gap: 10px;
  max-width: 360px;
  width: calc(100vw - 40px);
  pointer-events: none;
}

.toast {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  padding: 13px 14px;
  border-radius: 10px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.12);
  font-size: 0.9rem;
  font-weight: 500;
  line-height: 1.4;
  pointer-events: all;
  border-left: 4px solid transparent;
  background: #fff;
}

.toast-success { border-color: #27ae60; background: #f0faf4; color: #1a6b3a; }
.toast-error   { border-color: #C0392B; background: #fdf2f1; color: #7b1f18; }
.toast-warning { border-color: #e67e22; background: #fdf6ec; color: #7a4210; }
.toast-info    { border-color: #2980b9; background: #eef5fb; color: #1a4f72; }

.toast-icon { flex-shrink: 0; width: 18px; height: 18px; margin-top: 1px; }
.toast-icon svg { width: 18px; height: 18px; }

.toast-message { flex: 1; }

.toast-close {
  flex-shrink: 0;
  background: none;
  border: none;
  cursor: pointer;
  padding: 2px;
  border-radius: 4px;
  opacity: 0.5;
  transition: opacity 0.15s;
  color: inherit;
  margin-top: 1px;
}
.toast-close:hover { opacity: 1; }

/* Transition */
.toast-enter-active, .toast-leave-active { transition: all 0.25s ease; }
.toast-enter { opacity: 0; transform: translateX(24px); }
.toast-leave-to { opacity: 0; transform: translateX(24px); }

@media (max-width: 480px) {
  .toast-container { top: auto; bottom: 20px; right: 10px; left: 10px; max-width: 100%; width: auto; }
}
</style>
