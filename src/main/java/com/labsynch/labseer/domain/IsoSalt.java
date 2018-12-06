package com.labsynch.labseer.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.utils.Configuration;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Transactional
@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findIsoSaltsBySaltForm", "findIsoSaltsBySaltFormAndType" })
public class IsoSalt {

	private static final Logger logger = LoggerFactory.getLogger(IsoSalt.class);
	
    @ManyToOne
    private Isotope isotope;

    @ManyToOne
    private Salt salt;

    @Size(max = 25)
    private String type;

    private Double equivalents;

    private Boolean ignore;

    @ManyToOne
    private SaltForm saltForm;

    
    public String toJson() {
        return new JSONSerializer().exclude("*.class", "saltForm").serialize(this);
    }

    public static IsoSalt fromJsonToIsoSalt(String json) {
        return new JSONDeserializer<IsoSalt>().use(null, IsoSalt.class).deserialize(json);
    }

    public static String toJsonArray(Collection<IsoSalt> collection) {
        return new JSONSerializer().exclude("*.class", "saltForm").serialize(collection);
    }

    public static Collection<IsoSalt> fromJsonArrayToIsoSalts(String json) {
        return new JSONDeserializer<List<IsoSalt>>().use(null, ArrayList.class).use("values", IsoSalt.class).deserialize(json);
    }
    
    public static double calculateSaltWeight(IsoSalt isoSalt){
		double saltWeight = 0;
		logger.debug("isoSalt type: " + isoSalt.getType());
		if (isoSalt.getType().equalsIgnoreCase("isotope")){
			logger.debug("isoSalt type: " + isoSalt.getType());

			saltWeight = isoSalt.getIsotope().getMassChange(); 
			logger.debug("intial saltWeight: " + saltWeight);	
			logger.debug("equivalents: " + isoSalt.getEquivalents());
			saltWeight = saltWeight * isoSalt.getEquivalents();
			logger.debug("final saltWeight: " + saltWeight);	
		} 
		else if (isoSalt.getType().equalsIgnoreCase("salt")){
			saltWeight = isoSalt.getSalt().getMolWeight();
			logger.debug("intial saltWeight: " + saltWeight);	
			logger.debug("equivalents: " + isoSalt.getEquivalents());
			//correction for charged salts
			saltWeight = saltWeight * isoSalt.getEquivalents();
			if (Configuration.getConfigInfo().getMetaLot().isUseExactMass()){
				saltWeight = saltWeight - (1.007825)*(isoSalt.getSalt().getCharge() * isoSalt.getEquivalents());
			}else{
				saltWeight = saltWeight - (1.00794)*(isoSalt.getSalt().getCharge() * isoSalt.getEquivalents());
			}
			logger.debug("final saltWeight: " + saltWeight);	
		}
		return saltWeight;
	}
    
}
