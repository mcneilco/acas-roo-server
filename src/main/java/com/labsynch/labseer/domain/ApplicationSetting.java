package com.labsynch.labseer.domain;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@Configurable

public class ApplicationSetting {

    // private static final Logger logger =
    // LoggerFactory.getLogger(ApplicationSetting.class);

    @Size(max = 255)
    private String propName;

    @Size(max = 255)
    private String propValue;

    @Size(max = 512)
    private String comments;

    private boolean ignored;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MM")
    private Date recordedDate;

    // @PostConstruct
    // public void init(){
    // logger.info("calling the appSetting 201 init method upon startup");
    // this.setPropName("testing201");
    // this.setPropValue("testingValue201");
    // this.persist();
    // }

    public static Long countFindApplicationSettingsByPropNameEquals(String propName) {
        if (propName == null || propName.length() == 0)
            throw new IllegalArgumentException("The propName argument is required");
        EntityManager em = ApplicationSetting.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ApplicationSetting AS o WHERE o.propName = :propName",
                Long.class);
        q.setParameter("propName", propName);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindApplicationSettingsByPropNameEqualsAndIgnoredNot(String propName, boolean ignored) {
        if (propName == null || propName.length() == 0)
            throw new IllegalArgumentException("The propName argument is required");
        EntityManager em = ApplicationSetting.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM ApplicationSetting AS o WHERE o.propName = :propName  AND o.ignored IS NOT :ignored",
                Long.class);
        q.setParameter("propName", propName);
        q.setParameter("ignored", ignored);
        return ((Long) q.getSingleResult());
    }

    public static TypedQuery<ApplicationSetting> findApplicationSettingsByPropNameEquals(String propName) {
        if (propName == null || propName.length() == 0)
            throw new IllegalArgumentException("The propName argument is required");
        EntityManager em = ApplicationSetting.entityManager();
        TypedQuery<ApplicationSetting> q = em.createQuery(
                "SELECT o FROM ApplicationSetting AS o WHERE o.propName = :propName", ApplicationSetting.class);
        q.setParameter("propName", propName);
        return q;
    }

    public static TypedQuery<ApplicationSetting> findApplicationSettingsByPropNameEquals(String propName,
            String sortFieldName, String sortOrder) {
        if (propName == null || propName.length() == 0)
            throw new IllegalArgumentException("The propName argument is required");
        EntityManager em = ApplicationSetting.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM ApplicationSetting AS o WHERE o.propName = :propName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ApplicationSetting> q = em.createQuery(queryBuilder.toString(), ApplicationSetting.class);
        q.setParameter("propName", propName);
        return q;
    }

    public static TypedQuery<ApplicationSetting> findApplicationSettingsByPropNameEqualsAndIgnoredNot(String propName,
            boolean ignored) {
        if (propName == null || propName.length() == 0)
            throw new IllegalArgumentException("The propName argument is required");
        EntityManager em = ApplicationSetting.entityManager();
        TypedQuery<ApplicationSetting> q = em.createQuery(
                "SELECT o FROM ApplicationSetting AS o WHERE o.propName = :propName  AND o.ignored IS NOT :ignored",
                ApplicationSetting.class);
        q.setParameter("propName", propName);
        q.setParameter("ignored", ignored);
        return q;
    }

    public static TypedQuery<ApplicationSetting> findApplicationSettingsByPropNameEqualsAndIgnoredNot(String propName,
            boolean ignored, String sortFieldName, String sortOrder) {
        if (propName == null || propName.length() == 0)
            throw new IllegalArgumentException("The propName argument is required");
        EntityManager em = ApplicationSetting.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM ApplicationSetting AS o WHERE o.propName = :propName  AND o.ignored IS NOT :ignored");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ApplicationSetting> q = em.createQuery(queryBuilder.toString(), ApplicationSetting.class);
        q.setParameter("propName", propName);
        q.setParameter("ignored", ignored);
        return q;
    }

    @PersistenceContext
    transient EntityManager entityManager;

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("propName", "propValue",
            "comments", "ignored", "recordedDate");

    public static final EntityManager entityManager() {
        EntityManager em = new ApplicationSetting().entityManager;
        if (em == null)
            throw new IllegalStateException(
                    "Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countApplicationSettings() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ApplicationSetting o", Long.class).getSingleResult();
    }

    public static List<ApplicationSetting> findAllApplicationSettings() {
        return entityManager().createQuery("SELECT o FROM ApplicationSetting o", ApplicationSetting.class)
                .getResultList();
    }

    public static List<ApplicationSetting> findAllApplicationSettings(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ApplicationSetting o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ApplicationSetting.class).getResultList();
    }

    public static ApplicationSetting findApplicationSetting(Long id) {
        if (id == null)
            return null;
        return entityManager().find(ApplicationSetting.class, id);
    }

    public static List<ApplicationSetting> findApplicationSettingEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ApplicationSetting o", ApplicationSetting.class)
                .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static List<ApplicationSetting> findApplicationSettingEntries(int firstResult, int maxResults,
            String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ApplicationSetting o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ApplicationSetting.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
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
            ApplicationSetting attached = ApplicationSetting.findApplicationSetting(this.id);
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
    public ApplicationSetting merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        ApplicationSetting merged = this.entityManager.merge(this);
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

    public static ApplicationSetting fromJsonToApplicationSetting(String json) {
        return new JSONDeserializer<ApplicationSetting>()
                .use(null, ApplicationSetting.class).deserialize(json);
    }

    public static String toJsonArray(Collection<ApplicationSetting> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<ApplicationSetting> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<ApplicationSetting> fromJsonArrayToApplicationSettings(String json) {
        return new JSONDeserializer<List<ApplicationSetting>>()
                .use("values", ApplicationSetting.class).deserialize(json);
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Id
    @SequenceGenerator(name = "applicationSettingGen", sequenceName = "APPSETTING_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "applicationSettingGen")
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

    public String getPropName() {
        return this.propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }

    public String getPropValue() {
        return this.propValue;
    }

    public void setPropValue(String propValue) {
        this.propValue = propValue;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isIgnored() {
        return this.ignored;
    }

    public void setIgnored(boolean ignored) {
        this.ignored = ignored;
    }

    public Date getRecordedDate() {
        return this.recordedDate;
    }

    public void setRecordedDate(Date recordedDate) {
        this.recordedDate = recordedDate;
    }
}
