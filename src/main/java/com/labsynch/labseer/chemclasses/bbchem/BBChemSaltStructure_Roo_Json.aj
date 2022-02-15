// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.chemclasses.bbchem;

import com.labsynch.labseer.chemclasses.bbchem.BBChemSaltStructure;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect BBChemSaltStructure_Roo_Json {
    
    public String BBChemSaltStructure.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String BBChemSaltStructure.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static BBChemSaltStructure BBChemSaltStructure.fromJsonToBBChemSaltStructure(String json) {
        return new JSONDeserializer<BBChemSaltStructure>()
        .use(null, BBChemSaltStructure.class).deserialize(json);
    }
    
    public static String BBChemSaltStructure.toJsonArray(Collection<BBChemSaltStructure> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String BBChemSaltStructure.toJsonArray(Collection<BBChemSaltStructure> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<BBChemSaltStructure> BBChemSaltStructure.fromJsonArrayToBBChemSaltStructures(String json) {
        return new JSONDeserializer<List<BBChemSaltStructure>>()
        .use("values", BBChemSaltStructure.class).deserialize(json);
    }
    
}