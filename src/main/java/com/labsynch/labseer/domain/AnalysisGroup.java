package com.labsynch.labseer.domain;

import com.labsynch.labseer.dto.AnalysisGroupCsvDTO;
import com.labsynch.labseer.dto.FlatThingCsvDTO;
import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

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

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findAnalysisGroupsByLsTransactionEquals", "findAnalysisGroupsByExperiments", "findAnalysisGroupsByExperimentsAndIgnoredNot" })
public class AnalysisGroup extends AbstractThing {

    private static final Logger logger = LoggerFactory.getLogger(AnalysisGroup.class);
    
    //Subject is grandparent
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "EXPERIMENT_ANALYSISGROUP", joinColumns = { @javax.persistence.JoinColumn(name = "analysis_group_id") }, inverseJoinColumns = { @javax.persistence.JoinColumn(name = "experiment_id") })
    private Set<Experiment> experiments = new HashSet<Experiment>();
    
    //Experiment is grandparent
//    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "analysisGroups")  
//    private Set<Experiment> experiments = new HashSet<Experiment>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "analysisGroup", fetch = FetchType.LAZY)
    private Set<AnalysisGroupLabel> lsLabels = new HashSet<AnalysisGroupLabel>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "analysisGroup", fetch = FetchType.LAZY)
    private Set<AnalysisGroupState> lsStates = new HashSet<AnalysisGroupState>();

    //Subject is grandparent
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "analysisGroups")
    private Set<TreatmentGroup> treatmentGroups = new HashSet<TreatmentGroup>();

    //Experiment is grandparent
