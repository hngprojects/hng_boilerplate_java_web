-- Add the organisation_id column to the org_permissionss table
ALTER TABLE org_permissions
ADD COLUMN organisation_id VARCHAR(36);

-- Ensure the organisation_id column cannot be null
ALTER TABLE org_permissions
ALTER COLUMN organisation_id SET NOT NULL;

-- Add the foreign key constraint to organisation_id
ALTER TABLE org_permissions
ADD CONSTRAINT fk_org_permissions_organisation
FOREIGN KEY (organisation_id)
REFERENCES organisations (id)
ON DELETE CASCADE;
