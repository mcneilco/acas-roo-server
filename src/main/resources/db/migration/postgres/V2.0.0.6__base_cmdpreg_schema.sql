--
-- PostgreSQL database dump
--

-- Dumped from database version 9.2.8
-- Dumped by pg_dump version 9.3.1
-- Started on 2015-06-12 11:33:36 PDT

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 8 (class 2615 OID 878259)
-- Name: compound; Type: SCHEMA; Schema: -; Owner: 
--




SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 172 (class 1259 OID 878268)
-- Name: corp_name; Type: TABLE; Schema: ; Owner: ; Tablespace: 
--

CREATE TABLE corp_name (
    id bigint NOT NULL,
    comment character varying(50),
    ignore boolean,
    parent_corp_name character varying(50),
    version integer
);



--
-- TOC entry 173 (class 1259 OID 878273)
-- Name: file_list; Type: TABLE; Schema: ; Owner: ; Tablespace: 
--

CREATE TABLE file_list (
    id bigint NOT NULL,
    description character varying(255),
    file_name character varying(255),
    file_path character varying(255),
    ie boolean,
    name character varying(255),
    size bigint NOT NULL,
    subdir character varying(255),
    type character varying(255),
    uploaded boolean,
    url character varying(255),
    version integer,
    lot bigint
);



--
-- TOC entry 174 (class 1259 OID 878281)
-- Name: file_type; Type: TABLE; Schema: ; Owner: ; Tablespace: 
--

CREATE TABLE file_type (
    id bigint NOT NULL,
    code character varying(255),
    name character varying(255),
    version integer
);



--
-- TOC entry 192 (class 1259 OID 878546)
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: ; Owner: 
--

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- TOC entry 175 (class 1259 OID 878289)
-- Name: iso_salt; Type: TABLE; Schema: ; Owner: ; Tablespace: 
--

CREATE TABLE iso_salt (
    id bigint NOT NULL,
    equivalents double precision,
    ignore boolean,
    type character varying(25),
    version integer,
    isotope bigint,
    salt bigint,
    salt_form bigint
);



--
-- TOC entry 176 (class 1259 OID 878294)
-- Name: isotope; Type: TABLE; Schema: ; Owner: ; Tablespace: 
--

CREATE TABLE isotope (
    id bigint NOT NULL,
    abbrev character varying(100),
    ignore boolean,
    mass_change double precision,
    name character varying(255),
    version integer
);



--
-- TOC entry 177 (class 1259 OID 878299)
-- Name: lot; Type: TABLE; Schema: ; Owner: ; Tablespace: 
--

CREATE TABLE lot (
    id bigint NOT NULL,
    amount double precision,
    as_drawn_struct text,
    barcode character varying(255),
    boiling_point double precision,
    buid bigint NOT NULL,
    color character varying(255),
    comments character varying(2000),
    corp_name character varying(255) NOT NULL,
    ignore boolean,
    is_virtual boolean,
    lot_as_drawn_cd_id integer NOT NULL,
    lot_mol_weight double precision,
    lot_number integer NOT NULL,
    melting_point double precision,
    notebook_page character varying(255),
    percentee double precision,
    purity double precision,
    registration_date timestamp without time zone,
    retain double precision,
    solution_amount double precision,
    supplier character varying(255),
    supplierid character varying(255),
    supplier_lot character varying(255),
    synthesis_date timestamp without time zone,
    version integer,
    amount_units bigint,
    chemist character varying(255),
    physical_state bigint,
    project bigint,
    purity_measured_by bigint,
    purity_operator bigint,
    retain_units bigint,
    salt_form bigint,
    solution_amount_units bigint,
    vendor bigint
);



--
-- TOC entry 178 (class 1259 OID 878309)
-- Name: operator; Type: TABLE; Schema: ; Owner: ; Tablespace: 
--

CREATE TABLE operator (
    id bigint NOT NULL,
    code character varying(255),
    name character varying(255),
    version integer
);



