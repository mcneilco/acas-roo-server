package com.labsynch.labseer.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.ContainerLabel;
import com.labsynch.labseer.domain.ContainerState;
import com.labsynch.labseer.domain.ContainerValue;
import com.labsynch.labseer.domain.ItxContainerContainer;
import com.labsynch.labseer.domain.LsTransaction;
import com.labsynch.labseer.dto.CodeLabelDTO;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.ContainerBatchCodeDTO;
import com.labsynch.labseer.dto.ContainerBrowserQueryDTO;
import com.labsynch.labseer.dto.ContainerCodeNameStateDTO;
import com.labsynch.labseer.dto.ContainerDependencyCheckDTO;
import com.labsynch.labseer.dto.ContainerErrorMessageDTO;
import com.labsynch.labseer.dto.ContainerLocationDTO;
import com.labsynch.labseer.dto.ContainerLocationTreeDTO;
import com.labsynch.labseer.dto.ContainerQueryDTO;
import com.labsynch.labseer.dto.ContainerRequestDTO;
import com.labsynch.labseer.dto.ContainerSearchRequestDTO;
import com.labsynch.labseer.dto.ContainerValueRequestDTO;
import com.labsynch.labseer.dto.ContainerWellCodeDTO;
import com.labsynch.labseer.dto.CreatePlateRequestDTO;
import com.labsynch.labseer.dto.PlateStubDTO;
import com.labsynch.labseer.dto.PlateWellDTO;
import com.labsynch.labseer.dto.PreferredNameRequestDTO;
import com.labsynch.labseer.dto.PreferredNameResultsDTO;
import com.labsynch.labseer.dto.WellContentDTO;
import com.labsynch.labseer.exceptions.ErrorMessage;

import org.springframework.stereotype.Service;

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
			List<String> labelTexts, String containerType, String containerKind, String labelType, String labelKind,
			Boolean like, Boolean likeRight);

	ContainerDependencyCheckDTO checkDependencies(Container container);

	Collection<WellContentDTO> getWellContent(List<String> wellCodes);

	ArrayList<ErrorMessage> validateContainer(Container container);

	PreferredNameResultsDTO getCodeNameFromName(String containerType,
			String containerKind, String labelType, String labelKind,
			PreferredNameRequestDTO requestDTO);

	String pickBestLabel(Container container);

	Collection<ContainerErrorMessageDTO> throwInTrash(
			Collection<ContainerRequestDTO> containersToTrash) throws Exception;

	Collection<ContainerErrorMessageDTO> updateAmountInWell(
			Collection<ContainerRequestDTO> wellsToUpdate);

	PlateStubDTO createPlate(CreatePlateRequestDTO plateRequest, String containerKind) throws Exception;

	PlateStubDTO createTube(CreatePlateRequestDTO plateRequest) throws Exception;

	Collection<PlateStubDTO> createTubes(Collection<CreatePlateRequestDTO> plateRequests) throws Exception;

	PlateStubDTO createPlate(CreatePlateRequestDTO plateRequest) throws Exception;

	Collection<PlateStubDTO> createPlates(Collection<CreatePlateRequestDTO> plateRequests) throws Exception;

	Collection<WellContentDTO> getWellContentByPlateBarcode(String plateBarcode);

	Collection<ContainerErrorMessageDTO> updateWellContent(
			Collection<WellContentDTO> wellsToUpdate, boolean copyPreviousValues) throws Exception;

	PlateStubDTO getPlateTypeByPlateBarcode(String plateBarcode);

	Collection<ContainerErrorMessageDTO> getContainersByCodeNames(List<String> codeNames);

	Collection<ContainerErrorMessageDTO> getDefinitionContainersByContainerCodeNames(
			List<String> codeNames);

	Collection<ContainerWellCodeDTO> getWellCodesByContainerCodes(
			List<String> codeNames);

	Collection<ContainerLocationDTO> moveToLocation(
			Collection<ContainerLocationDTO> requests);

	List<Long> insertContainerStates(List<ContainerState> states)
			throws SQLException;

	void ignoreContainerStates(List<ContainerState> states)
			throws SQLException;

	List<Long> insertContainerValues(List<ContainerValue> values)
			throws SQLException;

	List<Long> insertContainers(List<Container> containers) throws SQLException;

	List<Long> insertContainerLabels(List<ContainerLabel> labels)
			throws SQLException;

	List<Long> insertItxContainerContainers(
			List<ItxContainerContainer> itxContainerContainers)
			throws SQLException;

	List<CodeTableDTO> convertToCodeTables(List<Container> containers);

	Collection<Container> searchContainers(
			ContainerSearchRequestDTO searchRequest);

	void ignoreItxContainerContainers(
			List<ItxContainerContainer> itxContainerContainers)
			throws SQLException;

	Collection<String> getContainersByContainerValue(
			ContainerValueRequestDTO requestDTO, Boolean like, Boolean likeRight) throws Exception;

	Collection<Long> searchContainerIdsByBrowserQueryDTO(
			ContainerBrowserQueryDTO query) throws Exception;

	Collection<Container> getContainersByIds(Collection<Long> containerIds);

	Collection<Long> searchContainerIdsByQueryDTO(ContainerQueryDTO query)
			throws Exception;

	Collection<CodeTableDTO> convertToCodeTables(Collection<Container> results, String labelType);

	Collection<CodeTableDTO> convertToCodeTables(Collection<Container> containers);

	Collection<ContainerBatchCodeDTO> getContainerDTOsByBatchCodes(List<String> batchCodes);

	void logicalDeleteContainerArray(Collection<Container> foundContainers);

	void logicalDeleteContainerCodesArray(List<String> containerCodes);

	List<ContainerLocationTreeDTO> getLocationTreeByRootLabel(String rootLabel, Boolean withContainers)
			throws SQLException;

	List<ContainerLocationTreeDTO> getLocationCodeByLabelBreadcrumbByRecursiveQuery(String rootLabel,
			List<String> breadcrumbList) throws SQLException;

	Container getOrCreateTrash(String recordedBy) throws Exception;

	Container getOrCreateBench(String recordedBy, LsTransaction lsTransaction) throws SQLException;

	List<ContainerLocationTreeDTO> getLocationTreeDTO(String rootLabel, String rootCodeName,
			List<String> breadcrumbList, Boolean withContainers) throws SQLException;

	List<ContainerLocationTreeDTO> getLocationTreeByRootCodeName(String rootCodeName, Boolean withContainers)
			throws SQLException;

	List<ContainerCodeNameStateDTO> saveContainerCodeNameStateDTOArray(
			List<ContainerCodeNameStateDTO> stateDTOs) throws SQLException;

	int renameBatchCode(String oldCode, String newCode, String modifiedByUser, Long transactionId);

}
