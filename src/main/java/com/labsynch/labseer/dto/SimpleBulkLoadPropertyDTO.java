package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class SimpleBulkLoadPropertyDTO {

    private String name;
    
    private String dataType;
    
    private Boolean required;
    
	private Integer displayOrder;
	
    private Boolean ignored;
    
    public SimpleBulkLoadPropertyDTO(){
    	
    }
    
    public SimpleBulkLoadPropertyDTO(String name){
    	this.name = name;
    }
    
    public String toJson() {
        return new JSONSerializer().exclude("*.class", "class").include("name","dataType","required","displayOrder","ignored").transform(new ExcludeNulls(), void.class).serialize(this);
    }
    
    @Override
    public int hashCode(){
    	int nameHashCode = 0;
    	int dataTypeHashCode = 0;
    	int requiredHashCode = 0;
    	int displayOrderHashCode = 0;
    	int ignoredHashCode = 0;
    	if (name != null) nameHashCode+=name.hashCode();
    	if (dataType != null) dataTypeHashCode+=dataType.hashCode();
    	if (required != null) requiredHashCode+=required.hashCode();
    	if (displayOrder != null) displayOrderHashCode+=displayOrder.hashCode();
    	if (ignored != null) ignoredHashCode+=ignored.hashCode();
    	return nameHashCode + dataTypeHashCode + requiredHashCode + displayOrderHashCode + ignoredHashCode;
    }
    
    @Override
    public boolean equals(Object that){
    	if (that.getClass() != SimpleBulkLoadPropertyDTO.class) return false;
    	else{
    		SimpleBulkLoadPropertyDTO thatDTO = (SimpleBulkLoadPropertyDTO) that;
    		boolean nameEqual = false;
    		if (this.name == null && thatDTO.name == null) nameEqual = true;
    		else if (this.name == null && thatDTO.name != null) nameEqual = false;
    		else if (this.name != null && thatDTO.name == null) nameEqual = false;
    		else if (this.name != null && thatDTO.name != null) nameEqual = this.name.equals(thatDTO.name);
    		boolean dataTypeEqual = false;
    		if (this.dataType == null && thatDTO.dataType == null) dataTypeEqual = true;
    		else if (this.dataType == null && thatDTO.dataType != null) dataTypeEqual = false;
    		else if (this.dataType != null && thatDTO.dataType == null) dataTypeEqual = false;
    		else if (this.dataType != null && thatDTO.dataType != null) dataTypeEqual = this.dataType.equals(thatDTO.dataType);
    		boolean requiredEqual = false;
    		if (this.required == null && thatDTO.required == null) requiredEqual = true;
    		else if (this.required == null && thatDTO.required != null) requiredEqual = false;
    		else if (this.required != null && thatDTO.required == null) requiredEqual = false;
    		else if (this.required != null && thatDTO.required != null) requiredEqual = this.required.equals(thatDTO.required);
    		boolean displayOrderEqual = false;
    		if (this.displayOrder == null && thatDTO.displayOrder == null) displayOrderEqual = true;
    		else if (this.displayOrder == null && thatDTO.displayOrder != null) displayOrderEqual = false;
    		else if (this.displayOrder != null && thatDTO.displayOrder == null) displayOrderEqual = false;
    		else if (this.displayOrder != null && thatDTO.displayOrder != null) displayOrderEqual = this.displayOrder.equals(thatDTO.displayOrder);
    		boolean ignoredEqual = false;
    		if (this.ignored == null && thatDTO.ignored == null) ignoredEqual = true;
    		else if (this.ignored == null && thatDTO.ignored != null) ignoredEqual = false;
    		else if (this.ignored != null && thatDTO.ignored == null) ignoredEqual = false;
    		else if (this.ignored != null && thatDTO.ignored != null) ignoredEqual = this.ignored.equals(thatDTO.ignored);
    		return (nameEqual & dataTypeEqual & requiredEqual & displayOrderEqual & ignoredEqual);
    	}
    }
}
