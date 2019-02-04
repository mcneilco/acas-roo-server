CREATE TABLE [compound]
(
  [id] [numeric](19, 0) IDENTITY(1,1) NOT NULL,
  [version] [int] NULL,
  [corp_name] [varchar](255) NULL,
  [external_id] [varchar](255) NULL,
  [cd_id] [int] NOT NULL,
  [ignored] [tinyint] NULL,
  [deleted] [tinyint] NULL,
  [created_date] [datetime] NULL,
  [modified_date] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)
) ON [PRIMARY]

CREATE INDEX [compound_cdid_idx] ON [compound]
(
	[cd_id] ASC
) 
 
