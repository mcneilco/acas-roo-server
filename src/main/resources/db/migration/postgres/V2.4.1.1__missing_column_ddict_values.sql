do $script$
begin
-- As part of ACAS-413, followup from previous migration
-- Exit early if there is no expt data as this indicates a fresh system
if not exists (select * from experiment)
then
	return;
end if;

-- Fill in missing 'data column' DDict Values based on saved data
-- Create a reusable function to insert missing ddict values
create or replace function add_data_column_ddict_value(val varchar, ls_kind varchar) returns bigint as $$
	--create ddict type if not exists
	insert into ddict_type (id, ignored, name, version)
		select nextval('ddict_type_pkseq') as id, false as ignored, 'data column', 0
		where not exists (select * from ddict_type where name = 'data column');
	--create ddict kind if not exists
	insert into ddict_kind (id, ignored, ls_type, ls_type_and_kind, name, version)
		select
			nextval('ddict_kind_pkseq') as id,
			false as ignored,
			'data column' as ls_type,
			'data column_'||ls_kind as ls_type_and_kind,
			ls_kind as name,
			0 as version
			where not exists (
				select * from ddict_kind where ls_type = 'data column' and name = ls_kind
			);

	--create ddict value if not exists
	insert into ddict_value (id, ignored, code_name, label_text, ls_type, ls_kind, ls_type_and_kind, short_name, version)
		select
			nextval('ddict_value_pkseq') as id,
			false as ignored,
			'DDICT-' || lpad((select nextval(db_sequence) from label_sequence where label_prefix = 'DDICT'):: text, 6, '0'::text) as code_name,
			val as label_text,
			'data column' as ls_type,
			ls_kind as ls_kind,
			'data column_'||ls_kind as ls_type_and_kind,
			val as short_name,
			0 as version
		where not exists (
			select * from ddict_value dv 
				where dv.ls_type = 'data column' 
					and dv.ls_kind = ls_kind 
					and dv.ignored = false 
					and dv.short_name = val
			)
		returning id
	$$ language sql;

--Add missing column name ddicts
perform add_data_column_ddict_value(code_value, 'column name'::varchar) from
(SELECT DISTINCT ev.code_value
	FROM experiment e
	JOIN experiment_state es ON e.id = es.experiment_id
	JOIN experiment_value ev ON es.id = ev.experiment_state_id
	WHERE e.ignored = FALSE
	AND es.ignored = FALSE
	AND ev.ignored = FALSE
	AND es.ls_kind = 'data column order'
	AND ev.ls_kind = 'column name'
	AND ev.code_value IS NOT NULL
	AND NOT exists(
		SELECT 1 FROM ddict_value dv WHERE dv.ls_type = ev.code_type AND dv.ls_kind = ev.code_kind AND dv.short_name = ev.code_value
)) a;

--units
perform add_data_column_ddict_value(code_value, 'column units'::varchar) from
(SELECT DISTINCT ev.code_value
	FROM experiment e
	JOIN experiment_state es ON e.id = es.experiment_id
	JOIN experiment_value ev ON es.id = ev.experiment_state_id
	WHERE e.ignored = FALSE
	AND es.ignored = FALSE
	AND ev.ignored = FALSE
	AND es.ls_kind = 'data column order'
	AND ev.ls_kind = 'column units'
	AND ev.code_value IS NOT NULL
	AND NOT exists(
		SELECT 1 FROM ddict_value dv WHERE dv.ls_type = ev.code_type AND dv.ls_kind = ev.code_kind AND dv.short_name = ev.code_value
)) a;

--conc units
perform add_data_column_ddict_value(code_value, 'column conc units'::varchar) from
(SELECT DISTINCT ev.code_value
	FROM experiment e
	JOIN experiment_state es ON e.id = es.experiment_id
	JOIN experiment_value ev ON es.id = ev.experiment_state_id
	WHERE e.ignored = FALSE
	AND es.ignored = FALSE
	AND ev.ignored = FALSE
	AND es.ls_kind = 'data column order'
	AND ev.ls_kind = 'column conc units'
	AND ev.code_value IS NOT NULL
	AND NOT exists(
		SELECT 1 FROM ddict_value dv WHERE dv.ls_type = ev.code_type AND dv.ls_kind = ev.code_kind AND dv.short_name = ev.code_value
)) a;

--time units
perform add_data_column_ddict_value(code_value, 'column time units'::varchar) from
(SELECT DISTINCT ev.code_value
	FROM experiment e
	JOIN experiment_state es ON e.id = es.experiment_id
	JOIN experiment_value ev ON es.id = ev.experiment_state_id
	WHERE e.ignored = FALSE
	AND es.ignored = FALSE
	AND ev.ignored = FALSE
	AND es.ls_kind = 'data column order'
	AND ev.ls_kind = 'column time units'
	AND ev.code_value IS NOT NULL
	AND NOT exists(
		SELECT 1 FROM ddict_value dv WHERE dv.ls_type = ev.code_type AND dv.ls_kind = ev.code_kind AND dv.short_name = ev.code_value
)) a;

end $script$