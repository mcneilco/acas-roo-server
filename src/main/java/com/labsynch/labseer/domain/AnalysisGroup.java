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
import javax.persistence.Transient;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findAnalysisGroupsByLsTransactionEquals", "findAnalysisGroupsByExperiments" })
public class AnalysisGroup extends AbstractThing {

	private static final Logger logger = LoggerFactory.getLogger(AnalysisGroup.class);

	//    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "analysisGroups")
	//    private Set<Experiment> experiments = new HashSet<Experiment>();
	
	@ManyToMany(cascade = CascadeType.ALL, fetch =  FetchType.LAZY)
	@JoinTable(name="EXPERIMENT_ANALYSISGROUP", 
	joinColumns={@JoinColumn(name="analysis_group_id")}, 
	inverseJoinColumns={@JoinColumn(name="experiment_id")})
	private Set<Experiment> experiments = new HashSet<Experiment>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "analysisGroup", fetch = FetchType.LAZY)
	private Set<AnalysisGroupLabel> lsLabels = new HashSet<AnalysisGroupLabel>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "analysisGroup", fetch = FetchType.LAZY)
	private Set<AnalysisGroupState> lsStates = new HashSet<AnalysisGroupState>();
	
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "analysisGroups")  
	private Set<TreatmentGroup> treatmentGroups = new HashSet<TreatmentGroup>();

