package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class TempThingDTO {

    private static final Logger logger = LoggerFactory.getLogger(TempThingDTO.class);

    public TempThingDTO() {
        // empty constructor
    }

    private Long parentId;
    private String parentCodeName;

    private Long id;
    private String codeName;
    private String tempId;

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

    public static TempThingDTO fromJsonToTempThingDTO(String json) {
        return new JSONDeserializer<TempThingDTO>()
                .use(null, TempThingDTO.class).deserialize(json);
    }

    public static String toJsonArray(Collection<TempThingDTO> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<TempThingDTO> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<TempThingDTO> fromJsonArrayToTempThingDTO(String json) {
        return new JSONDeserializer<List<TempThingDTO>>()
                .use("values", TempThingDTO.class).deserialize(json);
    }

    public Long getParentId() {
        return this.parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentCodeName() {
        return this.parentCodeName;
    }

    public void setParentCodeName(String parentCodeName) {
        this.parentCodeName = parentCodeName;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeName() {
        return this.codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getTempId() {
        return this.tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
    }
}
