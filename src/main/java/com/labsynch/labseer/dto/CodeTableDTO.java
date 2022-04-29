package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.DDictValue;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentLabel;
import com.labsynch.labseer.utils.ExcludeNulls;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class CodeTableDTO {

	public CodeTableDTO() {
	}

	
	public CodeTableDTO(DDictValue dDictVal) {
		this.setId(dDictVal.getId());
		this.setCode(dDictVal.getShortName());
		this.setName(dDictVal.getLabelText());
		this.setIgnored(dDictVal.getIgnored());
		this.setDisplayOrder(dDictVal.getDisplayOrder());
		this.setDescription(dDictVal.getDescription());
		this.setComments(dDictVal.getComments());
		this.setCodeKind(dDictVal.getLsKind());
		this.setCodeType(dDictVal.getLsType());
	}
	
	public CodeTableDTO(Experiment experiment) {
		this.setId(experiment.getId());
		this.setCode(experiment.getCodeName());
		try{
			ExperimentLabel experimentLabel = ExperimentLabel.findExperimentPreferredName(experiment.getId()).getSingleResult();
			this.setName(experimentLabel.getLabelText());
		}catch (IncorrectResultSizeDataAccessException e){
			this.setName(experiment.getCodeName());
		}
		this.setIgnored(experiment.isIgnored());
	}

	private String code;

//	private String codeName;  //code and codeName should be the same. Prefer to use codeName but older client code may be using code.
//	//some classes use code, some use codeName

	private String name;
	
	private boolean ignored;
	
	private Integer displayOrder;
	
	private Long id;
	
	private String description;

	private String comments;
	
	private String codeType;
	
	private String codeKind;
	
	private String codeOrigin;


	
	public static List<CodeTableDTO> sortCodeTables(List<CodeTableDTO> list){
		List<CodeTableDTO> byDisplayOrder = new ArrayList<CodeTableDTO>();
		List<CodeTableDTO> byName = new ArrayList<CodeTableDTO>();
		for (CodeTableDTO e : list) {
			if (e.displayOrder!=null) byDisplayOrder.add(e);
			else byName.add(e);
		}
		byDisplayOrder = mergesort(byDisplayOrder, "displayOrder");
		byName = mergesort(byName, "name");
		byDisplayOrder.addAll(byName);
		return byDisplayOrder;
	}
	
	private static List<CodeTableDTO> mergesort(List<CodeTableDTO> list, String sortBy){
		if (list.size()<=1) return list;
		
		List<CodeTableDTO> left = new ArrayList<CodeTableDTO>();
		List<CodeTableDTO> right = new ArrayList<CodeTableDTO>();
		int middle = list.size()/2;
		for (int i=0; i<list.size(); i++) {
			if (i<middle) left.add(list.get(i));
			else right.add(list.get(i));
		}
		left = mergesort(left, sortBy);
		right = mergesort(right, sortBy);
		return merge(left, right, sortBy);
	}
	
	private static List<CodeTableDTO> merge(List<CodeTableDTO> left, List<CodeTableDTO> right, String sortBy){
		List<CodeTableDTO> result = new ArrayList<CodeTableDTO>();
		//loop through until both left and right are empty
		while (left.size()>0 || right.size()>0){
			if (left.size() > 0 && right.size() > 0) {			//if neither is empty, we must compare
				int comp = 0;									//initialize our comparison result
				if (sortBy.equals("displayOrder")) comp = left.get(0).compareByDisplayOrder(right.get(0));
				if (sortBy.equals("name")) comp = left.get(0).compareByName(right.get(0));
				
				if (comp<=0) {									//if the first of the left comes before (or equal) the first of the right, add first(left)
					result.add(left.remove(0));
				}else {
					result.add(right.remove(0));				//else use the first from the right
				}
				
			} else if (left.size() > 0){ 						//if right is empty and left isn't, add first(left)
				result.add(left.remove(0));
			}
			else if (right.size() > 0){							//if left is empty and right isn't, add first(right)
				result.add(right.remove(0));
			}
		}
		return result;
	}
	
	private int compareByDisplayOrder(CodeTableDTO that) {
		if (this.displayOrder < that.displayOrder) return -1;
		if (this.displayOrder > that.displayOrder) return 1;
		else return 0;
	}
	
	private int compareByName(CodeTableDTO that) {
		return this.name.compareTo(that.name);
	}
	
	public String toJson() {
        return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
	
	public static String toJsonArray(Collection<CodeTableDTO> collection) {
        return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
    }

	public static CodeTableDTO fromJsonToCodeTableDTO(String json) {
        return new JSONDeserializer<CodeTableDTO>()
        .use(null, CodeTableDTO.class).deserialize(json);
    }

	public static Collection<CodeTableDTO> fromJsonArrayToCoes(String json) {
        return new JSONDeserializer<List<CodeTableDTO>>()
        .use("values", CodeTableDTO.class).deserialize(json);
    }

	public String getCode() {
        return this.code;
    }

	public void setCode(String code) {
        this.code = code;
    }

	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public boolean isIgnored() {
        return this.ignored;
    }

	public void setIgnored(boolean ignored) {
        this.ignored = ignored;
    }

	public Integer getDisplayOrder() {
        return this.displayOrder;
    }

	public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public String getDescription() {
        return this.description;
    }

	public void setDescription(String description) {
        this.description = description;
    }

	public String getComments() {
        return this.comments;
    }

	public void setComments(String comments) {
        this.comments = comments;
    }

	public String getCodeType() {
        return this.codeType;
    }

	public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

	public String getCodeKind() {
        return this.codeKind;
    }

	public void setCodeKind(String codeKind) {
        this.codeKind = codeKind;
    }

	public String getCodeOrigin() {
        return this.codeOrigin;
    }

	public void setCodeOrigin(String codeOrigin) {
        this.codeOrigin = codeOrigin;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}


