package com.labsynch.labseer.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.chemclasses.CmpdRegMoleculeFactory;
import com.labsynch.labseer.chemclasses.CmpdRegSDFReader;
import com.labsynch.labseer.chemclasses.CmpdRegSDFReaderFactory;
import com.labsynch.labseer.chemclasses.CmpdRegSDFWriter;
import com.labsynch.labseer.chemclasses.CmpdRegSDFWriterFactory;
import com.labsynch.labseer.domain.BulkLoadFile;
import com.labsynch.labseer.domain.BulkLoadTemplate;
import com.labsynch.labseer.domain.CompoundType;
import com.labsynch.labseer.domain.DryRunCompound;
import com.labsynch.labseer.domain.FileList;
import com.labsynch.labseer.domain.IsoSalt;
import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.domain.LotAlias;
import com.labsynch.labseer.domain.Operator;
import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.ParentAlias;
import com.labsynch.labseer.domain.ParentAnnotation;
import com.labsynch.labseer.domain.PhysicalState;
import com.labsynch.labseer.domain.PurityMeasuredBy;
import com.labsynch.labseer.domain.Salt;
import com.labsynch.labseer.domain.SaltForm;
import com.labsynch.labseer.domain.SolutionUnit;
import com.labsynch.labseer.domain.StandardizationDryRunCompound;
import com.labsynch.labseer.domain.StereoCategory;
import com.labsynch.labseer.domain.Unit;
import com.labsynch.labseer.domain.Vendor;
import com.labsynch.labseer.dto.BatchCodeDependencyDTO;
import com.labsynch.labseer.dto.BulkLoadParentSaltFormLotDTO;
import com.labsynch.labseer.dto.BulkLoadPropertiesDTO;
import com.labsynch.labseer.dto.BulkLoadPropertyMappingDTO;
import com.labsynch.labseer.dto.BulkLoadRegisterSDFRequestDTO;
import com.labsynch.labseer.dto.BulkLoadRegisterSDFResponseDTO;
import com.labsynch.labseer.dto.BulkLoadSDFPropertyRequestDTO;
import com.labsynch.labseer.dto.BulkLoadSDFValidationPropertiesResponseDTO;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.ContainerBatchCodeDTO;
import com.labsynch.labseer.dto.LabelPrefixDTO;
import com.labsynch.labseer.dto.Metalot;
import com.labsynch.labseer.dto.MetalotReturn;
import com.labsynch.labseer.dto.PurgeFileDependencyCheckResponseDTO;
import com.labsynch.labseer.dto.PurgeFileResponseDTO;
import com.labsynch.labseer.dto.SimpleBulkLoadPropertyDTO;
import com.labsynch.labseer.dto.StrippedSaltDTO;
import com.labsynch.labseer.dto.ValidationResponseDTO;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.exceptions.DupeLotException;
import com.labsynch.labseer.exceptions.DupeParentException;
import com.labsynch.labseer.exceptions.MissingPropertyException;
import com.labsynch.labseer.exceptions.NonUniqueAliasException;
import com.labsynch.labseer.exceptions.SaltedCompoundException;
import com.labsynch.labseer.service.ChemStructureService.StructureType;
import com.labsynch.labseer.utils.PropertiesUtilService;
import com.labsynch.labseer.utils.SimpleUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONSerializer;

import com.labsynch.labseer.dto.CmpdRegBatchCodeDTO;

@Service
public class BulkLoadServiceImpl implements BulkLoadService {

