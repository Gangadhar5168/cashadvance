-- Flyway migration: add status column to advance_transaction
-- Adds a status column with default 'ACTIVE' and sets existing rows to ACTIVE

ALTER TABLE advance_transaction
ADD COLUMN IF NOT EXISTS status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE';

-- Ensure existing NULLs (if any) are set to ACTIVE
UPDATE advance_transaction SET status = 'ACTIVE' WHERE status IS NULL;
