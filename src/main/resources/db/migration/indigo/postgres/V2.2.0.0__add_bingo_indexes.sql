DO $$
BEGIN
	IF NOT EXISTS (
		SELECT 1
		  FROM pg_class c
		  JOIN pg_namespace n ON n.oid = c.relnamespace
		  WHERE c.relname = 'parent_mol_idx' --index name
	) THEN
		CREATE index parent_mol_idx ON parent USING bingo_idx (mol_structure bingo.molecule);
	END IF;
	
	IF NOT EXISTS (
		SELECT 1
		  FROM pg_class c
		  JOIN pg_namespace n ON n.oid = c.relnamespace
		  WHERE c.relname = 'salt_form_mol_idx' --index name
	) THEN
		CREATE index salt_form_mol_idx ON parent USING bingo_idx (mol_structure bingo.molecule);
	END IF;
	
	IF NOT EXISTS (
		SELECT 1
		  FROM pg_class c
		  JOIN pg_namespace n ON n.oid = c.relnamespace
		  WHERE c.relname = 'salt_mol_idx' --index name
	) THEN
		CREATE index salt_mol_idx ON parent USING bingo_idx (mol_structure bingo.molecule);
	END IF;
END
$$