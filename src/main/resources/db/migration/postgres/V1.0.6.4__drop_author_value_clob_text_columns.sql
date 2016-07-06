SELECT lo_unlink(clob_value::int) FROM author_value;
ALTER TABLE author_value ALTER COLUMN clob_value SET DATA TYPE text USING clob_value_temp;
ALTER TABLE author_value DROP COLUMN clob_value_temp;
