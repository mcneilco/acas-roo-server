-- As part of ACAS-413
-- Setup a temp table with all the existing experiment columns
-- We create an artificial "column_order" that will only be used for experiments
-- that are missing the 'data column order' states
create table tmp_expt_columns as
    select *, row_number() over (partition by experiment_id order by column_name) as column_order
    from (
		select distinct e.id as experiment_id,
			agv.ls_type as column_type,
			agv.ls_kind as column_name,
			agv.unit_kind as units,
			agv.concentration,
			agv.conc_unit as conc_units,
			not agv.public_data as hidden
		from protocol p
		join experiment e on p.id = e.protocol_id and e.ignored = false
		join experiment_analysisgroup ea on e.id = ea.experiment_id 
		join analysis_group ag on ag.id = ea.analysis_group_id and ag.ignored = false
		join analysis_group_state ags on ags.analysis_group_id = ag.id and ags.ignored = false and ags.ls_kind != 'dose response'
		join analysis_group_value agv on ags.id = agv.analysis_state_id and agv.ignored = false and agv.ls_kind != 'batch code'
		where p.ignored = false
	) a;

--Create an ls_transaction to tie all our states and values to
insert into ls_transaction (id, comments, recorded_date, version, recorded_by)
	select nextval('ls_transaction_pkseq'), 'V2.4.1.0 data column order states migration', now(), 0, 'ACAS';

-- Part 1: Fill in 'data column' DDict Values based on saved data
-- Create a reusable function to insert missing ddict values
create or replace function add_data_column_order_ddict_value(val varchar, ls_kind varchar) returns bigint as $$
	insert into ddict_value (id, ignored, code_name, label_text, ls_type, ls_kind, ls_type_and_kind, short_name, version)
		select
			nextval('ddict_value_pkseq') as id,
			false as ignored,
			'DDICT-' || lpad(nextval('labelseq_1_ddict_id_codename_document_datadictionary'):: text, 6, '0'::text) as code_name,
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

--column name
select add_data_column_order_ddict_value(column_name, 'column name'::varchar) from
(select distinct column_name from tmp_expt_columns) a;
--units
select add_data_column_order_ddict_value(units, 'column units'::varchar) from
(select distinct units from tmp_expt_columns) a where a.units is not null;
--conc units
select add_data_column_order_ddict_value(conc_units, 'column conc units'::varchar) from
(select distinct conc_units from tmp_expt_columns) a where a.conc_units is not null;
--End Part 1

