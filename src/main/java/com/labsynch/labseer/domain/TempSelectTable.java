package com.labsynch.labseer.domain;

import java.util.Date;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(sequenceName = "TEMP_SELECT_PKSEQ")
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


}
