package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;

@RooJavaBean
@RooToString
@RooJson
public class SearchCompoundReturnDTO{

    private String corpName;
    
    private String corpNameType;
    
    private List<ParentAliasDTO> parentAliases;

	private String stereoCategoryName;

	private String stereoComment;

	private List<SearchLotDTO> lotIDs = new ArrayList<SearchLotDTO>();

	private String molStructure;


	public String toJson() {
        return new JSONSerializer().exclude("*.class")
        		.transform( new DateTransformer( "MM/dd/yyyy"), Date.class)
        		.serialize(this);
    }

	public static SearchCompoundReturnDTO fromJsonToSearchCompoundReturnDTO(String json) {
        return new JSONDeserializer<SearchCompoundReturnDTO>().use(null, SearchCompoundReturnDTO.class)
        		.use( Date.class, new DateTransformer( "MM/dd/yyyy"))
        		.deserialize(json);
    }

	public static String toJsonArray(Collection<SearchCompoundReturnDTO> collection) {
        return new JSONSerializer().exclude("*.class").include("lotIDs", "parentAliases")
        		.transform( new DateTransformer( "MM/dd/yyyy"), Date.class)
        		.serialize(collection);
    }

	public static Collection<SearchCompoundReturnDTO> fromJsonArrayToSearchCompoes(String json) {
        return new JSONDeserializer<List<SearchCompoundReturnDTO>>().use(null, ArrayList.class).use("values", SearchCompoundReturnDTO.class)
        		.use( Date.class, new DateTransformer( "MM/dd/yyyy"))
        		.deserialize(json);
    }
}
