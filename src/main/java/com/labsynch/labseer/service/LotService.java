package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.dto.LotDTO;
import com.labsynch.labseer.dto.LotsByProjectDTO;
import com.labsynch.labseer.dto.PreferredNameDTO;

public interface LotService {

	public Lot updateLotWeight(Lot lot);

	Lot updateLotMeta(LotDTO lotDTO);

	String updateLotMetaArray(String jsonArray);

	Lot reparentLot(String lotCorpName, String parentCorpName, String modifiedByUser);

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

}
