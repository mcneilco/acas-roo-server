package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.persistence.NoResultException;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.ContainerKind;
import com.labsynch.labseer.domain.ContainerType;
import com.labsynch.labseer.domain.DDictKind;
import com.labsynch.labseer.domain.DDictType;
import com.labsynch.labseer.domain.ExperimentKind;
import com.labsynch.labseer.domain.ExperimentType;
import com.labsynch.labseer.domain.InteractionKind;
import com.labsynch.labseer.domain.InteractionType;
import com.labsynch.labseer.domain.LabelKind;
import com.labsynch.labseer.domain.LabelType;
import com.labsynch.labseer.domain.OperatorKind;
import com.labsynch.labseer.domain.OperatorType;
import com.labsynch.labseer.domain.ProtocolKind;
import com.labsynch.labseer.domain.ProtocolType;
import com.labsynch.labseer.domain.RoleKind;
import com.labsynch.labseer.domain.RoleType;
import com.labsynch.labseer.domain.StateKind;
import com.labsynch.labseer.domain.StateType;
import com.labsynch.labseer.domain.ThingKind;
import com.labsynch.labseer.domain.ThingType;
import com.labsynch.labseer.domain.UnitKind;
import com.labsynch.labseer.domain.UnitType;
import com.labsynch.labseer.domain.ValueKind;
import com.labsynch.labseer.domain.ValueType;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class TypeKindDTO {
	
	private static final Logger logger = LoggerFactory.getLogger(TypeKindDTO.class);
	
	private String typeName;
	
	private String kindName;
	
	public TypeKindDTO(
			String typeName, 
			String kindName){
		
		this.typeName = typeName;
		this.kindName = kindName;
	}


	public TypeKindDTO() {
	}
	
	public static Collection<ProtocolKind> getOrCreateProtocolKinds(
			List<TypeKindDTO> typeKinds) {
		HashSet<ProtocolKind> protocolKinds = new HashSet<ProtocolKind>();
		for (TypeKindDTO typeKind : typeKinds){
			ProtocolType protocolType;
			try{
				protocolType = ProtocolType.findProtocolTypesByTypeNameEquals(typeKind.typeName).getSingleResult();
			} catch (Exception e){
				logger.error("ProtocolType " + typeKind.typeName + " has not been created.");
				return null;
			}
			ProtocolKind protocolKind;
			try{
				protocolKind = ProtocolKind.findProtocolKindsByLsTypeEqualsAndKindNameEquals(protocolType, typeKind.kindName).getSingleResult();
			} catch(NoResultException e){
				protocolKind = new ProtocolKind();
				protocolKind.setLsType(protocolType);
				protocolKind.setKindName(typeKind.kindName);
				protocolKind.persist();
			}
			protocolKinds.add(protocolKind);
		}
		return protocolKinds;
	}
	
	public static Collection<ExperimentKind> getOrCreateExperimentKinds(
			List<TypeKindDTO> typeKinds) {
		HashSet<ExperimentKind> experimentKinds = new HashSet<ExperimentKind>();
		for (TypeKindDTO typeKind : typeKinds){
			ExperimentType experimentType;
			try{
				experimentType = ExperimentType.findExperimentTypesByTypeNameEquals(typeKind.typeName).getSingleResult();
			} catch (Exception e){
				logger.error("ExperimentType " + typeKind.typeName + " has not been created.");
				return null;
			}
			ExperimentKind experimentKind;
			try{
				experimentKind = ExperimentKind.findExperimentKindsByLsTypeEqualsAndKindNameEquals(experimentType, typeKind.kindName).getSingleResult();
			} catch(NoResultException e){
				experimentKind = new ExperimentKind();
				experimentKind.setLsType(experimentType);
				experimentKind.setKindName(typeKind.kindName);
				experimentKind.persist();
			}
			experimentKinds.add(experimentKind);
		}
		return experimentKinds;
	}
	
	public static Collection<InteractionKind> getOrCreateInteractionKinds(
			List<TypeKindDTO> typeKinds) {
		HashSet<InteractionKind> interactionKinds = new HashSet<InteractionKind>();
		for (TypeKindDTO typeKind : typeKinds){
			InteractionType interactionType;
			try{
				interactionType = InteractionType.findInteractionTypesByTypeNameEquals(typeKind.typeName).getSingleResult();
			} catch (Exception e){
				logger.error("InteractionType " + typeKind.typeName + " has not been created.");
				return null;
			}
			InteractionKind interactionKind;
			try{
				interactionKind = InteractionKind.findInteractionKindsByKindNameEqualsAndLsType(typeKind.kindName, interactionType).getSingleResult();
			} catch(NoResultException e){
				interactionKind = new InteractionKind();
				interactionKind.setLsType(interactionType);
				interactionKind.setKindName(typeKind.kindName);
				interactionKind.persist();
			}
			interactionKinds.add(interactionKind);
		}
		return interactionKinds;
	}
	
	public static Collection<ContainerKind> getOrCreateContainerKinds(
			List<TypeKindDTO> typeKinds) {
		HashSet<ContainerKind> containerKinds = new HashSet<ContainerKind>();
		for (TypeKindDTO typeKind : typeKinds){
			ContainerType containerType;
			try{
				containerType = ContainerType.findContainerTypesByTypeNameEquals(typeKind.typeName).getSingleResult();
			} catch (Exception e){
				logger.error("ContainerType " + typeKind.typeName + " has not been created.");
				return null;
			}
			ContainerKind containerKind;
			try{
				containerKind = ContainerKind.findContainerKindsByKindNameEqualsAndLsType(typeKind.kindName, containerType).getSingleResult();
			} catch(NoResultException e){
				containerKind = new ContainerKind();
				containerKind.setLsType(containerType);
				containerKind.setKindName(typeKind.kindName);
				containerKind.persist();
			}
			containerKinds.add(containerKind);
		}
		return containerKinds;
	}
	
	public static Collection<StateKind> getOrCreateStateKinds(
			List<TypeKindDTO> typeKinds) {
		HashSet<StateKind> stateKinds = new HashSet<StateKind>();
		for (TypeKindDTO typeKind : typeKinds){
			StateType stateType;
			try{
				stateType = StateType.findStateTypesByTypeNameEquals(typeKind.typeName).getSingleResult();
			} catch (Exception e){
				logger.error("StateType " + typeKind.typeName + " has not been created.");
				return null;
			}
			StateKind stateKind;
			try{
				stateKind = StateKind.findStateKindsByKindNameEqualsAndLsType(typeKind.kindName, stateType).getSingleResult();
			} catch(NoResultException e){
				stateKind = new StateKind();
				stateKind.setLsType(stateType);
				stateKind.setKindName(typeKind.kindName);
				stateKind.persist();
			}
			stateKinds.add(stateKind);
		}
		return stateKinds;
	}
	
	public static Collection<ValueKind> getOrCreateValueKinds(
			List<TypeKindDTO> typeKinds) {
		HashSet<ValueKind> valueKinds = new HashSet<ValueKind>();
		for (TypeKindDTO typeKind : typeKinds){
			ValueType valueType;
			try{
				valueType = ValueType.findValueTypesByTypeNameEquals(typeKind.typeName).getSingleResult();
			} catch (Exception e){
				logger.error("ValueType " + typeKind.typeName + " has not been created.");
				return null;
			}
			ValueKind valueKind;
			try{
				valueKind = ValueKind.findValueKindsByKindNameEqualsAndLsType(typeKind.kindName, valueType).getSingleResult();
			} catch(NoResultException e){
				valueKind = new ValueKind();
				valueKind.setLsType(valueType);
				valueKind.setKindName(typeKind.kindName);
				valueKind.persist();
			}
			valueKinds.add(valueKind);
		}
		return valueKinds;
	}
	
	public static Collection<LabelKind> getOrCreateLabelKinds(
			List<TypeKindDTO> typeKinds) {
		HashSet<LabelKind> labelKinds = new HashSet<LabelKind>();
		for (TypeKindDTO typeKind : typeKinds){
			LabelType labelType;
			try{
				labelType = LabelType.findLabelTypesByTypeNameEquals(typeKind.typeName).getSingleResult();
			} catch (Exception e){
				logger.error("LabelType " + typeKind.typeName + " has not been created.");
				return null;
			}
			LabelKind labelKind;
			try{
				labelKind = LabelKind.findLabelKindsByKindNameEqualsAndLsType(typeKind.kindName, labelType).getSingleResult();
			} catch(NoResultException e){
				labelKind = new LabelKind();
				labelKind.setLsType(labelType);
				labelKind.setKindName(typeKind.kindName);
				labelKind.persist();
			}
			labelKinds.add(labelKind);
		}
		return labelKinds;
	}
	
	public static Collection<ThingKind> getOrCreateThingKinds(
			List<TypeKindDTO> typeKinds) {
		HashSet<ThingKind> thingKinds = new HashSet<ThingKind>();
		for (TypeKindDTO typeKind : typeKinds){
			ThingType thingType;
			try{
				thingType = ThingType.findThingTypesByTypeNameEquals(typeKind.typeName).getSingleResult();
			} catch (Exception e){
				logger.error("ThingType " + typeKind.typeName + " has not been created.");
				return null;
			}
			ThingKind thingKind;
			try{
				thingKind = ThingKind.findThingKindsByKindNameEqualsAndLsType(typeKind.kindName, thingType).getSingleResult();
			} catch(NoResultException e){
				thingKind = new ThingKind();
				thingKind.setLsType(thingType);
				thingKind.setKindName(typeKind.kindName);
				thingKind.persist();
			}
			thingKinds.add(thingKind);
		}
		return thingKinds;
	}
	
	public static Collection<OperatorKind> getOrCreateOperatorKinds(
			List<TypeKindDTO> typeKinds) {
		HashSet<OperatorKind> operatorKinds = new HashSet<OperatorKind>();
		for (TypeKindDTO typeKind : typeKinds){
			OperatorType operatorType;
			try{
				operatorType = OperatorType.findOperatorTypesByTypeNameEquals(typeKind.typeName).getSingleResult();
			} catch (Exception e){
				logger.error("OperatorType " + typeKind.typeName + " has not been created.");
				return null;
			}
			OperatorKind operatorKind;
			try{
				operatorKind = OperatorKind.findOperatorKindsByKindNameEqualsAndLsType(typeKind.kindName, operatorType).getSingleResult();
			} catch(NoResultException e){
				operatorKind = new OperatorKind();
				operatorKind.setLsType(operatorType);
				operatorKind.setKindName(typeKind.kindName);
				operatorKind.persist();
			}
			operatorKinds.add(operatorKind);
		}
		return operatorKinds;
	}
	
	public static Collection<UnitKind> getOrCreateUnitKinds(
			List<TypeKindDTO> typeKinds) {
		HashSet<UnitKind> unitKinds = new HashSet<UnitKind>();
		for (TypeKindDTO typeKind : typeKinds){
			UnitType unitType;
			try{
				unitType = UnitType.findUnitTypesByTypeNameEquals(typeKind.typeName).getSingleResult();
			} catch (Exception e){
				logger.error("UnitType " + typeKind.typeName + " has not been created.");
				return null;
			}
			UnitKind unitKind;
			try{
				unitKind = UnitKind.findUnitKindsByKindNameEqualsAndLsType(typeKind.kindName, unitType).getSingleResult();
			} catch(NoResultException e){
				unitKind = new UnitKind();
				unitKind.setLsType(unitType);
				unitKind.setKindName(typeKind.kindName);
				unitKind.persist();
			}
			unitKinds.add(unitKind);
		}
		return unitKinds;
	}
	
	public static Collection<DDictKind> getOrCreateDDictKinds(
			List<TypeKindDTO> typeKinds) {
		HashSet<DDictKind> dDictKinds = new HashSet<DDictKind>();
		for (TypeKindDTO typeKind : typeKinds){
			DDictType dDictType;
			try{
				dDictType = DDictType.findDDictTypesByNameEquals(typeKind.typeName).getSingleResult();
			} catch (Exception e){
				logger.error("DDictType " + typeKind.typeName + " has not been created.");
				return null;
			}
			DDictKind dDictKind;
			try{
				dDictKind = DDictKind.findDDictKindsByLsTypeEqualsAndNameEquals(dDictType.getName(), typeKind.kindName).getSingleResult();
			} catch(NoResultException e){
				dDictKind = new DDictKind();
				dDictKind.setLsType(dDictType.getName());
				dDictKind.setName(typeKind.kindName);
				dDictKind.persist();
			}
			dDictKinds.add(dDictKind);
		}
		return dDictKinds;
	}
	
	public static Collection<RoleKind> getOrCreateRoleKinds(
			List<TypeKindDTO> typeKinds) {
		HashSet<RoleKind> roleKinds = new HashSet<RoleKind>();
		for (TypeKindDTO typeKind : typeKinds){
			RoleType roleType;
			try{
				roleType = RoleType.findRoleTypesByTypeNameEquals(typeKind.typeName).getSingleResult();
			} catch (Exception e){
				logger.error("RoleType " + typeKind.typeName + " has not been created.");
				return null;
			}
			RoleKind roleKind;
			try{
				roleKind = RoleKind.findRoleKindsByKindNameEqualsAndLsType(typeKind.kindName, roleType).getSingleResult();
			} catch(NoResultException e){
				roleKind = new RoleKind();
				roleKind.setLsType(roleType);
				roleKind.setKindName(typeKind.kindName);
				roleKind.persist();
			}
			roleKinds.add(roleKind);
		}
		return roleKinds;
	}
	
	
	
	
	

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static TypeKindDTO fromJsonToTypeKindDTO(String json) {
        return new JSONDeserializer<TypeKindDTO>()
        .use(null, TypeKindDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<TypeKindDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<TypeKindDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<TypeKindDTO> fromJsonArrayToTypeKindDTO(String json) {
        return new JSONDeserializer<List<TypeKindDTO>>()
        .use("values", TypeKindDTO.class).deserialize(json);
    }

	public String getTypeName() {
        return this.typeName;
    }

	public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

	public String getKindName() {
        return this.kindName;
    }

	public void setKindName(String kindName) {
        this.kindName = kindName;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}


