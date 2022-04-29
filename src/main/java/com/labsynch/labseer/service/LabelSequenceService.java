package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.AuthorRole;
import com.labsynch.labseer.domain.LabelSequence;

import org.springframework.stereotype.Service;

@Service
public interface LabelSequenceService {

	Collection<LabelSequence> saveLabelSequenceArray(
			Collection<LabelSequence> labelSequences);

	List<LabelSequence> getAuthorizedLabelSequences(Collection<AuthorRole> authorRoles, String thingTypeAndKind,
			String labelTypeAndKind);

}
