-- V1__Create_profiles_table.sql
CREATE TABLE IF NOT EXISTS profiles (
    id VARCHAR(36) PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    phone VARCHAR(255),
    avatar_url TEXT
);

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    user_role VARCHAR(255) NOT NULL,
    profile_id VARCHAR(36),
    is_enabled BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (profile_id) REFERENCES profiles(id)
);

-- V1__Create_organisations_table.sql
CREATE TABLE IF NOT EXISTS organisations (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT
);

-- V1__Create_products_table.sql

CREATE TABLE IF NOT EXISTS products (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- V1__Create_user_organisation_table.sql


CREATE TABLE IF NOT EXISTS user_organisation (
    user_id VARCHAR(36),
    organisation_id VARCHAR(36),
    PRIMARY KEY (user_id, organisation_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (organisation_id) REFERENCES organisations(id)
);