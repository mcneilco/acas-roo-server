package com.labsynch.labseer.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.ItxSubjectContainer;

@Service
public interface ItxSubjectContainerService {

	@Transactional
	ItxSubjectContainer saveLsItxSubjectContainer(ItxSubjectContainer itxSubjectContainer);

	ItxSubjectContainer updateItxSubjectContainer(
			ItxSubjectContainer jsonItxSubjectContainer);
	
	
	
}
