package com.labsynch.labseer.service;

import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.dto.LotDTO;


public interface LotService {

	public Lot updateLotWeight(Lot lot);

	Lot updateLotMeta(LotDTO lotDTO);

	String updateLotMetaArray(String jsonArray);

	Lot reparentLot(String lotCorpName, String parentCorpName, String modifiedByUser);


}