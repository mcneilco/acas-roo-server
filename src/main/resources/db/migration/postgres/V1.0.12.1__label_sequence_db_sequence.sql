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
		latest_num bigint;
	BEGIN
		for label_sequence_id, db_sequence_name, latest_num in
			SELECT id, COALESCE(db_sequence, regexp_replace(('labelseq_' || label_prefix || '_' || label_type_and_kind || '_' || thing_type_and_kind ), '[^a-zA-Z_]+', '_')), GREATEST(latest_number, 1::bigint) FROM label_sequence
		loop
			IF NOT EXISTS (
				SELECT TRUE FROM pg_class c
				WHERE c.relname = db_sequence_name
			) THEN
				EXECUTE format('CREATE SEQUENCE %s START WITH %s', db_sequence_name, latest_num);
			END IF;
			UPDATE label_sequence SET db_sequence = db_sequence_name WHERE id = label_sequence_id;
		END loop;
	ALTER TABLE label_sequence ALTER COLUMN db_sequence SET NOT NULL;
END$$;