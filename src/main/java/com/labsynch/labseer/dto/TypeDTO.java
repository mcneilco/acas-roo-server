package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.persistence.NoResultException;

import com.labsynch.labseer.domain.ContainerType;
import com.labsynch.labseer.domain.DDictType;
import com.labsynch.labseer.domain.ExperimentType;
import com.labsynch.labseer.domain.InteractionType;
import com.labsynch.labseer.domain.LabelType;
import com.labsynch.labseer.domain.OperatorType;
import com.labsynch.labseer.domain.ProtocolType;
import com.labsynch.labseer.domain.RoleType;
import com.labsynch.labseer.domain.StateType;
import com.labsynch.labseer.domain.ThingType;
import com.labsynch.labseer.domain.UnitType;
import com.labsynch.labseer.domain.ValueType;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


public class TypeDTO {
	
	private String typeName;
	
	private static final Logger logger = LoggerFactory.getLogger(TypeDTO.class);

	public TypeDTO(
			String typeName){
		
		this.typeName = typeName;
	}


	public TypeDTO() {
	}


	public static Collection<ProtocolType> getOrCreateProtocolTypes(
			List<TypeDTO> types) {
		HashSet<ProtocolType> protocolTypes = new HashSet<ProtocolType>();
		for (TypeDTO type : types){
			ProtocolType protocolType;
			try{
				protocolType = ProtocolType.findProtocolTypesByTypeNameEquals(type.typeName).getSingleResult();
			} catch(NoResultException e){
				protocolType = new ProtocolType();
				protocolType.setTypeName(type.typeName);
				protocolType.persist();
			}
			protocolTypes.add(protocolType);
		}
		return protocolTypes;
	}
	
	public static Collection<ExperimentType> getOrCreateExperimentTypes(
			List<TypeDTO> types) {
		HashSet<ExperimentType> experimentTypes = new HashSet<ExperimentType>();
		for (TypeDTO type : types){
			ExperimentType experimentType;
			try{
				experimentType = ExperimentType.findExperimentTypesByTypeNameEquals(type.typeName).getSingleResult();
			} catch(NoResultException e){
				experimentType = new ExperimentType();
				experimentType.setTypeName(type.typeName);
				experimentType.persist();
			}
			experimentTypes.add(experimentType);
		}
		return experimentTypes;
	}
	
	public static Collection<InteractionType> getOrCreateInteractionTypes(
			List<InteractionType> types) {
		HashSet<InteractionType> interactionTypes = new HashSet<InteractionType>();
		for (InteractionType type : types){
			InteractionType interactionType;
			try{
				interactionType = InteractionType.findInteractionTypesByTypeNameEquals(type.getTypeName()).getSingleResult();
			} catch(NoResultException e){
				interactionType = new InteractionType();
				interactionType.setTypeName(type.getTypeName());
				interactionType.setTypeVerb(type.getTypeVerb());
				interactionType.persist();
			}
			interactionTypes.add(interactionType);
		}
		return interactionTypes;
	}
	
	public static Collection<ContainerType> getOrCreateContainerTypes(
			List<TypeDTO> types) {
		HashSet<ContainerType> containerTypes = new HashSet<ContainerType>();
		for (TypeDTO type : types){
			ContainerType containerType;
			try{
				containerType = ContainerType.findContainerTypesByTypeNameEquals(type.typeName).getSingleResult();
			} catch(NoResultException e){
				containerType = new ContainerType();
				containerType.setTypeName(type.typeName);
				containerType.persist();
			}
			containerTypes.add(containerType);
		}
		return containerTypes;
	}
	
	public static Collection<StateType> getOrCreateStateTypes(
			List<TypeDTO> types) {
		HashSet<StateType> stateTypes = new HashSet<StateType>();
		for (TypeDTO type : types){
			StateType stateType;
			try{
				stateType = StateType.findStateTypesByTypeNameEquals(type.typeName).getSingleResult();
			} catch(NoResultException e){
				stateType = new StateType();
				stateType.setTypeName(type.typeName);
				stateType.persist();
			}
			stateTypes.add(stateType);
		}
		return stateTypes;
	}
	
