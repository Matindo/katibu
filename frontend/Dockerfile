# Build context is the frontend/ directory.
# docker-compose sets: build.context: frontend

# ---- Build Stage ----
FROM node:20-alpine AS builder
WORKDIR /app

# Install dependencies first (cached until package files change).
# --legacy-peer-deps is required because vue-jest@3 has peer dep conflicts with
# newer ajv/babel versions that npm ci treats as fatal; this is a known Vue 2 / Jest issue.
COPY package.json package-lock.json ./
RUN npm install --legacy-peer-deps --prefer-offline

# VUE_APP_* env vars are baked into the bundle at build time by Vue CLI
ARG VUE_APP_API_URL=https://api.katibu.my
ENV VUE_APP_API_URL=${VUE_APP_API_URL}

COPY . .
RUN npm run build

# ---- Runtime Stage ----
FROM nginx:stable-alpine
WORKDIR /usr/share/nginx/html

# Replace the default nginx config with ours (SPA routing + cache headers)
COPY nginx.conf /etc/nginx/conf.d/default.conf
RUN apk add --no-cache curl
COPY --from=builder /app/dist .

EXPOSE 80
