package com.labsynch.labseer.api;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

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
	public HttpEntity<String> create(@RequestParam("description[]") List<String> description,
			@RequestParam("subdir") String subdir,
			@RequestParam("ie") boolean ie,
			@RequestParam("file[]") List<MultipartFile> file) {

		logger.debug("receiving the post file");

		FileSaveSendDTO fileSave = new FileSaveSendDTO();
		fileSave.setDescription(description);
		fileSave.setFile(file);
		fileSave.setIe(ie);
		fileSave.setSubdir(subdir);

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

	@RequestMapping(value = "/getFile", method = RequestMethod.GET)
	public void getfile(@RequestParam("fileUrl") String fileUrl,
			HttpServletResponse response,
			Model model) {

		logger.debug("raw query url: " + fileUrl);

		// removing hack that was required due to Node pipe error.
		// fileUrl = fileUrl.replace("/", File.separator);
		// logger.debug("replacing with File.separator " + File.separator);
		// logger.debug("modified query url: " + fileUrl);

		String searchFileUrl = "getFile?fileUrl=" + fileUrl; // this is an ugly hack -- we should clean up this code
																// better to use the fileList id as a handle

		// It would be better to get the content type from the database instead of from
		// the file (performance)
		// However, I am concerned about getting the info by the fileUrl -- is it unique
		// prefer to get the unique ID to work with

		File readFile = new File(fileUrl);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		String fileName = readFile.getName();

		String fileType = MimeTypeUtil.getContentTypeFromExtension(fileName);
		logger.debug("Incoming file url: " + fileUrl);
		logger.debug("here is the file name from the file itself: " + fileName);
		logger.debug("here is the absolute file name from the file itself: " + readFile.getAbsoluteFile());

		try {
			response.setHeader("Content-Disposition", "inline;filename=\"" + fileName + "\"");
			// response.setHeader("Cache-Control","no-store, no-cache, must-revalidate");
			// response.setHeader("Pragma","no-cache");

			String mimeType = null;

			if (fileType == null) {
				logger.debug("file extension is not a known type");
				List<FileList> fileLists = FileList.findFileListsByUrlEquals(searchFileUrl).getResultList();
				FileList fileList = null;
				int numberOfFiles = fileLists.size();
				logger.debug("number of files found with the same url: " + numberOfFiles);
				if (numberOfFiles > 0) {
					fileList = fileLists.get(numberOfFiles - 1); // just get a single element - the most recent file
				} else {
					logger.error("did not find any files for the given url " + searchFileUrl);

				}
				logger.debug("here is the file type from the database object: " + fileList.getType());
				mimeType = fileList.getType();
			} else {
				logger.debug("here is the file type from the file object: " + fileType);
				mimeType = fileType;
			}
			logger.debug("the mime type for the file is " + mimeType);

			response.setContentType(mimeType);

			OutputStream out = response.getOutputStream();

			logger.debug("reading the file");
			fis = new FileInputStream(readFile);
			bis = new BufferedInputStream(fis);
			IOUtils.copy(bis, out);
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/showSavedFile/{id}", method = RequestMethod.GET)
	public String showdoc(@PathVariable("id") Long id,
			HttpServletResponse response,
			Model model) {

		FileList fileList = FileList.findFileList(id);

		File readFile = new File(fileList.getFilePath());
		FileInputStream fis = null;
		BufferedInputStream bis = null;

		try {
			response.setHeader("Content-Disposition", "inline;filename=\"" + fileList.getFileName() + "\"");
			OutputStream out = response.getOutputStream();
			response.setContentType(fileList.getType());

			fis = new FileInputStream(readFile);
			bis = new BufferedInputStream(fis);

			IOUtils.copy(bis, out);
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
