package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.AnalysisGroupLabel;
import com.labsynch.labseer.domain.AnalysisGroupState;
import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ItxSubjectContainer;
import com.labsynch.labseer.domain.ItxSubjectContainerState;
import com.labsynch.labseer.domain.ItxSubjectContainerValue;
import com.labsynch.labseer.domain.LsTag;
import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.SubjectLabel;
import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.domain.TreatmentGroupLabel;
import com.labsynch.labseer.domain.TreatmentGroupState;
import com.labsynch.labseer.domain.TreatmentGroupValue;
import com.labsynch.labseer.dto.AnalysisGroupValueDTO;
import com.labsynch.labseer.dto.BatchCodeDTO;
import com.labsynch.labseer.dto.ExperimentFilterDTO;
import com.labsynch.labseer.dto.ExperimentFilterSearchDTO;
import com.labsynch.labseer.dto.ExperimentSearchRequestDTO;
import com.labsynch.labseer.dto.JSTreeNodeDTO;
import com.labsynch.labseer.exceptions.ErrorMessage;
import com.labsynch.labseer.exceptions.UniqueExperimentNameException;
import com.labsynch.labseer.service.ExperimentService;
import com.labsynch.labseer.utils.PropertiesUtilService;
import flexjson.JSONDeserializer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

@Controller
@RequestMapping("/experiments")
@RooWebScaffold(path = "experiments", formBackingObject = Experiment.class)
@RooWebFinder
@Transactional
@RooWebJson(jsonObject = Experiment.class)
public class ExperimentController {

    private static final Logger logger = LoggerFactory.getLogger(ExperimentController.class);

    @Autowired
    private ExperimentService experimentService;

    @Autowired
    private PropertiesUtilService propertiesUtilService;

