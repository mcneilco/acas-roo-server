CREATE TABLE temp_select_table
(
  id bigint NOT NULL,
  ls_transaction bigint,
  number_var bigint,
  recorded_by character varying(255),
  recorded_date timestamp without time zone NOT NULL,
  string_var character varying(255),
  version integer,
  CONSTRAINT temp_select_table_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE temp_select_table
  OWNER TO acas;
GRANT ALL ON TABLE temp_select_table TO acas;

-- Index: temp_select_number_idx

-- DROP INDEX temp_select_number_idx;

CREATE INDEX temp_select_number_idx
  ON temp_select_table
  USING btree
  (number_var);

-- Index: temp_select_string_idx

-- DROP INDEX temp_select_string_idx;

CREATE INDEX temp_select_string_idx
  ON temp_select_table
  USING btree
  (string_var COLLATE pg_catalog."default");

-- Index: temp_select_trxn_idx

-- DROP INDEX temp_select_trxn_idx;

CREATE INDEX temp_select_trxn_idx
  ON temp_select_table
  USING btree
  (ls_transaction);

