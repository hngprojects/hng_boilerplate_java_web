-- Create the aboutPage table
CREATE TABLE IF NOT EXISTS about_page (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    introduction TEXT
);

