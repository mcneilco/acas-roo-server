package com.labsynch.labseer.web;

import java.util.Set;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.roo.addon.web.mvc.controller.converter.RooConversionService;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.AnalysisGroupLabel;
import com.labsynch.labseer.domain.AnalysisGroupState;
import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.domain.ApplicationSetting;
import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.domain.AuthorRole;
import com.labsynch.labseer.domain.CodeKind;
import com.labsynch.labseer.domain.CodeType;
import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.ContainerKind;
import com.labsynch.labseer.domain.ContainerLabel;
import com.labsynch.labseer.domain.ContainerState;
import com.labsynch.labseer.domain.ContainerType;
import com.labsynch.labseer.domain.ContainerValue;
import com.labsynch.labseer.domain.DDictKind;
import com.labsynch.labseer.domain.DDictType;
import com.labsynch.labseer.domain.DDictValue;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentKind;
import com.labsynch.labseer.domain.ExperimentLabel;
import com.labsynch.labseer.domain.ExperimentState;
import com.labsynch.labseer.domain.ExperimentType;
import com.labsynch.labseer.domain.ExperimentValue;
import com.labsynch.labseer.domain.FileThing;
import com.labsynch.labseer.domain.InteractionKind;
import com.labsynch.labseer.domain.InteractionType;
import com.labsynch.labseer.domain.ItxContainerContainer;
import com.labsynch.labseer.domain.ItxContainerContainerState;
import com.labsynch.labseer.domain.ItxContainerContainerValue;
import com.labsynch.labseer.domain.ItxProtocolProtocol;
import com.labsynch.labseer.domain.ItxProtocolProtocolState;
import com.labsynch.labseer.domain.ItxProtocolProtocolValue;
import com.labsynch.labseer.domain.ItxSubjectContainer;
import com.labsynch.labseer.domain.ItxSubjectContainerState;
import com.labsynch.labseer.domain.ItxSubjectContainerValue;
import com.labsynch.labseer.domain.LabelKind;
import com.labsynch.labseer.domain.LabelSequence;
import com.labsynch.labseer.domain.LabelType;
import com.labsynch.labseer.domain.LsInteraction;
import com.labsynch.labseer.domain.LsRole;
import com.labsynch.labseer.domain.LsTag;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.LsThingLabel;
import com.labsynch.labseer.domain.LsThingState;
import com.labsynch.labseer.domain.LsThingValue;
import com.labsynch.labseer.domain.LsTransaction;
import com.labsynch.labseer.domain.OperatorKind;
import com.labsynch.labseer.domain.OperatorType;
import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.domain.ProtocolKind;
import com.labsynch.labseer.domain.ProtocolLabel;
import com.labsynch.labseer.domain.ProtocolState;
import com.labsynch.labseer.domain.ProtocolType;
import com.labsynch.labseer.domain.ProtocolValue;
import com.labsynch.labseer.domain.StateKind;
import com.labsynch.labseer.domain.StateType;
import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.SubjectLabel;
import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.domain.ThingKind;
import com.labsynch.labseer.domain.ThingPage;
import com.labsynch.labseer.domain.ThingPageArchive;
import com.labsynch.labseer.domain.ThingType;
import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.domain.TreatmentGroupLabel;
import com.labsynch.labseer.domain.TreatmentGroupState;
import com.labsynch.labseer.domain.TreatmentGroupValue;
import com.labsynch.labseer.domain.UncertaintyKind;
import com.labsynch.labseer.domain.UnitKind;
import com.labsynch.labseer.domain.UnitType;
import com.labsynch.labseer.domain.UpdateLog;
import com.labsynch.labseer.domain.ValueKind;
import com.labsynch.labseer.domain.ValueType;

@Configurable
/**
 * A central place to register application converters and formatters. 
 */
