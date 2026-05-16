# Katibu Financial Management Platform

A localised financial records, accountability, and reporting platform for users, teams, groups, and SMEs. Modelled after cash-based accounting principles with support for both **IPSAS** (cash basis) and **IFRS** reporting standards.

---

## Features

- **Projects (Ledgers)** — Each project is an independent financial ledger with a defined time period.
- **Ledger Entries** — Record inflows (capital, donations, revenue, grants, loans, credits) and outflows (purchases, expenditures, loan repayments, debt payments, withdrawals).
- **Financial Reports** — Generate reports aligned with cash-based IPSAS and IFRS:
  - Statement of Receipts and Payments (IPSAS 2)
  - Cash Flow Statement (IFRS — Operating, Investing, Financing)
  - Statement of Financial Position
  - Summary Report
- **Public Links** — Share read-only reports via token-based URLs with optional expiry.
- **File Exports** — Download reports as PDF or CSV. Files are stored in MinIO.
- **Project Archiving** — Completed projects can be archived (read-only). Reports remain accessible.
- **Role-based Access** — Creator has full control. Added admins can record entries and generate reports/links.
- **PWA** — Installable on any device from the browser. Works offline for already-loaded views.

---

## Tech Stack

| Layer      | Technology                                         |
|------------|----------------------------------------------------|
| Backend    | Java 21, Spring Boot 3.3.x (monolith)              |
| Gateway    | Spring Cloud Gateway 2023.0.x                      |
| Database   | PostgreSQL (Flyway migrations)                     |
| File Store | MinIO                                              |
| PDF        | Apache PDFBox 3.x                                  |
| CSV        | Apache Commons CSV 1.11                            |
| Auth       | JWT (JJWT 0.12.x) + Spring Security               |
| Frontend   | Vue.js 2 (Options API), Vuex, Vue Router (PWA)     |

---

## Project Structure

