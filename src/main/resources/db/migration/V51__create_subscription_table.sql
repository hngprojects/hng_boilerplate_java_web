ALTER TABLE newsletters
ADD COLUMN user_id VARCHAR(50),
ADD CONSTRAINT fk_user_id
FOREIGN KEY (user_id)
REFERENCES users(id)
ON DELETE CASCADE
ON UPDATE CASCADE;

CREATE TABLE subscribers(
    user_id VARCHAR(50),
    newsletter_id VARCHAR(50),
    PRIMARY KEY (user_id, newsletter_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (newsletter_id) REFERENCES newsletters(id),
    subscribed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP);