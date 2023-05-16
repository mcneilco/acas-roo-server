-- Set the comment fields of the bbchem structure tables to be of type text
DO $$ 
DECLARE
	table_names text[] := array[ 
        'bbchem_standardization_dry_run_structure',
        'bbchem_dry_run_structure',
        'bbchem_parent_structure',
        'bbchem_salt_form_structure',
        'bbchem_salt_structure'
    ];
    column_names text[] := array[ 'registration_comment', 'standardization_comment' ];
    tbl_name text;
    col_name text;
BEGIN
    FOREACH tbl_name IN ARRAY table_names
    LOOP 
        FOREACH col_name IN ARRAY column_names
        LOOP
            -- If this is fresh instance then the columns won't exist at all so we create them
            IF NOT EXISTS (
                SELECT 1
                FROM information_schema.columns
                WHERE table_name = tbl_name
                AND column_name = col_name
            ) THEN
                RAISE NOTICE 'Column % does not exist in table % so creating it', col_name, tbl_name;
                EXECUTE 'ALTER TABLE ' || tbl_name || ' ADD COLUMN ' || col_name || ' text';
                CONTINUE;
            END IF;
            -- If we got here, then the columns exist but may be the wrong type varchar(255) created by hibernate
            -- Check if the registration_comment and standardization_comment columns are already text columns.
            -- If they are not, then alter them to be text columns.
            IF NOT EXISTS (
                SELECT 1
                FROM information_schema.columns
                WHERE table_name = tbl_name
                AND column_name = col_name
                AND data_type = 'text'
            ) THEN
                RAISE NOTICE 'Altering column % in table % to be of type text', col_name, tbl_name;
                EXECUTE 'ALTER TABLE ' || tbl_name || ' ALTER COLUMN ' || col_name || ' TYPE text';
            END IF;
        END LOOP;
    END LOOP;
END $$;
