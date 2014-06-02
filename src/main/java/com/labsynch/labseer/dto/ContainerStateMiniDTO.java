package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.ContainerState;

@RooJavaBean
@RooToString
@RooJson
public class ContainerStateMiniDTO {
	
    public ContainerStateMiniDTO(ContainerState state) {
    	this.setId(state.getId());
    }

	private Long id;
	
	private Integer version;

	private ContainerMiniDTO container;

}

