-- Alter each of the bbchem structure.substructure columns to be of type bit(2048) from bit(2112) by adding 1 to the length.
-- This is because the new substructure column is 64 bits shorter than the old one with a removal of 1 for the small vs bio molecule fingerprint which is handled differently now.
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
        -- Truncate the table to avoid any issues with the new column length acas will fill it back on startup.
        EXECUTE 'TRUNCATE TABLE ' || table_name;
        EXECUTE 'ALTER TABLE ' || table_name || ' ALTER COLUMN substructure TYPE bit(2048)';
    END LOOP;
END $$;
