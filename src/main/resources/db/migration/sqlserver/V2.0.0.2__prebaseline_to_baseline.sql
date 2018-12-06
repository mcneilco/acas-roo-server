/****** Object:  Table [solution_unit]    Script Date: 6/3/2016 1:45:47 PM ******/
CREATE TABLE [solution_unit](
	[id] [numeric](19, 0) IDENTITY(1,1) NOT NULL,
	[code] [varchar](255) NULL,
	[name] [varchar](255) NULL,
	[version] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

/****** Object:  Table [vendor]    Script Date: 6/3/2016 1:45:47 PM ******/
CREATE TABLE [vendor](
	[id] [numeric](19, 0) IDENTITY(1,1) NOT NULL,
	[code] [varchar](255) NULL,
	[name] [varchar](255) NULL,
	[version] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

--lot.barcode
ALTER TABLE [lot] ADD [barcode] [varchar](255) NULL;
CREATE NONCLUSTERED INDEX [Lot_Barcode_IDX] ON [lot]
(
	[barcode] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)

--lot.solution_amount
ALTER TABLE [lot] ADD [solution_amount] [float] NULL;
--lot.solution_amount_units
ALTER TABLE [lot] ADD [solution_amount_units] [numeric](19, 0) NULL;
ALTER TABLE [lot]  WITH CHECK ADD  CONSTRAINT [FKLOTSOLNAMTUNITS] FOREIGN KEY(solution_amount_units)
REFERENCES [solution_unit] ([id]);
--lot.vendor
ALTER TABLE [lot] ADD [vendor] [numeric](19, 0) NULL;
ALTER TABLE [lot]  WITH CHECK ADD  CONSTRAINT [FKLOTVENDOR] FOREIGN KEY(vendor)
REFERENCES [vendor] ([id]);
--parent.mol_formula
ALTER TABLE [parent] ADD [mol_formula] [varchar](4000) NULL;

CREATE SEQUENCE [hibernate_sequence]
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
