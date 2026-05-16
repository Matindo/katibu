<template>
  <nav class="navbar glass">
    <div class="container nav-inner">

      <!-- Logo -->
      <router-link to="/" class="nav-logo">
        <svg class="logo-icon" viewBox="0 0 36 36" fill="none" xmlns="http://www.w3.org/2000/svg">
          <circle cx="18" cy="18" r="18" fill="#2D6A4F"/>
          <path d="M18 9C18 9 26 11.5 27 18C28 24.5 18 27 18 27C18 27 8 24.5 9 18C10 11.5 18 9 18 9Z" fill="#74C69D" opacity="0.7"/>
          <line x1="18" y1="14" x2="18" y2="27" stroke="white" stroke-width="1.8" stroke-linecap="round"/>
          <line x1="18" y1="19" x2="13" y2="15.5" stroke="white" stroke-width="1.8" stroke-linecap="round"/>
          <line x1="18" y1="23" x2="23" y2="19.5" stroke="white" stroke-width="1.8" stroke-linecap="round"/>
        </svg>
        <span class="logo-text">Katibu</span>
      </router-link>

      <!-- Desktop links -->
      <div class="nav-links hide-mobile">
        <router-link to="/" exact class="nav-link">Home</router-link>
        <router-link to="/about" class="nav-link">About</router-link>
        <router-link v-if="isAuthenticated" to="/projects" class="nav-link">Projects</router-link>
        <router-link to="/license" class="nav-link">License</router-link>
      </div>

      <!-- Right: auth area + mobile burger -->
      <div class="nav-right">
        <template v-if="!isAuthenticated">
          <router-link to="/register" class="btn btn-outline btn-sm hide-mobile">Register</router-link>
          <router-link to="/login" class="btn btn-primary btn-sm">Login</router-link>
        </template>

        <div v-else class="user-menu" ref="userMenu">
          <button class="user-trigger" @click.stop="toggleUser" aria-label="Account menu">
            <div class="user-avatar">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <circle cx="12" cy="8" r="4"/>
                <path d="M4 20c0-4 3.6-7 8-7s8 3 8 7"/>
              </svg>
            </div>
            <span class="user-name hide-mobile">{{ displayName }}</span>
            <svg class="chevron" :class="{ open: userOpen }" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round">
              <path d="M6 9l6 6 6-6"/>
            </svg>
          </button>

          <transition name="fade">
            <div v-if="userOpen" class="dropdown glass">
              <div class="dd-user-name">{{ displayName }}</div>
              <div class="dd-user-email">{{ userEmail }}</div>
              <div class="divider" style="margin:10px 0"></div>
              <router-link to="/profile" class="dd-item" @click.native="closeUser">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="8" r="4"/><path d="M4 20c0-4 3.6-7 8-7s8 3 8 7"/></svg>
                Profile
              </router-link>
              <router-link to="/projects" class="dd-item" @click.native="closeUser">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M3 7a2 2 0 0 1 2-2h3l2 2h8a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V7Z"/></svg>
                My Projects
              </router-link>
              <div class="divider" style="margin:8px 0"></div>
              <button class="dd-item danger" @click="doLogout">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/></svg>
                Logout
              </button>
            </div>
          </transition>
        </div>

        <!-- Mobile burger -->
        <button class="burger" @click.stop="toggleMobile" aria-label="Menu">
          <span :class="{ open: mobileOpen }"></span>
          <span :class="{ open: mobileOpen }"></span>
          <span :class="{ open: mobileOpen }"></span>
        </button>
      </div>
    </div>

    <!-- Mobile nav drawer -->
    <transition name="slide">
      <div v-if="mobileOpen" class="mobile-drawer glass">
        <router-link to="/" exact class="mobile-link" @click.native="closeMobile">Home</router-link>
        <router-link to="/about" class="mobile-link" @click.native="closeMobile">About</router-link>
        <router-link v-if="isAuthenticated" to="/projects" class="mobile-link" @click.native="closeMobile">Projects</router-link>
        <router-link to="/license" class="mobile-link" @click.native="closeMobile">License</router-link>
        <div class="mobile-divider"></div>
        <template v-if="!isAuthenticated">
          <router-link to="/register" class="mobile-link" @click.native="closeMobile">Register</router-link>
          <router-link to="/login" class="mobile-link" @click.native="closeMobile">Login</router-link>
        </template>
        <template v-else>
          <router-link to="/profile" class="mobile-link" @click.native="closeMobile">Profile</router-link>
          <button class="mobile-link danger" @click="doLogout">Logout</button>
        </template>
      </div>
    </transition>
  </nav>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'NavBar',
  data: () => ({ userOpen: false, mobileOpen: false }),
  computed: {
    ...mapGetters(['isAuthenticated', 'currentUser']),
    displayName () { return this.currentUser?.fullName || 'Account' },
    userEmail () { return this.currentUser?.email || '' }
  },
  mounted () { document.addEventListener('click', this.handleOutside) },
  beforeDestroy () { document.removeEventListener('click', this.handleOutside) },
  watch: {
    $route () { this.closeAll() }
  },
  methods: {
    toggleUser () { this.userOpen = !this.userOpen; this.mobileOpen = false },
    toggleMobile () { this.mobileOpen = !this.mobileOpen; this.userOpen = false },
    closeUser () { this.userOpen = false },
    closeMobile () { this.mobileOpen = false },
    closeAll () { this.userOpen = false; this.mobileOpen = false },
    handleOutside (e) {
      if (this.$refs.userMenu && !this.$refs.userMenu.contains(e.target)) this.userOpen = false
      if (!this.$el.contains(e.target)) this.mobileOpen = false
    },
    doLogout () {
      this.closeAll()
      this.$store.dispatch('logout')
      this.$router.push('/')
    }
  }
}
</script>

