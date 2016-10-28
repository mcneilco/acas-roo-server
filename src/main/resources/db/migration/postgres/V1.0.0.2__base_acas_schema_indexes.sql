--
-- Name: analysis_group_code_name_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY analysis_group ADD CONSTRAINT analysis_group_code_name_key UNIQUE (code_name);


--
-- Name: analysis_group_label_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY analysis_group_label ADD CONSTRAINT analysis_group_label_pkey PRIMARY KEY (id);


--
-- Name: analysis_group_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY analysis_group ADD CONSTRAINT analysis_group_pkey PRIMARY KEY (id);


--
-- Name: analysis_group_state_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY analysis_group_state ADD CONSTRAINT analysis_group_state_pkey PRIMARY KEY (id);


--
-- Name: analysis_group_value_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY analysis_group_value ADD CONSTRAINT analysis_group_value_pkey PRIMARY KEY (id);


--
-- Name: analysisgroup_treatmentgroup_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY analysisgroup_treatmentgroup ADD CONSTRAINT analysisgroup_treatmentgroup_pkey PRIMARY KEY (treatment_group_id, analysis_group_id);


--
-- Name: application_setting_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY application_setting ADD CONSTRAINT application_setting_pkey PRIMARY KEY (id);


--
-- Name: author_email_address_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY author ADD CONSTRAINT author_email_address_key UNIQUE (email_address);


--
-- Name: author_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY author ADD CONSTRAINT author_pkey PRIMARY KEY (id);


--
-- Name: author_role_author_id_lsrole_id_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY author_role ADD CONSTRAINT author_role_author_id_lsrole_id_key UNIQUE (author_id, lsrole_id);


--
-- Name: author_role_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY author_role ADD CONSTRAINT author_role_pkey PRIMARY KEY (id);


--
-- Name: author_user_name_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY author ADD CONSTRAINT author_user_name_key UNIQUE (user_name);


--
-- Name: code_kind_ls_type_and_kind_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY code_kind ADD CONSTRAINT code_kind_ls_type_and_kind_key UNIQUE (ls_type_and_kind);


--
-- Name: code_kind_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY code_kind ADD CONSTRAINT code_kind_pkey PRIMARY KEY (id);


--
-- Name: code_origin_name_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY code_origin ADD CONSTRAINT code_origin_name_key UNIQUE (name);


--
-- Name: code_origin_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY code_origin ADD CONSTRAINT code_origin_pkey PRIMARY KEY (id);


--
-- Name: code_type_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY code_type ADD CONSTRAINT code_type_pkey PRIMARY KEY (id);


--
-- Name: code_type_type_name_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY code_type ADD CONSTRAINT code_type_type_name_key UNIQUE (type_name);


--
-- Name: container_code_name_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY container ADD CONSTRAINT container_code_name_key UNIQUE (code_name);


--
-- Name: container_kind_ls_type_and_kind_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY container_kind ADD CONSTRAINT container_kind_ls_type_and_kind_key UNIQUE (ls_type_and_kind);


--
-- Name: container_kind_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY container_kind ADD CONSTRAINT container_kind_pkey PRIMARY KEY (id);


--
-- Name: container_label_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY container_label ADD CONSTRAINT container_label_pkey PRIMARY KEY (id);


--
-- Name: container_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY container ADD CONSTRAINT container_pkey PRIMARY KEY (id);


--
-- Name: container_state_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY container_state ADD CONSTRAINT container_state_pkey PRIMARY KEY (id);


--
-- Name: container_type_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY container_type ADD CONSTRAINT container_type_pkey PRIMARY KEY (id);


--
-- Name: container_type_type_name_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY container_type ADD CONSTRAINT container_type_type_name_key UNIQUE (type_name);


--
-- Name: container_value_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY container_value ADD CONSTRAINT container_value_pkey PRIMARY KEY (id);


--
-- Name: ddict_kind_ls_type_and_kind_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY ddict_kind ADD CONSTRAINT ddict_kind_ls_type_and_kind_key UNIQUE (ls_type_and_kind);


--
-- Name: ddict_kind_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY ddict_kind ADD CONSTRAINT ddict_kind_pkey PRIMARY KEY (id);


--
-- Name: ddict_type_name_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY ddict_type ADD CONSTRAINT ddict_type_name_key UNIQUE (name);


--
-- Name: ddict_type_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY ddict_type ADD CONSTRAINT ddict_type_pkey PRIMARY KEY (id);


--
-- Name: ddict_value_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY ddict_value ADD CONSTRAINT ddict_value_pkey PRIMARY KEY (id);


--
-- Name: experiment_analysisgroup_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY experiment_analysisgroup ADD CONSTRAINT experiment_analysisgroup_pkey PRIMARY KEY (analysis_group_id, experiment_id);


--
-- Name: experiment_code_name_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY experiment ADD CONSTRAINT experiment_code_name_key UNIQUE (code_name);


--
-- Name: experiment_kind_ls_type_and_kind_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY experiment_kind ADD CONSTRAINT experiment_kind_ls_type_and_kind_key UNIQUE (ls_type_and_kind);


--
-- Name: experiment_kind_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY experiment_kind ADD CONSTRAINT experiment_kind_pkey PRIMARY KEY (id);


--
-- Name: experiment_label_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY experiment_label ADD CONSTRAINT experiment_label_pkey PRIMARY KEY (id);


--
-- Name: experiment_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY experiment ADD CONSTRAINT experiment_pkey PRIMARY KEY (id);


--
-- Name: experiment_state_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY experiment_state ADD CONSTRAINT experiment_state_pkey PRIMARY KEY (id);


--
-- Name: experiment_tag_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY experiment_tag ADD CONSTRAINT experiment_tag_pkey PRIMARY KEY (experiment_id, tag_id);


--
-- Name: experiment_type_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY experiment_type ADD CONSTRAINT experiment_type_pkey PRIMARY KEY (id);


--
-- Name: experiment_type_type_name_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY experiment_type ADD CONSTRAINT experiment_type_type_name_key UNIQUE (type_name);


--
-- Name: experiment_value_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY experiment_value ADD CONSTRAINT experiment_value_pkey PRIMARY KEY (id);


--
-- Name: file_thing_code_name_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY file_thing ADD CONSTRAINT file_thing_code_name_key UNIQUE (code_name);


--
-- Name: file_thing_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY file_thing ADD CONSTRAINT file_thing_pkey PRIMARY KEY (id);


--
-- Name: interaction_kind_ls_type_and_kind_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY interaction_kind ADD CONSTRAINT interaction_kind_ls_type_and_kind_key UNIQUE (ls_type_and_kind);


--
-- Name: interaction_kind_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY interaction_kind ADD CONSTRAINT interaction_kind_pkey PRIMARY KEY (id);


--
-- Name: interaction_type_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY interaction_type ADD CONSTRAINT interaction_type_pkey PRIMARY KEY (id);


--
-- Name: interaction_type_type_name_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY interaction_type ADD CONSTRAINT interaction_type_type_name_key UNIQUE (type_name);


--
-- Name: itx_container_container_code_name_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY itx_container_container ADD CONSTRAINT itx_container_container_code_name_key UNIQUE (code_name);


--
-- Name: itx_container_container_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY itx_container_container ADD CONSTRAINT itx_container_container_pkey PRIMARY KEY (id);


--
-- Name: itx_container_container_state_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY itx_container_container_state ADD CONSTRAINT itx_container_container_state_pkey PRIMARY KEY (id);


--
-- Name: itx_container_container_value_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY itx_container_container_value ADD CONSTRAINT itx_container_container_value_pkey PRIMARY KEY (id);

--
-- Name: itx_expt_expt_code_name_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY itx_expt_expt ADD CONSTRAINT itx_expt_expt_code_name_key UNIQUE (code_name);


--
-- Name: itx_expt_expt_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY itx_expt_expt ADD CONSTRAINT itx_expt_expt_pkey PRIMARY KEY (id);


--
-- Name: itx_expt_expt_state_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY itx_expt_expt_state ADD CONSTRAINT itx_expt_expt_state_pkey PRIMARY KEY (id);


--
-- Name: itx_expt_expt_value_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY itx_expt_expt_value ADD CONSTRAINT itx_expt_expt_value_pkey PRIMARY KEY (id);


--
-- Name: itx_ls_thing_ls_thing_code_name_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY itx_ls_thing_ls_thing ADD CONSTRAINT itx_ls_thing_ls_thing_code_name_key UNIQUE (code_name);


--
-- Name: itx_ls_thing_ls_thing_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY itx_ls_thing_ls_thing ADD CONSTRAINT itx_ls_thing_ls_thing_pkey PRIMARY KEY (id);


--
-- Name: itx_ls_thing_ls_thing_state_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY itx_ls_thing_ls_thing_state ADD CONSTRAINT itx_ls_thing_ls_thing_state_pkey PRIMARY KEY (id);


--
-- Name: itx_ls_thing_ls_thing_value_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY itx_ls_thing_ls_thing_value ADD CONSTRAINT itx_ls_thing_ls_thing_value_pkey PRIMARY KEY (id);


--
-- Name: itx_protocol_protocol_code_name_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY itx_protocol_protocol ADD CONSTRAINT itx_protocol_protocol_code_name_key UNIQUE (code_name);


