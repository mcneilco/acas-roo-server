// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.AuthorLabel;
import com.labsynch.labseer.domain.AuthorState;
import com.labsynch.labseer.domain.AuthorValue;
import com.labsynch.labseer.domain.CodeOrigin;
import com.labsynch.labseer.domain.CronJob;
import com.labsynch.labseer.domain.ItxExperimentExperiment;
import com.labsynch.labseer.domain.ItxExperimentExperimentState;
import com.labsynch.labseer.domain.ItxExperimentExperimentValue;
import com.labsynch.labseer.domain.ItxLsThingLsThing;
import com.labsynch.labseer.domain.ItxLsThingLsThingState;
import com.labsynch.labseer.domain.ItxLsThingLsThingValue;
import com.labsynch.labseer.domain.LabelSequenceRole;
import com.labsynch.labseer.domain.LsSeqAnlGrp;
import com.labsynch.labseer.domain.LsSeqContainer;
import com.labsynch.labseer.domain.LsSeqExpt;
import com.labsynch.labseer.domain.LsSeqItxCntrCntr;
import com.labsynch.labseer.domain.LsSeqItxExperimentExperiment;
import com.labsynch.labseer.domain.LsSeqItxLsThingLsThing;
import com.labsynch.labseer.domain.LsSeqItxProtocolProtocol;
import com.labsynch.labseer.domain.LsSeqItxSubjCntr;
import com.labsynch.labseer.domain.LsSeqProtocol;
import com.labsynch.labseer.domain.LsSeqSubject;
import com.labsynch.labseer.domain.LsSeqTrtGrp;
import com.labsynch.labseer.domain.RoleKind;
import com.labsynch.labseer.domain.RoleType;
import com.labsynch.labseer.domain.TempSelectTable;
import com.labsynch.labseer.web.ApplicationConversionServiceFactoryBean;
import org.springframework.core.convert.converter.Converter;

