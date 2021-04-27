BEGIN
	BEGIN
	  EXECUTE IMMEDIATE 'ALTER TABLE label_sequence ADD db_sequence VARCHAR2(255 CHAR)';
    COMMIT;
	EXCEPTION
	  WHEN OTHERS THEN
		IF SQLCODE = -1430 THEN
		  NULL;
		ELSE
		  RAISE;
		END IF;
	END;

	BEGIN
	  EXECUTE IMMEDIATE 'ALTER TABLE label_sequence RENAME COLUMN latest_number TO starting_number';
	EXCEPTION
	  WHEN OTHERS THEN
		IF SQLCODE = -957 THEN
		  NULL;
		ELSE
		  RAISE;
		END IF;
	END;
END;