package com.labsynch.labseer.api;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.labsynch.labseer.domain.FileList;
import com.labsynch.labseer.dto.FileSaveReturnDTO;
import com.labsynch.labseer.dto.FileSaveSendDTO;
import com.labsynch.labseer.utils.MimeTypeUtil;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping({ "/api/v1/filesave", "/api/v1/MultipleFilePicker" })
@Controller
public class ApiFileSaveController {

	// simplified controller. This will just save the file to the directory path
	// does not save the database info

	private static final Logger logger = LoggerFactory.getLogger(ApiFileSaveController.class);

	@RequestMapping(method = RequestMethod.POST)
	public HttpEntity<String> create(@RequestBody FileSaveSendDTO fileSave) {
		
	  
		logger.debug("receiving the post file");

		logger.debug("subdir to save to: " + fileSave.getSubdir());
		logger.debug("ie mode: " + fileSave.getIe());

		List<FileSaveReturnDTO> fileSaveArray = fileSave.saveFile();
		logger.debug(FileSaveReturnDTO.toJsonArray(fileSaveArray));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_HTML);
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); // HTTP 1.1
		headers.add("Pragma", "no-cache"); // HTTP 1.0
		headers.setExpires(0); // Expire the cache
		return new ResponseEntity<String>(FileSaveReturnDTO.toJsonArray(fileSaveArray), headers, HttpStatus.OK);
	}

}
