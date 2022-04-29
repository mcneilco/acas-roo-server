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
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
@RooJavaBean
@RooToString
@RooJpaActiveRecord(sequenceName = "PROTOCOL_TYPE_PKSEQ", finders = { "findProtocolTypesByTypeNameEquals" })
@RooJson
public class ProtocolType {

    @NotNull
    @Column(unique = true)
    @Size(max = 128)
    private String typeName;

	@Id
    @SequenceGenerator(name = "protocolTypeGen", sequenceName = "PROTOCOL_TYPE_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "protocolTypeGen")
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

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("typeName", "id", "version");

	public static final EntityManager entityManager() {
        EntityManager em = new ProtocolType().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countProtocolTypes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ProtocolType o", Long.class).getSingleResult();
    }

	public static List<ProtocolType> findAllProtocolTypes() {
        return entityManager().createQuery("SELECT o FROM ProtocolType o", ProtocolType.class).getResultList();
    }

	public static List<ProtocolType> findAllProtocolTypes(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ProtocolType o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ProtocolType.class).getResultList();
    }

	public static ProtocolType findProtocolType(Long id) {
        if (id == null) return null;
        return entityManager().find(ProtocolType.class, id);
    }

	public static List<ProtocolType> findProtocolTypeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ProtocolType o", ProtocolType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<ProtocolType> findProtocolTypeEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ProtocolType o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ProtocolType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            ProtocolType attached = ProtocolType.findProtocolType(this.id);
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
    public ProtocolType merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ProtocolType merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public static Long countFindProtocolTypesByTypeNameEquals(String typeName) {
        if (typeName == null || typeName.length() == 0) throw new IllegalArgumentException("The typeName argument is required");
        EntityManager em = ProtocolType.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ProtocolType AS o WHERE o.typeName = :typeName", Long.class);
        q.setParameter("typeName", typeName);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<ProtocolType> findProtocolTypesByTypeNameEquals(String typeName) {
        if (typeName == null || typeName.length() == 0) throw new IllegalArgumentException("The typeName argument is required");
        EntityManager em = ProtocolType.entityManager();
        TypedQuery<ProtocolType> q = em.createQuery("SELECT o FROM ProtocolType AS o WHERE o.typeName = :typeName", ProtocolType.class);
        q.setParameter("typeName", typeName);
        return q;
    }

	public static TypedQuery<ProtocolType> findProtocolTypesByTypeNameEquals(String typeName, String sortFieldName, String sortOrder) {
        if (typeName == null || typeName.length() == 0) throw new IllegalArgumentException("The typeName argument is required");
        EntityManager em = ProtocolType.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ProtocolType AS o WHERE o.typeName = :typeName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ProtocolType> q = em.createQuery(queryBuilder.toString(), ProtocolType.class);
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

	public static ProtocolType fromJsonToProtocolType(String json) {
        return new JSONDeserializer<ProtocolType>()
        .use(null, ProtocolType.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ProtocolType> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<ProtocolType> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<ProtocolType> fromJsonArrayToProtocolTypes(String json) {
        return new JSONDeserializer<List<ProtocolType>>()
        .use("values", ProtocolType.class).deserialize(json);
    }

	public String getTypeName() {
        return this.typeName;
    }

	public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