@RooConversionService
@Transactional
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
		// Register application converters and formatters
	}

	public Converter<AnalysisGroup, String> getAnalysisGroupToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.AnalysisGroup, java.lang.String>() {
            public String convert(AnalysisGroup analysisGroup) {
                return new StringBuilder().append(analysisGroup.getLsType()).append(' ').append(analysisGroup.getLsKind()).append(' ').append(analysisGroup.getLsTypeAndKind()).append(' ').append(analysisGroup.getCodeName()).toString();
            }
        };
    }

	public Converter<Long, AnalysisGroup> getIdToAnalysisGroupConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.AnalysisGroup>() {
            public com.labsynch.labseer.domain.AnalysisGroup convert(java.lang.Long id) {
                return AnalysisGroup.findAnalysisGroup(id);
            }
        };
    }

	public Converter<String, AnalysisGroup> getStringToAnalysisGroupConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.AnalysisGroup>() {
            public com.labsynch.labseer.domain.AnalysisGroup convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), AnalysisGroup.class);
            }
        };
    }

	public Converter<AnalysisGroupLabel, String> getAnalysisGroupLabelToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.AnalysisGroupLabel, java.lang.String>() {
            public String convert(AnalysisGroupLabel analysisGroupLabel) {
                return new StringBuilder().append(analysisGroupLabel.getLabelText()).append(' ').append(analysisGroupLabel.getRecordedBy()).append(' ').append(analysisGroupLabel.getRecordedDate()).append(' ').append(analysisGroupLabel.getModifiedDate()).toString();
            }
        };
    }

	public Converter<Long, AnalysisGroupLabel> getIdToAnalysisGroupLabelConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.AnalysisGroupLabel>() {
            public com.labsynch.labseer.domain.AnalysisGroupLabel convert(java.lang.Long id) {
                return AnalysisGroupLabel.findAnalysisGroupLabel(id);
            }
        };
    }

	public Converter<String, AnalysisGroupLabel> getStringToAnalysisGroupLabelConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.AnalysisGroupLabel>() {
            public com.labsynch.labseer.domain.AnalysisGroupLabel convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), AnalysisGroupLabel.class);
            }
        };
    }

	public Converter<AnalysisGroupState, String> getAnalysisGroupStateToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.AnalysisGroupState, java.lang.String>() {
            public String convert(AnalysisGroupState analysisGroupState) {
                return new StringBuilder().append(analysisGroupState.getRecordedBy()).append(' ').append(analysisGroupState.getRecordedDate()).append(' ').append(analysisGroupState.getModifiedBy()).append(' ').append(analysisGroupState.getModifiedDate()).toString();
            }
        };
    }

	public Converter<Long, AnalysisGroupState> getIdToAnalysisGroupStateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.AnalysisGroupState>() {
            public com.labsynch.labseer.domain.AnalysisGroupState convert(java.lang.Long id) {
                return AnalysisGroupState.findAnalysisGroupState(id);
            }
        };
    }

	public Converter<String, AnalysisGroupState> getStringToAnalysisGroupStateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.AnalysisGroupState>() {
            public com.labsynch.labseer.domain.AnalysisGroupState convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), AnalysisGroupState.class);
            }
        };
    }

	public Converter<AnalysisGroupValue, String> getAnalysisGroupValueToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.AnalysisGroupValue, java.lang.String>() {
            public String convert(AnalysisGroupValue analysisGroupValue) {
                return new StringBuilder().append(analysisGroupValue.getLsType()).append(' ').append(analysisGroupValue.getLsKind()).append(' ').append(analysisGroupValue.getLsTypeAndKind()).append(' ').append(analysisGroupValue.getStringValue()).toString();
            }
        };
    }

	public Converter<Long, AnalysisGroupValue> getIdToAnalysisGroupValueConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.AnalysisGroupValue>() {
            public com.labsynch.labseer.domain.AnalysisGroupValue convert(java.lang.Long id) {
                return AnalysisGroupValue.findAnalysisGroupValue(id);
            }
        };
    }

	public Converter<String, AnalysisGroupValue> getStringToAnalysisGroupValueConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.AnalysisGroupValue>() {
            public com.labsynch.labseer.domain.AnalysisGroupValue convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), AnalysisGroupValue.class);
            }
        };
    }

	public Converter<ApplicationSetting, String> getApplicationSettingToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ApplicationSetting, java.lang.String>() {
            public String convert(ApplicationSetting applicationSetting) {
                return new StringBuilder().append(applicationSetting.getPropName()).append(' ').append(applicationSetting.getPropValue()).append(' ').append(applicationSetting.getComments()).append(' ').append(applicationSetting.getRecordedDate()).toString();
            }
        };
    }

	public Converter<Long, ApplicationSetting> getIdToApplicationSettingConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ApplicationSetting>() {
            public com.labsynch.labseer.domain.ApplicationSetting convert(java.lang.Long id) {
                return ApplicationSetting.findApplicationSetting(id);
            }
        };
    }

	public Converter<String, ApplicationSetting> getStringToApplicationSettingConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ApplicationSetting>() {
            public com.labsynch.labseer.domain.ApplicationSetting convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ApplicationSetting.class);
            }
        };
    }


	public Converter<Container, String> getContainerToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.Container, java.lang.String>() {
            public String convert(Container container) {
                return new StringBuilder().append(container.getId()).toString();
            }
        };
    }

	public Converter<Long, Container> getIdToContainerConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.Container>() {
            public com.labsynch.labseer.domain.Container convert(java.lang.Long id) {
                return Container.findContainer(id);
            }
        };
    }

	public Converter<String, Container> getStringToContainerConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.Container>() {
            public com.labsynch.labseer.domain.Container convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Container.class);
            }
        };
    }

	public Converter<ContainerKind, String> getContainerKindToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ContainerKind, java.lang.String>() {
            public String convert(ContainerKind containerKind) {
                return new StringBuilder().append(containerKind.getKindName()).append(' ').append(containerKind.getLsTypeAndKind()).toString();
            }
        };
    }

	public Converter<Long, ContainerKind> getIdToContainerKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ContainerKind>() {
            public com.labsynch.labseer.domain.ContainerKind convert(java.lang.Long id) {
                return ContainerKind.findContainerKind(id);
            }
        };
    }

	public Converter<String, ContainerKind> getStringToContainerKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ContainerKind>() {
            public com.labsynch.labseer.domain.ContainerKind convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ContainerKind.class);
            }
        };
    }

	public Converter<ContainerLabel, String> getContainerLabelToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ContainerLabel, java.lang.String>() {
            public String convert(ContainerLabel containerLabel) {
                return new StringBuilder().append(containerLabel.getLabelText()).append(' ').append(containerLabel.getRecordedBy()).append(' ').append(containerLabel.getRecordedDate()).append(' ').append(containerLabel.getModifiedDate()).toString();
            }
        };
    }

	public Converter<Long, ContainerLabel> getIdToContainerLabelConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ContainerLabel>() {
            public com.labsynch.labseer.domain.ContainerLabel convert(java.lang.Long id) {
                return ContainerLabel.findContainerLabel(id);
            }
        };
    }

	public Converter<String, ContainerLabel> getStringToContainerLabelConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ContainerLabel>() {
            public com.labsynch.labseer.domain.ContainerLabel convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ContainerLabel.class);
            }
        };
    }

	public Converter<ContainerState, String> getContainerStateToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ContainerState, java.lang.String>() {
            public String convert(ContainerState containerState) {
                return new StringBuilder().append(containerState.getRecordedBy()).append(' ').append(containerState.getRecordedDate()).append(' ').append(containerState.getModifiedBy()).append(' ').append(containerState.getModifiedDate()).toString();
            }
        };
    }

	public Converter<Long, ContainerState> getIdToContainerStateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ContainerState>() {
            public com.labsynch.labseer.domain.ContainerState convert(java.lang.Long id) {
                return ContainerState.findContainerState(id);
            }
        };
    }

	public Converter<String, ContainerState> getStringToContainerStateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ContainerState>() {
            public com.labsynch.labseer.domain.ContainerState convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ContainerState.class);
            }
        };
    }

	public Converter<ContainerType, String> getContainerTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ContainerType, java.lang.String>() {
            public String convert(ContainerType containerType) {
                return new StringBuilder().append(containerType.getTypeName()).toString();
            }
        };
    }

	public Converter<Long, ContainerType> getIdToContainerTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ContainerType>() {
            public com.labsynch.labseer.domain.ContainerType convert(java.lang.Long id) {
                return ContainerType.findContainerType(id);
            }
        };
    }

	public Converter<String, ContainerType> getStringToContainerTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ContainerType>() {
            public com.labsynch.labseer.domain.ContainerType convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ContainerType.class);
            }
        };
    }

	public Converter<ContainerValue, String> getContainerValueToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ContainerValue, java.lang.String>() {
            public String convert(ContainerValue containerValue) {
                return new StringBuilder().append(containerValue.getLsType()).append(' ').append(containerValue.getLsKind()).append(' ').append(containerValue.getLsTypeAndKind()).append(' ').append(containerValue.getStringValue()).toString();
            }
        };
    }

	public Converter<Long, ContainerValue> getIdToContainerValueConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ContainerValue>() {
            public com.labsynch.labseer.domain.ContainerValue convert(java.lang.Long id) {
                return ContainerValue.findContainerValue(id);
            }
        };
    }

	public Converter<String, ContainerValue> getStringToContainerValueConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ContainerValue>() {
            public com.labsynch.labseer.domain.ContainerValue convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ContainerValue.class);
            }
        };
    }

	@Transactional
	public Converter<Experiment, String> getExperimentToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.Experiment, java.lang.String>() {
            public String convert(Experiment experiment) {
                return new StringBuilder().append(experiment.getLsType()).append(' ').append(experiment.getLsKind()).append(' ').append(experiment.getLsTypeAndKind()).append(' ').append(experiment.getCodeName()).toString();
            }
        };
    }

	@Transactional
	public Converter<Long, Experiment> getIdToExperimentConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.Experiment>() {
            public com.labsynch.labseer.domain.Experiment convert(java.lang.Long id) {
                return Experiment.findExperiment(id);
            }
        };
    }

	@Transactional
	public Converter<String, Experiment> getStringToExperimentConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.Experiment>() {
            public com.labsynch.labseer.domain.Experiment convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Experiment.class);
            }
        };
    }

	public Converter<ExperimentKind, String> getExperimentKindToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ExperimentKind, java.lang.String>() {
            public String convert(ExperimentKind experimentKind) {
                return new StringBuilder().append(experimentKind.getKindName()).append(' ').append(experimentKind.getLsTypeAndKind()).toString();
            }
        };
    }

	public Converter<Long, ExperimentKind> getIdToExperimentKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ExperimentKind>() {
            public com.labsynch.labseer.domain.ExperimentKind convert(java.lang.Long id) {
                return ExperimentKind.findExperimentKind(id);
            }
        };
    }

	public Converter<String, ExperimentKind> getStringToExperimentKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ExperimentKind>() {
            public com.labsynch.labseer.domain.ExperimentKind convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ExperimentKind.class);
            }
        };
    }

	public Converter<ExperimentLabel, String> getExperimentLabelToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ExperimentLabel, java.lang.String>() {
            public String convert(ExperimentLabel experimentLabel) {
                return new StringBuilder().append(experimentLabel.getLabelText()).append(' ').append(experimentLabel.getRecordedBy()).append(' ').append(experimentLabel.getRecordedDate()).append(' ').append(experimentLabel.getModifiedDate()).toString();
            }
        };
    }

	public Converter<Long, ExperimentLabel> getIdToExperimentLabelConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ExperimentLabel>() {
            public com.labsynch.labseer.domain.ExperimentLabel convert(java.lang.Long id) {
                return ExperimentLabel.findExperimentLabel(id);
            }
        };
    }

	public Converter<String, ExperimentLabel> getStringToExperimentLabelConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ExperimentLabel>() {
            public com.labsynch.labseer.domain.ExperimentLabel convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ExperimentLabel.class);
            }
        };
    }

	@Transactional
	public Converter<ExperimentState, String> getExperimentStateToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ExperimentState, java.lang.String>() {
            public String convert(ExperimentState experimentState) {
                return new StringBuilder().append(experimentState.getRecordedBy()).append(' ').append(experimentState.getRecordedDate()).append(' ').append(experimentState.getModifiedBy()).append(' ').append(experimentState.getModifiedDate()).toString();
            }
        };
    }

	@Transactional
	public Converter<Long, ExperimentState> getIdToExperimentStateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ExperimentState>() {
            public com.labsynch.labseer.domain.ExperimentState convert(java.lang.Long id) {
                return ExperimentState.findExperimentState(id);
            }
        };
    }

	@Transactional
	public Converter<String, ExperimentState> getStringToExperimentStateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ExperimentState>() {
            public com.labsynch.labseer.domain.ExperimentState convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ExperimentState.class);
            }
        };
    }

	public Converter<ExperimentType, String> getExperimentTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ExperimentType, java.lang.String>() {
            public String convert(ExperimentType experimentType) {
                return new StringBuilder().append(experimentType.getTypeName()).toString();
            }
        };
    }

	public Converter<Long, ExperimentType> getIdToExperimentTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ExperimentType>() {
            public com.labsynch.labseer.domain.ExperimentType convert(java.lang.Long id) {
                return ExperimentType.findExperimentType(id);
            }
        };
    }

	public Converter<String, ExperimentType> getStringToExperimentTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ExperimentType>() {
            public com.labsynch.labseer.domain.ExperimentType convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ExperimentType.class);
            }
        };
    }

	public Converter<ExperimentValue, String> getExperimentValueToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ExperimentValue, java.lang.String>() {
            public String convert(ExperimentValue experimentValue) {
                return new StringBuilder().append(experimentValue.getLsType()).append(' ').append(experimentValue.getLsKind()).append(' ').append(experimentValue.getLsTypeAndKind()).append(' ').append(experimentValue.getStringValue()).toString();
            }
        };
    }

	public Converter<Long, ExperimentValue> getIdToExperimentValueConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ExperimentValue>() {
            public com.labsynch.labseer.domain.ExperimentValue convert(java.lang.Long id) {
                return ExperimentValue.findExperimentValue(id);
            }
        };
    }

	public Converter<String, ExperimentValue> getStringToExperimentValueConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ExperimentValue>() {
            public com.labsynch.labseer.domain.ExperimentValue convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ExperimentValue.class);
            }
        };
    }

	public Converter<FileThing, String> getFileThingToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.FileThing, java.lang.String>() {
            public String convert(FileThing fileThing) {
                return new StringBuilder().append(fileThing.getLsType()).append(' ').append(fileThing.getLsKind()).append(' ').append(fileThing.getLsTypeAndKind()).append(' ').append(fileThing.getCodeName()).toString();
            }
        };
    }

	public Converter<Long, FileThing> getIdToFileThingConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.FileThing>() {
            public com.labsynch.labseer.domain.FileThing convert(java.lang.Long id) {
                return FileThing.findFileThing(id);
            }
        };
    }

	public Converter<String, FileThing> getStringToFileThingConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.FileThing>() {
            public com.labsynch.labseer.domain.FileThing convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), FileThing.class);
            }
        };
    }

	public Converter<InteractionKind, String> getInteractionKindToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.InteractionKind, java.lang.String>() {
            public String convert(InteractionKind interactionKind) {
                return new StringBuilder().append(interactionKind.getKindName()).append(' ').append(interactionKind.getLsTypeAndKind()).toString();
            }
        };
    }

	public Converter<Long, InteractionKind> getIdToInteractionKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.InteractionKind>() {
            public com.labsynch.labseer.domain.InteractionKind convert(java.lang.Long id) {
                return InteractionKind.findInteractionKind(id);
            }
        };
    }

	public Converter<String, InteractionKind> getStringToInteractionKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.InteractionKind>() {
            public com.labsynch.labseer.domain.InteractionKind convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), InteractionKind.class);
            }
        };
    }

	public Converter<InteractionType, String> getInteractionTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.InteractionType, java.lang.String>() {
            public String convert(InteractionType interactionType) {
                return new StringBuilder().append(interactionType.getTypeName()).append(' ').append(interactionType.getTypeVerb()).toString();
            }
        };
    }

	public Converter<Long, InteractionType> getIdToInteractionTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.InteractionType>() {
            public com.labsynch.labseer.domain.InteractionType convert(java.lang.Long id) {
                return InteractionType.findInteractionType(id);
            }
        };
    }

	public Converter<String, InteractionType> getStringToInteractionTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.InteractionType>() {
            public com.labsynch.labseer.domain.InteractionType convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), InteractionType.class);
            }
        };
    }

	public Converter<ItxContainerContainer, String> getItxContainerContainerToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ItxContainerContainer, java.lang.String>() {
            public String convert(ItxContainerContainer itxContainerContainer) {
                return new StringBuilder().append(itxContainerContainer.getLsType()).append(' ').append(itxContainerContainer.getLsKind()).append(' ').append(itxContainerContainer.getLsTypeAndKind()).append(' ').append(itxContainerContainer.getCodeName()).toString();
            }
        };
    }

	public Converter<Long, ItxContainerContainer> getIdToItxContainerContainerConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ItxContainerContainer>() {
            public com.labsynch.labseer.domain.ItxContainerContainer convert(java.lang.Long id) {
                return ItxContainerContainer.findItxContainerContainer(id);
            }
        };
    }

	public Converter<String, ItxContainerContainer> getStringToItxContainerContainerConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ItxContainerContainer>() {
            public com.labsynch.labseer.domain.ItxContainerContainer convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ItxContainerContainer.class);
            }
        };
    }

	public Converter<ItxContainerContainerState, String> getItxContainerContainerStateToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ItxContainerContainerState, java.lang.String>() {
            public String convert(ItxContainerContainerState itxContainerContainerState) {
                return new StringBuilder().append(itxContainerContainerState.getRecordedBy()).append(' ').append(itxContainerContainerState.getRecordedDate()).append(' ').append(itxContainerContainerState.getModifiedBy()).append(' ').append(itxContainerContainerState.getModifiedDate()).toString();
            }
        };
    }

	public Converter<Long, ItxContainerContainerState> getIdToItxContainerContainerStateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ItxContainerContainerState>() {
            public com.labsynch.labseer.domain.ItxContainerContainerState convert(java.lang.Long id) {
                return ItxContainerContainerState.findItxContainerContainerState(id);
            }
        };
    }

	public Converter<String, ItxContainerContainerState> getStringToItxContainerContainerStateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ItxContainerContainerState>() {
            public com.labsynch.labseer.domain.ItxContainerContainerState convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ItxContainerContainerState.class);
            }
        };
    }

	public Converter<ItxContainerContainerValue, String> getItxContainerContainerValueToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ItxContainerContainerValue, java.lang.String>() {
            public String convert(ItxContainerContainerValue itxContainerContainerValue) {
                return new StringBuilder().append(itxContainerContainerValue.getLsType()).append(' ').append(itxContainerContainerValue.getLsKind()).append(' ').append(itxContainerContainerValue.getLsTypeAndKind()).append(' ').append(itxContainerContainerValue.getStringValue()).toString();
            }
        };
    }

	public Converter<Long, ItxContainerContainerValue> getIdToItxContainerContainerValueConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ItxContainerContainerValue>() {
            public com.labsynch.labseer.domain.ItxContainerContainerValue convert(java.lang.Long id) {
                return ItxContainerContainerValue.findItxContainerContainerValue(id);
            }
        };
    }

	public Converter<String, ItxContainerContainerValue> getStringToItxContainerContainerValueConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ItxContainerContainerValue>() {
            public com.labsynch.labseer.domain.ItxContainerContainerValue convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ItxContainerContainerValue.class);
            }
        };
    }

	public Converter<ItxSubjectContainer, String> getItxSubjectContainerToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ItxSubjectContainer, java.lang.String>() {
            public String convert(ItxSubjectContainer itxSubjectContainer) {
                return new StringBuilder().append(itxSubjectContainer.getLsType()).append(' ').append(itxSubjectContainer.getLsKind()).append(' ').append(itxSubjectContainer.getLsTypeAndKind()).append(' ').append(itxSubjectContainer.getCodeName()).toString();
            }
        };
    }

	public Converter<Long, ItxSubjectContainer> getIdToItxSubjectContainerConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ItxSubjectContainer>() {
            public com.labsynch.labseer.domain.ItxSubjectContainer convert(java.lang.Long id) {
                return ItxSubjectContainer.findItxSubjectContainer(id);
            }
        };
    }

	public Converter<String, ItxSubjectContainer> getStringToItxSubjectContainerConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ItxSubjectContainer>() {
            public com.labsynch.labseer.domain.ItxSubjectContainer convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ItxSubjectContainer.class);
            }
        };
    }

	public Converter<ItxSubjectContainerState, String> getItxSubjectContainerStateToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ItxSubjectContainerState, java.lang.String>() {
            public String convert(ItxSubjectContainerState itxSubjectContainerState) {
                return new StringBuilder().append(itxSubjectContainerState.getRecordedBy()).append(' ').append(itxSubjectContainerState.getRecordedDate()).append(' ').append(itxSubjectContainerState.getModifiedBy()).append(' ').append(itxSubjectContainerState.getModifiedDate()).toString();
            }
        };
    }

	public Converter<Long, ItxSubjectContainerState> getIdToItxSubjectContainerStateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ItxSubjectContainerState>() {
            public com.labsynch.labseer.domain.ItxSubjectContainerState convert(java.lang.Long id) {
                return ItxSubjectContainerState.findItxSubjectContainerState(id);
            }
        };
    }

	public Converter<String, ItxSubjectContainerState> getStringToItxSubjectContainerStateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ItxSubjectContainerState>() {
            public com.labsynch.labseer.domain.ItxSubjectContainerState convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ItxSubjectContainerState.class);
            }
        };
    }

	public Converter<ItxSubjectContainerValue, String> getItxSubjectContainerValueToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ItxSubjectContainerValue, java.lang.String>() {
            public String convert(ItxSubjectContainerValue itxSubjectContainerValue) {
                return new StringBuilder().append(itxSubjectContainerValue.getLsType()).append(' ').append(itxSubjectContainerValue.getLsKind()).append(' ').append(itxSubjectContainerValue.getLsTypeAndKind()).append(' ').append(itxSubjectContainerValue.getStringValue()).toString();
            }
        };
    }

	public Converter<Long, ItxSubjectContainerValue> getIdToItxSubjectContainerValueConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ItxSubjectContainerValue>() {
            public com.labsynch.labseer.domain.ItxSubjectContainerValue convert(java.lang.Long id) {
                return ItxSubjectContainerValue.findItxSubjectContainerValue(id);
            }
        };
    }

	public Converter<String, ItxSubjectContainerValue> getStringToItxSubjectContainerValueConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ItxSubjectContainerValue>() {
            public com.labsynch.labseer.domain.ItxSubjectContainerValue convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ItxSubjectContainerValue.class);
            }
        };
    }

	public Converter<LabelKind, String> getLabelKindToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LabelKind, java.lang.String>() {
            public String convert(LabelKind labelKind) {
                return new StringBuilder().append(labelKind.getKindName()).append(' ').append(labelKind.getLsTypeAndKind()).toString();
            }
        };
    }

	public Converter<Long, LabelKind> getIdToLabelKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LabelKind>() {
            public com.labsynch.labseer.domain.LabelKind convert(java.lang.Long id) {
                return LabelKind.findLabelKind(id);
            }
        };
    }

	public Converter<String, LabelKind> getStringToLabelKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LabelKind>() {
            public com.labsynch.labseer.domain.LabelKind convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LabelKind.class);
            }
        };
    }

	public Converter<LabelSequence, String> getLabelSequenceToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LabelSequence, java.lang.String>() {
            public String convert(LabelSequence labelSequence) {
                return new StringBuilder().append(labelSequence.getThingTypeAndKind()).append(' ').append(labelSequence.getLabelTypeAndKind()).append(' ').append(labelSequence.getLabelPrefix()).append(' ').append(labelSequence.getLabelSeparator()).toString();
            }
        };
    }

	public Converter<Long, LabelSequence> getIdToLabelSequenceConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LabelSequence>() {
            public com.labsynch.labseer.domain.LabelSequence convert(java.lang.Long id) {
                return LabelSequence.findLabelSequence(id);
            }
        };
    }

	public Converter<String, LabelSequence> getStringToLabelSequenceConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LabelSequence>() {
            public com.labsynch.labseer.domain.LabelSequence convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LabelSequence.class);
            }
        };
    }

	public Converter<LabelType, String> getLabelTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LabelType, java.lang.String>() {
            public String convert(LabelType labelType) {
                return new StringBuilder().append(labelType.getTypeName()).toString();
            }
        };
    }

	public Converter<Long, LabelType> getIdToLabelTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LabelType>() {
            public com.labsynch.labseer.domain.LabelType convert(java.lang.Long id) {
                return LabelType.findLabelType(id);
            }
        };
    }

	public Converter<String, LabelType> getStringToLabelTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LabelType>() {
            public com.labsynch.labseer.domain.LabelType convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LabelType.class);
            }
        };
    }

	public Converter<LsInteraction, String> getLsInteractionToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LsInteraction, java.lang.String>() {
            public String convert(LsInteraction lsInteraction) {
                return new StringBuilder().append(lsInteraction.getLsType()).append(' ').append(lsInteraction.getLsKind()).append(' ').append(lsInteraction.getLsTypeAndKind()).append(' ').append(lsInteraction.getCodeName()).toString();
            }
        };
    }

	public Converter<Long, LsInteraction> getIdToLsInteractionConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LsInteraction>() {
            public com.labsynch.labseer.domain.LsInteraction convert(java.lang.Long id) {
                return LsInteraction.findLsInteraction(id);
            }
        };
    }

	public Converter<String, LsInteraction> getStringToLsInteractionConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LsInteraction>() {
            public com.labsynch.labseer.domain.LsInteraction convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LsInteraction.class);
            }
        };
    }

	public Converter<LsTransaction, String> getLsTransactionToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LsTransaction, java.lang.String>() {
            public String convert(LsTransaction lsTransaction) {
                return new StringBuilder().append(lsTransaction.getComments()).append(' ').append(lsTransaction.getRecordedDate()).toString();
            }
        };
    }

	public Converter<Long, AuthorRole> getIdToAuthorRoleConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.AuthorRole>() {
            public com.labsynch.labseer.domain.AuthorRole convert(java.lang.Long id) {
                return AuthorRole.findAuthorRole(id);
            }
        };
    }

