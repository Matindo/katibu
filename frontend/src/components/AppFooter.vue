<template>
  <footer class="footer">
    <div class="container">
      <div class="footer-grid">

        <!-- Brand column -->
        <div class="footer-brand">
          <div class="footer-logo">
            <img class="footer-logo-img" src="@/assets/images/high-resolution-logo-grayscale.png" alt="Katibu"/>
            <span class="footer-logo-text">Katibu</span>
          </div>
          <p class="footer-tagline">
            Financial records, accountability &amp; reporting for teams and SMEs. Cash-based. Transparent. Trustworthy.
          </p>
        </div>

        <!-- Product links -->
        <div class="footer-col">
          <h4 class="footer-heading">Product</h4>
          <ul>
            <li><router-link to="/">Home</router-link></li>
            <li><router-link to="/about">About &amp; Features</router-link></li>
            <li><router-link to="/register">Get Started</router-link></li>
            <li><router-link to="/login">Sign In</router-link></li>
          </ul>
        </div>

        <!-- Legal links -->
        <div class="footer-col">
          <h4 class="footer-heading">Legal</h4>
          <ul>
            <li><router-link to="/license">License</router-link></li>
            <li><router-link to="/license#terms">Terms &amp; Conditions</router-link></li>
            <li><router-link to="/license#privacy">Privacy Policy</router-link></li>
          </ul>
        </div>

        <!-- Contact -->
        <div class="footer-col">
          <h4 class="footer-heading">Contact</h4>
          <ul>
            <li><a href="mailto:support@katibu.my">support@katibu.my</a></li>
            <li><a href="mailto:info@bysonicInc.com">info@bysonicInc.com</a></li>
          </ul>
          <p class="footer-bysonic">A product of<br><strong>Bysonic Inc.</strong></p>
        </div>

      </div>

      <div class="footer-bottom">
        <span class="footer-copy">
          &copy; {{ year }} Bysonic Inc.&trade; All rights reserved.
        </span>
        <span class="footer-product">
          Katibu &mdash; Financial Management Platform
        </span>
      </div>
    </div>
  </footer>
</template>

<script>
export default {
  name: 'AppFooter',
  computed: {
    year () { return new Date().getFullYear() }
  }
}
</script>

<style scoped>
.footer {
  background: var(--green-deep);
  color: rgba(255,255,255,0.8);
  padding: 60px 0 0;
  margin-top: 80px;
}

.footer-grid {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr 1fr;
  gap: 40px;
  padding-bottom: 48px;
  border-bottom: 1px solid rgba(255,255,255,0.1);
}

/* Brand */
.footer-logo { display: flex; align-items: center; gap: 10px; margin-bottom: 16px; }
.footer-logo-img { width: 32px; height: 32px; object-fit: contain; }
.footer-logo-text {
  font-family: 'Playfair Display', serif;
  font-size: 1.4rem; font-weight: 700; color: var(--white);
}
.footer-tagline { font-size: 0.9rem; color: rgba(255,255,255,0.6); line-height: 1.7; max-width: 280px; }

/* Columns */
.footer-heading {
  font-family: 'Inter', sans-serif;
  font-size: 0.78rem; font-weight: 700;
  text-transform: uppercase; letter-spacing: 0.08em;
  color: var(--gold-light); margin-bottom: 16px;
}
.footer-col ul { list-style: none; display: flex; flex-direction: column; gap: 10px; }
.footer-col li a {
  font-size: 0.9rem; color: rgba(255,255,255,0.65);
  transition: color var(--t);
}
.footer-col li a:hover { color: var(--white); }

.footer-bysonic {
  margin-top: 20px; font-size: 0.85rem;
  color: rgba(255,255,255,0.5); line-height: 1.6;
}
.footer-bysonic strong { color: var(--gold-light); }

/* Bottom bar */
.footer-bottom {
  display: flex; justify-content: space-between; align-items: center;
  padding: 20px 0; gap: 16px; flex-wrap: wrap;
}
.footer-copy { font-size: 0.8125rem; color: rgba(255,255,255,0.45); }
.footer-product { font-size: 0.8125rem; color: rgba(201,168,76,0.6); }

@media (max-width: 900px) {
  .footer-grid { grid-template-columns: 1fr 1fr; }
  .footer-brand { grid-column: 1 / -1; }
}
@media (max-width: 540px) {
  .footer-grid { grid-template-columns: 1fr; }
  .footer-bottom { flex-direction: column; align-items: flex-start; gap: 6px; }
}
</style>
