package com.labsynch.labseer.dto;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.ItxLsThingLsThing;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.LsThingValue;
import com.labsynch.labseer.utils.ExcludeNulls;
import com.labsynch.labseer.utils.SimpleUtil;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


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
	
	BigDecimal amountMade;
	
	String amountMadeUnits;
	
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
				List<LsThingValue> duplexMolWeightValues = SimpleUtil.pluckValueByValueTypeKind(parent, "numericValue", "duplex molecular weight");
				if (molWeightValues.size() == 0 && duplexMolWeightValues.size() > 0){
					this.molWeight = duplexMolWeightValues.get(0).getNumericValue();
				} else if (molWeightValues.size() > 0){
					this.molWeight = molWeightValues.get(0).getNumericValue();
				}else{
					molWeightValues = SimpleUtil.pluckValueByValueTypeKind(parent, "numericValue", "measured molecular weight");
					if (molWeightValues.size() > 0){
						this.molWeight = molWeightValues.get(0).getNumericValue();
					}
				}
			}
		}
		
		List<LsThingValue> amountMadeValues = SimpleUtil.pluckValueByValueTypeKind(thing, "numericValue", "amount made");
		if (amountMadeValues.size() > 0){
			this.amountMade = amountMadeValues.get(0).getNumericValue();
			this.amountMadeUnits = amountMadeValues.get(0).getUnitKind();
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
	

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public static StoichiometryPropertiesDTO fromJsonToStoichiometryPropertiesDTO(String json) {
        return new JSONDeserializer<StoichiometryPropertiesDTO>()
        .use(null, StoichiometryPropertiesDTO.class).deserialize(json);
    }

	public static Collection<StoichiometryPropertiesDTO> fromJsonArrayToStoichiometryProes(String json) {
        return new JSONDeserializer<List<StoichiometryPropertiesDTO>>()
        .use("values", StoichiometryPropertiesDTO.class).deserialize(json);
    }

	public String getCodeName() {
        return this.codeName;
    }

	public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

	public String getCorpName() {
        return this.corpName;
    }

	public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

	public String getLsType() {
        return this.lsType;
    }

	public void setLsType(String lsType) {
        this.lsType = lsType;
    }

	public String getLsKind() {
        return this.lsKind;
    }

	public void setLsKind(String lsKind) {
        this.lsKind = lsKind;
    }

	public String getPreferredName() {
        return this.preferredName;
    }

	public void setPreferredName(String preferredName) {
        this.preferredName = preferredName;
    }

	public BigDecimal getMolWeight() {
        return this.molWeight;
    }

	public void setMolWeight(BigDecimal molWeight) {
        this.molWeight = molWeight;
    }

	public BigDecimal getDensity() {
        return this.density;
    }

	public void setDensity(BigDecimal density) {
        this.density = density;
    }

	public BigDecimal getConcentration() {
        return this.concentration;
    }

	public void setConcentration(BigDecimal concentration) {
        this.concentration = concentration;
    }

	public String getConcUnit() {
        return this.concUnit;
    }

	public void setConcUnit(String concUnit) {
        this.concUnit = concUnit;
    }

	public String getPhase() {
        return this.phase;
    }

	public void setPhase(String phase) {
        this.phase = phase;
    }

	public BigDecimal getAmountMade() {
        return this.amountMade;
    }

	public void setAmountMade(BigDecimal amountMade) {
        this.amountMade = amountMade;
    }

	public String getAmountMadeUnits() {
        return this.amountMadeUnits;
    }

	public void setAmountMadeUnits(String amountMadeUnits) {
        this.amountMadeUnits = amountMadeUnits;
    }
}
