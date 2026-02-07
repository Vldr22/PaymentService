CREATE TABLE users
(
    id          UUID PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    surname     VARCHAR(50) NOT NULL,
    middle_name VARCHAR(50),
    phone       VARCHAR(20) UNIQUE,
    email       VARCHAR(255) UNIQUE,
    password    VARCHAR(255),
    role        VARCHAR(20) NOT NULL,
    status      VARCHAR(20) NOT NULL,
    created_at  TIMESTAMP   NOT NULL
);

CREATE TABLE payments
(
    id            UUID PRIMARY KEY,
    type          VARCHAR(20)   NOT NULL,
    status        VARCHAR(20)   NOT NULL,
    amount        BIGINT        NOT NULL,
    currency      VARCHAR(3)    NOT NULL,
    description   VARCHAR(500)  NOT NULL,
    fee           BIGINT        NOT NULL,
    fee_percent   DECIMAL(7, 4) NOT NULL,
    user_id       UUID          NOT NULL REFERENCES users (id),
    card_token_id BIGINT,
    created_at    TIMESTAMP     NOT NULL,
    updated_at    TIMESTAMP
);

CREATE TABLE refunds
(
    id                 UUID PRIMARY KEY,
    payment_id         UUID        NOT NULL REFERENCES payments (id),
    amount             BIGINT      NOT NULL,
    currency           VARCHAR(3)  NOT NULL,
    status             VARCHAR(20) NOT NULL,
    reason             VARCHAR(500),
    yookassa_refund_id VARCHAR(255) UNIQUE,
    created_at         TIMESTAMP   NOT NULL,
    updated_at         TIMESTAMP
);

CREATE TABLE card_tokens
(
    id         UUID PRIMARY KEY,
    token      VARCHAR(500) NOT NULL UNIQUE,
    user_id    UUID         NOT NULL REFERENCES users (id),
    created_at TIMESTAMP    NOT NULL
);

CREATE TABLE subscription
(
    id                UUID PRIMARY KEY,
    subscription_type VARCHAR(20) NOT NULL,
    start_date        TIMESTAMP   NOT NULL,
    end_date          TIMESTAMP   NOT NULL,
    active            BOOLEAN     NOT NULL,
    user_id           UUID        NOT NULL REFERENCES users (id),
    last_payment_id   UUID REFERENCES payments (id),
    created_at        TIMESTAMP   NOT NULL,
    updated_at        TIMESTAMP
);

CREATE TABLE sms_codes
(
    id         UUID PRIMARY KEY,
    code       VARCHAR(4)  NOT NULL,
    phone      VARCHAR(20) NOT NULL,
    status     VARCHAR(20) NOT NULL,
    expires_at TIMESTAMP   NOT NULL,
    created_at TIMESTAMP   NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE registration_codes
(
    id         UUID PRIMARY KEY,
    code       VARCHAR(10)  NOT NULL UNIQUE,
    email      VARCHAR(255) NOT NULL UNIQUE,
    role       VARCHAR(20)  NOT NULL,
    used       BOOLEAN      NOT NULL,
    expires_at TIMESTAMP    NOT NULL,
    created_at TIMESTAMP    NOT NULL
);

CREATE TABLE webhook_events
(
    id           UUID PRIMARY KEY,
    event_id     VARCHAR(255) NOT NULL UNIQUE,
    event_type   VARCHAR(100) NOT NULL,
    payment_id   UUID         NOT NULL,
    payload      TEXT         NOT NULL,
    processed    BOOLEAN      NOT NULL,
    created_at   TIMESTAMP    NOT NULL,
    processed_at TIMESTAMP
);

CREATE TABLE payment_audit
(
    id         UUID PRIMARY KEY,
    payment_id UUID         NOT NULL REFERENCES payments (id),
    action     VARCHAR(100) NOT NULL,
    old_status VARCHAR(50),
    new_status VARCHAR(50),
    details    VARCHAR(500),
    created_at TIMESTAMP    NOT NULL
);