DO $$ 
DECLARE
	m   varchar[];
	arr varchar[] := array[
['SUBJECT_STATE', 'SUBJ_STATE_SUBJ_IDX', 'SUBJECT_ID'],
['SUBJECT_VALUE', 'SUBJ_VAL_SUBJ_STATE_IDX', 'SUBJECT_STATE_ID'],
['SUBJECT_LABEL', 'SUBJ_LBL_SUBJ_IDX', 'SUBJECT_ID']
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