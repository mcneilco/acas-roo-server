CREATE TABLE parent_alias
(
  [id] [numeric](19, 0) IDENTITY(1,1) NOT NULL,
  [version] [int] NULL,
  [parent] [numeric](19,0) NULL,
  [alias_name] [varchar](255) NULL,
  [ls_type] [varchar](255) NULL,
  [ls_kind] [varchar](255) NULL,
  [ignored] [tinyint] NULL,
  [deleted] [tinyint] NULL,
  [preferred] [tinyint] NULL,
  PRIMARY KEY CLUSTERED 
(
	[id] ASC
)
);  
  
CREATE TABLE salt_form_alias
(
  [id] [numeric](19, 0) IDENTITY(1,1) NOT NULL,
  [version] [int] NULL,
  [salt_form] [numeric](19,0) NULL,
  [alias_name] [varchar](255) NULL,
  [ls_type] [varchar](255) NULL,
  [ls_kind] [varchar](255) NULL,
  [ignored] [tinyint] NULL,
  [deleted] [tinyint] NULL,
  [preferred] [tinyint] NULL,
  PRIMARY KEY CLUSTERED 
(
	[id] ASC
)
);
  
CREATE TABLE lot_alias
(
  [id] [numeric](19, 0) IDENTITY(1,1) NOT NULL,
  [version] [int] NULL,
  [lot] [numeric](19,0) NULL,
  [alias_name] [varchar](255) NULL,
  [ls_type] [varchar](255) NULL,
  [ls_kind] [varchar](255) NULL,
  [ignored] [tinyint] NULL,
  [deleted] [tinyint] NULL,
  [preferred] [tinyint] NULL,
  PRIMARY KEY CLUSTERED 
(
	[id] ASC
)
);
  
CREATE TABLE parent_alias_type (
    [id] [numeric](19, 0) IDENTITY(1,1) NOT NULL,
    [type_name] [varchar](255) NULL,
    [version] [int] NULL,
  PRIMARY KEY CLUSTERED 
(
	[id] ASC
)
);

CREATE TABLE salt_form_alias_type (
    [id] [numeric](19, 0) IDENTITY(1,1) NOT NULL,
    [type_name] [varchar](255) NULL,
    [version] [int] NULL,
  PRIMARY KEY CLUSTERED 
(
	[id] ASC
)
);

CREATE TABLE lot_alias_type (
    [id] [numeric](19, 0) IDENTITY(1,1) NOT NULL,
    [type_name] [varchar](255) NULL,
    [version] [int] NULL,
  PRIMARY KEY CLUSTERED 
(
	[id] ASC
)
);


CREATE TABLE parent_alias_kind (
    [id] [numeric](19, 0) IDENTITY(1,1) NOT NULL,
	parent_alias_type bigint,
    [kind_name] [varchar](255) NULL,
    [version] [int] NULL,
  PRIMARY KEY CLUSTERED 
(
	[id] ASC
)
);

CREATE TABLE salt_form_alias_kind (
    [id] [numeric](19, 0) IDENTITY(1,1) NOT NULL,
	salt_form_alias_type bigint,
    [kind_name] [varchar](255) NULL,
    [version] [int] NULL,
  PRIMARY KEY CLUSTERED 
(
	[id] ASC
)
);

CREATE TABLE lot_alias_kind (
    [id] [numeric](19, 0) IDENTITY(1,1) NOT NULL,
	lot_alias_type bigint,
    [kind_name] [varchar](255) NULL,
    [version] [int] NULL,
  PRIMARY KEY CLUSTERED 
(
	[id] ASC
)
);