//	public Converter<String, AuthorRole> getStringToAuthorRoleConverter() {
//        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.AuthorRole>() {
//            public com.labsynch.labseer.domain.AuthorRole convert(String id) {
//                return getObject().convert(getObject().convert(id, Long.class), AuthorRole.class);
//            }
//        };
//    }
//	
//	public Converter<Long, LsTransaction> getIdToLsTransactionConverter() {
//        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LsTransaction>() {
//            public com.labsynch.labseer.domain.LsTransaction convert(java.lang.Long id) {
//                return LsTransaction.findLsTransaction(id);
//            }
//        };
//    }

	public Converter<String, LsTransaction> getStringToLsTransactionConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LsTransaction>() {
            public com.labsynch.labseer.domain.LsTransaction convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LsTransaction.class);
            }
        };
    }

	public Converter<OperatorKind, String> getOperatorKindToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.OperatorKind, java.lang.String>() {
            public String convert(OperatorKind operatorKind) {
                return new StringBuilder().append(operatorKind.getKindName()).append(' ').append(operatorKind.getLsTypeAndKind()).toString();
            }
        };
    }

	public Converter<Long, OperatorKind> getIdToOperatorKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.OperatorKind>() {
            public com.labsynch.labseer.domain.OperatorKind convert(java.lang.Long id) {
                return OperatorKind.findOperatorKind(id);
            }
        };
    }

	public Converter<String, OperatorKind> getStringToOperatorKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.OperatorKind>() {
            public com.labsynch.labseer.domain.OperatorKind convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), OperatorKind.class);
            }
        };
    }

	public Converter<OperatorType, String> getOperatorTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.OperatorType, java.lang.String>() {
            public String convert(OperatorType operatorType) {
                return new StringBuilder().append(operatorType.getTypeName()).toString();
            }
        };
    }

	public Converter<Long, OperatorType> getIdToOperatorTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.OperatorType>() {
            public com.labsynch.labseer.domain.OperatorType convert(java.lang.Long id) {
                return OperatorType.findOperatorType(id);
            }
        };
    }

	public Converter<String, OperatorType> getStringToOperatorTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.OperatorType>() {
            public com.labsynch.labseer.domain.OperatorType convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), OperatorType.class);
            }
        };
    }

	public Converter<Protocol, String> getProtocolToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.Protocol, java.lang.String>() {
            public String convert(Protocol protocol) {
                return new StringBuilder().append(protocol.getLsType()).append(' ').append(protocol.getLsKind()).append(' ').append(protocol.getLsTypeAndKind()).append(' ').append(protocol.getCodeName()).toString();
            }
        };
    }

	public Converter<Long, Protocol> getIdToProtocolConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.Protocol>() {
            public com.labsynch.labseer.domain.Protocol convert(java.lang.Long id) {
                return Protocol.findProtocol(id);
            }
        };
    }

	public Converter<String, Protocol> getStringToProtocolConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.Protocol>() {
            public com.labsynch.labseer.domain.Protocol convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Protocol.class);
            }
        };
    }

	public Converter<ProtocolKind, String> getProtocolKindToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ProtocolKind, java.lang.String>() {
            public String convert(ProtocolKind protocolKind) {
                return new StringBuilder().append(protocolKind.getKindName()).append(' ').append(protocolKind.getLsTypeAndKind()).toString();
            }
        };
    }

	public Converter<Long, ProtocolKind> getIdToProtocolKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ProtocolKind>() {
            public com.labsynch.labseer.domain.ProtocolKind convert(java.lang.Long id) {
                return ProtocolKind.findProtocolKind(id);
            }
        };
    }

	public Converter<String, ProtocolKind> getStringToProtocolKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ProtocolKind>() {
            public com.labsynch.labseer.domain.ProtocolKind convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ProtocolKind.class);
            }
        };
    }

	public Converter<ProtocolLabel, String> getProtocolLabelToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ProtocolLabel, java.lang.String>() {
            public String convert(ProtocolLabel protocolLabel) {
                return new StringBuilder().append(protocolLabel.getLabelText()).append(' ').append(protocolLabel.getRecordedBy()).append(' ').append(protocolLabel.getRecordedDate()).append(' ').append(protocolLabel.getModifiedDate()).toString();
            }
        };
    }

	public Converter<Long, ProtocolLabel> getIdToProtocolLabelConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ProtocolLabel>() {
            public com.labsynch.labseer.domain.ProtocolLabel convert(java.lang.Long id) {
                return ProtocolLabel.findProtocolLabel(id);
            }
        };
    }

	public Converter<String, ProtocolLabel> getStringToProtocolLabelConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ProtocolLabel>() {
            public com.labsynch.labseer.domain.ProtocolLabel convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ProtocolLabel.class);
            }
        };
    }

	public Converter<ProtocolState, String> getProtocolStateToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ProtocolState, java.lang.String>() {
            public String convert(ProtocolState protocolState) {
                return new StringBuilder().append(protocolState.getRecordedBy()).append(' ').append(protocolState.getRecordedDate()).append(' ').append(protocolState.getModifiedBy()).append(' ').append(protocolState.getModifiedDate()).toString();
            }
        };
    }

	public Converter<Long, ProtocolState> getIdToProtocolStateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ProtocolState>() {
            public com.labsynch.labseer.domain.ProtocolState convert(java.lang.Long id) {
                return ProtocolState.findProtocolState(id);
            }
        };
    }

	public Converter<String, ProtocolState> getStringToProtocolStateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ProtocolState>() {
            public com.labsynch.labseer.domain.ProtocolState convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ProtocolState.class);
            }
        };
    }

	public Converter<ProtocolType, String> getProtocolTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ProtocolType, java.lang.String>() {
            public String convert(ProtocolType protocolType) {
                return new StringBuilder().append(protocolType.getTypeName()).toString();
            }
        };
    }

	public Converter<Long, ProtocolType> getIdToProtocolTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ProtocolType>() {
            public com.labsynch.labseer.domain.ProtocolType convert(java.lang.Long id) {
                return ProtocolType.findProtocolType(id);
            }
        };
    }

	public Converter<String, ProtocolType> getStringToProtocolTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ProtocolType>() {
            public com.labsynch.labseer.domain.ProtocolType convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ProtocolType.class);
            }
        };
    }

	public Converter<ProtocolValue, String> getProtocolValueToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ProtocolValue, java.lang.String>() {
            public String convert(ProtocolValue protocolValue) {
                return new StringBuilder().append(protocolValue.getLsType()).append(' ').append(protocolValue.getLsKind()).append(' ').append(protocolValue.getLsTypeAndKind()).append(' ').append(protocolValue.getStringValue()).toString();
            }
        };
    }

	public Converter<Long, ProtocolValue> getIdToProtocolValueConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ProtocolValue>() {
            public com.labsynch.labseer.domain.ProtocolValue convert(java.lang.Long id) {
                return ProtocolValue.findProtocolValue(id);
            }
        };
    }

	public Converter<String, ProtocolValue> getStringToProtocolValueConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ProtocolValue>() {
            public com.labsynch.labseer.domain.ProtocolValue convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ProtocolValue.class);
            }
        };
    }

	public Converter<StateKind, String> getStateKindToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.StateKind, java.lang.String>() {
            public String convert(StateKind stateKind) {
                return new StringBuilder().append(stateKind.getKindName()).append(' ').append(stateKind.getLsTypeAndKind()).toString();
            }
        };
    }

	public Converter<Long, StateKind> getIdToStateKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.StateKind>() {
            public com.labsynch.labseer.domain.StateKind convert(java.lang.Long id) {
                return StateKind.findStateKind(id);
            }
        };
    }

	public Converter<String, StateKind> getStringToStateKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.StateKind>() {
            public com.labsynch.labseer.domain.StateKind convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), StateKind.class);
            }
        };
    }

	public Converter<StateType, String> getStateTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.StateType, java.lang.String>() {
            public String convert(StateType stateType) {
                return new StringBuilder().append(stateType.getTypeName()).toString();
            }
        };
    }

	public Converter<Long, StateType> getIdToStateTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.StateType>() {
            public com.labsynch.labseer.domain.StateType convert(java.lang.Long id) {
                return StateType.findStateType(id);
            }
        };
    }

	public Converter<String, StateType> getStringToStateTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.StateType>() {
            public com.labsynch.labseer.domain.StateType convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), StateType.class);
            }
        };
    }

	public Converter<Subject, String> getSubjectToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.Subject, java.lang.String>() {
            public String convert(Subject subject) {
                return new StringBuilder().append(subject.getLsType()).append(' ').append(subject.getLsKind()).append(' ').append(subject.getLsTypeAndKind()).append(' ').append(subject.getCodeName()).toString();
            }
        };
    }

	public Converter<Long, Subject> getIdToSubjectConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.Subject>() {
            public com.labsynch.labseer.domain.Subject convert(java.lang.Long id) {
                return Subject.findSubject(id);
            }
        };
    }

	public Converter<String, Subject> getStringToSubjectConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.Subject>() {
            public com.labsynch.labseer.domain.Subject convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Subject.class);
            }
        };
    }

	public Converter<SubjectLabel, String> getSubjectLabelToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.SubjectLabel, java.lang.String>() {
            public String convert(SubjectLabel subjectLabel) {
                return new StringBuilder().append(subjectLabel.getLabelText()).append(' ').append(subjectLabel.getRecordedBy()).append(' ').append(subjectLabel.getRecordedDate()).append(' ').append(subjectLabel.getModifiedDate()).toString();
            }
        };
    }

	public Converter<Long, SubjectLabel> getIdToSubjectLabelConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.SubjectLabel>() {
            public com.labsynch.labseer.domain.SubjectLabel convert(java.lang.Long id) {
                return SubjectLabel.findSubjectLabel(id);
            }
        };
    }

	public Converter<String, SubjectLabel> getStringToSubjectLabelConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.SubjectLabel>() {
            public com.labsynch.labseer.domain.SubjectLabel convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), SubjectLabel.class);
            }
        };
    }

	public Converter<SubjectState, String> getSubjectStateToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.SubjectState, java.lang.String>() {
            public String convert(SubjectState subjectState) {
                return new StringBuilder().append(subjectState.getRecordedBy()).append(' ').append(subjectState.getRecordedDate()).append(' ').append(subjectState.getModifiedBy()).append(' ').append(subjectState.getModifiedDate()).toString();
            }
        };
    }

	public Converter<Long, SubjectState> getIdToSubjectStateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.SubjectState>() {
            public com.labsynch.labseer.domain.SubjectState convert(java.lang.Long id) {
                return SubjectState.findSubjectState(id);
            }
        };
    }

	public Converter<String, SubjectState> getStringToSubjectStateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.SubjectState>() {
            public com.labsynch.labseer.domain.SubjectState convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), SubjectState.class);
            }
        };
    }

	public Converter<SubjectValue, String> getSubjectValueToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.SubjectValue, java.lang.String>() {
            public String convert(SubjectValue subjectValue) {
                return new StringBuilder().append(subjectValue.getLsType()).append(' ').append(subjectValue.getLsKind()).append(' ').append(subjectValue.getLsTypeAndKind()).append(' ').append(subjectValue.getStringValue()).toString();
            }
        };
    }

	public Converter<Long, SubjectValue> getIdToSubjectValueConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.SubjectValue>() {
            public com.labsynch.labseer.domain.SubjectValue convert(java.lang.Long id) {
                return SubjectValue.findSubjectValue(id);
            }
        };
    }

	public Converter<String, SubjectValue> getStringToSubjectValueConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.SubjectValue>() {
            public com.labsynch.labseer.domain.SubjectValue convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), SubjectValue.class);
            }
        };
    }

	public Converter<ThingKind, String> getThingKindToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ThingKind, java.lang.String>() {
            public String convert(ThingKind thingKind) {
                return new StringBuilder().append(thingKind.getKindName()).append(' ').append(thingKind.getLsTypeAndKind()).toString();
            }
        };
    }

	public Converter<Long, ThingKind> getIdToThingKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ThingKind>() {
            public com.labsynch.labseer.domain.ThingKind convert(java.lang.Long id) {
                return ThingKind.findThingKind(id);
            }
        };
    }

	public Converter<String, ThingKind> getStringToThingKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ThingKind>() {
            public com.labsynch.labseer.domain.ThingKind convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ThingKind.class);
            }
        };
    }

	public Converter<ThingPage, String> getThingPageToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ThingPage, java.lang.String>() {
            public String convert(ThingPage thingPage) {
                return new StringBuilder().append(thingPage.getPageName()).append(' ').append(thingPage.getRecordedBy()).append(' ').append(thingPage.getRecordedDate()).append(' ').append(thingPage.getPageContent()).toString();
            }
        };
    }

	public Converter<Long, ThingPage> getIdToThingPageConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ThingPage>() {
            public com.labsynch.labseer.domain.ThingPage convert(java.lang.Long id) {
                return ThingPage.findThingPage(id);
            }
        };
    }

	public Converter<String, ThingPage> getStringToThingPageConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ThingPage>() {
            public com.labsynch.labseer.domain.ThingPage convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ThingPage.class);
            }
        };
    }

	public Converter<ThingPageArchive, String> getThingPageArchiveToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ThingPageArchive, java.lang.String>() {
            public String convert(ThingPageArchive thingPageArchive) {
                return new StringBuilder().append(thingPageArchive.getPageName()).append(' ').append(thingPageArchive.getRecordedBy()).append(' ').append(thingPageArchive.getRecordedDate()).append(' ').append(thingPageArchive.getPageContent()).toString();
            }
        };
    }

	public Converter<Long, ThingPageArchive> getIdToThingPageArchiveConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ThingPageArchive>() {
            public com.labsynch.labseer.domain.ThingPageArchive convert(java.lang.Long id) {
                return ThingPageArchive.findThingPageArchive(id);
            }
        };
    }

	public Converter<String, ThingPageArchive> getStringToThingPageArchiveConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ThingPageArchive>() {
            public com.labsynch.labseer.domain.ThingPageArchive convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ThingPageArchive.class);
            }
        };
    }

	public Converter<ThingType, String> getThingTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ThingType, java.lang.String>() {
            public String convert(ThingType thingType) {
                return new StringBuilder().append(thingType.getTypeName()).toString();
            }
        };
    }

	public Converter<Long, ThingType> getIdToThingTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ThingType>() {
            public com.labsynch.labseer.domain.ThingType convert(java.lang.Long id) {
                return ThingType.findThingType(id);
            }
        };
    }

	public Converter<String, ThingType> getStringToThingTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ThingType>() {
            public com.labsynch.labseer.domain.ThingType convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ThingType.class);
            }
        };
    }

	public Converter<TreatmentGroup, String> getTreatmentGroupToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.TreatmentGroup, java.lang.String>() {
            public String convert(TreatmentGroup treatmentGroup) {
                return new StringBuilder().append(treatmentGroup.getLsType()).append(' ').append(treatmentGroup.getLsKind()).append(' ').append(treatmentGroup.getLsTypeAndKind()).append(' ').append(treatmentGroup.getCodeName()).toString();
            }
        };
    }

	public Converter<Long, TreatmentGroup> getIdToTreatmentGroupConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.TreatmentGroup>() {
            public com.labsynch.labseer.domain.TreatmentGroup convert(java.lang.Long id) {
                return TreatmentGroup.findTreatmentGroup(id);
            }
        };
    }

	public Converter<String, TreatmentGroup> getStringToTreatmentGroupConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.TreatmentGroup>() {
            public com.labsynch.labseer.domain.TreatmentGroup convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), TreatmentGroup.class);
            }
        };
    }

	public Converter<TreatmentGroupLabel, String> getTreatmentGroupLabelToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.TreatmentGroupLabel, java.lang.String>() {
            public String convert(TreatmentGroupLabel treatmentGroupLabel) {
                return new StringBuilder().append(treatmentGroupLabel.getLabelText()).append(' ').append(treatmentGroupLabel.getRecordedBy()).append(' ').append(treatmentGroupLabel.getRecordedDate()).append(' ').append(treatmentGroupLabel.getModifiedDate()).toString();
            }
        };
    }

	public Converter<Long, TreatmentGroupLabel> getIdToTreatmentGroupLabelConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.TreatmentGroupLabel>() {
            public com.labsynch.labseer.domain.TreatmentGroupLabel convert(java.lang.Long id) {
                return TreatmentGroupLabel.findTreatmentGroupLabel(id);
            }
        };
    }

	public Converter<String, TreatmentGroupLabel> getStringToTreatmentGroupLabelConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.TreatmentGroupLabel>() {
            public com.labsynch.labseer.domain.TreatmentGroupLabel convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), TreatmentGroupLabel.class);
            }
        };
    }

	public Converter<TreatmentGroupState, String> getTreatmentGroupStateToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.TreatmentGroupState, java.lang.String>() {
            public String convert(TreatmentGroupState treatmentGroupState) {
                return new StringBuilder().append(treatmentGroupState.getRecordedBy()).append(' ').append(treatmentGroupState.getRecordedDate()).append(' ').append(treatmentGroupState.getModifiedBy()).append(' ').append(treatmentGroupState.getModifiedDate()).toString();
            }
        };
    }

	public Converter<Long, TreatmentGroupState> getIdToTreatmentGroupStateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.TreatmentGroupState>() {
            public com.labsynch.labseer.domain.TreatmentGroupState convert(java.lang.Long id) {
                return TreatmentGroupState.findTreatmentGroupState(id);
            }
        };
    }

	public Converter<String, TreatmentGroupState> getStringToTreatmentGroupStateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.TreatmentGroupState>() {
            public com.labsynch.labseer.domain.TreatmentGroupState convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), TreatmentGroupState.class);
            }
        };
    }

	public Converter<TreatmentGroupValue, String> getTreatmentGroupValueToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.TreatmentGroupValue, java.lang.String>() {
            public String convert(TreatmentGroupValue treatmentGroupValue) {
                return new StringBuilder().append(treatmentGroupValue.getLsType()).append(' ').append(treatmentGroupValue.getLsKind()).append(' ').append(treatmentGroupValue.getLsTypeAndKind()).append(' ').append(treatmentGroupValue.getStringValue()).toString();
            }
        };
    }

	public Converter<Long, TreatmentGroupValue> getIdToTreatmentGroupValueConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.TreatmentGroupValue>() {
            public com.labsynch.labseer.domain.TreatmentGroupValue convert(java.lang.Long id) {
                return TreatmentGroupValue.findTreatmentGroupValue(id);
            }
        };
    }

	public Converter<String, TreatmentGroupValue> getStringToTreatmentGroupValueConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.TreatmentGroupValue>() {
            public com.labsynch.labseer.domain.TreatmentGroupValue convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), TreatmentGroupValue.class);
            }
        };
    }

	public Converter<UncertaintyKind, String> getUncertaintyKindToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.UncertaintyKind, java.lang.String>() {
            public String convert(UncertaintyKind uncertaintyKind) {
                return new StringBuilder().append(uncertaintyKind.getKindName()).toString();
            }
        };
    }

	public Converter<Long, UncertaintyKind> getIdToUncertaintyKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.UncertaintyKind>() {
            public com.labsynch.labseer.domain.UncertaintyKind convert(java.lang.Long id) {
                return UncertaintyKind.findUncertaintyKind(id);
            }
        };
    }

	public Converter<String, UncertaintyKind> getStringToUncertaintyKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.UncertaintyKind>() {
            public com.labsynch.labseer.domain.UncertaintyKind convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), UncertaintyKind.class);
            }
        };
    }

	public Converter<UnitKind, String> getUnitKindToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.UnitKind, java.lang.String>() {
            public String convert(UnitKind unitKind) {
                return new StringBuilder().append(unitKind.getKindName()).append(' ').append(unitKind.getLsTypeAndKind()).toString();
            }
        };
    }

	public Converter<Long, UnitKind> getIdToUnitKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.UnitKind>() {
            public com.labsynch.labseer.domain.UnitKind convert(java.lang.Long id) {
                return UnitKind.findUnitKind(id);
            }
        };
    }

	public Converter<String, UnitKind> getStringToUnitKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.UnitKind>() {
            public com.labsynch.labseer.domain.UnitKind convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), UnitKind.class);
            }
        };
    }

	public Converter<UnitType, String> getUnitTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.UnitType, java.lang.String>() {
            public String convert(UnitType unitType) {
                return new StringBuilder().append(unitType.getTypeName()).toString();
            }
        };
    }

	public Converter<Long, UnitType> getIdToUnitTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.UnitType>() {
            public com.labsynch.labseer.domain.UnitType convert(java.lang.Long id) {
                return UnitType.findUnitType(id);
            }
        };
    }

	public Converter<String, UnitType> getStringToUnitTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.UnitType>() {
            public com.labsynch.labseer.domain.UnitType convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), UnitType.class);
            }
        };
    }

	public Converter<ValueKind, String> getValueKindToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ValueKind, java.lang.String>() {
            public String convert(ValueKind valueKind) {
                return new StringBuilder().append(valueKind.getKindName()).append(' ').append(valueKind.getLsTypeAndKind()).toString();
            }
        };
    }

	public Converter<Long, ValueKind> getIdToValueKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ValueKind>() {
            public com.labsynch.labseer.domain.ValueKind convert(java.lang.Long id) {
                return ValueKind.findValueKind(id);
            }
        };
    }

	public Converter<String, ValueKind> getStringToValueKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ValueKind>() {
            public com.labsynch.labseer.domain.ValueKind convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ValueKind.class);
            }
        };
    }

	public Converter<ValueType, String> getValueTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ValueType, java.lang.String>() {
            public String convert(ValueType valueType) {
                return new StringBuilder().append(valueType.getTypeName()).toString();
            }
        };
    }

	public Converter<Long, ValueType> getIdToValueTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ValueType>() {
            public com.labsynch.labseer.domain.ValueType convert(java.lang.Long id) {
                return ValueType.findValueType(id);
            }
        };
    }

	public Converter<String, ValueType> getStringToValueTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ValueType>() {
            public com.labsynch.labseer.domain.ValueType convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ValueType.class);
            }
        };
    }
	
