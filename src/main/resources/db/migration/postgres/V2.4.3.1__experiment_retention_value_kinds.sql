-- ACAS-ROO: Add value_kinds for experiment retention policy
-- Adds value_kinds for deleted experiment retention days, database deleted date, and files deleted date

INSERT INTO value_kind (id, kind_name, ls_type_and_kind, version, ls_type)
SELECT nextval('value_kind_pkseq'), 'deleted experiment retention days', 'numericValue_deleted experiment retention days', 0, vt.id
FROM value_type vt WHERE vt.type_name = 'numericValue'
ON CONFLICT (ls_type_and_kind) DO NOTHING;

INSERT INTO value_kind (id, kind_name, ls_type_and_kind, version, ls_type)
SELECT nextval('value_kind_pkseq'), 'database deleted date', 'dateValue_database deleted date', 0, vt.id
FROM value_type vt WHERE vt.type_name = 'dateValue'
ON CONFLICT (ls_type_and_kind) DO NOTHING;

INSERT INTO value_kind (id, kind_name, ls_type_and_kind, version, ls_type)
SELECT nextval('value_kind_pkseq'), 'files deleted date', 'dateValue_files deleted date', 0, vt.id
FROM value_type vt WHERE vt.type_name = 'dateValue'
ON CONFLICT (ls_type_and_kind) DO NOTHING;
