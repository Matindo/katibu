<template>
  <main class="home">

    <!-- ── Hero ─────────────────────────────────────────────── -->
    <section class="hero">
      <!-- Decorative blobs -->
      <div class="blob blob-1"></div>
      <div class="blob blob-2"></div>
      <div class="blob blob-3"></div>

      <div class="container hero-inner">
        <!-- Left: copy -->
        <div class="hero-copy">
          <span class="section-eyebrow">Financial Management Platform</span>
          <h1 class="hero-h1">Track, Account &amp;<br>Report with Confidence</h1>
          <p class="hero-sub">
            Katibu brings cash-based IPSAS &amp; IFRS financial reporting to teams,
            community groups and SMEs — without the enterprise price tag.
          </p>
          <div class="hero-cta">
            <router-link to="/register" class="btn btn-primary btn-lg">Get Started Free</router-link>
            <router-link to="/about" class="btn btn-outline btn-lg">Learn More</router-link>
          </div>
          <div class="hero-chips">
            <span class="chip">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"><path d="M9 12.75 11.25 15 15 9.75m-3-7.036A11.959 11.959 0 0 1 3.598 6 11.99 11.99 0 0 0 3 9.749c0 5.592 3.824 10.29 9 11.623 5.176-1.332 9-6.03 9-11.622 0-1.31-.21-2.571-.598-3.751h-.152c-3.196 0-6.1-1.248-8.25-3.285Z"/></svg>
              IPSAS Cash Basis
            </span>
            <span class="chip">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"><path d="M3 13.125C3 12.504 3.504 12 4.125 12h2.25c.621 0 1.125.504 1.125 1.125v6.75C7.5 20.496 6.996 21 6.375 21h-2.25A1.125 1.125 0 0 1 3 19.875v-6.75ZM9.75 8.625c0-.621.504-1.125 1.125-1.125h2.25c.621 0 1.125.504 1.125 1.125v11.25c0 .621-.504 1.125-1.125 1.125h-2.25a1.125 1.125 0 0 1-1.125-1.125V8.625ZM16.5 4.125c0-.621.504-1.125 1.125-1.125h2.25C20.496 3 21 3.504 21 4.125v15.75c0 .621-.504 1.125-1.125 1.125h-2.25a1.125 1.125 0 0 1-1.125-1.125V4.125Z"/></svg>
              IFRS Reports
            </span>
            <span class="chip">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"><path d="M13.19 8.688a4.5 4.5 0 0 1 1.242 7.244l-4.5 4.5a4.5 4.5 0 0 1-6.364-6.364l1.757-1.757m13.35-.622 1.757-1.757a4.5 4.5 0 0 0-6.364-6.364l-4.5 4.5a4.5 4.5 0 0 0 1.242 7.244"/></svg>
              Public Sharing
            </span>
            <span class="chip">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"><path d="M3 16.5v2.25A2.25 2.25 0 0 0 5.25 21h13.5A2.25 2.25 0 0 0 21 18.75V16.5M16.5 12 12 16.5m0 0L7.5 12m4.5 4.5V3"/></svg>
              PDF &amp; CSV Export
            </span>
          </div>
        </div>

        <!-- Right: app mockup -->
        <div class="hero-mockup">
          <div class="mockup-window glass">
            <div class="mockup-bar">
              <span class="dot red"></span><span class="dot yellow"></span><span class="dot green"></span>
              <span class="mockup-url">katibu.my/projects</span>
            </div>
            <div class="mockup-body">
              <div class="mock-header">
                <div>
                  <div class="mock-title">Q1 2024 — Operations</div>
                  <span class="badge badge-active">Active</span>
                </div>
                <div class="mock-period">Jan 1 – Mar 31, 2024</div>
              </div>
              <div class="mock-stats">
                <div class="mock-stat green">
                  <div class="mock-stat-label">Balance</div>
                  <div class="mock-stat-val">KES 182,400</div>
                </div>
                <div class="mock-stat">
                  <div class="mock-stat-label">Total In</div>
                  <div class="mock-stat-val in">+500,000</div>
                </div>
                <div class="mock-stat">
                  <div class="mock-stat-label">Total Out</div>
                  <div class="mock-stat-val out">−317,600</div>
                </div>
              </div>
              <div class="mock-entries">
                <div class="mock-entry-hd">Recent Transactions</div>
                <div class="mock-entry">
                  <div class="mock-entry-type">REVENUE</div>
                  <div class="mock-entry-desc">Client payment – Invoice #042</div>
                  <div class="mock-entry-amt in">+45,000</div>
                </div>
                <div class="mock-entry">
                  <div class="mock-entry-type">EXPENDITURE</div>
                  <div class="mock-entry-desc">Office supplies & stationery</div>
                  <div class="mock-entry-amt out">−3,200</div>
                </div>
                <div class="mock-entry">
                  <div class="mock-entry-type">LOAN_REPAYMENT</div>
                  <div class="mock-entry-desc">Bank loan instalment – March</div>
                  <div class="mock-entry-amt out">−18,000</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- ── Feature strip ─────────────────────────────────────── -->
    <section class="features-strip">
      <div class="container">
        <div class="features-grid">
          <div v-for="f in features" :key="f.title" class="feature-card glass">
            <div class="feature-icon">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" v-html="f.path"></svg>
            </div>
            <h3>{{ f.title }}</h3>
            <p>{{ f.desc }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- ── How it works ──────────────────────────────────────── -->
    <section class="how-section">
      <div class="container">
        <div class="section-label">
          <span class="section-eyebrow">How it works</span>
          <h2>From setup to report in minutes</h2>
          <p class="section-sub">No accounting degree required. Katibu guides you through each step.</p>
        </div>
        <div class="steps-grid">
          <div v-for="(s, i) in steps" :key="i" class="step-card">
            <div class="step-num">{{ i + 1 }}</div>
            <h4>{{ s.title }}</h4>
            <p>{{ s.desc }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- ── Standards ─────────────────────────────────────────── -->
    <section class="standards-section">
      <div class="container">
        <div class="standards-inner glass">
          <div class="standards-text">
            <span class="section-eyebrow">Accounting Standards</span>
            <h2>Built on recognised frameworks</h2>
            <p>
              Katibu implements <strong>IPSAS 2</strong> (Statement of Receipts &amp; Payments) for
              cash-basis public and community accounting, and <strong>IFRS cash flow classification</strong>
              for operating, investing, and financing activities — giving your reports credibility with
              auditors and donors alike.
            </p>
            <router-link to="/about" class="btn btn-outline mt-24">See Report Types</router-link>
          </div>
          <div class="standards-badges">
            <div class="std-badge">
              <div class="std-icon gold">IPSAS</div>
              <div class="std-info">
                <strong>IPSAS 2</strong>
                <span>Statement of Receipts &amp; Payments</span>
              </div>
            </div>
            <div class="std-badge">
              <div class="std-icon green">IFRS</div>
              <div class="std-info">
                <strong>IFRS Cash Flow</strong>
                <span>Operating · Investing · Financing</span>
              </div>
            </div>
            <div class="std-badge">
              <div class="std-icon light">FP</div>
              <div class="std-info">
                <strong>Financial Position</strong>
                <span>Assets · Liabilities · Net Assets</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- ── CTA ───────────────────────────────────────────────── -->
    <section class="cta-section">
      <div class="container">
        <div class="cta-inner">
          <span class="section-eyebrow" style="color:var(--gold-light)">Ready to start?</span>
          <h2>Open your first project today</h2>
          <p>Free to use. No credit card. No complicated setup.</p>
          <div class="cta-btns">
            <router-link to="/register" class="btn btn-gold btn-lg">Create Free Account</router-link>
            <router-link to="/login" class="btn btn-outline-gold btn-lg">Sign In</router-link>
          </div>
        </div>
      </div>
    </section>

  </main>
</template>

<script>
export default {
  name: 'HomeView',
  data () {
    return {
      features: [
        {
          title: 'Project Ledgers',
          desc: 'Create independent ledgers for each project, fund, or period. Weekly, monthly, quarterly or custom durations.',
          path: '<path d="M3 7a2 2 0 0 1 2-2h3l2 2h8a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V7Z"/>'
        },
        {
          title: 'Financial Reports',
          desc: 'One click generates Receipts & Payments, Cash Flow Statement, Financial Position or a quick Summary.',
          path: '<path d="M3 13.125C3 12.504 3.504 12 4.125 12h2.25c.621 0 1.125.504 1.125 1.125v6.75C7.5 20.496 6.996 21 6.375 21h-2.25A1.125 1.125 0 0 1 3 19.875v-6.75ZM9.75 8.625c0-.621.504-1.125 1.125-1.125h2.25c.621 0 1.125.504 1.125 1.125v11.25c0 .621-.504 1.125-1.125 1.125h-2.25a1.125 1.125 0 0 1-1.125-1.125V8.625ZM16.5 4.125c0-.621.504-1.125 1.125-1.125h2.25C20.496 3 21 3.504 21 4.125v15.75c0 .621-.504 1.125-1.125 1.125h-2.25a1.125 1.125 0 0 1-1.125-1.125V4.125Z"/>'
        },
        {
          title: 'Public Sharing',
          desc: 'Generate a read-only shareable link for stakeholders, donors and auditors — no account needed to view.',
          path: '<path d="M13.19 8.688a4.5 4.5 0 0 1 1.242 7.244l-4.5 4.5a4.5 4.5 0 0 1-6.364-6.364l1.757-1.757m13.35-.622 1.757-1.757a4.5 4.5 0 0 0-6.364-6.364l-4.5 4.5a4.5 4.5 0 0 0 1.242 7.244"/>'
        },
        {
          title: 'PDF &amp; CSV Export',
          desc: 'Download any report as a formatted PDF or a raw CSV — stored securely and available whenever you need.',
          path: '<path d="M3 16.5v2.25A2.25 2.25 0 0 0 5.25 21h13.5A2.25 2.25 0 0 0 21 18.75V16.5M16.5 12 12 16.5m0 0L7.5 12m4.5 4.5V3"/>'
        },
        {
          title: 'Team Access',
          desc: 'Invite admin members to record transactions and generate reports — while the creator retains full control.',
          path: '<path d="M15 19.128a9.38 9.38 0 0 0 2.625.372 9.337 9.337 0 0 0 4.121-.952 4.125 4.125 0 0 0-7.533-2.493M15 19.128v-.003c0-1.113-.285-2.16-.786-3.07M15 19.128v.106A12.318 12.318 0 0 1 8.624 21c-2.331 0-4.512-.645-6.374-1.766l-.001-.109a6.375 6.375 0 0 1 11.964-3.07M12 6.375a3.375 3.375 0 1 1-6.75 0 3.375 3.375 0 0 1 6.75 0Zm8.25 2.25a2.625 2.625 0 1 1-5.25 0 2.625 2.625 0 0 1 5.25 0Z"/>'
        },
        {
          title: 'Audit Trail',
          desc: 'Every ledger entry is preserved — soft-deleted entries remain traceable. No record is ever permanently lost.',
          path: '<path d="M9 12.75 11.25 15 15 9.75m-3-7.036A11.959 11.959 0 0 1 3.598 6 11.99 11.99 0 0 0 3 9.749c0 5.592 3.824 10.29 9 11.623 5.176-1.332 9-6.03 9-11.622 0-1.31-.21-2.571-.598-3.751h-.152c-3.196 0-6.1-1.248-8.25-3.285Z"/>'
        }
      ],
      steps: [
        { title: 'Create a project',    desc: 'Set a name, duration type and start date. Katibu calculates the end date for you.' },
        { title: 'Record transactions', desc: 'Add inflows (donations, revenue, grants) and outflows (purchases, expenditure) as they happen.' },
        { title: 'Generate reports',    desc: 'Choose a date range and report type. Download as PDF or CSV instantly.' },
        { title: 'Share with stakeholders', desc: 'Create a public link for auditors, donors or the public — no login required on their end.' }
      ]
    }
  }
}
</script>

<style scoped>
/* ── Hero ───────────────────────────────────────────────────── */
.hero {
  position: relative; overflow: hidden;
  background: linear-gradient(160deg, #eef9f2 0%, #f8fdf9 55%, #fffdf5 100%);
  min-height: calc(100vh - var(--nav-h));
  display: flex; align-items: center;
  padding: calc(var(--nav-h) + 40px) 0 80px;
}

.blob {
  position: absolute; border-radius: 50%;
  filter: blur(64px); pointer-events: none;
}
.blob-1 { width: 480px; height: 480px; background: rgba(116,198,157,0.22); top: -120px; right: -60px; }
.blob-2 { width: 320px; height: 320px; background: rgba(201,168,76,0.14); bottom: 40px; right: 30%; }
.blob-3 { width: 260px; height: 260px; background: rgba(45,106,79,0.10); top: 60%; left: 10%; }

.hero-inner {
  display: grid; grid-template-columns: 1fr 1fr; gap: 64px; align-items: center;
  position: relative; z-index: 1;
}

.section-eyebrow { margin-bottom: 14px; }

.hero-h1 {
  font-size: clamp(2.2rem, 4.5vw, 3.4rem);
  color: var(--text-dark); margin-bottom: 20px; line-height: 1.18;
}

.hero-sub {
  font-size: 1.0625rem; color: var(--text-mid);
  max-width: 480px; margin-bottom: 32px; line-height: 1.75;
}

.hero-cta { display: flex; gap: 14px; flex-wrap: wrap; margin-bottom: 32px; }

.hero-chips { display: flex; flex-wrap: wrap; gap: 10px; }
.chip {
  display: inline-flex; align-items: center; gap: 6px;
  padding: 6px 14px; background: rgba(255,255,255,0.72);
  border: 1px solid var(--border-sub); border-radius: 99px;
  font-size: 0.825rem; font-weight: 600; color: var(--text-mid);
  backdrop-filter: blur(8px);
}
.chip svg { width: 14px; height: 14px; color: var(--green-mid); flex-shrink: 0; }

/* Mockup */
.hero-mockup { display: flex; justify-content: center; }
.mockup-window {
  width: 100%; max-width: 440px; border-radius: var(--r-lg);
  box-shadow: var(--sh-lg); overflow: hidden;
}
.mockup-bar {
  display: flex; align-items: center; gap: 6px;
  background: rgba(240,250,244,0.9); padding: 10px 16px;
  border-bottom: 1px solid var(--border-sub);
}
.dot {
  width: 10px; height: 10px; border-radius: 50%; flex-shrink: 0;
}
.dot.red    { background: #FF6057; }
.dot.yellow { background: #FEBC2E; }
.dot.green  { background: #2AC840; }
.mockup-url {
  margin-left: 8px; font-size: 0.78rem; color: var(--text-light);
  font-family: 'Inter', monospace;
}
.mockup-body { padding: 20px; background: var(--off-white); }
.mock-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 16px; }
.mock-title { font-family: 'Playfair Display', serif; font-size: 1rem; font-weight: 700; color: var(--text-dark); margin-bottom: 4px; }
.mock-period { font-size: 0.78rem; color: var(--text-light); text-align: right; }
.mock-stats { display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 8px; margin-bottom: 16px; }
.mock-stat {
  background: var(--white); border-radius: var(--r-sm); padding: 10px 8px;
  border: 1px solid var(--border-sub); text-align: center;
}
.mock-stat.green { border-color: rgba(116,198,157,0.4); background: var(--green-tint); }
.mock-stat-label { font-size: 0.68rem; color: var(--text-light); text-transform: uppercase; letter-spacing: 0.04em; margin-bottom: 4px; }
.mock-stat-val { font-size: 0.82rem; font-weight: 700; color: var(--text-dark); }
.mock-stat-val.in  { color: var(--green-deep); }
.mock-stat-val.out { color: #C0392B; }
.mock-entry-hd { font-size: 0.72rem; font-weight: 700; text-transform: uppercase; letter-spacing: 0.06em; color: var(--text-light); margin-bottom: 8px; }
.mock-entry {
  display: flex; align-items: center; gap: 8px; padding: 7px 0;
  border-bottom: 1px solid var(--border-sub); font-size: 0.8rem;
}
.mock-entry:last-child { border-bottom: none; }
.mock-entry-type {
  font-size: 0.65rem; font-weight: 700; padding: 2px 6px;
  border-radius: 4px; background: var(--green-pale); color: var(--green-deep);
  white-space: nowrap; flex-shrink: 0;
}
.mock-entry-desc { flex: 1; color: var(--text-mid); white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.mock-entry-amt { font-weight: 700; flex-shrink: 0; }
.mock-entry-amt.in  { color: var(--green-deep); }
.mock-entry-amt.out { color: #C0392B; }

/* Features strip */
.features-strip { padding: 80px 0; }
.features-grid {
  display: grid; grid-template-columns: repeat(3, 1fr); gap: 24px;
}
.feature-card {
  border-radius: var(--r-lg); padding: 28px 24px;
  box-shadow: var(--sh-sm);
  transition: transform var(--t), box-shadow var(--t);
}
.feature-card:hover { transform: translateY(-4px); box-shadow: var(--sh-md); }
.feature-icon {
  width: 48px; height: 48px; border-radius: var(--r-md);
  background: var(--green-tint); display: flex; align-items: center; justify-content: center;
  margin-bottom: 16px; color: var(--green-deep);
}
.feature-icon svg { width: 24px; height: 24px; }
.feature-card h3 { font-size: 1.1rem; margin-bottom: 8px; }
.feature-card p  { font-size: 0.9rem; color: var(--text-mid); line-height: 1.65; }

/* How section */
.how-section { padding: 80px 0; background: var(--green-tint); }
.section-label { text-align: center; margin-bottom: 48px; }
.section-label h2 { font-size: 2rem; margin-bottom: 12px; }
.section-sub { color: var(--text-mid); font-size: 1rem; max-width: 480px; margin: 0 auto; }
.steps-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 24px; }
.step-card { background: var(--white); border-radius: var(--r-lg); padding: 28px 20px; text-align: center; box-shadow: var(--sh-sm); }
.step-num {
  width: 40px; height: 40px; border-radius: 50%;
  background: var(--green-deep); color: var(--white);
  font-size: 1.1rem; font-weight: 700; display: flex; align-items: center; justify-content: center;
  margin: 0 auto 16px;
}
.step-card h4 { font-family: 'Playfair Display', serif; font-size: 1.05rem; margin-bottom: 10px; }
.step-card p  { font-size: 0.875rem; color: var(--text-mid); line-height: 1.65; }

/* Standards */
.standards-section { padding: 80px 0; }
.standards-inner {
  border-radius: var(--r-xl); padding: 52px;
  display: grid; grid-template-columns: 1fr 1fr; gap: 60px; align-items: center;
  box-shadow: var(--sh-md);
}
.standards-text p { color: var(--text-mid); line-height: 1.8; margin-top: 16px; }
.standards-text strong { color: var(--green-deep); }
.standards-badges { display: flex; flex-direction: column; gap: 16px; }
.std-badge {
  display: flex; align-items: center; gap: 16px;
  background: var(--white); border-radius: var(--r-md);
  padding: 16px 20px; border: 1px solid var(--border-sub);
  box-shadow: var(--sh-sm);
}
.std-icon {
  width: 52px; height: 52px; border-radius: var(--r-sm);
  display: flex; align-items: center; justify-content: center;
  font-family: 'Playfair Display', serif; font-size: 0.875rem; font-weight: 700;
  flex-shrink: 0;
}
.std-icon.gold  { background: rgba(201,168,76,0.12); color: var(--gold-dark); }
.std-icon.green { background: var(--green-pale); color: var(--green-deep); }
.std-icon.light { background: var(--off-white); color: var(--text-mid); border: 1px solid var(--border-sub); }
.std-info { display: flex; flex-direction: column; gap: 2px; }
.std-info strong { font-size: 0.95rem; color: var(--text-dark); }
.std-info span   { font-size: 0.8125rem; color: var(--text-light); }

/* CTA */
.cta-section { padding: 80px 0; }
.cta-inner {
  background: var(--green-deep); border-radius: var(--r-xl);
  padding: 64px 52px; text-align: center;
}
.cta-inner h2 { font-size: 2.2rem; color: var(--white); margin-bottom: 14px; }
.cta-inner p  { color: rgba(255,255,255,0.65); font-size: 1rem; margin-bottom: 32px; }
.cta-btns { display: flex; gap: 16px; justify-content: center; flex-wrap: wrap; }

/* Responsive */
@media (max-width: 1024px) {
  .steps-grid { grid-template-columns: repeat(2, 1fr); }
  .features-grid { grid-template-columns: repeat(2, 1fr); }
}
@media (max-width: 900px) {
  .hero-inner { grid-template-columns: 1fr; }
  .hero-mockup { display: none; }
  .standards-inner { grid-template-columns: 1fr; gap: 32px; }
}
@media (max-width: 640px) {
  .features-grid { grid-template-columns: 1fr; }
  .steps-grid { grid-template-columns: 1fr 1fr; }
  .cta-inner { padding: 40px 24px; }
  .cta-inner h2 { font-size: 1.7rem; }
}
@media (max-width: 420px) {
  .steps-grid { grid-template-columns: 1fr; }
}
</style>
