package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.labsynch.labseer.utils.ExcludeNulls;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


public class SubjectCodeDTO {
	
	private String subjectCode;
	private Collection<TreatmentGroupCodeDTO> treatmentGroupCodes;
	
	public SubjectCodeDTO(){
	}
	
	public String toJson() {
        return new JSONSerializer().include("treatmentGroupCodes.analysisGroupCodes.experimentCodes").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
	}

	public static String toJsonArray(Collection<SubjectCodeDTO> collection) {
		return new JSONSerializer().include("treatmentGroupCodes.analysisGroupCodes.experimentCodes").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
	}
	


	public static SubjectCodeDTO fromJsonToSubjectCodeDTO(String json) {
        return new JSONDeserializer<SubjectCodeDTO>()
        .use(null, SubjectCodeDTO.class).deserialize(json);
    }

	public static Collection<SubjectCodeDTO> fromJsonArrayToSubjectCoes(String json) {
        return new JSONDeserializer<List<SubjectCodeDTO>>()
        .use("values", SubjectCodeDTO.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getSubjectCode() {
        return this.subjectCode;
    }

	public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

	public Collection<TreatmentGroupCodeDTO> getTreatmentGroupCodes() {
        return this.treatmentGroupCodes;
    }

	public void setTreatmentGroupCodes(Collection<TreatmentGroupCodeDTO> treatmentGroupCodes) {
        this.treatmentGroupCodes = treatmentGroupCodes;
    }
}


