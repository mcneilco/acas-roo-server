package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.Scientist;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;

@RooJavaBean
@RooToString
@RooJson
public class SearchFormDTO{

	private String corpNameList;

	private List<String> formattedCorpNameList;

	private String corpNameFrom;

	private String corpNameTo;
	
	private String aliasContSelect;
	
	private String alias;
	
	private String dateFrom;
    
	private String dateTo;

	private Date minSynthDate;
    
	private Date maxSynthDate;
	
	private String searchType;

	private Float percentSimilarity;

	private Scientist chemist;
	
	private String molStructure;
	
	private Long buidNumber;

	private Long minParentNumber;
	
	private Long maxParentNumber;
	
	private String parentCorpName;
	
	private String saltFormCorpName;
	
	private String lotCorpName;
	
	private boolean valuesSet;
	
	private String loggedInUser;
	
	private Integer maxResults;
  

	public String toJson() {
        return new JSONSerializer().exclude("*.class")
        		.transform( new DateTransformer( "MM/dd/yyyy"), Date.class)
        		.serialize(this);
    }

	public static SearchFormDTO fromJsonToSearchFormDTO(String json) {
        return new JSONDeserializer<SearchFormDTO>().use(null, SearchFormDTO.class)
        		.use( Date.class, new DateTransformer( "MM/dd/yyyy"))
        		.deserialize(json);
    }

	public static String toJsonArray(Collection<SearchFormDTO> collection) {
        return new JSONSerializer().exclude("*.class")
        		.transform( new DateTransformer( "MM/dd/yyyy"), Date.class)
        		.serialize(collection);
    }

	public static Collection<SearchFormDTO> fromJsonArrayToSearchFoes(String json) {
        return new JSONDeserializer<List<SearchFormDTO>>().use(null, ArrayList.class).use("values", SearchFormDTO.class)
        		.use( Date.class, new DateTransformer( "MM/dd/yyyy"))
        		.deserialize(json);
    }
}
