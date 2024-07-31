-- Add the organisation_id column to the org_roles table
ALTER TABLE org_roles
ADD COLUMN organisation_id VARCHAR(36);

-- Ensure the organisation_id column cannot be null
ALTER TABLE org_roles
ALTER COLUMN organisation_id SET NOT NULL;

-- Add the foreign key constraint to organisation_id
ALTER TABLE org_roles
ADD CONSTRAINT fk_org_roles_organisation
FOREIGN KEY (organisation_id)
REFERENCES organisations (id)
ON DELETE CASCADE;
