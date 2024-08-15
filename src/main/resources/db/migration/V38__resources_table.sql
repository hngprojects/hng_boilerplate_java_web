-- V28__create_verification_token_table.sql
CREATE TABLE resources (
    id VARCHAR(255) PRIMARY KEY,
    title VARCHAR(255),
    description TEXT,
    image VARCHAR(255),
    date DATE,
    published BOOLEAN DEFAULT FALSE
);

