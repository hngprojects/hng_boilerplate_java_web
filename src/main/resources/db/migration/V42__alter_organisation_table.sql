ALTER TABLE organisations
    DROP CONSTRAINT IF EXISTS organisations_owner_id_fkey,  -- Drop the foreign key constraint if it exists
    ALTER COLUMN owner_id TYPE VARCHAR(255);