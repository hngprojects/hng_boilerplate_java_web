-- Enable the uuid-ossp extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create the video_suite table
CREATE TABLE video_suite (
    job_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    status VARCHAR(10) NOT NULL,
    job_type VARCHAR(50) NOT NULL,
    progress INTEGER,
    output_video_url TEXT,
    message TEXT
);