privileged aspect ApplicationConversionServiceFactoryBean_Roo_ConversionService {
    
    public Converter<AuthorLabel, String> ApplicationConversionServiceFactoryBean.getAuthorLabelToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.AuthorLabel, java.lang.String>() {
            public String convert(AuthorLabel authorLabel) {
                return new StringBuilder().append(authorLabel.getLabelText()).append(' ').append(authorLabel.getRecordedBy()).append(' ').append(authorLabel.getRecordedDate()).append(' ').append(authorLabel.getModifiedDate()).toString();
            }
        };
    }
    
    public Converter<Long, AuthorLabel> ApplicationConversionServiceFactoryBean.getIdToAuthorLabelConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.AuthorLabel>() {
            public com.labsynch.labseer.domain.AuthorLabel convert(java.lang.Long id) {
                return AuthorLabel.findAuthorLabel(id);
            }
        };
    }
    
    public Converter<String, AuthorLabel> ApplicationConversionServiceFactoryBean.getStringToAuthorLabelConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.AuthorLabel>() {
            public com.labsynch.labseer.domain.AuthorLabel convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), AuthorLabel.class);
            }
        };
    }
    
    public Converter<AuthorState, String> ApplicationConversionServiceFactoryBean.getAuthorStateToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.AuthorState, java.lang.String>() {
            public String convert(AuthorState authorState) {
                return new StringBuilder().append(authorState.getRecordedBy()).append(' ').append(authorState.getRecordedDate()).append(' ').append(authorState.getModifiedBy()).append(' ').append(authorState.getModifiedDate()).toString();
            }
        };
    }
    
    public Converter<Long, AuthorState> ApplicationConversionServiceFactoryBean.getIdToAuthorStateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.AuthorState>() {
            public com.labsynch.labseer.domain.AuthorState convert(java.lang.Long id) {
                return AuthorState.findAuthorState(id);
            }
        };
    }
    
    public Converter<String, AuthorState> ApplicationConversionServiceFactoryBean.getStringToAuthorStateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.AuthorState>() {
            public com.labsynch.labseer.domain.AuthorState convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), AuthorState.class);
            }
        };
    }
    
    public Converter<AuthorValue, String> ApplicationConversionServiceFactoryBean.getAuthorValueToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.AuthorValue, java.lang.String>() {
            public String convert(AuthorValue authorValue) {
                return new StringBuilder().append(authorValue.getLsType()).append(' ').append(authorValue.getLsKind()).append(' ').append(authorValue.getLsTypeAndKind()).append(' ').append(authorValue.getCodeOrigin()).toString();
            }
        };
    }
    
    public Converter<Long, AuthorValue> ApplicationConversionServiceFactoryBean.getIdToAuthorValueConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.AuthorValue>() {
            public com.labsynch.labseer.domain.AuthorValue convert(java.lang.Long id) {
                return AuthorValue.findAuthorValue(id);
            }
        };
    }
    
    public Converter<String, AuthorValue> ApplicationConversionServiceFactoryBean.getStringToAuthorValueConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.AuthorValue>() {
            public com.labsynch.labseer.domain.AuthorValue convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), AuthorValue.class);
            }
        };
    }
    
    public Converter<CodeOrigin, String> ApplicationConversionServiceFactoryBean.getCodeOriginToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.CodeOrigin, java.lang.String>() {
            public String convert(CodeOrigin codeOrigin) {
                return new StringBuilder().append(codeOrigin.getName()).toString();
            }
        };
    }
    
    public Converter<Long, CodeOrigin> ApplicationConversionServiceFactoryBean.getIdToCodeOriginConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.CodeOrigin>() {
            public com.labsynch.labseer.domain.CodeOrigin convert(java.lang.Long id) {
                return CodeOrigin.findCodeOrigin(id);
            }
        };
    }
    
    public Converter<String, CodeOrigin> ApplicationConversionServiceFactoryBean.getStringToCodeOriginConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.CodeOrigin>() {
            public com.labsynch.labseer.domain.CodeOrigin convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), CodeOrigin.class);
            }
        };
    }
    
    public Converter<CronJob, String> ApplicationConversionServiceFactoryBean.getCronJobToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.CronJob, java.lang.String>() {
            public String convert(CronJob cronJob) {
                return new StringBuilder().append(cronJob.getSchedule()).append(' ').append(cronJob.getScriptType()).append(' ').append(cronJob.getScriptFile()).append(' ').append(cronJob.getFunctionName()).toString();
            }
        };
    }
    
    public Converter<Long, CronJob> ApplicationConversionServiceFactoryBean.getIdToCronJobConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.CronJob>() {
            public com.labsynch.labseer.domain.CronJob convert(java.lang.Long id) {
                return CronJob.findCronJob(id);
            }
        };
    }
    
    public Converter<String, CronJob> ApplicationConversionServiceFactoryBean.getStringToCronJobConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.CronJob>() {
            public com.labsynch.labseer.domain.CronJob convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), CronJob.class);
            }
        };
    }
    
    public Converter<ItxExperimentExperiment, String> ApplicationConversionServiceFactoryBean.getItxExperimentExperimentToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ItxExperimentExperiment, java.lang.String>() {
            public String convert(ItxExperimentExperiment itxExperimentExperiment) {
                return new StringBuilder().append(itxExperimentExperiment.getLsType()).append(' ').append(itxExperimentExperiment.getLsKind()).append(' ').append(itxExperimentExperiment.getLsTypeAndKind()).append(' ').append(itxExperimentExperiment.getCodeName()).toString();
            }
        };
    }
    
    public Converter<Long, ItxExperimentExperiment> ApplicationConversionServiceFactoryBean.getIdToItxExperimentExperimentConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ItxExperimentExperiment>() {
            public com.labsynch.labseer.domain.ItxExperimentExperiment convert(java.lang.Long id) {
                return ItxExperimentExperiment.findItxExperimentExperiment(id);
            }
        };
    }
    
    public Converter<String, ItxExperimentExperiment> ApplicationConversionServiceFactoryBean.getStringToItxExperimentExperimentConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ItxExperimentExperiment>() {
            public com.labsynch.labseer.domain.ItxExperimentExperiment convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ItxExperimentExperiment.class);
            }
        };
    }
    
    public Converter<ItxExperimentExperimentState, String> ApplicationConversionServiceFactoryBean.getItxExperimentExperimentStateToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ItxExperimentExperimentState, java.lang.String>() {
            public String convert(ItxExperimentExperimentState itxExperimentExperimentState) {
                return new StringBuilder().append(itxExperimentExperimentState.getRecordedBy()).append(' ').append(itxExperimentExperimentState.getRecordedDate()).append(' ').append(itxExperimentExperimentState.getModifiedBy()).append(' ').append(itxExperimentExperimentState.getModifiedDate()).toString();
            }
        };
    }
    
    public Converter<Long, ItxExperimentExperimentState> ApplicationConversionServiceFactoryBean.getIdToItxExperimentExperimentStateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ItxExperimentExperimentState>() {
            public com.labsynch.labseer.domain.ItxExperimentExperimentState convert(java.lang.Long id) {
                return ItxExperimentExperimentState.findItxExperimentExperimentState(id);
            }
        };
    }
    
    public Converter<String, ItxExperimentExperimentState> ApplicationConversionServiceFactoryBean.getStringToItxExperimentExperimentStateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ItxExperimentExperimentState>() {
            public com.labsynch.labseer.domain.ItxExperimentExperimentState convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ItxExperimentExperimentState.class);
            }
        };
    }
    
    public Converter<ItxExperimentExperimentValue, String> ApplicationConversionServiceFactoryBean.getItxExperimentExperimentValueToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ItxExperimentExperimentValue, java.lang.String>() {
            public String convert(ItxExperimentExperimentValue itxExperimentExperimentValue) {
                return new StringBuilder().append(itxExperimentExperimentValue.getLsType()).append(' ').append(itxExperimentExperimentValue.getLsKind()).append(' ').append(itxExperimentExperimentValue.getLsTypeAndKind()).append(' ').append(itxExperimentExperimentValue.getCodeOrigin()).toString();
            }
        };
    }
    
    public Converter<Long, ItxExperimentExperimentValue> ApplicationConversionServiceFactoryBean.getIdToItxExperimentExperimentValueConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ItxExperimentExperimentValue>() {
            public com.labsynch.labseer.domain.ItxExperimentExperimentValue convert(java.lang.Long id) {
                return ItxExperimentExperimentValue.findItxExperimentExperimentValue(id);
            }
        };
    }
    
    public Converter<String, ItxExperimentExperimentValue> ApplicationConversionServiceFactoryBean.getStringToItxExperimentExperimentValueConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ItxExperimentExperimentValue>() {
            public com.labsynch.labseer.domain.ItxExperimentExperimentValue convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ItxExperimentExperimentValue.class);
            }
        };
    }
    
    public Converter<ItxLsThingLsThing, String> ApplicationConversionServiceFactoryBean.getItxLsThingLsThingToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ItxLsThingLsThing, java.lang.String>() {
            public String convert(ItxLsThingLsThing itxLsThingLsThing) {
                return new StringBuilder().append(itxLsThingLsThing.getLsType()).append(' ').append(itxLsThingLsThing.getLsKind()).append(' ').append(itxLsThingLsThing.getLsTypeAndKind()).append(' ').append(itxLsThingLsThing.getCodeName()).toString();
            }
        };
    }
    
    public Converter<Long, ItxLsThingLsThing> ApplicationConversionServiceFactoryBean.getIdToItxLsThingLsThingConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ItxLsThingLsThing>() {
            public com.labsynch.labseer.domain.ItxLsThingLsThing convert(java.lang.Long id) {
                return ItxLsThingLsThing.findItxLsThingLsThing(id);
            }
        };
    }
    
    public Converter<String, ItxLsThingLsThing> ApplicationConversionServiceFactoryBean.getStringToItxLsThingLsThingConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ItxLsThingLsThing>() {
            public com.labsynch.labseer.domain.ItxLsThingLsThing convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ItxLsThingLsThing.class);
            }
        };
    }
    
    public Converter<ItxLsThingLsThingState, String> ApplicationConversionServiceFactoryBean.getItxLsThingLsThingStateToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ItxLsThingLsThingState, java.lang.String>() {
            public String convert(ItxLsThingLsThingState itxLsThingLsThingState) {
                return new StringBuilder().append(itxLsThingLsThingState.getRecordedBy()).append(' ').append(itxLsThingLsThingState.getRecordedDate()).append(' ').append(itxLsThingLsThingState.getModifiedBy()).append(' ').append(itxLsThingLsThingState.getModifiedDate()).toString();
            }
        };
    }
    
    public Converter<Long, ItxLsThingLsThingState> ApplicationConversionServiceFactoryBean.getIdToItxLsThingLsThingStateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ItxLsThingLsThingState>() {
            public com.labsynch.labseer.domain.ItxLsThingLsThingState convert(java.lang.Long id) {
                return ItxLsThingLsThingState.findItxLsThingLsThingState(id);
            }
        };
    }
    
    public Converter<String, ItxLsThingLsThingState> ApplicationConversionServiceFactoryBean.getStringToItxLsThingLsThingStateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ItxLsThingLsThingState>() {
            public com.labsynch.labseer.domain.ItxLsThingLsThingState convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ItxLsThingLsThingState.class);
            }
        };
    }
    
    public Converter<ItxLsThingLsThingValue, String> ApplicationConversionServiceFactoryBean.getItxLsThingLsThingValueToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ItxLsThingLsThingValue, java.lang.String>() {
            public String convert(ItxLsThingLsThingValue itxLsThingLsThingValue) {
                return new StringBuilder().append(itxLsThingLsThingValue.getLsType()).append(' ').append(itxLsThingLsThingValue.getLsKind()).append(' ').append(itxLsThingLsThingValue.getLsTypeAndKind()).append(' ').append(itxLsThingLsThingValue.getCodeOrigin()).toString();
            }
        };
    }
    
    public Converter<Long, ItxLsThingLsThingValue> ApplicationConversionServiceFactoryBean.getIdToItxLsThingLsThingValueConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ItxLsThingLsThingValue>() {
            public com.labsynch.labseer.domain.ItxLsThingLsThingValue convert(java.lang.Long id) {
                return ItxLsThingLsThingValue.findItxLsThingLsThingValue(id);
            }
        };
    }
    
    public Converter<String, ItxLsThingLsThingValue> ApplicationConversionServiceFactoryBean.getStringToItxLsThingLsThingValueConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ItxLsThingLsThingValue>() {
            public com.labsynch.labseer.domain.ItxLsThingLsThingValue convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ItxLsThingLsThingValue.class);
            }
        };
    }
    
    public Converter<LabelSequenceRole, String> ApplicationConversionServiceFactoryBean.getLabelSequenceRoleToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LabelSequenceRole, java.lang.String>() {
            public String convert(LabelSequenceRole labelSequenceRole) {
                return "(no displayable fields)";
            }
        };
    }
    
    public Converter<Long, LabelSequenceRole> ApplicationConversionServiceFactoryBean.getIdToLabelSequenceRoleConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LabelSequenceRole>() {
            public com.labsynch.labseer.domain.LabelSequenceRole convert(java.lang.Long id) {
                return LabelSequenceRole.findLabelSequenceRole(id);
            }
        };
    }
    
    public Converter<String, LabelSequenceRole> ApplicationConversionServiceFactoryBean.getStringToLabelSequenceRoleConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LabelSequenceRole>() {
            public com.labsynch.labseer.domain.LabelSequenceRole convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LabelSequenceRole.class);
            }
        };
    }
    
    public Converter<LsSeqAnlGrp, String> ApplicationConversionServiceFactoryBean.getLsSeqAnlGrpToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LsSeqAnlGrp, java.lang.String>() {
            public String convert(LsSeqAnlGrp lsSeqAnlGrp) {
                return "(no displayable fields)";
            }
        };
    }
    
    public Converter<Long, LsSeqAnlGrp> ApplicationConversionServiceFactoryBean.getIdToLsSeqAnlGrpConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LsSeqAnlGrp>() {
            public com.labsynch.labseer.domain.LsSeqAnlGrp convert(java.lang.Long id) {
                return LsSeqAnlGrp.findLsSeqAnlGrp(id);
            }
        };
    }
    
    public Converter<String, LsSeqAnlGrp> ApplicationConversionServiceFactoryBean.getStringToLsSeqAnlGrpConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LsSeqAnlGrp>() {
            public com.labsynch.labseer.domain.LsSeqAnlGrp convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LsSeqAnlGrp.class);
            }
        };
    }
    
    public Converter<LsSeqContainer, String> ApplicationConversionServiceFactoryBean.getLsSeqContainerToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LsSeqContainer, java.lang.String>() {
            public String convert(LsSeqContainer lsSeqContainer) {
                return "(no displayable fields)";
            }
        };
    }
    
    public Converter<Long, LsSeqContainer> ApplicationConversionServiceFactoryBean.getIdToLsSeqContainerConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LsSeqContainer>() {
            public com.labsynch.labseer.domain.LsSeqContainer convert(java.lang.Long id) {
                return LsSeqContainer.findLsSeqContainer(id);
            }
        };
    }
    
    public Converter<String, LsSeqContainer> ApplicationConversionServiceFactoryBean.getStringToLsSeqContainerConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LsSeqContainer>() {
            public com.labsynch.labseer.domain.LsSeqContainer convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LsSeqContainer.class);
            }
        };
    }
    
    public Converter<LsSeqExpt, String> ApplicationConversionServiceFactoryBean.getLsSeqExptToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LsSeqExpt, java.lang.String>() {
            public String convert(LsSeqExpt lsSeqExpt) {
                return "(no displayable fields)";
            }
        };
    }
    
    public Converter<Long, LsSeqExpt> ApplicationConversionServiceFactoryBean.getIdToLsSeqExptConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LsSeqExpt>() {
            public com.labsynch.labseer.domain.LsSeqExpt convert(java.lang.Long id) {
                return LsSeqExpt.findLsSeqExpt(id);
            }
        };
    }
    
    public Converter<String, LsSeqExpt> ApplicationConversionServiceFactoryBean.getStringToLsSeqExptConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LsSeqExpt>() {
            public com.labsynch.labseer.domain.LsSeqExpt convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LsSeqExpt.class);
            }
        };
    }
    
    public Converter<LsSeqItxCntrCntr, String> ApplicationConversionServiceFactoryBean.getLsSeqItxCntrCntrToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LsSeqItxCntrCntr, java.lang.String>() {
            public String convert(LsSeqItxCntrCntr lsSeqItxCntrCntr) {
                return "(no displayable fields)";
            }
        };
    }
    
    public Converter<Long, LsSeqItxCntrCntr> ApplicationConversionServiceFactoryBean.getIdToLsSeqItxCntrCntrConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LsSeqItxCntrCntr>() {
            public com.labsynch.labseer.domain.LsSeqItxCntrCntr convert(java.lang.Long id) {
                return LsSeqItxCntrCntr.findLsSeqItxCntrCntr(id);
            }
        };
    }
    
    public Converter<String, LsSeqItxCntrCntr> ApplicationConversionServiceFactoryBean.getStringToLsSeqItxCntrCntrConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LsSeqItxCntrCntr>() {
            public com.labsynch.labseer.domain.LsSeqItxCntrCntr convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LsSeqItxCntrCntr.class);
            }
        };
    }
    
    public Converter<LsSeqItxExperimentExperiment, String> ApplicationConversionServiceFactoryBean.getLsSeqItxExperimentExperimentToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LsSeqItxExperimentExperiment, java.lang.String>() {
            public String convert(LsSeqItxExperimentExperiment lsSeqItxExperimentExperiment) {
                return "(no displayable fields)";
            }
        };
    }
    
    public Converter<Long, LsSeqItxExperimentExperiment> ApplicationConversionServiceFactoryBean.getIdToLsSeqItxExperimentExperimentConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LsSeqItxExperimentExperiment>() {
            public com.labsynch.labseer.domain.LsSeqItxExperimentExperiment convert(java.lang.Long id) {
                return LsSeqItxExperimentExperiment.findLsSeqItxExperimentExperiment(id);
            }
        };
    }
    
    public Converter<String, LsSeqItxExperimentExperiment> ApplicationConversionServiceFactoryBean.getStringToLsSeqItxExperimentExperimentConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LsSeqItxExperimentExperiment>() {
            public com.labsynch.labseer.domain.LsSeqItxExperimentExperiment convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LsSeqItxExperimentExperiment.class);
            }
        };
    }
    
    public Converter<LsSeqItxLsThingLsThing, String> ApplicationConversionServiceFactoryBean.getLsSeqItxLsThingLsThingToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LsSeqItxLsThingLsThing, java.lang.String>() {
            public String convert(LsSeqItxLsThingLsThing lsSeqItxLsThingLsThing) {
                return "(no displayable fields)";
            }
        };
    }
    
    public Converter<Long, LsSeqItxLsThingLsThing> ApplicationConversionServiceFactoryBean.getIdToLsSeqItxLsThingLsThingConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LsSeqItxLsThingLsThing>() {
            public com.labsynch.labseer.domain.LsSeqItxLsThingLsThing convert(java.lang.Long id) {
                return LsSeqItxLsThingLsThing.findLsSeqItxLsThingLsThing(id);
            }
        };
    }
    
    public Converter<String, LsSeqItxLsThingLsThing> ApplicationConversionServiceFactoryBean.getStringToLsSeqItxLsThingLsThingConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LsSeqItxLsThingLsThing>() {
            public com.labsynch.labseer.domain.LsSeqItxLsThingLsThing convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LsSeqItxLsThingLsThing.class);
            }
        };
    }
    
    public Converter<LsSeqItxProtocolProtocol, String> ApplicationConversionServiceFactoryBean.getLsSeqItxProtocolProtocolToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LsSeqItxProtocolProtocol, java.lang.String>() {
            public String convert(LsSeqItxProtocolProtocol lsSeqItxProtocolProtocol) {
                return "(no displayable fields)";
            }
        };
    }
    
    public Converter<Long, LsSeqItxProtocolProtocol> ApplicationConversionServiceFactoryBean.getIdToLsSeqItxProtocolProtocolConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LsSeqItxProtocolProtocol>() {
            public com.labsynch.labseer.domain.LsSeqItxProtocolProtocol convert(java.lang.Long id) {
                return LsSeqItxProtocolProtocol.findLsSeqItxProtocolProtocol(id);
            }
        };
    }
    
    public Converter<String, LsSeqItxProtocolProtocol> ApplicationConversionServiceFactoryBean.getStringToLsSeqItxProtocolProtocolConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LsSeqItxProtocolProtocol>() {
            public com.labsynch.labseer.domain.LsSeqItxProtocolProtocol convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LsSeqItxProtocolProtocol.class);
            }
        };
    }
    
    public Converter<LsSeqItxSubjCntr, String> ApplicationConversionServiceFactoryBean.getLsSeqItxSubjCntrToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LsSeqItxSubjCntr, java.lang.String>() {
            public String convert(LsSeqItxSubjCntr lsSeqItxSubjCntr) {
                return "(no displayable fields)";
            }
        };
    }
    
    public Converter<Long, LsSeqItxSubjCntr> ApplicationConversionServiceFactoryBean.getIdToLsSeqItxSubjCntrConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LsSeqItxSubjCntr>() {
            public com.labsynch.labseer.domain.LsSeqItxSubjCntr convert(java.lang.Long id) {
                return LsSeqItxSubjCntr.findLsSeqItxSubjCntr(id);
            }
        };
    }
    
    public Converter<String, LsSeqItxSubjCntr> ApplicationConversionServiceFactoryBean.getStringToLsSeqItxSubjCntrConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LsSeqItxSubjCntr>() {
            public com.labsynch.labseer.domain.LsSeqItxSubjCntr convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LsSeqItxSubjCntr.class);
            }
        };
    }
    
    public Converter<LsSeqProtocol, String> ApplicationConversionServiceFactoryBean.getLsSeqProtocolToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LsSeqProtocol, java.lang.String>() {
            public String convert(LsSeqProtocol lsSeqProtocol) {
                return "(no displayable fields)";
            }
        };
    }
    
    public Converter<Long, LsSeqProtocol> ApplicationConversionServiceFactoryBean.getIdToLsSeqProtocolConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LsSeqProtocol>() {
            public com.labsynch.labseer.domain.LsSeqProtocol convert(java.lang.Long id) {
                return LsSeqProtocol.findLsSeqProtocol(id);
            }
        };
    }
    
    public Converter<String, LsSeqProtocol> ApplicationConversionServiceFactoryBean.getStringToLsSeqProtocolConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LsSeqProtocol>() {
            public com.labsynch.labseer.domain.LsSeqProtocol convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LsSeqProtocol.class);
            }
        };
    }
    
    public Converter<LsSeqSubject, String> ApplicationConversionServiceFactoryBean.getLsSeqSubjectToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LsSeqSubject, java.lang.String>() {
            public String convert(LsSeqSubject lsSeqSubject) {
                return "(no displayable fields)";
            }
        };
    }
    
    public Converter<Long, LsSeqSubject> ApplicationConversionServiceFactoryBean.getIdToLsSeqSubjectConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LsSeqSubject>() {
            public com.labsynch.labseer.domain.LsSeqSubject convert(java.lang.Long id) {
                return LsSeqSubject.findLsSeqSubject(id);
            }
        };
    }
    
    public Converter<String, LsSeqSubject> ApplicationConversionServiceFactoryBean.getStringToLsSeqSubjectConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LsSeqSubject>() {
            public com.labsynch.labseer.domain.LsSeqSubject convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LsSeqSubject.class);
            }
        };
    }
    
    public Converter<LsSeqTrtGrp, String> ApplicationConversionServiceFactoryBean.getLsSeqTrtGrpToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LsSeqTrtGrp, java.lang.String>() {
            public String convert(LsSeqTrtGrp lsSeqTrtGrp) {
                return "(no displayable fields)";
            }
        };
    }
    
    public Converter<Long, LsSeqTrtGrp> ApplicationConversionServiceFactoryBean.getIdToLsSeqTrtGrpConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LsSeqTrtGrp>() {
            public com.labsynch.labseer.domain.LsSeqTrtGrp convert(java.lang.Long id) {
                return LsSeqTrtGrp.findLsSeqTrtGrp(id);
            }
        };
    }
    
    public Converter<String, LsSeqTrtGrp> ApplicationConversionServiceFactoryBean.getStringToLsSeqTrtGrpConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LsSeqTrtGrp>() {
            public com.labsynch.labseer.domain.LsSeqTrtGrp convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LsSeqTrtGrp.class);
            }
        };
    }
    
    public Converter<Long, RoleKind> ApplicationConversionServiceFactoryBean.getIdToRoleKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.RoleKind>() {
            public com.labsynch.labseer.domain.RoleKind convert(java.lang.Long id) {
                return RoleKind.findRoleKind(id);
            }
        };
    }
    
    public Converter<String, RoleKind> ApplicationConversionServiceFactoryBean.getStringToRoleKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.RoleKind>() {
            public com.labsynch.labseer.domain.RoleKind convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), RoleKind.class);
            }
        };
    }
    
    public Converter<RoleType, String> ApplicationConversionServiceFactoryBean.getRoleTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.RoleType, java.lang.String>() {
            public String convert(RoleType roleType) {
                return new StringBuilder().append(roleType.getTypeName()).toString();
            }
        };
    }
    
    public Converter<Long, RoleType> ApplicationConversionServiceFactoryBean.getIdToRoleTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.RoleType>() {
            public com.labsynch.labseer.domain.RoleType convert(java.lang.Long id) {
                return RoleType.findRoleType(id);
            }
        };
    }
    
    public Converter<String, RoleType> ApplicationConversionServiceFactoryBean.getStringToRoleTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.RoleType>() {
            public com.labsynch.labseer.domain.RoleType convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), RoleType.class);
            }
        };
    }
    
    public Converter<TempSelectTable, String> ApplicationConversionServiceFactoryBean.getTempSelectTableToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.TempSelectTable, java.lang.String>() {
            public String convert(TempSelectTable tempSelectTable) {
                return new StringBuilder().append(tempSelectTable.getNumberVar()).append(' ').append(tempSelectTable.getStringVar()).append(' ').append(tempSelectTable.getLsTransaction()).append(' ').append(tempSelectTable.getRecordedDate()).toString();
            }
        };
    }
    
    public Converter<Long, TempSelectTable> ApplicationConversionServiceFactoryBean.getIdToTempSelectTableConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.TempSelectTable>() {
            public com.labsynch.labseer.domain.TempSelectTable convert(java.lang.Long id) {
                return TempSelectTable.findTempSelectTable(id);
            }
        };
    }
    
    public Converter<String, TempSelectTable> ApplicationConversionServiceFactoryBean.getStringToTempSelectTableConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.TempSelectTable>() {
            public com.labsynch.labseer.domain.TempSelectTable convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), TempSelectTable.class);
            }
        };
    }
    
}
