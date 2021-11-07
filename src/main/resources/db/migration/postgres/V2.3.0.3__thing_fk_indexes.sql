DO $$ 
DECLARE
	m   varchar[];
	arr varchar[] := array[
['ls_thing_value_lsthing_state_id_idx', 'create index ls_thing_value_lsthing_state_id_idx on ls_thing_value(lsthing_state_id)'],
['ls_thing_state_lsthing_id_idx', 'create index ls_thing_state_lsthing_id_idx on ls_thing_state(lsthing_id)'],
['ls_thing_label_lsthing_id_idx', 'create index ls_thing_label_lsthing_id_idx on ls_thing_label(lsthing_id)']
  ];
BEGIN
  FOREACH m SLICE 1 IN ARRAY arr
LOOP 
   RAISE NOTICE '%', m[2];
   IF EXISTS (
    SELECT 1
    FROM   pg_class c
    JOIN   pg_namespace n ON n.oid = c.relnamespace
    WHERE  lower(c.relname) = lower(m[1])
    AND    n.nspname = CURRENT_SCHEMA
    ) THEN
    RAISE NOTICE 'INDEX EXISTS';
   ELSE
     EXECUTE m[2];
     RAISE NOTICE 'CREATED INDEX %', m[1];

END IF;
END LOOP;
END $$;