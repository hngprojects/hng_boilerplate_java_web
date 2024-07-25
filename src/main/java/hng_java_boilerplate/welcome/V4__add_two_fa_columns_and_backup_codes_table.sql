V4__add_two_fa_columns_and_backup_codes_table.sql

Add columns to the users table
ALTER TABLE users
ADD COLUMN is2FAEnabled BOOLEAN,
ADD COLUMN twoFASecretKey VARCHAR(255);

Create the two_fa_backup_codes table
CREATE TABLE two_fa_backup_codes (
                                     user_id VARCHAR(255) NOT NULL,
                                     backup_code VARCHAR(255),
                                     PRIMARY KEY (user_id, backup_code),
                                     FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
