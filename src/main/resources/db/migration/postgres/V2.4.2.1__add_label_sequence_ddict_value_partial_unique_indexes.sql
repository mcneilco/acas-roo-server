CREATE UNIQUE INDEX IF NOT EXISTS label_sequence_thing_label_prefix_key
 ON label_sequence (thing_type_and_kind, label_type_and_kind)
 WHERE ignored IS FALSE;
CREATE UNIQUE INDEX IF NOT EXISTS ddict_value_code_type_kind_short_name_key
 ON ddict_value (ls_type, ls_kind, short_name)
 WHERE ignored IS FALSE;
