CREATE TABLE email_templates (
 id VARCHAR(36) PRIMARY KEY,
 title VARCHAR(255) NOT NULL UNIQUE,
 template TEXT NOT NULL,
 status VARCHAR(255),
 created_at TIMESTAMP DEFAULT current_timestamp NOT NULL,
 updated_at TIMESTAMP DEFAULT current_timestamp NOT NULL
);
