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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Configurable
@Entity


public class CmpdRegAppSetting {
		
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
    
    

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static CmpdRegAppSetting fromJsonToCmpdRegAppSetting(String json) {
        return new JSONDeserializer<CmpdRegAppSetting>()
        .use(null, CmpdRegAppSetting.class).deserialize(json);
    }

	public static String toJsonArray(Collection<CmpdRegAppSetting> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<CmpdRegAppSetting> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<CmpdRegAppSetting> fromJsonArrayToCmpdRegAppSettings(String json) {
        return new JSONDeserializer<List<CmpdRegAppSetting>>()
        .use("values", CmpdRegAppSetting.class).deserialize(json);
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

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("propName", "propValue", "comments", "ignored", "recordedDate");

	public static final EntityManager entityManager() {
        EntityManager em = new CmpdRegAppSetting().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countCmpdRegAppSettings() {
        return entityManager().createQuery("SELECT COUNT(o) FROM CmpdRegAppSetting o", Long.class).getSingleResult();
    }

	public static List<CmpdRegAppSetting> findAllCmpdRegAppSettings() {
        return entityManager().createQuery("SELECT o FROM CmpdRegAppSetting o", CmpdRegAppSetting.class).getResultList();
    }

	public static List<CmpdRegAppSetting> findAllCmpdRegAppSettings(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM CmpdRegAppSetting o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, CmpdRegAppSetting.class).getResultList();
    }

	public static CmpdRegAppSetting findCmpdRegAppSetting(Long id) {
        if (id == null) return null;
        return entityManager().find(CmpdRegAppSetting.class, id);
    }

	public static List<CmpdRegAppSetting> findCmpdRegAppSettingEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM CmpdRegAppSetting o", CmpdRegAppSetting.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<CmpdRegAppSetting> findCmpdRegAppSettingEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM CmpdRegAppSetting o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, CmpdRegAppSetting.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            CmpdRegAppSetting attached = CmpdRegAppSetting.findCmpdRegAppSetting(this.id);
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
    public CmpdRegAppSetting merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        CmpdRegAppSetting merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
