package com.labsynch.labseer.web;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.gvnix.addon.datatables.annotations.GvNIXDatatables;
import org.gvnix.addon.web.mvc.annotations.jquery.GvNIXWebJQuery;
import org.gvnix.web.datatables.query.SearchResults;
import org.gvnix.web.datatables.util.DatatablesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;
import com.github.dandelion.datatables.extras.spring3.ajax.DatatablesParams;
import com.labsynch.labseer.domain.IsoSalt;
import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.SaltForm;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.service.ChemStructureService;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.path.PathBuilder;

@RooWebScaffold(path = "saltforms", formBackingObject = SaltForm.class)
@RequestMapping("/saltforms")
@Controller
@Transactional
@GvNIXWebJQuery
@GvNIXDatatables(ajax = false)
@RooWebFinder
public class SaltFormController {

	private static final Logger logger = LoggerFactory.getLogger(SaltFormController.class);

	@Autowired
	private ChemStructureService service;

	private SaltForm saveSaltFormStructure(SaltForm saltForm) {
		int saltFormCdId = service.saveStructure(saltForm.getMolStructure(), "SaltForm_Structure");
		saltForm.setCdId(saltFormCdId);
		return saltForm;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String create(@Valid SaltForm saltForm, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			uiModel.addAttribute("saltForm", saltForm);
			return "saltforms/create";
		}
		uiModel.asMap().clear();
		String molfile;
		try {
			molfile = service.toMolfile(saltForm.getMolStructure());
			saltForm.setMolStructure(molfile);
			saltForm = this.saveSaltFormStructure(saltForm);
			saltForm.persist();
			return "redirect:/saltforms/" + encodeUrlPathSegment(saltForm.getId().toString(), httpServletRequest);
		} catch (CmpdRegMolFormatException e) {
			return "redirect:/error";
		}
	}

	@RequestMapping(params = "form", method = RequestMethod.GET)
	public String createForm(Model uiModel) {
		uiModel.addAttribute("saltForm", new SaltForm());
		return "saltforms/create";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("saltform", SaltForm.findSaltForm(id));
		uiModel.addAttribute("itemId", id);
		return "saltforms/show";
	}

	@RequestMapping(method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		if (page != null || size != null) {
			int sizeNo = size == null ? 10 : size.intValue();
			uiModel.addAttribute("saltforms", SaltForm.findSaltFormEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
			float nrOfPages = (float) SaltForm.countSaltForms() / sizeNo;
			uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
		} else {
			uiModel.addAttribute("saltforms", SaltForm.findSaltFormEntries(0, 10));
		}
		return "saltforms/list";
	}

	@Transactional
	@RequestMapping(method = RequestMethod.PUT)
	public String update(@Valid SaltForm saltForm, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			logger.debug("binding error lajolla ");
			uiModel.addAttribute("saltForm", saltForm);
			Parent parent = saltForm.getParent();
			List<Parent> parents = new ArrayList<Parent>();
			parents.add(parent);
			uiModel.addAttribute("parents", parents);
			uiModel.addAttribute("parent", parent);
			return "saltforms/update";
		}
		uiModel.asMap().clear();
		saltForm.merge();
		return "redirect:/saltforms/" + encodeUrlPathSegment(saltForm.getId().toString(), httpServletRequest);
	}

	@Transactional
	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model uiModel) {
		SaltForm saltForm = SaltForm.findSaltForm(id);
		uiModel.addAttribute("saltForm", saltForm);
		uiModel.addAttribute("itemId", id);
		List<IsoSalt> isoSalts = new ArrayList<IsoSalt>();
		if (saltForm.getIsoSalts() != null) {
			for (IsoSalt isoSalt : saltForm.getIsoSalts()) {
				isoSalts.add(isoSalt);
			}
			uiModel.addAttribute("isosalts", isoSalts);
		}
		List<Lot> lots = new ArrayList<Lot>();
		if (saltForm.getLots() != null) {
			for (Lot lot : saltForm.getLots()) {
				lots.add(lot);
			}
			uiModel.addAttribute("lots", lots);
		}
		List<Parent> parents = new ArrayList<Parent>();
		if (saltForm.getParent() != null) {
			Parent parent = Parent.findParent(saltForm.getParent().getId());
			parents.add(parent);
			uiModel.addAttribute("parents", parents);
			uiModel.addAttribute("parent", parent);
		}
		return "saltforms/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		SaltForm.findSaltForm(id).remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/saltforms";
	}

	//	  @ModelAttribute("isosalts")
	//	  public Collection<IsoSalt> populateIsoSalts() {
	//		  return IsoSalt.findIsoSaltEntries(0, 2);
	//	  }
	//
	//	  @ModelAttribute("lots")
	//	  public Collection<Lot> populateLots() {
	//		  return Lot.findLotEntries(0, 2);
	//	  }
	//
	//	@ModelAttribute("parents")
	//	  public Collection<Parent> populateParents() {
	//		  return Parent.findParentEntries(0, 2);
	//	  }
	//
	//	@ModelAttribute("saltforms")
	//	  public Collection<SaltForm> populateSaltForms() {
	//		  return SaltForm.findSaltFormEntries(0, 2);
	//	  }
	//
	//	@ModelAttribute("scientists")
	//	  public Collection<Scientist> populateScientists() {
	//		  return Scientist.findAllScientists();
	//	  }
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

	@RequestMapping(headers = "Accept=application/json", value = "/datatables/ajax", params = "ajax_find=ByCdId", produces = "application/json")
	@ResponseBody
	public DatatablesResponse<Map<String, String>> findSaltFormsByCdId(@DatatablesParams DatatablesCriterias criterias, @RequestParam("CdId") int CdId) {
		BooleanBuilder baseSearch = new BooleanBuilder();
		// Base Search. Using BooleanBuilder, a cascading builder for
		// Predicate expressions
		PathBuilder<SaltForm> entity = new PathBuilder<SaltForm>(SaltForm.class, "entity");
		if (CdId > 0) {
			baseSearch.and(entity.getNumber("CdId", int.class).eq(CdId));
		} else {
			baseSearch.and(entity.getNumber("CdId", int.class).isNull());
		}
		SearchResults<SaltForm> searchResult = DatatablesUtils.findByCriteria(entity, SaltForm.entityManager(), criterias, baseSearch);
		// Get datatables required counts
		long totalRecords = searchResult.getTotalCount();
		long recordsFound = searchResult.getResultsCount();
		// Entity pk field name
		String pkFieldName = "id";
		org.springframework.ui.Model uiModel = new org.springframework.ui.ExtendedModelMap();
		addDateTimeFormatPatterns(uiModel);
		Map<String, Object> datePattern = uiModel.asMap();
		DataSet<Map<String, String>> dataSet = DatatablesUtils.populateDataSet(searchResult.getResults(), pkFieldName, totalRecords, recordsFound, criterias.getColumnDefs(), datePattern, conversionService_dtt);
		return DatatablesResponse.build(dataSet, criterias);
	}
	
	@RequestMapping(produces = "text/html", value = "/list")
	public String listDatatablesDetail(Model uiModel, HttpServletRequest request, @ModelAttribute SaltForm saltForm) {
		// Do common datatables operations: get entity list filtered by request parameters
		listDatatables(uiModel, request, saltForm);
		// Show only the list fragment (without footer, header, menu, etc.) 
		return "forward:/WEB-INF/views/saltforms/list.jspx";
	}
}
