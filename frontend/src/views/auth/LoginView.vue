<template>
  <main class="auth-page">
    <div class="blob blob-1"></div>
    <div class="blob blob-2"></div>

    <div class="auth-card glass">
      <div class="auth-logo">
        <router-link to="/" class="auth-brand">
          <img class="auth-brand-logo" src="@/assets/images/high-resolution-color-logo.png" alt="Katibu"/>
        </router-link>
      </div>

      <h2>Welcome back</h2>
      <p class="auth-sub">Sign in to your account to continue</p>

      <div v-if="error" class="alert alert-error">{{ error }}</div>

      <form @submit.prevent="submit">
        <div class="form-group">
          <label for="email">Email address</label>
          <input id="email" v-model="form.email" type="email" class="field" placeholder="you@example.com" required autocomplete="email"/>
        </div>
        <div class="form-group">
          <label for="password">Password</label>
          <input id="password" v-model="form.password" :type="showPw ? 'text' : 'password'" class="field" placeholder="••••••••" required autocomplete="current-password"/>
          <button type="button" class="pw-toggle" @click="showPw = !showPw">
            {{ showPw ? 'Hide' : 'Show' }}
          </button>
        </div>

        <button type="submit" class="btn btn-primary w-full" :disabled="loading">
          <span v-if="loading" class="spinner"></span>
          {{ loading ? 'Signing in…' : 'Sign In' }}
        </button>
      </form>

      <div class="auth-footer">
        Don't have an account?
        <router-link to="/register" class="auth-link">Create one</router-link>
      </div>
    </div>
  </main>
</template>

<script>
import api from '../../api'

export default {
  name: 'LoginView',
  data: () => ({ form: { email: '', password: '' }, loading: false, error: '', showPw: false }),
  methods: {
    async submit () {
      this.error = ''
      this.loading = true
      try {
        const res = await api.auth.login(this.form)
        this.$store.dispatch('login', { token: res.token, user: { email: res.email, fullName: res.fullName, id: res.userId } })
        const redirect = this.$route.query.redirect || '/projects'
        this.$router.push(redirect)
      } catch (e) {
        this.error = e?.message || 'Invalid email or password.'
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.auth-page {
  min-height: 100vh; display: flex; align-items: center; justify-content: center;
  padding: calc(var(--nav-h) + 40px) 24px 60px;
  background: linear-gradient(140deg, #eef9f2 0%, #f8fdf9 60%, #fffdf5 100%);
  position: relative; overflow: hidden;
}
.blob { position: absolute; border-radius: 50%; filter: blur(64px); pointer-events: none; }
.blob-1 { width: 400px; height: 400px; background: rgba(116,198,157,0.2); top: -80px; right: -80px; }
.blob-2 { width: 280px; height: 280px; background: rgba(201,168,76,0.12); bottom: 40px; left: 10%; }

.auth-card {
  width: 100%; max-width: 440px; border-radius: var(--r-xl);
  padding: 48px 40px; box-shadow: var(--sh-lg); position: relative; z-index: 1;
}
.auth-logo { text-align: center; margin-bottom: 28px; }
.auth-brand { display: inline-flex; text-decoration: none; }
.auth-brand-logo { width: 56px; height: 56px; object-fit: contain; }
.auth-card h2 { text-align: center; font-size: 1.6rem; margin-bottom: 8px; }
.auth-sub { text-align: center; color: var(--text-mid); font-size: 0.9375rem; margin-bottom: 28px; }

.form-group { position: relative; }
.pw-toggle {
  position: absolute; right: 14px; top: 38px;
  background: none; border: none; color: var(--text-light);
  font-size: 0.8rem; font-weight: 600; cursor: pointer;
  transition: color var(--t);
}
.pw-toggle:hover { color: var(--green-mid); }

.w-full { width: 100%; justify-content: center; margin-top: 8px; }

.auth-footer {
  text-align: center; margin-top: 24px;
  font-size: 0.9rem; color: var(--text-mid);
}
.auth-link { color: var(--green-deep); font-weight: 600; }
.auth-link:hover { text-decoration: underline; }

.spinner {
  display: inline-block; width: 14px; height: 14px; border: 2px solid rgba(255,255,255,0.4);
  border-top-color: white; border-radius: 50%; animation: spin 0.7s linear infinite;
}
@keyframes spin { to { transform: rotate(360deg); } }

@media (max-width: 480px) {
  .auth-card { padding: 36px 24px; }
}
</style>
