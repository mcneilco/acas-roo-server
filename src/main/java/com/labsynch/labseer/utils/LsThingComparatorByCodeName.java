package com.labsynch.labseer.utils;

import java.util.Comparator;

import com.labsynch.labseer.domain.LsThing;

public class LsThingComparatorByCodeName implements Comparator<LsThing> {
	
	@Override
	public int compare(LsThing o1, LsThing o2){
		String o1CodeName = o1.getCodeName();
		String o2CodeName = o2.getCodeName();
		return o1CodeName.compareTo(o2CodeName);
	}

}
