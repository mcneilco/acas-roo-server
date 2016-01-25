--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: analysis_group; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE analysis_group (
    id bigint NOT NULL,
    code_name character varying(255),
    deleted boolean NOT NULL,
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(255) NOT NULL,
    ls_type_and_kind character varying(255),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    version integer
);


ALTER TABLE analysis_group OWNER TO acas;

--
-- Name: analysis_group_label; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE analysis_group_label (
    id bigint NOT NULL,
    deleted boolean NOT NULL,
    ignored boolean NOT NULL,
    image_file character varying(255),
    label_text character varying(255) NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(64) NOT NULL,
    ls_type_and_kind character varying(255),
    modified_date timestamp without time zone,
    physically_labled boolean NOT NULL,
    preferred boolean NOT NULL,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    version integer,
    analysis_group_id bigint NOT NULL
);


ALTER TABLE analysis_group_label OWNER TO acas;

--
-- Name: analysis_group_state; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE analysis_group_state (
    id bigint NOT NULL,
    comments character varying(512),
    deleted boolean NOT NULL,
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(64) NOT NULL,
    ls_type_and_kind character varying(255),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    version integer,
    analysis_group_id bigint NOT NULL
);


ALTER TABLE analysis_group_state OWNER TO acas;

--
-- Name: analysis_group_value; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE analysis_group_value (
    id bigint NOT NULL,
    blob_value oid,
    clob_value text,
    code_kind character varying(255),
    code_origin character varying(255),
    code_type character varying(255),
    code_type_and_kind character varying(350),
    code_value character varying(255),
    comments character varying(512),
    conc_unit character varying(25),
    concentration double precision,
    date_value timestamp without time zone,
    deleted boolean NOT NULL,
    file_value character varying(512),
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(64) NOT NULL,
    ls_type_and_kind character varying(350),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    number_of_replicates integer,
    numeric_value numeric(38,18),
    operator_kind character varying(10),
    operator_type character varying(25),
    operator_type_and_kind character varying(50),
    public_data boolean NOT NULL,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    sig_figs integer,
    string_value character varying(255),
    uncertainty numeric(38,18),
    uncertainty_type character varying(255),
    unit_kind character varying(25),
    unit_type character varying(25),
    unit_type_and_kind character varying(55),
    url_value character varying(2000),
    version integer,
    analysis_state_id bigint NOT NULL
);


ALTER TABLE analysis_group_value OWNER TO acas;

--
-- Name: analysisgroup_treatmentgroup; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE analysisgroup_treatmentgroup (
    treatment_group_id bigint NOT NULL,
    analysis_group_id bigint NOT NULL
);


ALTER TABLE analysisgroup_treatmentgroup OWNER TO acas;

--
-- Name: application_setting; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE application_setting (
    id bigint NOT NULL,
    comments character varying(512),
    ignored boolean NOT NULL,
    prop_name character varying(255),
    prop_value character varying(255),
    recorded_date timestamp without time zone,
    version integer
);


ALTER TABLE application_setting OWNER TO acas;


--
-- Name: author; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE author (
    id bigint NOT NULL,
    activation_date timestamp without time zone,
    activation_key character varying(255),
    email_address character varying(255) NOT NULL,
    enabled boolean,
    first_name character varying(255) NOT NULL,
    last_name character varying(255) NOT NULL,
    locked boolean,
    modified_date timestamp without time zone,
    password character varying(255) NOT NULL,
    recorded_date timestamp without time zone,
    user_name character varying(255) NOT NULL,
    version integer
);


ALTER TABLE author OWNER TO acas;

--
-- Name: author_role; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE author_role (
    id bigint NOT NULL,
    version integer,
    lsrole_id bigint NOT NULL,
    author_id bigint NOT NULL
);


ALTER TABLE author_role OWNER TO acas;

--
-- Name: code_kind; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE code_kind (
    id bigint NOT NULL,
    kind_name character varying(255) NOT NULL,
    ls_type_and_kind character varying(255),
    version integer,
    ls_type bigint NOT NULL
);


ALTER TABLE code_kind OWNER TO acas;

--
-- Name: code_origin; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE code_origin (
    id bigint NOT NULL,
    name character varying(256) NOT NULL,
    version integer
);


ALTER TABLE code_origin OWNER TO acas;

--
-- Name: code_type; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE code_type (
    id bigint NOT NULL,
    type_name character varying(128) NOT NULL,
    version integer
);


ALTER TABLE code_type OWNER TO acas;
--
-- Name: container; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE container (
    id bigint NOT NULL,
    code_name character varying(255),
    deleted boolean NOT NULL,
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(255) NOT NULL,
    ls_type_and_kind character varying(255),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    version integer,
    location_id bigint
);


ALTER TABLE container OWNER TO acas;

--
-- Name: container_kind; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE container_kind (
    id bigint NOT NULL,
    kind_name character varying(255) NOT NULL,
    ls_type_and_kind character varying(255),
    version integer,
    ls_type bigint NOT NULL
);


ALTER TABLE container_kind OWNER TO acas;

