package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.LsThingState;

public interface LsThingStateService {

	List<LsThingState> getLsThingStatesByLsThingIdAndStateTypeKind(
			Long lsThingId, String stateType, String stateKind);

	Collection<LsThingState> ignoreAllLsThingStates(
			Collection<LsThingState> lsThingStates);

	LsThingState createLsThingStateByLsThingIdAndStateTypeKind(Long lsThingId,
			String stateType, String stateKind);

	LsThingState saveLsThingState(LsThingState lsThingState);

	Collection<LsThingState> saveLsThingStates(
			Collection<LsThingState> lsThingStates);

}
