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
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
public class BulkLoadFile {

    @Size(max = 1000)
    private String fileName;

    private int numberOfMols;
    
    private int fileSize;
    
	@Column(columnDefinition="text")
    private String jsonTemplate;
	
	@NotNull
	private String recordedBy;
	
	private Date fileDate;
	
	@NotNull
	private Date recordedDate;
    
	public BulkLoadFile(){
	}
	
	public BulkLoadFile(String fileName, int numberOfMols, int fileSize, String jsonTemplate, String recordedBy, Date recordedDate){
		this.fileName = fileName;
		this.numberOfMols = numberOfMols;
		this.fileSize = fileSize;
		this.jsonTemplate = jsonTemplate;
		this.recordedBy = recordedBy;
		this.recordedDate = recordedDate;
	}

	public static Long countFindBulkLoadFilesByFileNameEquals(String fileName) {
        if (fileName == null || fileName.length() == 0) throw new IllegalArgumentException("The fileName argument is required");
        EntityManager em = BulkLoadFile.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM BulkLoadFile AS o WHERE o.fileName = :fileName", Long.class);
        q.setParameter("fileName", fileName);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindBulkLoadFilesByRecordedByEquals(String recordedBy) {
        if (recordedBy == null || recordedBy.length() == 0) throw new IllegalArgumentException("The recordedBy argument is required");
        EntityManager em = BulkLoadFile.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM BulkLoadFile AS o WHERE o.recordedBy = :recordedBy", Long.class);
        q.setParameter("recordedBy", recordedBy);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<BulkLoadFile> findBulkLoadFilesByFileNameEquals(String fileName) {
        if (fileName == null || fileName.length() == 0) throw new IllegalArgumentException("The fileName argument is required");
        EntityManager em = BulkLoadFile.entityManager();
        TypedQuery<BulkLoadFile> q = em.createQuery("SELECT o FROM BulkLoadFile AS o WHERE o.fileName = :fileName", BulkLoadFile.class);
        q.setParameter("fileName", fileName);
        return q;
    }

	public static TypedQuery<BulkLoadFile> findBulkLoadFilesByFileNameEquals(String fileName, String sortFieldName, String sortOrder) {
        if (fileName == null || fileName.length() == 0) throw new IllegalArgumentException("The fileName argument is required");
        EntityManager em = BulkLoadFile.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM BulkLoadFile AS o WHERE o.fileName = :fileName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<BulkLoadFile> q = em.createQuery(queryBuilder.toString(), BulkLoadFile.class);
        q.setParameter("fileName", fileName);
        return q;
    }

	public static TypedQuery<BulkLoadFile> findBulkLoadFilesByRecordedByEquals(String recordedBy) {
        if (recordedBy == null || recordedBy.length() == 0) throw new IllegalArgumentException("The recordedBy argument is required");
        EntityManager em = BulkLoadFile.entityManager();
        TypedQuery<BulkLoadFile> q = em.createQuery("SELECT o FROM BulkLoadFile AS o WHERE o.recordedBy = :recordedBy", BulkLoadFile.class);
        q.setParameter("recordedBy", recordedBy);
        return q;
    }

	public static TypedQuery<BulkLoadFile> findBulkLoadFilesByRecordedByEquals(String recordedBy, String sortFieldName, String sortOrder) {
        if (recordedBy == null || recordedBy.length() == 0) throw new IllegalArgumentException("The recordedBy argument is required");
        EntityManager em = BulkLoadFile.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM BulkLoadFile AS o WHERE o.recordedBy = :recordedBy");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<BulkLoadFile> q = em.createQuery(queryBuilder.toString(), BulkLoadFile.class);
        q.setParameter("recordedBy", recordedBy);
        return q;
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

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("fileName", "numberOfMols", "fileSize", "jsonTemplate", "recordedBy", "fileDate", "recordedDate");

	public static final EntityManager entityManager() {
        EntityManager em = new BulkLoadFile().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countBulkLoadFiles() {
        return entityManager().createQuery("SELECT COUNT(o) FROM BulkLoadFile o", Long.class).getSingleResult();
    }

	public static List<BulkLoadFile> findAllBulkLoadFiles() {
        return entityManager().createQuery("SELECT o FROM BulkLoadFile o", BulkLoadFile.class).getResultList();
    }

	public static List<BulkLoadFile> findAllBulkLoadFiles(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM BulkLoadFile o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, BulkLoadFile.class).getResultList();
    }

	public static BulkLoadFile findBulkLoadFile(Long id) {
        if (id == null) return null;
        return entityManager().find(BulkLoadFile.class, id);
    }

	public static List<BulkLoadFile> findBulkLoadFileEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM BulkLoadFile o", BulkLoadFile.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<BulkLoadFile> findBulkLoadFileEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM BulkLoadFile o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, BulkLoadFile.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            BulkLoadFile attached = BulkLoadFile.findBulkLoadFile(this.id);
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
    public BulkLoadFile merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        BulkLoadFile merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

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

	public static BulkLoadFile fromJsonToBulkLoadFile(String json) {
        return new JSONDeserializer<BulkLoadFile>()
        .use(null, BulkLoadFile.class).deserialize(json);
    }

	public static String toJsonArray(Collection<BulkLoadFile> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<BulkLoadFile> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<BulkLoadFile> fromJsonArrayToBulkLoadFiles(String json) {
        return new JSONDeserializer<List<BulkLoadFile>>()
        .use("values", BulkLoadFile.class).deserialize(json);
    }

	public String getFileName() {
        return this.fileName;
    }

	public void setFileName(String fileName) {
        this.fileName = fileName;
    }

	public int getNumberOfMols() {
        return this.numberOfMols;
    }

	public void setNumberOfMols(int numberOfMols) {
        this.numberOfMols = numberOfMols;
    }

	public int getFileSize() {
        return this.fileSize;
    }

	public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

	public String getJsonTemplate() {
        return this.jsonTemplate;
    }

	public void setJsonTemplate(String jsonTemplate) {
        this.jsonTemplate = jsonTemplate;
    }

	public String getRecordedBy() {
        return this.recordedBy;
    }

	public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }

	public Date getFileDate() {
        return this.fileDate;
    }

	public void setFileDate(Date fileDate) {
        this.fileDate = fileDate;
    }

	public Date getRecordedDate() {
        return this.recordedDate;
    }

	public void setRecordedDate(Date recordedDate) {
        this.recordedDate = recordedDate;
    }
}