--
-- TOC entry 179 (class 1259 OID 878317)
-- Name: parent; Type: TABLE; Schema: ; Owner: ; Tablespace: 
--

CREATE TABLE parent (
    id bigint NOT NULL,
    cd_id integer NOT NULL,
    common_name character varying(1000),
    corp_name character varying(255) NOT NULL,
    ignore boolean,
    mol_formula character varying(4000),
    mol_structure text,
    mol_weight double precision,
    parent_number bigint NOT NULL,
    registration_date timestamp without time zone,
    stereo_comment character varying(1000),
    version integer,
    chemist character varying(255),
    stereo_category bigint
);



--
-- TOC entry 180 (class 1259 OID 878327)
-- Name: physical_state; Type: TABLE; Schema: ; Owner: ; Tablespace: 
--

CREATE TABLE physical_state (
    id bigint NOT NULL,
    code character varying(100),
    name character varying(255),
    version integer
);



--
-- TOC entry 181 (class 1259 OID 878332)
-- Name: pre_def_corp_name; Type: TABLE; Schema: ; Owner: ; Tablespace: 
--

CREATE TABLE pre_def_corp_name (
    id bigint NOT NULL,
    comment character varying(255),
    corp_name character varying(64),
    corp_number bigint NOT NULL,
    skip boolean,
    used boolean,
    version integer
);



--
-- TOC entry 182 (class 1259 OID 878337)
-- Name: project; Type: TABLE; Schema: ; Owner: ; Tablespace: 
--

CREATE TABLE project (
    id bigint NOT NULL,
    code character varying(255),
    name character varying(255),
    version integer
);



--
-- TOC entry 183 (class 1259 OID 878345)
-- Name: purity_measured_by; Type: TABLE; Schema: ; Owner: ; Tablespace: 
--

CREATE TABLE purity_measured_by (
    id bigint NOT NULL,
    code character varying(255),
    name character varying(255),
    version integer
);



--
-- TOC entry 184 (class 1259 OID 878353)
-- Name: salt; Type: TABLE; Schema: ; Owner: ; Tablespace: 
--

CREATE TABLE salt (
    id bigint NOT NULL,
    abbrev character varying(100),
    cd_id integer NOT NULL,
    formula character varying(255),
    ignore boolean,
    mol_structure text,
    mol_weight double precision,
    name character varying(255),
    original_structure text,
    version integer
);



--
-- TOC entry 185 (class 1259 OID 878361)
-- Name: salt_form; Type: TABLE; Schema: ; Owner: ; Tablespace: 
--

CREATE TABLE salt_form (
    id bigint NOT NULL,
    cd_id integer NOT NULL,
    cas_number character varying(255),
    corp_name character varying(255),
    ignore boolean,
    mol_structure text,
    registration_date timestamp without time zone,
    salt_weight double precision,
    version integer,
    chemist character varying(255),
    parent bigint
);



--
-- TOC entry 186 (class 1259 OID 878369)
-- Name: salt_loader; Type: TABLE; Schema: ; Owner: ; Tablespace: 
--

CREATE TABLE salt_loader (
    id bigint NOT NULL,
    description character varying(255),
    file_name character varying(255),
    loaded_date timestamp without time zone,
    name character varying(255),
    number_of_salts bigint NOT NULL,
    size bigint NOT NULL,
    uploaded boolean,
    version integer
);


--
-- TOC entry 188 (class 1259 OID 878385)
-- Name: solution_unit; Type: TABLE; Schema: ; Owner: ; Tablespace: 
--

CREATE TABLE solution_unit (
    id bigint NOT NULL,
    code character varying(255),
    name character varying(255),
    version integer
);



--
-- TOC entry 189 (class 1259 OID 878393)
-- Name: stereo_category; Type: TABLE; Schema: ; Owner: ; Tablespace: 
--

CREATE TABLE stereo_category (
    id bigint NOT NULL,
    code character varying(255),
    name character varying(255),
    version integer
);



--
-- TOC entry 190 (class 1259 OID 878401)
-- Name: unit; Type: TABLE; Schema: ; Owner: ; Tablespace: 
--

