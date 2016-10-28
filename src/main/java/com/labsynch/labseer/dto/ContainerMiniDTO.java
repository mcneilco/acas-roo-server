package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.Container;

@RooJavaBean
@RooToString
@RooJson
public class ContainerMiniDTO {
	
    public ContainerMiniDTO(Container container) {
    	this.setId(container.getId());
    	this.setVersion(container.getVersion());
    }

	private Long id;
    
	private Integer version;
	

}


