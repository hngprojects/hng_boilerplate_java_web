__ V10comments_table
CREATE TABLE Comments(
comment_id VARCHAR(36) PRIMARY KEY,
post_id VARCHAR(36) NOT NULL,
user_id VARCHAR(36) NOT Null,
comment TEXT NOT NULL,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (blog_id) REFERENCES posts (blog_id),
FOREIGN KEY (user_id) REFERENCES users (user_id)
);