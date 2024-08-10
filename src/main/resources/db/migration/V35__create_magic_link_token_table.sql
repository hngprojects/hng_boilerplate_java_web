CREATE TABLE magic_link_token (
    id VARCHAR(36) PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    expiration_time TIMESTAMP NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);