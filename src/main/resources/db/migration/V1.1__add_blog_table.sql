-- Create blog table
CREATE TABLE blogs (
    id BIGINT PRIMARY KEY,
    blog_id VARCHAR(36) UNIQUE,
    title VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    image_urls TEXT[],
    tags TEXT[],
    author VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create trigger function for update column
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create trigger to call the function before any update
CREATE TRIGGER set_updated_at
BEFORE UPDATE ON blogs
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();
