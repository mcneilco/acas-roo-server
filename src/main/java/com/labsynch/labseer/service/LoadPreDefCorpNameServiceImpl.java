package com.labsynch.labseer.service;

import com.labsynch.labseer.domain.PreDef_CorpName;
import com.labsynch.labseer.utils.PropertiesUtilService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoadPreDefCorpNameServiceImpl implements LoadPreDefCorpNameService {

	Logger logger = LoggerFactory.getLogger(LoadPreDefCorpNameServiceImpl.class);
	
	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Override
	public boolean loadDefaultPreDefCorpNames() {
		long numberOfCorpNamesToGenerate = 50000;
		long corpNumber = propertiesUtilService.getStartingCorpNumber(); //starting number
		int corpDigits = propertiesUtilService.getNumberCorpDigits();
		String formatCorpDigits = "%0" + corpDigits + "d";
		try{
			while ( corpNumber < numberOfCorpNamesToGenerate ){
				corpNumber++;
				String corpName = null;
				corpName = propertiesUtilService.getCorpPrefix().concat(propertiesUtilService.getCorpSeparator()).concat(String.format(formatCorpDigits, corpNumber));	    		
				boolean used = false;
				boolean skip = false;
				PreDef_CorpName preDefCorpName = new PreDef_CorpName();
				preDefCorpName.setCorpName(corpName);
				preDefCorpName.setCorpNumber(corpNumber);
				preDefCorpName.setUsed(used);
				preDefCorpName.setSkip(skip);
				preDefCorpName.persist();
				logger.debug(corpName);
			}
			return true;
		} catch(Exception e){
			return false;
		}
	}

}

