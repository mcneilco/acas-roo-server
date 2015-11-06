package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.dto.AuthGroupsAndProjectsDTO;
import com.labsynch.labseer.dto.CodeTableDTO;

@Service
public interface AuthorService {

	public CodeTableDTO getAuthorCodeTable(Author author);

	public List<CodeTableDTO> convertToCodeTables(List<Author> authors);

	Collection<Author> findAuthorsByAuthorRoleName(String authorRoleName);

	Collection<LsThing> getUserProjects(String userName);

	AuthGroupsAndProjectsDTO getAuthGroupsAndProjects();

	CodeTableDTO getProjectCodeTable(LsThing project);

	List<CodeTableDTO> convertProjectsToCodeTables(Collection<LsThing> projects);

}
