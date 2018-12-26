package com.labsynch.labseer.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.domain.Project;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.SearchCompoundReturnDTO;
import com.labsynch.labseer.dto.SearchFormReturnDTO;
import com.labsynch.labseer.dto.SearchLotDTO;
import com.labsynch.labseer.dto.configuration.MainConfigDTO;
import com.labsynch.labseer.utils.Configuration;

@Service
public class ProjectServiceImpl implements ProjectService {

	Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

	public static final MainConfigDTO mainConfig = Configuration.getConfigInfo();
	
	@Override
	@Transactional
	public Collection<Project> getACASProjectsByUser(String userName) {
		// TODO Fetch list of projects from ACAS by user
		Collection<CodeTableDTO> acasProjects = null;
		try{
			acasProjects = getProjectsFromACAS(userName);
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		// Compare list of ACAS projects with current CmpdReg list of projects
		// If ACAS projects are found with no matching-code CmpdReg project, create new CmpdReg projects
		// Return corresponding CmpdReg projects
		Collection<Project> convertedProjects = getOrCreateProjects(acasProjects);
		
		return convertedProjects;
	}
	
	private Collection<CodeTableDTO> getProjectsFromACAS(String userName) throws MalformedURLException, IOException{
		String url = mainConfig.getServerConnection().getAcasAppURL()+"projects/"+userName;
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setRequestMethod("GET");
		InputStream input = connection.getInputStream();
		byte[] bytes = IOUtils.toByteArray(input);
		String responseJson = new String(bytes);
		logger.debug(responseJson);
		return CodeTableDTO.fromJsonArrayToCoes(responseJson);
		}
	
	private Collection<Project> getOrCreateProjects(Collection<CodeTableDTO> acasProjects){
		Collection<Project> projects = new ArrayList<Project>();
		for(CodeTableDTO acasProject : acasProjects){
			boolean projectExists = false;
			for(Project project : Project.findAllProjects()){
				if (project.getCode().equals(acasProject.getCode())){
					projectExists=true;
					projects.add(project);
				}
			}
			if (!projectExists){
				Project newProject = new Project();
				newProject.setCode(acasProject.getCode());
				newProject.setName(acasProject.getName());
				newProject.persist();
				projects.add(newProject);
			}
		}
		return projects;
	}
	
	@Override
	public SearchFormReturnDTO filterSearchResultsByProject(SearchFormReturnDTO searchResults, String loggedInUser){
		if (!searchResults.getFoundCompounds().isEmpty()){
			Collection<Project> authorizedProjects = getACASProjectsByUser(loggedInUser);
			Collection<SearchCompoundReturnDTO> filteredFoundCompounds = new HashSet<SearchCompoundReturnDTO>();
			for (SearchCompoundReturnDTO foundCompound : searchResults.getFoundCompounds()){
				List<SearchLotDTO> filteredFoundLots = new ArrayList<SearchLotDTO>();
				for (SearchLotDTO foundLot : foundCompound.getLotIDs()){
					String lotProject = Lot.findLotsByCorpNameEquals(foundLot.getCorpName()).getSingleResult().getProject();
					if(authorizedProjects.contains(lotProject)) filteredFoundLots.add(foundLot);
				}
				if (!filteredFoundLots.isEmpty()){
					foundCompound.setLotIDs(filteredFoundLots);
					filteredFoundCompounds.add(foundCompound);
				}
			}
			if (filteredFoundCompounds.isEmpty()) searchResults.setLotsWithheld(true);
			searchResults.setFoundCompounds(filteredFoundCompounds);
		}
		return searchResults;
	}

}

