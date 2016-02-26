package com.labsynch.labseer.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.dto.CodeLabelDTO;
import com.labsynch.labseer.dto.ContainerRequestDTO;
import com.labsynch.labseer.dto.ContainerErrorMessageDTO;
import com.labsynch.labseer.dto.ContainerLocationDTO;
import com.labsynch.labseer.dto.CreatePlateRequestDTO;
import com.labsynch.labseer.dto.ErrorMessageDTO;
import com.labsynch.labseer.dto.PlateStubDTO;
import com.labsynch.labseer.dto.PlateWellDTO;
import com.labsynch.labseer.dto.PreferredNameRequestDTO;
import com.labsynch.labseer.dto.PreferredNameResultsDTO;
import com.labsynch.labseer.dto.WellContentDTO;
import com.labsynch.labseer.exceptions.ErrorMessage;

@Service
public interface ContainerService {

	Container saveLsContainer(Container container);
	
	Collection<Container> saveLsContainers(Collection<Container> containers);

	Container updateContainer(Container fromJsonToContainer);

	Collection<Container> saveLsContainers(String json);

	Collection<Container> saveLsContainersParse(String json);

	Collection<Container> saveLsContainersFile(String jsonFile);

	Container saveLsContainer(String json);

	Collection<ContainerLocationDTO> getContainersInLocation(
			Collection<String> locationCodeNames);

	Collection<ContainerLocationDTO> getContainersInLocation(
			Collection<String> locationCodeNames, String containerType,
			String containerKind);

	Collection<PlateWellDTO> getWellCodesByPlateBarcodes(
			List<String> plateBarcodes);

	Collection<CodeLabelDTO> getContainerCodesByLabels(
			List<String> labelTexts, String containerType, String containerKind, String labelType, String labelKind);

	Collection<WellContentDTO> getWellContent(Collection<ContainerRequestDTO> wellCodes);

	ArrayList<ErrorMessage> validateContainer(Container container);

	PreferredNameResultsDTO getCodeNameFromName(String containerType,
			String containerKind, String labelType, String labelKind,
			PreferredNameRequestDTO requestDTO);

	String pickBestLabel(Container container);

	Collection<ContainerErrorMessageDTO> throwInTrash(
			Collection<ContainerRequestDTO> containersToTrash) throws Exception;

	Collection<ContainerErrorMessageDTO> updateAmountInWell(
			Collection<ContainerRequestDTO> wellsToUpdate);

	PlateStubDTO createPlate(CreatePlateRequestDTO plateRequest) throws Exception;

	Collection<WellContentDTO> getWellContentByPlateBarcode(String plateBarcode);

	Collection<ContainerErrorMessageDTO> updateWellStatus(
			Collection<WellContentDTO> wellsToUpdate);

	PlateStubDTO getPlateTypeByPlateBarcode(String plateBarcode);

	Collection<ContainerErrorMessageDTO> getContainersByCodeNames(List<String> codeNames);

	Collection<ContainerErrorMessageDTO> getDefinitionContainersByContainerCodeNames(
			List<String> codeNames);
	
	
}
