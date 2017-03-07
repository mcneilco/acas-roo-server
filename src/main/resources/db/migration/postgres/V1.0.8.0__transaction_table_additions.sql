DO $$ 
BEGIN
IF EXISTS (
  SELECT column_name
  FROM   information_schema.columns
  WHERE  table_name='ls_transaction' AND column_name IN ('recorded_by', 'status','type')
  ) THEN
  RAISE NOTICE 'COLUMNS ALREADY EXIST';
 ELSE
   EXECUTE 'ALTER TABLE ls_transaction ADD COLUMN recorded_by character varying(255),
										ADD COLUMN status character varying(64), 
										ADD COLUMN type character varying(64)';
   RAISE NOTICE 'CREATED COLUMNS';

END IF;
END $$;