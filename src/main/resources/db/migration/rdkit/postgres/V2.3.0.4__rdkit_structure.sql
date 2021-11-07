CREATE TABLE IF NOT EXISTS rdkit_structure
(
  id bigint NOT NULL,
  pre_reg CHAR(40) NOT NULL,
  reg CHAR(40) NOT NULL,
  mol TEXT NOT NULL,
  CONSTRAINT rdkit_structure_pkey PRIMARY KEY (id)
);

CREATE INDEX rdkit_structure_pk_idx ON rdkit_structure(id);
CREATE INDEX rdkit_structure_pre_reg_idx ON rdkit_structure(pre_reg);
CREATE INDEX rdkit_structure_reg_idx ON rdkit_structure(reg);


-- LOT - Original structure/As drawn
-- Parent - LD displayed Structure (standardized structure)
-- Structure - Same as PARENT