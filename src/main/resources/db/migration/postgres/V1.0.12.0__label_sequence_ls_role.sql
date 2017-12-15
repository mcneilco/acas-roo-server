CREATE TABLE IF NOT EXISTS label_sequence_ls_role (
	id bigint NOT NULL PRIMARY KEY,
    version integer,
    label_sequence_id bigint NOT NULL REFERENCES label_sequence(id),
    ls_role_id bigint NOT NULL REFERENCES ls_role(id)
);

DO $$
BEGIN

IF to_regclass('idx_labelseq_lsrole_uniq') IS NULL THEN
    CREATE UNIQUE INDEX idx_labelseq_lsrole_uniq ON label_sequence_ls_role (label_sequence_id, ls_role_id);
END IF;

END$$;

DO $$
BEGIN

	IF NOT EXISTS(SELECT TRUE FROM pg_class c
		WHERE c.relname = 'labelseq_role_pkseq'
	) THEN
		CREATE SEQUENCE labelseq_role_pkseq;
	END IF;
END $$;