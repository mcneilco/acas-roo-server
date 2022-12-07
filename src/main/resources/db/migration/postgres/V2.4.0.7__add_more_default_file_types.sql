-- add new options into "file_type" if they aren't already there
with vals as (values ('1H-NMR'), ('13C-NMR'), ('19F-NMR'), ('Chiral HPLC'))
insert into file_type (id, code, name, version)
select nextval('hibernate_sequence'), vals.column1 as code, vals.column1 as name, 0 as version
from vals
where not exists (select 1 from file_type where code = vals.column1);