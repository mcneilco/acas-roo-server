package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Lob;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.labsynch.labseer.domain.IsoSalt;
import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.domain.SaltForm;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class SaltFormDTO{


	private long id;
	
	@Lob
	//@Size(max = 10485760)
	private String molStructure;

	@Size(max = 255)
	private String corpName;

	@Size(max = 255)
	private String casNumber;

	private String chemist;

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
    
    public String getChemist() {
        return this.chemist;
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

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static SaltFormDTO fromJsonToSaltFormDTO(String json) {
        return new JSONDeserializer<SaltFormDTO>()
        .use(null, SaltFormDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<SaltFormDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<SaltFormDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<SaltFormDTO> fromJsonArrayToSaltFoes(String json) {
        return new JSONDeserializer<List<SaltFormDTO>>()
        .use("values", SaltFormDTO.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public void setChemist(String chemist) {
        this.chemist = chemist;
    }
}
