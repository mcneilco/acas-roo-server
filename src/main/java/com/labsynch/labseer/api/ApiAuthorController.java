package com.labsynch.labseer.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.service.AuthorService;

@Controller
@RequestMapping("api/v1/authors")
public class ApiAuthorController {
	
	@Autowired
	private AuthorService authorService;
	
	@RequestMapping(value="/{format}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> listJsonWithFormat(@PathVariable("format") String format) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<Author> result = Author.findAllAuthors();
        if(format.equalsIgnoreCase("codeTable")) {
			List<CodeTableDTO> results = authorService.convertToCodeTables(result); 
			results = CodeTableDTO.sortCodeTables(results);
			return new ResponseEntity<String>(CodeTableDTO.toJsonArray(results), headers, HttpStatus.OK);
        }
        else return new ResponseEntity<String>(Author.toJsonArray(result), headers, HttpStatus.OK);
    }
}
