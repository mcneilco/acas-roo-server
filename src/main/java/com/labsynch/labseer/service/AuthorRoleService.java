package com.labsynch.labseer.service;

import java.util.Collection;

import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.dto.AuthorRoleDTO;

import org.springframework.stereotype.Service;

@Service
public interface AuthorRoleService {

	Author syncRoles(Author author, Collection<String> grantedRoles);

	void saveAuthorRoleDTOs(Collection<AuthorRoleDTO> authorRoleDTOs);

	void deleteAuthorRoleDTOs(Collection<AuthorRoleDTO> authorRoleDTOs);

}
