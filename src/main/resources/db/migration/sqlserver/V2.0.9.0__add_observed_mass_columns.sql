IF COL_LENGTH('lot','observed_mass_one') IS NULL
 BEGIN
 	ALTER TABLE lot ADD [observed_mass_one] [float] NULL,
					ADD [observed_mass_two] [float] NULL;
 END
