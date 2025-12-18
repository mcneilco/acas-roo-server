package com.labsynch.labseer.domain;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import com.labsynch.labseer.utils.ExcludeNulls;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Configurable
@Entity

public class ItxProtocolProtocolValue extends AbstractValue {

    @ManyToOne
    @JoinColumn(name = "ls_state")
    private ItxProtocolProtocolState lsState;

    public static ItxProtocolProtocolValue update(ItxProtocolProtocolValue object) {
        ItxProtocolProtocolValue updatedObject = new JSONDeserializer<ItxProtocolProtocolValue>()
                .use(null, ItxProtocolProtocolValue.class).deserializeInto(object.toJson(),
                        ItxProtocolProtocolValue.findItxProtocolProtocolValue(object.getId()));
        updatedObject.setModifiedDate(new Date());
        updatedObject.merge();
        return updatedObject;
    }

    public static ItxProtocolProtocolValue fromJsonToItxProtocolProtocolValue(String json) {
        return new JSONDeserializer<ItxProtocolProtocolValue>().use(null, ItxProtocolProtocolValue.class).

                deserialize(json);
    }

    public static Collection<ItxProtocolProtocolValue> fromJsonArrayToItxProtocolProtocolValues(String json) {
        return new JSONDeserializer<List<ItxProtocolProtocolValue>>().use(null, ArrayList.class)
                .use("values", ItxProtocolProtocolValue.class).

                deserialize(json);
    }

    public static Collection<ItxProtocolProtocolValue> fromJsonArrayToItxProtocolProtocolValues(Reader json) {
        return new JSONDeserializer<List<ItxProtocolProtocolValue>>().use(null, ArrayList.class)
                .use("values", ItxProtocolProtocolValue.class).

                deserialize(json);
    }

    @Transactional
    public String toJson() {
        return new JSONSerializer().exclude("*.class")
                .transform(new ExcludeNulls(), void.class)
                .serialize(this);
    }

    @Transactional
    public static String toJsonArray(Collection<ItxProtocolProtocolValue> collection) {
        return new JSONSerializer().exclude("*.class")
                .transform(new ExcludeNulls(), void.class)
                .serialize(collection);
    }

    @Transactional
    public static String toJsonArrayStub(Collection<ItxProtocolProtocolValue> collection) {
        return new JSONSerializer().exclude("*.class", "lsState")
                .transform(new ExcludeNulls(), void.class)
                .serialize(collection);
    }

    public static ItxProtocolProtocolValue create(ItxProtocolProtocolValue lsThingValue) {
        ItxProtocolProtocolValue newItxProtocolProtocolValue = new JSONDeserializer<ItxProtocolProtocolValue>()
                .use(null, ItxProtocolProtocolValue.class)
                .deserializeInto(lsThingValue.toJson(), new ItxProtocolProtocolValue());
        return newItxProtocolProtocolValue;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("lsState");

    public static long countItxProtocolProtocolValues() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ItxProtocolProtocolValue o", Long.class)
                .getSingleResult();
    }

    public static List<ItxProtocolProtocolValue> findAllItxProtocolProtocolValues() {
        return entityManager().createQuery("SELECT o FROM ItxProtocolProtocolValue o", ItxProtocolProtocolValue.class)
                .getResultList();
    }

    public static List<ItxProtocolProtocolValue> findAllItxProtocolProtocolValues(String sortFieldName,
            String sortOrder) {
        String jpaQuery = "SELECT o FROM ItxProtocolProtocolValue o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ItxProtocolProtocolValue.class).getResultList();
    }

    public static ItxProtocolProtocolValue findItxProtocolProtocolValue(Long id) {
        if (id == null)
            return null;
        return entityManager().find(ItxProtocolProtocolValue.class, id);
    }

    public static List<ItxProtocolProtocolValue> findItxProtocolProtocolValueEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ItxProtocolProtocolValue o", ItxProtocolProtocolValue.class)
                .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static List<ItxProtocolProtocolValue> findItxProtocolProtocolValueEntries(int firstResult, int maxResults,
            String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ItxProtocolProtocolValue o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ItxProtocolProtocolValue.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public ItxProtocolProtocolValue merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        ItxProtocolProtocolValue merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public ItxProtocolProtocolState getLsState() {
        return this.lsState;
    }

    public void setLsState(ItxProtocolProtocolState lsState) {
        this.lsState = lsState;
    }
}
