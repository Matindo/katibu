import axios from 'axios'
import store from '../store'

const api = axios.create({
  baseURL: process.env.VUE_APP_API_URL || 'http://localhost:8080'
})

api.interceptors.request.use(config => {
  const token = store.state.token
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

api.interceptors.response.use(
  response => {
    // 204 No Content (deletes)
    if (response.status === 204) return null
    const body = response.data
    // Unwrap ApiResponse envelope: return inner data, keep message accessible via promise chain
    if (body && typeof body === 'object' && 'success' in body) {
      // Attach message as a non-enumerable property so callers can read it if needed
      const result = body.data != null ? body.data : null
      if (result !== null && typeof result === 'object') {
        Object.defineProperty(result, '_message', { value: body.message, enumerable: false, writable: true })
      }
      return result
    }
    return body
  },
  error => {
    if (error.response?.status === 401) {
      store.dispatch('logout')
      window.location.href = '/login'
      return Promise.reject(new Error('Session expired. Please log in again.'))
    }
    if (error.response?.data) {
      const body = error.response.data
      const msg = (body && typeof body === 'object' && body.message) ? body.message : 'An unexpected error occurred'
      return Promise.reject(new Error(msg))
    }
    return Promise.reject(new Error('Network error. Please check your connection.'))
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
    financialPosition: (projectId, params) => api.get(`/projects/${projectId}/reports/financial-position`, { params }),
    generalLedger: (projectId, params) => api.get(`/projects/${projectId}/reports/general-ledger`, { params }),
    trialBalance: (projectId, params) => api.get(`/projects/${projectId}/reports/trial-balance`, { params }),
    balanceSheet: (projectId, params) => api.get(`/projects/${projectId}/reports/balance-sheet`, { params })
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
    info: token => api.get(`/public/${token}/info`),
    summary: (token, params) => api.get(`/public/${token}/summary`, { params }),
    receiptsPayments: (token, params) => api.get(`/public/${token}/receipts-payments`, { params }),
    cashFlow: (token, params) => api.get(`/public/${token}/cash-flow`, { params }),
    financialPosition: (token, params) => api.get(`/public/${token}/financial-position`, { params }),
    generalLedger: (token, params) => api.get(`/public/${token}/general-ledger`, { params }),
    trialBalance: (token, params) => api.get(`/public/${token}/trial-balance`, { params }),
    balanceSheet: (token, params) => api.get(`/public/${token}/balance-sheet`, { params })
  }
}
