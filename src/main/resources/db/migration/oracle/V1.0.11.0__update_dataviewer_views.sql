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

	EXECUTE IMMEDIATE '
	CREATE OR REPLACE VIEW vw_dv_ag_results
	 AS 
	  SELECT 
		ag.id AS ag_id,
		ag.code_name AS ag_code,
		e.id AS experiment_id,
		e.code_name AS experiment_code,
		p.id AS protocol_id,
		p.code_name AS protocol_code,
		agv2.code_value AS tested_lot,
		agv2.concentration AS tested_conc,
		agv2.conc_unit AS tested_conc_unit,
		agv4.numeric_value AS "time",
		agv4.unit_kind AS time_unit,
		ags.id AS analysis_state_id,
		agv.id AS agv_id,
		agv.operator_kind AS operator,
		agv.unit_type,
		agv.unit_kind,
		agv.uncertainty_type,
		agv.uncertainty,
		agv.ls_type,
			CASE
				WHEN agv.ls_type = ''inlineFileValue'' THEN agv.ls_type_and_kind
				ELSE agv.ls_kind
			END AS ls_kind,
			CASE
				WHEN agv.ls_kind LIKE ''%curve id'' THEN NULL
				ELSE agv.numeric_value
			END AS numeric_value,
			CASE
				WHEN agv.ls_type = ''inlineFileValue'' THEN agv.file_value
				ELSE agv.string_value
			END AS string_value,
		agv.file_value,
		agv.url_value,
		agv.date_value,
		agv.clob_value,
		agv.comments,
		agv.recorded_date AS agv_recorded_date,
		agv.public_data
	   FROM experiment e
		 JOIN protocol p ON e.protocol_id = p.id
		 JOIN experiment_analysisgroup eag ON e.id = eag.experiment_id
		 JOIN analysis_group ag ON eag.analysis_group_id = ag.id
		 JOIN analysis_group_state ags ON ags.analysis_group_id = ag.id
		 JOIN analysis_group_value agv ON agv.analysis_state_id = ags.id AND agv.ls_kind <> ''batch code'' AND agv.ls_kind <> ''time''
		 JOIN analysis_group_value agv2 ON agv2.analysis_state_id = ags.id AND agv2.ls_kind = ''batch code'' AND agv2.ignored = ''0''
		 LEFT JOIN analysis_group_value agv4 ON agv4.analysis_state_id = ags.id AND agv4.ls_kind = ''time'' AND agv4.ignored = ''0''
	  WHERE p.ignored = ''0'' AND e.ignored = ''0'' AND ag.ignored = ''0'' AND ags.ignored = ''0'' AND agv.ignored = ''0''';

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

END;