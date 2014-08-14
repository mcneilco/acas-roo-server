package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.AnalysisGroupState;
import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.dto.AnalysisGroupValueDTO;
import com.labsynch.labseer.utils.PropertiesUtilService;
import flexjson.JSONDeserializer;
import flexjson.JSONTokener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

@RooWebJson(jsonObject = AnalysisGroupValue.class)
@Controller
@RequestMapping("/analysisgroupvalues")
@RooWebScaffold(path = "analysisgroupvalues", formBackingObject = AnalysisGroupValue.class)
@Transactional
@RooWebFinder
public class AnalysisGroupValueController {

    private static final Logger logger = LoggerFactory.getLogger(AnalysisGroupValueController.class);

    @Autowired
    private PropertiesUtilService propertiesUtilService;

    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
        AnalysisGroupValue analysisGroupValue = AnalysisGroupValue.findAnalysisGroupValue(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (analysisGroupValue == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(analysisGroupValue.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/geneCodeData/geneid/{labelText}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getGeneCodeDataByGeneID(@PathVariable("labelText") String labelText) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        String thingType = "gene";
        String thingKind = "entrez gene";
        String labelType = "name";
        String labelKind = "Entrez Gene ID";
        LinkedHashSet<String> geneCodeHash = new LinkedHashSet<String>();
        List<LsThing> lsThings = LsThing.findLsThingByLabelText(thingType, thingKind, labelType, labelKind, labelText).getResultList();
        logger.debug("search text: " + labelText);
        logger.debug("number of results: " + lsThings.size());
        for (LsThing lsThing : lsThings) {
            geneCodeHash.add(lsThing.getCodeName());
        }
        Set<String> geneCodeList = new HashSet<String>();
        geneCodeList.addAll(geneCodeHash);
        List<AnalysisGroupValueDTO> agValues = null;
        try {
            agValues = AnalysisGroupValue.findAnalysisGroupValueDTO(geneCodeList).getResultList();
        } catch (Exception e) {
            logger.error("caught an error" + e.toString());
            return new ResponseEntity<String>("[]", headers, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>(AnalysisGroupValueDTO.toJsonArray(agValues), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/geneCodeData", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getGeneCodeData(@RequestBody String json, @RequestParam(value = "format", required = false) String format, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size) {
        logger.debug("incoming json: " + json);
        Collection<String> batchCodes = new JSONDeserializer<List<String>>().use(null, ArrayList.class).use("values", String.class).deserialize(json);
        for (String bc : batchCodes) {
            logger.debug("batch code: " + bc);
        }
        Set<String> geneCodeList = new HashSet<String>();
        geneCodeList.addAll(batchCodes);
        List<AnalysisGroupValueDTO> agValues = null;
        try {
            agValues = AnalysisGroupValue.findAnalysisGroupValueDTO(geneCodeList).getResultList();
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

    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<AnalysisGroupValue> result = AnalysisGroupValue.findAllAnalysisGroupValues();
        return new ResponseEntity<String>(AnalysisGroupValue.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
        AnalysisGroupValue analysisGroupValue = AnalysisGroupValue.fromJsonToAnalysisGroupValue(json);
        analysisGroupValue.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(analysisGroupValue.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/jsonArrayFile", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArrayFile(@RequestBody String jsonFile) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        int batchSize = propertiesUtilService.getBatchSize();
        int i = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(jsonFile));
            JSONTokener jsonTokens = new JSONTokener(br);
            Object token;
            char delimiter;
            char END_OF_ARRAY = ']';
            while (jsonTokens.more()) {
                delimiter = jsonTokens.nextClean();
                if (delimiter != END_OF_ARRAY) {
                    token = jsonTokens.nextValue();
                    AnalysisGroupValue analysisGroupValue = AnalysisGroupValue.fromJsonToAnalysisGroupValue(token.toString());
                    analysisGroupValue.persist();
                    if (i % batchSize == 0) {
                        analysisGroupValue.flush();
                        analysisGroupValue.clear();
                    }
                    i++;
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            logger.error("ERROR: " + e);
            return new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("[]", headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/jsonArrayParseOld", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArrayParse(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        int batchSize = propertiesUtilService.getBatchSize();
        int i = 0;
        try {
            JSONTokener jsonTokens = new JSONTokener(json);
            Object token;
            char delimiter;
            char END_OF_ARRAY = ']';
            while (jsonTokens.more()) {
                delimiter = jsonTokens.nextClean();
                if (delimiter != END_OF_ARRAY) {
                    token = jsonTokens.nextValue();
                    AnalysisGroupValue analysisGroupValue = AnalysisGroupValue.fromJsonToAnalysisGroupValue(token.toString());
                    analysisGroupValue.persist();
                    if (i % batchSize == 0) {
                        analysisGroupValue.flush();
                        analysisGroupValue.clear();
                    }
                    i++;
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            logger.error("ERROR: " + e);
            return new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("[]", headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = { "/jsonArray", "/jsonArrayParse" }, method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        int batchSize = propertiesUtilService.getBatchSize();
        int i = 0;
        BufferedReader br = null;
        StringReader sr = null;
        try {
            sr = new StringReader(json);
            br = new BufferedReader(sr);
            for (AnalysisGroupValue analysisGroupValue : AnalysisGroupValue.fromJsonArrayToAnalysisGroupValues(br)) {
                analysisGroupValue.persist();
                if (i % batchSize == 0) {
                    analysisGroupValue.flush();
                    analysisGroupValue.clear();
                    logger.debug("flushing analysis group batch value: " + i);
                }
                i++;
            }
        } catch (Exception e) {
            logger.error("ERROR: " + e);
            return new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
        } finally {
            IOUtils.closeQuietly(sr);
            IOUtils.closeQuietly(br);
        }
        return new ResponseEntity<String>("[]", headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = { "/", "/{id}" }, method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        AnalysisGroupValue analysisGroupValue = AnalysisGroupValue.fromJsonToAnalysisGroupValue(json);
        if (analysisGroupValue.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (AnalysisGroupValue analysisGroupValue : AnalysisGroupValue.fromJsonArrayToAnalysisGroupValues(json)) {
            if (analysisGroupValue.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
        AnalysisGroupValue analysisGroupValue = AnalysisGroupValue.findAnalysisGroupValue(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (analysisGroupValue == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        analysisGroupValue.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    void populateEditForm(Model uiModel, AnalysisGroupValue analysisGroupValue) {
        uiModel.addAttribute("analysisGroupValue", analysisGroupValue);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("analysisgroupstates", new ArrayList<AnalysisGroupState>().add(analysisGroupValue.getLsState()));
    }
}