    @Transactional
    @RequestMapping(value = "/agdata/batchcodelist", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getGeneCodeData(@RequestBody String json, @RequestParam(value = "format", required = false) String format) {
        logger.debug("incoming json: " + json);
        ExperimentSearchRequestDTO searchRequest = ExperimentSearchRequestDTO.fromJsonToExperimentSearchRequestDTO(json);
        List<AnalysisGroupValueDTO> agValues = null;
        try {
            agValues = AnalysisGroupValue.findAnalysisGroupValueDTO(searchRequest.getBatchCodeList()).getResultList();
        } catch (Exception e) {
            logger.error(e.toString());
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (agValues == null || agValues.size() == 0) {
            return new ResponseEntity<String>("[]", headers, HttpStatus.EXPECTATION_FAILED);
        }
        if (format != null && format.equalsIgnoreCase("csv")) {
            StringWriter outFile = new StringWriter();
            ICsvBeanWriter beanWriter = null;
            try {
                beanWriter = new CsvBeanWriter(outFile, CsvPreference.STANDARD_PREFERENCE);
                final String[] header = AnalysisGroupValueDTO.getColumns();
                final CellProcessor[] processors = AnalysisGroupValueDTO.getProcessors();
                beanWriter.writeHeader(header);
                for (final AnalysisGroupValueDTO agValue : agValues) {
                    beanWriter.write(agValue, header, processors);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (beanWriter != null) {
                    try {
                        beanWriter.close();
                        outFile.flush();
                        outFile.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return new ResponseEntity<String>(outFile.toString(), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(AnalysisGroupValueDTO.toJsonArray(agValues), headers, HttpStatus.OK);
        }
    }

    @Transactional
    @RequestMapping(value = "/agdata/batchcodelist/experimentcodelist", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getAGDataByBatchAndExperiment(@RequestBody String json, @RequestParam(value = "format", required = false) String format) {
        logger.debug("incoming json: " + json);
        ExperimentSearchRequestDTO searchRequest = ExperimentSearchRequestDTO.fromJsonToExperimentSearchRequestDTO(json);
        logger.debug("converted json: " + searchRequest.toJson());
        List<AnalysisGroupValueDTO> agValues = null;
        try {
            agValues = experimentService.getFilteredAGData(searchRequest);
            logger.debug("number of agvalues found: " + agValues.size());
        } catch (Exception e) {
            logger.error(e.toString());
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (agValues == null || agValues.size() == 0) {
            return new ResponseEntity<String>("[]", headers, HttpStatus.EXPECTATION_FAILED);
        }
        if (format != null && format.equalsIgnoreCase("csv")) {
            StringWriter outFile = new StringWriter();
            ICsvBeanWriter beanWriter = null;
            try {
                beanWriter = new CsvBeanWriter(outFile, CsvPreference.STANDARD_PREFERENCE);
                final String[] header = AnalysisGroupValueDTO.getColumns();
                final CellProcessor[] processors = AnalysisGroupValueDTO.getProcessors();
                beanWriter.writeHeader(header);
                for (final AnalysisGroupValueDTO agValue : agValues) {
                    beanWriter.write(agValue, header, processors);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (beanWriter != null) {
                    try {
                        beanWriter.close();
                        outFile.flush();
                        outFile.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return new ResponseEntity<String>(outFile.toString(), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(AnalysisGroupValueDTO.toJsonArray(agValues), headers, HttpStatus.OK);
        }
    }

    @SuppressWarnings("unchecked")
    @Transactional
    @RequestMapping(value = "/agdata/batchcodelist/experimentcodelist/searchfilters/batchcodeArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getBatchCodesByBatchAndExperimentAndFilters(@RequestBody String json, @RequestParam(value = "format", required = false) String format) {
        logger.debug("incoming json: " + json);
        ExperimentSearchRequestDTO searchRequest = ExperimentSearchRequestDTO.fromJsonToExperimentSearchRequestDTO(json);
        Set<String> uniqueBatchCodes = new HashSet<String>();
        List<String> secondBatchCodes = null;
        Collection<String> collectionOfCodes = null;
        try {
            boolean firstPass = true;
            for (ExperimentFilterSearchDTO singleSearchFilter : searchRequest.getSearchFilters()) {
                if (firstPass) {
                    collectionOfCodes = AnalysisGroupValue.findBatchCodeBySearchFilter(searchRequest.getBatchCodeList(), searchRequest.getExperimentCodeList(), singleSearchFilter).getResultList();
                    logger.info("size of firstBatchCodes: " + collectionOfCodes.size());
                    firstPass = false;
                } else {
                    secondBatchCodes = AnalysisGroupValue.findBatchCodeBySearchFilter(searchRequest.getBatchCodeList(), searchRequest.getExperimentCodeList(), singleSearchFilter).getResultList();
                    logger.info("size of firstBatchCodes: " + collectionOfCodes.size());
                    logger.info("size of secondBatchCodes: " + secondBatchCodes.size());
                    if (searchRequest.getBooleanFilter().equalsIgnoreCase("AND")) {
                        collectionOfCodes = CollectionUtils.intersection(collectionOfCodes, secondBatchCodes);
                    } else if (searchRequest.getBooleanFilter().equalsIgnoreCase("NOT")) {
                        collectionOfCodes = CollectionUtils.subtract(collectionOfCodes, secondBatchCodes);
                    } else if (searchRequest.getBooleanFilter().equalsIgnoreCase("OR")) {
                        collectionOfCodes = CollectionUtils.union(collectionOfCodes, secondBatchCodes);
                    } else {
                        logger.error("boolean filter is neither AND, OR, NOT");
                        collectionOfCodes = CollectionUtils.intersection(collectionOfCodes, secondBatchCodes);
                    }
                    logger.info("size of intersectCodes: " + collectionOfCodes.size());
                }
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
        uniqueBatchCodes.addAll(collectionOfCodes);
        List<BatchCodeDTO> batchCodes = new ArrayList<BatchCodeDTO>();
        for (String codeValue : uniqueBatchCodes) {
            BatchCodeDTO bc = new BatchCodeDTO();
            bc.setBatchCode(codeValue);
            batchCodes.add(bc);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (batchCodes == null || batchCodes.size() == 0) {
            return new ResponseEntity<String>("[]", headers, HttpStatus.EXPECTATION_FAILED);
        }
        if (format != null && format.equalsIgnoreCase("csv")) {
            StringWriter outFile = new StringWriter();
            ICsvBeanWriter beanWriter = null;
            try {
                beanWriter = new CsvBeanWriter(outFile, CsvPreference.STANDARD_PREFERENCE);
                final String[] header = BatchCodeDTO.getColumns();
                final CellProcessor[] processors = BatchCodeDTO.getProcessors();
                beanWriter.writeHeader(header);
                for (final BatchCodeDTO batchCode : batchCodes) {
                    beanWriter.write(batchCode, header, processors);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (beanWriter != null) {
                    try {
                        beanWriter.close();
                        outFile.flush();
                        outFile.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return new ResponseEntity<String>(outFile.toString(), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(BatchCodeDTO.toJsonArray(batchCodes), headers, HttpStatus.OK);
        }
    }

    @Transactional
    @RequestMapping(value = "/agdata/batchcodelist/experimentcodelist/searchfilters", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getAgDataByBatchAndExperimentAndFilters(@RequestBody String json, @RequestParam(value = "format", required = false) String format) {
        logger.debug("incoming json: " + json);
        ExperimentSearchRequestDTO searchRequest = ExperimentSearchRequestDTO.fromJsonToExperimentSearchRequestDTO(json);
        List<String> agValues = null;
        try {
            agValues = AnalysisGroupValue.findBatchCodeBySearchFilters(searchRequest.getBatchCodeList(), searchRequest.getExperimentCodeList(), searchRequest.getSearchFilters()).getResultList();
        } catch (Exception e) {
            logger.error(e.toString());
        }
        List<BatchCodeDTO> batchCodes = new ArrayList<BatchCodeDTO>();
        for (String agValue : agValues) {
            BatchCodeDTO bc = new BatchCodeDTO();
            bc.setBatchCode(agValue);
            batchCodes.add(bc);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (batchCodes == null || batchCodes.size() == 0) {
            return new ResponseEntity<String>("[]", headers, HttpStatus.EXPECTATION_FAILED);
        }
        if (format != null && format.equalsIgnoreCase("csv")) {
            StringWriter outFile = new StringWriter();
            ICsvBeanWriter beanWriter = null;
            try {
                beanWriter = new CsvBeanWriter(outFile, CsvPreference.STANDARD_PREFERENCE);
                final String[] header = BatchCodeDTO.getColumns();
                final CellProcessor[] processors = BatchCodeDTO.getProcessors();
                beanWriter.writeHeader(header);
                for (final BatchCodeDTO batchCode : batchCodes) {
                    beanWriter.write(batchCode, header, processors);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (beanWriter != null) {
                    try {
                        beanWriter.close();
                        outFile.flush();
                        outFile.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return new ResponseEntity<String>(outFile.toString(), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(BatchCodeDTO.toJsonArray(batchCodes), headers, HttpStatus.OK);
        }
    }

    @Transactional
    @RequestMapping(value = "/filters/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getExperimentFilters(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<String> experimentCodes = new JSONDeserializer<List<String>>().use(null, ArrayList.class).use("values", String.class).deserialize(json);
        Collection<ExperimentFilterDTO> results = experimentService.getExperimentFilters(experimentCodes);
        return new ResponseEntity<String>(ExperimentFilterDTO.toJsonArray(results), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/jstreenodes/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getJsTreeNodes(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<BatchCodeDTO> batchCodes = BatchCodeDTO.fromJsonArrayToBatchCoes(json);
        Collection<String> codeValues = new HashSet<String>();
        for (BatchCodeDTO batchCode : batchCodes) {
            codeValues.add(batchCode.getBatchCode());
        }
        Collection<JSTreeNodeDTO> results = experimentService.getExperimentNodes(codeValues);
        return new ResponseEntity<String>(JSTreeNodeDTO.toJsonArray(results), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/full/{id}", headers = "Accept=application/json")
    @ResponseBody
    @Transactional
    public ResponseEntity<java.lang.String> showFullJson(@PathVariable("id") Long id) {
        Experiment experiment = Experiment.findExperiment(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (experiment == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(experiment.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/stub/{id}", headers = "Accept=application/json")
    @ResponseBody
    @Transactional
    public ResponseEntity<java.lang.String> showJsonStub(@PathVariable("id") Long id, @RequestParam(value = "with", required = false) String with) {
        Experiment experiment = Experiment.findExperiment(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (experiment == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        if (with.equalsIgnoreCase("prettyjson")) {
            return new ResponseEntity<String>(experiment.toPrettyJsonStub(), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(experiment.toJsonStub(), headers, HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    @Transactional
    public ResponseEntity<java.lang.String> showJsonStubWith(@PathVariable("id") Long id, @RequestParam(value = "with", required = false) String with) {
        Experiment experiment = Experiment.findExperiment(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (experiment == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        if (with != null) {
            if (with.equalsIgnoreCase("analysisgroups")) {
                return new ResponseEntity<String>(experiment.toJsonStubWithAnalysisGroups(), headers, HttpStatus.OK);
            } else if (with.equalsIgnoreCase("analysisgroupstates")) {
                return new ResponseEntity<String>(experiment.toJsonStubWithAnalysisGroupStates(), headers, HttpStatus.OK);
            } else if (with.equalsIgnoreCase("analysisgroupvalues")) {
                return new ResponseEntity<String>(experiment.toJsonStubWithAnalysisGroups(), headers, HttpStatus.OK);
            } else if (with.equalsIgnoreCase("fullobject")) {
                return new ResponseEntity<String>(experiment.toJson(), headers, HttpStatus.OK);
            } else if (with.equalsIgnoreCase("prettyjson")) {
                return new ResponseEntity<String>(experiment.toPrettyJson(), headers, HttpStatus.OK);
            } else if (with.equalsIgnoreCase("prettyjsonstub")) {
                return new ResponseEntity<String>(experiment.toPrettyJsonStub(), headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<String>("ERROR: with" + with + " route is not implemented. ", headers, HttpStatus.NOT_IMPLEMENTED);
            }
        } else {
            return new ResponseEntity<String>(experiment.toJsonStub(), headers, HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    @Transactional
    public ResponseEntity<java.lang.String> listJson(@RequestParam(value = "protocolKind", required = false) String protocolKind) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<Experiment> result = new ArrayList<Experiment>();
        if (protocolKind != null) {
            List<Protocol> protocols = Protocol.findProtocolsByLsKindEquals(protocolKind).getResultList();
            for (Protocol protocol : protocols) {
                List<Experiment> experiments = Experiment.findExperimentsByProtocol(protocol).getResultList();
                result.addAll(experiments);
            }
        } else {
            result.addAll(Experiment.findAllExperiments());
        }
        return new ResponseEntity<String>(Experiment.toJsonArrayStub(result), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/protocolKind/{protocolKind}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    @Transactional
    public ResponseEntity<java.lang.String> listJsonByProtocolKind(@PathVariable("protocolKind") String protocolKind) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<Experiment> result = new ArrayList<Experiment>();
        if (protocolKind != null) {
            List<Protocol> protocols = Protocol.findProtocolsByLsKindEquals(protocolKind).getResultList();
            for (Protocol protocol : protocols) {
                List<Experiment> experiments = Experiment.findExperimentsByProtocol(protocol).getResultList();
                result.addAll(experiments);
            }
        } else {
            result.addAll(Experiment.findAllExperiments());
        }
        return new ResponseEntity<String>(Experiment.toJsonArrayStub(result), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
        logger.debug("----from the Experiment POST controller----");
        logger.debug("incoming json " + json);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ArrayList<ErrorMessage> errors = new ArrayList<ErrorMessage>();
        boolean errorsFound = false;
        Experiment experiment = null;
        try {
            experiment = experimentService.saveLsExperiment(Experiment.fromJsonToExperiment(json));
        } catch (UniqueExperimentNameException e) {
            logger.error("----from the controller----" + e.getMessage().toString() + " whole message  " + e.toString());
            ErrorMessage error = new ErrorMessage();
            error.setErrorLevel("error");
            error.setMessage("not unique experiment name");
            errors.add(error);
            errorsFound = true;
        }
        if (errorsFound) {
            return new ResponseEntity<String>(ErrorMessage.toJsonArray(errors), headers, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<String>(experiment.toJson(), headers, HttpStatus.CREATED);
        }
    }

    @Transactional
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
        Collection<Experiment> savedExperiments = new ArrayList<Experiment>();
        int batchSize = propertiesUtilService.getBatchSize();
        int i = 0;
        StringReader sr = new StringReader(json);
        BufferedReader br = new BufferedReader(sr);
        for (Experiment experiment : Experiment.fromJsonArrayToExperiments(br)) {
            try {
                experiment = experimentService.saveLsExperiment(experiment);
            } catch (Exception e) {
                e.printStackTrace();
            }
            savedExperiments.add(Experiment.findExperiment(experiment.getId()));
            if (i % batchSize == 0) {
                experiment.flush();
                experiment.clear();
            }
            i++;
        }
        IOUtils.closeQuietly(sr);
        IOUtils.closeQuietly(br);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(Experiment.toJsonArrayStub(savedExperiments), headers, HttpStatus.CREATED);
    }

    @Transactional
    @RequestMapping(value = { "/{id}", "/" }, method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Experiment experiment = experimentService.updateExperiment(Experiment.fromJsonToExperiment(json));
        if (experiment.getId() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(Experiment.findExperiment(experiment.getId()).toJsonStub(), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<Experiment> experiments = Experiment.fromJsonArrayToExperiments(json);
        Collection<Experiment> updatedExperiments = new ArrayList<Experiment>();
        for (Experiment experiment : experiments) {
            updatedExperiments.add(experimentService.updateExperiment(experiment));
        }
        return new ResponseEntity<String>(Experiment.toJsonArrayStub(updatedExperiments), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id, @RequestParam(value = "with", required = false) String with) {
        Experiment experiment = Experiment.findExperiment(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (experiment == null) {
            logger.info("Did not find the experiment before delete");
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        if (with != null && with.equalsIgnoreCase("analysisgroups")) {
            logger.info("deleting analysis groups within an experiment: " + id);
            ItxSubjectContainerValue.deleteByExperimentID(id);
            ItxSubjectContainerState.deleteByExperimentID(id);
            int deletedItxValues = ItxSubjectContainer.deleteByExperimentID(id);
            logger.debug("deleted number of ItxSubjectContainers: " + deletedItxValues);
            int deletedValues = SubjectValue.deleteByExperimentID(id);
            logger.debug("deleted number of subject values: " + deletedValues);
            int deletedLabels = SubjectLabel.deleteByExperimentID(id);
            logger.debug("deleted number of subject labels: " + deletedLabels);
            int numberOfStates = SubjectState.deleteByExperimentID(id);
            logger.debug("deleted number of numberOfStates: " + numberOfStates);
            int deletedSubjects = Subject.deleteByExperimentID(id);
            logger.debug("deleted number of subjects: " + deletedSubjects);
            int tt2 = TreatmentGroupValue.deleteByExperimentID(id);
            logger.debug("deleted number of TreatmentGroupValue: " + tt2);
            int tt1 = TreatmentGroupState.deleteByExperimentID(id);
            logger.debug("deleted number of TreatmentGroupState: " + tt1);
            int tt3 = TreatmentGroupLabel.deleteByExperimentID(id);
            logger.debug("deleted number of TreatmentGroupLabel: " + tt3);
            int tt = TreatmentGroup.deleteByExperimentID(id);
            logger.debug("deleted number of TreatmentGroups: " + tt);
            int ag1 = AnalysisGroupValue.deleteByExperimentID(id);
            int ag2 = AnalysisGroupState.deleteByExperimentID(id);
            int ag3 = AnalysisGroupLabel.deleteByExperimentID(id);
            int ag4 = AnalysisGroup.deleteByExperimentID(id);
            logger.info("deleted number of AnalysisGroupValue: " + ag1);
            logger.info("deleted number of AnalysisGroupState: " + ag2);
            logger.info("deleted number of AnalysisGroupLabel: " + ag3);
            logger.info("deleted number of AnalysisGroup: " + ag4);
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } else {
            logger.info("deleting entire experiment: " + id);
            ItxSubjectContainerValue.deleteByExperimentID(id);
            ItxSubjectContainerState.deleteByExperimentID(id);
            int deletedItxValues = ItxSubjectContainer.deleteByExperimentID(id);
            logger.debug("deleted number of ItxSubjectContainers: " + deletedItxValues);
            int deletedValues = SubjectValue.deleteByExperimentID(id);
            logger.debug("deleted number of subject values: " + deletedValues);
            int deletedLabels = SubjectLabel.deleteByExperimentID(id);
            logger.debug("deleted number of subject labels: " + deletedLabels);
            int numberOfStates = SubjectState.deleteByExperimentID(id);
            logger.debug("deleted number of numberOfStates: " + numberOfStates);
            int deletedSubjects = Subject.deleteByExperimentID(id);
            logger.debug("deleted number of subjects: " + deletedSubjects);
            int tt2 = TreatmentGroupValue.deleteByExperimentID(id);
            logger.debug("deleted number of TreatmentGroupValue: " + tt2);
            int tt1 = TreatmentGroupState.deleteByExperimentID(id);
            logger.debug("deleted number of TreatmentGroupState: " + tt1);
            int tt3 = TreatmentGroupLabel.deleteByExperimentID(id);
            logger.debug("deleted number of TreatmentGroupLabel: " + tt3);
            int tt = TreatmentGroup.deleteByExperimentID(id);
            logger.debug("deleted number of TreatmentGroups: " + tt);
            int ag1 = AnalysisGroupValue.deleteByExperimentID(id);
            int ag2 = AnalysisGroupState.deleteByExperimentID(id);
            int ag3 = AnalysisGroupLabel.deleteByExperimentID(id);
            int ag4 = AnalysisGroup.deleteByExperimentID(id);
            logger.info("deleted number of AnalysisGroupValue: " + ag1);
            logger.info("deleted number of AnalysisGroupState: " + ag2);
            logger.info("deleted number of AnalysisGroupLabel: " + ag3);
            logger.info("deleted number of AnalysisGroup: " + ag4);
            experiment.remove();
            if (Experiment.findExperiment(id) == null) {
                logger.info("Did not find the experiment after delete");
                return new ResponseEntity<String>(headers, HttpStatus.OK);
            } else {
                logger.info("Found the experiment after delete");
                return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Transactional
    @RequestMapping(value = "/codename/{codeName}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindExperimentsByCodeNameEqualsRoute(@PathVariable("codeName") String codeName, @RequestParam(value = "with", required = false) String with) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<Experiment> experiments = Experiment.findExperimentsByCodeNameEquals(codeName).getResultList();
        if (with != null) {
            if (with.equalsIgnoreCase("analysisgroups")) {
                return new ResponseEntity<String>(Experiment.toJsonArrayStubWithAG(experiments), headers, HttpStatus.OK);
            } else if (with.equalsIgnoreCase("analysisgroupstates")) {
                return new ResponseEntity<String>(Experiment.toJsonArrayStubWithAGStates(experiments), headers, HttpStatus.OK);
            } else if (with.equalsIgnoreCase("analysisgroupvalues")) {
                return new ResponseEntity<String>(Experiment.toJsonArrayStubWithAGValues(experiments), headers, HttpStatus.OK);
            } else if (with.equalsIgnoreCase("fullobject")) {
                return new ResponseEntity<String>(Experiment.toJsonArray(experiments), headers, HttpStatus.OK);
            } else if (with.equalsIgnoreCase("prettyjson")) {
                return new ResponseEntity<String>(Experiment.toJsonArrayPretty(experiments), headers, HttpStatus.OK);
            } else if (with.equalsIgnoreCase("prettyjsonstub")) {
                return new ResponseEntity<String>(Experiment.toJsonArrayStubPretty(experiments), headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<String>("ERROR: with" + with + " route is not implemented. ", headers, HttpStatus.NOT_IMPLEMENTED);
            }
        } else {
            return new ResponseEntity<String>(Experiment.toJsonArrayStub(experiments), headers, HttpStatus.OK);
        }
    }

    @Transactional
    @RequestMapping(params = "FindByCodeName", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindExperimentsByCodeNameEquals(@RequestParam("codeName") String codeName, @RequestParam(value = "with", required = false) String with) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<Experiment> experiments = Experiment.findExperimentsByCodeNameEquals(codeName).getResultList();
        if (with != null) {
            if (with.equalsIgnoreCase("analysisgroups")) {
                return new ResponseEntity<String>(Experiment.toJsonArrayStubWithAG(experiments), headers, HttpStatus.OK);
            } else if (with.equalsIgnoreCase("fullobject")) {
                return new ResponseEntity<String>(Experiment.toJsonArray(experiments), headers, HttpStatus.OK);
            } else if (with.equalsIgnoreCase("prettyjson")) {
                return new ResponseEntity<String>(Experiment.toJsonArrayPretty(experiments), headers, HttpStatus.OK);
            } else if (with.equalsIgnoreCase("prettyjsonstub")) {
                return new ResponseEntity<String>(Experiment.toJsonArrayStubPretty(experiments), headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<String>(Experiment.toJsonArrayStub(experiments), headers, HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<String>(Experiment.toJsonArrayStub(experiments), headers, HttpStatus.OK);
        }
    }

    @Transactional
    @RequestMapping(value = "/experimentname/**", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindProtocolByExperimentNameEqualsRoute(HttpServletRequest request) {
        String restOfTheUrl = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String experimentName = restOfTheUrl.split("experimentname\\/")[1].replaceAll("/$", "");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(Experiment.toJsonArrayStub(Experiment.findExperimentByExperimentName(experimentName)), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(params = "FindByExperimentName", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindExperimentByExperimentNameEqualsGet(@RequestParam("experimentName") String experimentName, @RequestParam(value = "with", required = false) String with, @RequestParam(value = "protocolId", required = false) Long protocolId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        logger.debug("incoming experiment name is " + experimentName);
        List<Experiment> experiments;
        if (protocolId != null && protocolId != 0) {
            experiments = Experiment.findExperimentByExperimentNameAndProtocolId(experimentName, protocolId);
        } else {
            experiments = Experiment.findExperimentByExperimentName(experimentName);
        }
        if (with != null) {
            logger.debug("incoming with param is " + with);
            if (with.equalsIgnoreCase("analysisgroups")) {
                return new ResponseEntity<String>(Experiment.toJsonArrayStubWithAG(experiments), headers, HttpStatus.OK);
            } else if (with.equalsIgnoreCase("fullobject")) {
                return new ResponseEntity<String>(Experiment.toJsonArray(experiments), headers, HttpStatus.OK);
            } else if (with.equalsIgnoreCase("prettyjson")) {
                return new ResponseEntity<String>(Experiment.toJsonArrayPretty(experiments), headers, HttpStatus.OK);
            } else if (with.equalsIgnoreCase("prettyjsonstub")) {
                return new ResponseEntity<String>(Experiment.toJsonArrayStubPretty(experiments), headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<String>(Experiment.toJsonArrayStub(experiments), headers, HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<String>(Experiment.toJsonArrayStub(experiments), headers, HttpStatus.OK);
        }
    }

    @Transactional
    @RequestMapping(params = "FindByName", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindExperimentByNameGet(@RequestParam("name") String name, @RequestParam(value = "with", required = false) String with, @RequestParam(value = "protocolId", required = false) Long protocolId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<Experiment> experiments;
        if (protocolId != null && protocolId != 0) {
            experiments = Experiment.findExperimentByExperimentNameAndProtocolId(name, protocolId);
        } else {
            experiments = Experiment.findExperimentByExperimentName(name);
        }
        if (with != null) {
            if (with.equalsIgnoreCase("analysisgroups")) {
                return new ResponseEntity<String>(Experiment.toJsonArrayStubWithAG(experiments), headers, HttpStatus.OK);
            } else if (with.equalsIgnoreCase("fullobject")) {
                return new ResponseEntity<String>(Experiment.toJsonArray(experiments), headers, HttpStatus.OK);
            } else if (with.equalsIgnoreCase("prettyjson")) {
                return new ResponseEntity<String>(Experiment.toJsonArrayPretty(experiments), headers, HttpStatus.OK);
            } else if (with.equalsIgnoreCase("prettyjsonstub")) {
                return new ResponseEntity<String>(Experiment.toJsonArrayStubPretty(experiments), headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<String>("ERROR: with" + with + " route is not implemented. ", headers, HttpStatus.NOT_IMPLEMENTED);
            }
        } else {
            return new ResponseEntity<String>(Experiment.toJsonArrayStub(experiments), headers, HttpStatus.OK);
        }
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Experiment experiment, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, experiment);
            return "experiments/create";
        }
        uiModel.asMap().clear();
        experiment.persist();
        return "redirect:/experiments/" + encodeUrlPathSegment(experiment.getId().toString(), httpServletRequest);
    }

    @Transactional
    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Experiment());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (Protocol.countProtocols() == 0) {
            dependencies.add(new String[] { "protocol", "protocols" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "experiments/create";
    }

    @Transactional
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("experiment", Experiment.findExperiment(id));
        uiModel.addAttribute("itemId", id);
        return "experiments/show";
    }

    @Transactional
    @RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("experiments", Experiment.findExperimentEntries(firstResult, sizeNo));
            float nrOfPages = (float) Experiment.countExperiments() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("experiments", Experiment.findAllExperiments());
        }
        addDateTimeFormatPatterns(uiModel);
        return "experiments/list";
    }

    @Transactional
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Experiment experiment, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, experiment);
            return "experiments/update";
        }
        uiModel.asMap().clear();
        experiment.merge();
        return "redirect:/experiments/" + encodeUrlPathSegment(experiment.getId().toString(), httpServletRequest);
    }

    @Transactional
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Experiment.findExperiment(id));
        return "experiments/update";
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Experiment experiment = Experiment.findExperiment(id);
        experiment.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/experiments";
    }

    void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("experiment_recordeddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("experiment_modifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }

    void populateEditForm(Model uiModel, Experiment experiment) {
        uiModel.addAttribute("experiment", experiment);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("lsTags", LsTag.findAllLsTags());
        List<Protocol> protocols = new ArrayList<Protocol>();
        protocols.add(Protocol.findProtocol(experiment.getProtocol().getId()));
        uiModel.addAttribute("protocols", protocols);
    }

    String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {
        }
        return pathSegment;
    }

    @RequestMapping(params = "find=ByLsTransaction", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindExperimentsByLsTransaction(@RequestParam("lsTransaction") Long lsTransaction) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(Experiment.toJsonArrayStub(Experiment.findExperimentsByLsTransaction(lsTransaction).getResultList()), headers, HttpStatus.OK);
    }

    @RequestMapping(params = "find=ByProtocol", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindExperimentsByProtocol(@RequestParam("protocol") Protocol protocol) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(Experiment.toJsonArrayStub(Experiment.findExperimentsByProtocol(protocol).getResultList()), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/protocol/{codeName}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindExperimentsByProtocolCodeName(@PathVariable("codeName") String codeName, @RequestParam(value = "with", required = false) String with) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<Protocol> protocols = Protocol.findProtocolsByCodeNameEquals(codeName).getResultList();
        if (protocols.size() == 1) {
            return new ResponseEntity<String>(Experiment.toJsonArrayStub(Experiment.findExperimentsByProtocol(protocols.get(0)).getResultList()), headers, HttpStatus.OK);
        } else if (protocols.size() > 1) {
            logger.error("ERROR: multiple protocols found with the same code name");
            return new ResponseEntity<String>("[ ]", headers, HttpStatus.CONFLICT);
        } else {
            logger.warn("WARN: no protocols found with the query code name");
            return new ResponseEntity<String>("[ ]", headers, HttpStatus.NOT_FOUND);
        }
    }
}