--
-- Name: itx_protocol_protocol_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY itx_protocol_protocol ADD CONSTRAINT itx_protocol_protocol_pkey PRIMARY KEY (id);


--
-- Name: itx_protocol_protocol_state_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY itx_protocol_protocol_state ADD CONSTRAINT itx_protocol_protocol_state_pkey PRIMARY KEY (id);


--
-- Name: itx_protocol_protocol_value_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY itx_protocol_protocol_value ADD CONSTRAINT itx_protocol_protocol_value_pkey PRIMARY KEY (id);


--
-- Name: itx_subject_container_code_name_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY itx_subject_container ADD CONSTRAINT itx_subject_container_code_name_key UNIQUE (code_name);


--
-- Name: itx_subject_container_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY itx_subject_container ADD CONSTRAINT itx_subject_container_pkey PRIMARY KEY (id);


--
-- Name: itx_subject_container_state_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY itx_subject_container_state ADD CONSTRAINT itx_subject_container_state_pkey PRIMARY KEY (id);


--
-- Name: itx_subject_container_value_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY itx_subject_container_value ADD CONSTRAINT itx_subject_container_value_pkey PRIMARY KEY (id);


--
-- Name: label_kind_ls_type_and_kind_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY label_kind ADD CONSTRAINT label_kind_ls_type_and_kind_key UNIQUE (ls_type_and_kind);


--
-- Name: label_kind_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY label_kind ADD CONSTRAINT label_kind_pkey PRIMARY KEY (id);


--
-- Name: label_sequence_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY label_sequence ADD CONSTRAINT label_sequence_pkey PRIMARY KEY (id);


--
-- Name: label_type_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY label_type ADD CONSTRAINT label_type_pkey PRIMARY KEY (id);


--
-- Name: label_type_type_name_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY label_type ADD CONSTRAINT label_type_type_name_key UNIQUE (type_name);


--
-- Name: ls_interaction_code_name_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY ls_interaction ADD CONSTRAINT ls_interaction_code_name_key UNIQUE (code_name);


--
-- Name: ls_interaction_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY ls_interaction ADD CONSTRAINT ls_interaction_pkey PRIMARY KEY (id);


--
-- Name: ls_role_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY ls_role ADD CONSTRAINT ls_role_pkey PRIMARY KEY (id);


--
-- Name: ls_role_role_name_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY ls_role ADD CONSTRAINT ls_role_role_name_key UNIQUE (role_name);


--
-- Name: ls_seq_anl_grp_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY ls_seq_anl_grp ADD CONSTRAINT ls_seq_anl_grp_pkey PRIMARY KEY (id);


--
-- Name: ls_seq_container_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY ls_seq_container ADD CONSTRAINT ls_seq_container_pkey PRIMARY KEY (id);


--
-- Name: ls_seq_expt_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY ls_seq_expt ADD CONSTRAINT ls_seq_expt_pkey PRIMARY KEY (id);


--
-- Name: ls_seq_itx_cntr_cntr_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY ls_seq_itx_cntr_cntr ADD CONSTRAINT ls_seq_itx_cntr_cntr_pkey PRIMARY KEY (id);


--
-- Name: ls_seq_itx_experiment_experiment_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY ls_seq_itx_experiment_experiment ADD CONSTRAINT ls_seq_itx_experiment_experiment_pkey PRIMARY KEY (id);


--
-- Name: ls_seq_itx_protocol_protocol_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY ls_seq_itx_protocol_protocol ADD CONSTRAINT ls_seq_itx_protocol_protocol_pkey PRIMARY KEY (id);


--
-- Name: ls_seq_itx_subj_cntr_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY ls_seq_itx_subj_cntr ADD CONSTRAINT ls_seq_itx_subj_cntr_pkey PRIMARY KEY (id);


--
-- Name: ls_seq_protocol_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY ls_seq_protocol ADD CONSTRAINT ls_seq_protocol_pkey PRIMARY KEY (id);


--
-- Name: ls_seq_subject_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY ls_seq_subject ADD CONSTRAINT ls_seq_subject_pkey PRIMARY KEY (id);


--
-- Name: ls_seq_trt_grp_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY ls_seq_trt_grp ADD CONSTRAINT ls_seq_trt_grp_pkey PRIMARY KEY (id);


--
-- Name: ls_tag_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY ls_tag ADD CONSTRAINT ls_tag_pkey PRIMARY KEY (id);


--
-- Name: ls_thing_code_name_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY ls_thing ADD CONSTRAINT ls_thing_code_name_key UNIQUE (code_name);


--
-- Name: ls_thing_label_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY ls_thing_label ADD CONSTRAINT ls_thing_label_pkey PRIMARY KEY (id);


--
-- Name: ls_thing_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY ls_thing ADD CONSTRAINT ls_thing_pkey PRIMARY KEY (id);


--
-- Name: ls_thing_state_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY ls_thing_state ADD CONSTRAINT ls_thing_state_pkey PRIMARY KEY (id);


--
-- Name: ls_thing_value_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY ls_thing_value ADD CONSTRAINT ls_thing_value_pkey PRIMARY KEY (id);


--
-- Name: ls_transaction_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY ls_transaction ADD CONSTRAINT ls_transaction_pkey PRIMARY KEY (id);


--
-- Name: lsthing_tag_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY lsthing_tag ADD CONSTRAINT lsthing_tag_pkey PRIMARY KEY (lsthing_id, tag_id);


--
-- Name: operator_kind_ls_type_and_kind_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY operator_kind ADD CONSTRAINT operator_kind_ls_type_and_kind_key UNIQUE (ls_type_and_kind);


--
-- Name: operator_kind_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY operator_kind ADD CONSTRAINT operator_kind_pkey PRIMARY KEY (id);


--
-- Name: operator_type_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY operator_type ADD CONSTRAINT operator_type_pkey PRIMARY KEY (id);


--
-- Name: operator_type_type_name_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY operator_type ADD CONSTRAINT operator_type_type_name_key UNIQUE (type_name);


--
-- Name: protocol_code_name_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY protocol ADD CONSTRAINT protocol_code_name_key UNIQUE (code_name);


--
-- Name: protocol_kind_ls_type_and_kind_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY protocol_kind ADD CONSTRAINT protocol_kind_ls_type_and_kind_key UNIQUE (ls_type_and_kind);


--
-- Name: protocol_kind_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY protocol_kind ADD CONSTRAINT protocol_kind_pkey PRIMARY KEY (id);


--
-- Name: protocol_label_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY protocol_label ADD CONSTRAINT protocol_label_pkey PRIMARY KEY (id);


--
-- Name: protocol_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY protocol ADD CONSTRAINT protocol_pkey PRIMARY KEY (id);


--
-- Name: protocol_state_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY protocol_state ADD CONSTRAINT protocol_state_pkey PRIMARY KEY (id);


--
-- Name: protocol_tag_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY protocol_tag ADD CONSTRAINT protocol_tag_pkey PRIMARY KEY (protocol_id, tag_id);


--
-- Name: protocol_type_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY protocol_type ADD CONSTRAINT protocol_type_pkey PRIMARY KEY (id);


--
-- Name: protocol_type_type_name_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY protocol_type ADD CONSTRAINT protocol_type_type_name_key UNIQUE (type_name);


--
-- Name: protocol_value_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY protocol_value ADD CONSTRAINT protocol_value_pkey PRIMARY KEY (id);


--
-- Name: state_kind_ls_type_and_kind_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY state_kind ADD CONSTRAINT state_kind_ls_type_and_kind_key UNIQUE (ls_type_and_kind);


--
-- Name: state_kind_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY state_kind ADD CONSTRAINT state_kind_pkey PRIMARY KEY (id);


--
-- Name: state_type_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY state_type ADD CONSTRAINT state_type_pkey PRIMARY KEY (id);


--
-- Name: state_type_type_name_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY state_type ADD CONSTRAINT state_type_type_name_key UNIQUE (type_name);


--
-- Name: subject_code_name_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY subject ADD CONSTRAINT subject_code_name_key UNIQUE (code_name);


--
-- Name: subject_label_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY subject_label ADD CONSTRAINT subject_label_pkey PRIMARY KEY (id);


--
-- Name: subject_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY subject ADD CONSTRAINT subject_pkey PRIMARY KEY (id);


--
-- Name: subject_state_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY subject_state ADD CONSTRAINT subject_state_pkey PRIMARY KEY (id);


--
-- Name: subject_value_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY subject_value ADD CONSTRAINT subject_value_pkey PRIMARY KEY (id);


--
-- Name: thing_kind_ls_type_and_kind_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY thing_kind ADD CONSTRAINT thing_kind_ls_type_and_kind_key UNIQUE (ls_type_and_kind);


--
-- Name: thing_kind_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY thing_kind ADD CONSTRAINT thing_kind_pkey PRIMARY KEY (id);


--
-- Name: thing_page_archive_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY thing_page_archive ADD CONSTRAINT thing_page_archive_pkey PRIMARY KEY (id);


--
-- Name: thing_page_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY thing_page ADD CONSTRAINT thing_page_pkey PRIMARY KEY (id);


--
-- Name: thing_type_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY thing_type ADD CONSTRAINT thing_type_pkey PRIMARY KEY (id);


