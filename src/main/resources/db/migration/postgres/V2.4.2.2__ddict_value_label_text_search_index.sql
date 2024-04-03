DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM   pg_class c
        JOIN   pg_namespace n ON n.oid = c.relnamespace
        WHERE  c.relname = 'ddict_value_labeltext_web_idx'
    )
    THEN
        CREATE INDEX ddict_value_labeltext_web_idx ON ddict_value USING GIN (to_tsvector('english', label_text));
    END IF;
END
$$;