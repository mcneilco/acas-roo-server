package com.labsynch.labseer.utils;

import java.lang.reflect.Type;
import java.math.BigDecimal;

import flexjson.ObjectBinder;
import flexjson.ObjectFactory;

public class CustomBigDecimalFactory implements ObjectFactory {
	public Object instantiate(ObjectBinder context, Object value, Type targetType, @SuppressWarnings("rawtypes") Class targetClass) {
		return new BigDecimal( value.toString() );
	}
}
