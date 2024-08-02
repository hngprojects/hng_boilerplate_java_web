-- V2__create_invitation_table.sql
CREATE TABLE invitation_table (
    id VARCHAR(255) PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    user_email VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    organisation_id VARCHAR NOT NULL,
    FOREIGN KEY (organisation_id) REFERENCES organisations(id)
);

ALTER TABLE invitation_table
ADD CONSTRAINT fk_organisation_id FOREIGN KEY (organisation_id) REFERENCES organisations(id);