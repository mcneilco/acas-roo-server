package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.Container;

@RooJavaBean
@RooToString
@RooJson
public class ContainerValueRequestDTO {

	private String containerType;
	private String containerKind;
	private String stateType;
	private String stateKind;
	private String valueType;
	private String valueKind;
	private String value;
	

}


