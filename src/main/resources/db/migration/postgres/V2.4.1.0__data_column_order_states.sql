do $script$
begin
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
			agv_batch_code.concentration,
			agv_batch_code.conc_unit as conc_units,
			agv_time.numeric_value as col_time,
			agv_time.unit_kind as time_units,
			not agv.public_data as hide_column
		from protocol p
		join experiment e on p.id = e.protocol_id and e.ignored = false
		join experiment_analysisgroup ea on e.id = ea.experiment_id 
		join analysis_group ag on ag.id = ea.analysis_group_id and ag.ignored = false
		join analysis_group_state ags on ags.analysis_group_id = ag.id and ags.ignored = false and ags.ls_kind != 'dose response'
		join analysis_group_value agv on ags.id = agv.analysis_state_id and agv.ignored = false and agv.ls_kind not in ('batch code', 'time')
		join analysis_group_value agv_batch_code on ags.id = agv_batch_code.analysis_state_id and agv_batch_code.ignored = false and agv_batch_code.ls_kind = 'batch code'
		left join analysis_group_value agv_time on ags.id = agv_time.analysis_state_id and agv_time.ignored = false and agv_time.ls_kind = 'time'
		where p.ignored = false
	) a;

-- Exit early if there is no expt data as this indicates a fresh system
if not exists (select * from tmp_expt_columns)
then
	drop table tmp_expt_columns;
	return;
end if;

--Create an ls_transaction to tie all our states and values to
insert into ls_transaction (id, comments, recorded_date, version, recorded_by)
	select nextval('ls_transaction_pkseq'), 'V2.4.1.0 data column order states migration', now(), 0, 'ACAS';

-- Part 1: Fill in 'data column' DDict Values based on saved data
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
perform add_data_column_ddict_value(column_name, 'column name'::varchar) from
(select distinct column_name from tmp_expt_columns) a;
--units
perform add_data_column_ddict_value(units, 'column units'::varchar) from
(select distinct units from tmp_expt_columns) a where a.units is not null;
--conc units
perform add_data_column_ddict_value(conc_units, 'column conc units'::varchar) from
(select distinct conc_units from tmp_expt_columns) a where a.conc_units is not null;
--time units
perform add_data_column_ddict_value(time_units, 'column time units'::varchar) from
(select distinct time_units from tmp_expt_columns) a where a.time_units is not null;
--End Part 1

-- Part 2: Create missing value_kinds for new codeValues
insert into value_kind (id, kind_name, ls_type_and_kind, version, ls_type) 
	select nextval('value_kind_pkseq'), 'column name', 'codeValue_column name', 0, (select id from value_type where type_name = 'codeValue')
	on conflict do nothing;
insert into value_kind (id, kind_name, ls_type_and_kind, version, ls_type) 
	select nextval('value_kind_pkseq'), 'column type', 'codeValue_column type', 0, (select id from value_type where type_name = 'codeValue')
	on conflict do nothing;
insert into value_kind (id, kind_name, ls_type_and_kind, version, ls_type) 
	select nextval('value_kind_pkseq'), 'column units', 'codeValue_column units', 0, (select id from value_type where type_name = 'codeValue')
	on conflict do nothing;
insert into value_kind (id, kind_name, ls_type_and_kind, version, ls_type) 
	select nextval('value_kind_pkseq'), 'column conc units', 'codeValue_column conc units', 0, (select id from value_type where type_name = 'codeValue')
	on conflict do nothing;
insert into value_kind (id, kind_name, ls_type_and_kind, version, ls_type) 
	select nextval('value_kind_pkseq'), 'column time units', 'codeValue_column time units', 0, (select id from value_type where type_name = 'codeValue')
	on conflict do nothing;
insert into value_kind (id, kind_name, ls_type_and_kind, version, ls_type) 
	select nextval('value_kind_pkseq'), 'hide column', 'codeValue_hide column', 0, (select id from value_type where type_name = 'codeValue')
	on conflict do nothing;
