-- Table: cmpd_reg_app_setting

-- DROP TABLE cmpd_reg_app_setting;

CREATE TABLE [cmpd_reg_app_setting]
(
  [id] [numeric](19, 0) IDENTITY(1,1) NOT NULL,
  [comments] [varchar](512) NULL,
  [ignored] [tinyint] NULL,
  [prop_name] [varchar](255) NULL,
  [prop_value] [varchar](255) NULL,
  [recorded_date] [datetime] NULL,
  [version] [int] NULL,
  PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

--
--DO
--$do$
--BEGIN
--	IF EXISTS ( SELECT 1 FROM information_schema.tables WHERE table_schema = 'compound' AND table_name = 'application_settings') THEN
--		INSERT INTO cmpd_reg_app_setting (SELECT * FROM application_settings);
--		DROP TABLE application_settings CASCADE;
--	END IF;
--END
--$do$
