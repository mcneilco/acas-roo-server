ALTER TABLE author
  ADD (code_name VARCHAR2(255 CHAR),
  deleted NUMBER(1,0),
  ignored NUMBER(1,0),
  ls_kind VARCHAR2(255 CHAR),
  ls_transaction NUMBER(19,0),
  ls_type VARCHAR2(255 CHAR),
  ls_type_and_kind VARCHAR2(255 CHAR),
  modified_by VARCHAR2(255 CHAR),
  recorded_by VARCHAR2(255 CHAR)
  )
;
UPDATE author SET code_name = 'AUTH-' || id;
ALTER TABLE author ADD CONSTRAINT author_code_name_key UNIQUE (code_name);
ALTER TABLE author MODIFY (deleted NOT NULL ENABLE);
ALTER TABLE author MODIFY (ignored NOT NULL ENABLE);
ALTER TABLE author MODIFY (ls_kind NOT NULL ENABLE);
ALTER TABLE author MODIFY (ls_type NOT NULL ENABLE);
ALTER TABLE author MODIFY (recorded_by NOT NULL ENABLE);


CREATE TABLE author_label
(
  id NUMBER(19,0) NOT NULL,
  deleted NUMBER(1,0) NOT NULL,
  ignored NUMBER(1,0) NOT NULL,
  image_file VARCHAR2(255 CHAR),
  label_text VARCHAR2(255 CHAR) NOT NULL,
  ls_kind VARCHAR2(255 CHAR) NOT NULL,
  ls_transaction NUMBER(19,0),
  ls_type VARCHAR2(64 CHAR) NOT NULL,
  ls_type_and_kind VARCHAR2(255 CHAR),
  modified_date TIMESTAMP (6),
  physically_labled NUMBER(1,0) NOT NULL,
  preferred NUMBER(1,0) NOT NULL,
  recorded_by VARCHAR2(255 CHAR) NOT NULL,
  recorded_date TIMESTAMP (6) NOT NULL,
  version NUMBER(10,0),
  author_id NUMBER(19,0) NOT NULL,
  CONSTRAINT author_label_pkey PRIMARY KEY (id),
  CONSTRAINT author_label_author_fk FOREIGN KEY (author_id) REFERENCES author (id)
);


CREATE TABLE author_state
(
  id NUMBER(19,0) NOT NULL,
  comments VARCHAR2(512 CHAR),
  deleted NUMBER(1,0) NOT NULL,
  ignored NUMBER(1,0) NOT NULL,
  ls_kind VARCHAR2(255 CHAR) NOT NULL,
  ls_transaction NUMBER(19,0),
  ls_type VARCHAR2(64 CHAR) NOT NULL,
  ls_type_and_kind VARCHAR2(255 CHAR),
  modified_by VARCHAR2(255 CHAR),
  modified_date TIMESTAMP (6),
  recorded_by VARCHAR2(255 CHAR) NOT NULL,
  recorded_date TIMESTAMP (6) NOT NULL,
  version NUMBER(10,0),
  author_id NUMBER(19,0) NOT NULL,
CONSTRAINT author_state_pkey PRIMARY KEY (id),
CONSTRAINT author_state_author_fk FOREIGN KEY (author_id) REFERENCES author (id),
CONSTRAINT auths_tk_fk FOREIGN KEY (ls_type_and_kind) references state_kind (ls_type_and_kind)
);


-- Table: author_value

-- DROP TABLE author_value;

CREATE TABLE author_value
(
  id NUMBER(19,0) NOT NULL,
  blob_value BLOB,
  clob_value CLOB,
  code_kind VARCHAR2(255 CHAR),
  code_origin VARCHAR2(255 CHAR),
  code_type VARCHAR2(255 CHAR),
  code_type_and_kind VARCHAR2(350 CHAR),
  code_value VARCHAR2(255 CHAR),
  comments VARCHAR2(512 CHAR),
  conc_unit VARCHAR2(25 CHAR),
  concentration FLOAT(126),
  date_value TIMESTAMP (6),
  deleted NUMBER(1,0) NOT NULL,
  file_value VARCHAR2(512 CHAR),
  ignored NUMBER(1,0) NOT NULL,
  ls_kind VARCHAR2(255 CHAR) NOT NULL,
  ls_transaction NUMBER(19,0),
  ls_type VARCHAR2(64 CHAR) NOT NULL,
  ls_type_and_kind VARCHAR2(350 CHAR),
  modified_by VARCHAR2(255 CHAR),
  modified_date TIMESTAMP (6),
  number_of_replicates NUMBER(10,0),
  numeric_value NUMBER(38,18),
  operator_kind VARCHAR2(10 CHAR),
  operator_type VARCHAR2(25 CHAR),
  operator_type_and_kind VARCHAR2(50 CHAR),
  public_data NUMBER(1,0) NOT NULL,
  recorded_by VARCHAR2(255 CHAR) NOT NULL,
  recorded_date TIMESTAMP (6) NOT NULL,
  sig_figs NUMBER(10,0),
  string_value VARCHAR2(255 CHAR),
  uncertainty NUMBER(38,18),
  uncertainty_type VARCHAR2(255 CHAR),
  unit_kind VARCHAR2(25 CHAR),
  unit_type VARCHAR2(25 CHAR),
  unit_type_and_kind VARCHAR2(55 CHAR),
  url_value VARCHAR2(2000 CHAR),
  version NUMBER(10,0),
  author_state_id NUMBER(19,0) NOT NULL,
  CONSTRAINT author_value_pkey PRIMARY KEY (id),
  CONSTRAINT author_value_state_fk FOREIGN KEY (author_state_id) REFERENCES author_state (id),
  CONSTRAINT authv_tk_fk FOREIGN KEY (ls_type_and_kind) references value_kind (ls_type_and_kind)
);

