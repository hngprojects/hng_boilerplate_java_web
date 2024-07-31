CREATE TABLE org_permissions (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE org_roles (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE org_role_permissions (
    org_role_id VARCHAR(36) NOT NULL,
    org_permission_id VARCHAR(36) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (org_role_id, org_permission_id),
    FOREIGN KEY (org_role_id) REFERENCES org_roles (id) ON DELETE CASCADE,
    FOREIGN KEY (org_permission_id) REFERENCES org_permissions (id) ON DELETE CASCADE
);


CREATE TABLE user_org_roles (
    user_id VARCHAR(36) NOT NULL,
    org_role_id VARCHAR(36) NOT NULL,
    PRIMARY KEY (user_id, org_role_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (org_role_id) REFERENCES org_roles (id) ON DELETE CASCADE
);








ALTER TABLE user_org_roles
ADD COLUMN organisation_id VARCHAR(36) NOT NULL;

ALTER TABLE user_org_roles
ADD FOREIGN KEY (organisation_id) REFERENCES organisations (id) ON DELETE CASCADE;

ALTER TABLE user_org_roles
DROP CONSTRAINT user_org_roles_pkey;

ALTER TABLE user_org_roles
ADD PRIMARY KEY (user_id, org_role_id, organisation_id);







ALTER TABLE org_roles
ADD COLUMN organisation_id VARCHAR(36);

ALTER TABLE org_roles
ALTER COLUMN organisation_id SET NOT NULL;

ALTER TABLE org_roles
ADD CONSTRAINT fk_org_roles_organisation
FOREIGN KEY (organisation_id)
REFERENCES organisations (id)
ON DELETE CASCADE;





ALTER TABLE org_permissions
ADD COLUMN organisation_id VARCHAR(36);

ALTER TABLE org_permissions
ALTER COLUMN organisation_id SET NOT NULL;

ALTER TABLE org_permissions
ADD CONSTRAINT fk_org_permissions_organisation
FOREIGN KEY (organisation_id)
REFERENCES organisations (id)
ON DELETE CASCADE;