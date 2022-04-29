package com.labsynch.labseer.service;

import java.util.Collection;

import com.labsynch.labseer.domain.ItxLsThingLsThing;
import com.labsynch.labseer.dto.CodeTableDTO;

import org.springframework.stereotype.Service;

@Service
public interface ItxLsThingLsThingService {

	ItxLsThingLsThing saveItxLsThingLsThing(ItxLsThingLsThing itxLsThingLsThing);

	ItxLsThingLsThing updateItxLsThingLsThing(ItxLsThingLsThing jsonItxLsThingLsThing);

	Collection<CodeTableDTO> convertToCodeTables(Collection<ItxLsThingLsThing> itxLsThingLsThings);

}
