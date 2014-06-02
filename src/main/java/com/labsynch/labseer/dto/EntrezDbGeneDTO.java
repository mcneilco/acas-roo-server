package com.labsynch.labseer.dto;

import java.util.Date;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class EntrezDbGeneDTO {
	
    private String taxId;
    private String geneId;
    private String symbol;
    private String locusTag;
    private String synonyms;
    private String dbXrefs;
    private String chromosome;
    private String mapLocation;
    private String description;
    private String typeOfGene;
    private String symbolFromAuthority;
    private String fullNameFromAuthority;
    private String nomenclatureStatus;
    private String otherDesignations;
    private Date modificationDate;

}
