CREATE TABLE role_type
(
  id bigint NOT NULL,
  type_name character varying(128) NOT NULL,
  version integer,
  CONSTRAINT role_type_pkey PRIMARY KEY (id),
  CONSTRAINT role_type_type_name_key UNIQUE (type_name)
);

CREATE SEQUENCE acas.role_type_pkseq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 3
  CACHE 1;
ALTER TABLE acas.role_type_pkseq
  OWNER TO acas;

CREATE TABLE role_kind
(
  id bigint NOT NULL,
  kind_name character varying(255) NOT NULL,
  version integer,
  ls_type bigint NOT NULL,
  ls_type_and_kind character varying(255) NOT NULL,
  CONSTRAINT role_kind_pkey PRIMARY KEY (id),
  CONSTRAINT role_kind_type_fk FOREIGN KEY (ls_type)
      REFERENCES role_type (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT role_kind_tk_uniq UNIQUE (ls_type_and_kind)
);

CREATE SEQUENCE acas.role_kind_pkseq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 5
  CACHE 1;
ALTER TABLE acas.role_kind_pkseq
  OWNER TO acas;