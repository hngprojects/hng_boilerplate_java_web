CREATE TABLE sale (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    total_sale FLOAT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE sale_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id VARCHAR(36) NOT NULL,
    sale_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (sale_id) REFERENCES sale(id)
);
