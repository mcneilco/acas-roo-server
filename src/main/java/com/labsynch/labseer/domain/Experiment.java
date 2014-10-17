package com.labsynch.labseer.domain;

import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@RooJavaBean
@RooToString(excludeFields = { "lsTags", "lsStates", "analysisGroups", "lsLabels" })
@RooJson
@RooJpaActiveRecord(finders = { "findExperimentsByCodeNameEquals", "findExperimentsByLsTransaction", "findExperimentsByProtocol", "findExperimentsByLsTypeEqualsAndLsKindEquals", "findExperimentsByLsKindLike", "findExperimentsByCodeNameLike" })
public class Experiment extends AbstractThing {

    private static final Logger logger = LoggerFactory.getLogger(Experiment.class);

    @Size(max = 1024)
    private String shortDescription;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "protocol_id")
    private Protocol protocol;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "experiment", fetch = FetchType.LAZY)
    private Set<ExperimentState> lsStates = new HashSet<ExperimentState>();

    @OneToMany(cascade = { javax.persistence.CascadeType.PERSIST, javax.persistence.CascadeType.MERGE }, mappedBy = "secondExperiment", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<ItxExperimentExperiment> firstExperiments = new HashSet<ItxExperimentExperiment>();

    @OneToMany(cascade = { javax.persistence.CascadeType.PERSIST, javax.persistence.CascadeType.MERGE }, mappedBy = "firstExperiment", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<ItxExperimentExperiment> secondExperiments = new HashSet<ItxExperimentExperiment>();

    //Subject is grandparent
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "experiments")
    private Set<AnalysisGroup> analysisGroups = new HashSet<AnalysisGroup>();
    
    //Experiment is grandparent
//    @ManyToMany(cascade = { javax.persistence.CascadeType.PERSIST, javax.persistence.CascadeType.MERGE }, fetch = FetchType.LAZY)
//    @JoinTable(name = "EXPERIMENT_ANALYSISGROUP", joinColumns = { @javax.persistence.JoinColumn(name = "experiment_id") }, inverseJoinColumns = { @javax.persistence.JoinColumn(name = "analysis_group_id") })
//    private Set<AnalysisGroup> analysisGroups = new HashSet<AnalysisGroup>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "experiment", fetch = FetchType.LAZY)
    private Set<ExperimentLabel> lsLabels = new HashSet<ExperimentLabel>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "EXPERIMENT_TAG", joinColumns = { @javax.persistence.JoinColumn(name = "experiment_id") }, inverseJoinColumns = { @javax.persistence.JoinColumn(name = "tag_id") })
    private Set<LsTag> lsTags = new HashSet<LsTag>();

    public Experiment() {
    }

    public Experiment(com.labsynch.labseer.domain.Experiment experiment) {
        this.setRecordedBy(experiment.getRecordedBy());
        this.setRecordedDate(experiment.getRecordedDate());
        this.setLsTransaction(experiment.getLsTransaction());
        this.setModifiedBy(experiment.getModifiedBy());
        this.setModifiedDate(experiment.getModifiedDate());
        this.setCodeName(experiment.getCodeName());
        this.setLsType(experiment.getLsType());
        this.setLsKind(experiment.getLsKind());
        this.setLsTypeAndKind(experiment.getLsTypeAndKind());
        this.shortDescription = experiment.getShortDescription();
        if (experiment.getLsTags() != null) {
            for (LsTag lsTag : experiment.getLsTags()) {
                List<LsTag> queryTags = LsTag.findLsTagsByTagTextEquals(lsTag.getTagText()).getResultList();
                if (queryTags.size() < 1) {
                    LsTag newLsTag = new LsTag(lsTag);
                    newLsTag.persist();
                    this.getLsTags().add(newLsTag);
                } else {
                    this.getLsTags().add(queryTags.get(0));
                }
            }
        } else {
            logger.info("No experiment tags to save");
        }
    }

    public static com.labsynch.labseer.domain.Experiment update(com.labsynch.labseer.domain.Experiment experiment) {
        Experiment updatedExperiment = Experiment.findExperiment(experiment.getId());
        updatedExperiment.setRecordedBy(experiment.getRecordedBy());
        updatedExperiment.setRecordedDate(experiment.getRecordedDate());
        updatedExperiment.setLsTransaction(experiment.getLsTransaction());
        updatedExperiment.setModifiedBy(experiment.getModifiedBy());
        updatedExperiment.setModifiedDate(new Date());
        updatedExperiment.setCodeName(experiment.getCodeName());
        updatedExperiment.setLsType(experiment.getLsType());
        updatedExperiment.setLsKind(experiment.getLsKind());
        updatedExperiment.setLsTypeAndKind(experiment.getLsTypeAndKind());
        updatedExperiment.shortDescription = experiment.getShortDescription();
        updatedExperiment.setIgnored(experiment.isIgnored());
        if (updatedExperiment.getLsTags() != null) {
            updatedExperiment.getLsTags().clear();
        }
        if (experiment.getLsTags() != null) {
            for (LsTag lsTag : experiment.getLsTags()) {
                List<LsTag> queryTags = LsTag.findLsTagsByTagTextEquals(lsTag.getTagText()).getResultList();
                if (queryTags.size() < 1) {
                    LsTag newLsTag = new LsTag(lsTag);
                    newLsTag.persist();
                    updatedExperiment.getLsTags().add(newLsTag);
                } else {
                    updatedExperiment.getLsTags().add(queryTags.get(0));
                }
            }
        } else {
            logger.info("No experiment labels to save");
        }
        updatedExperiment.merge();
        return updatedExperiment;
    }

    public Collection<com.labsynch.labseer.domain.AnalysisGroup> getAnalysisGroups(Long experimentId, boolean includeIgnored) {
        List<AnalysisGroup> results = AnalysisGroup.findAnalysisGroupsByExperimentIdAndIgnored(experimentId, includeIgnored).getResultList();
        Collection<AnalysisGroup> analysisGroups = new HashSet<AnalysisGroup>();
        analysisGroups.addAll(results);
        return analysisGroups;
    }

    @Transactional
    public static Collection<com.labsynch.labseer.domain.Experiment> findExperimentByName(String experimentName) {
        List<ExperimentLabel> foundExperimentLabels = ExperimentLabel.findExperimentLabelsByName(experimentName).getResultList();
        Collection<Experiment> experimentList = new HashSet<Experiment>();
        for (ExperimentLabel experimentLabel : foundExperimentLabels) {
            Experiment experiment = Experiment.findExperiment(experimentLabel.getExperiment().getId());
            experimentList.add(experiment);
        }
        List<Experiment> experiments = Experiment.findExperimentsByCodeNameEquals(experimentName).getResultList();
        for (Experiment experiment : experiments) {
            experimentList.add(experiment);
        }
        return experimentList;
    }

    @Transactional
    public static List<com.labsynch.labseer.domain.Experiment> findExperimentByExperimentName(String experimentName) {
        List<ExperimentLabel> foundExperimentLabels = ExperimentLabel.findExperimentLabelsByName(experimentName).getResultList();
        List<Experiment> experimentList = new ArrayList<Experiment>();
        for (ExperimentLabel experimentLabel : foundExperimentLabels) {
            Experiment experiment = Experiment.findExperiment(experimentLabel.getExperiment().getId());
            experimentList.add(experiment);
        }
        return experimentList;
    }
    
    @Transactional
    public static List<com.labsynch.labseer.domain.Experiment> findExperimentListByExperimentNameAndIgnoredNot(String experimentName) {
        List<ExperimentLabel> foundExperimentLabels = ExperimentLabel.findExperimentLabelsByName(experimentName).getResultList();
        List<Experiment> experimentList = new ArrayList<Experiment>();
        for (ExperimentLabel experimentLabel : foundExperimentLabels) {
            Experiment experiment = Experiment.findExperiment(experimentLabel.getExperiment().getId());
            experimentList.add(experiment);
        }
        for (Experiment experiment: experimentList) {
        	if (experiment.isIgnored()) experimentList.remove(experiment);
        }
        return experimentList;
    }

    @Transactional
    public static List<com.labsynch.labseer.domain.Experiment> findExperimentByExperimentNameAndProtocolId(String experimentName, Long protocolId) {
        List<ExperimentLabel> foundExperimentLabels = ExperimentLabel.findExperimentLabelsByNameAndProtocol(experimentName, protocolId).getResultList();
        List<Experiment> experimentList = new ArrayList<Experiment>();
        for (ExperimentLabel experimentLabel : foundExperimentLabels) {
            Experiment experiment = Experiment.findExperiment(experimentLabel.getExperiment().getId());
            experimentList.add(experiment);
        }
        return experimentList;
    }
    
    @Transactional
    public static List<com.labsynch.labseer.domain.Experiment> findExperimentListByExperimentNameAndProtocolIdAndIgnoredNot(String experimentName, Long protocolId) {
        List<ExperimentLabel> foundExperimentLabels = ExperimentLabel.findExperimentLabelsByNameAndProtocol(experimentName, protocolId).getResultList();
        List<Experiment> experimentList = new ArrayList<Experiment>();
        for (ExperimentLabel experimentLabel : foundExperimentLabels) {
            Experiment experiment = Experiment.findExperiment(experimentLabel.getExperiment().getId());
            experimentList.add(experiment);
        }
        for (Experiment experiment : experimentList) {
        	if (experiment.isIgnored()) experimentList.remove(experiment);
        }
        return experimentList;
    }

    @Transactional
    public static Collection<com.labsynch.labseer.domain.Experiment> findExperimentByNameAndProtocolId(String experimentName, Long protocolId) {
        List<ExperimentLabel> foundExperimentLabels = ExperimentLabel.findExperimentLabelsByNameAndProtocol(experimentName, protocolId).getResultList();
        Collection<Experiment> experimentList = new HashSet<Experiment>();
        for (ExperimentLabel experimentLabel : foundExperimentLabels) {
            Experiment experiment = Experiment.findExperiment(experimentLabel.getExperiment().getId());
            if (experiment.getProtocol().getId().equals(protocolId)) {
                experimentList.add(experiment);
            }
        }
        List<Experiment> experiments = Experiment.findExperimentsByCodeNameEquals(experimentName).getResultList();
        for (Experiment experiment : experiments) {
            if (experiment.getProtocol().getId() == protocolId) {
                experimentList.add(experiment);
            }
        }
        return experimentList;
    }

    @Transactional
    public String toJson() {
        return new JSONSerializer().exclude("*.class", "lsStates.lsValues.lsState", "lsStates.experiment", "analysisGroups.experiment", "lsLabels.experiment").include("lsTags", "lsLabels", "lsStates.lsValues", "analysisGroups.lsStates.lsValues", "analysisGroups.lsLabels", "analysisGroups.treatmentGroups.lsStates.lsValues", "analysisGroups.treatmentGroups.lsLabels", "analysisGroups.treatmentGroups.subjects.lsStates.lsValues", "analysisGroups.treatmentGroups.subjects.lsLabels").transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public String toPrettyJson() {
        return new JSONSerializer().exclude("*.class", "lsStates.lsValues.lsState", "lsStates.experiment", "analysisGroups.experiments", "lsLabels.experiment").include("lsTags", "lsLabels", "lsStates.lsValues", "analysisGroups", "analysisGroups.lsStates.lsValues", "analysisGroups.lsLabels", "analysisGroups.treatmentGroups.lsStates.lsValues", "analysisGroups.treatmentGroups.lsLabels", "analysisGroups.treatmentGroups.subjects.lsStates.lsValues", "analysisGroups.treatmentGroups.subjects.lsLabels").prettyPrint(true).transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public String toPrettyJsonStub() {
        return new JSONSerializer().exclude("*.class", "lsStates.lsValues.lsState", "lsStates.experiment", "analysisGroups", "lsLabels.experiment").include("lsTags", "lsLabels", "lsStates.lsValues").prettyPrint(true).transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public String toJsonStub() {
        return new JSONSerializer().exclude("*.class", "lsStates.lsValues.lsState", "lsStates.experiment", "analysisGroups", "lsLabels.experiment").include("lsTags", "lsLabels", "lsStates.lsValues").prettyPrint(false).transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public String toJsonStubWithAnalysisGroups() {
        return new JSONSerializer().exclude("*.class", "lsStates.lsValues.lsState", "lsStates.experiment", "lsLabels.experiment", "analysisGroups.subjects", "analysisGroups.experiment").include("lsTags", "lsLabels", "lsStates.lsValues", "analysisGroups").prettyPrint(false).transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public String toJsonStubWithAnalysisGroupValues() {
        return new JSONSerializer().exclude("*.class", "lsStates.lsValues.lsState", "lsStates.experiment", "lsLabels.experiment", "analysisGroups.subjects", "analysisGroups.experiment").include("lsTags", "lsLabels", "lsStates.lsValues", "analysisGroups.lsStates.lsValues").prettyPrint(false).transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public String toJsonStubWithAnalysisGroupStates() {
        return new JSONSerializer().exclude("*.class", "lsStates.lsValues.lsState", "lsStates.experiment", "lsLabels.experiment", "analysisGroups.subjects", "analysisGroups.experiment").include("lsTags", "lsLabels", "lsStates.lsValues", "analysisGroups.lsStates").prettyPrint(false).transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public static com.labsynch.labseer.domain.Experiment fromJsonToExperiment(String json) {
        return new JSONDeserializer<Experiment>().use(null, Experiment.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
    }

    @Transactional
    public static String toJsonArray(Collection<com.labsynch.labseer.domain.Experiment> collection) {
        return new JSONSerializer().include("lsTags", "lsLabels", "lsStates.lsValues", "analysisGroups.lsStates.lsValues", "analysisGroups.lsLabels", "analysisGroups.treatmentGroups.lsStates.lsValues", "analysisGroups.treatmentGroups.lsLabels", "analysisGroups.treatmentGroups.subjects.lsStates.lsValues", "analysisGroups.treatmentGroups.subjects.lsLabels").exclude("*.class", "lsStates.lsValues.lsState", "lsStates.experiment", "analysisGroups.experiment", "analysisGroups.subject", "lsLabels.experiment").transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    @Transactional
    public static String toJsonArrayPretty(Collection<com.labsynch.labseer.domain.Experiment> collection) {
        return new JSONSerializer().include("lsTags", "lsLabels", "lsStates.lsValues", "analysisGroups.lsStates.lsValues", "analysisGroups.lsLabels", "analysisGroups.treatmentGroups.lsStates.lsValues", "analysisGroups.treatmentGroups.lsLabels", "analysisGroups.treatmentGroups.subjects.lsStates.lsValues", "analysisGroups.treatmentGroups.subjects.lsLabels").exclude("*.class", "lsStates.lsValues.lsState", "lsStates.experiment", "analysisGroups.experiment", "analysisGroups.subject", "lsLabels.experiment").prettyPrint(true).transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    @Transactional
    public static String toJsonArrayStub(Collection<com.labsynch.labseer.domain.Experiment> collection) {
        return new JSONSerializer().include("lsTags", "lsLabels", "lsStates.lsValues").exclude("*.class", "analysisGroups", "lsStates.lsValues.lsState", "lsStates.experiment", "analysisGroups.experiment", "lsLabels.experiment").transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    @Transactional
    public static String toJsonArrayStubWithAG(Collection<com.labsynch.labseer.domain.Experiment> collection) {
        return new JSONSerializer().include("lsTags", "lsLabels", "lsStates.lsValues", "analysisGroups").exclude("*.class", "lsStates.lsValues.lsState", "lsStates.experiment", "analysisGroups.experiment", "analysisGroups.subjects", "lsLabels.experiment").transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    @Transactional
    public static String toJsonArrayStubWithAGStates(Collection<com.labsynch.labseer.domain.Experiment> collection) {
        return new JSONSerializer().include("lsTags", "lsLabels", "lsStates.lsValues", "analysisGroups.lsStates").exclude("*.class", "lsStates.lsValues.lsState", "lsStates.experiment", "analysisGroups.experiment", "analysisGroups.subjects", "lsLabels.experiment").transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    @Transactional
    public static String toJsonArrayStubWithAGValues(Collection<com.labsynch.labseer.domain.Experiment> collection) {
        return new JSONSerializer().include("lsTags", "lsLabels", "lsStates.lsValues", "analysisGroups.lsStates.lsValues").exclude("*.class", "lsStates.lsValues.lsState", "lsStates.experiment", "analysisGroups.experiment", "analysisGroups.subjects", "lsLabels.experiment").transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    @Transactional
    public static String toJsonArrayStubPretty(Collection<com.labsynch.labseer.domain.Experiment> collection) {
        return new JSONSerializer().include("lsTags", "lsLabels", "lsStates.lsValues").exclude("*.class", "analysisGroups", "lsStates.lsValues.lsState", "lsStates.experiment", "analysisGroups.experiment", "lsLabels.experiment").prettyPrint(true).transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    @Transactional
    public static Collection<com.labsynch.labseer.domain.Experiment> fromJsonArrayToExperiments(String json) {
        return new JSONDeserializer<List<Experiment>>().use(null, ArrayList.class).use("values", Experiment.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
    }

    @Transactional
    public static Collection<com.labsynch.labseer.domain.Experiment> fromJsonArrayToExperiments(Reader json) {
        return new JSONDeserializer<List<Experiment>>().use(null, ArrayList.class).use("values", Experiment.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
    }

    @Transactional
    public static void deleteExperiment(com.labsynch.labseer.domain.Experiment experiment) {
        EntityManager em = Experiment.entityManager();
        String deleteSQL = "DELETE FROM ItxSubjectContainer oo WHERE id in (select o.id from ItxSubjectContainer o where o.subject.treatmentGroup.analysisGroup.experiment.id = :experimentId)";
        Query q = em.createQuery(deleteSQL);
        q.setParameter("experimentId", experiment.getId());
        int numberOfDeletedEntities = q.executeUpdate();
        logger.info("deleted iteractions: " + numberOfDeletedEntities);
        if (em.contains(experiment)) {
            em.remove(experiment);
        } else {
            Experiment attached = Experiment.findExperiment(experiment.getId());
            logger.info("trying to remove the experiment: " + attached.getId());
            attached.remove();
        }
    }

    @Transactional
    public void logicalDelete() {
    	if (!this.isIgnored()) this.setIgnored(true);
    	Collection<ExperimentLabel> labels = ExperimentLabel.findExperimentLabelsByExperiment(this).getResultList();
    	labels.size();
    	if (!labels.isEmpty()) {
    		for (ExperimentLabel label: labels) {
        		label.remove();
        		//label.logicalDelete();
        	}
    	}
    }
    
    @Transactional
    public static void deleteExperiment(Long experimentId) {
        EntityManager em = Experiment.entityManager();
        em.flush();
        em.clear();
        String deleteSQL = "DELETE FROM Experiment oo WHERE id = :experimentId";
        Query q = em.createQuery(deleteSQL);
        q.setParameter("experimentId", experimentId);
        int numberOfDeletedEntities = q.executeUpdate();
        em.flush();
        logger.info("deleted iteractions: " + numberOfDeletedEntities);
    }

    @Transactional
    public static void deleteExperimentBySQL(com.labsynch.labseer.domain.Experiment experiment) {
        EntityManager em = Experiment.entityManager();
        TypedQuery<Experiment> q = em.createQuery("DELETE FROM Experiment AS o WHERE o.experiment = :experiment", Experiment.class);
        q.setParameter("experiment", experiment);
        q.executeUpdate();
    }

    @Transactional
    public static void deleteExperimentByTransactionID(Long lsTransaction) {
        EntityManager em = Experiment.entityManager();
        TypedQuery<Experiment> q = em.createQuery("DELETE FROM Experiment AS o WHERE o.lsTransaction = :lsTransaction", Experiment.class);
        q.setParameter("lsTransaction", lsTransaction);
        q.executeUpdate();
    }

    @Transactional
    public static void removeExperimentItxAware(Long id) {
        Experiment experiment = findExperiment(id);
        EntityManager em = Experiment.entityManager();
        Query q1 = em.createNativeQuery("DELETE FROM itx_experiment_experiment o WHERE o.first_experiment_id = :id", ItxExperimentExperiment.class);
        Query q2 = em.createNativeQuery("DELETE FROM itx_experiment_experiment o WHERE o.second_experiment_id = :id", ItxExperimentExperiment.class);
        q1.setParameter("id", id);
        q2.setParameter("id", id);
        q1.executeUpdate();
        q2.executeUpdate();
        experiment.remove();
    }

    public static long countExperiments() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Experiment o", Long.class).getSingleResult();
    }

    @Transactional
    public static List<com.labsynch.labseer.domain.Experiment> findAllExperiments() {
        return entityManager().createQuery("SELECT o FROM Experiment o", Experiment.class).getResultList();
    }

    @Transactional
    public static com.labsynch.labseer.domain.Experiment findExperiment(Long id) {
        if (id == null) return null;
        return entityManager().find(Experiment.class, id);
    }

    @Transactional
    public static String findExperimentJson(Long id) {
        if (id == null) return null;
        return entityManager().find(Experiment.class, id).toJson();
    }

    public static List<com.labsynch.labseer.domain.Experiment> findExperimentEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Experiment o", Experiment.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public com.labsynch.labseer.domain.Experiment merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Experiment merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public static TypedQuery<com.labsynch.labseer.domain.Experiment> findExperimentsByCodeNameEquals(String codeName) {
        if (codeName == null || codeName.length() == 0) throw new IllegalArgumentException("The codeName argument is required");
        EntityManager em = Experiment.entityManager();
        TypedQuery<Experiment> q = em.createQuery("SELECT o FROM Experiment AS o WHERE o.codeName = :codeName", Experiment.class);
        q.setParameter("codeName", codeName);
        return q;
    }

    public static TypedQuery<com.labsynch.labseer.domain.Experiment> findExperimentsByLsTransaction(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = Experiment.entityManager();
        TypedQuery<Experiment> q = em.createQuery("SELECT o FROM Experiment AS o WHERE o.lsTransaction = :lsTransaction", Experiment.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

    public static TypedQuery<com.labsynch.labseer.domain.Experiment> findExperimentsByProtocol(Protocol protocol) {
        if (protocol == null) throw new IllegalArgumentException("The protocol argument is required");
        EntityManager em = Experiment.entityManager();
        TypedQuery<Experiment> q = em.createQuery("SELECT o FROM Experiment AS o WHERE o.protocol = :protocol", Experiment.class);
        q.setParameter("protocol", protocol);
        return q;
    }

    public static Query findExperimentsByBatchCodes(Collection<java.lang.String> codeValues) {
        if (codeValues == null) throw new IllegalArgumentException("The batchCodeList argument is required");
        EntityManager em = Experiment.entityManager();
        String sqlQueryOld = "SELECT DISTINCT o FROM Experiment " + "WHERE o.analysisGroup.lsState.lsValues = :codeValues";
        String sqlQuery = "select distinct exp.* " + "from analysis_group_value agv " + "join analysis_group_state ags on ags.id = agv.analysis_state_id AND ags.ignored = false " + "join analysis_group ag on ag.id = ags.analysis_group_id AND ag.ignored = false " + "join experiment exp on exp.id = ag.experiment_id AND exp.ignored = false " + "where agv.code_value in (:batchCodeList) AND agv.ignored = false";
        String sqlQueryAA = "select distinct exp " + "from AnalysisGroupValue agv " + "join agv.lsState ags with ags.ignored = false " + "join ags.analysisGroup ag with ag.ignored = false " + "join ag.experiment exp with exp.ignored = false " + "where agv.codeValue in (:batchCodeList) AND agv.ignored = false";
        String sqlQueryNN = "select DISTINCT o FROM Experiment AS o " + "WHERE o.analysisGroups.lsStates.lsType = 'data' " + "AND o.analysisGroups.lsStates.lsValues.lsKind = 'batch code' " + "AND o.analysisGroups.lsStates.lsValues.codeValue IN (:batchCodeList) ";
        String sqlQueryOO = "select o FROM Experiment AS o " + "WHERE o.id in ( SELECT DISTINCT exp.id " + "FROM AnalysisGroupValue agv " + "JOIN agv.lsState.analysisGroup.experiment exp " + "WHERE agv.codeValue IN (:batchCodeList) )";
        String sqlQueryPP = "select DISTINCT o FROM Experiment AS o " + "WHERE o.analysisGroups.lsStates.lsValues.codeValue IN (:batchCodeList) ";
        String sqlQueryRef = "select new com.labsynch.labseer.dto.AnalysisGroupValueDTO(agv.id, expt.id as experimentId, expt.codeName, " + "agv.lsType as lsType, agv.lsKind as lsKind, " + "agv.stringValue as stringValue, agv.numericValue as numericValue, " + "agv2.codeValue AS testedLot " + " ) FROM AnalysisGroup ag " + "JOIN ag.lsStates ags with ags.lsType = 'data' " + "JOIN ags.lsValues agv with agv.lsKind != 'tested concentration' AND agv.lsKind != 'batch code' AND agv.lsKind != 'time' " + "JOIN ags.lsValues agv2 with agv2.lsKind = 'batch code' " + "JOIN ag.experiment expt " + "WHERE agv2.codeValue IN (:batchCodeList) ";
        Query q = em.createQuery(sqlQueryAA, Experiment.class);
        q.setParameter("batchCodeList", codeValues);
        return q;
    }

    public String findPreferredName() {
        if (this.getId() == null) {
            logger.debug("attempting to get the experiment preferred name -- but it is null");
            return " ";
        } else {
            List<ExperimentLabel> labels = ExperimentLabel.findExperimentPreferredName(this.getId()).getResultList();
            if (labels.size() == 1) {
                return labels.get(0).getLabelText();
            } else if (labels.size() > 1) {
                logger.error("ERROR: found mulitiple preferred names: " + labels.size());
                return labels.get(0).getLabelText();
            } else {
                logger.error("ERROR: no preferred name found");
                return " ";
            }
        }
    }

    public static TypedQuery<com.labsynch.labseer.domain.Experiment> findExperimentsByProtocolTypeAndKindAndExperimentTypeAndKind(String protocolType, String protocolKind, String lsType, String lsKind) {
        EntityManager em = Experiment.entityManager();
        TypedQuery<Experiment> q = em.createQuery("SELECT o FROM Experiment AS o WHERE o.protocol.lsType = :protocolType AND " + "o.protocol.lsKind = :protocolKind AND o.lsType = :lsType AND o.lsKind = :lsKind", Experiment.class);
        q.setParameter("protocolType", protocolType);
        q.setParameter("protocolKind", protocolKind);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return q;
    }
}
