CREATE OR REPLACE VIEW p_api_analysis_group_results AS 
SELECT ag.id AS ag_id, 
ag.code_name as ag_code_name,
eag.experiment_id AS experiment_id, 
agv2.code_value AS tested_lot, 
agv2.concentration AS tested_conc, 
CASE
  WHEN agv4.numeric_value IS NOT NULL AND agv2.concentration IS NOT NULL
		THEN agv2.conc_unit || ' and ' || agv4.numeric_value || ' ' || agv4.unit_kind
	WHEN agv4.numeric_value IS NOT NULL
		THEN agv4.numeric_value || ' ' || agv4.unit_kind
	ELSE agv2.conc_unit
END	
AS tested_conc_unit, 
agv.id AS agv_id,
agv.ls_type as ls_type,
CASE
    WHEN agv.ls_type = 'inlineFileValue'
    THEN agv.ls_type_and_kind
ELSE agv.ls_kind
END AS ls_kind,
agv.operator_kind, 
 CASE 
    WHEN agv.ls_kind like '%curve id' THEN null
    ELSE agv.numeric_value
  END as numeric_value,
agv.uncertainty, 
agv.unit_kind,
CASE
WHEN agv.ls_type = 'fileValue' 
THEN 
		('<A HREF="' || 
		(
			SELECT application_setting.prop_value
			FROM application_setting
        	WHERE application_setting.prop_name = 'BatchDocumentsURL'
        ) || 
        replace(agv.file_value, ' ', '%20') || 
        '">' || 
        agv.comments ||
	' (' ||
        agv.file_value ||
        ')' ||
        '</A>'
		)
WHEN agv.ls_type = 'inlineFileValue'
THEN agv.file_value
WHEN agv.ls_type = 'urlValue' 
THEN 
		('<A HREF="' || 
        replace(agv.url_value, ' ', '%20') || 
        '">' || 
        agv.comments ||
	' (' ||
        agv.url_value ||
        ')' ||
        '</A>'
		)
WHEN agv.ls_type = 'dateValue'
	THEN to_char(agv.date_value, 'yyyy-mm-dd')
WHEN agv.ls_type = 'codeValue'
	THEN agv.code_value
	ELSE COALESCE(agv.string_value, agv.clob_value, agv.comments)
END AS string_value,
agv.comments, 
agv.recorded_date::timestamp::date,
agv.public_data
FROM experiment e
JOIN experiment_analysisgroup eag on e.id=eag.experiment_id
JOIN analysis_GROUP ag ON eag.analysis_group_id = ag.id
JOIN analysis_GROUP_state ags ON ags.analysis_GROUP_id = ag.id
JOIN analysis_GROUP_value agv ON agv.analysis_state_id = ags.id AND agv.ls_kind <> 'batch code' AND agv.ls_kind <> 'time'
JOIN analysis_GROUP_value agv2 ON agv2.analysis_state_id = ags.id and agv2.ls_kind = 'batch code'
LEFT OUTER JOIN analysis_GROUP_value agv4 ON agv4.analysis_state_id = ags.id and agv4.ls_kind = 'time' AND agv4.ignored = '0'
WHERE ag.ignored = '0' and
ags.ignored = '0' and
agv.ignored = '0' and
agv2.ignored = '0' and
e.ignored = '0';