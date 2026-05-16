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
| Frontend   | Vue.js 2 (Options API), Vuex, Vue Router (scaffold)|

---

## Project Structure

```
katibu/
├── pom.xml                          # Parent Maven POM (modules: core, gateway)
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
│   │       │   │   │   ├── SecurityConfig.java       # JWT filter chain, CORS, password encoder
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
│   │       │   │       ├── AuthController.java        # POST /api/auth/register|login
│   │       │   │       ├── ProjectController.java     # /api/projects/**
│   │       │   │       ├── LedgerController.java      # /api/projects/{id}/entries/**
│   │       │   │       ├── ReportController.java      # /api/projects/{id}/reports/**
│   │       │   │       ├── PublicLinkController.java  # /api/projects/{id}/links/**
│   │       │   │       ├── PublicController.java      # /api/public/{token}/** (no auth)
│   │       │   │       └── FileController.java        # /api/projects/{id}/files/**
│   │       │   └── resources/
│   │       │       ├── application.yml
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
│           │       └── application.yml   # routes all /api/** → core on 8081
│           └── test/java/com/katibu/gateway/
│               └── GatewayApplicationTests.java
│
├── frontend/                         # Vue.js 2 scaffolding (Options API, no Vite)
│   ├── package.json
│   ├── index.html
│   ├── jest.config.js
│   └── src/
│       ├── main.js
│       ├── App.vue
│       ├── api/
│       │   └── index.js              # Axios client with JWT interceptor
│       ├── router/
│       │   └── index.js              # Vue Router with auth guards
│       ├── store/
│       │   └── index.js              # Vuex store (auth state)
│       ├── views/
│       │   ├── auth/                 # LoginView, RegisterView
│       │   ├── projects/             # ProjectListView, CreateProjectView, ProjectDetailView
│       │   ├── ledger/               # LedgerView, EntryFormView
│       │   ├── reports/              # ReportsView
│       │   └── public/               # PublicReportView
│       └── components/               # Shared components (to be built)
│
└── postman/
    └── Katibu.postman_collection.json  # Full API collection with tests
```

---

## API Reference

All responses follow the envelope: `{ "success": true|false, "data": {...}, "message": null|"..." }`

### Authentication

| Method | Path                  | Auth | Description            |
|--------|-----------------------|------|------------------------|
| POST   | `/api/auth/register`  | No   | Register a new account |
| POST   | `/api/auth/login`     | No   | Login, receive JWT     |

### Projects

| Method | Path                                     | Auth    | Who       | Description                   |
|--------|------------------------------------------|---------|-----------|-------------------------------|
| POST   | `/api/projects`                          | Bearer  | Any user  | Create a project              |
| GET    | `/api/projects`                          | Bearer  | Any user  | List accessible projects      |
| GET    | `/api/projects/{id}`                     | Bearer  | Member+   | Get project details           |
| PUT    | `/api/projects/{id}`                     | Bearer  | Member+   | Update name/description       |
| POST   | `/api/projects/{id}/archive`             | Bearer  | Creator   | Archive the project           |
| GET    | `/api/projects/{id}/members`             | Bearer  | Member+   | List admin members            |
| POST   | `/api/projects/{id}/members`             | Bearer  | Creator   | Add admin member by email     |
| DELETE | `/api/projects/{id}/members/{userId}`    | Bearer  | Creator   | Remove admin member           |

**Duration types:** `WEEKLY`, `MONTHLY`, `QUARTERLY`, `HALF_YEARLY`, `YEARLY`, `CUSTOM`  
For fixed types, `endDate` is auto-calculated. `CUSTOM` requires explicit `endDate`.

### Ledger Entries

| Method | Path                                              | Auth   | Description                     |
|--------|---------------------------------------------------|--------|---------------------------------|
| POST   | `/api/projects/{id}/entries`                      | Bearer | Add ledger entry                |
| GET    | `/api/projects/{id}/entries`                      | Bearer | List entries (date desc)        |
| GET    | `/api/projects/{id}/entries/{entryId}`            | Bearer | Get single entry                |
| PUT    | `/api/projects/{id}/entries/{entryId}`            | Bearer | Update entry                    |
| DELETE | `/api/projects/{id}/entries/{entryId}`            | Bearer | Soft-delete entry               |

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

