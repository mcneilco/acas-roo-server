package com.labsynch.labseer.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.CorpName;
import com.labsynch.labseer.domain.Lot;

@Service
public class CorpNameServiceImpl implements CorpNameService {

	Logger logger = LoggerFactory.getLogger(CorpNameServiceImpl.class);

	@Override
	@Transactional
	public String getPreferredNameFromBuid(String aliasList) {
		Long buidNumber = 0L;
		String[] aliases = aliasList.split(",");
		List<String> preferredIds = new ArrayList<String>(aliases.length);  
		for (String alias:aliases){
			alias = alias.trim();
			logger.debug("current alias: " + alias);
			String preferredId = null;
			if (alias.equalsIgnoreCase("")){
				logger.debug("blank entry");
				preferredId = "";
			} else {
				buidNumber = CorpName.convertToBuidNumber(alias);
				logger.debug("query buid number: " + buidNumber);
				List<Lot> lots = Lot.findLotsByBuidNumber(buidNumber).getResultList();
				if (lots.size() == 1){
					logger.debug(lots.get(0).getCorpName());
					logger.debug(Long.toString(lots.get(0).getLotNumber()));


					preferredId = lots.get(0).getCorpName();
				} else if (lots.size() > 1) {
					logger.error("multiple lots with the same BUID" );
					preferredId = "";					
				}else {
					logger.debug("the lot is null" );
					preferredId = "";
				}				
			}
			preferredIds.add(preferredId);
		}
		String outputString = null;
		boolean firstPass = true;
		int counter = 0;
		for (String id : preferredIds){
			counter++;
			logger.debug("prerredId: " + id);
			if (firstPass){
				outputString = id;
				firstPass = false;
			} else {
				outputString = outputString.concat(id);
			}
			if (counter < preferredIds.size()){
				outputString = outputString.concat(",");
			}
		}
		return outputString;
	}
	
	@Override
	@Transactional
	public String getLotNumberFromBuid(String aliasList) {
		Long buidNumber = 0L;
		String[] aliases = aliasList.split(",");
		List<String> preferredIds = new ArrayList<String>(aliases.length);  
		for (String alias:aliases){
			alias = alias.trim();
			logger.debug("current alias: " + alias);
			String preferredId = null;
			if (alias.equalsIgnoreCase("")){
				logger.debug("blank entry");
				preferredId = "";
			} else {
				buidNumber = CorpName.convertToBuidNumber(alias);
				logger.debug("query buid number: " + buidNumber);
				List<Lot> lots = Lot.findLotsByBuidNumber(buidNumber).getResultList();
				if (lots.size() == 1){
					logger.debug(Long.toString(lots.get(0).getLotNumber()));
					preferredId = Long.toString(lots.get(0).getLotNumber());
				} else if (lots.size() > 1) {
					logger.error("multiple lots with the same BUID" );
					preferredId = "";					
				}else {
					logger.debug("the lot is null" );
					preferredId = "";
				}				
			}
			preferredIds.add(preferredId);
		}
		String outputString = null;
		boolean firstPass = true;
		int counter = 0;
		for (String id : preferredIds){
			counter++;
			logger.debug("prerredId: " + id);
			if (firstPass){
				outputString = id;
				firstPass = false;
			} else {
				outputString = outputString.concat(id);
			}
			if (counter < preferredIds.size()){
				outputString = outputString.concat(",");
			}
		}
		return outputString;
	}
}

