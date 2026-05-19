<template>
  <main class="page">
    <div class="page-inner">

      <!-- Header -->
      <section class="about-hero container">
        <span class="section-eyebrow">About Katibu</span>
        <h1>Transparent finances for every team</h1>
        <p class="about-lead">
          Katibu was built for community groups, NGOs, SMEs and accountable teams who need professional
          financial records without enterprise complexity. Every feature follows recognised cash-based
          accounting standards.
        </p>
      </section>

      <!-- ── Feature: Projects ─────────────────────────────────────── -->
      <section class="feature-section container">
        <div class="feature-row">
          <div class="feature-shot">
            <!-- Projects list mockup -->
            <div class="shot-window glass">
              <div class="shot-bar">
                <span class="dot red"></span><span class="dot yellow"></span><span class="dot green"></span>
                <span class="shot-url">katibu.my/projects</span>
              </div>
              <div class="shot-body">
                <div class="shot-page-header">
                  <span class="shot-page-title">My Projects</span>
                  <span class="shot-btn">+ New Project</span>
                </div>
                <div class="shot-project-card" v-for="p in mockProjects" :key="p.name">
                  <div>
                    <div class="shot-proj-name">{{ p.name }}</div>
                    <div class="shot-proj-meta">{{ p.meta }}</div>
                  </div>
                  <div class="shot-proj-right">
                    <span :class="['badge', p.badge]">{{ p.status }}</span>
                    <div class="shot-proj-bal">{{ p.balance }}</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="feature-text">
            <span class="section-eyebrow">Project Ledgers</span>
            <h2>One project, one ledger</h2>
            <p>
              Each project is an independent financial ledger. Set a name, description,
              and duration (weekly, monthly, quarterly, half-yearly, yearly or a custom date range).
              Katibu calculates the end date for fixed durations automatically.
            </p>
            <ul class="feature-list">
              <li>Creator has full control including archiving</li>
              <li>Invite admin members to co-record transactions</li>
              <li>Archived projects remain readable — no data is deleted</li>
              <li>See all your projects at a glance from one dashboard</li>
            </ul>
          </div>
        </div>
      </section>

      <!-- ── Feature: Ledger entries ──────────────────────────────── -->
      <section class="feature-section container alt">
        <div class="feature-row reverse">
          <div class="feature-text">
            <span class="section-eyebrow">Transaction Recording</span>
            <h2>Every inflow and outflow, accounted for</h2>
            <p>
              Record eleven distinct entry types spanning the full lifecycle of project funds.
              Katibu automatically classifies each entry for IPSAS cash-basis reporting and
              IFRS cash flow categorisation.
            </p>
            <div class="entry-types">
              <div class="et-group">
                <div class="et-label inflow">Inflows</div>
                <div class="et-tags">
                  <span v-for="t in inflows" :key="t" class="et-tag in">{{ t }}</span>
                </div>
              </div>
              <div class="et-group">
                <div class="et-label outflow">Outflows</div>
                <div class="et-tags">
                  <span v-for="t in outflows" :key="t" class="et-tag out">{{ t }}</span>
                </div>
              </div>
            </div>
          </div>
          <div class="feature-shot">
            <!-- Entry form mockup -->
            <div class="shot-window glass">
              <div class="shot-bar">
                <span class="dot red"></span><span class="dot yellow"></span><span class="dot green"></span>
                <span class="shot-url">Add Transaction</span>
              </div>
              <div class="shot-body form-shot">
                <div class="shot-form-row">
                  <div class="shot-field-label">Entry Type</div>
                  <div class="shot-field-val select">REVENUE ▾</div>
                </div>
                <div class="shot-form-row">
                  <div class="shot-field-label">Amount (KES)</div>
                  <div class="shot-field-val">45,000.00</div>
                </div>
                <div class="shot-form-row">
                  <div class="shot-field-label">Description</div>
                  <div class="shot-field-val">Client payment – Invoice #042</div>
                </div>
                <div class="shot-form-row">
                  <div class="shot-field-label">Reference No.</div>
                  <div class="shot-field-val muted">INV-042</div>
                </div>
                <div class="shot-form-row">
                  <div class="shot-field-label">Transaction Date</div>
                  <div class="shot-field-val">2024-03-15</div>
                </div>
                <div class="shot-form-actions">
                  <span class="shot-cancel">Cancel</span>
                  <span class="shot-submit">Save Entry</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- ── Feature: Reports ─────────────────────────────────────── -->
      <section class="feature-section container">
        <div class="feature-row">
          <div class="feature-shot">
            <!-- Reports mockup -->
            <div class="shot-window glass">
              <div class="shot-bar">
                <span class="dot red"></span><span class="dot yellow"></span><span class="dot green"></span>
                <span class="shot-url">Financial Reports</span>
              </div>
              <div class="shot-body">
                <div class="shot-report-tabs">
                  <span class="shot-tab active">Receipts &amp; Payments</span>
                  <span class="shot-tab">Cash Flow</span>
                  <span class="shot-tab">General Ledger</span>
                  <span class="shot-tab">…</span>
                </div>
                <div class="shot-report-meta">Jan 1 – Mar 31, 2024 &nbsp;|&nbsp; IPSAS 2 (Cash Basis)</div>
                <div class="shot-report-section">
                  <div class="shot-section-hd in">Receipts</div>
                  <div class="shot-report-line"><span>Initial Capital</span><span class="in">500,000</span></div>
                  <div class="shot-report-line"><span>Revenue</span><span class="in">45,000</span></div>
                  <div class="shot-report-line total"><span>Total Receipts</span><span class="in">545,000</span></div>
                </div>
                <div class="shot-report-section">
                  <div class="shot-section-hd out">Payments</div>
                  <div class="shot-report-line"><span>Expenditure</span><span class="out">317,600</span></div>
                  <div class="shot-report-line total"><span>Total Payments</span><span class="out">317,600</span></div>
                </div>
                <div class="shot-report-bal">
                  <span>Closing Balance</span><span class="bal">KES 227,400</span>
                </div>
              </div>
            </div>
          </div>
          <div class="feature-text">
            <span class="section-eyebrow">Financial Reports</span>
            <h2>Seven report types, one click</h2>
            <p>
              Generate any of seven recognised report formats for any date range inside your project period.
              All calculations are strict cash-basis — no accruals, no estimates.
            </p>
            <ul class="feature-list">
              <li><strong>Statement of Receipts &amp; Payments</strong> — IPSAS 2 cash basis</li>
              <li><strong>Cash Flow Statement</strong> — IFRS: operating, investing &amp; financing</li>
              <li><strong>Statement of Financial Position</strong> — assets, liabilities, net assets</li>
              <li><strong>General Ledger</strong> — full transaction history with running balance</li>
              <li><strong>Trial Balance</strong> — debits and credits self-balancing proof</li>
              <li><strong>Balance Sheet</strong> — assets = liabilities + equity (accounting equation)</li>
              <li><strong>Summary Report</strong> — quick totals and closing balance</li>
            </ul>
          </div>
        </div>
      </section>

      <!-- ── Feature: Public links ────────────────────────────────── -->
      <section class="feature-section container alt">
        <div class="feature-row reverse">
          <div class="feature-text">
            <span class="section-eyebrow">Public Sharing</span>
            <h2>Share reports without sharing access</h2>
            <p>
              Generate a secure token-based link for any project. Recipients view live reports
              directly in their browser — no account required. Set an optional expiry date and
              revoke links at any time.
            </p>
            <ul class="feature-list">
              <li>Read-only access — no edits, no exposure</li>
              <li>Optional expiry date per link</li>
              <li>Revoke instantly from your dashboard</li>
              <li>Perfect for donor reports, audits or board presentations</li>
            </ul>
          </div>
          <div class="feature-shot">
            <!-- Public link mockup -->
            <div class="shot-window glass">
              <div class="shot-bar">
                <span class="dot red"></span><span class="dot yellow"></span><span class="dot green"></span>
                <span class="shot-url">Public Links</span>
              </div>
              <div class="shot-body">
                <div class="shot-link-row">
                  <div class="shot-link-icon">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"><path d="M13.19 8.688a4.5 4.5 0 0 1 1.242 7.244l-4.5 4.5a4.5 4.5 0 0 1-6.364-6.364l1.757-1.757m13.35-.622 1.757-1.757a4.5 4.5 0 0 0-6.364-6.364l-4.5 4.5a4.5 4.5 0 0 0 1.242 7.244"/></svg>
                  </div>
                  <div>
                    <div class="shot-link-url">katibu.my/public/a3f92c…</div>
                    <div class="shot-link-meta">Created Mar 15, 2024 &nbsp;·&nbsp; No expiry</div>
                  </div>
                  <span class="badge badge-active">Active</span>
                </div>
                <div class="shot-generate-area">
                  <div class="shot-gen-label">Generate new link</div>
                  <div class="shot-gen-row">
                    <span class="shot-gen-input">Expiry date (optional)</span>
                    <span class="shot-gen-btn">Generate</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- ── Feature: Exports ─────────────────────────────────────── -->
      <section class="feature-section container">
        <div class="feature-row">
          <div class="feature-shot">
            <div class="shot-window glass">
              <div class="shot-bar">
                <span class="dot red"></span><span class="dot yellow"></span><span class="dot green"></span>
                <span class="shot-url">Export Files</span>
              </div>
              <div class="shot-body">
                <div class="shot-export-row">
                  <div class="shot-file-icon pdf">PDF</div>
                  <div>
                    <div class="shot-file-name">receipts_payments_Q1_2024.pdf</div>
                    <div class="shot-file-meta">Generated Mar 31, 2024 · 148 KB</div>
                  </div>
                  <span class="shot-dl">↓</span>
                </div>
                <div class="shot-export-row">
                  <div class="shot-file-icon csv">CSV</div>
                  <div>
                    <div class="shot-file-name">ledger_full_Q1_2024.csv</div>
                    <div class="shot-file-meta">Generated Mar 31, 2024 · 22 KB</div>
                  </div>
                  <span class="shot-dl">↓</span>
                </div>
                <div class="shot-export-row">
                  <div class="shot-file-icon pdf">PDF</div>
                  <div>
                    <div class="shot-file-name">cash_flow_Q1_2024.pdf</div>
                    <div class="shot-file-meta">Generated Mar 28, 2024 · 132 KB</div>
                  </div>
                  <span class="shot-dl">↓</span>
                </div>
              </div>
            </div>
          </div>
          <div class="feature-text">
            <span class="section-eyebrow">File Exports</span>
            <h2>Download your records anytime</h2>
            <p>
              Export any report to a formatted PDF or a raw CSV spreadsheet.
              All files are stored securely and remain accessible for the lifetime of your project.
            </p>
            <ul class="feature-list">
              <li>PDF with formatted tables and project branding</li>
              <li>CSV for further analysis in Excel or Google Sheets</li>
              <li>Exports stored securely — download at any time</li>
              <li>Full ledger export available as a single file</li>
            </ul>
          </div>
        </div>
      </section>

      <!-- CTA -->
      <section class="container about-cta">
        <div class="cta-inner">
          <span class="section-eyebrow" style="color:var(--gold-light)">Get started</span>
          <h2>Open your first project</h2>
          <p>Free to use. No credit card. No setup complexity.</p>
          <router-link to="/register" class="btn btn-gold btn-lg">Create Free Account</router-link>
        </div>
      </section>

    </div>
  </main>
