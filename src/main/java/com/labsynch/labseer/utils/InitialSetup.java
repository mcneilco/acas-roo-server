package com.labsynch.labseer.utils;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.labsynch.labseer.domain.ExperimentKind;
import com.labsynch.labseer.domain.ExperimentType;
import com.labsynch.labseer.domain.InteractionType;
import com.labsynch.labseer.domain.LabelKind;
import com.labsynch.labseer.domain.LabelSequence;
import com.labsynch.labseer.domain.LabelType;
import com.labsynch.labseer.domain.ProtocolKind;
import com.labsynch.labseer.domain.ProtocolType;
import com.labsynch.labseer.domain.StateType;
import com.labsynch.labseer.domain.ThingKind;
import com.labsynch.labseer.domain.ThingType;
import com.labsynch.labseer.domain.ValueType;

public class InitialSetup {
		
	private static final Logger logger = LoggerFactory.getLogger(InitialSetup.class);

	public static void setupFixtures() throws IOException{
		setupProtocolTypes();
		setupProtocolKinds();
		setupExperimentTypes();
		setupExperimentKinds();
		setupThingTypes();
		setupThingKinds();

		setupInteractionTypes();
		setupInteractionKinds();

		setupContainerTypes();
		setupContainerKinds();
		
		setupStateTypes();
		setupStateKinds();

		setupLabelTypes();
		setupLabelKinds();
		setupInteractionTypes();
		setupValueTypes();
		setupLabelSequences();
	}

	private static void setupStateKinds() {
		// TODO Auto-generated method stub
		
	}

	private static void setupContainerTypes() {
		// TODO Auto-generated method stub
		
	}

	private static void setupContainerKinds() {
		// TODO Auto-generated method stub
		
	}

	private static void setupInteractionKinds() {
		// TODO Auto-generated method stub
		
	}

	private static void setupExperimentTypes() throws IOException {
		//experimentTypes
		if (ExperimentType.countExperimentTypes() < 1){
			String propertyName = "experimentTypes";
			logger.info("Setting up new " + propertyName);
			String json = SetupPropertiesUtils.getProperties(propertyName);
			Collection<ExperimentType> experimentTypes = ExperimentType.fromJsonArrayToExperimentTypes(json);
			for (ExperimentType experimentType : experimentTypes){
				if (ExperimentType.findExperimentTypesByTypeNameEquals(experimentType.getTypeName()).getMaxResults() == 0){
					experimentType.persist();					
				}
			}			
		}			
	}

	private static void setupExperimentKinds() throws IOException {
		if (ExperimentKind.countExperimentKinds() < 1){
			String propertyName = "experimentKinds";
			logger.info("Setting up new " + propertyName);
			String json = SetupPropertiesUtils.getProperties(propertyName);
			Collection<ExperimentKind> experimentKinds = ExperimentKind.fromJsonArrayToExperimentKinds(json);
			for (ExperimentKind experimentKind : experimentKinds){
				List<ExperimentType> dbLsTypes = ExperimentType.findExperimentTypesByTypeNameEquals(experimentKind.getLsType().getTypeName()).getResultList();
				if (dbLsTypes.size() == 1){
					experimentKind.setLsType(dbLsTypes.get(0));
					experimentKind.persist();
					logger.info(experimentKind.toJson());
				} else {
					logger.error(propertyName + "  did not get expected results: " + dbLsTypes.size());
				}
			}			
		}	
	}

	private static void setupProtocolTypes() throws IOException {
		//protocolTypes
		if (ProtocolType.countProtocolTypes() < 1){
			String propertyName = "protocolTypes";
			logger.info("Setting up new " + propertyName);
			String json = SetupPropertiesUtils.getProperties(propertyName);
			Collection<ProtocolType> protocolTypes = ProtocolType.fromJsonArrayToProtocolTypes(json);
			for (ProtocolType protocolType : protocolTypes){
				protocolType.persist();
			}			
		}		
	}
	
	private static void setupProtocolKinds() throws IOException {
		//protocolKinds
		if (ProtocolKind.countProtocolKinds() < 1){
			String propertyName = "protocolKinds";
			logger.info("Setting up new " + propertyName);
			String json = SetupPropertiesUtils.getProperties(propertyName);
			Collection<ProtocolKind> protocolKinds = ProtocolKind.fromJsonArrayToProtocolKinds(json);
			for (ProtocolKind protocolKind : protocolKinds){
				List<ProtocolType> dbLsTypes = ProtocolType.findProtocolTypesByTypeNameEquals(protocolKind.getLsType().getTypeName()).getResultList();
				if (dbLsTypes.size() == 1){
					protocolKind.setLsType(dbLsTypes.get(0));
					protocolKind.persist();
				} else {
					logger.error("did not get expected results: " + dbLsTypes.size());
				}
				logger.info(protocolKind.toJson());
			}			
		}		
	}