--
-- Name: thing_type_type_name_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY thing_type ADD CONSTRAINT thing_type_type_name_key UNIQUE (type_name);


--
-- Name: treatment_group_code_name_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY treatment_group ADD CONSTRAINT treatment_group_code_name_key UNIQUE (code_name);


--
-- Name: treatment_group_label_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY treatment_group_label ADD CONSTRAINT treatment_group_label_pkey PRIMARY KEY (id);


--
-- Name: treatment_group_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY treatment_group ADD CONSTRAINT treatment_group_pkey PRIMARY KEY (id);


--
-- Name: treatment_group_state_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY treatment_group_state ADD CONSTRAINT treatment_group_state_pkey PRIMARY KEY (id);


--
-- Name: treatment_group_value_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY treatment_group_value ADD CONSTRAINT treatment_group_value_pkey PRIMARY KEY (id);


--
-- Name: treatmentgroup_subject_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY treatmentgroup_subject ADD CONSTRAINT treatmentgroup_subject_pkey PRIMARY KEY (subject_id, treatment_group_id);


--
-- Name: uncertainty_kind_kind_name_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY uncertainty_kind ADD CONSTRAINT uncertainty_kind_kind_name_key UNIQUE (kind_name);


--
-- Name: uncertainty_kind_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY uncertainty_kind ADD CONSTRAINT uncertainty_kind_pkey PRIMARY KEY (id);


--
-- Name: unit_kind_ls_type_and_kind_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY unit_kind ADD CONSTRAINT unit_kind_ls_type_and_kind_key UNIQUE (ls_type_and_kind);


--
-- Name: unit_kind_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY unit_kind ADD CONSTRAINT unit_kind_pkey PRIMARY KEY (id);


--
-- Name: unit_type_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY unit_type ADD CONSTRAINT unit_type_pkey PRIMARY KEY (id);


--
-- Name: unit_type_type_name_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY unit_type ADD CONSTRAINT unit_type_type_name_key UNIQUE (type_name);


--
-- Name: update_log_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY update_log ADD CONSTRAINT update_log_pkey PRIMARY KEY (id);


--
-- Name: value_kind_ls_type_and_kind_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY value_kind ADD CONSTRAINT value_kind_ls_type_and_kind_key UNIQUE (ls_type_and_kind);


--
-- Name: value_kind_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY value_kind ADD CONSTRAINT value_kind_pkey PRIMARY KEY (id);


--
-- Name: value_type_pkey; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY value_type ADD CONSTRAINT value_type_pkey PRIMARY KEY (id);


--
-- Name: value_type_type_name_key; Type: CONSTRAINT; Schema: acas; Owner: acas; Tablespace: 
--

ALTER TABLE ONLY value_type ADD CONSTRAINT value_type_type_name_key UNIQUE (type_name);


--
-- Name: analysis_group_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX analysis_group_ign_idx ON analysis_group USING btree (ignored);


--
-- Name: analysis_group_kind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX analysis_group_kind_idx ON analysis_group USING btree (ls_kind);


--
-- Name: analysis_group_label_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX analysis_group_label_ign_idx ON analysis_group_label USING btree (ignored);


--
-- Name: analysis_group_label_kind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX analysis_group_label_kind_idx ON analysis_group_label USING btree (ls_kind);


--
-- Name: analysis_group_label_pref_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX analysis_group_label_pref_idx ON analysis_group_label USING btree (preferred);


--
-- Name: analysis_group_label_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX analysis_group_label_tk_idx ON analysis_group_label USING btree (ls_type_and_kind);


--
-- Name: analysis_group_label_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX analysis_group_label_trxn_idx ON analysis_group_label USING btree (ls_transaction);


--
-- Name: analysis_group_label_type_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX analysis_group_label_type_idx ON analysis_group_label USING btree (ls_type);


--
-- Name: analysis_group_state_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX analysis_group_state_ign_idx ON analysis_group_state USING btree (ignored);


--
-- Name: analysis_group_state_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX analysis_group_state_tk_idx ON analysis_group_state USING btree (ls_type_and_kind);


--
-- Name: analysis_group_state_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX analysis_group_state_trxn_idx ON analysis_group_state USING btree (ls_transaction);


--
-- Name: analysis_group_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX analysis_group_tk_idx ON analysis_group USING btree (ls_type_and_kind);


--
-- Name: analysis_group_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX analysis_group_trxn_idx ON analysis_group USING btree (ls_transaction);


--
-- Name: analysis_group_type_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX analysis_group_type_idx ON analysis_group USING btree (ls_type);


--
-- Name: analysis_group_value_cdkind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX analysis_group_value_cdkind_idx ON analysis_group_value USING btree (code_kind);


--
-- Name: analysis_group_value_cdorgn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX analysis_group_value_cdorgn_idx ON analysis_group_value USING btree (code_origin);


--
-- Name: analysis_group_value_cdtk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX analysis_group_value_cdtk_idx ON analysis_group_value USING btree (code_type_and_kind);


--
-- Name: analysis_group_value_cdtype_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX analysis_group_value_cdtype_idx ON analysis_group_value USING btree (code_type);


--
-- Name: analysis_group_value_code_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX analysis_group_value_code_idx ON analysis_group_value USING btree (code_value);


--
-- Name: analysis_group_value_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX analysis_group_value_ign_idx ON analysis_group_value USING btree (ignored);


--
-- Name: analysis_group_value_kind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX analysis_group_value_kind_idx ON analysis_group_value USING btree (ls_kind);


--
-- Name: analysis_group_value_optk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX analysis_group_value_optk_idx ON analysis_group_value USING btree (operator_type_and_kind);


--
-- Name: analysis_group_value_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX analysis_group_value_tk_idx ON analysis_group_value USING btree (ls_type_and_kind);


--
-- Name: analysis_group_value_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX analysis_group_value_trxn_idx ON analysis_group_value USING btree (ls_transaction);


--
-- Name: analysis_group_value_type_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX analysis_group_value_type_idx ON analysis_group_value USING btree (ls_type);


--
-- Name: analysis_group_value_untk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX analysis_group_value_untk_idx ON analysis_group_value USING btree (unit_type_and_kind);


--
-- Name: container_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX container_ign_idx ON container USING btree (ignored);


--
-- Name: container_kind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX container_kind_idx ON container USING btree (ls_kind);


--
-- Name: container_label_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX container_label_ign_idx ON container_label USING btree (ignored);


--
-- Name: container_label_kind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX container_label_kind_idx ON container_label USING btree (ls_kind);


--
-- Name: container_label_pref_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX container_label_pref_idx ON container_label USING btree (preferred);


--
-- Name: container_label_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX container_label_tk_idx ON container_label USING btree (ls_type_and_kind);


--
-- Name: container_label_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX container_label_trxn_idx ON container_label USING btree (ls_transaction);


--
-- Name: container_label_type_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX container_label_type_idx ON container_label USING btree (ls_type);


--
-- Name: container_state_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX container_state_ign_idx ON container_state USING btree (ignored);


--
-- Name: container_state_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX container_state_tk_idx ON container_state USING btree (ls_type_and_kind);


--
-- Name: container_state_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX container_state_trxn_idx ON container_state USING btree (ls_transaction);


--
-- Name: container_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX container_tk_idx ON container USING btree (ls_type_and_kind);


--
-- Name: container_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX container_trxn_idx ON container USING btree (ls_transaction);


--
-- Name: container_type_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX container_type_idx ON container USING btree (ls_type);


--
-- Name: container_value_cdkind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX container_value_cdkind_idx ON container_value USING btree (code_kind);


--
-- Name: container_value_cdorgn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX container_value_cdorgn_idx ON container_value USING btree (code_origin);


--
-- Name: container_value_cdtk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX container_value_cdtk_idx ON container_value USING btree (code_type_and_kind);


--
-- Name: container_value_cdtype_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX container_value_cdtype_idx ON container_value USING btree (code_type);


--
-- Name: container_value_code_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX container_value_code_idx ON container_value USING btree (code_value);


--
-- Name: container_value_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX container_value_ign_idx ON container_value USING btree (ignored);


--
-- Name: container_value_kind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX container_value_kind_idx ON container_value USING btree (ls_kind);


--
-- Name: container_value_optk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX container_value_optk_idx ON container_value USING btree (operator_type_and_kind);


--
-- Name: container_value_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX container_value_tk_idx ON container_value USING btree (ls_type_and_kind);


--
-- Name: container_value_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX container_value_trxn_idx ON container_value USING btree (ls_transaction);


--
-- Name: container_value_type_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX container_value_type_idx ON container_value USING btree (ls_type);


--
-- Name: container_value_untk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX container_value_untk_idx ON container_value USING btree (unit_type_and_kind);


--
-- Name: dd_kind_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX dd_kind_tk_idx ON ddict_kind USING btree (ls_type_and_kind);


--
-- Name: dd_value_code_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX dd_value_code_idx ON ddict_value USING btree (code_name);


--
-- Name: dd_value_kind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX dd_value_kind_idx ON ddict_value USING btree (ls_kind);


--
-- Name: dd_value_sname_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX dd_value_sname_idx ON ddict_value USING btree (short_name);


--
-- Name: dd_value_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX dd_value_tk_idx ON ddict_value USING btree (ls_type_and_kind);


