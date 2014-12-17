package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.DDictValue;

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
}


