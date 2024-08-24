-- V45__create_password_reset_token_table.sql
CREATE TABLE password_reset_token (
    id VARCHAR(36) PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    expiration_time TIMESTAMP NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
