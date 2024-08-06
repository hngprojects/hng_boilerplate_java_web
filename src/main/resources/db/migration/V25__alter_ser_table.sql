-- This script alters the users table to add phone, status, and isActive columns
ALTER TABLE users
ADD COLUMN phone VARCHAR(255),
ADD COLUMN status VARCHAR(255),
ADD COLUMN is_active BOOLEAN DEFAULT TRUE