//	@ManyToMany(cascade = CascadeType.ALL, fetch =  FetchType.LAZY)
//	@JoinTable(name="ANALYSIS_GROUP_TREATMENT_GROUP", 
//	joinColumns={@JoinColumn(name="analysis_group_id")}, 
//	inverseJoinColumns={@JoinColumn(name="treatment_group_id")})
//	private Set<TreatmentGroup> treatmentGroups = new HashSet<TreatmentGroup>();


	public AnalysisGroup() {
	}

	public AnalysisGroup(com.labsynch.labseer.domain.AnalysisGroup analysisGroup) {
		this.setRecordedBy(analysisGroup.getRecordedBy());
		this.setRecordedDate(analysisGroup.getRecordedDate());
		this.setLsTransaction(analysisGroup.getLsTransaction());
		this.setModifiedBy(analysisGroup.getModifiedBy());
		this.setModifiedDate(analysisGroup.getModifiedDate());
		this.setCodeName(analysisGroup.getCodeName());
		this.setLsKind(analysisGroup.getLsKind());
		this.setLsType(analysisGroup.getLsType());
//		for (Experiment experiment : analysisGroup.getExperiments()){
//			this.getExperiments().add(Experiment.findExperiment(experiment.getId()));
//		}
		Set<Experiment> experimentSet = new HashSet<Experiment>();
		for (Experiment experiment : analysisGroup.getExperiments()){
			experimentSet.add(Experiment.findExperiment(experiment.getId()));
		}
		this.setExperiments(experimentSet);	
		
	}

	//TODO: FIX this
	public static com.labsynch.labseer.domain.AnalysisGroup update(com.labsynch.labseer.domain.AnalysisGroup analysisGroup) {
		AnalysisGroup updatedAnalysisGroup = AnalysisGroup.findAnalysisGroup(analysisGroup.getId());
		updatedAnalysisGroup.setRecordedBy(analysisGroup.getRecordedBy());
		updatedAnalysisGroup.setRecordedDate(analysisGroup.getRecordedDate());
		updatedAnalysisGroup.setLsTransaction(analysisGroup.getLsTransaction());
		updatedAnalysisGroup.setModifiedBy(analysisGroup.getModifiedBy());
		updatedAnalysisGroup.setModifiedDate(new Date());
		updatedAnalysisGroup.setCodeName(analysisGroup.getCodeName());
		updatedAnalysisGroup.setLsKind(analysisGroup.getLsKind());
		updatedAnalysisGroup.setLsType(analysisGroup.getLsType());
		for (Experiment experiment : analysisGroup.getExperiments()){
			updatedAnalysisGroup.getExperiments().add(experiment);
		}
//		updatedAnalysisGroup.merge();
		return updatedAnalysisGroup;
	}

	public String toFullJson() {
		return new JSONSerializer().exclude("*.class", "lsStates.analysisGroup", "lsLabels.analysisGroup", "treatmentGroups.analysisGroup", "experiment.protocol").include("lsLabels", "lsStates.lsValues", "treatmentGroups.lsStates.lsValues", "treatmentGroups.lsLabels", "treatmentGroups.subjects.lsStates.lsValues", "treatmentGroups.subjects.lsLabels").transform(new ExcludeNulls(), void.class).serialize(this);
	}

	public String toPrettyFullJson() {
		return new JSONSerializer()
		.exclude("*.class", "lsStates.analysisGroup", "lsLabels.analysisGroup", 
				"treatmentGroups.analysisGroup", "experiments.protocol", "experiment.protocol")
				.include("experiments", "lsLabels", "lsStates.lsValues", "treatmentGroups.lsStates.lsValues", 
						"treatmentGroups.lsLabels", "treatmentGroups.subjects.lsStates.lsValues", 
						"treatmentGroups.subjects.lsLabels")
						.prettyPrint(true)
						.transform(new ExcludeNulls(), void.class).serialize(this);
	}

	public String toJson() {
		return new JSONSerializer().exclude("*.class", "lsStates.analysisGroup", "lsLabels.analysisGroup", "treatmentGroups.analysisGroup", "experiment.protocol").include("lsLabels", "lsStates.lsValues", "treatmentGroups.lsValues").transform(new ExcludeNulls(), void.class).serialize(this);
	}

	public String toJsonStub() {
		return new JSONSerializer().exclude("*.class", "lsStates.analysisGroup", "lsLabels.analysisGroup", "treatmentGroups.analysisGroup", "experiment.protocol").include("lsLabels", "lsStates.lsValues").transform(new ExcludeNulls(), void.class).serialize(this);
	}

	public String toPrettyJson() {
		return new JSONSerializer().exclude("*.class", "lsStates.analysisGroup", "lsLabels.analysisGroup", "treatmentGroups.analysisGroup", "experiment.protocol").include("lsLabels", "lsStates.lsValues", "treatmentGroups.lsValues").prettyPrint(true).transform(new ExcludeNulls(), void.class).serialize(this);
	}

	public String toPrettyJsonStub() {
		return new JSONSerializer().exclude("*.class", "lsStates.analysisGroup", "lsLabels.analysisGroup", "treatmentGroups.analysisGroup", "experiment.protocol").include("lsLabels", "lsStates.lsValues").prettyPrint(true).transform(new ExcludeNulls(), void.class).serialize(this);
	}

	public static String toJsonArray(Collection<com.labsynch.labseer.domain.AnalysisGroup> collection) {
		return new JSONSerializer().exclude("*.class", "experiment.protocol").transform(new ExcludeNulls(), void.class).serialize(collection);
	}

	public static com.labsynch.labseer.domain.AnalysisGroup fromJsonToAnalysisGroup(String json) {
		return new JSONDeserializer<AnalysisGroup>().use(null, AnalysisGroup.class).use("analysisGroup.lsStates", AnalysisGroupState.class).use("analysisGroup.lsStates.lsValues", AnalysisGroupValue.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json, AnalysisGroup.class);
	}

	public static com.labsynch.labseer.domain.AnalysisGroup fromJsonToAnalysisGroup2(Reader json) {
		return new JSONDeserializer<AnalysisGroup>().use(null, AnalysisGroup.class).use("analysisGroup.lsStates", AnalysisGroupState.class).use("analysisGroup.lsStates.lsValues", AnalysisGroupValue.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
	}

	public static Collection<com.labsynch.labseer.domain.AnalysisGroup> fromJsonArrayToAnalysisGroups(Reader json) {
		return new JSONDeserializer<List<AnalysisGroup>>().use(null, ArrayList.class).use("values", AnalysisGroup.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
	}

	public static Collection<com.labsynch.labseer.domain.AnalysisGroup> fromJsonArrayToAnalysisGroups(String json) {
		return new JSONDeserializer<List<AnalysisGroup>>().use(null, ArrayList.class).use("values", AnalysisGroup.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
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
		Experiment experiment = Experiment.findExperiment(id);
		Set<Experiment> experiments = new HashSet<Experiment>();
		experiments.add(experiment);
		List<AnalysisGroup> analysisgroups  = AnalysisGroup.findAnalysisGroupsByExperiments(experiments).getResultList();
		for (AnalysisGroup analysisgroup : analysisgroups) {
			logger.debug("removing analysis group: " + analysisgroup.getCodeName());
			analysisgroup.remove();
		}
	}

	public static TypedQuery<com.labsynch.labseer.domain.AnalysisGroup> findAnalysisGroupsByExperimentIdAndIgnored(Long id, boolean includeIgnored) {
		if (id == null) throw new IllegalArgumentException("The id argument is required");
		EntityManager em = Experiment.entityManager();
		String sqlQuery;
		if (includeIgnored) {
			sqlQuery = "SELECT o FROM AnalysisGroup AS o WHERE o.experiment.id = :id ";
		} else {
			sqlQuery = "SELECT o FROM AnalysisGroup AS o WHERE o.experiment.id = :id " + "AND o.ignored != true";
		}
		TypedQuery<AnalysisGroup> q = em.createQuery(sqlQuery, AnalysisGroup.class);
		q.setParameter("id", id);
		return q;
	}
}
