package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.LabelSequence;
import com.labsynch.labseer.domain.AuthorRole;

@Service
public interface LabelSequenceService {

	Collection<LabelSequence> saveLabelSequenceArray(
			Collection<LabelSequence> labelSequences);

	List<LabelSequence> getAuthorizedLabelSequences(Collection<AuthorRole> authorRoles, String thingTypeAndKind,
			String labelTypeAndKind);

}
