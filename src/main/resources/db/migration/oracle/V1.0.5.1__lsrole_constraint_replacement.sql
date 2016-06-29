ALTER TABLE ls_role DROP UNIQUE (ROLE_NAME) ;
UPDATE role_kind SET ls_type_and_kind = (select type_name from role_type where role_type.id = role_kind.ls_type) || '_' || kind_name ;
ALTER TABLE "ROLE_KIND" MODIFY ("LS_TYPE_AND_KIND" NOT NULL ENABLE);
ALTER TABLE "LS_ROLE" ADD (ls_type VARCHAR2(64 CHAR),
							ls_kind VARCHAR2(255 CHAR),
							ls_type_and_kind VARCHAR2(255 CHAR));
UPDATE ls_role SET ls_type_and_kind = (ls_type || '_' || ls_kind) ;
ALTER TABLE ls_role ADD CONSTRAINT ls_role_type_kind_name_unique UNIQUE (ls_type, ls_kind, role_name);
ALTER TABLE ls_role ADD CONSTRAINT ls_role_tk_fk FOREIGN KEY (ls_type_and_kind) REFERENCES role_kind (ls_type_and_kind);