//	@Transactional
//	public Converter<String, Set<LsTag>> getStringToLsSetConverter() {
//        return new Converter<String, Set<LsTag>>() {
//            public Set<LsTag> convert(String id) {
//                return getObject().convert(getObject().convert(id, Long.class), LsTag.class);
//            }
//        };
//    }
	
//	@Transactional
//    public Converter<Set<Object>, String> getObjectSetConverter() {
//        return new Converter<Set<Object>, String>() {
//            public String convert(Set<Object> objects) {
//            	StringBuilder sb = new StringBuilder();
//            	for (Object ob : objects){
//            		sb.append(ob).append(' ');
//            	}
//                return sb.toString();
//            }
//        };
//    }
//	
//	@Transactional
//    public Converter<Set<LsTag>, String> getLsSetConverter() {
//        return new Converter<Set<LsTag>, String>() {
//            public String convert(Set<LsTag> lsTags) {
//            	StringBuilder sb = new StringBuilder();
//            	for (LsTag lt : lsTags){
//            		sb.append(lt.getTagText()).append(' ');
//            	}
//                return sb.toString();
//            }
//        };
//    }
// 
//	@Transactional
//    public Converter<Set<AnalysisGroup>, String> getLsAnalysisGroupConverter() {
//        return new Converter<Set<AnalysisGroup>, String>() {
//            public String convert(Set<AnalysisGroup> lsTags) {
//            	StringBuilder sb = new StringBuilder();
//            	for (AnalysisGroup lt : lsTags){
//            		sb.append(lt.getCodeName()).append(' ');
//            	}
//                return sb.toString();
//            }
//        };
//    }
	
