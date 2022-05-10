DO $$ 
DECLARE
	m   varchar[];
	arr varchar[] := array[
['parent_alias_ls_type_idx', 'create index parent_alias_ls_type_idx on parent_alias(ls_type)'],
['parent_alias_ls_kind_idx', 'create index parent_alias_ls_kind_idx on parent_alias(ls_kind)'],
['parent_alias_ignored_idx', 'create index parent_alias_ignored_idx on parent_alias(ignored)'],
['parent_alias_deleted_idx', 'create index parent_alias_deleted_idx on parent_alias(deleted)'],
['parent_alias_alias_name_idx', 'create index parent_alias_alias_name_idx on parent_alias(alias_name)']
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