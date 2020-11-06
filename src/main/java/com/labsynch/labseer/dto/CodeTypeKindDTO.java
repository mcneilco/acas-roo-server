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
public class CodeTypeKindDTO {
		
	private String codeName;
	
	private String lsType;
	
	private String lsKind;
	
	public CodeTypeKindDTO(){
	}
	
	public CodeTypeKindDTO(String codeName, String lsType, String lsKind){
		this.codeName = codeName;
		this.lsType = lsType;
		this.lsKind = lsKind;
	}
	
	public String toJson() {
        return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
	
	public static String toJsonArray(Collection<CodeTypeKindDTO> collection) {
        return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
    }
	


	public static CodeTypeKindDTO fromJsonToCodeTypeKindDTO(String json) {
        return new JSONDeserializer<CodeTypeKindDTO>()
        .use(null, CodeTypeKindDTO.class).deserialize(json);
    }

	public static Collection<CodeTypeKindDTO> fromJsonArrayToCoes(String json) {
        return new JSONDeserializer<List<CodeTypeKindDTO>>()
        .use("values", CodeTypeKindDTO.class).deserialize(json);
    }

	public String getCodeName() {
        return this.codeName;
    }

	public void setCodeName(String codeName) {
        this.codeName = codeName;
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

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}


