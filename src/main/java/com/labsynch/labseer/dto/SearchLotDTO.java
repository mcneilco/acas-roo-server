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
public class SearchLotDTO {

    private String corpName;

    private int lotNumber;
    
    private long buid;

    private List<LotAliasDTO> lotAliases;
    
    private Date registrationDate;

    private Date synthesisDate;

    
	public String toJson() {
        return new JSONSerializer().exclude("*.class")
        		.transform( new DateTransformer( "MM/dd/yyyy"), Date.class)
        		.serialize(this);
    }

	public static SearchLotDTO fromJsonToSearchLotDTO(String json) {
        return new JSONDeserializer<SearchLotDTO>().use(null, SearchLotDTO.class)
        		.use( Date.class, new DateTransformer( "MM/dd/yyyy"))
        		.deserialize(json);
    }

	public static String toJsonArray(Collection<SearchLotDTO> collection) {
        return new JSONSerializer().exclude("*.class")
        		.transform( new DateTransformer( "MM/dd/yyyy"), Date.class)
        		.serialize(collection);
    }

	public static Collection<SearchLotDTO> fromJsonArrayToSearchLoes(String json) {
        return new JSONDeserializer<List<SearchLotDTO>>().use(null, ArrayList.class).use("values", SearchLotDTO.class)
        		.use( Date.class, new DateTransformer( "MM/dd/yyyy"))
        		.deserialize(json);
    }
}
