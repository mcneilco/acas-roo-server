CREATE TABLE bbchem_parent_structure (
    id bigint NOT NULL,
    mol text NOT NULL,
    substructure BIT(2048) NOT NULL,
    similarity BIT(1024) NOT NULL,
    pre_reg character(40) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    reg character(40) NOT NULL,
    version integer
);

ALTER TABLE ONLY bbchem_parent_structure
    ADD CONSTRAINT bbchem_parent_structure_pkey PRIMARY KEY (id);
CREATE INDEX bbchem_parent_structure_pk_idx ON bbchem_parent_structure(id);
CREATE INDEX bbchem_parent_structure_pre_reg_idx ON bbchem_parent_structure(pre_reg);
CREATE INDEX bbchem_parent_structure_reg_idx ON bbchem_parent_structure(reg);


CREATE TABLE bbchem_salt_structure (
    id bigint NOT NULL,
    mol text NOT NULL,
    substructure BIT(2048) NOT NULL,
    similarity BIT(1024) NOT NULL,
    pre_reg character(40) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    reg character(40) NOT NULL,
    version integer
);

ALTER TABLE ONLY bbchem_salt_structure
    ADD CONSTRAINT bbchem_salt_structure_pkey PRIMARY KEY (id);
CREATE INDEX bbchem_salt_structure_pk_idx ON bbchem_salt_structure(id);
CREATE INDEX bbchem_salt_structure_pre_reg_idx ON bbchem_salt_structure(pre_reg);
CREATE INDEX bbchem_salt_structure_reg_idx ON bbchem_salt_structure(reg);

CREATE TABLE bbchem_salt_form_structure (
    id bigint NOT NULL,
    mol text NOT NULL,
    substructure BIT(2048) NOT NULL,
    similarity BIT(1024) NOT NULL,
    pre_reg character(40) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    reg character(40) NOT NULL,
    version integer
);

ALTER TABLE ONLY bbchem_salt_form_structure
    ADD CONSTRAINT bbchem_salt_form_structure_pkey PRIMARY KEY (id);
CREATE INDEX bbchem_salt_form_structure_pk_idx ON bbchem_salt_form_structure(id);
CREATE INDEX bbchem_salt_form_structure_pre_reg_idx ON bbchem_salt_form_structure(pre_reg);
CREATE INDEX bbchem_salt_form_structure_reg_idx ON bbchem_salt_form_structure(reg);

CREATE TABLE bbchem_dry_run_structure (
    id bigint NOT NULL,
    mol text NOT NULL,
    substructure BIT(2048) NOT NULL,
    similarity BIT(1024) NOT NULL,
    pre_reg character(40) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    reg character(40) NOT NULL,
    version integer
);

ALTER TABLE ONLY bbchem_dry_run_structure
    ADD CONSTRAINT bbchem_dry_run_structure_pkey PRIMARY KEY (id);
CREATE INDEX bbchem_dry_run_structure_pk_idx ON bbchem_dry_run_structure(id);
CREATE INDEX bbchem_dry_run_structure_pre_reg_idx ON bbchem_dry_run_structure(pre_reg);
CREATE INDEX bbchem_dry_run_structure_reg_idx ON bbchem_dry_run_structure(reg);
