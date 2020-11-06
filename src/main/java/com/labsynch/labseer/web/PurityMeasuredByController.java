package com.labsynch.labseer.web;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;
import com.labsynch.labseer.domain.PurityMeasuredBy;
import com.labsynch.labseer.dto.configuration.MainConfigDTO;
import com.labsynch.labseer.utils.Configuration;
import java.io.UnsupportedEncodingException;


@RequestMapping({"/puritymeasuredbys","/purityMeasuredBys"})
@Controller

public class PurityMeasuredByController {


	private static final MainConfigDTO mainConfig = Configuration.getConfigInfo();

	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        PurityMeasuredBy puritymeasuredby = PurityMeasuredBy.findPurityMeasuredBy(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text; charset=utf-8");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control","no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma","no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache

        if (puritymeasuredby == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(puritymeasuredby.toJson(), headers, HttpStatus.OK);
    }

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
	        return new ResponseEntity<String>(PurityMeasuredBy.toJsonArray(PurityMeasuredBy.findAllPurityMeasuredBys("name", "ASC")), headers, HttpStatus.OK);
		} else {
	        return new ResponseEntity<String>(PurityMeasuredBy.toJsonArray(PurityMeasuredBy.findAllPurityMeasuredBys()), headers, HttpStatus.OK);
		}
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json) {
        PurityMeasuredBy.fromJsonToPurityMeasuredBy(json).persist();
        HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        for (PurityMeasuredBy purityMeasuredBy: PurityMeasuredBy.fromJsonArrayToPurityMeasuredBys(json)) {
            purityMeasuredBy.persist();
        }
        HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

	@RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        if (PurityMeasuredBy.fromJsonToPurityMeasuredBy(json).merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
	}


	@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        for (PurityMeasuredBy purityMeasuredBy: PurityMeasuredBy.fromJsonArrayToPurityMeasuredBys(json)) {
            if (purityMeasuredBy.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
        PurityMeasuredBy puritymeasuredby = PurityMeasuredBy.findPurityMeasuredBy(id);
        HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");       
        if (puritymeasuredby == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        puritymeasuredby.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
	
	
	@RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<String> getOptions() {
        HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text, text/html");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Max-Age", "86400");
        
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid PurityMeasuredBy purityMeasuredBy, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, purityMeasuredBy);
            return "puritymeasuredbys/create";
        }
        uiModel.asMap().clear();
        purityMeasuredBy.persist();
        return "redirect:/puritymeasuredbys/" + encodeUrlPathSegment(purityMeasuredBy.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new PurityMeasuredBy());
        return "puritymeasuredbys/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("puritymeasuredby", PurityMeasuredBy.findPurityMeasuredBy(id));
        uiModel.addAttribute("itemId", id);
        return "puritymeasuredbys/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("puritymeasuredbys", PurityMeasuredBy.findPurityMeasuredByEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) PurityMeasuredBy.countPurityMeasuredBys() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("puritymeasuredbys", PurityMeasuredBy.findAllPurityMeasuredBys(sortFieldName, sortOrder));
        }
        return "puritymeasuredbys/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid PurityMeasuredBy purityMeasuredBy, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, purityMeasuredBy);
            return "puritymeasuredbys/update";
        }
        uiModel.asMap().clear();
        purityMeasuredBy.merge();
        return "redirect:/puritymeasuredbys/" + encodeUrlPathSegment(purityMeasuredBy.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, PurityMeasuredBy.findPurityMeasuredBy(id));
        return "puritymeasuredbys/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        PurityMeasuredBy purityMeasuredBy = PurityMeasuredBy.findPurityMeasuredBy(id);
        purityMeasuredBy.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/puritymeasuredbys";
    }

	void populateEditForm(Model uiModel, PurityMeasuredBy purityMeasuredBy) {
        uiModel.addAttribute("purityMeasuredBy", purityMeasuredBy);
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

	@RequestMapping(params = "find=ByCodeEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindPurityMeasuredBysByCodeEquals(@RequestParam("code") String code) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(PurityMeasuredBy.toJsonArray(PurityMeasuredBy.findPurityMeasuredBysByCodeEquals(code).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByNameEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindPurityMeasuredBysByNameEquals(@RequestParam("name") String name) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(PurityMeasuredBy.toJsonArray(PurityMeasuredBy.findPurityMeasuredBysByNameEquals(name).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByNameLike", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindPurityMeasuredBysByNameLike(@RequestParam("name") String name) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(PurityMeasuredBy.toJsonArray(PurityMeasuredBy.findPurityMeasuredBysByNameLike(name).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = { "find=ByCodeEquals", "form" }, method = RequestMethod.GET)
    public String findPurityMeasuredBysByCodeEqualsForm(Model uiModel) {
        return "puritymeasuredbys/findPurityMeasuredBysByCodeEquals";
    }

	@RequestMapping(params = "find=ByCodeEquals", method = RequestMethod.GET)
    public String findPurityMeasuredBysByCodeEquals(@RequestParam("code") String code, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("puritymeasuredbys", PurityMeasuredBy.findPurityMeasuredBysByCodeEquals(code, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) PurityMeasuredBy.countFindPurityMeasuredBysByCodeEquals(code) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("puritymeasuredbys", PurityMeasuredBy.findPurityMeasuredBysByCodeEquals(code, sortFieldName, sortOrder).getResultList());
        }
        return "puritymeasuredbys/list";
    }

	@RequestMapping(params = { "find=ByNameEquals", "form" }, method = RequestMethod.GET)
    public String findPurityMeasuredBysByNameEqualsForm(Model uiModel) {
        return "puritymeasuredbys/findPurityMeasuredBysByNameEquals";
    }

	@RequestMapping(params = "find=ByNameEquals", method = RequestMethod.GET)
    public String findPurityMeasuredBysByNameEquals(@RequestParam("name") String name, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("puritymeasuredbys", PurityMeasuredBy.findPurityMeasuredBysByNameEquals(name, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) PurityMeasuredBy.countFindPurityMeasuredBysByNameEquals(name) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("puritymeasuredbys", PurityMeasuredBy.findPurityMeasuredBysByNameEquals(name, sortFieldName, sortOrder).getResultList());
        }
        return "puritymeasuredbys/list";
    }

	@RequestMapping(params = { "find=ByNameLike", "form" }, method = RequestMethod.GET)
    public String findPurityMeasuredBysByNameLikeForm(Model uiModel) {
        return "puritymeasuredbys/findPurityMeasuredBysByNameLike";
    }

	@RequestMapping(params = "find=ByNameLike", method = RequestMethod.GET)
    public String findPurityMeasuredBysByNameLike(@RequestParam("name") String name, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("puritymeasuredbys", PurityMeasuredBy.findPurityMeasuredBysByNameLike(name, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) PurityMeasuredBy.countFindPurityMeasuredBysByNameLike(name) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("puritymeasuredbys", PurityMeasuredBy.findPurityMeasuredBysByNameLike(name, sortFieldName, sortOrder).getResultList());
        }
        return "puritymeasuredbys/list";
    }
}