--
-- Name: container_label; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE container_label (
    id bigint NOT NULL,
    deleted boolean NOT NULL,
    ignored boolean NOT NULL,
    image_file character varying(255),
    label_text character varying(255) NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(64) NOT NULL,
    ls_type_and_kind character varying(255),
    modified_date timestamp without time zone,
    physically_labled boolean NOT NULL,
    preferred boolean NOT NULL,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    version integer,
    container_id bigint NOT NULL
);


ALTER TABLE container_label OWNER TO acas;

--
-- Name: container_state; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE container_state (
    id bigint NOT NULL,
    comments character varying(512),
    deleted boolean NOT NULL,
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(64) NOT NULL,
    ls_type_and_kind character varying(255),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    version integer,
    container_id bigint NOT NULL
);


ALTER TABLE container_state OWNER TO acas;

--
-- Name: container_type; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE container_type (
    id bigint NOT NULL,
    type_name character varying(128) NOT NULL,
    version integer
);


ALTER TABLE container_type OWNER TO acas;

--
-- Name: container_value; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE container_value (
    id bigint NOT NULL,
    blob_value oid,
    clob_value text,
    code_kind character varying(255),
    code_origin character varying(255),
    code_type character varying(255),
    code_type_and_kind character varying(350),
    code_value character varying(255),
    comments character varying(512),
    conc_unit character varying(25),
    concentration double precision,
    date_value timestamp without time zone,
    deleted boolean NOT NULL,
    file_value character varying(512),
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(64) NOT NULL,
    ls_type_and_kind character varying(350),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    number_of_replicates integer,
    numeric_value numeric(38,18),
    operator_kind character varying(10),
    operator_type character varying(25),
    operator_type_and_kind character varying(50),
    public_data boolean NOT NULL,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    sig_figs integer,
    string_value character varying(255),
    uncertainty numeric(38,18),
    uncertainty_type character varying(255),
    unit_kind character varying(25),
    unit_type character varying(25),
    unit_type_and_kind character varying(55),
    url_value character varying(2000),
    version integer,
    container_state_id bigint NOT NULL
);


ALTER TABLE container_value OWNER TO acas;

--
-- Name: ddict_kind; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE ddict_kind (
    id bigint NOT NULL,
    comments character varying(512),
    description character varying(512),
    display_order integer,
    ignored boolean NOT NULL,
    ls_type character varying(255),
    ls_type_and_kind character varying(255),
    name character varying(255) NOT NULL,
    version integer
);


ALTER TABLE ddict_kind OWNER TO acas;

--
-- Name: ddict_type; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE ddict_type (
    id bigint NOT NULL,
    comments character varying(512),
    description character varying(512),
    display_order integer,
    ignored boolean NOT NULL,
    name character varying(255) NOT NULL,
    version integer
);


ALTER TABLE ddict_type OWNER TO acas;

--
-- Name: ddict_value; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE ddict_value (
    id bigint NOT NULL,
    code_name character varying(255) NOT NULL,
    comments character varying(512),
    description character varying(512),
    display_order integer,
    ignored boolean NOT NULL,
    label_text character varying(512) NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_type character varying(255) NOT NULL,
    ls_type_and_kind character varying(255),
    short_name character varying(256),
    version integer
);


ALTER TABLE ddict_value OWNER TO acas;

--
-- Name: experiment; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE experiment (
    id bigint NOT NULL,
    code_name character varying(255),
    deleted boolean NOT NULL,
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(255) NOT NULL,
    ls_type_and_kind character varying(255),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    version integer,
    short_description character varying(1024),
    protocol_id bigint NOT NULL
);


ALTER TABLE experiment OWNER TO acas;

--
-- Name: experiment_analysisgroup; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE experiment_analysisgroup (
    analysis_group_id bigint NOT NULL,
    experiment_id bigint NOT NULL
);


ALTER TABLE experiment_analysisgroup OWNER TO acas;

--
-- Name: experiment_kind; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE experiment_kind (
    id bigint NOT NULL,
    kind_name character varying(255) NOT NULL,
    ls_type_and_kind character varying(255),
    version integer,
    ls_type bigint NOT NULL
);


ALTER TABLE experiment_kind OWNER TO acas;

--
-- Name: experiment_label; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE experiment_label (
    id bigint NOT NULL,
    deleted boolean NOT NULL,
    ignored boolean NOT NULL,
    image_file character varying(255),
    label_text character varying(255) NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(64) NOT NULL,
    ls_type_and_kind character varying(255),
    modified_date timestamp without time zone,
    physically_labled boolean NOT NULL,
    preferred boolean NOT NULL,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    version integer,
    experiment_id bigint NOT NULL
);


ALTER TABLE experiment_label OWNER TO acas;

--
-- Name: experiment_state; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE experiment_state (
    id bigint NOT NULL,
    comments character varying(512),
    deleted boolean NOT NULL,
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(64) NOT NULL,
    ls_type_and_kind character varying(255),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    version integer,
    experiment_id bigint NOT NULL
);


ALTER TABLE experiment_state OWNER TO acas;

--
-- Name: experiment_tag; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE experiment_tag (
    experiment_id bigint NOT NULL,
    tag_id bigint NOT NULL
);


ALTER TABLE experiment_tag OWNER TO acas;

--
-- Name: experiment_type; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE experiment_type (
    id bigint NOT NULL,
    type_name character varying(128) NOT NULL,
    version integer
);


ALTER TABLE experiment_type OWNER TO acas;

