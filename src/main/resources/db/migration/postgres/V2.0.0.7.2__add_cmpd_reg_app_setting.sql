-- Table: cmpd_reg_app_setting

-- DROP TABLE cmpd_reg_app_setting;

CREATE TABLE cmpd_reg_app_setting
(
  id bigint NOT NULL,
  comments character varying(512),
  ignored boolean NOT NULL,
  prop_name character varying(255),
  prop_value character varying(255),
  recorded_date timestamp without time zone,
  version integer,
  CONSTRAINT cmpd_reg_app_setting_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);


DO
$do$
BEGIN
	IF EXISTS ( SELECT 1 FROM information_schema.tables WHERE table_schema = 'compound' AND table_name = 'application_settings') THEN
		INSERT INTO cmpd_reg_app_setting (SELECT * FROM application_settings);
		DROP TABLE application_settings CASCADE;
	END IF;
END
$do$