-- Create the table for help center topics
CREATE TABLE help_center_topics (
    article_id UUID PRIMARY KEY,
    title TEXT NOT NULL UNIQUE,
    content TEXT NOT NULL,
    author TEXT NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

-- Create an index on the title for faster searching
CREATE INDEX idx_help_center_topics_title ON help_center_topics (title);
