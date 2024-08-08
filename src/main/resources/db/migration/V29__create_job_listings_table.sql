CREATE TABLE job_listings (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    location VARCHAR(255) NOT NULL,
    salary VARCHAR(255) NOT NULL,
    job_type VARCHAR(255) NOT NULL,
    company_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

-- Create an index on the created_at column for better query performance
CREATE INDEX idx_job_listings_created_at ON job_listings(created_at);

-- Create an index on the company_name column for better query performance when searching by company
CREATE INDEX idx_job_listings_company_name ON job_listings(company_name);