CREATE TABLE IF NOT EXISTS custom_section (
    id SERIAL PRIMARY KEY,
    about_page_id INTEGER,
    CONSTRAINT fk_about_page FOREIGN KEY(about_page_id) REFERENCES aboutPage(id)
);