```
katibu/
├── pom.xml                          # Parent Maven POM (modules: core, gateway)
├── .gitignore                       # Covers root, Java/Maven, Node.js/Vue, Spring env files
├── README.md
│
├── backend/
│   ├── core/                        # Main monolithic Spring Boot service (port 8081)
│   │   ├── pom.xml
│   │   └── src/
│   │       ├── main/
│   │       │   ├── java/com/katibu/
│   │       │   │   ├── KatibuApplication.java
│   │       │   │   ├── config/
│   │       │   │   │   ├── SecurityConfig.java       # JWT filter chain, CORS, password encoder; /actuator/health permitted without auth
│   │       │   │   │   └── MinioConfig.java          # MinIO client bean
│   │       │   │   ├── domain/
│   │       │   │   │   ├── entity/
│   │       │   │   │   │   ├── User.java
│   │       │   │   │   │   ├── Project.java
│   │       │   │   │   │   ├── ProjectMember.java
│   │       │   │   │   │   ├── LedgerEntry.java      # soft-delete via deletedAt
│   │       │   │   │   │   ├── PublicLink.java       # token-based public access
│   │       │   │   │   │   └── GeneratedFile.java    # MinIO-stored exports
│   │       │   │   │   └── enums/
│   │       │   │   │       ├── DurationType.java     # WEEKLY|MONTHLY|QUARTERLY|HALF_YEARLY|YEARLY|CUSTOM
│   │       │   │   │       ├── EntryType.java        # inflow/outflow classification + IFRS category
│   │       │   │   │       ├── ProjectStatus.java    # ACTIVE|ARCHIVED
│   │       │   │   │       ├── MemberRole.java       # ADMIN
│   │       │   │   │       ├── FileType.java         # PDF|CSV
│   │       │   │   │       └── ReportType.java       # SUMMARY|RECEIPTS_PAYMENTS|CASH_FLOW|FINANCIAL_POSITION|LEDGER
│   │       │   │   ├── repository/
│   │       │   │   │   ├── UserRepository.java
│   │       │   │   │   ├── ProjectRepository.java    # findAccessibleByUserId (creator OR member)
│   │       │   │   │   ├── ProjectMemberRepository.java
│   │       │   │   │   ├── LedgerEntryRepository.java # date-range + before-date queries
│   │       │   │   │   ├── PublicLinkRepository.java
│   │       │   │   │   └── GeneratedFileRepository.java
│   │       │   │   ├── security/
│   │       │   │   │   ├── JwtUtil.java              # generate/validate/extract JWT
│   │       │   │   │   ├── JwtAuthFilter.java        # OncePerRequestFilter
│   │       │   │   │   └── UserDetailsServiceImpl.java
│   │       │   │   ├── exception/
│   │       │   │   │   ├── ResourceNotFoundException.java  # → 404
│   │       │   │   │   ├── UnauthorizedException.java     # → 403
│   │       │   │   │   ├── BusinessException.java         # → 400
│   │       │   │   │   └── GlobalExceptionHandler.java
│   │       │   │   ├── dto/
│   │       │   │   │   ├── request/
│   │       │   │   │   │   ├── RegisterRequest.java
│   │       │   │   │   │   ├── LoginRequest.java
│   │       │   │   │   │   ├── UpdateProfileRequest.java
│   │       │   │   │   │   ├── ChangePasswordRequest.java
│   │       │   │   │   │   ├── CreateProjectRequest.java
│   │       │   │   │   │   ├── UpdateProjectRequest.java
│   │       │   │   │   │   ├── AddMemberRequest.java
│   │       │   │   │   │   ├── CreateEntryRequest.java
│   │       │   │   │   │   ├── UpdateEntryRequest.java
│   │       │   │   │   │   ├── ExportRequest.java
│   │       │   │   │   │   └── GenerateLinkRequest.java
│   │       │   │   │   └── response/
│   │       │   │   │       ├── ApiResponse.java      # {success, data, message}
│   │       │   │   │       ├── AuthResponse.java
│   │       │   │   │       ├── UserResponse.java
│   │       │   │   │       ├── ProjectResponse.java
│   │       │   │   │       ├── MemberResponse.java
│   │       │   │   │       ├── LedgerEntryResponse.java
│   │       │   │   │       ├── PublicLinkResponse.java
│   │       │   │   │       ├── GeneratedFileResponse.java
│   │       │   │   │       └── report/
│   │       │   │   │           ├── SummaryReport.java
│   │       │   │   │           ├── ReceiptsPaymentsReport.java
│   │       │   │   │           ├── CashFlowReport.java
│   │       │   │   │           └── FinancialPositionReport.java
│   │       │   │   ├── service/
│   │       │   │   │   ├── AuthService.java
│   │       │   │   │   ├── ProjectService.java       # access control + CRUD
│   │       │   │   │   ├── LedgerService.java        # entry CRUD, soft-delete
│   │       │   │   │   ├── ReportService.java        # all report calculations
│   │       │   │   │   ├── PublicLinkService.java    # token generation + resolution
│   │       │   │   │   ├── FileExportService.java    # PDF/CSV generation + MinIO storage
│   │       │   │   │   └── MinioService.java         # upload/download
│   │       │   │   └── controller/
│   │       │   │       ├── AuthController.java        # POST /auth/register|login
│   │       │   │       ├── UserController.java        # GET|PUT /users/me, PUT /users/me/password
│   │       │   │       ├── ProjectController.java     # /projects/**
│   │       │   │       ├── LedgerController.java      # /projects/{id}/entries/**
│   │       │   │       ├── ReportController.java      # /projects/{id}/reports/**
│   │       │   │       ├── PublicLinkController.java  # /projects/{id}/links/**
│   │       │   │       ├── PublicController.java      # /public/{token}/** (no auth)
│   │       │   │       └── FileController.java        # /projects/{id}/files/**
│   │       │   └── resources/
│   │       │       ├── application.yml               # DB, JWT, MinIO, Flyway; exposes only /actuator/health
│   │       │       └── db/migration/
│   │       │           └── V1__initial_schema.sql
│   │       └── test/java/com/katibu/
│   │           └── KatibuApplicationTests.java
│   │
│   └── gateway/                     # Spring Cloud Gateway (port 8080)
│       ├── pom.xml
│       └── src/
│           ├── main/
│           │   ├── java/com/katibu/gateway/
│           │   │   └── GatewayApplication.java
│           │   └── resources/
│           │       └── application.yml   # routes all /** → core on 8081; exposes only /actuator/health
│           └── test/java/com/katibu/gateway/
│               └── GatewayApplicationTests.java
│
├── frontend/                         # Vue.js 2 PWA (Options API, no Vite)
│   ├── Dockerfile                    # Multi-stage: node:20-alpine build → nginx:stable-alpine serve (curl installed for healthcheck)
│   ├── nginx.conf                    # SPA fallback + cache headers (copied into container)
│   ├── package.json
│   ├── vue.config.js                 # PWA plugin config (name, icons, workbox)
│   ├── jest.config.js                # Uses @vue/cli-plugin-unit-jest preset (jest 27)
│   ├── babel.config.js               # @babel/preset-env targeting current Node (required by jest)
│   ├── tests/
│   │   └── unit/                     # Jest unit tests (run: npm run test:unit)
│   │       ├── store.spec.js         # Vuex auth store
│   │       ├── api.spec.js           # Axios interceptor + envelope unwrap
│   │       ├── toast.spec.js         # Toast plugin state management
│   │       ├── AppToast.spec.js      # Toast UI component
│   │       ├── LoginView.spec.js     # Login form + auth flow
│   │       └── RegisterView.spec.js  # Register form + validation
│   ├── public/
│   │   ├── index.html                # PWA meta tags, manifest link
│   │   ├── favicon.ico
│   │   └── img/icons/               # PWA icons (see ICONS.md inside)
│   └── src/
│       ├── main.js
│       ├── registerServiceWorker.js  # Workbox service worker registration
│       ├── App.vue
│       ├── api/
│       │   └── index.js              # Axios client with JWT interceptor
│       ├── router/
│       │   └── index.js              # Vue Router with auth guards
│       ├── store/
│       │   └── index.js              # Vuex store (auth state)
│       ├── views/
│       │   ├── HomeView.vue          # Landing page
│       │   ├── AboutView.vue         # Feature tour with CSS mockups
│       │   ├── LicenseView.vue       # License / T&C / Privacy tabs
│       │   ├── ProfileView.vue       # Edit details + change password
│       │   ├── auth/                 # LoginView, RegisterView
│       │   ├── projects/             # ProjectsView, ProjectView (dashboard)
│       │   ├── reports/              # ReportsView
│       │   └── public/               # PublicReportView (no auth)
│       ├── plugins/
│       │   └── toast.js              # Vue.observable toast plugin ($toast.success/error/warning/info)
│       └── components/
│           ├── NavBar.vue            # Sticky glass navbar + user dropdown + mobile drawer
│           ├── AppFooter.vue         # Dark footer with links and Bysonic Inc. trademark
│           └── AppToast.vue          # Fixed top-right toast container (5s auto-dismiss, 4 types)
│
├── nginx/
│   ├── api.katibu.my.conf            # NPM reference (proxy host settings)
│   └── katibu.my.conf               # NPM reference (includes SPA routing snippet)
│
└── postman/
    └── Katibu.postman_collection.json  # Full API collection with tests
```