| Method | Path                                                 | Description                            |
|--------|------------------------------------------------------|----------------------------------------|
| GET    | `/api/projects/{id}/reports/summary`                 | Summary: totals, opening/closing       |
| GET    | `/api/projects/{id}/reports/receipts-payments`       | IPSAS 2: receipts and payments by type |
| GET    | `/api/projects/{id}/reports/cash-flow`               | IFRS: operating/investing/financing    |
| GET    | `/api/projects/{id}/reports/financial-position`      | Balance sheet equivalent (`?asAt=`)    |

### Public Links

| Method | Path                                         | Auth   | Description          |
|--------|----------------------------------------------|--------|----------------------|
| POST   | `/api/projects/{id}/links`                   | Bearer | Generate public link |
| GET    | `/api/projects/{id}/links`                   | Bearer | List links           |
| DELETE | `/api/projects/{id}/links/{linkId}`          | Bearer | Revoke link          |

### Public Access (No Auth)

| Method | Path                                              | Description                        |
|--------|---------------------------------------------------|------------------------------------|
| GET    | `/api/public/{token}/summary`                     | Public summary report              |
| GET    | `/api/public/{token}/receipts-payments`           | Public receipts & payments report  |
| GET    | `/api/public/{token}/cash-flow`                   | Public cash flow statement         |
| GET    | `/api/public/{token}/financial-position`          | Public financial position          |

### File Exports

| Method | Path                                                  | Description                    |
|--------|-------------------------------------------------------|--------------------------------|
| POST   | `/api/projects/{id}/files/export`                     | Generate PDF or CSV report     |
| GET    | `/api/projects/{id}/files`                            | List generated files           |
| GET    | `/api/projects/{id}/files/{fileId}/download`          | Download file (binary)         |

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
# Starts on port 8080, proxies all /api/** to 8081
```

All client requests go to **port 8080** (gateway).

### Environment Variables

| Variable            | Default                            | Description                      |
|---------------------|------------------------------------|----------------------------------|
| `DB_URL`            | `jdbc:postgresql://localhost:5432/katibu` | PostgreSQL JDBC URL         |
| `DB_USERNAME`       | `katibu`                           | Database user                    |
| `DB_PASSWORD`       | `katibu`                           | Database password                |
| `JWT_SECRET`        | *(dev default)*                    | **Change in production** (≥32 chars) |
| `JWT_EXPIRATION_MS` | `86400000` (24h)                   | Token validity                   |
| `MINIO_ENDPOINT`    | `http://localhost:9000`            | MinIO server URL                 |
| `MINIO_ACCESS_KEY`  | `minioadmin`                       | MinIO access key                 |
| `MINIO_SECRET_KEY`  | `minioadmin`                       | MinIO secret key                 |
| `MINIO_BUCKET`      | `katibu-files`                     | MinIO bucket name (auto-created) |
| `PUBLIC_BASE_URL`   | `http://localhost:8080`            | Base URL for public link URLs    |
| `CORE_SERVICE_URL`  | `http://localhost:8081`            | Gateway → Core routing           |

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

## Postman Collection

Located at `postman/Katibu.postman_collection.json`. Import into Postman.

**Collection variables to set before running:**
- `base_url` — `http://localhost:8080`

Run requests in order: Register → Login (sets `auth_token` automatically) → Create Project (sets `project_id`) → subsequent requests use stored variables.

All tests assert HTTP status, response time < 500ms, and data correctness.

---

## Architecture Notes

- **Monolith-first**: The single core service contains all business logic. The gateway is positioned now so the routing layer exists before microservice extraction begins.
- **Gateway role**: Routes all `/api/**` to the core service. CORS is handled at the gateway. When services are split, only the gateway routing config changes.
- **Soft deletes**: Ledger entries are never hard-deleted — `deleted_at` timestamp is set instead. This maintains a full audit trail per cash-based accounting requirements.
- **No accruals**: All calculations are strictly cash-based. An entry only exists if cash was received or paid.
- **Public links**: Tokens are stored in the database and validated on every request. Revocation is immediate. Optional expiry is checked server-side.
