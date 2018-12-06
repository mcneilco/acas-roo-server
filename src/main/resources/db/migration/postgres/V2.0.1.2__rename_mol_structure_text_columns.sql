SELECT lo_unlink(as_drawn_struct::int) FROM lot;
SELECT lo_unlink(mol_structure::int) FROM parent;
SELECT lo_unlink(mol_structure::int) FROM salt;
SELECT lo_unlink(original_structure::int) FROM salt;
SELECT lo_unlink(mol_structure::int) FROM salt_form;


ALTER TABLE lot ALTER COLUMN as_drawn_struct SET DATA TYPE text USING as_drawn_struct_text_temp;
ALTER TABLE parent ALTER COLUMN mol_structure SET DATA TYPE text USING mol_structure_text_temp;
ALTER TABLE salt ALTER COLUMN mol_structure SET DATA TYPE text USING mol_structure_text_temp;
ALTER TABLE salt ALTER COLUMN original_structure SET DATA TYPE text USING original_structure_text_temp;
ALTER TABLE salt_form ALTER COLUMN mol_structure SET DATA TYPE text USING mol_structure_text_temp;

ALTER TABLE lot DROP COLUMN as_drawn_struct_text_temp;
ALTER TABLE parent DROP COLUMN mol_structure_text_temp;
ALTER TABLE salt DROP mol_structure_text_temp;
ALTER TABLE salt DROP COLUMN original_structure_text_temp;
ALTER TABLE salt_form DROP COLUMN mol_structure_text_temp;