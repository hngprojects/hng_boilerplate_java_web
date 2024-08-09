-- Add 'status' and 'phoneNumber' columns to the 'users' table
ALTER TABLE users
    ADD COLUMN status VARCHAR(255),
    ADD COLUMN phone_number VARCHAR(255);
