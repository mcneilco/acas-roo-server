package com.labsynch.labseer.domain;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(sequenceName = "BBCHEM_STRUCTURE_PKSEQ", finders = { "findBBChemSaltFormStructuresByRegEquals", "findBBChemSaltFormStructuresByPreRegEquals"})
public class BBChemSaltFormStructure  extends AbstractBBChemStructure {

}