---

## API Reference

All responses follow the envelope: `{ "success": true|false, "data": {...}, "message": null|"..." }`

### Authentication

| Method | Path               | Auth | Description            |
|--------|--------------------|------|------------------------|
| POST   | `/auth/register`   | No   | Register a new account |
| POST   | `/auth/login`      | No   | Login, receive JWT     |

### User Profile

| Method | Path                  | Auth   | Description                                     |
|--------|-----------------------|--------|-------------------------------------------------|
| GET    | `/users/me`           | Bearer | Get authenticated user's profile                |
| PUT    | `/users/me`           | Bearer | Update full name and/or email                   |
| PUT    | `/users/me/password`  | Bearer | Change password (requires current password)     |

**Change password body:** `{ "currentPassword": "...", "newPassword": "..." }` (newPassword ≥ 8 chars)

### Projects

| Method | Path                                  | Auth    | Who       | Description                   |
|--------|---------------------------------------|---------|-----------|-------------------------------|
| POST   | `/projects`                           | Bearer  | Any user  | Create a project              |
| GET    | `/projects`                           | Bearer  | Any user  | List accessible projects      |
| GET    | `/projects/{id}`                      | Bearer  | Member+   | Get project details           |
| PUT    | `/projects/{id}`                      | Bearer  | Member+   | Update name/description       |
| POST   | `/projects/{id}/archive`              | Bearer  | Creator   | Archive the project           |
| GET    | `/projects/{id}/members`              | Bearer  | Member+   | List admin members            |
| POST   | `/projects/{id}/members`              | Bearer  | Creator   | Add admin member by email     |
| DELETE | `/projects/{id}/members/{userId}`     | Bearer  | Creator   | Remove admin member           |

