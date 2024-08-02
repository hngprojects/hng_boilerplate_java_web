CREATE TABLE role_permissions (
                                  role_id UUID NOT NULL,
                                  permission_id UUID NOT NULL,
                                  PRIMARY KEY (role_id, permission_id),
                                  CONSTRAINT fk_role FOREIGN KEY(role_id) REFERENCES roles(id),
                                  CONSTRAINT fk_permission FOREIGN KEY(permission_id) REFERENCES permissions(id)
);