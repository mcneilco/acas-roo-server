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
import javax.persistence.PersistenceContext;
import javax.persistence.SequenceGenerator;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity

public class LabelType {

	private static final Logger logger = LoggerFactory.getLogger(LabelType.class);

    @NotNull
    @Column(unique = true)
    @Size(max = 255)
    private String typeName;
    
	public static LabelType getOrCreate(String name) {

		LabelType lsType = null;
		List<LabelType> lsTypes = LabelType.findLabelTypesByTypeNameEquals(name).getResultList();
		if (lsTypes.size() == 0){
			lsType = new LabelType();
			lsType.setTypeName(name);
			lsType.persist();
		} else if (lsTypes.size() == 1){
			lsType = lsTypes.get(0);
		} else if (lsTypes.size() > 1){
			logger.error("ERROR: multiple Label types with the same name");
		}

		return lsType;
	}

	@Id
    @SequenceGenerator(name = "labelTypeGen", sequenceName = "LABEL_TYPE_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "labelTypeGen")
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

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "typeName", "id", "version");

	public static final EntityManager entityManager() {
        EntityManager em = new LabelType().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countLabelTypes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM LabelType o", Long.class).getSingleResult();
    }

	public static List<LabelType> findAllLabelTypes() {
        return entityManager().createQuery("SELECT o FROM LabelType o", LabelType.class).getResultList();
    }

	public static List<LabelType> findAllLabelTypes(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM LabelType o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LabelType.class).getResultList();
    }

	public static LabelType findLabelType(Long id) {
        if (id == null) return null;
        return entityManager().find(LabelType.class, id);
    }

	public static List<LabelType> findLabelTypeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM LabelType o", LabelType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<LabelType> findLabelTypeEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM LabelType o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LabelType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            LabelType attached = LabelType.findLabelType(this.id);
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
    public LabelType merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        LabelType merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String getTypeName() {
        return this.typeName;
    }

	public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static LabelType fromJsonToLabelType(String json) {
        return new JSONDeserializer<LabelType>()
        .use(null, LabelType.class).deserialize(json);
    }

	public static String toJsonArray(Collection<LabelType> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<LabelType> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<LabelType> fromJsonArrayToLabelTypes(String json) {
        return new JSONDeserializer<List<LabelType>>()
        .use("values", LabelType.class).deserialize(json);
    }

	public static Long countFindLabelTypesByTypeNameEquals(String typeName) {
        if (typeName == null || typeName.length() == 0) throw new IllegalArgumentException("The typeName argument is required");
        EntityManager em = LabelType.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM LabelType AS o WHERE o.typeName = :typeName", Long.class);
        q.setParameter("typeName", typeName);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<LabelType> findLabelTypesByTypeNameEquals(String typeName) {
        if (typeName == null || typeName.length() == 0) throw new IllegalArgumentException("The typeName argument is required");
        EntityManager em = LabelType.entityManager();
        TypedQuery<LabelType> q = em.createQuery("SELECT o FROM LabelType AS o WHERE o.typeName = :typeName", LabelType.class);
        q.setParameter("typeName", typeName);
        return q;
    }

	public static TypedQuery<LabelType> findLabelTypesByTypeNameEquals(String typeName, String sortFieldName, String sortOrder) {
        if (typeName == null || typeName.length() == 0) throw new IllegalArgumentException("The typeName argument is required");
        EntityManager em = LabelType.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM LabelType AS o WHERE o.typeName = :typeName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LabelType> q = em.createQuery(queryBuilder.toString(), LabelType.class);
        q.setParameter("typeName", typeName);
        return q;
    }
}
