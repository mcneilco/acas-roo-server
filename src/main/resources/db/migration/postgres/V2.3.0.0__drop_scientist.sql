-- drop foreign keys to scientist
ALTER TABLE parent drop constraint "fkc4ab08aa99c37a24"; --chemist foreign key';
ALTER TABLE parent drop constraint "parent_modified_by_fk"; --modified_by foreign key';
ALTER TABLE parent drop constraint "parent_registered_by_fk"; --registered_by foreign key';
ALTER TABLE salt_form drop constraint "fk138dcb0d99c37a24"; --salt_form chemist foreign key';
ALTER TABLE lot drop constraint "fk1a35199c37a24"; --lot chemist foreign key';
ALTER TABLE lot drop constraint "lot_modified_by_fk"; --lot modified_by foreign key';
ALTER TABLE lot drop constraint "lot_registered_by_fk"; --lot modified_by foreign key';

--change column types of chemist
CREATE FUNCTION get_chemist_code(bigint) RETURNS character varying(255) AS $$ SELECT code FROM scientist WHERE id = $1 $$ LANGUAGE SQL;

ALTER TABLE parent ALTER COLUMN chemist type character varying using get_chemist_code(chemist);
ALTER TABLE parent ALTER COLUMN modified_by type character varying using get_chemist_code(modified_by);
ALTER TABLE parent ALTER COLUMN registered_by type character varying using get_chemist_code(registered_by);
ALTER TABLE salt_form ALTER COLUMN chemist type character varying using get_chemist_code(chemist);
ALTER TABLE lot ALTER COLUMN chemist type character varying using get_chemist_code(chemist);
ALTER TABLE lot ALTER COLUMN modified_by type character varying using get_chemist_code(modified_by);
ALTER TABLE lot ALTER COLUMN registered_by type character varying using get_chemist_code(registered_by);