</template>

<script>
export default {
  name: 'AboutView',
  data () {
    return {
      mockProjects: [
        { name: 'Q1 2024 Operations', meta: 'QUARTERLY · Jan 1 – Mar 31, 2024', status: 'ACTIVE', badge: 'badge-active', balance: 'KES 182,400' },
        { name: 'Annual Fund 2023', meta: 'YEARLY · Jan 1 – Dec 31, 2023', status: 'ARCHIVED', badge: 'badge-archived', balance: 'KES 0' },
        { name: 'Community Grant H1', meta: 'HALF_YEARLY · Jan 1 – Jun 30, 2024', status: 'ACTIVE', badge: 'badge-active', balance: 'KES 65,200' }
      ],
      inflows: ['INITIAL_CAPITAL', 'DONATION', 'REVENUE', 'GRANT', 'LOAN_RECEIVED', 'CREDIT'],
      outflows: ['PURCHASE', 'EXPENDITURE', 'LOAN_REPAYMENT', 'DEBT_PAYMENT', 'WITHDRAWAL']
    }
  }
}
</script>

<style scoped>
/* About hero */
.about-hero { text-align: center; padding-bottom: 60px; }
.about-hero h1 { font-size: clamp(1.9rem, 4vw, 3rem); margin: 12px 0 20px; }
.about-lead { max-width: 680px; margin: 0 auto; color: var(--text-mid); font-size: 1.0625rem; line-height: 1.8; }

