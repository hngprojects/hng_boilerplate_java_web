ALTER TABLE video_suite
ADD COLUMN current_process VARCHAR(50);

ALTER TABLE video_suite
RENAME COLUMN output_video_url TO filename;

ALTER TABLE video_suite
ALTER COLUMN filename TYPE VARCHAR(50);

