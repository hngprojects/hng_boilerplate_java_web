CREATE TABLE IF NOT EXISTS stat (
    id SERIAL PRIMARY KEY,
    years_in_business INTEGER,
    customers INTEGER,
    monthly_blog_readers INTEGER,
    social_followers INTEGER,
    custom_section_id INTEGER,
    CONSTRAINT fk_stat_custom_section FOREIGN KEY(custom_section_id) REFERENCES custom_section(id)
);