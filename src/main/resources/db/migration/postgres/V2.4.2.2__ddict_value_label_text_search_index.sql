DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM   pg_class c
        JOIN   pg_namespace n ON n.oid = c.relnamespace
        WHERE  c.relname = 'ddict_value_labeltext_tsvector_idx'
    )
    THEN
        CREATE INDEX ddict_value_labeltext_tsvector_idx ON ddict_value USING GIN (to_tsvector('english', label_text));
    END IF;
END
$$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM   pg_class c
        JOIN   pg_namespace n ON n.oid = c.relnamespace
        WHERE  c.relname = 'idx_ddictvalue_labeltext_lower_pattern'
    )
    THEN
        CREATE INDEX idx_ddictvalue_labeltext_lower_pattern ON ddict_value(lower(label_text) varchar_pattern_ops);
    END IF;
END
$$;