**Duration types:** `WEEKLY`, `MONTHLY`, `QUARTERLY`, `HALF_YEARLY`, `YEARLY`, `CUSTOM`  
For fixed types, `endDate` is auto-calculated. `CUSTOM` requires explicit `endDate`.

### Ledger Entries

| Method | Path                                           | Auth   | Description                     |
|--------|------------------------------------------------|--------|---------------------------------|
| POST   | `/projects/{id}/entries`                       | Bearer | Add ledger entry                |
| GET    | `/projects/{id}/entries`                       | Bearer | List entries (date desc)        |
| GET    | `/projects/{id}/entries/{entryId}`             | Bearer | Get single entry                |
| PUT    | `/projects/{id}/entries/{entryId}`             | Bearer | Update entry                    |
| DELETE | `/projects/{id}/entries/{entryId}`             | Bearer | Soft-delete entry               |

**Entry types:**

| Type             | Direction | IFRS Category |
|------------------|-----------|---------------|
| INITIAL_CAPITAL  | Inflow    | Investing     |
| DONATION         | Inflow    | Operating     |
| REVENUE          | Inflow    | Operating     |
| GRANT            | Inflow    | Financing     |
| LOAN_RECEIVED    | Inflow    | Financing     |
| CREDIT           | Inflow    | Financing     |
| PURCHASE         | Outflow   | Investing     |
| EXPENDITURE      | Outflow   | Operating     |
| LOAN_REPAYMENT   | Outflow   | Financing     |
| DEBT_PAYMENT     | Outflow   | Financing     |
| WITHDRAWAL       | Outflow   | Financing     |

### Reports

All report endpoints require `startDate` and `endDate` query params (ISO date: `YYYY-MM-DD`).  
Financial Position uses `asAt` (single date).

| Method | Path                                            | Description                            |
|--------|-------------------------------------------------|----------------------------------------|
| GET    | `/projects/{id}/reports/summary`                | Summary: totals, opening/closing       |
| GET    | `/projects/{id}/reports/receipts-payments`      | IPSAS 2: receipts and payments by type |
| GET    | `/projects/{id}/reports/cash-flow`              | IFRS: operating/investing/financing    |
| GET    | `/projects/{id}/reports/financial-position`     | Balance sheet equivalent (`?asAt=`)    |

### Public Links

| Method | Path                                  | Auth   | Description          |
|--------|---------------------------------------|--------|----------------------|
| POST   | `/projects/{id}/links`                | Bearer | Generate public link |
| GET    | `/projects/{id}/links`                | Bearer | List links           |
| DELETE | `/projects/{id}/links/{linkId}`       | Bearer | Revoke link          |

### Public Access (No Auth)

| Method | Path                                         | Description                        |
|--------|----------------------------------------------|------------------------------------|
| GET    | `/public/{token}/summary`                    | Public summary report              |
| GET    | `/public/{token}/receipts-payments`          | Public receipts & payments report  |
| GET    | `/public/{token}/cash-flow`                  | Public cash flow statement         |
| GET    | `/public/{token}/financial-position`         | Public financial position          |

### File Exports

| Method | Path                                             | Description                    |
|--------|--------------------------------------------------|--------------------------------|
| POST   | `/projects/{id}/files/export`                    | Generate PDF or CSV report     |
| GET    | `/projects/{id}/files`                           | List generated files           |
| GET    | `/projects/{id}/files/{fileId}/download`         | Download file (binary)         |

**Export request body:** `{ "fileType": "PDF"|"CSV", "reportType": "SUMMARY"|"RECEIPTS_PAYMENTS"|"CASH_FLOW"|"FINANCIAL_POSITION"|"LEDGER", "startDate": "YYYY-MM-DD", "endDate": "YYYY-MM-DD" }`

---

## Running Locally

### Prerequisites

- Java 21
- Maven 3.9+
- PostgreSQL 15+
- MinIO (local or Docker)

