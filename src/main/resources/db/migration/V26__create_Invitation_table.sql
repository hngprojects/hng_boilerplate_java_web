CREATE TABLE invitations (
                             id VARCHAR(255) PRIMARY KEY,
                             token VARCHAR(255) NOT NULL UNIQUE,
                             organisation_id UUID NOT NULL,
                             invited_user_id UUID NOT NULL,
                             CONSTRAINT fk_organisation FOREIGN KEY(organisation_id) REFERENCES organisations(id),
                             CONSTRAINT fk_invited_user FOREIGN KEY(invited_user_id) REFERENCES users(id)
);

CREATE TABLE permissions (
                             id VARCHAR(255) PRIMARY KEY,
                             name VARCHAR(255) NOT NULL,
                             description TEXT
);

CREATE TABLE roles (
                       id VARCHAR(255) PRIMARY KEY,,
                       name VARCHAR(255) NOT NULL,
                       description TEXT,
                       organisation_id UUID NOT NULL,
                       CONSTRAINT fk_organisation FOREIGN KEY(organisation_id) REFERENCES organisations(id)
);
CREATE TABLE role_permissions (
                                  role_id VARCHAR(255) NOT NULL,
                                  permission_id VARCHAR(255) NOT NULL,
                                  PRIMARY KEY (role_id, permission_id),
                                  CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles (id),
                                  CONSTRAINT fk_permission FOREIGN KEY (permission_id) REFERENCES permissions (id)
);