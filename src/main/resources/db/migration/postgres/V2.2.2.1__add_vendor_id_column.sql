DO $$ 
    BEGIN
        BEGIN
            ALTER TABLE lot ADD COLUMN vendorid character varying(255);
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'COLUMN EXISTS';
        END;
    END;
$$