--
-- Name: dd_value_type_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX dd_value_type_idx ON ddict_value USING btree (ls_type);


--
-- Name: experiment_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX experiment_ign_idx ON experiment USING btree (ignored);


--
-- Name: experiment_kind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX experiment_kind_idx ON experiment USING btree (ls_kind);


--
-- Name: experiment_label_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX experiment_label_ign_idx ON experiment_label USING btree (ignored);


--
-- Name: experiment_label_kind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX experiment_label_kind_idx ON experiment_label USING btree (ls_kind);


--
-- Name: experiment_label_pref_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX experiment_label_pref_idx ON experiment_label USING btree (preferred);


--
-- Name: experiment_label_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX experiment_label_tk_idx ON experiment_label USING btree (ls_type_and_kind);


--
-- Name: experiment_label_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX experiment_label_trxn_idx ON experiment_label USING btree (ls_transaction);


--
-- Name: experiment_label_type_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX experiment_label_type_idx ON experiment_label USING btree (ls_type);


--
-- Name: experiment_state_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX experiment_state_ign_idx ON experiment_state USING btree (ignored);


--
-- Name: experiment_state_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX experiment_state_tk_idx ON experiment_state USING btree (ls_type_and_kind);


--
-- Name: experiment_state_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX experiment_state_trxn_idx ON experiment_state USING btree (ls_transaction);


--
-- Name: experiment_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX experiment_tk_idx ON experiment USING btree (ls_type_and_kind);


--
-- Name: experiment_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX experiment_trxn_idx ON experiment USING btree (ls_transaction);


--
-- Name: experiment_type_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX experiment_type_idx ON experiment USING btree (ls_type);


--
-- Name: experiment_value_cdkind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX experiment_value_cdkind_idx ON experiment_value USING btree (code_kind);


--
-- Name: experiment_value_cdorgn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX experiment_value_cdorgn_idx ON experiment_value USING btree (code_origin);


--
-- Name: experiment_value_cdtk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX experiment_value_cdtk_idx ON experiment_value USING btree (code_type_and_kind);


--
-- Name: experiment_value_cdtype_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX experiment_value_cdtype_idx ON experiment_value USING btree (code_type);


--
-- Name: experiment_value_code_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX experiment_value_code_idx ON experiment_value USING btree (code_value);


--
-- Name: experiment_value_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX experiment_value_ign_idx ON experiment_value USING btree (ignored);


--
-- Name: experiment_value_kind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX experiment_value_kind_idx ON experiment_value USING btree (ls_kind);


--
-- Name: experiment_value_optk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX experiment_value_optk_idx ON experiment_value USING btree (operator_type_and_kind);


--
-- Name: experiment_value_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX experiment_value_tk_idx ON experiment_value USING btree (ls_type_and_kind);


--
-- Name: experiment_value_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX experiment_value_trxn_idx ON experiment_value USING btree (ls_transaction);


--
-- Name: experiment_value_type_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX experiment_value_type_idx ON experiment_value USING btree (ls_type);


--
-- Name: experiment_value_untk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX experiment_value_untk_idx ON experiment_value USING btree (unit_type_and_kind);


--
-- Name: file_thing_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX file_thing_ign_idx ON file_thing USING btree (ignored);


--
-- Name: file_thing_kind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX file_thing_kind_idx ON file_thing USING btree (ls_kind);


--
-- Name: file_thing_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX file_thing_tk_idx ON file_thing USING btree (ls_type_and_kind);


--
-- Name: file_thing_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX file_thing_trxn_idx ON file_thing USING btree (ls_transaction);


--
-- Name: file_thing_type_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX file_thing_type_idx ON file_thing USING btree (ls_type);


--
-- Name: itx_container_container_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_container_container_ign_idx ON itx_container_container USING btree (ignored);


--
-- Name: itx_container_container_kind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_container_container_kind_idx ON itx_container_container USING btree (ls_kind);


--
-- Name: itx_container_container_state_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_container_container_state_ign_idx ON itx_container_container_state USING btree (ignored);


--
-- Name: itx_container_container_state_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_container_container_state_tk_idx ON itx_container_container_state USING btree (ls_type_and_kind);


--
-- Name: itx_container_container_state_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_container_container_state_trxn_idx ON itx_container_container_state USING btree (ls_transaction);


--
-- Name: itx_container_container_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_container_container_tk_idx ON itx_container_container USING btree (ls_type_and_kind);


--
-- Name: itx_container_container_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_container_container_trxn_idx ON itx_container_container USING btree (ls_transaction);


--
-- Name: itx_container_container_type_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_container_container_type_idx ON itx_container_container USING btree (ls_type);


--
-- Name: itx_container_container_value_cdkind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_container_container_value_cdkind_idx ON itx_container_container_value USING btree (code_kind);


--
-- Name: itx_container_container_value_cdorgn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_container_container_value_cdorgn_idx ON itx_container_container_value USING btree (code_origin);


--
-- Name: itx_container_container_value_cdtk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_container_container_value_cdtk_idx ON itx_container_container_value USING btree (code_type_and_kind);


--
-- Name: itx_container_container_value_cdtype_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_container_container_value_cdtype_idx ON itx_container_container_value USING btree (code_type);


--
-- Name: itx_container_container_value_code_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_container_container_value_code_idx ON itx_container_container_value USING btree (code_value);


--
-- Name: itx_container_container_value_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_container_container_value_ign_idx ON itx_container_container_value USING btree (ignored);


--
-- Name: itx_container_container_value_kind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_container_container_value_kind_idx ON itx_container_container_value USING btree (ls_kind);


--
-- Name: itx_container_container_value_optk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_container_container_value_optk_idx ON itx_container_container_value USING btree (operator_type_and_kind);


--
-- Name: itx_container_container_value_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_container_container_value_tk_idx ON itx_container_container_value USING btree (ls_type_and_kind);


--
-- Name: itx_container_container_value_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_container_container_value_trxn_idx ON itx_container_container_value USING btree (ls_transaction);


--
-- Name: itx_container_container_value_type_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_container_container_value_type_idx ON itx_container_container_value USING btree (ls_type);


--
-- Name: itx_container_container_value_untk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_container_container_value_untk_idx ON itx_container_container_value USING btree (unit_type_and_kind);

--
-- Name: itx_expt_expt_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_expt_expt_ign_idx ON itx_expt_expt USING btree (ignored);


--
-- Name: itx_expt_expt_kind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_expt_expt_kind_idx ON itx_expt_expt USING btree (ls_kind);


--
-- Name: itx_expt_expt_state_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_expt_expt_state_ign_idx ON itx_expt_expt_state USING btree (ignored);


--
-- Name: itx_expt_expt_state_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_expt_expt_state_tk_idx ON itx_expt_expt_state USING btree (ls_type_and_kind);


--
-- Name: itx_expt_expt_state_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_expt_expt_state_trxn_idx ON itx_expt_expt_state USING btree (ls_transaction);


--
-- Name: itx_expt_expt_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_expt_expt_tk_idx ON itx_expt_expt USING btree (ls_type_and_kind);


--
-- Name: itx_expt_expt_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_expt_expt_trxn_idx ON itx_expt_expt USING btree (ls_transaction);


--
-- Name: itx_expt_expt_type_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_expt_expt_type_idx ON itx_expt_expt USING btree (ls_type);


--
-- Name: itx_expt_expt_value_cdkind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_expt_expt_value_cdkind_idx ON itx_expt_expt_value USING btree (code_kind);


--
-- Name: itx_expt_expt_value_cdorgn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_expt_expt_value_cdorgn_idx ON itx_expt_expt_value USING btree (code_origin);


--
-- Name: itx_expt_expt_value_cdtk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_expt_expt_value_cdtk_idx ON itx_expt_expt_value USING btree (code_type_and_kind);


--
-- Name: itx_expt_expt_value_cdtype_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_expt_expt_value_cdtype_idx ON itx_expt_expt_value USING btree (code_type);


--
-- Name: itx_expt_expt_value_code_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_expt_expt_value_code_idx ON itx_expt_expt_value USING btree (code_value);


--
-- Name: itx_expt_expt_value_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_expt_expt_value_ign_idx ON itx_expt_expt_value USING btree (ignored);


--
-- Name: itx_expt_expt_value_kind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_expt_expt_value_kind_idx ON itx_expt_expt_value USING btree (ls_kind);


--
-- Name: itx_expt_expt_value_optk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_expt_expt_value_optk_idx ON itx_expt_expt_value USING btree (operator_type_and_kind);


--
-- Name: itx_expt_expt_value_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_expt_expt_value_tk_idx ON itx_expt_expt_value USING btree (ls_type_and_kind);


--
-- Name: itx_expt_expt_value_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_expt_expt_value_trxn_idx ON itx_expt_expt_value USING btree (ls_transaction);


--
-- Name: itx_expt_expt_value_type_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_expt_expt_value_type_idx ON itx_expt_expt_value USING btree (ls_type);


--
-- Name: itx_expt_expt_value_untk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_expt_expt_value_untk_idx ON itx_expt_expt_value USING btree (unit_type_and_kind);


--
-- Name: itx_ls_thing_ls_thing_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_ls_thing_ls_thing_ign_idx ON itx_ls_thing_ls_thing USING btree (ignored);


--
-- Name: itx_ls_thing_ls_thing_kind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_ls_thing_ls_thing_kind_idx ON itx_ls_thing_ls_thing USING btree (ls_kind);


