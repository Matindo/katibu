'use strict'

module.exports = {
  pwa: {
    name: 'Katibu',
    shortName: 'Katibu',
    themeColor: '#2D6A4F',
    msTileColor: '#2D6A4F',
    appleMobileWebAppCapable: 'yes',
    appleMobileWebAppStatusBarStyle: 'default',
    manifestOptions: {
      display: 'standalone',
      background_color: '#ffffff',
      start_url: '/',
      scope: '/',
      description: 'Financial records, accountability and reporting for teams and SMEs',
      icons: [
        { src: 'img/icons/android-chrome-192x192.png',         sizes: '192x192', type: 'image/png' },
        { src: 'img/icons/android-chrome-512x512.png',         sizes: '512x512', type: 'image/png' },
        { src: 'img/icons/android-chrome-maskable-192x192.png', sizes: '192x192', type: 'image/png', purpose: 'maskable' },
        { src: 'img/icons/android-chrome-maskable-512x512.png', sizes: '512x512', type: 'image/png', purpose: 'maskable' }
      ]
    },
    workboxOptions: {
      skipWaiting: true,
      clientsClaim: true
    }
  }
}
