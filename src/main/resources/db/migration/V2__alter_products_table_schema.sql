--Added needed columns
ALTER TABLE products
ADD COLUMN category VARCHAR(255),
ADD COLUMN price DOUBLE PRECISION,
ADD COLUMN image_url VARCHAR(255);
