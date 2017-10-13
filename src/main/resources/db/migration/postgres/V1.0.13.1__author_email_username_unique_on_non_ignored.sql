DO $$
BEGIN
	BEGIN
		ALTER TABLE author DROP CONSTRAINT author_email_address_key;
		ALTER TABLE author DROP CONSTRAINT author_user_name_key;
	EXCEPTION
		WHEN OTHERS THEN
			raise notice '% %', SQLERRM, SQLSTATE;
	END;
	BEGIN
		CREATE UNIQUE INDEX author_email_address_uq ON author (email_address) WHERE (ignored is FALSE);
		CREATE UNIQUE INDEX author_user_name_uq ON author (user_name) WHERE (ignored is FALSE);
	EXCEPTION
		WHEN OTHERS THEN
			raise notice '% %', SQLERRM, SQLSTATE;
	END;
END $$