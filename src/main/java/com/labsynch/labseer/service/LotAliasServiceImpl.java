package com.labsynch.labseer.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.domain.LotAlias;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LotAliasServiceImpl implements LotAliasService {

	Logger logger = LoggerFactory.getLogger(LotAliasServiceImpl.class);

	@Override
	@Transactional
	public Lot updateLotAliases(Lot lot) {
		Set<LotAlias> aliasesToBeSaved = lot.getLotAliases();
		logger.debug(LotAlias.toJsonArray(aliasesToBeSaved));
		Set<LotAlias> savedAliases = new HashSet<LotAlias>();
		if (aliasesToBeSaved != null && !aliasesToBeSaved.isEmpty()){
			for (LotAlias aliasToBeSaved : aliasesToBeSaved){
				logger.debug(aliasToBeSaved.toJson());
				aliasToBeSaved.setLot(lot);
				if (aliasToBeSaved.getId() == null) aliasToBeSaved.persist();
				else aliasToBeSaved.merge();
				savedAliases.add(aliasToBeSaved);
				logger.debug(aliasToBeSaved.toJson());
				logger.debug(LotAlias.toJsonArray(savedAliases));
			}
		}
		lot.setLotAliases(savedAliases);
		return lot;
	}

	@Override
	public Lot updateLotAliases(Lot lot,
			Set<LotAlias> lotAliases) {
		Set<LotAlias> aliasesToBeSaved = lotAliases;
		Set<LotAlias> savedAliases = new HashSet<LotAlias>();
		if (aliasesToBeSaved != null && !aliasesToBeSaved.isEmpty()){
			for (LotAlias aliasToBeSaved : aliasesToBeSaved){
				aliasToBeSaved.setLot(lot);
				if (aliasToBeSaved.getId() == null) aliasToBeSaved.persist();
				else aliasToBeSaved.merge();
				savedAliases.add(aliasToBeSaved);
			}
		}
		lot.setLotAliases(savedAliases);
		return lot;
	}

	//update default lot aliases
	@Override
	public Lot updateLotDefaultAlias(Lot lot, String aliasList) {
		String lsType = "default";
		String lsKind = "default";
		lot = updateLotAliasByTypeAndKind(lot, lsType, lsKind, aliasList);
		return lot;
	}
	
	@Override
	public Lot updateLotAliasByTypeAndKind(Lot lot, String lsType, String lsKind, String aliasList) {
		if (aliasList != null){
			List<LotAlias> existingAliases = LotAlias.findLotAliasesByLotAndLsTypeEqualsAndLsKindEquals(lot, lsType, lsKind).getResultList();
			Map<String, LotAlias> existingAliasMap = new HashMap<String, LotAlias>();
			for (LotAlias existingAlias : existingAliases){
				existingAliasMap.put(existingAlias.getAliasName(), existingAlias);
			}

			Map<String, LotAlias> newAliasMap = new HashMap<String, LotAlias>();
			Set<LotAlias> lotAliasSet = new HashSet<LotAlias>();
			
			String[] aliases = aliasList.split(";");
			for (String alias : aliases){
				if (!existingAliasMap.containsKey(alias)){
					LotAlias lotAlias = new LotAlias();
					lotAlias.setLsType(lsType);
					lotAlias.setLsKind(lsKind);
					lotAlias.setAliasName(alias);
					lotAlias.setLot(lot);
					lotAlias.persist();
					lotAliasSet.add(lotAlias);
					newAliasMap.put(lotAlias.getAliasName(), lotAlias);
				} else {
					lotAliasSet.add(existingAliasMap.get(alias));
					newAliasMap.put(alias, existingAliasMap.get(alias));
				}
			}

			for (String aliasKey : existingAliasMap.keySet()){
				if (!newAliasMap.containsKey(aliasKey)){
					//not present -- so marked to ignore
					LotAlias queryAlias = existingAliasMap.get(aliasKey);
					logger.info("current query alias: " + queryAlias);
					queryAlias.setIgnored(true);
					queryAlias.merge();
				}
			}

			lot.setLotAliases(lotAliasSet);
		}

		return lot;
	}
}

