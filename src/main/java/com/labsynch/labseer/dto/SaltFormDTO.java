package com.labsynch.labseer.dto;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Lob;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.IsoSalt;
import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.domain.SaltForm;
import com.labsynch.labseer.domain.Scientist;

@RooJavaBean
@RooToString
@RooJson
public class SaltFormDTO{


	private long id;
	
	@Lob
	//@Size(max = 10485760)
	private String molStructure;

	@Size(max = 255)
	private String corpName;

	@Size(max = 255)
	private String casNumber;

	private Scientist chemist;

	private int CdId;

	private Boolean ignore;	

	private Set<IsoSalt> isosalts = new HashSet<IsoSalt>();

	private Set<Lot> lots = new HashSet<Lot>();
	
    public void setSaltForm(SaltForm saltForm) {
    	this.setMolStructure(saltForm.getMolStructure());
    	this.setCorpName(saltForm.getCorpName());
    	this.setCasNumber(saltForm.getCasNumber());
    	this.setChemist(saltForm.getChemist());
    	this.setCdId(saltForm.getCdId());
    	this.setIgnore(saltForm.getIgnore());
    	this.setId(saltForm.getId());
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMolStructure() {
		return molStructure;
	}

	public void setMolStructure(String molStructure) {
		this.molStructure = molStructure;
	}

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
    
    public String getCasNumber() {
        return this.casNumber;
    }
    
    public void setCasNumber(String casNumber) {
        this.casNumber = casNumber;
    }
    
    public Scientist getChemist() {
        return this.chemist;
    }
    
    public void setChemist(Scientist chemist) {
        this.chemist = chemist;
    }
    
    public int getCdId() {
        return this.CdId;
    }
    
    public void setCdId(int CdId) {
        this.CdId = CdId;
    }
    
    public Boolean getIgnore() {
        return this.ignore;
    }
    
    public void setIgnore(Boolean ignore) {
        this.ignore = ignore;
    }
    
    public Set<IsoSalt> getIsosalts() {
        return this.isosalts;
    }
    
    public void setIsosalts(Set<IsoSalt> isosalts) {
        this.isosalts = isosalts;
    }
    
    public Set<Lot> getLots() {
        return this.lots;
    }
    
    public void setLots(Set<Lot> lots) {
        this.lots = lots;
    }
}
