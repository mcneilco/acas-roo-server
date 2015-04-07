package com.labsynch.labseer.utils;

import java.util.Comparator;

import com.labsynch.labseer.domain.ItxLsThingLsThing;

public class ItxLsThingLsThingComparator implements Comparator<ItxLsThingLsThing> {
	
	@Override
	public int compare(ItxLsThingLsThing o1, ItxLsThingLsThing o2){
		int o1Order = o1.grabItxOrder();
		int o2Order = o2.grabItxOrder();
		return o1Order - o2Order;
	}

}
