DO $$
BEGIN

IF (SELECT TRUE
	FROM   pg_attribute 
	WHERE  attrelid = 'label_sequence'::regclass  -- cast to a registered class (table)
	AND    attname = 'db_sequence'
	AND    NOT attisdropped  -- exclude dropped (dead) columns
	) 
	IS NULL THEN
    ALTER TABLE label_sequence ADD COLUMN db_sequence character varying(255);
END IF;

END$$;

DO $$
	DECLARE
		label_sequence_id bigint;
		db_sequence_name varchar;
		starting_num bigint;
		seq_exists int;
	BEGIN	
		IF EXISTS (SELECT TRUE
			FROM   pg_attribute 
			WHERE  attrelid = 'label_sequence'::regclass  -- cast to a registered class (table)
			AND    attname = 'latest_number'
			AND    NOT attisdropped  -- exclude dropped (dead) columns
			) 
			THEN
				ALTER TABLE label_sequence RENAME COLUMN latest_number TO starting_number;
		ELSE
			RAISE NOTICE 'label_sequence.latest_number has already been renamed to starting_number.';
		END IF;
		for label_sequence_id, db_sequence_name, starting_num in
			SELECT id, COALESCE(db_sequence, regexp_replace(('labelseq_' || label_prefix || '_' || label_type_and_kind || '_' || thing_type_and_kind ), '[^a-zA-Z_]+', '_')), GREATEST(starting_num, 1::bigint) FROM label_sequence
		loop
			EXECUTE ('SELECT 1 FROM pg_class c WHERE c.relname = LOWER(' || quote_literal(db_sequence_name) || ')') INTO seq_exists;
			IF seq_exists IS NOT NULL THEN
				RAISE NOTICE 'Sequence already exists: %', db_sequence_name;
			ELSE
				EXECUTE format('CREATE SEQUENCE %s START WITH %s', db_sequence_name, starting_num);
			END IF;
			UPDATE label_sequence SET db_sequence = db_sequence_name WHERE id = label_sequence_id;
		END loop;
		ALTER TABLE label_sequence ALTER COLUMN db_sequence SET NOT NULL;
END$$;