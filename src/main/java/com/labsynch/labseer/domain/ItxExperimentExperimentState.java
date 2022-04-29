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
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@Configurable

@Table(name = "ITX_EXPT_EXPT_STATE")
public class ItxExperimentExperimentState extends AbstractState {

    public ItxExperimentExperimentState(ItxExperimentExperimentState itxState) {
        this.setRecordedBy(itxState.getRecordedBy());
        this.setRecordedDate(itxState.getRecordedDate());
        this.setLsTransaction(itxState.getLsTransaction());
        this.setModifiedBy(itxState.getModifiedBy());
        this.setModifiedDate(itxState.getModifiedDate());

    }

    public static ItxExperimentExperimentState update(ItxExperimentExperimentState object) {
        ItxExperimentExperimentState updatedObject = new JSONDeserializer<ItxExperimentExperimentState>()
                .use(null, ItxExperimentExperimentState.class).deserializeInto(object.toJson(),
                        ItxExperimentExperimentState.findItxExperimentExperimentState(object.getId()));
        updatedObject.setModifiedDate(new Date());
        updatedObject.merge();
        return updatedObject;
    }

    @ManyToOne
    @JoinColumn(name = "itx_experiment_experiment")
    private ItxExperimentExperiment itxExperimentExperiment;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lsState", fetch = FetchType.LAZY)
    private Set<ItxExperimentExperimentValue> lsValues = new HashSet<ItxExperimentExperimentValue>();

    @Transactional
    public String toJson() {
        return new JSONSerializer().exclude("*.class")
                .transform(new ExcludeNulls(), void.class)
                .serialize(this);
    }

    @Transactional
    public String toJsonStub() {
        return new JSONSerializer().exclude("*.class", "itxExperimentExperiment")
                .transform(new ExcludeNulls(), void.class)
                .serialize(this);
    }

    public static ItxExperimentExperimentState fromJsonToItxContainerContainerState(String json) {
        return new JSONDeserializer<ItxExperimentExperimentState>().use(null, ItxExperimentExperimentState.class)
                .use(BigDecimal.class, new CustomBigDecimalFactory())
                .deserialize(json);
    }

    @Transactional
    public static String toJsonArray(Collection<ItxExperimentExperimentState> collection) {
        return new JSONSerializer().exclude("*.class")
                .transform(new ExcludeNulls(), void.class)
                .serialize(collection);
    }

    @Transactional
    public static String toJsonArrayStub(Collection<ItxExperimentExperimentState> collection) {
        return new JSONSerializer().exclude("*.class", "itxExperimentExperiment")
                .transform(new ExcludeNulls(), void.class)
                .serialize(collection);
    }

    public static Collection<ItxExperimentExperimentState> fromJsonArrayToItxExperimentExperimentStates(String json) {
        return new JSONDeserializer<List<ItxExperimentExperimentState>>().use(null, ArrayList.class)
                .use("values", ItxExperimentExperimentState.class)
                .use(BigDecimal.class, new CustomBigDecimalFactory())
                .deserialize(json);
    }

    public static Collection<ItxExperimentExperimentState> fromJsonArrayToItxExperimentExperimentStates(Reader json) {
        return new JSONDeserializer<List<ItxExperimentExperimentState>>().use(null, ArrayList.class)
                .use("values", ItxExperimentExperimentState.class)
                .use(BigDecimal.class, new CustomBigDecimalFactory())
                .deserialize(json);
    }

    public ItxExperimentExperiment getItxExperimentExperiment() {
        return this.itxExperimentExperiment;
    }

    public void setItxExperimentExperiment(ItxExperimentExperiment itxExperimentExperiment) {
        this.itxExperimentExperiment = itxExperimentExperiment;
    }

    public Set<ItxExperimentExperimentValue> getLsValues() {
        return this.lsValues;
    }

    public void setLsValues(Set<ItxExperimentExperimentValue> lsValues) {
        this.lsValues = lsValues;
    }

    public ItxExperimentExperimentState() {
        super();
    }

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("itxExperimentExperiment",
            "lsValues");

    public static long countItxExperimentExperimentStates() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ItxExperimentExperimentState o", Long.class)
                .getSingleResult();
    }

    public static List<ItxExperimentExperimentState> findAllItxExperimentExperimentStates() {
        return entityManager()
                .createQuery("SELECT o FROM ItxExperimentExperimentState o", ItxExperimentExperimentState.class)
                .getResultList();
    }

    public static List<ItxExperimentExperimentState> findAllItxExperimentExperimentStates(String sortFieldName,
            String sortOrder) {
        String jpaQuery = "SELECT o FROM ItxExperimentExperimentState o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ItxExperimentExperimentState.class).getResultList();
    }

    public static ItxExperimentExperimentState findItxExperimentExperimentState(Long id) {
        if (id == null)
            return null;
        return entityManager().find(ItxExperimentExperimentState.class, id);
    }

    public static List<ItxExperimentExperimentState> findItxExperimentExperimentStateEntries(int firstResult,
            int maxResults) {
        return entityManager()
                .createQuery("SELECT o FROM ItxExperimentExperimentState o", ItxExperimentExperimentState.class)
                .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static List<ItxExperimentExperimentState> findItxExperimentExperimentStateEntries(int firstResult,
            int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ItxExperimentExperimentState o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ItxExperimentExperimentState.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public ItxExperimentExperimentState merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        ItxExperimentExperimentState merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public static ItxExperimentExperimentState fromJsonToItxExperimentExperimentState(String json) {
        return new JSONDeserializer<ItxExperimentExperimentState>()
                .use(null, ItxExperimentExperimentState.class).deserialize(json);
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
