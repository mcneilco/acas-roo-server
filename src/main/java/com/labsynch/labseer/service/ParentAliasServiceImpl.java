package com.labsynch.labseer.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.ParentAlias;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ParentAliasServiceImpl implements ParentAliasService {

	Logger logger = LoggerFactory.getLogger(ParentAliasServiceImpl.class);

	@Override
	@Transactional
	public Parent updateParentAliases(Parent parent) {
		Set<ParentAlias> aliasesToBeSaved = parent.getParentAliases();
		logger.debug(ParentAlias.toJsonArray(aliasesToBeSaved));
		Set<ParentAlias> savedAliases = new HashSet<ParentAlias>();
		if (aliasesToBeSaved != null && !aliasesToBeSaved.isEmpty()){
			for (ParentAlias aliasToBeSaved : aliasesToBeSaved){
				logger.debug(aliasToBeSaved.toJson());
				aliasToBeSaved.setParent(parent);
				if (aliasToBeSaved.getId() == null) aliasToBeSaved.persist();
				else aliasToBeSaved.merge();
				savedAliases.add(aliasToBeSaved);
				logger.debug(aliasToBeSaved.toJson());
				logger.debug(ParentAlias.toJsonArray(savedAliases));
			}
		}
		parent.setParentAliases(savedAliases);
		return parent;
	}

	@Override
	public Parent updateParentAliases(Parent parent,
			Set<ParentAlias> parentAliases) {
		Set<ParentAlias> aliasesToBeSaved = parentAliases;
		Set<ParentAlias> savedAliases = new HashSet<ParentAlias>();
		if (aliasesToBeSaved != null && !aliasesToBeSaved.isEmpty()){
			for (ParentAlias aliasToBeSaved : aliasesToBeSaved){
				aliasToBeSaved.setParent(parent);
				if (aliasToBeSaved.getId() == null) aliasToBeSaved.persist();
				else aliasToBeSaved = aliasToBeSaved.merge();
				savedAliases.add(aliasToBeSaved);
			}
		}
		parent.setParentAliases(savedAliases);
		return parent;
	}

		
	//update liveDesign corp name aliases
	@Override
	public Parent updateParentLiveDesignAlias(Parent parent, String aliasList) {
		String lsType = "external id";
		String lsKind = "LiveDesign Corp Name";
		parent = updateParentAliasByTypeAndKind(parent, lsType, lsKind, aliasList);
		return parent;
	}

	
	//update common name aliases
	@Override
	public Parent updateParentCommonNameAlias(Parent parent, String aliasList) {
		String lsType = "other name";
		String lsKind = "Parent Common Name";
		parent = updateParentAliasByTypeAndKind(parent, lsType, lsKind, aliasList);
		return parent;
	}
	
	//update default parent aliases
	@Override
	public Parent updateParentDefaultAlias(Parent parent, String aliasList) {
		String lsType = "default";
		String lsKind = "default";
		parent = updateParentAliasByTypeAndKind(parent, lsType, lsKind, aliasList);
		return parent;
	}
	
	@Override
	public Parent updateParentAliasByTypeAndKind(Parent parent, String lsType, String lsKind, String aliasList) {
		if (aliasList != null){
			List<ParentAlias> existingAliases = ParentAlias.findParentAliasesByParentAndLsTypeEqualsAndLsKindEquals(parent, lsType, lsKind).getResultList();
			Map<String, ParentAlias> existingAliasMap = new HashMap<String, ParentAlias>();
			for (ParentAlias existingAlias : existingAliases){
				existingAliasMap.put(existingAlias.getAliasName(), existingAlias);
			}

			Map<String, ParentAlias> newAliasMap = new HashMap<String, ParentAlias>();
			Set<ParentAlias> parentAliasSet = new HashSet<ParentAlias>();
			
			logger.info("####### Incoming alias list: " + aliasList);
			String[] aliases = aliasList.split(";");
			for (String alias : aliases){
				if (!existingAliasMap.containsKey(alias)){
					ParentAlias parentAlias = new ParentAlias();
					parentAlias.setLsType(lsType);
					parentAlias.setLsKind(lsKind);
					parentAlias.setAliasName(alias);
					parentAlias.setParent(parent);
					parentAlias.persist();
					parentAliasSet.add(parentAlias);
					newAliasMap.put(parentAlias.getAliasName(), parentAlias);
				} else {
					parentAliasSet.add(existingAliasMap.get(alias));
					newAliasMap.put(alias, existingAliasMap.get(alias));
				}
			}

			for (String aliasKey : existingAliasMap.keySet()){
				if (!newAliasMap.containsKey(aliasKey)){
					//not present -- so marked to ignore
					ParentAlias queryAlias = existingAliasMap.get(aliasKey);
					logger.info("current query alias: " + queryAlias);
					queryAlias.setIgnored(true);
					queryAlias.merge();
				}
			}

			for (ParentAlias pa : parentAliasSet){
				logger.info(pa.toJson());
			}
			
			parent.setParentAliases(parentAliasSet);
		}

		return parent;
	}
	
}

