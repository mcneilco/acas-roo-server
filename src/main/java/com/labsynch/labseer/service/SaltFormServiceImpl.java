package com.labsynch.labseer.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.labsynch.labseer.domain.IsoSalt;
import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.SaltForm;
import com.labsynch.labseer.exceptions.DupeSaltFormStructureException;
import com.labsynch.labseer.exceptions.SaltFormMolFormatException;
import com.labsynch.labseer.service.ChemStructureService.StructureType;
import com.labsynch.labseer.utils.PropertiesUtilService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaltFormServiceImpl implements SaltFormService {

	@Autowired
	private ChemStructureService chemService;

	@Autowired
	private CorpNameService corpNameService;

	@Autowired
	private LotService lotService;

	@Autowired
	private IsoSaltService isoSaltService;

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	private static final Logger logger = LoggerFactory.getLogger(SaltFormServiceImpl.class);

	public void updateSaltWeight(SaltForm saltForm) {
		double totalSaltWeight = 0d;

		Set<IsoSalt> isoSalts = saltForm.getIsoSalts();
		for (IsoSalt isoSalt : isoSalts) {
			double saltWeight = 0;
			logger.debug("isoSalt type: " + isoSalt.getType());
			if (isoSalt.getType().equalsIgnoreCase("isotope")) {
				logger.debug("isoSalt type: " + isoSalt.getType());

				saltWeight = isoSalt.getIsotope().getMassChange();
				logger.debug("intial saltWeight: " + saltWeight);
				logger.debug("equivalents: " + isoSalt.getEquivalents());
				saltWeight = saltWeight * isoSalt.getEquivalents();
				logger.debug("final saltWeight: " + saltWeight);
			} else if (isoSalt.getType().equalsIgnoreCase("salt")) {
				saltWeight = isoSalt.getSalt().getMolWeight();
				logger.debug("intial saltWeight: " + saltWeight);
				logger.debug("equivalents: " + isoSalt.getEquivalents());
				// correction for charged salts
				saltWeight = saltWeight * isoSalt.getEquivalents();
				saltWeight = saltWeight - (1.00794) * (isoSalt.getSalt().getCharge() * isoSalt.getEquivalents());
				logger.debug("final saltWeight: " + saltWeight);
			}
			totalSaltWeight = totalSaltWeight + saltWeight;
			logger.debug("current totalSaltWeigth: " + totalSaltWeight);
		}
		saltForm.setSaltWeight(totalSaltWeight);
		saltForm.merge();

	}

	public SaltForm updateSaltForm(Parent parent, SaltForm saltForm, Set<IsoSalt> isoSalts, Lot lot,
			double totalSaltWeight, ArrayList<ErrorMessage> errors)
			throws SaltFormMolFormatException, DupeSaltFormStructureException {
		boolean changedSaltForm = false;
		Set<Long> currentIsoSaltIds = new HashSet<Long>();
		HashSet<IsoSalt> addedIsoSalts = new HashSet<IsoSalt>();
		HashSet<IsoSalt> changedIsoSalts = new HashSet<IsoSalt>();
		HashSet<IsoSalt> deletedIsoSalts = new HashSet<IsoSalt>();
		for (IsoSalt isoSalt : isoSalts) {
			if (isoSalt.getId() == null) {
				changedSaltForm = true;
				logger.debug("New isoSalt");
				addedIsoSalts.add(isoSalt);

			} else {
				IsoSalt oldIsoSalt = IsoSalt.findIsoSalt(isoSalt.getId());
				currentIsoSaltIds.add(oldIsoSalt.getId());
				boolean isotopeSame = (oldIsoSalt.getIsotope() == null && isoSalt.getIsotope() == null)
						|| (oldIsoSalt.getIsotope() != null && isoSalt.getIsotope() != null
								&& oldIsoSalt.getIsotope().getAbbrev().equals(isoSalt.getIsotope().getAbbrev()));
				boolean saltSame = (oldIsoSalt.getSalt() == null && isoSalt.getSalt() == null)
						|| (oldIsoSalt.getSalt() != null && isoSalt.getSalt() != null
								&& oldIsoSalt.getSalt().getAbbrev().equals(isoSalt.getSalt().getAbbrev()));
				boolean equivalentsSame = (oldIsoSalt.getEquivalents() == null && isoSalt.getEquivalents() == null)
						|| (oldIsoSalt.getEquivalents() != null && isoSalt.getEquivalents() != null
								&& (Math.abs(oldIsoSalt.getEquivalents() - isoSalt.getEquivalents()) < 1e-9));
				boolean typeSame = (oldIsoSalt.getType() == null && isoSalt.getType() == null)
						|| (oldIsoSalt.getType() != null && isoSalt.getType() != null
								&& oldIsoSalt.getType().equals(isoSalt.getType()));

				if (!isotopeSame || !saltSame || !equivalentsSame || !typeSame) {
					changedSaltForm = true;
					// logger.debug("Updating isoSalt");
				}
				isoSalt.setVersion(oldIsoSalt.getVersion());
				changedIsoSalts.add(isoSalt);
			}
		}
		for (IsoSalt isoSalt : SaltForm.findSaltForm(saltForm.getId()).getIsoSalts()) {
			if (!currentIsoSaltIds.contains(isoSalt.getId())) {
				changedSaltForm = true;
				deletedIsoSalts.add(isoSalt);
			}
		}
		if (changedSaltForm) {
			if (propertiesUtilService.getSaltBeforeLot()) {
				// isSaltBeforeLot == true; Parent > SaltForm > Lot mode
				// search for existing saltForms matching changed saltForm by set of isoSalts
				boolean isNewSaltForm = true;
				List<SaltForm> oldSaltForms = SaltForm.findSaltFormsByParent(parent).getResultList();
				for (SaltForm oldSaltForm : oldSaltForms) {
					if (checkIsoSaltSetsSame(isoSalts, oldSaltForm.getIsoSalts())) {
						isNewSaltForm = false;
						SaltForm previousSaltForm = SaltForm.findSaltForm(saltForm.getId());
						previousSaltForm.getLots().remove(lot);
						previousSaltForm.merge();
						saltForm = oldSaltForm;
						lot.setSaltForm(saltForm);
						lot.setLotNumber(-1);
						String oldCorpName = lot.getCorpName();
						lot.setCorpName(lotService.generateCorpName(lot));
						String newCorpName = lot.getCorpName();
						errors.add(new ErrorMessage("info",
								"Changing saltForm has renamed this lot from " + oldCorpName + " to " + newCorpName));
						saltForm.getLots().add(lot);
						saltForm.merge();
						break;
					}
				}
				if (isNewSaltForm) {
					SaltForm newSaltForm = new SaltForm();
					String saltFormCorpName = corpNameService.generateSaltFormCorpName(parent.getCorpName(), isoSalts);
					newSaltForm.setParent(parent);
					newSaltForm.setCasNumber(saltForm.getCasNumber());
					newSaltForm.setChemist(saltForm.getChemist());
					newSaltForm.setRegistrationDate(new Date());
					newSaltForm.setCorpName(saltFormCorpName);
					int cdId = 0;
					if (saltForm.getMolStructure() == null || saltForm.getMolStructure().trim().equalsIgnoreCase("")) {
						logger.debug("no salt form structure");

					} else {
						cdId = chemService.saveStructure(saltForm.getMolStructure(), StructureType.SALT_FORM, true);
						if (cdId == -1) {
							ErrorMessage saltFormError = new ErrorMessage();
							saltFormError.setLevel("error");
							saltFormError
									.setMessage("Bad molformat. Please fix the molfile: " + saltForm.getMolStructure());
							logger.error(saltFormError.getMessage());
							errors.add(saltFormError);
							throw new SaltFormMolFormatException();
						} else if (cdId == 0) {
							ErrorMessage saltFormError = new ErrorMessage();
							saltFormError.setLevel("warning");
							saltFormError.setMessage("Duplicate saltForm found. Please select existing saltForm.");
							logger.error(saltFormError.getMessage());
							errors.add(saltFormError);
							throw new DupeSaltFormStructureException(
									"Duplicate saltForm found. Please select existing saltForm.");
						}
					}
					newSaltForm.setCdId(cdId);
					newSaltForm.persist();
					saltForm = newSaltForm;
					lot.setSaltForm(saltForm);
					lot.setCorpName(lotService.generateCorpName(lot));
					lot.merge();
					Set<IsoSalt> newIsoSalts = new HashSet<IsoSalt>();
					for (IsoSalt isoSalt : isoSalts) {
						IsoSalt newIsoSalt = new IsoSalt();
						newIsoSalt.setSaltForm(saltForm);
						newIsoSalt.setType(isoSalt.getType());
						newIsoSalt.setSalt(isoSalt.getSalt());
						newIsoSalt.setIsotope(isoSalt.getIsotope());
						newIsoSalt.setEquivalents(isoSalt.getEquivalents());
						newIsoSalt.persist();
						newIsoSalts.add(newIsoSalt);
						double saltWeight = isoSaltService.calculateSaltWeight(newIsoSalt);
						totalSaltWeight = totalSaltWeight + saltWeight;
						logger.debug("current totalSaltWeigth: " + totalSaltWeight);
					}
					saltForm.setIsoSalts(newIsoSalts);
					saltForm.setSaltWeight(totalSaltWeight);
					saltForm.merge();
				} else {
					lot.setSaltForm(saltForm);
					lot.setCorpName(lotService.generateCorpName(lot));
					// lot.merge();
				}
			} else {
				// isSaltBeforeLot == false; Parent > Lot mode
				// just update the saltForm in place
				logger.warn("SaltForm changed - updating saltForm and dependent lots");
				double newTotalSaltWeight = 0;
				for (IsoSalt isoSalt : isoSalts) {
					newTotalSaltWeight += isoSaltService.calculateSaltWeight(isoSalt);
				}
				for (IsoSalt isoSalt : addedIsoSalts) {
					SaltForm oldSaltForm = SaltForm.findSaltForm(saltForm.getId());
					isoSalt.setSaltForm(oldSaltForm);
					isoSalt.persist();
					isoSalt.flush();
				}
				for (IsoSalt isoSalt : deletedIsoSalts) {
					logger.debug("deleting isoSalt " + isoSalt.getId().toString());
					IsoSalt oldIsoSalt = IsoSalt.findIsoSalt(isoSalt.getId());
					isoSalt.setVersion(oldIsoSalt.getVersion());
					isoSalt.remove();
				}
				SaltForm oldSaltForm = SaltForm.findSaltForm(lot.getSaltForm().getId());
				oldSaltForm.setSaltWeight(newTotalSaltWeight);
				String saltFormCorpName = corpNameService.generateSaltFormCorpName(parent.getCorpName(), isoSalts);
				oldSaltForm.setCorpName(saltFormCorpName);
				for (IsoSalt isoSalt : isoSalts) {
					isoSalt.setSaltForm(oldSaltForm);
				}
				oldSaltForm.setIsoSalts(isoSalts);
				oldSaltForm.setCasNumber(saltForm.getCasNumber());
				oldSaltForm.merge();
				oldSaltForm.flush();
				lot.setSaltForm(oldSaltForm);
				List<String> changedLotCorpNameMessages = new ArrayList<String>();
				Lot oldLot = Lot.findLot(lot.getId());
				String oldLotCorpName = oldLot.getCorpName();
				lot.setLotMolWeight(Lot.calculateLotMolWeight(oldLot));
				String newLotCorpName = oldLot.getCorpName();
				if (!propertiesUtilService.getCorpBatchFormat().equalsIgnoreCase("cas_style_format")) {
					// if NOT cas_style lot corpName; generate a new lot corp name based on lot
					// numbering
					lot.setCorpName(lotService.generateCorpName(lot));
					newLotCorpName = lot.getCorpName();
				}
				lot.merge();
				if (!oldLotCorpName.equals(newLotCorpName))
					changedLotCorpNameMessages.add(oldLotCorpName + " to " + newLotCorpName);
				if (!changedLotCorpNameMessages.isEmpty()) {
					ErrorMessage affectedLotsError = new ErrorMessage();
					affectedLotsError.setLevel("info");
					String affectedLotsMessage = "Editing saltForm has changed the corp name from ";
					for (String changedLotCorpNameMessage : changedLotCorpNameMessages) {
						affectedLotsMessage += changedLotCorpNameMessage;
					}
					affectedLotsError.setMessage(affectedLotsMessage);
					errors.add(affectedLotsError);
				}
				saltForm = oldSaltForm;
			}
		} else {
			saltForm.setVersion(SaltForm.findSaltForm(saltForm.getId()).getVersion());
			saltForm.setIsoSalts(isoSalts);
		}
		return saltForm;
	}

	public static boolean checkIsoSaltsSame(IsoSalt isoSalt, IsoSalt oldIsoSalt) {
		boolean isotopeSame = (oldIsoSalt.getIsotope() == null && isoSalt.getIsotope() == null)
				|| (oldIsoSalt.getIsotope() != null && isoSalt.getIsotope() != null
						&& oldIsoSalt.getIsotope().getAbbrev().equals(isoSalt.getIsotope().getAbbrev()));
		boolean saltSame = (oldIsoSalt.getSalt() == null && isoSalt.getSalt() == null) || (oldIsoSalt.getSalt() != null
				&& isoSalt.getSalt() != null && oldIsoSalt.getSalt().getAbbrev().equals(isoSalt.getSalt().getAbbrev()));
		boolean equivalentsSame = (oldIsoSalt.getEquivalents() == null && isoSalt.getEquivalents() == null)
				|| (oldIsoSalt.getEquivalents() != null && isoSalt.getEquivalents() != null
						&& (Math.abs(oldIsoSalt.getEquivalents() - isoSalt.getEquivalents()) < 1e-9));
		boolean typeSame = (oldIsoSalt.getType() == null && isoSalt.getType() == null) || (oldIsoSalt.getType() != null
				&& isoSalt.getType() != null && oldIsoSalt.getType().equals(isoSalt.getType()));
		if (!isotopeSame || !saltSame || !equivalentsSame || !typeSame) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean checkIsoSaltSetsSame(Set<IsoSalt> isoSalts, Set<IsoSalt> oldIsoSalts) {
		if (isoSalts.size() != oldIsoSalts.size())
			return false;
		else {
			isoSaltsLoop: for (IsoSalt isoSalt : isoSalts) {
				for (IsoSalt oldIsoSalt : oldIsoSalts) {
					if (checkIsoSaltsSame(isoSalt, oldIsoSalt))
						continue isoSaltsLoop;
				}
				return false;
			}
			return true;
		}
	}

	@Override
	public double calculateSaltWeight(SaltForm saltForm) {
		double totalSaltWeight = 0;
		for (IsoSalt isoSalt : saltForm.getIsoSalts()) {
			double saltWeight = isoSaltService.calculateSaltWeight(isoSalt);
			totalSaltWeight = totalSaltWeight + saltWeight;
			logger.debug("current totalSaltWeigth: " + totalSaltWeight);
		}
		return totalSaltWeight;
	}

}
