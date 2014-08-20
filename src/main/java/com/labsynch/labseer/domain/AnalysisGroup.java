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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.dto.AnalysisGroupCsvDTO;
import com.labsynch.labseer.dto.FlatThingCsvDTO;
import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findAnalysisGroupsByExperiment", "findAnalysisGroupsByLsTransactionEquals"  })
@RooJson
@Transactional
public class AnalysisGroup extends AbstractThing {
	
    private static final Logger logger = LoggerFactory.getLogger(AnalysisGroup.class);

    public AnalysisGroup() {
    }
	
	
	//constructor to instantiate a new analysisGroup from nested json objects
	public AnalysisGroup (AnalysisGroup analysisGroup){
		super.setRecordedBy(analysisGroup.getRecordedBy());
		super.setRecordedDate(analysisGroup.getRecordedDate());
		super.setLsTransaction(analysisGroup.getLsTransaction());
		super.setModifiedBy(analysisGroup.getModifiedBy());
		super.setModifiedDate(analysisGroup.getModifiedDate());
        super.setCodeName(analysisGroup.getCodeName());
        super.setLsKind(analysisGroup.getLsKind());
        super.setLsType(analysisGroup.getLsType());
//        this.setExperiment(analysisGroup.getExperiment());

	}

	public AnalysisGroup(AnalysisGroupCsvDTO analysisGroupDTO) {
		this.setRecordedBy(analysisGroupDTO.getRecordedBy());
		this.setRecordedDate(analysisGroupDTO.getRecordedDate());
		this.setLsTransaction(analysisGroupDTO.getLsTransaction());
		this.setModifiedBy(analysisGroupDTO.getModifiedBy());
		this.setModifiedDate(analysisGroupDTO.getModifiedDate());
		this.setCodeName(analysisGroupDTO.getCodeName());
		this.setLsKind(analysisGroupDTO.getLsKind());
		this.setLsType(analysisGroupDTO.getLsType());	
	}

	public AnalysisGroup(FlatThingCsvDTO analysisGroupDTO) {
		this.setRecordedBy(analysisGroupDTO.getRecordedBy());
		this.setRecordedDate(analysisGroupDTO.getRecordedDate());
		this.setLsTransaction(analysisGroupDTO.getLsTransaction());
		this.setModifiedBy(analysisGroupDTO.getModifiedBy());
		this.setModifiedDate(analysisGroupDTO.getModifiedDate());
		this.setCodeName(analysisGroupDTO.getCodeName());
		this.setLsKind(analysisGroupDTO.getLsKind());
		this.setLsType(analysisGroupDTO.getLsType());
	}
	

