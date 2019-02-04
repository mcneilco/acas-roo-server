-- add default parent alias types/kinds if they do not exist already
ALTER TABLE scientist ADD CONSTRAINT scientist_unique_code UNIQUE (code);