--Part 2: Create "data column order" states on experiments that are missing them
-- Function to create a "data column order" experiment states and the values within it
create or replace function create_expt_data_column_order_state(expt_id bigint, transaction_id bigint, column_order int, column_type varchar, column_name varchar, 
															   units varchar, concentration float8, conc_units varchar, hidden boolean, 
															   condition boolean) returns bigint 
  as $$
	declare
		expt_state_id bigint;
	begin
		--create a state
		insert into experiment_state (id, deleted, ignored, ls_kind, ls_transaction, ls_type, ls_type_and_kind, recorded_by, recorded_date, version, experiment_id)
			select nextval('state_pkseq'), false, false, 'data column order', transaction_id, 'metadata', 'metadata_data column order', 'ACAS', now(), 0, expt_id
		returning id into expt_state_id;
		-- create column order value
		insert into experiment_value (id, deleted, ignored, numeric_value,
									  ls_kind, ls_transaction, ls_type, ls_type_and_kind, public_data, recorded_by, recorded_date, 
									  version, experiment_state_id)
			select nextval('value_pkseq'), false, false, column_order,
									  'column order', transaction_id, 'numericValue', 'numericValue_column order', true, 'ACAS', now(),
									  0, expt_state_id;
		-- create column_type value
		insert into experiment_value (id, code_kind, code_origin, code_type, code_type_and_kind, code_value, deleted, ignored, 
									  ls_kind, ls_transaction, ls_type, ls_type_and_kind, public_data, recorded_by, recorded_date, 
									  version, experiment_state_id)
			select nextval('value_pkseq'), 'data column', 'ACAS DDICT', 'column type', 'data column_column type', column_type, false, false,
									  'column type', transaction_id, 'codeValue', 'codeValue_column type', true, 'ACAS', now(),
									  0, expt_state_id;
		-- create column_name value
		insert into experiment_value (id, code_kind, code_origin, code_type, code_type_and_kind, code_value, deleted, ignored, 
									  ls_kind, ls_transaction, ls_type, ls_type_and_kind, public_data, recorded_by, recorded_date, 
									  version, experiment_state_id)
			select nextval('value_pkseq'), 'data column', 'ACAS DDICT', 'column name', 'data column_column name', column_name, false, false,
									  'column name', transaction_id, 'codeValue', 'codeValue_column name', true, 'ACAS', now(),
									  0, expt_state_id;
		-- create units value
		insert into experiment_value (id, code_kind, code_origin, code_type, code_type_and_kind, code_value, deleted, ignored, 
									  ls_kind, ls_transaction, ls_type, ls_type_and_kind, public_data, recorded_by, recorded_date, 
									  version, experiment_state_id)
			select nextval('value_pkseq'), 'data column', 'ACAS DDICT', 'column units', 'data column_column units', units, false, false,
									  'column units', transaction_id, 'codeValue', 'codeValue_column units', true, 'ACAS', now(),
									  0, expt_state_id;
		-- create concentration value
		insert into experiment_value (id, deleted, ignored, numeric_value,
									  ls_kind, ls_transaction, ls_type, ls_type_and_kind, public_data, recorded_by, recorded_date, 
									  version, experiment_state_id)
			select nextval('value_pkseq'), false, false, concentration,
									  'column concentration', transaction_id, 'numericValue', 'numericValue_column concentration', true, 'ACAS', now(),
									  0, expt_state_id;
		-- create conc_units value
		insert into experiment_value (id, code_kind, code_origin, code_type, code_type_and_kind, code_value, deleted, ignored, 
									  ls_kind, ls_transaction, ls_type, ls_type_and_kind, public_data, recorded_by, recorded_date, 
									  version, experiment_state_id)
			select nextval('value_pkseq'), 'data column', 'ACAS DDICT', 'column conc units', 'data column_column conc units', conc_units, false, false,
									  'column conc units', transaction_id, 'codeValue', 'codeValue_column conc units', true, 'ACAS', now(),
									  0, expt_state_id;
		-- create hidden value
		insert into experiment_value (id, code_kind, code_origin, code_type, code_type_and_kind, code_value, deleted, ignored, 
									  ls_kind, ls_transaction, ls_type, ls_type_and_kind, public_data, recorded_by, recorded_date, 
									  version, experiment_state_id)
			select nextval('value_pkseq'), 'boolean', 'ACAS DDICT', 'boolean', 'boolean_boolean', case when hidden then 'TRUE' else 'FALSE' end, false, false,
									  'hide column', transaction_id, 'codeValue', 'codeValue_hide column', true, 'ACAS', now(),
									  0, expt_state_id;
		-- create condition value
		insert into experiment_value (id, code_kind, code_origin, code_type, code_type_and_kind, code_value, deleted, ignored, 
									  ls_kind, ls_transaction, ls_type, ls_type_and_kind, public_data, recorded_by, recorded_date, 
									  version, experiment_state_id)
			select nextval('value_pkseq'), 'boolean', 'ACAS DDICT', 'boolean', 'boolean_boolean', case when condition then 'TRUE' else 'FALSE' end, false, false,
									  'condition column', transaction_id, 'codeValue', 'codeValue_condition column', true, 'ACAS', now(),
									  0, expt_state_id;
		
		return expt_state_id;
	end
$$ language plpgsql;

-- Create 'data column order' states and values for all experiments which are missing them
select create_expt_data_column_order_state(experiment_id, (select id from ls_transaction where comments = 'V2.4.1.0 data column order states migration'),
										   column_order::int, column_type, column_name, units, concentration, conc_units, hidden, false)
	from tmp_expt_columns where experiment_id in
		(select e.id from experiment e
			where not exists (
				select 1 from experiment_state es where es.ls_kind = 'data column order' and es.experiment_id = e.id
			)
		);
