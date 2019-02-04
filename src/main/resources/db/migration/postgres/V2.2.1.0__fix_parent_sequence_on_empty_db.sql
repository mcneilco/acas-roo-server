DO $$
DECLARE
	numParents int;
BEGIN
	SELECT COUNT(*) INTO numParents FROM parent;
	IF numParents = 0 THEN
		ALTER SEQUENCE custom_parent_pkseq RESTART WITH 1;
	END IF;
END $$