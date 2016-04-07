package com.labsynch.labseer.service;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.Author;

@Service
public interface AuthorRoleService {

	Author syncRoles(Author author, Collection<String> grantedRoles);

	

}