insert into value_kind (id, kind_name, ls_type_and_kind, version, ls_type) 
	select nextval('value_kind_pkseq'), 'condition column', 'codeValue_condition column', 0, (select id from value_type where type_name = 'codeValue')
	on conflict do nothing;

--Part 2: Create "data column order" states on experiments that are missing them
-- Function to create a "data column order" experiment states and the values within it
create or replace function create_expt_data_column_order_state(expt_id bigint, transaction_id bigint, column_order int, column_type varchar, column_name varchar, 
															   units varchar, concentration float8, conc_units varchar, col_time float8, time_units varchar, hide_column boolean, 
															   condition_column boolean) returns bigint 
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
		if units is not null then
			insert into experiment_value (id, code_kind, code_origin, code_type, code_type_and_kind, code_value, deleted, ignored, 
										ls_kind, ls_transaction, ls_type, ls_type_and_kind, public_data, recorded_by, recorded_date, 
										version, experiment_state_id)
				select nextval('value_pkseq'), 'data column', 'ACAS DDICT', 'column units', 'data column_column units', units, false, false,
										'column units', transaction_id, 'codeValue', 'codeValue_column units', true, 'ACAS', now(),
										0, expt_state_id;
		end if;
		-- create concentration value
		if concentration is not null then
			insert into experiment_value (id, deleted, ignored, numeric_value,
										ls_kind, ls_transaction, ls_type, ls_type_and_kind, public_data, recorded_by, recorded_date, 
										version, experiment_state_id)
				select nextval('value_pkseq'), false, false, concentration,
										'column concentration', transaction_id, 'numericValue', 'numericValue_column concentration', true, 'ACAS', now(),
										0, expt_state_id;
		end if;
		-- create conc_units value
		if conc_units is not null then
			insert into experiment_value (id, code_kind, code_origin, code_type, code_type_and_kind, code_value, deleted, ignored, 
										ls_kind, ls_transaction, ls_type, ls_type_and_kind, public_data, recorded_by, recorded_date, 
										version, experiment_state_id)
				select nextval('value_pkseq'), 'data column', 'ACAS DDICT', 'column conc units', 'data column_column conc units', conc_units, false, false,
										'column conc units', transaction_id, 'codeValue', 'codeValue_column conc units', true, 'ACAS', now(),
										0, expt_state_id;
		end if;
		if col_time is not null then
			insert into experiment_value (id, deleted, ignored, numeric_value,
										ls_kind, ls_transaction, ls_type, ls_type_and_kind, public_data, recorded_by, recorded_date, 
										version, experiment_state_id)
				select nextval('value_pkseq'), false, false, col_time,
										'column time', transaction_id, 'numericValue', 'numericValue_column time', true, 'ACAS', now(),
										0, expt_state_id;
		end if;
		-- create conc_units value
		if time_units is not null then
			insert into experiment_value (id, code_kind, code_origin, code_type, code_type_and_kind, code_value, deleted, ignored, 
										ls_kind, ls_transaction, ls_type, ls_type_and_kind, public_data, recorded_by, recorded_date, 
										version, experiment_state_id)
				select nextval('value_pkseq'), 'data column', 'ACAS DDICT', 'column time units', 'data column_column time units', time_units, false, false,
										'column time units', transaction_id, 'codeValue', 'codeValue_column time units', true, 'ACAS', now(),
										0, expt_state_id;
		end if;
		-- create hidden value
		if hide_column is not null then
			insert into experiment_value (id, code_kind, code_origin, code_type, code_type_and_kind, code_value, deleted, ignored, 
										ls_kind, ls_transaction, ls_type, ls_type_and_kind, public_data, recorded_by, recorded_date, 
										version, experiment_state_id)
				select nextval('value_pkseq'), 'boolean', 'ACAS DDICT', 'boolean', 'boolean_boolean', case when hide_column then 'TRUE' else 'FALSE' end, false, false,
										'hide column', transaction_id, 'codeValue', 'codeValue_hide column', true, 'ACAS', now(),
										0, expt_state_id;
		end if;
		-- create condition value
		if condition_column is not null then
			insert into experiment_value (id, code_kind, code_origin, code_type, code_type_and_kind, code_value, deleted, ignored, 
										ls_kind, ls_transaction, ls_type, ls_type_and_kind, public_data, recorded_by, recorded_date, 
										version, experiment_state_id)
				select nextval('value_pkseq'), 'boolean', 'ACAS DDICT', 'boolean', 'boolean_boolean', case when condition_column then 'TRUE' else 'FALSE' end, false, false,
										'condition column', transaction_id, 'codeValue', 'codeValue_condition column', true, 'ACAS', now(),
										0, expt_state_id;
		end if;
		
		return expt_state_id;
	end
