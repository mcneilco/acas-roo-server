package com.labsynch.labseer.dto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.web.multipart.MultipartFile;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class FileSaveReturnDTO{
			 

	private static final Logger logger = LoggerFactory.getLogger(FileSaveReturnDTO.class);

    private String name;

    private long size;

    private String type;

    private String url;

    private String description;

    private Boolean uploaded;

    private Boolean ie;

    private String subdir; // added 
    
    @Transient 
    private MultipartFile file; // added 


    public MultipartFile getFile() { 
        return file; 
    } 

       // save file to disk ,save filename , file size to database  
    public void saveFile(MultipartFile file) { 

        try { 
            InputStream in = file.getInputStream(); 
            String rootSavePath = "/Applications/MAMP/htdocs/imageFolder/"; 
            logger.debug(rootSavePath);
            String savePath = rootSavePath + this.getSubdir() +  File.separator ;
            logger.debug(savePath);

            String saveFileName = savePath + this.getName();            
            logger.debug(saveFileName);

            boolean createdDir = new File(savePath).mkdirs();
            if (createdDir){
            	logger.debug("new directory created " + savePath);
            }
            logger.debug(" Saving file: " + this.name + " to  " + saveFileName); 
            String urlString = "http://localhost:8888/imageFolder/" + this.getSubdir() + File.separator + this.getName();
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
            // TODO Auto-generated catch block 
            e.printStackTrace(); 
        } catch (IOException e) { 
            // TODO Auto-generated catch block 
            e.printStackTrace(); 
        } 

    } 


	public String toJson() {
        return new JSONSerializer().exclude("*.class")
        		.exclude("file")
        		.serialize(this);
    }

	public static FileSaveReturnDTO fromJsonToFileSaveReturnDTO(String json) {
        return new JSONDeserializer<FileSaveReturnDTO>().use(null, FileSaveReturnDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<FileSaveReturnDTO> collection) {
        return new JSONSerializer().exclude("*.class")
        		.exclude("file")
        		.serialize(collection);
    }

	public static Collection<FileSaveReturnDTO> fromJsonArrayToFileSaveReturnDTO(String json) {
        return new JSONDeserializer<List<FileSaveReturnDTO>>().use(null, ArrayList.class).use("values", FileSaveReturnDTO.class).deserialize(json);
    }
}
