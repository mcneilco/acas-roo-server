package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class SearchCdIdReturnDTO{

    private String corpName;
    
	private int CdId;
    
	private String stereoCategoryName;

	private String stereoComment;

}
