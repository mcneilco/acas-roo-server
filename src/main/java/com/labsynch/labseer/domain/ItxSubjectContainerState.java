package com.labsynch.labseer.domain;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Query;

import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Configurable
@Entity

public class ItxSubjectContainerState extends AbstractState {

    public ItxSubjectContainerState() {
    }

    public ItxSubjectContainerState(ItxSubjectContainerState itxState) {
        super.setRecordedBy(itxState.getRecordedBy());
        super.setRecordedDate(itxState.getRecordedDate());
        super.setLsTransaction(itxState.getLsTransaction());
        super.setModifiedBy(itxState.getModifiedBy());
        super.setModifiedDate(itxState.getModifiedDate());
        // this.itxSubjectContainer = itxState.getItxSubjectContainer();
        // this.itxSubjectContainerValues = itxState.getItxSubjectContainerValues();
    }

    public static ItxSubjectContainerState update(ItxSubjectContainerState object) {
        ItxSubjectContainerState updatedObject = new JSONDeserializer<ItxSubjectContainerState>()
                .use(null, ItxSubjectContainerState.class).deserializeInto(object.toJson(),
                        ItxSubjectContainerState.findItxSubjectContainerState(object.getId()));
        updatedObject.setModifiedDate(new Date());
        updatedObject.merge();
        return updatedObject;
    }

    @ManyToOne
    @JoinColumn(name = "itx_subject_container")
    private ItxSubjectContainer itxSubjectContainer;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lsState")
    private Set<ItxSubjectContainerValue> lsValues = new HashSet<ItxSubjectContainerValue>();

    @Transactional
    public String toJson() {
        return new JSONSerializer().exclude("*.class")
                .transform(new ExcludeNulls(), void.class)
                .serialize(this);
    }

    public static ItxSubjectContainerState fromJsonToItxSubjectContainerState(String json) {
        return new JSONDeserializer<ItxSubjectContainerState>().use(null, ItxSubjectContainerState.class)
                .use(BigDecimal.class, new CustomBigDecimalFactory())
                .deserialize(json);
    }

    @Transactional
    public static String toJsonArray(Collection<ItxSubjectContainerState> collection) {
        return new JSONSerializer().exclude("*.class")
                .transform(new ExcludeNulls(), void.class)
                .serialize(collection);
    }

    @Transactional
    public static String toJsonArrayStub(Collection<ItxSubjectContainerState> collection) {
        return new JSONSerializer().exclude("*.class", "itxSubjectContainer")
                .transform(new ExcludeNulls(), void.class)
                .serialize(collection);
    }

    public static Collection<ItxSubjectContainerState> fromJsonArrayToItxSubjectContainerStates(String json) {
        return new JSONDeserializer<List<ItxSubjectContainerState>>()
                .use(null, ArrayList.class)
                .use("values", ItxSubjectContainerState.class)
                .use(BigDecimal.class, new CustomBigDecimalFactory())
                .deserialize(json);
    }

    public static Collection<ItxSubjectContainerState> fromJsonArrayToItxSubjectContainerStates(Reader json) {
        return new JSONDeserializer<List<ItxSubjectContainerState>>()
                .use(null, ArrayList.class)
                .use("values", ItxSubjectContainerState.class)
                .use(BigDecimal.class, new CustomBigDecimalFactory())
                .deserialize(json);
    }

    @Transactional
    public static int deleteByExperimentID(Long experimentId) {
        if (experimentId == null)
            return 0;
        EntityManager em = TreatmentGroupValue.entityManager();
        String deleteSQL = "DELETE FROM ItxSubjectContainerState oo WHERE id in (select o.id from ItxSubjectContainerState o where o.itxSubjectContainer.subject.treatmentGroup.analysisGroup.experiment.id = :experimentId)";

        Query q = em.createQuery(deleteSQL);
        q.setParameter("experimentId", experimentId);
        int numberOfDeletedEntities = q.executeUpdate();
        return numberOfDeletedEntities;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public ItxSubjectContainer getItxSubjectContainer() {
        return this.itxSubjectContainer;
    }

    public void setItxSubjectContainer(ItxSubjectContainer itxSubjectContainer) {
        this.itxSubjectContainer = itxSubjectContainer;
    }

    public Set<ItxSubjectContainerValue> getLsValues() {
        return this.lsValues;
    }

    public void setLsValues(Set<ItxSubjectContainerValue> lsValues) {
        this.lsValues = lsValues;
    }

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("itxSubjectContainer",
            "lsValues");

    public static long countItxSubjectContainerStates() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ItxSubjectContainerState o", Long.class)
                .getSingleResult();
    }

    public static List<ItxSubjectContainerState> findAllItxSubjectContainerStates() {
        return entityManager().createQuery("SELECT o FROM ItxSubjectContainerState o", ItxSubjectContainerState.class)
                .getResultList();
    }

    public static List<ItxSubjectContainerState> findAllItxSubjectContainerStates(String sortFieldName,
            String sortOrder) {
        String jpaQuery = "SELECT o FROM ItxSubjectContainerState o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ItxSubjectContainerState.class).getResultList();
    }

    public static ItxSubjectContainerState findItxSubjectContainerState(Long id) {
        if (id == null)
            return null;
        return entityManager().find(ItxSubjectContainerState.class, id);
    }

    public static List<ItxSubjectContainerState> findItxSubjectContainerStateEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ItxSubjectContainerState o", ItxSubjectContainerState.class)
                .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static List<ItxSubjectContainerState> findItxSubjectContainerStateEntries(int firstResult, int maxResults,
            String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ItxSubjectContainerState o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ItxSubjectContainerState.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public ItxSubjectContainerState merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        ItxSubjectContainerState merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
