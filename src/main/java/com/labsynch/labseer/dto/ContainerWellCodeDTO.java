package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.utils.ExcludeNulls;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class ContainerWellCodeDTO {
	
	private String requestCodeName;
	private List<String> wellCodeNames;
	
	public ContainerWellCodeDTO(){
	}
	
	public ContainerWellCodeDTO(String requestCodeName, List<String> wellCodeNames){
		this.requestCodeName = requestCodeName;
		this.wellCodeNames = wellCodeNames;
	}
	
	public String toJson() {
        return new JSONSerializer().include("wellCodeNames").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
	
	public static String toJsonArray(Collection<ContainerWellCodeDTO> collection) {
        return new JSONSerializer().include("wellCodeNames").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
    }


	public static ContainerWellCodeDTO fromJsonToContainerWellCodeDTO(String json) {
        return new JSONDeserializer<ContainerWellCodeDTO>()
        .use(null, ContainerWellCodeDTO.class).deserialize(json);
    }

	public static Collection<ContainerWellCodeDTO> fromJsonArrayToContainerWellCoes(String json) {
        return new JSONDeserializer<List<ContainerWellCodeDTO>>()
        .use("values", ContainerWellCodeDTO.class).deserialize(json);
    }

	public String getRequestCodeName() {
        return this.requestCodeName;
    }

	public void setRequestCodeName(String requestCodeName) {
        this.requestCodeName = requestCodeName;
    }

	public List<String> getWellCodeNames() {
        return this.wellCodeNames;
    }

	public void setWellCodeNames(List<String> wellCodeNames) {
        this.wellCodeNames = wellCodeNames;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}