CREATE TABLE unit (
    id bigint NOT NULL,
    code character varying(255),
    name character varying(255),
    version integer
);



--
-- TOC entry 191 (class 1259 OID 878409)
-- Name: vendor; Type: TABLE; Schema: ; Owner: ; Tablespace: 
--

CREATE TABLE vendor (
    id bigint NOT NULL,
    code character varying(255),
    name character varying(255),
    version integer
);



--
-- TOC entry 2781 (class 2606 OID 878272)
-- Name: corp_name_pkey; Type: CONSTRAINT; Schema: ; Owner: ; Tablespace: 
--

ALTER TABLE ONLY corp_name
    ADD CONSTRAINT corp_name_pkey PRIMARY KEY (id);


--
-- TOC entry 2783 (class 2606 OID 878280)
-- Name: file_list_pkey; Type: CONSTRAINT; Schema: ; Owner: ; Tablespace: 
--

ALTER TABLE ONLY file_list
    ADD CONSTRAINT file_list_pkey PRIMARY KEY (id);


--
-- TOC entry 2785 (class 2606 OID 878288)
-- Name: file_type_pkey; Type: CONSTRAINT; Schema: ; Owner: ; Tablespace: 
--

ALTER TABLE ONLY file_type
    ADD CONSTRAINT file_type_pkey PRIMARY KEY (id);


--
-- TOC entry 2787 (class 2606 OID 878293)
-- Name: iso_salt_pkey; Type: CONSTRAINT; Schema: ; Owner: ; Tablespace: 
--

ALTER TABLE ONLY iso_salt
    ADD CONSTRAINT iso_salt_pkey PRIMARY KEY (id);


--
-- TOC entry 2789 (class 2606 OID 878298)
-- Name: isotope_pkey; Type: CONSTRAINT; Schema: ; Owner: ; Tablespace: 
--

ALTER TABLE ONLY isotope
    ADD CONSTRAINT isotope_pkey PRIMARY KEY (id);


--
-- TOC entry 2793 (class 2606 OID 878308)
-- Name: lot_corp_name_key; Type: CONSTRAINT; Schema: ; Owner: ; Tablespace: 
--

ALTER TABLE ONLY lot
    ADD CONSTRAINT lot_corp_name_key UNIQUE (corp_name);


--
-- TOC entry 2799 (class 2606 OID 878306)
-- Name: lot_pkey; Type: CONSTRAINT; Schema: ; Owner: ; Tablespace: 
--

ALTER TABLE ONLY lot
    ADD CONSTRAINT lot_pkey PRIMARY KEY (id);


--
-- TOC entry 2807 (class 2606 OID 878316)
-- Name: operator_pkey; Type: CONSTRAINT; Schema: ; Owner: ; Tablespace: 
--

ALTER TABLE ONLY operator
    ADD CONSTRAINT operator_pkey PRIMARY KEY (id);


--
-- TOC entry 2812 (class 2606 OID 878326)
-- Name: parent_corp_name_key; Type: CONSTRAINT; Schema: ; Owner: ; Tablespace: 
--

ALTER TABLE ONLY parent
    ADD CONSTRAINT parent_corp_name_key UNIQUE (corp_name);


--
-- TOC entry 2816 (class 2606 OID 878324)
-- Name: parent_pkey; Type: CONSTRAINT; Schema: ; Owner: ; Tablespace: 
--

ALTER TABLE ONLY parent
    ADD CONSTRAINT parent_pkey PRIMARY KEY (id);


--
-- TOC entry 2820 (class 2606 OID 878331)
-- Name: physical_state_pkey; Type: CONSTRAINT; Schema: ; Owner: ; Tablespace: 
--

ALTER TABLE ONLY physical_state
    ADD CONSTRAINT physical_state_pkey PRIMARY KEY (id);


--
-- TOC entry 2822 (class 2606 OID 878336)
-- Name: pre_def_corp_name_pkey; Type: CONSTRAINT; Schema: ; Owner: ; Tablespace: 
--

