-- Alter each of the bbchem structure.substructure columns to be of type bit(2112) from bit(2048) by adding 1 to the length.
-- This is because the new substructure column is 64 bits longer than the old one with an additional 1 for the small vs bio molecule fingerprint.
DO $$ 
DECLARE
	table_names text[] := array[ 
        'bbchem_standardization_dry_run_structure',
        'bbchem_dry_run_structure',
        'bbchem_parent_structure',
        'bbchem_salt_form_structure',
        'bbchem_salt_structure'
    ];
    table_name text;
BEGIN
    FOREACH table_name IN ARRAY table_names
    LOOP 
        EXECUTE 'ALTER TABLE ' || table_name || ' ALTER COLUMN substructure TYPE bit(2112) USING substructure || B''1''::bit(64);';
    END LOOP;
END $$;
