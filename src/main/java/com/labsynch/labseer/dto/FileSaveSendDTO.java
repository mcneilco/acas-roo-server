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
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.web.multipart.MultipartFile;

import com.labsynch.labseer.utils.Configuration;
import com.labsynch.labseer.utils.MimeTypeUtil;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class FileSaveSendDTO{


	private static final Logger logger = LoggerFactory.getLogger(FileSaveSendDTO.class);


	private Boolean ie;

	private String subdir; // added 

	private List<String> description = new ArrayList<String>();

	@Transient 
	private List<MultipartFile> file = new ArrayList<MultipartFile>();



	public List<MultipartFile> getFile() { 
		return file; 
	} 

	// save file to disk, save filename , file size to database  
	public List<FileSaveReturnDTO> saveFile() { 

		List<MultipartFile> fileList = this.file;

		List<String> descriptionList = this.description;

		List<FileSaveReturnDTO> fileSaveArray = new ArrayList<FileSaveReturnDTO>();

		logger.debug("FileSaveSendDTO: Number of files to save: " + fileList.size());
		for (int i = 0; i < fileList.size(); i++){
			logger.debug("current file number: " + i); 
			MultipartFile file = fileList.get(i);
			logger.debug("is the file empty: " + file.isEmpty());

			if (!file.isEmpty()){
				String description = descriptionList.get(i);
				FileSaveReturnDTO fileSaveReturn = new FileSaveReturnDTO();
				try { 
					InputStream in = file.getInputStream(); 
					String rootSavePath = Configuration.getConfigInfo().getServerSettings().getNotebookSavePath(); 
					logger.debug(rootSavePath);
					String savePath = rootSavePath + this.getSubdir() +  File.separator ;
					logger.debug(savePath);
					String saveFileName = savePath + file.getOriginalFilename();            
					logger.debug(saveFileName);
					boolean createdDir = new File(savePath).mkdirs();
					if (createdDir){
						logger.debug("new directory created " + savePath);
					} else {
						logger.error("unable to create the directory " + savePath);
					}
					logger.debug(" Saving file: " + file.getOriginalFilename() + " to  " + saveFileName); 
					String urlString = "getFile?fileUrl=" + saveFileName;
					logger.debug("url string " + urlString);

					FileOutputStream f = new FileOutputStream(saveFileName); 

					int ch = 0; 
					while ((ch = in.read()) != -1) { 
						f.write(ch); 
					} 
					f.flush(); 
					f.close();   


					String contentType = null;
					contentType = MimeTypeUtil.getContentTypeFromExtension(file.getOriginalFilename());
					if (contentType==null){
//						logger.debug("content type was null -- try to get it from the file itself");
//						contentType = file.getContentType();
						logger.debug("unknown content type -- set to default binary type");
						contentType = MimeTypeUtil.getContentTypeFromExtension("fileName.defaultbin");

					}

					fileSaveReturn.setName(file.getOriginalFilename());
					fileSaveReturn.setSize(file.getSize());
					fileSaveReturn.setType(contentType);
					fileSaveReturn.setUploaded(true);
					fileSaveReturn.setUrl(urlString);
					fileSaveReturn.setDescription(description);
					fileSaveReturn.setIe(this.getIe());
					fileSaveReturn.setSubdir(this.getSubdir());

					logger.debug("Ready to save the file " + fileSaveReturn.getType() + "  " + fileSaveReturn.getName() + " " + fileSaveReturn.getUrl());

					fileSaveArray.add(fileSaveReturn);

				} catch (FileNotFoundException e) { 
					// TODO Auto-generated catch block 
					e.printStackTrace(); 
				} catch (IOException e) { 
					// TODO Auto-generated catch block 
					e.printStackTrace(); 
				}     			
			}
		}


		return fileSaveArray;

	}
	

	
	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static FileSaveSendDTO fromJsonToFileSaveSendDTO(String json) {
        return new JSONDeserializer<FileSaveSendDTO>()
        .use(null, FileSaveSendDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<FileSaveSendDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<FileSaveSendDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<FileSaveSendDTO> fromJsonArrayToFileSaveSendDTO(String json) {
        return new JSONDeserializer<List<FileSaveSendDTO>>()
        .use("values", FileSaveSendDTO.class).deserialize(json);
    }

	public Boolean getIe() {
        return this.ie;
    }

	public void setIe(Boolean ie) {
        this.ie = ie;
    }

	public String getSubdir() {
        return this.subdir;
    }

	public void setSubdir(String subdir) {
        this.subdir = subdir;
    }

	public List<String> getDescription() {
        return this.description;
    }

	public void setDescription(List<String> description) {
        this.description = description;
    }

	public void setFile(List<MultipartFile> file) {
        this.file = file;
    }
}
