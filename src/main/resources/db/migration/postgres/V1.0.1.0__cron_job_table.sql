CREATE TABLE cron_job
(
  id bigint NOT NULL,
  active boolean NOT NULL,
  code_name character varying(255) NOT NULL,
  function_name character varying(255),
  ignored boolean NOT NULL,
  last_duration bigint,
  last_resultjson text,
  last_start_time timestamp without time zone,
  number_of_executions integer,
  run_user character varying(255) NOT NULL,
  schedule character varying(255) NOT NULL,
  script_file character varying(255) NOT NULL,
  scriptjsondata text,
  script_type character varying(255) NOT NULL,
  version integer,
  CONSTRAINT cron_job_pkey PRIMARY KEY (id),
  CONSTRAINT cron_job_code_name_key UNIQUE (code_name)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE cron_job
  OWNER TO acas;
GRANT ALL ON TABLE cron_job TO acas;