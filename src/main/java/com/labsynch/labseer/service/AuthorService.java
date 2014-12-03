package com.labsynch.labseer.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.dto.CodeTableDTO;

@Service
public interface AuthorService {

	public CodeTableDTO getAuthorCodeTable(Author author);

	public List<CodeTableDTO> convertToCodeTables(List<Author> authors);

}
