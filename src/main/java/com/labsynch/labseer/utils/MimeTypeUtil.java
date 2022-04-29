package com.labsynch.labseer.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.labsynch.labseer.api.ApiFileSaveController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MimeTypeUtil {

	static Logger logger = LoggerFactory.getLogger(MimeTypeUtil.class);

	private static final String MIME_PROP_FILE = "mimeType.properties";
	private static InputStream mimeTypeStream;

	public static String getContentTypeFromExtension(String originalFilename) {
		logger.debug("trying to determine the file extension: ");

		String contentType = null;
		String fileExtension = getFileExtension(originalFilename);
		logger.debug("here is the file extension: " + fileExtension);
		try {
			mimeTypeStream = ApiFileSaveController.class.getClassLoader().getResourceAsStream(MIME_PROP_FILE);
			Properties mimeTypeProps = new Properties();
			mimeTypeProps.load(mimeTypeStream);
			contentType = mimeTypeProps.getProperty(fileExtension);
			logger.debug("here is the file contentType: " + contentType);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("unable to open the property file");
			e.printStackTrace();
		}
		return contentType;
	}

	public static String getFileExtension(String originalFilename) {
		int mid = originalFilename.lastIndexOf(".");
		String ext = originalFilename.substring(mid + 1, originalFilename.length());
		return ext;
	}
}
