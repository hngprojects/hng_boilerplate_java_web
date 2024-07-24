-- Add columns to the users table
ALTER TABLE users
    ADD COLUMN two_FA_status BOOLEAN,
ADD COLUMN two_FA_secret_key VARCHAR(255);