--End Part 2

--Part 3: Create "data column order" states on protocols
-- This represents a union of all data columns in each of the protocol's experiments

-- Build a temp table of all experiment data column order stuff
create table tmp_expt_column_order_data as
   -- Use row_number() to reassign column order at the protocol level, since different experiments may have different columns in different order
   -- The 'earliest experiment wins', meaning any new endpoints from later experiments will be added later in the column order
	select *, row_number() over (partition by protocol_id order by experiment_id, column_order) as new_column_order
	from (
		-- This query pivots all the 'data column order' experiment values into a table of column_type, column_name, etc.
		select e.protocol_id,
			coalesce(col_type.code_value, col_type.string_value) as column_type, --the coalesce is used to account for having the same data in stringValues or in codeValues
			coalesce(col_name.code_value, col_name.string_value) as column_name,
			coalesce(col_name.code_value, col_name.string_value) as units,
			col_conc.numeric_value as concentration,
		 	coalesce(col_conc_units.code_value, col_conc_units.string_value) as conc_units,
			coalesce(hide_col.code_value, hide_col.string_value) as hide_column,
			coalesce(cond_col.code_value, cond_col.string_value) as condition_column,
			--This query will GROUP BY all the columns above this point
			-- For column order and experiment, we take the lowest values as the same endpoint can show up in many experiments
			min(col_order.numeric_value) as column_order,
			min( e.id) as experiment_id
		from experiment e
		join experiment_state es on es.experiment_id = e.id and es.ignored = false and es.ls_kind = 'data column order'
		join experiment_value col_type on col_type.experiment_state_id = es.id and col_type.ls_kind = 'column type' and col_type.ignored = false
		join experiment_value col_name on col_name.experiment_state_id = es.id and col_name.ls_kind = 'column name' and col_name.ignored = false
		join experiment_value col_units on col_units.experiment_state_id = es.id and col_units.ls_kind = 'column units' and col_units.ignored = false
		left join experiment_value col_conc on col_conc.experiment_state_id = es.id and col_conc.ls_kind = 'column concentration' and col_conc.ignored = false
		left join experiment_value col_conc_units on col_conc_units.experiment_state_id = es.id and col_conc_units.ls_kind = 'column conc units' and col_conc_units.ignored = false
		left join experiment_value hide_col on hide_col.experiment_state_id = es.id and hide_col.ls_kind = 'hide column' and hide_col.ignored = false
		left join experiment_value cond_col on cond_col.experiment_state_id = es.id and cond_col.ls_kind = 'condition column' and cond_col.ignored = false
		left join experiment_value col_order on col_order.experiment_state_id = es.id and col_order.ls_kind = 'column order' and col_order.ignored = false
		group by 
			e.protocol_id,
			coalesce(col_type.code_value, col_type.string_value),
			coalesce(col_name.code_value, col_name.string_value),
			coalesce(col_name.code_value, col_name.string_value),
			col_conc.numeric_value,
		 	coalesce(col_conc_units.code_value, col_conc_units.string_value),
			coalesce(hide_col.code_value, hide_col.string_value),
			coalesce(cond_col.code_value, cond_col.string_value)
		) a;

