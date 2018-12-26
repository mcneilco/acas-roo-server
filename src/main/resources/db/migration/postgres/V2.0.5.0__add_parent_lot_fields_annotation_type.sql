--parent_annotation
CREATE TABLE parent_annotation (
    id bigint NOT NULL,
    code character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    display_order integer,
    comment character varying(255),
    version integer
);
ALTER TABLE parent_annotation ADD CONSTRAINT parent_annotation_pkey PRIMARY KEY (id);
ALTER TABLE parent_annotation ADD CONSTRAINT parent_annotation_code_unique UNIQUE (code);

ALTER TABLE parent ADD COLUMN parent_annotation bigint;
ALTER TABLE parent ADD CONSTRAINT parent_annotation_fk FOREIGN KEY (parent_annotation)
      REFERENCES parent_annotation (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX ON parent (parent_annotation);

      
--compound_type
CREATE TABLE compound_type (
    id bigint NOT NULL,
    code character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    display_order integer,
    comment character varying(255),
    version integer
);
ALTER TABLE compound_type ADD CONSTRAINT compound_type_pkey PRIMARY KEY (id);
ALTER TABLE compound_type ADD CONSTRAINT compound_type_code_unique UNIQUE (code);

ALTER TABLE parent ADD COLUMN compound_type bigint;
ALTER TABLE parent ADD CONSTRAINT parent_compound_type_fk FOREIGN KEY (compound_type)
      REFERENCES compound_type (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX ON parent (compound_type);

--new flat columns: parent
ALTER TABLE parent ADD COLUMN comment text,
                   ADD COLUMN exact_mass double precision;

--new flat columns: lot
ALTER TABLE lot ADD COLUMN lambda double precision,
                ADD COLUMN absorbance double precision,
                ADD COLUMN stock_solvent character varying(255),
                ADD COLUMN stock_location character varying(255),
                ADD COLUMN retain_location character varying(255);

--registered by
ALTER TABLE parent ADD COLUMN registered_by bigint;
ALTER TABLE parent ADD CONSTRAINT parent_registered_by_fk FOREIGN KEY (registered_by)
      REFERENCES scientist (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE lot ADD COLUMN registered_by bigint;
ALTER TABLE lot ADD CONSTRAINT lot_registered_by_fk FOREIGN KEY (registered_by)
      REFERENCES scientist (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;