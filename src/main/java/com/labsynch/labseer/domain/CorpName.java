package com.labsynch.labseer.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.dto.CorpNameDTO;
import com.labsynch.labseer.utils.Configuration;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@RooJson
public class CorpName {


	private static final Logger logger = LoggerFactory.getLogger(CorpName.class);

	public static final String prefix = Configuration.getConfigInfo().getServerSettings().getCorpPrefix();

	public static final String separator = Configuration.getConfigInfo().getServerSettings().getCorpSeparator();

	public static final String saltSeparator = Configuration.getConfigInfo().getServerSettings().getSaltSeparator();

	public static final String batchSeparator = Configuration.getConfigInfo().getServerSettings().getBatchSeparator();

	public static final Boolean isFancyCorpNumberFormat = Configuration.getConfigInfo().getServerSettings().isFancyCorpNumberFormat();

	public static final Integer numberCorpDigits = Configuration.getConfigInfo().getServerSettings().getNumberCorpDigits();

	public static final Boolean saltBeforeLot = Configuration.getConfigInfo().getMetaLot().isSaltBeforeLot();

	public static final Boolean appendSaltCodeToLotName = Configuration.getConfigInfo().getServerSettings().isAppendSaltCodeToLotName();

	public static final String corpParentFormat = Configuration.getConfigInfo().getServerSettings().getCorpParentFormat();

	public static final String corpBatchFormat = Configuration.getConfigInfo().getServerSettings().getCorpBatchFormat();

	public static final String databaseType = Configuration.getConfigInfo().getServerSettings().getDatabaseType();

	//	corpSeparator: "",
	//	saltSeparator: ".",   	
	//	batchSeparator: ":",

	@Size(max = 50)
	private String parentCorpName;

	@Size(max = 50)
	private String comment;

	private Boolean ignore;

	public static String formatCorpName(Long corpId){

		String corpName = null;

		//		if (corpParentFormat.equalsIgnoreCase("license_plate_format")){


		if (isFancyCorpNumberFormat){
			if (corpId < 10000){
				corpName = CorpName.prefix.concat(CorpName.separator).concat(String.format("%04d", corpId));	    		
			} else if (corpId < 100000) {
				corpName = CorpName.prefix.concat(CorpName.separator).concat(String.format("%05d", corpId));	    		
			} else if (corpId < 1000000) {
				corpName = CorpName.prefix.concat(CorpName.separator).concat(String.format("%06d", corpId));	    		
			} else if (corpId < 10000000) {
				corpName = CorpName.prefix.concat(CorpName.separator).concat(String.format("%07d", corpId));	    		
			}			
		} else {
			String formatCorpNumber = "%0" + numberCorpDigits + "d";
			logger.debug("format corpNumber: " + formatCorpNumber);
			corpName = CorpName.prefix.concat(CorpName.separator).concat(String.format(formatCorpNumber, corpId));	    		
		}

		return corpName;
	}

	public static String generateSaltFormCorpName(String parentCorpName, Set<IsoSalt> isoSalts){

		boolean saltBeforeLot = Configuration.getConfigInfo().getMetaLot().isSaltBeforeLot();

		logger.debug("number of isoSalts found: " + isoSalts.size());
		List<IsoSalt> isotopes = new ArrayList<IsoSalt>();
		List<IsoSalt> salts = new ArrayList<IsoSalt>();
		for (IsoSalt isoSalt : isoSalts){
			if(isoSalt.getType().equalsIgnoreCase("isotope")){
				isotopes.add(isoSalt);
			} else if(isoSalt.getType().equalsIgnoreCase("salt")){
				salts.add(isoSalt);
			}
		}
		logger.debug("number of isotopes found: " + isotopes.size());
		ArrayList<String> isotopeAbbrevs = new ArrayList<String>();
		if (isotopes.size() > 0){
			for (IsoSalt iso : isotopes){
				Isotope isotope = Isotope.findIsotope(iso.getIsotope().getId());
				String abbrev = isotope.getAbbrev();
				isotopeAbbrevs.add(abbrev);
			}
			Collections.sort(isotopeAbbrevs);
		}
		logger.debug("number of salts found: " + salts.size());
		ArrayList<String> saltAbbrevs = new ArrayList<String>();
		if (salts.size() > 0){
			for (IsoSalt isoSalt : salts){
				Salt salt = Salt.findSalt(isoSalt.getSalt().getId());
				String abbrev = salt.getAbbrev();
				saltAbbrevs.add(abbrev);
			}	    	
			Collections.sort(saltAbbrevs);
		}		
		String saltFormName = "";
		for (String abbrev : isotopeAbbrevs){
			saltFormName = saltFormName.concat(abbrev);
		}
		for (String abbrev : saltAbbrevs){
			saltFormName = saltFormName.concat(abbrev);
		}

		logger.debug("saltBeforeLot is: " + saltBeforeLot);
		String corpName = null;
		if (saltFormName.length() == 0){
			saltFormName = "0";
			if (!saltBeforeLot){

				corpName = parentCorpName;
				return corpName;
			} 
		}

		corpName = parentCorpName.concat(CorpName.separator).concat(saltFormName);
		return corpName;

		//  changing behavior of saltForm corp name -- Guy 2012-10-23		
		//		if (saltBeforeLot){
		//			corpName = parentCorpName.concat(CorpName.separator).concat(saltFormName);
		//			return corpName;
		//		} else {
		//			return parentCorpName;
		//		}

	}