-- Define a function to create the protocol column order states and values
-- this is nearly identical to the function above, just using protocol instead of experiment
create or replace function create_protocol_data_column_order_state(protocol_id bigint, transaction_id bigint, column_order int, column_type varchar, column_name varchar, 
															   units varchar, concentration float8, conc_units varchar, hide_column varchar, 
															   condition_column varchar) returns bigint 
  as $$
	declare
		protocol_state_id bigint;
	begin
		--create a state
		insert into protocol_state (id, deleted, ignored, ls_kind, ls_transaction, ls_type, ls_type_and_kind, recorded_by, recorded_date, version, protocol_id)
			select nextval('state_pkseq'), false, false, 'data column order', transaction_id, 'metadata', 'metadata_data column order', 'ACAS', now(), 0, protocol_id
		returning id into protocol_state_id;
		-- create column order value
		insert into protocol_value (id, deleted, ignored, numeric_value,
									  ls_kind, ls_transaction, ls_type, ls_type_and_kind, public_data, recorded_by, recorded_date, 
									  version, protocol_state_id)
			select nextval('value_pkseq'), false, false, column_order,
									  'column order', transaction_id, 'numericValue', 'numericValue_column order', true, 'ACAS', now(),
									  0, protocol_state_id;
		-- create column_type value
		insert into protocol_value (id, code_kind, code_origin, code_type, code_type_and_kind, code_value, deleted, ignored, 
									  ls_kind, ls_transaction, ls_type, ls_type_and_kind, public_data, recorded_by, recorded_date, 
									  version, protocol_state_id)
			select nextval('value_pkseq'), 'data column', 'ACAS DDICT', 'column type', 'data column_column type', column_type, false, false,
									  'column type', transaction_id, 'codeValue', 'codeValue_column type', true, 'ACAS', now(),
									  0, protocol_state_id;
		-- create column_name value
		insert into protocol_value (id, code_kind, code_origin, code_type, code_type_and_kind, code_value, deleted, ignored, 
									  ls_kind, ls_transaction, ls_type, ls_type_and_kind, public_data, recorded_by, recorded_date, 
									  version, protocol_state_id)
			select nextval('value_pkseq'), 'data column', 'ACAS DDICT', 'column name', 'data column_column name', column_name, false, false,
									  'column name', transaction_id, 'codeValue', 'codeValue_column name', true, 'ACAS', now(),
									  0, protocol_state_id;
		-- create units value
		insert into protocol_value (id, code_kind, code_origin, code_type, code_type_and_kind, code_value, deleted, ignored, 
									  ls_kind, ls_transaction, ls_type, ls_type_and_kind, public_data, recorded_by, recorded_date, 
									  version, protocol_state_id)
			select nextval('value_pkseq'), 'data column', 'ACAS DDICT', 'column units', 'data column_column units', units, false, false,
									  'column units', transaction_id, 'codeValue', 'codeValue_column units', true, 'ACAS', now(),
									  0, protocol_state_id;
		-- create concentration value
		insert into protocol_value (id, deleted, ignored, numeric_value,
									  ls_kind, ls_transaction, ls_type, ls_type_and_kind, public_data, recorded_by, recorded_date, 
									  version, protocol_state_id)
			select nextval('value_pkseq'), false, false, concentration,
									  'column concentration', transaction_id, 'numericValue', 'numericValue_column concentration', true, 'ACAS', now(),
									  0, protocol_state_id;
		-- create conc_units value
		insert into protocol_value (id, code_kind, code_origin, code_type, code_type_and_kind, code_value, deleted, ignored, 
									  ls_kind, ls_transaction, ls_type, ls_type_and_kind, public_data, recorded_by, recorded_date, 
									  version, protocol_state_id)
			select nextval('value_pkseq'), 'data column', 'ACAS DDICT', 'column conc units', 'data column_column conc units', conc_units, false, false,
									  'column conc units', transaction_id, 'codeValue', 'codeValue_column conc units', true, 'ACAS', now(),
									  0, protocol_state_id;
		-- create hidden value
		insert into protocol_value (id, code_kind, code_origin, code_type, code_type_and_kind, code_value, deleted, ignored, 
									  ls_kind, ls_transaction, ls_type, ls_type_and_kind, public_data, recorded_by, recorded_date, 
									  version, protocol_state_id)
			select nextval('value_pkseq'), 'boolean', 'ACAS DDICT', 'boolean', 'boolean_boolean', hide_column, false, false,
									  'hide column', transaction_id, 'codeValue', 'codeValue_hide column', true, 'ACAS', now(),
									  0, protocol_state_id;
		-- create condition value
		insert into protocol_value (id, code_kind, code_origin, code_type, code_type_and_kind, code_value, deleted, ignored, 
									  ls_kind, ls_transaction, ls_type, ls_type_and_kind, public_data, recorded_by, recorded_date, 
									  version, protocol_state_id)
			select nextval('value_pkseq'), 'boolean', 'ACAS DDICT', 'boolean', 'boolean_boolean', condition_column, false, false,
									  'condition column', transaction_id, 'codeValue', 'codeValue_condition column', true, 'ACAS', now(),
									  0, protocol_state_id;
		
		return protocol_state_id;
	end