$$ language plpgsql;

-- Create 'data column order' states and values for all experiments which are missing them
perform create_expt_data_column_order_state(experiment_id, (select id from ls_transaction where comments = 'V2.4.1.0 data column order states migration'),
										   column_order::int, column_type, column_name, units, concentration, conc_units, col_time, time_units, hide_column, false)
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
			col_type.string_value as column_type, --the coalesce is used to account for having the same data in stringValues or in codeValues
			col_name.string_value as column_name,
			col_units.string_value as units,
			col_conc.numeric_value as concentration,
		 	col_conc_units.string_value as conc_units,
			hide_col.string_value as hide_column,
			cond_col.string_value as condition_column,
			col_time.numeric_value as column_time,
			col_time_units.string_value as time_units,
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
		left join experiment_value col_time on col_time.experiment_state_id = es.id and col_time.ls_kind = 'column time' and col_time.ignored = false
		left join experiment_value col_time_units on col_time_units.experiment_state_id = es.id and col_time_units.ls_kind = 'column time units' and col_time_units.ignored = false
		group by 
			e.protocol_id,
			col_type.string_value,
			col_name.string_value,
			col_units.string_value,
			col_conc.numeric_value,
		 	col_conc_units.string_value,
			hide_col.string_value,
			cond_col.string_value,
			col_time.numeric_value,
			col_time_units.string_value
		) a;

