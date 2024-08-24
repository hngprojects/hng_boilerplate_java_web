CREATE TABLE api_status (
    id BIGSERIAL PRIMARY KEY,
    api_group VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    last_checked TIMESTAMP NOT NULL,
    response_time INTEGER,
    details TEXT
);

-- Create an index on the api_group column for faster lookups
CREATE INDEX idx_api_status_api_group ON api_status(api_group);

-- Add a constraint to ensure status is one of the allowed values
ALTER TABLE api_status
    ADD CONSTRAINT chk_status CHECK (status IN ('OPERATIONAL', 'DEGRADED', 'DOWN'));