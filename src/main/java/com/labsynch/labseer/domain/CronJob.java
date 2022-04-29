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
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Configurable
@Entity

public class CronJob {

	private static final Logger logger = LoggerFactory.getLogger(CronJob.class);

    @NotNull
    String schedule;
    
    @NotNull
    String scriptType;
    
    @NotNull
    String scriptFile;
    
    String functionName;
    
    @Column(columnDefinition="text")
    String scriptJSONData;
    
    @NotNull
    boolean active;
    
    @NotNull
    boolean ignored;
    
    @NotNull
    String runUser;
    
    @NotNull
    @Column(unique=true)
    String codeName;
    
    Date lastStartTime;
    
    Long lastDuration;
    
    @Column(columnDefinition="text")
    String lastResultJSON;
    
    Integer numberOfExecutions;
    
    public static CronJob update(CronJob cronJob) {
        CronJob updatedCronJob = CronJob.findCronJob(cronJob.getId());
        updatedCronJob.setSchedule(cronJob.getSchedule());
        updatedCronJob.setScriptType(cronJob.getScriptType());
        updatedCronJob.setScriptFile(cronJob.getScriptFile());
        updatedCronJob.setFunctionName(cronJob.getFunctionName());
        updatedCronJob.setScriptJSONData(cronJob.getScriptJSONData());
        updatedCronJob.setActive(cronJob.isActive());
        updatedCronJob.setIgnored(cronJob.isIgnored());
        updatedCronJob.setRunUser(cronJob.getRunUser());
        updatedCronJob.setCodeName(cronJob.getCodeName());
        updatedCronJob.setLastStartTime(cronJob.getLastStartTime());
        updatedCronJob.setLastDuration(cronJob.getLastDuration());
        updatedCronJob.setLastResultJSON(cronJob.getLastResultJSON());
        updatedCronJob.setNumberOfExecutions(cronJob.getNumberOfExecutions());
        updatedCronJob.merge();
        updatedCronJob.setVersion(CronJob.findCronJob(cronJob.getId()).getVersion());
        return updatedCronJob;
    }
    

	@Id
    @SequenceGenerator(name = "cronJobGen", sequenceName = "CRON_JOB_PKSEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cronJobGen")
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

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "schedule", "scriptType", "scriptFile", "functionName", "scriptJSONData", "active", "ignored", "runUser", "codeName", "lastStartTime", "lastDuration", "lastResultJSON", "numberOfExecutions", "id", "version");

	public static final EntityManager entityManager() {
        EntityManager em = new CronJob().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countCronJobs() {
        return entityManager().createQuery("SELECT COUNT(o) FROM CronJob o", Long.class).getSingleResult();
    }

	public static List<CronJob> findAllCronJobs() {
        return entityManager().createQuery("SELECT o FROM CronJob o", CronJob.class).getResultList();
    }

	public static List<CronJob> findAllCronJobs(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM CronJob o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, CronJob.class).getResultList();
    }

	public static CronJob findCronJob(Long id) {
        if (id == null) return null;
        return entityManager().find(CronJob.class, id);
    }

	public static List<CronJob> findCronJobEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM CronJob o", CronJob.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<CronJob> findCronJobEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM CronJob o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, CronJob.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            CronJob attached = CronJob.findCronJob(this.id);
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
    public CronJob merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        CronJob merged = this.entityManager.merge(this);
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

	public static CronJob fromJsonToCronJob(String json) {
        return new JSONDeserializer<CronJob>()
        .use(null, CronJob.class).deserialize(json);
    }

	public static String toJsonArray(Collection<CronJob> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<CronJob> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<CronJob> fromJsonArrayToCronJobs(String json) {
        return new JSONDeserializer<List<CronJob>>()
        .use("values", CronJob.class).deserialize(json);
    }

	public String getSchedule() {
        return this.schedule;
    }

	public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

	public String getScriptType() {
        return this.scriptType;
    }

	public void setScriptType(String scriptType) {
        this.scriptType = scriptType;
    }

	public String getScriptFile() {
        return this.scriptFile;
    }

	public void setScriptFile(String scriptFile) {
        this.scriptFile = scriptFile;
    }

	public String getFunctionName() {
        return this.functionName;
    }

	public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

	public String getScriptJSONData() {
        return this.scriptJSONData;
    }

	public void setScriptJSONData(String scriptJSONData) {
        this.scriptJSONData = scriptJSONData;
    }

	public boolean isActive() {
        return this.active;
    }

	public void setActive(boolean active) {
        this.active = active;
    }

	public boolean isIgnored() {
        return this.ignored;
    }

	public void setIgnored(boolean ignored) {
        this.ignored = ignored;
    }

	public String getRunUser() {
        return this.runUser;
    }

	public void setRunUser(String runUser) {
        this.runUser = runUser;
    }

	public String getCodeName() {
        return this.codeName;
    }

	public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

	public Date getLastStartTime() {
        return this.lastStartTime;
    }

	public void setLastStartTime(Date lastStartTime) {
        this.lastStartTime = lastStartTime;
    }

	public Long getLastDuration() {
        return this.lastDuration;
    }

	public void setLastDuration(Long lastDuration) {
        this.lastDuration = lastDuration;
    }

	public String getLastResultJSON() {
        return this.lastResultJSON;
    }

	public void setLastResultJSON(String lastResultJSON) {
        this.lastResultJSON = lastResultJSON;
    }

	public Integer getNumberOfExecutions() {
        return this.numberOfExecutions;
    }

	public void setNumberOfExecutions(Integer numberOfExecutions) {
        this.numberOfExecutions = numberOfExecutions;
    }

	public static Long countFindCronJobsByCodeNameEquals(String codeName) {
        if (codeName == null || codeName.length() == 0) throw new IllegalArgumentException("The codeName argument is required");
        EntityManager em = CronJob.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM CronJob AS o WHERE o.codeName = :codeName", Long.class);
        q.setParameter("codeName", codeName);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<CronJob> findCronJobsByCodeNameEquals(String codeName) {
        if (codeName == null || codeName.length() == 0) throw new IllegalArgumentException("The codeName argument is required");
        EntityManager em = CronJob.entityManager();
        TypedQuery<CronJob> q = em.createQuery("SELECT o FROM CronJob AS o WHERE o.codeName = :codeName", CronJob.class);
        q.setParameter("codeName", codeName);
        return q;
    }

	public static TypedQuery<CronJob> findCronJobsByCodeNameEquals(String codeName, String sortFieldName, String sortOrder) {
        if (codeName == null || codeName.length() == 0) throw new IllegalArgumentException("The codeName argument is required");
        EntityManager em = CronJob.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM CronJob AS o WHERE o.codeName = :codeName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<CronJob> q = em.createQuery(queryBuilder.toString(), CronJob.class);
        q.setParameter("codeName", codeName);
        return q;
    }
}
