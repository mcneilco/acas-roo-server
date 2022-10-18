DO $$ 
DECLARE
	m   varchar[];
	arr varchar[] := array[
['stndzn_dry_run_stereo_category_idx', 'create index stndzn_dry_run_stereo_category_idx on standardization_dry_run_compound(stereo_category)'],
['stndzn_dry_run_stereo_comment_idx', 'create index stndzn_dry_run_stereo_comment_idx on standardization_dry_run_compound(stereo_comment)'],
['parent_stereocomment_idx', 'create index parent_stereocomment_idx on parent(stereo_comment)']
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