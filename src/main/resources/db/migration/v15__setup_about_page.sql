
-- Create the aboutPage table
CREATE TABLE aboutPage (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    introduction TEXT
);

-- Create the custom_section table
CREATE TABLE custom_section (
    id SERIAL PRIMARY KEY,
    aboutPage_id INTEGER,
    CONSTRAINT fk_about_page FOREIGN KEY(aboutPage_id) REFERENCES aboutPage(id)
);

-- Create the services table
CREATE TABLE services (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    custom_section_id INTEGER,
    CONSTRAINT fk_custom_section FOREIGN KEY(custom_section_id) REFERENCES custom_section(id)
);

-- Create the stat table
CREATE TABLE stat (
    id SERIAL PRIMARY KEY,
    years_in_business INTEGER,
    customers INTEGER,
    monthly_blog_readers INTEGER,
    social_followers INTEGER,
    custom_section_id INTEGER,
    CONSTRAINT fk_stat_custom_section FOREIGN KEY(custom_section_id) REFERENCES custom_section(id)
);

