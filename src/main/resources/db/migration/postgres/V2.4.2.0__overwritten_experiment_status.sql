-- Goal Of This Migration - To Introduce to the Notion of an "Overwritten" Experiment to ACAS

-- 		1. SELECT ALL EXPERIMENTS WHERE "ignored=true" AND "deleted=true" 
-- 		2. From the Filtered Experiments -> Set Old Experiment Status to Ignore 
--    3. From the Filtered Experiments -> Set New Experiment Status of Being Overwritten 

--Create an ls_transaction to tie all our states and values to
insert into ls_transaction (id, comments, recorded_date, version, recorded_by)
	select nextval('ls_transaction_pkseq'), 'V2.4.2.0 overwritten experiment status', now(), 0, 'ACAS';


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
    (SELECT DISTINCT es.id as exp_state_id
     FROM experiment e
              JOIN experiment_state es
                   ON e.id = es.experiment_id
              JOIN experiment_value ev
                   ON es.id = ev.experiment_state_id
     WHERE e.ignored ='1'
       AND e.deleted = '1'
       AND es.ls_kind='experiment metadata')
        AS subquery;

UPDATE
    experiment_value ev
SET 
    ignored = true -- Ignoring Any Experiment Status Where Experiment is "Deleted" and "Ignored" (i.e. Our new definition of "Overwritten")
FROM 
    experiment e
    JOIN experiment_state es ON e.id = es.experiment_id
WHERE ev.experiment_state_id = es.id
  AND ev.ls_kind = 'experiment status'
  AND ev.code_value != 'overwritten' -- Don't want to ignore something that's already overwritten from above 
  AND ev.ignored = false
  AND es.ls_kind = 'experiment metadata'
  AND e.ignored = '1'
  AND e.deleted = '1'; -- Criteria for Experiment Being Overwritten
  
