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
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
public class ContainerType {

    @NotNull
    @Column(unique = true)
    @Size(max = 128)
    private String typeName;

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("typeName");

	public static final EntityManager entityManager() {
        EntityManager em = new ContainerType().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countContainerTypes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ContainerType o", Long.class).getSingleResult();
    }

	public static List<ContainerType> findAllContainerTypes() {
        return entityManager().createQuery("SELECT o FROM ContainerType o", ContainerType.class).getResultList();
    }

	public static List<ContainerType> findAllContainerTypes(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ContainerType o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ContainerType.class).getResultList();
    }

	public static ContainerType findContainerType(Long id) {
        if (id == null) return null;
        return entityManager().find(ContainerType.class, id);
    }

	public static List<ContainerType> findContainerTypeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ContainerType o", ContainerType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<ContainerType> findContainerTypeEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ContainerType o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ContainerType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            ContainerType attached = ContainerType.findContainerType(this.id);
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
    public ContainerType merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ContainerType merged = this.entityManager.merge(this);
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

	public static ContainerType fromJsonToContainerType(String json) {
        return new JSONDeserializer<ContainerType>()
        .use(null, ContainerType.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ContainerType> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<ContainerType> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<ContainerType> fromJsonArrayToContainerTypes(String json) {
        return new JSONDeserializer<List<ContainerType>>()
        .use("values", ContainerType.class).deserialize(json);
    }

	public static Long countFindContainerTypesByTypeNameEquals(String typeName) {
        if (typeName == null || typeName.length() == 0) throw new IllegalArgumentException("The typeName argument is required");
        EntityManager em = ContainerType.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ContainerType AS o WHERE o.typeName = :typeName", Long.class);
        q.setParameter("typeName", typeName);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<ContainerType> findContainerTypesByTypeNameEquals(String typeName) {
        if (typeName == null || typeName.length() == 0) throw new IllegalArgumentException("The typeName argument is required");
        EntityManager em = ContainerType.entityManager();
        TypedQuery<ContainerType> q = em.createQuery("SELECT o FROM ContainerType AS o WHERE o.typeName = :typeName", ContainerType.class);
        q.setParameter("typeName", typeName);
        return q;
    }

	public static TypedQuery<ContainerType> findContainerTypesByTypeNameEquals(String typeName, String sortFieldName, String sortOrder) {
        if (typeName == null || typeName.length() == 0) throw new IllegalArgumentException("The typeName argument is required");
        EntityManager em = ContainerType.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ContainerType AS o WHERE o.typeName = :typeName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ContainerType> q = em.createQuery(queryBuilder.toString(), ContainerType.class);
        q.setParameter("typeName", typeName);
        return q;
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

	@Id
    @SequenceGenerator(name = "containerTypeGen", sequenceName = "CONTAINER_TYPE_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "containerTypeGen")
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
}