ALTER TABLE ONLY pre_def_corp_name
    ADD CONSTRAINT pre_def_corp_name_pkey PRIMARY KEY (id);


--
-- TOC entry 2828 (class 2606 OID 878344)
-- Name: project_pkey; Type: CONSTRAINT; Schema: ; Owner: ; Tablespace: 
--

ALTER TABLE ONLY project
    ADD CONSTRAINT project_pkey PRIMARY KEY (id);


--
-- TOC entry 2830 (class 2606 OID 878352)
-- Name: purity_measured_by_pkey; Type: CONSTRAINT; Schema: ; Owner: ; Tablespace: 
--

ALTER TABLE ONLY purity_measured_by
    ADD CONSTRAINT purity_measured_by_pkey PRIMARY KEY (id);


--
-- TOC entry 2834 (class 2606 OID 878368)
-- Name: salt_form_pkey; Type: CONSTRAINT; Schema: ; Owner: ; Tablespace: 
--

ALTER TABLE ONLY salt_form
    ADD CONSTRAINT salt_form_pkey PRIMARY KEY (id);


--
-- TOC entry 2842 (class 2606 OID 878376)
-- Name: salt_loader_pkey; Type: CONSTRAINT; Schema: ; Owner: ; Tablespace: 
--

ALTER TABLE ONLY salt_loader
    ADD CONSTRAINT salt_loader_pkey PRIMARY KEY (id);


--
-- TOC entry 2832 (class 2606 OID 878360)
-- Name: salt_pkey; Type: CONSTRAINT; Schema: ; Owner: ; Tablespace: 
--

ALTER TABLE ONLY salt
    ADD CONSTRAINT salt_pkey PRIMARY KEY (id);


--
-- TOC entry 2846 (class 2606 OID 878392)
-- Name: solution_unit_pkey; Type: CONSTRAINT; Schema: ; Owner: ; Tablespace: 
--

ALTER TABLE ONLY solution_unit
    ADD CONSTRAINT solution_unit_pkey PRIMARY KEY (id);


--
-- TOC entry 2848 (class 2606 OID 878400)
-- Name: stereo_category_pkey; Type: CONSTRAINT; Schema: ; Owner: ; Tablespace: 
--

ALTER TABLE ONLY stereo_category
    ADD CONSTRAINT stereo_category_pkey PRIMARY KEY (id);


--
-- TOC entry 2850 (class 2606 OID 878408)
-- Name: unit_pkey; Type: CONSTRAINT; Schema: ; Owner: ; Tablespace: 
--

ALTER TABLE ONLY unit
    ADD CONSTRAINT unit_pkey PRIMARY KEY (id);


--
-- TOC entry 2852 (class 2606 OID 878416)
-- Name: vendor_pkey; Type: CONSTRAINT; Schema: ; Owner: ; Tablespace: 
--

ALTER TABLE ONLY vendor
    ADD CONSTRAINT vendor_pkey PRIMARY KEY (id);


--
-- TOC entry 2790 (class 1259 OID 878495)
-- Name: lot_barcode_idx; Type: INDEX; Schema: ; Owner: ; Tablespace: 
--

CREATE INDEX lot_barcode_idx ON lot USING btree (barcode);


--
-- TOC entry 2791 (class 1259 OID 878493)
-- Name: lot_chemist_idx; Type: INDEX; Schema: ; Owner: ; Tablespace: 
--

CREATE INDEX lot_chemist_idx ON lot USING btree (chemist);


--
-- TOC entry 2794 (class 1259 OID 878492)
-- Name: lot_corpname_idx; Type: INDEX; Schema: ; Owner: ; Tablespace: 
--

CREATE INDEX lot_corpname_idx ON lot USING btree (corp_name);


--
-- TOC entry 2795 (class 1259 OID 878497)
-- Name: lot_lotnumber_idx; Type: INDEX; Schema: ; Owner: ; Tablespace: 
--

