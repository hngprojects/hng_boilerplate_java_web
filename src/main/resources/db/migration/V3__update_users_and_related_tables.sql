-- Add new columns to users table
UPDATE users
SET password = 'default_password'
WHERE password IS NULL;

ALTER TABLE users
    ADD COLUMN password VARCHAR(255) NOT NULL,
    ADD COLUMN user_role VARCHAR(255),
    ADD COLUMN is_enabled BOOLEAN DEFAULT FALSE,
    ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;