//	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch =  FetchType.LAZY)
//	@JoinTable(name="ANALYSISGROUP_TREATMENTGROUP", 
//	joinColumns={@JoinColumn(name="analysis_group_id")}, 
//	inverseJoinColumns={@JoinColumn(name="treatment_group_id")})
//    private Set<TreatmentGroup> treatmentGroups = new HashSet<TreatmentGroup>();
    
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
        Set<Experiment> experimentSet = new HashSet<Experiment>();
        for (Experiment experiment : analysisGroup.getExperiments()) {
            experimentSet.add(Experiment.findExperiment(experiment.getId()));
        }
        this.setExperiments(experimentSet);
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

    public static com.labsynch.labseer.domain.AnalysisGroup update(com.labsynch.labseer.domain.AnalysisGroup analysisGroup) {
		if (AnalysisGroup.findAnalysisGroup(analysisGroup.getId()) != null){        
			AnalysisGroup updatedAnalysisGroup = AnalysisGroup.findAnalysisGroup(analysisGroup.getId());
        	if(analysisGroup.getRecordedBy() != null) updatedAnalysisGroup.setRecordedBy(analysisGroup.getRecordedBy());
        	if(analysisGroup.getRecordedDate() != null) updatedAnalysisGroup.setRecordedDate(analysisGroup.getRecordedDate());
        	if(analysisGroup.getLsTransaction() != null) updatedAnalysisGroup.setLsTransaction(analysisGroup.getLsTransaction());
        	if(analysisGroup.getModifiedBy() != null) updatedAnalysisGroup.setModifiedBy(analysisGroup.getModifiedBy());
        	updatedAnalysisGroup.setModifiedDate(new Date());
        	if(analysisGroup.getCodeName() != null) updatedAnalysisGroup.setCodeName(analysisGroup.getCodeName());
        	if(analysisGroup.getLsKind() != null) updatedAnalysisGroup.setLsKind(analysisGroup.getLsKind());
        	if(analysisGroup.getLsType() != null) updatedAnalysisGroup.setLsType(analysisGroup.getLsType());
        	for (Experiment experiment : analysisGroup.getExperiments()) {
          	  updatedAnalysisGroup.getExperiments().add(experiment);
        	}
        	return updatedAnalysisGroup;
		} else {
    		logger.error("DID not find the requested analysis group " + analysisGroup.getId() );
    		return null;
		}
    }

    public String toFullJson() {
        return new JSONSerializer().exclude("*.class", "lsStates.analysisGroup", "lsLabels.analysisGroup", "treatmentGroups.analysisGroup", "experiment.protocol").include("lsLabels", "lsStates.lsValues", "treatmentGroups.lsStates.lsValues", "treatmentGroups.lsLabels", "treatmentGroups.subjects.lsStates.lsValues", "treatmentGroups.subjects.lsLabels").transform(new ExcludeNulls(), void.class).serialize(this);
    }

    public String toPrettyFullJson() {
        return new JSONSerializer().exclude("*.class", "lsStates.analysisGroup", "lsLabels.analysisGroup", "treatmentGroups.analysisGroup", "experiments.protocol", "experiment.protocol").include("experiments", "lsLabels", "lsStates.lsValues", "treatmentGroups.lsStates.lsValues", "treatmentGroups.lsLabels", "treatmentGroups.subjects.lsStates.lsValues", "treatmentGroups.subjects.lsLabels").prettyPrint(true).transform(new ExcludeNulls(), void.class).serialize(this);
    }

    public String toJson() {
        return new JSONSerializer().exclude("*.class", "lsStates.analysisGroup", "lsLabels.analysisGroup", "treatmentGroups.analysisGroup", "experiments.protocol").include("lsLabels", "lsStates.lsValues", "treatmentGroups.lsStates.lsValues").transform(new ExcludeNulls(), void.class).serialize(this);
    }

    public String toJsonStub() {
        return new JSONSerializer().exclude("*.class", "lsStates.analysisGroup", "lsLabels.analysisGroup", "treatmentGroups.analysisGroup", "experiment.protocol").include("lsLabels", "lsStates.lsValues").transform(new ExcludeNulls(), void.class).serialize(this);
    }

    public String toPrettyJson() {
        return new JSONSerializer().exclude("*.class", "lsStates.analysisGroup", "lsLabels.analysisGroup", "treatmentGroups.analysisGroup", "experiment.protocol").include("lsLabels", "lsStates.lsValues", "treatmentGroups.lsStates.lsValues").prettyPrint(true).transform(new ExcludeNulls(), void.class).serialize(this);
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

    //@Transactional
    //TODO: later fix with direct hsql if possible
    public static int deleteByExperimentID(Long experimentId) {
//        Experiment experiment = Experiment.findExperiment(experimentId);
//        Set<Experiment> experiments = new HashSet<Experiment>();
//        experiments.add(experiment);
//        List<AnalysisGroup> analysisgroups = AnalysisGroup.findAnalysisGroupsByExperiments(experiments).getResultList();
//        int numberOfAnalysisGroups = analysisgroups.size();
//        for (AnalysisGroup analysisgroup : analysisgroups) {
//            logger.debug("removing analysis group: " + analysisgroup.getCodeName());
//            analysisgroup.remove();
//        }
//        
//        return numberOfAnalysisGroups;
        
        if (experimentId == null) return 0;
        EntityManager em = SubjectValue.entityManager();
        
        Query q1 = em.createQuery("SELECT ag.id FROM AnalysisGroup as ag JOIN ag.experiments exp WHERE exp.id = :experimentId");
        q1.setParameter("experimentId", experimentId);
        @SuppressWarnings("unchecked")
		List<Long> agIds = q1.getResultList();
        logger.info("Found number of agroups: " + agIds.size());
        
        Query q2 = em.createNativeQuery("DELETE FROM experiment_analysisgroup o WHERE o.experiment_id = :experimentId");
        q2.setParameter("experimentId", experimentId);
        int numberOfDeletes = q2.executeUpdate();
        logger.info("Deleted number of numberOfDeletes: " + numberOfDeletes);

        String deleteSQL = "DELETE FROM AnalysisGroup o where o.id in (:agIds)";
        Query q = em.createQuery(deleteSQL);
        q.setParameter("agIds", agIds);
        int numberOfDeletedEntities = q.executeUpdate();
        logger.info("Deleted number of numberOfDeletedEntities: " + numberOfDeletedEntities);

        
        return numberOfDeletedEntities;
//        return 0;
    }
    
//    public static int removeOrphans() {
//    	Set<Experiment> emptySet = new HashSet<Experiment>();
//    	emptySet.add(null);
//    	List<AnalysisGroup> orphansList = AnalysisGroup.findAnalysisGroupsByExperimentsAndIgnoredNot(emptySet, !false).getResultList();
//    	int numberOfOrphans = orphansList.size();
//    	for (AnalysisGroup orphan: orphansList) {
//    		orphan.remove();
//    	}
//    	return numberOfOrphans;
//    }
    
    //@Transactional
    public static void removeByExperimentID(Long id) {
        Experiment experiment = Experiment.findExperiment(id);
        Set<Experiment> experiments = new HashSet<Experiment>();
        experiments.add(experiment);
        List<AnalysisGroup> analysisgroups = AnalysisGroup.findAnalysisGroupsByExperiments(experiments).getResultList();
        for (AnalysisGroup analysisgroup : analysisgroups) {
            logger.debug("removing analysis group: " + analysisgroup.getCodeName());
            //analysisgroup.getTreatmentGroups().clear();
            analysisgroup.remove();
        }
    }

    public static TypedQuery<com.labsynch.labseer.domain.AnalysisGroup> findAnalysisGroupsByExperimentIdAndIgnored(Long id, boolean includeIgnored) {
        if (id == null) throw new IllegalArgumentException("The id argument is required");
        Experiment experiment = Experiment.findExperiment(id);
        Set<Experiment> experiments = new HashSet<Experiment>();
        experiments.add(experiment);
        //EntityManager em = Experiment.entityManager();
        //String sqlQuery;
//        if (includeIgnored) {
//            sqlQuery = "SELECT o FROM AnalysisGroup AS o WHERE AnalysisGroup IN (SELECT o FROM AnalysisGroup o JOIN o.experiments e WHERE e.id = :experimentId)";
//        } else {
//            sqlQuery = "SELECT o FROM AnalysisGroup AS o WHERE AnalysisGroup IN (SELECT o FROM AnalysisGroup o JOIN o.experiments e WHERE e.id = :experimentId) AND o.ignored != true";
//        }
//        TypedQuery<AnalysisGroup> q1 = em.createQuery(sqlQuery, AnalysisGroup.class);
        TypedQuery<AnalysisGroup> q2 = AnalysisGroup.findAnalysisGroupsByExperimentsAndIgnoredNot(experiments, !includeIgnored);
//        q1.setParameter("id", id);
        //q2.setParameter("id", id);
        return q2;
    }

    public static TypedQuery<com.labsynch.labseer.domain.AnalysisGroup> findAnalysisGroupsByCodeNameEquals(String codeName) {
        if (codeName == null || codeName.length() == 0) throw new IllegalArgumentException("The codeName argument is required");
        EntityManager em = Experiment.entityManager();
        TypedQuery<AnalysisGroup> q = em.createQuery("SELECT o FROM AnalysisGroup AS o WHERE o.codeName = :codeName", AnalysisGroup.class);
        q.setParameter("codeName", codeName);
        return q;
    }

	@Transactional
    public static Collection<Long> removeOrphans(Collection<Long> analysisGroupIds) {
		Collection<Long> treatmentGroupIds = new HashSet<Long>();
		for (Long id: analysisGroupIds) {
			AnalysisGroup analysisGroup = AnalysisGroup.findAnalysisGroup(id);
			if (analysisGroup.getExperiments().isEmpty()) {
				treatmentGroupIds.addAll(AnalysisGroup.removeCascadeAware(id));
			}
		}
		
		return treatmentGroupIds;
	}

	@Transactional
	private static Collection<Long> removeCascadeAware(Long id) {
		AnalysisGroup analysisGroup = findAnalysisGroup(id);
        Collection<TreatmentGroup> treatmentGroups = analysisGroup.getTreatmentGroups();
        Set<Long> treatmentGroupIds = new HashSet<Long>();
        for (TreatmentGroup treatmentGroup : treatmentGroups) {
        	treatmentGroupIds.add(treatmentGroup.getId());
        }
        treatmentGroups.clear();
        EntityManager em = AnalysisGroup.entityManager();
        Query q1 = em.createNativeQuery("DELETE FROM analysisGroup_treatmentgroup o WHERE o.analysis_Group_id = :id", AnalysisGroup.class);
        q1.setParameter("id", id);
        q1.executeUpdate();
        analysisGroup.remove();
        return treatmentGroupIds;
		
	}
}