--
-- Name: experiment_value; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE experiment_value (
    id bigint NOT NULL,
    blob_value oid,
    clob_value text,
    code_kind character varying(255),
    code_origin character varying(255),
    code_type character varying(255),
    code_type_and_kind character varying(350),
    code_value character varying(255),
    comments character varying(512),
    conc_unit character varying(25),
    concentration double precision,
    date_value timestamp without time zone,
    deleted boolean NOT NULL,
    file_value character varying(512),
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(64) NOT NULL,
    ls_type_and_kind character varying(350),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    number_of_replicates integer,
    numeric_value numeric(38,18),
    operator_kind character varying(10),
    operator_type character varying(25),
    operator_type_and_kind character varying(50),
    public_data boolean NOT NULL,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    sig_figs integer,
    string_value character varying(255),
    uncertainty numeric(38,18),
    uncertainty_type character varying(255),
    unit_kind character varying(25),
    unit_type character varying(25),
    unit_type_and_kind character varying(55),
    url_value character varying(2000),
    version integer,
    experiment_state_id bigint NOT NULL
);


ALTER TABLE experiment_value OWNER TO acas;

--
-- Name: file_thing; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE file_thing (
    id bigint NOT NULL,
    code_name character varying(255),
    deleted boolean NOT NULL,
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(255) NOT NULL,
    ls_type_and_kind character varying(255),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    version integer,
    application_type character varying(512),
    description character varying(512),
    file_extension character varying(255),
    file_size bigint,
    fileurl character varying(1024),
    mime_type character varying(512),
    name character varying(512)
);


ALTER TABLE file_thing OWNER TO acas;

--
-- Name: interaction_kind; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE interaction_kind (
    id bigint NOT NULL,
    kind_name character varying(255) NOT NULL,
    ls_type_and_kind character varying(255),
    version integer,
    ls_type bigint NOT NULL
);


ALTER TABLE interaction_kind OWNER TO acas;

--
-- Name: interaction_type; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE interaction_type (
    id bigint NOT NULL,
    type_name character varying(128) NOT NULL,
    type_verb character varying(128) NOT NULL,
    version integer
);


ALTER TABLE interaction_type OWNER TO acas;

--
-- Name: itx_container_container; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE itx_container_container (
    id bigint NOT NULL,
    code_name character varying(255),
    deleted boolean NOT NULL,
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(255) NOT NULL,
    ls_type_and_kind character varying(255),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    version integer,
    first_container_id bigint NOT NULL,
    second_container_id bigint NOT NULL
);


ALTER TABLE itx_container_container OWNER TO acas;

--
-- Name: itx_container_container_state; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE itx_container_container_state (
    id bigint NOT NULL,
    comments character varying(512),
    deleted boolean NOT NULL,
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(64) NOT NULL,
    ls_type_and_kind character varying(255),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    version integer,
    itx_container_container bigint
);


ALTER TABLE itx_container_container_state OWNER TO acas;

--
-- Name: itx_container_container_value; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE itx_container_container_value (
    id bigint NOT NULL,
    blob_value oid,
    clob_value text,
    code_kind character varying(255),
    code_origin character varying(255),
    code_type character varying(255),
    code_type_and_kind character varying(350),
    code_value character varying(255),
    comments character varying(512),
    conc_unit character varying(25),
    concentration double precision,
    date_value timestamp without time zone,
    deleted boolean NOT NULL,
    file_value character varying(512),
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(64) NOT NULL,
    ls_type_and_kind character varying(350),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    number_of_replicates integer,
    numeric_value numeric(38,18),
    operator_kind character varying(10),
    operator_type character varying(25),
    operator_type_and_kind character varying(50),
    public_data boolean NOT NULL,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    sig_figs integer,
    string_value character varying(255),
    uncertainty numeric(38,18),
    uncertainty_type character varying(255),
    unit_kind character varying(25),
    unit_type character varying(25),
    unit_type_and_kind character varying(55),
    url_value character varying(2000),
    version integer,
    ls_state bigint
);


ALTER TABLE itx_container_container_value OWNER TO acas;

--
-- Name: itx_expt_expt; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE itx_expt_expt (
    id bigint NOT NULL,
    code_name character varying(255),
    deleted boolean NOT NULL,
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(255) NOT NULL,
    ls_type_and_kind character varying(255),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    version integer,
    first_experiment_id bigint NOT NULL,
    second_experiment_id bigint NOT NULL
);


ALTER TABLE itx_expt_expt OWNER TO acas;

--
-- Name: itx_expt_expt_state; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE itx_expt_expt_state (
    id bigint NOT NULL,
    comments character varying(512),
    deleted boolean NOT NULL,
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(64) NOT NULL,
    ls_type_and_kind character varying(255),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    version integer,
    itx_experiment_experiment bigint
);


ALTER TABLE itx_expt_expt_state OWNER TO acas;

