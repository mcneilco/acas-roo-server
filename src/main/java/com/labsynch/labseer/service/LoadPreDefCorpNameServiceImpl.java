package com.labsynch.labseer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.CorpName;
import com.labsynch.labseer.domain.PreDef_CorpName;
import com.labsynch.labseer.dto.configuration.MainConfigDTO;
import com.labsynch.labseer.utils.Configuration;

@Service
public class LoadPreDefCorpNameServiceImpl implements LoadPreDefCorpNameService {

	Logger logger = LoggerFactory.getLogger(LoadPreDefCorpNameServiceImpl.class);
	
	private static final MainConfigDTO mainConfig = Configuration.getConfigInfo();

	@Override
	public boolean loadDefaultPreDefCorpNames() {
		long numberOfCorpNamesToGenerate = 50000;
		long corpNumber = mainConfig.getServerSettings().getStartingCorpNumber(); //starting number
		int corpDigits = mainConfig.getServerSettings().getNumberCorpDigits();
		String formatCorpDigits = "%0" + corpDigits + "d";
		try{
			while ( corpNumber < numberOfCorpNamesToGenerate ){
				corpNumber++;
				String corpName = null;
				corpName = CorpName.prefix.concat(CorpName.separator).concat(String.format(formatCorpDigits, corpNumber));	    		
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

