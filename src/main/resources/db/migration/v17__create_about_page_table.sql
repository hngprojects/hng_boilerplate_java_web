-- V1__Create_AboutPage_Tables.sql
CREATE TABLE about_page (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

ALTER TABLE about_page
ADD COLUMN stats_years_in_business INT NOT NULL DEFAULT 10,
ADD COLUMN stats_customers INT NOT NULL DEFAULT 75000,
ADD COLUMN stats_monthly_blog_readers INT NOT NULL DEFAULT 100000,
ADD COLUMN stats_social_followers INT NOT NULL DEFAULT 1200000,
ADD COLUMN service_title VARCHAR(255) NOT NULL DEFAULT 'Trained to Give You The Best',
ADD COLUMN service_description TEXT NOT NULL DEFAULT 'Since our founding, Hng Boilerplate has been dedicated to constantly evolving to stay ahead of the curve.';
