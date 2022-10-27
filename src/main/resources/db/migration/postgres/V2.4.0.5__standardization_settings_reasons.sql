-- As part of ACAS-387
ALTER TABLE standardization_settings ADD COLUMN IF NOT EXISTS valid boolean;
ALTER TABLE standardization_settings ADD COLUMN IF NOT EXISTS invalid_reasons text;
ALTER TABLE standardization_settings ADD COLUMN IF NOT EXISTS needs_standardization_reasons text;
ALTER TABLE standardization_settings ADD COLUMN IF NOT EXISTS suggested_configuration_changes text;
TRUNCATE standardization_settings;