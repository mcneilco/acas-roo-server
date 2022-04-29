package com.labsynch.labseer.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Configurable
@Entity
@Transactional
@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findIsoSaltsBySaltForm", "findIsoSaltsBySaltFormAndType" })
public class IsoSalt {

	private static final Logger logger = LoggerFactory.getLogger(IsoSalt.class);

    @ManyToOne
    @JoinColumn(name = "isotope")
    private Isotope isotope;

    @ManyToOne
    @JoinColumn(name = "salt")
    private Salt salt;

    @Size(max = 25)
    private String type;

    private Double equivalents;

    private Boolean ignore;

    @ManyToOne
    @JoinColumn(name = "salt_form")
    private SaltForm saltForm;

    
    public String toJson() {
        return new JSONSerializer().exclude("*.class", "saltForm").serialize(this);
    }

    public static IsoSalt fromJsonToIsoSalt(String json) {
        return new JSONDeserializer<IsoSalt>().use(null, IsoSalt.class).deserialize(json);
    }

    public static String toJsonArray(Collection<IsoSalt> collection) {
        return new JSONSerializer().exclude("*.class", "saltForm").serialize(collection);
    }

    public static Collection<IsoSalt> fromJsonArrayToIsoSalts(String json) {
        return new JSONDeserializer<List<IsoSalt>>().use(null, ArrayList.class).use("values", IsoSalt.class).deserialize(json);
    }
    

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "isotope", "salt", "type", "equivalents", "ignore", "saltForm");

	public static final EntityManager entityManager() {
        EntityManager em = new IsoSalt().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countIsoSalts() {
        return entityManager().createQuery("SELECT COUNT(o) FROM IsoSalt o", Long.class).getSingleResult();
    }

	public static List<IsoSalt> findAllIsoSalts() {
        return entityManager().createQuery("SELECT o FROM IsoSalt o", IsoSalt.class).getResultList();
    }

	public static List<IsoSalt> findAllIsoSalts(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM IsoSalt o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, IsoSalt.class).getResultList();
    }

	public static IsoSalt findIsoSalt(Long id) {
        if (id == null) return null;
        return entityManager().find(IsoSalt.class, id);
    }

	public static List<IsoSalt> findIsoSaltEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM IsoSalt o", IsoSalt.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<IsoSalt> findIsoSaltEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM IsoSalt o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, IsoSalt.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            IsoSalt attached = IsoSalt.findIsoSalt(this.id);
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
    public IsoSalt merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        IsoSalt merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public Isotope getIsotope() {
        return this.isotope;
    }

	public void setIsotope(Isotope isotope) {
        this.isotope = isotope;
    }

	public Salt getSalt() {
        return this.salt;
    }

	public void setSalt(Salt salt) {
        this.salt = salt;
    }

	public String getType() {
        return this.type;
    }

	public void setType(String type) {
        this.type = type;
    }

	public Double getEquivalents() {
        return this.equivalents;
    }

	public void setEquivalents(Double equivalents) {
        this.equivalents = equivalents;
    }

	public Boolean getIgnore() {
        return this.ignore;
    }

	public void setIgnore(Boolean ignore) {
        this.ignore = ignore;
    }

	public SaltForm getSaltForm() {
        return this.saltForm;
    }

	public void setSaltForm(SaltForm saltForm) {
        this.saltForm = saltForm;
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

	public static Long countFindIsoSaltsBySaltForm(SaltForm saltForm) {
        if (saltForm == null) throw new IllegalArgumentException("The saltForm argument is required");
        EntityManager em = IsoSalt.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM IsoSalt AS o WHERE o.saltForm = :saltForm", Long.class);
        q.setParameter("saltForm", saltForm);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindIsoSaltsBySaltFormAndType(SaltForm saltForm, String type) {
        if (saltForm == null) throw new IllegalArgumentException("The saltForm argument is required");
        if (type == null || type.length() == 0) throw new IllegalArgumentException("The type argument is required");
        EntityManager em = IsoSalt.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM IsoSalt AS o WHERE o.saltForm = :saltForm AND o.type = :type", Long.class);
        q.setParameter("saltForm", saltForm);
        q.setParameter("type", type);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<IsoSalt> findIsoSaltsBySaltForm(SaltForm saltForm) {
        if (saltForm == null) throw new IllegalArgumentException("The saltForm argument is required");
        EntityManager em = IsoSalt.entityManager();
        TypedQuery<IsoSalt> q = em.createQuery("SELECT o FROM IsoSalt AS o WHERE o.saltForm = :saltForm", IsoSalt.class);
        q.setParameter("saltForm", saltForm);
        return q;
    }

	public static TypedQuery<IsoSalt> findIsoSaltsBySaltForm(SaltForm saltForm, String sortFieldName, String sortOrder) {
        if (saltForm == null) throw new IllegalArgumentException("The saltForm argument is required");
        EntityManager em = IsoSalt.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM IsoSalt AS o WHERE o.saltForm = :saltForm");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<IsoSalt> q = em.createQuery(queryBuilder.toString(), IsoSalt.class);
        q.setParameter("saltForm", saltForm);
        return q;
    }

	public static TypedQuery<IsoSalt> findIsoSaltsBySaltFormAndType(SaltForm saltForm, String type) {
        if (saltForm == null) throw new IllegalArgumentException("The saltForm argument is required");
        if (type == null || type.length() == 0) throw new IllegalArgumentException("The type argument is required");
        EntityManager em = IsoSalt.entityManager();
        TypedQuery<IsoSalt> q = em.createQuery("SELECT o FROM IsoSalt AS o WHERE o.saltForm = :saltForm AND o.type = :type", IsoSalt.class);
        q.setParameter("saltForm", saltForm);
        q.setParameter("type", type);
        return q;
    }

	public static TypedQuery<IsoSalt> findIsoSaltsBySaltFormAndType(SaltForm saltForm, String type, String sortFieldName, String sortOrder) {
        if (saltForm == null) throw new IllegalArgumentException("The saltForm argument is required");
        if (type == null || type.length() == 0) throw new IllegalArgumentException("The type argument is required");
        EntityManager em = IsoSalt.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM IsoSalt AS o WHERE o.saltForm = :saltForm AND o.type = :type");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<IsoSalt> q = em.createQuery(queryBuilder.toString(), IsoSalt.class);
        q.setParameter("saltForm", saltForm);
        q.setParameter("type", type);
        return q;
    }
}
