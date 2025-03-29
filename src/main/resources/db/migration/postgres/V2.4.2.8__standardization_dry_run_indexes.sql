-- ACAS-855
-- parent id, registration_status, existing_duplicates_count
CREATE INDEX IF NOT EXISTS stndzn_dry_run_parent_id_idx on standardization_dry_run_compound(parent_id);
CREATE INDEX IF NOT EXISTS stndzn_dry_run_registration_status_idx on standardization_dry_run_compound(registration_status);
CREATE INDEX IF NOT EXISTS stndzn_dry_run_existing_duplicate_count_idx on standardization_dry_run_compound(existing_duplicate_count);