--
-- Name: itx_ls_thing_ls_thing_state_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_ls_thing_ls_thing_state_ign_idx ON itx_ls_thing_ls_thing_state USING btree (ignored);


--
-- Name: itx_ls_thing_ls_thing_state_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_ls_thing_ls_thing_state_tk_idx ON itx_ls_thing_ls_thing_state USING btree (ls_type_and_kind);


--
-- Name: itx_ls_thing_ls_thing_state_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_ls_thing_ls_thing_state_trxn_idx ON itx_ls_thing_ls_thing_state USING btree (ls_transaction);


--
-- Name: itx_ls_thing_ls_thing_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_ls_thing_ls_thing_tk_idx ON itx_ls_thing_ls_thing USING btree (ls_type_and_kind);


--
-- Name: itx_ls_thing_ls_thing_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_ls_thing_ls_thing_trxn_idx ON itx_ls_thing_ls_thing USING btree (ls_transaction);


--
-- Name: itx_ls_thing_ls_thing_type_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_ls_thing_ls_thing_type_idx ON itx_ls_thing_ls_thing USING btree (ls_type);


--
-- Name: itx_ls_thing_ls_thing_value_cdkind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_ls_thing_ls_thing_value_cdkind_idx ON itx_ls_thing_ls_thing_value USING btree (code_kind);


--
-- Name: itx_ls_thing_ls_thing_value_cdorgn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_ls_thing_ls_thing_value_cdorgn_idx ON itx_ls_thing_ls_thing_value USING btree (code_origin);


--
-- Name: itx_ls_thing_ls_thing_value_cdtk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_ls_thing_ls_thing_value_cdtk_idx ON itx_ls_thing_ls_thing_value USING btree (code_type_and_kind);


--
-- Name: itx_ls_thing_ls_thing_value_cdtype_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_ls_thing_ls_thing_value_cdtype_idx ON itx_ls_thing_ls_thing_value USING btree (code_type);


--
-- Name: itx_ls_thing_ls_thing_value_code_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_ls_thing_ls_thing_value_code_idx ON itx_ls_thing_ls_thing_value USING btree (code_value);


--
-- Name: itx_ls_thing_ls_thing_value_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_ls_thing_ls_thing_value_ign_idx ON itx_ls_thing_ls_thing_value USING btree (ignored);


--
-- Name: itx_ls_thing_ls_thing_value_kind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_ls_thing_ls_thing_value_kind_idx ON itx_ls_thing_ls_thing_value USING btree (ls_kind);


--
-- Name: itx_ls_thing_ls_thing_value_optk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_ls_thing_ls_thing_value_optk_idx ON itx_ls_thing_ls_thing_value USING btree (operator_type_and_kind);


--
-- Name: itx_ls_thing_ls_thing_value_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_ls_thing_ls_thing_value_tk_idx ON itx_ls_thing_ls_thing_value USING btree (ls_type_and_kind);


--
-- Name: itx_ls_thing_ls_thing_value_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_ls_thing_ls_thing_value_trxn_idx ON itx_ls_thing_ls_thing_value USING btree (ls_transaction);


--
-- Name: itx_ls_thing_ls_thing_value_type_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_ls_thing_ls_thing_value_type_idx ON itx_ls_thing_ls_thing_value USING btree (ls_type);


--
-- Name: itx_ls_thing_ls_thing_value_untk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_ls_thing_ls_thing_value_untk_idx ON itx_ls_thing_ls_thing_value USING btree (unit_type_and_kind);


--
-- Name: itx_protocol_protocol_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_protocol_protocol_ign_idx ON itx_protocol_protocol USING btree (ignored);


--
-- Name: itx_protocol_protocol_kind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_protocol_protocol_kind_idx ON itx_protocol_protocol USING btree (ls_kind);


--
-- Name: itx_protocol_protocol_state_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_protocol_protocol_state_ign_idx ON itx_protocol_protocol_state USING btree (ignored);


--
-- Name: itx_protocol_protocol_state_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_protocol_protocol_state_tk_idx ON itx_protocol_protocol_state USING btree (ls_type_and_kind);


--
-- Name: itx_protocol_protocol_state_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_protocol_protocol_state_trxn_idx ON itx_protocol_protocol_state USING btree (ls_transaction);


--
-- Name: itx_protocol_protocol_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_protocol_protocol_tk_idx ON itx_protocol_protocol USING btree (ls_type_and_kind);


--
-- Name: itx_protocol_protocol_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_protocol_protocol_trxn_idx ON itx_protocol_protocol USING btree (ls_transaction);


--
-- Name: itx_protocol_protocol_type_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_protocol_protocol_type_idx ON itx_protocol_protocol USING btree (ls_type);


--
-- Name: itx_protocol_protocol_value_cdkind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_protocol_protocol_value_cdkind_idx ON itx_protocol_protocol_value USING btree (code_kind);


--
-- Name: itx_protocol_protocol_value_cdorgn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_protocol_protocol_value_cdorgn_idx ON itx_protocol_protocol_value USING btree (code_origin);


--
-- Name: itx_protocol_protocol_value_cdtk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_protocol_protocol_value_cdtk_idx ON itx_protocol_protocol_value USING btree (code_type_and_kind);


--
-- Name: itx_protocol_protocol_value_cdtype_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_protocol_protocol_value_cdtype_idx ON itx_protocol_protocol_value USING btree (code_type);


--
-- Name: itx_protocol_protocol_value_code_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_protocol_protocol_value_code_idx ON itx_protocol_protocol_value USING btree (code_value);


--
-- Name: itx_protocol_protocol_value_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_protocol_protocol_value_ign_idx ON itx_protocol_protocol_value USING btree (ignored);


--
-- Name: itx_protocol_protocol_value_kind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_protocol_protocol_value_kind_idx ON itx_protocol_protocol_value USING btree (ls_kind);


--
-- Name: itx_protocol_protocol_value_optk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_protocol_protocol_value_optk_idx ON itx_protocol_protocol_value USING btree (operator_type_and_kind);


--
-- Name: itx_protocol_protocol_value_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_protocol_protocol_value_tk_idx ON itx_protocol_protocol_value USING btree (ls_type_and_kind);


--
-- Name: itx_protocol_protocol_value_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_protocol_protocol_value_trxn_idx ON itx_protocol_protocol_value USING btree (ls_transaction);


--
-- Name: itx_protocol_protocol_value_type_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_protocol_protocol_value_type_idx ON itx_protocol_protocol_value USING btree (ls_type);


--
-- Name: itx_protocol_protocol_value_untk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_protocol_protocol_value_untk_idx ON itx_protocol_protocol_value USING btree (unit_type_and_kind);


--
-- Name: itx_subject_container_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_subject_container_ign_idx ON itx_subject_container USING btree (ignored);


--
-- Name: itx_subject_container_kind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_subject_container_kind_idx ON itx_subject_container USING btree (ls_kind);


--
-- Name: itx_subject_container_state_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_subject_container_state_ign_idx ON itx_subject_container_state USING btree (ignored);


--
-- Name: itx_subject_container_state_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_subject_container_state_tk_idx ON itx_subject_container_state USING btree (ls_type_and_kind);


--
-- Name: itx_subject_container_state_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_subject_container_state_trxn_idx ON itx_subject_container_state USING btree (ls_transaction);


--
-- Name: itx_subject_container_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_subject_container_tk_idx ON itx_subject_container USING btree (ls_type_and_kind);


--
-- Name: itx_subject_container_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_subject_container_trxn_idx ON itx_subject_container USING btree (ls_transaction);


--
-- Name: itx_subject_container_type_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_subject_container_type_idx ON itx_subject_container USING btree (ls_type);


--
-- Name: itx_subject_container_value_cdkind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_subject_container_value_cdkind_idx ON itx_subject_container_value USING btree (code_kind);


--
-- Name: itx_subject_container_value_cdorgn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_subject_container_value_cdorgn_idx ON itx_subject_container_value USING btree (code_origin);


--
-- Name: itx_subject_container_value_cdtk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_subject_container_value_cdtk_idx ON itx_subject_container_value USING btree (code_type_and_kind);


--
-- Name: itx_subject_container_value_cdtype_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_subject_container_value_cdtype_idx ON itx_subject_container_value USING btree (code_type);


--
-- Name: itx_subject_container_value_code_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_subject_container_value_code_idx ON itx_subject_container_value USING btree (code_value);


--
-- Name: itx_subject_container_value_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_subject_container_value_ign_idx ON itx_subject_container_value USING btree (ignored);


--
-- Name: itx_subject_container_value_kind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_subject_container_value_kind_idx ON itx_subject_container_value USING btree (ls_kind);


--
-- Name: itx_subject_container_value_optk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_subject_container_value_optk_idx ON itx_subject_container_value USING btree (operator_type_and_kind);


--
-- Name: itx_subject_container_value_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_subject_container_value_tk_idx ON itx_subject_container_value USING btree (ls_type_and_kind);


--
-- Name: itx_subject_container_value_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_subject_container_value_trxn_idx ON itx_subject_container_value USING btree (ls_transaction);


--
-- Name: itx_subject_container_value_type_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_subject_container_value_type_idx ON itx_subject_container_value USING btree (ls_type);


--
-- Name: itx_subject_container_value_untk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX itx_subject_container_value_untk_idx ON itx_subject_container_value USING btree (unit_type_and_kind);


