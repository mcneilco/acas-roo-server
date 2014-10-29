package com.labsynch.labseer.service;

import java.util.List;

import com.labsynch.labseer.domain.LsThingValue;

public interface LsThingValueService {

	LsThingValue updateLsThingValue(String idOrCodeName, String stateType,
			String stateKind, String valueType, String valueKind, String value);

	LsThingValue saveLsThingValue(LsThingValue lsThingValue);

	LsThingValue updateLsThingValue(LsThingValue lsThingValue);

	List<LsThingValue> getLsThingValuesByLsThingId(Long id);

	List<LsThingValue> getLsThingValuesByLsThingIdAndStateTypeKindAndValueTypeKind(
			Long lsThingId, String stateType, String stateKind,
			String valueType, String valueKind);


}
