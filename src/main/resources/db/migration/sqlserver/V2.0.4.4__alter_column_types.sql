ALTER TABLE dbo.bulk_load_template
   ALTER COLUMN ignored BIT;
   
ALTER TABLE dbo.cmpd_reg_app_setting
   ALTER COLUMN ignored BIT;
   
ALTER TABLE dbo.compound
   ALTER COLUMN ignored BIT;
   
ALTER TABLE dbo.compound
   ALTER COLUMN deleted BIT;   
   
ALTER TABLE dbo.corp_name
   ALTER COLUMN ignore BIT;
   
ALTER TABLE dbo.file_list
   ALTER COLUMN ie BIT;   
   
ALTER TABLE dbo.file_list
   ALTER COLUMN uploaded BIT;   

ALTER TABLE dbo.iso_salt
   ALTER COLUMN ignore BIT;

ALTER TABLE dbo.isotope
   ALTER COLUMN ignore BIT;

ALTER TABLE dbo.parent
   ALTER COLUMN ignore BIT;
   
ALTER TABLE dbo.lot
   ALTER COLUMN ignore BIT;
   
IF EXISTS (SELECT name FROM sys.indexes WHERE name = 'Lot_Virtual_IDX')
    DROP INDEX Lot_Virtual_IDX ON lot;
          
ALTER TABLE dbo.lot
   ALTER COLUMN is_virtual BIT;

CREATE INDEX Lot_Virtual_IDX ON lot (is_virtual);   

ALTER TABLE dbo.lot_alias
   ALTER COLUMN deleted BIT;  
   
ALTER TABLE dbo.lot_alias
   ALTER COLUMN ignored BIT;  
   
ALTER TABLE dbo.lot_alias
   ALTER COLUMN preferred BIT;  
   
ALTER TABLE dbo.parent_alias
   ALTER COLUMN deleted BIT;  
   
ALTER TABLE dbo.parent_alias
   ALTER COLUMN ignored BIT;  
   
ALTER TABLE dbo.parent_alias
   ALTER COLUMN preferred BIT;        
 
IF EXISTS (SELECT name FROM sys.indexes WHERE name = 'PreDef_Skip_IDX')
    DROP INDEX PreDef_Skip_IDX ON pre_def_corp_name;
    
ALTER TABLE dbo.pre_def_corp_name
   ALTER COLUMN skip BIT;

CREATE INDEX PreDef_Skip_IDX ON pre_def_corp_name (skip);   
   
IF EXISTS (SELECT name FROM sys.indexes WHERE name = 'PreDef_Used_IDX')
    DROP INDEX PreDef_Used_IDX ON pre_def_corp_name;
    
ALTER TABLE dbo.pre_def_corp_name
   ALTER COLUMN used BIT;   
   
CREATE INDEX PreDef_Used_IDX ON pre_def_corp_name (used);   

ALTER TABLE dbo.salt
   ALTER COLUMN ignore BIT;  
   
ALTER TABLE dbo.salt_form
   ALTER COLUMN ignore BIT;   
   
ALTER TABLE dbo.salt_form_alias
   ALTER COLUMN deleted BIT;  
   
ALTER TABLE dbo.salt_form_alias
   ALTER COLUMN ignored BIT;  
   
ALTER TABLE dbo.salt_form_alias
   ALTER COLUMN preferred BIT; 
   
ALTER TABLE dbo.salt_loader
   ALTER COLUMN uploaded BIT;  

IF EXISTS (SELECT name FROM sys.indexes WHERE name = 'Lot_RegDate_IDX')
    DROP INDEX Lot_RegDate_IDX ON lot;

IF EXISTS (SELECT name FROM sys.indexes WHERE name = 'Lot_SynthDate_IDX')
    DROP INDEX Lot_SynthDate_IDX ON lot;
    
ALTER TABLE dbo.lot
   ALTER COLUMN registration_date datetime;

   ALTER TABLE dbo.lot
   ALTER COLUMN synthesis_date datetime;
   
ALTER TABLE dbo.lot
   ALTER COLUMN modified_date datetime;   

CREATE INDEX Lot_RegDate_IDX ON lot (registration_date);   
   
CREATE INDEX Lot_SynthDate_IDX ON lot (synthesis_date);   

IF EXISTS (SELECT name FROM sys.indexes WHERE name = 'Parent_RegDate_IDX')
    DROP INDEX Parent_RegDate_IDX ON parent;

ALTER TABLE dbo.parent
   ALTER COLUMN registration_date datetime;
   
ALTER TABLE dbo.parent
   ALTER COLUMN modified_date datetime;   

CREATE INDEX Parent_RegDate_IDX ON parent (registration_date);   
   
IF EXISTS (SELECT name FROM sys.indexes WHERE name = 'SaltForm_RegDate_IDX')
    DROP INDEX SaltForm_RegDate_IDX ON salt_form;

ALTER TABLE dbo.salt_form
   ALTER COLUMN registration_date datetime; 

CREATE INDEX SaltForm_RegDate_IDX ON salt_form (registration_date);   
      
ALTER TABLE dbo.salt_loader
   ALTER COLUMN loaded_date datetime;     
   
ALTER TABLE dbo.compound
   ALTER COLUMN created_date datetime;
   
ALTER TABLE dbo.compound
   ALTER COLUMN modified_date datetime;   
         
   
