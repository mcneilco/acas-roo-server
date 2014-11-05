package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.DDictValue;

@RooJavaBean
@RooToString
@RooJson
public class CodeTableDTO {

	public CodeTableDTO() {
	}

	
	public CodeTableDTO(DDictValue dDictVal) {
		this.setId(dDictVal.getId());
		this.setCode(dDictVal.getShortName());
		this.setName(dDictVal.getLabelText());
		this.setIgnored(dDictVal.isIgnored());
		this.setDisplayOrder(dDictVal.getDisplayOrder());
	}

	private String code;

	private String codeName;  //code and codeName should be the same. Prefer to use codeName but older client code may be using code.
	//some classes use code, some use codeName
	private String name;
	
	private boolean ignored;
	
	private Integer displayOrder;
	
	private Long id;

}


