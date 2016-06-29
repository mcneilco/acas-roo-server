package com.labsynch.labseer.api;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
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
import com.labsynch.labseer.domain.LsRole;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.dto.AuthGroupsAndProjectsDTO;
import com.labsynch.labseer.dto.AuthorNameDTO;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.service.AuthorService;
import com.labsynch.labseer.utils.PropertiesFileService;
import com.labsynch.labseer.utils.PropertiesUtilService;
import com.labsynch.labseer.web.AuthorController;

@Controller
@RequestMapping("api/v1/authorization")
public class ApiAuthorizationController {

	private static final Logger logger = LoggerFactory.getLogger(ApiAuthorizationController.class);

	@Autowired
	private AuthorService authorService;

	@RequestMapping(value = "/projects", params = { "find=ByUserName", "userName" }, method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<java.lang.String> jsonFindAuthorProjectsByUserName(@RequestParam("userName") String userName, @RequestParam(value="format", required=false) String format) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		Collection<LsThing> projects = authorService.getUserProjects(userName);
		logger.debug("searching for user: " + userName);
		if (format != null && format.equalsIgnoreCase("codeTable")){
			Collection<CodeTableDTO> codeTableProjects = authorService.convertProjectsToCodeTables(projects);
			return new ResponseEntity<String>(CodeTableDTO.toJsonArray(codeTableProjects), headers, HttpStatus.OK);
		}
		return new ResponseEntity<String>(LsThing.toJsonArrayStub(projects), headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/groupsAndProjects", params = { }, method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<java.lang.String> getAuthRolesAndProjects() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		AuthGroupsAndProjectsDTO results = authorService.getAuthGroupsAndProjects();
		return new ResponseEntity<String>(results.toJson(), headers, HttpStatus.OK);
	}
	
	
}
