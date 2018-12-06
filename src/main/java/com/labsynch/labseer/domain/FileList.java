package com.labsynch.labseer.domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.web.multipart.MultipartFile;

import com.labsynch.labseer.web.FileListController;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findFileListsByLot", "findFileListsByUrlEquals" })
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
    
}
