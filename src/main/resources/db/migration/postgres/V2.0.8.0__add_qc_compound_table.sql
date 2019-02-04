-- id, runNumber, qcDate, parentId, corpName, dupeCount, 
--dupeCorpName, asDrawnStruct, preMolStruct, postMolStruct, comment

CREATE TABLE qc_compound
(
  id bigint NOT NULL,
  version integer,
  run_number integer,
  qc_date timestamp without time zone,
  parent_id bigint,
  display_change boolean,  
  corp_name character varying(255),
  stereo_category character varying(255),
  stereo_comment character varying(255),
  dupe_count integer,
  dupe_corp_name character varying(255),
  alias character varying(1024),
  cd_id integer NOT NULL,
  mol_structure text,
  comment character varying(2000),
  ignore boolean,
  CONSTRAINT qccompound_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);


    
CREATE INDEX qccompound_cdid_idx ON qc_compound USING btree (cd_id);

  
 