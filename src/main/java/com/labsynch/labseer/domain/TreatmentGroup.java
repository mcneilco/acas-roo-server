package com.labsynch.labseer.domain;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Query;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.dto.FlatThingCsvDTO;
import com.labsynch.labseer.utils.CustomBigDecimalFactory;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findTreatmentGroupsByAnalysisGroups", "findTreatmentGroupsByLsTransactionEquals" })
@RooJson
public class TreatmentGroup extends AbstractThing {
	

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "treatmentGroup", fetch =  FetchType.LAZY)
    private Set<TreatmentGroupLabel> lsLabels = new HashSet<TreatmentGroupLabel>();
  
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "treatmentGroup", fetch =  FetchType.LAZY)
	private Set<TreatmentGroupState> lsStates = new HashSet<TreatmentGroupState>();
	
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "treatmentGroups")  
    private Set<Subject> subjects = new HashSet<Subject>();
 
	@ManyToMany(cascade = CascadeType.ALL, fetch =  FetchType.LAZY)
	@JoinTable(name="ANALYSISGROUP_TREATMENTGROUP", 
	joinColumns={@JoinColumn(name="treatment_group_id")}, 
	inverseJoinColumns={@JoinColumn(name="analysis_group_id")})
    private Set<AnalysisGroup> analysisGroups = new HashSet<AnalysisGroup>();
	
    public TreatmentGroup() {
    }
    
	//constructor to instantiate a new treatmentGroup from nested json objects
	public TreatmentGroup (TreatmentGroup treatmentGroup){
		super.setRecordedBy(treatmentGroup.getRecordedBy());
		super.setRecordedDate(treatmentGroup.getRecordedDate());
		super.setLsTransaction(treatmentGroup.getLsTransaction());
		super.setModifiedBy(treatmentGroup.getModifiedBy());
		super.setModifiedDate(treatmentGroup.getModifiedDate());
        super.setIgnored(treatmentGroup.isIgnored());
        super.setCodeName(treatmentGroup.getCodeName());
        super.setLsType(treatmentGroup.getLsType());
        super.setLsKind(treatmentGroup.getLsKind());
        super.setLsTypeAndKind(treatmentGroup.getLsTypeAndKind());

	}

	public TreatmentGroup(FlatThingCsvDTO treatmentGroupDTO) {
		this.setRecordedBy(treatmentGroupDTO.getRecordedBy());
		this.setRecordedDate(treatmentGroupDTO.getRecordedDate());
		this.setLsTransaction(treatmentGroupDTO.getLsTransaction());
		this.setModifiedBy(treatmentGroupDTO.getModifiedBy());
		this.setModifiedDate(treatmentGroupDTO.getModifiedDate());
		this.setCodeName(treatmentGroupDTO.getCodeName());
		this.setLsKind(treatmentGroupDTO.getLsKind());
		this.setLsType(treatmentGroupDTO.getLsType());
	}

	public static TreatmentGroup update(TreatmentGroup treatmentGroup) {
		TreatmentGroup updatedTreatmentGroup = TreatmentGroup.findTreatmentGroup(treatmentGroup.getId());
		updatedTreatmentGroup.setRecordedBy(treatmentGroup.getRecordedBy());
		updatedTreatmentGroup.setRecordedDate(treatmentGroup.getRecordedDate());
		updatedTreatmentGroup.setLsTransaction(treatmentGroup.getLsTransaction());
		updatedTreatmentGroup.setModifiedBy(treatmentGroup.getModifiedBy());
		updatedTreatmentGroup.setModifiedDate(new Date());
		updatedTreatmentGroup.setIgnored(treatmentGroup.isIgnored());
		updatedTreatmentGroup.setCodeName(treatmentGroup.getCodeName());
		updatedTreatmentGroup.setLsType(treatmentGroup.getLsType());
		updatedTreatmentGroup.setLsKind(treatmentGroup.getLsKind());
		updatedTreatmentGroup.setLsTypeAndKind(treatmentGroup.getLsTypeAndKind());
		updatedTreatmentGroup.merge();
		return updatedTreatmentGroup;
	}
	
	public String toJson() {
		return new JSONSerializer()
		.include("lsLabels","lsStates.lsValues", "subjects")
		.exclude("*.class", "analysisGroups.experiments", "lsStates.treatmentGroup", "lsLabels.treatmentGroup", "subjects.treatmentGroups")
		.serialize(this);
	}

	public static TreatmentGroup fromJsonToTreatmentGroup(String json) {
		return new JSONDeserializer<TreatmentGroup>().
				use(null, TreatmentGroup.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).
				deserialize(json);
	}

	public static String toJsonArray(Collection<TreatmentGroup> collection) {
		return new JSONSerializer()
		.include("lsLabels","lsStates.lsValues", "subjects")
		.exclude("*.class", "analysisGroup.experiment", "lsStates.treatmentGroup", "lsLabels.treatmentGroup", "subjects.treatmentGroup")
		.serialize(collection);
	}

	public static Collection<TreatmentGroup> fromJsonArrayToTreatmentGroups(String json) {
		return new JSONDeserializer<List<TreatmentGroup>>().
				use(null, ArrayList.class).
				use("values", TreatmentGroup.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).
				deserialize(json);
	}

	public static Collection<TreatmentGroup> fromJsonArrayToTreatmentGroups(Reader json) {
		return new JSONDeserializer<List<TreatmentGroup>>().
				use(null, ArrayList.class).
				use("values", TreatmentGroup.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).
				deserialize(json);
	}

	public static int deleteByExperimentID(Long experimentId) {
		if (experimentId == null) return 0;
		EntityManager em = SubjectValue.entityManager();
		String deleteSQL = "DELETE FROM TreatmentGroup oo WHERE id in (select o.id from TreatmentGroup o where o.analysisGroup.experiment.id = :experimentId)";

		Query q = em.createQuery(deleteSQL);
		q.setParameter("experimentId", experimentId);
		int numberOfDeletedEntities = q.executeUpdate();
		return numberOfDeletedEntities;
	}



}