--
-- Name: itx_expt_expt_value; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE itx_expt_expt_value (
    id bigint NOT NULL,
    blob_value oid,
    clob_value text,
    code_kind character varying(255),
    code_origin character varying(255),
    code_type character varying(255),
    code_type_and_kind character varying(350),
    code_value character varying(255),
    comments character varying(512),
    conc_unit character varying(25),
    concentration double precision,
    date_value timestamp without time zone,
    deleted boolean NOT NULL,
    file_value character varying(512),
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(64) NOT NULL,
    ls_type_and_kind character varying(350),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    number_of_replicates integer,
    numeric_value numeric(38,18),
    operator_kind character varying(10),
    operator_type character varying(25),
    operator_type_and_kind character varying(50),
    public_data boolean NOT NULL,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    sig_figs integer,
    string_value character varying(255),
    uncertainty numeric(38,18),
    uncertainty_type character varying(255),
    unit_kind character varying(25),
    unit_type character varying(25),
    unit_type_and_kind character varying(55),
    url_value character varying(2000),
    version integer,
    ls_state bigint
);


ALTER TABLE itx_expt_expt_value OWNER TO acas;

--
-- Name: itx_ls_thing_ls_thing; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE itx_ls_thing_ls_thing (
    id bigint NOT NULL,
    code_name character varying(255),
    deleted boolean NOT NULL,
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(255) NOT NULL,
    ls_type_and_kind character varying(255),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    version integer,
    first_ls_thing_id bigint NOT NULL,
    second_ls_thing_id bigint NOT NULL
);


ALTER TABLE itx_ls_thing_ls_thing OWNER TO acas;

--
-- Name: itx_ls_thing_ls_thing_state; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE itx_ls_thing_ls_thing_state (
    id bigint NOT NULL,
    comments character varying(512),
    deleted boolean NOT NULL,
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(64) NOT NULL,
    ls_type_and_kind character varying(255),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    version integer,
    itx_ls_thing_ls_thing bigint
);


ALTER TABLE itx_ls_thing_ls_thing_state OWNER TO acas;

--
-- Name: itx_ls_thing_ls_thing_value; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE itx_ls_thing_ls_thing_value (
    id bigint NOT NULL,
    blob_value oid,
    clob_value text,
    code_kind character varying(255),
    code_origin character varying(255),
    code_type character varying(255),
    code_type_and_kind character varying(350),
    code_value character varying(255),
    comments character varying(512),
    conc_unit character varying(25),
    concentration double precision,
    date_value timestamp without time zone,
    deleted boolean NOT NULL,
    file_value character varying(512),
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(64) NOT NULL,
    ls_type_and_kind character varying(350),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    number_of_replicates integer,
    numeric_value numeric(38,18),
    operator_kind character varying(10),
    operator_type character varying(25),
    operator_type_and_kind character varying(50),
    public_data boolean NOT NULL,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    sig_figs integer,
    string_value character varying(255),
    uncertainty numeric(38,18),
    uncertainty_type character varying(255),
    unit_kind character varying(25),
    unit_type character varying(25),
    unit_type_and_kind character varying(55),
    url_value character varying(2000),
    version integer,
    ls_state bigint
);


ALTER TABLE itx_ls_thing_ls_thing_value OWNER TO acas;

--
-- Name: itx_protocol_protocol; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE itx_protocol_protocol (
    id bigint NOT NULL,
    code_name character varying(255),
    deleted boolean NOT NULL,
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(255) NOT NULL,
    ls_type_and_kind character varying(255),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    version integer,
    first_protocol_id bigint NOT NULL,
    second_protocol_id bigint NOT NULL
);


ALTER TABLE itx_protocol_protocol OWNER TO acas;

--
-- Name: itx_protocol_protocol_state; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE itx_protocol_protocol_state (
    id bigint NOT NULL,
    comments character varying(512),
    deleted boolean NOT NULL,
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(64) NOT NULL,
    ls_type_and_kind character varying(255),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    version integer,
    itx_protocol_protocol bigint
);


ALTER TABLE itx_protocol_protocol_state OWNER TO acas;

--
-- Name: itx_protocol_protocol_value; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE itx_protocol_protocol_value (
    id bigint NOT NULL,
    blob_value oid,
    clob_value text,
    code_kind character varying(255),
    code_origin character varying(255),
    code_type character varying(255),
    code_type_and_kind character varying(350),
    code_value character varying(255),
    comments character varying(512),
    conc_unit character varying(25),
    concentration double precision,
    date_value timestamp without time zone,
    deleted boolean NOT NULL,
    file_value character varying(512),
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(64) NOT NULL,
    ls_type_and_kind character varying(350),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    number_of_replicates integer,
    numeric_value numeric(38,18),
    operator_kind character varying(10),
    operator_type character varying(25),
    operator_type_and_kind character varying(50),
    public_data boolean NOT NULL,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    sig_figs integer,
    string_value character varying(255),
    uncertainty numeric(38,18),
    uncertainty_type character varying(255),
    unit_kind character varying(25),
    unit_type character varying(25),
    unit_type_and_kind character varying(55),
    url_value character varying(2000),
    version integer,
    ls_state bigint
);


ALTER TABLE itx_protocol_protocol_value OWNER TO acas;

--
-- Name: itx_subject_container; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE itx_subject_container (
    id bigint NOT NULL,
    code_name character varying(255),
    deleted boolean NOT NULL,
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(255) NOT NULL,
    ls_type_and_kind character varying(255),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    version integer,
    container_id bigint NOT NULL,
    subject_id bigint NOT NULL
);


ALTER TABLE itx_subject_container OWNER TO acas;

