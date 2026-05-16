import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  // Auth
  { path: '/login', name: 'Login', component: () => import('../views/auth/LoginView.vue') },
  { path: '/register', name: 'Register', component: () => import('../views/auth/RegisterView.vue') },

  // Projects
  { path: '/', name: 'Projects', component: () => import('../views/projects/ProjectListView.vue'), meta: { requiresAuth: true } },
  { path: '/projects/new', name: 'CreateProject', component: () => import('../views/projects/CreateProjectView.vue'), meta: { requiresAuth: true } },
  { path: '/projects/:id', name: 'ProjectDetail', component: () => import('../views/projects/ProjectDetailView.vue'), meta: { requiresAuth: true } },

  // Ledger
  { path: '/projects/:id/ledger', name: 'Ledger', component: () => import('../views/ledger/LedgerView.vue'), meta: { requiresAuth: true } },
  { path: '/projects/:id/ledger/new', name: 'NewEntry', component: () => import('../views/ledger/EntryFormView.vue'), meta: { requiresAuth: true } },

  // Reports
  { path: '/projects/:id/reports', name: 'Reports', component: () => import('../views/reports/ReportsView.vue'), meta: { requiresAuth: true } },

  // Public
  { path: '/public/:token', name: 'PublicReport', component: () => import('../views/public/PublicReportView.vue') },

  { path: '*', redirect: '/' }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('katibu_token')
  if (to.meta.requiresAuth && !token) {
    next({ name: 'Login' })
  } else {
    next()
  }
})

export default router
