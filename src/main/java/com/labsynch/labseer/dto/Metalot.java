package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.FileList;
import com.labsynch.labseer.domain.IsoSalt;
import com.labsynch.labseer.domain.Lot;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;

@RooJavaBean
@RooToString
@RooJson

public class Metalot {
	
	private boolean skipParentDupeCheck = false;

	private Lot lot;
	
	private Set<IsoSalt> isosalts = new HashSet<IsoSalt>();

	private Set<FileList> fileList = new HashSet<FileList>();

	

	public String toJson() {
        String json = new JSONSerializer().include("isosalts", "fileList", "lot", "lot.lotAliases", "lot.saltForm.parent.parentAliases").exclude("*.class", "isosalts.saltForm", "fileList.lot")
        		.transform( new DateTransformer( "MM/dd/yyyy"), Date.class)
        		.serialize(this);
        System.out.println("fromMetaLotToJson");
        System.out.println(json);
        return json;
	}


	public static Metalot fromJsonToMetalot(String json) {
        System.out.println("fromJsonToMetalot");
        System.out.println(json);
        return new JSONDeserializer<Metalot>().use(null, Metalot.class)
        		.use( Date.class, new DateTransformer( "MM/dd/yyyy"))
        		.deserialize(json);
    }

	public static String toJsonArray(Collection<Metalot> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

	public static Collection<Metalot> fromJsonArrayToMetalots(String json) {
        return new JSONDeserializer<List<Metalot>>().use(null, ArrayList.class).use("values", Metalot.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public boolean isSkipParentDupeCheck() {
        return this.skipParentDupeCheck;
    }

	public void setSkipParentDupeCheck(boolean skipParentDupeCheck) {
        this.skipParentDupeCheck = skipParentDupeCheck;
    }

	public Lot getLot() {
        return this.lot;
    }

	public void setLot(Lot lot) {
        this.lot = lot;
    }

	public Set<IsoSalt> getIsosalts() {
        return this.isosalts;
    }

	public void setIsosalts(Set<IsoSalt> isosalts) {
        this.isosalts = isosalts;
    }

	public Set<FileList> getFileList() {
        return this.fileList;
    }

	public void setFileList(Set<FileList> fileList) {
        this.fileList = fileList;
    }
}