//	@Transactional
//    public Converter<Set<Experiment>, String> getExperimentSetConverter() {
//        return new Converter<Set<Experiment>, String>() {
//            public String convert(Set<Experiment> experiments) {
//            	StringBuilder sb = new StringBuilder();
//            	for (Experiment exp : experiments){
//            		sb.append(exp.getId().toString()).append(' ').append(exp.getCodeName()).append(' ');
//            	}
//                return sb.toString();
//            }
//        };
//    }
	
//	@Transactional
//    public Converter<Set<Protocol>, String> getProtocolSetConverter() {
//        return new Converter<Set<Protocol>, String>() {
//            public String convert(Set<Protocol> protocols) {
//            	StringBuilder sb = new StringBuilder();
//            	for (Protocol prot : protocols){
//            		sb.append(prot.getId().toString()).append(' ').append(prot.getCodeName()).append(' ');
//            	}
//                return sb.toString();
//            }
//        };
//    }	
//	
	
	
	public void installLabelConverters(FormatterRegistry registry) {
//		registry.addConverter(getLsSetConverter());
		registry.addConverter(getLsTagToStringConverter());
        registry.addConverter(getIdToLsTagConverter());
		registry.addConverter(getAnalysisGroupToStringConverter());
        registry.addConverter(getIdToAnalysisGroupConverter());
        registry.addConverter(getStringToAnalysisGroupConverter());
        registry.addConverter(getAnalysisGroupLabelToStringConverter());
        registry.addConverter(getIdToAnalysisGroupLabelConverter());
        registry.addConverter(getStringToAnalysisGroupLabelConverter());
        registry.addConverter(getAnalysisGroupStateToStringConverter());
        registry.addConverter(getIdToAnalysisGroupStateConverter());
        registry.addConverter(getStringToAnalysisGroupStateConverter());
        registry.addConverter(getAnalysisGroupValueToStringConverter());
        registry.addConverter(getIdToAnalysisGroupValueConverter());
        registry.addConverter(getStringToAnalysisGroupValueConverter());
        registry.addConverter(getApplicationSettingToStringConverter());
        registry.addConverter(getIdToApplicationSettingConverter());
        registry.addConverter(getStringToApplicationSettingConverter());
        registry.addConverter(getContainerToStringConverter());
        registry.addConverter(getIdToContainerConverter());
        registry.addConverter(getStringToContainerConverter());
        registry.addConverter(getContainerKindToStringConverter());
        registry.addConverter(getIdToContainerKindConverter());
        registry.addConverter(getStringToContainerKindConverter());
        registry.addConverter(getContainerLabelToStringConverter());
        registry.addConverter(getIdToContainerLabelConverter());
        registry.addConverter(getStringToContainerLabelConverter());
        registry.addConverter(getContainerStateToStringConverter());
        registry.addConverter(getIdToContainerStateConverter());
        registry.addConverter(getStringToContainerStateConverter());
        registry.addConverter(getContainerTypeToStringConverter());
        registry.addConverter(getIdToContainerTypeConverter());
        registry.addConverter(getStringToContainerTypeConverter());
        registry.addConverter(getContainerValueToStringConverter());
        registry.addConverter(getIdToContainerValueConverter());
        registry.addConverter(getStringToContainerValueConverter());
        registry.addConverter(getExperimentToStringConverter());
        registry.addConverter(getIdToExperimentConverter());
        registry.addConverter(getStringToExperimentConverter());
        registry.addConverter(getExperimentKindToStringConverter());
        registry.addConverter(getIdToExperimentKindConverter());
        registry.addConverter(getStringToExperimentKindConverter());
        registry.addConverter(getExperimentLabelToStringConverter());
        registry.addConverter(getIdToExperimentLabelConverter());
        registry.addConverter(getStringToExperimentLabelConverter());
        registry.addConverter(getExperimentStateToStringConverter());
        registry.addConverter(getIdToExperimentStateConverter());
        registry.addConverter(getStringToExperimentStateConverter());
        registry.addConverter(getExperimentTypeToStringConverter());
        registry.addConverter(getIdToExperimentTypeConverter());
        registry.addConverter(getStringToExperimentTypeConverter());
        registry.addConverter(getExperimentValueToStringConverter());
        registry.addConverter(getIdToExperimentValueConverter());
        registry.addConverter(getStringToExperimentValueConverter());
        registry.addConverter(getFileThingToStringConverter());
        registry.addConverter(getIdToFileThingConverter());
        registry.addConverter(getStringToFileThingConverter());
        registry.addConverter(getInteractionKindToStringConverter());
        registry.addConverter(getIdToInteractionKindConverter());
        registry.addConverter(getStringToInteractionKindConverter());
        registry.addConverter(getInteractionTypeToStringConverter());
        registry.addConverter(getIdToInteractionTypeConverter());
        registry.addConverter(getStringToInteractionTypeConverter());
        registry.addConverter(getItxContainerContainerToStringConverter());
        registry.addConverter(getIdToItxContainerContainerConverter());
        registry.addConverter(getStringToItxContainerContainerConverter());
        registry.addConverter(getItxContainerContainerStateToStringConverter());
        registry.addConverter(getIdToItxContainerContainerStateConverter());
        registry.addConverter(getStringToItxContainerContainerStateConverter());
        registry.addConverter(getItxContainerContainerValueToStringConverter());
        registry.addConverter(getIdToItxContainerContainerValueConverter());
        registry.addConverter(getStringToItxContainerContainerValueConverter());
        registry.addConverter(getItxSubjectContainerToStringConverter());
        registry.addConverter(getIdToItxSubjectContainerConverter());
        registry.addConverter(getStringToItxSubjectContainerConverter());
        registry.addConverter(getItxSubjectContainerStateToStringConverter());
        registry.addConverter(getIdToItxSubjectContainerStateConverter());
        registry.addConverter(getStringToItxSubjectContainerStateConverter());
        registry.addConverter(getItxSubjectContainerValueToStringConverter());
        registry.addConverter(getIdToItxSubjectContainerValueConverter());
        registry.addConverter(getStringToItxSubjectContainerValueConverter());
        registry.addConverter(getLabelKindToStringConverter());
        registry.addConverter(getIdToLabelKindConverter());
        registry.addConverter(getStringToLabelKindConverter());
        registry.addConverter(getLabelSequenceToStringConverter());
        registry.addConverter(getIdToLabelSequenceConverter());
        registry.addConverter(getStringToLabelSequenceConverter());
        registry.addConverter(getLabelTypeToStringConverter());
        registry.addConverter(getIdToLabelTypeConverter());
        registry.addConverter(getStringToLabelTypeConverter());
        registry.addConverter(getLsInteractionToStringConverter());
        registry.addConverter(getIdToLsInteractionConverter());
        registry.addConverter(getStringToLsInteractionConverter());
        registry.addConverter(getLsTransactionToStringConverter());
        registry.addConverter(getIdToLsTransactionConverter());
        registry.addConverter(getStringToLsTransactionConverter());
        registry.addConverter(getOperatorKindToStringConverter());
        registry.addConverter(getIdToOperatorKindConverter());
        registry.addConverter(getStringToOperatorKindConverter());
        registry.addConverter(getOperatorTypeToStringConverter());
        registry.addConverter(getIdToOperatorTypeConverter());
        registry.addConverter(getStringToOperatorTypeConverter());
        registry.addConverter(getProtocolToStringConverter());
        registry.addConverter(getIdToProtocolConverter());
        registry.addConverter(getStringToProtocolConverter());
        registry.addConverter(getProtocolKindToStringConverter());
        registry.addConverter(getIdToProtocolKindConverter());
        registry.addConverter(getStringToProtocolKindConverter());
        registry.addConverter(getProtocolLabelToStringConverter());
        registry.addConverter(getIdToProtocolLabelConverter());
        registry.addConverter(getStringToProtocolLabelConverter());
        registry.addConverter(getProtocolStateToStringConverter());
        registry.addConverter(getIdToProtocolStateConverter());
        registry.addConverter(getStringToProtocolStateConverter());
        registry.addConverter(getProtocolTypeToStringConverter());
        registry.addConverter(getIdToProtocolTypeConverter());
        registry.addConverter(getStringToProtocolTypeConverter());
        registry.addConverter(getProtocolValueToStringConverter());
        registry.addConverter(getIdToProtocolValueConverter());
        registry.addConverter(getStringToProtocolValueConverter());
        registry.addConverter(getStateKindToStringConverter());
        registry.addConverter(getIdToStateKindConverter());
        registry.addConverter(getStringToStateKindConverter());
        registry.addConverter(getStateTypeToStringConverter());
        registry.addConverter(getIdToStateTypeConverter());
        registry.addConverter(getStringToStateTypeConverter());
        registry.addConverter(getSubjectToStringConverter());
        registry.addConverter(getIdToSubjectConverter());
        registry.addConverter(getStringToSubjectConverter());
        registry.addConverter(getSubjectLabelToStringConverter());
        registry.addConverter(getIdToSubjectLabelConverter());
        registry.addConverter(getStringToSubjectLabelConverter());
        registry.addConverter(getSubjectStateToStringConverter());
        registry.addConverter(getIdToSubjectStateConverter());
        registry.addConverter(getStringToSubjectStateConverter());
        registry.addConverter(getSubjectValueToStringConverter());
        registry.addConverter(getIdToSubjectValueConverter());
        registry.addConverter(getStringToSubjectValueConverter());
        registry.addConverter(getThingKindToStringConverter());
        registry.addConverter(getIdToThingKindConverter());
        registry.addConverter(getStringToThingKindConverter());
        registry.addConverter(getThingPageToStringConverter());
        registry.addConverter(getIdToThingPageConverter());
        registry.addConverter(getStringToThingPageConverter());
        registry.addConverter(getThingPageArchiveToStringConverter());
        registry.addConverter(getIdToThingPageArchiveConverter());
        registry.addConverter(getStringToThingPageArchiveConverter());
        registry.addConverter(getThingTypeToStringConverter());
        registry.addConverter(getIdToThingTypeConverter());
        registry.addConverter(getStringToThingTypeConverter());
        registry.addConverter(getTreatmentGroupToStringConverter());
        registry.addConverter(getIdToTreatmentGroupConverter());
        registry.addConverter(getStringToTreatmentGroupConverter());
        registry.addConverter(getTreatmentGroupLabelToStringConverter());
        registry.addConverter(getIdToTreatmentGroupLabelConverter());
        registry.addConverter(getStringToTreatmentGroupLabelConverter());
        registry.addConverter(getTreatmentGroupStateToStringConverter());
        registry.addConverter(getIdToTreatmentGroupStateConverter());
        registry.addConverter(getStringToTreatmentGroupStateConverter());
        registry.addConverter(getTreatmentGroupValueToStringConverter());
        registry.addConverter(getIdToTreatmentGroupValueConverter());
        registry.addConverter(getStringToTreatmentGroupValueConverter());
        registry.addConverter(getUncertaintyKindToStringConverter());
        registry.addConverter(getIdToUncertaintyKindConverter());
        registry.addConverter(getStringToUncertaintyKindConverter());
        registry.addConverter(getUnitKindToStringConverter());
        registry.addConverter(getIdToUnitKindConverter());
        registry.addConverter(getStringToUnitKindConverter());
        registry.addConverter(getUnitTypeToStringConverter());
        registry.addConverter(getIdToUnitTypeConverter());
        registry.addConverter(getStringToUnitTypeConverter());
        registry.addConverter(getValueKindToStringConverter());
        registry.addConverter(getIdToValueKindConverter());
        registry.addConverter(getStringToValueKindConverter());
        registry.addConverter(getValueTypeToStringConverter());
        registry.addConverter(getIdToValueTypeConverter());
        registry.addConverter(getStringToValueTypeConverter());
        registry.addConverter(getAuthorRoleToStringConverter());
        registry.addConverter(getStringToAuthorRoleConverter());
        registry.addConverter(getAuthorToStringConverter());
        registry.addConverter(getStringToAuthorConverter());
        registry.addConverter(getLsRoleToStringConverter());
        registry.addConverter(getDDictKindToStringConverter());
        registry.addConverter(getDDictTypeToStringConverter());

        
        

    }

	public void afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }

	
