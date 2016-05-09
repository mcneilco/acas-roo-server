package com.labsynch.labseer.service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.domain.AuthorRole;
import com.labsynch.labseer.domain.DDictValue;
import com.labsynch.labseer.domain.LsRole;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.LsThingLabel;
import com.labsynch.labseer.domain.LsThingState;
import com.labsynch.labseer.domain.LsThingValue;
import com.labsynch.labseer.dto.AuthGroupsAndProjectsDTO;
import com.labsynch.labseer.dto.AuthGroupsDTO;
import com.labsynch.labseer.dto.AuthProjectGroupsDTO;
import com.labsynch.labseer.dto.AutoLabelDTO;
import com.labsynch.labseer.dto.CodeTableDTO;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

	@Autowired
	private AutoLabelService autoLabelService;

	private static final Logger logger = LoggerFactory.getLogger(AuthorServiceImpl.class);
	

	@Override
	public CodeTableDTO getAuthorCodeTable(Author author) {
		CodeTableDTO codeTable = new CodeTableDTO();
		codeTable.setName(author.getFirstName() + " " + author.getLastName());
		codeTable.setCode(author.getUserName());
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
		Author author = Author.findAuthorsByUserName(userName).getSingleResult();
		Set<AuthorRole> roles = author.getAuthorRoles();
		for (AuthorRole role : roles){
			LsRole entry = role.getRoleEntry();
			if (entry.getLsType()!= null && entry.getLsType().equalsIgnoreCase("Project")){
				projectThings.addAll(LsThing.findLsThingsByCodeNameEquals(entry.getLsKind()).getResultList());
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
			authGroups.add(ag);			
		}
		
		Collection<LsThing> projectCollection = LsThing.findLsThingsByLsTypeEqualsAndLsKindEquals("project", "project").getResultList();
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
						if (projectValue.getLsKind().equalsIgnoreCase("project status")){
							if (projectValue.getCodeValue().equalsIgnoreCase("Active")) active = true;
							else if (projectValue.getCodeValue().equalsIgnoreCase("Inactive")) active = false;
						}else if (projectValue.getLsKind().equalsIgnoreCase("is restricted")){
							if (projectValue.getCodeValue().equalsIgnoreCase("true")) isRestricted = true;
							else if (projectValue.getCodeValue().equalsIgnoreCase("false")) isRestricted = false;
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
}
