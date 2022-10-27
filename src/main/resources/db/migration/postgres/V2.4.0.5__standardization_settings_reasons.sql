-- As part of ACAS-387
ALTER TABLE standardization_settings ADD COLUMN IF NOT EXISTS reasons text;
ALTER TABLE standardization_settings ADD COLUMN IF NOT EXISTS suggested_configuration_changes text;
