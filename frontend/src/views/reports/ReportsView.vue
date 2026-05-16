<template>
  <main class="page">
    <div class="page-inner container">

      <div class="back-row">
        <router-link :to="`/projects/${projectId}`" class="back-link">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" width="16" height="16"><path d="M19 12H5M12 5l-7 7 7 7"/></svg>
          Back to Project
        </router-link>
      </div>

      <div class="page-header">
        <h1>Financial Reports</h1>
        <p class="text-mid">{{ projectName }}</p>
      </div>

      <!-- Controls -->
      <div class="card controls-card">
        <div class="controls-grid">
          <div class="form-group" style="margin-bottom:0">
            <label>Report type</label>
            <select v-model="reportType" class="field">
              <option value="summary">Summary</option>
              <option value="receipts-payments">Receipts &amp; Payments (IPSAS 2)</option>
              <option value="cash-flow">Cash Flow Statement (IFRS)</option>
              <option value="financial-position">Financial Position</option>
            </select>
          </div>
          <div class="form-group" style="margin-bottom:0">
            <label>{{ reportType === 'financial-position' ? 'As at date' : 'Start date' }}</label>
            <input v-model="startDate" type="date" class="field"/>
          </div>
          <div v-if="reportType !== 'financial-position'" class="form-group" style="margin-bottom:0">
            <label>End date</label>
            <input v-model="endDate" type="date" class="field"/>
          </div>
          <div class="controls-btns">
            <button class="btn btn-primary" @click="run" :disabled="loading">
              <span v-if="loading" class="spinner"></span>
              {{ loading ? 'Generating…' : 'Generate' }}
            </button>
            <button class="btn btn-ghost" @click="exportFile('PDF')" :disabled="exporting">Export PDF</button>
            <button class="btn btn-ghost" @click="exportFile('CSV')" :disabled="exporting">Export CSV</button>
          </div>
        </div>
      </div>

      <div v-if="error" class="alert alert-error mt-16">{{ error }}</div>

      <!-- Report output -->
      <div v-if="report" class="card report-card mt-24">

        <!-- Summary -->
        <template v-if="reportType === 'summary'">
          <div class="report-title">Summary Report</div>
          <div class="report-period">{{ startDate }} – {{ endDate }}</div>
          <div class="summary-grid">
            <div class="sum-item"><div class="sum-label">Opening Balance</div><div class="sum-val">{{ fmt(report.openingBalance) }}</div></div>
            <div class="sum-item"><div class="sum-label">Total Receipts</div><div class="sum-val in">+{{ fmt(report.totalReceipts) }}</div></div>
            <div class="sum-item"><div class="sum-label">Total Payments</div><div class="sum-val out">−{{ fmt(report.totalPayments) }}</div></div>
            <div class="sum-item highlight"><div class="sum-label">Closing Balance</div><div class="sum-val big">{{ fmt(report.closingBalance) }}</div></div>
          </div>
        </template>

        <!-- Receipts & Payments -->
        <template v-else-if="reportType === 'receipts-payments'">
          <div class="report-title">Statement of Receipts &amp; Payments</div>
          <div class="report-period">{{ startDate }} – {{ endDate }}</div>
          <div class="report-meta">Cash Basis — IPSAS 2</div>
          <div class="rp-section">
            <div class="rp-hd in">Receipts</div>
            <div v-for="r in report.receipts" :key="r.entryType" class="rp-line">
              <span>{{ r.entryType }}</span><span class="in">{{ fmt(r.amount) }}</span>
            </div>
            <div class="rp-line total"><span>Total Receipts</span><span class="in">{{ fmt(report.totalReceipts) }}</span></div>
          </div>
          <div class="rp-section">
            <div class="rp-hd out">Payments</div>
            <div v-for="p in report.payments" :key="p.entryType" class="rp-line">
              <span>{{ p.entryType }}</span><span class="out">{{ fmt(p.amount) }}</span>
            </div>
            <div class="rp-line total"><span>Total Payments</span><span class="out">{{ fmt(report.totalPayments) }}</span></div>
          </div>
          <div class="rp-balance">
            <div class="rp-bal-row"><span>Opening Balance</span><span>{{ fmt(report.openingBalance) }}</span></div>
            <div class="rp-bal-row highlight"><span>Closing Balance</span><span class="in big">{{ fmt(report.closingBalance) }}</span></div>
          </div>
        </template>

        <!-- Cash Flow -->
        <template v-else-if="reportType === 'cash-flow'">
          <div class="report-title">Cash Flow Statement</div>
          <div class="report-period">{{ startDate }} – {{ endDate }}</div>
          <div class="report-meta">IFRS Classification</div>
          <div v-for="cat in ['operatingActivities','investingActivities','financingActivities']" :key="cat" class="rp-section">
            <div class="rp-hd">{{ catLabel(cat) }}</div>
            <div v-for="e in report[cat]" :key="e.entryType" class="rp-line">
              <span>{{ e.entryType }}</span>
              <span :class="e.amount >= 0 ? 'in' : 'out'">{{ e.amount >= 0 ? '+' : '−' }}{{ fmt(Math.abs(e.amount)) }}</span>
            </div>
            <div class="rp-line total"><span>Net {{ catLabel(cat) }}</span><span :class="report['net'+cap(cat)] >= 0 ? 'in' : 'out'">{{ fmt(report['net'+cap(cat)]) }}</span></div>
          </div>
          <div class="rp-balance">
            <div class="rp-bal-row highlight"><span>Net Cash Flow</span><span :class="report.netCashFlow >= 0 ? 'in big' : 'out big'">{{ fmt(report.netCashFlow) }}</span></div>
          </div>
        </template>

        <!-- Financial Position -->
        <template v-else-if="reportType === 'financial-position'">
          <div class="report-title">Statement of Financial Position</div>
          <div class="report-period">As at {{ startDate }}</div>
          <div class="rp-section">
            <div class="rp-hd in">Assets</div>
            <div class="rp-line total"><span>Cash and Cash Equivalents</span><span class="in">{{ fmt(report.totalAssets) }}</span></div>
          </div>
          <div class="rp-section">
            <div class="rp-hd out">Liabilities</div>
            <div v-for="l in report.liabilities" :key="l.entryType" class="rp-line">
              <span>{{ l.entryType }}</span><span class="out">{{ fmt(l.amount) }}</span>
            </div>
            <div class="rp-line total"><span>Total Liabilities</span><span class="out">{{ fmt(report.totalLiabilities) }}</span></div>
          </div>
          <div class="rp-balance">
            <div class="rp-bal-row highlight"><span>Net Assets</span><span class="in big">{{ fmt(report.netAssets) }}</span></div>
          </div>
        </template>

      </div>

    </div>
  </main>
