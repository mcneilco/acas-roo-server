package com.labsynch.labseer.api;

import java.util.Collection;
import java.util.Date;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.dto.AuthGroupsAndProjectsDTO;
import com.labsynch.labseer.dto.ChangePasswordDTO;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.service.AuthorService;
import com.labsynch.labseer.utils.PropertiesUtilService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("api/v1/authorization")
public class ApiAuthorizationController {

	private static final Logger logger = LoggerFactory.getLogger(ApiAuthorizationController.class);

	@Autowired
	private AuthorService authorService;

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@RequestMapping(value = "/projects", params = { "find=ByUserName",
			"userName" }, method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<java.lang.String> jsonFindAuthorProjectsByUserName(@RequestParam("userName") String userName,
			@RequestParam(value = "format", required = false) String format) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		Collection<LsThing> projects = authorService.getUserProjects(userName);
		logger.debug("searching for user: " + userName);
		if (format != null && format.equalsIgnoreCase("codeTable")) {
			Collection<CodeTableDTO> codeTableProjects = authorService.convertProjectsToCodeTables(projects);
			return new ResponseEntity<String>(CodeTableDTO.toJsonArray(codeTableProjects), headers, HttpStatus.OK);
		}
		return new ResponseEntity<String>(LsThing.toJsonArrayStub(projects), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/groupsAndProjects", params = {}, method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<java.lang.String> getAuthRolesAndProjects() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		AuthGroupsAndProjectsDTO results = authorService.getAuthGroupsAndProjects();
		return new ResponseEntity<String>(results.toJson(), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/activateUser", params = "activate", method = RequestMethod.GET)
	public ResponseEntity<java.lang.String> activateUser(
			@RequestParam(value = "activate", required = true) String activationKey,
			@RequestParam(value = "emailAddress", required = true) String emailAddress, Model model) {
		TypedQuery<Author> query = Author.findAuthorsByActivationKeyAndEmailAddress(activationKey, emailAddress);
		Author User = query.getSingleResult();
		if (null != User) {
			User.setActivationDate(new Date());
			User.setEnabled(true);
			User.merge();
			return new ResponseEntity<String>(HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}

	}

	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
	public ResponseEntity<java.lang.String> resetPassword(@RequestBody String emailAddress) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json;");
		try {
			authorService.resetPassword(emailAddress);
		} catch (Exception e) {
			return new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public ResponseEntity<java.lang.String> changePassword(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json;");
		ChangePasswordDTO changePasswordDTO = ChangePasswordDTO.fromJsonToChangePasswordDTO(json);
		Author author;
		try {
			author = Author.findAuthorsByUserName(changePasswordDTO.getUsername()).getSingleResult();
		} catch (NoResultException e) {
			return new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
		}
		try {
			authorService.changePassword(author, changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword(),
					changePasswordDTO.getNewPasswordAgain());
		} catch (Exception e) {
			return new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

}
