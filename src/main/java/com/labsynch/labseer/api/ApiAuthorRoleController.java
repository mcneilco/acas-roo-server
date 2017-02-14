package com.labsynch.labseer.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.domain.AuthorRole;
import com.labsynch.labseer.dto.AuthorNameDTO;
import com.labsynch.labseer.dto.AuthorRoleDTO;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.service.AuthorRoleService;
import com.labsynch.labseer.service.AuthorService;
import com.labsynch.labseer.service.ContainerService;
import com.labsynch.labseer.utils.PropertiesFileService;
import com.labsynch.labseer.utils.PropertiesUtilService;
import com.labsynch.labseer.web.AuthorController;
import com.labsynch.labseer.web.AuthorRoleController;

@Controller
@RequestMapping("api/v1/authorroles")
public class ApiAuthorRoleController {
	
	@Autowired
    private AuthorRoleService authorRoleService;
	
	private static final Logger logger = LoggerFactory.getLogger(AuthorRoleController.class);

	
    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
        AuthorRole authorRole = AuthorRole.findAuthorRole(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (authorRole == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(authorRole.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<AuthorRole> result = AuthorRole.findAllAuthorRoles();
        return new ResponseEntity<String>(AuthorRole.toJsonArray(result), headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/saveRoles", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromDTOs(@RequestBody String json) {
        Collection<AuthorRoleDTO> authorRoleDTOs = AuthorRoleDTO.fromJsonArrayToAuthorRoes(json);
        authorRoleService.saveAuthorRoleDTOs(authorRoleDTOs);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJson(@RequestBody AuthorRole authorRole) {
        authorRole.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody List<AuthorRole> authorRoles) {
        for (AuthorRole authorRole : authorRoles) {
            authorRole.persist();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody AuthorRole authorRole) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (authorRole.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody List<AuthorRole> authorRoles) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (AuthorRole authorRole : authorRoles) {
            if (authorRole.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/deleteRoles", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> deleteFromDTOs(@RequestBody String json) {
        Collection<AuthorRoleDTO> authorRoleDTOs = AuthorRoleDTO.fromJsonArrayToAuthorRoes(json);
        authorRoleService.deleteAuthorRoleDTOs(authorRoleDTOs);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
        AuthorRole authorRole = AuthorRole.findAuthorRole(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (authorRole == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        authorRole.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(params = "find=ByUserEntry", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindAuthorRolesByUserEntry(@RequestParam("userEntry") Author userEntry) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(AuthorRole.toJsonArray(AuthorRole.findAuthorRolesByUserEntry(userEntry).getResultList()), headers, HttpStatus.OK);
    }
}
