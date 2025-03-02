ALTER TABLE newsletters
DROP COLUMN user_id;

ALTER TABLE newsletters
ADD COLUMN title VARCHAR(255);

ALTER TABLE newsletters
ADD COLUMN email VARCHAR(255);

ALTER TABLE newsletters
ADD COLUMN content TEXT;

CREATE TABLE subscribers(
    email VARCHAR(50),
    newsletter_id VARCHAR(50),
    PRIMARY KEY (newsletter_id),
    FOREIGN KEY (newsletter_id) REFERENCES newsletters(id),
    subscribed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP);