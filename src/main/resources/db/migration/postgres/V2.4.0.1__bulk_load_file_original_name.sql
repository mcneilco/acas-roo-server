ALTER TABLE bulk_load_file ADD COLUMN original_file_name character varying(1000);
UPDATE bulk_load_file SET original_file_name = file_name where original_file_name IS NULL;