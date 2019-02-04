IF COL_LENGTH('lot','vendor_id') IS NULL
 BEGIN
 	ALTER TABLE lot ADD [varchar](255) NULL,
 END
