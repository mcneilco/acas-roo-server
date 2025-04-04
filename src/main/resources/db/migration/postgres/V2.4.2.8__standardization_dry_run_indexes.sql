-- ACAS-860
-- parent id, registration_status, existing_duplicates_count, new_duplicates_count, changed_structure
CREATE INDEX IF NOT EXISTS stndzn_dry_run_parent_id_idx on standardization_dry_run_compound(parent_id);
CREATE INDEX IF NOT EXISTS stndzn_dry_run_registration_status_idx on standardization_dry_run_compound(registration_status);
CREATE INDEX IF NOT EXISTS stndzn_dry_run_existing_duplicate_count_idx on standardization_dry_run_compound(existing_duplicate_count);
CREATE INDEX IF NOT EXISTS stndzn_dry_run_new_duplicate_count_idx on standardization_dry_run_compound(new_duplicate_count);
CREATE INDEX IF NOT EXISTS stndzn_dry_run_changed_structure_idx on standardization_dry_run_compound(changed_structure);

-- Create a unique constraint on parent_id
CREATE UNIQUE INDEX IF NOT EXISTS stndzn_dry_run_parent_id_unique_idx on standardization_dry_run_compound(parent_id) where parent_id is not null;