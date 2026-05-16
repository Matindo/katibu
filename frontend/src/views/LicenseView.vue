<template>
  <main class="page">
    <div class="page-inner container">

      <div class="page-header">
        <h1>Legal &amp; Licensing</h1>
        <p class="text-mid">License, Terms &amp; Conditions, and Privacy Policy for Katibu</p>
      </div>

      <!-- Nav tabs -->
      <div class="legal-tabs">
        <button v-for="t in tabs" :key="t.id" :class="['tab-btn', { active: active === t.id }]" @click="active = t.id">{{ t.label }}</button>
      </div>

      <!-- License -->
      <div v-if="active === 'license'" id="license" class="legal-card card">
        <h2>Software License</h2>
        <p class="legal-meta">Katibu Financial Management Platform &nbsp;·&nbsp; Bysonic Inc. &nbsp;·&nbsp; All rights reserved.</p>
        <div class="legal-body">
          <h4>1. Grant of License</h4>
          <p>Subject to these terms, Bysonic Inc. grants you a non-exclusive, non-transferable, limited license to use Katibu solely for your own internal business or organisational financial record-keeping purposes.</p>

          <h4>2. Restrictions</h4>
          <p>You may not: (a) reproduce, distribute, or publicly display the software; (b) reverse-engineer or decompile any portion of the platform; (c) use the platform to provide competing services; (d) remove or alter any proprietary notices.</p>

          <h4>3. Intellectual Property</h4>
          <p>All intellectual property rights in Katibu, including the underlying software, design, and content, are owned by Bysonic Inc. or its licensors. This license does not transfer any ownership rights.</p>

          <h4>4. Financial Data</h4>
          <p>You retain full ownership of the financial data you input into Katibu. Bysonic Inc. does not claim any rights to your project data. You are responsible for the accuracy of records and compliance with applicable accounting standards.</p>

          <h4>5. Disclaimer</h4>
          <p>Katibu is provided "as is" without warranty of any kind. Bysonic Inc. does not warrant that the platform will be error-free or that financial reports generated will satisfy the requirements of any specific regulatory body. Users are encouraged to have reports reviewed by qualified financial professionals.</p>

          <h4>6. Limitation of Liability</h4>
          <p>To the maximum extent permitted by law, Bysonic Inc. shall not be liable for any indirect, incidental, or consequential damages arising from your use of or inability to use Katibu.</p>

          <h4>7. Governing Law</h4>
          <p>This license is governed by the laws of Kenya. Any disputes shall be subject to the exclusive jurisdiction of the courts of Nairobi, Kenya.</p>
        </div>
      </div>

      <!-- Terms -->
      <div v-if="active === 'terms'" id="terms" class="legal-card card">
        <h2>Terms &amp; Conditions</h2>
        <p class="legal-meta">Last updated: January 2025 &nbsp;·&nbsp; Bysonic Inc.</p>
        <div class="legal-body">
          <h4>1. Acceptance of Terms</h4>
          <p>By creating an account or using Katibu, you agree to be bound by these Terms &amp; Conditions. If you do not agree, do not use the service.</p>

          <h4>2. Account Responsibilities</h4>
          <p>You are responsible for maintaining the security of your account credentials. You must notify us immediately of any unauthorised access. Each account is for use by a single individual or organisation.</p>

          <h4>3. Acceptable Use</h4>
          <p>You may not use Katibu to record fraudulent, fictitious, or illegal financial transactions. You agree not to attempt to disrupt the service or gain unauthorised access to any system.</p>

          <h4>4. Data Accuracy</h4>
          <p>You are solely responsible for the accuracy, completeness, and legality of all financial data you record. Katibu does not audit or verify the accuracy of your entries.</p>

          <h4>5. Public Links</h4>
          <p>When you generate a public link, you are granting anonymous read access to your project's reports to anyone with that link. You are responsible for who you share links with. Revoke links when access is no longer needed.</p>

          <h4>6. Service Availability</h4>
          <p>Bysonic Inc. will endeavour to maintain high service availability, but does not guarantee uninterrupted access. Scheduled maintenance will be communicated in advance where practicable.</p>

          <h4>7. Termination</h4>
          <p>We reserve the right to suspend or terminate accounts that violate these terms. You may delete your account at any time by contacting support.</p>

          <h4>8. Changes to Terms</h4>
          <p>We may update these terms from time to time. Continued use of Katibu after changes constitutes acceptance of the revised terms.</p>
        </div>
      </div>

      <!-- Privacy -->
      <div v-if="active === 'privacy'" id="privacy" class="legal-card card">
        <h2>Privacy Policy</h2>
        <p class="legal-meta">Last updated: January 2025 &nbsp;·&nbsp; Bysonic Inc.</p>
        <div class="legal-body">
          <h4>1. Information We Collect</h4>
          <p>We collect: (a) account information you provide (name, email, password); (b) financial data you record within projects; (c) technical usage data (IP address, browser type, access logs) for security and performance purposes.</p>

          <h4>2. How We Use Your Data</h4>
          <p>Your data is used to: (a) provide and improve the Katibu service; (b) authenticate your identity and secure your account; (c) generate the financial reports you request; (d) communicate important service updates.</p>

          <h4>3. Data Storage</h4>
          <p>Your data is stored on secure servers within our cloud infrastructure. Financial records and exported files are stored securely with access controls. We implement industry-standard encryption in transit and at rest.</p>

          <h4>4. Data Sharing</h4>
          <p>We do not sell, trade, or rent your personal information to third parties. We do not share your financial data with any third party except as required by law or as necessary to operate the service (e.g., database hosting providers).</p>

          <h4>5. Public Links</h4>
          <p>When you create a public link, report data accessible via that link is intentionally visible to anyone with the URL. This is a feature you control — revoke links to restrict access.</p>

          <h4>6. Data Retention</h4>
          <p>We retain your data for as long as your account is active. Upon account deletion, personal data is removed within 30 days. Financial records may be retained for up to 7 years as required by applicable accounting regulations.</p>

          <h4>7. Your Rights</h4>
          <p>You have the right to access, correct, or request deletion of your personal data. To exercise these rights, contact us at <a href="mailto:privacy@katibu.my">privacy@katibu.my</a>.</p>

          <h4>8. Contact</h4>
          <p>For privacy concerns, contact: <a href="mailto:privacy@katibu.my">privacy@katibu.my</a></p>
        </div>
      </div>

      <div class="legal-footer">
        <p>&copy; {{ year }} Bysonic Inc.&trade; All rights reserved. Katibu is a registered product of Bysonic Inc.</p>
        <p>Questions? Contact <a href="mailto:legal@katibu.my">legal@katibu.my</a></p>
      </div>

    </div>
  </main>
