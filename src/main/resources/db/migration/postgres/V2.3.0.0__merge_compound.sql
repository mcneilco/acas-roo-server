-- Run the following as superuser
/*
DO $$ 
BEGIN
RAISE NOTICE 'ATTEMPTING MIGRATION WHICH REQUIRES SUPERUSER.  Please run: "ALTER USER acas SUPERUSER;" you can revoke this later using ("ALTER USER acas WITH NOSUPERUSER;")';

IF EXISTS (
  SELECT schema_name FROM information_schema.schemata WHERE schema_name = 'bingo'
  ) THEN
  RAISE NOTICE 'FOUND BINGO SCHEMA, ATTEMPTING MIGRATION';
    GRANT usage on schema bingo to acas;
    GRANT select on all tables in schema bingo to acas;
    GRANT execute on all functions in schema bingo to acas;
 ELSE
   RAISE NOTICE 'BINGO SCHEMA NOT FOUND, NOT ATTEMPTING MIGRATION';
END IF;
END $$;

--Move Compound tables to acas schema and set ownership
UPDATE pg_catalog.pg_class
SET relnamespace = (SELECT oid FROM pg_catalog.pg_namespace
                      WHERE nspname = 'acas')
where relnamespace = (SELECT oid FROM pg_catalog.pg_namespace
                      WHERE nspname = 'compound')
and relname not in ('schema_version', 'schema_version_pk','schema_version_vr_idx', 'schema_version_ir_idx', 'schema_version_s_idx');

UPDATE pg_catalog.pg_class
SET relowner = (SELECT oid FROM pg_roles
                      WHERE rolname = 'acas')
where relowner = (SELECT oid FROM pg_roles
                      WHERE rolname = 'compound_admin');
     
update compound.schema_version set version_rank=version_rank+45, installed_rank=installed_rank+45, version=regexp_replace(version, '^.', '2'), script=regexp_replace(script, 'V1','V2');
insert into acas.schema_version select * from compound.schema_version;

*/

-- drop foreign keys to scientist
ALTER TABLE parent drop constraint "fkc4ab08aa99c37a24"; --chemist foreign key';
ALTER TABLE parent drop constraint "parent_modified_by_fk"; --modified_by foreign key';
ALTER TABLE parent drop constraint "parent_registered_by_fk"; --registered_by foreign key';
ALTER TABLE salt_form drop constraint "fk138dcb0d99c37a24"; --salt_form chemist foreign key';
ALTER TABLE lot drop constraint "fk1a35199c37a24"; --lot chemist foreign key';
ALTER TABLE lot drop constraint "lot_modified_by_fk"; --lot modified_by foreign key';
ALTER TABLE lot drop constraint "lot_registered_by_fk"; --lot modified_by foreign key';

--change column types of chemist
DROP VIEW IF EXISTS api_lot_properties;
CREATE FUNCTION get_chemist_code(bigint) RETURNS character varying(255) AS $$ SELECT code FROM scientist WHERE id = $1 $$ LANGUAGE SQL;
ALTER TABLE parent ALTER COLUMN chemist type character varying using get_chemist_code(chemist);
ALTER TABLE parent ALTER COLUMN modified_by type character varying using get_chemist_code(modified_by);
ALTER TABLE parent ALTER COLUMN registered_by type character varying using get_chemist_code(registered_by);
ALTER TABLE salt_form ALTER COLUMN chemist type character varying using get_chemist_code(chemist);
ALTER TABLE lot ALTER COLUMN chemist type character varying using get_chemist_code(chemist);
ALTER TABLE lot ALTER COLUMN modified_by type character varying using get_chemist_code(modified_by);
ALTER TABLE lot ALTER COLUMN registered_by type character varying using get_chemist_code(registered_by);
DROP FUNCTION get_chemist_code(bigint);

-- drop scientist table
DROP TABLE scientist;

-- drop foreign keys to project
ALTER TABLE lot drop constraint "fk1a35197886161";

--change colume types of project
CREATE FUNCTION get_project_code(bigint) RETURNS character varying(255) AS $$ SELECT code FROM project WHERE id = $1 $$ LANGUAGE SQL;
ALTER TABLE lot ALTER COLUMN project type character varying using get_project_code(project);

-- drop project table
DROP TABLE project;