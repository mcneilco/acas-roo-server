package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.dto.LotDTO;
import com.labsynch.labseer.dto.LotsByProjectDTO;


public interface LotService {

	public Lot updateLotWeight(Lot lot);

	Lot updateLotMeta(LotDTO lotDTO);

	String updateLotMetaArray(String jsonArray);

	Lot reparentLot(String lotCorpName, String parentCorpName, String modifiedByUser);

	public Collection<LotsByProjectDTO> getLotsByProjectsList(List<String> projects);

}