### 1. Start Infrastructure

```bash
# PostgreSQL
psql -U postgres -c "CREATE DATABASE katibu; CREATE USER katibu WITH PASSWORD 'katibu'; GRANT ALL ON DATABASE katibu TO katibu;"

# MinIO (Docker)
docker run -p 9000:9000 -p 9001:9001 --name minio \
  -e MINIO_ROOT_USER=minioadmin \
  -e MINIO_ROOT_PASSWORD=minioadmin \
  minio/minio server /data --console-address ":9001"
```

### 2. Build

```bash
mvn clean install -DskipTests
```

### 3. Run Core Service

```bash
cd backend/core
mvn spring-boot:run
# Starts on port 8081
```

### 4. Run Gateway

```bash
cd backend/gateway
mvn spring-boot:run
# Starts on port 8080, proxies all requests to 8081
```

All client requests go to **port 8080** (gateway).

### Environment Variables

| Variable            | Default                                   | Description                          |
|---------------------|-------------------------------------------|--------------------------------------|
| `DB_URL`            | `jdbc:postgresql://localhost:5432/katibu` | PostgreSQL JDBC URL                  |
| `DB_USERNAME`       | `katibu`                                  | Database user                        |
| `DB_PASSWORD`       | `katibu`                                  | Database password                    |
| `JWT_SECRET`        | *(dev default)*                           | **Change in production** (≥32 chars) |
| `JWT_EXPIRATION_MS` | `86400000` (24h)                          | Token validity                       |
| `MINIO_ENDPOINT`    | `http://localhost:9000`                   | MinIO server URL                     |
| `MINIO_ACCESS_KEY`  | `minioadmin`                              | MinIO access key                     |
| `MINIO_SECRET_KEY`  | `minioadmin`                              | MinIO secret key                     |
| `MINIO_BUCKET`      | `katibu-files`                            | MinIO bucket name (auto-created)     |
| `PUBLIC_BASE_URL`   | `http://localhost:8080`                   | Base URL embedded in public link URLs |
| `CORE_SERVICE_URL`  | `http://localhost:8081`                   | Gateway → Core routing               |
| `ALLOWED_ORIGINS`   | `*`                                       | Comma-separated CORS origins (gateway) |
| `VUE_APP_API_URL`   | `https://api.katibu.my`                   | API base URL baked into the frontend bundle at build time |

---

## Deployment (Docker + VPS)

### Production domains

| Domain           | Purpose              |
|------------------|----------------------|
| `katibu.my`      | Frontend (Vue.js PWA)|
| `api.katibu.my`  | API gateway          |

### Architecture

```
Internet
  │
  ▼
Nginx Proxy Manager (TLS termination)
  ├── api.katibu.my  ──▶  127.0.0.1:8080  ──▶  [gateway container :8080]
  │                                                      │
  │                                                      ▼
  │                                            [core container :8081]
  │                                            [postgres container]
  │                                            [minio container]
  │
  └── katibu.my  ──▶  127.0.0.1:3000  ──▶  [frontend container :80]
                                             (nginx:stable-alpine + Vue dist)
```

Both the gateway (`:8080`) and frontend (`:3000`) bind to `127.0.0.1` only — nothing is directly reachable from the internet. Nginx Proxy Manager handles TLS and reverse proxying for both.

### 1. VPS prerequisites

```bash
# Docker
curl -fsSL https://get.docker.com | sh
systemctl enable --now docker
```

Nginx Proxy Manager (NPM) should already be running on your VPS. If not, see the NPM docs for its own Docker Compose setup.

### 2. DNS

Point both `api.katibu.my` and `katibu.my` A records to your VPS IP.

### 3. Environment variables

```bash
cp .env.example .env
# Edit .env with your real values
nano .env
```

Generate a secure JWT secret:
```bash
openssl rand -hex 32
```

Key production values:
```
PUBLIC_BASE_URL=https://api.katibu.my
ALLOWED_ORIGINS=https://katibu.my,https://www.katibu.my
VUE_APP_API_URL=https://api.katibu.my
```

> **Important:** `VUE_APP_API_URL` is baked into the Vue.js bundle at build time by Vue CLI. It must be set in `.env` **before** running `docker compose up --build`. Changing it after the build requires a rebuild of the `frontend` image.

