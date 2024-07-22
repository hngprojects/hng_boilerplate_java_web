-- V2__Create_sms_table.sql

CREATE TABLE sms (
    id BIGSERIAL PRIMARY KEY,
    destination_phone_number VARCHAR(255) NOT NULL,
    message VARCHAR(255) NOT NULL,
    sender_id VARCHAR(36) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES users(id)
);