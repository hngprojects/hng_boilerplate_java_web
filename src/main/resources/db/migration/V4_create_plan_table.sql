-- V4__Create_plan_table.sql
CREATE TABLE plans
(
    id            VARCHAR(255) PRIMARY KEY,
    name          VARCHAR(255)  NOT NULL UNIQUE,
    description   VARCHAR(255)  NOT NULL,
    price         DECIMAL(0, 2) NOT NULL,
    duration      INTEGER       NOT NULL,
    duration_unit VARCHAR(255)  NOT NULL
);