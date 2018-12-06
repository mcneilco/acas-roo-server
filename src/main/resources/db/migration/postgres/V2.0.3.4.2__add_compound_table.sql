CREATE TABLE compound
(
  id bigint NOT NULL,
  version integer,
  corp_name character varying(255),
  external_id character varying(255),
  cd_id integer NOT NULL,
  ignored boolean,
  deleted boolean,
  created_date timestamp without time zone,
  modified_date timestamp without time zone,
  CONSTRAINT compound_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);


  
CREATE INDEX compound_cdid_idx ON compound USING btree (cd_id);
  
  
 