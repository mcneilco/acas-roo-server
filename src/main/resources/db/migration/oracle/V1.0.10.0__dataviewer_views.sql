BEGIN
	BEGIN
	  for i IN (SELECT null FROM user_views WHERE view_name = 'VW_DV_EXPT_AG_KINDS') LOOP
		EXECUTE IMMEDIATE 'DROP VIEW VW_DV_EXPT_AG_KINDS';
	  END LOOP;
	END;

	BEGIN
	  for i IN (SELECT null FROM user_views WHERE view_name = 'v_api_dv_ag_results') LOOP
		EXECUTE IMMEDIATE 'DROP VIEW v_api_dv_ag_results';
	  END LOOP;
	END;

	BEGIN
	  for i IN (SELECT null FROM user_views WHERE view_name = 'dv_api_all_ag_results') LOOP
		EXECUTE IMMEDIATE 'DROP VIEW dv_api_all_ag_results';
	  END LOOP;
	END;

	BEGIN
	  for i IN (SELECT null FROM user_views WHERE view_name = 'vw_dv_ag_results') LOOP
		EXECUTE IMMEDIATE 'DROP VIEW vw_dv_ag_results';
	  END LOOP;
	END;
	
	BEGIN
		EXECUTE IMMEDIATE '
		CREATE OR REPLACE VIEW V_API_DV_PROTOCOL 
		AS
		SELECT distinct prot.id as protocol_id,
		prot.code_name as protocol_codeName, 
		pl.label_text as protocol_name
		FROM protocol prot
		JOIN protocol_label pl ON prot.id = pl.protocol_id AND pl.ignored <> ''1'' 
		AND pl.preferred = ''1'' AND pl.ls_type = ''name'' 
		WHERE prot.ignored = ''0''
		';
	END;

	BEGIN
	  for i IN (SELECT null FROM user_views WHERE view_name = 'VW_API_DV_EXPERIMENT') LOOP
		EXECUTE IMMEDIATE 'DROP VIEW VW_API_DV_EXPERIMENT';
	  END LOOP;
	END;

  EXECUTE IMMEDIATE '
  CREATE OR REPLACE VIEW VW_API_DV_EXPERIMENT 
  AS
  SELECT distinct 
  exp.id as experiment_id,
  exp.code_name as experiment_codeName,
  el.label_text as experiment_name,
  el2.label_text as experiment_corpName,
  exp.protocol_id
  FROM experiment exp
  JOIN experiment_label el ON exp.id = el.experiment_id AND el.ignored <> ''1'' 
    AND el.preferred = ''1'' AND el.ls_type = ''name''
  LEFT OUTER JOIN experiment_label el2 ON exp.id = el2.experiment_id AND el2.ignored <> ''1'' AND el2.ls_type = ''corpName'' AND el2.ls_kind = ''experiment corpName'' 
  WHERE exp.ignored = ''0''';

	BEGIN
	  for i IN (SELECT null FROM user_views WHERE view_name = 'V_API_DV_EXPERIMENT') LOOP
		EXECUTE IMMEDIATE 'DROP VIEW V_API_DV_EXPERIMENT';
	  END LOOP;
	END;
  
  EXECUTE IMMEDIATE '
    CREATE OR REPLACE VIEW V_API_DV_EXPERIMENT 
    AS
    SELECT * FROM VW_API_DV_EXPERIMENT'; 

	EXECUTE IMMEDIATE 'CREATE OR REPLACE VIEW vw_dv_ag_results 
    AS
    select 
     p.id as protocol_id, 
     p.code_name as protocol_code,
     e.id as experiment_id,
     e.code_name as experiment_code,
     ag.id as ag_id,
     ags.id as state_id,
     agv.id as value_id,
     agv2.code_value as tested_lot,
     agv2.concentration as tested_conc,
     CASE
       WHEN agv4.numeric_value IS NOT NULL AND agv2.concentration IS NOT NULL THEN ((((agv2.conc_unit || '' and '') || agv4.numeric_value) || '' '') || agv4.unit_kind)
       WHEN agv4.numeric_value IS NOT NULL THEN ((agv4.numeric_value || '' '') || agv4.unit_kind)
       ELSE agv2.conc_unit
     END AS tested_conc_unit,
     agv.ls_type,
     CASE
       WHEN agv.ls_type = ''inlineFileValue'' THEN agv.ls_type_and_kind
       ELSE agv.ls_kind
     END AS ls_kind,
     agv.operator_kind as operator,
     agv.numeric_value,
     agv.uncertainty,
     agv.unit_kind,
     CASE
       WHEN agv.ls_type = ''fileValue'' THEN agv.file_value
       WHEN agv.ls_type = ''inlineFileValue'' THEN agv.file_value
       WHEN agv.ls_type = ''urlValue'' THEN agv.url_value
       WHEN agv.ls_type = ''dateValue'' THEN to_char(agv.date_value, ''yyyy-mm-dd'')
       WHEN agv.ls_type = ''codeValue'' THEN agv.code_value
       ELSE COALESCE(agv.string_value, TO_CHAR(SUBSTR(agv.clob_value, 0, 3999)))
     END AS string_value,
     agv.comments,
     agv.recorded_date AS recorded_date,
     agv.public_data
    FROM protocol p
       JOIN experiment e ON p.id = e.protocol_id AND p.ignored = ''0'' AND e.ignored = ''0''
       JOIN experiment_analysisgroup eag ON e.id = eag.experiment_id
       JOIN analysis_group ag ON eag.analysis_group_id = ag.id AND ag.ignored = ''0''
       JOIN analysis_group_state ags ON ags.analysis_group_id = ag.id AND ags.ignored = ''0''
       JOIN analysis_group_value agv ON agv.analysis_state_id = ags.id AND agv.ls_kind <> ''batch code'' AND agv.ls_kind <> ''time''
       JOIN analysis_group_value agv2 ON agv2.analysis_state_id = ags.id AND agv2.ls_kind = ''batch code''
       LEFT JOIN analysis_group_value agv4 ON agv4.analysis_state_id = ags.id AND agv4.ls_kind = ''time''
      WHERE  agv.ignored = ''0''';
  EXECUTE IMMEDIATE 'CREATE OR REPLACE VIEW V_API_DV_AG_RESULTS AS SELECT * FROM vw_dv_ag_results';
  EXECUTE IMMEDIATE 'CREATE OR REPLACE VIEW DV_API_ALL_AG_RESULTS AS SELECT * FROM vw_dv_ag_results';
  
  EXECUTE IMMEDIATE '
	CREATE OR REPLACE VIEW VW_DV_EXPT_AG_KINDS 
	AS
	SELECT distinct agr.protocol_id, agr.protocol_code,  pl.label_text as protocol_name, 
	agr.experiment_id, agr.experiment_code, el.label_text as experiment_name, 
	agr.ls_type, agr.ls_kind 
	from vw_dv_ag_results agr
	JOIN experiment_label el on agr.experiment_id = el.experiment_id 
	AND el.ignored = ''0'' and el.preferred = ''1'' 
	AND el.ls_type = ''name'' AND el.ls_kind = ''experiment name''
	JOIN protocol_label pl on agr.protocol_id = pl.protocol_id 
	AND pl.ignored = ''0'' and pl.preferred = ''1'' 
	AND pl.ls_type = ''name'' AND pl.ls_kind = ''protocol name''
	where agr.ls_type in (''stringValue'', ''numericValue'')';


	--View for Dataviewer assay tree view
	BEGIN
	  for i IN (SELECT null FROM user_views WHERE view_name = 'VW_ASSAY_TREE') LOOP
		EXECUTE IMMEDIATE 'DROP VIEW VW_ASSAY_TREE';
	  END LOOP;
	END;

  EXECUTE IMMEDIATE '
	CREATE OR REPLACE VIEW VW_ASSAY_TREE
	AS
	  select distinct prot.id as protocol_id,
	prot.code_name as protocol_codeName, pl.label_text as protocol_name,
	prot.SHORT_DESCRIPTION as protocol_description, pv.string_value as assay_tree_rule,
	exp.id as experiment_id,
	exp.code_name as experiment_codename,
	el.label_text as experiment_name,
	el2.label_text as experiment_corpname,
	exp.short_description as experiment_description,
	LISTAGG(ptag.TAG_TEXT, '';'') WITHIN GROUP (ORDER BY ptag.TAG_TEXT) as protocol_tag,
	LISTAGG(etag.TAG_TEXT, '';'') WITHIN GROUP (ORDER BY etag.TAG_TEXT) as experiment_tag,
	ev.code_value as status
	FROM experiment exp
	JOIN experiment_state es ON exp.id=es.experiment_id AND es.ls_kind=''experiment metadata''
	JOIN experiment_value ev ON es.id=ev.Experiment_State_Id AND ev.ls_kind = ''experiment status''
	JOIN protocol prot ON exp.protocol_id = prot.id AND prot.ignored <>''1''
	JOIN experiment_label el ON exp.id = el.experiment_id AND el.ignored <> ''1'' AND el.preferred = ''1'' AND el.ls_type = ''name''
	LEFT OUTER JOIN experiment_label el2 ON exp.id = el2.experiment_id AND el2.ignored <> ''1'' AND el2.ls_type = ''corpName'' AND el2.ls_kind = ''experiment corpName''
	JOIN protocol_label pl ON prot.id = pl.protocol_id AND pl.ignored <> ''1'' AND pl.preferred = ''1'' AND pl.ls_type = ''name''
	LEFT OUTER JOIN protocol_state ps ON prot.id = ps.protocol_id AND ps.ignored <> ''1'' AND ps.ls_type = ''metadata'' AND ps.ls_kind = ''protocol metadata''
	LEFT OUTER JOIN PROTOCOL_VALUE pv ON ps.id = pv.protocol_state_id AND pv.ignored <> ''1'' AND pv.ls_type = ''stringValue'' AND pv.ls_kind = ''assay tree rule''
	LEFT OUTER JOIN PROTOCOL_TAG pt ON prot.id = pt.protocol_id
	LEFT OUTER JOIN ls_tag ptag ON pt.tag_id = ptag.id
	LEFT OUTER JOIN experiment_tag et ON exp.id = et.experiment_id
	LEFT OUTER JOIN ls_tag etag ON et.tag_id = etag.id
	WHERE exp.ignored <> ''1'' AND exp.deleted <> ''1''
	AND es.ignored <> ''1''
	AND ev.ignored <> ''1''
	AND ev.code_value = ''approved''
	GROUP by prot.id, exp.id,
	prot.code_name, pl.label_text,
	prot.SHORT_DESCRIPTION,
	pv.string_value,
	exp.code_name,
	el.label_text, 
	el2.label_text, 
	exp.short_description,
	ev.code_value';

	BEGIN
	  for i IN (SELECT null FROM user_views WHERE view_name = 'api_batch_parentname') LOOP
		EXECUTE IMMEDIATE 'DROP VIEW api_batch_parentname';
	  END LOOP;
	END;

  EXECUTE IMMEDIATE '
	CREATE OR REPLACE VIEW api_batch_parentname AS 
	 SELECT batchthing.id AS batch_id,
		batchthing.code_name AS batch_code,
		parentlabel.label_text AS parent_label
	   FROM ls_thing batchthing
		 JOIN itx_ls_thing_ls_thing itxt1 ON batchthing.id = itxt1.first_ls_thing_id AND itxt1.ls_type = ''instantiates'' AND itxt1.ls_kind = ''batch_parent''
		 JOIN ls_thing parentthing ON itxt1.second_ls_thing_id = parentthing.id
		 LEFT JOIN ls_thing_label parentlabel ON parentthing.id = parentlabel.lsthing_id AND parentlabel.ignored = ''0'' AND parentlabel.preferred = ''1'' AND parentlabel.ls_type = ''name'' AND parentlabel.ls_kind = parentthing.ls_kind
	  WHERE batchthing.ignored = ''0'' AND parentthing.ignored = ''0'' AND batchthing.ls_type = ''batch'' AND parentthing.ls_type = ''parent'' AND parentthing.ls_kind <> ''formulation''
	UNION
	 SELECT batchthing.id AS batch_id,
		batchthing.code_name AS batch_code,
		parentlabel.label_text AS parent_label
	   FROM ls_thing batchthing
		 JOIN itx_ls_thing_ls_thing itxt1 ON batchthing.id = itxt1.first_ls_thing_id AND itxt1.ls_type = ''instantiates'' AND itxt1.ls_kind = ''batch_parent''
		 JOIN ls_thing parentthing ON itxt1.second_ls_thing_id = parentthing.id
		 LEFT JOIN ls_thing_label parentlabel ON parentthing.id = parentlabel.lsthing_id AND parentlabel.ignored = ''0'' AND parentlabel.preferred = ''1'' AND parentlabel.ls_type = ''name'' AND parentlabel.ls_kind = ''descriptive''
	  WHERE batchthing.ignored = ''0'' AND parentthing.ignored = ''0'' AND batchthing.ls_type = ''batch'' AND parentthing.ls_type = ''parent'' AND parentthing.ls_kind = ''formulation''';

END;