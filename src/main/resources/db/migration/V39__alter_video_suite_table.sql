ALTER TABLE video_suite
ALTER COLUMN filename TYPE VARCHAR(512);

ALTER TABLE video_suite
ADD COLUMN bitrate VARCHAR(255),
ADD COLUMN resolution VARCHAR(255),
ADD COLUMN original_file_size VARCHAR(255),
ADD COLUMN compressed_file_size VARCHAR(255)

