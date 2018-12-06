ALTER TABLE lot ADD COLUMN modified_date timestamp without time zone;
ALTER TABLE lot ADD COLUMN modified_by bigint;
ALTER TABLE parent ADD COLUMN modified_date timestamp without time zone;
ALTER TABLE parent ADD COLUMN modified_by bigint;
ALTER TABLE ONLY lot ADD CONSTRAINT lot_modified_by_fk FOREIGN KEY (modified_by) REFERENCES scientist(id);
ALTER TABLE ONLY parent ADD CONSTRAINT parent_modified_by_fk FOREIGN KEY (modified_by) REFERENCES scientist(id);
