package com.labsynch.labseer.dto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Transient;

import com.labsynch.labseer.utils.MimeTypeUtil;
import com.labsynch.labseer.utils.PropertiesUtilService;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.web.multipart.MultipartFile;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Configurable

public class FileSaveSendDTO {

	private static final Logger logger = LoggerFactory.getLogger(FileSaveSendDTO.class);

	private Boolean ie;

	private String subdir; // added

	private List<String> description = new ArrayList<String>();

	private List<String> writeup = new ArrayList<String>();

	private List<Long> size = new ArrayList<Long>();

	private List<String> contentType = new ArrayList<String>();

	@Transient
	private List<String> file = new ArrayList<String>();

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	public List<String> getFile() {
		return file;
	}

	// save file to disk, save filename , file size to database
	public List<FileSaveReturnDTO> saveFile() {

		List<String> fileList = this.file;

		List<String> descriptionList = this.description;

		List<String> writeupList = this.writeup;

		List<Long> sizeList = this.size;

		List<String> contentTypeList = this.contentType;

		List<FileSaveReturnDTO> fileSaveArray = new ArrayList<FileSaveReturnDTO>();

		logger.debug("FileSaveSendDTO: Number of files to save: " + fileList.size());
		for (int i = 0; i < fileList.size(); i++) {
			logger.debug("current file number: " + i);
			String file = fileList.get(i);
			logger.debug("is the file empty: " + file.isEmpty());

			if (!file.isEmpty()) {
				String description = descriptionList.get(i);
				String writeup = writeupList.get(i);
				Long size = sizeList.get(i);
				String contentType = contentTypeList.get(i);
				FileSaveReturnDTO fileSaveReturn = new FileSaveReturnDTO();

				// Get the file name from the string file path
				File aFile = new File(file);

				fileSaveReturn.setName(aFile.getName());
				fileSaveReturn.setSize(size);
				fileSaveReturn.setType(contentType);
				// fileSaveReturn.setUrl(urlString);

				fileSaveReturn.setUploaded(true);
				fileSaveReturn.setDescription(description);
				fileSaveReturn.setWriteup(writeup);
				fileSaveReturn.setIe(this.getIe());
				fileSaveReturn.setSubdir(this.getSubdir());

				logger.debug("Ready to save the file " + fileSaveReturn.getType() + "  " + fileSaveReturn.getName()
						+ " " + fileSaveReturn.getUrl());

				fileSaveArray.add(fileSaveReturn);

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

	public List<String> getWriteup() {
		return this.writeup;
	}

	public void setWriteup(List<String> writeup) {
		this.writeup = writeup;
	}

	public void setFile(List<String> file) {
		this.file = file;
	}

	public PropertiesUtilService getPropertiesUtilService() {
		return this.propertiesUtilService;
	}

	public void setPropertiesUtilService(PropertiesUtilService propertiesUtilService) {
		this.propertiesUtilService = propertiesUtilService;
	}

	public List<Long> getSize() {
		return this.size;
	}

	public void setSize(List<Long> size) {
		this.size = size;
	}

	public List<String> getContentType() {
		return this.contentType;
	}

	public void setContentType(List<String> contentType) {
		this.contentType = contentType;
	}

}