--
-- Name: ls_interaction_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX ls_interaction_ign_idx ON ls_interaction USING btree (ignored);


--
-- Name: ls_interaction_kind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX ls_interaction_kind_idx ON ls_interaction USING btree (ls_kind);


--
-- Name: ls_interaction_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX ls_interaction_tk_idx ON ls_interaction USING btree (ls_type_and_kind);


--
-- Name: ls_interaction_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX ls_interaction_trxn_idx ON ls_interaction USING btree (ls_transaction);


--
-- Name: ls_interaction_type_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX ls_interaction_type_idx ON ls_interaction USING btree (ls_type);


--
-- Name: ls_thing_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX ls_thing_ign_idx ON ls_thing USING btree (ignored);


--
-- Name: ls_thing_kind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX ls_thing_kind_idx ON ls_thing USING btree (ls_kind);


--
-- Name: ls_thing_label_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX ls_thing_label_ign_idx ON ls_thing_label USING btree (ignored);


--
-- Name: ls_thing_label_kind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX ls_thing_label_kind_idx ON ls_thing_label USING btree (ls_kind);


--
-- Name: ls_thing_label_pref_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX ls_thing_label_pref_idx ON ls_thing_label USING btree (preferred);


--
-- Name: ls_thing_label_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX ls_thing_label_tk_idx ON ls_thing_label USING btree (ls_type_and_kind);


--
-- Name: ls_thing_label_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX ls_thing_label_trxn_idx ON ls_thing_label USING btree (ls_transaction);


--
-- Name: ls_thing_label_type_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX ls_thing_label_type_idx ON ls_thing_label USING btree (ls_type);


--
-- Name: ls_thing_state_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX ls_thing_state_ign_idx ON ls_thing_state USING btree (ignored);


--
-- Name: ls_thing_state_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX ls_thing_state_tk_idx ON ls_thing_state USING btree (ls_type_and_kind);


--
-- Name: ls_thing_state_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX ls_thing_state_trxn_idx ON ls_thing_state USING btree (ls_transaction);


--
-- Name: ls_thing_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX ls_thing_tk_idx ON ls_thing USING btree (ls_type_and_kind);


--
-- Name: ls_thing_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX ls_thing_trxn_idx ON ls_thing USING btree (ls_transaction);


--
-- Name: ls_thing_type_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX ls_thing_type_idx ON ls_thing USING btree (ls_type);


--
-- Name: ls_thing_value_cdkind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX ls_thing_value_cdkind_idx ON ls_thing_value USING btree (code_kind);


--
-- Name: ls_thing_value_cdorgn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX ls_thing_value_cdorgn_idx ON ls_thing_value USING btree (code_origin);


--
-- Name: ls_thing_value_cdtk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX ls_thing_value_cdtk_idx ON ls_thing_value USING btree (code_type_and_kind);


--
-- Name: ls_thing_value_cdtype_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX ls_thing_value_cdtype_idx ON ls_thing_value USING btree (code_type);


--
-- Name: ls_thing_value_code_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX ls_thing_value_code_idx ON ls_thing_value USING btree (code_value);


--
-- Name: ls_thing_value_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX ls_thing_value_ign_idx ON ls_thing_value USING btree (ignored);


--
-- Name: ls_thing_value_kind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX ls_thing_value_kind_idx ON ls_thing_value USING btree (ls_kind);


--
-- Name: ls_thing_value_optk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX ls_thing_value_optk_idx ON ls_thing_value USING btree (operator_type_and_kind);


--
-- Name: ls_thing_value_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX ls_thing_value_tk_idx ON ls_thing_value USING btree (ls_type_and_kind);


--
-- Name: ls_thing_value_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX ls_thing_value_trxn_idx ON ls_thing_value USING btree (ls_transaction);


--
-- Name: ls_thing_value_type_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX ls_thing_value_type_idx ON ls_thing_value USING btree (ls_type);


--
-- Name: ls_thing_value_untk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX ls_thing_value_untk_idx ON ls_thing_value USING btree (unit_type_and_kind);


--
-- Name: protocol_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX protocol_ign_idx ON protocol USING btree (ignored);


--
-- Name: protocol_kind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX protocol_kind_idx ON protocol USING btree (ls_kind);


--
-- Name: protocol_label_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX protocol_label_ign_idx ON protocol_label USING btree (ignored);


--
-- Name: protocol_label_kind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX protocol_label_kind_idx ON protocol_label USING btree (ls_kind);


--
-- Name: protocol_label_pref_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX protocol_label_pref_idx ON protocol_label USING btree (preferred);


--
-- Name: protocol_label_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX protocol_label_tk_idx ON protocol_label USING btree (ls_type_and_kind);


--
-- Name: protocol_label_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX protocol_label_trxn_idx ON protocol_label USING btree (ls_transaction);


--
-- Name: protocol_label_type_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX protocol_label_type_idx ON protocol_label USING btree (ls_type);


--
-- Name: protocol_state_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX protocol_state_ign_idx ON protocol_state USING btree (ignored);


--
-- Name: protocol_state_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX protocol_state_tk_idx ON protocol_state USING btree (ls_type_and_kind);


--
-- Name: protocol_state_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX protocol_state_trxn_idx ON protocol_state USING btree (ls_transaction);


--
-- Name: protocol_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX protocol_tk_idx ON protocol USING btree (ls_type_and_kind);


--
-- Name: protocol_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX protocol_trxn_idx ON protocol USING btree (ls_transaction);


--
-- Name: protocol_type_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX protocol_type_idx ON protocol USING btree (ls_type);


--
-- Name: protocol_value_cdkind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX protocol_value_cdkind_idx ON protocol_value USING btree (code_kind);


--
-- Name: protocol_value_cdorgn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX protocol_value_cdorgn_idx ON protocol_value USING btree (code_origin);


--
-- Name: protocol_value_cdtk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX protocol_value_cdtk_idx ON protocol_value USING btree (code_type_and_kind);


--
-- Name: protocol_value_cdtype_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX protocol_value_cdtype_idx ON protocol_value USING btree (code_type);


--
-- Name: protocol_value_code_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX protocol_value_code_idx ON protocol_value USING btree (code_value);


--
-- Name: protocol_value_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX protocol_value_ign_idx ON protocol_value USING btree (ignored);


--
-- Name: protocol_value_kind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX protocol_value_kind_idx ON protocol_value USING btree (ls_kind);


--
-- Name: protocol_value_optk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX protocol_value_optk_idx ON protocol_value USING btree (operator_type_and_kind);


--
-- Name: protocol_value_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX protocol_value_tk_idx ON protocol_value USING btree (ls_type_and_kind);


--
-- Name: protocol_value_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX protocol_value_trxn_idx ON protocol_value USING btree (ls_transaction);


--
-- Name: protocol_value_type_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX protocol_value_type_idx ON protocol_value USING btree (ls_type);


--
-- Name: protocol_value_untk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX protocol_value_untk_idx ON protocol_value USING btree (unit_type_and_kind);


--
-- Name: subject_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX subject_ign_idx ON subject USING btree (ignored);


--
-- Name: subject_kind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX subject_kind_idx ON subject USING btree (ls_kind);


--
-- Name: subject_label_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX subject_label_ign_idx ON subject_label USING btree (ignored);


--
-- Name: subject_label_kind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX subject_label_kind_idx ON subject_label USING btree (ls_kind);


--
-- Name: subject_label_pref_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX subject_label_pref_idx ON subject_label USING btree (preferred);


--
-- Name: subject_label_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX subject_label_tk_idx ON subject_label USING btree (ls_type_and_kind);


--
-- Name: subject_label_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX subject_label_trxn_idx ON subject_label USING btree (ls_transaction);


--
-- Name: subject_label_type_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX subject_label_type_idx ON subject_label USING btree (ls_type);


--
-- Name: subject_state_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX subject_state_ign_idx ON subject_state USING btree (ignored);


--
-- Name: subject_state_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX subject_state_tk_idx ON subject_state USING btree (ls_type_and_kind);


--
-- Name: subject_state_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX subject_state_trxn_idx ON subject_state USING btree (ls_transaction);


--
-- Name: subject_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX subject_tk_idx ON subject USING btree (ls_type_and_kind);


--
-- Name: subject_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX subject_trxn_idx ON subject USING btree (ls_transaction);


--
-- Name: subject_type_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX subject_type_idx ON subject USING btree (ls_type);


--
-- Name: subject_value_cdkind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX subject_value_cdkind_idx ON subject_value USING btree (code_kind);


--
-- Name: subject_value_cdorgn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX subject_value_cdorgn_idx ON subject_value USING btree (code_origin);


--
-- Name: subject_value_cdtk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX subject_value_cdtk_idx ON subject_value USING btree (code_type_and_kind);


--
-- Name: subject_value_cdtype_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX subject_value_cdtype_idx ON subject_value USING btree (code_type);


--
-- Name: subject_value_code_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX subject_value_code_idx ON subject_value USING btree (code_value);


--
-- Name: subject_value_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX subject_value_ign_idx ON subject_value USING btree (ignored);


--
-- Name: subject_value_kind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX subject_value_kind_idx ON subject_value USING btree (ls_kind);


--
-- Name: subject_value_optk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX subject_value_optk_idx ON subject_value USING btree (operator_type_and_kind);


--
-- Name: subject_value_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX subject_value_tk_idx ON subject_value USING btree (ls_type_and_kind);


