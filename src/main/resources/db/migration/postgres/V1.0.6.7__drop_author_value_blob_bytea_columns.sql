SELECT lo_unlink(blob_value::int) FROM author_value;
ALTER TABLE author_value ALTER COLUMN blob_value SET DATA TYPE bytea USING blob_value_temp;
ALTER TABLE author_value DROP COLUMN blob_value_temp;
