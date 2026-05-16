/**
 * Tests for the Axios response interceptor in api/index.js.
 * We mock axios directly to control what the interceptor receives.
 */

// Mock store before importing api
jest.mock('@/store/index', () => ({
  state: { token: null },
  dispatch: jest.fn()
}))

// Capture the interceptors registered by api/index.js
let successInterceptor
let errorInterceptor

jest.mock('axios', () => {
  const instance = {
    interceptors: {
      request: { use: jest.fn() },
      response: {
        use: jest.fn((onSuccess, onError) => {
          successInterceptor = onSuccess
          errorInterceptor = onError
        })
      }
    },
    get: jest.fn(),
    post: jest.fn(),
    put: jest.fn(),
    delete: jest.fn()
  }
  return { create: jest.fn(() => instance) }
})

// Import api so the interceptors are registered
require('@/api/index')

describe('api response interceptor', () => {
  describe('success path', () => {
    it('unwraps the ApiResponse envelope and returns data', () => {
      const response = {
        status: 200,
        data: { success: true, data: { id: '1', name: 'Test' }, message: null }
      }
      const result = successInterceptor(response)
      expect(result).toEqual({ id: '1', name: 'Test' })
    })

    it('returns null for 204 No Content', () => {
      const response = { status: 204, data: '' }
      expect(successInterceptor(response)).toBeNull()
    })

    it('returns null when envelope data field is null', () => {
      const response = {
        status: 200,
        data: { success: true, data: null, message: 'Password updated successfully' }
      }
      expect(successInterceptor(response)).toBeNull()
    })

    it('returns array data when the envelope wraps a list', () => {
      const list = [{ id: '1' }, { id: '2' }]
      const response = {
        status: 200,
        data: { success: true, data: list, message: null }
      }
      expect(successInterceptor(response)).toEqual(list)
    })
  })

  describe('error path', () => {
    it('rejects with the API message from a 400 response', async () => {
      const error = {
        response: {
          status: 400,
          data: { success: false, message: 'Email already registered' }
        }
      }
      await expect(errorInterceptor(error)).rejects.toThrow('Email already registered')
    })

    it('rejects with a network error message when there is no response', async () => {
      const error = { response: null, message: 'Network Error' }
      await expect(errorInterceptor(error)).rejects.toThrow('Network error. Please check your connection.')
    })

    it('rejects with session expired message on 401', async () => {
      const error = { response: { status: 401, data: {} } }
      await expect(errorInterceptor(error)).rejects.toThrow('Session expired. Please log in again.')
    })
  })
})