### 4. Build and start all containers

```bash
docker compose up -d --build
```

Check that everything came up:
```bash
docker compose ps
docker compose logs -f
```

### 5. Configure Nginx Proxy Manager

**api.katibu.my** (proxy host):
- Forward Hostname / IP: `127.0.0.1`
- Forward Port: `8080`
- Scheme: `http`
- Enable SSL via Let's Encrypt in the SSL tab

**katibu.my** (proxy host → frontend container):
- Forward Hostname / IP: `127.0.0.1`
- Forward Port: `3000`
- Scheme: `http`
- Enable SSL via Let's Encrypt in the SSL tab
- In the **Advanced** tab, add this to the Custom Nginx Configuration field:

```nginx
location / {
    try_files $uri $uri/ /index.html;
}
```

This is a safety net for NPM's proxy layer. The frontend container's internal nginx already includes this directive, so Vue Router deep links work regardless.

### 6. Verify

```bash
# Gateway health (Spring Actuator)
curl https://api.katibu.my/actuator/health
# → {"status":"UP"}

# Core health via gateway (same endpoint, proxied through)
# The gateway's own /actuator/health responds directly — the core's endpoint is at :8081 internally
docker exec katibu-core curl -sf http://localhost:8081/actuator/health
# → {"status":"UP"}

# Frontend is up
curl -s -o /dev/null -w "%{http_code}" https://katibu.my/
# → 200

# SPA routing works (deep link must return 200, not 404)
curl -s -o /dev/null -w "%{http_code}" https://katibu.my/projects/test
# → 200

# Check all container health statuses
docker compose ps
```

### Useful operations

```bash
# View logs for one service
docker compose logs -f core

# Restart a single service after config change
docker compose restart gateway

# Pull latest images and rebuild
docker compose pull && docker compose up -d --build

# Database backup
docker exec katibu-postgres pg_dump -U katibu katibu > katibu_$(date +%Y%m%d).sql

# MinIO console (access via SSH tunnel from your local machine)
# ssh -L 9001:localhost:9001 user@your-vps  then open http://localhost:9001
```

---

## PWA Setup

The frontend is configured as a Progressive Web App. Users will see the browser's install prompt when visiting `katibu.my` on a supported device.

**Requirements for install prompt:**
- Site must be served over HTTPS (handled by NPM)
- `manifest.json` must reference at least a 192×192 and a 512×512 icon
- A service worker must be registered (handled by `registerServiceWorker.js`)

**Icon files required** — place in `frontend/public/img/icons/`:

| File | Size |
|------|------|
| `android-chrome-192x192.png` | 192×192 |
| `android-chrome-512x512.png` | 512×512 |
| `android-chrome-maskable-192x192.png` | 192×192 (maskable) |
| `android-chrome-maskable-512x512.png` | 512×512 (maskable) |
| `apple-touch-icon-152x152.png` | 152×152 |
| `favicon-32x32.png` | 32×32 |
| `favicon-16x16.png` | 16×16 |
| `msapplication-icon-144x144.png` | 144×144 |
| `mstile-150x150.png` | 150×150 |

