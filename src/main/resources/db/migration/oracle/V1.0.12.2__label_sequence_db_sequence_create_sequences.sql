BEGIN
	DECLARE
	  TYPE labelseqtype IS RECORD (
		label_sequence_id NUMBER(19,0),
		db_sequence_name VARCHAR2(30),
		starting_num NUMBER(19,0)
		);
	  labelseq labelseqtype;
	BEGIN
	  for labelseq in
			(SELECT id as label_sequence_id, SUBSTR(COALESCE(db_sequence, regexp_replace(('labelseq_' || label_prefix || '_' || label_type_and_kind || '_' || thing_type_and_kind ), '[^a-zA-Z_]+', '_')), 0, 29) as db_sequence_name, GREATEST(starting_number, 1) as starting_num FROM label_sequence)
		loop
        	dbms_output.Put_line('Creating new sequence: ' || labelseq.db_sequence_name || ' starting with '||labelseq.starting_num);
			EXECUTE IMMEDIATE 'CREATE SEQUENCE '||labelseq.db_sequence_name||' START WITH '|| labelseq.starting_num;
		  	EXECUTE IMMEDIATE 'UPDATE label_sequence SET db_sequence = :dbseqname WHERE id = :labelseqid' USING labelseq.db_sequence_name, labelseq.label_sequence_id;
		END loop;
	EXCEPTION
    WHEN OTHERS THEN
      IF SQLCODE = -955 THEN
        NULL;
      ELSE
        RAISE;
      END IF;
	END;

	BEGIN
	  EXECUTE IMMEDIATE 'ALTER TABLE label_sequence MODIFY (db_sequence NOT NULL)';
	EXCEPTION
	  WHEN OTHERS THEN
		IF SQLCODE = -1442 THEN
		  NULL;
		ELSE
		  RAISE;
		END IF;
	END;
END;