</template>

<script>
import { mapGetters } from 'vuex'
import api from '../../api'

export default {
  name: 'ReportsView',
  data () {
    const today = new Date().toISOString().slice(0, 10)
    return {
      reportType: 'receipts-payments',
      startDate: today.slice(0, 8) + '01',
      endDate: today,
      report: null, loading: false, error: '',
      exporting: false
    }
  },
  computed: {
    ...mapGetters(['currentProject']),
    projectId () { return this.$route.params.id },
    projectName () { return this.currentProject?.name || '' }
  },
  methods: {
    async run () {
      this.error = ''; this.loading = true; this.report = null
      try {
        const id = this.projectId
        const params = { startDate: this.startDate, endDate: this.endDate }
        const asAt = { asAt: this.startDate }
        const map = {
          'summary': () => api.reports.summary(id, params),
          'receipts-payments': () => api.reports.receiptsPayments(id, params),
          'cash-flow': () => api.reports.cashFlow(id, params),
          'financial-position': () => api.reports.financialPosition(id, asAt)
        }
        this.report = await map[this.reportType]()
      } catch (e) { this.error = e?.message || 'Failed to generate report.' }
      finally { this.loading = false }
    },
    async exportFile (fileType) {
      this.exporting = true
      try {
        const typeMap = { 'summary': 'SUMMARY', 'receipts-payments': 'RECEIPTS_PAYMENTS', 'cash-flow': 'CASH_FLOW', 'financial-position': 'FINANCIAL_POSITION' }
        await api.files.export(this.projectId, { fileType, reportType: typeMap[this.reportType], startDate: this.startDate, endDate: this.endDate })
        this.$toast.success(`${fileType} export queued — check the project files list`)
      } catch (e) { this.$toast.error(e?.message || 'Export failed.') }
      finally { this.exporting = false }
    },
    fmt (n) { return Number(n || 0).toLocaleString('en-KE', { minimumFractionDigits: 2 }) },
    catLabel (k) { return { operatingActivities: 'Operating Activities', investingActivities: 'Investing Activities', financingActivities: 'Financing Activities' }[k] },
    cap (s) { return s.charAt(0).toUpperCase() + s.slice(1) }
  }
}
</script>

