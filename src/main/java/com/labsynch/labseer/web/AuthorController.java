package com.labsynch.labseer.web;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.dto.AuthorNameDTO;
import com.labsynch.labseer.utils.PropertiesFileService;
import com.labsynch.labseer.utils.PropertiesUtilService;

@RooWebJson(jsonObject = Author.class)
@Controller
@RequestMapping("/authors")
@RooWebScaffold(path = "authors", formBackingObject = Author.class)
@RooWebFinder
public class AuthorController {

    private static final Logger logger = LoggerFactory.getLogger(AuthorController.class);
    
	@Autowired
	private PropertiesUtilService propertiesUtilService;
	
	@Autowired
	private PropertiesFileService propertiesFileService;
	
	

    @Autowired
    private MessageDigestPasswordEncoder messageDigestPasswordEncoder;

    @RequestMapping(value = "/findbyname", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindAuthorsByName(@RequestBody String json) {
        AuthorNameDTO authorName = AuthorNameDTO.fromJsonToAuthorNameDTO(json);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        
        if (propertiesUtilService.getAuthStrategy().equalsIgnoreCase("properties")){
            logger.debug("searching for properites user: " + authorName.getName());

        	String propertiesUserName = propertiesFileService.getUsernameProperty(propertiesUtilService.getSecurityProperties(), authorName.getName());
            logger.debug("found properites user: " + propertiesUserName);

        	if (propertiesUserName != null && authorName.getName().equalsIgnoreCase(propertiesUserName)){
	        		Author author = new Author();
	        		author.setUserName(propertiesUserName);
	        		author.setFirstName(propertiesUserName);
	        		author.setId(new Date().getTime());
	                return new ResponseEntity<String>(author.toJson(), headers, HttpStatus.OK);
        	}
        } else {
            List<Author> authors = Author.findAuthorsByUserName(authorName.getName()).getResultList();
            if (authors.size() == 0) {
                authors = Author.findAuthorsByEmailAddress(authorName.getName()).getResultList();
            }
            logger.debug("searching for user: " + authorName.getName());
            if (authors.size() == 1) {
                return new ResponseEntity<String>(authors.get(0).toJson(), headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<String>(Author.toJsonArray(authors), headers, HttpStatus.OK);
            }
        	
        }
		return new ResponseEntity<String>("[ ]", headers, HttpStatus.NOT_FOUND);
        
    }

    @RequestMapping(value = "/username", params = { "userName" }, method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindAuthorsByUserNamePath(@RequestParam("userName") String userName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<Author> authors = Author.findAuthorsByUserName(userName).getResultList();
        logger.debug("searching for user: " + userName);
        if (authors.size() == 1) {
            return new ResponseEntity<String>(authors.get(0).toJson(), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(Author.toJsonArray(authors), headers, HttpStatus.OK);
        }
    }

    @RequestMapping(params = { "find=ByUserName", "userName" }, method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindAuthorsByUserName(@RequestParam("userName") String userName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<Author> authors = Author.findAuthorsByUserName(userName).getResultList();
        logger.debug("searching for user: " + userName);
        if (authors.size() == 1) {
            return new ResponseEntity<String>(authors.get(0).toJson(), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(Author.toJsonArray(authors), headers, HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Author author, BindingResult result, Model model, HttpServletRequest request) {
        if (result.hasErrors()) {
            model.addAttribute("author", author);
            addDateTimeFormatPatterns(model);
            return "users/create";
        }
        if (author.getId() != null) {
            Author savedUser = Author.findAuthor(author.getId());
            if (!savedUser.getPassword().equals(author.getPassword())) {
                author.setPassword(messageDigestPasswordEncoder.encodePassword(author.getPassword(), null));
            }
        } else {
            author.setPassword(messageDigestPasswordEncoder.encodePassword(author.getPassword(), null));
        }
        author.persist();
        return "redirect:/users/" + encodeUrlPathSegment(author.getId().toString(), request);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
        Author author = Author.findAuthor(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (author == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(author.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<Author> result = Author.findAllAuthors();
        return new ResponseEntity<String>(Author.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
        Author author = Author.fromJsonToAuthor(json);
        author.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
        for (Author author : Author.fromJsonArrayToAuthors(json)) {
            author.persist();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Author author = Author.fromJsonToAuthor(json);
        if (author.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (Author author : Author.fromJsonArrayToAuthors(json)) {
            if (author.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
        Author author = Author.findAuthor(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (author == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        author.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(params = "find=ByActivationKeyAndEmailAddress", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindAuthorsByActivationKeyAndEmailAddress(@RequestParam("activationKey") String activationKey, @RequestParam("emailAddress") String emailAddress) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<Author> authors = Author.findAuthorsByActivationKeyAndEmailAddress(activationKey, emailAddress).getResultList();
        if (authors.size() == 1) {
            return new ResponseEntity<String>(authors.get(0).toJson(), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(Author.toJsonArray(authors), headers, HttpStatus.OK);
        }
    }

    @RequestMapping(params = "find=ByEmailAddress", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindAuthorsByEmailAddress(@RequestParam("emailAddress") String emailAddress) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<Author> authors = Author.findAuthorsByEmailAddress(emailAddress).getResultList();
        if (authors.size() == 1) {
            return new ResponseEntity<String>(authors.get(0).toJson(), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(Author.toJsonArray(authors), headers, HttpStatus.OK);
        }
    }
}