--
-- Name: itx_subject_container_state; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE itx_subject_container_state (
    id bigint NOT NULL,
    comments character varying(512),
    deleted boolean NOT NULL,
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(64) NOT NULL,
    ls_type_and_kind character varying(255),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    version integer,
    itx_subject_container bigint
);


ALTER TABLE itx_subject_container_state OWNER TO acas;

--
-- Name: itx_subject_container_value; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE itx_subject_container_value (
    id bigint NOT NULL,
    blob_value oid,
    clob_value text,
    code_kind character varying(255),
    code_origin character varying(255),
    code_type character varying(255),
    code_type_and_kind character varying(350),
    code_value character varying(255),
    comments character varying(512),
    conc_unit character varying(25),
    concentration double precision,
    date_value timestamp without time zone,
    deleted boolean NOT NULL,
    file_value character varying(512),
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(64) NOT NULL,
    ls_type_and_kind character varying(350),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    number_of_replicates integer,
    numeric_value numeric(38,18),
    operator_kind character varying(10),
    operator_type character varying(25),
    operator_type_and_kind character varying(50),
    public_data boolean NOT NULL,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    sig_figs integer,
    string_value character varying(255),
    uncertainty numeric(38,18),
    uncertainty_type character varying(255),
    unit_kind character varying(25),
    unit_type character varying(25),
    unit_type_and_kind character varying(55),
    url_value character varying(2000),
    version integer,
    ls_state bigint
);


ALTER TABLE itx_subject_container_value OWNER TO acas;

--
-- Name: label_kind; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE label_kind (
    id bigint NOT NULL,
    kind_name character varying(255) NOT NULL,
    ls_type_and_kind character varying(255),
    version integer,
    ls_type bigint
);


ALTER TABLE label_kind OWNER TO acas;

--
-- Name: label_sequence; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE label_sequence (
    id bigint NOT NULL,
    digits integer,
    group_digits boolean NOT NULL,
    ignored boolean NOT NULL,
    label_prefix character varying(50) NOT NULL,
    label_separator character varying(10),
    label_type_and_kind character varying(255) NOT NULL,
    latest_number bigint NOT NULL,
    modified_date timestamp without time zone,
    thing_type_and_kind character varying(255) NOT NULL,
    version integer
);


ALTER TABLE label_sequence OWNER TO acas;

--
-- Name: label_type; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE label_type (
    id bigint NOT NULL,
    type_name character varying(255) NOT NULL,
    version integer
);


ALTER TABLE label_type OWNER TO acas;

--
-- Name: ls_interaction; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE ls_interaction (
    id bigint NOT NULL,
    code_name character varying(255),
    deleted boolean NOT NULL,
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(255) NOT NULL,
    ls_type_and_kind character varying(255),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    version integer,
    first_thing_id bigint NOT NULL,
    second_thing_id bigint NOT NULL
);


ALTER TABLE ls_interaction OWNER TO acas;

--
-- Name: ls_role; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE ls_role (
    id bigint NOT NULL,
    role_description character varying(200),
    role_name character varying(255) NOT NULL,
    version integer
);


ALTER TABLE ls_role OWNER TO acas;

--
-- Name: ls_seq_anl_grp; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE ls_seq_anl_grp (
    id bigint NOT NULL,
    version integer
);


ALTER TABLE ls_seq_anl_grp OWNER TO acas;

--
-- Name: ls_seq_container; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE ls_seq_container (
    id bigint NOT NULL,
    version integer
);


ALTER TABLE ls_seq_container OWNER TO acas;

--
-- Name: ls_seq_expt; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE ls_seq_expt (
    id bigint NOT NULL,
    version integer
);


ALTER TABLE ls_seq_expt OWNER TO acas;

--
-- Name: ls_seq_itx_cntr_cntr; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE ls_seq_itx_cntr_cntr (
    id bigint NOT NULL,
    version integer
);


ALTER TABLE ls_seq_itx_cntr_cntr OWNER TO acas;

--
-- Name: ls_seq_itx_experiment_experiment; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE ls_seq_itx_experiment_experiment (
    id bigint NOT NULL,
    version integer
);


ALTER TABLE ls_seq_itx_experiment_experiment OWNER TO acas;

--
-- Name: ls_seq_itx_protocol_protocol; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE ls_seq_itx_protocol_protocol (
    id bigint NOT NULL,
    version integer
);


ALTER TABLE ls_seq_itx_protocol_protocol OWNER TO acas;

--
-- Name: ls_seq_itx_subj_cntr; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE ls_seq_itx_subj_cntr (
    id bigint NOT NULL,
    version integer
);


ALTER TABLE ls_seq_itx_subj_cntr OWNER TO acas;

--
-- Name: ls_seq_protocol; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE ls_seq_protocol (
    id bigint NOT NULL,
    version integer
);


ALTER TABLE ls_seq_protocol OWNER TO acas;

--
-- Name: ls_seq_subject; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE ls_seq_subject (
    id bigint NOT NULL,
    version integer
);


ALTER TABLE ls_seq_subject OWNER TO acas;

--
-- Name: ls_seq_trt_grp; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE ls_seq_trt_grp (
    id bigint NOT NULL,
    version integer
);


ALTER TABLE ls_seq_trt_grp OWNER TO acas;

