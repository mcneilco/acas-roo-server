package com.labsynch.labseer.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.LabelSequence;
import com.labsynch.labseer.dto.AutoLabelDTO;
import com.labsynch.labseer.dto.LabelSequenceDTO;

@Service
@Transactional
public class AutoLabelServiceImpl implements AutoLabelService {

	private static final Logger logger = LoggerFactory.getLogger(AutoLabelServiceImpl.class);
	
	@Override
	public List<AutoLabelDTO> getAutoLabels(String json) {
		
        LabelSequenceDTO lsDTO = LabelSequenceDTO.fromJsonToLabelSequenceDTO(json);
        logger.info("incoming label seq: " + lsDTO.toJson());
		
		return getAutoLabels(lsDTO.getThingTypeAndKind(), lsDTO.getLabelTypeAndKind(), lsDTO.getNumberOfLabels());
	}
	
	@Override
	public List<AutoLabelDTO> getAutoLabels(String thingTypeAndKind, String labelTypeAndKind, Long numberOfLabels) throws NonUniqueResultException {
		    		
            List<LabelSequence> labelSequences = LabelSequence.findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEquals(thingTypeAndKind, labelTypeAndKind).getResultList();
            LabelSequence labelSequence;
            if(labelSequences.size() == 0) {
            	logger.info("Label sequence does not exist! Creating new Sequence.");
            	labelSequence = new LabelSequence();
            	labelSequence.setDigits(6);
            	labelSequence.setGroupDigits(false);
            	labelSequence.setIgnored(false);
            	labelSequence.setLabelPrefix(generateLabelPrifix(thingTypeAndKind));
            	labelSequence.setLabelSeparator("-");
            	labelSequence.setLabelTypeAndKind(labelTypeAndKind);
            	labelSequence.setLatestNumber(0L);
            	labelSequence.setModifiedDate((new Date()));
            	labelSequence.setThingTypeAndKind(thingTypeAndKind);
            	labelSequence.setVersion(0);
            	labelSequence.persist();
            } else if (labelSequences.size() != 1) {
                logger.info("found duplicate sequences!!!");
                throw new NonUniqueResultException();
            } else {
                 labelSequence = LabelSequence.findLabelSequence(labelSequences.get(0).getId());           	
            }
    		
            long currentLastNumber = labelSequence.getLatestNumber();
            
            labelSequence.setLatestNumber(labelSequence.getLatestNumber() + numberOfLabels);
            labelSequence.setModifiedDate(new Date());
            labelSequence.merge();
            
            long startingNumber = currentLastNumber + 1L;
            long endingNumber = currentLastNumber + numberOfLabels;

            long labelNumber = startingNumber;
            String formatLabelNumber = "%";
            formatLabelNumber = formatLabelNumber.concat("0").concat(labelSequence.getDigits().toString()).concat("d");
            logger.info("format corpNumber: " + formatLabelNumber);
            List<AutoLabelDTO> autoLabels = new ArrayList<AutoLabelDTO>();
            while (labelNumber <= endingNumber) {
                String label = labelSequence.getLabelPrefix().concat(labelSequence.getLabelSeparator()).concat(String.format(formatLabelNumber, labelNumber));
                logger.info("new label: " + label);
                AutoLabelDTO autoLabel = new AutoLabelDTO();
                autoLabel.setAutoLabel(label);
                autoLabels.add(autoLabel);
                labelNumber++;
            }
    		
    		return autoLabels;
    	}
	
	private String generateLabelPrifix(String thingTypeAndKind) {
		String kindSegment = thingTypeAndKind.split("_")[1];
		String labelPrefix = kindSegment.substring(0, 1);
		if(kindSegment.contains("_")) {
			labelPrefix = labelPrefix + kindSegment.split("_")[1].substring(0,1);
		}
		if(kindSegment.contains(" ")) {
			labelPrefix = labelPrefix + kindSegment.split(" ")[1].substring(0,1);
		}
		return labelPrefix.toUpperCase();
	}

}