$$ language plpgsql;

-- Create states and values on any protocol that is missing data column order states
select create_protocol_data_column_order_state(protocol_id, (select id from ls_transaction where comments = 'V2.4.1.0 data column order states migration'),
										   new_column_order::int, column_type, column_name, units, concentration, conc_units, hide_column, condition_column)
	from tmp_expt_column_order_data where protocol_id in
		(select p.id from protocol p
			where not exists (
				select 1 from protocol_state ps where ps.ls_kind = 'data column order' and ps.protocol_id = p.id
			)
		);

-- End Part 3

-- Part 4: Migrate any "stringValue" data column order values to "codeValue"
-- function for migrating experiment values
create or replace function fix_experiment_string_values(ls_kind varchar, code_type varchar, code_kind varchar) returns int as $$
	declare
		rows_affected int;
	begin
		update experiment_value set code_kind = $3, 
								code_origin = 'ACAS DDICT',
								code_type = $2,
								code_type_and_kind = $2 || '_' || $3,
								ls_type = 'codeValue',
								ls_type_and_kind = 'codeValue_' || $1,
								code_value = string_value,
								string_value = null,
								modified_by = 'ACAS',
								modified_date = now(),
								version = version+1
		where experiment_value.ls_kind = $1 and ls_type = 'stringValue';
	get diagnostics rows_affected = ROW_COUNT;
	return rows_affected;
	end
$$ language plpgsql;

--migrate experiment values
select fix_experiment_string_values('column type', 'data column', 'column type');
select fix_experiment_string_values('column name', 'data column', 'column name');
select fix_experiment_string_values('column units', 'data column', 'column units');
select fix_experiment_string_values('column conc units', 'data column', 'column conc units');
select fix_experiment_string_values('hide column', 'boolean', 'boolean');
select fix_experiment_string_values('condition column', 'boolean', 'boolean');

-- function for migrating protocol values
create or replace function fix_protocol_string_values(ls_kind varchar, code_type varchar, code_kind varchar) returns int as $$
	declare
		rows_affected int;
	begin
		update protocol_value set code_kind = $3, 
								code_origin = 'ACAS DDICT',
								code_type = $2,
								code_type_and_kind = $2 || '_' || $3,
								ls_type = 'codeValue',
								ls_type_and_kind = 'codeValue_' || $1,
								code_value = string_value,
								string_value = null,
								modified_by = 'ACAS',
								modified_date = now(),
								version = version+1
		where protocol_value.ls_kind = $1 and ls_type = 'stringValue';
	get diagnostics rows_affected = ROW_COUNT;
	return rows_affected;
	end
$$ language plpgsql;

--migrate protocol values
select fix_protocol_string_values('column type', 'data column', 'column type');
select fix_protocol_string_values('column name', 'data column', 'column name');
select fix_protocol_string_values('column units', 'data column', 'column units');
select fix_protocol_string_values('column conc units', 'data column', 'column conc units');
select fix_protocol_string_values('hide column', 'boolean', 'boolean');
select fix_protocol_string_values('condition column', 'boolean', 'boolean');

-- Cleanup: drop tables and functions created during this migration
drop table tmp_expt_columns;
drop table tmp_expt_column_order_data;
drop function add_data_column_order_ddict_value(varchar, varchar);
drop function create_expt_data_column_order_state(bigint, bigint, int, varchar, varchar, varchar, float8, varchar, boolean, boolean);
drop function create_protocol_data_column_order_state(bigint, bigint, int, varchar, varchar, varchar, float8, varchar, varchar, varchar);
drop function fix_experiment_string_values(varchar, varchar, varchar);
drop function fix_protocol_string_values(varchar, varchar, varchar);