--copies parent.common_name into an alias in parent_alias if that name does not already exist as an alias
INSERT INTO parent_alias (version, parent, alias_name, ls_type, ls_kind, ignored, deleted, preferred) 
	SELECT  0, id, common_name, 'other name', 'Parent Common Name', 0, 0, 0
	from parent where id NOT IN (select parent.id from parent join parent_alias on parent.id = parent_alias.parent
	 and parent.common_name = parent_alias.alias_name) AND common_name <> '';
