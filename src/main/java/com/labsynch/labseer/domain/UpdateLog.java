package com.labsynch.labseer.domain;

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
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
@RooJavaBean
@RooToString
@RooJpaActiveRecord(sequenceName = "UPDATE_LOG_PKSEQ")
public class UpdateLog {

    private Long thing;

    @Size(max = 255)
    private String updateAction;
    
	@Size(max = 512)
	private String comments;
	
	private Long lsTransaction;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "MM")
	private Date recordedDate;
	
	@Size(max = 255)
	private String recordedBy;
    
    

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("thing", "updateAction", "comments", "lsTransaction", "recordedDate", "recordedBy");

	public static final EntityManager entityManager() {
        EntityManager em = new UpdateLog().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countUpdateLogs() {
        return entityManager().createQuery("SELECT COUNT(o) FROM UpdateLog o", Long.class).getSingleResult();
    }

	public static List<UpdateLog> findAllUpdateLogs() {
        return entityManager().createQuery("SELECT o FROM UpdateLog o", UpdateLog.class).getResultList();
    }

	public static List<UpdateLog> findAllUpdateLogs(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM UpdateLog o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, UpdateLog.class).getResultList();
    }

	public static UpdateLog findUpdateLog(Long id) {
        if (id == null) return null;
        return entityManager().find(UpdateLog.class, id);
    }

	public static List<UpdateLog> findUpdateLogEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM UpdateLog o", UpdateLog.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<UpdateLog> findUpdateLogEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM UpdateLog o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, UpdateLog.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            UpdateLog attached = UpdateLog.findUpdateLog(this.id);
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
    public UpdateLog merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        UpdateLog merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	@Id
    @SequenceGenerator(name = "updateLogGen", sequenceName = "UPDATE_LOG_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "updateLogGen")
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

	public Long getThing() {
        return this.thing;
    }

	public void setThing(Long thing) {
        this.thing = thing;
    }

	public String getUpdateAction() {
        return this.updateAction;
    }

	public void setUpdateAction(String updateAction) {
        this.updateAction = updateAction;
    }

	public String getComments() {
        return this.comments;
    }

	public void setComments(String comments) {
        this.comments = comments;
    }

	public Long getLsTransaction() {
        return this.lsTransaction;
    }

	public void setLsTransaction(Long lsTransaction) {
        this.lsTransaction = lsTransaction;
    }

	public Date getRecordedDate() {
        return this.recordedDate;
    }

	public void setRecordedDate(Date recordedDate) {
        this.recordedDate = recordedDate;
    }

	public String getRecordedBy() {
        return this.recordedBy;
    }

	public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