CREATE INDEX lot_lotnumber_idx ON lot USING btree (lot_number);


--
-- TOC entry 2796 (class 1259 OID 878496)
-- Name: lot_notebook_idx; Type: INDEX; Schema: ; Owner: ; Tablespace: 
--

CREATE INDEX lot_notebook_idx ON lot USING btree (notebook_page);


--
-- TOC entry 2797 (class 1259 OID 878494)
-- Name: lot_physicalstate_idx; Type: INDEX; Schema: ; Owner: ; Tablespace: 
--

CREATE INDEX lot_physicalstate_idx ON lot USING btree (physical_state);


--
-- TOC entry 2800 (class 1259 OID 878499)
-- Name: lot_project_idx; Type: INDEX; Schema: ; Owner: ; Tablespace: 
--

CREATE INDEX lot_project_idx ON lot USING btree (project);


--
-- TOC entry 2801 (class 1259 OID 878500)
-- Name: lot_regdate_idx; Type: INDEX; Schema: ; Owner: ; Tablespace: 
--

CREATE INDEX lot_regdate_idx ON lot USING btree (registration_date);


--
-- TOC entry 2802 (class 1259 OID 878501)
-- Name: lot_saltform_idx; Type: INDEX; Schema: ; Owner: ; Tablespace: 
--

CREATE INDEX lot_saltform_idx ON lot USING btree (salt_form);


--
-- TOC entry 2803 (class 1259 OID 878498)
-- Name: lot_synthdate_idx; Type: INDEX; Schema: ; Owner: ; Tablespace: 
--

CREATE INDEX lot_synthdate_idx ON lot USING btree (synthesis_date);


--
-- TOC entry 2804 (class 1259 OID 878502)
-- Name: lot_vendor_idx; Type: INDEX; Schema: ; Owner: ; Tablespace: 
--

CREATE INDEX lot_vendor_idx ON lot USING btree (vendor);


--
-- TOC entry 2805 (class 1259 OID 878503)
-- Name: lot_virtual_idx; Type: INDEX; Schema: ; Owner: ; Tablespace: 
--

CREATE INDEX lot_virtual_idx ON lot USING btree (is_virtual);


--
-- TOC entry 2808 (class 1259 OID 878525)
-- Name: parent_cdid_idx; Type: INDEX; Schema: ; Owner: ; Tablespace: 
--

CREATE INDEX parent_cdid_idx ON parent USING btree (cd_id);


--
-- TOC entry 2809 (class 1259 OID 878523)
-- Name: parent_chemist_idx; Type: INDEX; Schema: ; Owner: ; Tablespace: 
--

CREATE INDEX parent_chemist_idx ON parent USING btree (chemist);


--
-- TOC entry 2810 (class 1259 OID 878520)
-- Name: parent_commonname_idx; Type: INDEX; Schema: ; Owner: ; Tablespace: 
--

CREATE INDEX parent_commonname_idx ON parent USING btree (common_name);


--
-- TOC entry 2813 (class 1259 OID 878524)
-- Name: parent_corpname_idx; Type: INDEX; Schema: ; Owner: ; Tablespace: 
--

CREATE INDEX parent_corpname_idx ON parent USING btree (corp_name);


--
-- TOC entry 2814 (class 1259 OID 878519)
-- Name: parent_parentnumber_idx; Type: INDEX; Schema: ; Owner: ; Tablespace: 
--

CREATE INDEX parent_parentnumber_idx ON parent USING btree (parent_number);


--
-- TOC entry 2817 (class 1259 OID 878522)
-- Name: parent_regdate_idx; Type: INDEX; Schema: ; Owner: ; Tablespace: 
--

CREATE INDEX parent_regdate_idx ON parent USING btree (registration_date);


--
-- TOC entry 2818 (class 1259 OID 878521)
-- Name: parent_stereocategory_idx; Type: INDEX; Schema: ; Owner: ; Tablespace: 
--

CREATE INDEX parent_stereocategory_idx ON parent USING btree (stereo_category);


