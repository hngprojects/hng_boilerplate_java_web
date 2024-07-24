UPDATE users
SET password = 'default_password'
WHERE password IS NULL;

ALTER TABLE users
ALTER COLUMN password SET NOT NULL;