/* Feature sections */
.feature-section { padding: 60px 0; }
.feature-section.alt { background: var(--green-tint); border-radius: var(--r-xl); padding: 60px 40px; }
.feature-row {
  display: grid; grid-template-columns: 1fr 1fr; gap: 64px; align-items: center;
}
.feature-row.reverse .feature-shot { order: 2; }
.feature-row.reverse .feature-text { order: 1; }

/* Feature text */
.feature-text h2 { font-size: clamp(1.5rem, 2.5vw, 2rem); margin: 10px 0 16px; }
.feature-text p  { color: var(--text-mid); line-height: 1.8; margin-bottom: 20px; }
.feature-list { list-style: none; display: flex; flex-direction: column; gap: 10px; padding: 0; }
.feature-list li {
  padding-left: 20px; position: relative; font-size: 0.9375rem; color: var(--text-mid);
}
.feature-list li::before {
  content: ''; position: absolute; left: 0; top: 8px;
  width: 8px; height: 8px; border-radius: 50%;
  background: var(--green-mid);
}
.feature-list li strong { color: var(--text-dark); }

/* Entry types */
.entry-types { display: flex; flex-direction: column; gap: 14px; margin-top: 8px; }
.et-group {}
.et-label { font-size: 0.78rem; font-weight: 700; text-transform: uppercase; letter-spacing: 0.06em; margin-bottom: 6px; }
.et-label.inflow { color: var(--green-deep); }
.et-label.outflow { color: #C0392B; }
.et-tags { display: flex; flex-wrap: wrap; gap: 6px; }
.et-tag {
  font-size: 0.7rem; font-weight: 700; padding: 3px 9px;
  border-radius: 4px; letter-spacing: 0.03em;
}
.et-tag.in  { background: var(--green-pale); color: var(--green-deep); }
.et-tag.out { background: #FEE8E8; color: #C0392B; }

/* Screenshot windows */
.feature-shot { display: flex; justify-content: center; }
.shot-window { width: 100%; max-width: 420px; border-radius: var(--r-lg); box-shadow: var(--sh-lg); overflow: hidden; }
.shot-bar {
  display: flex; align-items: center; gap: 6px;
  background: rgba(240,250,244,0.9); padding: 10px 14px;
  border-bottom: 1px solid var(--border-sub);
}
.dot { width: 9px; height: 9px; border-radius: 50%; flex-shrink: 0; }
.dot.red    { background: #FF6057; }
.dot.yellow { background: #FEBC2E; }
.dot.green  { background: #2AC840; }
.shot-url { margin-left: 6px; font-size: 0.75rem; color: var(--text-light); font-family: 'Inter', monospace; }
.shot-body { padding: 18px; background: var(--off-white); }

/* Projects list shot */
.shot-page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; }
.shot-page-title { font-family: 'Playfair Display', serif; font-size: 0.95rem; font-weight: 700; color: var(--text-dark); }
.shot-btn { font-size: 0.72rem; font-weight: 700; padding: 4px 10px; background: var(--green-deep); color: white; border-radius: var(--r-sm); }
.shot-project-card {
  display: flex; justify-content: space-between; align-items: center;
  background: var(--white); border-radius: var(--r-sm); padding: 10px 12px;
  margin-bottom: 8px; border: 1px solid var(--border-sub);
}
.shot-proj-name { font-size: 0.85rem; font-weight: 600; color: var(--text-dark); margin-bottom: 2px; }
.shot-proj-meta { font-size: 0.7rem; color: var(--text-light); }
.shot-proj-right { display: flex; flex-direction: column; align-items: flex-end; gap: 4px; }
.shot-proj-bal { font-size: 0.78rem; font-weight: 700; color: var(--green-deep); }

/* Form shot */
.form-shot { padding: 14px 16px; }
.shot-form-row { margin-bottom: 10px; }
.shot-field-label { font-size: 0.68rem; font-weight: 600; color: var(--text-light); text-transform: uppercase; letter-spacing: 0.05em; margin-bottom: 3px; }
.shot-field-val {
  font-size: 0.82rem; color: var(--text-dark); padding: 7px 10px;
  background: var(--white); border: 1.5px solid var(--border-sub); border-radius: 6px;
}
.shot-field-val.select { color: var(--green-deep); font-weight: 600; }
.shot-field-val.muted { color: var(--text-light); }
.shot-form-actions { display: flex; gap: 8px; justify-content: flex-end; margin-top: 14px; }
.shot-cancel { font-size: 0.78rem; padding: 6px 12px; border-radius: 6px; color: var(--text-mid); background: var(--off-white); border: 1px solid var(--border-sub); cursor: default; }
.shot-submit { font-size: 0.78rem; padding: 6px 14px; border-radius: 6px; color: white; background: var(--green-deep); cursor: default; font-weight: 700; }

/* Report shot */
.shot-report-tabs { display: flex; gap: 0; margin-bottom: 12px; border-bottom: 1px solid var(--border-sub); }
.shot-tab { font-size: 0.72rem; padding: 6px 10px; color: var(--text-light); cursor: default; }
.shot-tab.active { color: var(--green-deep); font-weight: 700; border-bottom: 2px solid var(--green-mid); margin-bottom: -1px; }
.shot-report-meta { font-size: 0.7rem; color: var(--text-light); margin-bottom: 12px; }
.shot-report-section { margin-bottom: 10px; }
.shot-section-hd { font-size: 0.7rem; font-weight: 700; text-transform: uppercase; letter-spacing: 0.05em; margin-bottom: 6px; padding-bottom: 4px; border-bottom: 1px solid var(--border-sub); }
.shot-section-hd.in  { color: var(--green-deep); }
.shot-section-hd.out { color: #C0392B; }
.shot-report-line { display: flex; justify-content: space-between; font-size: 0.78rem; padding: 3px 0; color: var(--text-mid); }
.shot-report-line.total { border-top: 1px solid var(--border-sub); margin-top: 4px; font-weight: 700; color: var(--text-dark); }
.shot-report-line .in  { color: var(--green-deep); font-weight: 600; }
.shot-report-line .out { color: #C0392B; font-weight: 600; }
.shot-report-bal {
  display: flex; justify-content: space-between; align-items: center;
  margin-top: 10px; padding: 10px 12px; background: var(--green-tint);
  border-radius: var(--r-sm); font-size: 0.82rem; font-weight: 700; color: var(--text-dark);
}
.shot-report-bal .bal { color: var(--green-deep); font-size: 0.9rem; }

/* Public link shot */
.shot-link-row {
  display: flex; align-items: center; gap: 10px;
  background: var(--white); border-radius: var(--r-sm); padding: 12px;
  margin-bottom: 12px; border: 1px solid var(--border-sub);
}
.shot-link-icon { width: 32px; height: 32px; background: var(--green-tint); border-radius: 8px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; }
.shot-link-icon svg { width: 16px; height: 16px; color: var(--green-deep); }
.shot-link-url { font-size: 0.78rem; font-weight: 600; color: var(--text-dark); margin-bottom: 2px; }
.shot-link-meta { font-size: 0.68rem; color: var(--text-light); }
.shot-generate-area { background: var(--white); border-radius: var(--r-sm); padding: 12px; border: 1px dashed var(--border-sub); }
.shot-gen-label { font-size: 0.7rem; font-weight: 700; color: var(--text-mid); text-transform: uppercase; letter-spacing: 0.05em; margin-bottom: 8px; }
.shot-gen-row { display: flex; gap: 8px; }
.shot-gen-input { flex: 1; font-size: 0.75rem; padding: 6px 10px; border: 1.5px solid var(--border-sub); border-radius: 6px; color: var(--text-light); background: var(--off-white); }
.shot-gen-btn { font-size: 0.75rem; padding: 6px 12px; background: var(--gold); color: white; border-radius: 6px; font-weight: 700; cursor: default; }

/* Export shot */
.shot-export-row {
  display: flex; align-items: center; gap: 10px;
  background: var(--white); border-radius: var(--r-sm); padding: 10px 12px;
  margin-bottom: 8px; border: 1px solid var(--border-sub);
}
.shot-file-icon {
  width: 36px; height: 36px; border-radius: 6px;
  display: flex; align-items: center; justify-content: center;
  font-size: 0.65rem; font-weight: 800; flex-shrink: 0;
}
.shot-file-icon.pdf { background: rgba(192,57,43,0.1); color: #C0392B; }
.shot-file-icon.csv { background: var(--green-pale); color: var(--green-deep); }
.shot-file-name { font-size: 0.78rem; font-weight: 600; color: var(--text-dark); margin-bottom: 2px; }
.shot-file-meta { font-size: 0.68rem; color: var(--text-light); }
.shot-dl { margin-left: auto; width: 28px; height: 28px; border-radius: 50%; background: var(--green-pale); color: var(--green-deep); display: flex; align-items: center; justify-content: center; font-size: 1rem; cursor: default; }

/* About CTA */
.about-cta { margin-top: 60px; }
.cta-inner {
  background: var(--green-deep); border-radius: var(--r-xl);
  padding: 56px; text-align: center;
}
.cta-inner h2 { font-size: 2rem; color: var(--white); margin-bottom: 12px; }
.cta-inner p  { color: rgba(255,255,255,0.65); margin-bottom: 28px; }

@media (max-width: 900px) {
  .feature-row { grid-template-columns: 1fr; gap: 32px; }
  .feature-row.reverse .feature-shot,
  .feature-row.reverse .feature-text { order: unset; }
  .feature-section.alt { padding: 40px 20px; }
}
@media (max-width: 560px) {
  .cta-inner { padding: 36px 24px; }
  .cta-inner h2 { font-size: 1.6rem; }
}
</style>
