<template>
  <main class="page">
    <div class="page-inner container">

      <!-- Header -->
      <div class="ph-row">
        <div class="page-header" style="margin-bottom:0">
          <h1>My Projects</h1>
          <p class="text-mid">Ledgers you created or have been added to</p>
        </div>
        <button class="btn btn-primary" @click="showCreate = true">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" width="18" height="18"><path d="M12 4.5v15m7.5-7.5h-15"/></svg>
          New Project
        </button>
      </div>

      <!-- Loading -->
      <div v-if="loading" class="loading-row">
        <span class="spinner-dark"></span> Loading projects…
      </div>

      <!-- Error -->
      <div v-else-if="error" class="alert alert-error mt-16">{{ error }}</div>

      <!-- Empty -->
      <div v-else-if="!projects.length" class="empty-state">
        <div class="empty-icon">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"><path d="M3 7a2 2 0 0 1 2-2h3l2 2h8a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V7Z"/></svg>
        </div>
        <h3>No projects yet</h3>
        <p>Create your first project ledger to start recording transactions.</p>
        <button class="btn btn-primary" @click="showCreate = true">Create Project</button>
      </div>

      <!-- Project cards -->
      <div v-else class="projects-grid">
        <div v-for="p in projects" :key="p.id" class="project-card card" @click="open(p)">
          <div class="pc-header">
            <div>
              <div class="pc-name">{{ p.name }}</div>
              <div class="pc-meta">{{ p.durationType }} &nbsp;·&nbsp; {{ p.startDate }} – {{ p.endDate }}</div>
            </div>
            <span :class="['badge', p.status === 'ACTIVE' ? 'badge-active' : 'badge-archived']">
              {{ p.status }}
            </span>
          </div>
          <p v-if="p.description" class="pc-desc">{{ p.description }}</p>
          <div class="pc-footer">
            <span class="pc-role">{{ isCreator(p) ? 'Creator' : 'Admin Member' }}</span>
            <span class="pc-arrow">→</span>
          </div>
        </div>
      </div>

    </div>

    <!-- ── Create Project Modal ─────────────────────────────── -->
    <div v-if="showCreate" class="modal-overlay" @click.self="showCreate = false">
      <div class="modal">
        <div class="modal-header">
          <h3>New Project</h3>
          <button class="modal-close" @click="showCreate = false">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" width="20" height="20"><path d="M6 18 18 6M6 6l12 12"/></svg>
          </button>
        </div>

        <div v-if="createError" class="alert alert-error">{{ createError }}</div>

        <form @submit.prevent="createProject">
          <div class="form-group">
            <label>Project name *</label>
            <input v-model="form.name" type="text" class="field" placeholder="e.g. Q1 2024 Operations" required/>
          </div>
          <div class="form-group">
            <label>Description</label>
            <textarea v-model="form.description" class="field" rows="2" placeholder="Brief description (optional)"></textarea>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>Duration type *</label>
              <select v-model="form.durationType" class="field" required>
                <option value="">Select…</option>
                <option v-for="d in durationTypes" :key="d" :value="d">{{ d }}</option>
              </select>
            </div>
            <div class="form-group">
              <label>Start date *</label>
              <input v-model="form.startDate" type="date" class="field" required/>
            </div>
          </div>
          <div v-if="form.durationType === 'CUSTOM'" class="form-group">
            <label>End date * (CUSTOM only)</label>
            <input v-model="form.endDate" type="date" class="field" required/>
          </div>
          <div class="modal-actions">
            <button type="button" class="btn btn-ghost" @click="showCreate = false">Cancel</button>
            <button type="submit" class="btn btn-primary" :disabled="createLoading">
              <span v-if="createLoading" class="spinner"></span>
              {{ createLoading ? 'Creating…' : 'Create Project' }}
            </button>
          </div>
        </form>
      </div>
    </div>

  </main>
</template>

<script>
import { mapGetters } from 'vuex'
import api from '../../api'

