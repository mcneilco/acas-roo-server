package com.labsynch.labseer.service;

import java.util.List;
import java.util.Set;

import com.labsynch.labseer.domain.IsoSalt;
import com.labsynch.labseer.dto.CorpNameDTO;

public interface CorpNameService {


	public String getPreferredNameFromBuid(String aliasList);

	public String getLotNumberFromBuid(String aliasList);

	public String formatCorpName(Long corpId);

	public String convertCorpNameNumber(String corpName);

	public String generateSaltFormCorpName(String parentCorpName, Set<IsoSalt> isoSalts);
	
	public String convertCorpNamePrefix(String corpName);

	public String removeCorpNamePrefix(String corpName);

	public Long convertToParentNumber(String corpName);

	public boolean checkParentCorpName(String corpName);

	public boolean checkCorpNumber(String corpName);

	public boolean checkSaltFormCorpName(String corpName);

	public boolean checkLotCorpName(String corpName);

	public List<Object> generateCustomParentSequence();

	public boolean checkBuidNumber(String corpName);

	public Long parseCorpNumber(String corpName);

	public Long convertToBuidNumber(String corpName);

	public Long parseParentNumber(String corpName);

	public String generateLicensePlate(int inputNumber);

	public String generateCorpLicensePlate();

	public CorpNameDTO generateParentNameFromSequence();
	
}
