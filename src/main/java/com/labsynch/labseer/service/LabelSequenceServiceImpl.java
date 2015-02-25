package com.labsynch.labseer.service;


import java.util.Collection;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.LabelSequence;


@Service
@Transactional
public class LabelSequenceServiceImpl implements LabelSequenceService {

	private static final Logger logger = LoggerFactory.getLogger(LabelSequenceServiceImpl.class);

	@Override
	public Collection<LabelSequence> saveLabelSequenceArray(
			Collection<LabelSequence> labelSequences) {
		Collection<LabelSequence> savedLabelSequences = new HashSet<LabelSequence>();
		for(LabelSequence labelSequence : labelSequences){
			LabelSequence savedLabelSequence;
			try{
				savedLabelSequence = LabelSequence.findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEquals(labelSequence.getThingTypeAndKind(), labelSequence.getLabelTypeAndKind()).getSingleResult();
			}catch(EmptyResultDataAccessException e){
				savedLabelSequence = labelSequence;
				savedLabelSequence.persist();
			}
			if (!savedLabelSequence.getLabelPrefix().equals(labelSequence.getLabelPrefix())){
				logger.info("Changing LabelSequence labelPrefix from: " + savedLabelSequence.getLabelPrefix() + " to: "+ labelSequence.getLabelPrefix());
				savedLabelSequence.setLabelPrefix(labelSequence.getLabelPrefix());
				savedLabelSequence.merge();
			}
			savedLabelSequences.add(savedLabelSequence);
		}
		return savedLabelSequences;
	}

	

}
