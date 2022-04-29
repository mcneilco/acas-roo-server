package com.labsynch.labseer.domain;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.labsynch.labseer.dto.FlatThingCsvDTO;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@Configurable

public class TreatmentGroup extends AbstractThing {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "treatmentGroup", fetch = FetchType.LAZY)
    private Set<TreatmentGroupLabel> lsLabels = new HashSet<TreatmentGroupLabel>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "treatmentGroup", fetch = FetchType.LAZY)
    private Set<TreatmentGroupState> lsStates = new HashSet<TreatmentGroupState>();

    @ManyToMany(cascade = { javax.persistence.CascadeType.PERSIST, javax.persistence.CascadeType.MERGE }, mappedBy = "treatmentGroups")
    private Set<Subject> subjects = new HashSet<Subject>();

    @ManyToMany(cascade = { javax.persistence.CascadeType.PERSIST, javax.persistence.CascadeType.MERGE }, fetch = FetchType.LAZY)
    @JoinTable(name = "ANALYSISGROUP_TREATMENTGROUP", joinColumns = { @javax.persistence.JoinColumn(name = "treatment_group_id") }, inverseJoinColumns = { @javax.persistence.JoinColumn(name = "analysis_group_id") })
    private Set<AnalysisGroup> analysisGroups = new HashSet<AnalysisGroup>();

    public TreatmentGroup() {
    }

    public TreatmentGroup(com.labsynch.labseer.domain.TreatmentGroup treatmentGroup) {
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

    public static com.labsynch.labseer.domain.TreatmentGroup update(com.labsynch.labseer.domain.TreatmentGroup treatmentGroup) {
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
        return new JSONSerializer().include("lsLabels", "lsStates.lsValues", "subjects").exclude("*.class", "analysisGroups.experiments", "lsStates.treatmentGroup", "lsLabels.treatmentGroup", "subjects.treatmentGroups").serialize(this);
    }

    public static com.labsynch.labseer.domain.TreatmentGroup fromJsonToTreatmentGroup(String json) {
        return new JSONDeserializer<TreatmentGroup>().use(null, TreatmentGroup.class).deserialize(json);
    }

    public static String toJsonArray(Collection<com.labsynch.labseer.domain.TreatmentGroup> collection) {
        return new JSONSerializer().include("lsLabels", "lsStates.lsValues", "subjects").exclude("*.class", "analysisGroup.experiment", "lsStates.treatmentGroup", "lsLabels.treatmentGroup", "subjects.treatmentGroup").serialize(collection);
    }

    public static Collection<com.labsynch.labseer.domain.TreatmentGroup> fromJsonArrayToTreatmentGroups(String json) {
        return new JSONDeserializer<List<TreatmentGroup>>().use(null, ArrayList.class).use("values", TreatmentGroup.class).deserialize(json);
    }

    public static Collection<com.labsynch.labseer.domain.TreatmentGroup> fromJsonArrayToTreatmentGroups(Reader json) {
        return new JSONDeserializer<List<TreatmentGroup>>().use(null, ArrayList.class).use("values", TreatmentGroup.class).deserialize(json);
    }

    public static int deleteByExperimentID(Long experimentId) {
        if (experimentId == null) return 0;
        EntityManager em = SubjectValue.entityManager();
        String deleteSQL = "DELETE FROM TreatmentGroup t WHERE TreatmentGroup IN (SELECT t FROM TreatmentGroup t JOIN t.analysisGroups a JOIN a.experiments e WHERE e.id = :experimentId)";
        Query q = em.createQuery(deleteSQL);
        q.setParameter("experimentId", experimentId);
        return 0;
    }

    @Transactional
    public static Collection<java.lang.Long> removeOrphans(Collection<java.lang.Long> treatmentGroupIds) {
        Collection<Long> subjectIds = new HashSet<Long>();
        for (Long id : treatmentGroupIds) {
            TreatmentGroup treatmentGroup = TreatmentGroup.findTreatmentGroup(id);
            if (treatmentGroup.getAnalysisGroups().isEmpty()) {
                subjectIds.addAll(TreatmentGroup.removeCascadeAware(id));
            }
        }
        return subjectIds;
    }

    @Transactional
    private static Collection<java.lang.Long> removeCascadeAware(Long id) {
        TreatmentGroup treatmentGroup = findTreatmentGroup(id);
        Collection<Subject> subjects = treatmentGroup.getSubjects();
        Set<Long> subjectIds = new HashSet<Long>();
        for (Subject subject : subjects) {
            subjectIds.add(subject.getId());
        }
        subjects.clear();
        EntityManager em = TreatmentGroup.entityManager();
        Query q1 = em.createNativeQuery("DELETE FROM treatmentGroup_subject o WHERE o.treatment_group_id = :id", TreatmentGroup.class);
        q1.setParameter("id", id);
        q1.executeUpdate();
        treatmentGroup.remove();
        return subjectIds;
    }

	public static Long countFindTreatmentGroupsByAnalysisGroups(Set<AnalysisGroup> analysisGroups) {
        if (analysisGroups == null) throw new IllegalArgumentException("The analysisGroups argument is required");
        EntityManager em = TreatmentGroup.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(o) FROM TreatmentGroup AS o WHERE");
        for (int i = 0; i < analysisGroups.size(); i++) {
            if (i > 0) queryBuilder.append(" AND");
            queryBuilder.append(" :analysisGroups_item").append(i).append(" MEMBER OF o.analysisGroups");
        }
        TypedQuery q = em.createQuery(queryBuilder.toString(), Long.class);
        int analysisGroupsIndex = 0;
        for (AnalysisGroup _analysisgroup: analysisGroups) {
            q.setParameter("analysisGroups_item" + analysisGroupsIndex++, _analysisgroup);
        }
        return ((Long) q.getSingleResult());
    }

	public static Long countFindTreatmentGroupsByCodeNameEquals(String codeName) {
        if (codeName == null || codeName.length() == 0) throw new IllegalArgumentException("The codeName argument is required");
        EntityManager em = TreatmentGroup.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM TreatmentGroup AS o WHERE o.codeName = :codeName", Long.class);
        q.setParameter("codeName", codeName);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindTreatmentGroupsByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = TreatmentGroup.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM TreatmentGroup AS o WHERE o.lsTransaction = :lsTransaction", Long.class);
        q.setParameter("lsTransaction", lsTransaction);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<TreatmentGroup> findTreatmentGroupsByAnalysisGroups(Set<AnalysisGroup> analysisGroups) {
        if (analysisGroups == null) throw new IllegalArgumentException("The analysisGroups argument is required");
        EntityManager em = TreatmentGroup.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM TreatmentGroup AS o WHERE");
        for (int i = 0; i < analysisGroups.size(); i++) {
            if (i > 0) queryBuilder.append(" AND");
            queryBuilder.append(" :analysisGroups_item").append(i).append(" MEMBER OF o.analysisGroups");
        }
        TypedQuery<TreatmentGroup> q = em.createQuery(queryBuilder.toString(), TreatmentGroup.class);
        int analysisGroupsIndex = 0;
        for (AnalysisGroup _analysisgroup: analysisGroups) {
            q.setParameter("analysisGroups_item" + analysisGroupsIndex++, _analysisgroup);
        }
        return q;
    }

	public static TypedQuery<TreatmentGroup> findTreatmentGroupsByAnalysisGroups(Set<AnalysisGroup> analysisGroups, String sortFieldName, String sortOrder) {
        if (analysisGroups == null) throw new IllegalArgumentException("The analysisGroups argument is required");
        EntityManager em = TreatmentGroup.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM TreatmentGroup AS o WHERE");
        for (int i = 0; i < analysisGroups.size(); i++) {
            if (i > 0) queryBuilder.append(" AND");
            queryBuilder.append(" :analysisGroups_item").append(i).append(" MEMBER OF o.analysisGroups");
        }
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" " + sortOrder);
            }
        }
        TypedQuery<TreatmentGroup> q = em.createQuery(queryBuilder.toString(), TreatmentGroup.class);
        int analysisGroupsIndex = 0;
        for (AnalysisGroup _analysisgroup: analysisGroups) {
            q.setParameter("analysisGroups_item" + analysisGroupsIndex++, _analysisgroup);
        }
        return q;
    }

	public static TypedQuery<TreatmentGroup> findTreatmentGroupsByCodeNameEquals(String codeName) {
        if (codeName == null || codeName.length() == 0) throw new IllegalArgumentException("The codeName argument is required");
        EntityManager em = TreatmentGroup.entityManager();
        TypedQuery<TreatmentGroup> q = em.createQuery("SELECT o FROM TreatmentGroup AS o WHERE o.codeName = :codeName", TreatmentGroup.class);
        q.setParameter("codeName", codeName);
        return q;
    }

	public static TypedQuery<TreatmentGroup> findTreatmentGroupsByCodeNameEquals(String codeName, String sortFieldName, String sortOrder) {
        if (codeName == null || codeName.length() == 0) throw new IllegalArgumentException("The codeName argument is required");
        EntityManager em = TreatmentGroup.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM TreatmentGroup AS o WHERE o.codeName = :codeName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<TreatmentGroup> q = em.createQuery(queryBuilder.toString(), TreatmentGroup.class);
        q.setParameter("codeName", codeName);
        return q;
    }

	public static TypedQuery<TreatmentGroup> findTreatmentGroupsByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = TreatmentGroup.entityManager();
        TypedQuery<TreatmentGroup> q = em.createQuery("SELECT o FROM TreatmentGroup AS o WHERE o.lsTransaction = :lsTransaction", TreatmentGroup.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

	public static TypedQuery<TreatmentGroup> findTreatmentGroupsByLsTransactionEquals(Long lsTransaction, String sortFieldName, String sortOrder) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = TreatmentGroup.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM TreatmentGroup AS o WHERE o.lsTransaction = :lsTransaction");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<TreatmentGroup> q = em.createQuery(queryBuilder.toString(), TreatmentGroup.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("lsLabels", "lsStates", "subjects", "analysisGroups");

	public static long countTreatmentGroups() {
        return entityManager().createQuery("SELECT COUNT(o) FROM TreatmentGroup o", Long.class).getSingleResult();
    }

	public static List<TreatmentGroup> findAllTreatmentGroups() {
        return entityManager().createQuery("SELECT o FROM TreatmentGroup o", TreatmentGroup.class).getResultList();
    }

	public static List<TreatmentGroup> findAllTreatmentGroups(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM TreatmentGroup o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, TreatmentGroup.class).getResultList();
    }

	public static TreatmentGroup findTreatmentGroup(Long id) {
        if (id == null) return null;
        return entityManager().find(TreatmentGroup.class, id);
    }

	public static List<TreatmentGroup> findTreatmentGroupEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM TreatmentGroup o", TreatmentGroup.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<TreatmentGroup> findTreatmentGroupEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM TreatmentGroup o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, TreatmentGroup.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public TreatmentGroup merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        TreatmentGroup merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public Set<TreatmentGroupLabel> getLsLabels() {
        return this.lsLabels;
    }

	public void setLsLabels(Set<TreatmentGroupLabel> lsLabels) {
        this.lsLabels = lsLabels;
    }

	public Set<TreatmentGroupState> getLsStates() {
        return this.lsStates;
    }

	public void setLsStates(Set<TreatmentGroupState> lsStates) {
        this.lsStates = lsStates;
    }

	public Set<Subject> getSubjects() {
        return this.subjects;
    }

	public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }

	public Set<AnalysisGroup> getAnalysisGroups() {
        return this.analysisGroups;
    }

	public void setAnalysisGroups(Set<AnalysisGroup> analysisGroups) {
        this.analysisGroups = analysisGroups;
    }
}
