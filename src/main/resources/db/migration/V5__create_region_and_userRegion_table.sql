
-- V5__Create_region_table.sql
CREATE TABLE regions (
    region_code VARCHAR(255) PRIMARY KEY,
    region_name VARCHAR(255) NOT NULL,
    status INT NOT NULL,
    created_on TIMESTAMP NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    modified_on TIMESTAMP NOT NULL,
    modified_by VARCHAR(255) NOT NULL
);

-- V5__Create_userRegion_table.sql
CREATE TABLE user_region (
    region_id SERIAL PRIMARY KEY,
    user_id UUID NOT NULL,
    region_name VARCHAR(255) NOT NULL,
    region_code VARCHAR(255) NOT NULL
);
