package com.labsynch.labseer.service;

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
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Configurable

public class SaltLoader {
	
    private String name;
	
    private String description;

    private long numberOfSalts;

    private long size;

    private Boolean uploaded;
    
    @Transient 
    private MultipartFile file; // added 

    private String fileName; // added 
	
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    private Date loadedDate;

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("name", "description", "numberOfSalts", "size", "uploaded", "file", "fileName", "loadedDate");

	public static final EntityManager entityManager() {
        EntityManager em = new SaltLoader().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countSaltLoaders() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SaltLoader o", Long.class).getSingleResult();
    }

	public static List<SaltLoader> findAllSaltLoaders() {
        return entityManager().createQuery("SELECT o FROM SaltLoader o", SaltLoader.class).getResultList();
    }

	public static List<SaltLoader> findAllSaltLoaders(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM SaltLoader o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, SaltLoader.class).getResultList();
    }

	public static SaltLoader findSaltLoader(Long id) {
        if (id == null) return null;
        return entityManager().find(SaltLoader.class, id);
    }

	public static List<SaltLoader> findSaltLoaderEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SaltLoader o", SaltLoader.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<SaltLoader> findSaltLoaderEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM SaltLoader o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, SaltLoader.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            SaltLoader attached = SaltLoader.findSaltLoader(this.id);
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
    public SaltLoader merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SaltLoader merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public String getDescription() {
        return this.description;
    }

	public void setDescription(String description) {
        this.description = description;
    }

	public long getNumberOfSalts() {
        return this.numberOfSalts;
    }

	public void setNumberOfSalts(long numberOfSalts) {
        this.numberOfSalts = numberOfSalts;
    }

	public long getSize() {
        return this.size;
    }

	public void setSize(long size) {
        this.size = size;
    }

	public Boolean getUploaded() {
        return this.uploaded;
    }

	public void setUploaded(Boolean uploaded) {
        this.uploaded = uploaded;
    }

	public MultipartFile getFile() {
        return this.file;
    }

	public void setFile(MultipartFile file) {
        this.file = file;
    }

	public String getFileName() {
        return this.fileName;
    }

	public void setFileName(String fileName) {
        this.fileName = fileName;
    }

	public Date getLoadedDate() {
        return this.loadedDate;
    }

	public void setLoadedDate(Date loadedDate) {
        this.loadedDate = loadedDate;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
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
}
