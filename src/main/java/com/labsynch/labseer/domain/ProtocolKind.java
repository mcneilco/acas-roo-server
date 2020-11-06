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
public class ProtocolKind {

    @Id
    @SequenceGenerator(name = "protocolKindGen", sequenceName = "PROTOCOL_KIND_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "protocolKindGen")
    @Column(name = "id")
    private Long id;

    @Version
    @Column(name = "version")
    private Integer version;

    @NotNull
    @ManyToOne
    private ProtocolType lsType;

    @NotNull
    @Size(max = 255)
    private String kindName;

    @Size(max = 255)
    @Column(unique = true)
    private String lsTypeAndKind;

    @PersistenceContext
    transient EntityManager entityManager;

    public static final EntityManager entityManager() {
        EntityManager em = new ProtocolKind().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countProtocolKinds() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ProtocolKind o", Long.class).getSingleResult();
    }

    public static List<com.labsynch.labseer.domain.ProtocolKind> findAllProtocolKinds() {
        return entityManager().createQuery("SELECT o FROM ProtocolKind o", ProtocolKind.class).getResultList();
    }

    public static com.labsynch.labseer.domain.ProtocolKind findProtocolKind(Long id) {
        if (id == null) return null;
        return entityManager().find(ProtocolKind.class, id);
    }

    public static List<com.labsynch.labseer.domain.ProtocolKind> findProtocolKindEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ProtocolKind o", ProtocolKind.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.lsType = ProtocolType.findProtocolType(this.getLsType().getId());
        this.lsTypeAndKind = new StringBuilder().append(this.getLsType().getTypeName()).append('_').append(this.getKindName()).toString();
        this.entityManager.persist(this);
    }

    @Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            ProtocolKind attached = ProtocolKind.findProtocolKind(this.id);
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
    public com.labsynch.labseer.domain.ProtocolKind merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.lsTypeAndKind = new StringBuilder().append(this.getLsType().getTypeName()).append('_').append(this.getKindName()).toString();
        ProtocolKind merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static Long countFindProtocolKindsByLsTypeEqualsAndKindNameEquals(ProtocolType lsType, String kindName) {
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        EntityManager em = ProtocolKind.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ProtocolKind AS o WHERE o.lsType = :lsType  AND o.kindName = :kindName", Long.class);
        q.setParameter("lsType", lsType);
        q.setParameter("kindName", kindName);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<ProtocolKind> findProtocolKindsByLsTypeEqualsAndKindNameEquals(ProtocolType lsType, String kindName) {
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        EntityManager em = ProtocolKind.entityManager();
        TypedQuery<ProtocolKind> q = em.createQuery("SELECT o FROM ProtocolKind AS o WHERE o.lsType = :lsType  AND o.kindName = :kindName", ProtocolKind.class);
        q.setParameter("lsType", lsType);
        q.setParameter("kindName", kindName);
        return q;
    }

	public static TypedQuery<ProtocolKind> findProtocolKindsByLsTypeEqualsAndKindNameEquals(ProtocolType lsType, String kindName, String sortFieldName, String sortOrder) {
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        EntityManager em = ProtocolKind.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ProtocolKind AS o WHERE o.lsType = :lsType  AND o.kindName = :kindName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ProtocolKind> q = em.createQuery(queryBuilder.toString(), ProtocolKind.class);
        q.setParameter("lsType", lsType);
        q.setParameter("kindName", kindName);
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

	public static ProtocolKind fromJsonToProtocolKind(String json) {
        return new JSONDeserializer<ProtocolKind>()
        .use(null, ProtocolKind.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ProtocolKind> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<ProtocolKind> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<ProtocolKind> fromJsonArrayToProtocolKinds(String json) {
        return new JSONDeserializer<List<ProtocolKind>>()
        .use("values", ProtocolKind.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

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

	public ProtocolType getLsType() {
        return this.lsType;
    }

	public void setLsType(ProtocolType lsType) {
        this.lsType = lsType;
    }

	public String getKindName() {
        return this.kindName;
    }

	public void setKindName(String kindName) {
        this.kindName = kindName;
    }

	public String getLsTypeAndKind() {
        return this.lsTypeAndKind;
    }

	public void setLsTypeAndKind(String lsTypeAndKind) {
        this.lsTypeAndKind = lsTypeAndKind;
    }

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("id", "version", "lsType", "kindName", "lsTypeAndKind", "entityManager");

	public static List<ProtocolKind> findAllProtocolKinds(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ProtocolKind o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ProtocolKind.class).getResultList();
    }

	public static List<ProtocolKind> findProtocolKindEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ProtocolKind o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ProtocolKind.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
}
