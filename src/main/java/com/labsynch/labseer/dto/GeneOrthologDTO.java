package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.utils.ExcludeNulls;

@RooJavaBean
@RooToString
@RooJson
public class GeneOrthologDTO {
	
	private Long id;
	private String recordedBy;

    private Integer rowIndex;

	private String geneId;
    private String symbol;
    private String taxId;
    private String species;
    private String typeOfGene;

    private String mappedGeneId;
    private String mappedSymbol;
    private String mappedTaxId;
    private String mappedSpecies;
    private String mappedTypeOfGene;
    
    private Integer mappedScore;
    private Integer mappedPerc;
    private Integer mappedHitLen;
    private Double mappedPercRatio;
    
    private Integer mappedLocalIndex;
    private Integer mappedGeneIndex;
    private Integer mappedIsoformIndex;
    
    private String orthCode;
    private String versionName;
    private Integer curationLevel;
    private String curator;

	public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
	

	public static GeneOrthologDTO fromJsonToGeneOrthologDTO(String json) {
        return new JSONDeserializer<GeneOrthologDTO>().use(null, GeneOrthologDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<GeneOrthologDTO> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
	
	public static Collection<GeneOrthologDTO> fromJsonArrayToGeneOrtholoes(String json) {
        return new JSONDeserializer<List<GeneOrthologDTO>>().use(null, ArrayList.class).use("values", GeneOrthologDTO.class).deserialize(json);
    }
}
