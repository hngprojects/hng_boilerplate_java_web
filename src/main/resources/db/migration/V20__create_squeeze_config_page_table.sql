CREATE TABLE squeeze_page (
    id UUID PRIMARY KEY,
    page_title VARCHAR(255) NOT NULL,
    url_slug VARCHAR(255) NOT NULL UNIQUE,
    headline_text VARCHAR(255) NOT NULL,
    subheadline_text VARCHAR(255) NOT NULL,
    body_text TEXT NOT NULL,
    active BOOLEAN NOT NULL DEFAULT false,
    created_date TIMESTAMP NOT NULL
);

-- Create an index on url_slug for faster lookups
CREATE INDEX idx_squeeze_page_url_slug ON squeeze_page(url_slug);

-- Create an index on created_date for sorting
CREATE INDEX idx_squeeze_page_created_date ON squeeze_page(created_date);