CREATE TABLE coupons (
    id              VARCHAR(36)     PRIMARY KEY,
    code            VARCHAR(6)      NOT NULL UNIQUE,
    description     VARCHAR(255)    NOT NULL,
    discount_value  NUMERIC(10, 2)  NOT NULL,
    expiration_date TIMESTAMP       NOT NULL,
    status          VARCHAR(20)     NOT NULL,
    published       BOOLEAN         NOT NULL,
    redeemed        BOOLEAN         NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMP       NOT NULL,
    updated_at      TIMESTAMP       NOT NULL
);
