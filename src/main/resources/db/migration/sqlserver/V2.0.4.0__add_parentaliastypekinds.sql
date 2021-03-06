-- add default parent alias types/kinds if they do not exist already

INSERT INTO [parent_alias_type] ([type_name], [version])
SELECT 'other name', 0
WHERE NOT EXISTS (SELECT id FROM parent_alias_type WHERE type_name = 'other name');

INSERT INTO [parent_alias_type] ([type_name], [version])
SELECT 'External ID', 0
WHERE NOT EXISTS (SELECT id FROM parent_alias_type WHERE type_name = 'External ID');

INSERT INTO [parent_alias_kind] ([ls_type], [kind_name], [version])
SELECT (SELECT id FROM parent_alias_type where type_name = 'other name'), 'Common Name', 0
WHERE NOT EXISTS (SELECT id FROM parent_alias_kind WHERE kind_name = 'Common Name');

INSERT INTO [parent_alias_kind] ([ls_type], [kind_name], [version])
SELECT (SELECT id FROM parent_alias_type where type_name = 'External ID'), 'LiveDesign Corp Name', 0
WHERE NOT EXISTS (SELECT id FROM parent_alias_kind WHERE kind_name = 'LiveDesign Corp Name');
