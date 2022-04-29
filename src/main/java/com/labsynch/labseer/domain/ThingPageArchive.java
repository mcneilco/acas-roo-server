package com.labsynch.labseer.domain;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
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

public class ThingPageArchive {

    @Size(max = 255)
    private String pageName;

    @NotNull
    @Size(max = 255)
    private String recordedBy;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MM")
    private Date recordedDate;

    @Column(columnDefinition = "text")
    @Basic(fetch = FetchType.LAZY)
    private String pageContent;

    @NotNull
    @Size(max = 255)
    private String modifiedBy;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MM")
    private Date modifiedDate;

    @Size(max = 255)
    private String currentEditor;

    @NotNull
    private boolean ignored;

    private boolean archived;

    private Long lsTransaction;

    @Column(name = "thing_id")
    private Long thing;

    private Integer pageVersion;

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(this);
    }

    public static ThingPageArchive fromJsonToThingPageArchive(String json) {
        return new JSONDeserializer<ThingPageArchive>()
                .use(null, ThingPageArchive.class).deserialize(json);
    }

    public static String toJsonArray(Collection<ThingPageArchive> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<ThingPageArchive> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<ThingPageArchive> fromJsonArrayToThingPageArchives(String json) {
        return new JSONDeserializer<List<ThingPageArchive>>()
                .use("values", ThingPageArchive.class).deserialize(json);
    }

    @Id
    @SequenceGenerator(name = "thingPageArchiveGen", sequenceName = "THING_PAGE_ARCHIVE_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "thingPageArchiveGen")
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

    public String getPageName() {
        return this.pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getRecordedBy() {
        return this.recordedBy;
    }

    public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }

    public Date getRecordedDate() {
        return this.recordedDate;
    }

    public void setRecordedDate(Date recordedDate) {
        this.recordedDate = recordedDate;
    }

    public String getPageContent() {
        return this.pageContent;
    }

    public void setPageContent(String pageContent) {
        this.pageContent = pageContent;
    }

    public String getModifiedBy() {
        return this.modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedDate() {
        return this.modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getCurrentEditor() {
        return this.currentEditor;
    }

    public void setCurrentEditor(String currentEditor) {
        this.currentEditor = currentEditor;
    }

    public boolean isIgnored() {
        return this.ignored;
    }

    public void setIgnored(boolean ignored) {
        this.ignored = ignored;
    }

    public boolean isArchived() {
        return this.archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public Long getLsTransaction() {
        return this.lsTransaction;
    }

    public void setLsTransaction(Long lsTransaction) {
        this.lsTransaction = lsTransaction;
    }

    public Long getThing() {
        return this.thing;
    }

    public void setThing(Long thing) {
        this.thing = thing;
    }

    public Integer getPageVersion() {
        return this.pageVersion;
    }

    public void setPageVersion(Integer pageVersion) {
        this.pageVersion = pageVersion;
    }

    @PersistenceContext
    transient EntityManager entityManager;

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("pageName", "recordedBy",
            "recordedDate", "pageContent", "modifiedBy", "modifiedDate", "currentEditor", "ignored", "archived",
            "lsTransaction", "thing", "pageVersion");

    public static final EntityManager entityManager() {
        EntityManager em = new ThingPageArchive().entityManager;
        if (em == null)
            throw new IllegalStateException(
                    "Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countThingPageArchives() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ThingPageArchive o", Long.class).getSingleResult();
    }

    public static List<ThingPageArchive> findAllThingPageArchives() {
        return entityManager().createQuery("SELECT o FROM ThingPageArchive o", ThingPageArchive.class).getResultList();
    }

    public static List<ThingPageArchive> findAllThingPageArchives(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ThingPageArchive o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ThingPageArchive.class).getResultList();
    }

    public static ThingPageArchive findThingPageArchive(Long id) {
        if (id == null)
            return null;
        return entityManager().find(ThingPageArchive.class, id);
    }

    public static List<ThingPageArchive> findThingPageArchiveEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ThingPageArchive o", ThingPageArchive.class)
                .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static List<ThingPageArchive> findThingPageArchiveEntries(int firstResult, int maxResults,
            String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ThingPageArchive o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ThingPageArchive.class).setFirstResult(firstResult)
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
            ThingPageArchive attached = ThingPageArchive.findThingPageArchive(this.id);
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
    public ThingPageArchive merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        ThingPageArchive merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
