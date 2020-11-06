package com.labsynch.labseer.domain;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
@RooJavaBean
@RooToString
@RooJpaActiveRecord(sequenceName = "THING_PAGE_PKSEQ")
@RooJson
public class ThingPage {

    @Size(max = 255)
    private String pageName;

    @NotNull
    @Size(max = 255)
    private String recordedBy;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MM")
    private Date recordedDate;

    @Column(columnDefinition="text")
    @Basic(fetch = FetchType.LAZY)
    private String pageContent;

    @NotNull
    @Size(max = 255)
    private String modifiedBy;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MM")
    private Date modifiedDate;

    @NotNull
    @Size(max = 255)
    private String currentEditor;

    @NotNull
    private boolean ignored;

    @NotNull
    private boolean archived;

    @ManyToOne
    private LsTransaction lsTransaction;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "thing_id")
    private AbstractThing thing;

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static ThingPage fromJsonToThingPage(String json) {
        return new JSONDeserializer<ThingPage>()
        .use(null, ThingPage.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ThingPage> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<ThingPage> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<ThingPage> fromJsonArrayToThingPages(String json) {
        return new JSONDeserializer<List<ThingPage>>()
        .use("values", ThingPage.class).deserialize(json);
    }

	@Id
    @SequenceGenerator(name = "thingPageGen", sequenceName = "THING_PAGE_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "thingPageGen")
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

	public LsTransaction getLsTransaction() {
        return this.lsTransaction;
    }

	public void setLsTransaction(LsTransaction lsTransaction) {
        this.lsTransaction = lsTransaction;
    }

	public AbstractThing getThing() {
        return this.thing;
    }

	public void setThing(AbstractThing thing) {
        this.thing = thing;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("pageName", "recordedBy", "recordedDate", "pageContent", "modifiedBy", "modifiedDate", "currentEditor", "ignored", "archived", "lsTransaction", "thing");

	public static final EntityManager entityManager() {
        EntityManager em = new ThingPage().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countThingPages() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ThingPage o", Long.class).getSingleResult();
    }

	public static List<ThingPage> findAllThingPages() {
        return entityManager().createQuery("SELECT o FROM ThingPage o", ThingPage.class).getResultList();
    }

	public static List<ThingPage> findAllThingPages(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ThingPage o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ThingPage.class).getResultList();
    }

	public static ThingPage findThingPage(Long id) {
        if (id == null) return null;
        return entityManager().find(ThingPage.class, id);
    }

	public static List<ThingPage> findThingPageEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ThingPage o", ThingPage.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<ThingPage> findThingPageEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ThingPage o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ThingPage.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            ThingPage attached = ThingPage.findThingPage(this.id);
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
    public ThingPage merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ThingPage merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
