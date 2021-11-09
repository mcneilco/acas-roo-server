CREATE TABLE rdkit_structure (
    id bigint NOT NULL,
    mol text NOT NULL,
    pre_reg character(40) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    reg character(40) NOT NULL,
    version integer
);

ALTER TABLE ONLY rdkit_structure
    ADD CONSTRAINT rdkit_structure_pkey PRIMARY KEY (id);
CREATE INDEX rdkit_structure_pk_idx ON rdkit_structure(id);
CREATE INDEX rdkit_structure_pre_reg_idx ON rdkit_structure(pre_reg);
CREATE INDEX rdkit_structure_reg_idx ON rdkit_structure(reg);


CREATE TABLE rdkit_salt_structure (
    id bigint NOT NULL,
    mol text NOT NULL,
    pre_reg character(40) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    reg character(40) NOT NULL,
    version integer
);

ALTER TABLE ONLY rdkit_salt_structure
    ADD CONSTRAINT rdkit_salt_structure_pkey PRIMARY KEY (id);
CREATE INDEX rdkit_salt_structure_pk_idx ON rdkit_salt_structure(id);
CREATE INDEX rdkit_salt_structure_pre_reg_idx ON rdkit_salt_structure(pre_reg);
CREATE INDEX rdkit_salt_structure_reg_idx ON rdkit_salt_structure(reg);

CREATE TABLE rdkit_salt_form_structure (
    id bigint NOT NULL,
    mol text NOT NULL,
    pre_reg character(40) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    reg character(40) NOT NULL,
    version integer
);

ALTER TABLE ONLY rdkit_salt_form_structure
    ADD CONSTRAINT rdkit_salt_form_structure_pkey PRIMARY KEY (id);
CREATE INDEX rdkit_salt_form_structure_pk_idx ON rdkit_salt_form_structure(id);
CREATE INDEX rrdkit_salt_form_structure_pre_reg_idx ON rdkit_salt_form_structure(pre_reg);
CREATE INDEX rdkit_salt_form_structure_reg_idx ON rdkit_salt_form_structure(reg);
