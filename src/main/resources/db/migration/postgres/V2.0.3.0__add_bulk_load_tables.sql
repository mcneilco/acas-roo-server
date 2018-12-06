-- Table: bulk_load_file
CREATE TABLE bulk_load_file
(
  id bigint NOT NULL,
  file_name character varying(1000),
  file_size integer NOT NULL,
  json_template text,
  number_of_mols integer NOT NULL,
  recorded_by character varying(255) NOT NULL,
  version integer,
  CONSTRAINT bulk_load_file_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);


-- Table: bulk_load_template
 CREATE TABLE bulk_load_template
(
  id bigint NOT NULL,
  json_template text,
  recorded_by character varying(255) NOT NULL,
  template_name character varying(255),
  version integer,
  ignored boolean NOT NULL,
  CONSTRAINT bulk_load_template_pkey PRIMARY KEY (id),
  CONSTRAINT template_uq_name_recordedby UNIQUE (template_name, recorded_by)
)
WITH (
  OIDS=FALSE
);

 
--adding columns to lot, salt_form, and parent to reference bulk_load_file
ALTER TABLE lot ADD COLUMN bulk_load_file bigint;
ALTER TABLE lot ADD CONSTRAINT lot_bulk_load_file FOREIGN KEY (bulk_load_file)
      REFERENCES bulk_load_file (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE salt_form ADD COLUMN bulk_load_file bigint;
ALTER TABLE salt_form ADD CONSTRAINT salt_form_bulk_load_file FOREIGN KEY (bulk_load_file)
      REFERENCES bulk_load_file (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE parent ADD COLUMN bulk_load_file bigint;
ALTER TABLE parent ADD CONSTRAINT parent_bulk_load_file FOREIGN KEY (bulk_load_file)
      REFERENCES bulk_load_file (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
