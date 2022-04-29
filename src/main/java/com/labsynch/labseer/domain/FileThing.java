package com.labsynch.labseer.domain;

import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Configurable
@Entity

public class FileThing extends AbstractThing {

    @Size(max = 512)
    private String name;

    @Size(max = 512)
    private String description;

    @Size(max = 255)
    private String fileExtension;

    @Size(max = 512)
    private String applicationType;

    @Size(max = 512)
    private String mimeType;

    @Size(max = 1024)
    private String fileURL;

    private Long fileSize;

    public FileThing(FileThing fileThing) {
        super.setRecordedBy(fileThing.getRecordedBy());
        super.setRecordedDate(fileThing.getRecordedDate());
        super.setLsTransaction(fileThing.getLsTransaction());
        super.setModifiedBy(fileThing.getModifiedBy());
        super.setModifiedDate(fileThing.getModifiedDate());
        super.setCodeName(fileThing.getCodeName());
        super.setLsType(fileThing.getLsType());
        super.setLsKind(fileThing.getLsKind());
        super.setLsTypeAndKind(fileThing.getLsTypeAndKind());
        this.name = fileThing.getName();
        this.description = fileThing.getDescription();
        this.fileExtension = fileThing.getFileExtension();
        this.applicationType = fileThing.getApplicationType();
        this.mimeType = fileThing.getMimeType();
        this.fileURL = fileThing.getFileURL();
        this.fileSize = fileThing.getFileSize();
    }

    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(this);
    }

    public static FileThing fromJsonToFileThing(String json) {
        return new JSONDeserializer<FileThing>()
                .use(null, FileThing.class).deserialize(json);
    }

    public static String toJsonArray(Collection<FileThing> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<FileThing> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<FileThing> fromJsonArrayToFileThings(String json) {
        return new JSONDeserializer<List<FileThing>>()
                .use("values", FileThing.class).deserialize(json);
    }

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("name", "description",
            "fileExtension", "applicationType", "mimeType", "fileURL", "fileSize");

    public static long countFileThings() {
        return entityManager().createQuery("SELECT COUNT(o) FROM FileThing o", Long.class).getSingleResult();
    }

    public static List<FileThing> findAllFileThings() {
        return entityManager().createQuery("SELECT o FROM FileThing o", FileThing.class).getResultList();
    }

    public static List<FileThing> findAllFileThings(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM FileThing o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, FileThing.class).getResultList();
    }

    public static FileThing findFileThing(Long id) {
        if (id == null)
            return null;
        return entityManager().find(FileThing.class, id);
    }

    public static List<FileThing> findFileThingEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM FileThing o", FileThing.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    public static List<FileThing> findFileThingEntries(int firstResult, int maxResults, String sortFieldName,
            String sortOrder) {
        String jpaQuery = "SELECT o FROM FileThing o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, FileThing.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public FileThing merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        FileThing merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public FileThing() {
        super();
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

    public String getFileExtension() {
        return this.fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getApplicationType() {
        return this.applicationType;
    }

    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getFileURL() {
        return this.fileURL;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }

    public Long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
}
