DECLARE
TYPE string_assarrtype
IS
  TABLE OF VARCHAR2 ( 30 ) INDEX BY VARCHAR2 ( 30 );
  arr string_assarrtype;
  l_idx VARCHAR2(60);
A NUMBER(1) := 0;
BEGIN
  arr ( 'ANALYSIS_GROUP' )                := 'ANALYSIS_GROUP_DLTD_IDX';
  arr ( 'ANALYSIS_GROUP_LABEL' )          := 'ANALYSIS_GROUP_LABEL_DLTD_IDX';
  arr ( 'ANALYSIS_GROUP_STATE' )          := 'ANALYSIS_GROUP_STATE_DLTD_IDX';
  arr ( 'ANALYSIS_GROUP_VALUE' )          := 'ANALYSIS_GROUP_VALUE_DLTD_IDX';
  arr ( 'CONTAINER' )                     := 'CONTAINER_DLTD_IDX';
  arr ( 'CONTAINER_LABEL' )               := 'CNTRLBL_DLTD_IDX';
  arr ( 'CONTAINER_STATE' )               := 'CNTRST_DLTD_IDX';
  arr ( 'CONTAINER_VALUE' )               := 'CNTRVL_DLTD_IDX';
  arr ( 'EXPERIMENT' )                    := 'EXPERIMENT_DLTD_IDX';
  arr ( 'EXPERIMENT_LABEL' )              := 'EXPERIMENT_LABEL_DLTD_IDX';
  arr ( 'EXPERIMENT_STATE' )              := 'EXPERIMENT_STATE_DLTD_IDX';
  arr ( 'EXPERIMENT_VALUE' )              := 'EXPERIMENT_VALUE_DLTD_IDX';
  arr ( 'FILE_THING' )                    := 'FILE_THING_DLTD_IDX';
  arr ( 'ITX_CONTAINER_CONTAINER' )       := 'ITXCNTR_DLTD_IDX';
  arr ( 'ITX_CONTAINER_CONTAINER_STATE' ) := 'ITXCNTRST_DLTD_IDX';
  arr ( 'ITX_CONTAINER_CONTAINER_VALUE' ) := 'ITXCNTRVL_DLTD_IDX';
  arr ( 'ITX_EXPT_EXPT' )                 := 'ITX_EXPT_EXP_DLTD_IDX';
  arr ( 'ITX_EXPT_EXPT_STATE' )           := 'ITX_EXPT_EXPT_STATE_DLTD_IDX';
  arr ( 'ITX_EXPT_EXPT_VALUE' )           := 'ITX_EXPT_EXPT_VALUE_DLTD_IDX';
  arr ( 'ITX_LS_THING_LS_THING' )         := 'ITX_LS_THING_LS_THNG_DLTD_IDX';
  arr ( 'ITX_LS_THING_LS_THING_STATE' )   := 'ITX_THNG_THNG_STATE_DLTD_IDX';
  arr ( 'ITX_LS_THING_LS_THING_VALUE' )   := 'ITX_THNG_THNG_VAL_DLTD_IDX';
  arr ( 'ITX_PROTOCOL_PROTOCOL' )         := 'ITX_PROTOCOL_PROTOCO_DLTD_IDX';
  arr ( 'ITX_PROTOCOL_PROTOCOL_STATE' )   := 'ITX_PRCL_PRCL_STATE_DLTD_IDX';
  arr ( 'ITX_PROTOCOL_PROTOCOL_VALUE' )   := 'ITX_PRCL_PRCL_VALUE_DLTD_IDX';
  arr ( 'ITX_SUBJECT_CONTAINER' )         := 'ITXSUBJCNTR_DLTD_IDX';
  arr ( 'ITX_SUBJECT_CONTAINER_STATE' )   := 'ITXSBCNTRST_DLTD_IDX';
  arr ( 'ITX_SUBJECT_CONTAINER_VALUE' )   := 'ITXSBCNTRVL_DLTD_IDX';
  arr ( 'LS_INTERACTION' )                := 'LS_INTERACTION_DLTD_IDX';
  arr ( 'LS_THING' )                      := 'LS_THING_DLTD_IDX';
  arr ( 'LS_THING_LABEL' )                := 'LS_THING_LABEL_DLTD_IDX';
  arr ( 'LS_THING_STATE' )                := 'LS_THING_STATE_DLTD_IDX';
  arr ( 'LS_THING_VALUE' )                := 'LS_THING_VALUE_DLTD_IDX';
  arr ( 'PROTOCOL' )                      := 'PROTOCO_DLTD_IDX';
  arr ( 'PROTOCOL_LABEL' )                := 'PROTLBL_DLTD_IDX';
  arr ( 'PROTOCOL_STATE' )                := 'PROTOCOL_DLTD_IDX';
  arr ( 'PROTOCOL_VALUE' )                := 'PROTOCOL_VALUE_DLTD_IDX';
  arr ( 'SUBJECT' )                       := 'SUBJECT_DLTD_IDX';
  arr ( 'SUBJECT_LABEL' )                 := 'SBJLBL_DLTD_IDX';
  arr ( 'SUBJECT_STATE' )                 := 'SBJST_DLTD_IDX';
  arr ( 'SUBJECT_VALUE' )                 := 'SBJVL_DLTD_IDX';
  arr ( 'TREATMENT_GROUP' )               := 'TREATMENT_GRP_DLTD_IDX';
  arr ( 'TREATMENT_GROUP_LABEL' )         := 'TREATMENT_GRP_LABEL_DLTD_IDX';
  arr ( 'TREATMENT_GROUP_STATE' )         := 'TREATMENT_GRP_STATE_DLTD_IDX';
  arr ( 'TREATMENT_GROUP_VALUE' )         := 'TREATMENT_GRP_VALUE_DLTD_IDX';
  l_idx                                   := arr.first;
  WHILE (l_idx                            IS NOT NULL)
  LOOP
    dbms_output.put_line( arr(l_idx) );
    SELECT
      CASE
        WHEN EXISTS
          (SELECT * FROM USER_INDEXES WHERE INDEX_NAME = arr(l_idx))
        THEN 1
        ELSE 0
      END
      CASE
        INTO A
        FROM DUAL;
        IF A = 1 THEN
          DBMS_OUTPUT.PUT_LINE('INDEX EXISTS');
        ELSE
          DBMS_OUTPUT.PUT_LINE('NEW INDEX');
          EXECUTE IMMEDIATE 'CREATE INDEX ' || arr(l_idx) || ' ON ' || l_idx || ' ("DELETED")';
          DBMS_OUTPUT.PUT_LINE('CREATED INDEX');
        END IF;
        l_idx := arr.next(l_idx);
      END LOOP;
    END;
    /