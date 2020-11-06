package com.labsynch.labseer.web;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;
import com.labsynch.labseer.domain.FileList;
import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.dto.FileSaveReturnDTO;

@RequestMapping("/filelists")
@Controller
public class FileListController {

    private static final Logger logger = LoggerFactory.getLogger(FileListController.class);

    @RequestMapping(method = RequestMethod.POST)
    public String createFile(@Valid FileList fileList, BindingResult bindingResult, Model uiModel, @RequestParam("file") MultipartFile file, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("fileList", fileList);
            return "filelists/create";
        }
        uiModel.asMap().clear();
        fileList.setFile(file);
        fileList.setType(file.getContentType());
        fileList.setFileName(file.getOriginalFilename());
        fileList.setSize(file.getSize());
        fileList.setName(file.getOriginalFilename());
        logger.debug("Name: " + file.getOriginalFilename());
        logger.debug("Description: " + fileList.getDescription());
        logger.debug("File: " + file.getName());
        logger.debug("Type: " + file.getContentType());
        logger.debug("Size: " + fileList.getSize());
        fileList.saveFile(file);
        fileList.persist();
        return "redirect:/filelists/" + encodeUrlPathSegment(fileList.getId().toString(), httpServletRequest);
    }

    @RequestMapping(value = "/saveFile", method = RequestMethod.POST)
    public ResponseEntity<String> create(@Valid FileList fileList, BindingResult bindingResult, Model uiModel, @RequestParam("file") MultipartFile file, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("fileList", fileList);
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.debug("receiving the post file");
        uiModel.asMap().clear();
        fileList.setFile(file);
        fileList.setType(file.getContentType());
        fileList.setFileName(file.getOriginalFilename());
        fileList.setSize(file.getSize());
        fileList.setName(file.getOriginalFilename());
        logger.debug("Name: " + file.getOriginalFilename());
        logger.debug("Description: " + fileList.getDescription());
        logger.debug("File: " + file.getName());
        logger.debug("Type: " + file.getContentType());
        logger.debug("Size: " + fileList.getSize());
        fileList.saveFile(file);
        FileSaveReturnDTO fileSave = new FileSaveReturnDTO();
        fileSave.setDescription(fileList.getDescription());
        fileSave.setSubdir(fileList.getSubdir());
        fileSave.setIe(fileList.getIe());
        fileSave.setFile(file);
        fileSave.setType(file.getContentType());
        fileSave.setSize(file.getSize());
        fileSave.setName(file.getOriginalFilename());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<String>(fileSave.toJson(), headers, HttpStatus.CREATED);
    }

    // 	 @RequestParam("description") String description,
    // 	 @RequestParam("subdir") String subdir,
    // 	 @RequestParam("ie") boolean ie,
    @RequestMapping(value = "/createSaveFile", method = RequestMethod.POST)
    public ResponseEntity<String> createSaveFile(@Valid FileList fileList, BindingResult bindingResult, Model uiModel, @RequestParam("file") MultipartFile file, HttpServletRequest httpServletRequest) {
        logger.debug("receiving the post file");
        logger.debug("Filelist Description: " + fileList.getDescription());
        fileList.setFile(file);
        fileList.setType(file.getContentType());
        fileList.setFileName(file.getOriginalFilename());
        fileList.setSize(file.getSize());
        fileList.setName(file.getOriginalFilename());
        fileList.setFilePath(fileList.getSubdir());
        logger.debug("Name: " + file.getOriginalFilename());
        logger.debug("Description: " + fileList.getDescription());
        logger.debug("File: " + file.getName());
        logger.debug("Type: " + file.getContentType());
        logger.debug("Size: " + fileList.getSize());
        logger.debug("Subdir to save: " + fileList.getSubdir());
        fileList.saveFile(file);
        FileSaveReturnDTO fileSave = new FileSaveReturnDTO();
        fileSave.setDescription(fileList.getDescription());
        fileSave.setSubdir(fileList.getSubdir());
        fileSave.setIe(fileList.getIe());
        fileSave.setFile(file);
        fileSave.setType(file.getContentType());
        fileSave.setSize(file.getSize());
        fileSave.setName(file.getOriginalFilename());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
        headers.add("Pragma", "no-cache"); //HTTP 1.0
        headers.setExpires(0); // Expire the cache
        return new ResponseEntity<String>(fileSave.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(Model uiModel) {
        uiModel.addAttribute("fileList", new FileList());
        return "filelists/create";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("filelist", FileList.findFileList(id));
        uiModel.addAttribute("itemId", id);
        return "filelists/show";
    }

    @RequestMapping(value = "/showfile/{id}", method = RequestMethod.GET)
    public String showdoc(@PathVariable("id") Long id, HttpServletResponse response, Model model) {
        FileList fileList = FileList.findFileList(id);
        File readFile = new File(fileList.getFilePath());
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
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

    @RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            uiModel.addAttribute("filelists", FileList.findFileListEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) FileList.countFileLists() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("filelists", FileList.findAllFileLists());
        }
        return "filelists/list";
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String update(@Valid FileList fileList, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("fileList", fileList);
            return "filelists/update";
        }
        uiModel.asMap().clear();
        fileList.merge();
        return "redirect:/filelists/" + encodeUrlPathSegment(fileList.getId().toString(), httpServletRequest);
    }

    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("fileList", FileList.findFileList(id));
        return "filelists/update";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        FileList.findFileList(id).remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/filelists";
    }

    @ModelAttribute("filelists")
    public Collection<FileList> populateFileLists() {
        return FileList.findAllFileLists();
    }

    @ModelAttribute("lots")
    public Collection<Lot> populateLots() {
        return Lot.findAllLots();
    }

    String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {
        }
        return pathSegment;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        FileList filelist = FileList.findFileList(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text; charset=utf-8");
        if (filelist == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(filelist.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text; charset=utf-8");
        return new ResponseEntity<String>(FileList.toJsonArray(FileList.findAllFileLists()), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json) {
        FileList.fromJsonToFileList(json).persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        for (FileList fileList : FileList.fromJsonArrayToFileLists(json)) {
            fileList.persist();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text");
        if (FileList.fromJsonToFileList(json).merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text");
        for (FileList fileList : FileList.fromJsonArrayToFileLists(json)) {
            if (fileList.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
        FileList filelist = FileList.findFileList(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text");
        if (filelist == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        filelist.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(params = "find=ByLot", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindFileListsByLot(@RequestParam("lot") Lot lot) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text; charset=utf-8");
        return new ResponseEntity<String>(FileList.toJsonArray(FileList.findFileListsByLot(lot).getResultList()), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<String> getOptions() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text, text/html");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
        headers.add("Pragma", "no-cache"); //HTTP 1.0
        headers.setExpires(0); // Expire the cache
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

	void populateEditForm(Model uiModel, FileList fileList) {
        uiModel.addAttribute("fileList", fileList);
        uiModel.addAttribute("lots", Lot.findAllLots());
    }

	@RequestMapping(params = { "find=ByLot", "form" }, method = RequestMethod.GET)
    public String findFileListsByLotForm(Model uiModel) {
        uiModel.addAttribute("lots", Lot.findAllLots());
        return "filelists/findFileListsByLot";
    }

	@RequestMapping(params = "find=ByLot", method = RequestMethod.GET)
    public String findFileListsByLot(@RequestParam("lot") Lot lot, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("filelists", FileList.findFileListsByLot(lot, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) FileList.countFindFileListsByLot(lot) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("filelists", FileList.findFileListsByLot(lot, sortFieldName, sortOrder).getResultList());
        }
        return "filelists/list";
    }

	@RequestMapping(params = { "find=ByUrlEquals", "form" }, method = RequestMethod.GET)
    public String findFileListsByUrlEqualsForm(Model uiModel) {
        return "filelists/findFileListsByUrlEquals";
    }

	@RequestMapping(params = "find=ByUrlEquals", method = RequestMethod.GET)
    public String findFileListsByUrlEquals(@RequestParam("url") String url, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("filelists", FileList.findFileListsByUrlEquals(url, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) FileList.countFindFileListsByUrlEquals(url) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("filelists", FileList.findFileListsByUrlEquals(url, sortFieldName, sortOrder).getResultList());
        }
        return "filelists/list";
    }
}
