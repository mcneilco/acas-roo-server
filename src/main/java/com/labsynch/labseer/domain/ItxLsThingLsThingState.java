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
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@Configurable

public class ItxLsThingLsThingState extends AbstractState {

    private static final Logger logger = LoggerFactory.getLogger(ItxLsThingLsThingState.class);

    public ItxLsThingLsThingState(ItxLsThingLsThingState itxState) {
        super.setLsType(itxState.getLsType());
        super.setLsKind(itxState.getLsKind());
        super.setIgnored(itxState.isIgnored());
        super.setDeleted(itxState.isDeleted());
        super.setRecordedBy(itxState.getRecordedBy());
        super.setRecordedDate(itxState.getRecordedDate());
        super.setLsTransaction(itxState.getLsTransaction());
        super.setModifiedBy(itxState.getModifiedBy());
        super.setModifiedDate(itxState.getModifiedDate());

    }

    public static ItxLsThingLsThingState update(ItxLsThingLsThingState itxState) {
        ItxLsThingLsThingState updatedObject = ItxLsThingLsThingState.findItxLsThingLsThingState(itxState.getId());
        updatedObject.setLsType(itxState.getLsType());
        updatedObject.setLsKind(itxState.getLsKind());
        updatedObject.setIgnored(itxState.isIgnored());
        updatedObject.setDeleted(itxState.isDeleted());
        updatedObject.setRecordedBy(itxState.getRecordedBy());
        updatedObject.setRecordedDate(itxState.getRecordedDate());
        updatedObject.setLsTransaction(itxState.getLsTransaction());
        updatedObject.setModifiedDate(new Date());
        updatedObject.merge();
        return updatedObject;
    }

    // public static ItxLsThingLsThingState update(ItxLsThingLsThingState object) {
    // ItxLsThingLsThingState updatedObject = new
    // JSONDeserializer<ItxLsThingLsThingState>().use(null,
    // ItxLsThingLsThingState.class).
    // deserializeInto(object.toJson(),
    // ItxLsThingLsThingState.findItxLsThingLsThingState(object.getId()));
    // updatedObject.setModifiedDate(new Date());
    // updatedObject.merge();
    // return updatedObject;
    // }

    public static ItxLsThingLsThingState updateNoMerge(
            ItxLsThingLsThingState object) {
        ItxLsThingLsThingState updatedObject = new JSONDeserializer<ItxLsThingLsThingState>()
                .use(null, ItxLsThingLsThingState.class).deserializeInto(object.toJson(),
                        ItxLsThingLsThingState.findItxLsThingLsThingState(object.getId()));
        updatedObject.setModifiedDate(new Date());
        return updatedObject;
    }

    @ManyToOne
    @JoinColumn(name = "itx_ls_thing_ls_thing")
    private ItxLsThingLsThing itxLsThingLsThing;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lsState", fetch = FetchType.LAZY)
    private Set<ItxLsThingLsThingValue> lsValues = new HashSet<ItxLsThingLsThingValue>();

    @Transactional
    public String toJson() {
        return new JSONSerializer().exclude("*.class")
                .transform(new ExcludeNulls(), void.class)
                .serialize(this);
    }

    @Transactional
    public String toJsonStub() {
        return new JSONSerializer().exclude("*.class", "itxLsThingLsThing")
                .transform(new ExcludeNulls(), void.class)
                .serialize(this);
    }

    public static ItxLsThingLsThingState fromJsonToItxLsThingLsThingState(String json) {
        return new JSONDeserializer<ItxLsThingLsThingState>().use(null, ItxLsThingLsThingState.class)
                .use(BigDecimal.class, new CustomBigDecimalFactory())
                .deserialize(json);
    }

    @Transactional
    public static String toJsonArray(Collection<ItxLsThingLsThingState> collection) {
        return new JSONSerializer().exclude("*.class")
                .transform(new ExcludeNulls(), void.class)
                .serialize(collection);
    }

    @Transactional
    public static String toJsonArrayStub(Collection<ItxLsThingLsThingState> collection) {
        return new JSONSerializer().exclude("*.class", "itxLsThingLsThing")
                .transform(new ExcludeNulls(), void.class)
                .serialize(collection);
    }

    public static Collection<ItxLsThingLsThingState> fromJsonArrayToItxLsThingLsThingStates(String json) {
        return new JSONDeserializer<List<ItxLsThingLsThingState>>().use(null, ArrayList.class)
                .use("values", ItxLsThingLsThingState.class)
                .use(BigDecimal.class, new CustomBigDecimalFactory())
                .deserialize(json);
    }

    public static Collection<ItxLsThingLsThingState> fromJsonArrayToItxLsThingLsThingStates(Reader json) {
        return new JSONDeserializer<List<ItxLsThingLsThingState>>().use(null, ArrayList.class)
                .use("values", ItxLsThingLsThingState.class)
                .use(BigDecimal.class, new CustomBigDecimalFactory())
                .deserialize(json);
    }

    public ItxLsThingLsThingState() {
        super();
    }

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger",
            "itxLsThingLsThing", "lsValues");

    public static long countItxLsThingLsThingStates() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ItxLsThingLsThingState o", Long.class)
                .getSingleResult();
    }

    public static List<ItxLsThingLsThingState> findAllItxLsThingLsThingStates() {
        return entityManager().createQuery("SELECT o FROM ItxLsThingLsThingState o", ItxLsThingLsThingState.class)
                .getResultList();
    }

    public static List<ItxLsThingLsThingState> findAllItxLsThingLsThingStates(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ItxLsThingLsThingState o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ItxLsThingLsThingState.class).getResultList();
    }

    public static ItxLsThingLsThingState findItxLsThingLsThingState(Long id) {
        if (id == null)
            return null;
        return entityManager().find(ItxLsThingLsThingState.class, id);
    }

    public static List<ItxLsThingLsThingState> findItxLsThingLsThingStateEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ItxLsThingLsThingState o", ItxLsThingLsThingState.class)
                .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static List<ItxLsThingLsThingState> findItxLsThingLsThingStateEntries(int firstResult, int maxResults,
            String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ItxLsThingLsThingState o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ItxLsThingLsThingState.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public ItxLsThingLsThingState merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        ItxLsThingLsThingState merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public ItxLsThingLsThing getItxLsThingLsThing() {
        return this.itxLsThingLsThing;
    }

    public void setItxLsThingLsThing(ItxLsThingLsThing itxLsThingLsThing) {
        this.itxLsThingLsThing = itxLsThingLsThing;
    }

    public Set<ItxLsThingLsThingValue> getLsValues() {
        return this.lsValues;
    }

    public void setLsValues(Set<ItxLsThingLsThingValue> lsValues) {
        this.lsValues = lsValues;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
