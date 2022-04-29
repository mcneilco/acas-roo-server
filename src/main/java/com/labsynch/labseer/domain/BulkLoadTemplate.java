package com.labsynch.labseer.domain;

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
import com.labsynch.labseer.dto.BulkLoadPropertyMappingDTO;
import com.labsynch.labseer.dto.BulkLoadTemplateDTO;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;

@Configurable
@Entity

public class BulkLoadTemplate {

    @Size(max = 255)
    private String templateName;
    
	@Column(columnDefinition="text")
    private String jsonTemplate;
	
	@NotNull
	private String recordedBy;
	
	@NotNull
	private boolean ignored;
    
	public BulkLoadTemplate(){
		this.ignored =false;
	}
	
	public BulkLoadTemplate(String templateName, String jsonTemplate, String recordedBy){
		this.templateName = templateName;
		this.jsonTemplate = jsonTemplate;
		this.recordedBy = recordedBy;
		this.ignored = false;
	}

	public void update(BulkLoadTemplate templateToSave) {
		this.templateName = templateToSave.getTemplateName();
		this.jsonTemplate = templateToSave.getJsonTemplate();
		this.recordedBy = templateToSave.getRecordedBy();
		this.ignored = templateToSave.isIgnored();
		this.merge();
	}
	
	public BulkLoadTemplate(BulkLoadTemplateDTO templateDTO){
		this.templateName = templateDTO.getTemplateName();
		this.jsonTemplate = BulkLoadPropertyMappingDTO.toJsonArray(templateDTO.getMappings());
		this.recordedBy = templateDTO.getRecordedBy();
		this.ignored = templateDTO.isIgnored();
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

	public static Long countFindBulkLoadTemplatesByRecordedByEquals(String recordedBy) {
        if (recordedBy == null || recordedBy.length() == 0) throw new IllegalArgumentException("The recordedBy argument is required");
        EntityManager em = BulkLoadTemplate.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM BulkLoadTemplate AS o WHERE o.recordedBy = :recordedBy", Long.class);
        q.setParameter("recordedBy", recordedBy);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindBulkLoadTemplatesByTemplateNameEquals(String templateName) {
        if (templateName == null || templateName.length() == 0) throw new IllegalArgumentException("The templateName argument is required");
        EntityManager em = BulkLoadTemplate.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM BulkLoadTemplate AS o WHERE o.templateName = :templateName", Long.class);
        q.setParameter("templateName", templateName);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindBulkLoadTemplatesByTemplateNameEqualsAndRecordedByEquals(String templateName, String recordedBy) {
        if (templateName == null || templateName.length() == 0) throw new IllegalArgumentException("The templateName argument is required");
        if (recordedBy == null || recordedBy.length() == 0) throw new IllegalArgumentException("The recordedBy argument is required");
        EntityManager em = BulkLoadTemplate.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM BulkLoadTemplate AS o WHERE o.templateName = :templateName  AND o.recordedBy = :recordedBy", Long.class);
        q.setParameter("templateName", templateName);
        q.setParameter("recordedBy", recordedBy);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<BulkLoadTemplate> findBulkLoadTemplatesByRecordedByEquals(String recordedBy) {
        if (recordedBy == null || recordedBy.length() == 0) throw new IllegalArgumentException("The recordedBy argument is required");
        EntityManager em = BulkLoadTemplate.entityManager();
        TypedQuery<BulkLoadTemplate> q = em.createQuery("SELECT o FROM BulkLoadTemplate AS o WHERE o.recordedBy = :recordedBy", BulkLoadTemplate.class);
        q.setParameter("recordedBy", recordedBy);
        return q;
    }

	public static TypedQuery<BulkLoadTemplate> findBulkLoadTemplatesByRecordedByEquals(String recordedBy, String sortFieldName, String sortOrder) {
        if (recordedBy == null || recordedBy.length() == 0) throw new IllegalArgumentException("The recordedBy argument is required");
        EntityManager em = BulkLoadTemplate.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM BulkLoadTemplate AS o WHERE o.recordedBy = :recordedBy");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<BulkLoadTemplate> q = em.createQuery(queryBuilder.toString(), BulkLoadTemplate.class);
        q.setParameter("recordedBy", recordedBy);
        return q;
    }

	public static TypedQuery<BulkLoadTemplate> findBulkLoadTemplatesByTemplateNameEquals(String templateName) {
        if (templateName == null || templateName.length() == 0) throw new IllegalArgumentException("The templateName argument is required");
        EntityManager em = BulkLoadTemplate.entityManager();
        TypedQuery<BulkLoadTemplate> q = em.createQuery("SELECT o FROM BulkLoadTemplate AS o WHERE o.templateName = :templateName", BulkLoadTemplate.class);
        q.setParameter("templateName", templateName);
        return q;
    }

	public static TypedQuery<BulkLoadTemplate> findBulkLoadTemplatesByTemplateNameEquals(String templateName, String sortFieldName, String sortOrder) {
        if (templateName == null || templateName.length() == 0) throw new IllegalArgumentException("The templateName argument is required");
        EntityManager em = BulkLoadTemplate.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM BulkLoadTemplate AS o WHERE o.templateName = :templateName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<BulkLoadTemplate> q = em.createQuery(queryBuilder.toString(), BulkLoadTemplate.class);
        q.setParameter("templateName", templateName);
        return q;
    }

	public static TypedQuery<BulkLoadTemplate> findBulkLoadTemplatesByTemplateNameEqualsAndRecordedByEquals(String templateName, String recordedBy) {
        if (templateName == null || templateName.length() == 0) throw new IllegalArgumentException("The templateName argument is required");
        if (recordedBy == null || recordedBy.length() == 0) throw new IllegalArgumentException("The recordedBy argument is required");
        EntityManager em = BulkLoadTemplate.entityManager();
        TypedQuery<BulkLoadTemplate> q = em.createQuery("SELECT o FROM BulkLoadTemplate AS o WHERE o.templateName = :templateName  AND o.recordedBy = :recordedBy", BulkLoadTemplate.class);
        q.setParameter("templateName", templateName);
        q.setParameter("recordedBy", recordedBy);
        return q;
    }

	public static TypedQuery<BulkLoadTemplate> findBulkLoadTemplatesByTemplateNameEqualsAndRecordedByEquals(String templateName, String recordedBy, String sortFieldName, String sortOrder) {
        if (templateName == null || templateName.length() == 0) throw new IllegalArgumentException("The templateName argument is required");
        if (recordedBy == null || recordedBy.length() == 0) throw new IllegalArgumentException("The recordedBy argument is required");
        EntityManager em = BulkLoadTemplate.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM BulkLoadTemplate AS o WHERE o.templateName = :templateName  AND o.recordedBy = :recordedBy");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<BulkLoadTemplate> q = em.createQuery(queryBuilder.toString(), BulkLoadTemplate.class);
        q.setParameter("templateName", templateName);
        q.setParameter("recordedBy", recordedBy);
        return q;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("templateName", "jsonTemplate", "recordedBy", "ignored");

	public static final EntityManager entityManager() {
        EntityManager em = new BulkLoadTemplate().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countBulkLoadTemplates() {
        return entityManager().createQuery("SELECT COUNT(o) FROM BulkLoadTemplate o", Long.class).getSingleResult();
    }

	public static List<BulkLoadTemplate> findAllBulkLoadTemplates() {
        return entityManager().createQuery("SELECT o FROM BulkLoadTemplate o", BulkLoadTemplate.class).getResultList();
    }

	public static List<BulkLoadTemplate> findAllBulkLoadTemplates(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM BulkLoadTemplate o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, BulkLoadTemplate.class).getResultList();
    }

	public static BulkLoadTemplate findBulkLoadTemplate(Long id) {
        if (id == null) return null;
        return entityManager().find(BulkLoadTemplate.class, id);
    }

	public static List<BulkLoadTemplate> findBulkLoadTemplateEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM BulkLoadTemplate o", BulkLoadTemplate.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<BulkLoadTemplate> findBulkLoadTemplateEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM BulkLoadTemplate o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, BulkLoadTemplate.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            BulkLoadTemplate attached = BulkLoadTemplate.findBulkLoadTemplate(this.id);
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
    public BulkLoadTemplate merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        BulkLoadTemplate merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String getTemplateName() {
        return this.templateName;
    }

	public void setTemplateName(String templateName) {
        this.templateName = templateName;
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

	public boolean isIgnored() {
        return this.ignored;
    }

	public void setIgnored(boolean ignored) {
        this.ignored = ignored;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static BulkLoadTemplate fromJsonToBulkLoadTemplate(String json) {
        return new JSONDeserializer<BulkLoadTemplate>()
        .use(null, BulkLoadTemplate.class).deserialize(json);
    }

	public static String toJsonArray(Collection<BulkLoadTemplate> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<BulkLoadTemplate> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<BulkLoadTemplate> fromJsonArrayToBulkLoadTemplates(String json) {
        return new JSONDeserializer<List<BulkLoadTemplate>>()
        .use("values", BulkLoadTemplate.class).deserialize(json);
    }
}
