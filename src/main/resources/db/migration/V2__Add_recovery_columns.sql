-- V2__Add_recovery_columns.sql
ALTER TABLE users ADD COLUMN recovery_email VARCHAR(255);
ALTER TABLE users ADD COLUMN recovery_phone_number VARCHAR(255);
