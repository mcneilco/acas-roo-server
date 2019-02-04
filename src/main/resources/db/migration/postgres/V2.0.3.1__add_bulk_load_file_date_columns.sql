ALTER TABLE bulk_load_file ADD COLUMN file_date timestamp without time zone;
ALTER TABLE bulk_load_file ADD COLUMN recorded_date timestamp without time zone NOT NULL;
