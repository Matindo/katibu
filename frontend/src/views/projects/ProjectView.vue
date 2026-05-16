<template>
  <main class="page">
    <div class="page-inner container">

      <!-- Loading -->
      <div v-if="loading" class="loading-row"><span class="spinner-dark"></span> Loading project…</div>

      <!-- Error -->
      <div v-else-if="error" class="alert alert-error mt-16">{{ error }}</div>

      <template v-else-if="project">

        <!-- ── Project header ──────────────────────────────── -->
        <div class="proj-header">
          <div class="proj-header-left">
            <router-link to="/projects" class="back-link">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" width="16" height="16"><path d="M19 12H5M12 5l-7 7 7 7"/></svg>
              All Projects
            </router-link>
            <div class="proj-title-row">
              <h1>{{ project.name }}</h1>
              <span :class="['badge', project.status === 'ACTIVE' ? 'badge-active' : 'badge-archived']">{{ project.status }}</span>
            </div>
            <p class="proj-meta">{{ project.durationType }} &nbsp;·&nbsp; {{ project.startDate }} – {{ project.endDate }}</p>
            <p v-if="project.description" class="proj-desc">{{ project.description }}</p>
          </div>
          <div class="proj-header-right" v-if="project.status === 'ACTIVE'">
            <button class="btn btn-ghost btn-sm" @click="showEdit = true" v-if="isCreator">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" width="15" height="15"><path d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Zm0 0L19.5 7.125"/></svg>
              Edit
            </button>
            <button class="btn btn-danger btn-sm" @click="confirmArchive" v-if="isCreator">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" width="15" height="15"><path d="m20.25 7.5-.625 10.632a2.25 2.25 0 0 1-2.247 2.118H6.622a2.25 2.25 0 0 1-2.247-2.118L3.75 7.5M10 11.25h4M3.375 7.5h17.25c.621 0 1.125-.504 1.125-1.125v-1.5c0-.621-.504-1.125-1.125-1.125H3.375c-.621 0-1.125.504-1.125 1.125v1.5c0 .621.504 1.125 1.125 1.125Z"/></svg>
              Archive
            </button>
          </div>
        </div>

        <!-- ── Stats ────────────────────────────────────────── -->
        <div class="stats-row">
          <div class="stat-card glass">
            <div class="stat-label">Closing Balance</div>
            <div class="stat-val green">{{ fmt(balance) }}</div>
            <div class="stat-sub">All time</div>
          </div>
          <div class="stat-card glass">
            <div class="stat-label">Total Inflows</div>
            <div class="stat-val in">+{{ fmt(totalIn) }}</div>
          </div>
          <div class="stat-card glass">
            <div class="stat-label">Total Outflows</div>
            <div class="stat-val out">−{{ fmt(totalOut) }}</div>
          </div>
          <div class="stat-card glass">
            <div class="stat-label">Entries</div>
            <div class="stat-val">{{ entries.length }}</div>
          </div>
        </div>

        <!-- ── Action buttons ───────────────────────────────── -->
        <div class="actions-row" v-if="project.status === 'ACTIVE'">
          <button class="action-btn primary" @click="showEntry = true">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" width="18" height="18"><path d="M12 4.5v15m7.5-7.5h-15"/></svg>
            Add Transaction
          </button>
          <button class="action-btn" @click="goReports">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" width="18" height="18"><path d="M3 13.125C3 12.504 3.504 12 4.125 12h2.25c.621 0 1.125.504 1.125 1.125v6.75C7.5 20.496 6.996 21 6.375 21h-2.25A1.125 1.125 0 0 1 3 19.875v-6.75ZM9.75 8.625c0-.621.504-1.125 1.125-1.125h2.25c.621 0 1.125.504 1.125 1.125v11.25c0 .621-.504 1.125-1.125 1.125h-2.25a1.125 1.125 0 0 1-1.125-1.125V8.625ZM16.5 4.125c0-.621.504-1.125 1.125-1.125h2.25C20.496 3 21 3.504 21 4.125v15.75c0 .621-.504 1.125-1.125 1.125h-2.25a1.125 1.125 0 0 1-1.125-1.125V4.125Z"/></svg>
            View Reports
          </button>
          <button class="action-btn" @click="showLink = true">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" width="18" height="18"><path d="M13.19 8.688a4.5 4.5 0 0 1 1.242 7.244l-4.5 4.5a4.5 4.5 0 0 1-6.364-6.364l1.757-1.757m13.35-.622 1.757-1.757a4.5 4.5 0 0 0-6.364-6.364l-4.5 4.5a4.5 4.5 0 0 0 1.242 7.244"/></svg>
            Share Link
          </button>
          <button class="action-btn" @click="showMembers = true" v-if="isCreator">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" width="18" height="18"><path d="M15 19.128a9.38 9.38 0 0 0 2.625.372 9.337 9.337 0 0 0 4.121-.952 4.125 4.125 0 0 0-7.533-2.493M15 19.128v-.003c0-1.113-.285-2.16-.786-3.07M15 19.128v.106A12.318 12.318 0 0 1 8.624 21c-2.331 0-4.512-.645-6.374-1.766l-.001-.109a6.375 6.375 0 0 1 11.964-3.07M12 6.375a3.375 3.375 0 1 1-6.75 0 3.375 3.375 0 0 1 6.75 0Zm8.25 2.25a2.625 2.625 0 1 1-5.25 0 2.625 2.625 0 0 1 5.25 0Z"/></svg>
            Manage Members
          </button>
        </div>

        <!-- ── Ledger table ──────────────────────────────────── -->
        <div class="section-block">
          <div class="section-row">
            <h2 class="section-title">Transactions</h2>
            <span class="text-light text-sm">{{ entries.length }} record{{ entries.length !== 1 ? 's' : '' }}</span>
          </div>
          <div v-if="!entries.length" class="empty-inline">No transactions recorded yet.</div>
          <div v-else class="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Date</th><th>Type</th><th>Description</th><th>Reference</th><th>Amount</th>
                  <th v-if="project.status === 'ACTIVE'"></th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="e in entries" :key="e.id">
                  <td>{{ e.transactionDate }}</td>
                  <td><span class="type-tag">{{ e.entryType }}</span></td>
                  <td>{{ e.description }}</td>
                  <td class="text-light">{{ e.reference || '—' }}</td>
                  <td :class="e.inflow ? 'inflow' : 'outflow'">
                    {{ e.inflow ? '+' : '−' }}{{ fmt(e.amount) }}
                  </td>
                  <td v-if="project.status === 'ACTIVE'">
                    <button class="icon-btn del" @click="deleteEntry(e)" title="Delete">
                      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" width="15" height="15"><polyline points="3 6 5 6 21 6"/><path d="M19 6l-1 14H6L5 6"/><path d="M10 11v6M14 11v6"/><path d="M9 6V4h6v2"/></svg>
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

      </template>
    </div>

    <!-- ── Add Transaction Modal ───────────────────────────── -->
    <div v-if="showEntry" class="modal-overlay" @click.self="showEntry = false">
      <div class="modal">
        <div class="modal-header">
          <h3>Add Transaction</h3>
          <button class="modal-close" @click="showEntry = false"><svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" width="20" height="20"><path d="M6 18 18 6M6 6l12 12"/></svg></button>
        </div>
        <div v-if="entryError" class="alert alert-error">{{ entryError }}</div>
        <form @submit.prevent="saveEntry">
          <div class="form-group">
            <label>Entry type *</label>
            <select v-model="entryForm.entryType" class="field" required>
              <option value="">Select…</option>
              <optgroup label="Inflows">
                <option v-for="t in inflows" :key="t" :value="t">{{ t }}</option>
              </optgroup>
              <optgroup label="Outflows">
                <option v-for="t in outflows" :key="t" :value="t">{{ t }}</option>
              </optgroup>
            </select>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>Amount *</label>
              <input v-model.number="entryForm.amount" type="number" step="0.01" min="0.01" class="field" placeholder="0.00" required/>
            </div>
            <div class="form-group">
              <label>Transaction date *</label>
              <input v-model="entryForm.transactionDate" type="date" class="field" required/>
            </div>
          </div>
          <div class="form-group">
            <label>Description *</label>
            <input v-model="entryForm.description" type="text" class="field" placeholder="Brief description" required/>
          </div>
          <div class="form-group">
            <label>Reference</label>
            <input v-model="entryForm.reference" type="text" class="field" placeholder="Invoice / receipt number (optional)"/>
          </div>
          <div class="modal-actions">
            <button type="button" class="btn btn-ghost" @click="showEntry = false">Cancel</button>
            <button type="submit" class="btn btn-primary" :disabled="entryLoading">
              <span v-if="entryLoading" class="spinner"></span>
              {{ entryLoading ? 'Saving…' : 'Save Transaction' }}
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- ── Share Link Modal ────────────────────────────────── -->
    <div v-if="showLink" class="modal-overlay" @click.self="showLink = false">
      <div class="modal">
        <div class="modal-header">
          <h3>Share Project</h3>
          <button class="modal-close" @click="showLink = false"><svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" width="20" height="20"><path d="M6 18 18 6M6 6l12 12"/></svg></button>
        </div>
        <div v-if="linkError" class="alert alert-error">{{ linkError }}</div>

        <!-- Existing links -->
        <div v-if="links.length" class="links-list">
          <div v-for="l in links" :key="l.id" class="link-row">
            <div class="link-url text-sm">{{ l.url }}</div>
            <div class="link-meta text-xs text-light">{{ l.expiresAt ? 'Expires ' + l.expiresAt : 'No expiry' }}</div>
            <div class="link-actions">
              <button class="btn btn-ghost btn-sm" @click="copyLink(l.url)">Copy</button>
              <button v-if="l.active" class="btn btn-danger btn-sm" @click="revokeLink(l.id)">Revoke</button>
              <span v-else class="badge badge-archived">Revoked</span>
            </div>
          </div>
        </div>

        <div class="divider"></div>
        <h4 class="text-sm fw-600 text-mid mb-8">Generate new link</h4>
        <div class="form-group">
          <label>Expiry date (optional)</label>
          <input v-model="linkExpiry" type="datetime-local" class="field"/>
        </div>
        <div class="modal-actions">
          <button type="button" class="btn btn-ghost" @click="showLink = false">Close</button>
          <button class="btn btn-gold" @click="generateLink" :disabled="linkLoading">
            <span v-if="linkLoading" class="spinner"></span>
            Generate Link
          </button>
        </div>
      </div>
    </div>

    <!-- ── Members Modal ───────────────────────────────────── -->
    <div v-if="showMembers" class="modal-overlay" @click.self="showMembers = false">
      <div class="modal">
        <div class="modal-header">
          <h3>Team Members</h3>
          <button class="modal-close" @click="showMembers = false"><svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" width="20" height="20"><path d="M6 18 18 6M6 6l12 12"/></svg></button>
        </div>
        <div v-if="memberError" class="alert alert-error">{{ memberError }}</div>

        <div v-if="members.length" class="members-list">
          <div v-for="m in members" :key="m.userId" class="member-row">
            <div class="member-avatar">{{ initial(m.fullName) }}</div>
            <div>
              <div class="text-sm fw-600">{{ m.fullName }}</div>
              <div class="text-xs text-light">{{ m.email }}</div>
            </div>
            <button class="btn btn-danger btn-sm ml-auto" @click="removeMember(m.userId)">Remove</button>
          </div>
        </div>
        <p v-else class="text-mid text-sm mt-8">No admin members added yet.</p>

        <div class="divider"></div>
        <h4 class="text-sm fw-600 text-mid mb-8">Add member by email</h4>
        <div class="form-group">
          <label>Email address</label>
          <input v-model="memberEmail" type="email" class="field" placeholder="member@example.com"/>
        </div>
        <div class="modal-actions">
          <button type="button" class="btn btn-ghost" @click="showMembers = false">Close</button>
          <button class="btn btn-primary" @click="addMember" :disabled="memberLoading">
            <span v-if="memberLoading" class="spinner"></span>
            Add Member
          </button>
        </div>
      </div>
    </div>

    <!-- ── Edit Project Modal ──────────────────────────────── -->
    <div v-if="showEdit" class="modal-overlay" @click.self="showEdit = false">
      <div class="modal">
        <div class="modal-header">
          <h3>Edit Project</h3>
          <button class="modal-close" @click="showEdit = false"><svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" width="20" height="20"><path d="M6 18 18 6M6 6l12 12"/></svg></button>
        </div>
        <div v-if="editError" class="alert alert-error">{{ editError }}</div>
        <form @submit.prevent="saveEdit">
          <div class="form-group">
            <label>Project name *</label>
            <input v-model="editForm.name" type="text" class="field" required/>
          </div>
          <div class="form-group">
            <label>Description</label>
            <textarea v-model="editForm.description" class="field" rows="3"></textarea>
          </div>
          <div class="modal-actions">
            <button type="button" class="btn btn-ghost" @click="showEdit = false">Cancel</button>
            <button type="submit" class="btn btn-primary" :disabled="editLoading">
              <span v-if="editLoading" class="spinner"></span>
              Save Changes
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

