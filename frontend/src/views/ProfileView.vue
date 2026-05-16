<template>
  <main class="page">
    <div class="page-inner container">

      <div class="page-header">
        <h1>Profile</h1>
        <p class="text-mid">Manage your account details and security</p>
      </div>

      <div class="profile-grid">

        <!-- ── Details ──────────────────────────────────────── -->
        <div class="card">
          <h3 class="section-h3">Account Details</h3>
          <div class="divider"></div>

          <div v-if="detailsError" class="alert alert-error">{{ detailsError }}</div>
          <div v-if="detailsSuccess" class="alert alert-success">{{ detailsSuccess }}</div>

          <form @submit.prevent="saveDetails">
            <div class="form-group">
              <label for="fullName">Full name</label>
              <input id="fullName" v-model="details.fullName" type="text" class="field" required/>
            </div>
            <div class="form-group">
              <label for="email">Email address</label>
              <input id="email" v-model="details.email" type="email" class="field" required/>
            </div>
            <button type="submit" class="btn btn-primary" :disabled="detailsLoading">
              <span v-if="detailsLoading" class="spinner"></span>
              {{ detailsLoading ? 'Saving…' : 'Save Changes' }}
            </button>
          </form>
        </div>

        <!-- ── Password ─────────────────────────────────────── -->
        <div class="card">
          <h3 class="section-h3">Change Password</h3>
          <div class="divider"></div>

          <div v-if="pwError" class="alert alert-error">{{ pwError }}</div>
          <div v-if="pwSuccess" class="alert alert-success">{{ pwSuccess }}</div>

          <form @submit.prevent="changePassword">
            <div class="form-group">
              <label for="currentPw">Current password</label>
              <input id="currentPw" v-model="pw.current" :type="showPw ? 'text' : 'password'" class="field" required/>
            </div>
            <div class="form-group">
              <label for="newPw">New password</label>
              <input id="newPw" v-model="pw.next" :type="showPw ? 'text' : 'password'" class="field" required minlength="8"/>
            </div>
            <div class="form-group">
              <label for="confirmPw">Confirm new password</label>
              <input id="confirmPw" v-model="pw.confirm" :type="showPw ? 'text' : 'password'" class="field" required/>
            </div>

            <div class="pw-opts">
              <label class="toggle-label">
                <input type="checkbox" v-model="showPw"/> Show passwords
              </label>
            </div>

            <button type="submit" class="btn btn-primary" :disabled="pwLoading">
              <span v-if="pwLoading" class="spinner"></span>
              {{ pwLoading ? 'Updating…' : 'Update Password' }}
            </button>
          </form>
        </div>

        <!-- ── Account info ─────────────────────────────────── -->
        <div class="card account-info">
          <h3 class="section-h3">Account Info</h3>
          <div class="divider"></div>
          <div class="info-row">
            <span class="info-label">Member since</span>
            <span class="info-val">{{ memberSince }}</span>
          </div>
          <div class="info-row">
            <span class="info-label">Account ID</span>
            <span class="info-val mono">{{ userId }}</span>
          </div>
          <div class="divider"></div>
          <button class="btn btn-ghost btn-sm text-sm" style="color:#C0392B" @click="logout">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" width="16" height="16"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/></svg>
            Sign out of account
          </button>
        </div>

      </div>
    </div>
  </main>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'ProfileView',
  data () {
    return {
      details: { fullName: '', email: '' },
      pw: { current: '', next: '', confirm: '' },
      detailsLoading: false, detailsError: '', detailsSuccess: '',
      pwLoading: false, pwError: '', pwSuccess: '',
      showPw: false
    }
  },
  computed: {
    ...mapGetters(['currentUser']),
    memberSince () { return 'Katibu member' },
    userId () { return this.currentUser?.id ? String(this.currentUser.id).slice(0, 8) + '…' : '—' }
  },
  created () {
    if (this.currentUser) {
      this.details.fullName = this.currentUser.fullName || ''
      this.details.email = this.currentUser.email || ''
    }
  },
  methods: {
    async saveDetails () {
      this.detailsError = ''; this.detailsSuccess = ''
      this.detailsLoading = true
      try {
        // API call would go here: await api.users.update(this.details)
        this.$store.dispatch('updateUser', this.details)
        this.detailsSuccess = 'Details updated successfully.'
      } catch (e) {
        this.detailsError = e?.message || 'Failed to update details.'
      } finally {
        this.detailsLoading = false
      }
    },
    async changePassword () {
      this.pwError = ''; this.pwSuccess = ''
      if (this.pw.next !== this.pw.confirm) { this.pwError = 'New passwords do not match.'; return }
      this.pwLoading = true
      try {
        // API call: await api.users.changePassword({ current: this.pw.current, password: this.pw.next })
        this.pwSuccess = 'Password updated successfully.'
        this.pw = { current: '', next: '', confirm: '' }
      } catch (e) {
        this.pwError = e?.message || 'Failed to update password.'
      } finally {
        this.pwLoading = false
      }
    },
    logout () {
      this.$store.dispatch('logout')
      this.$router.push('/')
    }
  }
}
</script>

<style scoped>
.profile-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 24px; }
.account-info { grid-column: 1 / -1; }
.section-h3 { font-size: 1.15rem; margin-bottom: 0; }
.info-row { display: flex; justify-content: space-between; padding: 10px 0; border-bottom: 1px solid var(--border-sub); font-size: 0.9rem; }
.info-row:last-of-type { border-bottom: none; }
.info-label { color: var(--text-mid); font-weight: 500; }
.info-val    { color: var(--text-dark); font-weight: 600; }
.mono { font-family: 'Inter', monospace; font-size: 0.85rem; }
.pw-opts { margin-bottom: 16px; }
.toggle-label { display: flex; align-items: center; gap: 8px; font-size: 0.875rem; color: var(--text-mid); cursor: pointer; }
.spinner { display: inline-block; width: 14px; height: 14px; border: 2px solid rgba(255,255,255,0.4); border-top-color: white; border-radius: 50%; animation: spin 0.7s linear infinite; }
@keyframes spin { to { transform: rotate(360deg); } }

@media (max-width: 760px) {
  .profile-grid { grid-template-columns: 1fr; }
  .account-info { grid-column: 1; }
}
</style>
