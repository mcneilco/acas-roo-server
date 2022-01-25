package com.labsynch.labseer.chemclasses.bbchem;

import com.labsynch.labseer.domain.AbstractBBChemStructure;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(sequenceName = "BBCHEM_STRUCTURE_PKSEQ", finders = { "findBBChemDryRunStructuresByRegEquals", "findBBChemDryRunStructuresByPreRegEquals"})
public class BBChemDryRunStructure extends AbstractBBChemStructure {

}
