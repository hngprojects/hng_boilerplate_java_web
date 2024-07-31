-- V9.2__Create_org_permissions_table.sql
CREATE TABLE user_org_roles (
    user_id VARCHAR(36) NOT NULL,
    org_role_id VARCHAR(36) NOT NULL,
    PRIMARY KEY (user_id, org_role_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (org_role_id) REFERENCES org_roles (id) ON DELETE CASCADE
);