--
-- TOC entry 2823 (class 1259 OID 878529)
-- Name: predef_corpname_idx; Type: INDEX; Schema: ; Owner: ; Tablespace: 
--

CREATE INDEX predef_corpname_idx ON pre_def_corp_name USING btree (corp_name);


--
-- TOC entry 2824 (class 1259 OID 878527)
-- Name: predef_corpnumber_idx; Type: INDEX; Schema: ; Owner: ; Tablespace: 
--

CREATE INDEX predef_corpnumber_idx ON pre_def_corp_name USING btree (corp_number);


--
-- TOC entry 2825 (class 1259 OID 878526)
-- Name: predef_skip_idx; Type: INDEX; Schema: ; Owner: ; Tablespace: 
--

CREATE INDEX predef_skip_idx ON pre_def_corp_name USING btree (skip);


--
-- TOC entry 2826 (class 1259 OID 878528)
-- Name: predef_used_idx; Type: INDEX; Schema: ; Owner: ; Tablespace: 
--

CREATE INDEX predef_used_idx ON pre_def_corp_name USING btree (used);


--
-- TOC entry 2835 (class 1259 OID 878545)
-- Name: saltform_casnumber_idx; Type: INDEX; Schema: ; Owner: ; Tablespace: 
--

CREATE INDEX saltform_casnumber_idx ON salt_form USING btree (cas_number);


--
-- TOC entry 2836 (class 1259 OID 878540)
-- Name: saltform_cdid_idx; Type: INDEX; Schema: ; Owner: ; Tablespace: 
--

CREATE INDEX saltform_cdid_idx ON salt_form USING btree (cd_id);


--
-- TOC entry 2837 (class 1259 OID 878543)
-- Name: saltform_chemist_idx; Type: INDEX; Schema: ; Owner: ; Tablespace: 
--

CREATE INDEX saltform_chemist_idx ON salt_form USING btree (chemist);


--
-- TOC entry 2838 (class 1259 OID 878544)
-- Name: saltform_corpname_idx; Type: INDEX; Schema: ; Owner: ; Tablespace: 
--

CREATE INDEX saltform_corpname_idx ON salt_form USING btree (corp_name);


--
-- TOC entry 2839 (class 1259 OID 878542)
-- Name: saltform_parent_idx; Type: INDEX; Schema: ; Owner: ; Tablespace: 
--

CREATE INDEX saltform_parent_idx ON salt_form USING btree (parent);


--
-- TOC entry 2840 (class 1259 OID 878541)
-- Name: saltform_regdate_idx; Type: INDEX; Schema: ; Owner: ; Tablespace: 
--

CREATE INDEX saltform_regdate_idx ON salt_form USING btree (registration_date);

--
-- TOC entry 2872 (class 2606 OID 878535)
-- Name: fk138dcb0dea40a445; Type: FK CONSTRAINT; Schema: ; Owner: 
--

ALTER TABLE ONLY salt_form
    ADD CONSTRAINT fk138dcb0dea40a445 FOREIGN KEY (parent) REFERENCES parent(id);

--
-- TOC entry 2861 (class 2606 OID 878457)
-- Name: fk1a35150191912; Type: FK CONSTRAINT; Schema: ; Owner: 
--

ALTER TABLE ONLY lot
    ADD CONSTRAINT fk1a35150191912 FOREIGN KEY (physical_state) REFERENCES physical_state(id);


--
-- TOC entry 2857 (class 2606 OID 878437)
-- Name: fk1a35158528bf8; Type: FK CONSTRAINT; Schema: ; Owner: 
--

ALTER TABLE ONLY lot
    ADD CONSTRAINT fk1a35158528bf8 FOREIGN KEY (salt_form) REFERENCES salt_form(id);


--
-- TOC entry 2866 (class 2606 OID 878482)
-- Name: fk1a3517133eff7; Type: FK CONSTRAINT; Schema: ; Owner: 
--

ALTER TABLE ONLY lot
    ADD CONSTRAINT fk1a3517133eff7 FOREIGN KEY (purity_operator) REFERENCES operator(id);


