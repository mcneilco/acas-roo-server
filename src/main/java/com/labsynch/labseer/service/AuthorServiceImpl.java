package com.labsynch.labseer.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.domain.AuthorLabel;
import com.labsynch.labseer.domain.AuthorRole;
import com.labsynch.labseer.domain.AuthorState;
import com.labsynch.labseer.domain.AuthorValue;
import com.labsynch.labseer.domain.LsRole;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.LsThingLabel;
import com.labsynch.labseer.domain.LsThingState;
import com.labsynch.labseer.domain.LsThingValue;
import com.labsynch.labseer.dto.AuthGroupsAndProjectsDTO;
import com.labsynch.labseer.dto.AuthGroupsDTO;
import com.labsynch.labseer.dto.AuthProjectGroupsDTO;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.utils.PropertiesUtilService;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

	@Autowired
	private AutoLabelService autoLabelService;

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	private static final Logger logger = LoggerFactory.getLogger(AuthorServiceImpl.class);


	@Override
	public CodeTableDTO getAuthorCodeTable(Author author) {
		CodeTableDTO codeTable = new CodeTableDTO();
		codeTable.setName(author.getFirstName() + " " + author.getLastName());
		codeTable.setCode(author.getUserName());
		codeTable.setId(author.getId());
		codeTable.setIgnored(false);
		return codeTable;
	}


	@Override
	public List<CodeTableDTO> convertToCodeTables(List<Author> authors) {
		List<CodeTableDTO> codeTableList = new ArrayList<CodeTableDTO>();
		for (Author author : authors) {
			CodeTableDTO codeTable = getAuthorCodeTable(author);
			codeTableList.add(codeTable);
		}
		return codeTableList;	
	}

	@Override
	public Collection<Author> findAuthorsByAuthorRoleName(String authorRoleName){
		List<LsRole> roleEntries = LsRole.findLsRolesByRoleNameEquals(authorRoleName).getResultList();
		Collection<Author> authors = new HashSet<Author>();
		for (LsRole roleEntry : roleEntries){
			Collection<AuthorRole> authorRoles = AuthorRole.findAuthorRolesByRoleEntry(roleEntry).getResultList();
			for (AuthorRole authorRole : authorRoles){
				authors.add(authorRole.getUserEntry());
			}	
		}
		return authors;
	}

	@Override
	public Collection<LsThing> getUserProjects(String userName	){
		Collection<LsThing> projectThings = new HashSet<LsThing>();
		List<Author> authors = Author.findAuthorsByUserName(userName).getResultList();
		Author author = null;
		if (authors.size() == 0){
			logger.error("No author found by name: " + userName);
		} else if (authors.size() > 1) {
			logger.error("Found multiple authors by name: " + userName);
			author = authors.get(0);
		} else {
			author = authors.get(0);
		}

		if (author != null){
			Set<AuthorRole> roles = author.getAuthorRoles();
			for (AuthorRole role : roles){
				LsRole entry = role.getRoleEntry();
				if (entry.getLsType()!= null && entry.getLsType().equalsIgnoreCase("Project")){
					projectThings.addAll(LsThing.findLsThingsByCodeNameEquals(entry.getLsKind()).getResultList());
				}else if (entry.getRoleName().equals(propertiesUtilService.getAcasAdminRole())){
					Collection<LsThing> allProjects = LsThing.findLsThingsByLsTypeEqualsAndLsKindEquals("project", "project").getResultList();
					projectThings.addAll(allProjects);
				}
			}

		}

		return projectThings;

	}

	@Override
	public AuthGroupsAndProjectsDTO getAuthGroupsAndProjects(){
		AuthGroupsAndProjectsDTO agp = new AuthGroupsAndProjectsDTO();
		Collection<AuthProjectGroupsDTO> authProjectsGroups = new ArrayList<AuthProjectGroupsDTO>();
		Collection<AuthGroupsDTO> authGroups = new HashSet<AuthGroupsDTO>() ;
		List<LsRole> allRoles = LsRole.findAllLsRoles();
		AuthGroupsDTO ag;
		Collection<String> members;

		for (LsRole lsRole : allRoles){
			ag = new AuthGroupsDTO();
			members = new HashSet<String>();
			ag.setName(new StringBuilder().append(lsRole.getLsType()).append("_").append(lsRole.getLsKind()).append("_").append(lsRole.getRoleName()).toString());
			Set<AuthorRole> authorRoles = lsRole.getAuthorRoles();
			for (AuthorRole authorRole : authorRoles){
				members.add(authorRole.getUserEntry().getUserName());
			}
			ag.setMembers(members);
			logger.info(ag.toJson());
			authGroups.add(ag);			
		}

		//Collection<LsThing> 
//		Collection<LsThing> projectCollection = LsThing.findLsThingsByLsTypeEqualsAndLsKindEquals("project", "project").getResultList();
		List<LsThing> projectList = LsThing.findLsThingsByLsTypeEqualsAndLsKindEquals("project", "project").getResultList();
		
		Collection<LsThing> projectCollection = new ArrayList<LsThing>(projectList);		
		AuthProjectGroupsDTO authProjectGroup;
		Collection<String> groups;
		List<LsRole> projectRoles;
		Set<LsThingLabel> projectLabels;
		String projectName = null;
		String projectAlias = null;
		boolean active = true;
		boolean isRestricted = true;

		for (LsThing project : projectCollection){
			if (!project.isIgnored()){
				projectRoles = LsRole.findLsRolesByLsTypeEqualsAndLsKindEquals("Project", project.getCodeName()).getResultList();
				groups = new HashSet<String>();
				for (LsRole projectRole : projectRoles){
					groups.add(new StringBuilder().append(projectRole.getLsType()).append("_").append(projectRole.getLsKind()).append("_").append(projectRole.getRoleName()).toString());
				}
				List<LsRole> acasAdminRoles = LsRole.findLsRolesByRoleNameEquals(propertiesUtilService.getAcasAdminRole()).getResultList();
				for (LsRole acasAdminRole : acasAdminRoles){
					groups.add(new StringBuilder().append(acasAdminRole.getLsType()).append("_").append(acasAdminRole.getLsKind()).append("_").append(acasAdminRole.getRoleName()).toString());
				}

				projectLabels = project.getLsLabels();
				for (LsThingLabel projectLabel : projectLabels){
					if (!projectLabel.isIgnored()){
						if (projectLabel.getLsType().equalsIgnoreCase("name")){
							if (projectLabel.getLsKind().equalsIgnoreCase("Project Name")){
								projectName = projectLabel.getLabelText();
							} else if (projectLabel.getLsKind().equalsIgnoreCase("Project Alias")){
								projectAlias = projectLabel.getLabelText();
							}						
						}
					}
				}
				for (LsThingState projectState : project.getLsStates()){
					for (LsThingValue projectValue : projectState.getLsValues()){
						if (!projectValue.isIgnored()){
							if (projectValue.getLsKind().equalsIgnoreCase("project status")){
								if (projectValue.getCodeValue().equalsIgnoreCase("Active")) active = true;
								else if (projectValue.getCodeValue().equalsIgnoreCase("Inactive")) active = false;
							}else if (projectValue.getLsKind().equalsIgnoreCase("is restricted")){
								if (projectValue.getCodeValue().equalsIgnoreCase("true")) isRestricted = true;
								else if (projectValue.getCodeValue().equalsIgnoreCase("false")) isRestricted = false;
							}
						}
					}
				}

				authProjectGroup = new AuthProjectGroupsDTO();
				authProjectGroup.setId(project.getId());				
				authProjectGroup.setCode(project.getCodeName());
				authProjectGroup.setName(projectName);
				authProjectGroup.setAlias(projectAlias);
				authProjectGroup.setActive(active);
				authProjectGroup.setIsRestricted(isRestricted);
				authProjectGroup.setGroups(groups);
				authProjectsGroups.add(authProjectGroup);
			}
		}

		logger.debug(AuthProjectGroupsDTO.toJsonArray(authProjectsGroups));
		agp.setGroups(authGroups);
		agp.setProjects(authProjectsGroups);

		return agp;

	}

	@Override
	public CodeTableDTO getProjectCodeTable(LsThing project) {
		CodeTableDTO codeTable = new CodeTableDTO();
		codeTable.setName(LsThingLabel.pickBestLabel(project.getLsLabels()).getLabelText());
		codeTable.setCode(project.getCodeName());
		codeTable.setIgnored(project.isIgnored());
		return codeTable;
	}


	@Override
	public List<CodeTableDTO> convertProjectsToCodeTables(Collection<LsThing> projects) {
		List<CodeTableDTO> codeTableList = new ArrayList<CodeTableDTO>();
		for (LsThing project : projects) {
			CodeTableDTO codeTable = getProjectCodeTable(project);
			codeTableList.add(codeTable);
		}
		return codeTableList;	
	}


	@Override
	public Collection<Author> findAuthorsByRoleTypeAndRoleKindAndRoleName(
			String roleType, String roleKind, String roleName) {
		List<LsRole> roleEntries = LsRole.findLsRolesByLsTypeEqualsAndLsKindEqualsAndRoleNameEquals(roleType, roleKind, roleName).getResultList();
		Collection<Author> authors = new HashSet<Author>();
		for (LsRole roleEntry : roleEntries){
			Collection<AuthorRole> authorRoles = AuthorRole.findAuthorRolesByRoleEntry(roleEntry).getResultList();
			for (AuthorRole authorRole : authorRoles){
				authors.add(authorRole.getUserEntry());
			}	
		}
		return authors;
	}

	@Override
	@Transactional
	public Author saveAuthor(Author author) {
		if (logger.isDebugEnabled()) logger.debug("incoming meta author: " + author.toJson() + "\n");
		Author newAuthor = new Author(author);
		if (newAuthor.getCodeName() == null){
			newAuthor.setCodeName(autoLabelService.getAuthorCodeName());
		}
		if (newAuthor.getLsType() == null) newAuthor.setLsType("default");
		if (newAuthor.getLsKind() == null) newAuthor.setLsKind("default");
		if (newAuthor.getLsTypeAndKind() == null) newAuthor.setLsTypeAndKind(newAuthor.getLsType()+"_"+newAuthor.getLsKind());
		newAuthor.persist();
		if (author.getLsLabels() != null){
			Set<AuthorLabel> lsLabels = new HashSet<AuthorLabel>();
			for(AuthorLabel authorLabel : author.getLsLabels()){
				AuthorLabel newAuthorLabel = new AuthorLabel(authorLabel);
				newAuthorLabel.setAuthor(newAuthor);
				if (logger.isDebugEnabled()) logger.debug("here is the newAuthorLabel before save: " + newAuthorLabel.toJson());
				newAuthorLabel.persist();
				lsLabels.add(newAuthorLabel);
			}
			newAuthor.setLsLabels(lsLabels);
		} else {
			if (logger.isDebugEnabled()) logger.debug("No author labels to save");	
		}

		if (author.getLsStates() != null){
			Set<AuthorState> lsStates = new HashSet<AuthorState>();
			for(AuthorState lsState : author.getLsStates()){
				AuthorState newLsState = new AuthorState(lsState);
				newLsState.setAuthor(newAuthor);
				if (logger.isDebugEnabled()) logger.debug("here is the newLsState before save: " + newLsState.toJson());
				newLsState.persist();
				if (logger.isDebugEnabled()) logger.debug("persisted the newLsState: " + newLsState.toJson());
				if (lsState.getLsValues() != null){
					Set<AuthorValue> lsValues = new HashSet<AuthorValue>();
					for(AuthorValue authorValue : lsState.getLsValues()){
						if (logger.isDebugEnabled()) logger.debug("authorValue: " + authorValue.toJson());
						AuthorValue newAuthorValue = AuthorValue.create(authorValue);
						newAuthorValue.setLsState(newLsState);
						newAuthorValue.persist();
						lsValues.add(newAuthorValue);
						if (logger.isDebugEnabled()) logger.debug("persisted the authorValue: " + newAuthorValue.toJson());
					}
					newLsState.setLsValues(lsValues);
				} else {
					if (logger.isDebugEnabled()) logger.debug("No author values to save");
				}
				lsStates.add(newLsState);
			}
			newAuthor.setLsStates(lsStates);
		}

		return Author.findAuthor(newAuthor.getId());
	}


	@Override
	public Author updateAuthor(Author author) {
		logger.info("incoming meta author: " + author.toJson() + "\n");
		Author updatedAuthor = Author.update(author);
		if (author.getLsLabels() != null){
			for(AuthorLabel authorLabel : author.getLsLabels()){
				if (authorLabel.getId() == null){
					AuthorLabel newAuthorLabel = new AuthorLabel(authorLabel);
					newAuthorLabel.setAuthor(updatedAuthor);
					newAuthorLabel.persist();
					updatedAuthor.getLsLabels().add(newAuthorLabel);
				} else {
					AuthorLabel updatedLabel = AuthorLabel.update(authorLabel);
					updatedAuthor.getLsLabels().add(updatedLabel);
				}
			}	
		} else {
			logger.info("No author labels to update");	
		}
		updateLsStates(author, updatedAuthor);
		return Author.findAuthor(updatedAuthor.getId());
	}

	public void updateLsStates(Author jsonAuthor, Author updatedAuthor){
		if(jsonAuthor.getLsStates() != null){
			for(AuthorState lsThingState : jsonAuthor.getLsStates()){
				AuthorState updatedAuthorState;
				if (lsThingState.getId() == null){
					updatedAuthorState = new AuthorState(lsThingState);
					updatedAuthorState.setAuthor(updatedAuthor);
					updatedAuthorState.persist();
					updatedAuthor.getLsStates().add(updatedAuthorState);
					if (logger.isDebugEnabled()) logger.debug("persisted new lsThing state " + updatedAuthorState.getId());

				} else {
					updatedAuthorState = AuthorState.update(lsThingState);
					updatedAuthor.getLsStates().add(updatedAuthorState);

					if (logger.isDebugEnabled()) logger.debug("updated lsThing state " + updatedAuthorState.getId());

				}
				if (lsThingState.getLsValues() != null){
					for(AuthorValue lsThingValue : lsThingState.getLsValues()){
						if (lsThingValue.getLsState() == null) lsThingValue.setLsState(updatedAuthorState);
						AuthorValue updatedAuthorValue;
						if (lsThingValue.getId() == null){
							updatedAuthorValue = AuthorValue.create(lsThingValue);
							updatedAuthorValue.setLsState(AuthorState.findAuthorState(updatedAuthorState.getId()));
							updatedAuthorValue.persist();
							updatedAuthorState.getLsValues().add(updatedAuthorValue);
						} else {
							updatedAuthorValue = AuthorValue.update(lsThingValue);
							if (logger.isDebugEnabled()) logger.debug("updated lsThing value " + updatedAuthorValue.getId());
						}
						if (logger.isDebugEnabled()) logger.debug("checking lsThingValue " + updatedAuthorValue.toJson());

					}	
				} else {
					if (logger.isDebugEnabled()) logger.debug("No lsThing values to update");
				}
			}
		}
	}

	@Override
	public Author getOrCreateAuthor(Author author) {
		String userName = author.getUserName();
		try{
			Author foundAuthor = Author.findAuthorsByUserName(userName).getSingleResult();
			return foundAuthor;
		}catch(EmptyResultDataAccessException e){
			logger.debug("Author "+userName+" not found. Creating new author");
			return saveAuthor(author);
		}
	}

}