--
-- Name: subject_value_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX subject_value_trxn_idx ON subject_value USING btree (ls_transaction);


--
-- Name: subject_value_type_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX subject_value_type_idx ON subject_value USING btree (ls_type);


--
-- Name: subject_value_untk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX subject_value_untk_idx ON subject_value USING btree (unit_type_and_kind);


--
-- Name: treatment_group_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX treatment_group_ign_idx ON treatment_group USING btree (ignored);


--
-- Name: treatment_group_kind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX treatment_group_kind_idx ON treatment_group USING btree (ls_kind);


--
-- Name: treatment_group_label_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX treatment_group_label_ign_idx ON treatment_group_label USING btree (ignored);


--
-- Name: treatment_group_label_kind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX treatment_group_label_kind_idx ON treatment_group_label USING btree (ls_kind);


--
-- Name: treatment_group_label_pref_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX treatment_group_label_pref_idx ON treatment_group_label USING btree (preferred);


--
-- Name: treatment_group_label_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX treatment_group_label_tk_idx ON treatment_group_label USING btree (ls_type_and_kind);


--
-- Name: treatment_group_label_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX treatment_group_label_trxn_idx ON treatment_group_label USING btree (ls_transaction);


--
-- Name: treatment_group_label_type_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX treatment_group_label_type_idx ON treatment_group_label USING btree (ls_type);


--
-- Name: treatment_group_state_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX treatment_group_state_ign_idx ON treatment_group_state USING btree (ignored);


--
-- Name: treatment_group_state_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX treatment_group_state_tk_idx ON treatment_group_state USING btree (ls_type_and_kind);


--
-- Name: treatment_group_state_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX treatment_group_state_trxn_idx ON treatment_group_state USING btree (ls_transaction);


--
-- Name: treatment_group_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX treatment_group_tk_idx ON treatment_group USING btree (ls_type_and_kind);


--
-- Name: treatment_group_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX treatment_group_trxn_idx ON treatment_group USING btree (ls_transaction);


--
-- Name: treatment_group_type_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX treatment_group_type_idx ON treatment_group USING btree (ls_type);


--
-- Name: treatment_group_value_cdkind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX treatment_group_value_cdkind_idx ON treatment_group_value USING btree (code_kind);


--
-- Name: treatment_group_value_cdorgn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX treatment_group_value_cdorgn_idx ON treatment_group_value USING btree (code_origin);


--
-- Name: treatment_group_value_cdtk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX treatment_group_value_cdtk_idx ON treatment_group_value USING btree (code_type_and_kind);


--
-- Name: treatment_group_value_cdtype_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX treatment_group_value_cdtype_idx ON treatment_group_value USING btree (code_type);


--
-- Name: treatment_group_value_code_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX treatment_group_value_code_idx ON treatment_group_value USING btree (code_value);


--
-- Name: treatment_group_value_ign_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX treatment_group_value_ign_idx ON treatment_group_value USING btree (ignored);


--
-- Name: treatment_group_value_kind_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX treatment_group_value_kind_idx ON treatment_group_value USING btree (ls_kind);


--
-- Name: treatment_group_value_optk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX treatment_group_value_optk_idx ON treatment_group_value USING btree (operator_type_and_kind);


--
-- Name: treatment_group_value_tk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX treatment_group_value_tk_idx ON treatment_group_value USING btree (ls_type_and_kind);


--
-- Name: treatment_group_value_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX treatment_group_value_trxn_idx ON treatment_group_value USING btree (ls_transaction);


--
-- Name: treatment_group_value_type_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX treatment_group_value_type_idx ON treatment_group_value USING btree (ls_type);


--
-- Name: treatment_group_value_untk_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX treatment_group_value_untk_idx ON treatment_group_value USING btree (unit_type_and_kind);


--
-- Name: updatelog_thing_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX updatelog_thing_idx ON update_log USING btree (thing);


--
-- Name: updatelog_trxn_idx; Type: INDEX; Schema: acas; Owner: acas; Tablespace: 
--

CREATE INDEX updatelog_trxn_idx ON update_log USING btree (ls_transaction);


--
-- Name: fk1aa4a62166673913; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY subject_label ADD CONSTRAINT fk1aa4a62166673913 FOREIGN KEY (subject_id) REFERENCES subject(id);


--
-- Name: fk1b0febbe66673913; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY subject_state ADD CONSTRAINT fk1b0febbe66673913 FOREIGN KEY (subject_id) REFERENCES subject(id);


--
-- Name: fk1b31b89ec83958c8; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY subject_value ADD CONSTRAINT fk1b31b89ec83958c8 FOREIGN KEY (subject_state_id) REFERENCES subject_state(id);


--
-- Name: fk1b571e8db262bf01; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY protocol_label ADD CONSTRAINT fk1b571e8db262bf01 FOREIGN KEY (protocol_id) REFERENCES protocol(id);


--
-- Name: fk1bc2642ab262bf01; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY protocol_state ADD CONSTRAINT fk1bc2642ab262bf01 FOREIGN KEY (protocol_id) REFERENCES protocol(id);


--
-- Name: fk1be4310ace9fea42; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY protocol_value ADD CONSTRAINT fk1be4310ace9fea42 FOREIGN KEY (protocol_state_id) REFERENCES protocol_state(id);


--
-- Name: fk1c981f0d537768f9; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY itx_ls_thing_ls_thing ADD CONSTRAINT fk1c981f0d537768f9 FOREIGN KEY (second_ls_thing_id) REFERENCES ls_thing(id);


--
-- Name: fk1c981f0dff1b7d35; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY itx_ls_thing_ls_thing ADD CONSTRAINT fk1c981f0dff1b7d35 FOREIGN KEY (first_ls_thing_id) REFERENCES ls_thing(id);


--
-- Name: fk2a2bc4db58695ab; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY protocol_kind ADD CONSTRAINT fk2a2bc4db58695ab FOREIGN KEY (ls_type) REFERENCES protocol_type(id);


--
-- Name: fk2a8156aa3de5be1; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY author_role ADD CONSTRAINT fk2a8156aa3de5be1 FOREIGN KEY (author_id) REFERENCES author(id);


--
-- Name: fk2a8156aa479bd2c1; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY author_role ADD CONSTRAINT fk2a8156aa479bd2c1 FOREIGN KEY (lsrole_id) REFERENCES ls_role(id);


--
-- Name: fk2c9809b61b382ab3; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY container_label ADD CONSTRAINT fk2c9809b61b382ab3 FOREIGN KEY (container_id) REFERENCES container(id);


--
-- Name: fk2d034f531b382ab3; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY container_state ADD CONSTRAINT fk2d034f531b382ab3 FOREIGN KEY (container_id) REFERENCES container(id);


--
-- Name: fk2d251c337627111e; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY container_value ADD CONSTRAINT fk2d251c337627111e FOREIGN KEY (container_state_id) REFERENCES container_state(id);


--
-- Name: fk32fc5092d29e5e66; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY container_kind ADD CONSTRAINT fk32fc5092d29e5e66 FOREIGN KEY (ls_type) REFERENCES container_type(id);


--
-- Name: fk4736924ff5f249b7; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY operator_kind ADD CONSTRAINT fk4736924ff5f249b7 FOREIGN KEY (ls_type) REFERENCES operator_type(id);


--
-- Name: fk4c9e0c32dd459321; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY experiment_label ADD CONSTRAINT fk4c9e0c32dd459321 FOREIGN KEY (experiment_id) REFERENCES experiment(id);


--
-- Name: fk4d0951cfdd459321; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY experiment_state ADD CONSTRAINT fk4d0951cfdd459321 FOREIGN KEY (experiment_id) REFERENCES experiment(id);


--
-- Name: fk4d2b1eaf8cbb85f8; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY experiment_value ADD CONSTRAINT fk4d2b1eaf8cbb85f8 FOREIGN KEY (experiment_state_id) REFERENCES experiment_state(id);


--
-- Name: fk5080cbff6dc8946d; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY itx_expt_expt_state ADD CONSTRAINT fk5080cbff6dc8946d FOREIGN KEY (itx_experiment_experiment) REFERENCES itx_expt_expt(id);


--
-- Name: fk50a298dfb9a1634a; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY itx_expt_expt_value ADD CONSTRAINT fk50a298dfb9a1634a FOREIGN KEY (ls_state) REFERENCES itx_expt_expt_state(id);


--
-- Name: fk5312ad4d17e353b6; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY itx_protocol_protocol ADD CONSTRAINT fk5312ad4d17e353b6 FOREIGN KEY (second_protocol_id) REFERENCES protocol(id);


--
-- Name: fk5312ad4dc38767f2; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY itx_protocol_protocol ADD CONSTRAINT fk5312ad4dc38767f2 FOREIGN KEY (first_protocol_id) REFERENCES protocol(id);


--
-- Name: fk594892838b822f57; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY itx_container_container_state ADD CONSTRAINT fk594892838b822f57 FOREIGN KEY (itx_container_container) REFERENCES itx_container_container(id);


--
-- Name: fk596a5f6332d66464; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY itx_container_container_value ADD CONSTRAINT fk596a5f6332d66464 FOREIGN KEY (ls_state) REFERENCES itx_container_container_state(id);


--
-- Name: fk5eca21a1c0730d37; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY interaction_kind ADD CONSTRAINT fk5eca21a1c0730d37 FOREIGN KEY (ls_type) REFERENCES interaction_type(id);


