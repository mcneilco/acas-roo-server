DECLARE
  v_column_exists number := 0;
BEGIN
  SELECT COUNT(*) into v_column_exists
    FROM   USER_INDEXES
    WHERE  INDEX_NAME  IN('SUBJECT_VALUE_DATEVAL_IDX');  
 IF (v_column_exists > 0) THEN
  dbms_output.put_line('INDEXES ALREADY EXIST');
 ELSE
     EXECUTE IMMEDIATE 'CREATE INDEX SUBJECT_VALUE_DATEVAL_IDX ON SUBJECT_VALUE (DATE_VALUE)';
     dbms_output.put_line('CREATED INDEXES');
END IF;
END;