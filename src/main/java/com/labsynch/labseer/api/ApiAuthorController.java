package com.labsynch.labseer.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.dto.AuthorBrowserQueryDTO;
import com.labsynch.labseer.dto.AuthorNameDTO;
import com.labsynch.labseer.dto.AuthorQueryResultDTO;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.GenericQueryCodeTableResultDTO;
import com.labsynch.labseer.exceptions.ErrorMessage;
import com.labsynch.labseer.service.AuthorService;
import com.labsynch.labseer.utils.PropertiesFileService;
import com.labsynch.labseer.utils.PropertiesUtilService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("api/v1/authors")
public class ApiAuthorController {

	private static final Logger logger = LoggerFactory.getLogger(ApiAuthorController.class);

	@Autowired
	private AuthorService authorService;

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Autowired
	private PropertiesFileService propertiesFileService;

	@RequestMapping(value = "/{format}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<java.lang.String> listJsonWithFormat(@PathVariable("format") String format) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		List<Author> result = Author.findAllAuthors();
		if (format.equalsIgnoreCase("codeTable")) {
			List<CodeTableDTO> results = authorService.convertToCodeTables(result);
			results = CodeTableDTO.sortCodeTables(results);
			return new ResponseEntity<String>(CodeTableDTO.toJsonArray(results), headers, HttpStatus.OK);
		} else
			return new ResponseEntity<String>(Author.toJsonArray(result), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/findByRoleName", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<java.lang.String> listAuthorsByRoleName(@RequestParam("authorRoleName") String authorRoleName,
			@RequestParam(value = "format", required = false) String format) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		try {
			Collection<Author> authors = authorService.findAuthorsByAuthorRoleName(authorRoleName);
			if (format != null && format.equalsIgnoreCase("codeTable")) {
				List<Author> authorList = new ArrayList<Author>();
				authorList.addAll(authors);
				List<CodeTableDTO> results = authorService.convertToCodeTables(authorList);
				results = CodeTableDTO.sortCodeTables(results);
				return new ResponseEntity<String>(CodeTableDTO.toJsonArray(results), headers, HttpStatus.OK);
			} else
				return new ResponseEntity<String>(Author.toJsonArray(authors), headers, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Caught exception looking up authors with role: " + authorRoleName, e);
			return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/findByRoleTypeKindAndName", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<java.lang.String> listAuthorsByRoleTypeAndRoleKindAndRoleName(
			@RequestParam("roleName") String roleName,
			@RequestParam("roleType") String roleType,
			@RequestParam("roleKind") String roleKind,
			@RequestParam(value = "format", required = false) String format) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		try {
			Collection<Author> authors = authorService.findAuthorsByRoleTypeAndRoleKindAndRoleName(roleType, roleKind,
					roleName);
			if (format != null && format.equalsIgnoreCase("codeTable")) {
				List<Author> authorList = new ArrayList<Author>();
				authorList.addAll(authors);
				List<CodeTableDTO> results = authorService.convertToCodeTables(authorList);
				results = CodeTableDTO.sortCodeTables(results);
				return new ResponseEntity<String>(CodeTableDTO.toJsonArray(results), headers, HttpStatus.OK);
			} else
				return new ResponseEntity<String>(Author.toJsonArray(authors), headers, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Caught exception looking up authors by role type kind and name", e);
			return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/findbyname", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<java.lang.String> jsonFindAuthorsByName(@RequestBody AuthorNameDTO authorName) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (propertiesUtilService.getAuthStrategy().equalsIgnoreCase("properties")) {
			logger.debug("searching for properites user: " + authorName.getName());
			String propertiesUserName = propertiesFileService
					.getUsernameProperty(propertiesUtilService.getSecurityProperties(), authorName.getName());
			logger.debug("found properites user: " + propertiesUserName);
			if (propertiesUserName != null && authorName.getName().equalsIgnoreCase(propertiesUserName)) {
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

	@RequestMapping(value = "/username", params = {
			"userName" }, method = RequestMethod.GET, headers = "Accept=application/json")
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

	@RequestMapping(params = { "find=ByUserName",
			"userName" }, method = RequestMethod.GET, headers = "Accept=application/json")
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

	// @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers =
	// "Accept=application/json")
	// @ResponseBody
	// public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id)
	// {
	// Author author = Author.findAuthor(id);
	// HttpHeaders headers = new HttpHeaders();
	// headers.add("Content-Type", "application/json; charset=utf-8");
	// if (author == null) {
	// return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
	// }
	// return new ResponseEntity<String>(author.toJson(), headers, HttpStatus.OK);
	// }

	@RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<java.lang.String> listJson() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		List<Author> result = Author.findAllAuthors();
		return new ResponseEntity<String>(Author.toJsonArray(result), headers, HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(value = {"", "/"}, method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		try {
			Author author = Author.fromJsonToAuthor(json);
			author = authorService.saveAuthor(author);
			return new ResponseEntity<String>(author.toJson(), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("Caught exception saving author", e);
			return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		try {
			Collection<Author> authors = Author.fromJsonArrayToAuthors(json);
			Collection<Author> savedAuthors = new ArrayList<Author>();
			for (Author author : authors) {
				Author savedAuthor = authorService.saveAuthor(author);
				savedAuthors.add(savedAuthor);
			}
			return new ResponseEntity<String>(Author.toJsonArray(savedAuthors), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("Caught exception saving author", e);
			return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Transactional
	@RequestMapping(value = { "/{id}", "/" }, method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
		Author author = Author.fromJsonToAuthor(json);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		author = authorService.updateAuthor(author);
		if (author.getId() == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(author.toJson(), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		try {
			Collection<Author> authors = Author.fromJsonArrayToAuthors(json);
			Collection<Author> updatedAuthors = new ArrayList<Author>();
			for (Author author : authors) {
				Author updatedAuthor = authorService.saveAuthor(author);
				updatedAuthors.add(updatedAuthor);
			}
			return new ResponseEntity<String>(Author.toJsonArray(updatedAuthors), headers, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Caught exception updating authors", e);
			return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
		Author author = Author.findAuthor(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (author == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		author.logicalDelete();
		author.setEnabled(false);
		author.setLocked(true);
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	@RequestMapping(params = "find=ByActivationKeyAndEmailAddress", headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<java.lang.String> jsonFindAuthorsByActivationKeyAndEmailAddress(
			@RequestParam("activationKey") String activationKey, @RequestParam("emailAddress") String emailAddress) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		List<Author> authors = Author.findAuthorsByActivationKeyAndEmailAddress(activationKey, emailAddress)
				.getResultList();
		if (authors.size() == 1) {
			return new ResponseEntity<String>(authors.get(0).toJson(), headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(Author.toJsonArray(authors), headers, HttpStatus.OK);
		}
	}

	@RequestMapping(params = "find=ByEmailAddress", headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<java.lang.String> jsonFindAuthorsByEmailAddress(
			@RequestParam("emailAddress") String emailAddress) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		List<Author> authors = Author.findAuthorsByEmailAddress(emailAddress).getResultList();
		if (authors.size() == 1) {
			return new ResponseEntity<String>(authors.get(0).toJson(), headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(Author.toJsonArray(authors), headers, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/projects", params = { "find=ByUserName",
			"userName" }, method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<java.lang.String> jsonFindAuthorProjectsByUserName(
			@RequestParam("userName") String userName) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		Collection<LsThing> projects = authorService.getUserProjects(userName);
		logger.debug("searching for user: " + userName);

		return new ResponseEntity<String>(LsThing.toJsonArrayStub(projects), headers, HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(value = "/getOrCreate", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> getOrCreateFromJson(@RequestBody String json) {
		Author author = Author.fromJsonToAuthor(json);
		author = authorService.getOrCreateAuthor(author);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(author.toJson(), headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/genericBrowserSearch", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> genericBrowserSearch(@RequestBody String json,
			@RequestParam(value = "with", required = false) String with) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		AuthorBrowserQueryDTO query = AuthorBrowserQueryDTO.fromJsonToAuthorBrowserQueryDTO(json);
		ArrayList<ErrorMessage> errors = new ArrayList<ErrorMessage>();
		boolean errorsFound = false;
		Collection<Long> authorIds;
		AuthorQueryResultDTO result = new AuthorQueryResultDTO();
		try {
			authorIds = authorService.searchAuthorIdsByBrowserQueryDTO(query);
			int maxResults = 1000;
			if (query.getQueryDTO().getMaxResults() != null)
				maxResults = query.getQueryDTO().getMaxResults();
			result.setMaxResults(maxResults);
			result.setNumberOfResults(authorIds.size());
			if (result.getNumberOfResults() <= result.getMaxResults()) {
				result.setResults(authorService.getAuthorsByIds(authorIds));
			}
		} catch (Exception e) {
			logger.error("Caught searching for authors in generic interaction search", e);
			ErrorMessage error = new ErrorMessage();
			error.setErrorLevel("error");
			error.setMessage(e.getMessage());
			errors.add(error);
			errorsFound = true;
		}

		if (errorsFound) {
			return new ResponseEntity<String>(ErrorMessage.toJsonArray(errors), headers, HttpStatus.NOT_FOUND);
		} else {
			if (with != null) {
				if (with.equalsIgnoreCase("codeTable")) {
					GenericQueryCodeTableResultDTO resultDTO = new GenericQueryCodeTableResultDTO();
					resultDTO.setMaxResults(result.getMaxResults());
					resultDTO.setNumberOfResults(result.getNumberOfResults());
					if (result.getResults() != null) {
						resultDTO.setResults((Collection<CodeTableDTO>) authorService
								.convertToCodeTables(new ArrayList<Author>(result.getResults())));
					}
					return new ResponseEntity<String>(resultDTO.toJson(), headers, HttpStatus.OK);
				}
			}
			return new ResponseEntity<String>(result.toJson(), headers, HttpStatus.OK);
		}

	}

	@RequestMapping(value = "/signupAuthor", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> signupFromJson(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		try {
			Author author = Author.fromJsonToAuthor(json);
			author = authorService.signupAuthor(author);
			return new ResponseEntity<String>(author.toJson(), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("Caught exception signing up author", e);
			return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