<style scoped>
.navbar {
  position: fixed; top: 0; left: 0; right: 0;
  height: var(--nav-h); z-index: 100;
  box-shadow: 0 2px 20px rgba(45,106,79,0.08);
}
.nav-inner {
  display: flex; align-items: center; justify-content: space-between; height: 100%;
}

/* Logo */
.nav-logo { display: flex; align-items: center; gap: 10px; flex-shrink: 0; }
.logo-icon { width: 36px; height: 36px; }
.logo-text {
  font-family: 'Playfair Display', serif; font-size: 1.4rem; font-weight: 700;
  color: var(--green-deep); letter-spacing: -0.02em;
}

/* Desktop links */
.nav-links { display: flex; align-items: center; gap: 28px; }
.nav-link {
  font-size: 0.9375rem; font-weight: 500; color: var(--text-mid);
  transition: color var(--t); position: relative; padding-bottom: 2px;
}
.nav-link::after {
  content: ''; position: absolute; bottom: -2px; left: 0; right: 0;
  height: 2px; background: var(--gold); border-radius: 1px;
  transform: scaleX(0); transition: transform var(--t); transform-origin: left;
}
.nav-link:hover { color: var(--green-deep); }
.nav-link.router-link-active { color: var(--green-deep); }
.nav-link.router-link-active::after { transform: scaleX(1); }

/* Right */
.nav-right { display: flex; align-items: center; gap: 12px; }

/* User menu */
.user-menu { position: relative; }
.user-trigger {
  display: flex; align-items: center; gap: 8px;
  background: none; border: none; padding: 6px 8px; border-radius: var(--r-md);
  cursor: pointer; transition: background var(--t);
}
.user-trigger:hover { background: var(--green-tint); }
.user-avatar {
  width: 34px; height: 34px; border-radius: 50%;
  background: var(--green-pale); display: flex; align-items: center; justify-content: center;
  color: var(--green-deep); flex-shrink: 0;
}
.user-avatar svg { width: 18px; height: 18px; }
.user-name { font-size: 0.9rem; font-weight: 600; color: var(--text-dark); }
.chevron { width: 16px; height: 16px; color: var(--text-light); transition: transform var(--t); }
.chevron.open { transform: rotate(180deg); }

/* Dropdown */
.dropdown {
  position: absolute; top: calc(100% + 10px); right: 0;
  min-width: 224px; border-radius: var(--r-md);
  padding: 14px; box-shadow: var(--sh-md); z-index: 200;
}
.dd-user-name { font-weight: 600; font-size: 0.9375rem; color: var(--text-dark); }
.dd-user-email { font-size: 0.8125rem; color: var(--text-light); margin-top: 2px; }
.dd-item {
  display: flex; align-items: center; gap: 10px; width: 100%;
  padding: 9px 10px; border-radius: var(--r-sm);
  font-size: 0.9rem; font-weight: 500; color: var(--text-mid);
  background: none; border: none; cursor: pointer; text-align: left;
  transition: background var(--t), color var(--t);
}
.dd-item svg { width: 16px; height: 16px; flex-shrink: 0; }
.dd-item:hover { background: var(--green-tint); color: var(--green-deep); }
.dd-item.danger:hover { background: #FEE8E8; color: #C0392B; }

/* Burger */
.burger {
  display: none; flex-direction: column; justify-content: center;
  gap: 5px; padding: 8px; background: none; border: none;
  cursor: pointer; border-radius: var(--r-sm);
}
.burger span {
  display: block; width: 22px; height: 2px;
  background: var(--text-mid); border-radius: 1px;
  transition: all 0.22s ease; transform-origin: center;
}
.burger span:nth-child(1).open { transform: translateY(7px) rotate(45deg); }
.burger span:nth-child(2).open { opacity: 0; transform: scaleX(0); }
.burger span:nth-child(3).open { transform: translateY(-7px) rotate(-45deg); }

/* Mobile drawer */
.mobile-drawer {
  position: absolute; top: var(--nav-h); left: 0; right: 0;
  padding: 16px 24px 20px; box-shadow: var(--sh-md);
  border-top: 1px solid var(--border-sub);
}
.mobile-link {
  display: block; padding: 12px 4px; font-size: 1rem;
  font-weight: 500; color: var(--text-mid); border-bottom: 1px solid var(--border-sub);
  background: none; border-left: none; border-right: none; border-top: none;
  width: 100%; text-align: left; cursor: pointer;
  transition: color var(--t);
}
.mobile-link:last-child { border-bottom: none; }
.mobile-link:hover { color: var(--green-deep); }
.mobile-link.danger:hover { color: #C0392B; }
.mobile-divider { height: 1px; background: var(--border-sub); margin: 6px 0; }

@media (max-width: 768px) {
  .burger { display: flex; }
  .nav-links { display: none; }
}
</style>