</template>

<script>
export default {
  name: 'LicenseView',
  data () {
    const hash = window.location.hash.replace('#', '')
    return {
      active: ['license','terms','privacy'].includes(hash) ? hash : 'license',
      tabs: [
        { id: 'license', label: 'License' },
        { id: 'terms',   label: 'Terms & Conditions' },
        { id: 'privacy', label: 'Privacy Policy' }
      ]
    }
  },
  computed: {
    year () { return new Date().getFullYear() }
  }
}
</script>

<style scoped>
.legal-tabs { display: flex; gap: 4px; margin-bottom: 28px; border-bottom: 1px solid var(--border-sub); }
.tab-btn {
  padding: 10px 20px; background: none; border: none; border-bottom: 2px solid transparent;
  font-size: 0.9375rem; font-weight: 600; color: var(--text-light); cursor: pointer;
  transition: color var(--t), border-color var(--t); margin-bottom: -1px;
}
.tab-btn:hover { color: var(--green-deep); }
.tab-btn.active { color: var(--green-deep); border-bottom-color: var(--green-mid); }

.legal-card { max-width: 800px; }
.legal-card h2 { font-size: 1.6rem; margin-bottom: 6px; }
.legal-meta { font-size: 0.875rem; color: var(--text-light); margin-bottom: 28px; }

.legal-body h4 {
  font-family: 'Inter', sans-serif; font-size: 0.9375rem;
  font-weight: 700; color: var(--text-dark);
  margin: 22px 0 8px;
}
.legal-body h4:first-child { margin-top: 0; }
.legal-body p { font-size: 0.9375rem; color: var(--text-mid); line-height: 1.8; }
.legal-body a { color: var(--green-mid); text-decoration: underline; }

.legal-footer { margin-top: 48px; padding-top: 24px; border-top: 1px solid var(--border-sub); }
.legal-footer p { font-size: 0.875rem; color: var(--text-light); line-height: 1.8; }
.legal-footer a { color: var(--green-mid); }

@media (max-width: 560px) {
  .legal-tabs { flex-wrap: wrap; }
  .tab-btn { padding: 8px 14px; font-size: 0.875rem; }
}
</style>
