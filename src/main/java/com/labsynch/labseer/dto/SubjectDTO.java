package com.labsynch.labseer.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.ThingPage;
import com.labsynch.labseer.utils.CustomBigDecimalFactory;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class SubjectDTO {
	
    public SubjectDTO(Subject subject) {
    	this.setId(subject.getId());
    	this.setVersion(subject.getVersion());
    	this.setRecordedBy(subject.getRecordedBy());
		this.setRecordedDate(subject.getRecordedDate());
		this.setLsTransaction(subject.getLsTransaction());
		this.setModifiedBy(subject.getModifiedBy());
		this.setModifiedDate(subject.getModifiedDate());
		this.setIgnored(subject.isIgnored());
		this.setCodeName(subject.getCodeName());
		this.setLsKind(subject.getLsKind());
		this.setTreatmentGroup(new TreatmentGroupMiniDTO(subject.getTreatmentGroup()));
    }

    
	private Long id;
	
	private Integer version;
    
	private String lsType;

	private String lsKind;
	
	private String codeName;

	private String recordedBy;

	private Date recordedDate;

	private String modifiedBy;

	private Date modifiedDate;

	private boolean ignored;

	private Long lsTransaction;

	private TreatmentGroupMiniDTO treatmentGroup;

    private Set<SubjectLabelDTO> lsLabels = new HashSet<SubjectLabelDTO>();

    private Set<SubjectStateDTO> lsStates = new HashSet<SubjectStateDTO>();
    
	private Set<ThingPage> thingPage = new HashSet<ThingPage>();




	public String toJson() {
        return new JSONSerializer()
        .include("lsLabels", "lsStates.lsValues")
        .exclude("*.class", "lsStates.subject", "lsStates.lsValues.lsState", "treatmentGroup.analysisGroup")
        .serialize(this);
    }

	public static SubjectDTO fromJsonToSubjectDTO(String json) {
        return new JSONDeserializer<SubjectDTO>().use(null, SubjectDTO.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
    }

	public static String toJsonArray(Collection<SubjectDTO> collection) {
        return new JSONSerializer()
        .include("lsLabels", "lsStates.lsValues")
        .exclude("*.class", "lsStates.subject", "lsStates.lsValues.lsState", "treatmentGroup.analysisGroup")
        .serialize(collection);
    }

	public static Collection<SubjectDTO> fromJsonArrayToSubjectDTO(String json) {
        return new JSONDeserializer<List<SubjectDTO>>().use(null, ArrayList.class).use("values", SubjectDTO.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
    }
}