//	@Transactional
//	public Converter<Long, AuthorRole> getIdToAuthorRoleConverter() {
//        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.AuthorRole>() {
//            public com.labsynch.labseer.domain.AuthorRole convert(java.lang.Long id) {
//                return AuthorRole.findAuthorRole(id);
//            }
//        };
//    }
//	
	@Transactional
    public Converter<LsRole, String> getLsRoleToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LsRole, java.lang.String>() {
            public String convert(LsRole lsRole) {
                return new StringBuilder().append(lsRole.getRoleName()).append(' ').append(lsRole.getRoleDescription()).toString();
            }
        };
    }
    
	@Transactional
    public Converter<AuthorRole, String> getAuthorRoleToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.AuthorRole, java.lang.String>() {
            public String convert(AuthorRole authorRole) {
                return new StringBuilder().append(authorRole.getUserEntry().getUserName()).append(' ').append(authorRole.getRoleEntry().getRoleName()).toString();
            }
        };
    }
	
	@Transactional
    public Converter<String, AuthorRole> getStringToAuthorRoleConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.AuthorRole>() {
            public com.labsynch.labseer.domain.AuthorRole convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), AuthorRole.class);
            }
        };
    }
    
	
	@Transactional
    public Converter<Author, String> getAuthorToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.Author, java.lang.String>() {
            public String convert(Author author) {
                return new StringBuilder().append(author.getUserName()).toString();
            }
        };
    }
    
	@Transactional
    public Converter<String, Author> getStringToAuthorConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.Author>() {
            public com.labsynch.labseer.domain.Author convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Author.class);
            }
        };
    }
	
	@Transactional
    public Converter<Long, Author> getIdToAuthorConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.Author>() {
            public com.labsynch.labseer.domain.Author convert(java.lang.Long id) {
                return Author.findAuthor(id);
            }
        };
    }
    
	
	@Transactional
	public Converter<LsTag, String> getLsTagToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LsTag, java.lang.String>() {
            public String convert(LsTag lsTag) {
                return new StringBuilder().append(lsTag.getTagText()).append(' ').append(lsTag.getRecordedDate()).toString();
            }
        };
    }

	@Transactional
	public Converter<Long, LsTag> getIdToLsTagConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LsTag>() {
            public com.labsynch.labseer.domain.LsTag convert(java.lang.Long id) {
                return LsTag.findLsTag(id);
            }
        };
    }

	@Transactional
	public Converter<String, LsTag> getStringToLsTagConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LsTag>() {
            public com.labsynch.labseer.domain.LsTag convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LsTag.class);
            }
        };
    }

	public Converter<UpdateLog, String> getUpdateLogToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.UpdateLog, java.lang.String>() {
            public String convert(UpdateLog updateLog) {
                return new StringBuilder().append(updateLog.getThing()).append(' ').append(updateLog.getUpdateAction()).append(' ').append(updateLog.getComments()).append(' ').append(updateLog.getLsTransaction()).toString();
            }
        };
    }

	public Converter<Long, UpdateLog> getIdToUpdateLogConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.UpdateLog>() {
            public com.labsynch.labseer.domain.UpdateLog convert(java.lang.Long id) {
                return UpdateLog.findUpdateLog(id);
            }
        };
    }

	public Converter<String, UpdateLog> getStringToUpdateLogConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.UpdateLog>() {
            public com.labsynch.labseer.domain.UpdateLog convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), UpdateLog.class);
            }
        };
    }

	public Converter<CodeKind, String> getCodeKindToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.CodeKind, java.lang.String>() {
            public String convert(CodeKind codeKind) {
                return new StringBuilder().append(codeKind.getKindName()).append(' ').append(codeKind.getLsTypeAndKind()).toString();
            }
        };
    }

	public Converter<Long, CodeKind> getIdToCodeKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.CodeKind>() {
            public com.labsynch.labseer.domain.CodeKind convert(java.lang.Long id) {
                return CodeKind.findCodeKind(id);
            }
        };
    }

	public Converter<String, CodeKind> getStringToCodeKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.CodeKind>() {
            public com.labsynch.labseer.domain.CodeKind convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), CodeKind.class);
            }
        };
    }

	public Converter<CodeType, String> getCodeTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.CodeType, java.lang.String>() {
            public String convert(CodeType codeType) {
                return new StringBuilder().append(codeType.getTypeName()).toString();
            }
        };
    }

	public Converter<Long, CodeType> getIdToCodeTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.CodeType>() {
            public com.labsynch.labseer.domain.CodeType convert(java.lang.Long id) {
                return CodeType.findCodeType(id);
            }
        };
    }

	public Converter<String, CodeType> getStringToCodeTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.CodeType>() {
            public com.labsynch.labseer.domain.CodeType convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), CodeType.class);
            }
        };
    }

	public Converter<DDictKind, String> getDDictKindToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.DDictKind, java.lang.String>() {
            public String convert(DDictKind dDictKind) {
                return new StringBuilder().append(dDictKind.getLsType()).append('_').append(dDictKind.getName()).toString();
            }
        };
    }

	public Converter<Long, DDictKind> getIdToDDictKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.DDictKind>() {
            public com.labsynch.labseer.domain.DDictKind convert(java.lang.Long id) {
                return DDictKind.findDDictKind(id);
            }
        };
    }

	public Converter<String, DDictKind> getStringToDDictKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.DDictKind>() {
            public com.labsynch.labseer.domain.DDictKind convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), DDictKind.class);
            }
        };
    }

	public Converter<DDictType, String> getDDictTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.DDictType, java.lang.String>() {
            public String convert(DDictType dDictType) {
                return new StringBuilder().append(dDictType.getName()).toString();
            }
        };
    }

	public Converter<Long, DDictType> getIdToDDictTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.DDictType>() {
            public com.labsynch.labseer.domain.DDictType convert(java.lang.Long id) {
                return DDictType.findDDictType(id);
            }
        };
    }

	public Converter<String, DDictType> getStringToDDictTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.DDictType>() {
            public com.labsynch.labseer.domain.DDictType convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), DDictType.class);
            }
        };
    }

	public Converter<DDictValue, String> getDDictValueToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.DDictValue, java.lang.String>() {
            public String convert(DDictValue dDictValue) {
                return new StringBuilder().append(dDictValue.getLsType()).append(' ').append(dDictValue.getLsKind()).append(' ').append(dDictValue.getLsTypeAndKind()).append(' ').append(dDictValue.getLabelText()).toString();
            }
        };
    }

	public Converter<Long, DDictValue> getIdToDDictValueConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.DDictValue>() {
            public com.labsynch.labseer.domain.DDictValue convert(java.lang.Long id) {
                return DDictValue.findDDictValue(id);
            }
        };
    }

	public Converter<String, DDictValue> getStringToDDictValueConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.DDictValue>() {
            public com.labsynch.labseer.domain.DDictValue convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), DDictValue.class);
            }
        };
    }

	public Converter<ItxProtocolProtocol, String> getItxProtocolProtocolToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ItxProtocolProtocol, java.lang.String>() {
            public String convert(ItxProtocolProtocol itxProtocolProtocol) {
                return new StringBuilder().append(itxProtocolProtocol.getLsType()).append(' ').append(itxProtocolProtocol.getLsKind()).append(' ').append(itxProtocolProtocol.getLsTypeAndKind()).append(' ').append(itxProtocolProtocol.getCodeName()).toString();
            }
        };
    }

	public Converter<Long, ItxProtocolProtocol> getIdToItxProtocolProtocolConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ItxProtocolProtocol>() {
            public com.labsynch.labseer.domain.ItxProtocolProtocol convert(java.lang.Long id) {
                return ItxProtocolProtocol.findItxProtocolProtocol(id);
            }
        };
    }

	public Converter<String, ItxProtocolProtocol> getStringToItxProtocolProtocolConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ItxProtocolProtocol>() {
            public com.labsynch.labseer.domain.ItxProtocolProtocol convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ItxProtocolProtocol.class);
            }
        };
    }

	public Converter<ItxProtocolProtocolState, String> getItxProtocolProtocolStateToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ItxProtocolProtocolState, java.lang.String>() {
            public String convert(ItxProtocolProtocolState itxProtocolProtocolState) {
                return new StringBuilder().append(itxProtocolProtocolState.getRecordedBy()).append(' ').append(itxProtocolProtocolState.getRecordedDate()).append(' ').append(itxProtocolProtocolState.getModifiedBy()).append(' ').append(itxProtocolProtocolState.getModifiedDate()).toString();
            }
        };
    }

	public Converter<Long, ItxProtocolProtocolState> getIdToItxProtocolProtocolStateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ItxProtocolProtocolState>() {
            public com.labsynch.labseer.domain.ItxProtocolProtocolState convert(java.lang.Long id) {
                return ItxProtocolProtocolState.findItxProtocolProtocolState(id);
            }
        };
    }

	public Converter<String, ItxProtocolProtocolState> getStringToItxProtocolProtocolStateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ItxProtocolProtocolState>() {
            public com.labsynch.labseer.domain.ItxProtocolProtocolState convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ItxProtocolProtocolState.class);
            }
        };
    }

	public Converter<ItxProtocolProtocolValue, String> getItxProtocolProtocolValueToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ItxProtocolProtocolValue, java.lang.String>() {
            public String convert(ItxProtocolProtocolValue itxProtocolProtocolValue) {
                return new StringBuilder().append(itxProtocolProtocolValue.getLsType()).append(' ').append(itxProtocolProtocolValue.getLsKind()).append(' ').append(itxProtocolProtocolValue.getLsTypeAndKind()).append(' ').append(itxProtocolProtocolValue.getCodeType()).toString();
            }
        };
    }

	public Converter<Long, ItxProtocolProtocolValue> getIdToItxProtocolProtocolValueConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ItxProtocolProtocolValue>() {
            public com.labsynch.labseer.domain.ItxProtocolProtocolValue convert(java.lang.Long id) {
                return ItxProtocolProtocolValue.findItxProtocolProtocolValue(id);
            }
        };
    }

	public Converter<String, ItxProtocolProtocolValue> getStringToItxProtocolProtocolValueConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ItxProtocolProtocolValue>() {
            public com.labsynch.labseer.domain.ItxProtocolProtocolValue convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ItxProtocolProtocolValue.class);
            }
        };
    }

	public Converter<Long, LsRole> getIdToLsRoleConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LsRole>() {
            public com.labsynch.labseer.domain.LsRole convert(java.lang.Long id) {
                return LsRole.findLsRole(id);
            }
        };
    }

	public Converter<String, LsRole> getStringToLsRoleConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LsRole>() {
            public com.labsynch.labseer.domain.LsRole convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LsRole.class);
            }
        };
    }

	public Converter<LsThing, String> getLsThingToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LsThing, java.lang.String>() {
            public String convert(LsThing lsThing) {
                return new StringBuilder().append(lsThing.getLsType()).append(' ').append(lsThing.getLsKind()).append(' ').append(lsThing.getLsTypeAndKind()).append(' ').append(lsThing.getCodeName()).toString();
            }
        };
    }

	public Converter<Long, LsThing> getIdToLsThingConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LsThing>() {
            public com.labsynch.labseer.domain.LsThing convert(java.lang.Long id) {
                return LsThing.findLsThing(id);
            }
        };
    }

	public Converter<String, LsThing> getStringToLsThingConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LsThing>() {
            public com.labsynch.labseer.domain.LsThing convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LsThing.class);
            }
        };
    }

	public Converter<LsThingLabel, String> getLsThingLabelToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LsThingLabel, java.lang.String>() {
            public String convert(LsThingLabel lsThingLabel) {
                return new StringBuilder().append(lsThingLabel.getLabelText()).append(' ').append(lsThingLabel.getRecordedBy()).append(' ').append(lsThingLabel.getRecordedDate()).append(' ').append(lsThingLabel.getModifiedDate()).toString();
            }
        };
    }

	public Converter<Long, LsThingLabel> getIdToLsThingLabelConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LsThingLabel>() {
            public com.labsynch.labseer.domain.LsThingLabel convert(java.lang.Long id) {
                return LsThingLabel.findLsThingLabel(id);
            }
        };
    }

	public Converter<String, LsThingLabel> getStringToLsThingLabelConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LsThingLabel>() {
            public com.labsynch.labseer.domain.LsThingLabel convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LsThingLabel.class);
            }
        };
    }

	public Converter<LsThingState, String> getLsThingStateToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LsThingState, java.lang.String>() {
            public String convert(LsThingState lsThingState) {
                return new StringBuilder().append(lsThingState.getRecordedBy()).append(' ').append(lsThingState.getRecordedDate()).append(' ').append(lsThingState.getModifiedBy()).append(' ').append(lsThingState.getModifiedDate()).toString();
            }
        };
    }

	public Converter<Long, LsThingState> getIdToLsThingStateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LsThingState>() {
            public com.labsynch.labseer.domain.LsThingState convert(java.lang.Long id) {
                return LsThingState.findLsThingState(id);
            }
        };
    }

	public Converter<String, LsThingState> getStringToLsThingStateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LsThingState>() {
            public com.labsynch.labseer.domain.LsThingState convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LsThingState.class);
            }
        };
    }

	public Converter<LsThingValue, String> getLsThingValueToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LsThingValue, java.lang.String>() {
            public String convert(LsThingValue lsThingValue) {
                return new StringBuilder().append(lsThingValue.getLsType()).append(' ').append(lsThingValue.getLsKind()).append(' ').append(lsThingValue.getLsTypeAndKind()).append(' ').append(lsThingValue.getCodeType()).toString();
            }
        };
    }

	public Converter<Long, LsThingValue> getIdToLsThingValueConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LsThingValue>() {
            public com.labsynch.labseer.domain.LsThingValue convert(java.lang.Long id) {
                return LsThingValue.findLsThingValue(id);
            }
        };
    }

	public Converter<String, LsThingValue> getStringToLsThingValueConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LsThingValue>() {
            public com.labsynch.labseer.domain.LsThingValue convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LsThingValue.class);
            }
        };
    }

	public Converter<Long, LsTransaction> getIdToLsTransactionConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LsTransaction>() {
            public com.labsynch.labseer.domain.LsTransaction convert(java.lang.Long id) {
                return LsTransaction.findLsTransaction(id);
            }
        };
    }
	
}
