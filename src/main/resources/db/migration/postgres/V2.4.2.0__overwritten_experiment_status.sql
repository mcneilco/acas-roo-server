-- Goal Of This Migration - To Introduce to the Notion of an "Overwritten" Experiment to ACAS

-- 		1. SELECT ALL EXPERIMENTS WHERE "ignored=true" AND "deleted=true" 
-- 		2. From the Filtered Experiments -> Set Old Experiment Status to Ignore 
--    3. From the Filtered Experiments -> Set New Experiment Status of Being Overwritten 

--Create an ls_transaction to tie all our states and values to
insert into ls_transaction (id, comments, recorded_date, version, recorded_by)
	select nextval('ls_transaction_pkseq'), 'V2.4.2.0 overwritten experiment status', now(), 0, 'ACAS';


UPDATE
    experiment_value
SET
    ignored = '1'  -- Ignoring Any Experiment Status Where Experiment is "Deleted" and "Ignored" (i.e. Our new definition of "Overwritten")
FROM
    (SELECT e.id AS id,
    e.code_name || '::' || el.label_text as experiment_name,
    e.code_name,
    el.label_text,
    e.Ls_Type_And_Kind as kind,
    e.recorded_by,
    e.recorded_date,
    e.short_description,
    e.protocol_id,
    MAX( CASE ev.ls_kind WHEN 'experiment status' THEN ev.code_value ELSE null END ) AS status,
    es.id as exp_state_id
	FROM experiment e
	JOIN experiment_label el
	ON e.id=el.experiment_id
	JOIN experiment_state es
	ON e.id=es.experiment_id
	JOIN experiment_value ev
	ON Es.Id=Ev.Experiment_State_Id
	WHERE e.ignored ='1' and e.deleted = '1' -- Criteria for Experiment Being Overwritten 
	AND es.ls_kind='experiment metadata' Group By E.Id, E.Code_Name, E.Ls_Type_And_Kind, E.Recorded_By, E.Recorded_Date, E.Short_Description, E.Protocol_Id, El.Label_Text, exp_state_id)
	AS subquery
WHERE 
	experiment_value.ls_kind = 'experiment status' and experiment_value.experiment_state_id = subquery.exp_state_id;


-- Add New Ignore Status Set to "Overwritten"


-- Inserting "Overwritten Status" Into Experiment Values w/ Associated Experiemnt State IDs From Same Filtering Criteria Above
INSERT INTO experiment_value (id, deleted, ignored, code_kind, code_origin, code_type, code_type_and_kind, code_value, 
								ls_kind, ls_transaction, 
                ls_type, ls_type_and_kind, public_data, recorded_by, recorded_date, 
								version, experiment_state_id)
SELECT nextval('value_pkseq'), false, false, 'status', 'ACAS DDICT', 'experiment', 'experiment_status', 'overwritten',
								'experiment status', (select id from ls_transaction where comments = 'V2.4.2.0 overwritten experiment status'), 
                'codeValue', 'codeValue_experiment status', true, 'ACAS', now(),
								0, subquery.exp_state_id
FROM 
    (SELECT e.id AS exp_id,
    e.code_name || '::' || el.label_text as experiment_name,
    e.code_name,
    el.label_text,
    es.id as exp_state_id
    FROM experiment e
    JOIN experiment_label el
    ON e.id=el.experiment_id
    JOIN experiment_state es
    ON e.id=es.experiment_id
    JOIN experiment_value ev
    ON Es.Id=Ev.Experiment_State_Id
    WHERE e.ignored ='1' and e.deleted = '1'
    AND es.ls_kind='experiment metadata' Group By E.Id, E.Code_Name, El.Label_Text, es.id)
    AS subquery
 ;


--- Check w/ select * from experiment_value WHERE experiment_value.ls_kind = 'experiment status';
