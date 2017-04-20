DECLARE
  v_column_exists number := 0;
BEGIN
  SELECT COUNT(*) into v_column_exists
  	from user_tab_cols
  	WHERE table_name='LS_TRANSACTION' AND column_name IN ('RECORDED_BY', 'STATUS','TYPE');
  
 IF (v_column_exists > 0) THEN
  dbms_output.put_line('COLUMNS ALREADY EXIST');
 ELSE
   EXECUTE IMMEDIATE 'ALTER TABLE LS_TRANSACTION ADD ( "RECORDED_BY" VARCHAR2(255 CHAR), "STATUS" VARCHAR2(64 CHAR), "TYPE" VARCHAR2(64 CHAR) )';
   dbms_output.put_line('CREATED COLUMNS');

END IF;
END;