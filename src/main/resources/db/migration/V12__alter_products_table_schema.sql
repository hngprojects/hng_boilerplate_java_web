--Added needed columns
ALTER TABLE products
ADD COLUMN current_stock DOUBLE PRECISION,
ADD COLUMN created_at TIMESTAMP,
ADD COLUMN updated_at TIMESTAMP,
ADD COLUMN last_updated TIMESTAMP;