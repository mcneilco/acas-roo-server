ALTER TABLE author
  ADD code_name character varying(255),
  ADD deleted boolean NOT NULL,
  ADD ignored boolean NOT NULL,
  ADD ls_kind character varying(255) NOT NULL,
  ADD ls_transaction bigint,
  ADD ls_type character varying(255) NOT NULL,
  ADD ls_type_and_kind character varying(255),
  ADD modified_by character varying(255),
  ADD recorded_by character varying(255) NOT NULL
;
UPDATE author SET code_name = 'AUTH-' || id;
ALTER TABLE author ADD CONSTRAINT author_code_name_key UNIQUE (code_name);

CREATE TABLE author_label
(
  id bigint NOT NULL,
  deleted boolean NOT NULL,
  ignored boolean NOT NULL,
  image_file character varying(255),
  label_text character varying(255) NOT NULL,
  ls_kind character varying(255) NOT NULL,
  ls_transaction bigint,
  ls_type character varying(64) NOT NULL,
  ls_type_and_kind character varying(255),
  modified_date timestamp without time zone,
  physically_labled boolean NOT NULL,
  preferred boolean NOT NULL,
  recorded_by character varying(255) NOT NULL,
  recorded_date timestamp without time zone NOT NULL,
  version integer,
  author_id bigint NOT NULL
)
WITH (
  OIDS=FALSE
);

ALTER TABLE author_label ADD CONSTRAINT author_label_pkey PRIMARY KEY (id);
ALTER TABLE author_label ADD CONSTRAINT author_label_author_fk FOREIGN KEY (author_id) REFERENCES author (id);
  
CREATE TABLE author_state
(
  id bigint NOT NULL,
  comments character varying(512),
  deleted boolean NOT NULL,
  ignored boolean NOT NULL,
  ls_kind character varying(255) NOT NULL,
  ls_transaction bigint,
  ls_type character varying(64) NOT NULL,
  ls_type_and_kind character varying(255),
  modified_by character varying(255),
  modified_date timestamp without time zone,
  recorded_by character varying(255) NOT NULL,
  recorded_date timestamp without time zone NOT NULL,
  version integer,
  author_id bigint NOT NULL
)
WITH (
  OIDS=FALSE
);
ALTER TABLE author_state ADD CONSTRAINT author_state_pkey PRIMARY KEY (id);
ALTER TABLE author_state ADD CONSTRAINT author_state_author_fk FOREIGN KEY (author_id) REFERENCES author (id);
ALTER TABLE author_state ADD CONSTRAINT auths_tk_fk FOREIGN KEY (ls_type_and_kind) references state_kind (ls_type_and_kind);


CREATE TABLE author_value
(
  id bigint NOT NULL,
  blob_value oid,
  clob_value text,
  code_kind character varying(255),
  code_origin character varying(255),
  code_type character varying(255),
  code_type_and_kind character varying(350),
  code_value character varying(255),
  comments character varying(512),
  conc_unit character varying(25),
  concentration double precision,
  date_value timestamp without time zone,
  deleted boolean NOT NULL,
  file_value character varying(512),
  ignored boolean NOT NULL,
  ls_kind character varying(255) NOT NULL,
  ls_transaction bigint,
  ls_type character varying(64) NOT NULL,
  ls_type_and_kind character varying(350),
  modified_by character varying(255),
  modified_date timestamp without time zone,
  number_of_replicates integer,
  numeric_value numeric(38,18),
  operator_kind character varying(10),
  operator_type character varying(25),
  operator_type_and_kind character varying(50),
  public_data boolean NOT NULL,
  recorded_by character varying(255) NOT NULL,
  recorded_date timestamp without time zone NOT NULL,
  sig_figs integer,
  string_value character varying(255),
  uncertainty numeric(38,18),
  uncertainty_type character varying(255),
  unit_kind character varying(25),
  unit_type character varying(25),
  unit_type_and_kind character varying(55),
  url_value character varying(2000),
  version integer,
  author_state_id bigint NOT NULL
)
WITH (
  OIDS=FALSE
);

ALTER TABLE author_value ADD CONSTRAINT author_value_pkey PRIMARY KEY (id);
ALTER TABLE author_value ADD CONSTRAINT author_value_state_fk FOREIGN KEY (author_state_id) REFERENCES author_state (id);
ALTER TABLE author_value ADD CONSTRAINT authv_tk_fk FOREIGN KEY (ls_type_and_kind) references value_kind (ls_type_and_kind);