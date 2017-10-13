DO $$
BEGIN
	ALTER TABLE label_sequence DROP CONSTRAINT label_seq_uq;
	ALTER TABLE label_sequence ADD CONSTRAINT label_seq_uq UNIQUE (label_prefix, label_type_and_kind, thing_type_and_kind);
EXCEPTION
	WHEN others THEN
		raise notice '% %', SQLERRM, SQLSTATE;
END$$