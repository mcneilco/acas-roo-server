package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


public class PreferredNameWithErrorDTO {

	private String requestName;

	private String preferredName;

	private String referenceName;
	
	private ErrorMessageDTO errorMesageDTO;
	
	

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

	public static PreferredNameWithErrorDTO fromJsonToPreferredNameWithErrorDTO(String json) {
        return new JSONDeserializer<PreferredNameWithErrorDTO>()
        .use(null, PreferredNameWithErrorDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<PreferredNameWithErrorDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<PreferredNameWithErrorDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<PreferredNameWithErrorDTO> fromJsonArrayToPreferredNameWithErroes(String json) {
        return new JSONDeserializer<List<PreferredNameWithErrorDTO>>()
        .use("values", PreferredNameWithErrorDTO.class).deserialize(json);
    }

	public String getRequestName() {
        return this.requestName;
    }

	public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

	public String getPreferredName() {
        return this.preferredName;
    }

	public void setPreferredName(String preferredName) {
        this.preferredName = preferredName;
    }

	public String getReferenceName() {
        return this.referenceName;
    }

	public void setReferenceName(String referenceName) {
        this.referenceName = referenceName;
    }

	public ErrorMessageDTO getErrorMesageDTO() {
        return this.errorMesageDTO;
    }

	public void setErrorMesageDTO(ErrorMessageDTO errorMesageDTO) {
        this.errorMesageDTO = errorMesageDTO;
    }
}


