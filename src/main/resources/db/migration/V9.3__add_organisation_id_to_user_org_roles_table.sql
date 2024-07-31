-- V9.3__add_organisation_to_user_org_roles.sql

-- Adding organization_id column
ALTER TABLE user_org_roles
ADD COLUMN organisation_id VARCHAR(36) NOT NULL;

-- Adding foreign key constraint
ALTER TABLE user_org_roles
ADD FOREIGN KEY (organisation_id) REFERENCES organisations (id) ON DELETE CASCADE;

-- Dropping the existing primary key constraint
ALTER TABLE user_org_roles
DROP CONSTRAINT user_org_roles_pkey;

-- Adding the new composite primary key constraint
ALTER TABLE user_org_roles
ADD PRIMARY KEY (user_id, org_role_id, organisation_id);
