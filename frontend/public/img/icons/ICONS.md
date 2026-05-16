# PWA Icons

Place the following icon files in this directory. All must be PNG.

| File | Size | Purpose |
|------|------|---------|
| `favicon-16x16.png` | 16×16 | Browser tab (small) |
| `favicon-32x32.png` | 32×32 | Browser tab |
| `android-chrome-192x192.png` | 192×192 | Android home screen / Chrome install prompt |
| `android-chrome-512x512.png` | 512×512 | Android splash screen / Chrome install prompt |
| `android-chrome-maskable-192x192.png` | 192×192 | Android adaptive icon (maskable) |
| `android-chrome-maskable-512x512.png` | 512×512 | Android adaptive icon (maskable) — required by Lighthouse |
| `apple-touch-icon-152x152.png` | 152×152 | iOS home screen icon |
| `msapplication-icon-144x144.png` | 144×144 | Windows tile |
| `mstile-150x150.png` | 150×150 | Windows Start menu |

Also place `favicon.ico` (multi-size: 16, 32, 48) in `frontend/public/`.

## Minimum for install prompt

Chrome's PWA install prompt requires at minimum:
- `android-chrome-192x192.png`
- `android-chrome-512x512.png`

## Generating icons

Use a tool like https://realfavicongenerator.net or https://maskable.app (for maskable variants)
with a single high-resolution source image (1024×1024 recommended).