--
-- Name: ls_tag; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE ls_tag (
    id bigint NOT NULL,
    recorded_date timestamp without time zone,
    tag_text character varying(255),
    version integer
);


ALTER TABLE ls_tag OWNER TO acas;

--
-- Name: ls_thing; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE ls_thing (
    id bigint NOT NULL,
    code_name character varying(255),
    deleted boolean NOT NULL,
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(255) NOT NULL,
    ls_type_and_kind character varying(255),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    version integer
);


ALTER TABLE ls_thing OWNER TO acas;

--
-- Name: ls_thing_label; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE ls_thing_label (
    id bigint NOT NULL,
    deleted boolean NOT NULL,
    ignored boolean NOT NULL,
    image_file character varying(255),
    label_text character varying(255) NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(64) NOT NULL,
    ls_type_and_kind character varying(255),
    modified_date timestamp without time zone,
    physically_labled boolean NOT NULL,
    preferred boolean NOT NULL,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    version integer,
    ls_thing_type_and_kind character varying(255),
    lsthing_id bigint NOT NULL
);


ALTER TABLE ls_thing_label OWNER TO acas;

--
-- Name: ls_thing_state; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE ls_thing_state (
    id bigint NOT NULL,
    comments character varying(512),
    deleted boolean NOT NULL,
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(64) NOT NULL,
    ls_type_and_kind character varying(255),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    version integer,
    lsthing_id bigint NOT NULL
);


ALTER TABLE ls_thing_state OWNER TO acas;

--
-- Name: ls_thing_value; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE ls_thing_value (
    id bigint NOT NULL,
    blob_value oid,
    clob_value text,
    code_kind character varying(255),
    code_origin character varying(255),
    code_type character varying(255),
    code_type_and_kind character varying(350),
    code_value character varying(255),
    comments character varying(512),
    conc_unit character varying(25),
    concentration double precision,
    date_value timestamp without time zone,
    deleted boolean NOT NULL,
    file_value character varying(512),
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(64) NOT NULL,
    ls_type_and_kind character varying(350),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    number_of_replicates integer,
    numeric_value numeric(38,18),
    operator_kind character varying(10),
    operator_type character varying(25),
    operator_type_and_kind character varying(50),
    public_data boolean NOT NULL,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    sig_figs integer,
    string_value character varying(255),
    uncertainty numeric(38,18),
    uncertainty_type character varying(255),
    unit_kind character varying(25),
    unit_type character varying(25),
    unit_type_and_kind character varying(55),
    url_value character varying(2000),
    version integer,
    lsthing_state_id bigint NOT NULL
);


ALTER TABLE ls_thing_value OWNER TO acas;

--
-- Name: ls_transaction; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE ls_transaction (
    id bigint NOT NULL,
    comments character varying(255),
    recorded_date timestamp without time zone NOT NULL,
    version integer
);


ALTER TABLE ls_transaction OWNER TO acas;

--
-- Name: lsthing_tag; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE lsthing_tag (
    lsthing_id bigint NOT NULL,
    tag_id bigint NOT NULL
);


ALTER TABLE lsthing_tag OWNER TO acas;

--
-- Name: operator_kind; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE operator_kind (
    id bigint NOT NULL,
    kind_name character varying(64) NOT NULL,
    ls_type_and_kind character varying(255),
    version integer,
    ls_type bigint NOT NULL
);


ALTER TABLE operator_kind OWNER TO acas;

--
-- Name: operator_type; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE operator_type (
    id bigint NOT NULL,
    type_name character varying(25) NOT NULL,
    version integer
);


ALTER TABLE operator_type OWNER TO acas;

--
-- Name: protocol; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE protocol (
    id bigint NOT NULL,
    code_name character varying(255),
    deleted boolean NOT NULL,
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(255) NOT NULL,
    ls_type_and_kind character varying(255),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    version integer,
    short_description character varying(1000)
);


ALTER TABLE protocol OWNER TO acas;

--
-- Name: protocol_kind; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE protocol_kind (
    id bigint NOT NULL,
    kind_name character varying(255) NOT NULL,
    ls_type_and_kind character varying(255),
    version integer,
    ls_type bigint NOT NULL
);


ALTER TABLE protocol_kind OWNER TO acas;

--
-- Name: protocol_label; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE protocol_label (
    id bigint NOT NULL,
    deleted boolean NOT NULL,
    ignored boolean NOT NULL,
    image_file character varying(255),
    label_text character varying(255) NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(64) NOT NULL,
    ls_type_and_kind character varying(255),
    modified_date timestamp without time zone,
    physically_labled boolean NOT NULL,
    preferred boolean NOT NULL,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    version integer,
    protocol_id bigint NOT NULL
);


ALTER TABLE protocol_label OWNER TO acas;

--
-- Name: protocol_state; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE protocol_state (
    id bigint NOT NULL,
    comments character varying(512),
    deleted boolean NOT NULL,
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(64) NOT NULL,
    ls_type_and_kind character varying(255),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    version integer,
    protocol_id bigint NOT NULL
);


ALTER TABLE protocol_state OWNER TO acas;

--
-- Name: protocol_tag; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE protocol_tag (
    protocol_id bigint NOT NULL,
    tag_id bigint NOT NULL
);


ALTER TABLE protocol_tag OWNER TO acas;

