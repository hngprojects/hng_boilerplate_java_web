CREATE TABLE activity_logs (
    id BIGSERIAL PRIMARY KEY,
    org_id VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    activity TEXT NOT NULL,
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL
);