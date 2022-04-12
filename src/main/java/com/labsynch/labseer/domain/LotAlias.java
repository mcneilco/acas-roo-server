package com.labsynch.labseer.domain;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.dto.LotAliasDTO;
import com.labsynch.labseer.exceptions.LotNotFoundException;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders={"findLotAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals", 
						     "findLotAliasesByLotAndLsTypeEqualsAndLsKindEquals",
							"findLotAliasesByLot"})
public class LotAlias {

	private static final Logger logger = LoggerFactory.getLogger(LotAlias.class);
	
	@ManyToOne
    @org.hibernate.annotations.Index(name="LotAlias_Parent_IDX")
	@JoinColumn(name = "lot")
	private Lot lot;
	
    private String lsType;

    private String lsKind;
    
    private String aliasName;
    
	private boolean preferred;
	
	private boolean ignored;
	
	private boolean deleted;
    
	public LotAlias(){
	}
	
	public LotAlias(Lot lot, String lsType, String lsKind, String aliasName, boolean preferred){
		this.lot = lot;
		this.lsType = lsType;
		this.lsKind = lsKind;
		this.aliasName = aliasName;
		this.preferred = preferred;
	}
	
	public LotAlias(LotAliasDTO lotAliasDTO) throws LotNotFoundException{
		try{
			Lot lot = Lot.findLotsByCorpNameEquals(lotAliasDTO.getLotCorpName()).getSingleResult();
			this.lot = lot;
		}catch (Exception e){
			logger.error("Lot "+lotAliasDTO.getLotCorpName()+" could not be found.",e);
			throw new LotNotFoundException("Lot "+lotAliasDTO.getLotCorpName()+" could not be found.");
		}
		this.lsType = lotAliasDTO.getLsType();
		this.lsKind = lotAliasDTO.getLsKind();
		this.aliasName = lotAliasDTO.getAliasName();
		this.preferred = lotAliasDTO.isPreferred();
	}
}
