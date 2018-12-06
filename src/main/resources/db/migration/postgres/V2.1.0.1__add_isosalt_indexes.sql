DO $$
BEGIN
	IF NOT EXISTS (
		SELECT
		  U.usename                AS user_name,
		  ns.nspname               AS schema_name,
		  idx.indrelid :: REGCLASS AS table_name,
		  i.relname                AS index_name,
		  idx.indisunique          AS is_unique,
		  idx.indisprimary         AS is_primary,
		  am.amname                AS index_type,
		  idx.indkey,
		       ARRAY(
		           SELECT pg_get_indexdef(idx.indexrelid, k + 1, TRUE)
		           FROM
		             generate_subscripts(idx.indkey, 1) AS k
		           ORDER BY k
		       ) AS index_keys,
		  (idx.indexprs IS NOT NULL) OR (idx.indkey::int[] @> array[0]) AS is_functional,
		  idx.indpred IS NOT NULL AS is_partial
		FROM pg_index AS idx
		  JOIN pg_class AS i
		    ON i.oid = idx.indexrelid
		  JOIN pg_am AS am
		    ON i.relam = am.oid
		  JOIN pg_namespace AS NS ON i.relnamespace = NS.OID
		  JOIN pg_user AS U ON i.relowner = U.usesysid
		WHERE NOT nspname LIKE 'pg%' -- Excluding system tables
		AND idx.indrelid :: REGCLASS :: TEXT = 'iso_salt' --table name
		AND 'salt_form' = --column name
		ALL (ARRAY( SELECT pg_get_indexdef(idx.indexrelid, k + 1, TRUE) FROM generate_subscripts(idx.indkey, 1) AS k ORDER BY k))
	) THEN
		CREATE INDEX iso_salt_salt_form_idx ON iso_salt (salt_form); --table name (column name)
	END IF;
	
	IF NOT EXISTS (
		SELECT
		  U.usename                AS user_name,
		  ns.nspname               AS schema_name,
		  idx.indrelid :: REGCLASS AS table_name,
		  i.relname                AS index_name,
		  idx.indisunique          AS is_unique,
		  idx.indisprimary         AS is_primary,
		  am.amname                AS index_type,
		  idx.indkey,
		       ARRAY(
		           SELECT pg_get_indexdef(idx.indexrelid, k + 1, TRUE)
		           FROM
		             generate_subscripts(idx.indkey, 1) AS k
		           ORDER BY k
		       ) AS index_keys,
		  (idx.indexprs IS NOT NULL) OR (idx.indkey::int[] @> array[0]) AS is_functional,
		  idx.indpred IS NOT NULL AS is_partial
		FROM pg_index AS idx
		  JOIN pg_class AS i
		    ON i.oid = idx.indexrelid
		  JOIN pg_am AS am
		    ON i.relam = am.oid
		  JOIN pg_namespace AS NS ON i.relnamespace = NS.OID
		  JOIN pg_user AS U ON i.relowner = U.usesysid
		WHERE NOT nspname LIKE 'pg%' -- Excluding system tables
		AND idx.indrelid :: REGCLASS :: TEXT = 'iso_salt' --table name
		AND 'salt' = --column name
		ALL (ARRAY( SELECT pg_get_indexdef(idx.indexrelid, k + 1, TRUE) FROM generate_subscripts(idx.indkey, 1) AS k ORDER BY k))
	) THEN
		CREATE INDEX iso_salt_salt_idx ON iso_salt (salt);  --table name (column name)
	END IF;
	
	IF NOT EXISTS (
		SELECT
		  U.usename                AS user_name,
		  ns.nspname               AS schema_name,
		  idx.indrelid :: REGCLASS AS table_name,
		  i.relname                AS index_name,
		  idx.indisunique          AS is_unique,
		  idx.indisprimary         AS is_primary,
		  am.amname                AS index_type,
		  idx.indkey,
		       ARRAY(
		           SELECT pg_get_indexdef(idx.indexrelid, k + 1, TRUE)
		           FROM
		             generate_subscripts(idx.indkey, 1) AS k
		           ORDER BY k
		       ) AS index_keys,
		  (idx.indexprs IS NOT NULL) OR (idx.indkey::int[] @> array[0]) AS is_functional,
		  idx.indpred IS NOT NULL AS is_partial
		FROM pg_index AS idx
		  JOIN pg_class AS i
		    ON i.oid = idx.indexrelid
		  JOIN pg_am AS am
		    ON i.relam = am.oid
		  JOIN pg_namespace AS NS ON i.relnamespace = NS.OID
		  JOIN pg_user AS U ON i.relowner = U.usesysid
		WHERE NOT nspname LIKE 'pg%' -- Excluding system tables
		AND idx.indrelid :: REGCLASS :: TEXT = 'iso_salt' --table name
		AND 'isotope' = --column name
		ALL (ARRAY( SELECT pg_get_indexdef(idx.indexrelid, k + 1, TRUE) FROM generate_subscripts(idx.indkey, 1) AS k ORDER BY k))
	) THEN
		CREATE INDEX iso_salt_isotope_idx ON iso_salt (isotope); --table name (column name)
	END IF;
END
$$