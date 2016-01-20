--------------------------------------------------------
--  DDL for Index ANALYSIS_GROUP_IGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ANALYSIS_GROUP_IGN_IDX" ON "ACAS"."ANALYSIS_GROUP" ("IGNORED");
--------------------------------------------------------
--  DDL for Index ANALYSIS_GROUP_KIND_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ANALYSIS_GROUP_KIND_IDX" ON "ACAS"."ANALYSIS_GROUP" ("LS_KIND");
--------------------------------------------------------
--  DDL for Index ANALYSIS_GROUP_LABEL_IGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ANALYSIS_GROUP_LABEL_IGN_IDX" ON "ACAS"."ANALYSIS_GROUP_LABEL" ("IGNORED");
--------------------------------------------------------
--  DDL for Index ANALYSIS_GROUP_LABEL_KIND_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ANALYSIS_GROUP_LABEL_KIND_IDX" ON "ACAS"."ANALYSIS_GROUP_LABEL" ("LS_KIND");
--------------------------------------------------------
--  DDL for Index ANALYSIS_GROUP_LABEL_PREF_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ANALYSIS_GROUP_LABEL_PREF_IDX" ON "ACAS"."ANALYSIS_GROUP_LABEL" ("PREFERRED");
--------------------------------------------------------
--  DDL for Index ANALYSIS_GROUP_LABEL_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ANALYSIS_GROUP_LABEL_TK_IDX" ON "ACAS"."ANALYSIS_GROUP_LABEL" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index ANALYSIS_GROUP_LABEL_TRXN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ANALYSIS_GROUP_LABEL_TRXN_IDX" ON "ACAS"."ANALYSIS_GROUP_LABEL" ("LS_TRANSACTION");
--------------------------------------------------------
--  DDL for Index ANALYSIS_GROUP_LABEL_TYPE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ANALYSIS_GROUP_LABEL_TYPE_IDX" ON "ACAS"."ANALYSIS_GROUP_LABEL" ("LS_TYPE");
--------------------------------------------------------
--  DDL for Index ANALYSIS_GROUP_STATE_IGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ANALYSIS_GROUP_STATE_IGN_IDX" ON "ACAS"."ANALYSIS_GROUP_STATE" ("IGNORED");
--------------------------------------------------------
--  DDL for Index ANALYSIS_GROUP_STATE_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ANALYSIS_GROUP_STATE_TK_IDX" ON "ACAS"."ANALYSIS_GROUP_STATE" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index ANALYSIS_GROUP_STATE_TRXN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ANALYSIS_GROUP_STATE_TRXN_IDX" ON "ACAS"."ANALYSIS_GROUP_STATE" ("LS_TRANSACTION");
--------------------------------------------------------
--  DDL for Index ANALYSIS_GROUP_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ANALYSIS_GROUP_TK_IDX" ON "ACAS"."ANALYSIS_GROUP" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index ANALYSIS_GROUP_TRXN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ANALYSIS_GROUP_TRXN_IDX" ON "ACAS"."ANALYSIS_GROUP" ("LS_TRANSACTION");
--------------------------------------------------------
--  DDL for Index ANALYSIS_GROUP_TYPE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ANALYSIS_GROUP_TYPE_IDX" ON "ACAS"."ANALYSIS_GROUP" ("LS_TYPE");
--------------------------------------------------------
--  DDL for Index ANALYSIS_GROUP_VALUE_CDTK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ANALYSIS_GROUP_VALUE_CDTK_IDX" ON "ACAS"."ANALYSIS_GROUP_VALUE" ("CODE_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index ANALYSIS_GROUP_VALUE_CODE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ANALYSIS_GROUP_VALUE_CODE_IDX" ON "ACAS"."ANALYSIS_GROUP_VALUE" ("CODE_VALUE");
--------------------------------------------------------
--  DDL for Index ANALYSIS_GROUP_VALUE_IGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ANALYSIS_GROUP_VALUE_IGN_IDX" ON "ACAS"."ANALYSIS_GROUP_VALUE" ("IGNORED");
--------------------------------------------------------
--  DDL for Index ANALYSIS_GROUP_VALUE_KIND_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ANALYSIS_GROUP_VALUE_KIND_IDX" ON "ACAS"."ANALYSIS_GROUP_VALUE" ("LS_KIND");
--------------------------------------------------------
--  DDL for Index ANALYSIS_GROUP_VALUE_OPTK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ANALYSIS_GROUP_VALUE_OPTK_IDX" ON "ACAS"."ANALYSIS_GROUP_VALUE" ("OPERATOR_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index ANALYSIS_GROUP_VALUE_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ANALYSIS_GROUP_VALUE_TK_IDX" ON "ACAS"."ANALYSIS_GROUP_VALUE" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index ANALYSIS_GROUP_VALUE_TRXN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ANALYSIS_GROUP_VALUE_TRXN_IDX" ON "ACAS"."ANALYSIS_GROUP_VALUE" ("LS_TRANSACTION");
--------------------------------------------------------
--  DDL for Index ANALYSIS_GROUP_VALUE_TYPE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ANALYSIS_GROUP_VALUE_TYPE_IDX" ON "ACAS"."ANALYSIS_GROUP_VALUE" ("LS_TYPE");
--------------------------------------------------------
--  DDL for Index ANALYSIS_GROUP_VALUE_UNTK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ANALYSIS_GROUP_VALUE_UNTK_IDX" ON "ACAS"."ANALYSIS_GROUP_VALUE" ("UNIT_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index CONTAINER_IGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."CONTAINER_IGN_IDX" ON "ACAS"."CONTAINER" ("IGNORED");
--------------------------------------------------------
--  DDL for Index CONTAINER_KIND_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."CONTAINER_KIND_IDX" ON "ACAS"."CONTAINER" ("LS_KIND");
--------------------------------------------------------
--  DDL for Index CONTAINER_LABEL_IGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."CONTAINER_LABEL_IGN_IDX" ON "ACAS"."CONTAINER_LABEL" ("IGNORED");
--------------------------------------------------------
--  DDL for Index CONTAINER_LABEL_KIND_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."CONTAINER_LABEL_KIND_IDX" ON "ACAS"."CONTAINER_LABEL" ("LS_KIND");
--------------------------------------------------------
--  DDL for Index CONTAINER_LABEL_PREF_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."CONTAINER_LABEL_PREF_IDX" ON "ACAS"."CONTAINER_LABEL" ("PREFERRED");
--------------------------------------------------------
--  DDL for Index CONTAINER_LABEL_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."CONTAINER_LABEL_TK_IDX" ON "ACAS"."CONTAINER_LABEL" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index CONTAINER_LABEL_TRXN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."CONTAINER_LABEL_TRXN_IDX" ON "ACAS"."CONTAINER_LABEL" ("LS_TRANSACTION");
--------------------------------------------------------
--  DDL for Index CONTAINER_LABEL_TYPE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."CONTAINER_LABEL_TYPE_IDX" ON "ACAS"."CONTAINER_LABEL" ("LS_TYPE");
--------------------------------------------------------
--  DDL for Index CONTAINER_STATE_IGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."CONTAINER_STATE_IGN_IDX" ON "ACAS"."CONTAINER_STATE" ("IGNORED");
--------------------------------------------------------
--  DDL for Index CONTAINER_STATE_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."CONTAINER_STATE_TK_IDX" ON "ACAS"."CONTAINER_STATE" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index CONTAINER_STATE_TRXN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."CONTAINER_STATE_TRXN_IDX" ON "ACAS"."CONTAINER_STATE" ("LS_TRANSACTION");
--------------------------------------------------------
--  DDL for Index CONTAINER_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."CONTAINER_TK_IDX" ON "ACAS"."CONTAINER" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index CONTAINER_TRXN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."CONTAINER_TRXN_IDX" ON "ACAS"."CONTAINER" ("LS_TRANSACTION");
--------------------------------------------------------
--  DDL for Index CONTAINER_TYPE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."CONTAINER_TYPE_IDX" ON "ACAS"."CONTAINER" ("LS_TYPE");
--------------------------------------------------------
--  DDL for Index CONTAINER_VALUE_CDKIND_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."CONTAINER_VALUE_CDKIND_IDX" ON "ACAS"."CONTAINER_VALUE" ("CODE_KIND");
--------------------------------------------------------
--  DDL for Index CONTAINER_VALUE_CDORGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."CONTAINER_VALUE_CDORGN_IDX" ON "ACAS"."CONTAINER_VALUE" ("CODE_ORIGIN");
--------------------------------------------------------
--  DDL for Index CONTAINER_VALUE_CDTK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."CONTAINER_VALUE_CDTK_IDX" ON "ACAS"."CONTAINER_VALUE" ("CODE_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index CONTAINER_VALUE_CDTYPE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."CONTAINER_VALUE_CDTYPE_IDX" ON "ACAS"."CONTAINER_VALUE" ("CODE_TYPE");
--------------------------------------------------------
--  DDL for Index CONTAINER_VALUE_CODE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."CONTAINER_VALUE_CODE_IDX" ON "ACAS"."CONTAINER_VALUE" ("CODE_VALUE");
--------------------------------------------------------
--  DDL for Index CONTAINER_VALUE_IGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."CONTAINER_VALUE_IGN_IDX" ON "ACAS"."CONTAINER_VALUE" ("IGNORED");
--------------------------------------------------------
--  DDL for Index CONTAINER_VALUE_KIND_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."CONTAINER_VALUE_KIND_IDX" ON "ACAS"."CONTAINER_VALUE" ("LS_KIND");
--------------------------------------------------------
--  DDL for Index CONTAINER_VALUE_OPTK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."CONTAINER_VALUE_OPTK_IDX" ON "ACAS"."CONTAINER_VALUE" ("OPERATOR_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index CONTAINER_VALUE_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."CONTAINER_VALUE_TK_IDX" ON "ACAS"."CONTAINER_VALUE" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index CONTAINER_VALUE_TRXN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."CONTAINER_VALUE_TRXN_IDX" ON "ACAS"."CONTAINER_VALUE" ("LS_TRANSACTION");
--------------------------------------------------------
--  DDL for Index CONTAINER_VALUE_TYPE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."CONTAINER_VALUE_TYPE_IDX" ON "ACAS"."CONTAINER_VALUE" ("LS_TYPE");
--------------------------------------------------------
--  DDL for Index CONTAINER_VALUE_UNTK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."CONTAINER_VALUE_UNTK_IDX" ON "ACAS"."CONTAINER_VALUE" ("UNIT_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index DD_VALUE_CODE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."DD_VALUE_CODE_IDX" ON "ACAS"."DDICT_VALUE" ("CODE_NAME");
--------------------------------------------------------
--  DDL for Index DD_VALUE_KIND_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."DD_VALUE_KIND_IDX" ON "ACAS"."DDICT_VALUE" ("LS_KIND");
--------------------------------------------------------
--  DDL for Index DD_VALUE_SNAME_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."DD_VALUE_SNAME_IDX" ON "ACAS"."DDICT_VALUE" ("SHORT_NAME");
--------------------------------------------------------
--  DDL for Index DD_VALUE_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."DD_VALUE_TK_IDX" ON "ACAS"."DDICT_VALUE" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index DD_VALUE_TYPE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."DD_VALUE_TYPE_IDX" ON "ACAS"."DDICT_VALUE" ("LS_TYPE");
--------------------------------------------------------
--  DDL for Index EXPERIMENT_IGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."EXPERIMENT_IGN_IDX" ON "ACAS"."EXPERIMENT" ("IGNORED");
--------------------------------------------------------
--  DDL for Index EXPERIMENT_KIND_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."EXPERIMENT_KIND_IDX" ON "ACAS"."EXPERIMENT" ("LS_KIND");
--------------------------------------------------------
--  DDL for Index EXPERIMENT_LABEL_IGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."EXPERIMENT_LABEL_IGN_IDX" ON "ACAS"."EXPERIMENT_LABEL" ("IGNORED");
--------------------------------------------------------
--  DDL for Index EXPERIMENT_LABEL_KIND_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."EXPERIMENT_LABEL_KIND_IDX" ON "ACAS"."EXPERIMENT_LABEL" ("LS_KIND");
--------------------------------------------------------
--  DDL for Index EXPERIMENT_LABEL_PREF_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."EXPERIMENT_LABEL_PREF_IDX" ON "ACAS"."EXPERIMENT_LABEL" ("PREFERRED");
--------------------------------------------------------
--  DDL for Index EXPERIMENT_LABEL_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."EXPERIMENT_LABEL_TK_IDX" ON "ACAS"."EXPERIMENT_LABEL" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index EXPERIMENT_LABEL_TRXN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."EXPERIMENT_LABEL_TRXN_IDX" ON "ACAS"."EXPERIMENT_LABEL" ("LS_TRANSACTION");
--------------------------------------------------------
--  DDL for Index EXPERIMENT_LABEL_TYPE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."EXPERIMENT_LABEL_TYPE_IDX" ON "ACAS"."EXPERIMENT_LABEL" ("LS_TYPE");
--------------------------------------------------------
--  DDL for Index EXPERIMENT_STATE_IGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."EXPERIMENT_STATE_IGN_IDX" ON "ACAS"."EXPERIMENT_STATE" ("IGNORED");
--------------------------------------------------------
--  DDL for Index EXPERIMENT_STATE_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."EXPERIMENT_STATE_TK_IDX" ON "ACAS"."EXPERIMENT_STATE" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index EXPERIMENT_STATE_TRXN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."EXPERIMENT_STATE_TRXN_IDX" ON "ACAS"."EXPERIMENT_STATE" ("LS_TRANSACTION");
--------------------------------------------------------
--  DDL for Index EXPERIMENT_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."EXPERIMENT_TK_IDX" ON "ACAS"."EXPERIMENT" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index EXPERIMENT_TRXN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."EXPERIMENT_TRXN_IDX" ON "ACAS"."EXPERIMENT" ("LS_TRANSACTION");
--------------------------------------------------------
--  DDL for Index EXPERIMENT_TYPE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."EXPERIMENT_TYPE_IDX" ON "ACAS"."EXPERIMENT" ("LS_TYPE");
--------------------------------------------------------
--  DDL for Index EXPERIMENT_VALUE_CDKIND_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."EXPERIMENT_VALUE_CDKIND_IDX" ON "ACAS"."EXPERIMENT_VALUE" ("CODE_KIND");
--------------------------------------------------------
--  DDL for Index EXPERIMENT_VALUE_CDORGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."EXPERIMENT_VALUE_CDORGN_IDX" ON "ACAS"."EXPERIMENT_VALUE" ("CODE_ORIGIN");
--------------------------------------------------------
--  DDL for Index EXPERIMENT_VALUE_CDTK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."EXPERIMENT_VALUE_CDTK_IDX" ON "ACAS"."EXPERIMENT_VALUE" ("CODE_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index EXPERIMENT_VALUE_CDTYPE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."EXPERIMENT_VALUE_CDTYPE_IDX" ON "ACAS"."EXPERIMENT_VALUE" ("CODE_TYPE");
--------------------------------------------------------
--  DDL for Index EXPERIMENT_VALUE_CODE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."EXPERIMENT_VALUE_CODE_IDX" ON "ACAS"."EXPERIMENT_VALUE" ("CODE_VALUE");
--------------------------------------------------------
--  DDL for Index EXPERIMENT_VALUE_IGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."EXPERIMENT_VALUE_IGN_IDX" ON "ACAS"."EXPERIMENT_VALUE" ("IGNORED");
--------------------------------------------------------
--  DDL for Index EXPERIMENT_VALUE_KIND_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."EXPERIMENT_VALUE_KIND_IDX" ON "ACAS"."EXPERIMENT_VALUE" ("LS_KIND");
--------------------------------------------------------
--  DDL for Index EXPERIMENT_VALUE_OPTK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."EXPERIMENT_VALUE_OPTK_IDX" ON "ACAS"."EXPERIMENT_VALUE" ("OPERATOR_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index EXPERIMENT_VALUE_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."EXPERIMENT_VALUE_TK_IDX" ON "ACAS"."EXPERIMENT_VALUE" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index EXPERIMENT_VALUE_TRXN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."EXPERIMENT_VALUE_TRXN_IDX" ON "ACAS"."EXPERIMENT_VALUE" ("LS_TRANSACTION");
--------------------------------------------------------
--  DDL for Index EXPERIMENT_VALUE_TYPE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."EXPERIMENT_VALUE_TYPE_IDX" ON "ACAS"."EXPERIMENT_VALUE" ("LS_TYPE");
--------------------------------------------------------
--  DDL for Index EXPERIMENT_VALUE_UNTK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."EXPERIMENT_VALUE_UNTK_IDX" ON "ACAS"."EXPERIMENT_VALUE" ("UNIT_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index FILE_THING_IGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."FILE_THING_IGN_IDX" ON "ACAS"."FILE_THING" ("IGNORED");
--------------------------------------------------------
--  DDL for Index FILE_THING_KIND_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."FILE_THING_KIND_IDX" ON "ACAS"."FILE_THING" ("LS_KIND");
--------------------------------------------------------
--  DDL for Index FILE_THING_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."FILE_THING_TK_IDX" ON "ACAS"."FILE_THING" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index FILE_THING_TRXN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."FILE_THING_TRXN_IDX" ON "ACAS"."FILE_THING" ("LS_TRANSACTION");
--------------------------------------------------------
--  DDL for Index FILE_THING_TYPE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."FILE_THING_TYPE_IDX" ON "ACAS"."FILE_THING" ("LS_TYPE");
--------------------------------------------------------
--  DDL for Index ITX_CONTAINER_CONTAINER_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ITX_CONTAINER_CONTAINER_TK_IDX" ON "ACAS"."ITX_CONTAINER_CONTAINER" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index ITX_EXPT_EXPT_IGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ITX_EXPT_EXPT_IGN_IDX" ON "ACAS"."ITX_EXPT_EXPT" ("IGNORED");
--------------------------------------------------------
--  DDL for Index ITX_EXPT_EXPT_KIND_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ITX_EXPT_EXPT_KIND_IDX" ON "ACAS"."ITX_EXPT_EXPT" ("LS_KIND");
--------------------------------------------------------
--  DDL for Index ITX_EXPT_EXPT_STATE_IGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ITX_EXPT_EXPT_STATE_IGN_IDX" ON "ACAS"."ITX_EXPT_EXPT_STATE" ("IGNORED");
--------------------------------------------------------
--  DDL for Index ITX_EXPT_EXPT_STATE_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ITX_EXPT_EXPT_STATE_TK_IDX" ON "ACAS"."ITX_EXPT_EXPT_STATE" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index ITX_EXPT_EXPT_STATE_TRXN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ITX_EXPT_EXPT_STATE_TRXN_IDX" ON "ACAS"."ITX_EXPT_EXPT_STATE" ("LS_TRANSACTION");
--------------------------------------------------------
--  DDL for Index ITX_EXPT_EXPT_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ITX_EXPT_EXPT_TK_IDX" ON "ACAS"."ITX_EXPT_EXPT" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index ITX_EXPT_EXPT_TRXN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ITX_EXPT_EXPT_TRXN_IDX" ON "ACAS"."ITX_EXPT_EXPT" ("LS_TRANSACTION");
--------------------------------------------------------
--  DDL for Index ITX_EXPT_EXPT_TYPE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ITX_EXPT_EXPT_TYPE_IDX" ON "ACAS"."ITX_EXPT_EXPT" ("LS_TYPE");
--------------------------------------------------------
--  DDL for Index ITX_EXPT_EXPT_VALUE_CDKIND_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ITX_EXPT_EXPT_VALUE_CDKIND_IDX" ON "ACAS"."ITX_EXPT_EXPT_VALUE" ("CODE_KIND");
--------------------------------------------------------
--  DDL for Index ITX_EXPT_EXPT_VALUE_CDORGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ITX_EXPT_EXPT_VALUE_CDORGN_IDX" ON "ACAS"."ITX_EXPT_EXPT_VALUE" ("CODE_ORIGIN");
--------------------------------------------------------
--  DDL for Index ITX_EXPT_EXPT_VALUE_CDTK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ITX_EXPT_EXPT_VALUE_CDTK_IDX" ON "ACAS"."ITX_EXPT_EXPT_VALUE" ("CODE_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index ITX_EXPT_EXPT_VALUE_CDTYPE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ITX_EXPT_EXPT_VALUE_CDTYPE_IDX" ON "ACAS"."ITX_EXPT_EXPT_VALUE" ("CODE_TYPE");
--------------------------------------------------------
--  DDL for Index ITX_EXPT_EXPT_VALUE_CODE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ITX_EXPT_EXPT_VALUE_CODE_IDX" ON "ACAS"."ITX_EXPT_EXPT_VALUE" ("CODE_VALUE");
--------------------------------------------------------
--  DDL for Index ITX_EXPT_EXPT_VALUE_IGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ITX_EXPT_EXPT_VALUE_IGN_IDX" ON "ACAS"."ITX_EXPT_EXPT_VALUE" ("IGNORED");
--------------------------------------------------------
--  DDL for Index ITX_EXPT_EXPT_VALUE_KIND_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ITX_EXPT_EXPT_VALUE_KIND_IDX" ON "ACAS"."ITX_EXPT_EXPT_VALUE" ("LS_KIND");
--------------------------------------------------------
--  DDL for Index ITX_EXPT_EXPT_VALUE_OPTK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ITX_EXPT_EXPT_VALUE_OPTK_IDX" ON "ACAS"."ITX_EXPT_EXPT_VALUE" ("OPERATOR_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index ITX_EXPT_EXPT_VALUE_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ITX_EXPT_EXPT_VALUE_TK_IDX" ON "ACAS"."ITX_EXPT_EXPT_VALUE" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index ITX_EXPT_EXPT_VALUE_TRXN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ITX_EXPT_EXPT_VALUE_TRXN_IDX" ON "ACAS"."ITX_EXPT_EXPT_VALUE" ("LS_TRANSACTION");
--------------------------------------------------------
--  DDL for Index ITX_EXPT_EXPT_VALUE_TYPE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ITX_EXPT_EXPT_VALUE_TYPE_IDX" ON "ACAS"."ITX_EXPT_EXPT_VALUE" ("LS_TYPE");
--------------------------------------------------------
--  DDL for Index ITX_EXPT_EXPT_VALUE_UNTK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ITX_EXPT_EXPT_VALUE_UNTK_IDX" ON "ACAS"."ITX_EXPT_EXPT_VALUE" ("UNIT_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index ITX_LS_THING_LS_THING_IGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ITX_LS_THING_LS_THING_IGN_IDX" ON "ACAS"."ITX_LS_THING_LS_THING" ("IGNORED");
--------------------------------------------------------
--  DDL for Index ITX_LS_THING_LS_THING_KIND_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ITX_LS_THING_LS_THING_KIND_IDX" ON "ACAS"."ITX_LS_THING_LS_THING" ("LS_KIND");
--------------------------------------------------------
--  DDL for Index ITX_LS_THING_LS_THING_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ITX_LS_THING_LS_THING_TK_IDX" ON "ACAS"."ITX_LS_THING_LS_THING" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index ITX_LS_THING_LS_THING_TRXN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ITX_LS_THING_LS_THING_TRXN_IDX" ON "ACAS"."ITX_LS_THING_LS_THING" ("LS_TRANSACTION");
--------------------------------------------------------
--  DDL for Index ITX_LS_THING_LS_THING_TYPE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ITX_LS_THING_LS_THING_TYPE_IDX" ON "ACAS"."ITX_LS_THING_LS_THING" ("LS_TYPE");
--------------------------------------------------------
--  DDL for Index ITX_PROTOCOL_PROTOCOL_IGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ITX_PROTOCOL_PROTOCOL_IGN_IDX" ON "ACAS"."ITX_PROTOCOL_PROTOCOL" ("IGNORED");
--------------------------------------------------------
--  DDL for Index ITX_PROTOCOL_PROTOCOL_KIND_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ITX_PROTOCOL_PROTOCOL_KIND_IDX" ON "ACAS"."ITX_PROTOCOL_PROTOCOL" ("LS_KIND");
--------------------------------------------------------
--  DDL for Index ITX_PROTOCOL_PROTOCOL_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ITX_PROTOCOL_PROTOCOL_TK_IDX" ON "ACAS"."ITX_PROTOCOL_PROTOCOL" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index ITX_PROTOCOL_PROTOCOL_TRXN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ITX_PROTOCOL_PROTOCOL_TRXN_IDX" ON "ACAS"."ITX_PROTOCOL_PROTOCOL" ("LS_TRANSACTION");
--------------------------------------------------------
--  DDL for Index ITX_PROTOCOL_PROTOCOL_TYPE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ITX_PROTOCOL_PROTOCOL_TYPE_IDX" ON "ACAS"."ITX_PROTOCOL_PROTOCOL" ("LS_TYPE");
--------------------------------------------------------
--  DDL for Index ITX_SUBJECT_CONTAINER_IGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ITX_SUBJECT_CONTAINER_IGN_IDX" ON "ACAS"."ITX_SUBJECT_CONTAINER" ("IGNORED");
--------------------------------------------------------
--  DDL for Index ITX_SUBJECT_CONTAINER_KIND_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ITX_SUBJECT_CONTAINER_KIND_IDX" ON "ACAS"."ITX_SUBJECT_CONTAINER" ("LS_KIND");
--------------------------------------------------------
--  DDL for Index ITX_SUBJECT_CONTAINER_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ITX_SUBJECT_CONTAINER_TK_IDX" ON "ACAS"."ITX_SUBJECT_CONTAINER" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index ITX_SUBJECT_CONTAINER_TRXN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ITX_SUBJECT_CONTAINER_TRXN_IDX" ON "ACAS"."ITX_SUBJECT_CONTAINER" ("LS_TRANSACTION");
--------------------------------------------------------
--  DDL for Index ITX_SUBJECT_CONTAINER_TYPE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."ITX_SUBJECT_CONTAINER_TYPE_IDX" ON "ACAS"."ITX_SUBJECT_CONTAINER" ("LS_TYPE");
--------------------------------------------------------
--  DDL for Index LS_INTERACTION_IGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."LS_INTERACTION_IGN_IDX" ON "ACAS"."LS_INTERACTION" ("IGNORED");
--------------------------------------------------------
--  DDL for Index LS_INTERACTION_KIND_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."LS_INTERACTION_KIND_IDX" ON "ACAS"."LS_INTERACTION" ("LS_KIND");
--------------------------------------------------------
--  DDL for Index LS_INTERACTION_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."LS_INTERACTION_TK_IDX" ON "ACAS"."LS_INTERACTION" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index LS_INTERACTION_TRXN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."LS_INTERACTION_TRXN_IDX" ON "ACAS"."LS_INTERACTION" ("LS_TRANSACTION");
--------------------------------------------------------
--  DDL for Index LS_INTERACTION_TYPE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."LS_INTERACTION_TYPE_IDX" ON "ACAS"."LS_INTERACTION" ("LS_TYPE");
--------------------------------------------------------
--  DDL for Index LS_THING_IGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."LS_THING_IGN_IDX" ON "ACAS"."LS_THING" ("IGNORED");
--------------------------------------------------------
--  DDL for Index LS_THING_KIND_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."LS_THING_KIND_IDX" ON "ACAS"."LS_THING" ("LS_KIND");
--------------------------------------------------------
--  DDL for Index LS_THING_LABEL_IGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."LS_THING_LABEL_IGN_IDX" ON "ACAS"."LS_THING_LABEL" ("IGNORED");
--------------------------------------------------------
--  DDL for Index LS_THING_LABEL_KIND_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."LS_THING_LABEL_KIND_IDX" ON "ACAS"."LS_THING_LABEL" ("LS_KIND");
--------------------------------------------------------
--  DDL for Index LS_THING_LABEL_PREF_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."LS_THING_LABEL_PREF_IDX" ON "ACAS"."LS_THING_LABEL" ("PREFERRED");
--------------------------------------------------------
--  DDL for Index LS_THING_LABEL_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."LS_THING_LABEL_TK_IDX" ON "ACAS"."LS_THING_LABEL" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index LS_THING_LABEL_TRXN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."LS_THING_LABEL_TRXN_IDX" ON "ACAS"."LS_THING_LABEL" ("LS_TRANSACTION");
--------------------------------------------------------
--  DDL for Index LS_THING_LABEL_TYPE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."LS_THING_LABEL_TYPE_IDX" ON "ACAS"."LS_THING_LABEL" ("LS_TYPE");
--------------------------------------------------------
--  DDL for Index LS_THING_STATE_IGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."LS_THING_STATE_IGN_IDX" ON "ACAS"."LS_THING_STATE" ("IGNORED");
--------------------------------------------------------
--  DDL for Index LS_THING_STATE_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."LS_THING_STATE_TK_IDX" ON "ACAS"."LS_THING_STATE" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index LS_THING_STATE_TRXN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."LS_THING_STATE_TRXN_IDX" ON "ACAS"."LS_THING_STATE" ("LS_TRANSACTION");
--------------------------------------------------------
--  DDL for Index LS_THING_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."LS_THING_TK_IDX" ON "ACAS"."LS_THING" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index LS_THING_TRXN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."LS_THING_TRXN_IDX" ON "ACAS"."LS_THING" ("LS_TRANSACTION");
--------------------------------------------------------
--  DDL for Index LS_THING_TYPE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."LS_THING_TYPE_IDX" ON "ACAS"."LS_THING" ("LS_TYPE");
--------------------------------------------------------
--  DDL for Index LS_THING_VALUE_CDKIND_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."LS_THING_VALUE_CDKIND_IDX" ON "ACAS"."LS_THING_VALUE" ("CODE_KIND");
--------------------------------------------------------
--  DDL for Index LS_THING_VALUE_CDORGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."LS_THING_VALUE_CDORGN_IDX" ON "ACAS"."LS_THING_VALUE" ("CODE_ORIGIN");
--------------------------------------------------------
--  DDL for Index LS_THING_VALUE_CDTK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."LS_THING_VALUE_CDTK_IDX" ON "ACAS"."LS_THING_VALUE" ("CODE_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index LS_THING_VALUE_CDTYPE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."LS_THING_VALUE_CDTYPE_IDX" ON "ACAS"."LS_THING_VALUE" ("CODE_TYPE");
--------------------------------------------------------
--  DDL for Index LS_THING_VALUE_CODE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."LS_THING_VALUE_CODE_IDX" ON "ACAS"."LS_THING_VALUE" ("CODE_VALUE");
--------------------------------------------------------
--  DDL for Index LS_THING_VALUE_IGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."LS_THING_VALUE_IGN_IDX" ON "ACAS"."LS_THING_VALUE" ("IGNORED");
--------------------------------------------------------
--  DDL for Index LS_THING_VALUE_KIND_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."LS_THING_VALUE_KIND_IDX" ON "ACAS"."LS_THING_VALUE" ("LS_KIND");
--------------------------------------------------------
--  DDL for Index LS_THING_VALUE_OPTK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."LS_THING_VALUE_OPTK_IDX" ON "ACAS"."LS_THING_VALUE" ("OPERATOR_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index LS_THING_VALUE_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."LS_THING_VALUE_TK_IDX" ON "ACAS"."LS_THING_VALUE" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index LS_THING_VALUE_TRXN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."LS_THING_VALUE_TRXN_IDX" ON "ACAS"."LS_THING_VALUE" ("LS_TRANSACTION");
--------------------------------------------------------
--  DDL for Index LS_THING_VALUE_TYPE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."LS_THING_VALUE_TYPE_IDX" ON "ACAS"."LS_THING_VALUE" ("LS_TYPE");
--------------------------------------------------------
--  DDL for Index LS_THING_VALUE_UNTK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."LS_THING_VALUE_UNTK_IDX" ON "ACAS"."LS_THING_VALUE" ("UNIT_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index PROTOCOL_IGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."PROTOCOL_IGN_IDX" ON "ACAS"."PROTOCOL" ("IGNORED");
--------------------------------------------------------
--  DDL for Index PROTOCOL_KIND_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."PROTOCOL_KIND_IDX" ON "ACAS"."PROTOCOL" ("LS_KIND");
--------------------------------------------------------
--  DDL for Index PROTOCOL_LABEL_IGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."PROTOCOL_LABEL_IGN_IDX" ON "ACAS"."PROTOCOL_LABEL" ("IGNORED");
--------------------------------------------------------
--  DDL for Index PROTOCOL_LABEL_KIND_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."PROTOCOL_LABEL_KIND_IDX" ON "ACAS"."PROTOCOL_LABEL" ("LS_KIND");
--------------------------------------------------------
--  DDL for Index PROTOCOL_LABEL_PREF_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."PROTOCOL_LABEL_PREF_IDX" ON "ACAS"."PROTOCOL_LABEL" ("PREFERRED");
--------------------------------------------------------
--  DDL for Index PROTOCOL_LABEL_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."PROTOCOL_LABEL_TK_IDX" ON "ACAS"."PROTOCOL_LABEL" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index PROTOCOL_LABEL_TRXN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."PROTOCOL_LABEL_TRXN_IDX" ON "ACAS"."PROTOCOL_LABEL" ("LS_TRANSACTION");
--------------------------------------------------------
--  DDL for Index PROTOCOL_LABEL_TYPE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."PROTOCOL_LABEL_TYPE_IDX" ON "ACAS"."PROTOCOL_LABEL" ("LS_TYPE");
--------------------------------------------------------
--  DDL for Index PROTOCOL_STATE_IGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."PROTOCOL_STATE_IGN_IDX" ON "ACAS"."PROTOCOL_STATE" ("IGNORED");
--------------------------------------------------------
--  DDL for Index PROTOCOL_STATE_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."PROTOCOL_STATE_TK_IDX" ON "ACAS"."PROTOCOL_STATE" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index PROTOCOL_STATE_TRXN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."PROTOCOL_STATE_TRXN_IDX" ON "ACAS"."PROTOCOL_STATE" ("LS_TRANSACTION");
--------------------------------------------------------
--  DDL for Index PROTOCOL_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."PROTOCOL_TK_IDX" ON "ACAS"."PROTOCOL" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index PROTOCOL_TRXN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."PROTOCOL_TRXN_IDX" ON "ACAS"."PROTOCOL" ("LS_TRANSACTION");
--------------------------------------------------------
--  DDL for Index PROTOCOL_TYPE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."PROTOCOL_TYPE_IDX" ON "ACAS"."PROTOCOL" ("LS_TYPE");
--------------------------------------------------------
--  DDL for Index PROTOCOL_VALUE_CDKIND_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."PROTOCOL_VALUE_CDKIND_IDX" ON "ACAS"."PROTOCOL_VALUE" ("CODE_KIND");
--------------------------------------------------------
--  DDL for Index PROTOCOL_VALUE_CDORGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."PROTOCOL_VALUE_CDORGN_IDX" ON "ACAS"."PROTOCOL_VALUE" ("CODE_ORIGIN");
--------------------------------------------------------
--  DDL for Index PROTOCOL_VALUE_CDTK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."PROTOCOL_VALUE_CDTK_IDX" ON "ACAS"."PROTOCOL_VALUE" ("CODE_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index PROTOCOL_VALUE_CDTYPE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."PROTOCOL_VALUE_CDTYPE_IDX" ON "ACAS"."PROTOCOL_VALUE" ("CODE_TYPE");
--------------------------------------------------------
--  DDL for Index PROTOCOL_VALUE_CODE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."PROTOCOL_VALUE_CODE_IDX" ON "ACAS"."PROTOCOL_VALUE" ("CODE_VALUE");
--------------------------------------------------------
--  DDL for Index PROTOCOL_VALUE_IGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."PROTOCOL_VALUE_IGN_IDX" ON "ACAS"."PROTOCOL_VALUE" ("IGNORED");
--------------------------------------------------------
--  DDL for Index PROTOCOL_VALUE_KIND_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."PROTOCOL_VALUE_KIND_IDX" ON "ACAS"."PROTOCOL_VALUE" ("LS_KIND");
--------------------------------------------------------
--  DDL for Index PROTOCOL_VALUE_OPTK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."PROTOCOL_VALUE_OPTK_IDX" ON "ACAS"."PROTOCOL_VALUE" ("OPERATOR_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index PROTOCOL_VALUE_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."PROTOCOL_VALUE_TK_IDX" ON "ACAS"."PROTOCOL_VALUE" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index PROTOCOL_VALUE_TRXN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."PROTOCOL_VALUE_TRXN_IDX" ON "ACAS"."PROTOCOL_VALUE" ("LS_TRANSACTION");
--------------------------------------------------------
--  DDL for Index PROTOCOL_VALUE_TYPE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."PROTOCOL_VALUE_TYPE_IDX" ON "ACAS"."PROTOCOL_VALUE" ("LS_TYPE");
--------------------------------------------------------
--  DDL for Index PROTOCOL_VALUE_UNTK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."PROTOCOL_VALUE_UNTK_IDX" ON "ACAS"."PROTOCOL_VALUE" ("UNIT_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index SUBJECT_IGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."SUBJECT_IGN_IDX" ON "ACAS"."SUBJECT" ("IGNORED");
--------------------------------------------------------
--  DDL for Index SUBJECT_KIND_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."SUBJECT_KIND_IDX" ON "ACAS"."SUBJECT" ("LS_KIND");
--------------------------------------------------------
--  DDL for Index SUBJECT_LABEL_IGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."SUBJECT_LABEL_IGN_IDX" ON "ACAS"."SUBJECT_LABEL" ("IGNORED");
--------------------------------------------------------
--  DDL for Index SUBJECT_LABEL_KIND_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."SUBJECT_LABEL_KIND_IDX" ON "ACAS"."SUBJECT_LABEL" ("LS_KIND");
--------------------------------------------------------
--  DDL for Index SUBJECT_LABEL_PREF_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."SUBJECT_LABEL_PREF_IDX" ON "ACAS"."SUBJECT_LABEL" ("PREFERRED");
--------------------------------------------------------
--  DDL for Index SUBJECT_LABEL_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."SUBJECT_LABEL_TK_IDX" ON "ACAS"."SUBJECT_LABEL" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index SUBJECT_LABEL_TRXN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."SUBJECT_LABEL_TRXN_IDX" ON "ACAS"."SUBJECT_LABEL" ("LS_TRANSACTION");
--------------------------------------------------------
--  DDL for Index SUBJECT_LABEL_TYPE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."SUBJECT_LABEL_TYPE_IDX" ON "ACAS"."SUBJECT_LABEL" ("LS_TYPE");
--------------------------------------------------------
--  DDL for Index SUBJECT_STATE_IGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."SUBJECT_STATE_IGN_IDX" ON "ACAS"."SUBJECT_STATE" ("IGNORED");
--------------------------------------------------------
--  DDL for Index SUBJECT_STATE_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."SUBJECT_STATE_TK_IDX" ON "ACAS"."SUBJECT_STATE" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index SUBJECT_STATE_TRXN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."SUBJECT_STATE_TRXN_IDX" ON "ACAS"."SUBJECT_STATE" ("LS_TRANSACTION");
--------------------------------------------------------
--  DDL for Index SUBJECT_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."SUBJECT_TK_IDX" ON "ACAS"."SUBJECT" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index SUBJECT_TRXN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."SUBJECT_TRXN_IDX" ON "ACAS"."SUBJECT" ("LS_TRANSACTION");
--------------------------------------------------------
--  DDL for Index SUBJECT_TYPE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."SUBJECT_TYPE_IDX" ON "ACAS"."SUBJECT" ("LS_TYPE");
--------------------------------------------------------
--  DDL for Index SUBJECT_VALUE_CDKIND_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."SUBJECT_VALUE_CDKIND_IDX" ON "ACAS"."SUBJECT_VALUE" ("CODE_KIND");
--------------------------------------------------------
--  DDL for Index SUBJECT_VALUE_CDORGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."SUBJECT_VALUE_CDORGN_IDX" ON "ACAS"."SUBJECT_VALUE" ("CODE_ORIGIN");
--------------------------------------------------------
--  DDL for Index SUBJECT_VALUE_CDTK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."SUBJECT_VALUE_CDTK_IDX" ON "ACAS"."SUBJECT_VALUE" ("CODE_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index SUBJECT_VALUE_CDTYPE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."SUBJECT_VALUE_CDTYPE_IDX" ON "ACAS"."SUBJECT_VALUE" ("CODE_TYPE");
--------------------------------------------------------
--  DDL for Index SUBJECT_VALUE_CODE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."SUBJECT_VALUE_CODE_IDX" ON "ACAS"."SUBJECT_VALUE" ("CODE_VALUE");
--------------------------------------------------------
--  DDL for Index SUBJECT_VALUE_IGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."SUBJECT_VALUE_IGN_IDX" ON "ACAS"."SUBJECT_VALUE" ("IGNORED");
--------------------------------------------------------
--  DDL for Index SUBJECT_VALUE_KIND_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."SUBJECT_VALUE_KIND_IDX" ON "ACAS"."SUBJECT_VALUE" ("LS_KIND");
--------------------------------------------------------
--  DDL for Index SUBJECT_VALUE_OPTK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."SUBJECT_VALUE_OPTK_IDX" ON "ACAS"."SUBJECT_VALUE" ("OPERATOR_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index SUBJECT_VALUE_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."SUBJECT_VALUE_TK_IDX" ON "ACAS"."SUBJECT_VALUE" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index SUBJECT_VALUE_TRXN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."SUBJECT_VALUE_TRXN_IDX" ON "ACAS"."SUBJECT_VALUE" ("LS_TRANSACTION");
--------------------------------------------------------
--  DDL for Index SUBJECT_VALUE_TYPE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."SUBJECT_VALUE_TYPE_IDX" ON "ACAS"."SUBJECT_VALUE" ("LS_TYPE");
--------------------------------------------------------
--  DDL for Index SUBJECT_VALUE_UNTK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."SUBJECT_VALUE_UNTK_IDX" ON "ACAS"."SUBJECT_VALUE" ("UNIT_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index SYS_C006994
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C006994" ON "ACAS"."ANALYSIS_GROUP" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C006995
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C006995" ON "ACAS"."ANALYSIS_GROUP" ("CODE_NAME");
--------------------------------------------------------
--  DDL for Index SYS_C007007
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007007" ON "ACAS"."ANALYSIS_GROUP_LABEL" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007016
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007016" ON "ACAS"."ANALYSIS_GROUP_STATE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007026
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007026" ON "ACAS"."ANALYSIS_GROUP_VALUE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007029
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007029" ON "ACAS"."ANALYSISGROUP_TREATMENTGROUP" ("TREATMENT_GROUP_ID", "ANALYSIS_GROUP_ID");
--------------------------------------------------------
--  DDL for Index SYS_C007032
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007032" ON "ACAS"."APPLICATION_SETTING" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007039
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007039" ON "ACAS"."AUTHOR" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007040
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007040" ON "ACAS"."AUTHOR" ("EMAIL_ADDRESS");
--------------------------------------------------------
--  DDL for Index SYS_C007041
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007041" ON "ACAS"."AUTHOR" ("USER_NAME");
--------------------------------------------------------
--  DDL for Index SYS_C007045
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007045" ON "ACAS"."AUTHOR_ROLE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007046
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007046" ON "ACAS"."AUTHOR_ROLE" ("AUTHOR_ID", "LSROLE_ID");
--------------------------------------------------------
--  DDL for Index SYS_C007050
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007050" ON "ACAS"."CODE_KIND" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007051
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007051" ON "ACAS"."CODE_KIND" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index SYS_C007054
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007054" ON "ACAS"."CODE_ORIGIN" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007055
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007055" ON "ACAS"."CODE_ORIGIN" ("NAME");
--------------------------------------------------------
--  DDL for Index SYS_C007058
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007058" ON "ACAS"."CODE_TYPE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007059
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007059" ON "ACAS"."CODE_TYPE" ("TYPE_NAME");
--------------------------------------------------------
--  DDL for Index SYS_C007067
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007067" ON "ACAS"."CONTAINER" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007068
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007068" ON "ACAS"."CONTAINER" ("CODE_NAME");
--------------------------------------------------------
--  DDL for Index SYS_C007072
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007072" ON "ACAS"."CONTAINER_KIND" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007073
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007073" ON "ACAS"."CONTAINER_KIND" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index SYS_C007085
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007085" ON "ACAS"."CONTAINER_LABEL" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007094
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007094" ON "ACAS"."CONTAINER_STATE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007097
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007097" ON "ACAS"."CONTAINER_TYPE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007098
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007098" ON "ACAS"."CONTAINER_TYPE" ("TYPE_NAME");
--------------------------------------------------------
--  DDL for Index SYS_C007108
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007108" ON "ACAS"."CONTAINER_VALUE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007112
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007112" ON "ACAS"."DDICT_KIND" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007113
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007113" ON "ACAS"."DDICT_KIND" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index SYS_C007117
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007117" ON "ACAS"."DDICT_TYPE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007118
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007118" ON "ACAS"."DDICT_TYPE" ("NAME");
--------------------------------------------------------
--  DDL for Index SYS_C007125
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007125" ON "ACAS"."DDICT_VALUE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007134
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007134" ON "ACAS"."EXPERIMENT" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007135
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007135" ON "ACAS"."EXPERIMENT" ("CODE_NAME");
--------------------------------------------------------
--  DDL for Index SYS_C007138
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007138" ON "ACAS"."EXPERIMENT_ANALYSISGROUP" ("ANALYSIS_GROUP_ID", "EXPERIMENT_ID");
--------------------------------------------------------
--  DDL for Index SYS_C007142
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007142" ON "ACAS"."EXPERIMENT_KIND" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007143
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007143" ON "ACAS"."EXPERIMENT_KIND" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index SYS_C007155
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007155" ON "ACAS"."EXPERIMENT_LABEL" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007164
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007164" ON "ACAS"."EXPERIMENT_STATE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007167
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007167" ON "ACAS"."EXPERIMENT_TAG" ("EXPERIMENT_ID", "TAG_ID");
--------------------------------------------------------
--  DDL for Index SYS_C007170
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007170" ON "ACAS"."EXPERIMENT_TYPE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007171
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007171" ON "ACAS"."EXPERIMENT_TYPE" ("TYPE_NAME");
--------------------------------------------------------
--  DDL for Index SYS_C007181
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007181" ON "ACAS"."EXPERIMENT_VALUE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007189
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007189" ON "ACAS"."FILE_THING" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007190
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007190" ON "ACAS"."FILE_THING" ("CODE_NAME");
--------------------------------------------------------
--  DDL for Index SYS_C007194
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007194" ON "ACAS"."INTERACTION_KIND" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007195
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007195" ON "ACAS"."INTERACTION_KIND" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index SYS_C007199
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007199" ON "ACAS"."INTERACTION_TYPE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007200
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007200" ON "ACAS"."INTERACTION_TYPE" ("TYPE_NAME");
--------------------------------------------------------
--  DDL for Index SYS_C007210
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007210" ON "ACAS"."ITX_CONTAINER_CONTAINER" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007211
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007211" ON "ACAS"."ITX_CONTAINER_CONTAINER" ("CODE_NAME");
--------------------------------------------------------
--  DDL for Index SYS_C007219
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007219" ON "ACAS"."ITX_CONTAINER_CONTAINER_STATE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007228
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007228" ON "ACAS"."ITX_CONTAINER_CONTAINER_VALUE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007238
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007238" ON "ACAS"."ITX_EXPT_EXPT" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007239
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007239" ON "ACAS"."ITX_EXPT_EXPT" ("CODE_NAME");
--------------------------------------------------------
--  DDL for Index SYS_C007247
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007247" ON "ACAS"."ITX_EXPT_EXPT_STATE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007256
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007256" ON "ACAS"."ITX_EXPT_EXPT_VALUE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007266
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007266" ON "ACAS"."ITX_LS_THING_LS_THING" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007267
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007267" ON "ACAS"."ITX_LS_THING_LS_THING" ("CODE_NAME");
--------------------------------------------------------
--  DDL for Index SYS_C007275
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007275" ON "ACAS"."ITX_LS_THING_LS_THING_STATE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007284
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007284" ON "ACAS"."ITX_LS_THING_LS_THING_VALUE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007294
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007294" ON "ACAS"."ITX_PROTOCOL_PROTOCOL" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007295
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007295" ON "ACAS"."ITX_PROTOCOL_PROTOCOL" ("CODE_NAME");
--------------------------------------------------------
--  DDL for Index SYS_C007303
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007303" ON "ACAS"."ITX_PROTOCOL_PROTOCOL_STATE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007312
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007312" ON "ACAS"."ITX_PROTOCOL_PROTOCOL_VALUE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007322
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007322" ON "ACAS"."ITX_SUBJECT_CONTAINER" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007323
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007323" ON "ACAS"."ITX_SUBJECT_CONTAINER" ("CODE_NAME");
--------------------------------------------------------
--  DDL for Index SYS_C007331
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007331" ON "ACAS"."ITX_SUBJECT_CONTAINER_STATE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007340
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007340" ON "ACAS"."ITX_SUBJECT_CONTAINER_VALUE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007343
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007343" ON "ACAS"."LABEL_KIND" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007344
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007344" ON "ACAS"."LABEL_KIND" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index SYS_C007352
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007352" ON "ACAS"."LABEL_SEQUENCE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007355
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007355" ON "ACAS"."LABEL_TYPE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007356
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007356" ON "ACAS"."LABEL_TYPE" ("TYPE_NAME");
--------------------------------------------------------
--  DDL for Index SYS_C007366
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007366" ON "ACAS"."LS_INTERACTION" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007367
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007367" ON "ACAS"."LS_INTERACTION" ("CODE_NAME");
--------------------------------------------------------
--  DDL for Index SYS_C007370
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007370" ON "ACAS"."LS_ROLE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007371
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007371" ON "ACAS"."LS_ROLE" ("ROLE_NAME");
--------------------------------------------------------
--  DDL for Index SYS_C007373
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007373" ON "ACAS"."LS_SEQ_ANL_GRP" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007375
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007375" ON "ACAS"."LS_SEQ_CONTAINER" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007377
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007377" ON "ACAS"."LS_SEQ_EXPT" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007379
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007379" ON "ACAS"."LS_SEQ_ITX_CNTR_CNTR" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007381
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007381" ON "ACAS"."LS_SEQ_ITX_PROTOCOL_PROTOCOL" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007383
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007383" ON "ACAS"."LS_SEQ_ITX_SUBJ_CNTR" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007385
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007385" ON "ACAS"."LS_SEQ_PROTOCOL" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007387
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007387" ON "ACAS"."LS_SEQ_SUBJECT" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007389
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007389" ON "ACAS"."LS_SEQ_TRT_GRP" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007391
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007391" ON "ACAS"."LS_TAG" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007399
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007399" ON "ACAS"."LS_THING" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007400
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007400" ON "ACAS"."LS_THING" ("CODE_NAME");
--------------------------------------------------------
--  DDL for Index SYS_C007412
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007412" ON "ACAS"."LS_THING_LABEL" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007421
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007421" ON "ACAS"."LS_THING_STATE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007431
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007431" ON "ACAS"."LS_THING_VALUE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007434
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007434" ON "ACAS"."LS_TRANSACTION" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007437
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007437" ON "ACAS"."LSTHING_TAG" ("LSTHING_ID", "TAG_ID");
--------------------------------------------------------
--  DDL for Index SYS_C007441
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007441" ON "ACAS"."OPERATOR_KIND" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007442
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007442" ON "ACAS"."OPERATOR_KIND" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index SYS_C007445
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007445" ON "ACAS"."OPERATOR_TYPE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007446
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007446" ON "ACAS"."OPERATOR_TYPE" ("TYPE_NAME");
--------------------------------------------------------
--  DDL for Index SYS_C007454
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007454" ON "ACAS"."PROTOCOL" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007455
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007455" ON "ACAS"."PROTOCOL" ("CODE_NAME");
--------------------------------------------------------
--  DDL for Index SYS_C007459
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007459" ON "ACAS"."PROTOCOL_KIND" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007460
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007460" ON "ACAS"."PROTOCOL_KIND" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index SYS_C007472
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007472" ON "ACAS"."PROTOCOL_LABEL" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007481
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007481" ON "ACAS"."PROTOCOL_STATE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007484
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007484" ON "ACAS"."PROTOCOL_TAG" ("PROTOCOL_ID", "TAG_ID");
--------------------------------------------------------
--  DDL for Index SYS_C007487
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007487" ON "ACAS"."PROTOCOL_TYPE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007488
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007488" ON "ACAS"."PROTOCOL_TYPE" ("TYPE_NAME");
--------------------------------------------------------
--  DDL for Index SYS_C007498
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007498" ON "ACAS"."PROTOCOL_VALUE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007502
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007502" ON "ACAS"."STATE_KIND" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007503
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007503" ON "ACAS"."STATE_KIND" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index SYS_C007506
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007506" ON "ACAS"."STATE_TYPE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007507
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007507" ON "ACAS"."STATE_TYPE" ("TYPE_NAME");
--------------------------------------------------------
--  DDL for Index SYS_C007515
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007515" ON "ACAS"."SUBJECT" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007516
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007516" ON "ACAS"."SUBJECT" ("CODE_NAME");
--------------------------------------------------------
--  DDL for Index SYS_C007528
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007528" ON "ACAS"."SUBJECT_LABEL" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007537
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007537" ON "ACAS"."SUBJECT_STATE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007547
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007547" ON "ACAS"."SUBJECT_VALUE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007551
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007551" ON "ACAS"."THING_KIND" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007552
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007552" ON "ACAS"."THING_KIND" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index SYS_C007562
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007562" ON "ACAS"."THING_PAGE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007570
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007570" ON "ACAS"."THING_PAGE_ARCHIVE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007573
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007573" ON "ACAS"."THING_TYPE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007574
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007574" ON "ACAS"."THING_TYPE" ("TYPE_NAME");
--------------------------------------------------------
--  DDL for Index SYS_C007582
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007582" ON "ACAS"."TREATMENT_GROUP" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007583
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007583" ON "ACAS"."TREATMENT_GROUP" ("CODE_NAME");
--------------------------------------------------------
--  DDL for Index SYS_C007595
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007595" ON "ACAS"."TREATMENT_GROUP_LABEL" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007604
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007604" ON "ACAS"."TREATMENT_GROUP_STATE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007614
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007614" ON "ACAS"."TREATMENT_GROUP_VALUE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007617
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007617" ON "ACAS"."TREATMENTGROUP_SUBJECT" ("SUBJECT_ID", "TREATMENT_GROUP_ID");
--------------------------------------------------------
--  DDL for Index SYS_C007620
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007620" ON "ACAS"."UNCERTAINTY_KIND" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007621
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007621" ON "ACAS"."UNCERTAINTY_KIND" ("KIND_NAME");
--------------------------------------------------------
--  DDL for Index SYS_C007625
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007625" ON "ACAS"."UNIT_KIND" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007626
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007626" ON "ACAS"."UNIT_KIND" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index SYS_C007629
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007629" ON "ACAS"."UNIT_TYPE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007630
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007630" ON "ACAS"."UNIT_TYPE" ("TYPE_NAME");
--------------------------------------------------------
--  DDL for Index SYS_C007633
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007633" ON "ACAS"."UPDATE_LOG" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007637
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007637" ON "ACAS"."VALUE_KIND" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007638
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007638" ON "ACAS"."VALUE_KIND" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index SYS_C007641
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007641" ON "ACAS"."VALUE_TYPE" ("ID");
--------------------------------------------------------
--  DDL for Index SYS_C007642
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACAS"."SYS_C007642" ON "ACAS"."VALUE_TYPE" ("TYPE_NAME");
--------------------------------------------------------
--  DDL for Index TREATMENT_GROUP_IGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."TREATMENT_GROUP_IGN_IDX" ON "ACAS"."TREATMENT_GROUP" ("IGNORED");
--------------------------------------------------------
--  DDL for Index TREATMENT_GROUP_KIND_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."TREATMENT_GROUP_KIND_IDX" ON "ACAS"."TREATMENT_GROUP" ("LS_KIND");
--------------------------------------------------------
--  DDL for Index TREATMENT_GROUP_LABEL_IGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."TREATMENT_GROUP_LABEL_IGN_IDX" ON "ACAS"."TREATMENT_GROUP_LABEL" ("IGNORED");
--------------------------------------------------------
--  DDL for Index TREATMENT_GROUP_LABEL_KIND_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."TREATMENT_GROUP_LABEL_KIND_IDX" ON "ACAS"."TREATMENT_GROUP_LABEL" ("LS_KIND");
--------------------------------------------------------
--  DDL for Index TREATMENT_GROUP_LABEL_PREF_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."TREATMENT_GROUP_LABEL_PREF_IDX" ON "ACAS"."TREATMENT_GROUP_LABEL" ("PREFERRED");
--------------------------------------------------------
--  DDL for Index TREATMENT_GROUP_LABEL_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."TREATMENT_GROUP_LABEL_TK_IDX" ON "ACAS"."TREATMENT_GROUP_LABEL" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index TREATMENT_GROUP_LABEL_TRXN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."TREATMENT_GROUP_LABEL_TRXN_IDX" ON "ACAS"."TREATMENT_GROUP_LABEL" ("LS_TRANSACTION");
--------------------------------------------------------
--  DDL for Index TREATMENT_GROUP_LABEL_TYPE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."TREATMENT_GROUP_LABEL_TYPE_IDX" ON "ACAS"."TREATMENT_GROUP_LABEL" ("LS_TYPE");
--------------------------------------------------------
--  DDL for Index TREATMENT_GROUP_STATE_IGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."TREATMENT_GROUP_STATE_IGN_IDX" ON "ACAS"."TREATMENT_GROUP_STATE" ("IGNORED");
--------------------------------------------------------
--  DDL for Index TREATMENT_GROUP_STATE_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."TREATMENT_GROUP_STATE_TK_IDX" ON "ACAS"."TREATMENT_GROUP_STATE" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index TREATMENT_GROUP_STATE_TRXN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."TREATMENT_GROUP_STATE_TRXN_IDX" ON "ACAS"."TREATMENT_GROUP_STATE" ("LS_TRANSACTION");
--------------------------------------------------------
--  DDL for Index TREATMENT_GROUP_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."TREATMENT_GROUP_TK_IDX" ON "ACAS"."TREATMENT_GROUP" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index TREATMENT_GROUP_TRXN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."TREATMENT_GROUP_TRXN_IDX" ON "ACAS"."TREATMENT_GROUP" ("LS_TRANSACTION");
--------------------------------------------------------
--  DDL for Index TREATMENT_GROUP_TYPE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."TREATMENT_GROUP_TYPE_IDX" ON "ACAS"."TREATMENT_GROUP" ("LS_TYPE");
--------------------------------------------------------
--  DDL for Index TREATMENT_GROUP_VALUE_CDTK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."TREATMENT_GROUP_VALUE_CDTK_IDX" ON "ACAS"."TREATMENT_GROUP_VALUE" ("CODE_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index TREATMENT_GROUP_VALUE_CODE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."TREATMENT_GROUP_VALUE_CODE_IDX" ON "ACAS"."TREATMENT_GROUP_VALUE" ("CODE_VALUE");
--------------------------------------------------------
--  DDL for Index TREATMENT_GROUP_VALUE_IGN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."TREATMENT_GROUP_VALUE_IGN_IDX" ON "ACAS"."TREATMENT_GROUP_VALUE" ("IGNORED");
--------------------------------------------------------
--  DDL for Index TREATMENT_GROUP_VALUE_KIND_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."TREATMENT_GROUP_VALUE_KIND_IDX" ON "ACAS"."TREATMENT_GROUP_VALUE" ("LS_KIND");
--------------------------------------------------------
--  DDL for Index TREATMENT_GROUP_VALUE_OPTK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."TREATMENT_GROUP_VALUE_OPTK_IDX" ON "ACAS"."TREATMENT_GROUP_VALUE" ("OPERATOR_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index TREATMENT_GROUP_VALUE_TK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."TREATMENT_GROUP_VALUE_TK_IDX" ON "ACAS"."TREATMENT_GROUP_VALUE" ("LS_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index TREATMENT_GROUP_VALUE_TRXN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."TREATMENT_GROUP_VALUE_TRXN_IDX" ON "ACAS"."TREATMENT_GROUP_VALUE" ("LS_TRANSACTION");
--------------------------------------------------------
--  DDL for Index TREATMENT_GROUP_VALUE_TYPE_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."TREATMENT_GROUP_VALUE_TYPE_IDX" ON "ACAS"."TREATMENT_GROUP_VALUE" ("LS_TYPE");
--------------------------------------------------------
--  DDL for Index TREATMENT_GROUP_VALUE_UNTK_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."TREATMENT_GROUP_VALUE_UNTK_IDX" ON "ACAS"."TREATMENT_GROUP_VALUE" ("UNIT_TYPE_AND_KIND");
--------------------------------------------------------
--  DDL for Index UPDATELOG_THING_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."UPDATELOG_THING_IDX" ON "ACAS"."UPDATE_LOG" ("THING");
--------------------------------------------------------
--  DDL for Index UPDATELOG_TRXN_IDX
--------------------------------------------------------

  CREATE INDEX "ACAS"."UPDATELOG_TRXN_IDX" ON "ACAS"."UPDATE_LOG" ("LS_TRANSACTION");

  --------------------------------------------------------
--  Constraints for Table ANALYSISGROUP_TREATMENTGROUP
--------------------------------------------------------

  ALTER TABLE "ACAS"."ANALYSISGROUP_TREATMENTGROUP" ADD PRIMARY KEY ("TREATMENT_GROUP_ID", "ANALYSIS_GROUP_ID");
  ALTER TABLE "ACAS"."ANALYSISGROUP_TREATMENTGROUP" MODIFY ("ANALYSIS_GROUP_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ANALYSISGROUP_TREATMENTGROUP" MODIFY ("TREATMENT_GROUP_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table ANALYSIS_GROUP
--------------------------------------------------------

  ALTER TABLE "ACAS"."ANALYSIS_GROUP" ADD UNIQUE ("CODE_NAME");
  ALTER TABLE "ACAS"."ANALYSIS_GROUP" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."ANALYSIS_GROUP" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ANALYSIS_GROUP" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ANALYSIS_GROUP" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ANALYSIS_GROUP" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ANALYSIS_GROUP" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ANALYSIS_GROUP" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ANALYSIS_GROUP" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table ANALYSIS_GROUP_LABEL
--------------------------------------------------------

  ALTER TABLE "ACAS"."ANALYSIS_GROUP_LABEL" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."ANALYSIS_GROUP_LABEL" MODIFY ("ANALYSIS_GROUP_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ANALYSIS_GROUP_LABEL" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ANALYSIS_GROUP_LABEL" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ANALYSIS_GROUP_LABEL" MODIFY ("PREFERRED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ANALYSIS_GROUP_LABEL" MODIFY ("PHYSICALLY_LABLED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ANALYSIS_GROUP_LABEL" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ANALYSIS_GROUP_LABEL" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ANALYSIS_GROUP_LABEL" MODIFY ("LABEL_TEXT" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ANALYSIS_GROUP_LABEL" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ANALYSIS_GROUP_LABEL" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ANALYSIS_GROUP_LABEL" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table ANALYSIS_GROUP_STATE
--------------------------------------------------------

  ALTER TABLE "ACAS"."ANALYSIS_GROUP_STATE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."ANALYSIS_GROUP_STATE" MODIFY ("ANALYSIS_GROUP_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ANALYSIS_GROUP_STATE" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ANALYSIS_GROUP_STATE" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ANALYSIS_GROUP_STATE" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ANALYSIS_GROUP_STATE" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ANALYSIS_GROUP_STATE" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ANALYSIS_GROUP_STATE" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ANALYSIS_GROUP_STATE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table ANALYSIS_GROUP_VALUE
--------------------------------------------------------

  ALTER TABLE "ACAS"."ANALYSIS_GROUP_VALUE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."ANALYSIS_GROUP_VALUE" MODIFY ("ANALYSIS_STATE_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ANALYSIS_GROUP_VALUE" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ANALYSIS_GROUP_VALUE" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ANALYSIS_GROUP_VALUE" MODIFY ("PUBLIC_DATA" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ANALYSIS_GROUP_VALUE" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ANALYSIS_GROUP_VALUE" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ANALYSIS_GROUP_VALUE" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ANALYSIS_GROUP_VALUE" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ANALYSIS_GROUP_VALUE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table APPLICATION_SETTING
--------------------------------------------------------

  ALTER TABLE "ACAS"."APPLICATION_SETTING" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."APPLICATION_SETTING" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."APPLICATION_SETTING" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table AUTHOR
--------------------------------------------------------

  ALTER TABLE "ACAS"."AUTHOR" ADD UNIQUE ("USER_NAME");
  ALTER TABLE "ACAS"."AUTHOR" ADD UNIQUE ("EMAIL_ADDRESS");
  ALTER TABLE "ACAS"."AUTHOR" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."AUTHOR" MODIFY ("USER_NAME" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."AUTHOR" MODIFY ("PASSWORD" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."AUTHOR" MODIFY ("LAST_NAME" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."AUTHOR" MODIFY ("FIRST_NAME" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."AUTHOR" MODIFY ("EMAIL_ADDRESS" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."AUTHOR" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table AUTHOR_ROLE
--------------------------------------------------------

  ALTER TABLE "ACAS"."AUTHOR_ROLE" ADD UNIQUE ("AUTHOR_ID", "LSROLE_ID");
  ALTER TABLE "ACAS"."AUTHOR_ROLE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."AUTHOR_ROLE" MODIFY ("AUTHOR_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."AUTHOR_ROLE" MODIFY ("LSROLE_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."AUTHOR_ROLE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table CODE_KIND
--------------------------------------------------------

  ALTER TABLE "ACAS"."CODE_KIND" ADD UNIQUE ("LS_TYPE_AND_KIND");
  ALTER TABLE "ACAS"."CODE_KIND" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."CODE_KIND" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CODE_KIND" MODIFY ("KIND_NAME" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CODE_KIND" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table CODE_ORIGIN
--------------------------------------------------------

  ALTER TABLE "ACAS"."CODE_ORIGIN" ADD UNIQUE ("NAME");
  ALTER TABLE "ACAS"."CODE_ORIGIN" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."CODE_ORIGIN" MODIFY ("NAME" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CODE_ORIGIN" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table CODE_TYPE
--------------------------------------------------------

  ALTER TABLE "ACAS"."CODE_TYPE" ADD UNIQUE ("TYPE_NAME");
  ALTER TABLE "ACAS"."CODE_TYPE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."CODE_TYPE" MODIFY ("TYPE_NAME" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CODE_TYPE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table CONTAINER
--------------------------------------------------------

  ALTER TABLE "ACAS"."CONTAINER" ADD UNIQUE ("CODE_NAME");
  ALTER TABLE "ACAS"."CONTAINER" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."CONTAINER" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CONTAINER" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CONTAINER" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CONTAINER" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CONTAINER" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CONTAINER" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CONTAINER" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table CONTAINER_KIND
--------------------------------------------------------

  ALTER TABLE "ACAS"."CONTAINER_KIND" ADD UNIQUE ("LS_TYPE_AND_KIND");
  ALTER TABLE "ACAS"."CONTAINER_KIND" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."CONTAINER_KIND" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CONTAINER_KIND" MODIFY ("KIND_NAME" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CONTAINER_KIND" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table CONTAINER_LABEL
--------------------------------------------------------

  ALTER TABLE "ACAS"."CONTAINER_LABEL" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."CONTAINER_LABEL" MODIFY ("CONTAINER_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CONTAINER_LABEL" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CONTAINER_LABEL" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CONTAINER_LABEL" MODIFY ("PREFERRED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CONTAINER_LABEL" MODIFY ("PHYSICALLY_LABLED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CONTAINER_LABEL" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CONTAINER_LABEL" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CONTAINER_LABEL" MODIFY ("LABEL_TEXT" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CONTAINER_LABEL" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CONTAINER_LABEL" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CONTAINER_LABEL" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table CONTAINER_STATE
--------------------------------------------------------

  ALTER TABLE "ACAS"."CONTAINER_STATE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."CONTAINER_STATE" MODIFY ("CONTAINER_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CONTAINER_STATE" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CONTAINER_STATE" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CONTAINER_STATE" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CONTAINER_STATE" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CONTAINER_STATE" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CONTAINER_STATE" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CONTAINER_STATE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table CONTAINER_TYPE
--------------------------------------------------------

  ALTER TABLE "ACAS"."CONTAINER_TYPE" ADD UNIQUE ("TYPE_NAME");
  ALTER TABLE "ACAS"."CONTAINER_TYPE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."CONTAINER_TYPE" MODIFY ("TYPE_NAME" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CONTAINER_TYPE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table CONTAINER_VALUE
--------------------------------------------------------

  ALTER TABLE "ACAS"."CONTAINER_VALUE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."CONTAINER_VALUE" MODIFY ("CONTAINER_STATE_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CONTAINER_VALUE" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CONTAINER_VALUE" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CONTAINER_VALUE" MODIFY ("PUBLIC_DATA" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CONTAINER_VALUE" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CONTAINER_VALUE" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CONTAINER_VALUE" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CONTAINER_VALUE" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."CONTAINER_VALUE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table DDICT_KIND
--------------------------------------------------------

  ALTER TABLE "ACAS"."DDICT_KIND" ADD UNIQUE ("LS_TYPE_AND_KIND");
  ALTER TABLE "ACAS"."DDICT_KIND" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."DDICT_KIND" MODIFY ("NAME" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."DDICT_KIND" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."DDICT_KIND" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table DDICT_TYPE
--------------------------------------------------------

  ALTER TABLE "ACAS"."DDICT_TYPE" ADD UNIQUE ("NAME");
  ALTER TABLE "ACAS"."DDICT_TYPE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."DDICT_TYPE" MODIFY ("NAME" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."DDICT_TYPE" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."DDICT_TYPE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table DDICT_VALUE
--------------------------------------------------------

  ALTER TABLE "ACAS"."DDICT_VALUE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."DDICT_VALUE" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."DDICT_VALUE" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."DDICT_VALUE" MODIFY ("LABEL_TEXT" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."DDICT_VALUE" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."DDICT_VALUE" MODIFY ("CODE_NAME" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."DDICT_VALUE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table EXPERIMENT
--------------------------------------------------------

  ALTER TABLE "ACAS"."EXPERIMENT" ADD UNIQUE ("CODE_NAME");
  ALTER TABLE "ACAS"."EXPERIMENT" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."EXPERIMENT" MODIFY ("PROTOCOL_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."EXPERIMENT" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."EXPERIMENT" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."EXPERIMENT" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."EXPERIMENT" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."EXPERIMENT" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."EXPERIMENT" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."EXPERIMENT" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table EXPERIMENT_ANALYSISGROUP
--------------------------------------------------------

  ALTER TABLE "ACAS"."EXPERIMENT_ANALYSISGROUP" ADD PRIMARY KEY ("ANALYSIS_GROUP_ID", "EXPERIMENT_ID");
  ALTER TABLE "ACAS"."EXPERIMENT_ANALYSISGROUP" MODIFY ("EXPERIMENT_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."EXPERIMENT_ANALYSISGROUP" MODIFY ("ANALYSIS_GROUP_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table EXPERIMENT_KIND
--------------------------------------------------------

  ALTER TABLE "ACAS"."EXPERIMENT_KIND" ADD UNIQUE ("LS_TYPE_AND_KIND");
  ALTER TABLE "ACAS"."EXPERIMENT_KIND" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."EXPERIMENT_KIND" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."EXPERIMENT_KIND" MODIFY ("KIND_NAME" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."EXPERIMENT_KIND" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table EXPERIMENT_LABEL
--------------------------------------------------------

  ALTER TABLE "ACAS"."EXPERIMENT_LABEL" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."EXPERIMENT_LABEL" MODIFY ("EXPERIMENT_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."EXPERIMENT_LABEL" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."EXPERIMENT_LABEL" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."EXPERIMENT_LABEL" MODIFY ("PREFERRED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."EXPERIMENT_LABEL" MODIFY ("PHYSICALLY_LABLED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."EXPERIMENT_LABEL" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."EXPERIMENT_LABEL" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."EXPERIMENT_LABEL" MODIFY ("LABEL_TEXT" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."EXPERIMENT_LABEL" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."EXPERIMENT_LABEL" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."EXPERIMENT_LABEL" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table EXPERIMENT_STATE
--------------------------------------------------------

  ALTER TABLE "ACAS"."EXPERIMENT_STATE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."EXPERIMENT_STATE" MODIFY ("EXPERIMENT_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."EXPERIMENT_STATE" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."EXPERIMENT_STATE" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."EXPERIMENT_STATE" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."EXPERIMENT_STATE" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."EXPERIMENT_STATE" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."EXPERIMENT_STATE" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."EXPERIMENT_STATE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table EXPERIMENT_TAG
--------------------------------------------------------

  ALTER TABLE "ACAS"."EXPERIMENT_TAG" ADD PRIMARY KEY ("EXPERIMENT_ID", "TAG_ID");
  ALTER TABLE "ACAS"."EXPERIMENT_TAG" MODIFY ("TAG_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."EXPERIMENT_TAG" MODIFY ("EXPERIMENT_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table EXPERIMENT_TYPE
--------------------------------------------------------

  ALTER TABLE "ACAS"."EXPERIMENT_TYPE" ADD UNIQUE ("TYPE_NAME");
  ALTER TABLE "ACAS"."EXPERIMENT_TYPE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."EXPERIMENT_TYPE" MODIFY ("TYPE_NAME" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."EXPERIMENT_TYPE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table EXPERIMENT_VALUE
--------------------------------------------------------

  ALTER TABLE "ACAS"."EXPERIMENT_VALUE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."EXPERIMENT_VALUE" MODIFY ("EXPERIMENT_STATE_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."EXPERIMENT_VALUE" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."EXPERIMENT_VALUE" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."EXPERIMENT_VALUE" MODIFY ("PUBLIC_DATA" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."EXPERIMENT_VALUE" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."EXPERIMENT_VALUE" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."EXPERIMENT_VALUE" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."EXPERIMENT_VALUE" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."EXPERIMENT_VALUE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table FILE_THING
--------------------------------------------------------

  ALTER TABLE "ACAS"."FILE_THING" ADD UNIQUE ("CODE_NAME");
  ALTER TABLE "ACAS"."FILE_THING" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."FILE_THING" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."FILE_THING" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."FILE_THING" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."FILE_THING" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."FILE_THING" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."FILE_THING" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."FILE_THING" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table INTERACTION_KIND
--------------------------------------------------------

  ALTER TABLE "ACAS"."INTERACTION_KIND" ADD UNIQUE ("LS_TYPE_AND_KIND");
  ALTER TABLE "ACAS"."INTERACTION_KIND" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."INTERACTION_KIND" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."INTERACTION_KIND" MODIFY ("KIND_NAME" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."INTERACTION_KIND" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table INTERACTION_TYPE
--------------------------------------------------------

  ALTER TABLE "ACAS"."INTERACTION_TYPE" MODIFY ("ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."INTERACTION_TYPE" ADD UNIQUE ("TYPE_NAME");
  ALTER TABLE "ACAS"."INTERACTION_TYPE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."INTERACTION_TYPE" MODIFY ("TYPE_VERB" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."INTERACTION_TYPE" MODIFY ("TYPE_NAME" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table ITX_CONTAINER_CONTAINER
--------------------------------------------------------

  ALTER TABLE "ACAS"."ITX_CONTAINER_CONTAINER" ADD UNIQUE ("CODE_NAME");
  ALTER TABLE "ACAS"."ITX_CONTAINER_CONTAINER" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."ITX_CONTAINER_CONTAINER" MODIFY ("SECOND_CONTAINER_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_CONTAINER_CONTAINER" MODIFY ("FIRST_CONTAINER_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_CONTAINER_CONTAINER" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_CONTAINER_CONTAINER" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_CONTAINER_CONTAINER" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_CONTAINER_CONTAINER" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_CONTAINER_CONTAINER" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_CONTAINER_CONTAINER" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_CONTAINER_CONTAINER" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table ITX_CONTAINER_CONTAINER_STATE
--------------------------------------------------------

  ALTER TABLE "ACAS"."ITX_CONTAINER_CONTAINER_STATE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."ITX_CONTAINER_CONTAINER_STATE" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_CONTAINER_CONTAINER_STATE" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_CONTAINER_CONTAINER_STATE" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_CONTAINER_CONTAINER_STATE" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_CONTAINER_CONTAINER_STATE" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_CONTAINER_CONTAINER_STATE" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_CONTAINER_CONTAINER_STATE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table ITX_CONTAINER_CONTAINER_VALUE
--------------------------------------------------------

  ALTER TABLE "ACAS"."ITX_CONTAINER_CONTAINER_VALUE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."ITX_CONTAINER_CONTAINER_VALUE" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_CONTAINER_CONTAINER_VALUE" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_CONTAINER_CONTAINER_VALUE" MODIFY ("PUBLIC_DATA" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_CONTAINER_CONTAINER_VALUE" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_CONTAINER_CONTAINER_VALUE" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_CONTAINER_CONTAINER_VALUE" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_CONTAINER_CONTAINER_VALUE" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_CONTAINER_CONTAINER_VALUE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table ITX_EXPT_EXPT
--------------------------------------------------------

  ALTER TABLE "ACAS"."ITX_EXPT_EXPT" ADD UNIQUE ("CODE_NAME");
  ALTER TABLE "ACAS"."ITX_EXPT_EXPT" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."ITX_EXPT_EXPT" MODIFY ("SECOND_EXPERIMENT_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_EXPT_EXPT" MODIFY ("FIRST_EXPERIMENT_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_EXPT_EXPT" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_EXPT_EXPT" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_EXPT_EXPT" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_EXPT_EXPT" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_EXPT_EXPT" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_EXPT_EXPT" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_EXPT_EXPT" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table ITX_EXPT_EXPT_STATE
--------------------------------------------------------

  ALTER TABLE "ACAS"."ITX_EXPT_EXPT_STATE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."ITX_EXPT_EXPT_STATE" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_EXPT_EXPT_STATE" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_EXPT_EXPT_STATE" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_EXPT_EXPT_STATE" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_EXPT_EXPT_STATE" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_EXPT_EXPT_STATE" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_EXPT_EXPT_STATE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table ITX_EXPT_EXPT_VALUE
--------------------------------------------------------

  ALTER TABLE "ACAS"."ITX_EXPT_EXPT_VALUE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."ITX_EXPT_EXPT_VALUE" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_EXPT_EXPT_VALUE" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_EXPT_EXPT_VALUE" MODIFY ("PUBLIC_DATA" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_EXPT_EXPT_VALUE" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_EXPT_EXPT_VALUE" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_EXPT_EXPT_VALUE" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_EXPT_EXPT_VALUE" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_EXPT_EXPT_VALUE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table ITX_LS_THING_LS_THING
--------------------------------------------------------

  ALTER TABLE "ACAS"."ITX_LS_THING_LS_THING" ADD UNIQUE ("CODE_NAME");
  ALTER TABLE "ACAS"."ITX_LS_THING_LS_THING" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."ITX_LS_THING_LS_THING" MODIFY ("SECOND_LS_THING_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_LS_THING_LS_THING" MODIFY ("FIRST_LS_THING_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_LS_THING_LS_THING" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_LS_THING_LS_THING" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_LS_THING_LS_THING" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_LS_THING_LS_THING" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_LS_THING_LS_THING" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_LS_THING_LS_THING" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_LS_THING_LS_THING" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table ITX_LS_THING_LS_THING_STATE
--------------------------------------------------------

  ALTER TABLE "ACAS"."ITX_LS_THING_LS_THING_STATE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."ITX_LS_THING_LS_THING_STATE" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_LS_THING_LS_THING_STATE" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_LS_THING_LS_THING_STATE" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_LS_THING_LS_THING_STATE" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_LS_THING_LS_THING_STATE" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_LS_THING_LS_THING_STATE" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_LS_THING_LS_THING_STATE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table ITX_LS_THING_LS_THING_VALUE
--------------------------------------------------------

  ALTER TABLE "ACAS"."ITX_LS_THING_LS_THING_VALUE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."ITX_LS_THING_LS_THING_VALUE" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_LS_THING_LS_THING_VALUE" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_LS_THING_LS_THING_VALUE" MODIFY ("PUBLIC_DATA" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_LS_THING_LS_THING_VALUE" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_LS_THING_LS_THING_VALUE" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_LS_THING_LS_THING_VALUE" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_LS_THING_LS_THING_VALUE" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_LS_THING_LS_THING_VALUE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table ITX_PROTOCOL_PROTOCOL
--------------------------------------------------------

  ALTER TABLE "ACAS"."ITX_PROTOCOL_PROTOCOL" ADD UNIQUE ("CODE_NAME");
  ALTER TABLE "ACAS"."ITX_PROTOCOL_PROTOCOL" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."ITX_PROTOCOL_PROTOCOL" MODIFY ("SECOND_PROTOCOL_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_PROTOCOL_PROTOCOL" MODIFY ("FIRST_PROTOCOL_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_PROTOCOL_PROTOCOL" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_PROTOCOL_PROTOCOL" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_PROTOCOL_PROTOCOL" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_PROTOCOL_PROTOCOL" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_PROTOCOL_PROTOCOL" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_PROTOCOL_PROTOCOL" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_PROTOCOL_PROTOCOL" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table ITX_PROTOCOL_PROTOCOL_STATE
--------------------------------------------------------

  ALTER TABLE "ACAS"."ITX_PROTOCOL_PROTOCOL_STATE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."ITX_PROTOCOL_PROTOCOL_STATE" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_PROTOCOL_PROTOCOL_STATE" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_PROTOCOL_PROTOCOL_STATE" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_PROTOCOL_PROTOCOL_STATE" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_PROTOCOL_PROTOCOL_STATE" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_PROTOCOL_PROTOCOL_STATE" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_PROTOCOL_PROTOCOL_STATE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table ITX_PROTOCOL_PROTOCOL_VALUE
--------------------------------------------------------

  ALTER TABLE "ACAS"."ITX_PROTOCOL_PROTOCOL_VALUE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."ITX_PROTOCOL_PROTOCOL_VALUE" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_PROTOCOL_PROTOCOL_VALUE" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_PROTOCOL_PROTOCOL_VALUE" MODIFY ("PUBLIC_DATA" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_PROTOCOL_PROTOCOL_VALUE" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_PROTOCOL_PROTOCOL_VALUE" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_PROTOCOL_PROTOCOL_VALUE" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_PROTOCOL_PROTOCOL_VALUE" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_PROTOCOL_PROTOCOL_VALUE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table ITX_SUBJECT_CONTAINER
--------------------------------------------------------

  ALTER TABLE "ACAS"."ITX_SUBJECT_CONTAINER" ADD UNIQUE ("CODE_NAME");
  ALTER TABLE "ACAS"."ITX_SUBJECT_CONTAINER" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."ITX_SUBJECT_CONTAINER" MODIFY ("SUBJECT_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_SUBJECT_CONTAINER" MODIFY ("CONTAINER_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_SUBJECT_CONTAINER" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_SUBJECT_CONTAINER" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_SUBJECT_CONTAINER" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_SUBJECT_CONTAINER" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_SUBJECT_CONTAINER" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_SUBJECT_CONTAINER" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_SUBJECT_CONTAINER" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table ITX_SUBJECT_CONTAINER_STATE
--------------------------------------------------------

  ALTER TABLE "ACAS"."ITX_SUBJECT_CONTAINER_STATE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."ITX_SUBJECT_CONTAINER_STATE" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_SUBJECT_CONTAINER_STATE" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_SUBJECT_CONTAINER_STATE" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_SUBJECT_CONTAINER_STATE" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_SUBJECT_CONTAINER_STATE" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_SUBJECT_CONTAINER_STATE" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_SUBJECT_CONTAINER_STATE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table ITX_SUBJECT_CONTAINER_VALUE
--------------------------------------------------------

  ALTER TABLE "ACAS"."ITX_SUBJECT_CONTAINER_VALUE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."ITX_SUBJECT_CONTAINER_VALUE" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_SUBJECT_CONTAINER_VALUE" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_SUBJECT_CONTAINER_VALUE" MODIFY ("PUBLIC_DATA" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_SUBJECT_CONTAINER_VALUE" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_SUBJECT_CONTAINER_VALUE" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_SUBJECT_CONTAINER_VALUE" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_SUBJECT_CONTAINER_VALUE" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."ITX_SUBJECT_CONTAINER_VALUE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table LABEL_KIND
--------------------------------------------------------

  ALTER TABLE "ACAS"."LABEL_KIND" ADD UNIQUE ("LS_TYPE_AND_KIND");
  ALTER TABLE "ACAS"."LABEL_KIND" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."LABEL_KIND" MODIFY ("KIND_NAME" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LABEL_KIND" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table LABEL_SEQUENCE
--------------------------------------------------------

  ALTER TABLE "ACAS"."LABEL_SEQUENCE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."LABEL_SEQUENCE" MODIFY ("THING_TYPE_AND_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LABEL_SEQUENCE" MODIFY ("LATEST_NUMBER" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LABEL_SEQUENCE" MODIFY ("LABEL_TYPE_AND_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LABEL_SEQUENCE" MODIFY ("LABEL_PREFIX" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LABEL_SEQUENCE" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LABEL_SEQUENCE" MODIFY ("GROUP_DIGITS" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LABEL_SEQUENCE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table LABEL_TYPE
--------------------------------------------------------

  ALTER TABLE "ACAS"."LABEL_TYPE" ADD UNIQUE ("TYPE_NAME");
  ALTER TABLE "ACAS"."LABEL_TYPE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."LABEL_TYPE" MODIFY ("TYPE_NAME" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LABEL_TYPE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table LSTHING_TAG
--------------------------------------------------------

  ALTER TABLE "ACAS"."LSTHING_TAG" ADD PRIMARY KEY ("LSTHING_ID", "TAG_ID");
  ALTER TABLE "ACAS"."LSTHING_TAG" MODIFY ("TAG_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LSTHING_TAG" MODIFY ("LSTHING_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table LS_INTERACTION
--------------------------------------------------------

  ALTER TABLE "ACAS"."LS_INTERACTION" ADD UNIQUE ("CODE_NAME");
  ALTER TABLE "ACAS"."LS_INTERACTION" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."LS_INTERACTION" MODIFY ("SECOND_THING_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_INTERACTION" MODIFY ("FIRST_THING_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_INTERACTION" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_INTERACTION" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_INTERACTION" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_INTERACTION" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_INTERACTION" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_INTERACTION" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_INTERACTION" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table LS_ROLE
--------------------------------------------------------

  ALTER TABLE "ACAS"."LS_ROLE" ADD UNIQUE ("ROLE_NAME");
  ALTER TABLE "ACAS"."LS_ROLE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."LS_ROLE" MODIFY ("ROLE_NAME" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_ROLE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table LS_SEQ_ANL_GRP
--------------------------------------------------------

  ALTER TABLE "ACAS"."LS_SEQ_ANL_GRP" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."LS_SEQ_ANL_GRP" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table LS_SEQ_CONTAINER
--------------------------------------------------------

  ALTER TABLE "ACAS"."LS_SEQ_CONTAINER" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."LS_SEQ_CONTAINER" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table LS_SEQ_EXPT
--------------------------------------------------------

  ALTER TABLE "ACAS"."LS_SEQ_EXPT" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."LS_SEQ_EXPT" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table LS_SEQ_ITX_CNTR_CNTR
--------------------------------------------------------

  ALTER TABLE "ACAS"."LS_SEQ_ITX_CNTR_CNTR" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."LS_SEQ_ITX_CNTR_CNTR" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table LS_SEQ_ITX_PROTOCOL_PROTOCOL
--------------------------------------------------------

  ALTER TABLE "ACAS"."LS_SEQ_ITX_PROTOCOL_PROTOCOL" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."LS_SEQ_ITX_PROTOCOL_PROTOCOL" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table LS_SEQ_ITX_SUBJ_CNTR
--------------------------------------------------------

  ALTER TABLE "ACAS"."LS_SEQ_ITX_SUBJ_CNTR" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."LS_SEQ_ITX_SUBJ_CNTR" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table LS_SEQ_PROTOCOL
--------------------------------------------------------

  ALTER TABLE "ACAS"."LS_SEQ_PROTOCOL" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."LS_SEQ_PROTOCOL" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table LS_SEQ_SUBJECT
--------------------------------------------------------

  ALTER TABLE "ACAS"."LS_SEQ_SUBJECT" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."LS_SEQ_SUBJECT" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table LS_SEQ_TRT_GRP
--------------------------------------------------------

  ALTER TABLE "ACAS"."LS_SEQ_TRT_GRP" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."LS_SEQ_TRT_GRP" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table LS_TAG
--------------------------------------------------------

  ALTER TABLE "ACAS"."LS_TAG" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."LS_TAG" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table LS_THING
--------------------------------------------------------

  ALTER TABLE "ACAS"."LS_THING" ADD UNIQUE ("CODE_NAME");
  ALTER TABLE "ACAS"."LS_THING" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."LS_THING" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_THING" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_THING" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_THING" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_THING" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_THING" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_THING" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table LS_THING_LABEL
--------------------------------------------------------

  ALTER TABLE "ACAS"."LS_THING_LABEL" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."LS_THING_LABEL" MODIFY ("LSTHING_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_THING_LABEL" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_THING_LABEL" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_THING_LABEL" MODIFY ("PREFERRED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_THING_LABEL" MODIFY ("PHYSICALLY_LABLED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_THING_LABEL" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_THING_LABEL" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_THING_LABEL" MODIFY ("LABEL_TEXT" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_THING_LABEL" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_THING_LABEL" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_THING_LABEL" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table LS_THING_STATE
--------------------------------------------------------

  ALTER TABLE "ACAS"."LS_THING_STATE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."LS_THING_STATE" MODIFY ("LSTHING_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_THING_STATE" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_THING_STATE" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_THING_STATE" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_THING_STATE" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_THING_STATE" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_THING_STATE" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_THING_STATE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table LS_THING_VALUE
--------------------------------------------------------

  ALTER TABLE "ACAS"."LS_THING_VALUE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."LS_THING_VALUE" MODIFY ("LSTHING_STATE_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_THING_VALUE" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_THING_VALUE" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_THING_VALUE" MODIFY ("PUBLIC_DATA" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_THING_VALUE" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_THING_VALUE" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_THING_VALUE" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_THING_VALUE" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_THING_VALUE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table LS_TRANSACTION
--------------------------------------------------------

  ALTER TABLE "ACAS"."LS_TRANSACTION" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."LS_TRANSACTION" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."LS_TRANSACTION" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table OPERATOR_KIND
--------------------------------------------------------

  ALTER TABLE "ACAS"."OPERATOR_KIND" ADD UNIQUE ("LS_TYPE_AND_KIND");
  ALTER TABLE "ACAS"."OPERATOR_KIND" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."OPERATOR_KIND" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."OPERATOR_KIND" MODIFY ("KIND_NAME" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."OPERATOR_KIND" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table OPERATOR_TYPE
--------------------------------------------------------

  ALTER TABLE "ACAS"."OPERATOR_TYPE" ADD UNIQUE ("TYPE_NAME");
  ALTER TABLE "ACAS"."OPERATOR_TYPE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."OPERATOR_TYPE" MODIFY ("TYPE_NAME" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."OPERATOR_TYPE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PROTOCOL
--------------------------------------------------------

  ALTER TABLE "ACAS"."PROTOCOL" ADD UNIQUE ("CODE_NAME");
  ALTER TABLE "ACAS"."PROTOCOL" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."PROTOCOL" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."PROTOCOL" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."PROTOCOL" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."PROTOCOL" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."PROTOCOL" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."PROTOCOL" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."PROTOCOL" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PROTOCOL_KIND
--------------------------------------------------------

  ALTER TABLE "ACAS"."PROTOCOL_KIND" ADD UNIQUE ("LS_TYPE_AND_KIND");
  ALTER TABLE "ACAS"."PROTOCOL_KIND" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."PROTOCOL_KIND" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."PROTOCOL_KIND" MODIFY ("KIND_NAME" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."PROTOCOL_KIND" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PROTOCOL_LABEL
--------------------------------------------------------

  ALTER TABLE "ACAS"."PROTOCOL_LABEL" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."PROTOCOL_LABEL" MODIFY ("PROTOCOL_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."PROTOCOL_LABEL" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."PROTOCOL_LABEL" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."PROTOCOL_LABEL" MODIFY ("PREFERRED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."PROTOCOL_LABEL" MODIFY ("PHYSICALLY_LABLED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."PROTOCOL_LABEL" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."PROTOCOL_LABEL" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."PROTOCOL_LABEL" MODIFY ("LABEL_TEXT" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."PROTOCOL_LABEL" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."PROTOCOL_LABEL" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."PROTOCOL_LABEL" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PROTOCOL_STATE
--------------------------------------------------------

  ALTER TABLE "ACAS"."PROTOCOL_STATE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."PROTOCOL_STATE" MODIFY ("PROTOCOL_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."PROTOCOL_STATE" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."PROTOCOL_STATE" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."PROTOCOL_STATE" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."PROTOCOL_STATE" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."PROTOCOL_STATE" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."PROTOCOL_STATE" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."PROTOCOL_STATE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PROTOCOL_TAG
--------------------------------------------------------

  ALTER TABLE "ACAS"."PROTOCOL_TAG" ADD PRIMARY KEY ("PROTOCOL_ID", "TAG_ID");
  ALTER TABLE "ACAS"."PROTOCOL_TAG" MODIFY ("TAG_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."PROTOCOL_TAG" MODIFY ("PROTOCOL_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PROTOCOL_TYPE
--------------------------------------------------------

  ALTER TABLE "ACAS"."PROTOCOL_TYPE" ADD UNIQUE ("TYPE_NAME");
  ALTER TABLE "ACAS"."PROTOCOL_TYPE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."PROTOCOL_TYPE" MODIFY ("TYPE_NAME" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."PROTOCOL_TYPE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PROTOCOL_VALUE
--------------------------------------------------------

  ALTER TABLE "ACAS"."PROTOCOL_VALUE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."PROTOCOL_VALUE" MODIFY ("PROTOCOL_STATE_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."PROTOCOL_VALUE" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."PROTOCOL_VALUE" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."PROTOCOL_VALUE" MODIFY ("PUBLIC_DATA" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."PROTOCOL_VALUE" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."PROTOCOL_VALUE" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."PROTOCOL_VALUE" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."PROTOCOL_VALUE" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."PROTOCOL_VALUE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table STATE_KIND
--------------------------------------------------------

  ALTER TABLE "ACAS"."STATE_KIND" ADD UNIQUE ("LS_TYPE_AND_KIND");
  ALTER TABLE "ACAS"."STATE_KIND" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."STATE_KIND" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."STATE_KIND" MODIFY ("KIND_NAME" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."STATE_KIND" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table STATE_TYPE
--------------------------------------------------------

  ALTER TABLE "ACAS"."STATE_TYPE" ADD UNIQUE ("TYPE_NAME");
  ALTER TABLE "ACAS"."STATE_TYPE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."STATE_TYPE" MODIFY ("TYPE_NAME" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."STATE_TYPE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table SUBJECT
--------------------------------------------------------

  ALTER TABLE "ACAS"."SUBJECT" ADD UNIQUE ("CODE_NAME");
  ALTER TABLE "ACAS"."SUBJECT" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."SUBJECT" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."SUBJECT" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."SUBJECT" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."SUBJECT" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."SUBJECT" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."SUBJECT" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."SUBJECT" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table SUBJECT_LABEL
--------------------------------------------------------

  ALTER TABLE "ACAS"."SUBJECT_LABEL" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."SUBJECT_LABEL" MODIFY ("SUBJECT_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."SUBJECT_LABEL" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."SUBJECT_LABEL" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."SUBJECT_LABEL" MODIFY ("PREFERRED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."SUBJECT_LABEL" MODIFY ("PHYSICALLY_LABLED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."SUBJECT_LABEL" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."SUBJECT_LABEL" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."SUBJECT_LABEL" MODIFY ("LABEL_TEXT" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."SUBJECT_LABEL" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."SUBJECT_LABEL" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."SUBJECT_LABEL" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table SUBJECT_STATE
--------------------------------------------------------

  ALTER TABLE "ACAS"."SUBJECT_STATE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."SUBJECT_STATE" MODIFY ("SUBJECT_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."SUBJECT_STATE" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."SUBJECT_STATE" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."SUBJECT_STATE" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."SUBJECT_STATE" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."SUBJECT_STATE" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."SUBJECT_STATE" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."SUBJECT_STATE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table SUBJECT_VALUE
--------------------------------------------------------

  ALTER TABLE "ACAS"."SUBJECT_VALUE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."SUBJECT_VALUE" MODIFY ("SUBJECT_STATE_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."SUBJECT_VALUE" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."SUBJECT_VALUE" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."SUBJECT_VALUE" MODIFY ("PUBLIC_DATA" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."SUBJECT_VALUE" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."SUBJECT_VALUE" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."SUBJECT_VALUE" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."SUBJECT_VALUE" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."SUBJECT_VALUE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table THING_KIND
--------------------------------------------------------

  ALTER TABLE "ACAS"."THING_KIND" ADD UNIQUE ("LS_TYPE_AND_KIND");
  ALTER TABLE "ACAS"."THING_KIND" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."THING_KIND" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."THING_KIND" MODIFY ("KIND_NAME" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."THING_KIND" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table THING_PAGE
--------------------------------------------------------

  ALTER TABLE "ACAS"."THING_PAGE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."THING_PAGE" MODIFY ("THING_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."THING_PAGE" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."THING_PAGE" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."THING_PAGE" MODIFY ("MODIFIED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."THING_PAGE" MODIFY ("MODIFIED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."THING_PAGE" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."THING_PAGE" MODIFY ("CURRENT_EDITOR" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."THING_PAGE" MODIFY ("ARCHIVED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."THING_PAGE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table THING_PAGE_ARCHIVE
--------------------------------------------------------

  ALTER TABLE "ACAS"."THING_PAGE_ARCHIVE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."THING_PAGE_ARCHIVE" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."THING_PAGE_ARCHIVE" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."THING_PAGE_ARCHIVE" MODIFY ("MODIFIED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."THING_PAGE_ARCHIVE" MODIFY ("MODIFIED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."THING_PAGE_ARCHIVE" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."THING_PAGE_ARCHIVE" MODIFY ("ARCHIVED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."THING_PAGE_ARCHIVE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table THING_TYPE
--------------------------------------------------------

  ALTER TABLE "ACAS"."THING_TYPE" ADD UNIQUE ("TYPE_NAME");
  ALTER TABLE "ACAS"."THING_TYPE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."THING_TYPE" MODIFY ("TYPE_NAME" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."THING_TYPE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TREATMENTGROUP_SUBJECT
--------------------------------------------------------

  ALTER TABLE "ACAS"."TREATMENTGROUP_SUBJECT" ADD PRIMARY KEY ("SUBJECT_ID", "TREATMENT_GROUP_ID");
  ALTER TABLE "ACAS"."TREATMENTGROUP_SUBJECT" MODIFY ("TREATMENT_GROUP_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."TREATMENTGROUP_SUBJECT" MODIFY ("SUBJECT_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TREATMENT_GROUP
--------------------------------------------------------

  ALTER TABLE "ACAS"."TREATMENT_GROUP" ADD UNIQUE ("CODE_NAME");
  ALTER TABLE "ACAS"."TREATMENT_GROUP" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."TREATMENT_GROUP" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."TREATMENT_GROUP" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."TREATMENT_GROUP" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."TREATMENT_GROUP" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."TREATMENT_GROUP" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."TREATMENT_GROUP" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."TREATMENT_GROUP" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TREATMENT_GROUP_LABEL
--------------------------------------------------------

  ALTER TABLE "ACAS"."TREATMENT_GROUP_LABEL" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."TREATMENT_GROUP_LABEL" MODIFY ("TREATMENT_GROUP_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."TREATMENT_GROUP_LABEL" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."TREATMENT_GROUP_LABEL" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."TREATMENT_GROUP_LABEL" MODIFY ("PREFERRED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."TREATMENT_GROUP_LABEL" MODIFY ("PHYSICALLY_LABLED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."TREATMENT_GROUP_LABEL" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."TREATMENT_GROUP_LABEL" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."TREATMENT_GROUP_LABEL" MODIFY ("LABEL_TEXT" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."TREATMENT_GROUP_LABEL" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."TREATMENT_GROUP_LABEL" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."TREATMENT_GROUP_LABEL" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TREATMENT_GROUP_STATE
--------------------------------------------------------

  ALTER TABLE "ACAS"."TREATMENT_GROUP_STATE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."TREATMENT_GROUP_STATE" MODIFY ("TREATMENT_GROUP_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."TREATMENT_GROUP_STATE" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."TREATMENT_GROUP_STATE" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."TREATMENT_GROUP_STATE" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."TREATMENT_GROUP_STATE" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."TREATMENT_GROUP_STATE" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."TREATMENT_GROUP_STATE" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."TREATMENT_GROUP_STATE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TREATMENT_GROUP_VALUE
--------------------------------------------------------

  ALTER TABLE "ACAS"."TREATMENT_GROUP_VALUE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."TREATMENT_GROUP_VALUE" MODIFY ("TREATMENT_STATE_ID" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."TREATMENT_GROUP_VALUE" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."TREATMENT_GROUP_VALUE" MODIFY ("RECORDED_BY" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."TREATMENT_GROUP_VALUE" MODIFY ("PUBLIC_DATA" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."TREATMENT_GROUP_VALUE" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."TREATMENT_GROUP_VALUE" MODIFY ("LS_KIND" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."TREATMENT_GROUP_VALUE" MODIFY ("IGNORED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."TREATMENT_GROUP_VALUE" MODIFY ("DELETED" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."TREATMENT_GROUP_VALUE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table UNCERTAINTY_KIND
--------------------------------------------------------

  ALTER TABLE "ACAS"."UNCERTAINTY_KIND" ADD UNIQUE ("KIND_NAME");
  ALTER TABLE "ACAS"."UNCERTAINTY_KIND" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."UNCERTAINTY_KIND" MODIFY ("KIND_NAME" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."UNCERTAINTY_KIND" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table UNIT_KIND
--------------------------------------------------------

  ALTER TABLE "ACAS"."UNIT_KIND" ADD UNIQUE ("LS_TYPE_AND_KIND");
  ALTER TABLE "ACAS"."UNIT_KIND" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."UNIT_KIND" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."UNIT_KIND" MODIFY ("KIND_NAME" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."UNIT_KIND" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table UNIT_TYPE
--------------------------------------------------------

  ALTER TABLE "ACAS"."UNIT_TYPE" ADD UNIQUE ("TYPE_NAME");
  ALTER TABLE "ACAS"."UNIT_TYPE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."UNIT_TYPE" MODIFY ("TYPE_NAME" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."UNIT_TYPE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table UPDATE_LOG
--------------------------------------------------------

  ALTER TABLE "ACAS"."UPDATE_LOG" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."UPDATE_LOG" MODIFY ("RECORDED_DATE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."UPDATE_LOG" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table VALUE_KIND
--------------------------------------------------------

  ALTER TABLE "ACAS"."VALUE_KIND" ADD UNIQUE ("LS_TYPE_AND_KIND");
  ALTER TABLE "ACAS"."VALUE_KIND" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."VALUE_KIND" MODIFY ("LS_TYPE" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."VALUE_KIND" MODIFY ("KIND_NAME" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."VALUE_KIND" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table VALUE_TYPE
--------------------------------------------------------

  ALTER TABLE "ACAS"."VALUE_TYPE" ADD UNIQUE ("TYPE_NAME");
  ALTER TABLE "ACAS"."VALUE_TYPE" ADD PRIMARY KEY ("ID");
  ALTER TABLE "ACAS"."VALUE_TYPE" MODIFY ("TYPE_NAME" NOT NULL ENABLE);
  ALTER TABLE "ACAS"."VALUE_TYPE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Ref Constraints for Table ANALYSISGROUP_TREATMENTGROUP
--------------------------------------------------------

  ALTER TABLE "ACAS"."ANALYSISGROUP_TREATMENTGROUP" ADD CONSTRAINT "FKDF0BFFA32C65B2B0" FOREIGN KEY ("TREATMENT_GROUP_ID") REFERENCES "ACAS"."TREATMENT_GROUP" ("ID") ENABLE;
  ALTER TABLE "ACAS"."ANALYSISGROUP_TREATMENTGROUP" ADD CONSTRAINT "FKDF0BFFA39951C9FA" FOREIGN KEY ("ANALYSIS_GROUP_ID") REFERENCES "ACAS"."ANALYSIS_GROUP" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table ANALYSIS_GROUP_LABEL
--------------------------------------------------------

  ALTER TABLE "ACAS"."ANALYSIS_GROUP_LABEL" ADD CONSTRAINT "FK86FEE4519951C9FA" FOREIGN KEY ("ANALYSIS_GROUP_ID") REFERENCES "ACAS"."ANALYSIS_GROUP" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table ANALYSIS_GROUP_STATE
--------------------------------------------------------

  ALTER TABLE "ACAS"."ANALYSIS_GROUP_STATE" ADD CONSTRAINT "FK876A29EE9951C9FA" FOREIGN KEY ("ANALYSIS_GROUP_ID") REFERENCES "ACAS"."ANALYSIS_GROUP" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table ANALYSIS_GROUP_VALUE
--------------------------------------------------------

  ALTER TABLE "ACAS"."ANALYSIS_GROUP_VALUE" ADD CONSTRAINT "FK878BF6CE41555661" FOREIGN KEY ("ANALYSIS_STATE_ID") REFERENCES "ACAS"."ANALYSIS_GROUP_STATE" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table AUTHOR_ROLE
--------------------------------------------------------

  ALTER TABLE "ACAS"."AUTHOR_ROLE" ADD CONSTRAINT "FK2A8156AA3DE5BE1" FOREIGN KEY ("AUTHOR_ID") REFERENCES "ACAS"."AUTHOR" ("ID") ENABLE;
  ALTER TABLE "ACAS"."AUTHOR_ROLE" ADD CONSTRAINT "FK2A8156AA479BD2C1" FOREIGN KEY ("LSROLE_ID") REFERENCES "ACAS"."LS_ROLE" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table CODE_KIND
--------------------------------------------------------

  ALTER TABLE "ACAS"."CODE_KIND" ADD CONSTRAINT "FKBCCEF2A6ECE27E00" FOREIGN KEY ("LS_TYPE") REFERENCES "ACAS"."CODE_TYPE" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table CONTAINER_KIND
--------------------------------------------------------

  ALTER TABLE "ACAS"."CONTAINER_KIND" ADD CONSTRAINT "FK32FC5092D29E5E66" FOREIGN KEY ("LS_TYPE") REFERENCES "ACAS"."CONTAINER_TYPE" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table CONTAINER_LABEL
--------------------------------------------------------

  ALTER TABLE "ACAS"."CONTAINER_LABEL" ADD CONSTRAINT "FK2C9809B61B382AB3" FOREIGN KEY ("CONTAINER_ID") REFERENCES "ACAS"."CONTAINER" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table CONTAINER_STATE
--------------------------------------------------------

  ALTER TABLE "ACAS"."CONTAINER_STATE" ADD CONSTRAINT "FK2D034F531B382AB3" FOREIGN KEY ("CONTAINER_ID") REFERENCES "ACAS"."CONTAINER" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table CONTAINER_VALUE
--------------------------------------------------------

  ALTER TABLE "ACAS"."CONTAINER_VALUE" ADD CONSTRAINT "FK2D251C337627111E" FOREIGN KEY ("CONTAINER_STATE_ID") REFERENCES "ACAS"."CONTAINER_STATE" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table EXPERIMENT
--------------------------------------------------------

  ALTER TABLE "ACAS"."EXPERIMENT" ADD CONSTRAINT "FKFAE9DBFDB262BF01" FOREIGN KEY ("PROTOCOL_ID") REFERENCES "ACAS"."PROTOCOL" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table EXPERIMENT_ANALYSISGROUP
--------------------------------------------------------

  ALTER TABLE "ACAS"."EXPERIMENT_ANALYSISGROUP" ADD CONSTRAINT "FKE4DE9C619951C9FA" FOREIGN KEY ("ANALYSIS_GROUP_ID") REFERENCES "ACAS"."ANALYSIS_GROUP" ("ID") ENABLE;
  ALTER TABLE "ACAS"."EXPERIMENT_ANALYSISGROUP" ADD CONSTRAINT "FKE4DE9C61DD459321" FOREIGN KEY ("EXPERIMENT_ID") REFERENCES "ACAS"."EXPERIMENT" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table EXPERIMENT_KIND
--------------------------------------------------------

  ALTER TABLE "ACAS"."EXPERIMENT_KIND" ADD CONSTRAINT "FKD92E0E96A50B92D0" FOREIGN KEY ("LS_TYPE") REFERENCES "ACAS"."EXPERIMENT_TYPE" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table EXPERIMENT_LABEL
--------------------------------------------------------

  ALTER TABLE "ACAS"."EXPERIMENT_LABEL" ADD CONSTRAINT "FK4C9E0C32DD459321" FOREIGN KEY ("EXPERIMENT_ID") REFERENCES "ACAS"."EXPERIMENT" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table EXPERIMENT_STATE
--------------------------------------------------------

  ALTER TABLE "ACAS"."EXPERIMENT_STATE" ADD CONSTRAINT "FK4D0951CFDD459321" FOREIGN KEY ("EXPERIMENT_ID") REFERENCES "ACAS"."EXPERIMENT" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table EXPERIMENT_TAG
--------------------------------------------------------

  ALTER TABLE "ACAS"."EXPERIMENT_TAG" ADD CONSTRAINT "FK7A9E8458C95C958C" FOREIGN KEY ("TAG_ID") REFERENCES "ACAS"."LS_TAG" ("ID") ENABLE;
  ALTER TABLE "ACAS"."EXPERIMENT_TAG" ADD CONSTRAINT "FK7A9E8458DD459321" FOREIGN KEY ("EXPERIMENT_ID") REFERENCES "ACAS"."EXPERIMENT" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table EXPERIMENT_VALUE
--------------------------------------------------------

  ALTER TABLE "ACAS"."EXPERIMENT_VALUE" ADD CONSTRAINT "FK4D2B1EAF8CBB85F8" FOREIGN KEY ("EXPERIMENT_STATE_ID") REFERENCES "ACAS"."EXPERIMENT_STATE" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table INTERACTION_KIND
--------------------------------------------------------

  ALTER TABLE "ACAS"."INTERACTION_KIND" ADD CONSTRAINT "FK5ECA21A1C0730D37" FOREIGN KEY ("LS_TYPE") REFERENCES "ACAS"."INTERACTION_TYPE" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table ITX_CONTAINER_CONTAINER
--------------------------------------------------------

  ALTER TABLE "ACAS"."ITX_CONTAINER_CONTAINER" ADD CONSTRAINT "FKF504B3B12EA89FE2" FOREIGN KEY ("FIRST_CONTAINER_ID") REFERENCES "ACAS"."CONTAINER" ("ID") ENABLE;
  ALTER TABLE "ACAS"."ITX_CONTAINER_CONTAINER" ADD CONSTRAINT "FKF504B3B165CA2C9E" FOREIGN KEY ("SECOND_CONTAINER_ID") REFERENCES "ACAS"."CONTAINER" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table ITX_CONTAINER_CONTAINER_STATE
--------------------------------------------------------

  ALTER TABLE "ACAS"."ITX_CONTAINER_CONTAINER_STATE" ADD CONSTRAINT "FK594892838B822F57" FOREIGN KEY ("ITX_CONTAINER_CONTAINER") REFERENCES "ACAS"."ITX_CONTAINER_CONTAINER" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table ITX_CONTAINER_CONTAINER_VALUE
--------------------------------------------------------

  ALTER TABLE "ACAS"."ITX_CONTAINER_CONTAINER_VALUE" ADD CONSTRAINT "FK596A5F6332D66464" FOREIGN KEY ("LS_STATE") REFERENCES "ACAS"."ITX_CONTAINER_CONTAINER_STATE" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table ITX_EXPT_EXPT
--------------------------------------------------------

  ALTER TABLE "ACAS"."ITX_EXPT_EXPT" ADD CONSTRAINT "FKE354BA2D37E3C3D2" FOREIGN KEY ("FIRST_EXPERIMENT_ID") REFERENCES "ACAS"."EXPERIMENT" ("ID") ENABLE;
  ALTER TABLE "ACAS"."ITX_EXPT_EXPT" ADD CONSTRAINT "FKE354BA2DE4F3CE96" FOREIGN KEY ("SECOND_EXPERIMENT_ID") REFERENCES "ACAS"."EXPERIMENT" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table ITX_EXPT_EXPT_STATE
--------------------------------------------------------

  ALTER TABLE "ACAS"."ITX_EXPT_EXPT_STATE" ADD CONSTRAINT "FK5080CBFF6DC8946D" FOREIGN KEY ("ITX_EXPERIMENT_EXPERIMENT") REFERENCES "ACAS"."ITX_EXPT_EXPT" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table ITX_EXPT_EXPT_VALUE
--------------------------------------------------------

  ALTER TABLE "ACAS"."ITX_EXPT_EXPT_VALUE" ADD CONSTRAINT "FK50A298DFB9A1634A" FOREIGN KEY ("LS_STATE") REFERENCES "ACAS"."ITX_EXPT_EXPT_STATE" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table ITX_LS_THING_LS_THING
--------------------------------------------------------

  ALTER TABLE "ACAS"."ITX_LS_THING_LS_THING" ADD CONSTRAINT "FK1C981F0D537768F9" FOREIGN KEY ("SECOND_LS_THING_ID") REFERENCES "ACAS"."LS_THING" ("ID") ENABLE;
  ALTER TABLE "ACAS"."ITX_LS_THING_LS_THING" ADD CONSTRAINT "FK1C981F0DFF1B7D35" FOREIGN KEY ("FIRST_LS_THING_ID") REFERENCES "ACAS"."LS_THING" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table ITX_LS_THING_LS_THING_STATE
--------------------------------------------------------

  ALTER TABLE "ACAS"."ITX_LS_THING_LS_THING_STATE" ADD CONSTRAINT "FK841D08DF4FC6BAB3" FOREIGN KEY ("ITX_LS_THING_LS_THING") REFERENCES "ACAS"."ITX_LS_THING_LS_THING" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table ITX_LS_THING_LS_THING_VALUE
--------------------------------------------------------

  ALTER TABLE "ACAS"."ITX_LS_THING_LS_THING_VALUE" ADD CONSTRAINT "FK843ED5BF77D94464" FOREIGN KEY ("LS_STATE") REFERENCES "ACAS"."ITX_LS_THING_LS_THING_STATE" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table ITX_PROTOCOL_PROTOCOL
--------------------------------------------------------

  ALTER TABLE "ACAS"."ITX_PROTOCOL_PROTOCOL" ADD CONSTRAINT "FK5312AD4D17E353B6" FOREIGN KEY ("SECOND_PROTOCOL_ID") REFERENCES "ACAS"."PROTOCOL" ("ID") ENABLE;
  ALTER TABLE "ACAS"."ITX_PROTOCOL_PROTOCOL" ADD CONSTRAINT "FK5312AD4DC38767F2" FOREIGN KEY ("FIRST_PROTOCOL_ID") REFERENCES "ACAS"."PROTOCOL" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table ITX_PROTOCOL_PROTOCOL_STATE
--------------------------------------------------------

  ALTER TABLE "ACAS"."ITX_PROTOCOL_PROTOCOL_STATE" ADD CONSTRAINT "FK6583E71FA0EC62C3" FOREIGN KEY ("ITX_PROTOCOL_PROTOCOL") REFERENCES "ACAS"."ITX_PROTOCOL_PROTOCOL" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table ITX_PROTOCOL_PROTOCOL_VALUE
--------------------------------------------------------

  ALTER TABLE "ACAS"."ITX_PROTOCOL_PROTOCOL_VALUE" ADD CONSTRAINT "FK65A5B3FF5C25CC94" FOREIGN KEY ("LS_STATE") REFERENCES "ACAS"."ITX_PROTOCOL_PROTOCOL_STATE" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table ITX_SUBJECT_CONTAINER
--------------------------------------------------------

  ALTER TABLE "ACAS"."ITX_SUBJECT_CONTAINER" ADD CONSTRAINT "FKD3A7A61C1B382AB3" FOREIGN KEY ("CONTAINER_ID") REFERENCES "ACAS"."CONTAINER" ("ID") ENABLE;
  ALTER TABLE "ACAS"."ITX_SUBJECT_CONTAINER" ADD CONSTRAINT "FKD3A7A61C66673913" FOREIGN KEY ("SUBJECT_ID") REFERENCES "ACAS"."SUBJECT" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table ITX_SUBJECT_CONTAINER_STATE
--------------------------------------------------------

  ALTER TABLE "ACAS"."ITX_SUBJECT_CONTAINER_STATE" ADD CONSTRAINT "FKF6A8C8AE8C161AF7" FOREIGN KEY ("ITX_SUBJECT_CONTAINER") REFERENCES "ACAS"."ITX_SUBJECT_CONTAINER" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table ITX_SUBJECT_CONTAINER_VALUE
--------------------------------------------------------

  ALTER TABLE "ACAS"."ITX_SUBJECT_CONTAINER_VALUE" ADD CONSTRAINT "FKF6CA958E809E44F" FOREIGN KEY ("LS_STATE") REFERENCES "ACAS"."ITX_SUBJECT_CONTAINER_STATE" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table LABEL_KIND
--------------------------------------------------------

  ALTER TABLE "ACAS"."LABEL_KIND" ADD CONSTRAINT "FK9DAAADFFAAA613D9" FOREIGN KEY ("LS_TYPE") REFERENCES "ACAS"."LABEL_TYPE" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table LSTHING_TAG
--------------------------------------------------------

  ALTER TABLE "ACAS"."LSTHING_TAG" ADD CONSTRAINT "FK63F187224D1FF13" FOREIGN KEY ("LSTHING_ID") REFERENCES "ACAS"."LS_THING" ("ID") ENABLE;
  ALTER TABLE "ACAS"."LSTHING_TAG" ADD CONSTRAINT "FK63F18722C95C958C" FOREIGN KEY ("TAG_ID") REFERENCES "ACAS"."LS_TAG" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table LS_THING_LABEL
--------------------------------------------------------

  ALTER TABLE "ACAS"."LS_THING_LABEL" ADD CONSTRAINT "FK6280D5AB4D1FF13" FOREIGN KEY ("LSTHING_ID") REFERENCES "ACAS"."LS_THING" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table LS_THING_STATE
--------------------------------------------------------

  ALTER TABLE "ACAS"."LS_THING_STATE" ADD CONSTRAINT "FK62EC1B484D1FF13" FOREIGN KEY ("LSTHING_ID") REFERENCES "ACAS"."LS_THING" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table LS_THING_VALUE
--------------------------------------------------------

  ALTER TABLE "ACAS"."LS_THING_VALUE" ADD CONSTRAINT "FK630DE828F5FF5EF2" FOREIGN KEY ("LSTHING_STATE_ID") REFERENCES "ACAS"."LS_THING_STATE" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table OPERATOR_KIND
--------------------------------------------------------

  ALTER TABLE "ACAS"."OPERATOR_KIND" ADD CONSTRAINT "FK4736924FF5F249B7" FOREIGN KEY ("LS_TYPE") REFERENCES "ACAS"."OPERATOR_TYPE" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PROTOCOL_KIND
--------------------------------------------------------

  ALTER TABLE "ACAS"."PROTOCOL_KIND" ADD CONSTRAINT "FK2A2BC4DB58695AB" FOREIGN KEY ("LS_TYPE") REFERENCES "ACAS"."PROTOCOL_TYPE" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PROTOCOL_LABEL
--------------------------------------------------------

  ALTER TABLE "ACAS"."PROTOCOL_LABEL" ADD CONSTRAINT "FK1B571E8DB262BF01" FOREIGN KEY ("PROTOCOL_ID") REFERENCES "ACAS"."PROTOCOL" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PROTOCOL_STATE
--------------------------------------------------------

  ALTER TABLE "ACAS"."PROTOCOL_STATE" ADD CONSTRAINT "FK1BC2642AB262BF01" FOREIGN KEY ("PROTOCOL_ID") REFERENCES "ACAS"."PROTOCOL" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PROTOCOL_TAG
--------------------------------------------------------

  ALTER TABLE "ACAS"."PROTOCOL_TAG" ADD CONSTRAINT "FK99E7173B262BF01" FOREIGN KEY ("PROTOCOL_ID") REFERENCES "ACAS"."PROTOCOL" ("ID") ENABLE;
  ALTER TABLE "ACAS"."PROTOCOL_TAG" ADD CONSTRAINT "FK99E7173C95C958C" FOREIGN KEY ("TAG_ID") REFERENCES "ACAS"."LS_TAG" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PROTOCOL_VALUE
--------------------------------------------------------

  ALTER TABLE "ACAS"."PROTOCOL_VALUE" ADD CONSTRAINT "FK1BE4310ACE9FEA42" FOREIGN KEY ("PROTOCOL_STATE_ID") REFERENCES "ACAS"."PROTOCOL_STATE" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table STATE_KIND
--------------------------------------------------------

  ALTER TABLE "ACAS"."STATE_KIND" ADD CONSTRAINT "FKF26B6282528B42F6" FOREIGN KEY ("LS_TYPE") REFERENCES "ACAS"."STATE_TYPE" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table SUBJECT_LABEL
--------------------------------------------------------

  ALTER TABLE "ACAS"."SUBJECT_LABEL" ADD CONSTRAINT "FK1AA4A62166673913" FOREIGN KEY ("SUBJECT_ID") REFERENCES "ACAS"."SUBJECT" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table SUBJECT_STATE
--------------------------------------------------------

  ALTER TABLE "ACAS"."SUBJECT_STATE" ADD CONSTRAINT "FK1B0FEBBE66673913" FOREIGN KEY ("SUBJECT_ID") REFERENCES "ACAS"."SUBJECT" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table SUBJECT_VALUE
--------------------------------------------------------

  ALTER TABLE "ACAS"."SUBJECT_VALUE" ADD CONSTRAINT "FK1B31B89EC83958C8" FOREIGN KEY ("SUBJECT_STATE_ID") REFERENCES "ACAS"."SUBJECT_STATE" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table THING_KIND
--------------------------------------------------------

  ALTER TABLE "ACAS"."THING_KIND" ADD CONSTRAINT "FKF88D5445A5528CD3" FOREIGN KEY ("LS_TYPE") REFERENCES "ACAS"."THING_TYPE" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table THING_PAGE
--------------------------------------------------------

  ALTER TABLE "ACAS"."THING_PAGE" ADD CONSTRAINT "FKF88F7B402AA4F16" FOREIGN KEY ("LS_TRANSACTION") REFERENCES "ACAS"."LS_TRANSACTION" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TREATMENTGROUP_SUBJECT
--------------------------------------------------------

  ALTER TABLE "ACAS"."TREATMENTGROUP_SUBJECT" ADD CONSTRAINT "FKD2980DF42C65B2B0" FOREIGN KEY ("TREATMENT_GROUP_ID") REFERENCES "ACAS"."TREATMENT_GROUP" ("ID") ENABLE;
  ALTER TABLE "ACAS"."TREATMENTGROUP_SUBJECT" ADD CONSTRAINT "FKD2980DF466673913" FOREIGN KEY ("SUBJECT_ID") REFERENCES "ACAS"."SUBJECT" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TREATMENT_GROUP_LABEL
--------------------------------------------------------

  ALTER TABLE "ACAS"."TREATMENT_GROUP_LABEL" ADD CONSTRAINT "FKC3609FED2C65B2B0" FOREIGN KEY ("TREATMENT_GROUP_ID") REFERENCES "ACAS"."TREATMENT_GROUP" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TREATMENT_GROUP_STATE
--------------------------------------------------------

  ALTER TABLE "ACAS"."TREATMENT_GROUP_STATE" ADD CONSTRAINT "FKC3CBE58A2C65B2B0" FOREIGN KEY ("TREATMENT_GROUP_ID") REFERENCES "ACAS"."TREATMENT_GROUP" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TREATMENT_GROUP_VALUE
--------------------------------------------------------

  ALTER TABLE "ACAS"."TREATMENT_GROUP_VALUE" ADD CONSTRAINT "FKC3EDB26AE33874B3" FOREIGN KEY ("TREATMENT_STATE_ID") REFERENCES "ACAS"."TREATMENT_GROUP_STATE" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table UNIT_KIND
--------------------------------------------------------

  ALTER TABLE "ACAS"."UNIT_KIND" ADD CONSTRAINT "FKE3D56D6FF2D0617" FOREIGN KEY ("LS_TYPE") REFERENCES "ACAS"."UNIT_TYPE" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table VALUE_KIND
--------------------------------------------------------

  ALTER TABLE "ACAS"."VALUE_KIND" ADD CONSTRAINT "FK79C6A1A2A13B9FD6" FOREIGN KEY ("LS_TYPE") REFERENCES "ACAS"."VALUE_TYPE" ("ID") ENABLE;
