package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.dto.CodeTableDTO;

@Service
public interface AuthorService {

	public CodeTableDTO getAuthorCodeTable(Author author);

	public List<CodeTableDTO> convertToCodeTables(List<Author> authors);

	Collection<Author> findAuthorsByAuthorRoleName(String authorRoleName);

	public Author saveAuthor(Author author);

	public Author updateAuthor(Author author);

	public Author getOrCreateAuthor(Author author);

}
