package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class IdCollectionDTO {

    private Long id;

    private Integer version;

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

    public static IdCollectionDTO fromJsonToIdCollectionDTO(String json) {
        return new JSONDeserializer<IdCollectionDTO>()
                .use(null, IdCollectionDTO.class).deserialize(json);
    }

    public static String toJsonArray(Collection<IdCollectionDTO> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<IdCollectionDTO> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<IdCollectionDTO> fromJsonArrayToIdCollectioes(String json) {
        return new JSONDeserializer<List<IdCollectionDTO>>()
                .use("values", IdCollectionDTO.class).deserialize(json);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