--
-- Name: fk6280d5ab4d1ff13; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY ls_thing_label ADD CONSTRAINT fk6280d5ab4d1ff13 FOREIGN KEY (lsthing_id) REFERENCES ls_thing(id);


--
-- Name: fk62ec1b484d1ff13; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY ls_thing_state ADD CONSTRAINT fk62ec1b484d1ff13 FOREIGN KEY (lsthing_id) REFERENCES ls_thing(id);


--
-- Name: fk630de828f5ff5ef2; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY ls_thing_value ADD CONSTRAINT fk630de828f5ff5ef2 FOREIGN KEY (lsthing_state_id) REFERENCES ls_thing_state(id);


--
-- Name: fk63f187224d1ff13; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY lsthing_tag ADD CONSTRAINT fk63f187224d1ff13 FOREIGN KEY (lsthing_id) REFERENCES ls_thing(id);


--
-- Name: fk63f18722c95c958c; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY lsthing_tag ADD CONSTRAINT fk63f18722c95c958c FOREIGN KEY (tag_id) REFERENCES ls_tag(id);


--
-- Name: fk6583e71fa0ec62c3; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY itx_protocol_protocol_state ADD CONSTRAINT fk6583e71fa0ec62c3 FOREIGN KEY (itx_protocol_protocol) REFERENCES itx_protocol_protocol(id);


--
-- Name: fk65a5b3ff5c25cc94; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY itx_protocol_protocol_value ADD CONSTRAINT fk65a5b3ff5c25cc94 FOREIGN KEY (ls_state) REFERENCES itx_protocol_protocol_state(id);


--
-- Name: fk79c6a1a2a13b9fd6; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY value_kind ADD CONSTRAINT fk79c6a1a2a13b9fd6 FOREIGN KEY (ls_type) REFERENCES value_type(id);


--
-- Name: fk7a9e8458c95c958c; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY experiment_tag ADD CONSTRAINT fk7a9e8458c95c958c FOREIGN KEY (tag_id) REFERENCES ls_tag(id);


--
-- Name: fk7a9e8458dd459321; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY experiment_tag ADD CONSTRAINT fk7a9e8458dd459321 FOREIGN KEY (experiment_id) REFERENCES experiment(id);


--
-- Name: fk841d08df4fc6bab3; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY itx_ls_thing_ls_thing_state ADD CONSTRAINT fk841d08df4fc6bab3 FOREIGN KEY (itx_ls_thing_ls_thing) REFERENCES itx_ls_thing_ls_thing(id);


--
-- Name: fk843ed5bf77d94464; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY itx_ls_thing_ls_thing_value ADD CONSTRAINT fk843ed5bf77d94464 FOREIGN KEY (ls_state) REFERENCES itx_ls_thing_ls_thing_state(id);


--
-- Name: fk86fee4519951c9fa; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY analysis_group_label ADD CONSTRAINT fk86fee4519951c9fa FOREIGN KEY (analysis_group_id) REFERENCES analysis_group(id);


--
-- Name: fk876a29ee9951c9fa; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY analysis_group_state ADD CONSTRAINT fk876a29ee9951c9fa FOREIGN KEY (analysis_group_id) REFERENCES analysis_group(id);


--
-- Name: fk878bf6ce41555661; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY analysis_group_value ADD CONSTRAINT fk878bf6ce41555661 FOREIGN KEY (analysis_state_id) REFERENCES analysis_group_state(id);


--
-- Name: fk99e7173b262bf01; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY protocol_tag ADD CONSTRAINT fk99e7173b262bf01 FOREIGN KEY (protocol_id) REFERENCES protocol(id);


--
-- Name: fk99e7173c95c958c; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY protocol_tag ADD CONSTRAINT fk99e7173c95c958c FOREIGN KEY (tag_id) REFERENCES ls_tag(id);


--
-- Name: fk9daaadffaaa613d9; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY label_kind ADD CONSTRAINT fk9daaadffaaa613d9 FOREIGN KEY (ls_type) REFERENCES label_type(id);


--
-- Name: fkbccef2a6ece27e00; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY code_kind ADD CONSTRAINT fkbccef2a6ece27e00 FOREIGN KEY (ls_type) REFERENCES code_type(id);


--
-- Name: fkc3609fed2c65b2b0; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY treatment_group_label ADD CONSTRAINT fkc3609fed2c65b2b0 FOREIGN KEY (treatment_group_id) REFERENCES treatment_group(id);


--
-- Name: fkc3cbe58a2c65b2b0; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY treatment_group_state ADD CONSTRAINT fkc3cbe58a2c65b2b0 FOREIGN KEY (treatment_group_id) REFERENCES treatment_group(id);


--
-- Name: fkc3edb26ae33874b3; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY treatment_group_value ADD CONSTRAINT fkc3edb26ae33874b3 FOREIGN KEY (treatment_state_id) REFERENCES treatment_group_state(id);


--
-- Name: fkd2980df42c65b2b0; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY treatmentgroup_subject ADD CONSTRAINT fkd2980df42c65b2b0 FOREIGN KEY (treatment_group_id) REFERENCES treatment_group(id);


--
-- Name: fkd2980df466673913; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY treatmentgroup_subject ADD CONSTRAINT fkd2980df466673913 FOREIGN KEY (subject_id) REFERENCES subject(id);


--
-- Name: fkd3a7a61c1b382ab3; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY itx_subject_container ADD CONSTRAINT fkd3a7a61c1b382ab3 FOREIGN KEY (container_id) REFERENCES container(id);


--
-- Name: fkd3a7a61c66673913; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY itx_subject_container ADD CONSTRAINT fkd3a7a61c66673913 FOREIGN KEY (subject_id) REFERENCES subject(id);


--
-- Name: fkd92e0e96a50b92d0; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY experiment_kind ADD CONSTRAINT fkd92e0e96a50b92d0 FOREIGN KEY (ls_type) REFERENCES experiment_type(id);


--
-- Name: fkdf0bffa32c65b2b0; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY analysisgroup_treatmentgroup ADD CONSTRAINT fkdf0bffa32c65b2b0 FOREIGN KEY (treatment_group_id) REFERENCES treatment_group(id);


--
-- Name: fkdf0bffa39951c9fa; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY analysisgroup_treatmentgroup ADD CONSTRAINT fkdf0bffa39951c9fa FOREIGN KEY (analysis_group_id) REFERENCES analysis_group(id);


--
-- Name: fke354ba2d37e3c3d2; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY itx_expt_expt ADD CONSTRAINT fke354ba2d37e3c3d2 FOREIGN KEY (first_experiment_id) REFERENCES experiment(id);


--
-- Name: fke354ba2de4f3ce96; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY itx_expt_expt ADD CONSTRAINT fke354ba2de4f3ce96 FOREIGN KEY (second_experiment_id) REFERENCES experiment(id);


--
-- Name: fke3d56d6ff2d0617; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY unit_kind ADD CONSTRAINT fke3d56d6ff2d0617 FOREIGN KEY (ls_type) REFERENCES unit_type(id);


--
-- Name: fke4de9c619951c9fa; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY experiment_analysisgroup ADD CONSTRAINT fke4de9c619951c9fa FOREIGN KEY (analysis_group_id) REFERENCES analysis_group(id);


--
-- Name: fke4de9c61dd459321; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY experiment_analysisgroup ADD CONSTRAINT fke4de9c61dd459321 FOREIGN KEY (experiment_id) REFERENCES experiment(id);


--
-- Name: fkf26b6282528b42f6; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY state_kind ADD CONSTRAINT fkf26b6282528b42f6 FOREIGN KEY (ls_type) REFERENCES state_type(id);


--
-- Name: fkf504b3b12ea89fe2; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY itx_container_container ADD CONSTRAINT fkf504b3b12ea89fe2 FOREIGN KEY (first_container_id) REFERENCES container(id);


--
-- Name: fkf504b3b165ca2c9e; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY itx_container_container ADD CONSTRAINT fkf504b3b165ca2c9e FOREIGN KEY (second_container_id) REFERENCES container(id);


--
-- Name: fkf6a8c8ae8c161af7; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY itx_subject_container_state ADD CONSTRAINT fkf6a8c8ae8c161af7 FOREIGN KEY (itx_subject_container) REFERENCES itx_subject_container(id);


--
-- Name: fkf6ca958e809e44f; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY itx_subject_container_value ADD CONSTRAINT fkf6ca958e809e44f FOREIGN KEY (ls_state) REFERENCES itx_subject_container_state(id);


--
-- Name: fkf88d5445a5528cd3; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY thing_kind ADD CONSTRAINT fkf88d5445a5528cd3 FOREIGN KEY (ls_type) REFERENCES thing_type(id);


--
-- Name: fkf88f7b402aa4f16; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY thing_page ADD CONSTRAINT fkf88f7b402aa4f16 FOREIGN KEY (ls_transaction) REFERENCES ls_transaction(id);


--
-- Name: fkfae9dbfdb262bf01; Type: FK CONSTRAINT; Schema: acas; Owner: acas
--

ALTER TABLE ONLY experiment ADD CONSTRAINT fkfae9dbfdb262bf01 FOREIGN KEY (protocol_id) REFERENCES protocol(id);