--
-- TOC entry 2865 (class 2606 OID 878477)
-- Name: fk1a3518f32029c; Type: FK CONSTRAINT; Schema: ; Owner: 
--

ALTER TABLE ONLY lot
    ADD CONSTRAINT fk1a3518f32029c FOREIGN KEY (solution_amount_units) REFERENCES solution_unit(id);


--
-- TOC entry 2858 (class 2606 OID 878442)
-- Name: fk1a35197886161; Type: FK CONSTRAINT; Schema: ; Owner: 
--

ALTER TABLE ONLY lot
    ADD CONSTRAINT fk1a35197886161 FOREIGN KEY (project) REFERENCES project(id);


--
-- TOC entry 2864 (class 2606 OID 878472)
-- Name: fk1a351c170886a; Type: FK CONSTRAINT; Schema: ; Owner: 
--

ALTER TABLE ONLY lot
    ADD CONSTRAINT fk1a351c170886a FOREIGN KEY (retain_units) REFERENCES unit(id);


--
-- TOC entry 2860 (class 2606 OID 878452)
-- Name: fk1a351f97d6c81; Type: FK CONSTRAINT; Schema: ; Owner: 
--

ALTER TABLE ONLY lot
    ADD CONSTRAINT fk1a351f97d6c81 FOREIGN KEY (purity_measured_by) REFERENCES purity_measured_by(id);


--
-- TOC entry 2862 (class 2606 OID 878462)
-- Name: fk1a351fa7edd7d; Type: FK CONSTRAINT; Schema: ; Owner: 
--

ALTER TABLE ONLY lot
    ADD CONSTRAINT fk1a351fa7edd7d FOREIGN KEY (amount_units) REFERENCES unit(id);


--
-- TOC entry 2859 (class 2606 OID 878447)
-- Name: fk1a351ff27dd81; Type: FK CONSTRAINT; Schema: ; Owner: 
--

ALTER TABLE ONLY lot
    ADD CONSTRAINT fk1a351ff27dd81 FOREIGN KEY (vendor) REFERENCES vendor(id);


--
-- TOC entry 2854 (class 2606 OID 878422)
-- Name: fk21a26bd058528bf8; Type: FK CONSTRAINT; Schema: ; Owner: 
--

ALTER TABLE ONLY iso_salt
    ADD CONSTRAINT fk21a26bd058528bf8 FOREIGN KEY (salt_form) REFERENCES salt_form(id);


--
-- TOC entry 2855 (class 2606 OID 878427)
-- Name: fk21a26bd0b6643a99; Type: FK CONSTRAINT; Schema: ; Owner: 
--

ALTER TABLE ONLY iso_salt
    ADD CONSTRAINT fk21a26bd0b6643a99 FOREIGN KEY (isotope) REFERENCES isotope(id);


--
-- TOC entry 2856 (class 2606 OID 878432)
-- Name: fk21a26bd0b8101bdd; Type: FK CONSTRAINT; Schema: ; Owner: 
--

ALTER TABLE ONLY iso_salt
    ADD CONSTRAINT fk21a26bd0b8101bdd FOREIGN KEY (salt) REFERENCES salt(id);


--
-- TOC entry 2853 (class 2606 OID 878417)
-- Name: fkb18781811eb60151; Type: FK CONSTRAINT; Schema: ; Owner: 
--

ALTER TABLE ONLY file_list
    ADD CONSTRAINT fkb18781811eb60151 FOREIGN KEY (lot) REFERENCES lot(id);

--
-- TOC entry 2868 (class 2606 OID 878504)
-- Name: fkc4ab08aaf46e206c; Type: FK CONSTRAINT; Schema: ; Owner: 
--

ALTER TABLE ONLY parent
    ADD CONSTRAINT fkc4ab08aaf46e206c FOREIGN KEY (stereo_category) REFERENCES stereo_category(id);


-- Completed on 2015-06-12 11:33:56 PDT

--
-- PostgreSQL database dump complete
--

