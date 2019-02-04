ALTER TABLE parent ADD is_mixture BIT;
ALTER TABLE parent ADD compound_type [numeric](19, 0) NULL;
ALTER TABLE parent ADD parent_annotation [numeric](19, 0) NULL;

