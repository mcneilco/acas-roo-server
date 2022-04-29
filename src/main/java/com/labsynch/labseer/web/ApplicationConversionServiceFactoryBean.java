package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.*;
import com.labsynch.labseer.service.SaltLoader;
import java.util.Set;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.transaction.annotation.Transactional;


@Configurable
/**
 * A central place to register application converters and formatters. 
 */
@Transactional
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

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

    public Converter<CmpdRegAppSetting, String> getCmpdRegAppSettingToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.CmpdRegAppSetting, java.lang.String>() {
            public String convert(CmpdRegAppSetting cmpdRegAppSetting) {
                return new StringBuilder().append(cmpdRegAppSetting.getPropName()).append(' ').append(cmpdRegAppSetting.getPropValue()).append(' ').append(cmpdRegAppSetting.getComments()).append(' ').append(cmpdRegAppSetting.getRecordedDate()).toString();
            }
        };
    }

	public Converter<Long, CmpdRegAppSetting> getIdToCmpdRegAppSettingConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.CmpdRegAppSetting>() {
            public com.labsynch.labseer.domain.CmpdRegAppSetting convert(java.lang.Long id) {
                return CmpdRegAppSetting.findCmpdRegAppSetting(id);
            }
        };
    }

	public Converter<String, CmpdRegAppSetting> getStringToCmpdRegAppSettingConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.CmpdRegAppSetting>() {
            public com.labsynch.labseer.domain.CmpdRegAppSetting convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), CmpdRegAppSetting.class);
            }
        };
    }

	public Converter<CorpName, String> getCorpNameToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.CorpName, java.lang.String>() {
            public String convert(CorpName corpName) {
                return new StringBuilder().append(corpName.getParentCorpName()).append(' ').append(corpName.getComment()).toString();
            }
        };
    }

	public Converter<Long, CorpName> getIdToCorpNameConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.CorpName>() {
            public com.labsynch.labseer.domain.CorpName convert(java.lang.Long id) {
                return CorpName.findCorpName(id);
            }
        };
    }

	public Converter<String, CorpName> getStringToCorpNameConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.CorpName>() {
            public com.labsynch.labseer.domain.CorpName convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), CorpName.class);
            }
        };
    }

	public Converter<FileList, String> getFileListToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.FileList, java.lang.String>() {
            public String convert(FileList fileList) {
                return new StringBuilder().append(fileList.getFile()).append(' ').append(fileList.getDescription()).append(' ').append(fileList.getName()).append(' ').append(fileList.getType()).toString();
            }
        };
    }

	public Converter<Long, FileList> getIdToFileListConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.FileList>() {
            public com.labsynch.labseer.domain.FileList convert(java.lang.Long id) {
                return FileList.findFileList(id);
            }
        };
    }

	public Converter<String, FileList> getStringToFileListConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.FileList>() {
            public com.labsynch.labseer.domain.FileList convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), FileList.class);
            }
        };
    }
	
	public Converter<ParentAliasType, String> getParentAliasTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ParentAliasType, java.lang.String>() {
            public String convert(ParentAliasType parentAliasType) {
                return new StringBuilder().append(parentAliasType.getTypeName()).toString();
            }
        };
    }

	public Converter<FileType, String> getFileTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.FileType, java.lang.String>() {
            public String convert(FileType fileType) {
                return new StringBuilder().append(fileType.getName()).append(' ').append(fileType.getCode()).toString();
            }
        };
    }

	public Converter<Long, FileType> getIdToFileTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.FileType>() {
            public com.labsynch.labseer.domain.FileType convert(java.lang.Long id) {
                return FileType.findFileType(id);
            }
        };
    }

	public Converter<String, FileType> getStringToFileTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.FileType>() {
            public com.labsynch.labseer.domain.FileType convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), FileType.class);
            }
        };
    }

	public Converter<Isotope, String> getIsotopeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.Isotope, java.lang.String>() {
            public String convert(Isotope isotope) {
                return new StringBuilder().append(isotope.getName()).append(' ').append(isotope.getAbbrev()).append(' ').append(isotope.getMassChange()).toString();
            }
        };
    }

	public Converter<Long, Isotope> getIdToIsotopeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.Isotope>() {
            public com.labsynch.labseer.domain.Isotope convert(java.lang.Long id) {
                return Isotope.findIsotope(id);
            }
        };
    }

	public Converter<String, Isotope> getStringToIsotopeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.Isotope>() {
            public com.labsynch.labseer.domain.Isotope convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Isotope.class);
            }
        };
    }

	public Converter<Operator, String> getOperatorToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.Operator, java.lang.String>() {
            public String convert(Operator operator) {
                return new StringBuilder().append(operator.getName()).append(' ').append(operator.getCode()).toString();
            }
        };
    }

	public Converter<Long, Operator> getIdToOperatorConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.Operator>() {
            public com.labsynch.labseer.domain.Operator convert(java.lang.Long id) {
                return Operator.findOperator(id);
            }
        };
    }

	public Converter<String, Operator> getStringToOperatorConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.Operator>() {
            public com.labsynch.labseer.domain.Operator convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Operator.class);
            }
        };
    }

	public Converter<Parent, String> getParentToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.Parent, java.lang.String>() {
            public String convert(Parent parent) {
                return new StringBuilder().append(parent.getId()).toString();
            }
        };
    }

	public Converter<Long, Parent> getIdToParentConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.Parent>() {
            public com.labsynch.labseer.domain.Parent convert(java.lang.Long id) {
                return Parent.findParent(id);
            }
        };
    }

	public Converter<String, Parent> getStringToParentConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.Parent>() {
            public com.labsynch.labseer.domain.Parent convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Parent.class);
            }
        };
    }

	public Converter<PhysicalState, String> getPhysicalStateToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.PhysicalState, java.lang.String>() {
            public String convert(PhysicalState physicalState) {
                return new StringBuilder().append(physicalState.getName()).append(' ').append(physicalState.getCode()).toString();
            }
        };
    }

	public Converter<Long, PhysicalState> getIdToPhysicalStateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.PhysicalState>() {
            public com.labsynch.labseer.domain.PhysicalState convert(java.lang.Long id) {
                return PhysicalState.findPhysicalState(id);
            }
        };
    }

	public Converter<String, PhysicalState> getStringToPhysicalStateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.PhysicalState>() {
            public com.labsynch.labseer.domain.PhysicalState convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), PhysicalState.class);
            }
        };
    }

	public Converter<PreDef_CorpName, String> getPreDef_CorpNameToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.PreDef_CorpName, java.lang.String>() {
            public String convert(PreDef_CorpName preDef_CorpName) {
                return new StringBuilder().append(preDef_CorpName.getCorpNumber()).append(' ').append(preDef_CorpName.getCorpName()).append(' ').append(preDef_CorpName.getComment()).toString();
            }
        };
    }

	public Converter<Long, PreDef_CorpName> getIdToPreDef_CorpNameConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.PreDef_CorpName>() {
            public com.labsynch.labseer.domain.PreDef_CorpName convert(java.lang.Long id) {
                return PreDef_CorpName.findPreDef_CorpName(id);
            }
        };
    }

	public Converter<String, PreDef_CorpName> getStringToPreDef_CorpNameConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.PreDef_CorpName>() {
            public com.labsynch.labseer.domain.PreDef_CorpName convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), PreDef_CorpName.class);
            }
        };
    }

	public Converter<PurityMeasuredBy, String> getPurityMeasuredByToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.PurityMeasuredBy, java.lang.String>() {
            public String convert(PurityMeasuredBy purityMeasuredBy) {
                return new StringBuilder().append(purityMeasuredBy.getName()).append(' ').append(purityMeasuredBy.getCode()).toString();
            }
        };
    }

	public Converter<Long, PurityMeasuredBy> getIdToPurityMeasuredByConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.PurityMeasuredBy>() {
            public com.labsynch.labseer.domain.PurityMeasuredBy convert(java.lang.Long id) {
                return PurityMeasuredBy.findPurityMeasuredBy(id);
            }
        };
    }

	public Converter<String, PurityMeasuredBy> getStringToPurityMeasuredByConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.PurityMeasuredBy>() {
            public com.labsynch.labseer.domain.PurityMeasuredBy convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), PurityMeasuredBy.class);
            }
        };
    }

	public Converter<Salt, String> getSaltToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.Salt, java.lang.String>() {
            public String convert(Salt salt) {
                return new StringBuilder().append(salt.getMolStructure()).append(' ').append(salt.getName()).append(' ').append(salt.getOriginalStructure()).append(' ').append(salt.getAbbrev()).toString();
            }
        };
    }

	public Converter<Long, Salt> getIdToSaltConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.Salt>() {
            public com.labsynch.labseer.domain.Salt convert(java.lang.Long id) {
                return Salt.findSalt(id);
            }
        };
    }

	public Converter<String, Salt> getStringToSaltConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.Salt>() {
            public com.labsynch.labseer.domain.Salt convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Salt.class);
            }
        };
    }

	public Converter<SaltForm, String> getSaltFormToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.SaltForm, java.lang.String>() {
            public String convert(SaltForm saltForm) {
                return new StringBuilder().append(saltForm.getId()).toString();
            }
        };
    }

	public Converter<Long, SaltForm> getIdToSaltFormConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.SaltForm>() {
            public com.labsynch.labseer.domain.SaltForm convert(java.lang.Long id) {
                return SaltForm.findSaltForm(id);
            }
        };
    }

	public Converter<String, SaltForm> getStringToSaltFormConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.SaltForm>() {
            public com.labsynch.labseer.domain.SaltForm convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), SaltForm.class);
            }
        };
    }

	public Converter<SolutionUnit, String> getSolutionUnitToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.SolutionUnit, java.lang.String>() {
            public String convert(SolutionUnit solutionUnit) {
                return new StringBuilder().append(solutionUnit.getName()).append(' ').append(solutionUnit.getCode()).toString();
            }
        };
    }

	public Converter<Long, SolutionUnit> getIdToSolutionUnitConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.SolutionUnit>() {
            public com.labsynch.labseer.domain.SolutionUnit convert(java.lang.Long id) {
                return SolutionUnit.findSolutionUnit(id);
            }
        };
    }

	public Converter<String, SolutionUnit> getStringToSolutionUnitConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.SolutionUnit>() {
            public com.labsynch.labseer.domain.SolutionUnit convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), SolutionUnit.class);
            }
        };
    }

	public Converter<StereoCategory, String> getStereoCategoryToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.StereoCategory, java.lang.String>() {
            public String convert(StereoCategory stereoCategory) {
                return new StringBuilder().append(stereoCategory.getName()).append(' ').append(stereoCategory.getCode()).toString();
            }
        };
    }

	public Converter<Long, StereoCategory> getIdToStereoCategoryConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.StereoCategory>() {
            public com.labsynch.labseer.domain.StereoCategory convert(java.lang.Long id) {
                return StereoCategory.findStereoCategory(id);
            }
        };
    }

	public Converter<String, StereoCategory> getStringToStereoCategoryConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.StereoCategory>() {
            public com.labsynch.labseer.domain.StereoCategory convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), StereoCategory.class);
            }
        };
    }

	public Converter<Unit, String> getUnitToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.Unit, java.lang.String>() {
            public String convert(Unit unit) {
                return new StringBuilder().append(unit.getName()).append(' ').append(unit.getCode()).toString();
            }
        };
    }

	public Converter<Long, Unit> getIdToUnitConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.Unit>() {
            public com.labsynch.labseer.domain.Unit convert(java.lang.Long id) {
                return Unit.findUnit(id);
            }
        };
    }

	public Converter<String, Unit> getStringToUnitConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.Unit>() {
            public com.labsynch.labseer.domain.Unit convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Unit.class);
            }
        };
    }

	public Converter<Vendor, String> getVendorToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.Vendor, java.lang.String>() {
            public String convert(Vendor vendor) {
                return new StringBuilder().append(vendor.getName()).append(' ').append(vendor.getCode()).toString();
            }
        };
    }

	public Converter<Long, Vendor> getIdToVendorConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.Vendor>() {
            public com.labsynch.labseer.domain.Vendor convert(java.lang.Long id) {
                return Vendor.findVendor(id);
            }
        };
    }

	public Converter<String, Vendor> getStringToVendorConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.Vendor>() {
            public com.labsynch.labseer.domain.Vendor convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Vendor.class);
            }
        };
    }

    public Converter<BulkLoadFile, String> getBulkLoadFileToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.BulkLoadFile, java.lang.String>() {
            public String convert(BulkLoadFile bulkLoadFile) {
                return new StringBuilder().append(bulkLoadFile.getFileName()).toString();
            }
        };
    }

	public Converter<Long, BulkLoadFile> getIdToBulkLoadFileConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.BulkLoadFile>() {
            public com.labsynch.labseer.domain.BulkLoadFile convert(java.lang.Long id) {
                return BulkLoadFile.findBulkLoadFile(id);
            }
        };
    }

	public Converter<String, BulkLoadFile> getStringToBulkLoadFileConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.BulkLoadFile>() {
            public com.labsynch.labseer.domain.BulkLoadFile convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), BulkLoadFile.class);
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
        registry.addConverter(getLsRoleTypeToStringConverter());
        registry.addConverter(getRoleKindToStringConverter());
        registry.addConverter(getCmpdRegAppSettingToStringConverter());
        registry.addConverter(getIdToCmpdRegAppSettingConverter());
        registry.addConverter(getStringToCmpdRegAppSettingConverter());
        registry.addConverter(getCorpNameToStringConverter());
        registry.addConverter(getIdToCorpNameConverter());
        registry.addConverter(getStringToCorpNameConverter());
        registry.addConverter(getFileListToStringConverter());
        registry.addConverter(getIdToFileListConverter());
        registry.addConverter(getStringToFileListConverter());
        registry.addConverter(getFileTypeToStringConverter());
        registry.addConverter(getIdToFileTypeConverter());
        registry.addConverter(getStringToFileTypeConverter());
        registry.addConverter(getIsotopeToStringConverter());
        registry.addConverter(getIdToIsotopeConverter());
        registry.addConverter(getStringToIsotopeConverter());
        registry.addConverter(getOperatorToStringConverter());
        registry.addConverter(getIdToOperatorConverter());
        registry.addConverter(getStringToOperatorConverter());
        registry.addConverter(getParentToStringConverter());
        registry.addConverter(getIdToParentConverter());
        registry.addConverter(getStringToParentConverter());
        registry.addConverter(getPhysicalStateToStringConverter());
        registry.addConverter(getIdToPhysicalStateConverter());
        registry.addConverter(getStringToPhysicalStateConverter());
        registry.addConverter(getPreDef_CorpNameToStringConverter());
        registry.addConverter(getIdToPreDef_CorpNameConverter());
        registry.addConverter(getStringToPreDef_CorpNameConverter());
        registry.addConverter(getPurityMeasuredByToStringConverter());
        registry.addConverter(getIdToPurityMeasuredByConverter());
        registry.addConverter(getStringToPurityMeasuredByConverter());
        registry.addConverter(getSaltToStringConverter());
        registry.addConverter(getIdToSaltConverter());
        registry.addConverter(getStringToSaltConverter());
        registry.addConverter(getSaltFormToStringConverter());
        registry.addConverter(getIdToSaltFormConverter());
        registry.addConverter(getStringToSaltFormConverter());
        registry.addConverter(getSolutionUnitToStringConverter());
        registry.addConverter(getIdToSolutionUnitConverter());
        registry.addConverter(getStringToSolutionUnitConverter());
        registry.addConverter(getStereoCategoryToStringConverter());
        registry.addConverter(getIdToStereoCategoryConverter());
        registry.addConverter(getStringToStereoCategoryConverter());
        registry.addConverter(getUnitToStringConverter());
        registry.addConverter(getIdToUnitConverter());
        registry.addConverter(getStringToUnitConverter());
        registry.addConverter(getVendorToStringConverter());
        registry.addConverter(getIdToVendorConverter());
        registry.addConverter(getStringToVendorConverter());
        registry.addConverter(getBulkLoadFileToStringConverter());
        registry.addConverter(getIdToBulkLoadFileConverter());
        registry.addConverter(getStringToBulkLoadFileConverter());
        registry.addConverter(getParentAliasTypeToStringConverter());
        
        

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
    public Converter<RoleType, String> getLsRoleTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.RoleType, java.lang.String>() {
            public String convert(RoleType roleType) {
                return new StringBuilder().append(roleType.getTypeName()).toString();
            }
        };
    }
	
	
    public Converter<RoleKind, String> getRoleKindToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.RoleKind, java.lang.String>() {
            public String convert(RoleKind roleKind) {
                return new StringBuilder().append(roleKind.getKindName()).toString();
            }
        };
    }
	
	
	@Transactional
    public Converter<LsRole, String> getLsRoleToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LsRole, java.lang.String>() {
            public String convert(LsRole lsRole) {
                return new StringBuilder().append(lsRole.getLsType()).append(' ').append(lsRole.getLsKind()).append(' ').append(lsRole.getRoleName()).toString();
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
    
    public Converter<CompoundType, String> getCompoundTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.CompoundType, java.lang.String>() {
            public String convert(CompoundType compoundType) {
                return new StringBuilder().append(compoundType.getCode()).toString();
            }
        };
    }

	public Converter<Long, CompoundType> getIdToCompoundTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.CompoundType>() {
            public com.labsynch.labseer.domain.CompoundType convert(java.lang.Long id) {
                return CompoundType.findCompoundType(id);
            }
        };
    }

	public Converter<String, CompoundType> getStringToCompoundTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.CompoundType>() {
            public com.labsynch.labseer.domain.CompoundType convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), CompoundType.class);
            }
        };
    }

	public Converter<LotAlias, String> getLotAliasToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LotAlias, java.lang.String>() {
            public String convert(LotAlias lotAlias) {
                return new StringBuilder().append(lotAlias.getLsType()).append(' ').append(lotAlias.getLsKind()).append(' ').append(lotAlias.getAliasName()).toString();
            }
        };
    }

	public Converter<Long, LotAlias> getIdToLotAliasConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LotAlias>() {
            public com.labsynch.labseer.domain.LotAlias convert(java.lang.Long id) {
                return LotAlias.findLotAlias(id);
            }
        };
    }

	public Converter<String, LotAlias> getStringToLotAliasConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LotAlias>() {
            public com.labsynch.labseer.domain.LotAlias convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LotAlias.class);
            }
        };
    }

	public Converter<LotAliasKind, String> getLotAliasKindToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LotAliasKind, java.lang.String>() {
            public String convert(LotAliasKind lotAliasKind) {
                return new StringBuilder().append(lotAliasKind.getKindName()).toString();
            }
        };
    }

	public Converter<Long, LotAliasKind> getIdToLotAliasKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LotAliasKind>() {
            public com.labsynch.labseer.domain.LotAliasKind convert(java.lang.Long id) {
                return LotAliasKind.findLotAliasKind(id);
            }
        };
    }

	public Converter<String, LotAliasKind> getStringToLotAliasKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LotAliasKind>() {
            public com.labsynch.labseer.domain.LotAliasKind convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LotAliasKind.class);
            }
        };
    }

	public Converter<LotAliasType, String> getLotAliasTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LotAliasType, java.lang.String>() {
            public String convert(LotAliasType lotAliasType) {
                return new StringBuilder().append(lotAliasType.getTypeName()).toString();
            }
        };
    }

	public Converter<Long, LotAliasType> getIdToLotAliasTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LotAliasType>() {
            public com.labsynch.labseer.domain.LotAliasType convert(java.lang.Long id) {
                return LotAliasType.findLotAliasType(id);
            }
        };
    }

	public Converter<String, LotAliasType> getStringToLotAliasTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LotAliasType>() {
            public com.labsynch.labseer.domain.LotAliasType convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LotAliasType.class);
            }
        };
    }

	public Converter<ParentAlias, String> getParentAliasToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ParentAlias, java.lang.String>() {
            public String convert(ParentAlias parentAlias) {
                return new StringBuilder().append(parentAlias.getLsType()).append(' ').append(parentAlias.getLsKind()).append(' ').append(parentAlias.getAliasName()).append(' ').append(parentAlias.getSortId()).toString();
            }
        };
    }

	public Converter<Long, ParentAlias> getIdToParentAliasConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ParentAlias>() {
            public com.labsynch.labseer.domain.ParentAlias convert(java.lang.Long id) {
                return ParentAlias.findParentAlias(id);
            }
        };
    }

	public Converter<String, ParentAlias> getStringToParentAliasConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ParentAlias>() {
            public com.labsynch.labseer.domain.ParentAlias convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ParentAlias.class);
            }
        };
    }

	public Converter<ParentAliasKind, String> getParentAliasKindToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ParentAliasKind, java.lang.String>() {
            public String convert(ParentAliasKind parentAliasKind) {
                return new StringBuilder().append(parentAliasKind.getKindName()).toString();
            }
        };
    }

	public Converter<Long, ParentAliasKind> getIdToParentAliasKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ParentAliasKind>() {
            public com.labsynch.labseer.domain.ParentAliasKind convert(java.lang.Long id) {
                return ParentAliasKind.findParentAliasKind(id);
            }
        };
    }

	public Converter<String, ParentAliasKind> getStringToParentAliasKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ParentAliasKind>() {
            public com.labsynch.labseer.domain.ParentAliasKind convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ParentAliasKind.class);
            }
        };
    }

	public Converter<Long, ParentAliasType> getIdToParentAliasTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ParentAliasType>() {
            public com.labsynch.labseer.domain.ParentAliasType convert(java.lang.Long id) {
                return ParentAliasType.findParentAliasType(id);
            }
        };
    }

	public Converter<String, ParentAliasType> getStringToParentAliasTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ParentAliasType>() {
            public com.labsynch.labseer.domain.ParentAliasType convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ParentAliasType.class);
            }
        };
    }

	public Converter<ParentAnnotation, String> getParentAnnotationToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ParentAnnotation, java.lang.String>() {
            public String convert(ParentAnnotation parentAnnotation) {
                return new StringBuilder().append(parentAnnotation.getCode()).append(' ').append(parentAnnotation.getName()).append(' ').append(parentAnnotation.getDisplayOrder()).append(' ').append(parentAnnotation.getComment()).toString();
            }
        };
    }

	public Converter<Long, ParentAnnotation> getIdToParentAnnotationConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ParentAnnotation>() {
            public com.labsynch.labseer.domain.ParentAnnotation convert(java.lang.Long id) {
                return ParentAnnotation.findParentAnnotation(id);
            }
        };
    }

	public Converter<String, ParentAnnotation> getStringToParentAnnotationConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ParentAnnotation>() {
            public com.labsynch.labseer.domain.ParentAnnotation convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ParentAnnotation.class);
            }
        };
    }

	public Converter<AuthorLabel, String> getAuthorLabelToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.AuthorLabel, java.lang.String>() {
            public String convert(AuthorLabel authorLabel) {
                return new StringBuilder().append(authorLabel.getLabelText()).append(' ').append(authorLabel.getRecordedBy()).append(' ').append(authorLabel.getRecordedDate()).append(' ').append(authorLabel.getModifiedDate()).toString();
            }
        };
    }

	public Converter<Long, AuthorLabel> getIdToAuthorLabelConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.AuthorLabel>() {
            public com.labsynch.labseer.domain.AuthorLabel convert(java.lang.Long id) {
                return AuthorLabel.findAuthorLabel(id);
            }
        };
    }

	public Converter<String, AuthorLabel> getStringToAuthorLabelConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.AuthorLabel>() {
            public com.labsynch.labseer.domain.AuthorLabel convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), AuthorLabel.class);
            }
        };
    }

	public Converter<AuthorState, String> getAuthorStateToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.AuthorState, java.lang.String>() {
            public String convert(AuthorState authorState) {
                return new StringBuilder().append(authorState.getRecordedBy()).append(' ').append(authorState.getRecordedDate()).append(' ').append(authorState.getModifiedBy()).append(' ').append(authorState.getModifiedDate()).toString();
            }
        };
    }

	public Converter<Long, AuthorState> getIdToAuthorStateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.AuthorState>() {
            public com.labsynch.labseer.domain.AuthorState convert(java.lang.Long id) {
                return AuthorState.findAuthorState(id);
            }
        };
    }

	public Converter<String, AuthorState> getStringToAuthorStateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.AuthorState>() {
            public com.labsynch.labseer.domain.AuthorState convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), AuthorState.class);
            }
        };
    }

	public Converter<AuthorValue, String> getAuthorValueToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.AuthorValue, java.lang.String>() {
            public String convert(AuthorValue authorValue) {
                return new StringBuilder().append(authorValue.getLsType()).append(' ').append(authorValue.getLsKind()).append(' ').append(authorValue.getLsTypeAndKind()).append(' ').append(authorValue.getCodeOrigin()).toString();
            }
        };
    }

	public Converter<Long, AuthorValue> getIdToAuthorValueConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.AuthorValue>() {
            public com.labsynch.labseer.domain.AuthorValue convert(java.lang.Long id) {
                return AuthorValue.findAuthorValue(id);
            }
        };
    }

	public Converter<String, AuthorValue> getStringToAuthorValueConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.AuthorValue>() {
            public com.labsynch.labseer.domain.AuthorValue convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), AuthorValue.class);
            }
        };
    }

	public Converter<BulkLoadTemplate, String> getBulkLoadTemplateToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.BulkLoadTemplate, java.lang.String>() {
            public String convert(BulkLoadTemplate bulkLoadTemplate) {
                return new StringBuilder().append(bulkLoadTemplate.getTemplateName()).append(' ').append(bulkLoadTemplate.getJsonTemplate()).append(' ').append(bulkLoadTemplate.getRecordedBy()).toString();
            }
        };
    }

	public Converter<Long, BulkLoadTemplate> getIdToBulkLoadTemplateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.BulkLoadTemplate>() {
            public com.labsynch.labseer.domain.BulkLoadTemplate convert(java.lang.Long id) {
                return BulkLoadTemplate.findBulkLoadTemplate(id);
            }
        };
    }

	public Converter<String, BulkLoadTemplate> getStringToBulkLoadTemplateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.BulkLoadTemplate>() {
            public com.labsynch.labseer.domain.BulkLoadTemplate convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), BulkLoadTemplate.class);
            }
        };
    }

	public Converter<CodeOrigin, String> getCodeOriginToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.CodeOrigin, java.lang.String>() {
            public String convert(CodeOrigin codeOrigin) {
                return new StringBuilder().append(codeOrigin.getName()).toString();
            }
        };
    }

	public Converter<Long, CodeOrigin> getIdToCodeOriginConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.CodeOrigin>() {
            public com.labsynch.labseer.domain.CodeOrigin convert(java.lang.Long id) {
                return CodeOrigin.findCodeOrigin(id);
            }
        };
    }

	public Converter<String, CodeOrigin> getStringToCodeOriginConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.CodeOrigin>() {
            public com.labsynch.labseer.domain.CodeOrigin convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), CodeOrigin.class);
            }
        };
    }

	public Converter<Compound, String> getCompoundToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.Compound, java.lang.String>() {
            public String convert(Compound compound) {
                return new StringBuilder().append(compound.getCorpName()).append(' ').append(compound.getExternal_id()).append(' ').append(compound.getCdId()).append(' ').append(compound.getCreatedDate()).toString();
            }
        };
    }

	public Converter<Long, Compound> getIdToCompoundConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.Compound>() {
            public com.labsynch.labseer.domain.Compound convert(java.lang.Long id) {
                return Compound.findCompound(id);
            }
        };
    }

	public Converter<String, Compound> getStringToCompoundConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.Compound>() {
            public com.labsynch.labseer.domain.Compound convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Compound.class);
            }
        };
    }

	public Converter<CronJob, String> getCronJobToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.CronJob, java.lang.String>() {
            public String convert(CronJob cronJob) {
                return new StringBuilder().append(cronJob.getSchedule()).append(' ').append(cronJob.getScriptType()).append(' ').append(cronJob.getScriptFile()).append(' ').append(cronJob.getFunctionName()).toString();
            }
        };
    }

	public Converter<Long, CronJob> getIdToCronJobConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.CronJob>() {
            public com.labsynch.labseer.domain.CronJob convert(java.lang.Long id) {
                return CronJob.findCronJob(id);
            }
        };
    }

	public Converter<String, CronJob> getStringToCronJobConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.CronJob>() {
            public com.labsynch.labseer.domain.CronJob convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), CronJob.class);
            }
        };
    }

	public Converter<IsoSalt, String> getIsoSaltToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.IsoSalt, java.lang.String>() {
            public String convert(IsoSalt isoSalt) {
                return new StringBuilder().append(isoSalt.getType()).append(' ').append(isoSalt.getEquivalents()).toString();
            }
        };
    }

	public Converter<Long, IsoSalt> getIdToIsoSaltConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.IsoSalt>() {
            public com.labsynch.labseer.domain.IsoSalt convert(java.lang.Long id) {
                return IsoSalt.findIsoSalt(id);
            }
        };
    }

	public Converter<String, IsoSalt> getStringToIsoSaltConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.IsoSalt>() {
            public com.labsynch.labseer.domain.IsoSalt convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), IsoSalt.class);
            }
        };
    }

	public Converter<ItxExperimentExperiment, String> getItxExperimentExperimentToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ItxExperimentExperiment, java.lang.String>() {
            public String convert(ItxExperimentExperiment itxExperimentExperiment) {
                return new StringBuilder().append(itxExperimentExperiment.getLsType()).append(' ').append(itxExperimentExperiment.getLsKind()).append(' ').append(itxExperimentExperiment.getLsTypeAndKind()).append(' ').append(itxExperimentExperiment.getCodeName()).toString();
            }
        };
    }

	public Converter<Long, ItxExperimentExperiment> getIdToItxExperimentExperimentConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ItxExperimentExperiment>() {
            public com.labsynch.labseer.domain.ItxExperimentExperiment convert(java.lang.Long id) {
                return ItxExperimentExperiment.findItxExperimentExperiment(id);
            }
        };
    }

	public Converter<String, ItxExperimentExperiment> getStringToItxExperimentExperimentConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ItxExperimentExperiment>() {
            public com.labsynch.labseer.domain.ItxExperimentExperiment convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ItxExperimentExperiment.class);
            }
        };
    }

	public Converter<ItxExperimentExperimentState, String> getItxExperimentExperimentStateToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ItxExperimentExperimentState, java.lang.String>() {
            public String convert(ItxExperimentExperimentState itxExperimentExperimentState) {
                return new StringBuilder().append(itxExperimentExperimentState.getRecordedBy()).append(' ').append(itxExperimentExperimentState.getRecordedDate()).append(' ').append(itxExperimentExperimentState.getModifiedBy()).append(' ').append(itxExperimentExperimentState.getModifiedDate()).toString();
            }
        };
    }

	public Converter<Long, ItxExperimentExperimentState> getIdToItxExperimentExperimentStateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ItxExperimentExperimentState>() {
            public com.labsynch.labseer.domain.ItxExperimentExperimentState convert(java.lang.Long id) {
                return ItxExperimentExperimentState.findItxExperimentExperimentState(id);
            }
        };
    }

	public Converter<String, ItxExperimentExperimentState> getStringToItxExperimentExperimentStateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ItxExperimentExperimentState>() {
            public com.labsynch.labseer.domain.ItxExperimentExperimentState convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ItxExperimentExperimentState.class);
            }
        };
    }

	public Converter<ItxExperimentExperimentValue, String> getItxExperimentExperimentValueToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ItxExperimentExperimentValue, java.lang.String>() {
            public String convert(ItxExperimentExperimentValue itxExperimentExperimentValue) {
                return new StringBuilder().append(itxExperimentExperimentValue.getLsType()).append(' ').append(itxExperimentExperimentValue.getLsKind()).append(' ').append(itxExperimentExperimentValue.getLsTypeAndKind()).append(' ').append(itxExperimentExperimentValue.getCodeOrigin()).toString();
            }
        };
    }

	public Converter<Long, ItxExperimentExperimentValue> getIdToItxExperimentExperimentValueConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ItxExperimentExperimentValue>() {
            public com.labsynch.labseer.domain.ItxExperimentExperimentValue convert(java.lang.Long id) {
                return ItxExperimentExperimentValue.findItxExperimentExperimentValue(id);
            }
        };
    }

	public Converter<String, ItxExperimentExperimentValue> getStringToItxExperimentExperimentValueConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ItxExperimentExperimentValue>() {
            public com.labsynch.labseer.domain.ItxExperimentExperimentValue convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ItxExperimentExperimentValue.class);
            }
        };
    }

	public Converter<ItxLsThingLsThing, String> getItxLsThingLsThingToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ItxLsThingLsThing, java.lang.String>() {
            public String convert(ItxLsThingLsThing itxLsThingLsThing) {
                return new StringBuilder().append(itxLsThingLsThing.getLsType()).append(' ').append(itxLsThingLsThing.getLsKind()).append(' ').append(itxLsThingLsThing.getLsTypeAndKind()).append(' ').append(itxLsThingLsThing.getCodeName()).toString();
            }
        };
    }

	public Converter<Long, ItxLsThingLsThing> getIdToItxLsThingLsThingConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ItxLsThingLsThing>() {
            public com.labsynch.labseer.domain.ItxLsThingLsThing convert(java.lang.Long id) {
                return ItxLsThingLsThing.findItxLsThingLsThing(id);
            }
        };
    }

	public Converter<String, ItxLsThingLsThing> getStringToItxLsThingLsThingConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ItxLsThingLsThing>() {
            public com.labsynch.labseer.domain.ItxLsThingLsThing convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ItxLsThingLsThing.class);
            }
        };
    }

	public Converter<ItxLsThingLsThingState, String> getItxLsThingLsThingStateToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ItxLsThingLsThingState, java.lang.String>() {
            public String convert(ItxLsThingLsThingState itxLsThingLsThingState) {
                return new StringBuilder().append(itxLsThingLsThingState.getRecordedBy()).append(' ').append(itxLsThingLsThingState.getRecordedDate()).append(' ').append(itxLsThingLsThingState.getModifiedBy()).append(' ').append(itxLsThingLsThingState.getModifiedDate()).toString();
            }
        };
    }

	public Converter<Long, ItxLsThingLsThingState> getIdToItxLsThingLsThingStateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ItxLsThingLsThingState>() {
            public com.labsynch.labseer.domain.ItxLsThingLsThingState convert(java.lang.Long id) {
                return ItxLsThingLsThingState.findItxLsThingLsThingState(id);
            }
        };
    }

	public Converter<String, ItxLsThingLsThingState> getStringToItxLsThingLsThingStateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ItxLsThingLsThingState>() {
            public com.labsynch.labseer.domain.ItxLsThingLsThingState convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ItxLsThingLsThingState.class);
            }
        };
    }

	public Converter<ItxLsThingLsThingValue, String> getItxLsThingLsThingValueToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.ItxLsThingLsThingValue, java.lang.String>() {
            public String convert(ItxLsThingLsThingValue itxLsThingLsThingValue) {
                return new StringBuilder().append(itxLsThingLsThingValue.getLsType()).append(' ').append(itxLsThingLsThingValue.getLsKind()).append(' ').append(itxLsThingLsThingValue.getLsTypeAndKind()).append(' ').append(itxLsThingLsThingValue.getCodeOrigin()).toString();
            }
        };
    }

	public Converter<Long, ItxLsThingLsThingValue> getIdToItxLsThingLsThingValueConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.ItxLsThingLsThingValue>() {
            public com.labsynch.labseer.domain.ItxLsThingLsThingValue convert(java.lang.Long id) {
                return ItxLsThingLsThingValue.findItxLsThingLsThingValue(id);
            }
        };
    }

	public Converter<String, ItxLsThingLsThingValue> getStringToItxLsThingLsThingValueConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.ItxLsThingLsThingValue>() {
            public com.labsynch.labseer.domain.ItxLsThingLsThingValue convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ItxLsThingLsThingValue.class);
            }
        };
    }

	public Converter<LabelSequenceRole, String> getLabelSequenceRoleToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LabelSequenceRole, java.lang.String>() {
            public String convert(LabelSequenceRole labelSequenceRole) {
                return "(no displayable fields)";
            }
        };
    }

	public Converter<Long, LabelSequenceRole> getIdToLabelSequenceRoleConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LabelSequenceRole>() {
            public com.labsynch.labseer.domain.LabelSequenceRole convert(java.lang.Long id) {
                return LabelSequenceRole.findLabelSequenceRole(id);
            }
        };
    }

	public Converter<String, LabelSequenceRole> getStringToLabelSequenceRoleConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LabelSequenceRole>() {
            public com.labsynch.labseer.domain.LabelSequenceRole convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LabelSequenceRole.class);
            }
        };
    }

	public Converter<Lot, String> getLotToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.Lot, java.lang.String>() {
            public String convert(Lot lot) {
                return new StringBuilder().append(lot.getStorageLocation()).append(' ').append(lot.getBuid()).append(' ').append(lot.getAsDrawnStruct()).append(' ').append(lot.getLotAsDrawnCdId()).toString();
            }
        };
    }

	public Converter<Long, Lot> getIdToLotConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.Lot>() {
            public com.labsynch.labseer.domain.Lot convert(java.lang.Long id) {
                return Lot.findLot(id);
            }
        };
    }

	public Converter<String, Lot> getStringToLotConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.Lot>() {
            public com.labsynch.labseer.domain.Lot convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Lot.class);
            }
        };
    }

	public Converter<LsSeqAnlGrp, String> getLsSeqAnlGrpToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LsSeqAnlGrp, java.lang.String>() {
            public String convert(LsSeqAnlGrp lsSeqAnlGrp) {
                return "(no displayable fields)";
            }
        };
    }

	public Converter<Long, LsSeqAnlGrp> getIdToLsSeqAnlGrpConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LsSeqAnlGrp>() {
            public com.labsynch.labseer.domain.LsSeqAnlGrp convert(java.lang.Long id) {
                return LsSeqAnlGrp.findLsSeqAnlGrp(id);
            }
        };
    }

	public Converter<String, LsSeqAnlGrp> getStringToLsSeqAnlGrpConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LsSeqAnlGrp>() {
            public com.labsynch.labseer.domain.LsSeqAnlGrp convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LsSeqAnlGrp.class);
            }
        };
    }

	public Converter<LsSeqContainer, String> getLsSeqContainerToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LsSeqContainer, java.lang.String>() {
            public String convert(LsSeqContainer lsSeqContainer) {
                return "(no displayable fields)";
            }
        };
    }

	public Converter<Long, LsSeqContainer> getIdToLsSeqContainerConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LsSeqContainer>() {
            public com.labsynch.labseer.domain.LsSeqContainer convert(java.lang.Long id) {
                return LsSeqContainer.findLsSeqContainer(id);
            }
        };
    }

	public Converter<String, LsSeqContainer> getStringToLsSeqContainerConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LsSeqContainer>() {
            public com.labsynch.labseer.domain.LsSeqContainer convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LsSeqContainer.class);
            }
        };
    }

	public Converter<LsSeqExpt, String> getLsSeqExptToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LsSeqExpt, java.lang.String>() {
            public String convert(LsSeqExpt lsSeqExpt) {
                return "(no displayable fields)";
            }
        };
    }

	public Converter<Long, LsSeqExpt> getIdToLsSeqExptConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LsSeqExpt>() {
            public com.labsynch.labseer.domain.LsSeqExpt convert(java.lang.Long id) {
                return LsSeqExpt.findLsSeqExpt(id);
            }
        };
    }

	public Converter<String, LsSeqExpt> getStringToLsSeqExptConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LsSeqExpt>() {
            public com.labsynch.labseer.domain.LsSeqExpt convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LsSeqExpt.class);
            }
        };
    }

	public Converter<LsSeqItxCntrCntr, String> getLsSeqItxCntrCntrToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LsSeqItxCntrCntr, java.lang.String>() {
            public String convert(LsSeqItxCntrCntr lsSeqItxCntrCntr) {
                return "(no displayable fields)";
            }
        };
    }

	public Converter<Long, LsSeqItxCntrCntr> getIdToLsSeqItxCntrCntrConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LsSeqItxCntrCntr>() {
            public com.labsynch.labseer.domain.LsSeqItxCntrCntr convert(java.lang.Long id) {
                return LsSeqItxCntrCntr.findLsSeqItxCntrCntr(id);
            }
        };
    }

	public Converter<String, LsSeqItxCntrCntr> getStringToLsSeqItxCntrCntrConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LsSeqItxCntrCntr>() {
            public com.labsynch.labseer.domain.LsSeqItxCntrCntr convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LsSeqItxCntrCntr.class);
            }
        };
    }

	public Converter<LsSeqItxExperimentExperiment, String> getLsSeqItxExperimentExperimentToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LsSeqItxExperimentExperiment, java.lang.String>() {
            public String convert(LsSeqItxExperimentExperiment lsSeqItxExperimentExperiment) {
                return "(no displayable fields)";
            }
        };
    }

	public Converter<Long, LsSeqItxExperimentExperiment> getIdToLsSeqItxExperimentExperimentConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LsSeqItxExperimentExperiment>() {
            public com.labsynch.labseer.domain.LsSeqItxExperimentExperiment convert(java.lang.Long id) {
                return LsSeqItxExperimentExperiment.findLsSeqItxExperimentExperiment(id);
            }
        };
    }

	public Converter<String, LsSeqItxExperimentExperiment> getStringToLsSeqItxExperimentExperimentConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LsSeqItxExperimentExperiment>() {
            public com.labsynch.labseer.domain.LsSeqItxExperimentExperiment convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LsSeqItxExperimentExperiment.class);
            }
        };
    }

	public Converter<LsSeqItxLsThingLsThing, String> getLsSeqItxLsThingLsThingToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LsSeqItxLsThingLsThing, java.lang.String>() {
            public String convert(LsSeqItxLsThingLsThing lsSeqItxLsThingLsThing) {
                return "(no displayable fields)";
            }
        };
    }

	public Converter<Long, LsSeqItxLsThingLsThing> getIdToLsSeqItxLsThingLsThingConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LsSeqItxLsThingLsThing>() {
            public com.labsynch.labseer.domain.LsSeqItxLsThingLsThing convert(java.lang.Long id) {
                return LsSeqItxLsThingLsThing.findLsSeqItxLsThingLsThing(id);
            }
        };
    }

	public Converter<String, LsSeqItxLsThingLsThing> getStringToLsSeqItxLsThingLsThingConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LsSeqItxLsThingLsThing>() {
            public com.labsynch.labseer.domain.LsSeqItxLsThingLsThing convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LsSeqItxLsThingLsThing.class);
            }
        };
    }

	public Converter<LsSeqItxProtocolProtocol, String> getLsSeqItxProtocolProtocolToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LsSeqItxProtocolProtocol, java.lang.String>() {
            public String convert(LsSeqItxProtocolProtocol lsSeqItxProtocolProtocol) {
                return "(no displayable fields)";
            }
        };
    }

	public Converter<Long, LsSeqItxProtocolProtocol> getIdToLsSeqItxProtocolProtocolConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LsSeqItxProtocolProtocol>() {
            public com.labsynch.labseer.domain.LsSeqItxProtocolProtocol convert(java.lang.Long id) {
                return LsSeqItxProtocolProtocol.findLsSeqItxProtocolProtocol(id);
            }
        };
    }

	public Converter<String, LsSeqItxProtocolProtocol> getStringToLsSeqItxProtocolProtocolConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LsSeqItxProtocolProtocol>() {
            public com.labsynch.labseer.domain.LsSeqItxProtocolProtocol convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LsSeqItxProtocolProtocol.class);
            }
        };
    }

	public Converter<LsSeqItxSubjCntr, String> getLsSeqItxSubjCntrToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LsSeqItxSubjCntr, java.lang.String>() {
            public String convert(LsSeqItxSubjCntr lsSeqItxSubjCntr) {
                return "(no displayable fields)";
            }
        };
    }

	public Converter<Long, LsSeqItxSubjCntr> getIdToLsSeqItxSubjCntrConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LsSeqItxSubjCntr>() {
            public com.labsynch.labseer.domain.LsSeqItxSubjCntr convert(java.lang.Long id) {
                return LsSeqItxSubjCntr.findLsSeqItxSubjCntr(id);
            }
        };
    }

	public Converter<String, LsSeqItxSubjCntr> getStringToLsSeqItxSubjCntrConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LsSeqItxSubjCntr>() {
            public com.labsynch.labseer.domain.LsSeqItxSubjCntr convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LsSeqItxSubjCntr.class);
            }
        };
    }

	public Converter<LsSeqProtocol, String> getLsSeqProtocolToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LsSeqProtocol, java.lang.String>() {
            public String convert(LsSeqProtocol lsSeqProtocol) {
                return "(no displayable fields)";
            }
        };
    }

	public Converter<Long, LsSeqProtocol> getIdToLsSeqProtocolConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LsSeqProtocol>() {
            public com.labsynch.labseer.domain.LsSeqProtocol convert(java.lang.Long id) {
                return LsSeqProtocol.findLsSeqProtocol(id);
            }
        };
    }

	public Converter<String, LsSeqProtocol> getStringToLsSeqProtocolConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LsSeqProtocol>() {
            public com.labsynch.labseer.domain.LsSeqProtocol convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LsSeqProtocol.class);
            }
        };
    }

	public Converter<LsSeqSubject, String> getLsSeqSubjectToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LsSeqSubject, java.lang.String>() {
            public String convert(LsSeqSubject lsSeqSubject) {
                return "(no displayable fields)";
            }
        };
    }

	public Converter<Long, LsSeqSubject> getIdToLsSeqSubjectConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LsSeqSubject>() {
            public com.labsynch.labseer.domain.LsSeqSubject convert(java.lang.Long id) {
                return LsSeqSubject.findLsSeqSubject(id);
            }
        };
    }

	public Converter<String, LsSeqSubject> getStringToLsSeqSubjectConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LsSeqSubject>() {
            public com.labsynch.labseer.domain.LsSeqSubject convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LsSeqSubject.class);
            }
        };
    }

	public Converter<LsSeqTrtGrp, String> getLsSeqTrtGrpToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.LsSeqTrtGrp, java.lang.String>() {
            public String convert(LsSeqTrtGrp lsSeqTrtGrp) {
                return "(no displayable fields)";
            }
        };
    }

	public Converter<Long, LsSeqTrtGrp> getIdToLsSeqTrtGrpConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.LsSeqTrtGrp>() {
            public com.labsynch.labseer.domain.LsSeqTrtGrp convert(java.lang.Long id) {
                return LsSeqTrtGrp.findLsSeqTrtGrp(id);
            }
        };
    }

	public Converter<String, LsSeqTrtGrp> getStringToLsSeqTrtGrpConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.LsSeqTrtGrp>() {
            public com.labsynch.labseer.domain.LsSeqTrtGrp convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LsSeqTrtGrp.class);
            }
        };
    }

	public Converter<Long, RoleKind> getIdToRoleKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.RoleKind>() {
            public com.labsynch.labseer.domain.RoleKind convert(java.lang.Long id) {
                return RoleKind.findRoleKind(id);
            }
        };
    }

	public Converter<String, RoleKind> getStringToRoleKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.RoleKind>() {
            public com.labsynch.labseer.domain.RoleKind convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), RoleKind.class);
            }
        };
    }

	public Converter<RoleType, String> getRoleTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.RoleType, java.lang.String>() {
            public String convert(RoleType roleType) {
                return new StringBuilder().append(roleType.getTypeName()).toString();
            }
        };
    }

	public Converter<Long, RoleType> getIdToRoleTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.RoleType>() {
            public com.labsynch.labseer.domain.RoleType convert(java.lang.Long id) {
                return RoleType.findRoleType(id);
            }
        };
    }

	public Converter<String, RoleType> getStringToRoleTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.RoleType>() {
            public com.labsynch.labseer.domain.RoleType convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), RoleType.class);
            }
        };
    }

	public Converter<SaltFormAlias, String> getSaltFormAliasToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.SaltFormAlias, java.lang.String>() {
            public String convert(SaltFormAlias saltFormAlias) {
                return new StringBuilder().append(saltFormAlias.getLsType()).append(' ').append(saltFormAlias.getLsKind()).append(' ').append(saltFormAlias.getAliasName()).toString();
            }
        };
    }

	public Converter<Long, SaltFormAlias> getIdToSaltFormAliasConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.SaltFormAlias>() {
            public com.labsynch.labseer.domain.SaltFormAlias convert(java.lang.Long id) {
                return SaltFormAlias.findSaltFormAlias(id);
            }
        };
    }

	public Converter<String, SaltFormAlias> getStringToSaltFormAliasConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.SaltFormAlias>() {
            public com.labsynch.labseer.domain.SaltFormAlias convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), SaltFormAlias.class);
            }
        };
    }

	public Converter<SaltFormAliasKind, String> getSaltFormAliasKindToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.SaltFormAliasKind, java.lang.String>() {
            public String convert(SaltFormAliasKind saltFormAliasKind) {
                return new StringBuilder().append(saltFormAliasKind.getKindName()).toString();
            }
        };
    }

	public Converter<Long, SaltFormAliasKind> getIdToSaltFormAliasKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.SaltFormAliasKind>() {
            public com.labsynch.labseer.domain.SaltFormAliasKind convert(java.lang.Long id) {
                return SaltFormAliasKind.findSaltFormAliasKind(id);
            }
        };
    }

	public Converter<String, SaltFormAliasKind> getStringToSaltFormAliasKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.SaltFormAliasKind>() {
            public com.labsynch.labseer.domain.SaltFormAliasKind convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), SaltFormAliasKind.class);
            }
        };
    }

	public Converter<SaltFormAliasType, String> getSaltFormAliasTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.SaltFormAliasType, java.lang.String>() {
            public String convert(SaltFormAliasType saltFormAliasType) {
                return new StringBuilder().append(saltFormAliasType.getTypeName()).toString();
            }
        };
    }

	public Converter<Long, SaltFormAliasType> getIdToSaltFormAliasTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.SaltFormAliasType>() {
            public com.labsynch.labseer.domain.SaltFormAliasType convert(java.lang.Long id) {
                return SaltFormAliasType.findSaltFormAliasType(id);
            }
        };
    }

	public Converter<String, SaltFormAliasType> getStringToSaltFormAliasTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.SaltFormAliasType>() {
            public com.labsynch.labseer.domain.SaltFormAliasType convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), SaltFormAliasType.class);
            }
        };
    }

	public Converter<TempSelectTable, String> getTempSelectTableToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.domain.TempSelectTable, java.lang.String>() {
            public String convert(TempSelectTable tempSelectTable) {
                return new StringBuilder().append(tempSelectTable.getNumberVar()).append(' ').append(tempSelectTable.getStringVar()).append(' ').append(tempSelectTable.getLsTransaction()).append(' ').append(tempSelectTable.getRecordedDate()).toString();
            }
        };
    }

	public Converter<Long, TempSelectTable> getIdToTempSelectTableConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.domain.TempSelectTable>() {
            public com.labsynch.labseer.domain.TempSelectTable convert(java.lang.Long id) {
                return TempSelectTable.findTempSelectTable(id);
            }
        };
    }

	public Converter<String, TempSelectTable> getStringToTempSelectTableConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.domain.TempSelectTable>() {
            public com.labsynch.labseer.domain.TempSelectTable convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), TempSelectTable.class);
            }
        };
    }

	public Converter<SaltLoader, String> getSaltLoaderToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.labseer.service.SaltLoader, java.lang.String>() {
            public String convert(SaltLoader saltLoader) {
                return new StringBuilder().append(saltLoader.getName()).append(' ').append(saltLoader.getDescription()).append(' ').append(saltLoader.getNumberOfSalts()).append(' ').append(saltLoader.getSize()).toString();
            }
        };
    }

	public Converter<Long, SaltLoader> getIdToSaltLoaderConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.labseer.service.SaltLoader>() {
            public com.labsynch.labseer.service.SaltLoader convert(java.lang.Long id) {
                return SaltLoader.findSaltLoader(id);
            }
        };
    }

	public Converter<String, SaltLoader> getStringToSaltLoaderConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.labseer.service.SaltLoader>() {
            public com.labsynch.labseer.service.SaltLoader convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), SaltLoader.class);
            }
        };
    }
}
