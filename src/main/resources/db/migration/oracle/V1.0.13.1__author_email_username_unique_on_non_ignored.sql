BEGIN
	BEGIN
		EXECUTE IMMEDIATE 'ALTER TABLE author DROP CONSTRAINT author_email_address_key';
		EXECUTE IMMEDIATE 'ALTER TABLE author DROP CONSTRAINT author_user_name_key';
	EXCEPTION
		WHEN OTHERS THEN
			IF SQLCODE = -2443 THEN
			  NULL;
			ELSE
			  RAISE;
			END IF;
	END;
	BEGIN
		EXECUTE IMMEDIATE 'CREATE UNIQUE INDEX author_email_address_uq ON author ((CASE WHEN ignored = 0 THEN email_address ELSE null END))';
		EXECUTE IMMEDIATE 'CREATE UNIQUE INDEX author_user_name_uq ON author ((CASE WHEN ignored = 0 THEN user_name ELSE null END))';
	EXCEPTION
		WHEN OTHERS THEN
			IF SQLCODE = -2443 THEN
			  NULL;
			ELSE
			  RAISE;
			END IF;
	END;
END;