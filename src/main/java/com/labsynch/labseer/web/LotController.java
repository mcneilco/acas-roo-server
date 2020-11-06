package com.labsynch.labseer.web;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;
import com.labsynch.labseer.domain.BulkLoadFile;
import com.labsynch.labseer.domain.FileList;
import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.domain.LotAlias;
import com.labsynch.labseer.domain.Operator;
import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.PhysicalState;
import com.labsynch.labseer.domain.PurityMeasuredBy;
import com.labsynch.labseer.domain.SaltForm;
import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.domain.SolutionUnit;
import com.labsynch.labseer.domain.Unit;
import com.labsynch.labseer.domain.Vendor;

//import org.joda.time.DateTime;
//import org.springframework.format.annotation.DateTimeFormat;
@RequestMapping("/lots")
@Controller
@Transactional
public class LotController {

    Logger logger = LoggerFactory.getLogger(LotController.class);

    @RequestMapping(method = RequestMethod.POST)
    public String create(@Valid Lot lot, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("lot", lot);
            addDateTimeFormatPatterns(uiModel);
            return "lots/create";
        }
        uiModel.asMap().clear();
        lot.persist();
        return "redirect:/lots/" + encodeUrlPathSegment(lot.getId().toString(), httpServletRequest);
    }

    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(Model uiModel) {
        uiModel.addAttribute("lot", new Lot());
        addDateTimeFormatPatterns(uiModel);
        return "lots/create";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        Lot lot = Lot.findLot(id);
        uiModel.addAttribute("lot", lot);
        uiModel.addAttribute("itemId", id);
        uiModel.addAttribute("parent", lot.getSaltForm().getParent());
        List<SaltForm> saltForms = new ArrayList<SaltForm>();
        saltForms.add(lot.getSaltForm());
        uiModel.addAttribute("saltforms", saltForms);
        return "lots/show";
    }

    @Transactional
    @RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            uiModel.addAttribute("lots", Lot.findLotEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) Lot.countLots() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lots", Lot.findLotEntries(0, 10));
        }
        addDateTimeFormatPatterns(uiModel);
        return "lots/list";
    }

    @Transactional
    @RequestMapping(method = RequestMethod.PUT)
    public String update(@Valid Lot lot, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("lot", lot);
            logger.debug("ERROR lot: " + lot.toJson());
            SaltForm saltForm = lot.getSaltForm();
            Parent parent = saltForm.getParent();
            uiModel.addAttribute("parent", parent);
            List<SaltForm> saltForms = new ArrayList<SaltForm>();
            saltForms.add(lot.getSaltForm());
            uiModel.addAttribute("saltforms", saltForms);
            addDateTimeFormatPatterns(uiModel);
            return "lots/update";
        }
        uiModel.asMap().clear();
        logger.debug("before merging the lot data back");
        lot.merge();
        return "redirect:/lots/" + encodeUrlPathSegment(lot.getId().toString(), httpServletRequest);
    }

    @Transactional
    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        Lot lot = Lot.findLot(id);
        uiModel.addAttribute("lot", lot);
        SaltForm saltForm = lot.getSaltForm();
        Parent parent = saltForm.getParent();
        uiModel.addAttribute("parent", parent);
        List<SaltForm> saltForms = new ArrayList<SaltForm>();
        saltForms.add(Lot.findLot(id).getSaltForm());
        uiModel.addAttribute("saltforms", saltForms);
        addDateTimeFormatPatterns(uiModel);
        return "lots/update";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Lot.findLot(id).remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/lots";
    }

    //	@ModelAttribute("lots")
    //    public Collection<Lot> populateLots() {
    //        return Lot.findLotEntries(0, 2);
    //    }
    //
    //    @ModelAttribute("parents")
    //    public Collection<Parent> populateParents() {
    //        return Parent.findParentEntries(0, 2);
    //    }
    //
    //    @ModelAttribute("filelists")
    //    public Collection<FileList> populateFileLists() {
    //        return FileList.findFileListEntries(0, 2);
    //    }
    void addDateTimeFormatPatterns(Model uiModel) {
        //        uiModel.addAttribute("lot_synthesisdate_date_format", DateTimeFormat.patternForStyle("S-", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("lot_synthesisdate_date_format", DateTimeFormat.patternForStyle("S-", LocaleContextHolder.getLocale()));
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

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        Lot lot = Lot.findLot(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text; charset=utf-8");
        if (lot == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        //        return new ResponseEntity<String>(lot.toJson(), headers, HttpStatus.OK);
        return showJson(lot.getId());
    }

    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text; charset=utf-8");
        return new ResponseEntity<String>(Lot.toJsonArray(Lot.findAllLots()), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json) {
        Lot lot = Lot.fromJsonToLot(json);
        lot.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        return showJson(lot.getId());
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        for (Lot lot : Lot.fromJsonArrayToLots(json)) {
            lot.persist();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text");
        if (Lot.fromJsonToLot(json).merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text");
        for (Lot lot : Lot.fromJsonArrayToLots(json)) {
            if (lot.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
        Lot lot = Lot.findLot(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text");
        if (lot == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        lot.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @ModelAttribute("operators")
    public Collection<Operator> populateOperators() {
        return Operator.findAllOperators();
    }

    @ModelAttribute("physicalstates")
    public Collection<PhysicalState> populatePhysicalStates() {
        return PhysicalState.findAllPhysicalStates();
    }

    @ModelAttribute("puritymeasuredbys")
    public Collection<PurityMeasuredBy> populatePurityMeasuredBys() {
        return PurityMeasuredBy.findAllPurityMeasuredBys();
    }

    @ModelAttribute("solutionunits")
    public Collection<SolutionUnit> populateSolutionUnits() {
        return SolutionUnit.findAllSolutionUnits();
    }

    @ModelAttribute("units")
    public Collection<Unit> populateUnits() {
        return Unit.findAllUnits();
    }

    @ModelAttribute("vendors")
    public Collection<Vendor> populateVendors() {
        return Vendor.findAllVendors();
    }

    @RequestMapping(params = { "find=ByBuid", "form" }, method = RequestMethod.GET)
    public String findLotsByBuidForm(Model uiModel) {
        return "lots/findLotsByBuid";
    }

    @RequestMapping(params = "find=ByBuid", method = RequestMethod.GET)
    public String findLotsByBuid(@RequestParam("buid") long buid, Model uiModel) {
        uiModel.addAttribute("lots", Lot.findLotsByBuid(buid).getResultList());
        return "lots/list";
    }

    @RequestMapping(params = { "find=ByChemistAndSynthesisDateBetween", "form" }, method = RequestMethod.GET)
    public String findLotsByChemistAndSynthesisDateBetweenForm(Model uiModel) {
        uiModel.addAttribute("scientists", Author.findAllAuthors());
        addDateTimeFormatPatterns(uiModel);
        return "lots/findLotsByChemistAndSynthesisDateBetween";
    }

    @RequestMapping(params = "find=ByChemistAndSynthesisDateBetween", method = RequestMethod.GET)
    public String findLotsByChemistAndSynthesisDateBetween(@RequestParam("chemist") Author chemist, @RequestParam("minSynthesisDate") Date minSynthesisDate, @RequestParam("maxSynthesisDate") Date maxSynthesisDate, Model uiModel) {
        uiModel.addAttribute("lots", Lot.findLotsByChemistAndSynthesisDateBetween(chemist.getUserName(), minSynthesisDate, maxSynthesisDate).getResultList());
        addDateTimeFormatPatterns(uiModel);
        return "lots/list";
    }

    @RequestMapping(params = { "find=ByCorpNameEquals", "form" }, method = RequestMethod.GET)
    public String findLotsByCorpNameEqualsForm(Model uiModel) {
        return "lots/findLotsByCorpNameEquals";
    }

    @RequestMapping(params = "find=ByCorpNameEquals", method = RequestMethod.GET)
    public String findLotsByCorpNameEquals(@RequestParam("corpName") String corpName, Model uiModel) {
        uiModel.addAttribute("lots", Lot.findLotsByCorpNameEquals(corpName).getResultList());
        return "lots/list";
    }

    @RequestMapping(params = { "find=ByCorpNameLike", "form" }, method = RequestMethod.GET)
    public String findLotsByCorpNameLikeForm(Model uiModel) {
        return "lots/findLotsByCorpNameLike";
    }

    @RequestMapping(params = "find=ByCorpNameLike", method = RequestMethod.GET)
    public String findLotsByCorpNameLike(@RequestParam("corpName") String corpName, Model uiModel) {
        uiModel.addAttribute("lots", Lot.findLotsByCorpNameLike(corpName).getResultList());
        return "lots/list";
    }

    @RequestMapping(params = { "find=ByIsVirtualNot", "form" }, method = RequestMethod.GET)
    public String findLotsByIsVirtualNotForm(Model uiModel) {
        return "lots/findLotsByIsVirtualNot";
    }

    @RequestMapping(params = "find=ByIsVirtualNot", method = RequestMethod.GET)
    public String findLotsByIsVirtualNot(@RequestParam(value = "isVirtual", required = false) Boolean isVirtual, Model uiModel) {
        uiModel.addAttribute("lots", Lot.findLotsByIsVirtualNot(isVirtual == null ? new Boolean(false) : isVirtual).getResultList());
        return "lots/list";
    }

    @RequestMapping(params = { "find=ByNotebookPageEquals", "form" }, method = RequestMethod.GET)
    public String findLotsByNotebookPageEqualsForm(Model uiModel) {
        return "lots/findLotsByNotebookPageEquals";
    }

    @RequestMapping(params = "find=ByNotebookPageEquals", method = RequestMethod.GET)
    public String findLotsByNotebookPageEquals(@RequestParam("notebookPage") String notebookPage, Model uiModel) {
        uiModel.addAttribute("lots", Lot.findLotsByNotebookPageEquals(notebookPage).getResultList());
        return "lots/list";
    }

    @RequestMapping(params = { "find=ByNotebookPageEqualsAndIgnoreNot", "form" }, method = RequestMethod.GET)
    public String findLotsByNotebookPageEqualsAndIgnoreNotForm(Model uiModel) {
        return "lots/findLotsByNotebookPageEqualsAndIgnoreNot";
    }

    @RequestMapping(params = "find=ByNotebookPageEqualsAndIgnoreNot", method = RequestMethod.GET)
    public String findLotsByNotebookPageEqualsAndIgnoreNot(@RequestParam("notebookPage") String notebookPage, @RequestParam(value = "ignore", required = false) Boolean ignore, Model uiModel) {
        uiModel.addAttribute("lots", Lot.findLotsByNotebookPageEqualsAndIgnoreNot(notebookPage, ignore == null ? new Boolean(false) : ignore).getResultList());
        return "lots/list";
    }

    @RequestMapping(params = { "find=BySaltForm", "form" }, method = RequestMethod.GET)
    public String findLotsBySaltFormForm(Model uiModel) {
        uiModel.addAttribute("saltforms", SaltForm.findAllSaltForms());
        return "lots/findLotsBySaltForm";
    }

    @RequestMapping(params = "find=BySaltForm", method = RequestMethod.GET)
    public String findLotsBySaltForm(@RequestParam("saltForm") SaltForm saltForm, Model uiModel) {
        uiModel.addAttribute("lots", Lot.findLotsBySaltForm(saltForm).getResultList());
        return "lots/list";
    }

    @RequestMapping(params = { "find=BySynthesisDateBetween", "form" }, method = RequestMethod.GET)
    public String findLotsBySynthesisDateBetweenForm(Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        return "lots/findLotsBySynthesisDateBetween";
    }

    @RequestMapping(params = "find=BySynthesisDateBetween", method = RequestMethod.GET)
    public String findLotsBySynthesisDateBetween(@RequestParam("minSynthesisDate") Date minSynthesisDate, @RequestParam("maxSynthesisDate") Date maxSynthesisDate, Model uiModel) {
        uiModel.addAttribute("lots", Lot.findLotsBySynthesisDateBetween(minSynthesisDate, maxSynthesisDate).getResultList());
        addDateTimeFormatPatterns(uiModel);
        return "lots/list";
    }

    @RequestMapping(params = { "find=BySynthesisDateGreaterThan", "form" }, method = RequestMethod.GET)
    public String findLotsBySynthesisDateGreaterThanForm(Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        return "lots/findLotsBySynthesisDateGreaterThan";
    }

    @RequestMapping(params = "find=BySynthesisDateGreaterThan", method = RequestMethod.GET)
    public String findLotsBySynthesisDateGreaterThan(@RequestParam("synthesisDate") Date synthesisDate, Model uiModel) {
        uiModel.addAttribute("lots", Lot.findLotsBySynthesisDateGreaterThan(synthesisDate).getResultList());
        addDateTimeFormatPatterns(uiModel);
        return "lots/list";
    }

    @RequestMapping(params = { "find=BySynthesisDateLessThan", "form" }, method = RequestMethod.GET)
    public String findLotsBySynthesisDateLessThanForm(Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        return "lots/findLotsBySynthesisDateLessThan";
    }

    @RequestMapping(params = "find=BySynthesisDateLessThan", method = RequestMethod.GET)
    public String findLotsBySynthesisDateLessThan(@RequestParam("synthesisDate") Date synthesisDate, Model uiModel) {
        uiModel.addAttribute("lots", Lot.findLotsBySynthesisDateLessThan(synthesisDate).getResultList());
        addDateTimeFormatPatterns(uiModel);
        return "lots/list";
    }

	void populateEditForm(Model uiModel, Lot lot) {
        uiModel.addAttribute("lot", lot);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("bulkloadfiles", BulkLoadFile.findAllBulkLoadFiles());
        uiModel.addAttribute("filelists", FileList.findAllFileLists());
        uiModel.addAttribute("lotaliases", LotAlias.findAllLotAliases());
        uiModel.addAttribute("operators", Operator.findAllOperators());
        uiModel.addAttribute("physicalstates", PhysicalState.findAllPhysicalStates());
        uiModel.addAttribute("puritymeasuredbys", PurityMeasuredBy.findAllPurityMeasuredBys());
        uiModel.addAttribute("saltforms", SaltForm.findAllSaltForms());
        uiModel.addAttribute("solutionunits", SolutionUnit.findAllSolutionUnits());
        uiModel.addAttribute("units", Unit.findAllUnits());
        uiModel.addAttribute("vendors", Vendor.findAllVendors());
    }
}
