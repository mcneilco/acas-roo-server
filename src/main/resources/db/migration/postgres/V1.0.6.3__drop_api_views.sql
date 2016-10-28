-----------------------------
-- Run all of these as acas the acas user
-----------------------------
--TODO: make into proper ddl
--drop (ordered by dependency)
DROP VIEW IF EXISTS api_system_statistics;
DROP VIEW IF EXISTS API_SUBJECT_CONTAINER_RESULTS;
DROP VIEW IF EXISTS API_SUBJECT_RESULTS;
drop view IF EXISTS api_container_contents;
DROP VIEW IF EXISTS api_curve_params;
DROP VIEW IF EXISTS api_dose_response;
DROP VIEW IF EXISTS API_HTS_TREATMENT_RESULTS;
DROP VIEW IF EXISTS api_experiment_results;
DROP VIEW IF EXISTS api_analysis_group_results;
DROP VIEW IF EXISTS p_api_analysis_group_results;
DROP VIEW IF EXISTS api_all_data;
DROP VIEW IF EXISTS batch_code_experiment_links;
DROP VIEW IF EXISTS api_protocol;
DROP VIEW IF EXISTS api_experiment_approved;
DROP VIEW IF EXISTS api_experiment;
