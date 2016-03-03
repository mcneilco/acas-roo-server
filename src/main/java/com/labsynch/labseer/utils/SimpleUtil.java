package com.labsynch.labseer.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class SimpleUtil {
	public static boolean isNumeric(String str) {
		for (char c : str.toCharArray()) {
			if (!Character.isDigit(c)) return false;
		}
		return true;
	}
	
	public static boolean isDecimalNumeric(String str) {
		for (char c : str.toCharArray()) {
			if (!Character.isDigit(c) && c != '-' && c!= '.') return false;
		}
		return true;
	}
	
	public static int countLines(String filename) throws IOException {
	    InputStream is = new BufferedInputStream(new FileInputStream(filename));
	    try {
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
	        boolean empty = true;
	        while ((readChars = is.read(c)) != -1) {
	            empty = false;
	            for (int i = 0; i < readChars; ++i) {
	                if (c[i] == '\n') {
	                    ++count;
	                }
	            }
	        }
	        return (count == 0 && !empty) ? 1 : count;
	    } finally {
	        is.close();
	    }
	}
	
	public static List<String> splitSearchString(String searchString){
		List<String> list = new ArrayList<String>();
		Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(searchString);
		while (m.find()){
			list.add(m.group(1).replace("\"",""));
		}
		return list;
	}
	
	public static Query addHqlInClause(EntityManager em, String queryString, String attributeName, List<String> matchStrings){
		Map<String, Collection<String>> sqlCurveIdMap = new HashMap<String, Collection<String>>();
    	List<String> allCodes = new ArrayList<String>();
    	allCodes.addAll(matchStrings);
    	int startIndex = 0;
    	while (startIndex < matchStrings.size()){
    		int endIndex;
    		if (startIndex+999 < matchStrings.size()) endIndex = startIndex+999;
    		else endIndex = matchStrings.size();
    		List<String> nextCodes = allCodes.subList(startIndex, endIndex);
    		String groupName = "strings"+startIndex;
    		String sqlClause = " "+attributeName+" IN (:"+groupName+")";
    		sqlCurveIdMap.put(sqlClause, nextCodes);
    		startIndex=endIndex;
    	}
    	int numClause = 1;
    	for (String sqlClause : sqlCurveIdMap.keySet()){
    		if (numClause == 1){
    			queryString = queryString + sqlClause;
    		}else{
    			queryString = queryString + " OR " + sqlClause;
    		}
    		numClause++;
    	}
    	queryString = queryString + " )";
    	Query q = em.createQuery(queryString);
		for (String sqlClause : sqlCurveIdMap.keySet()){
        	String groupName = sqlClause.split(":")[1].replace(")","");
        	q.setParameter(groupName, sqlCurveIdMap.get(sqlClause));
        }
    	return q;
	}
}
