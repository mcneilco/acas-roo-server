package com.labsynch.labseer.web;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
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
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;
import com.labsynch.labseer.domain.FileType;
import com.labsynch.labseer.dto.configuration.MainConfigDTO;
import com.labsynch.labseer.utils.Configuration;
import java.io.UnsupportedEncodingException;

@RequestMapping({"/filetypes", "/fileTypes"})
@Controller

@RooWebJson(jsonObject = FileType.class)
@RooWebScaffold(path = "filetypes", formBackingObject = FileType.class)
@RooWebFinder
public class FileTypeController {
	
	private static final MainConfigDTO mainConfig = Configuration.getConfigInfo();

	@RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text; charset=utf-8");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control","no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma","no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache

		if (mainConfig.getServerSettings().isOrderSelectLists()){
	        return new ResponseEntity<String>(FileType.toJsonArray(FileType.findAllFileTypes("name", "ASC")), headers, HttpStatus.OK);
		} else {
	        return new ResponseEntity<String>(FileType.toJsonArray(FileType.findAllFileTypes()), headers, HttpStatus.OK);
		}
		
    }
	
	@RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<String> getIsotopeOptions() {
        HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control","no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma","no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache
        return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

    

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid FileType fileType, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, fileType);
            return "filetypes/create";
        }
        uiModel.asMap().clear();
        fileType.persist();
        return "redirect:/filetypes/" + encodeUrlPathSegment(fileType.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new FileType());
        return "filetypes/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("filetype", FileType.findFileType(id));
        uiModel.addAttribute("itemId", id);
        return "filetypes/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("filetypes", FileType.findFileTypeEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) FileType.countFileTypes() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("filetypes", FileType.findAllFileTypes(sortFieldName, sortOrder));
        }
        return "filetypes/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid FileType fileType, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, fileType);
            return "filetypes/update";
        }
        uiModel.asMap().clear();
        fileType.merge();
        return "redirect:/filetypes/" + encodeUrlPathSegment(fileType.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, FileType.findFileType(id));
        return "filetypes/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        FileType fileType = FileType.findFileType(id);
        fileType.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/filetypes";
    }

	void populateEditForm(Model uiModel, FileType fileType) {
        uiModel.addAttribute("fileType", fileType);
    }

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try {
            FileType fileType = FileType.findFileType(id);
            if (fileType == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(fileType.toJson(), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json, UriComponentsBuilder uriBuilder) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            FileType fileType = FileType.fromJsonToFileType(json);
            fileType.persist();
            RequestMapping a = (RequestMapping) getClass().getAnnotation(RequestMapping.class);
            headers.add("Location",uriBuilder.path(a.value()[0]+"/"+fileType.getId().toString()).build().toUriString());
            return new ResponseEntity<String>(headers, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            for (FileType fileType: FileType.fromJsonArrayToFileTypes(json)) {
                fileType.persist();
            }
            return new ResponseEntity<String>(headers, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json, @PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            FileType fileType = FileType.fromJsonToFileType(json);
            fileType.setId(id);
            if (fileType.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            FileType fileType = FileType.findFileType(id);
            if (fileType == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            fileType.remove();
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByCodeEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindFileTypesByCodeEquals(@RequestParam("code") String code) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(FileType.toJsonArray(FileType.findFileTypesByCodeEquals(code).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = { "find=ByCodeEquals", "form" }, method = RequestMethod.GET)
    public String findFileTypesByCodeEqualsForm(Model uiModel) {
        return "filetypes/findFileTypesByCodeEquals";
    }

	@RequestMapping(params = "find=ByCodeEquals", method = RequestMethod.GET)
    public String findFileTypesByCodeEquals(@RequestParam("code") String code, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("filetypes", FileType.findFileTypesByCodeEquals(code, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) FileType.countFindFileTypesByCodeEquals(code) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("filetypes", FileType.findFileTypesByCodeEquals(code, sortFieldName, sortOrder).getResultList());
        }
        return "filetypes/list";
    }
}
