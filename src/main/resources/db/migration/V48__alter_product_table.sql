ALTER TABLE products
ADD COLUMN created_at TIMESTAMP,
ADD COLUMN updated_at TIMESTAMP,
ADD COLUMN org_id VARCHAR(36);

ALTER TABLE products
ADD CONSTRAINT fk_org_id
FOREIGN KEY (org_id)
REFERENCES organisations(id);