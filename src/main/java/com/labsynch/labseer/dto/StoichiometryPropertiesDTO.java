package com.labsynch.labseer.dto;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.ItxLsThingLsThing;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.LsThingValue;
import com.labsynch.labseer.utils.ExcludeNulls;
import com.labsynch.labseer.utils.SimpleUtil;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class StoichiometryPropertiesDTO {
	
	private static final Logger logger = LoggerFactory.getLogger(StoichiometryPropertiesDTO.class);

	String codeName;
	
	String corpName;
	
	String lsType;
	
	String lsKind;
	
	String preferredName;
	
	BigDecimal molWeight;
	
	BigDecimal density;
	
	BigDecimal concentration;
	
	String concUnit;
	
	String phase;
	
	public String toJson() {
        return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
	
	public static String toJsonArray(Collection<StoichiometryPropertiesDTO> collection) {
        return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
    }
	
	public StoichiometryPropertiesDTO(){
	}
	
	public StoichiometryPropertiesDTO(LsThing thing){
		LsThing parent = null;
		try{
			parent = ItxLsThingLsThing.findItxLsThingLsThingsByLsTypeEqualsAndLsKindEqualsAndFirstLsThingEquals("instantiates", "batch_parent", thing).getSingleResult().getSecondLsThing();
		}catch (Exception e){
			logger.debug("Could not find parent for provided lsThing. Continuing with batch properties");
		}
		//For all properties, try batch first, then try parent if property was not found on batch
		this.codeName = thing.getCodeName();
		if (thing.pickBestCorpName() != null){
			this.corpName = thing.pickBestCorpName().getLabelText();
		}else if (parent != null && parent.pickBestCorpName() != null){
			this.corpName = parent.pickBestCorpName().getLabelText();
		}
		this.lsType = thing.getLsType();
		this.lsKind = thing.getLsKind();
		if (thing.pickBestName() != null){
			this.preferredName = thing.pickBestName().getLabelText();
		}else if (parent != null && parent.pickBestName() != null){
			this.preferredName = parent.pickBestName().getLabelText();
		}
		List<LsThingValue> molWeightValues = SimpleUtil.pluckValueByValueTypeKind(thing, "numericValue", "molecular weight");
		if (molWeightValues.size() > 0){
			this.molWeight = molWeightValues.get(0).getNumericValue();
		} else{
			molWeightValues = SimpleUtil.pluckValueByValueTypeKind(thing, "numericValue", "measured molecular weight");
			if (molWeightValues.size() > 0){
				this.molWeight = molWeightValues.get(0).getNumericValue();
			}else if (parent != null){
				molWeightValues = SimpleUtil.pluckValueByValueTypeKind(parent, "numericValue", "molecular weight");
				if (molWeightValues.size() > 0){
					this.molWeight = molWeightValues.get(0).getNumericValue();
				}else{
					molWeightValues = SimpleUtil.pluckValueByValueTypeKind(parent, "numericValue", "measured molecular weight");
					if (molWeightValues.size() > 0){
						this.molWeight = molWeightValues.get(0).getNumericValue();
					}
				}
			}
		}
		List<LsThingValue> densityValues = SimpleUtil.pluckValueByValueTypeKind(thing, "numericValue", "density");
		if (densityValues.size() > 0){
			this.density = densityValues.get(0).getNumericValue();
		}else if (parent != null){
			densityValues = SimpleUtil.pluckValueByValueTypeKind(parent, "numericValue", "density");
			if (densityValues.size() > 0){
				this.density = densityValues.get(0).getNumericValue();
			}
		}
		List<LsThingValue> concValues = SimpleUtil.pluckValueByValueTypeKind(thing, "numericValue", "concentration");
		if (concValues.size() > 0){
			 this.concentration = concValues.get(0).getNumericValue();
			 this.concUnit = concValues.get(0).getUnitKind();
		}else if (parent != null){
			concValues = SimpleUtil.pluckValueByValueTypeKind(parent, "numericValue", "concentration");
			if (concValues.size() > 0){
				 this.concentration = concValues.get(0).getNumericValue();
				 this.concUnit = concValues.get(0).getUnitKind();
			}
		}
		List<LsThingValue> phaseValues = SimpleUtil.pluckValueByValueTypeKind(thing, "codeValue", "phase");
		if (phaseValues.size() > 0){
			this.phase = phaseValues.get(0).getCodeValue();
		}else if (parent != null){
			phaseValues = SimpleUtil.pluckValueByValueTypeKind(thing, "codeValue", "phase");
			if (phaseValues.size() > 0){
				this.phase = phaseValues.get(0).getCodeValue();
			}
		}
	}
	
}
