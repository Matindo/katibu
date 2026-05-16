import { register } from 'register-service-worker'

if (process.env.NODE_ENV === 'production') {
  register(`${process.env.BASE_URL}service-worker.js`, {
    ready () {},
    registered () {},
    cached () {},
    updatefound () {},
    updated (registration) {
      // Signal the app so it can prompt the user to reload for the new version.
      document.dispatchEvent(new CustomEvent('swUpdated', { detail: registration }))
    },
    offline () {},
    error (error) {
      console.error('Service worker registration error:', error)
    }
  })
}
