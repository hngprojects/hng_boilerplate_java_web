CREATE TABLE posts(
    blog_id VARCHAR(36) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    image_urls VARCHAR(255) NOT NULL,
    tags VARCHAR(255),
    creation_date TIMESTAMP NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);