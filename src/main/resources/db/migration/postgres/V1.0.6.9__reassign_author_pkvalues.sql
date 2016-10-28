-- need to reassign author abstract_thing id because of an entity change

-- duplicate existing authors
insert into author(
id, activation_date, activation_key, email_address, 
enabled, first_name, last_name, locked, modified_date, password,
recorded_date, user_name, version, code_name, deleted, ignored, ls_kind, ls_type, 
modified_by, recorded_by
)
SELECT nextval('thing_pkseq'),
activation_date, activation_key, concat(email_address,'-TODELETE'), 
enabled, first_name, last_name, locked, now(), password,
recorded_date, concat(user_name,'-TODELETE'), version, concat(code_name, '-TODELETE'), deleted, ignored, ls_kind, ls_type, 
modified_by, recorded_by
from author;

--update author_role table
UPDATE author_role SET author_id = new_id 
FROM (select old.id as old_id, new.id as new_id 
FROM author old JOIN author new ON old.user_name || '-TODELETE' = new.user_name 
AND old.id < new.id) as foo WHERE author_id = old_id;

-- drop old authors
delete from author where modified_date < now()::date or modified_date is null;


-- remove extra tag on names
update author set email_address=(select replace(email_address, '-TODELETE', ''));
update author set user_name=(select replace(user_name, '-TODELETE', ''));
update author set code_name=(select replace(code_name, '-TODELETE', ''));
