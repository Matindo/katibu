import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  // Public pages
  { path: '/',        name: 'Home',    component: () => import('../views/HomeView.vue') },
  { path: '/about',   name: 'About',   component: () => import('../views/AboutView.vue') },
  { path: '/license', name: 'License', component: () => import('../views/LicenseView.vue') },

  // Auth
  { path: '/login',    name: 'Login',    component: () => import('../views/auth/LoginView.vue') },
  { path: '/register', name: 'Register', component: () => import('../views/auth/RegisterView.vue') },

  // Authenticated
  { path: '/profile',  name: 'Profile',  component: () => import('../views/ProfileView.vue'),           meta: { requiresAuth: true } },
  { path: '/projects', name: 'Projects', component: () => import('../views/projects/ProjectsView.vue'), meta: { requiresAuth: true } },
  { path: '/projects/:id', name: 'ProjectDetail', component: () => import('../views/projects/ProjectView.vue'), meta: { requiresAuth: true } },
  { path: '/projects/:id/reports', name: 'Reports', component: () => import('../views/reports/ReportsView.vue'), meta: { requiresAuth: true } },

  // Public report (no auth)
  { path: '/public/:token', name: 'PublicReport', component: () => import('../views/public/PublicReportView.vue') },

  { path: '*', redirect: '/' }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  scrollBehavior: () => ({ x: 0, y: 0 }),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('katibu_token')
  if (to.meta.requiresAuth && !token) {
    next({ name: 'Login', query: { redirect: to.fullPath } })
  } else {
    next()
  }
})

export default router