	Logger logger = LoggerFactory.getLogger(BulkLoadServiceImpl.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;
 
	@Autowired
	public ChemStructureService chemStructureService;

	@Autowired
	private LotService lotService;

	@Autowired
	public MetalotService metalotService;

	@Autowired
	public CmpdRegMoleculeFactory moleculeFactory;

	@Autowired
	public CmpdRegSDFReaderFactory sdfReaderFactory;

	@Autowired
	public CmpdRegSDFWriterFactory sdfWriterFactory;

	@Autowired
	private ParentAliasService parentAliasService;

	@Autowired
	private ContainerService containerService;

	@Override
	public BulkLoadPropertiesDTO readSDFPropertiesFromFile(BulkLoadSDFPropertyRequestDTO requestDTO) {
		// get file reading parameters from request
		String inputFileName = requestDTO.getFileName();
		int numRowsToRead = requestDTO.getNumRecords();
		// create the resultDTO to be filled in
		BulkLoadPropertiesDTO resultDTO = new BulkLoadPropertiesDTO(propertiesUtilService.getDbProperties());
		// fill in any provided mappings
		resultDTO.setBulkLoadProperties(requestDTO.getMappings());
		HashSet<SimpleBulkLoadPropertyDTO> foundProperties = new HashSet<SimpleBulkLoadPropertyDTO>();
		HashSet<ErrorMessage> errors = new HashSet<ErrorMessage>();
		int numRecordsRead = 0;
		try {
			CmpdRegSDFReader molReader = sdfReaderFactory.getCmpdRegSDFReader(inputFileName);
			int batchSize = numRowsToRead;
			// If reading the whole file, do it in batches
			if (batchSize == -1){
				batchSize = propertiesUtilService.getStandardizationBatchSize();
			}
			Collection<CmpdRegMolecule> mols = molReader.readNextMols(batchSize);
			while ((numRowsToRead == -1 || numRecordsRead < numRowsToRead) && (mols.size() > 0)) {
				for (CmpdRegMolecule mol : mols) {
					String[] propertyKeys = mol.getPropertyKeys();
					if (propertyKeys.length != 0) {
						for (String key : propertyKeys) {
							SimpleBulkLoadPropertyDTO propDTO = new SimpleBulkLoadPropertyDTO(key);
							propDTO.setDataType(mol.getPropertyType(key));
							foundProperties.add(propDTO);
						}
					}
					numRecordsRead++;
					logger.info("Num records read " + numRecordsRead);
				}
				// Read another batch
				mols = molReader.readNextMols(batchSize);
			}
		} catch (Exception e) {
			logger.error("Caught exception trying to read SDF properties (num records read was " + numRecordsRead + 1,
					") ", e);
			errors.add(new ErrorMessage("error",
					"Error when reading record number " + numRecordsRead + 1 + ": " + e.getMessage()));
			resultDTO.setErrors(errors);
			return resultDTO;
		}
		if (logger.isDebugEnabled())
			if (logger.isDebugEnabled())
				logger.debug("found: " + SimpleBulkLoadPropertyDTO.toJsonArray(foundProperties));
		// Assembly meta DTO:
		resultDTO.setSdfProperties(foundProperties);
		resultDTO.setErrors(errors);
		resultDTO.setNumRecordsRead(numRecordsRead);
		// if (resultDTO.getTemplateName() != null)
		// resultDTO.applyTemplateMappings(requestDTO.getTemplateName());
		resultDTO.autoAssignMappings();
		resultDTO.cleanMappings();
		if (requestDTO.getTemplateName() != null && requestDTO.getTemplateName().length() > 0) {
			resultDTO.checkAgainstTemplate(requestDTO.getTemplateName());
		}
		return resultDTO;
	}

	@Override
	public BulkLoadTemplate saveBulkLoadTemplate(
			BulkLoadTemplate templateToSave) {
		try {
			BulkLoadTemplate oldTemplate = BulkLoadTemplate
					.findBulkLoadTemplatesByTemplateNameEqualsAndRecordedByEquals(templateToSave.getTemplateName(),
							templateToSave.getRecordedBy())
					.getSingleResult();
			if (logger.isDebugEnabled())
				logger.debug("Found existing template. Trying to update.");
			oldTemplate.update(templateToSave);
			if (logger.isDebugEnabled())
				if (logger.isDebugEnabled())
					logger.debug("Updated template to: " + oldTemplate.toJson());
			return oldTemplate;
		} catch (NoResultException e) {
			if (logger.isDebugEnabled())
				logger.debug("Saving new template");
			templateToSave.persist();
			return templateToSave;
		}
	}

	@Override
	// @Transactional
	public BulkLoadSDFValidationPropertiesResponseDTO validationProperties(BulkLoadRegisterSDFRequestDTO requestDTO) {
		// get properties out the request
		String inputFileName = requestDTO.getFilePath();
		Long startTime = new Date().getTime();
		Long currentTime = new Date().getTime();
		String chemist = requestDTO.getUserName();
		Collection<BulkLoadPropertyMappingDTO> mappings = requestDTO.getMappings();

		try {
			// create our record of the BulkLoadFile that will be updated with the number of
			// molecules later
			logger.info("inputFileName: " + inputFileName);
			String[] splitString;
			if (File.separator.equalsIgnoreCase("\\")) {
				logger.info("file sep is \\");
				inputFileName = inputFileName.replace("\\", "\\\\");
				logger.info("modified inputFileName: " + inputFileName);
				splitString = inputFileName.split("\\\\");
			} else {
				splitString = inputFileName.split(File.separator);
			}
			String shortFileName = splitString[splitString.length - 1];
			logger.info("shortFileName: " + shortFileName);

			int fileSizeInBytes = (int) (new File(inputFileName)).length();

			CmpdRegSDFReader molReader = sdfReaderFactory.getCmpdRegSDFReader(inputFileName);
			List<String> chemists = new ArrayList<String>();
			List<String> projects = new ArrayList<String>();
			int numRecordsRead = 0;
			// Read records in bulk, using configured batch size
			int batchSize = propertiesUtilService.getStandardizationBatchSize();
			boolean done = false;
			while (!done) {
				// Read the next set of molecules from the SDF
				Collection<CmpdRegMolecule> batchOfMols = molReader.readNextMols(batchSize);
				if (batchOfMols.size() == 0){
					done = true;
					break;
				}
				for (CmpdRegMolecule mol : batchOfMols) {
					numRecordsRead++;
					String lotChemist = getStringValueFromMappings(mol, "Lot Chemist", mappings);
					if (!chemists.contains(lotChemist)) {
						chemists.add(lotChemist);
					}
					String lotProject = getStringValueFromMappings(mol, "Project", mappings);
					if (lotProject != null && !projects.contains(lotProject)) {
						projects.add(lotProject);
					}
					
					currentTime = new Date().getTime();
					if (currentTime > startTime) {
						logger.info("VALIDATION SPEED REPORT:");
						logger.info("Time Elapsed:" + (currentTime - startTime));
						logger.info("Rows Handled:" + numRecordsRead);
						logger.info("Average speed (rows/min):"
								+ (numRecordsRead / ((currentTime - startTime) / 60.0 / 1000.0)));
					}
				}				
			}
			return new BulkLoadSDFValidationPropertiesResponseDTO(chemists, projects);
		} catch (Exception e) {
			logger.error("Caught an error in the big loop", e);
			return new BulkLoadSDFValidationPropertiesResponseDTO(null, null);
		}
	}

	@Transactional
	public int saveDryRunCompound(CmpdRegMolecule mol, Parent parent, int numRecordsRead, DryRunCompound dryRunCompound)
			throws CmpdRegMolFormatException {
		int cdId = chemStructureService.saveStructure(parent.getCmpdRegMolecule(), StructureType.DRY_RUN, false);
		dryRunCompound = new DryRunCompound();
		dryRunCompound.setCdId(cdId);
		dryRunCompound.setRecordNumber(numRecordsRead);
		dryRunCompound.setCorpName(parent.getCorpName());
		dryRunCompound.setStereoCategory(parent.getStereoCategory().getCode());
		dryRunCompound.setStereoComment(parent.getStereoComment());
		dryRunCompound.setMolStructure(mol.getMolStructure());
		dryRunCompound.persist();
		return cdId;
	}

	@Override
	// @Transactional
	public BulkLoadRegisterSDFResponseDTO registerSdf(BulkLoadRegisterSDFRequestDTO registerRequestDTO) {
		// get properties out the request
		String inputFileName = registerRequestDTO.getFilePath();
		String originalFileName = registerRequestDTO.getOriginalFileName();
		Long startTime = new Date().getTime();
		String chemist = registerRequestDTO.getUserName();
		Collection<ValidationResponseDTO> results = new ArrayList();
		Boolean validate = registerRequestDTO.getValidate();
		if (validate == null) {
			validate = false;
		}
		DryRunCompound dryRunCompound = new DryRunCompound();

		if (validate) {
			boolean dropTable = chemStructureService.truncateStructureTable(StructureType.DRY_RUN);
			dryRunCompound.truncateTable();
		}

		Collection<BulkLoadPropertyMappingDTO> mappings = registerRequestDTO.getMappings();
		// instantiate input and output streams
		FileOutputStream errorCSVOutStream;
		FileOutputStream registeredCSVOutStream;
		FileOutputStream reportOutStream;
		// main try/catch block
		try {
			// create our record of the BulkLoadFile that will be updated with the number of
			// molecules later
			logger.info("inputFileName: " + inputFileName);
			String[] splitString;
			if (File.separator.equalsIgnoreCase("\\")) {
				logger.info("file sep is \\");
				inputFileName = inputFileName.replace("\\", "\\\\");
				logger.info("modified inputFileName: " + inputFileName);
				splitString = inputFileName.split("\\\\");
			} else {
				splitString = inputFileName.split(File.separator);
			}
			String shortFileName = splitString[splitString.length - 1];
			logger.info("shortFileName: " + shortFileName);

			int fileSizeInBytes = (int) (new File(inputFileName)).length();
			BulkLoadFile bulkLoadFile = new BulkLoadFile(shortFileName, originalFileName, 0, fileSizeInBytes,
					BulkLoadPropertyMappingDTO.toJsonArray(mappings), chemist, new Date());
			if (registerRequestDTO.getFileDate() != null)
				bulkLoadFile.setFileDate(registerRequestDTO.getFileDate());
			else
				bulkLoadFile.setFileDate(new Date());
			if (!validate) {
				bulkLoadFile.persist();
			}

			// construct the error and report file names and output streams
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			String ymd = String.format("%04d", cal.get(Calendar.YEAR)) + "-"
					+ String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-"
					+ String.format("%02d", cal.get(Calendar.DATE));
			String rootOutFileName = inputFileName + "_" + ymd + "_";

			String errorSDFName = rootOutFileName + "errors.sdf";
			String errorCSVName = rootOutFileName + "errors.csv";
			String registeredSuffix = "registered";
			if (validate) {
				registeredSuffix = "validated";
			}
			String registeredSDFName = rootOutFileName + registeredSuffix + ".sdf";
			String registeredCSVName = rootOutFileName + registeredSuffix + ".csv";
			String reportName = rootOutFileName + "report.log";

			Collection<String> reportFiles = new HashSet<String>();
			reportFiles.add(errorSDFName);
			reportFiles.add(errorCSVName);
			reportFiles.add(registeredSDFName);
			reportFiles.add(registeredCSVName);
			reportFiles.add(reportName);

			CmpdRegSDFReader molReader = sdfReaderFactory.getCmpdRegSDFReader(inputFileName);

			// errorSDFOutStream = new FileOutputStream (errorSDFName, false);
			errorCSVOutStream = new FileOutputStream(errorCSVName, false);
			// registeredSDFOutStream = new FileOutputStream (registeredSDFName, false);
			registeredCSVOutStream = new FileOutputStream(registeredCSVName, false);
			reportOutStream = new FileOutputStream(reportName, false);

			// write headers for csv's
			String errorCSVHeaders = "Record Number,"
					+ "Corp Name in File,"
					+ "Corp Name in DB,"
					+ "Alias Corp Names in File,"
					+ "Error Description,"
					+ "Warnings\n";
			errorCSVOutStream.write(errorCSVHeaders.getBytes());
			String registeredCSVHeaders = "Record Number,"
					+ "Corp Name in File,"
					+ "Corp Name in DB,"
					+ "Registered Parent Corp Name,"
					+ "Registered Parent Aliases,"
					+ "Registered Lot Aliases,"
					+ "Registration Level,"
					+ "Warnings\n";
			registeredCSVOutStream.write(registeredCSVHeaders.getBytes());

			CmpdRegSDFWriter errorMolExporter = sdfWriterFactory.getCmpdRegSDFWriter(errorSDFName);
			CmpdRegSDFWriter registeredMolExporter = sdfWriterFactory.getCmpdRegSDFWriter(registeredSDFName);
			HashMap<String, Integer> allAliasMaps = new HashMap<String, Integer>();

			int numRecordsRead = 0;
			int numNewParentsLoaded = 0;
			int numNewLotsOldParentsLoaded = 0;
			boolean done = false;

			int batchSize = propertiesUtilService.getStandardizationBatchSize();
			while (!done) {
				// Read the next set of molecules from the SDF
				Collection<CmpdRegMolecule> batchOfMols = molReader.readNextMols(batchSize);
				if (batchOfMols.size() == 0){
					done = true;
					break;
				}
				// Process structures in bulk
				HashMap<String, String> origMolStructures = new LinkedHashMap<String, String>();
				HashMap<String, CmpdRegMolecule> origMols = new LinkedHashMap<String, CmpdRegMolecule>();
				HashMap<String, CmpdRegMolecule> standardizedMols = null;
				int recNo = 0;
				for (CmpdRegMolecule mol : batchOfMols) {
					String key = String.valueOf(recNo);
					origMolStructures.put(key, mol.getMolStructure());
					origMols.put(key, mol);
					recNo++;
				}
				if (propertiesUtilService.getUseExternalStandardizerConfig()) {
					// Standardize in bulk to increase performance
					standardizedMols = chemStructureService.standardizeStructures(origMolStructures);
				} else {
					// If standardization is disabled, pass through the origMols as the "standardized" structures
					standardizedMols = origMols;
				}
				// Check for multiple fragments in bulk too
				List<Boolean> multipleFragmentsList = chemStructureService.checkForSalts(origMolStructures.values());
				for (String key : origMols.keySet()) {
					numRecordsRead++;
					CmpdRegMolecule origMol = origMols.get(key);
					CmpdRegMolecule standardizedMol = standardizedMols.get(key);
					Boolean multipleFragments = multipleFragmentsList.get(Integer.parseInt(key));
					try{
						Boolean isNewParent = registerMol(origMol, standardizedMol, multipleFragments, bulkLoadFile, mappings, errorCSVOutStream, registeredCSVOutStream, errorMolExporter, registeredMolExporter, allAliasMaps, numRecordsRead, results, validate, chemist, registerRequestDTO, dryRunCompound, startTime);
						if(isNewParent != null) {
							if (isNewParent)
								numNewParentsLoaded++;
							else
								numNewLotsOldParentsLoaded++;
						}
					} catch (Exception e) {
						logger.error("Error error on record " + numRecordsRead + ": " + e.getMessage());
					}
				}
			}
			// generate summary in two formats: HTML and plaintext
			String summaryHtml = generateSummaryHtml(numRecordsRead, numNewParentsLoaded, numNewLotsOldParentsLoaded,
					results);
			String summaryText = generateSummaryText(numRecordsRead, numNewParentsLoaded, numNewLotsOldParentsLoaded,
					results);
			// Write summary text to report file
			reportOutStream.write(summaryText.getBytes());
			if (logger.isDebugEnabled())
				logger.debug(summaryHtml);

			// truncate the validation table
			if (validate) {
				dryRunCompound.truncateTable();
			}

			errorCSVOutStream.close();
			registeredCSVOutStream.close();
			reportOutStream.close();

			registeredMolExporter.close();
			errorMolExporter.close();

			// if there were no errors, delete the error files, since they are empty
			Boolean errorFree = true;
			for (ValidationResponseDTO valResp : results) {
				if (valResp.getLevel() == "error") {
					errorFree = false;
					break;
				}
			}
			if (errorFree) {
				File errorCSVFile = new File(errorCSVName);
				File errorSDFFile = new File(errorSDFName);
				errorCSVFile.delete();
				errorSDFFile.delete();
				reportFiles.remove(errorSDFName);
				reportFiles.remove(errorCSVName);
			}

			// update the BulkLoadFile with how many records were read.
			bulkLoadFile.setNumberOfMols(numRecordsRead);
			if (!validate) {
				bulkLoadFile.merge();
			}
			if (validate) {
				logger.info("Finished validating bulk load file: " + bulkLoadFile.toJson());
			} else {
				logger.info("Finished bulk loading file: " + bulkLoadFile.toJson());
			}
			return new BulkLoadRegisterSDFResponseDTO(summaryHtml, results, reportFiles, bulkLoadFile.getId());
		} catch (Exception e) {
			logger.error("Caught an error in the big loop", e);
			results.add(new ValidationResponseDTO("error", -1, "Unassigned", e.getMessage(), e.getMessage()));
			return new BulkLoadRegisterSDFResponseDTO(e.getMessage(), results, null, null);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Boolean registerMol(CmpdRegMolecule mol, CmpdRegMolecule standardizedMol, Boolean multipleFragments, BulkLoadFile bulkLoadFile,Collection<BulkLoadPropertyMappingDTO> mappings,
			FileOutputStream errorCSVOutStream, FileOutputStream registeredCSVOutStream,
			CmpdRegSDFWriter errorMolExporter, CmpdRegSDFWriter registeredMolExporter,
			HashMap<String, Integer> allAliasMaps, int numRecordsRead, Collection<ValidationResponseDTO> results, boolean validate, String chemist, 
			BulkLoadRegisterSDFRequestDTO registerRequestDTO, DryRunCompound dryRunCompound, Long startTime) throws IOException, CmpdRegMolFormatException{
		boolean isNewParent = true;
		// We are building up a Metalot, which will have a nested Lot, SaltForm, and
		// Parent
		Parent parent;
		SaltForm saltForm;
		Lot lot;
		// attempt to strip salts
		try {
			CmpdRegMolecule inputMol = mol;
			mol = processForSaltStripping(mol, multipleFragments, mappings, results, numRecordsRead);
			// Reset the multipleFragments boolean and standardizedMol if salt stripping changed the mol
			if (mol != inputMol) {
				multipleFragments = null;
				standardizedMol = null;
			}
		} catch (CmpdRegMolFormatException e) {
			String emptyMolfile = "\n" +
					"  Ketcher 09111712282D 1   1.00000     0.00000     0\n" +
					"\n" +
					"  0  0  0     0  0            999 V2000\n" +
					"M  END\n" +
					"";
			CmpdRegMolecule emptyMol = mol.replaceStructure(emptyMolfile);
			logError(e, numRecordsRead, emptyMol, mappings, errorMolExporter, results, errorCSVOutStream);
			return null;
		}
		try {
			parent = createParent(mol, standardizedMol, multipleFragments, mappings, chemist, registerRequestDTO.getLabelPrefix(), results,
					numRecordsRead);
		} catch (Exception e) {
			logError(e, numRecordsRead, mol, mappings, errorMolExporter, results, errorCSVOutStream);
			return null;
		}
		try {
			parent = validateParent(parent, chemist, mappings, numRecordsRead, results);
		} catch (PersistenceException rollbackException) {
			logError(rollbackException, numRecordsRead, mol, mappings, errorMolExporter, results,
					errorCSVOutStream);
					return null;
		} catch (Exception e) {
			logError(e, numRecordsRead, mol, mappings, errorMolExporter, results, errorCSVOutStream);
			return null;
		}
		if (parent.getId() != null)
			isNewParent = false;
		try {
			saltForm = createSaltForm(mol, mappings, results, numRecordsRead);
		} catch (Exception e) {
			logError(e, numRecordsRead, mol, mappings, errorMolExporter, results, errorCSVOutStream);
			return null;
		}
		saltForm.setParent(parent);
		try {
			saltForm = validateSaltForm(saltForm, mappings);
		} catch (Exception e) {
			logError(e, numRecordsRead, mol, mappings, errorMolExporter, results, errorCSVOutStream);
			return null;
		}

		try {
			lot = createLot(mol, numRecordsRead, mappings, results, bulkLoadFile.getFileDate(), chemist);
		} catch (Exception e) {
			logError(e, numRecordsRead, mol, mappings, errorMolExporter, results, errorCSVOutStream);
			return null;
		}
		lot.setSaltForm(saltForm);
		lot.setParent(parent);

		if (lot.getId() == null)
			lot.setBulkLoadFile(bulkLoadFile);
		if (saltForm.getId() == null)
			saltForm.setBulkLoadFile(bulkLoadFile);
		if (parent.getId() == null)
			parent.setBulkLoadFile(bulkLoadFile);

		try {
			lot = validateLot(lot, mappings, results);
		} catch (Exception e) {
			logError(e, numRecordsRead, mol, mappings, errorMolExporter, results, errorCSVOutStream);
			return null;
		}

		Metalot metalot = new Metalot();
		metalot.setLot(lot);
		metalot.setIsosalts(lot.getSaltForm().getIsoSalts());
		metalot.setSkipParentDupeCheck(true);
		MetalotReturn metalotReturn = null;

		if (validate) {
			try {
				parent = validateParentAgainstDryRunCompound(parent, numRecordsRead, results);
				// Check list of aliases within file being bulkloaded
				if (!propertiesUtilService.getAllowDuplicateParentAliases()) {
					for (ParentAlias alias : parent.getParentAliases()) {
						// Skip ignored and deleted aliases
						if (alias.isDeleted() | alias.isIgnored()) {
							continue;
						}
						// Make sure the parent doesn't already have this alias name
						if (allAliasMaps.get(alias.getAliasName()) == null) {
							allAliasMaps.put(alias.getAliasName(), numRecordsRead);
						} else {
							throw new NonUniqueAliasException(
									"Within File, Parent Alias " + alias.getAliasName()
											+ " is not unique");
						}
					}
				}
				saveDryRunCompound(mol, parent, numRecordsRead, dryRunCompound);

			} catch (TransactionSystemException rollbackException) {
				logger.error("Rollback exception", rollbackException.getApplicationException());
				Exception causeException = new Exception(
						rollbackException.getApplicationException().getMessage(),
						rollbackException.getApplicationException());
				logError(causeException, numRecordsRead, mol, mappings, errorMolExporter, results,
						errorCSVOutStream);
				return null;
			} catch (Exception e) {
				logError(e, numRecordsRead, mol, mappings, errorMolExporter, results, errorCSVOutStream);
				return null;
			}

		}
		if (!validate) {
			try {
				metalotReturn = metalotService.save(metalot);
				if (!metalotReturn.getErrors().isEmpty()) {
					for (ErrorMessage errorMessage : metalotReturn.getErrors()) {
						Exception e = new Exception(errorMessage.getMessage());
						logError(e, numRecordsRead, mol, mappings, errorMolExporter, results,
								errorCSVOutStream);
					}
					return null;
				}
			} catch (Exception e) {
				logError(e, numRecordsRead, mol, mappings, errorMolExporter, results, errorCSVOutStream);
				return null;
			}
		}

		// Successful registration/validation: update summary and write
		// registered/validated record
		writeRegisteredMol(numRecordsRead, mol, metalotReturn, mappings, registeredMolExporter,
				registeredCSVOutStream, isNewParent, results);

		Long currentTime = new Date().getTime();
		if (currentTime > startTime) {
			logger.info("REGISTRATION SPEED REPORT:");
			logger.info("Time Elapsed:" + (currentTime - startTime));
			logger.info("Rows Handled:" + numRecordsRead);
			logger.info("Average speed (rows/min):"
					+ (numRecordsRead / ((currentTime - startTime) / 60.0 / 1000.0)));
		}

		return isNewParent;
	}

	public void writeRegisteredMol(int numRecordsRead, CmpdRegMolecule mol, MetalotReturn metalotReturn,
			Collection<BulkLoadPropertyMappingDTO> mappings, CmpdRegSDFWriter registeredMolExporter,
			FileOutputStream registeredCSVOutStream, Boolean isNewParent,
			Collection<ValidationResponseDTO> validationResponse) throws IOException, CmpdRegMolFormatException {
		String sdfCorpName = getStringValueFromMappings(mol, "Parent Corp Name", mappings);
		String dbCorpName = "";
		String registeredParentCorpName = "";
		String allParentAliases = "";
		String allLotAliases = "";

		BulkLoadPropertyMappingDTO mapping = BulkLoadPropertyMappingDTO.findMappingByDbPropertyEquals(mappings,
				"Lot Corp Name");
		if (metalotReturn != null) {
			dbCorpName = metalotReturn.getMetalot().getLot().getCorpName();

			registeredParentCorpName = metalotReturn.getMetalot().getLot().getSaltForm().getParent().getCorpName();
			Collection<ParentAlias> parentAliases = metalotReturn.getMetalot().getLot().getSaltForm().getParent()
					.getParentAliases();
			Collection<LotAlias> lotAliases = metalotReturn.getMetalot().getLot().getLotAliases();
			List<String> parentAliasList = new ArrayList<String>();
			if (!parentAliases.isEmpty()) {
				for (ParentAlias parentAlias : parentAliases) {
					parentAliasList.add(parentAlias.getAliasName());
				}
			}
			List<String> lotAliasList = new ArrayList<String>();
			if (!lotAliases.isEmpty()) {
				for (LotAlias lotAlias : lotAliases) {
					lotAliasList.add(lotAlias.getAliasName());
				}
			}
			mol.setProperty("Registered Parent Corp Name", registeredParentCorpName);
			mol.setProperty("Registered Lot Corp Name", dbCorpName);
			mol.setProperty("Registered Lot Project", ((metalotReturn.getMetalot().getLot().getProject() == null) ? ""
					: metalotReturn.getMetalot().getLot().getProject()));
			mol.setProperty("Registered Lot Barcode", ((metalotReturn.getMetalot().getLot().getBarcode() == null) ? ""
					: metalotReturn.getMetalot().getLot().getBarcode()));
			mol.setProperty("Registered Lot Amount", ((metalotReturn.getMetalot().getLot().getAmount() == null) ? ""
					: metalotReturn.getMetalot().getLot().getAmount().toString()));
			mol.setProperty("Registered Lot Amount Units",
					((metalotReturn.getMetalot().getLot().getAmountUnits() == null) ? ""
							: metalotReturn.getMetalot().getLot().getAmountUnits().getCode()));
			mol.setProperty("Registered Lot Supplier", ((metalotReturn.getMetalot().getLot().getSupplier() == null) ? ""
					: metalotReturn.getMetalot().getLot().getSupplier()));
			mol.setProperty("Registered Lot Supplier ID",
					((metalotReturn.getMetalot().getLot().getSupplierID() == null) ? ""
							: metalotReturn.getMetalot().getLot().getSupplierID()));
			mol.setProperty("Registered Lot Vendor ID",
					((metalotReturn.getMetalot().getLot().getVendorID() == null) ? ""
							: metalotReturn.getMetalot().getLot().getVendorID()));
			if (!parentAliasList.isEmpty()) {
				for (String alias : parentAliasList) {
					if (allParentAliases.length() == 0)
						allParentAliases += alias;
					else
						allParentAliases += "; " + alias;
				}
				mol.setProperty("Registered Parent Aliases", allParentAliases);
			}
		}
		ArrayList<String> warningMessagesArray = new ArrayList<>();
		for (ValidationResponseDTO valResp : validationResponse) {
			if (valResp.getRecord() == numRecordsRead && valResp.getLevel() == "warning") {
				warningMessagesArray.add(valResp.getMessage());
			}
		}
		String warningMessages = String.join("; ", warningMessagesArray);
		mol.setProperty("Warnings", warningMessages);

		String registrationLevel;
		if (isNewParent)
			registrationLevel = "New parent with new lot";
		else
			registrationLevel = "New lot of existing parent";
		mol.setProperty("Registration Level", registrationLevel);
		registeredMolExporter.writeMol(mol);
		String csvRow = numRecordsRead + ",\"" + sdfCorpName + "\",\"" + dbCorpName + "\",\"" + registeredParentCorpName
				+ "\",\"" + allParentAliases + "\",\"" + allLotAliases + "\",\"" + registrationLevel + "\",\""
				+ warningMessages + "\"\n";
		registeredCSVOutStream.write(csvRow.getBytes());
	}

	public void logError(Exception e, int numRecordsRead, CmpdRegMolecule mol,
			Collection<BulkLoadPropertyMappingDTO> mappings, CmpdRegSDFWriter errorMolExporter,
			Collection<ValidationResponseDTO> validationResponse, FileOutputStream errorCSVOutStream)
			throws IOException, CmpdRegMolFormatException {
		logger.error("Caught exception on molecule number " + numRecordsRead, e);

		String errorMessage = e.getMessage();

		// assuming sdf and db corpNames are Lot Corp Name
		String sdfCorpName = getStringValueFromMappings(mol, "Parent Corp Name", mappings);
		String dbCorpName = "";
		String aliasCorpNames = "";
		String categoryCode = "Unassigned";
		BulkLoadPropertyMappingDTO mapping = BulkLoadPropertyMappingDTO.findMappingByDbPropertyEquals(mappings,
				"Lot Corp Name");
		if (e.getClass() == DupeLotException.class) {
			DupeLotException dupeLotError = (DupeLotException) e;
			categoryCode = "DupeLot";
			dbCorpName = dupeLotError.getLotCorpName();
			if (dbCorpName != null && dbCorpName.length() > 0)
				mol.setProperty("Lot Corp Name in DB", dbCorpName);
		} else if (e.getClass() == DupeParentException.class) {
			DupeParentException dupeParentError = (DupeParentException) e;
			categoryCode = "DupeParent";
			dbCorpName = dupeParentError.getDbCorpName();
			sdfCorpName = dupeParentError.getSdfCorpName();
			if (dupeParentError.getAliasCorpNames() != null && !dupeParentError.getAliasCorpNames().isEmpty()) {
				for (String aliasCorpName : dupeParentError.getAliasCorpNames()) {
					if (aliasCorpName.length() == 0)
						aliasCorpNames += aliasCorpName;
					else
						aliasCorpNames += "; " + aliasCorpName;
				}
			}
			if (dbCorpName != null && dbCorpName.length() > 0)
				mol.setProperty("Parent Corp Name in DB", dbCorpName);
			if (sdfCorpName != null && sdfCorpName.length() > 0)
				mol.setProperty("Parent Corp Name in SDF", sdfCorpName);
			if (aliasCorpNames != null && aliasCorpNames.length() > 0)
				mol.setProperty("Alias Corp Names in SDF", aliasCorpNames);

		}

		validationResponse
				.add(new ValidationResponseDTO("error", numRecordsRead, categoryCode, errorMessage, errorMessage));
		mol.setProperty("Error", errorMessage);

		ArrayList<String> warningMessagesArray = new ArrayList<>();
		for (ValidationResponseDTO valResp : validationResponse) {
			if (valResp.getRecord() == numRecordsRead && valResp.getLevel() == "warning") {
				warningMessagesArray.add(valResp.getMessage());
			}
		}
		String warningMessages = String.join("; ", warningMessagesArray);
		mol.setProperty("Warnings", errorMessage);

		errorMolExporter.writeMol(mol);
		String csvRow = numRecordsRead + ",\"" + sdfCorpName + "\",\"" + dbCorpName + "\",\"" + aliasCorpNames + "\",\""
				+ errorMessage + "\",\"" + warningMessages + "\"\n";
		errorCSVOutStream.write(csvRow.getBytes());
	}

	public void logWarning(String categoryCode, String categoryDescription, String warningMessage, int numRecordsRead,
			Collection<ValidationResponseDTO> validationResponse) {
		logger.error("Caught warning on molecule number " + numRecordsRead, warningMessage);
		validationResponse.add(new ValidationResponseDTO("warning", numRecordsRead, categoryCode, categoryDescription,
				warningMessage));
		String warning = categoryCode + ": " + categoryDescription;
	}

	public Parent validateParent(Parent parent, String chemist, Collection<BulkLoadPropertyMappingDTO> mappings, int numRecordsRead,
			Collection<ValidationResponseDTO> validationResponse)
			throws MissingPropertyException, DupeParentException, SaltedCompoundException, Exception {
		// Grab the parent's CmpdRegMolecule in case it is lost when overwriting the parent variable
		CmpdRegMolecule standardizedMol = parent.getCmpdRegMolecule();
		// Search for the parent structure + stereo category
		if (parent.getStereoCategory() == null)
			throw new MissingPropertyException("Parent Stereo Category must be provided");
		HashSet<String> requiredDbProperties = new HashSet<String>();
		for (BulkLoadPropertyMappingDTO mapping : mappings) {
			if (mapping.isRequired())
				requiredDbProperties.add(mapping.getDbProperty());
		}
		// check parent corp name for whitespace
		if (parent.getCorpName() != null && parent.getCorpName().length() > 0) {
			parent.setCorpName(parent.getCorpName().trim());
			if (parent.getCorpName().contains(" ") | parent.getCorpName().contains("\t")
					| parent.getCorpName().contains("\n")) {
				throw new Exception("Parent Corp Name: " + parent.getCorpName() + " contains internal whitespace.");
			}
		}
		// check if parent stereo category is "See Comments" but no stereo comment is
		// provided
		if (parent.getStereoCategory().getCode().equalsIgnoreCase("see_comments")
				&& (parent.getStereoComment() == null || parent.getStereoComment().length() == 0)) {
			logger.error("Stereo category is See Comments, but no stereo comment provided");
			throw new MissingPropertyException("Stereo category is See Comments, but no stereo comment provided");
		}
		int[] dupeParentList = {};
		if (propertiesUtilService.getRegisterNoStructureCompoundsAsUniqueParents()
				&& chemStructureService.isEmpty(parent.getMolStructure())) {
			// if true then we are no checking this one for hits
			logger.warn(
					"mol is empty and registerNoStructureCompoundsAsUniqueParents so not checking for dupe parents by structure but other dupe checking will be done");
		} else {
			dupeParentList = chemStructureService.searchMolStructures(parent.getCmpdRegMolecule(), StructureType.PARENT, ChemStructureService.SearchType.DUPLICATE_TAUTOMER, -1F, -1);
		}
		if (dupeParentList.length > 0) {
			// If we find a duplicate parent, we want to maintain the multiple fragments boolean we already have to save time since we have already checked for this
			// and it is a transient property which wil be null on the found parent we may be overwriting with
			Boolean multipleFragments = parent.getMultipleFragments();
			searchResultLoop: for (int foundParentCdId : dupeParentList) {
				List<Parent> foundParents = Parent.findParentsByCdId(foundParentCdId).getResultList();

				DupeParentException dupeException = null;
				String categoryDescription;
				for (Parent foundParent : foundParents) {
					// same structure
					boolean sameStereoCategory = (parent.getStereoCategory().getCode()
							.equalsIgnoreCase(foundParent.getStereoCategory().getCode()));
					boolean sameStereoComment = (((parent.getStereoComment() == null
							|| parent.getStereoComment().length() < 1)
							&& (foundParent.getStereoComment() == null || foundParent.getStereoComment().length() < 1))
							|| (parent.getStereoComment() != null && foundParent.getStereoComment() != null
									&& parent.getStereoComment().equalsIgnoreCase(foundParent.getStereoComment())));
					boolean sameCorpName = (parent.getCorpName() != null
							&& parent.getCorpName().equals(foundParent.getCorpName()));
					boolean noCorpName = (parent.getCorpName() == null);
					boolean sameCorpPrefixOrNoPrefix = (parent.getLabelPrefix() == null
							|| foundParent.getCorpName().contains(parent.getLabelPrefix().getLabelPrefix()));
					if (sameStereoCategory & sameStereoComment
							& (sameCorpName | (noCorpName & sameCorpPrefixOrNoPrefix))) {
						// parents match (based on above criteria)
						boolean equalAliases = compareParentAliases(parent, foundParent);
						// If Incoming Parent and Found Parent Have Equal Aliases 
						if(equalAliases) {
							// No Changes or Modifications Needed; Can Break Loop
							// Continue 
							parent = foundParent;
							// Add the multiple fragments boolean back to the parent
							// from the transient property we calculated earlier
							parent.setMultipleFragments(multipleFragments);
							break searchResultLoop; 
						} else { 
							// Need to Update Aliases of Matching Parent to Be Union of Two Sets
							parent = updateParentAliases(parent, foundParent, chemist); 		
							break searchResultLoop; 					
						}
					} else if (sameStereoCategory & sameStereoComment & !sameCorpName & !noCorpName) {
						// corp name conflict
						logger.error(
								"Mismatched corp names for same parent structure, stereo category and stereo comment! sdf corp name: "
										+ parent.getCorpName() + " db corp name: " + foundParent.getCorpName());
						if (dupeException == null)
							dupeException = new DupeParentException(
									"Mismatched corp names for same parent structure, stereo category and stereo comment!",
									foundParent.getCorpName(), parent.getCorpName(), new ArrayList<String>());
					} else if (sameStereoCategory & sameStereoComment & noCorpName & !sameCorpPrefixOrNoPrefix) {
						// corp prefix conflict
						logger.error(
								"Mismatched corp prefix for same parent structure, stereo category, and stereo comment! sdf corp prefix: "
										+ parent.getLabelPrefix().getLabelPrefix() + " db corp name: "
										+ foundParent.getCorpName());
						if (dupeException == null)
							dupeException = new DupeParentException(
									"Mismatched corp prefix for same parent structure, stereo category, and stereo comment!",
									foundParent.getCorpName(), parent.getLabelPrefix().getLabelPrefix(),
									new ArrayList<String>());
					} else if (sameStereoCategory & !sameStereoComment & sameCorpName & !noCorpName) {
						// stereo comment conflict for same corpName
						logger.error(
								"Mismatched stereo comments for same parent structure, stereo category and corp name! Corp name: "
										+ parent.getCorpName() + " sdf stereo category: "
										+ parent.getStereoCategory().getCode() + " sdf stereo comment: "
										+ parent.getStereoComment() + " db stereo category: "
										+ foundParent.getStereoCategory().getCode() + " db stereo comment: "
										+ foundParent.getStereoComment());
						if (dupeException == null)
							dupeException = new DupeParentException(
									"Mismatched stereo comments for same parent structure, stereo category and corp name!",
									foundParent.getCorpName(), parent.getCorpName(), new ArrayList<String>());
					} else if (sameStereoCategory & !sameStereoComment & (!sameCorpName | noCorpName)) {
						// same stereo category, but different stereo comment => new parent
						categoryDescription = "Matching structure found with same stereo category but different stereo comment and different corp name";
						logWarning("MatchingStructureSameStereoDifferentComment", categoryDescription,
								categoryDescription + ": " + foundParent.getCorpName(), numRecordsRead,
								validationResponse);
						categoryDescription = "New parent will be assigned due to different stereo comment";
						// logWarning will add to validationResponse and pass information to user 
						logWarning("AssigningNewParent", categoryDescription,
								categoryDescription + ": " + foundParent.getCorpName(), numRecordsRead,
								validationResponse);
						continue;
					} else if (!sameStereoCategory & sameCorpName & !noCorpName) {
						logger.error("Mismatched stereo categories for same parent structure and corp name! Corp name: "
								+ parent.getCorpName() + " sdf stereo category: " + parent.getStereoCategory().getCode()
								+ " db stereo category: " + foundParent.getStereoCategory().getCode());
						if (dupeException == null)
							dupeException = new DupeParentException(
									"Mismatched stereo categories for same parent structure and corp name!",
									foundParent.getCorpName(), parent.getCorpName(), new ArrayList<String>());
					} else if (!sameStereoCategory & (!sameCorpName | noCorpName)) {
						// Different stereo category, different corpName => new parent
						categoryDescription = "Matching structure found with different stereo category and different db corp name";
						logWarning("MatchingStructureDifferentStereoCategory", categoryDescription,
								categoryDescription + ": " + foundParent.getCorpName(), numRecordsRead,
								validationResponse);
						categoryDescription = "New parent will be assigned due to different stereo category";
						// logWarning will add to validationResponse and pass information to user 
						logWarning("AssigningNewParent", categoryDescription,
							categoryDescription + ": " + foundParent.getCorpName(), numRecordsRead,
							validationResponse);
						continue;
					}
				}
				if (dupeException != null) {
					throw dupeException;
				}
			}
		}

		// Only run mixture check if we have not identified an already existing parent
		if(parent.getId() == null) {
			// check for multiple fragments, and if isMixture or not
			Boolean hasMultipleFragments = parent.getMultipleFragments();
			if (hasMultipleFragments == null){
				hasMultipleFragments = chemStructureService.checkForSalt(parent.getMolStructure());
			}
			if (hasMultipleFragments) {
				// multiple fragments
				if (parent.getIsMixture() != null) {
					if (!parent.getIsMixture()) {
						logger.error("Multiple fragments detected. Please fix or mark \"Parent Is Mixture\" as true");
						throw new SaltedCompoundException(
								"Multiple fragments detected. Please fix or mark \"Parent Is Mixture\" as true");
					} else {
						// do nothing - structure is appropriately marked as a mixture
					}
				} else {
					logger.error("Multiple fragments detected. Please fix or mark \"Parent Is Mixture\" as true");
					throw new SaltedCompoundException(
							"Multiple fragments detected. Please fix or mark \"Parent Is Mixture\" as true");
				}
			}
		}
		// If parent has not already been identified, see if the corpName is provided
		// and exists
		if (parent.getId() == null && parent.getCorpName() != null && parent.getCorpName().length() > 0) {
			Parent foundParent = null;
			try {
				foundParent = Parent.findParentsByCorpNameEquals(parent.getCorpName()).getSingleResult();
				boolean structuresMatch = chemStructureService.standardizedMolCompare(parent.getMolStructure(),
						foundParent.getMolStructure());

				if (!structuresMatch) {
					logger.error("Parent corp name already exists for a different parent structure: "
							+ foundParent.getCorpName());
					throw new DupeParentException("Parent corp name already exists for a different parent structure: "
							+ foundParent.getCorpName());
				} else {
					parent = foundParent;
				}
			} catch (NoResultException empty) {
				logger.warn("Parent corp name entered that doesn't already exist (sdf corp name: '"
						+ parent.getCorpName() + "'), this compound will be registered with a new parent corp name: ");
				foundParent = null;
			} catch (DupeParentException dupe) {
				throw dupe;
			} catch (Exception e) {
				logger.error(
						"Caught exception comparing parent structures for parent corp name: " + parent.getCorpName(),
						e);
				foundParent = null;
			}
			if (foundParent != null && foundParent != parent)
				throw new DupeParentException(
						"Duplicate corp names for different parent structure and stereo category! sdf corp name: "
								+ parent.getCorpName() + " db corp name: " + foundParent.getCorpName());
		}
		// Validate lot aliases locally and in the database
		parentAliasService.validateParentAliases(parent.getId(), parent.getParentAliases());

		// If the parent's cmpdRegMolecule was lost, put it back
		if (parent.getCmpdRegMolecule() == null){
			parent.setCmpdRegMolecule(standardizedMol);
		}

		return parent;
	}

	public Parent validateParentAgainstDryRunCompound(Parent parent, int numRecordsRead,
			Collection<ValidationResponseDTO> validationResponse)
			throws MissingPropertyException, DupeParentException, SaltedCompoundException, Exception {
		int[] dupeDryRunCompoundsList = chemStructureService.searchMolStructures(parent.getCmpdRegMolecule(), StructureType.DRY_RUN, ChemStructureService.SearchType.DUPLICATE_TAUTOMER,  -1F, -1);
		if (dupeDryRunCompoundsList.length > 0) {
			searchResultLoop: for (int foundParentCdId : dupeDryRunCompoundsList) {
				List<DryRunCompound> foundDryRunCompounds = DryRunCompound.findDryRunCompoundsByCdId(foundParentCdId)
						.getResultList();

				DupeParentException dupeException = null;
				String categoryDescription;
				for (DryRunCompound foundDryRunCompound : foundDryRunCompounds) {
					// same structure
					boolean sameStereoCategory = (parent.getStereoCategory().getCode()
							.equalsIgnoreCase(foundDryRunCompound.getStereoCategory()));
					boolean sameStereoComment = (((parent.getStereoComment() == null
							|| parent.getStereoComment().length() < 1)
							&& (foundDryRunCompound.getStereoComment() == null
									|| foundDryRunCompound.getStereoComment().length() < 1))
							|| (parent.getStereoComment() != null && foundDryRunCompound.getStereoComment() != null
									&& parent.getStereoComment()
											.equalsIgnoreCase(foundDryRunCompound.getStereoComment())));
					boolean sameCorpName = (parent.getCorpName() != null && foundDryRunCompound.getCorpName() != null
							&& parent.getCorpName().equals(foundDryRunCompound.getCorpName()));
					boolean noCorpName = (parent.getCorpName() == null);
					boolean noCorpNameDryRunCompound = (foundDryRunCompound.getCorpName() == null);
					// Its possible that the first compound in the validate table has no corporate
					// ID specified and because we aren't currently predicting what the corp name
					// will be that it receives as part of label
					// sequences, we need to guard against the null corporate ID. In the case that a
					// found dry run compound has a null corp name and the new corp name has a
					// corporte ID specified, this is NOT considered
					// the same prefix. If both compounds are null corp names then we DO consider
					// them to have the same corp prefix. If the found dry run compound has a
					// specified corp name and the parent does not, then
					// we need to check the found dry run compound for the matching specified prefix
					// to make sure they will get the same corp name id on the non dry run.
					boolean sameCorpPrefixOrNoPrefix = (parent.getLabelPrefix() == null
							|| (noCorpName && noCorpNameDryRunCompound)
							|| (!noCorpNameDryRunCompound && foundDryRunCompound.getCorpName()
									.contains(parent.getLabelPrefix().getLabelPrefix())));
					if (sameStereoCategory & sameStereoComment
							& (sameCorpName | (noCorpName & (noCorpNameDryRunCompound | sameCorpPrefixOrNoPrefix)))) {
						// parents match
						break searchResultLoop;
					} else if (sameStereoCategory & sameStereoComment & !sameCorpName & !noCorpName) {
						// corp name conflict
						logger.error(
								"Within file, mismatched corp names for same parent structure, stereo category and stereo comment! sdf corp name: '"
										+ parent.getCorpName() + "' earlier sdf record corp name: '"
										+ foundDryRunCompound.getCorpName() + "'");
						if (dupeException == null)
							dupeException = new DupeParentException(
									"Within file, mismatched corp names for same parent structure, stereo category and stereo comment!",
									foundDryRunCompound.getCorpName(), parent.getCorpName(), new ArrayList<String>());
					} else if (sameStereoCategory & sameStereoComment & noCorpName & !sameCorpPrefixOrNoPrefix) {
						// corp prefix conflict
						logger.error(
								"Within file, mismatched corp prefix for same parent structure, stereo category, and stereo comment! sdf corp prefix: '"
										+ parent.getLabelPrefix().getLabelPrefix() + "' earlier sdf record corp name: '"
										+ foundDryRunCompound.getCorpName() + "'");
						if (dupeException == null)
							dupeException = new DupeParentException(
									"Within file, mismatched corp prefix for same parent structure, stereo category, and stereo comment!",
									foundDryRunCompound.getCorpName(), parent.getLabelPrefix().getLabelPrefix(),
									new ArrayList<String>());
					} else if (sameStereoCategory & !sameStereoComment & sameCorpName & !noCorpName) {
						// stereo comment conflict for same corpName
						logger.error(
								"Within file, mismatched stereo comments for same parent structure, stereo category and corp name! Corp name: "
										+ parent.getCorpName() + " sdf stereo category: '"
										+ parent.getStereoCategory().getCode() + "' sdf stereo comment: '"
										+ parent.getStereoComment() + "' earlier sdf record stereo category: '"
										+ foundDryRunCompound.getStereoCategory()
										+ "' earlier sdf record stereo comment: '"
										+ foundDryRunCompound.getStereoComment() + "'");
						if (dupeException == null)
							dupeException = new DupeParentException(
									"Within file, mismatched stereo comments for same parent structure, stereo category and corp name!",
									foundDryRunCompound.getCorpName(), parent.getCorpName(), new ArrayList<String>());
					} else if (sameStereoCategory & !sameStereoComment & (!sameCorpName | noCorpName)) {
						// same stereo category, but different stereo comment => new parent
						categoryDescription = "Within file, matching structure found with same stereo category but different stereo comment and different corp name";
						logWarning("WithinFileMatchingStructureSameStereoDifferentComment", categoryDescription,
								categoryDescription + ": " + foundDryRunCompound.getCorpName() + "(record number "
										+ foundParentCdId + ")",
								numRecordsRead, validationResponse);
						categoryDescription = "New parent will be assigned due to different stereo comment";
						// logWarning will add to validationResponse and pass information to user 
						logWarning("WithinFileMatchingStructureSameStereoDifferentComment", categoryDescription,
								categoryDescription + ": " + foundDryRunCompound.getCorpName() + "(record number "
								+ foundParentCdId + ")", numRecordsRead, validationResponse);
						continue;
					} else if (!sameStereoCategory & sameCorpName & !noCorpName) {
						logger.error(
								"Within file, mismatched stereo categories for same parent structure and corp name! Corp name: '"
										+ parent.getCorpName() + "' sdf stereo category: '"
										+ parent.getStereoCategory().getCode()
										+ "' earlier sdf record stereo category: '"
										+ foundDryRunCompound.getStereoCategory() + "'");
						if (dupeException == null)
							dupeException = new DupeParentException(
									"Within file, mismatched stereo categories for same parent structure and corp name!",
									foundDryRunCompound.getCorpName(), parent.getCorpName(), new ArrayList<String>());
					} else if (!sameStereoCategory & (!sameCorpName | noCorpName)) {
						// Different stereo category, different corpName => new parent
						categoryDescription = "Within file, matching structure found with different stereo category and different corp name";
						logWarning("WithinFileMatchingStructureDifferentStereoCategory", categoryDescription,
								categoryDescription + ": " + foundDryRunCompound.getCorpName() + "(record number "
										+ foundParentCdId + ")",
								numRecordsRead, validationResponse);
						categoryDescription = "New parent will be assigned due to different stereo category";
						// logWarning will add to validationResponse and pass information to user 
						logWarning("WithinFileMatchingStructureDifferentStereoCategory", categoryDescription,
								categoryDescription + ": " + foundDryRunCompound.getCorpName() + "(record number "
								+ foundParentCdId + ")", numRecordsRead, validationResponse);
						continue;
					}
				}
				if (dupeException != null) {
					throw dupeException;
				}
			}
		} else {
			// At this point we know the structured doesn't already exist in the file so
			// check to see if Corp name already exists
			// if the corp name does exist, that is an error because it's a different
			// structure
			if (parent.getCorpName() != null && !parent.getCorpName().equals("")) {
				List<DryRunCompound> dryRunCmpds = DryRunCompound
						.findDryRunCompoundsByCorpNameEquals(parent.getCorpName()).getResultList();
				if (dryRunCmpds.size() > 0) {
					logger.error("Within file, parent corp name already exists for a different parent structure: "
							+ dryRunCmpds.get(0).getCorpName());
					throw new DupeParentException("Within file, parent corp name " + dryRunCmpds.get(0).getCorpName()
							+ " already exists for a different parent structure record number "
							+ dryRunCmpds.get(0).getRecordNumber());
				}
			}
		}

		return parent;
	}

	public SaltForm validateSaltForm(SaltForm saltForm, Collection<BulkLoadPropertyMappingDTO> mappings)
			throws CmpdRegMolFormatException {
		// only try to check for existing saltForm if server is in saltBeforeLot mode
		if (propertiesUtilService.getSaltBeforeLot()) {
			// structure search
			if (saltForm.getMolStructure() != null) {
				int[] dupeSaltFormList = chemStructureService.checkDupeMol(saltForm.getMolStructure(),
						StructureType.SALT_FORM);
				if (dupeSaltFormList.length > 0) {
					for (int foundSaltFormCdId : dupeSaltFormList) {
						// verify the found salt form has the same parent. If so, it's a match! If not,
						// probably belongs to a parent with another stereo category
						SaltForm foundSaltForm = SaltForm.findSaltFormsByCdId(foundSaltFormCdId).getSingleResult();
						if (foundSaltForm.getParent().getId() == saltForm.getParent().getId()) {
							if (logger.isDebugEnabled())
								logger.debug("Found matching existing salt form with same parent.");
							saltForm = foundSaltForm;
							return saltForm;
						}
					}
				}
			} else if (saltForm.getParent().getId() != null) {
				// parent already exists, so check if this saltForm may already exist
				Collection<SaltForm> existingSaltForms = SaltForm.findSaltFormsByParent(saltForm.getParent())
						.getResultList();
				for (SaltForm existingSaltForm : existingSaltForms) {
					if (existingSaltForm.getIsoSalts().size() == saltForm.getIsoSalts().size()) {
						if (saltForm.getIsoSalts().size() == 0) {
							saltForm = existingSaltForm;
							return saltForm;
						} else {
							// make a map of salt or isotope abbrev and equivalents for the new saltForm to
							// use for determining uniqueness
							Map<String, Double> isoSaltMap = new HashMap<String, Double>();
							for (IsoSalt isoSalt : saltForm.getIsoSalts()) {
								if (isoSalt.getSalt() != null)
									isoSaltMap.put(isoSalt.getSalt().getAbbrev(), isoSalt.getEquivalents());
								else
									isoSaltMap.put(isoSalt.getIsotope().getAbbrev(), isoSalt.getEquivalents());
							}
							boolean sameSaltForm = true;
							for (IsoSalt isoSalt : existingSaltForm.getIsoSalts()) {
								if (isoSalt.getSalt() != null) {
									if (!isoSaltMap.containsKey(isoSalt.getSalt().getAbbrev())) {
										sameSaltForm = false;
										break;
									} else if (Math.abs(isoSaltMap.get(isoSalt.getSalt().getAbbrev())
											- isoSalt.getEquivalents()) < 1e-4) {
										// same abbrev, and equivalents are equal
										continue;
									} else {
										// same abbrev, but different equivalents
										sameSaltForm = false;
									}
								}
							}
							if (sameSaltForm) {
								saltForm = existingSaltForm;
								return saltForm;
							}
						}
					}
				}
			}
		}
		return saltForm;
	}

	public Lot validateLot(Lot lot, Collection<BulkLoadPropertyMappingDTO> mappings,
			Collection<ValidationResponseDTO> results) throws MissingPropertyException, DupeLotException {
		// check for required fields
		HashSet<String> requiredDbProperties = new HashSet<String>();
		for (BulkLoadPropertyMappingDTO mapping : mappings) {
			if (mapping.isRequired())
				requiredDbProperties.add(mapping.getDbProperty());
		}
		HashSet<String> missingProperties = new HashSet<String>();
		if (lot.getLotNumber() == -1 && requiredDbProperties.contains("Lot Number"))
			missingProperties.add("Lot Number");
		if (lot.getSynthesisDate() == null && requiredDbProperties.contains("Lot Synthesis Date"))
			missingProperties.add("Lot Synthesis Date");
		if (lot.getNotebookPage() == null && requiredDbProperties.contains("Lot Notebook Page"))
			missingProperties.add("Lot Notebook Page");
		if (lot.getChemist() == null && requiredDbProperties.contains("Lot Chemist"))
			missingProperties.add("Lot Chemist");
		if (lot.getPurityMeasuredBy() == null && requiredDbProperties.contains("Lot Purity Measured By"))
			missingProperties.add("Lot Purity Measured By");

		// If lot inventory is one then we do extra validation to make sure
		// that lot barcode, amount and amount untis are filled in if the user
		// is trying to add lot inventory
		if (propertiesUtilService.getCompoundInventory()) {
			if ((lot.getAmount() != null || lot.getAmountUnits() != null) && lot.getBarcode() == null)
				missingProperties.add("Lot Barcode");
			if ((lot.getBarcode() != null || lot.getAmountUnits() != null) && lot.getAmount() == null)
				missingProperties.add("Lot Amount");
			if ((lot.getBarcode() != null || lot.getAmount() != null) && lot.getAmountUnits() == null)
				missingProperties.add("Lot Amount Units");
		}

		if (!missingProperties.isEmpty()) {
			String errorMessage = "";
			for (String missingProperty : missingProperties) {
				errorMessage += missingProperty + ", ";
			}
			errorMessage = errorMessage.substring(0, errorMessage.length() - 2) + " must be provided";
			throw new MissingPropertyException(errorMessage);
		}

		if (lot.getLotNumber() == 0) {
			lot.setIsVirtual(true);
		} else if (lot.getLotNumber() == -1 && lot.getIsVirtual() != null && lot.getIsVirtual() == true) {
			lot.setLotNumber(0);
		} else if (lot.getIsVirtual() != null && lot.getIsVirtual() == true) {
			throw new MissingPropertyException("Cannot register a virtual lot with lot number " + lot.getLotNumber()
					+ ". Try again with lot number = 0 for a virtual lot, or remove the isVirtual property if a non-virtual lot is desired.");
		} else if (lot.getLotNumber() == -1) {
			lot.setLotNumber(lotService.generateLotNumber(lot));
		}
		if (propertiesUtilService.getUseProjectRoles()) {
			// check project is assigned and is in allowed list
			if (lot.getProject() == null) {
				throw new MissingPropertyException("Project not specified. Please specify a valid project.");
			}
		}
		if (lot.getParent().getId() != null) {
			// Then check for duplicate lot (lot number should not match)
			Collection<Lot> previousLots = Lot.findLotsByParent(lot.getParent()).getResultList();
			if (previousLots.size() > 0) {
				for (Lot previousLot : previousLots) {
					if (previousLot.getLotNumber() == lot.getLotNumber()) {
						logger.error("Cannot register duplicate of lot: " + previousLot.getCorpName());
						// check if this duplicate lot came from the same/current BulkLoadFile, or
						// whether it was pre-existing
						if (previousLot.getBulkLoadFile() != null
								&& previousLot.getBulkLoadFile().getId() == lot.getBulkLoadFile().getId()) {
							throw new DupeLotException(
									"Duplicate lot cannot be registered due to duplicate in same bulk load file.",
									previousLot.getCorpName());
						} else {
							throw new DupeLotException(
									"Duplicate lot cannot be registered due to previously existing lot in database.",
									previousLot.getCorpName());
						}
					}
				}
			}
		}
		// Check if the lot corp name already exists in the database
		if (lot.getCorpName() != null) {
			try {
				Lot previousLot = Lot.findLotsByCorpNameEquals(lot.getCorpName()).getSingleResult();
				logger.error("Cannot register duplicate of lot: " + previousLot.getCorpName());
				// check if this duplicate lot came from the same/current BulkLoadFile, or
				// whether it was pre-existing
				if (previousLot.getBulkLoadFile() != null
						&& previousLot.getBulkLoadFile().getId() == lot.getBulkLoadFile().getId()) {
					throw new DupeLotException(
							"Duplicate lot cannot be registered due to duplicate in same bulk load file.",
							previousLot.getCorpName());
				} else {
					throw new DupeLotException(
							"Duplicate lot cannot be registered due to previously existing lot in database.",
							previousLot.getCorpName());
				}
			} catch (NoResultException e) {
				logger.debug("Not a duplicate lot corp name");
			}
		}

		return lot;
	}

	public Lot createLot(CmpdRegMolecule mol, int recordNumber, Collection<BulkLoadPropertyMappingDTO> mappings,
			Collection<ValidationResponseDTO> results, Date registrationDate, String chemist) throws Exception {
		// Here we try to fetch all of the possible Lot database properties from the
		// sdf, according to the mappings
		Lot lot = new Lot();
		// set a couple properties that do not come in from mappings
		lot.setAsDrawnStruct(mol.getMolStructure());
		lot.setRegistrationDate(registrationDate);
		lot.setRegisteredBy(chemist);

		// regular fields that do not require lookups or conversions
		lot.setSynthesisDate(getDateValueFromMappings(mol, "Lot Synthesis Date", mappings, results, recordNumber));
		lot.setNotebookPage(getStringValueFromMappings(mol, "Lot Notebook Page", mappings, results, recordNumber));
		lot.setCorpName(getStringValueFromMappings(mol, "Lot Corp Name", mappings, results, recordNumber));
		lot.setBarcode(getStringValueFromMappings(mol, "Lot Barcode", mappings, results, recordNumber));
		lot.setColor(getStringValueFromMappings(mol, "Lot Color", mappings, results, recordNumber));
		lot.setSupplier(getStringValueFromMappings(mol, "Lot Supplier", mappings, results, recordNumber));
		lot.setSupplierID(getStringValueFromMappings(mol, "Lot Supplier ID", mappings, results, recordNumber));
		lot.setVendorID(getStringValueFromMappings(mol, "Lot Vendor ID", mappings, results, recordNumber));
		lot.setComments(getStringValueFromMappings(mol, "Lot Comments", mappings, results, recordNumber));
		lot.setSupplierLot(getStringValueFromMappings(mol, "Lot Supplier Lot", mappings, results, recordNumber));
		lot.setMeltingPoint(getNumericValueFromMappings(mol, "Lot Melting Point", mappings, results, recordNumber));
		lot.setBoilingPoint(getNumericValueFromMappings(mol, "Lot Boiling Point", mappings, results, recordNumber));
		lot.setRetain(getNumericValueFromMappings(mol, "Lot Retain", mappings, results, recordNumber));
		lot.setPercentEE(getNumericValueFromMappings(mol, "Lot Percent ee", mappings, results, recordNumber));
		lot.setPurity(getNumericValueFromMappings(mol, "Lot Purity", mappings, results, recordNumber));
		lot.setAmount(getNumericValueFromMappings(mol, "Lot Amount", mappings, results, recordNumber));
		lot.setSolutionAmount(getNumericValueFromMappings(mol, "Lot Solution Amount", mappings, results, recordNumber));
		lot.setLambda(getNumericValueFromMappings(mol, "Lot Lambda", mappings, results, recordNumber));
		lot.setAbsorbance(getNumericValueFromMappings(mol, "Lot Absorbance", mappings, results, recordNumber));
		lot.setStockSolvent(getStringValueFromMappings(mol, "Lot Stock Solvent", mappings, results, recordNumber));
		lot.setStockLocation(getStringValueFromMappings(mol, "Lot Stock Location", mappings, results, recordNumber));
		lot.setRetainLocation(getStringValueFromMappings(mol, "Lot Retain Location", mappings, results, recordNumber));
		lot.setObservedMassOne(
				getNumericValueFromMappings(mol, "Lot Observed Mass #1", mappings, results, recordNumber));
		lot.setObservedMassTwo(
				getNumericValueFromMappings(mol, "Lot Observed Mass #2", mappings, results, recordNumber));
		lot.setTareWeight(getNumericValueFromMappings(mol, "Lot Tare Weight", mappings, results, recordNumber));
		lot.setTotalAmountStored(
				getNumericValueFromMappings(mol, "Lot Total Amount Stored", mappings, results, recordNumber));
		lot.setChemist(getStringValueFromMappings(mol, "Lot Chemist", mappings, results, recordNumber));

		// special field for Lot Inventory - not saved in Lot table
		lot.setStorageLocation(
				getStringValueFromMappings(mol, "Lot Storage Location", mappings, results, recordNumber));

		// conversions
		lot.setIsVirtual(
				Boolean.valueOf(getStringValueFromMappings(mol, "Lot Is Virtual", mappings, results, recordNumber)));
		Double lotNumber = getNumericValueFromMappings(mol, "Lot Number", mappings, results, recordNumber);
		if (lotNumber != null)
			lot.setLotNumber(lotNumber.intValue());
		else
			lot.setLotNumber(-1);

		// lookups
		String lookUpString = null;
		String lookUpProperty = null;
		Collection<String> lookUpStringList = null;

		lookUpProperty = "Lot Alias";
		lookUpStringList = getStringValuesFromMappings(mol, lookUpProperty, mappings, results, recordNumber);
		if (lookUpStringList != null && !lookUpStringList.isEmpty()) {
			String aliasType = "default";
			String aliasKind = "default";
			for (String lookUpStringEntry : lookUpStringList) {
				lot = addLotAlias(lot, aliasType, aliasKind, lookUpProperty, lookUpStringEntry);
			}
		}

		try {
			lookUpProperty = "Project";
			lookUpString = getStringValueFromMappings(mol, lookUpProperty, mappings, results, recordNumber);
			logger.info("Project lookup: " + lookUpString);
			lot.setProject(lookUpString);
			lookUpProperty = "Lot Purity Measured By";
			lookUpString = getStringValueFromMappings(mol, lookUpProperty, mappings, results, recordNumber);
			if (lookUpString != null && lookUpString.length() > 0) {
				try {
					lot.setPurityMeasuredBy(
							PurityMeasuredBy.findPurityMeasuredBysByNameEquals(lookUpString).getSingleResult());
				} catch (Exception e) {
					lot.setPurityMeasuredBy(
							PurityMeasuredBy.findPurityMeasuredBysByCodeEquals(lookUpString).getSingleResult());
				}
			}
			lookUpProperty = "Lot Physical State";
			lookUpString = getStringValueFromMappings(mol, lookUpProperty, mappings, results, recordNumber);
			if (lookUpString != null && lookUpString.length() > 0) {
				try {
					lot.setPhysicalState(PhysicalState.findPhysicalStatesByNameEqualsIgnoreCase(lookUpString).getSingleResult());
				} catch (Exception e) {
					lot.setPhysicalState(PhysicalState.findPhysicalStatesByCodeEqualsIgnoreCase(lookUpString).getSingleResult());
				}
			}
			lookUpProperty = "Lot Vendor";
			lookUpString = getStringValueFromMappings(mol, lookUpProperty, mappings, results, recordNumber);
			if (lookUpString != null && lookUpString.length() > 0) {
				try {
					lot.setVendor(Vendor.findVendorsByNameEqualsIgnoreCase(lookUpString).getSingleResult());
				} catch (Exception e) {
					lot.setVendor(Vendor.findVendorsByCodeEqualsIgnoreCase(lookUpString).getSingleResult());
				}
			}
			lookUpProperty = "Lot Purity Operator";
			lookUpString = getStringValueFromMappings(mol, lookUpProperty, mappings, results, recordNumber);
			if (lookUpString != null && lookUpString.length() > 0) {
				try {
					lot.setPurityOperator(Operator.findOperatorsByNameEquals(lookUpString).getSingleResult());
				} catch (Exception e) {
					lot.setPurityOperator(Operator.findOperatorsByCodeEquals(lookUpString).getSingleResult());
				}
			}
			lookUpProperty = "Lot Amount Units";
			lookUpString = getStringValueFromMappings(mol, lookUpProperty, mappings, results, recordNumber);
			if (lookUpString != null && lookUpString.length() > 0) {
				try {
					lot.setAmountUnits(Unit.findUnitsByNameEquals(lookUpString).getSingleResult());
				} catch (Exception e) {
					lot.setAmountUnits(Unit.findUnitsByCodeEquals(lookUpString).getSingleResult());
				}
			}
			lookUpProperty = "Lot Retain Units";
			lookUpString = getStringValueFromMappings(mol, lookUpProperty, mappings, results, recordNumber);
			if (lookUpString != null && lookUpString.length() > 0) {
				try {
					lot.setRetainUnits(Unit.findUnitsByNameEquals(lookUpString).getSingleResult());
				} catch (Exception e) {
					lot.setRetainUnits(Unit.findUnitsByCodeEquals(lookUpString).getSingleResult());
				}
			}
			lookUpProperty = "Lot Solution Amount Units";
			lookUpString = getStringValueFromMappings(mol, lookUpProperty, mappings, results, recordNumber);
			if (lookUpString != null && lookUpString.length() > 0) {
				try {
					lot.setSolutionAmountUnits(
							SolutionUnit.findSolutionUnitsByNameEquals(lookUpString).getSingleResult());
				} catch (Exception e) {
					lot.setSolutionAmountUnits(
							SolutionUnit.findSolutionUnitsByCodeEquals(lookUpString).getSingleResult());
				}
			}
			lookUpProperty = "Lot Tare Weight Units";
			lookUpString = getStringValueFromMappings(mol, lookUpProperty, mappings, results, recordNumber);
			if (lookUpString != null && lookUpString.length() > 0) {
				try {
					lot.setTareWeightUnits(Unit.findUnitsByNameEquals(lookUpString).getSingleResult());
				} catch (Exception e) {
					lot.setTareWeightUnits(Unit.findUnitsByCodeEquals(lookUpString).getSingleResult());
				}
			}
			lookUpProperty = "Lot Total Amount Stored Units";
			lookUpString = getStringValueFromMappings(mol, lookUpProperty, mappings, results, recordNumber);
			if (lookUpString != null && lookUpString.length() > 0) {
				try {
					lot.setTotalAmountStoredUnits(Unit.findUnitsByNameEquals(lookUpString).getSingleResult());
				} catch (Exception e) {
					lot.setTotalAmountStoredUnits(Unit.findUnitsByCodeEquals(lookUpString).getSingleResult());
				}
			}
		} catch (Exception e) {
			logger.error("Caught error looking up lot property " + lookUpProperty + " with name " + lookUpString, e);
			throw new Exception(
					"An error has occurred looking up lot property " + lookUpProperty + " with name " + lookUpString);
		}

		return lot;
	}

	public SaltForm createSaltForm(CmpdRegMolecule mol, Collection<BulkLoadPropertyMappingDTO> mappings,
			Collection<ValidationResponseDTO> results, int recordNumber) throws Exception {
		// Here we try to fetch all of the possible Lot database properties from the
		// sdf, according to the mappings
		SaltForm saltForm = new SaltForm();
		// saltForm.setMolStructure(mol.getMolStructure());
		saltForm.setRegistrationDate(new Date());

		// regular fields that do not require lookups or conversions
		saltForm.setCasNumber(getStringValueFromMappings(mol, "CAS Number", mappings, results, recordNumber));

		// lookups
		String lookUpString = null;
		String lookUpProperty = null;
		try {
			saltForm.setChemist(getStringValueFromMappings(mol, "Lot Chemist", mappings, results, recordNumber));
			lookUpProperty = "Lot Salt Abbrev";
			lookUpString = getStringValueFromMappings(mol, lookUpProperty, mappings, results, recordNumber);
			if (lookUpString != null && lookUpString.length() > 0) {
				// found a salt. Need to split by semicolon, look up salt by abbrev, and check
				// for a matching equivalents #
				logger.info("Found one or more salts: " + lookUpString);
				String[] saltCodes = lookUpString.split(";");
				List<String> trimmedSaltCodes = new ArrayList<String>();
				for (String saltCode : saltCodes) {
					String trimmedSaltCode = saltCode.trim();
					if (trimmedSaltCode != null && trimmedSaltCode.length() > 0)
						trimmedSaltCodes.add(trimmedSaltCode);
				}
				int numberOfSalts = trimmedSaltCodes.size();
				List<Salt> salts = new ArrayList<Salt>();
				for (String saltCode : trimmedSaltCodes) {
					try {
						Salt salt = Salt.findSaltsByAbbrevEquals(saltCode).getSingleResult();
						salts.add(salt);
					} catch (Exception e) {
						logger.error("Caught error looking up salt with abbrev: " + saltCode, e);
						throw new Exception("Caught error looking up salt with abbrev: " + saltCode);
					}
				}
				// if we reached this point, we have found all the referenced salts by abbrev.
				// We must check for equivalents.
				lookUpProperty = "Lot Salt Equivalents";
				lookUpString = getStringValueFromMappings(mol, lookUpProperty, mappings, results, recordNumber);
				if (lookUpString == null) {
					logger.error("Caught error creating salt: Lot Salt Equivalents must be supplied.");
					throw new Exception("Caught error creating salt: Lot Salt Equivalents must be supplied.");
				}
				String[] equivalentsStrings = lookUpString.split(";");
				List<String> trimmedEquivalentsStrings = new ArrayList<String>();
				for (String equivalentsString : equivalentsStrings) {
					String trimmedEquivalentsString = equivalentsString.trim();
					if (trimmedEquivalentsString != null && trimmedEquivalentsString.length() > 0)
						trimmedEquivalentsStrings.add(trimmedEquivalentsString);
				}
				List<Double> equivalentsList = new ArrayList<Double>();
				for (String equivalentsString : trimmedEquivalentsStrings) {
					equivalentsList.add(Double.valueOf(equivalentsString));
				}
				// check to make sure we have the same number of salts and equivalents.
				if (equivalentsList.size() != salts.size()) {
					logger.error("Caught error creating salts: Number of salts (" + salts.size()
							+ ") is not equal to number of equivalents provided (" + equivalentsList.size() + ")");
					throw new Exception("Caught error creating salts: Number of salts (" + salts.size()
							+ ") is not equal to number of equivalents provided (" + equivalentsList.size() + ")");
				} else {
					// Salts found and matched to number of equivalents. We create IsoSalts and add
					// them to our SaltForm
					Set<IsoSalt> isoSalts = new HashSet<IsoSalt>();
					int i = 0;
					while (i < numberOfSalts) {
						Salt salt = salts.get(i);
						Double equivalents = equivalentsList.get(i);
						IsoSalt isoSalt = new IsoSalt();
						isoSalt.setSalt(salt);
						isoSalt.setEquivalents(equivalents);
						isoSalt.setSaltForm(saltForm);
						isoSalt.setType("salt");
						isoSalts.add(isoSalt);
						i++;
					}
					saltForm.setIsoSalts(isoSalts);
				}
			}
		} catch (Exception e) {
			logger.error("Caught error looking up salt form property " + lookUpProperty + " with code " + lookUpString,
					e);
			throw new Exception("An error has occurred looking up salt form property " + lookUpProperty + " with code "
					+ lookUpString);
		}

		return saltForm;
	}

	@Transactional
	public Parent createParent(CmpdRegMolecule mol, CmpdRegMolecule standardizedMol, Boolean multipleFragments, Collection<BulkLoadPropertyMappingDTO> mappings, String chemist,
			LabelPrefixDTO labelPrefix, Collection<ValidationResponseDTO> results, int recordNumber) throws Exception {
		// Here we try to fetch all of the possible Lot database properties from the
		// sdf, according to the mappings
		Parent parent = new Parent();
		parent.setMolStructure(mol.getMolStructure());
		parent.setRegistrationDate(new Date());
		parent.setRegisteredBy(chemist);
		// The following transient properties are used later on in registration
		// Normally they should be populated, but when salt stripping has happened they may be blank
		// Check and fill them in if they aren't populated.
		if (standardizedMol == null) {
			if (propertiesUtilService.getUseExternalStandardizerConfig()) {
				// Standardize this single structure
				HashMap<String, String> inMap = new HashMap<String, String>();
				inMap.put("tmp", mol.getMolStructure());
				HashMap<String, CmpdRegMolecule> outMap = chemStructureService.standardizeStructures(inMap);
				standardizedMol = outMap.get("tmp");
			} else {
				// If standardization is disabled, pass through the current mol as the "standardized" structures
				standardizedMol = mol;
			}
		}
		if (multipleFragments == null) {
			multipleFragments = chemStructureService.checkForSalt(mol.getMolStructure());
		}
		parent.setCmpdRegMolecule(standardizedMol);
		parent.setMultipleFragments(multipleFragments);

		// regular fields that do not require lookups or conversions
		parent.setCorpName(getStringValueFromMappings(mol, "Parent Corp Name", mappings, results, recordNumber));
		// Not adding warning for coercing this next property because the warning is
		// covered in alias mappings
		parent.setCommonName(getStringValueFromMappings(mol, "Parent Common Name", mappings));
		parent.setStereoComment(
				getStringValueFromMappings(mol, "Parent Stereo Comment", mappings, results, recordNumber));
		parent.setComment(getStringValueFromMappings(mol, "Parent Comment", mappings, results, recordNumber));
		// Not adding warning for coercing this next property because the warning is
		// covered in create lot
		parent.setChemist(getStringValueFromMappings(mol, "Lot Chemist", mappings));

		// conversion
		// Not adding warning for coercing this next property because the warning is
		// covered salt stripping function
		parent.setIsMixture(
				Boolean.valueOf(getStringValueFromMappings(mol, "Parent Is Mixture", mappings, results, recordNumber)));

		String lookUpString = null;
		String lookUpProperty = null;
		Collection<String> lookUpStringList = null;
		Collection<String> invalidValues = null;

		// add parent aliases
		lookUpProperty = "Parent LiveDesign Corp Name";
		lookUpStringList = getStringValuesFromMappings(mol, lookUpProperty, mappings, results, recordNumber);

		if (lookUpStringList != null && !lookUpStringList.isEmpty()) {
			String aliasType = "External ID";
			String aliasKind = "LiveDesign Corp Name";
			for (String lookUpStringEntry : lookUpStringList) {
				parent = addParentAlias(parent, aliasType, aliasKind, lookUpProperty, lookUpStringEntry);
			}
		}

		lookUpProperty = "Parent Common Name";
		lookUpStringList = getStringValuesFromMappings(mol, lookUpProperty, mappings, results, recordNumber);
		if (lookUpStringList != null && !lookUpStringList.isEmpty()) {
			String aliasType = "other name";
			String aliasKind = "Common Name";
			for (String lookUpStringEntry : lookUpStringList) {
				parent = addParentAlias(parent, aliasType, aliasKind, lookUpProperty, lookUpStringEntry);
			}
		}

		lookUpProperty = "Parent Alias";
		lookUpStringList = getStringValuesFromMappings(mol, lookUpProperty, mappings, results, recordNumber);
		if (lookUpStringList != null && !lookUpStringList.isEmpty()) {
			String aliasType = "default";
			String aliasKind = "default";
			for (String lookUpStringEntry : lookUpStringList) {
				parent = addParentAlias(parent, aliasType, aliasKind, lookUpProperty, lookUpStringEntry);
			}
		}

		// lookups
		try {
			// Currently this section of invalid values passed in will only work for string
			// values
			for (BulkLoadPropertyMappingDTO mapping : mappings) {
				if (mapping.getInvalidValues() != null) {
					lookUpProperty = mapping.getDbProperty();
					lookUpString = getStringValueFromMappings(mol, lookUpProperty, mappings, results, recordNumber);
					invalidValues = mapping.getInvalidValues();
					if (invalidValues.contains(lookUpString)) {
						logger.error("Found invalid " + lookUpProperty + " with code " + lookUpString
								+ " in list of invalid values passed to service");
						throw new Exception("Invalid property");
					}
				}
			}
			lookUpProperty = "Parent Stereo Category";
			lookUpString = getStringValueFromMappings(mol, lookUpProperty, mappings, results, recordNumber);
			if (lookUpString != null && lookUpString.length() > 0)
				parent.setStereoCategory(
						StereoCategory.findStereoCategorysByCodeEqualsIgnoreCase(lookUpString).getSingleResult());
			lookUpProperty = "Parent Annotation";
			lookUpString = getStringValueFromMappings(mol, lookUpProperty, mappings, results, recordNumber);
			if (lookUpString != null && lookUpString.length() > 0)
				parent.setParentAnnotation(
						ParentAnnotation.findParentAnnotationsByCodeEquals(lookUpString).getSingleResult());
			lookUpProperty = "Parent Compound Type";
			lookUpString = getStringValueFromMappings(mol, lookUpProperty, mappings, results, recordNumber);
			if (lookUpString != null && lookUpString.length() > 0)
				parent.setCompoundType(CompoundType.findCompoundTypesByCodeEquals(lookUpString).getSingleResult());
		} catch (Exception e) {
			logger.error("Caught error looking up parent property " + lookUpProperty + " with code " + lookUpString, e);
			throw new Exception("An error has occurred looking up parent property " + lookUpProperty + " with code "
					+ lookUpString);
		}

		// Set labelPrefix DTO if it is passed in
		if (labelPrefix != null)
			parent.setLabelPrefix(labelPrefix);

		return parent;
	}

	private String[] splitAndTrim(String lookUpString) {
		String[] aliases = Arrays.stream(lookUpString.split(";")) // Split on semicolon
				.map(String::trim) // trim whitespace
				.filter(Objects::nonNull) //remove nulls
				.filter(Predicate.not(String::isEmpty)) // remove empty strings
				.toArray(String[]::new);
		return aliases;
	}

	public Parent addParentAlias(Parent parent, String lsType, String lsKind,
			String lookUpProperty, String lookUpString) {

		if (lookUpString != null) {
			// Split on semicolon and trim
			String[] aliases = splitAndTrim(lookUpString);
			if (aliases.length > 0){
				logger.info("Found one or more parent alias: " + lookUpProperty + "  " + lookUpString);
			}
			if (parent.getParentAliases() == null) {
				logger.info("---------- the parent Alias set is null ----------------");
				parent.setParentAliases(new HashSet<ParentAlias>());
			}
			for (String alias : aliases) {
				ParentAlias parentAlias = new ParentAlias();
				parentAlias.setLsType(lsType);
				parentAlias.setLsKind(lsKind);
				parentAlias.setAliasName(alias);
				logger.info(parentAlias.toJson());
				parent.getParentAliases().add(parentAlias);
			}
		}

		String[] fields = { "parentAliases" };
		if (logger.isDebugEnabled())
			logger.debug("about to return the parent: " + parent.toJson(fields));
		return parent;
	}

	public Lot addLotAlias(Lot lot, String lsType, String lsKind,
			String lookUpProperty, String lookUpString) {

		if (lookUpString != null) {
			// Split on semicolon and trim
			String[] aliases = splitAndTrim(lookUpString);
			if (aliases.length > 0){
				logger.info("Found one or more lot alias: " + lookUpProperty + "  " + lookUpString);
			}
			if (lot.getLotAliases() == null) {
				logger.info("---------- the lot Alias set is null ----------------");
				lot.setLotAliases(new HashSet<LotAlias>());
			}
			for (String alias : aliases) {
				LotAlias lotAlias = new LotAlias();
				lotAlias.setLsType(lsType);
				lotAlias.setLsKind(lsKind);
				lotAlias.setAliasName(alias);
				logger.info(lotAlias.toJson());
				lot.getLotAliases().add(lotAlias);
			}
		}

		String[] fields = { "lotAliases" };
		if (logger.isDebugEnabled())
			logger.debug("about to return the lot: " + lot.toJson(fields));
		return lot;
	}

	public String getStringValueFromMappings(CmpdRegMolecule mol, String dbProperty,
			Collection<BulkLoadPropertyMappingDTO> mappings) {
		Collection<ValidationResponseDTO> results = new ArrayList();
		String value = getStringValueFromMappings(mol, dbProperty, mappings, results, -1);
		return (value);
	}

	public String getStringValueFromMappings(CmpdRegMolecule mol, String dbProperty,
			Collection<BulkLoadPropertyMappingDTO> mappings, Collection<ValidationResponseDTO> results,
			int recordNumber) {
		BulkLoadPropertyMappingDTO mapping = BulkLoadPropertyMappingDTO.findMappingByDbPropertyEquals(mappings,
				dbProperty);
		if (mapping == null)
			return null;
		String sdfProperty = mapping.getSdfProperty();
		String value = null;
		if (sdfProperty != null && sdfProperty.length() > 0)
			value = mol.getProperty(sdfProperty);
		String regexMatch = "(\\x00|\\^M|\\^\\@)$";
		if (value != null)
			value = value.replaceAll(regexMatch, "");
		if (value == null)
			value = mapping.getDefaultVal();
		String origValue = value;
		Boolean coerced = false;
		if (value != null) {
			String trimmedString = value.trim();
			if (!trimmedString.equals(value)) {
				value = trimmedString;
				coerced = true;
			}
			// Check if value is part of 'invalidValues' and a coerced change in case will yield one of the 'validValues'
			if (mapping.getInvalidValues() != null && mapping.getValidValues() != null){
				if (mapping.getInvalidValues().contains(value)){
					for (String validValue : mapping.getValidValues()) {
						if (validValue.toLowerCase().equals(value.toLowerCase())) {
							// Make the substitution
							value = validValue;
							coerced = true;
						}
					}
				}
			}
		}
		if (recordNumber != -1 && coerced)
			logWarning("TrimmedStringValue", "Trimmed string value for property " + sdfProperty,
					sdfProperty + " '" + origValue + "' trimmed to '" + value + "'", recordNumber,
					results);
		if (value != null)
			if (logger.isDebugEnabled())
				logger.debug("requested property: " + sdfProperty + "  value: " + value);
		return value;
	}

	public Collection<String> getStringValuesFromMappings(CmpdRegMolecule mol, String dbProperty,
			Collection<BulkLoadPropertyMappingDTO> mappings, Collection<ValidationResponseDTO> results,
			int recordNumber) {
		Collection<BulkLoadPropertyMappingDTO> foundMappings = BulkLoadPropertyMappingDTO
				.findMappingsByDbPropertyEquals(mappings, dbProperty);
		if (foundMappings == null || foundMappings.isEmpty())
			return null;
		String regexMatch = "(\\x00|\\^M|\\^\\@)$";
		Collection<String> stringValues = new ArrayList<String>();
		for (BulkLoadPropertyMappingDTO mapping : foundMappings) {
			String sdfProperty = mapping.getSdfProperty();
			String value = null;
			if (sdfProperty != null && sdfProperty.length() > 0)
				value = mol.getProperty(sdfProperty);
			if (value != null)
				value = value.replaceAll(regexMatch, "");
			if (value == null)
				value = mapping.getDefaultVal();

			// TRIM white space and log if the value was changed
			Boolean coerced = false;
			if (value != null) {
				String trimmedString = value.trim();
				if (!trimmedString.equals(value)) {
					value = trimmedString;
					coerced = true;
				}
			}
			if (recordNumber != -1 && coerced)
				logWarning("TrimmedStringValue", "Trimmed string value for property " + sdfProperty,
						sdfProperty + " '" + mol.getProperty(sdfProperty) + "' trimmed to '" + value + "'",
						recordNumber, results);
			stringValues.add(value);
		}
		return stringValues;
	}

	public Double getNumericValueFromMappings(CmpdRegMolecule mol, String dbProperty,
			Collection<BulkLoadPropertyMappingDTO> mappings, Collection<ValidationResponseDTO> results,
			int recordNumber) {
		BulkLoadPropertyMappingDTO mapping = BulkLoadPropertyMappingDTO.findMappingByDbPropertyEquals(mappings,
				dbProperty);
		if (mapping == null)
			return null;
		String sdfProperty = mapping.getSdfProperty();
		String stringVal = null;
		if (sdfProperty != null && sdfProperty.length() > 0)
			stringVal = mol.getProperty(sdfProperty);
		String regexMatch1 = "(\\x00|\\^M|\\^\\@)$";
		String regexMatch2 = "[^0-9.]+";
		Boolean coerced = false;
		if (stringVal != null) {
			Matcher matches1 = Pattern.compile(regexMatch1).matcher(stringVal);
			if (matches1.find()) {
				stringVal = matches1.replaceAll("");
				coerced = true;
			}
			Matcher matches2 = Pattern.compile(regexMatch2).matcher(stringVal);
			if (matches2.find()) {
				stringVal = matches2.replaceAll("");
				coerced = true;
			}
		}
		if (stringVal != null && stringVal.length() < 1)
			stringVal = null;
		Double value;
		if (stringVal == null && mapping.getDefaultVal() != null && mapping.getDefaultVal().length() > 0)
			value = new Double(mapping.getDefaultVal());
		else if (stringVal == null)
			value = null;
		else
			value = new Double(stringVal);
		if (coerced)
			logWarning("CoercedNumericValue", "Coerced numeric value for property " + sdfProperty,
					sdfProperty + " '" + mol.getProperty(sdfProperty) + "' coerced to '" + value + "'", recordNumber,
					results);
		if (value != null)
			if (logger.isDebugEnabled())
				logger.debug("requested property: " + sdfProperty + "  value: " + value);
		return value;
	}

	public Date getDateValueFromMappings(CmpdRegMolecule mol, String dbProperty,
			Collection<BulkLoadPropertyMappingDTO> mappings, Collection<ValidationResponseDTO> results,
			int recordNumber) throws Exception {
		BulkLoadPropertyMappingDTO mapping = BulkLoadPropertyMappingDTO.findMappingByDbPropertyEquals(mappings,
				dbProperty);
		if (mapping == null)
			return null;
		String sdfProperty = mapping.getSdfProperty();
		String stringVal = null;
		if (sdfProperty != null && sdfProperty.length() > 0)
			stringVal = mol.getProperty(sdfProperty);
		String regexMatch = "(\\x00|\\^M|\\^\\@)$";
		if (stringVal != null)
			stringVal = stringVal.replaceAll(regexMatch, "");
		Date value;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat df2 = new SimpleDateFormat("MM-dd-yyyy");
		DateFormat df3 = new SimpleDateFormat("yyyy/MM/dd");
		if (stringVal == null) {
			// no value in sdf, trying default value
			String defaultVal = mapping.getDefaultVal();
			if (defaultVal != null) {
				try {
					value = df.parse(mapping.getDefaultVal());
					if (!df.format(value).equals(mapping.getDefaultVal())) {
						logWarning("CoercedDateValue", "Coerced date value for property " + sdfProperty,
								sdfProperty + " '" + mapping.getDefaultVal() + " coerced to " + df.format(value) + "'",
								recordNumber, results);
					}
				} catch (ParseException e) {
					try {
						value = df2.parse(mapping.getDefaultVal());
						if (!df2.format(value).equals(mapping.getDefaultVal())) {
							logWarning("CoercedDateValue", "Coerced date value for property " + sdfProperty, sdfProperty
									+ " '" + mapping.getDefaultVal() + "' coerced to " + df2.format(value) + "'",
									recordNumber, results);
						}
					} catch (ParseException e2) {
						try {
							value = df3.parse(mapping.getDefaultVal());
							if (!df3.format(value).equals(mapping.getDefaultVal())) {
								logWarning("CoercedDateValue", "Coerced date value for property " + sdfProperty,
										sdfProperty + " '" + mapping.getDefaultVal() + "' coerced to "
												+ df3.format(value) + "'",
										recordNumber, results);
							}
						} catch (ParseException e3) {
							logger.error("Could not parse date:" + mapping.getDefaultVal());
							throw new Exception("Could not parse the date:" + mapping.getDefaultVal()
									+ ". Accepted date formats are YYYY-MM-DD or MM-DD-YYYY");
						}
					}
				}
			} else {
				value = null;
			}
		} else {
			// there is a value in sdf, try to parse it
			try {
				value = df.parse(stringVal);
				if (!df.format(value).equals(stringVal)) {
					logWarning("CoercedDateValue", "Coerced date value for property " + sdfProperty,
							sdfProperty + " '" + stringVal + "' coerced to '" + df.format(value) + "'", recordNumber,
							results);
				}
			} catch (ParseException e) {
				try {
					value = df2.parse(stringVal);
					if (!df2.format(value).equals(stringVal)) {
						logWarning("CoercedDateValue", "Coerced date value for property " + sdfProperty,
								sdfProperty + " '" + stringVal + "' coerced to '" + df2.format(value) + "'",
								recordNumber, results);
					}
				} catch (ParseException e2) {
					try {
						value = df3.parse(stringVal);
						if (!df3.format(value).equals(stringVal)) {
							logWarning("CoercedDateValue", "Coerced date value for property " + sdfProperty,
									sdfProperty + " '" + stringVal + "' coerced to '" + df3.format(value) + "'",
									recordNumber, results);
						}
					} catch (ParseException e3) {
						logger.error("Could not parse date:" + stringVal);
						throw new Exception("Could not parse the date:" + stringVal
								+ ". Accepted date formats are YYYY-MM-DD or MM-DD-YYYY");
					}
				}
			}

		}
		if (logger.isDebugEnabled())
			logger.debug("requested property: " + sdfProperty + "  value: " + value);
		return value;
	}

	private Map<String, Collection<String>> generateSummaryMap (int numRecordsRead,
	int numNewParentsLoaded, int numNewLotsOldParentsLoaded, Collection<ValidationResponseDTO> validationResponse) {
		// Parse the validationResponse to generate maps of errors and warnings
		int numErrorRecords = 0;
		int numWarningRecords = 0;
		Map<String, Integer> errorMap = new HashMap<String, Integer>();
		for (ValidationResponseDTO valResp : validationResponse) {
			if (valResp.getLevel() == "error") {
				if (!errorMap.containsKey(valResp.getCategoryDescription()))
					errorMap.put(valResp.getCategoryDescription(), 1);
				else
					errorMap.put(valResp.getCategoryDescription(), errorMap.get(valResp.getCategoryDescription()) + 1);
			}
		}
		for (Integer count : errorMap.values())
			numErrorRecords += count;

		Map<String, Integer> warningMap = new HashMap<String, Integer>();
		for (ValidationResponseDTO valResp : validationResponse) {
			if (valResp.getLevel() == "warning") {
				if (!warningMap.containsKey(valResp.getCategoryDescription()))
					warningMap.put(valResp.getCategoryDescription(), 1);
				else
					warningMap.put(valResp.getCategoryDescription(),
							warningMap.get(valResp.getCategoryDescription()) + 1);
			}
		}
		for (Integer count : warningMap.values())
			numWarningRecords += count;
		// create errors and warnings strings
		Collection<String> errors = new ArrayList<String>();
		Collection<String> warnings = new ArrayList<String>();
		for (String error : errorMap.keySet()) {
			errors.add(errorMap.get(error) + " entries had: " + error);
		}
		for (String warning : warningMap.keySet()) {
			warnings.add(warningMap.get(warning) + " entries had: " + warning);
		}
		// create general summary strings
		Collection<String> summaries = new ArrayList<String>();
		summaries.add("Number of entries processed: " + numRecordsRead);
		summaries.add("Number of entries with error: " + numErrorRecords);
		summaries.add("Number of warnings: " + numWarningRecords);
		summaries.add("New compounds: " + numNewParentsLoaded);
		summaries.add("New lots of existing compounds: " + numNewLotsOldParentsLoaded);
		summaries.add("New lots of new compounds in the file: " + numNewLotsOldParentsLoaded);
		// bundle the messages together to return a single object
		Map<String, Collection<String>> summaryMap = new HashMap<String, Collection<String>>();
		summaryMap.put("errors", errors);
		summaryMap.put("warnings", warnings);
		summaryMap.put("summaries", summaries);
		return summaryMap;
	}

	public String generateSummaryHtml(int numRecordsRead,
			int numNewParentsLoaded, int numNewLotsOldParentsLoaded,
			Collection<ValidationResponseDTO> validationResponse) {
		// Parse the validationResponse to generate maps of errors and warnings and summary messages
		Map<String, Collection<String>> summaryMap = generateSummaryMap(numRecordsRead,
		numNewParentsLoaded, numNewLotsOldParentsLoaded, validationResponse);
		// Build the summary HTML
		String summary = "<div><ul>";
		for (String summaryMessage : summaryMap.get("summaries")) {
			summary += "<li>" + summaryMessage + "</li>";
		}
		summary += "</ul></div>";
		Collection<String> errors = summaryMap.get("errors");
		if (errors.size() > 0) {
			summary += "<div><h5>Errors</h5><ul>";
			for (String errorMessage : errors) {
				summary += "<li>" + errorMessage + "</li>";
			}
			summary += "</ul></div>";
		}
		Collection<String> warnings = summaryMap.get("warnings");
		if (warnings.size() > 0) {
			summary += "<div><h5>Warnings</h5><ul>";
			for (String warningMessage : warnings) {
				summary += "<li>" + warningMessage + "</li>";
			}
			summary += "</ul></div>";
		}
		return summary;
	}

	public String generateSummaryText(int numRecordsRead,
			int numNewParentsLoaded, int numNewLotsOldParentsLoaded,
			Collection<ValidationResponseDTO> validationResponse) {
		// Parse the validationResponse to generate maps of errors and warnings and summary messages
		Map<String, Collection<String>> summaryMap = generateSummaryMap(numRecordsRead,
		numNewParentsLoaded, numNewLotsOldParentsLoaded, validationResponse);
		// Build the summary
		String summary = "";
		for (String summaryMessage : summaryMap.get("summaries")) {
			summary += summaryMessage + "\n";
		}
		Collection<String> errors = summaryMap.get("errors");
		if (errors.size() > 0) {
			summary += "Errors:\n";
			for (String errorMessage : errors) {
				summary += errorMessage + "\n";
			}
		}
		Collection<String> warnings = summaryMap.get("warnings");
		if (warnings.size() > 0) {
			summary += "Warnings:\n";
			for (String warningMessage : warnings) {
				summary += warningMessage + "\n";
			}
		}
		return summary;
	}

	public Collection<BulkLoadParentSaltFormLotDTO> getBulkLoadDTOsByBulkLoadFileId(Long bulkLoadFileId) {
		// This query pulls back the corp name and bulk load file ids for all parents, saltforms, and lots that are at all linked
		// to the bulkLoadFileId in question. 
		EntityManager em = Parent.entityManager();
		TypedQuery<BulkLoadParentSaltFormLotDTO> q = em.createQuery("SELECT new com.labsynch.labseer.dto.BulkLoadParentSaltFormLotDTO("
		+ "p.corpName, p.bulkLoadFile.id, sf.corpName, sf.bulkLoadFile.id, l.corpName, l.bulkLoadFile.id) "
		+ "FROM Lot l JOIN l.saltForm sf JOIN sf.parent p "
		+ "WHERE l.bulkLoadFile.id = :bulkLoadFileId OR sf.bulkLoadFile.id = :bulkLoadFileId OR p.bulkLoadFile.id = :bulkLoadFileId",
		BulkLoadParentSaltFormLotDTO.class);
		q.setParameter("bulkLoadFileId", bulkLoadFileId);
		return q.getResultList();
	}

	public Map<String, HashSet<String>> addToHashMapAsList(String key, String value, Map<String, HashSet<String>> map){
		// Helper function to avoid repetition
		if (map.containsKey(key)){
			map.get(key).add(value);
		} else {
			HashSet<String> values = new HashSet<String>();
			values.add(value);
			map.put(key, values);
		}
		return map;
	}

	@Override
	public PurgeFileDependencyCheckResponseDTO checkPurgeFileDependencies(BulkLoadFile bulkLoadFile) {
		// acasDependencies is a map of corpName : set of ACAS experiments that depend on that corpName
		Map<String, HashSet<String>> acasDependencies = new HashMap<String, HashSet<String>>();
		// cmpdRegDependencies is a map of corpName : set of CReg BulkLoadFiles that depend on that corpName
		Map<String, HashSet<String>> cmpdRegDependencies = new HashMap<String, HashSet<String>>();
		// dependentSingleRegLots is a set of lot corpnames which came from single (non-bulk) registration and depend on parents or saltforms from this file
		HashSet<String> dependentSingleRegLots = new HashSet<String>();
		// These Sets are used to count the number of parents, saltForms, and lots directly linked to this bulkLoadFile
		Set<String> uniqueParentCorpNames = new HashSet<String>();
		Set<String> uniqueSaltFormCorpNames = new HashSet<String>();
		Set<String> uniqueLotCorpNames = new HashSet<String>();
		
		// Grab the id of the current bulkLoadFile being checked, since we'll reference it a lot below
		Long bulkLoadFileId = bulkLoadFile.getId();
		// collect all parent, saltform, and lot corpNames and bulkLoadFileIds tied to this file (directly or indirectly)
		Collection<BulkLoadParentSaltFormLotDTO> dtos = getBulkLoadDTOsByBulkLoadFileId(bulkLoadFileId);
		// collect all the unique bulkLoadFile ids, and later build a map of id to name
		Map<Long, String> bulkLoadIdsToNames = new HashMap<Long, String>();
		
		for (BulkLoadParentSaltFormLotDTO dto : dtos) {
			String parentCorpName = dto.getParentCorpName();
			String saltFormCorpName = dto.getSaltFormCorpName();
			String lotCorpName = dto.getLotCorpName();
			Long parentBulkLoadFileId = dto.getParentBulkLoadFileId();
			Long saltFormBulkLoadFileId = dto.getSaltFormBulkLoadFileId();
			Long lotBulkLoadFileId = dto.getLotBulkLoadFileId();
			// acasDependencies, unique corpName lists: collect all parent, saltform, and lot corpnames directly tied to the file
			if (bulkLoadFileId.equals(parentBulkLoadFileId)){
				acasDependencies.put(parentCorpName, new HashSet<String>());
				uniqueParentCorpNames.add(parentCorpName);
			}
			if (bulkLoadFileId.equals(saltFormBulkLoadFileId)){
				acasDependencies.put(saltFormCorpName, new HashSet<String>());
				uniqueSaltFormCorpNames.add(saltFormCorpName);
			}
			if (bulkLoadFileId.equals(lotBulkLoadFileId)){
				acasDependencies.put(lotCorpName, new HashSet<String>());
				uniqueLotCorpNames.add(lotCorpName);
			}
			// Find the names of any bulkLoadFiles we haven't yet seen
			// We're using bulkLoadIdsToNames as a cache to avoid repeat lookups
			List<Long> bulkLoadFileIds = Arrays.asList(parentBulkLoadFileId, saltFormBulkLoadFileId, lotBulkLoadFileId);
			for (Long id : bulkLoadFileIds){
				if (id != null && !bulkLoadIdsToNames.containsKey(id)){
					BulkLoadFile blf = BulkLoadFile.findBulkLoadFile(id);
					bulkLoadIdsToNames.put(id, blf.getFileName());
				}
			}
			// cmpdRegDependencies: collect all other bulkload files that are dependent on this one, meaning their saltforms or lots depend on this file's parents or saltforms
			if (bulkLoadFileId.equals(parentBulkLoadFileId)){
				// if the parent belongs to the current file, check if the saltform or lot are from a different file
				// if so, we collect their file names
				if (saltFormBulkLoadFileId != null && !saltFormBulkLoadFileId.equals(bulkLoadFileId)) {
					cmpdRegDependencies = addToHashMapAsList(parentCorpName, bulkLoadIdsToNames.get(saltFormBulkLoadFileId), cmpdRegDependencies);
				}
				if (lotBulkLoadFileId != null && !lotBulkLoadFileId.equals(bulkLoadFileId)) {
					cmpdRegDependencies = addToHashMapAsList(parentCorpName, bulkLoadIdsToNames.get(lotBulkLoadFileId), cmpdRegDependencies);
				}
			} else if (bulkLoadFileId.equals(saltFormBulkLoadFileId)) {
				// if the parent is from a different file, but the saltForm is from the current file, we check if the lot is from a different file
				if (lotBulkLoadFileId != null && !lotBulkLoadFileId.equals(bulkLoadFileId)) {
					cmpdRegDependencies = addToHashMapAsList(parentCorpName, bulkLoadIdsToNames.get(lotBulkLoadFileId), cmpdRegDependencies);
				}
			}
			// dependentSingleRegLots: collect any dependent lots that are from single registration (bulkLoadId = null)
			if (lotBulkLoadFileId == null) {
				dependentSingleRegLots.add(lotCorpName);
			}
		}
		// counts: Count up the parents, saltForms, and lots directly linked to the file (these are the ones that will be purged along with the file)
		// to avoid duplication, we build a unique list, then count afterwards
		int numberOfParents = uniqueParentCorpNames.size();
		int numberOfSaltForms = uniqueSaltFormCorpNames.size();
		int numberOfLots = uniqueLotCorpNames.size();

		if (logger.isDebugEnabled())
			logger.debug(cmpdRegDependencies.toString());
		// Check for all the vials in ACAS that reference lots being purged
		Integer numberOfDependentContainers = null;
		Collection<ContainerBatchCodeDTO> dependentContainers = null;
		if (!acasDependencies.isEmpty()) {
			try {
				dependentContainers = checkDependentACASContainers(acasDependencies.keySet());
				numberOfDependentContainers = dependentContainers.size();
			} catch (Exception e) {
				logger.error("Caught exception checking for ACAS dependencies.", e);
			}
		}
		// Then check for data dependencies in ACAS.
		if (!acasDependencies.isEmpty()) {
			// check dependencies differently if config to check by barcode is enabled
			if (propertiesUtilService.getCheckACASDependenciesByContainerCode()) {
				try {
					Map<String, HashSet<String>> acasContainerDependencies = new HashMap<String, HashSet<String>>();
					for (ContainerBatchCodeDTO container : dependentContainers) {
						acasContainerDependencies.put(container.getContainerCodeName(), new HashSet<String>());
					}
					acasContainerDependencies = checkACASDependencies(acasContainerDependencies);
					for (ContainerBatchCodeDTO containerBatchDTO : dependentContainers) {
						HashSet<String> currentDependencies = acasDependencies.get(containerBatchDTO.getBatchCode());
						currentDependencies
								.addAll(acasContainerDependencies.get(containerBatchDTO.getContainerCodeName()));
						acasDependencies.put(containerBatchDTO.getBatchCode(), currentDependencies);
					}
				} catch (Exception e) {
					logger.error("Caught exception checking for ACAS dependencies by barcode.", e);
				}
			} else {
				try {
					acasDependencies = checkACASDependencies(acasDependencies);
				} catch (Exception e) {
					logger.error("Caught exception checking for ACAS dependencies.", e);
				}
			}
		}
		if (logger.isDebugEnabled())
			logger.debug(acasDependencies.toString());

		HashSet<String> dependentFiles = new HashSet<String>();
		for (HashSet<String> dependentSet : cmpdRegDependencies.values()) {
			for (String dependent : dependentSet) {
				dependentFiles.add(dependent);
			}
		}
		HashSet<String> dependentExperiments = new HashSet<String>();
		for (HashSet<String> dependentSet : acasDependencies.values()) {
			for (String dependent : dependentSet) {
				dependentExperiments.add(dependent);
			}
		}

		if (!dependentFiles.isEmpty() || !dependentExperiments.isEmpty() || !dependentSingleRegLots.isEmpty()) {
			String summary = generateErrorCheckHtml(numberOfParents, numberOfSaltForms, numberOfLots, dependentFiles,
					dependentExperiments, dependentSingleRegLots, numberOfDependentContainers);
			return new PurgeFileDependencyCheckResponseDTO(summary, false);
		} else {
			String summary = generateSuccessfulCheckHtml(numberOfParents, numberOfSaltForms, numberOfLots,
					numberOfDependentContainers);
			return new PurgeFileDependencyCheckResponseDTO(summary, true);
		}
	}

	private Collection<ContainerBatchCodeDTO> checkDependentACASContainers(Set<String> batchCodes)
			throws MalformedURLException, IOException {
		List<String> batchCodeList = new ArrayList<String>();
		batchCodeList.addAll(batchCodes);
		Collection<ContainerBatchCodeDTO> responseDTOs = containerService.getContainerDTOsByBatchCodes(batchCodeList);
		return responseDTOs;

	}

	private boolean compareParentAliases(Parent parent, Parent foundParent){
		boolean equalAliases = true; // assumption is aliases are same unless proven to be false

		// Need to Create String Sets to Do 
		// "Is alias (str) of parent in aliases of foundParent (set of strs)?" (and vice versa) logic 

		// Note: This is not the most efficient / concise code to do this process
		// however due to readability and the assumingly small number of aliases 
		// parents will have this is fine 

		Set<String> parentAliasStrings = new HashSet<>();
		for(ParentAlias alias : parent.getParentAliases()){
			if (! alias.isIgnored()) {
				parentAliasStrings.add(alias.getAliasName());
			}
		}
		Set<String> foundParentAliasStrings = new HashSet<>(); 
		for(ParentAlias alias : foundParent.getParentAliases()){
			if (! alias.isIgnored()) {
			foundParentAliasStrings.add(alias.getAliasName());
			}
		}

		for(ParentAlias alias : parent.getParentAliases())
		{
			// Check alias in foundParentAlias (String Comparison)
			// if not found then equalAliases is false 
			if(! alias.isIgnored() && !foundParentAliasStrings.contains(alias.getAliasName())){
				equalAliases = false;
			}
		}

		if(equalAliases) 
		// Only need to do this if no problems found in previous loop; otherwise redundant 
		// to processs that occurs after
		{
			for(ParentAlias alias : foundParent.getParentAliases()) {
				// Check alias in parentAlias (String Comparison)
				// if not found then equal Aliases is false 
				if(! alias.isIgnored() && !parentAliasStrings.contains(alias.getAliasName())){
					equalAliases = false; 
				}
			}
		}	

		return equalAliases;
	}

	private Parent updateParentAliases(Parent parent, Parent foundParent, String chemist) {

		// Need to Create String Sets to Do 
		// "Is alias (str) of parent in aliases of foundParent (set of strs)?" (and vice versa) logic 

		// Note: This is not the most efficient / concise code to do this process
		// however due to readability and the assumingly small number of aliases 
		// parents will have this is fine 
		HashMap<String, ParentAlias> newAliases = new HashMap<String, ParentAlias>();
		for(ParentAlias alias : parent.getParentAliases()){
			if (! alias.isIgnored()) {
				newAliases.put(alias.getAliasName(), alias);
			}
		}
		HashMap<String, ParentAlias> existingParentAliases = new HashMap<String, ParentAlias>();
		for(ParentAlias alias : foundParent.getParentAliases()){
			if (! alias.isIgnored()) {
				existingParentAliases.put(alias.getAliasName(), alias);
			}
		}
		// Create a set to hold the union of all unique aliases
		// Start with the existing aliases
		Set<ParentAlias> unionParentAliases = foundParent.getParentAliases();
		// Find any new aliases not already in the old set, and add them
		Set<String> newAliasStrings = newAliases.keySet().stream().filter(e -> 
			!existingParentAliases.keySet().contains(e)).collect(Collectors.toSet());
		for(String newAliasString : newAliasStrings) {
			unionParentAliases.add(newAliases.get(newAliasString));
		}
		// Parent Would Be Found Parent w/ Appropriate Updates
		parent = foundParent;
		// Update Parent Object to Have "Union" of All Aliases 
		parent.setParentAliases(unionParentAliases);
		// If Alias List Updated Then Updated Modified By and Modified Date 
		parent.setModifiedDate(new Date());
		parent.setModifiedBy(chemist);

		return parent;
	}

	public String generateSuccessfulCheckHtml(int numberOfParents,
			int numberOfSaltForms, int numberOfLots, Integer numberOfDependentContainers) {
		String summary = "<div>Are you sure you want to purge this file?</div>";
		summary += "<div style=\"margin-top:15px;\">";
		summary += "<div>" + numberOfParents + " parent compounds will be deleted." + "</div>";
		summary += "<div>" + numberOfLots + " compound lots will be deleted." + "</div>";
		if (numberOfDependentContainers != null && numberOfDependentContainers > 0)
			summary += "<div>" + numberOfDependentContainers + " containers referencing these lots will be deleted."
					+ "</div>";
		// summary+="<div style=\"margin-top:15px;margin-bottom:10px;\">";
		// summary+=""+numErrorRecords+" errors have been written to: "+errorSDFName+"";
		// summary+="</div>";
		summary += "</div>";

		return summary;
	}

	public String generateErrorCheckHtml(int numberOfParents,
			int numberOfSaltForms, int numberOfLots,
			HashSet<String> dependentFiles,
			HashSet<String> dependentExperiments, HashSet<String> dependentSingleRegLots,
			Integer numberOfDependentContainers) {
		String summary = "<div>File cannot be purged.</div>";
		summary += "<div style=\"margin-top:15px;\">";
		summary += "<div>" + numberOfParents + " parent compounds were referenced." + "</div>";
		summary += "<div>" + numberOfLots + " compound lots were referenced." + "</div>";
		if (numberOfDependentContainers != null && numberOfDependentContainers > 0)
			summary += "<div>" + numberOfDependentContainers + " containers reference these lots." + "</div>";

		boolean hasCmpdRegDependencies = !dependentFiles.isEmpty();
		boolean hasAcasDependencies = !dependentExperiments.isEmpty();
		boolean hasSingleRegDependencies = !dependentSingleRegLots.isEmpty();
		if (hasCmpdRegDependencies || hasAcasDependencies || hasSingleRegDependencies) {
			summary += "<br>";
			summary += "<div>If you still want to purge this file, the following must first be purged or deleted:</div>";
			summary += "<br>";
			if (hasCmpdRegDependencies) {
				summary += "<div>Bulk loaded files:</div>";
				summary += "<div style=\"margin-left:15px;\">";
				for (String dependent : dependentFiles) {
					summary += "<div>" + dependent + "</div>";
				}
				summary += "</div>";
				summary += "<br>";
			}
			if (hasAcasDependencies) {
				summary += "<div>ACAS experiments:</div>";
				summary += "<div style=\"margin-left:15px;\">";
				for (String dependent : dependentExperiments) {
					summary += "<div>" + dependent + "</div>";
				}
				summary += "</div>";
				summary += "<br>";
			}
			if (hasSingleRegDependencies) {
				summary += "<div>Manually registered lots:</div>";
				summary += "<div style=\"margin-left:15px;\">";
				for (String dependent : dependentSingleRegLots) {
					summary += "<div>" + dependent + "</div>";
				}
				summary += "</div>";
				summary += "<br>";
			}
			summary += "</div>";
		}

		return summary;
	}

	public Map<String, HashSet<String>> checkACASDependencies(
			Map<String, HashSet<String>> acasDependencies) throws MalformedURLException, IOException {
		CmpdRegBatchCodeDTO batchDTO = new CmpdRegBatchCodeDTO(acasDependencies.keySet());
		batchDTO.checkForDependentData();
		if (logger.isDebugEnabled())
			if (logger.isDebugEnabled())
				logger.debug(batchDTO.toJson());
		if (batchDTO.getLinkedDataExists()) {
			logger.info("Found experimental data in ACAS for some compounds.");
			for (CodeTableDTO experimentCodeTable : batchDTO.getLinkedExperiments()) {
				String experimentCodeAndName = experimentCodeTable.getCode() + " ( " + experimentCodeTable.getName()
						+ " )";
				if (acasDependencies.containsKey(experimentCodeTable.getComments())) {
					acasDependencies.get(experimentCodeTable.getComments()).add(experimentCodeAndName);
				} else {
					HashSet<String> codes = new HashSet<String>();
					codes.add(experimentCodeAndName);
					acasDependencies.put(experimentCodeTable.getComments(), codes);
				}
			}
		}
		return acasDependencies;
	}

	@Override
	@Transactional
	public PurgeFileResponseDTO purgeFile(BulkLoadFile bulkLoadFile) throws MalformedURLException, IOException {
		int numLots = 0;
		int numSaltForms = 0;
		int numParents = 0;
		int numContainers = 0;
		String fileName = bulkLoadFile.getFileName();
		String originalFileName = bulkLoadFile.getOriginalFileName();
		Collection<Lot> lots = Lot.findLotsByBulkLoadFileEquals(bulkLoadFile).getResultList();
		Set<String> lotCorpNames = new HashSet<String>();
		for (Lot lot : lots) {
			lotCorpNames.add(lot.getCorpName());
		}
		if (!lotCorpNames.isEmpty()) {
			Collection<ContainerBatchCodeDTO> containerBatchCodeDTOs = checkDependentACASContainers(lotCorpNames);
			numContainers = containerBatchCodeDTOs.size();
			Collection<String> containerCodeNames = new ArrayList<String>();
			for (ContainerBatchCodeDTO dto : containerBatchCodeDTOs) {
				containerCodeNames.add(dto.getContainerCodeName());
			}
			if (!containerCodeNames.isEmpty()) {
				String url = propertiesUtilService.getAcasURL() + "containers/deleteArrayByCodeNames";
				String json = (new JSONSerializer()).serialize(containerCodeNames);
				SimpleUtil.postRequestToExternalServer(url, json, logger);
			}
		}

		numLots = lots.size();
		for (Lot lot : lots) {
			Collection<FileList> fileLists = lot.getFileLists();
			fileLists.addAll(FileList.findFileListsByLot(lot).getResultList());
			if (fileLists != null) {
				for (FileList fileList : fileLists) {
					fileList.remove();
				}
			}
			Set<LotAlias> lotAliases = lot.getLotAliases();
			if (lotAliases != null) {
				for (LotAlias lotAlias : lotAliases) {
					lotAlias.remove();
				}
			}
			lot.remove();
		}
		Collection<SaltForm> saltForms = SaltForm.findSaltFormsByBulkLoadFileEquals(bulkLoadFile).getResultList();
		numSaltForms = saltForms.size();
		for (SaltForm saltForm : saltForms) {
			Collection<IsoSalt> isoSalts = saltForm.getIsoSalts();
			isoSalts.addAll(IsoSalt.findIsoSaltsBySaltForm(saltForm).getResultList());
			if (isoSalts != null) {
				for (IsoSalt isoSalt : isoSalts) {
					isoSalt.remove();
					// isoSalt.flush();
				}
			}
			chemStructureService.deleteStructure(StructureType.SALT_FORM, saltForm.getCdId());
			saltForm.remove();
		}
		Collection<Parent> parents = Parent.findParentsByBulkLoadFileEquals(bulkLoadFile).getResultList();
		numParents = parents.size();
		for (Parent parent : parents) {

			// Delete any StandardizationDryRunCompounds that are associated with the parent
			for(StandardizationDryRunCompound s : StandardizationDryRunCompound.findStandardizationDryRunCompoundsByParent(parent)) {
				s.remove();
			}

			chemStructureService.deleteStructure(StructureType.PARENT, parent.getCdId());
			for (ParentAlias alias : parent.getParentAliases()) {
				alias.remove();
			}
			parent.remove();
		}
		bulkLoadFile.remove();

		String summary = generateSuccessfulPurgeHtml(originalFileName, numParents, numSaltForms, numLots, numContainers);
		return new PurgeFileResponseDTO(summary, true, fileName, originalFileName);
	}

	public String generateSuccessfulPurgeHtml(String fileName, int numberOfParents,
			int numberOfSaltForms, int numberOfLots, int numContainers) {
		String summary = "<div>Successfully purged file: " + fileName + "</div>";
		summary += "<div style=\"margin-top:15px;\">";
		summary += "<div>" + numberOfParents + " parent compounds were deleted." + "</div>";
		summary += "<div>" + numberOfLots + " compound lots were deleted." + "</div>";
		summary += "<div>" + numContainers + " containers were deleted." + "</div>";
		// summary+="<div style=\"margin-top:15px;margin-bottom:10px;\">";
		// summary+=""+numErrorRecords+" errors have been written to: "+errorSDFName+"";
		// summary+="</div>";
		summary += "</div>";

		return summary;
	}

	public CmpdRegMolecule processForSaltStripping(CmpdRegMolecule inputMol, Boolean multipleFragments,
			Collection<BulkLoadPropertyMappingDTO> mappings, Collection<ValidationResponseDTO> results,
			int recordNumber) throws CmpdRegMolFormatException {
		boolean markedAsMixture = Boolean
				.valueOf(getStringValueFromMappings(inputMol, "Parent Is Mixture", mappings, results, recordNumber));
		if (multipleFragments && !markedAsMixture) {
			// attempt salt stripping
			StrippedSaltDTO strippedSaltDTO = chemStructureService.stripSalts(inputMol);
			if (!strippedSaltDTO.getSaltCounts().isEmpty() && strippedSaltDTO.getUnidentifiedFragments().size() == 1) {
				logger.debug("Successful salt stripping! " + strippedSaltDTO.getSaltCounts().size()
						+ " distinct salts found. No unidentified fragments.");
				inputMol = inputMol.replaceStructure(
						strippedSaltDTO.getUnidentifiedFragments().iterator().next().getMolStructure());
				logger.debug("Cleaned parent structure (only fragment not identified as salt)");
				logger.debug(inputMol.getMolStructure());
				String saltAbbrevString = "";
				String saltEquivString = "";
				String saltAbbrevMapping = getStringValueFromMappings(inputMol, "Lot Salt Abbrev", mappings, results,
						recordNumber);
				if (saltAbbrevMapping != null && saltAbbrevMapping.length() > 0) {
					// Salt(s) has been found in structure and property mapping => not allowed 
					CmpdRegMolFormatException saltException = new CmpdRegMolFormatException("Salts found in both structure and SDF Property");
					throw saltException;
				} 
				if (saltAbbrevMapping != null) {
					saltAbbrevString += saltAbbrevMapping;
					saltAbbrevString += ";";
				}
				String saltEquivMapping = getStringValueFromMappings(inputMol, "Lot Salt Equivalents", mappings,
						results, recordNumber);
				if (saltEquivMapping != null) {
					saltEquivString += saltEquivMapping;
					saltEquivString += ";";
				}
				for (Salt salt : strippedSaltDTO.getSaltCounts().keySet()) {
					if (saltAbbrevString.length() > 0)
						saltAbbrevString += ";";
					saltAbbrevString += salt.getAbbrev();
					if (saltEquivString.length() > 0)
						saltEquivString += ";";
					saltEquivString += strippedSaltDTO.getSaltCounts().get(salt);
				}
				String dbProperty = "Lot Salt Abbrev";
				String sdfProperty = dbProperty;
				if (BulkLoadPropertyMappingDTO.findMappingByDbPropertyEquals(mappings, dbProperty) == null) {
					// no mapping found - add a new one with dbProperty = sdfProperty
					BulkLoadPropertyMappingDTO newMapping = new BulkLoadPropertyMappingDTO(dbProperty, sdfProperty,
							false, null, null, null, false);
					mappings.add(newMapping);
				} else {
					sdfProperty = BulkLoadPropertyMappingDTO.findMappingByDbPropertyEquals(mappings, dbProperty)
						.getSdfProperty();
				}
				inputMol.setProperty(dbProperty, saltAbbrevString);

				dbProperty = "Lot Salt Equivalents";
				sdfProperty = dbProperty;
				if (BulkLoadPropertyMappingDTO.findMappingByDbPropertyEquals(mappings, dbProperty) == null) {
					// no mapping found - add a new one with dbProperty = sdfProperty
					BulkLoadPropertyMappingDTO newMapping = new BulkLoadPropertyMappingDTO(dbProperty, sdfProperty,
							false, null, null, null, false);
					mappings.add(newMapping);
				} else {
					sdfProperty = BulkLoadPropertyMappingDTO.findMappingByDbPropertyEquals(mappings, dbProperty).getSdfProperty();
				}
				inputMol.setProperty(dbProperty, saltEquivString);
				return inputMol;
			} else if (!strippedSaltDTO.getUnidentifiedFragments().isEmpty()) {
				logger.warn("Attempted salt stripping failed - could not identify "
						+ strippedSaltDTO.getUnidentifiedFragments().size() + " fragments.");
				for (CmpdRegMolecule unidentifiedFragment : strippedSaltDTO.getUnidentifiedFragments()) {
					logger.warn(unidentifiedFragment.getMolStructure());
				}
				return inputMol;
			} else {
				return inputMol;
			}
		} else {
			return inputMol;
		}
	}

}
