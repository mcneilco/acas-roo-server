package com.labsynch.labseer.service;

import java.util.Collection;

import com.labsynch.labseer.domain.Project;
import com.labsynch.labseer.dto.SearchFormReturnDTO;


public interface ProjectService {

	Collection<Project> getACASProjectsByUser(String userName);

	SearchFormReturnDTO filterSearchResultsByProject(
			SearchFormReturnDTO searchResults, String loggedInUser);



}
