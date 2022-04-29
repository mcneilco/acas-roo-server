package com.labsynch.labseer.service;

import com.labsynch.labseer.domain.ItxSubjectContainer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public interface ItxSubjectContainerService {

	@Transactional
	ItxSubjectContainer saveLsItxSubjectContainer(ItxSubjectContainer itxSubjectContainer);

	ItxSubjectContainer updateItxSubjectContainer(
			ItxSubjectContainer jsonItxSubjectContainer);
	
	
	
}
