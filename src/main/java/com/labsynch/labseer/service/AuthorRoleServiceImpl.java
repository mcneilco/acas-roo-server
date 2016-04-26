package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.domain.AuthorRole;
import com.labsynch.labseer.domain.LsRole;

@Service
@Transactional
public class AuthorRoleServiceImpl implements AuthorRoleService {

	@Autowired
	private AutoLabelService autoLabelService;

	private static final Logger logger = LoggerFactory.getLogger(AuthorRoleServiceImpl.class);
	

	@Override
	@Transactional
	public Author syncRoles(Author author, Collection<String> grantedRoles) {
		Collection<AuthorRole> cachedRoles = author.getAuthorRoles();
		logger.debug(AuthorRole.toJsonArray(cachedRoles));
		HashSet<String> cachedRoleNames = new HashSet<String>();
		for (AuthorRole cachedRole : cachedRoles){
			if (cachedRole.getRoleEntry().getLsType().equalsIgnoreCase("LDAP")){
				cachedRoleNames.add(cachedRole.getRoleEntry().getRoleName());
			}
		}
		//diff the two sets - cached and new/granted
		HashSet<String> roleNamesToDelete = new HashSet<String>();
		roleNamesToDelete.addAll(cachedRoleNames);
		roleNamesToDelete.removeAll(grantedRoles);
		HashSet<String> roleNamesToAdd = new HashSet<String>();
		roleNamesToAdd.addAll(grantedRoles);
		roleNamesToAdd.removeAll(cachedRoleNames);
		Set<AuthorRole> newAuthorRoles = new HashSet<AuthorRole>();
		logger.debug(roleNamesToDelete.toString());
		logger.debug(roleNamesToAdd.toString());
		//add new roles, autocreating LsRole if necessary
		for (String roleName : roleNamesToAdd){
			LsRole role;
			try{
				role = LsRole.findLsRolesByRoleNameEquals(roleName).getSingleResult();
			}catch (EmptyResultDataAccessException e){
				role = new LsRole();
				role.setRoleName(roleName);
				role.setLsType("LDAP");
				role.setLsKind("Auto");
				role.setRoleDescription("autocreated by ACAS from LDAP role");
				role.persist();
			}
			AuthorRole authorRole = new AuthorRole();
			authorRole.setRoleEntry(role);
			authorRole.setUserEntry(author);
			authorRole.persist();
			newAuthorRoles.add(authorRole);
		}
		//delete the roles that no longer apply
		Set<AuthorRole> removedAuthorRoles = new HashSet<AuthorRole>();
		for (String roleName : roleNamesToDelete){
			LsRole role = LsRole.findLsRolesByRoleNameEquals(roleName).getSingleResult();
			AuthorRole authorRole = AuthorRole.findAuthorRolesByRoleEntryAndUserEntry(role, author).getSingleResult();
			authorRole.remove();
			removedAuthorRoles.add(authorRole);
		}
		author.getAuthorRoles().removeAll(removedAuthorRoles);
		author.getAuthorRoles().addAll(newAuthorRoles);
		author.merge();
		return author;
	}

	
}
