package com.labsynch.labseer.domain;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
public class ParentAliasKind {
	
    @NotNull
    @ManyToOne
    private ParentAliasType lsType;

    @Size(max = 255)
    private String kindName;
    
    

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

	@Version
    @Column(name = "version")
    private Integer version;

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }

	public ParentAliasType getLsType() {
        return this.lsType;
    }

	public void setLsType(ParentAliasType lsType) {
        this.lsType = lsType;
    }

	public String getKindName() {
        return this.kindName;
    }

	public void setKindName(String kindName) {
        this.kindName = kindName;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static ParentAliasKind fromJsonToParentAliasKind(String json) {
        return new JSONDeserializer<ParentAliasKind>()
        .use(null, ParentAliasKind.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ParentAliasKind> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<ParentAliasKind> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<ParentAliasKind> fromJsonArrayToParentAliasKinds(String json) {
        return new JSONDeserializer<List<ParentAliasKind>>()
        .use("values", ParentAliasKind.class).deserialize(json);
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("lsType", "kindName");

	public static final EntityManager entityManager() {
        EntityManager em = new ParentAliasKind().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countParentAliasKinds() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ParentAliasKind o", Long.class).getSingleResult();
    }

	public static List<ParentAliasKind> findAllParentAliasKinds() {
        return entityManager().createQuery("SELECT o FROM ParentAliasKind o", ParentAliasKind.class).getResultList();
    }

	public static List<ParentAliasKind> findAllParentAliasKinds(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ParentAliasKind o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ParentAliasKind.class).getResultList();
    }

	public static ParentAliasKind findParentAliasKind(Long id) {
        if (id == null) return null;
        return entityManager().find(ParentAliasKind.class, id);
    }

	public static List<ParentAliasKind> findParentAliasKindEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ParentAliasKind o", ParentAliasKind.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<ParentAliasKind> findParentAliasKindEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ParentAliasKind o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ParentAliasKind.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            ParentAliasKind attached = ParentAliasKind.findParentAliasKind(this.id);
            this.entityManager.remove(attached);
        }
    }

	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

	@Transactional
    public ParentAliasKind merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ParentAliasKind merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
