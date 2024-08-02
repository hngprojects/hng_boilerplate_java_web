CREATE TABLE invitations (
                             id SERIAL PRIMARY KEY,
                             token VARCHAR(255) NOT NULL UNIQUE,
                             organisation_id UUID NOT NULL,
                             invited_user_id UUID NOT NULL,
                             CONSTRAINT fk_organisation FOREIGN KEY(organisation_id) REFERENCES organisations(id),
                             CONSTRAINT fk_invited_user FOREIGN KEY(invited_user_id) REFERENCES users(id)
);