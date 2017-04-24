package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class StructureSearchDTO {
	
	private String queryMol;
	private String lsType;
	private String lsKind;
	private String searchType;
	private Integer maxResults;
	private Float similarity;
	
	public StructureSearchDTO(){
	}
	
	public StructureSearchDTO(String queryMol, String lsType, String lsKind, String searchType, Integer maxResults, Float similarity){
		this.queryMol = queryMol;
		this.lsType = lsType;
		this.lsKind = lsKind;
		this.searchType = searchType;
		this.maxResults = maxResults;
		this.similarity = similarity;
	}

}


