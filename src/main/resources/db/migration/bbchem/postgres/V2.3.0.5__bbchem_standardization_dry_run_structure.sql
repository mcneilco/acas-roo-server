CREATE TABLE bbchem_standardization_dry_run_structure (
    id bigint NOT NULL,
    mol text NOT NULL,
    substructure BIT(2048),
    similarity BIT(1024),
    pre_reg character(40) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    reg character(40) NOT NULL,
    version integer
);

ALTER TABLE ONLY bbchem_standardization_dry_run_structure
    ADD CONSTRAINT bbchem_standardization_dry_run_structure_pkey PRIMARY KEY (id);
CREATE INDEX bbchem_standardization_dry_run_structure_pk_idx ON bbchem_standardization_dry_run_structure(id);
CREATE INDEX bbchem_standardization_dry_run_structure_pre_reg_idx ON bbchem_standardization_dry_run_structure(pre_reg);
CREATE INDEX bbchem_standardization_dry_run_structure_reg_idx ON bbchem_standardization_dry_run_structure(reg);
