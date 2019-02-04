-- create indexes
-- recreate the kind tables

CREATE INDEX parent_alias_parent_idx ON parent_alias USING btree (parent);

ALTER TABLE ONLY parent_alias
    ADD CONSTRAINT parent_alias_parent_fk FOREIGN KEY (parent) REFERENCES parent(id);

CREATE INDEX salt_form_alias_saltform_idx ON salt_form_alias USING btree (salt_form);

ALTER TABLE ONLY salt_form_alias
    ADD CONSTRAINT salt_form_alias_saltform_fk FOREIGN KEY (salt_form) REFERENCES salt_form(id);

CREATE INDEX lot_alias_lot_idx ON lot_alias USING btree (lot);

ALTER TABLE ONLY lot_alias
    ADD CONSTRAINT lot_alias_lot_fk FOREIGN KEY (lot) REFERENCES lot(id);






  

ALTER TABLE parent_alias_kind 
	RENAME COLUMN parent_alias_type TO ls_type ;

ALTER TABLE salt_form_alias_kind 
	RENAME COLUMN salt_form_alias_type TO ls_type ;
	
ALTER TABLE lot_alias_kind 
	RENAME COLUMN lot_alias_type TO ls_type ;	
	



  







