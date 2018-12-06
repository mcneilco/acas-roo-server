ALTER TABLE lot ADD COLUMN as_drawn_struct_text_temp text;
ALTER TABLE parent ADD COLUMN mol_structure_text_temp text ;
ALTER TABLE salt ADD COLUMN mol_structure_text_temp text;
ALTER TABLE salt ADD COLUMN original_structure_text_temp text;
ALTER TABLE salt_form ADD COLUMN mol_structure_text_temp text;