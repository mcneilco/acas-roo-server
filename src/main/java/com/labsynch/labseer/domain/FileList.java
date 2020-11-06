package com.labsynch.labseer.domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.labsynch.labseer.web.FileListController;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@Configurable
public class FileList {

	
    private static final Logger logger = LoggerFactory.getLogger(FileListController.class);

    @ManyToOne(fetch = FetchType.LAZY)
    private Lot lot;

    private String description;

    private String name;

    @Size(max = 255)
    private String type;

    private long size;

    private String url;

    private Boolean ie;

    private Boolean uploaded;

    @Transient
    private MultipartFile file;

    private String fileName;

    private String subdir;

    private String filePath;

    public MultipartFile getFile() {
        return file;
    }

    public void saveFile(MultipartFile file) {
        this.file = file;
        try {
            InputStream in = file.getInputStream();
            String rootSavePath = "/Applications/MAMP/htdocs/imageFolder/";
            String savePath = rootSavePath + File.separator + this.getSubdir();
            String saveFileName = savePath + this.getFileName();
            boolean createdDir = new File(savePath).mkdirs();
            if (createdDir) {
                logger.debug("new directory created " + savePath);
            }
            logger.debug(" Saving file: " + this.name + " to  " + saveFileName);
            String urlString = "http://localhost:8888/imageFolder/" + this.getSubdir() + File.separator + this.getFileName();
            logger.debug("url string " + urlString);
            FileOutputStream f = new FileOutputStream(saveFileName);
            int ch = 0;
            while ((ch = in.read()) != -1) {
                f.write(ch);
            }
            f.flush();
            f.close();
            this.setUploaded(true);
            this.setUrl(urlString);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Lot getLot() {
        return this.lot;
    }

    public void setLot(Lot lot) {
        this.lot = lot;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getIe() {
        return this.ie;
    }

    public void setIe(Boolean ie) {
        this.ie = ie;
    }

    public Boolean getUploaded() {
        return this.uploaded;
    }

    public void setUploaded(Boolean uploaded) {
        this.uploaded = uploaded;
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

    public String getSubdir() {
        return this.subdir;
    }

    public void setSubdir(String subdir) {
        this.subdir = subdir;
    }
    
    public static TypedQuery<FileList> findFileListsByLot(Lot lot) {
        if (lot == null) throw new IllegalArgumentException("The lot argument is required");
        EntityManager em = FileList.entityManager();
        TypedQuery<FileList> q = em.createQuery("SELECT o FROM FileList AS o WHERE o.lot = :lot", FileList.class);
        q.setParameter("lot", lot);
        return q;
    }
    
    public static TypedQuery<FileList> findFileListsByUrlEquals(String url) {
        if (url == null || url.length() == 0) throw new IllegalArgumentException("The url argument is required");
        EntityManager em = FileList.entityManager();
        TypedQuery<FileList> q = em.createQuery("SELECT o FROM FileList AS o WHERE o.url = :url ORDER by o", FileList.class);
        q.setParameter("url", url);
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

	public static Long countFindFileListsByLot(Lot lot) {
        if (lot == null) throw new IllegalArgumentException("The lot argument is required");
        EntityManager em = FileList.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM FileList AS o WHERE o.lot = :lot", Long.class);
        q.setParameter("lot", lot);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindFileListsByUrlEquals(String url) {
        if (url == null || url.length() == 0) throw new IllegalArgumentException("The url argument is required");
        EntityManager em = FileList.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM FileList AS o WHERE o.url = :url", Long.class);
        q.setParameter("url", url);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<FileList> findFileListsByLot(Lot lot, String sortFieldName, String sortOrder) {
        if (lot == null) throw new IllegalArgumentException("The lot argument is required");
        EntityManager em = FileList.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM FileList AS o WHERE o.lot = :lot");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<FileList> q = em.createQuery(queryBuilder.toString(), FileList.class);
        q.setParameter("lot", lot);
        return q;
    }

	public static TypedQuery<FileList> findFileListsByUrlEquals(String url, String sortFieldName, String sortOrder) {
        if (url == null || url.length() == 0) throw new IllegalArgumentException("The url argument is required");
        EntityManager em = FileList.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM FileList AS o WHERE o.url = :url");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<FileList> q = em.createQuery(queryBuilder.toString(), FileList.class);
        q.setParameter("url", url);
        return q;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static FileList fromJsonToFileList(String json) {
        return new JSONDeserializer<FileList>()
        .use(null, FileList.class).deserialize(json);
    }

	public static String toJsonArray(Collection<FileList> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<FileList> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<FileList> fromJsonArrayToFileLists(String json) {
        return new JSONDeserializer<List<FileList>>()
        .use("values", FileList.class).deserialize(json);
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "lot", "description", "name", "type", "size", "url", "ie", "uploaded", "file", "fileName", "subdir", "filePath");

	public static final EntityManager entityManager() {
        EntityManager em = new FileList().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countFileLists() {
        return entityManager().createQuery("SELECT COUNT(o) FROM FileList o", Long.class).getSingleResult();
    }

	public static List<FileList> findAllFileLists() {
        return entityManager().createQuery("SELECT o FROM FileList o", FileList.class).getResultList();
    }

	public static List<FileList> findAllFileLists(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM FileList o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, FileList.class).getResultList();
    }

	public static FileList findFileList(Long id) {
        if (id == null) return null;
        return entityManager().find(FileList.class, id);
    }

	public static List<FileList> findFileListEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM FileList o", FileList.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<FileList> findFileListEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM FileList o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, FileList.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            FileList attached = FileList.findFileList(this.id);
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
    public FileList merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        FileList merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String getFilePath() {
        return this.filePath;
    }

	public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
