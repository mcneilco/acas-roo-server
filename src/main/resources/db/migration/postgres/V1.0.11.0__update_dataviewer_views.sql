DROP VIEW IF EXISTS VW_DV_EXPT_AG_KINDS;
DROP VIEW IF EXISTS v_api_dv_ag_results;
DROP VIEW IF EXISTS dv_api_all_ag_results;
DROP VIEW IF EXISTS vw_dv_ag_results;

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
            WHEN agv.ls_type::text = 'inlineFileValue'::text THEN agv.ls_type_and_kind
            ELSE agv.ls_kind
        END AS ls_kind,
        CASE
            WHEN agv.ls_kind::text ~~ '%curve id'::text THEN NULL::numeric
            ELSE agv.numeric_value
        END AS numeric_value,
        CASE
            WHEN agv.ls_type::text = 'inlineFileValue'::text THEN agv.file_value
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
     JOIN analysis_group_value agv ON agv.analysis_state_id = ags.id AND agv.ls_kind::text <> 'batch code'::text AND agv.ls_kind::text <> 'time'::text
     JOIN analysis_group_value agv2 ON agv2.analysis_state_id = ags.id AND agv2.ls_kind::text = 'batch code'::text AND agv2.ignored = false
     LEFT JOIN analysis_group_value agv4 ON agv4.analysis_state_id = ags.id AND agv4.ls_kind::text = 'time'::text AND agv4.ignored = false
  WHERE p.ignored = false AND e.ignored = false AND ag.ignored = false AND ags.ignored = false AND agv.ignored = false;

-- can switch the underlying view to allow a little flexibility
CREATE OR REPLACE VIEW V_API_DV_AG_RESULTS AS SELECT * FROM vw_dv_ag_results;

CREATE OR REPLACE VIEW DV_API_ALL_AG_RESULTS AS SELECT * FROM vw_dv_ag_results;


CREATE OR REPLACE VIEW VW_DV_EXPT_AG_KINDS 
AS
SELECT distinct agr.protocol_id, agr.protocol_code,  pl.label_text as protocol_name, 
agr.experiment_id, agr.experiment_code, el.label_text as experiment_name, 
agr.ls_type, agr.ls_kind 
from vw_dv_ag_results agr
JOIN experiment_label el on agr.experiment_id = el.experiment_id 
AND el.ignored = false and el.preferred = true 
AND el.ls_type = 'name' AND el.ls_kind = 'experiment name'
JOIN protocol_label pl on agr.protocol_id = pl.protocol_id 
AND pl.ignored = false and pl.preferred = true 
AND pl.ls_type = 'name' AND pl.ls_kind = 'protocol name'
where agr.ls_type in ('stringValue', 'numericValue');
