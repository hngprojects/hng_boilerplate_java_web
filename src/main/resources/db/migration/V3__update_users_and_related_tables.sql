-- Drop existing foreign key constraints
ALTER TABLE products DROP CONSTRAINT IF EXISTS products_user_id_fkey;
ALTER TABLE user_organisation DROP CONSTRAINT IF EXISTS user_organisation_user_id_fkey;

-- Drop primary key constraint on users table
ALTER TABLE users DROP CONSTRAINT IF EXISTS users_pkey;

-- Create sequence for BIGINT id
CREATE SEQUENCE IF NOT EXISTS users_id_seq OWNED BY users.id;

-- Alter the users table
ALTER TABLE users
    -- Alter id column to BIGINT and make it auto-increment
    ALTER COLUMN id TYPE BIGINT USING id::BIGINT,
    ALTER COLUMN id SET DEFAULT nextval('users_id_seq');

-- Set id column as NOT NULL and add PRIMARY KEY constraint
ALTER TABLE users
    ALTER COLUMN id SET NOT NULL,
    ADD PRIMARY KEY (id);

-- Add new columns to users table
ALTER TABLE users
    ADD COLUMN user_id VARCHAR(36) NOT NULL UNIQUE,
    ADD COLUMN password VARCHAR(255) NOT NULL,
    ADD COLUMN user_role VARCHAR(255) NOT NULL,
    ADD COLUMN is_enabled BOOLEAN DEFAULT FALSE,
    ADD COLUMN created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP;

-- Recreate foreign key constraint on profile_id if it doesn't already exist
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'users_profile_id_fkey'
    ) THEN
        ALTER TABLE users
            ADD CONSTRAINT users_profile_id_fkey FOREIGN KEY (profile_id) REFERENCES profiles(id);
    END IF;
END
$$;

-- Update user_id column type in related tables with USING clause
ALTER TABLE products
    ALTER COLUMN user_id TYPE BIGINT USING user_id::BIGINT;

ALTER TABLE user_organisation
    ALTER COLUMN user_id TYPE BIGINT USING user_id::BIGINT;

-- Recreate foreign key constraints in products and user_organisation tables if they don't already exist
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'products_user_id_fkey'
    ) THEN
        ALTER TABLE products
            ADD CONSTRAINT products_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id);
    END IF;

    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'user_organisation_user_id_fkey'
    ) THEN
        ALTER TABLE user_organisation
            ADD CONSTRAINT user_organisation_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id);
    END IF;
END
$$;