Also place `favicon.ico` in `frontend/public/`. Use a tool such as [realfavicongenerator.net](https://realfavicongenerator.net) with a 1024×1024 source image.

---

## Accounting Standards

### Cash-Based IPSAS (IPSAS 2)
The **Statement of Receipts and Payments** records transactions when cash is actually received or paid. No accruals. Opening balance plus net receipts/payments equals closing balance.

### IFRS Cash Flow Classification
For the **Cash Flow Statement**, entries are categorised:
- **Operating** — day-to-day: revenue, donations, expenditure
- **Investing** — capital items: initial capital, asset purchases
- **Financing** — funding: loans, grants, repayments, withdrawals

### Financial Position
The **Statement of Financial Position** shows cash on hand as the primary asset, outstanding loan liabilities, and net assets represented by initial capital plus accumulated surplus/deficit.

---

## Database Schema

Managed by Flyway. Migration files in `backend/core/src/main/resources/db/migration/`.

| Table              | Purpose                                         |
|--------------------|-------------------------------------------------|
| `users`            | Registered accounts                            |
| `projects`         | Ledger containers with duration and status      |
| `project_members`  | Admin members added by creator                  |
| `ledger_entries`   | All financial transactions (soft-deleted)       |
| `public_links`     | Token-based shareable report links              |
| `generated_files`  | Metadata for PDF/CSV exports stored in MinIO    |

---

## CI / CD

Defined in `.github/workflows/ci.yml`. Triggers on every push and pull request to `main`.

### Pipeline stages

| Stage | Trigger | What it does |
|-------|---------|--------------|
| `test-frontend` | push + PR | Installs Node 20, runs `npm run test:unit -- --ci --coverage`, uploads coverage artifact |
| `test-backend` | push + PR | Starts a Postgres 15 service container, runs `mvn -B test` with real DB |
| `build-push` | push to `main` only | Builds and pushes `frontend`, `core`, and `gateway` Docker images **in parallel** to Docker Hub |

### Docker images

Images are pushed to Docker Hub as `<DOCKER_USERNAME>/katibu-<service>` with two tags: `latest` and `sha-<commit-sha>`.

Required repository secrets (`Settings → Secrets and variables → Actions`):

| Secret | Value |
|--------|-------|
| `DOCKER_USERNAME` | Docker Hub username |
| `DOCKER_PASSWORD` | Docker Hub password or access token |

### Running frontend tests locally

```bash
cd frontend
npm install --legacy-peer-deps
npm run test:unit
# With coverage:
npm run test:unit -- --coverage
```

The test suite uses `vue-cli-service test:unit` which bundles jest 27 (compatible with `jest-environment-jsdom@27`). Do not invoke `jest` directly as it picks up the standalone `jest@29` entry and causes environment version mismatches.

---

## Postman Collection

Located at `postman/Katibu.postman_collection.json`. Import into Postman.

**Collection variables to set before running:**
- `base_url` — `http://localhost:8080`

Run requests in order: Register → Login (sets `auth_token` automatically) → Create Project (sets `project_id`) → subsequent requests use stored variables.

All tests assert HTTP status, response time < 500ms, and data correctness.

---

## Architecture Notes

- **Monolith-first**: The single core service contains all business logic. The gateway is positioned now so the routing layer exists before microservice extraction begins.
- **Gateway role**: Routes all requests to the core service. CORS and cross-origin policy are enforced exclusively at the gateway via `CorsWebFilter`. The allowed origins are controlled by the `ALLOWED_ORIGINS` env var (`*` in dev, `https://katibu.my` in production). When services are split, only the gateway routing config changes.
- **TLS boundary**: Nginx Proxy Manager on the host terminates TLS. The gateway and core communicate over plain HTTP on the internal Docker network. The gateway is bound to `127.0.0.1:8080` and is not reachable from the internet directly.
- **Soft deletes**: Ledger entries are never hard-deleted — `deleted_at` timestamp is set instead. This maintains a full audit trail per cash-based accounting requirements.
- **No accruals**: All calculations are strictly cash-based. An entry only exists if cash was received or paid.
- **Public links**: Tokens are stored in the database and validated on every request. Revocation is immediate. Optional expiry is checked server-side.
- **Health probes**: Both core and gateway include `spring-boot-starter-actuator`. Only the `/actuator/health` endpoint is exposed (`show-details: never`). Docker Compose healthchecks hit this endpoint on each container's local port. The endpoint is permitted without authentication in `SecurityConfig`.
- **Frontend logo**: All logo instances in the Vue frontend (`NavBar`, `AppFooter`, auth pages, public report view) use the actual logo files from `frontend/src/assets/images/` via `<img>` tags processed by webpack — `high-resolution-color-logo.png` for light backgrounds, `high-resolution-logo-grayscale.png` for the dark footer.
- **Toast notifications**: All user-facing feedback (success, error, warning, info) goes through the `$toast` plugin (`frontend/src/plugins/toast.js`). Toasts appear top-right, auto-dismiss after 5 seconds, and are colour-coded by type. The `AppToast` component reads from a `Vue.observable` singleton — no event bus required.
- **API envelope**: All Spring REST endpoints return `{ "success": true|false, "data": T, "message": "..." }`. The Axios response interceptor in `frontend/src/api/index.js` automatically unwraps the envelope so callers receive `body.data` directly.
