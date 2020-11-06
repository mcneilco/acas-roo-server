package com.labsynch.labseer.dto;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.labsynch.labseer.domain.Container;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;

public class ContainerValueRequestDTO {

	private String containerType;
	private String containerKind;
	private String stateType;
	private String stateKind;
	private String valueType;
	private String valueKind;
	private String value;
	


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

	public static ContainerValueRequestDTO fromJsonToContainerValueRequestDTO(String json) {
        return new JSONDeserializer<ContainerValueRequestDTO>()
        .use(null, ContainerValueRequestDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ContainerValueRequestDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<ContainerValueRequestDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<ContainerValueRequestDTO> fromJsonArrayToCoes(String json) {
        return new JSONDeserializer<List<ContainerValueRequestDTO>>()
        .use("values", ContainerValueRequestDTO.class).deserialize(json);
    }

	public String getContainerType() {
        return this.containerType;
    }

	public void setContainerType(String containerType) {
        this.containerType = containerType;
    }

	public String getContainerKind() {
        return this.containerKind;
    }

	public void setContainerKind(String containerKind) {
        this.containerKind = containerKind;
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

	public String getValue() {
        return this.value;
    }

	public void setValue(String value) {
        this.value = value;
    }
}


