// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.ChemStructure;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect ChemStructure_Roo_Json {
    
    public String ChemStructure.toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public static ChemStructure ChemStructure.fromJsonToChemStructure(String json) {
        return new JSONDeserializer<ChemStructure>().use(null, ChemStructure.class).deserialize(json);
    }
    
    public static String ChemStructure.toJsonArray(Collection<ChemStructure> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static Collection<ChemStructure> ChemStructure.fromJsonArrayToChemStructures(String json) {
        return new JSONDeserializer<List<ChemStructure>>().use(null, ArrayList.class).use("values", ChemStructure.class).deserialize(json);
    }
    
}
