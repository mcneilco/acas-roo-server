-- Table: bulk_load_file
CREATE TABLE [bulk_load_file]
(
  [id] [numeric](19, 0) IDENTITY(1,1) NOT NULL,
  [file_name] [varchar](1000) NULL,
  [file_size] [int] NOT NULL,
  [json_template] [text] NULL,
  [number_of_mols] [int] NOT NULL,
  [recorded_by] [varchar](255) NOT NULL,
  [version] [int] NULL,
  PRIMARY KEY CLUSTERED 
(
	[id] ASC
)
) ON [PRIMARY]

-- Table: bulk_load_template
 CREATE TABLE bulk_load_template
(
  [id] [numeric](19, 0) IDENTITY(1,1) NOT NULL,
  [json_template] [text] NULL,
  [recorded_by] [varchar](255) NOT NULL,
  [template_name] character varying(255),
  [version] [int] NULL,
  [ignored] [tinyint] NOT NULL,
  PRIMARY KEY CLUSTERED 
(
	[id] ASC
),
CONSTRAINT [UQ_template_recorded_by] UNIQUE NONCLUSTERED 
(
	[template_name], [recorded_by]
)
) ON [PRIMARY]

 
--adding columns to lot, salt_form, and parent to reference bulk_load_file
ALTER TABLE [lot] ADD [bulk_load_file] [numeric](19, 0);
ALTER TABLE [lot] ADD CONSTRAINT [lot_bulk_load_file] FOREIGN KEY (bulk_load_file)
      REFERENCES [bulk_load_file] ([id]);
ALTER TABLE [salt_form] ADD [bulk_load_file] [numeric](19, 0);
ALTER TABLE [salt_form] ADD CONSTRAINT [salt_form_bulk_load_file] FOREIGN KEY (bulk_load_file)
      REFERENCES [bulk_load_file] ([id]);
ALTER TABLE [parent] ADD [bulk_load_file] [numeric](19, 0);
ALTER TABLE [parent] ADD CONSTRAINT [parent_bulk_load_file] FOREIGN KEY (bulk_load_file)
      REFERENCES [bulk_load_file] ([id]);
      
