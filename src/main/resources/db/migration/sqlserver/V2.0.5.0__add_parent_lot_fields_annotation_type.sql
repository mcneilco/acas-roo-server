--parent_annotation
CREATE TABLE parent_annotation (
    [id] [numeric](19, 0) IDENTITY(1,1) NOT NULL,
    [code] [varchar](255) NULL,
    [name] [varchar](255) NULL,
    [comment] [varchar](255) NULL,
    [display_order] [int] NULL,
    [version] [int] NULL,
  PRIMARY KEY CLUSTERED 
(
	[id] ASC
),
CONSTRAINT [UQ_parent_annot_code] UNIQUE NONCLUSTERED 
(
	[code]
)
) ON [PRIMARY]

      
--compound_type
CREATE TABLE compound_type (
    [id] [numeric](19, 0) IDENTITY(1,1) NOT NULL,
    [code] [varchar](255) NULL,
    [name] [varchar](255) NULL,
    [comment] [varchar](255) NULL,
    [display_order] [int] NULL,
    [version] [int] NULL,
  PRIMARY KEY CLUSTERED 
(
	[id] ASC
),
CONSTRAINT [UQ_compound_type_code] UNIQUE NONCLUSTERED 
(
	[code]
)
) ON [PRIMARY]



--new flat columns: parent
ALTER TABLE [parent] ADD [comment] [text] NULL,
                     [exact_mass] [float] NULL;
                     
--new flat columns: lot
ALTER TABLE [lot] ADD [lambda] [float] NULL,
                 [absorbance] [float] NULL,
                 [stock_solvent] [varchar](255) NULL,
                 [stock_location] [varchar](255) NULL,
                 [retain_location] [varchar](255) NULL;
--GO

--registered by
ALTER TABLE parent ADD registered_by [numeric](19, 0);
      
ALTER TABLE lot ADD registered_by [numeric](19, 0);

      
