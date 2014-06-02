package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.dto.AnalysisGroupValueDTO;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.ExperimentFilterDTO;
import com.labsynch.labseer.dto.ExperimentSearchRequestDTO;
import com.labsynch.labseer.dto.JSTreeNodeDTO;
import com.labsynch.labseer.exceptions.UniqueExperimentNameException;

@Service
public interface ExperimentService {

	public Experiment saveLsExperiment(Experiment experiment) throws UniqueExperimentNameException;

	public void deleteLsExperiment(Experiment experiment);

	public Experiment updateExperiment(Experiment experiment);

	public Experiment getFullExperiment(Experiment queryExperiment);

	Collection<JSTreeNodeDTO> getExperimentNodes(Collection<String> codeValues);



	List<AnalysisGroupValueDTO> getFilteredAGData(ExperimentSearchRequestDTO searchRequest);

	Collection<ExperimentFilterDTO> getExperimentFilters(Collection<String> experimentCodes);


	Collection<JSTreeNodeDTO> getExperimentNodesByProtocolTree(
			Collection<String> codeValues);

	
}
