CREATE TABLE roles (
                       id UUID PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       description TEXT,
                       organisation_id UUID NOT NULL,
                       CONSTRAINT fk_organisation FOREIGN KEY(organisation_id) REFERENCES organisations(id)
);
CREATE TABLE role_permissions (
                                  role_id UUID NOT NULL,
                                  permission_id UUID NOT NULL,
                                  PRIMARY KEY (role_id, permission_id),
                                  CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles (id),
                                  CONSTRAINT fk_permission FOREIGN KEY (permission_id) REFERENCES permissions (id)
);