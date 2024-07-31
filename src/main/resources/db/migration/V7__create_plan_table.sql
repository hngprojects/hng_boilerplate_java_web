-- V7__create_plan_table.sql
CREATE TABLE plans
(
    id            VARCHAR(255) PRIMARY KEY,
    name          VARCHAR(255),
    description   VARCHAR(255)  NOT NULL,
    price         FLOAT NOT NULL,
    duration      INTEGER,
    duration_unit VARCHAR(255)
);