BEGIN
	BEGIN
	  for i IN (SELECT null FROM user_views WHERE view_name = 'vw_experiment') LOOP
		EXECUTE IMMEDIATE 'DROP VIEW vw_experiment';
	  END LOOP;
	END;
	
	EXECUTE IMMEDIATE '
	CREATE OR REPLACE VIEW vw_experiment AS 
	 SELECT e.protocol_id,
		e.id,
		e.code_name,
		e.ls_type,
		e.ls_kind,
		el.label_text AS experiment_name,
		el2.label_text AS experiment_corp_name,
		e.short_description
	   FROM experiment e
		 JOIN experiment_label el ON e.id = el.experiment_id AND el.ls_type = ''name'' AND el.ls_kind = ''experiment name''
		 LEFT JOIN experiment_label el2 ON e.id = el2.experiment_id AND el2.ls_type = ''name'' AND el2.ls_kind = ''experiment corp name''
	  WHERE e.ignored = ''0'' AND el.ignored = ''0''';
END;