DO $$ 
    BEGIN
        BEGIN
            ALTER TABLE salt ADD COLUMN charge integer default 0;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column charge already exists in salt table.';
        END;
    END;
$$