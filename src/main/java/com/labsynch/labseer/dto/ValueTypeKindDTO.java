package com.labsynch.labseer.dto;

import javax.persistence.NoResultException;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.ValueKind;
import com.labsynch.labseer.domain.ValueType;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;

@RooJavaBean
@RooToString
@RooJson
public class ValueTypeKindDTO {
	
	private String lsType;
	
	private String lsKind;
	
	private int displayOrder;
	
	private ValueKind valueKind;

	public ValueTypeKindDTO(
			String lsType, 
			String lsKind){
		
		this.lsType = lsType;
		this.lsKind = lsKind;
	}


	public ValueTypeKindDTO() {
	}
	
	@Transactional
	public void findValueKind(){
		ValueType valueType;
    	try{
    		valueType = ValueType.findValueTypesByTypeNameEquals(this.lsType).getSingleResult();
    	} catch(NoResultException e){
    		this.valueKind = null;
    		valueType = null;
    	}
    	ValueKind valueKind;
    	try{
    		valueKind = ValueKind.findValueKindsByKindNameEqualsAndLsType(this.lsKind, valueType).getSingleResult();
    		this.valueKind = valueKind;
    	} catch(NoResultException e){
    		this.valueKind = null;
    	}
	}

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static ValueTypeKindDTO fromJsonToValueTypeKindDTO(String json) {
        return new JSONDeserializer<ValueTypeKindDTO>()
        .use(null, ValueTypeKindDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ValueTypeKindDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<ValueTypeKindDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<ValueTypeKindDTO> fromJsonArrayToValueTypeKindDTO(String json) {
        return new JSONDeserializer<List<ValueTypeKindDTO>>()
        .use("values", ValueTypeKindDTO.class).deserialize(json);
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

	public int getDisplayOrder() {
        return this.displayOrder;
    }

	public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

	public ValueKind getValueKind() {
        return this.valueKind;
    }

	public void setValueKind(ValueKind valueKind) {
        this.valueKind = valueKind;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}


