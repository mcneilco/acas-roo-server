package com.labsynch.labseer.service;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.ItxLsThingLsThing;
import com.labsynch.labseer.dto.CodeTableDTO;

@Service
public interface ItxLsThingLsThingService {

	ItxLsThingLsThing saveItxLsThingLsThing(ItxLsThingLsThing itxLsThingLsThing);

	ItxLsThingLsThing updateItxLsThingLsThing(ItxLsThingLsThing jsonItxLsThingLsThing);

	Collection<CodeTableDTO> convertToCodeTables(Collection<ItxLsThingLsThing> itxLsThingLsThings);

	
	
}
