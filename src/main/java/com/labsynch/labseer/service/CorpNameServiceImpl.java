package com.labsynch.labseer.service;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import com.labsynch.labseer.domain.CorpName;
import com.labsynch.labseer.domain.IsoSalt;
import com.labsynch.labseer.domain.Isotope;
import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.domain.Salt;
import com.labsynch.labseer.dto.CorpNameDTO;
import com.labsynch.labseer.utils.PropertiesUtilService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CorpNameServiceImpl implements CorpNameService {
	static Logger logger = LoggerFactory.getLogger(CorpNameServiceImpl.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Override
	@Transactional
	public String getPreferredNameFromBuid(String aliasList) {
		Long buidNumber = 0L;
		String[] aliases = aliasList.split(",");
		List<String> preferredIds = new ArrayList<String>(aliases.length);
		for (String alias : aliases) {
			alias = alias.trim();
			logger.debug("current alias: " + alias);
			String preferredId = null;
			if (alias.equalsIgnoreCase("")) {
				logger.debug("blank entry");
				preferredId = "";
			} else {
				buidNumber = convertToBuidNumber(alias);
				logger.debug("query buid number: " + buidNumber);
				List<Lot> lots = Lot.findLotsByBuidNumber(buidNumber).getResultList();
				if (lots.size() == 1) {
					logger.debug(lots.get(0).getCorpName());
					logger.debug(Long.toString(lots.get(0).getLotNumber()));

					preferredId = lots.get(0).getCorpName();
				} else if (lots.size() > 1) {
					logger.error("multiple lots with the same BUID");
					preferredId = "";
				} else {
					logger.debug("the lot is null");
					preferredId = "";
				}
			}
			preferredIds.add(preferredId);
		}
		String outputString = null;
		boolean firstPass = true;
		int counter = 0;
		for (String id : preferredIds) {
			counter++;
			logger.debug("prerredId: " + id);
			if (firstPass) {
				outputString = id;
				firstPass = false;
			} else {
				outputString = outputString.concat(id);
			}
			if (counter < preferredIds.size()) {
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
		for (String alias : aliases) {
			alias = alias.trim();
			logger.debug("current alias: " + alias);
			String preferredId = null;
			if (alias.equalsIgnoreCase("")) {
				logger.debug("blank entry");
				preferredId = "";
			} else {
				buidNumber = convertToBuidNumber(alias);
				logger.debug("query buid number: " + buidNumber);
				List<Lot> lots = Lot.findLotsByBuidNumber(buidNumber).getResultList();
				if (lots.size() == 1) {
					logger.debug(Long.toString(lots.get(0).getLotNumber()));
					preferredId = Long.toString(lots.get(0).getLotNumber());
				} else if (lots.size() > 1) {
					logger.error("multiple lots with the same BUID");
					preferredId = "";
				} else {
					logger.debug("the lot is null");
					preferredId = "";
				}
			}
			preferredIds.add(preferredId);
		}
		String outputString = null;
		boolean firstPass = true;
		int counter = 0;
		for (String id : preferredIds) {
			counter++;
			logger.debug("prerredId: " + id);
			if (firstPass) {
				outputString = id;
				firstPass = false;
			} else {
				outputString = outputString.concat(id);
			}
			if (counter < preferredIds.size()) {
				outputString = outputString.concat(",");
			}
		}
		return outputString;
	}

	@Override
	public String convertCorpNameNumber(String corpName) {
		corpName = corpName.trim();
		Long corpNumber = Long.parseLong(corpName);
		return formatCorpName(corpNumber);
	}

	@Override
	public String convertCorpNamePrefix(String corpName) {
		corpName = corpName.trim();
		Pattern corpNamePattern = Pattern.compile(
				"^" + propertiesUtilService.getCorpPrefix() + propertiesUtilService.getCorpSeparator(),
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = corpNamePattern.matcher(corpName);
		return (matcher.replaceFirst(propertiesUtilService.getCorpPrefix() + propertiesUtilService.getCorpSeparator()));
	}

	@Override
	public String removeCorpNamePrefix(String corpName) {
		corpName = corpName.trim().toUpperCase();
		Pattern corpNamePattern = Pattern.compile("^" + propertiesUtilService.getCorpPrefix() + ".*?" + "([0-9]{1,9}$)",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = corpNamePattern.matcher(corpName);
		return (matcher.replaceFirst("$1"));
	}

	@Override
	public Long convertToParentNumber(String corpName) {
		corpName = corpName.trim();
		Pattern corpNamePattern = Pattern.compile("^" + ".*?" + "([0-9]{1,9})" + ".*?$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = corpNamePattern.matcher(corpName);
		logger.debug("found: " + matcher.replaceFirst("$1"));
		String corpNumberString = matcher.replaceFirst("$1");
		Long corpNumber = parseCorpNumber(corpNumberString);
		return corpNumber;
	}

	@Override
	/**
	 * Takes a string and returns the numeric value of the longest numeric
	 * substring, or 0 if the string has no numerals
	 * 
	 * @param corpName The corporate ID string
	 * @return the Long value of the corp number parsed
	 */
	public Long parseParentNumber(String corpName) {
		// Returns the numeric value of the largest numeric substring of the input
		// string, or 0 if one cannot be parsed
		String longestSub = "";
		for (String sub : corpName.split("[^0-9]")) {
			if (sub.length() > longestSub.length()) {
				longestSub = sub;
			}
		}
		if (longestSub.length() > 0) {
			return Long.parseLong(longestSub);
		} else {
			return 0L;
		}
	}

	@Override
	public boolean checkCorpNumber(String corpName) {
		corpName = corpName.trim();
		Pattern corpNumberPattern = Pattern.compile("^[0-9]{1,9}$");
		Matcher matcher = corpNumberPattern.matcher(corpName);
		return matcher.find();
	}

	@Override
	public boolean checkParentCorpName(String corpName) {
		corpName = corpName.trim();
		Pattern pattern = Pattern.compile(
				"^" + propertiesUtilService.getCorpPrefix() + propertiesUtilService.getCorpSeparator() + "[0-9]{1,9}$",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(corpName);
		return matcher.find();
	}

	@Override
	public boolean checkSaltFormCorpName(String corpName) {
		corpName = corpName.trim();
		Pattern pattern = Pattern.compile(
				"^" + propertiesUtilService.getCorpPrefix() + propertiesUtilService.getCorpSeparator() + "[0-9]{4,9}" +
						propertiesUtilService.getCorpSeparator() + "\\w+$",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(corpName);
		return matcher.find();
	}

	@Override
	public boolean checkLotCorpName(String corpName) {
		corpName = corpName.trim();
		Pattern pattern = null;
		if (propertiesUtilService.getSaltBeforeLot()) {
			pattern = Pattern.compile("^" + propertiesUtilService.getCorpPrefix()
					+ propertiesUtilService.getCorpSeparator() + "[0-9]{4,9}" +
					propertiesUtilService.getCorpSeparator() + "\\w+" + propertiesUtilService.getCorpSeparator() +
					"[0-9]{2}$", Pattern.CASE_INSENSITIVE);
		} else {
			if (propertiesUtilService.getAppendSaltCodeToLotName()) {
				pattern = Pattern.compile("^" + propertiesUtilService.getCorpPrefix()
						+ propertiesUtilService.getCorpSeparator() + "[0-9]{1,9}" +
						propertiesUtilService.getBatchSeparator() +
						"[0-9]{1,3}" + ".*?$", Pattern.CASE_INSENSITIVE);
			} else {
				pattern = Pattern.compile("^" + propertiesUtilService.getCorpPrefix()
						+ propertiesUtilService.getCorpSeparator() + "[0-9]{1,9}" +
						propertiesUtilService.getBatchSeparator() +
						"[0-9]{1,3}$", Pattern.CASE_INSENSITIVE);
			}
		}

		Matcher matcher = pattern.matcher(corpName);
		return matcher.find();
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public List<Object> generateCustomParentSequence() {
		String sqlQuery;

		// need to know database dialect
		EntityManager em = CorpName.entityManager();
		String databaseType = null;
		Float databaseVersion = 0.0f;
		try {
			org.hibernate.engine.spi.SessionImplementor sessionImp = (org.hibernate.engine.spi.SessionImplementor) em
					.getDelegate();
			DatabaseMetaData metadata = sessionImp.getJdbcConnectionAccess().obtainConnection().getMetaData();
			databaseType = metadata.getDatabaseProductName();
			databaseVersion = Float
					.parseFloat(metadata.getDatabaseMajorVersion() + "." + metadata.getDatabaseMinorVersion());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (databaseType == null || databaseType.equalsIgnoreCase("PostgreSQL")) {
			sqlQuery = "SELECT nextval('custom_parent_pkseq') as custom_parent_id";
		} else if (databaseType.equalsIgnoreCase("oracle")) {
			sqlQuery = "SELECT custom_parent_pkseq FROM dual";
		} else if (databaseType.equalsIgnoreCase("sqlserver")) {
			sqlQuery = "SELECT NEXT VALUE FOR custom_parent_pkseq as custom_parent_id";
		} else {
			sqlQuery = "SELECT nextval('custom_parent_pkseq') as custom_parent_id";
		}
		logger.debug(sqlQuery);
		Query q = em.createNativeQuery(sqlQuery);
		return q.getResultList();
	}

	@Override
	public boolean checkBuidNumber(String corpName) {
		corpName = corpName.trim();
		Pattern buidPattern = Pattern.compile("^BUID" + ".*?" + "[0-9]{1,9}" + ".*?$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = buidPattern.matcher(corpName);
		return matcher.find();
	}

	@Override
	public Long parseCorpNumber(String corpName) {
		corpName = corpName.trim();
		long corpNumber;
		try {
			corpNumber = Long.parseLong(corpName);
		} catch (Exception e) {
			logger.debug("caught an exception parsing the corp number. set to default 0");
			corpNumber = 0L;
		}
		return corpNumber;
	}

	@Override
	public Long convertToBuidNumber(String corpName) {
		corpName = corpName.trim();
		Pattern buidNamePattern = Pattern.compile("^BUID" + ".*?" + "([0-9]{1,9})" + ".*?$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = buidNamePattern.matcher(corpName);
		logger.debug("found BUID: " + matcher.replaceFirst("$1"));
		String buidNumberString = matcher.replaceFirst("$1");
		Long corpNumber = parseCorpNumber(buidNumberString);
		return corpNumber;
	}

	@Override
	public String generateLicensePlate(int inputNumber) {
		inputNumber = inputNumber - 1;
		int num;
		if (inputNumber >= 1062600) {
			// changed algorithm to skip '000'
			inputNumber = inputNumber - 1063;
			num = inputNumber % 999 + 1;
			inputNumber = (int) (inputNumber / 999);

		} else {
			num = inputNumber % 1000;
			inputNumber = (int) (inputNumber / 1000);

		}
		;

		String let = "";

		for (int i = 0; i < 3; i++) {
			let = let + (char) (inputNumber % 26 + 65);
			inputNumber = (int) (inputNumber / 26);
		}
		;
		let = new StringBuilder(let).reverse().toString();
		return String.format("%s%03d", let, num);
	}

	@Override
	public String generateCorpLicensePlate() {
		List<Object> seqList = generateCustomParentSequence();
		String corpName = generateLicensePlate(Integer.parseInt(String.valueOf(seqList.get(0))));
		return corpName;
	}

	@Override
	public CorpNameDTO generateParentNameFromSequence() {
		List<Object> seqList = generateCustomParentSequence();
		int corpDigits = propertiesUtilService.getNumberCorpDigits();
		String formatCorpDigits = "%0" + corpDigits + "d";
		Integer corpNumber = Integer.parseInt(String.valueOf(seqList.get(0)));
		String corpName = propertiesUtilService.getCorpPrefix().concat(propertiesUtilService.getCorpSeparator())
				.concat(String.format(formatCorpDigits, corpNumber));
		CorpNameDTO corpNameDTO = new CorpNameDTO();
		corpNameDTO.setCorpName(corpName);
		corpNameDTO.setCorpNumber(corpNumber);
		return corpNameDTO;
	}

	@Override
	public String formatCorpName(Long corpId) {

		String corpName = null;

		// if (corpParentFormat.equalsIgnoreCase("license_plate_format")){

		if (propertiesUtilService.getFancyCorpNumberFormat()) {
			if (corpId < 10000) {
				corpName = propertiesUtilService.getCorpPrefix().concat(propertiesUtilService.getCorpSeparator())
						.concat(String.format("%04d", corpId));
			} else if (corpId < 100000) {
				corpName = propertiesUtilService.getCorpPrefix().concat(propertiesUtilService.getCorpSeparator())
						.concat(String.format("%05d", corpId));
			} else if (corpId < 1000000) {
				corpName = propertiesUtilService.getCorpPrefix().concat(propertiesUtilService.getCorpSeparator())
						.concat(String.format("%06d", corpId));
			} else if (corpId < 10000000) {
				corpName = propertiesUtilService.getCorpPrefix().concat(propertiesUtilService.getCorpSeparator())
						.concat(String.format("%07d", corpId));
			}
		} else {
			String formatCorpNumber = "%0" + propertiesUtilService.getNumberCorpDigits() + "d";
			logger.debug("format corpNumber: " + formatCorpNumber);
			corpName = propertiesUtilService.getCorpPrefix().concat(propertiesUtilService.getCorpSeparator())
					.concat(String.format(formatCorpNumber, corpId));
		}

		return corpName;
	}

	@Override
	public String generateSaltFormCorpName(String parentCorpName, Set<IsoSalt> isoSalts) {

		logger.debug("number of isoSalts found: " + isoSalts.size());
		List<IsoSalt> isotopes = new ArrayList<IsoSalt>();
		List<IsoSalt> salts = new ArrayList<IsoSalt>();
		for (IsoSalt isoSalt : isoSalts) {
			if (isoSalt.getType().equalsIgnoreCase("isotope")) {
				isotopes.add(isoSalt);
			} else if (isoSalt.getType().equalsIgnoreCase("salt")) {
				salts.add(isoSalt);
			}
		}
		logger.debug("number of isotopes found: " + isotopes.size());
		ArrayList<String> isotopeAbbrevs = new ArrayList<String>();
		if (isotopes.size() > 0) {
			for (IsoSalt iso : isotopes) {
				Isotope isotope = Isotope.findIsotope(iso.getIsotope().getId());
				String abbrev = isotope.getAbbrev();
				isotopeAbbrevs.add(abbrev);
			}
			Collections.sort(isotopeAbbrevs);
		}
		logger.debug("number of salts found: " + salts.size());
		ArrayList<String> saltAbbrevs = new ArrayList<String>();
		if (salts.size() > 0) {
			for (IsoSalt isoSalt : salts) {
				Salt salt = Salt.findSalt(isoSalt.getSalt().getId());
				String abbrev = salt.getAbbrev();
				saltAbbrevs.add(abbrev);
			}
			Collections.sort(saltAbbrevs);
		}
		String saltFormName = "";
		for (String abbrev : isotopeAbbrevs) {
			saltFormName = saltFormName.concat(abbrev);
		}
		for (String abbrev : saltAbbrevs) {
			saltFormName = saltFormName.concat(abbrev);
		}

		logger.debug("saltBeforeLot is: " + propertiesUtilService.getSaltBeforeLot());
		String corpName = null;
		if (saltFormName.length() == 0) {
			saltFormName = "0";
			if (!propertiesUtilService.getSaltBeforeLot()) {

				corpName = parentCorpName;
				return corpName;
			}
		}

		corpName = parentCorpName.concat(propertiesUtilService.getCorpSeparator()).concat(saltFormName);
		return corpName;

	}
}
