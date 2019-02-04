DO $$ 
    BEGIN
        BEGIN
            ALTER TABLE lot ADD COLUMN tare_weight double precision,
							ADD COLUMN total_amount_stored double precision,
							ADD COLUMN     tare_weight_units bigint,
							ADD COLUMN     total_amount_stored_units bigint;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'COLUMN EXISTS';
        END;
    END;
$$
