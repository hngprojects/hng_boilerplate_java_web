-- V1__Create_waitlist_table.sql
CREATE TABLE waitlist (
    id UUID PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);