const INFLOWS  = ['INITIAL_CAPITAL','DONATION','REVENUE','GRANT','LOAN_RECEIVED','CREDIT']
const OUTFLOWS = ['PURCHASE','EXPENDITURE','LOAN_REPAYMENT','DEBT_PAYMENT','WITHDRAWAL']

export default {
  name: 'ProjectView',
  data () {
    return {
      project: null, entries: [], members: [], links: [],
      loading: false, error: '',
      showEntry: false, entryLoading: false, entryError: '',
      entryForm: { entryType: '', amount: '', transactionDate: '', description: '', reference: '' },
      showLink: false, linkLoading: false, linkError: '', linkExpiry: '',
      showMembers: false, memberLoading: false, memberError: '', memberEmail: '',
      showEdit: false, editLoading: false, editError: '',
      editForm: { name: '', description: '' },
      inflows: INFLOWS, outflows: OUTFLOWS
    }
  },
  computed: {
    ...mapGetters(['currentUser']),
    isCreator () { return this.project && this.project.creatorId === this.currentUser?.id },
    totalIn ()  { return this.entries.filter(e => e.inflow).reduce((s, e) => s + e.amount, 0) },
    totalOut () { return this.entries.filter(e => !e.inflow).reduce((s, e) => s + e.amount, 0) },
    balance ()  { return this.totalIn - this.totalOut }
  },
  created () { this.load() },
  methods: {
    async load () {
      this.loading = true; this.error = ''
      const id = this.$route.params.id
      try {
        const [proj, ents] = await Promise.all([api.projects.get(id), api.ledger.list(id)])
        this.project = proj
        this.entries = ents
        this.editForm = { name: proj.name, description: proj.description || '' }
        this.$store.dispatch('setCurrentProject', proj)
        if (this.isCreator) {
          const [mems, lks] = await Promise.all([api.projects.listMembers(id), api.links.list(id)])
          this.members = mems; this.links = lks
        }
      } catch (e) { this.error = e?.message || 'Failed to load project.' }
      finally { this.loading = false }
    },
    fmt (n) { return Number(n || 0).toLocaleString('en-KE', { minimumFractionDigits: 2 }) },
    initial (name) { return (name || '?')[0].toUpperCase() },
    goReports () { this.$router.push(`/projects/${this.project.id}/reports`) },

    async saveEntry () {
      this.entryError = ''; this.entryLoading = true
      try {
        await api.ledger.create(this.project.id, this.entryForm)
        this.showEntry = false
        this.entryForm = { entryType: '', amount: '', transactionDate: '', description: '', reference: '' }
        this.entries = await api.ledger.list(this.project.id)
        this.$toast.success('Transaction recorded')
      } catch (e) { this.entryError = e?.message || 'Failed to save transaction.' }
      finally { this.entryLoading = false }
    },
    async deleteEntry (entry) {
      if (!window.confirm(`Delete this ${entry.entryType} entry?`)) return
      try {
        await api.ledger.delete(this.project.id, entry.id)
        this.entries = this.entries.filter(x => x.id !== entry.id)
        this.$toast.success('Entry deleted')
      } catch (err) { this.$toast.error(err?.message || 'Failed to delete entry.') }
    },

    async generateLink () {
      this.linkError = ''; this.linkLoading = true
      try {
        const link = await api.links.generate(this.project.id, { expiresAt: this.linkExpiry || null })
        this.links.unshift(link)
        this.linkExpiry = ''
        this.$toast.success('Public link generated')
      } catch (e) { this.linkError = e?.message || 'Failed to generate link.' }
      finally { this.linkLoading = false }
    },
    async revokeLink (linkId) {
      try {
        await api.links.revoke(this.project.id, linkId)
        const l = this.links.find(x => x.id === linkId)
        if (l) l.active = false
        this.$toast.success('Link revoked')
      } catch (e) { this.$toast.error(e?.message || 'Failed to revoke link.') }
    },
    copyLink (url) {
      navigator.clipboard.writeText(url).then(() => this.$toast.info('Link copied to clipboard'))
    },

    async addMember () {
      this.memberError = ''; this.memberLoading = true
      try {
        const m = await api.projects.addMember(this.project.id, { email: this.memberEmail })
        this.members.push(m)
        this.memberEmail = ''
        this.$toast.success('Member added successfully')
      } catch (e) { this.memberError = e?.message || 'Failed to add member.' }
      finally { this.memberLoading = false }
    },
    async removeMember (userId) {
      if (!window.confirm('Remove this member from the project?')) return
      try {
        await api.projects.removeMember(this.project.id, userId)
        this.members = this.members.filter(m => m.userId !== userId)
        this.$toast.success('Member removed')
      } catch (e) { this.$toast.error(e?.message || 'Failed to remove member.') }
    },

    async saveEdit () {
      this.editError = ''; this.editLoading = true
      try {
        const updated = await api.projects.update(this.project.id, this.editForm)
        this.project = updated
        this.showEdit = false
        this.$toast.success('Project updated')
      } catch (e) { this.editError = e?.message || 'Failed to update project.' }
      finally { this.editLoading = false }
    },
    async confirmArchive () {
      if (!window.confirm(`Archive "${this.project.name}"? The project will become read-only.`)) return
      try {
        const updated = await api.projects.archive(this.project.id)
        this.project = updated
        this.$toast.success('Project archived')
      } catch (e) { this.$toast.error(e?.message || 'Failed to archive project.') }
    }
  }
}
</script>

