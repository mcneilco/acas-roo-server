package com.labsynch.labseer.dto;

import java.util.Date;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class EntrezDiscontinuedGeneDTO {
	
	//tax_id  GeneID  Discontinued_GeneID     Discontinued_Symbol     Discontinue_Date
	
    private String taxId;
    private String geneId;
    private String discontinuedGeneId;
    private String discontinuedSymbol;    
    private Date discontinuedDate;

}
