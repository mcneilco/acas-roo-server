package com.labsynch.labseer.dto;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.CompoundType;
import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.ParentAnnotation;
import com.labsynch.labseer.domain.StereoCategory;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.service.ChemStructureService;
import com.labsynch.labseer.utils.MoleculeUtil;

@RooJavaBean
@RooToString
@RooJson
public class ParentDTO{

	private Long id;
	
	private String corpName;

	private String chemist;

	private String commonName;

	private StereoCategory stereoCategory;

	private String stereoComment;

	private String molStructure;
		
	private Double exactMass;

	private String molFormula;

	private int CdId;

    private Double molWeight;

	private Boolean ignore;
	
    private ParentAnnotation parentAnnotation;
    
    private CompoundType compoundType;
    
    private String comment;
    
    private Boolean isMixture;

	private Set<SaltFormDTO> saltForms = new HashSet<SaltFormDTO>();
	
	private Set<ParentAliasDTO> parentAliases = new HashSet<ParentAliasDTO>();

	public void setParent(Parent parent) {
		
		this.setChemist(parent.getChemist());
		this.setCommonName(parent.getCommonName());
		this.setCorpName(parent.getCorpName());
		this.setIgnore(parent.getIgnore());
		this.setCompoundType(parent.getCompoundType());
		this.setParentAnnotation(parent.getParentAnnotation());
		this.setComment(parent.getComment());
		this.setIsMixture(parent.getIsMixture());
		//this may be commented out in when we display by id
		this.setMolStructure(parent.getMolStructure());
		try{
			this.setMolFormula(MoleculeUtil.getMolFormula(parent.getMolStructure()));
		}catch (CmpdRegMolFormatException e) {
			//leave mol formula blank
		}
		this.setCdId(parent.getCdId());
		this.setMolWeight(parent.getMolWeight());
		this.setExactMass(parent.getExactMass());
		this.setStereoCategory(parent.getStereoCategory());
		this.setStereoComment(parent.getStereoComment());
		this.setId(parent.getId());
	}

}