	public static AnalysisGroup update(AnalysisGroup analysisGroup){
		AnalysisGroup updatedAnalysisGroup = AnalysisGroup.findAnalysisGroup(analysisGroup.getId());
		updatedAnalysisGroup.setRecordedBy(analysisGroup.getRecordedBy());
		updatedAnalysisGroup.setRecordedDate(analysisGroup.getRecordedDate());
		updatedAnalysisGroup.setLsTransaction(analysisGroup.getLsTransaction());
		updatedAnalysisGroup.setModifiedBy(analysisGroup.getModifiedBy());
		updatedAnalysisGroup.setModifiedDate(new Date());
		updatedAnalysisGroup.setCodeName(analysisGroup.getCodeName());
		updatedAnalysisGroup.setLsKind(analysisGroup.getLsKind());
		updatedAnalysisGroup.setLsType(analysisGroup.getLsType());
		updatedAnalysisGroup.merge();
		return updatedAnalysisGroup;
	}
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "experiment_id")
	private Experiment experiment;
	
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "analysisGroup", fetch =  FetchType.LAZY)
    private Set<AnalysisGroupLabel> lsLabels = new HashSet<AnalysisGroupLabel>();
    
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "analysisGroup", fetch =  FetchType.LAZY)
	private Set<AnalysisGroupState> lsStates = new HashSet<AnalysisGroupState>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "analysisGroup", fetch =  FetchType.LAZY)
	private Set<TreatmentGroup> treatmentGroups = new HashSet<TreatmentGroup>();

	public String toFullJson() {
		return new JSONSerializer()
		.exclude("*.class", "lsStates.analysisGroup", "lsLabels.analysisGroup", 
				"treatmentGroups.analysisGroup", "experiment.protocol")
		.include("lsLabels", "lsStates.lsValues", 
				"treatmentGroups.lsStates.lsValues", "treatmentGroups.lsLabels",
				"treatmentGroups.subjects.lsStates.lsValues", "treatmentGroups.subjects.lsLabels")
		.transform(new ExcludeNulls(), void.class)
		.serialize(this);
	}
	
	public String toPrettyFullJson() {
		return new JSONSerializer()
		.exclude("*.class", "lsStates.analysisGroup", "lsLabels.analysisGroup", 
				"treatmentGroups.analysisGroup", "experiment.protocol")
		.include("lsLabels", "lsStates.lsValues", 
				"treatmentGroups.lsStates.lsValues", "treatmentGroups.lsLabels",
				"treatmentGroups.subjects.lsStates.lsValues", "treatmentGroups.subjects.lsLabels")
		.prettyPrint(true)
		.transform(new ExcludeNulls(), void.class)
		.serialize(this);
	}
	
	public String toJson() {
		return new JSONSerializer()
		.exclude("*.class", "lsStates.analysisGroup", "lsLabels.analysisGroup", "treatmentGroups.analysisGroup", "experiment.protocol")
		.include("lsLabels", "lsStates.lsValues", "treatmentGroups.lsValues" )
		.transform(new ExcludeNulls(), void.class)
		.serialize(this);
	}
	
	public String toJsonStub() {
		return new JSONSerializer()
		.exclude("*.class", "lsStates.analysisGroup", "lsLabels.analysisGroup", "treatmentGroups.analysisGroup", "experiment.protocol")
		.include("lsLabels", "lsStates.lsValues" )
		.transform(new ExcludeNulls(), void.class)
		.serialize(this);
	}


	public String toPrettyJson() {
		return new JSONSerializer()
		.exclude("*.class", "lsStates.analysisGroup", "lsLabels.analysisGroup", "treatmentGroups.analysisGroup", "experiment.protocol")
		.include("lsLabels", "lsStates.lsValues", "treatmentGroups.lsValues" )
		.prettyPrint(true)
		.transform(new ExcludeNulls(), void.class)
		.serialize(this);
	}

	public String toPrettyJsonStub() {
		return new JSONSerializer()
		.exclude("*.class", "lsStates.analysisGroup", "lsLabels.analysisGroup", "treatmentGroups.analysisGroup", "experiment.protocol")
		.include("lsLabels", "lsStates.lsValues" )
		.prettyPrint(true)
		.transform(new ExcludeNulls(), void.class)
		.serialize(this);
	}
	
	public static String toJsonArray(Collection<AnalysisGroup> collection) {
		return new JSONSerializer()
		.exclude("*.class", "experiment.protocol")		            	
		.transform(new ExcludeNulls(), void.class)
		.serialize(collection);
	}

	
	public static AnalysisGroup fromJsonToAnalysisGroup(String json) {
        return new JSONDeserializer<AnalysisGroup>().
        		use(null, AnalysisGroup.class).
        		use("analysisGroup.lsStates", AnalysisGroupState.class).
        		use("analysisGroup.lsStates.lsValues", AnalysisGroupValue.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).
        		deserialize(json, AnalysisGroup.class);
    }
	
	public static AnalysisGroup fromJsonToAnalysisGroup2(Reader json) {
		return new JSONDeserializer<AnalysisGroup>().
        		use(null, AnalysisGroup.class).
        		use("analysisGroup.lsStates", AnalysisGroupState.class).
        		use("analysisGroup.lsStates.lsValues", AnalysisGroupValue.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).
        		deserialize(json);
    }

	public static Collection<AnalysisGroup> fromJsonArrayToAnalysisGroups(Reader json) {
        return new JSONDeserializer<List<AnalysisGroup>>().
        		use(null, ArrayList.class).
        		use("values", AnalysisGroup.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).
        		deserialize(json);
    }
	
	public static Collection<AnalysisGroup> fromJsonArrayToAnalysisGroups(String json) {
        return new JSONDeserializer<List<AnalysisGroup>>().
        		use(null, ArrayList.class).
        		use("values", AnalysisGroup.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).
        		deserialize(json);
    }

	@Transactional
	public static int deleteByExperimentID(Long experimentId) {
		if (experimentId == null) return 0;
		EntityManager em = SubjectValue.entityManager();
		String deleteSQL = "DELETE FROM AnalysisGroup oo WHERE id in (select o.id from AnalysisGroup o where o.experiment.id = :experimentId)";
		Query q = em.createQuery(deleteSQL);
		q.setParameter("experimentId", experimentId);
		int numberOfDeletedEntities = q.executeUpdate();
		return numberOfDeletedEntities;
	}
	
	@Transactional
	public static void removeByExperimentID(Long id) {
		List<AnalysisGroup> analysisgroups = AnalysisGroup.findAnalysisGroupsByExperiment(Experiment.findExperiment(id)).getResultList();
		for (AnalysisGroup analysisgroup : analysisgroups){
			logger.debug("removing analysis group: " + analysisgroup.getCodeName());
			analysisgroup.remove();
			
		}
			
	}


	public static TypedQuery<AnalysisGroup> findAnalysisGroupsByExperimentIdAndIgnored(Long id, boolean includeIgnored) {
        if (id == null ) throw new IllegalArgumentException("The id argument is required");
        EntityManager em = Experiment.entityManager();
        String sqlQuery;
        if (includeIgnored){
            sqlQuery = "SELECT o FROM AnalysisGroup AS o WHERE o.experiment.id = :id ";
        } else {
            sqlQuery = "SELECT o FROM AnalysisGroup AS o WHERE o.experiment.id = :id "
					 + "AND o.ignored != true";
        }
 
        TypedQuery<AnalysisGroup> q = em.createQuery(sqlQuery, AnalysisGroup.class);
        q.setParameter("id", id);
		return q;
	}
	
	public static TypedQuery<AnalysisGroup> findAnalysisGroupsByCodeNameEquals(String codeName) {
		if (codeName == null || codeName.length() == 0) throw new IllegalArgumentException("The codeName argument is required");
		EntityManager em = Experiment.entityManager();
		TypedQuery<AnalysisGroup> q = em.createQuery("SELECT o FROM AnalysisGroup AS o WHERE o.codeName = :codeName", AnalysisGroup.class);
		q.setParameter("codeName", codeName);
		return q;
	}
}
