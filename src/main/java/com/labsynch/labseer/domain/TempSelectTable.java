package com.labsynch.labseer.domain;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity

public class TempSelectTable {

	private static final Logger logger = LoggerFactory.getLogger(TempSelectTable.class);

	
    private Long numberVar;

	
	
    @Size(max = 255)
    private String stringVar;
    
	
	private Long lsTransaction;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "MM")
	private Date recordedDate;
	
	@Size(max = 255)
	private String recordedBy;
    

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new TempSelectTable().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
		
	@Transactional
	public static int deleteTempSelectTableEntries(Long lsTranscation) {
		String sqlQuery = "DELETE FROM TempSelectTable o WHERE o.lsTransaction = :lsTranscation ";

		logger.info("Input lsTranscation: " + lsTranscation);
		logger.info("sqlQuery: " + sqlQuery);

		
		Query em = entityManager().createQuery(sqlQuery);
		em.setParameter("lsTranscation", lsTranscation);
		int result = em.executeUpdate();
		return result;
    }

	public static Long saveStrings(Set<String> stringList,  String recordedBy, Date recordedDate) {
		
		LsTransaction lsTransaction = new LsTransaction();
		lsTransaction.setRecordedDate(recordedDate);
		lsTransaction.persist();
		
		int batchSize = 25;
		int batchCounter = 1;
		TempSelectTable tst;
		for (String batchCode : stringList){
			tst = new TempSelectTable();
			tst.setStringVar(batchCode);
			tst.setLsTransaction(lsTransaction.getId());
			tst.setRecordedDate(recordedDate);
			tst.setRecordedBy(recordedBy);
			tst.persist();
			if (batchCounter % batchSize == 0){
				tst.flush();
				tst.clear();
			}
			batchCounter++;
		}
		
		return lsTransaction.getId();
	}
	
	public static Long saveNumbers(Set<Long> numericList, String recordedBy, Date recordedDate) {
		
		LsTransaction lsTransaction = new LsTransaction();
		lsTransaction.setRecordedDate(recordedDate);
		lsTransaction.persist();
		
		int batchSize = 25;
		int batchCounter = 1;
		TempSelectTable tst;
		for (Long batchCode : numericList){
			tst = new TempSelectTable();
			tst.setNumberVar(batchCode);
			tst.setLsTransaction(lsTransaction.getId());
			tst.setRecordedDate(recordedDate);
			tst.setRecordedBy(recordedBy);
			tst.persist();
			if (batchCounter % batchSize == 0){
				tst.flush();
				tst.clear();
			}
			batchCounter++;
		}
		
		return lsTransaction.getId();
	}



	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	@Id
    @SequenceGenerator(name = "tempSelectTableGen", sequenceName = "TEMP_SELECT_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "tempSelectTableGen")
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

	public Long getNumberVar() {
        return this.numberVar;
    }

	public void setNumberVar(Long numberVar) {
        this.numberVar = numberVar;
    }

	public String getStringVar() {
        return this.stringVar;
    }

	public void setStringVar(String stringVar) {
        this.stringVar = stringVar;
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

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "numberVar", "stringVar", "lsTransaction", "recordedDate", "recordedBy", "entityManager");

	public static long countTempSelectTables() {
        return entityManager().createQuery("SELECT COUNT(o) FROM TempSelectTable o", Long.class).getSingleResult();
    }

	public static List<TempSelectTable> findAllTempSelectTables() {
        return entityManager().createQuery("SELECT o FROM TempSelectTable o", TempSelectTable.class).getResultList();
    }

	public static List<TempSelectTable> findAllTempSelectTables(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM TempSelectTable o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, TempSelectTable.class).getResultList();
    }

	public static TempSelectTable findTempSelectTable(Long id) {
        if (id == null) return null;
        return entityManager().find(TempSelectTable.class, id);
    }

	public static List<TempSelectTable> findTempSelectTableEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM TempSelectTable o", TempSelectTable.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<TempSelectTable> findTempSelectTableEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM TempSelectTable o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, TempSelectTable.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            TempSelectTable attached = TempSelectTable.findTempSelectTable(this.id);
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
    public TempSelectTable merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        TempSelectTable merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
