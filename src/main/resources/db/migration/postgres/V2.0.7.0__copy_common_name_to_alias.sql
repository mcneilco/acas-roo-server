--copies parent.common_name into an alias in parent_alias if that name does not already exist as an alias
INSERT INTO parent_alias (id, version, parent, alias_name, ls_type, ls_kind, ignored, deleted, preferred) 
	SELECT nextval('hibernate_sequence'), 0, id, common_name, 'other name', 'Parent Common Name', false, false, false
	from parent where (id, common_name) NOT IN (select parent, alias_name from parent_alias) AND common_name <> '';