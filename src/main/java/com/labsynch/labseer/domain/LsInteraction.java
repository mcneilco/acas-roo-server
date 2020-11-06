package com.labsynch.labseer.domain;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
@RooJavaBean
@RooToString
@RooJpaActiveRecord
@RooJson
public class LsInteraction extends AbstractThing {

    @NotNull
    @Column(name = "first_thing_id")
    private Long firstThing;

    @NotNull
    @Column(name = "second_thing_id")
    private Long secondThing;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("firstThing", "secondThing");

	public static long countLsInteractions() {
        return entityManager().createQuery("SELECT COUNT(o) FROM LsInteraction o", Long.class).getSingleResult();
    }

	public static List<LsInteraction> findAllLsInteractions() {
        return entityManager().createQuery("SELECT o FROM LsInteraction o", LsInteraction.class).getResultList();
    }

	public static List<LsInteraction> findAllLsInteractions(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM LsInteraction o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LsInteraction.class).getResultList();
    }

	public static LsInteraction findLsInteraction(Long id) {
        if (id == null) return null;
        return entityManager().find(LsInteraction.class, id);
    }

	public static List<LsInteraction> findLsInteractionEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM LsInteraction o", LsInteraction.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<LsInteraction> findLsInteractionEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM LsInteraction o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LsInteraction.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public LsInteraction merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        LsInteraction merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static LsInteraction fromJsonToLsInteraction(String json) {
        return new JSONDeserializer<LsInteraction>()
        .use(null, LsInteraction.class).deserialize(json);
    }

	public static String toJsonArray(Collection<LsInteraction> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<LsInteraction> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<LsInteraction> fromJsonArrayToLsInteractions(String json) {
        return new JSONDeserializer<List<LsInteraction>>()
        .use("values", LsInteraction.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public Long getFirstThing() {
        return this.firstThing;
    }

	public void setFirstThing(Long firstThing) {
        this.firstThing = firstThing;
    }

	public Long getSecondThing() {
        return this.secondThing;
    }

	public void setSecondThing(Long secondThing) {
        this.secondThing = secondThing;
    }
}