export default {
  name: 'ProjectsView',
  data () {
    return {
      projects: [], loading: false, error: '',
      showCreate: false, createLoading: false, createError: '',
      form: { name: '', description: '', durationType: '', startDate: '', endDate: '' },
      durationTypes: ['WEEKLY', 'MONTHLY', 'QUARTERLY', 'HALF_YEARLY', 'YEARLY', 'CUSTOM']
    }
  },
  computed: {
    ...mapGetters(['currentUser'])
  },
  created () { this.load() },
  methods: {
    async load () {
      this.loading = true; this.error = ''
      try {
        this.projects = await api.projects.list()
      } catch (e) {
        this.error = e?.message || 'Failed to load projects.'
      } finally { this.loading = false }
    },
    isCreator (p) { return p.creatorId === this.currentUser?.id },
    open (p) { this.$router.push(`/projects/${p.id}`) },
    async createProject () {
      this.createError = ''; this.createLoading = true
      try {
        const payload = { name: this.form.name, description: this.form.description, durationType: this.form.durationType, startDate: this.form.startDate }
        if (this.form.durationType === 'CUSTOM') payload.endDate = this.form.endDate
        await api.projects.create(payload)
        this.showCreate = false
        this.form = { name: '', description: '', durationType: '', startDate: '', endDate: '' }
        await this.load()
      } catch (e) {
        this.createError = e?.message || 'Failed to create project.'
      } finally { this.createLoading = false }
    }
  }
}
</script>

<style scoped>
.ph-row { display: flex; align-items: flex-start; justify-content: space-between; gap: 16px; margin-bottom: 32px; flex-wrap: wrap; }

.loading-row { display: flex; align-items: center; gap: 10px; color: var(--text-mid); padding: 40px 0; }
.spinner-dark { display: inline-block; width: 18px; height: 18px; border: 2px solid var(--border-sub); border-top-color: var(--green-mid); border-radius: 50%; animation: spin 0.7s linear infinite; }
@keyframes spin { to { transform: rotate(360deg); } }

.empty-state { text-align: center; padding: 80px 20px; }
.empty-icon {
  width: 72px; height: 72px; background: var(--green-tint); border-radius: var(--r-xl);
  display: flex; align-items: center; justify-content: center; margin: 0 auto 20px;
  color: var(--green-mid);
}
.empty-icon svg { width: 36px; height: 36px; }
.empty-state h3 { font-size: 1.3rem; margin-bottom: 8px; }
.empty-state p { color: var(--text-mid); margin-bottom: 24px; }

.projects-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); gap: 20px; }

.project-card {
  cursor: pointer; transition: transform var(--t), box-shadow var(--t);
}
.project-card:hover { transform: translateY(-3px); box-shadow: var(--sh-md); }

.pc-header { display: flex; justify-content: space-between; align-items: flex-start; gap: 12px; margin-bottom: 10px; }
.pc-name { font-family: 'Playfair Display', serif; font-size: 1.05rem; font-weight: 700; color: var(--text-dark); margin-bottom: 4px; }
.pc-meta { font-size: 0.8rem; color: var(--text-light); }
.pc-desc { font-size: 0.875rem; color: var(--text-mid); line-height: 1.6; margin-bottom: 14px; }
.pc-footer { display: flex; justify-content: space-between; align-items: center; margin-top: 14px; padding-top: 14px; border-top: 1px solid var(--border-sub); }
.pc-role { font-size: 0.8rem; font-weight: 600; color: var(--text-light); }
.pc-arrow { color: var(--green-mid); font-weight: 700; transition: transform var(--t); }
.project-card:hover .pc-arrow { transform: translateX(4px); }

.form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
.modal-actions { display: flex; gap: 12px; justify-content: flex-end; margin-top: 8px; }

.spinner { display: inline-block; width: 14px; height: 14px; border: 2px solid rgba(255,255,255,0.4); border-top-color: white; border-radius: 50%; animation: spin 0.7s linear infinite; }

@media (max-width: 560px) { .form-row { grid-template-columns: 1fr; } }
</style>
