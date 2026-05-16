<template>
  <main class="page">
    <div class="page-inner container">

      <div v-if="loadError" class="alert alert-error">{{ loadError }}</div>

      <template v-else>
        <div class="pub-header glass">
          <div class="pub-logo">
            <svg width="28" height="28" viewBox="0 0 36 36" fill="none">
              <circle cx="18" cy="18" r="18" fill="#2D6A4F"/>
              <path d="M18 9C18 9 26 11.5 27 18C28 24.5 18 27 18 27C18 27 8 24.5 9 18C10 11.5 18 9 18 9Z" fill="#74C69D" opacity="0.7"/>
              <line x1="18" y1="14" x2="18" y2="27" stroke="white" stroke-width="1.8" stroke-linecap="round"/>
              <line x1="18" y1="19" x2="13" y2="15.5" stroke="white" stroke-width="1.8" stroke-linecap="round"/>
              <line x1="18" y1="23" x2="23" y2="19.5" stroke="white" stroke-width="1.8" stroke-linecap="round"/>
            </svg>
            <span class="pub-logo-text">Katibu</span>
          </div>
          <div class="pub-badge badge badge-gold">Public Report — Read Only</div>
        </div>

        <!-- Controls -->
        <div class="card controls-card">
          <div class="controls-grid">
            <div class="form-group" style="margin-bottom:0">
              <label>Report type</label>
              <select v-model="reportType" class="field">
                <option value="summary">Summary</option>
                <option value="receipts-payments">Receipts &amp; Payments</option>
                <option value="cash-flow">Cash Flow Statement</option>
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
            <button class="btn btn-primary" @click="run" :disabled="loading">
              <span v-if="loading" class="spinner"></span>
              {{ loading ? 'Loading…' : 'View Report' }}
            </button>
          </div>
        </div>

        <div v-if="error" class="alert alert-error mt-16">{{ error }}</div>

        <div v-if="report" class="card report-card mt-24">
          <div class="report-title">{{ reportLabel }}</div>
          <div class="report-period">{{ reportType === 'financial-position' ? 'As at ' + startDate : startDate + ' – ' + endDate }}</div>

          <!-- Summary -->
          <template v-if="reportType === 'summary'">
            <div class="summary-grid">
              <div class="sum-item"><div class="sum-label">Opening Balance</div><div class="sum-val">{{ fmt(report.openingBalance) }}</div></div>
              <div class="sum-item"><div class="sum-label">Total Receipts</div><div class="sum-val in">+{{ fmt(report.totalReceipts) }}</div></div>
              <div class="sum-item"><div class="sum-label">Total Payments</div><div class="sum-val out">−{{ fmt(report.totalPayments) }}</div></div>
              <div class="sum-item highlight"><div class="sum-label">Closing Balance</div><div class="sum-val big">{{ fmt(report.closingBalance) }}</div></div>
            </div>
          </template>

          <!-- R&P -->
          <template v-else-if="reportType === 'receipts-payments'">
            <div class="rp-section">
              <div class="rp-hd in">Receipts</div>
              <div v-for="r in report.receipts" :key="r.entryType" class="rp-line"><span>{{ r.entryType }}</span><span class="in">{{ fmt(r.amount) }}</span></div>
              <div class="rp-line total"><span>Total Receipts</span><span class="in">{{ fmt(report.totalReceipts) }}</span></div>
            </div>
            <div class="rp-section">
              <div class="rp-hd out">Payments</div>
              <div v-for="p in report.payments" :key="p.entryType" class="rp-line"><span>{{ p.entryType }}</span><span class="out">{{ fmt(p.amount) }}</span></div>
              <div class="rp-line total"><span>Total Payments</span><span class="out">{{ fmt(report.totalPayments) }}</span></div>
            </div>
            <div class="rp-balance">
              <div class="rp-bal-row"><span>Opening Balance</span><span>{{ fmt(report.openingBalance) }}</span></div>
              <div class="rp-bal-row highlight"><span>Closing Balance</span><span class="in big">{{ fmt(report.closingBalance) }}</span></div>
            </div>
          </template>

          <!-- Cash Flow -->
          <template v-else-if="reportType === 'cash-flow'">
            <div v-for="cat in ['operatingActivities','investingActivities','financingActivities']" :key="cat" class="rp-section">
              <div class="rp-hd">{{ { operatingActivities:'Operating Activities', investingActivities:'Investing Activities', financingActivities:'Financing Activities' }[cat] }}</div>
              <div v-for="e in report[cat]" :key="e.entryType" class="rp-line"><span>{{ e.entryType }}</span><span :class="e.amount>=0?'in':'out'">{{ e.amount>=0?'+':'−' }}{{ fmt(Math.abs(e.amount)) }}</span></div>
            </div>
            <div class="rp-balance"><div class="rp-bal-row highlight"><span>Net Cash Flow</span><span :class="report.netCashFlow>=0?'in big':'out big'">{{ fmt(report.netCashFlow) }}</span></div></div>
          </template>

          <!-- Financial Position -->
          <template v-else-if="reportType === 'financial-position'">
            <div class="rp-section">
              <div class="rp-hd in">Assets</div>
              <div class="rp-line total"><span>Cash and Cash Equivalents</span><span class="in">{{ fmt(report.totalAssets) }}</span></div>
            </div>
            <div class="rp-section">
              <div class="rp-hd out">Liabilities</div>
              <div v-for="l in report.liabilities" :key="l.entryType" class="rp-line"><span>{{ l.entryType }}</span><span class="out">{{ fmt(l.amount) }}</span></div>
              <div class="rp-line total"><span>Total Liabilities</span><span class="out">{{ fmt(report.totalLiabilities) }}</span></div>
            </div>
            <div class="rp-balance"><div class="rp-bal-row highlight"><span>Net Assets</span><span class="in big">{{ fmt(report.netAssets) }}</span></div></div>
          </template>
        </div>

        <p class="pub-footer">
          This is a read-only public report generated by
          <a href="/" class="text-green fw-600">Katibu</a> — a product of Bysonic Inc.
        </p>
      </template>
    </div>
  </main>
