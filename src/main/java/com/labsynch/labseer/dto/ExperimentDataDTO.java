package com.labsynch.labseer.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.LsTag;
import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


@RooJavaBean
@RooToString
@RooJson
public class ExperimentDataDTO {


	private String protocolName;
	
	private String protocolCodeName;

	private Long protocolId;
	
	private String assayFolderRule;
	
	private String protocolShortDescription;

	private String experimentName;
	
	private String experimentCodeName;

	private Long experimentId;
		
	private String experimentShortDescription;
	
	private String displayResults;

	private String renderData;

	private Set<LsTag> lsTags = new HashSet<LsTag>();
	
	private Set<AnalysisGroupValueDTO> analysisGroupValues = new HashSet<AnalysisGroupValueDTO>();
	


	public String toJson() {
        return new JSONSerializer().
        		exclude("*.class", "analysisGroupValues.experimentName", 
        				"analysisGroupValues.experimentCodeName", 
        				"analysisGroupValues.protocolId",
        				"analysisGroupValues.stateId",
        				"analysisGroupValues.experimentId",
        				"analysisGroupValues.geneId",
        				"analysisGroupValues.testedLot").
        		include("lsTags", "analysisGroupValues").
        		serialize(this);
    }

	public static ExperimentDataDTO fromJsonToExperimentDataDTO(String json) {
        return new JSONDeserializer<ExperimentDataDTO>().use(null, ExperimentDataDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ExperimentDataDTO> collection) {
        return new JSONSerializer().
        		exclude("*.class").
        		include("lsTags", "analysisGroupValues").
        		exclude("*.class", "analysisGroupValues.experimentName", 
        				"analysisGroupValues.experimentCodeName", 
        				"analysisGroupValues.protocolId",
        				"analysisGroupValues.stateId",
        				"analysisGroupValues.experimentId",
        				"analysisGroupValues.geneId",
        				"analysisGroupValues.testedLot").
        		serialize(collection);
    }

	public static Collection<ExperimentDataDTO> fromJsonArrayToExperimentDataDTO(String json) {
        return new JSONDeserializer<List<ExperimentDataDTO>>().use(null, ArrayList.class).use("values", ExperimentDataDTO.class).deserialize(json);
    }
}
	



