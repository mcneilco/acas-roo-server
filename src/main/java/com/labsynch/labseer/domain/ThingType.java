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

public class ThingType {

	private static final Logger logger = LoggerFactory.getLogger(ThingType.class);

	
	@NotNull
	@Column(unique = true)
	@Size(max = 128)
	private String typeName;

	public static ThingType getOrCreate(String name) {

		ThingType thingType = null;
		List<ThingType> thingTypes = ThingType.findThingTypesByTypeNameEquals(name).getResultList();
		if (thingTypes.size() == 0){
			thingType = new ThingType();
			thingType.setTypeName(name);
			thingType.persist();
		} else if (thingTypes.size() == 1){
			thingType = thingTypes.get(0);
		} else if (thingTypes.size() > 1){
			logger.error("ERROR: multiple thing types with the same name");
		}
		

		return thingType;
	}

	@Id
    @SequenceGenerator(name = "thingTypeGen", sequenceName = "THING_TYPE_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "thingTypeGen")
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

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "typeName", "id", "version");

	public static final EntityManager entityManager() {
        EntityManager em = new ThingType().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countThingTypes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ThingType o", Long.class).getSingleResult();
    }

	public static List<ThingType> findAllThingTypes() {
        return entityManager().createQuery("SELECT o FROM ThingType o", ThingType.class).getResultList();
    }

	public static List<ThingType> findAllThingTypes(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ThingType o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ThingType.class).getResultList();
    }

	public static ThingType findThingType(Long id) {
        if (id == null) return null;
        return entityManager().find(ThingType.class, id);
    }

	public static List<ThingType> findThingTypeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ThingType o", ThingType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<ThingType> findThingTypeEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ThingType o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ThingType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            ThingType attached = ThingType.findThingType(this.id);
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
    public ThingType merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ThingType merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static Long countFindThingTypesByTypeNameEquals(String typeName) {
        if (typeName == null || typeName.length() == 0) throw new IllegalArgumentException("The typeName argument is required");
        EntityManager em = ThingType.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ThingType AS o WHERE o.typeName = :typeName", Long.class);
        q.setParameter("typeName", typeName);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<ThingType> findThingTypesByTypeNameEquals(String typeName) {
        if (typeName == null || typeName.length() == 0) throw new IllegalArgumentException("The typeName argument is required");
        EntityManager em = ThingType.entityManager();
        TypedQuery<ThingType> q = em.createQuery("SELECT o FROM ThingType AS o WHERE o.typeName = :typeName", ThingType.class);
        q.setParameter("typeName", typeName);
        return q;
    }

	public static TypedQuery<ThingType> findThingTypesByTypeNameEquals(String typeName, String sortFieldName, String sortOrder) {
        if (typeName == null || typeName.length() == 0) throw new IllegalArgumentException("The typeName argument is required");
        EntityManager em = ThingType.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ThingType AS o WHERE o.typeName = :typeName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ThingType> q = em.createQuery(queryBuilder.toString(), ThingType.class);
        q.setParameter("typeName", typeName);
        return q;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static ThingType fromJsonToThingType(String json) {
        return new JSONDeserializer<ThingType>()
        .use(null, ThingType.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ThingType> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<ThingType> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<ThingType> fromJsonArrayToThingTypes(String json) {
        return new JSONDeserializer<List<ThingType>>()
        .use("values", ThingType.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getTypeName() {
        return this.typeName;
    }

	public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
