package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.service.ErrorMessage;

import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;

@RooJavaBean
@RooToString
@RooJson
public class MetalotReturn {
	
	Metalot metalot;
	ArrayList<ErrorMessage> errors;
	
	@Transactional
	public String toJson() {
        String json = new JSONSerializer().include("errors","metalot.isosalts", "metalot.fileLists", "metalot.lot", "metalot.lot.lotAliases", "metalot.lot.saltForm.parent.parentAliases").exclude("*.class", "metalot.isosalts.saltForm", "metalot.fileList.lot")
        		.transform( new DateTransformer( "MM/dd/yyyy"), Date.class)
        		.serialize(this);
//        System.out.println("fromMetaLotToJson");
//        System.out.println(json);
        return json;
	}

}