	private static void setupThingTypes() throws IOException{
		//thingTypes
		if (ThingType.countThingTypes() < 1){
			String propertyName = "thingTypes";
			logger.info("Setting up new " + propertyName);
			String json = SetupPropertiesUtils.getProperties(propertyName);

			Collection<ThingType> thingTypes = ThingType.fromJsonArrayToThingTypes(json);
			for (ThingType thingType : thingTypes){
				thingType.persist();
				logger.info(thingType.toJson());
			}
			
		}
		
	}

	private static void setupThingKinds() throws IOException{
		//thingKinds
		if (ThingKind.countThingKinds() < 1){
			String propertyName = "thingKinds";
			logger.info("Setting up new " + propertyName);
			String json = SetupPropertiesUtils.getProperties(propertyName);
			Collection<ThingKind> thingKinds = ThingKind.fromJsonArrayToThingKinds(json);
			for (ThingKind thingKind : thingKinds){
				List<ThingType> dbLsTypes = ThingType.findThingTypesByTypeNameEquals(thingKind.getLsType().getTypeName()).getResultList();
				if (dbLsTypes.size() == 1){
					thingKind.setLsType(dbLsTypes.get(0));
					thingKind.persist();
				} else {
					logger.error("did not get expected results: " + dbLsTypes.size());
				}
				logger.info(thingKind.toJson());
			}
			
		}
		
	}

	private static void setupLabelTypes() throws IOException{
		//labelTypes
		if (LabelType.countLabelTypes() < 1){
			String propertyName = "labelTypes";
			logger.info("Setting up new " + propertyName);
			String json = SetupPropertiesUtils.getProperties(propertyName);

			Collection<LabelType> labelTypes = LabelType.fromJsonArrayToLabelTypes(json);
			for (LabelType labelType : labelTypes){
				labelType.persist();
				logger.info(labelType.toJson());
			}
			
		}
		
	}
	
	private static void setupLabelKinds() throws IOException{
		//labelKinds
		if (LabelKind.countLabelKinds() < 1){
			String propertyName = "labelKinds";
			logger.info("Setting up new " + propertyName);
			String json = SetupPropertiesUtils.getProperties(propertyName);
			Collection<LabelKind> labelKinds = LabelKind.fromJsonArrayToLabelKinds(json);
			for (LabelKind labelKind : labelKinds){
				List<LabelType> dbLsTypes = LabelType.findLabelTypesByTypeNameEquals(labelKind.getLsType().getTypeName()).getResultList();
				if (dbLsTypes.size() == 1){
					labelKind.setLsType(dbLsTypes.get(0));
					labelKind.persist();
					logger.info(labelKind.toJson());
				} else {
					logger.error("did not get expected results: " + dbLsTypes.size());
				}
				
			}
			
		}
		
	}

	private static void setupInteractionTypes() throws IOException{
		//interactionTypes
		if (InteractionType.countInteractionTypes() < 1){
			String propertyName = "interactionTypes";
			logger.info("Setting up new " + propertyName);
			String json = SetupPropertiesUtils.getProperties(propertyName);
			Collection<InteractionType> interactionTypes = InteractionType.fromJsonArrayToInteractionTypes(json);
			for (InteractionType interactionType : interactionTypes){
				interactionType.persist();
				logger.info(interactionType.toJson());
			}
			
		}
		
	}
	
	private static void setupStateTypes() throws IOException{
		//StateTypes
		if (StateType.countStateTypes() < 1){
			String propertyName = "stateTypes";
			logger.info("Setting up new " + propertyName);

			String json = SetupPropertiesUtils.getProperties(propertyName);

			Collection<StateType> stateTypes = StateType.fromJsonArrayToStateTypes(json);
			for (StateType stateType : stateTypes){
				stateType.persist();
				logger.info(stateType.toJson());
			}
			
		}
		
	}
	
	private static void setupValueTypes() throws IOException{
		//valueTypes
		if (ValueType.countValueTypes() < 1){
			String propertyName = "valueTypes";
			logger.info("Setting up new " + propertyName);

			String json = SetupPropertiesUtils.getProperties(propertyName);

			Collection<ValueType> valueTypes = ValueType.fromJsonArrayToValueTypes(json);
			for (ValueType valueType : valueTypes){
				valueType.persist();
				logger.info(valueType.toJson());
			}
			
		}
		
	}
	
	private static void setupLabelSequences() throws IOException{
		//labelSequences
		if (LabelSequence.countLabelSequences() < 1){
			String propertyName = "labelSequences";
			logger.info("Setting up new " + propertyName);

			String json = SetupPropertiesUtils.getProperties(propertyName);

			Collection<LabelSequence> labelSequences = LabelSequence.fromJsonArrayToLabelSequences(json);
			for (LabelSequence labelSequence : labelSequences){
				logger.info(labelSequence.toJson());
				labelSequence.persist();
			}
			
		}
		
	}
	
}
