DO $$ 
DECLARE
	m   varchar[];
	arr varchar[] := array[
['ITX_LS_THING_LS_THING', 'ITX_LS_THING_LS_THNG_FRST_IDX', 'FIRST_LS_THING_ID'],
['ITX_LS_THING_LS_THING', 'ITX_LS_THING_LS_THNG_SCND_IDX', 'SECOND_LS_THING_ID'],
['ITX_LS_THING_LS_THING_STATE', 'ITX_THNG_THNG_STATE_THNG_IDX', 'ITX_LS_THING_LS_THING'],
['ITX_LS_THING_LS_THING_VALUE', 'ITX_THNG_THNG_VAL_STATE_IDX', 'LS_STATE']
];  
BEGIN
  FOREACH m SLICE 1 IN ARRAY arr
LOOP 
   RAISE NOTICE '%', m[2];
   IF EXISTS (
    SELECT 1
    FROM   pg_class c
    JOIN   pg_namespace n ON n.oid = c.relnamespace
    WHERE  lower(c.relname) = lower(m[2])
    AND    n.nspname = CURRENT_SCHEMA
    ) THEN
    RAISE NOTICE 'INDEX EXISTS';
   ELSE
     EXECUTE 'CREATE INDEX ' || m[2] || ' ON ' || m[1] || '(' || m[3] || ')';
     RAISE NOTICE 'CREATED INDEX';

END IF;
END LOOP;
END $$;