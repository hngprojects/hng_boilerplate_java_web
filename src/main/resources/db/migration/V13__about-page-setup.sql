CREATE TABLE about_page (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255),
    introduction TEXT
);

CREATE TABLE custom_section (
    id BIGSERIAL PRIMARY KEY,
    about_page_id BIGINT,
    FOREIGN KEY (about_page_id) REFERENCES about_page(id)
);

CREATE TABLE services (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255),
    description TEXT,
    custom_section_id BIGINT,
    FOREIGN KEY (custom_section_id) REFERENCES custom_section(id)
);

CREATE TABLE stat (
    id BIGSERIAL PRIMARY KEY,
    years_in_business integer,
    customers integer,
    monthly_blog_readers integer,
    social_followers integer,
    custom_section_id BIGINT,
    FOREIGN KEY (custom_section_id) REFERENCES custom_section(id)
);