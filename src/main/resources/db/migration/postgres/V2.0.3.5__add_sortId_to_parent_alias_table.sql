-- add integer column sort_id

ALTER TABLE ONLY parent_alias
    ADD COLUMN sort_id integer DEFAULT 0;







