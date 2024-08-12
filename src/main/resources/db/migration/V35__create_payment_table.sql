ALTER TABLE users
ADD COLUMN plan_id VARCHAR(255);

ALTER TABLE users
ADD CONSTRAINT fk_plan
FOREIGN KEY (plan_id)
REFERENCES plans(id);



ALTER TABLE plans
DROP COLUMN duration,
DROP COLUMN duration_unit;

INSERT INTO plans (id, name, description,price, features)
VALUES
('1', 'Free', 'To try out our platform and see if it fits your needs.', 0.00, 'Ai-powered transcription, subtitles and translation.;10 minutes for a single file;File export not available.'),
('2', 'Pro', 'For advanced creators and teams with recurring language needs.', 10.00, 'Ai-powered transcription, subtitles and translation.;300 minutes per month;Export transcriptions in Word, TXT and PDF.;Export subtitles in SRT, MP4, VTT, STL, HTML, XML, TXT, DOCX and more.'),
('3', 'Enterprise', 'For large organizations in need of recurring language needs.', 300.00, '600 minutes for a single file;Expand up to 100 hours per month.;3 users seats included.;Manage workspace roles and permissions.;Create unlimited style guides and glossaries.;5% discount on human-made services.;Premium support.');


CREATE TABLE payments (
id VARCHAR(36) PRIMARY KEY,
amount FLOAT NOT NULL,
status VARCHAR(255) NOT NULL,
user_id VARCHAR(36) UNIQUE,
created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (user_id) REFERENCES users(id)
);


CREATE TABLE user_payments (
user_id VARCHAR(36),
payment_id VARCHAR(36),
PRIMARY KEY (user_id, payment_id),
FOREIGN KEY (user_id) REFERENCES users(id),
FOREIGN KEY (payment_id) REFERENCES payments(id)
);
