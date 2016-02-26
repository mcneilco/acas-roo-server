package com.labsynch.labseer.dto;

import java.util.Collection;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class ContainerErrorMessageDTO {

	String containerCodeName;
	
	String level;
	
	String message;
	
	Container container;
	
	Container definition;
	
	public String toJson() {
        return new JSONSerializer().include("container.lsStates.lsValues","container.lsLabels","definition.lsStates.lsValues","definition.lsLabels").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
	
	public static String toJsonArray(Collection<ContainerErrorMessageDTO> collection) {
        return new JSONSerializer().include("container.lsStates.lsValues","container.lsLabels","definition.lsStates.lsValues","definition.lsLabels").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
    }
	
	public ContainerErrorMessageDTO(){
	}
	
	public ContainerErrorMessageDTO(String containerCodeName, Container container){
		this.containerCodeName = containerCodeName;
		this.container = container;
	}
	
}
