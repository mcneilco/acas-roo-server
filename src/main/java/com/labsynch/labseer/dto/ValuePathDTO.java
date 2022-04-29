package com.labsynch.labseer.dto;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.labsynch.labseer.domain.ItxLsThingLsThing;
import com.labsynch.labseer.domain.ItxLsThingLsThingState;
import com.labsynch.labseer.domain.ItxLsThingLsThingValue;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.LsThingState;
import com.labsynch.labseer.domain.LsThingValue;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class ValuePathDTO {

	public ValuePathDTO() {

	}

	private String entity;
	private String entityType;
	private String entityKind;
	private String stateType;
	private String stateKind;
	private String valueType;
	private String valueKind;

	public String extractStringValue(LsThing lsThing) {
		if (entity.equalsIgnoreCase("LsThing")
				&& entityType.equals(lsThing.getLsType())
				&& entityKind.equals(lsThing.getLsKind())) {
			Set<LsThingState> lsStates = lsThing.getLsStates();
			for (LsThingState lsState : lsStates) {
				if (stateType.equals(lsState.getLsType()) && stateKind.equals(lsState.getLsKind())) {
					Set<LsThingValue> lsValues = lsState.getLsValues();
					for (LsThingValue lsValue : lsValues) {
						if (valueType.equals(lsValue.getLsType()) && valueKind.equals(lsValue.getLsKind())) {
							if (valueType.equals("stringValue"))
								return lsValue.getStringValue();
							if (valueType.equals("codeValue"))
								return lsValue.getCodeValue();
						}
					}
				}
			}
		}
		return null;
	}

	public BigDecimal extractNumericValue(LsThing lsThing) {
		if (entity.equalsIgnoreCase("LsThing")
				&& entityType.equals(lsThing.getLsType())
				&& entityKind.equals(lsThing.getLsKind())) {
			Set<LsThingState> lsStates = lsThing.getLsStates();
			for (LsThingState lsState : lsStates) {
				if (stateType.equals(lsState.getLsType()) && stateKind.equals(lsState.getLsKind())) {
					Set<LsThingValue> lsValues = lsState.getLsValues();
					for (LsThingValue lsValue : lsValues) {
						if (valueType.equals(lsValue.getLsType()) && valueKind.equals(lsValue.getLsKind())) {
							if (valueType.equals("numericValue"))
								return lsValue.getNumericValue();
						}
					}
				}
			}
		}
		return null;
	}

	public String extractStringValue(ItxLsThingLsThing ItxLsThingLsThing) {
		if (entity.equalsIgnoreCase("ItxLsThingLsThing")
				&& entityType.equals(ItxLsThingLsThing.getLsType())
				&& entityKind.equals(ItxLsThingLsThing.getLsKind())) {
			Set<ItxLsThingLsThingState> lsStates = ItxLsThingLsThing.getLsStates();
			for (ItxLsThingLsThingState lsState : lsStates) {
				if (stateType.equals(lsState.getLsType()) && stateKind.equals(lsState.getLsKind())) {
					Set<ItxLsThingLsThingValue> lsValues = lsState.getLsValues();
					for (ItxLsThingLsThingValue lsValue : lsValues) {
						if (valueType.equals(lsValue.getLsType()) && valueKind.equals(lsValue.getLsKind())) {
							if (valueType.equals("stringValue"))
								return lsValue.getStringValue();
							if (valueType.equals("codeValue"))
								return lsValue.getCodeValue();
						}
					}
				}
			}
		}
		return null;
	}

	public BigDecimal extractNumericValue(ItxLsThingLsThing ItxLsThingLsThing) {
		if (entity.equalsIgnoreCase("ItxLsThingLsThing")
				&& entityType.equals(ItxLsThingLsThing.getLsType())
				&& entityKind.equals(ItxLsThingLsThing.getLsKind())) {
			Set<ItxLsThingLsThingState> lsStates = ItxLsThingLsThing.getLsStates();
			for (ItxLsThingLsThingState lsState : lsStates) {
				if (stateType.equals(lsState.getLsType()) && stateKind.equals(lsState.getLsKind())) {
					Set<ItxLsThingLsThingValue> lsValues = lsState.getLsValues();
					for (ItxLsThingLsThingValue lsValue : lsValues) {
						if (valueType.equals(lsValue.getLsType()) && valueKind.equals(lsValue.getLsKind())) {
							if (valueType.equals("numericValue"))
								return lsValue.getNumericValue();
						}
					}
				}
			}
		}
		return null;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public String toJson() {
		return new JSONSerializer()
				.exclude("*.class").serialize(this);
	}

	public String toJson(String[] fields) {
		return new JSONSerializer()
				.include(fields).exclude("*.class").serialize(this);
	}

	public static ValuePathDTO fromJsonToValuePathDTO(String json) {
		return new JSONDeserializer<ValuePathDTO>()
				.use(null, ValuePathDTO.class).deserialize(json);
	}

	public static String toJsonArray(Collection<ValuePathDTO> collection) {
		return new JSONSerializer()
				.exclude("*.class").serialize(collection);
	}

	public static String toJsonArray(Collection<ValuePathDTO> collection, String[] fields) {
		return new JSONSerializer()
				.include(fields).exclude("*.class").serialize(collection);
	}

	public static Collection<ValuePathDTO> fromJsonArrayToValuePathDTO(String json) {
		return new JSONDeserializer<List<ValuePathDTO>>()
				.use("values", ValuePathDTO.class).deserialize(json);
	}

	public String getEntity() {
		return this.entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getEntityType() {
		return this.entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getEntityKind() {
		return this.entityKind;
	}

	public void setEntityKind(String entityKind) {
		this.entityKind = entityKind;
	}

	public String getStateType() {
		return this.stateType;
	}

	public void setStateType(String stateType) {
		this.stateType = stateType;
	}

	public String getStateKind() {
		return this.stateKind;
	}

	public void setStateKind(String stateKind) {
		this.stateKind = stateKind;
	}

	public String getValueType() {
		return this.valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public String getValueKind() {
		return this.valueKind;
	}

	public void setValueKind(String valueKind) {
		this.valueKind = valueKind;
	}
}
