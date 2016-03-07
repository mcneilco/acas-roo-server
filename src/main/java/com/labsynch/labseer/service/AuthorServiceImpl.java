package com.labsynch.labseer.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.domain.AuthorLabel;
import com.labsynch.labseer.domain.AuthorRole;
import com.labsynch.labseer.domain.AuthorState;
import com.labsynch.labseer.domain.AuthorValue;
import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.ContainerLabel;
import com.labsynch.labseer.domain.ContainerState;
import com.labsynch.labseer.domain.ContainerValue;
import com.labsynch.labseer.domain.ItxContainerContainer;
import com.labsynch.labseer.domain.LsRole;
import com.labsynch.labseer.dto.CodeTableDTO;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

	@Autowired
	private AutoLabelService autoLabelService;

	private static final Logger logger = LoggerFactory.getLogger(AuthorServiceImpl.class);
	

	@Override
	public CodeTableDTO getAuthorCodeTable(Author author) {
		CodeTableDTO codeTable = new CodeTableDTO();
		codeTable.setName(author.getFirstName() + " " + author.getLastName());
		codeTable.setCode(author.getUserName());
		codeTable.setIgnored(false);
		return codeTable;
	}


	@Override
	public List<CodeTableDTO> convertToCodeTables(List<Author> authors) {
		List<CodeTableDTO> codeTableList = new ArrayList<CodeTableDTO>();
		for (Author author : authors) {
			CodeTableDTO codeTable = getAuthorCodeTable(author);
			codeTableList.add(codeTable);
		}
		return codeTableList;	
	}
	
	@Override
	public Collection<Author> findAuthorsByAuthorRoleName(String authorRoleName){
		LsRole roleEntry = LsRole.findLsRolesByRoleNameEquals(authorRoleName).getSingleResult();
		Collection<AuthorRole> authorRoles = AuthorRole.findAuthorRolesByRoleEntry(roleEntry).getResultList();
		Collection<Author> authors = new HashSet<Author>();
		for (AuthorRole authorRole : authorRoles){
			authors.add(authorRole.getUserEntry());
		}
		return authors;
	}


	@Override
	public Author saveAuthor(Author author) {
		if (logger.isDebugEnabled()) logger.debug("incoming meta author: " + author.toJson() + "\n");
		Author newAuthor = new Author(author);
		if (newAuthor.getCodeName() == null){
			if (newAuthor.getLsTypeAndKind() == null) newAuthor.setLsTypeAndKind(newAuthor.getLsType()+"_"+newAuthor.getLsKind());
			newAuthor.setCodeName(autoLabelService.getAuthorCodeName());
		}
		newAuthor.persist();
		if (author.getLsLabels() != null){
			Set<AuthorLabel> lsLabels = new HashSet<AuthorLabel>();
			for(AuthorLabel authorLabel : author.getLsLabels()){
				AuthorLabel newAuthorLabel = new AuthorLabel(authorLabel);
				newAuthorLabel.setAuthor(newAuthor);
				if (logger.isDebugEnabled()) logger.debug("here is the newAuthorLabel before save: " + newAuthorLabel.toJson());
				newAuthorLabel.persist();
				lsLabels.add(newAuthorLabel);
			}
			newAuthor.setLsLabels(lsLabels);
		} else {
			if (logger.isDebugEnabled()) logger.debug("No author labels to save");	
		}

		if (author.getLsStates() != null){
			Set<AuthorState> lsStates = new HashSet<AuthorState>();
			for(AuthorState lsState : author.getLsStates()){
				AuthorState newLsState = new AuthorState(lsState);
				newLsState.setAuthor(newAuthor);
				if (logger.isDebugEnabled()) logger.debug("here is the newLsState before save: " + newLsState.toJson());
				newLsState.persist();
				if (logger.isDebugEnabled()) logger.debug("persisted the newLsState: " + newLsState.toJson());
				if (lsState.getLsValues() != null){
					Set<AuthorValue> lsValues = new HashSet<AuthorValue>();
					for(AuthorValue authorValue : lsState.getLsValues()){
						if (logger.isDebugEnabled()) logger.debug("authorValue: " + authorValue.toJson());
						AuthorValue newAuthorValue = AuthorValue.create(authorValue);
						newAuthorValue.setLsState(newLsState);
						newAuthorValue.persist();
						lsValues.add(newAuthorValue);
						if (logger.isDebugEnabled()) logger.debug("persisted the authorValue: " + newAuthorValue.toJson());
					}
					newLsState.setLsValues(lsValues);
				} else {
					if (logger.isDebugEnabled()) logger.debug("No author values to save");
				}
				lsStates.add(newLsState);
			}
			newAuthor.setLsStates(lsStates);
		}
		
		return Author.findAuthor(newAuthor.getId());
	}


	@Override
	public Author updateAuthor(Author author) {
		logger.info("incoming meta author: " + author.toJson() + "\n");
		Author updatedAuthor = Author.update(author);
		if (author.getLsLabels() != null){
			for(AuthorLabel authorLabel : author.getLsLabels()){
				if (authorLabel.getId() == null){
					AuthorLabel newAuthorLabel = new AuthorLabel(authorLabel);
					newAuthorLabel.setAuthor(updatedAuthor);
					newAuthorLabel.persist();
					updatedAuthor.getLsLabels().add(newAuthorLabel);
				} else {
					AuthorLabel updatedLabel = AuthorLabel.update(authorLabel);
					updatedAuthor.getLsLabels().add(updatedLabel);
				}
			}	
		} else {
			logger.info("No author labels to update");	
		}
		updateLsStates(author, updatedAuthor);
		return Author.findAuthor(updatedAuthor.getId());
	}

	public void updateLsStates(Author jsonAuthor, Author updatedAuthor){
		if(jsonAuthor.getLsStates() != null){
			for(AuthorState lsThingState : jsonAuthor.getLsStates()){
				AuthorState updatedAuthorState;
				if (lsThingState.getId() == null){
					updatedAuthorState = new AuthorState(lsThingState);
					updatedAuthorState.setAuthor(updatedAuthor);
					updatedAuthorState.persist();
					updatedAuthor.getLsStates().add(updatedAuthorState);
					if (logger.isDebugEnabled()) logger.debug("persisted new lsThing state " + updatedAuthorState.getId());

				} else {
					updatedAuthorState = AuthorState.update(lsThingState);
					updatedAuthor.getLsStates().add(updatedAuthorState);

					if (logger.isDebugEnabled()) logger.debug("updated lsThing state " + updatedAuthorState.getId());

				}
				if (lsThingState.getLsValues() != null){
					for(AuthorValue lsThingValue : lsThingState.getLsValues()){
						if (lsThingValue.getLsState() == null) lsThingValue.setLsState(updatedAuthorState);
						AuthorValue updatedAuthorValue;
						if (lsThingValue.getId() == null){
							updatedAuthorValue = AuthorValue.create(lsThingValue);
							updatedAuthorValue.setLsState(AuthorState.findAuthorState(updatedAuthorState.getId()));
							updatedAuthorValue.persist();
							updatedAuthorState.getLsValues().add(updatedAuthorValue);
						} else {
							updatedAuthorValue = AuthorValue.update(lsThingValue);
							if (logger.isDebugEnabled()) logger.debug("updated lsThing value " + updatedAuthorValue.getId());
						}
						if (logger.isDebugEnabled()) logger.debug("checking lsThingValue " + updatedAuthorValue.toJson());

					}	
				} else {
					if (logger.isDebugEnabled()) logger.debug("No lsThing values to update");
				}
			}
		}
	}

	@Override
	public Author getOrCreateAuthor(Author author) {
		String userName = author.getUserName();
		try{
			Author foundAuthor = Author.findAuthorsByUserName(userName).getSingleResult();
			return foundAuthor;
		}catch(EmptyResultDataAccessException e){
			logger.debug("Author "+userName+" not found. Creating new author");
			return saveAuthor(author);
		}
	}
	
}
