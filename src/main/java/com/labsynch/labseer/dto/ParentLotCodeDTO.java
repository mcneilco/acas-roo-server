package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class ParentLotCodeDTO {
	
	private String requestName;
	
	private String referenceCode;
	
	private Collection<String> lotCodes;
	
	public ParentLotCodeDTO(){
		
	}
	
	public String toJson() {
        return new JSONSerializer()
        .include("lotCodes")
        .exclude("*.class").serialize(this);
    }
	
	public static String toJsonArray(Collection<ParentLotCodeDTO> collection) {
        return new JSONSerializer()
        .include("lotCodes")
        .exclude("*.class").serialize(collection);
    }


	public static ParentLotCodeDTO fromJsonToParentLotCodeDTO(String json) {
        return new JSONDeserializer<ParentLotCodeDTO>()
        .use(null, ParentLotCodeDTO.class).deserialize(json);
    }

	public static Collection<ParentLotCodeDTO> fromJsonArrayToParentLotCoes(String json) {
        return new JSONDeserializer<List<ParentLotCodeDTO>>()
        .use("values", ParentLotCodeDTO.class).deserialize(json);
    }

	public String getRequestName() {
        return this.requestName;
    }

	public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

	public String getReferenceCode() {
        return this.referenceCode;
    }

	public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

	public Collection<String> getLotCodes() {
        return this.lotCodes;
    }

	public void setLotCodes(Collection<String> lotCodes) {
        this.lotCodes = lotCodes;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
