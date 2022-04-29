package com.labsynch.labseer.service;

import com.labsynch.labseer.domain.IsoSalt;
import com.labsynch.labseer.utils.PropertiesUtilService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class IsoSaltServiceImpl implements IsoSaltService {

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	private static final Logger logger = LoggerFactory.getLogger(LotServiceImpl.class);
	
	@Override
	public double calculateSaltWeight(IsoSalt isoSalt){
		double saltWeight = 0;
		logger.debug("isoSalt type: " + isoSalt.getType());
		if (isoSalt.getType().equalsIgnoreCase("isotope")){
			logger.debug("isoSalt type: " + isoSalt.getType());

			saltWeight = isoSalt.getIsotope().getMassChange(); 
			logger.debug("intial saltWeight: " + saltWeight);	
			logger.debug("equivalents: " + isoSalt.getEquivalents());
			saltWeight = saltWeight * isoSalt.getEquivalents();
			logger.debug("final saltWeight: " + saltWeight);	
		} 
		else if (isoSalt.getType().equalsIgnoreCase("salt")){
			saltWeight = isoSalt.getSalt().getMolWeight();
			logger.debug("intial saltWeight: " + saltWeight);	
			logger.debug("equivalents: " + isoSalt.getEquivalents());
			//correction for charged salts
			saltWeight = saltWeight * isoSalt.getEquivalents();
			if (propertiesUtilService.getUseExactMass()){
				saltWeight = saltWeight - (1.007825)*(isoSalt.getSalt().getCharge() * isoSalt.getEquivalents());
			}else{
				saltWeight = saltWeight - (1.00794)*(isoSalt.getSalt().getCharge() * isoSalt.getEquivalents());
			}
			logger.debug("final saltWeight: " + saltWeight);	
		}
		return saltWeight;
	}
    

}
