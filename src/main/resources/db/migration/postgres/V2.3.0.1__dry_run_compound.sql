CREATE TABLE dry_run_compound
(
  id bigint NOT NULL,
  corp_name character varying(255),
  stereo_category character varying(255),
  stereo_comment character varying(255),
  cd_id integer NOT NULL,
  record_number integer NOT NULL,
  mol_structure text,
  CONSTRAINT drcompound_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
  
CREATE INDEX drcompound_cdid_idx ON dry_run_compound USING btree (cd_id);
