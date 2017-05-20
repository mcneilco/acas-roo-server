package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.LsThingValue;
import com.labsynch.labseer.dto.GenericValuePathRequest;
import com.labsynch.labseer.dto.LsThingValuePathDTO;

public interface LsThingValueService {

	LsThingValue updateLsThingValue(String idOrCodeName, String stateType,
			String stateKind, String valueType, String valueKind, String value);

	LsThingValue saveLsThingValue(LsThingValue lsThingValue);

	LsThingValue updateLsThingValue(LsThingValue lsThingValue);

	List<LsThingValue> getLsThingValuesByLsThingId(Long id);

	List<LsThingValue> getLsThingValuesByLsThingIdAndStateTypeKindAndValueTypeKind(
			Long lsThingId, String stateType, String stateKind,
			String valueType, String valueKind);

	Collection<LsThingValue> saveLsThingValues(
			Collection<LsThingValue> lsThingValues);

	Collection<LsThingValue> updateLsThingValues(
			Collection<LsThingValue> lsThingValues);

	LsThingValue getLsThingValue(String idOrCodeName, String stateType,
			String stateKind, String valueType, String valueKind);

	Collection<LsThingValuePathDTO> getLsThingValues(
			Collection<GenericValuePathRequest> genericRequests);


}
