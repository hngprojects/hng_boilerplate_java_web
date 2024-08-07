--Added needed columns
ALTER TABLE products
ADD COLUMN is_available BOOLEAN DEFAULT TRUE;
