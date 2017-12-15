BEGIN
	DECLARE
	  v_sql LONG;
	BEGIN
	  v_sql:='CREATE TABLE label_sequence_ls_role (
		id NUMBER(19,0) NOT NULL PRIMARY KEY,
		 version NUMBER(10,0),
		 label_sequence_id NUMBER(19,0) NOT NULL REFERENCES label_sequence(id),
		 ls_role_id NUMBER(19,0) NOT NULL REFERENCES ls_role(id)
	  )';
	  EXECUTE IMMEDIATE v_sql;
	EXCEPTION
	  WHEN OTHERS THEN
		IF SQLCODE = -955 THEN
		  NULL;
		ELSE
		  RAISE;
		END IF;
	END;

	DECLARE
	  v_column_exists number := 0;
	BEGIN
	  SELECT COUNT(*) into v_column_exists
		FROM   USER_INDEXES
		WHERE  INDEX_NAME  IN('idx_labelseq_lsrole_uniq','idx_labelseq_lsrole_uniq');  
	 IF (v_column_exists > 0) THEN
	  dbms_output.put_line('INDEXES ALREADY EXIST');
	 ELSE
		 EXECUTE IMMEDIATE 'CREATE UNIQUE INDEX idx_labelseq_lsrole_uniq ON label_sequence_ls_role (label_sequence_id, ls_role_id)';
		 EXECUTE IMMEDIATE 'CREATE UNIQUE INDEX idx_labelseq_lsrole_uniq ON label_sequence_ls_role (label_sequence_id, ls_role_id)';
		 dbms_output.put_line('CREATED INDEXES');
	END IF;
	EXCEPTION
	  WHEN OTHERS THEN
		 IF SQLCODE = -1408 OR SQLCODE = -955 THEN
		   NULL;
		 ELSE
		  RAISE;
		 END IF;
	END;

	BEGIN
	  EXECUTE IMMEDIATE 'CREATE SEQUENCE labelseq_role_pkseq';
	EXCEPTION
	  WHEN OTHERS THEN
		IF SQLCODE = -955 THEN
		  NULL;
		ELSE
		  RAISE;
		END IF;
	END;
END;