<style scoped>
.loading-row { display: flex; align-items: center; gap: 10px; color: var(--text-mid); padding: 40px 0; }
.spinner-dark { display: inline-block; width: 18px; height: 18px; border: 2px solid var(--border-sub); border-top-color: var(--green-mid); border-radius: 50%; animation: spin 0.7s linear infinite; }
@keyframes spin { to { transform: rotate(360deg); } }

/* Header */
.proj-header { display: flex; justify-content: space-between; align-items: flex-start; gap: 24px; margin-bottom: 32px; flex-wrap: wrap; }
.proj-header-right { display: flex; gap: 10px; flex-shrink: 0; }
.back-link { display: inline-flex; align-items: center; gap: 6px; font-size: 0.875rem; color: var(--text-light); margin-bottom: 12px; transition: color var(--t); }
.back-link:hover { color: var(--green-deep); }
.proj-title-row { display: flex; align-items: center; gap: 12px; margin-bottom: 6px; flex-wrap: wrap; }
.proj-title-row h1 { font-size: clamp(1.5rem, 3vw, 2.2rem); }
.proj-meta { font-size: 0.875rem; color: var(--text-light); margin-bottom: 6px; }
.proj-desc { font-size: 0.9375rem; color: var(--text-mid); }

/* Stats */
.stats-row { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; margin-bottom: 28px; }
.stat-card { border-radius: var(--r-lg); padding: 20px 18px; box-shadow: var(--sh-sm); }
.stat-label { font-size: 0.78rem; font-weight: 600; text-transform: uppercase; letter-spacing: 0.05em; color: var(--text-light); margin-bottom: 8px; }
.stat-val { font-family: 'Playfair Display', serif; font-size: 1.5rem; font-weight: 700; color: var(--text-dark); }
.stat-val.green { color: var(--green-deep); }
.stat-val.in    { color: var(--green-deep); }
.stat-val.out   { color: #C0392B; }
.stat-sub { font-size: 0.75rem; color: var(--text-light); margin-top: 4px; }

/* Actions */
.actions-row { display: flex; gap: 10px; flex-wrap: wrap; margin-bottom: 32px; }
.action-btn {
  display: inline-flex; align-items: center; gap: 8px;
  padding: 10px 18px; border-radius: var(--r-md); font-size: 0.9rem; font-weight: 600;
  border: 1.5px solid var(--border-sub); background: var(--white); color: var(--text-mid);
  cursor: pointer; transition: all var(--t);
}
.action-btn:hover { border-color: var(--green-mid); color: var(--green-deep); background: var(--green-tint); }
.action-btn.primary { background: var(--green-deep); color: white; border-color: var(--green-deep); }
.action-btn.primary:hover { background: var(--green-mid); border-color: var(--green-mid); }

/* Section */
.section-block { margin-top: 8px; }
.section-row { display: flex; align-items: center; justify-content: space-between; margin-bottom: 16px; }
.section-title { font-size: 1.2rem; }
.type-tag { font-size: 0.68rem; font-weight: 700; padding: 2px 7px; border-radius: 4px; background: var(--green-pale); color: var(--green-deep); white-space: nowrap; }
.empty-inline { padding: 40px 0; text-align: center; color: var(--text-light); }
.icon-btn { background: none; border: none; cursor: pointer; color: var(--text-light); padding: 4px; border-radius: 4px; display: flex; transition: color var(--t), background var(--t); }
.icon-btn.del:hover { color: #C0392B; background: #FEE8E8; }

/* Links */
.links-list { display: flex; flex-direction: column; gap: 12px; margin-bottom: 8px; }
.link-row { background: var(--off-white); border-radius: var(--r-sm); padding: 12px; border: 1px solid var(--border-sub); }
.link-url { word-break: break-all; font-family: 'Inter', monospace; color: var(--green-deep); margin-bottom: 4px; }
.link-meta { margin-bottom: 8px; }
.link-actions { display: flex; gap: 8px; align-items: center; }

/* Members */
.members-list { display: flex; flex-direction: column; gap: 10px; }
.member-row { display: flex; align-items: center; gap: 12px; padding: 10px; background: var(--off-white); border-radius: var(--r-sm); border: 1px solid var(--border-sub); }
.member-avatar { width: 36px; height: 36px; border-radius: 50%; background: var(--green-pale); color: var(--green-deep); display: flex; align-items: center; justify-content: center; font-weight: 700; font-size: 0.9rem; flex-shrink: 0; }
.ml-auto { margin-left: auto; }

/* Misc */
.form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
.modal-actions { display: flex; gap: 12px; justify-content: flex-end; margin-top: 8px; }
.spinner { display: inline-block; width: 14px; height: 14px; border: 2px solid rgba(255,255,255,0.4); border-top-color: white; border-radius: 50%; animation: spin 0.7s linear infinite; }

@media (max-width: 900px) { .stats-row { grid-template-columns: 1fr 1fr; } }
@media (max-width: 560px) {
  .stats-row { grid-template-columns: 1fr 1fr; }
  .form-row { grid-template-columns: 1fr; }
}
</style>
