import axios from 'axios'
import store from '../store'

const api = axios.create({
  baseURL: process.env.VUE_APP_API_URL || 'http://localhost:8080'
})

api.interceptors.request.use(config => {
  const token = store.state.token
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

api.interceptors.response.use(
  response => response.data,
  error => {
    if (error.response && error.response.status === 401) {
      store.dispatch('logout')
      window.location.href = '/login'
    }
    return Promise.reject(error.response ? error.response.data : error)
  }
)

export default {
  // Auth
  auth: {
    register: data => api.post('/auth/register', data),
    login: data => api.post('/auth/login', data)
  },

  // Projects
  projects: {
    list: () => api.get('/projects'),
    get: id => api.get(`/projects/${id}`),
    create: data => api.post('/projects', data),
    update: (id, data) => api.put(`/projects/${id}`, data),
    archive: id => api.post(`/projects/${id}/archive`),
    listMembers: id => api.get(`/projects/${id}/members`),
    addMember: (id, data) => api.post(`/projects/${id}/members`, data),
    removeMember: (id, userId) => api.delete(`/projects/${id}/members/${userId}`)
  },

  // Ledger
  ledger: {
    list: projectId => api.get(`/projects/${projectId}/entries`),
    get: (projectId, entryId) => api.get(`/projects/${projectId}/entries/${entryId}`),
    create: (projectId, data) => api.post(`/projects/${projectId}/entries`, data),
    update: (projectId, entryId, data) => api.put(`/projects/${projectId}/entries/${entryId}`, data),
    delete: (projectId, entryId) => api.delete(`/projects/${projectId}/entries/${entryId}`)
  },

  // Reports
  reports: {
    summary: (projectId, params) => api.get(`/projects/${projectId}/reports/summary`, { params }),
    receiptsPayments: (projectId, params) => api.get(`/projects/${projectId}/reports/receipts-payments`, { params }),
    cashFlow: (projectId, params) => api.get(`/projects/${projectId}/reports/cash-flow`, { params }),
    financialPosition: (projectId, params) => api.get(`/projects/${projectId}/reports/financial-position`, { params })
  },

  // Public links
  links: {
    generate: (projectId, data) => api.post(`/projects/${projectId}/links`, data),
    list: projectId => api.get(`/projects/${projectId}/links`),
    revoke: (projectId, linkId) => api.delete(`/projects/${projectId}/links/${linkId}`)
  },

  // Files
  files: {
    export: (projectId, data) => api.post(`/projects/${projectId}/files/export`, data),
    list: projectId => api.get(`/projects/${projectId}/files`),
    download: (projectId, fileId) => api.get(`/projects/${projectId}/files/${fileId}/download`, { responseType: 'blob' })
  },

  // User profile
  users: {
    me: () => api.get('/users/me'),
    update: data => api.put('/users/me', data),
    changePassword: data => api.put('/users/me/password', data)
  },

  // Public (no auth)
  public: {
    summary: (token, params) => api.get(`/public/${token}/summary`, { params }),
    receiptsPayments: (token, params) => api.get(`/public/${token}/receipts-payments`, { params }),
    cashFlow: (token, params) => api.get(`/public/${token}/cash-flow`, { params }),
    financialPosition: (token, params) => api.get(`/public/${token}/financial-position`, { params })
  }
}
