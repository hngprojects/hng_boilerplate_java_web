
CREATE TABLE IF NOT EXISTS user_payments (
   user_id VARCHAR(36),
   payment_id VARCHAR(36),
   PRIMARY KEY (user_id, payment_id),
   FOREIGN KEY (user_id) REFERENCES users(id),
   FOREIGN KEY (payment_id) REFERENCES payments(id)
);


INSERT INTO plans (id, name, description,price, features)
VALUES
('4', 'Premium', 'Designed for power users and maximum functionalities.', 100.00, '250 Projects.;Up to 10000 subscribers.;Advanced analytics.;24-hour support response time.;Marketing advisor.');

UPDATE  plans
SET name = 'Advanced', description = 'Perfect for users who want more features.', price = 50.00, features = '200 Projects.;Up to 100 subscribers.;Advanced analytics.;24-hour support response time.;Marketing advisor.'
WHERE id = '3';

UPDATE plans
SET name = 'Basic', description = 'Ideal for growing needs who want more features.', price = 20.00, features = '100 Projects.;Up to 50 subscribers.;Advanced analytics.;24-hour support response time.'
WHERE id = '2';