DROP VIEW IF EXISTS vw_experiment;

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
     JOIN experiment_label el ON e.id = el.experiment_id AND el.ls_type::text = 'name'::text AND el.ls_kind::text = 'experiment name'::text
     LEFT JOIN experiment_label el2 ON e.id = el2.experiment_id AND el2.ls_type::text = 'name'::text AND el2.ls_kind::text = 'experiment corp name'::text
  WHERE e.ignored = false AND el.ignored = false;