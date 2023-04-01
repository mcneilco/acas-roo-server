CREATE UNIQUE INDEX IF NOT EXISTS label_sequence_thing_label_prefix_key
 ON label_sequence (thing_type_and_kind, label_type_and_kind, label_prefix)
 WHERE ignored IS FALSE;
