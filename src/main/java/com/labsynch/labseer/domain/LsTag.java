package com.labsynch.labseer.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Configurable
@Entity
public class LsTag {

    @Size(max = 255)
    private String tagText;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MM")
    private Date recordedDate;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "lsTags")
    private Set<Experiment> experiments = new HashSet<Experiment>();

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "lsTags")
    private Set<Protocol> protocols = new HashSet<Protocol>();

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "lsTags")
    private Set<LsThing> lsThings = new HashSet<LsThing>();

    public LsTag(com.labsynch.labseer.domain.LsTag lsTag) {
        this.tagText = lsTag.getTagText();
        if (lsTag.getRecordedDate() == null) {
            this.recordedDate = new Date();
        } else {
            this.recordedDate = lsTag.getRecordedDate();
        }
    }

    public LsTag() {
    }

    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }

    public static com.labsynch.labseer.domain.LsTag fromJsonToLsTag(String json) {
        return new JSONDeserializer<LsTag>().use(null, LsTag.class).deserialize(json);
    }

    public static String toJsonArray(Collection<com.labsynch.labseer.domain.LsTag> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

    public static Collection<com.labsynch.labseer.domain.LsTag> fromJsonArrayToLsTags(String json) {
        return new JSONDeserializer<List<LsTag>>().use(null, ArrayList.class).use("values", LsTag.class)
                .deserialize(json);
    }

    public static Long countFindLsTagsByTagTextEquals(String tagText) {
        if (tagText == null || tagText.length() == 0)
            throw new IllegalArgumentException("The tagText argument is required");
        EntityManager em = LsTag.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM LsTag AS o WHERE o.tagText = :tagText", Long.class);
        q.setParameter("tagText", tagText);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindLsTagsByTagTextLike(String tagText) {
        if (tagText == null || tagText.length() == 0)
            throw new IllegalArgumentException("The tagText argument is required");
        tagText = tagText.replace('*', '%');
        if (tagText.charAt(0) != '%') {
            tagText = "%" + tagText;
        }
        if (tagText.charAt(tagText.length() - 1) != '%') {
            tagText = tagText + "%";
        }
        EntityManager em = LsTag.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM LsTag AS o WHERE LOWER(o.tagText) LIKE LOWER(:tagText)",
                Long.class);
        q.setParameter("tagText", tagText);
        return ((Long) q.getSingleResult());
    }

    public static TypedQuery<LsTag> findLsTagsByTagTextEquals(String tagText) {
        if (tagText == null || tagText.length() == 0)
            throw new IllegalArgumentException("The tagText argument is required");
        EntityManager em = LsTag.entityManager();
        TypedQuery<LsTag> q = em.createQuery("SELECT o FROM LsTag AS o WHERE o.tagText = :tagText", LsTag.class);
        q.setParameter("tagText", tagText);
        return q;
    }

    public static TypedQuery<LsTag> findLsTagsByTagTextEquals(String tagText, String sortFieldName, String sortOrder) {
        if (tagText == null || tagText.length() == 0)
            throw new IllegalArgumentException("The tagText argument is required");
        EntityManager em = LsTag.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM LsTag AS o WHERE o.tagText = :tagText");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LsTag> q = em.createQuery(queryBuilder.toString(), LsTag.class);
        q.setParameter("tagText", tagText);
        return q;
    }

    public static TypedQuery<LsTag> findLsTagsByTagTextLike(String tagText) {
        if (tagText == null || tagText.length() == 0)
            throw new IllegalArgumentException("The tagText argument is required");
        tagText = tagText.replace('*', '%');
        if (tagText.charAt(0) != '%') {
            tagText = "%" + tagText;
        }
        if (tagText.charAt(tagText.length() - 1) != '%') {
            tagText = tagText + "%";
        }
        EntityManager em = LsTag.entityManager();
        TypedQuery<LsTag> q = em.createQuery("SELECT o FROM LsTag AS o WHERE LOWER(o.tagText) LIKE LOWER(:tagText)",
                LsTag.class);
        q.setParameter("tagText", tagText);
        return q;
    }

    public static TypedQuery<LsTag> findLsTagsByTagTextLike(String tagText, String sortFieldName, String sortOrder) {
        if (tagText == null || tagText.length() == 0)
            throw new IllegalArgumentException("The tagText argument is required");
        tagText = tagText.replace('*', '%');
        if (tagText.charAt(0) != '%') {
            tagText = "%" + tagText;
        }
        if (tagText.charAt(tagText.length() - 1) != '%') {
            tagText = tagText + "%";
        }
        EntityManager em = LsTag.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM LsTag AS o WHERE LOWER(o.tagText) LIKE LOWER(:tagText)");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LsTag> q = em.createQuery(queryBuilder.toString(), LsTag.class);
        q.setParameter("tagText", tagText);
        return q;
    }

    public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .setExcludeFieldNames("experiments", "protocols", "recordedDate", "version").toString();
    }

    public String getTagText() {
        return this.tagText;
    }

    public void setTagText(String tagText) {
        this.tagText = tagText;
    }

    public Date getRecordedDate() {
        return this.recordedDate;
    }

    public void setRecordedDate(Date recordedDate) {
        this.recordedDate = recordedDate;
    }

    public Set<Experiment> getExperiments() {
        return this.experiments;
    }

    public void setExperiments(Set<Experiment> experiments) {
        this.experiments = experiments;
    }

    public Set<Protocol> getProtocols() {
        return this.protocols;
    }

    public void setProtocols(Set<Protocol> protocols) {
        this.protocols = protocols;
    }

    public Set<LsThing> getLsThings() {
        return this.lsThings;
    }

    public void setLsThings(Set<LsThing> lsThings) {
        this.lsThings = lsThings;
    }

    @Id
    @SequenceGenerator(name = "lsTagGen", sequenceName = "LS_TAG_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "lsTagGen")
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

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("tagText", "recordedDate",
            "experiments", "protocols", "lsThings");

    public static final EntityManager entityManager() {
        EntityManager em = new LsTag().entityManager;
        if (em == null)
            throw new IllegalStateException(
                    "Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countLsTags() {
        return entityManager().createQuery("SELECT COUNT(o) FROM LsTag o", Long.class).getSingleResult();
    }

    public static List<LsTag> findAllLsTags() {
        return entityManager().createQuery("SELECT o FROM LsTag o", LsTag.class).getResultList();
    }

    public static List<LsTag> findAllLsTags(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM LsTag o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LsTag.class).getResultList();
    }

    public static LsTag findLsTag(Long id) {
        if (id == null)
            return null;
        return entityManager().find(LsTag.class, id);
    }

    public static List<LsTag> findLsTagEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM LsTag o", LsTag.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    public static List<LsTag> findLsTagEntries(int firstResult, int maxResults, String sortFieldName,
            String sortOrder) {
        String jpaQuery = "SELECT o FROM LsTag o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LsTag.class).setFirstResult(firstResult).setMaxResults(maxResults)
                .getResultList();
    }

    @Transactional
    public void persist() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

    @Transactional
    public void remove() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            LsTag attached = LsTag.findLsTag(this.id);
            this.entityManager.remove(attached);
        }
    }

    @Transactional
    public void flush() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        this.entityManager.flush();
    }

    @Transactional
    public void clear() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        this.entityManager.clear();
    }

    @Transactional
    public LsTag merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        LsTag merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
