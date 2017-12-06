DROP VIEW IF EXISTS vw_protocol_conditions;

CREATE OR REPLACE VIEW vw_protocol_conditions AS
SELECT e.protocol_id, col_name_val.string_value as column_name, col_type_val.string_value as column_type, 'condition'::varchar(255) as column_purpose
FROM experiment e
join experiment_state column_order_state ON e.id = column_order_state.experiment_id
join experiment_value is_condition_val ON column_order_state.id = is_condition_val.experiment_state_id
join experiment_value col_name_val ON column_order_state.id = col_name_val.experiment_state_id
JOIN experiment_value col_type_val ON column_order_state.id = col_type_val.experiment_state_id
WHERE e.ignored <> '1'
AND column_order_state.ls_kind = 'data column order' AND column_order_state.ignored <> '1'
AND is_condition_val.ls_kind = 'condition column' AND is_condition_val.ignored <> '1'
AND col_name_val.ls_kind = 'column name' AND col_name_val.ignored <> '1'
AND col_type_val.ls_kind = 'column type' AND col_type_val.ignored <> '1'
AND is_condition_val.string_value = 'TRUE';