</template>

<script>
import api from '../../api'

export default {
  name: 'PublicReportView',
  data () {
    const today = new Date().toISOString().slice(0, 10)
    return {
      reportType: 'receipts-payments',
      startDate: today.slice(0, 8) + '01',
      endDate: today,
      report: null, loading: false, error: '', loadError: ''
    }
  },
  computed: {
    token () { return this.$route.params.token },
    reportLabel () {
      return { summary: 'Summary Report', 'receipts-payments': 'Statement of Receipts & Payments', 'cash-flow': 'Cash Flow Statement', 'financial-position': 'Statement of Financial Position' }[this.reportType]
    }
  },
  methods: {
    async run () {
      this.error = ''; this.loading = true; this.report = null
      try {
        const t = this.token
        const p = { startDate: this.startDate, endDate: this.endDate }
        const a = { asAt: this.startDate }
        const map = { summary: () => api.public.summary(t, p), 'receipts-payments': () => api.public.receiptsPayments(t, p), 'cash-flow': () => api.public.cashFlow(t, p), 'financial-position': () => api.public.financialPosition(t, a) }
        this.report = await map[this.reportType]()
      } catch (e) { this.error = e?.message || 'Failed to load report. The link may have expired or been revoked.' }
      finally { this.loading = false }
    },
    fmt (n) { return Number(n || 0).toLocaleString('en-KE', { minimumFractionDigits: 2 }) }
  }
}
</script>

<style scoped>
.pub-header { display: flex; align-items: center; justify-content: space-between; border-radius: var(--r-md); padding: 14px 20px; margin-bottom: 24px; box-shadow: var(--sh-sm); }
.pub-logo { display: flex; align-items: center; gap: 8px; }
.pub-logo-text { font-family: 'Playfair Display', serif; font-size: 1.2rem; font-weight: 700; color: var(--green-deep); }

.controls-card { margin-bottom: 8px; }
.controls-grid { display: grid; grid-template-columns: 2fr 1fr 1fr auto; gap: 16px; align-items: end; }

.report-card {}
.report-title { font-family: 'Playfair Display', serif; font-size: 1.4rem; font-weight: 700; margin-bottom: 4px; }
.report-period { font-size: 0.9rem; color: var(--text-mid); margin-bottom: 20px; }

.summary-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; margin-top: 12px; }
.sum-item { background: var(--off-white); border-radius: var(--r-md); padding: 16px; border: 1px solid var(--border-sub); }
.sum-item.highlight { background: var(--green-tint); border-color: rgba(116,198,157,0.4); }
.sum-label { font-size: 0.75rem; font-weight: 600; text-transform: uppercase; letter-spacing: 0.05em; color: var(--text-light); margin-bottom: 8px; }
.sum-val { font-family: 'Playfair Display', serif; font-size: 1.2rem; font-weight: 700; }
.sum-val.in  { color: var(--green-deep); }
.sum-val.out { color: #C0392B; }
.sum-val.big { font-size: 1.4rem; }

.rp-section { margin-bottom: 20px; }
.rp-hd { font-size: 0.8rem; font-weight: 700; text-transform: uppercase; letter-spacing: 0.06em; padding-bottom: 8px; border-bottom: 2px solid var(--border-sub); margin-bottom: 8px; color: var(--text-mid); }
.rp-hd.in  { color: var(--green-deep); border-color: var(--green-light); }
.rp-hd.out { color: #C0392B; border-color: rgba(192,57,43,0.2); }
.rp-line { display: flex; justify-content: space-between; padding: 7px 0; font-size: 0.9rem; border-bottom: 1px solid rgba(0,0,0,0.04); color: var(--text-mid); }
.rp-line.total { border-top: 1px solid var(--border-sub); font-weight: 700; color: var(--text-dark); border-bottom: none; margin-top: 4px; }
.rp-line .in  { color: var(--green-deep); font-weight: 600; }
.rp-line .out { color: #C0392B; font-weight: 600; }
.rp-balance { background: var(--green-tint); border-radius: var(--r-md); padding: 14px 20px; margin-top: 16px; }
.rp-bal-row { display: flex; justify-content: space-between; font-size: 0.9rem; padding: 4px 0; color: var(--text-mid); }
.rp-bal-row.highlight { font-weight: 700; color: var(--text-dark); }
.rp-bal-row .in  { color: var(--green-deep); }
.rp-bal-row .out { color: #C0392B; }
.rp-bal-row .big { font-family: 'Playfair Display', serif; font-size: 1.3rem; }

.pub-footer { text-align: center; margin-top: 40px; font-size: 0.875rem; color: var(--text-light); }
.spinner { display: inline-block; width: 14px; height: 14px; border: 2px solid rgba(255,255,255,0.4); border-top-color: white; border-radius: 50%; animation: spin 0.7s linear infinite; }
@keyframes spin { to { transform: rotate(360deg); } }

@media (max-width: 900px) { .controls-grid { grid-template-columns: 1fr 1fr; } .summary-grid { grid-template-columns: 1fr 1fr; } }
@media (max-width: 560px)  { .controls-grid { grid-template-columns: 1fr; } .summary-grid { grid-template-columns: 1fr 1fr; } }
</style>
