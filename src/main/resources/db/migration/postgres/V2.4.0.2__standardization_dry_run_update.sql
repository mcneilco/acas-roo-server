-- As part of ACAS-480
CREATE INDEX IF NOT EXISTS parent_stereocomment_lower_idx on parent(lower(stereo_comment));

ALTER TABLE standardization_dry_run_compound ADD COLUMN IF NOT EXISTS sync_status character varying(255);
ALTER TABLE standardization_dry_run_compound DROP COLUMN IF EXISTS stereo_category;
ALTER TABLE standardization_dry_run_compound DROP COLUMN IF EXISTS stereo_comment;
ALTER TABLE standardization_dry_run_compound DROP COLUMN IF EXISTS old_mol_weight;

CREATE INDEX IF NOT EXISTS stndzn_dry_run_sync_status_idx on standardization_dry_run_compound(sync_status);
