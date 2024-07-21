-- V1__Create_profiles_table.sql
CREATE TABLE profiles (
    id VARCHAR(36) PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    phone VARCHAR(255),
    avatar_url TEXT
);

-- V1__Create_users_table.sql
CREATE TABLE users (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    profile_id VARCHAR(36) UNIQUE,
    FOREIGN KEY (profile_id) REFERENCES profiles(id)
);

-- V1__Create_organisations_table.sql
CREATE TABLE organisations (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT
);

-- V1__Create_products_table.sql
CREATE TABLE products (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    user_id VARCHAR(36) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- V1__Create_user_organisation_table.sql
CREATE TABLE user_organisation (
    user_id VARCHAR(36),
    organisation_id VARCHAR(36),
    PRIMARY KEY (user_id, organisation_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (organisation_id) REFERENCES organisations(id)
);