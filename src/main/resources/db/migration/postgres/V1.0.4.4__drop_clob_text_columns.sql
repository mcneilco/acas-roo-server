SELECT lo_unlink(last_resultjson::int) FROM cron_job;
SELECT lo_unlink(scriptjsondata::int) FROM cron_job;
SELECT lo_unlink(clob_value::int) FROM analysis_group_value;
--SELECT lo_unlink(clob_value::int) FROM author_value;
SELECT lo_unlink(clob_value::int) FROM container_value;
SELECT lo_unlink(clob_value::int) FROM experiment_value;
SELECT lo_unlink(clob_value::int) FROM itx_container_container_value;
SELECT lo_unlink(clob_value::int) FROM itx_expt_expt_value;
SELECT lo_unlink(clob_value::int) FROM itx_ls_thing_ls_thing_value;
SELECT lo_unlink(clob_value::int) FROM itx_protocol_protocol_value;
SELECT lo_unlink(clob_value::int) FROM itx_subject_container_value;
SELECT lo_unlink(clob_value::int) FROM ls_thing_value;
SELECT lo_unlink(clob_value::int) FROM protocol_value;
SELECT lo_unlink(clob_value::int) FROM subject_value;
SELECT lo_unlink(page_content::int) FROM thing_page;
SELECT lo_unlink(page_content::int) FROM thing_page_archive;
SELECT lo_unlink(clob_value::int) FROM treatment_group_value;

ALTER TABLE cron_job ALTER COLUMN last_resultjson SET DATA TYPE text USING last_resultjson_temp;
ALTER TABLE cron_job ALTER COLUMN scriptjsondata SET DATA TYPE text USING scriptjsondata_temp;
ALTER TABLE analysis_group_value ALTER COLUMN clob_value SET DATA TYPE text USING clob_value_temp;
--ALTER TABLE author_value ALTER COLUMN clob_value SET DATA TYPE text USING clob_value_temp;
ALTER TABLE container_value ALTER COLUMN clob_value SET DATA TYPE text USING clob_value_temp;
ALTER TABLE experiment_value ALTER COLUMN clob_value SET DATA TYPE text USING clob_value_temp;
ALTER TABLE itx_container_container_value ALTER COLUMN clob_value SET DATA TYPE text USING clob_value_temp;
ALTER TABLE itx_expt_expt_value ALTER COLUMN clob_value SET DATA TYPE text USING clob_value_temp;
ALTER TABLE itx_ls_thing_ls_thing_value ALTER COLUMN clob_value SET DATA TYPE text USING clob_value_temp;
ALTER TABLE itx_protocol_protocol_value ALTER COLUMN clob_value SET DATA TYPE text USING clob_value_temp;
ALTER TABLE itx_subject_container_value ALTER COLUMN clob_value SET DATA TYPE text USING clob_value_temp;
ALTER TABLE ls_thing_value ALTER COLUMN clob_value SET DATA TYPE text USING clob_value_temp;
ALTER TABLE protocol_value ALTER COLUMN clob_value SET DATA TYPE text USING clob_value_temp;
ALTER TABLE subject_value ALTER COLUMN clob_value SET DATA TYPE text USING clob_value_temp;
ALTER TABLE thing_page ALTER COLUMN page_content SET DATA TYPE text USING page_content_temp;
ALTER TABLE thing_page_archive ALTER COLUMN page_content SET DATA TYPE text USING page_content_temp;
ALTER TABLE treatment_group_value ALTER COLUMN clob_value SET DATA TYPE text USING clob_value_temp;

ALTER TABLE cron_job DROP COLUMN last_resultjson_temp;
ALTER TABLE cron_job DROP COLUMN scriptjsondata_temp;
ALTER TABLE analysis_group_value DROP COLUMN clob_value_temp;
--ALTER TABLE author_value DROP COLUMN clob_value_temp;
ALTER TABLE container_value DROP COLUMN clob_value_temp;
ALTER TABLE experiment_value DROP COLUMN clob_value_temp;
ALTER TABLE itx_container_container_value DROP COLUMN clob_value_temp;
ALTER TABLE itx_expt_expt_value DROP COLUMN clob_value_temp;
ALTER TABLE itx_ls_thing_ls_thing_value DROP COLUMN clob_value_temp;
ALTER TABLE itx_protocol_protocol_value DROP COLUMN clob_value_temp;
ALTER TABLE itx_subject_container_value DROP COLUMN clob_value_temp;
ALTER TABLE ls_thing_value DROP COLUMN clob_value_temp;
ALTER TABLE protocol_value DROP COLUMN clob_value_temp;
ALTER TABLE subject_value DROP COLUMN clob_value_temp;
ALTER TABLE thing_page DROP COLUMN page_content_temp;
ALTER TABLE thing_page_archive DROP COLUMN page_content_temp;
ALTER TABLE treatment_group_value DROP COLUMN clob_value_temp;