--Added needed columns
ALTER TABLE products
ADD COLUMN current_stock DOUBLE PRECISION,
ADD COLUMN last_updated TIMESTAMP;