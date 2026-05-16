'use strict'

module.exports = {
  pwa: {
    name: 'Katibu',
    shortName: 'Katibu',
    themeColor: '#1565C0',
    msTileColor: '#ffffff',
    appleMobileWebAppCapable: 'yes',
    appleMobileWebAppStatusBarStyle: 'default',
    manifestOptions: {
      display: 'standalone',
      background_color: '#ffffff',
      start_url: '/',
      scope: '/',
      description: 'Financial records, accountability and reporting for teams and SMEs'
    },
    workboxOptions: {
      // New service worker takes over immediately without waiting for all tabs to close.
      skipWaiting: true,
      clientsClaim: true
    }
  }
}
