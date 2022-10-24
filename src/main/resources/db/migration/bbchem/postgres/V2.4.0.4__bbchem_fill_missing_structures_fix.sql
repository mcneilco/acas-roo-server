-- Delete bbchem_salt_form structures which are a result of having an empty mol_structure.
DELETE FROM bbchem_salt_form_structure
USING salt_form
WHERE bbchem_salt_form_structure.id = salt_form.cd_id
and salt_form.mol_structure = '';

-- Update cd_ids to be 0 for compounds which have an empty mol_structure
UPDATE salt_form
SET cd_id = 0
where salt_form.mol_structure = '';