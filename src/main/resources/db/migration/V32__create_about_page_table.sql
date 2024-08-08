CREATE TABLE about_page_content (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    introduction TEXT NOT NULL,
    years_in_business INTEGER,
    customers INTEGER,
    monthly_blog_readers INTEGER,
    social_followers INTEGER,
    services_title VARCHAR(255),
    services_description TEXT
);
