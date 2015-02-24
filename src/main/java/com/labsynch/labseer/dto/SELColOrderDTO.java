package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class SELColOrderDTO implements Comparable {
	
	private Long stateId;

	private String columnName;
	
	private Integer columnOrder;
	
	private boolean columnDisplay;
	
	private String lsType;
	
	private String lsKind;

	private boolean publicData;
	


	public SELColOrderDTO() {
	}
	
	@Override
	public int compareTo(Object compareObject) {
		Integer compareColOrder = ((SELColOrderDTO) compareObject).columnOrder;
		return this.columnOrder - compareColOrder;
	}
	
}


