--CREATE
CREATE OR REPLACE VIEW application_paths
as
select cmpd_reg_app_setting.id, cmpd_reg_app_setting.prop_name, cmpd_reg_app_setting_pivot_full_path.full_path || '/' || cmpd_reg_app_setting.prop_value as path
from (
select *, prefix || '://' || host ||':' || port ||'/' || path as full_path
from (
select 
	max(case when prop_name = 'prefix' then prop_value else null end) as prefix,
	max(case when prop_name = 'host' then prop_value else null end) as host,
	max(case when prop_name = 'port' then prop_value else null end) as port,
	max(case when prop_name = 'path' then prop_value else null end) as path
from cmpd_reg_app_setting
) cmpd_reg_app_setting_pivot
) cmpd_reg_app_setting_pivot_full_path, cmpd_reg_app_setting
where cmpd_reg_app_setting.prop_name like '%_path';
GRANT ALL ON TABLE application_paths
   TO compound;
  
CREATE OR REPLACE VIEW api_file_list AS 
 SELECT file_list.id AS file_id, 
	lot.corp_name AS lot_corp_name, 
	lot.id AS lot_id, file_list.name, 
	file_list.description, 
	'<A HREF="' || application_paths.path || replace(file_list.url, ' ', '%20') || '">' || file_list.name || ' (' || file_list.description || ')</A>' AS fileref
   FROM application_paths,
	lot
   JOIN file_list ON lot.id = file_list.lot
   where application_paths.prop_name = 'file_path';
GRANT ALL ON TABLE application_paths
   TO compound;

CREATE OR REPLACE VIEW api_salt_iso_salt AS 
 SELECT iso_salt.id, iso_salt.equivalents, iso_salt.ignore, iso_salt.type, iso_salt.version, iso_salt.isotope, iso_salt.salt, iso_salt.salt_form, salt.id AS salt_id, salt.abbrev AS salt_abbrev, salt.cd_id AS salt_cd_id, salt.formula AS salt_formula, salt.ignore AS salt_ignore, salt.mol_structure AS salt_mol_structure, salt.mol_weight AS salt_molweight, salt.name AS salt_name, salt.original_structure AS salt_original_structure, salt.version AS salt_version
   FROM salt
   JOIN iso_salt ON salt.id = iso_salt.salt;
GRANT ALL ON TABLE application_paths
   TO compound;

CREATE OR REPLACE VIEW api_isotope_iso_salt AS 
 SELECT iso_salt.id, iso_salt.equivalents, iso_salt.ignore, iso_salt.type, 
    iso_salt.version, iso_salt.isotope, iso_salt.salt, iso_salt.salt_form,
    isotope.id as isotope_id,
    isotope.abbrev as isotope_abbrev,
    isotope.ignore as isotope_ignore,
    isotope.mass_change as isotope_mass_change,
    isotope.name as isotope_name,
    isotope.version as isotope_version
   FROM isotope
   JOIN iso_salt ON isotope.id = iso_salt.isotope;
GRANT ALL ON TABLE application_paths
   TO compound;

CREATE OR REPLACE VIEW api_batch_cmpd_reg_links AS 
 SELECT lot.corp_name AS lot_corp_name, 
	lot.id AS lot_id, 
	'<A HREF="' || application_paths.path || '/' || replace(lot.corp_name, ' ', '%20') || '">'|| lot.corp_name|| '</A>' AS lot_registration_atag,
	application_paths.path || '/' || replace(lot.corp_name, ' ', '%20') AS lot_registration_url
   FROM lot,
	application_paths
	where application_paths.prop_name = 'lot_path';
GRANT ALL ON TABLE application_paths
   TO compound;