--
-- Name: protocol_type; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE protocol_type (
    id bigint NOT NULL,
    type_name character varying(128) NOT NULL,
    version integer
);


ALTER TABLE protocol_type OWNER TO acas;

--
-- Name: protocol_value; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE protocol_value (
    id bigint NOT NULL,
    blob_value oid,
    clob_value text,
    code_kind character varying(255),
    code_origin character varying(255),
    code_type character varying(255),
    code_type_and_kind character varying(350),
    code_value character varying(255),
    comments character varying(512),
    conc_unit character varying(25),
    concentration double precision,
    date_value timestamp without time zone,
    deleted boolean NOT NULL,
    file_value character varying(512),
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(64) NOT NULL,
    ls_type_and_kind character varying(350),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    number_of_replicates integer,
    numeric_value numeric(38,18),
    operator_kind character varying(10),
    operator_type character varying(25),
    operator_type_and_kind character varying(50),
    public_data boolean NOT NULL,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    sig_figs integer,
    string_value character varying(255),
    uncertainty numeric(38,18),
    uncertainty_type character varying(255),
    unit_kind character varying(25),
    unit_type character varying(25),
    unit_type_and_kind character varying(55),
    url_value character varying(2000),
    version integer,
    protocol_state_id bigint NOT NULL
);


ALTER TABLE protocol_value OWNER TO acas;

--
-- Name: state_kind; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE state_kind (
    id bigint NOT NULL,
    kind_name character varying(64) NOT NULL,
    ls_type_and_kind character varying(255),
    version integer,
    ls_type bigint NOT NULL
);


ALTER TABLE state_kind OWNER TO acas;

--
-- Name: state_type; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE state_type (
    id bigint NOT NULL,
    type_name character varying(64) NOT NULL,
    version integer
);


ALTER TABLE state_type OWNER TO acas;

--
-- Name: subject; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE subject (
    id bigint NOT NULL,
    code_name character varying(255),
    deleted boolean NOT NULL,
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(255) NOT NULL,
    ls_type_and_kind character varying(255),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    version integer
);


ALTER TABLE subject OWNER TO acas;

--
-- Name: subject_label; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE subject_label (
    id bigint NOT NULL,
    deleted boolean NOT NULL,
    ignored boolean NOT NULL,
    image_file character varying(255),
    label_text character varying(255) NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(64) NOT NULL,
    ls_type_and_kind character varying(255),
    modified_date timestamp without time zone,
    physically_labled boolean NOT NULL,
    preferred boolean NOT NULL,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    version integer,
    subject_id bigint NOT NULL
);


ALTER TABLE subject_label OWNER TO acas;

--
-- Name: subject_state; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE subject_state (
    id bigint NOT NULL,
    comments character varying(512),
    deleted boolean NOT NULL,
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(64) NOT NULL,
    ls_type_and_kind character varying(255),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    version integer,
    subject_id bigint NOT NULL
);


ALTER TABLE subject_state OWNER TO acas;

--
-- Name: subject_value; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE subject_value (
    id bigint NOT NULL,
    blob_value oid,
    clob_value text,
    code_kind character varying(255),
    code_origin character varying(255),
    code_type character varying(255),
    code_type_and_kind character varying(350),
    code_value character varying(255),
    comments character varying(512),
    conc_unit character varying(25),
    concentration double precision,
    date_value timestamp without time zone,
    deleted boolean NOT NULL,
    file_value character varying(512),
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(64) NOT NULL,
    ls_type_and_kind character varying(350),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    number_of_replicates integer,
    numeric_value numeric(38,18),
    operator_kind character varying(10),
    operator_type character varying(25),
    operator_type_and_kind character varying(50),
    public_data boolean NOT NULL,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    sig_figs integer,
    string_value character varying(255),
    uncertainty numeric(38,18),
    uncertainty_type character varying(255),
    unit_kind character varying(25),
    unit_type character varying(25),
    unit_type_and_kind character varying(55),
    url_value character varying(2000),
    version integer,
    subject_state_id bigint NOT NULL
);


ALTER TABLE subject_value OWNER TO acas;

--
-- Name: thing_kind; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE thing_kind (
    id bigint NOT NULL,
    kind_name character varying(255) NOT NULL,
    ls_type_and_kind character varying(255),
    version integer,
    ls_type bigint NOT NULL
);


ALTER TABLE thing_kind OWNER TO acas;

--
-- Name: thing_page; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE thing_page (
    id bigint NOT NULL,
    archived boolean NOT NULL,
    current_editor character varying(255) NOT NULL,
    ignored boolean NOT NULL,
    modified_by character varying(255) NOT NULL,
    modified_date timestamp without time zone NOT NULL,
    page_content text,
    page_name character varying(255),
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    version integer,
    ls_transaction bigint,
    thing_id bigint NOT NULL
);


ALTER TABLE thing_page OWNER TO acas;

--
-- Name: thing_page_archive; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE thing_page_archive (
    id bigint NOT NULL,
    archived boolean NOT NULL,
    current_editor character varying(255),
    ignored boolean NOT NULL,
    ls_transaction bigint,
    modified_by character varying(255) NOT NULL,
    modified_date timestamp without time zone NOT NULL,
    page_content text,
    page_name character varying(255),
    page_version integer,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    thing_id bigint,
    version integer
);


