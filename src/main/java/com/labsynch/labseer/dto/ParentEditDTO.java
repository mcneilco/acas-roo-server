package com.labsynch.labseer.dto;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.CompoundType;
import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.ParentAnnotation;
import com.labsynch.labseer.domain.Scientist;
import com.labsynch.labseer.domain.StereoCategory;

@RooJavaBean
@RooToString
@RooJson
public class ParentEditDTO{

	private Long id;

	private String corpName;

	private String chemistCode;

	private String commonName;

	private String stereoCategoryCode;

	private String stereoComment;

	private String molStructure;
	
	private Double exactMass;

	private String molFormula;

	private int CdId;

	private Double molWeight;

	private Boolean ignore;

	private String parentAnnotationCode;

	private String compoundTypeCode;

	private String comment;

	private Boolean isMixture;

	private String commonNameAliases;

	private String liveDesignAliases;

	private String defaultAliases;

//	private Set<SaltFormDTO> saltForms = new HashSet<SaltFormDTO>();
	
	private Set<ParentAliasDTO> parentAliases = new HashSet<ParentAliasDTO>();

	public void setParent() {

	}
	
	public void setParent(Parent parent) {
		
		this.setChemistCode(parent.getChemist().getCode());
		this.setCommonName(parent.getCommonName());
		this.setCorpName(parent.getCorpName());
		this.setIgnore(parent.getIgnore());
		this.setCompoundTypeCode(parent.getCompoundType().getCode());
//		this.setParentAnnotation(parent.getParentAnnotation());
		this.setComment(parent.getComment());
		this.setIsMixture(parent.getIsMixture());
//		//this may be commented out in when we display by id
//		this.setMolStructure(parent.getMolStructure());
//		this.setMolFormula(parent.getMolFormula());
//		this.setCdId(parent.getCdId());
//		this.setMolWeight(parent.getMolWeight());
//		this.setExactMass(parent.getExactMass());
//		this.setStereoCategory(parent.getStereoCategory());
//		this.setStereoComment(parent.getStereoComment());
//		this.setId(parent.getId());
	}

}
