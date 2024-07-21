-- V4__Create_plan_table.sql
CREATE TABLE profiles (
                          id VARCHAR(36) PRIMARY KEY,
                          name VARCHAR(255),
                          price DECIMAL(0, 2),
                          duration INTEGER(255),
                          duration_unit VARCHAR(12)
);