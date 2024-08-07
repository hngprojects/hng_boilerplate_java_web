-- Create the video_suite table
CREATE TABLE video_suite (
    job_id VARCHAR(36) PRIMARY KEY,
    status VARCHAR(10) NOT NULL,
    job_type VARCHAR(50) NOT NULL,
    progress INTEGER,
    output_video_url TEXT,
    message TEXT
);
