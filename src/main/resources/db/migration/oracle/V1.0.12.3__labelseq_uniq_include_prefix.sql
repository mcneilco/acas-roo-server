BEGIN
  EXECUTE IMMEDIATE 'ALTER TABLE label_sequence DROP CONSTRAINT label_seq_uq';
  EXECUTE IMMEDIATE 'ALTER TABLE label_sequence ADD CONSTRAINT label_seq_uq UNIQUE (label_prefix, label_type_and_kind, thing_type_and_kind)';
  COMMIT;
EXCEPTION
  WHEN OTHERS THEN
	IF SQLCODE = -2443 THEN
	  NULL;
	ELSE
	  RAISE;
	END IF;
END;