package com.labsynch.labseer.dto;

import java.util.Collection;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.ContainerState;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class ContainerCodeNameStateDTO {
	
	 public ContainerCodeNameStateDTO() {
	 }
	
    public ContainerCodeNameStateDTO(String containerCodeName, ContainerState state) {
    		this.containerCodeName = containerCodeName;
    		this.lsState = state;
    }

	private String containerCodeName;
	
	private ContainerState lsState;
	
	public String toJson() {
        return new JSONSerializer().include("lsState.lsValues").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
	
	public static String toJsonArray(Collection<ContainerCodeNameStateDTO> collection) {
        return new JSONSerializer().include("lsState.lsValues").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
    }

}