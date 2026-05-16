-- Katibu Financial Management Platform
-- Initial Schema - V1

CREATE TABLE users (
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email         VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name     VARCHAR(255) NOT NULL,
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE projects (
    id            UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    name          VARCHAR(255) NOT NULL,
    description   VARCHAR(1000),
    creator_id    UUID         NOT NULL REFERENCES users (id),
    duration_type VARCHAR(20)  NOT NULL CHECK (duration_type IN ('WEEKLY','MONTHLY','QUARTERLY','HALF_YEARLY','YEARLY','CUSTOM')),
    start_date    DATE         NOT NULL,
    end_date      DATE         NOT NULL,
    status        VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE','ARCHIVED')),
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE project_members (
    id         UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    project_id UUID        NOT NULL REFERENCES projects (id),
    user_id    UUID        NOT NULL REFERENCES users (id),
    role       VARCHAR(20) NOT NULL DEFAULT 'ADMIN' CHECK (role IN ('ADMIN')),
    added_by   UUID        REFERENCES users (id),
    added_at   TIMESTAMP   NOT NULL DEFAULT NOW(),
    UNIQUE (project_id, user_id)
);

CREATE TABLE ledger_entries (
    id               UUID           PRIMARY KEY DEFAULT gen_random_uuid(),
    project_id       UUID           NOT NULL REFERENCES projects (id),
    entry_type       VARCHAR(30)    NOT NULL CHECK (entry_type IN (
                                       'INITIAL_CAPITAL','DONATION','REVENUE','GRANT','LOAN_RECEIVED','CREDIT',
                                       'PURCHASE','EXPENDITURE','LOAN_REPAYMENT','DEBT_PAYMENT','WITHDRAWAL')),
    amount           NUMERIC(19, 4) NOT NULL CHECK (amount > 0),
    description      VARCHAR(1000)  NOT NULL,
    reference        VARCHAR(255),
    transaction_date DATE           NOT NULL,
    recorded_by      UUID           NOT NULL REFERENCES users (id),
    recorded_at      TIMESTAMP      NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMP      NOT NULL DEFAULT NOW(),
    deleted_at       TIMESTAMP
);

CREATE TABLE public_links (
    id         UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    project_id UUID        NOT NULL REFERENCES projects (id),
    token      VARCHAR(100) NOT NULL UNIQUE,
    created_by UUID        NOT NULL REFERENCES users (id),
    expires_at TIMESTAMP,
    active     BOOLEAN     NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP   NOT NULL DEFAULT NOW()
);

CREATE TABLE generated_files (
    id          UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    project_id  UUID        NOT NULL REFERENCES projects (id),
    file_name   VARCHAR(255) NOT NULL,
    object_key  VARCHAR(500) NOT NULL,
    file_type   VARCHAR(10)  NOT NULL CHECK (file_type IN ('PDF','CSV')),
    report_type VARCHAR(30)  NOT NULL CHECK (report_type IN ('SUMMARY','RECEIPTS_PAYMENTS','CASH_FLOW','FINANCIAL_POSITION','LEDGER')),
    size_bytes  BIGINT,
    created_by  UUID        NOT NULL REFERENCES users (id),
    created_at  TIMESTAMP   NOT NULL DEFAULT NOW()
);

-- Performance indexes
CREATE INDEX idx_projects_creator      ON projects (creator_id);
CREATE INDEX idx_projects_status       ON projects (status);
CREATE INDEX idx_members_project       ON project_members (project_id);
CREATE INDEX idx_members_user          ON project_members (user_id);
CREATE INDEX idx_entries_project       ON ledger_entries (project_id);
CREATE INDEX idx_entries_date          ON ledger_entries (transaction_date);
CREATE INDEX idx_entries_project_date  ON ledger_entries (project_id, transaction_date) WHERE deleted_at IS NULL;
CREATE INDEX idx_links_token           ON public_links (token);
CREATE INDEX idx_links_project         ON public_links (project_id);
CREATE INDEX idx_files_project         ON generated_files (project_id);