	public static Collection<ValueType> getOrCreateValueTypes(
			List<TypeDTO> types) {
		HashSet<ValueType> valueTypes = new HashSet<ValueType>();
		for (TypeDTO type : types){
			ValueType valueType;
			try{
				valueType = ValueType.findValueTypesByTypeNameEquals(type.typeName).getSingleResult();
			} catch(NoResultException e){
				valueType = new ValueType();
				valueType.setTypeName(type.typeName);
				valueType.persist();
				valueType.flush();
			}
			valueTypes.add(valueType);
		}
		return valueTypes;
	}
	
	public static Collection<LabelType> getOrCreateLabelTypes(
			List<TypeDTO> types) {
		HashSet<LabelType> labelTypes = new HashSet<LabelType>();
		for (TypeDTO type : types){
			LabelType labelType;
			try{
				labelType = LabelType.findLabelTypesByTypeNameEquals(type.typeName).getSingleResult();
			} catch(NoResultException e){
				labelType = new LabelType();
				labelType.setTypeName(type.typeName);
				labelType.persist();
			}
			labelTypes.add(labelType);
		}
		return labelTypes;
	}
	
	public static Collection<ThingType> getOrCreateThingTypes(
			List<TypeDTO> types) {
		HashSet<ThingType> thingTypes = new HashSet<ThingType>();
		for (TypeDTO type : types){
			ThingType thingType;
			try{
				thingType = ThingType.findThingTypesByTypeNameEquals(type.typeName).getSingleResult();
			} catch(NoResultException e){
				thingType = new ThingType();
				thingType.setTypeName(type.typeName);
				thingType.persist();
			}
			thingTypes.add(thingType);
		}
		return thingTypes;
	}
	
	public static Collection<OperatorType> getOrCreateOperatorTypes(
			List<TypeDTO> types) {
		HashSet<OperatorType> operatorTypes = new HashSet<OperatorType>();
		for (TypeDTO type : types){
			OperatorType operatorType;
			try{
				operatorType = OperatorType.findOperatorTypesByTypeNameEquals(type.typeName).getSingleResult();
			} catch(NoResultException e){
				operatorType = new OperatorType();
				operatorType.setTypeName(type.typeName);
				operatorType.persist();
			}
			operatorTypes.add(operatorType);
		}
		return operatorTypes;
	}
	
	public static Collection<UnitType> getOrCreateUnitTypes(
			List<TypeDTO> types) {
		HashSet<UnitType> unitTypes = new HashSet<UnitType>();
		for (TypeDTO type : types){
			UnitType unitType;
			try{
				unitType = UnitType.findUnitTypesByTypeNameEquals(type.typeName).getSingleResult();
			} catch(NoResultException e){
				unitType = new UnitType();
				unitType.setTypeName(type.typeName);
				unitType.persist();
			}
			unitTypes.add(unitType);
		}
		return unitTypes;
	}
	
	public static Collection<DDictType> getOrCreateDDictTypes(
			List<TypeDTO> types) {
		HashSet<DDictType> dDictTypes = new HashSet<DDictType>();
		for (TypeDTO type : types){
			DDictType dDictType;
			try{
				dDictType = DDictType.findDDictTypesByNameEquals(type.typeName).getSingleResult();
			} catch(NoResultException e){
				dDictType = new DDictType();
				dDictType.setName(type.typeName);
				dDictType.persist();
			}
			dDictTypes.add(dDictType);
		}
		return dDictTypes;
	}
	
	public static Collection<RoleType> getOrCreateRoleTypes(
			List<TypeDTO> types) {
		HashSet<RoleType> roleTypes = new HashSet<RoleType>();
		for (TypeDTO type : types){
			RoleType roleType;
			try{
				roleType = RoleType.findRoleTypesByTypeNameEquals(type.typeName).getSingleResult();
			} catch(NoResultException e){
				roleType = new RoleType();
				roleType.setTypeName(type.typeName);
				roleType.persist();
			}
			roleTypes.add(roleType);
		}
		return roleTypes;
	}

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getTypeName() {
        return this.typeName;
    }

	public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static TypeDTO fromJsonToTypeDTO(String json) {
        return new JSONDeserializer<TypeDTO>()
        .use(null, TypeDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<TypeDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<TypeDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<TypeDTO> fromJsonArrayToTypeDTO(String json) {
        return new JSONDeserializer<List<TypeDTO>>()
        .use("values", TypeDTO.class).deserialize(json);
    }
}


