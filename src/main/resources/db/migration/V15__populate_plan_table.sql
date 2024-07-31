-- V15__populate_plan_table.sql

-- Add the plan_type column to the plans table
ALTER TABLE plans
    ADD COLUMN plan_type VARCHAR(255);

-- Insert initial data into the plans table
INSERT INTO plans (id, description, price, plan_type) VALUES
    ('1', 'The essentials to provide your best work for clients', 0, 'Free'),
    ('2', 'Ideal for growing needs who want more features', 20, 'Basic'),
    ('3', 'Designed for power users and maximum functionalities', 50, 'Advanced'),
    ('4', 'Perfect for users who want more features', 100, 'Premium');