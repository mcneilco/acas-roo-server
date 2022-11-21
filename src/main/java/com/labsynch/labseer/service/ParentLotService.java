package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.ParentLotCodeDTO;
import com.labsynch.labseer.dto.ParentLotAssayDTO;

public interface ParentLotService {

	public Collection<CodeTableDTO> getCodeTableLotsByParentCorpName(String parentCorpName);

	public Collection<ParentLotCodeDTO> getLotCodesByParentAlias(Collection<ParentLotCodeDTO> requestDTOs);

	public Collection<Lot> getLotsByParentCorpName(String parentCorpName);

	public void deleteLot(Lot lot);

	public List<ParentLotAssayDTO> getAssayLinkedToParentLot(String corporateName);

}
