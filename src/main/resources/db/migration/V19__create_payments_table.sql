-- V1__create_payments_table.sql
CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    user_email VARCHAR(255),
    payment_status VARCHAR(50),
    payment_provider VARCHAR(50),
    payment_interval VARCHAR(255),
    plan_id VARCHAR(255),
    organization_id VARCHAR(255) NOT NULL,
    transaction_reference VARCHAR(255),
    amount DECIMAL(19, 4),
    currency VARCHAR(10),
    payment_channel VARCHAR(50),
    initiated_at TIMESTAMP,
    completed_at TIMESTAMP
);

