ALTER TABLE ONLY ls_role DROP CONSTRAINT ls_role_role_name_key;
UPDATE role_kind SET ls_type_and_kind = (select type_name from role_type where role_type.id = role_kind.ls_type) || '_' || kind_name ;
ALTER TABLE role_kind ALTER COLUMN ls_type_and_kind SET NOT NULL;
ALTER TABLE ls_role ADD COLUMN ls_type character varying(64),
					ADD COLUMN ls_kind character varying(255),
					ADD COLUMN ls_type_and_kind character varying(255);
UPDATE ls_role SET ls_type_and_kind = ls_type || '_' || ls_kind ;
ALTER TABLE ls_role ADD CONSTRAINT ls_role_type_kind_name_unique UNIQUE (ls_type, ls_kind, role_name);
ALTER TABLE ls_role ADD CONSTRAINT ls_role_tk_fk FOREIGN KEY (ls_type_and_kind) REFERENCES role_kind (ls_type_and_kind);