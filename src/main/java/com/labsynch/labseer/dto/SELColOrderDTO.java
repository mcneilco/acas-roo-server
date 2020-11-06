package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SELColOrderDTO implements Comparable {
	
	private Long stateId;

	private String columnName;
	
	private Integer columnOrder;
	
	private boolean columnDisplay;
	
	private String lsType;
	
	private String lsKind;

	private boolean publicData;
	


	public SELColOrderDTO() {
	}
	
	@Override
	public int compareTo(Object compareObject) {
		Integer compareColOrder = ((SELColOrderDTO) compareObject).columnOrder;
		return this.columnOrder - compareColOrder;
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

	public static SELColOrderDTO fromJsonToSELColOrderDTO(String json) {
        return new JSONDeserializer<SELColOrderDTO>()
        .use(null, SELColOrderDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<SELColOrderDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<SELColOrderDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<SELColOrderDTO> fromJsonArrayToSELCoes(String json) {
        return new JSONDeserializer<List<SELColOrderDTO>>()
        .use("values", SELColOrderDTO.class).deserialize(json);
    }

	public Long getStateId() {
        return this.stateId;
    }

	public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

	public String getColumnName() {
        return this.columnName;
    }

	public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

	public Integer getColumnOrder() {
        return this.columnOrder;
    }

	public void setColumnOrder(Integer columnOrder) {
        this.columnOrder = columnOrder;
    }

	public boolean isColumnDisplay() {
        return this.columnDisplay;
    }

	public void setColumnDisplay(boolean columnDisplay) {
        this.columnDisplay = columnDisplay;
    }

	public String getLsType() {
        return this.lsType;
    }

	public void setLsType(String lsType) {
        this.lsType = lsType;
    }

	public String getLsKind() {
        return this.lsKind;
    }

	public void setLsKind(String lsKind) {
        this.lsKind = lsKind;
    }

	public boolean isPublicData() {
        return this.publicData;
    }

	public void setPublicData(boolean publicData) {
        this.publicData = publicData;
    }
}