ALTER TABLE thing_page_archive OWNER TO acas;

--
-- Name: thing_type; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE thing_type (
    id bigint NOT NULL,
    type_name character varying(128) NOT NULL,
    version integer
);


ALTER TABLE thing_type OWNER TO acas;

--
-- Name: treatment_group; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE treatment_group (
    id bigint NOT NULL,
    code_name character varying(255),
    deleted boolean NOT NULL,
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(255) NOT NULL,
    ls_type_and_kind character varying(255),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    version integer
);


ALTER TABLE treatment_group OWNER TO acas;

--
-- Name: treatment_group_label; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE treatment_group_label (
    id bigint NOT NULL,
    deleted boolean NOT NULL,
    ignored boolean NOT NULL,
    image_file character varying(255),
    label_text character varying(255) NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(64) NOT NULL,
    ls_type_and_kind character varying(255),
    modified_date timestamp without time zone,
    physically_labled boolean NOT NULL,
    preferred boolean NOT NULL,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    version integer,
    treatment_group_id bigint NOT NULL
);


ALTER TABLE treatment_group_label OWNER TO acas;

--
-- Name: treatment_group_state; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE treatment_group_state (
    id bigint NOT NULL,
    comments character varying(512),
    deleted boolean NOT NULL,
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(64) NOT NULL,
    ls_type_and_kind character varying(255),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    version integer,
    treatment_group_id bigint NOT NULL
);


ALTER TABLE treatment_group_state OWNER TO acas;

--
-- Name: treatment_group_value; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE treatment_group_value (
    id bigint NOT NULL,
    blob_value oid,
    clob_value text,
    code_kind character varying(255),
    code_origin character varying(255),
    code_type character varying(255),
    code_type_and_kind character varying(350),
    code_value character varying(255),
    comments character varying(512),
    conc_unit character varying(25),
    concentration double precision,
    date_value timestamp without time zone,
    deleted boolean NOT NULL,
    file_value character varying(512),
    ignored boolean NOT NULL,
    ls_kind character varying(255) NOT NULL,
    ls_transaction bigint,
    ls_type character varying(64) NOT NULL,
    ls_type_and_kind character varying(350),
    modified_by character varying(255),
    modified_date timestamp without time zone,
    number_of_replicates integer,
    numeric_value numeric(38,18),
    operator_kind character varying(10),
    operator_type character varying(25),
    operator_type_and_kind character varying(50),
    public_data boolean NOT NULL,
    recorded_by character varying(255) NOT NULL,
    recorded_date timestamp without time zone NOT NULL,
    sig_figs integer,
    string_value character varying(255),
    uncertainty numeric(38,18),
    uncertainty_type character varying(255),
    unit_kind character varying(25),
    unit_type character varying(25),
    unit_type_and_kind character varying(55),
    url_value character varying(2000),
    version integer,
    treatment_state_id bigint NOT NULL
);


ALTER TABLE treatment_group_value OWNER TO acas;

--
-- Name: treatmentgroup_subject; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE treatmentgroup_subject (
    subject_id bigint NOT NULL,
    treatment_group_id bigint NOT NULL
);


ALTER TABLE treatmentgroup_subject OWNER TO acas;

--
-- Name: uncertainty_kind; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE uncertainty_kind (
    id bigint NOT NULL,
    kind_name character varying(255) NOT NULL,
    version integer
);


ALTER TABLE uncertainty_kind OWNER TO acas;

--
-- Name: unit_kind; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE unit_kind (
    id bigint NOT NULL,
    kind_name character varying(64) NOT NULL,
    ls_type_and_kind character varying(255),
    version integer,
    ls_type bigint NOT NULL
);


ALTER TABLE unit_kind OWNER TO acas;

--
-- Name: unit_type; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE unit_type (
    id bigint NOT NULL,
    type_name character varying(64) NOT NULL,
    version integer
);


ALTER TABLE unit_type OWNER TO acas;

--
-- Name: update_log; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE update_log (
    id bigint NOT NULL,
    comments character varying(512),
    ls_transaction bigint,
    recorded_by character varying(255),
    recorded_date timestamp without time zone NOT NULL,
    thing bigint,
    update_action character varying(255),
    version integer
);


ALTER TABLE update_log OWNER TO acas;

--
-- Name: value_kind; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE value_kind (
    id bigint NOT NULL,
    kind_name character varying(64) NOT NULL,
    ls_type_and_kind character varying(255),
    version integer,
    ls_type bigint NOT NULL
);


ALTER TABLE value_kind OWNER TO acas;

--
-- Name: value_type; Type: TABLE; Schema: acas; Owner: acas; Tablespace: 
--

CREATE TABLE value_type (
    id bigint NOT NULL,
    type_name character varying(64) NOT NULL,
    version integer
);


ALTER TABLE value_type OWNER TO acas;

--
-- Name: acas; Type: ACL; Schema: -; Owner: acas
--

REVOKE ALL ON SCHEMA acas FROM PUBLIC;
REVOKE ALL ON SCHEMA acas FROM acas;
GRANT ALL ON SCHEMA acas TO acas;
GRANT ALL ON SCHEMA acas TO postgres;
GRANT USAGE ON SCHEMA acas TO seurat;


--
-- PostgreSQL database dump complete
--

