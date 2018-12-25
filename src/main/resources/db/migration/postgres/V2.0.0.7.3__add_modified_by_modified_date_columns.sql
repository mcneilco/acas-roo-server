ALTER TABLE lot ADD COLUMN modified_date timestamp without time zone;
ALTER TABLE lot ADD COLUMN modified_by character varying(255);
ALTER TABLE parent ADD COLUMN modified_date timestamp without time zone;
ALTER TABLE parent ADD COLUMN modified_by character varying(255);
