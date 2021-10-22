DO $$
BEGIN
	IF NOT EXISTS (
		SELECT 1
		  FROM pg_class c
		  JOIN pg_namespace n ON n.oid = c.relnamespace
		  WHERE c.relname = 'dry_run_compound_mol_idx' --index name
	) THEN
		CREATE index dry_run_compound_mol_idx ON dry_run_compound USING bingo_idx (mol_structure bingo.molecule);
	END IF;
END
$$;