-- V9.1__Create_org_permissions_table.sql
CREATE TABLE org_permissions (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- V9.1__Create_org_roles_table.sql
CREATE TABLE org_roles (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- V9.1__Create_org_role_permissions_table.sql
CREATE TABLE org_role_permissions (
    org_role_id VARCHAR(36) NOT NULL,
    org_permission_id VARCHAR(36) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (org_role_id, org_permission_id),
    FOREIGN KEY (org_role_id) REFERENCES org_roles (id) ON DELETE CASCADE,
    FOREIGN KEY (org_permission_id) REFERENCES org_permissions (id) ON DELETE CASCADE
);