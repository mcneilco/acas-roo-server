package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class ContainerLocationTreeDTO {
	
	private String codeName;
	private String parentCodeName;
	private String labelText;
	private String codeTree;
	private String labelTree;
	private Integer level;
	private String rootCodeName;
	private String codeNameBreadcrumb;
	private String labelTextBreadcrumb;
	
	public ContainerLocationTreeDTO (String codeName, String parentCodeName, String labelText, String codeTree, String labelTree, 
			Integer level, String rootCodeName, String codeNameBreadcrumb, String labelTextBreadcrumb) {
		this.codeName = codeName;
		this.parentCodeName = parentCodeName;
		this.labelText = labelText;
		this.codeTree = codeTree;
		this.labelTree = labelTree;
		this.level =  level;
		this.rootCodeName = rootCodeName;
		this.codeNameBreadcrumb = codeNameBreadcrumb;
		this.labelTextBreadcrumb = labelTextBreadcrumb;
	}

}
