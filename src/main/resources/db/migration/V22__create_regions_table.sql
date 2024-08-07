-- Create regions table
CREATE TABLE regions (
    id VARCHAR(100) PRIMARY KEY,
    region VARCHAR(255) NOT NULL,
    language VARCHAR(255) NOT NULL,
    timezone VARCHAR(255) NOT NULL,
    user_id VARCHAR(100) UNIQUE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);