	public static boolean checkParentCorpName(String corpName) {
		corpName = corpName.trim();
		Pattern pattern = Pattern.compile("^" + CorpName.prefix + CorpName.separator + "[0-9]{1,9}$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(corpName);
		return matcher.find();
	}

	public static boolean checkParentCorpNameAlt(String corpName) {
		logger.debug("incoming corpName to check: " + corpName);
		corpName = corpName.trim();
		Pattern pattern = Pattern.compile("^" + CorpName.prefix + ".*?" + "([0-9]{1,9})$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(corpName);
		return matcher.find();
	}

	public static boolean checkSaltFormCorpName(String corpName) {
		corpName = corpName.trim();
		Pattern pattern = Pattern.compile("^" + CorpName.prefix + CorpName.separator + "[0-9]{4,9}" + 
				CorpName.separator + "\\w+$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(corpName);
		return matcher.find();
	}

	public static boolean checkLotCorpName(String corpName) {
		corpName = corpName.trim();
		Pattern pattern = null;
		if (saltBeforeLot){
			pattern = Pattern.compile("^" + CorpName.prefix + CorpName.separator + "[0-9]{4,9}" + 
					CorpName.separator + "\\w+" + CorpName.separator + 
					"[0-9]{2}$", Pattern.CASE_INSENSITIVE);
		} else {
			if (appendSaltCodeToLotName){
				pattern = Pattern.compile("^" + CorpName.prefix + CorpName.separator + "[0-9]{1,9}" + 
						CorpName.batchSeparator + 
						"[0-9]{1,3}" + ".*?$", Pattern.CASE_INSENSITIVE);			
			} else {
				pattern = Pattern.compile("^" + CorpName.prefix + CorpName.separator + "[0-9]{1,9}" + 
						CorpName.batchSeparator + 
						"[0-9]{1,3}$", Pattern.CASE_INSENSITIVE);							
			}
		}

		Matcher matcher = pattern.matcher(corpName);
		return matcher.find();
	}

	public static boolean checkStringName(String corpName) {
		corpName = corpName.trim();
		Pattern pattern = Pattern.compile("^[A-Z].*$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(corpName);
		return matcher.find();
	}

	public static String convertLotToSaltFormCorpName(String corpName) {
		corpName = corpName.trim();
		Pattern pattern = Pattern.compile("^(" + CorpName.prefix + CorpName.separator + "[0-9]{4,9}" + 
				CorpName.separator + "\\w+)" + CorpName.separator + 
				"[0-9]{2}$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(corpName);
		return (matcher.replaceFirst("$1"));
	}
	
	/**
	 * Takes a string and returns the numeric value of the longest numeric substring, or 0 if the string has no numerals
	 * @param corpName The corporate ID string
	 * @return the Long value of the corp number parsed
	 */
	public static Long parseParentNumber(String corpName){
		//Returns the numeric value of the largest numeric substring of the input string, or 0 if one cannot be parsed
		String longestSub = "";
		for (String sub : corpName.split("[^0-9]")){
			if (sub.length() > longestSub.length()){
				longestSub = sub;
			}
		}
		if (longestSub.length() > 0){
			return Long.parseLong(longestSub);
		}else {
			return 0L;
		}
	}

	public static Long parseCorpNumber(String corpName) {
		corpName = corpName.trim();
		long corpNumber;
		try {
			corpNumber = Long.parseLong(corpName);
		} catch (Exception e){
			logger.debug("caught an exception parsing the corp number. set to default 0");
			corpNumber = 0L;
		}
		return corpNumber;
	}

	public static String convertCorpNameNumber(String corpName) {
		corpName = corpName.trim();
		Long corpNumber = Long.parseLong(corpName);
		return CorpName.formatCorpName(corpNumber);
	}

	public static String convertCorpNamePrefix(String corpName) {
		corpName = corpName.trim();
		Pattern corpNamePattern = Pattern.compile("^" + CorpName.prefix + CorpName.separator, Pattern.CASE_INSENSITIVE);
		Matcher matcher = corpNamePattern.matcher(corpName);
		return (matcher.replaceFirst(CorpName.prefix + CorpName.separator));
	}

	public static String removeCorpNamePrefix(String corpName) {
		corpName = corpName.trim().toUpperCase();
		Pattern corpNamePattern = Pattern.compile("^" + CorpName.prefix + ".*?" + "([0-9]{1,9}$)", Pattern.CASE_INSENSITIVE);
		Matcher matcher = corpNamePattern.matcher(corpName);
		return (matcher.replaceFirst("$1"));
	}

	public static Long convertToParentNumber(String corpName) {
		corpName = corpName.trim();
		Pattern corpNamePattern = Pattern.compile("^" + ".*?" + "([0-9]{1,9})" + ".*?$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = corpNamePattern.matcher(corpName);
		logger.debug("found: " + matcher.replaceFirst("$1"));
		String corpNumberString = matcher.replaceFirst("$1");
		Long corpNumber = parseCorpNumber(corpNumberString);
		return corpNumber;
	}


	public static boolean checkCorpNumber(String corpName) {
		corpName = corpName.trim();
		Pattern corpNumberPattern = Pattern.compile("^[0-9]{1,9}$");
		Matcher matcher = corpNumberPattern.matcher(corpName);
		return matcher.find();
	}

	public static Long convertCorpNameToNumber(String corpName) {
		corpName = corpName.trim();
		Pattern pattern = Pattern.compile(CorpName.separator);
		String[] result = pattern.split(corpName);
		String corpNumberString = result[1].trim();
		Long corpNumber = Long.parseLong(corpNumberString);
		return corpNumber;
	}

	public static boolean checkBuidNumber(String corpName) {
		corpName = corpName.trim();
		Pattern buidPattern = Pattern.compile("^BUID" + ".*?" + "[0-9]{1,9}" + ".*?$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = buidPattern.matcher(corpName);
		return matcher.find();
	}

	public static Long convertToBuidNumber(String corpName) {
		corpName = corpName.trim();
		Pattern buidNamePattern = Pattern.compile("^BUID" + ".*?" + "([0-9]{1,9})" + ".*?$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = buidNamePattern.matcher(corpName);
		logger.debug("found BUID: " + matcher.replaceFirst("$1"));
		String buidNumberString = matcher.replaceFirst("$1");
		Long corpNumber = parseCorpNumber(buidNumberString);
		return corpNumber;
	}

	@SuppressWarnings({"unchecked" })
	public static List<Object> generateCustomParentSequence() {
		String sqlQuery;
		// need to know database dialect
		if (databaseType == null || databaseType.equalsIgnoreCase("postgres")){
			sqlQuery = "SELECT nextval('custom_parent_pkseq') as custom_parent_id";
		} else if (databaseType.equalsIgnoreCase("oracle")) {
			sqlQuery = "SELECT custom_parent_pkseq FROM dual";
		} else if (databaseType.equalsIgnoreCase("sqlserver")){
			sqlQuery = "SELECT NEXT VALUE FOR custom_parent_pkseq as custom_parent_id";
		} else {
			sqlQuery = "SELECT nextval('custom_parent_pkseq') as custom_parent_id";
		}
		logger.debug(sqlQuery);
		EntityManager em = Lot.entityManager();
		Query q = em.createNativeQuery(sqlQuery);
		return q.getResultList();
	}

	public static String generateLicensePlate(int inputNumber) {
		inputNumber = inputNumber - 1;
		int num;
		if (inputNumber >= 1062600) {
			// changed algorithm to skip '000'
			inputNumber = inputNumber - 1063;
			num= inputNumber % 999 + 1;
			inputNumber=(int)(inputNumber/999);

		} else {
			num=inputNumber%1000;
			inputNumber=(int)(inputNumber/1000);
      
		};

		String let="";
		
		for (int i=0; i<3; i++) {
			let = let+(char) (inputNumber%26 + 65);
			inputNumber = (int)(inputNumber/26);
		};
		let = new StringBuilder(let).reverse().toString();
		return String.format("%s%03d", let, num);
	}

	public static String generateCorpLicensePlate() {
		List<Object> seqList = CorpName.generateCustomParentSequence();
		String corpName = CorpName.generateLicensePlate(Integer.parseInt(String.valueOf(seqList.get(0))));
		return corpName;
	}


	public static CorpNameDTO generateParentNameFromSequence() {
		List<Object> seqList = CorpName.generateCustomParentSequence();
		int corpDigits = Configuration.getConfigInfo().getServerSettings().getNumberCorpDigits();
		String formatCorpDigits = "%0" + corpDigits + "d";
		Integer corpNumber = Integer.parseInt(String.valueOf(seqList.get(0)));
		String corpName = CorpName.prefix.concat(CorpName.separator).concat(String.format(formatCorpDigits, corpNumber));	    		
		CorpNameDTO corpNameDTO = new CorpNameDTO();
		corpNameDTO.setCorpName(corpName);
		corpNameDTO.setCorpNumber(corpNumber);
		return corpNameDTO;
	}

}
