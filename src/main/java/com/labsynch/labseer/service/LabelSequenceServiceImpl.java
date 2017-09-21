package com.labsynch.labseer.service;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.domain.AuthorRole;
import com.labsynch.labseer.domain.LabelSequence;
import com.labsynch.labseer.domain.LabelSequenceRole;
import com.labsynch.labseer.domain.LsRole;


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
				savedLabelSequence.save();
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

	@Override
	public List<LabelSequence> getAuthorizedLabelSequences(String userName, String thingTypeAndKind,
			String labelTypeAndKind) {
		List<LabelSequence> allLabelSequences;
		List<LabelSequence> authorizedLabelSequences = new ArrayList<LabelSequence>();
		if (thingTypeAndKind != null && labelTypeAndKind != null) {
			allLabelSequences = LabelSequence.findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEquals(thingTypeAndKind, labelTypeAndKind).getResultList();
		}else if (thingTypeAndKind != null) {
			allLabelSequences = LabelSequence.findLabelSequencesByThingTypeAndKindEquals(thingTypeAndKind).getResultList();
		}else if (labelTypeAndKind != null) {
			allLabelSequences = LabelSequence.findLabelSequencesByLabelTypeAndKindEquals(labelTypeAndKind).getResultList();
		}else {
			allLabelSequences = LabelSequence.findAllLabelSequences();
		}
		Author author = Author.findAuthorsByUserName(userName).getSingleResult();
		List<LsRole> authorRoles = new ArrayList<LsRole>();
		for (AuthorRole authorRole : author.getAuthorRoles()) {
			authorRoles.add(authorRole.getRoleEntry());
		}
		for (LabelSequence labelSequence : allLabelSequences) {
			List<LsRole> labelSeqRoles = new ArrayList<LsRole>();
			if (labelSequence.getLabelSequenceRoles() != null && !labelSequence.getLabelSequenceRoles().isEmpty()) {
				for (LabelSequenceRole labelSeqRole : labelSequence.getLabelSequenceRoles()) {
					labelSeqRoles.add(labelSeqRole.getRoleEntry());
				}
				if (anyRolesMatch(authorRoles, labelSeqRoles)){
					authorizedLabelSequences.add(labelSequence);
				}
			}else {
				authorizedLabelSequences.add(labelSequence);
			}
		}
		
		return authorizedLabelSequences;
	}
	
	public static boolean containsLsRole(Collection<LsRole> list, LsRole queryRole) {
		for (LsRole role : list) {
			if (role != null && queryRole != null && role.getId().equals(queryRole.getId())) return true;
		}
		return false;
	}
	
	public static boolean anyRolesMatch(Collection<LsRole> roleList, Collection<LsRole> queryRoleList) {
		for (LsRole role : queryRoleList) {
			if (containsLsRole(roleList, role)) return true;
		}
		return false;
	}

	

}