-- Define a function to create the protocol column order states and values
-- this is nearly identical to the function above, just using protocol instead of experiment
create or replace function create_protocol_data_column_order_state(protocol_id bigint, transaction_id bigint, column_order int, column_type varchar, column_name varchar, 
															   units varchar, concentration float8, conc_units varchar, col_time float8, time_units varchar, hide_column varchar, 
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
		if units is not null then
			insert into protocol_value (id, code_kind, code_origin, code_type, code_type_and_kind, code_value, deleted, ignored, 
										ls_kind, ls_transaction, ls_type, ls_type_and_kind, public_data, recorded_by, recorded_date, 
										version, protocol_state_id)
				select nextval('value_pkseq'), 'data column', 'ACAS DDICT', 'column units', 'data column_column units', units, false, false,
										'column units', transaction_id, 'codeValue', 'codeValue_column units', true, 'ACAS', now(),
										0, protocol_state_id;
		end if;
		-- create concentration value
		if units is not null then
			insert into protocol_value (id, deleted, ignored, numeric_value,
										ls_kind, ls_transaction, ls_type, ls_type_and_kind, public_data, recorded_by, recorded_date, 
										version, protocol_state_id)
				select nextval('value_pkseq'), false, false, concentration,
										'column concentration', transaction_id, 'numericValue', 'numericValue_column concentration', true, 'ACAS', now(),
										0, protocol_state_id;
		end if;
		-- create conc_units value
		if units is not null then
		insert into protocol_value (id, code_kind, code_origin, code_type, code_type_and_kind, code_value, deleted, ignored, 
									  ls_kind, ls_transaction, ls_type, ls_type_and_kind, public_data, recorded_by, recorded_date, 
									  version, protocol_state_id)
			select nextval('value_pkseq'), 'data column', 'ACAS DDICT', 'column conc units', 'data column_column conc units', conc_units, false, false,
									  'column conc units', transaction_id, 'codeValue', 'codeValue_column conc units', true, 'ACAS', now(),
									  0, protocol_state_id;
		end if;
		if col_time is not null then
			insert into protocol_value (id, deleted, ignored, numeric_value,
										ls_kind, ls_transaction, ls_type, ls_type_and_kind, public_data, recorded_by, recorded_date, 
										version, protocol_state_id)
				select nextval('value_pkseq'), false, false, col_time,
										'column time', transaction_id, 'numericValue', 'numericValue_column time', true, 'ACAS', now(),
										0, protocol_state_id;
		end if;
		-- create conc_units value
		if time_units is not null then
			insert into protocol_value (id, code_kind, code_origin, code_type, code_type_and_kind, code_value, deleted, ignored, 
										ls_kind, ls_transaction, ls_type, ls_type_and_kind, public_data, recorded_by, recorded_date, 
										version, protocol_state_id)
				select nextval('value_pkseq'), 'data column', 'ACAS DDICT', 'column time units', 'data column_column time units', time_units, false, false,
										'column time units', transaction_id, 'codeValue', 'codeValue_column time units', true, 'ACAS', now(),
										0, protocol_state_id;
		end if;
		-- create hidden value
		if hide_column is not null then
			insert into protocol_value (id, code_kind, code_origin, code_type, code_type_and_kind, code_value, deleted, ignored, 
										ls_kind, ls_transaction, ls_type, ls_type_and_kind, public_data, recorded_by, recorded_date, 
										version, protocol_state_id)
				select nextval('value_pkseq'), 'boolean', 'ACAS DDICT', 'boolean', 'boolean_boolean', hide_column, false, false,
										'hide column', transaction_id, 'codeValue', 'codeValue_hide column', true, 'ACAS', now(),
										0, protocol_state_id;
		end if;
		-- create condition value
		if condition_column is not null then
			insert into protocol_value (id, code_kind, code_origin, code_type, code_type_and_kind, code_value, deleted, ignored, 
										ls_kind, ls_transaction, ls_type, ls_type_and_kind, public_data, recorded_by, recorded_date, 
										version, protocol_state_id)
				select nextval('value_pkseq'), 'boolean', 'ACAS DDICT', 'boolean', 'boolean_boolean', condition_column, false, false,
										'condition column', transaction_id, 'codeValue', 'codeValue_condition column', true, 'ACAS', now(),
										0, protocol_state_id;
		end if;
		
		return protocol_state_id;
	end
$$ language plpgsql;

-- Create states and values on any protocol that is missing data column order states
perform create_protocol_data_column_order_state(protocol_id, (select id from ls_transaction where comments = 'V2.4.1.0 data column order states migration'),
										   new_column_order::int, column_type, column_name, units, concentration, conc_units, column_time, time_units, hide_column, condition_column)
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
perform fix_experiment_string_values('column type', 'data column', 'column type');
perform fix_experiment_string_values('column name', 'data column', 'column name');
perform fix_experiment_string_values('column units', 'data column', 'column units');
perform fix_experiment_string_values('column conc units', 'data column', 'column conc units');
perform fix_experiment_string_values('hide column', 'boolean', 'boolean');
perform fix_experiment_string_values('condition column', 'boolean', 'boolean');

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
perform fix_protocol_string_values('column type', 'data column', 'column type');
perform fix_protocol_string_values('column name', 'data column', 'column name');
perform fix_protocol_string_values('column units', 'data column', 'column units');
perform fix_protocol_string_values('column conc units', 'data column', 'column conc units');
perform fix_protocol_string_values('hide column', 'boolean', 'boolean');
perform fix_protocol_string_values('condition column', 'boolean', 'boolean');

-- Cleanup: drop tables and functions created during this migration
drop table tmp_expt_columns;
drop table tmp_expt_column_order_data;
drop function add_data_column_ddict_value(varchar, varchar);
drop function create_expt_data_column_order_state(bigint, bigint, int, varchar, varchar, varchar, float8, varchar, float8, varchar, boolean, boolean);
drop function create_protocol_data_column_order_state(bigint, bigint, int, varchar, varchar, varchar, float8, varchar, float8, varchar, varchar, varchar);
drop function fix_experiment_string_values(varchar, varchar, varchar);
drop function fix_protocol_string_values(varchar, varchar, varchar);

end $script$