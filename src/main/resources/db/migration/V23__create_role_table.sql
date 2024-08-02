CREATE TABLE roles (
                       id UUID PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       description TEXT,
                       organisation_id UUID NOT NULL,
                       CONSTRAINT fk_organisation FOREIGN KEY(organisation_id) REFERENCES organisations(id)
);