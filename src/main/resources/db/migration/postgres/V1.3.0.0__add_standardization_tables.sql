CREATE TABLE standardization_settings
(
  id bigint NOT NULL,
  version integer,
  modified_date timestamp without time zone,
  needs_standardization boolean
)
WITH (
  OIDS=FALSE
);

CREATE SEQUENCE stndzn_settings_pkseq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE standardization_dry_run_compound
(
  id bigint NOT NULL,
  version integer,
  run_number integer,
  qc_date timestamp without time zone,
  parent_id bigint,
  changed_structure boolean,
  old_mol_weight double precision,
  new_mol_weight double precision,
  delta_mol_weight double precision,
  existing_duplicate_count integer,
  new_duplicate_count integer,
  new_duplicates text,
  display_change boolean,  
  existing_duplicates text,
  as_drawn_display_change boolean,  
  corp_name character varying(255),
  stereo_category character varying(255),
  stereo_comment character varying(255),
  alias character varying(1024),
  cd_id integer NOT NULL,
  mol_structure text,
  comment character varying(2000),
  registration_status character varying(255),
  registration_comment text,
  standardization_status character varying(255),
  standardization_comment text,
  ignore boolean,
  CONSTRAINT stndzn_dry_run_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);

CREATE INDEX stndzn_dry_run_cdid_idx ON standardization_dry_run_compound USING btree (cd_id);

CREATE TABLE standardization_history
(
  id bigint NOT NULL,
  version integer,
  recorded_date timestamp without time zone,
  settings_hash integer,
  settings text,
  dry_run_status character varying(20),
  dry_run_start timestamp without time zone,
  dry_run_complete timestamp without time zone,
  standardization_status character varying(20),
  standardization_user text,
  standardization_reason text,
  standardization_start timestamp without time zone,
  standardization_complete timestamp without time zone,
  structures_standardized_count integer,
  structures_updated_count integer,
  new_duplicate_count integer,
  existing_duplicate_count integer,
  display_change_count integer,
  as_drawn_display_change_count integer,
  changed_structure_count integer,
  CONSTRAINT stndzn_history_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);

CREATE SEQUENCE stndzn_hist_pkseq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