<style scoped>
.back-row { margin-bottom: 16px; }
.back-link { display: inline-flex; align-items: center; gap: 6px; font-size: 0.875rem; color: var(--text-light); transition: color var(--t); }
.back-link:hover { color: var(--green-deep); }

.controls-card { margin-bottom: 8px; }
.controls-grid { display: grid; grid-template-columns: 2fr 1fr 1fr auto; gap: 16px; align-items: end; }
.controls-btns { display: flex; gap: 8px; flex-wrap: wrap; align-items: center; }

.report-card {}
.report-title { font-family: 'Playfair Display', serif; font-size: 1.4rem; font-weight: 700; margin-bottom: 4px; }
.report-period { font-size: 0.9rem; color: var(--text-mid); margin-bottom: 4px; }
.report-meta { font-size: 0.8rem; color: var(--text-light); margin-bottom: 24px; }

/* Summary */
.summary-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; margin-top: 20px; }
.sum-item { background: var(--off-white); border-radius: var(--r-md); padding: 18px; border: 1px solid var(--border-sub); }
.sum-item.highlight { background: var(--green-tint); border-color: rgba(116,198,157,0.4); }
.sum-label { font-size: 0.78rem; font-weight: 600; text-transform: uppercase; letter-spacing: 0.05em; color: var(--text-light); margin-bottom: 8px; }
.sum-val { font-family: 'Playfair Display', serif; font-size: 1.3rem; font-weight: 700; color: var(--text-dark); }
.sum-val.in  { color: var(--green-deep); }
.sum-val.out { color: #C0392B; }
.sum-val.big { font-size: 1.5rem; }

/* R&P / CF / FP */
.rp-section { margin-bottom: 20px; }
.rp-hd { font-size: 0.8rem; font-weight: 700; text-transform: uppercase; letter-spacing: 0.06em; padding-bottom: 8px; border-bottom: 2px solid var(--border-sub); margin-bottom: 8px; color: var(--text-mid); }
.rp-hd.in  { color: var(--green-deep); border-color: var(--green-light); }
.rp-hd.out { color: #C0392B; border-color: rgba(192,57,43,0.2); }
.rp-line { display: flex; justify-content: space-between; padding: 7px 0; font-size: 0.9rem; border-bottom: 1px solid rgba(0,0,0,0.04); color: var(--text-mid); }
.rp-line.total { border-top: 1px solid var(--border-sub); font-weight: 700; color: var(--text-dark); border-bottom: none; margin-top: 4px; }
.rp-line .in  { color: var(--green-deep); font-weight: 600; }
.rp-line .out { color: #C0392B; font-weight: 600; }

.rp-balance { background: var(--green-tint); border-radius: var(--r-md); padding: 16px 20px; margin-top: 16px; }
.rp-bal-row { display: flex; justify-content: space-between; font-size: 0.9rem; padding: 4px 0; color: var(--text-mid); }
.rp-bal-row.highlight { font-weight: 700; color: var(--text-dark); font-size: 1rem; }
.rp-bal-row .in  { color: var(--green-deep); }
.rp-bal-row .out { color: #C0392B; }
.rp-bal-row .big { font-family: 'Playfair Display', serif; font-size: 1.3rem; }

.spinner { display: inline-block; width: 14px; height: 14px; border: 2px solid rgba(255,255,255,0.4); border-top-color: white; border-radius: 50%; animation: spin 0.7s linear infinite; }
@keyframes spin { to { transform: rotate(360deg); } }

@media (max-width: 900px) { .controls-grid { grid-template-columns: 1fr 1fr; } .summary-grid { grid-template-columns: 1fr 1fr; } }
@media (max-width: 560px)  { .controls-grid { grid-template-columns: 1fr; } .summary-grid { grid-template-columns: 1fr 1fr; } }
</style>
