package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.dto.CmpdRegBatchCodeDTO;
import com.labsynch.labseer.dto.LotDTO;
import com.labsynch.labseer.dto.LotsByProjectDTO;
import com.labsynch.labseer.dto.PreferredNameDTO;
import com.labsynch.labseer.dto.ReparentLotResponseDTO;
import com.labsynch.labseer.exceptions.DupeLotException;
import com.labsynch.labseer.service.LotServiceImpl.LotOrphanLevel;

public interface LotService {

	public Lot updateLotWeight(Lot lot);

	Lot updateLotMeta(LotDTO lotDTO);

	String updateLotMetaArray(String jsonArray);

	ReparentLotResponseDTO reparentLot(String lotCorpName, String parentCorpName, String modifiedByUser, Boolean updateInventory, Boolean updateAssayData) throws DupeLotException;

	public Collection<LotsByProjectDTO> getLotsByProjectsList(List<String> projects);

	String generateSaltFormLotName(Lot lot);

	String appendSaltCode(String corpName, Lot lot);

	String generateParentLotName(Lot lot);

	String generateCasStyleLotName(Lot lot);

	int generateSaltFormLotNumber(Lot lot);

	int generateCasCheckDigit(String inputCasLabel);

	String generateCorpName(Lot lot);

	int generateLotNumber(Lot lot);

	int generateParentLotNumber(Lot lot);

	public Collection<PreferredNameDTO> getPreferredNames(Collection<PreferredNameDTO> preferredNameDTOs);

	public CmpdRegBatchCodeDTO checkForDependentData(Set<String> lotCorpNames);

	public void deleteLots(Collection<Lot> lots);

	public void deleteLot(Lot lot);

	public LotOrphanLevel checkLotOrphanLevel(Lot lot);

}
