package com.labsynch.labseer.domain;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
@Transactional
@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findCompoundsByCdId", "findCompoundsByExternal_idEquals" })
public class Compound {

    private static final Logger logger = LoggerFactory.getLogger(Compound.class);

    @Size(max = 255)
    private String corpName;

    @Size(max = 255)
    private String external_id;

    private int CdId;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    private Date modifiedDate;

    private Boolean ignored;

    private Boolean deleted;

	public static Long countFindCompoundsByCdId(int CdId) {
        EntityManager em = Compound.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Compound AS o WHERE o.CdId = :CdId", Long.class);
        q.setParameter("CdId", CdId);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindCompoundsByExternal_idEquals(String external_id) {
        if (external_id == null || external_id.length() == 0) throw new IllegalArgumentException("The external_id argument is required");
        EntityManager em = Compound.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Compound AS o WHERE o.external_id = :external_id", Long.class);
        q.setParameter("external_id", external_id);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<Compound> findCompoundsByCdId(int CdId) {
        EntityManager em = Compound.entityManager();
        TypedQuery<Compound> q = em.createQuery("SELECT o FROM Compound AS o WHERE o.CdId = :CdId", Compound.class);
        q.setParameter("CdId", CdId);
        return q;
    }

	public static TypedQuery<Compound> findCompoundsByCdId(int CdId, String sortFieldName, String sortOrder) {
        EntityManager em = Compound.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Compound AS o WHERE o.CdId = :CdId");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Compound> q = em.createQuery(queryBuilder.toString(), Compound.class);
        q.setParameter("CdId", CdId);
        return q;
    }

	public static TypedQuery<Compound> findCompoundsByExternal_idEquals(String external_id) {
        if (external_id == null || external_id.length() == 0) throw new IllegalArgumentException("The external_id argument is required");
        EntityManager em = Compound.entityManager();
        TypedQuery<Compound> q = em.createQuery("SELECT o FROM Compound AS o WHERE o.external_id = :external_id", Compound.class);
        q.setParameter("external_id", external_id);
        return q;
    }

	public static TypedQuery<Compound> findCompoundsByExternal_idEquals(String external_id, String sortFieldName, String sortOrder) {
        if (external_id == null || external_id.length() == 0) throw new IllegalArgumentException("The external_id argument is required");
        EntityManager em = Compound.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Compound AS o WHERE o.external_id = :external_id");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Compound> q = em.createQuery(queryBuilder.toString(), Compound.class);
        q.setParameter("external_id", external_id);
        return q;
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "corpName", "external_id", "CdId", "createdDate", "modifiedDate", "ignored", "deleted");

	public static final EntityManager entityManager() {
        EntityManager em = new Compound().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countCompounds() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Compound o", Long.class).getSingleResult();
    }

	public static List<Compound> findAllCompounds() {
        return entityManager().createQuery("SELECT o FROM Compound o", Compound.class).getResultList();
    }

	public static List<Compound> findAllCompounds(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Compound o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Compound.class).getResultList();
    }

	public static Compound findCompound(Long id) {
        if (id == null) return null;
        return entityManager().find(Compound.class, id);
    }

	public static List<Compound> findCompoundEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Compound o", Compound.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Compound> findCompoundEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Compound o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Compound.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Compound attached = Compound.findCompound(this.id);
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
    public Compound merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Compound merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String getCorpName() {
        return this.corpName;
    }

	public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

	public String getExternal_id() {
        return this.external_id;
    }

	public void setExternal_id(String external_id) {
        this.external_id = external_id;
    }

	public int getCdId() {
        return this.CdId;
    }

	public void setCdId(int CdId) {
        this.CdId = CdId;
    }

	public Date getCreatedDate() {
        return this.createdDate;
    }

	public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

	public Date getModifiedDate() {
        return this.modifiedDate;
    }

	public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

	public Boolean getIgnored() {
        return this.ignored;
    }

	public void setIgnored(Boolean ignored) {
        this.ignored = ignored;
    }

	public Boolean getDeleted() {
        return this.deleted;
    }

	public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static Compound fromJsonToCompound(String json) {
        return new JSONDeserializer<Compound>()
        .use(null, Compound.class).deserialize(json);
    }

	public static String toJsonArray(Collection<Compound> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<Compound> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<Compound> fromJsonArrayToCompounds(String json) {
        return new JSONDeserializer<List<Compound>>()
        .use("values", Compound.class).deserialize(json);
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

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
