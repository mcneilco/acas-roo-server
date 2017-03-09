package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class StructureSearchDTO {
	
	private String queryMol;
	private String searchType;
	private Integer maxResults;
	private Float similarity;
	
	public StructureSearchDTO(){
	}
	
	public StructureSearchDTO(String queryMol, String searchType, Integer maxResults, Float similarity){
		this.queryMol = queryMol;
		this.searchType = searchType;
		this.maxResults = maxResults;
		this.similarity = similarity;
	}

}


