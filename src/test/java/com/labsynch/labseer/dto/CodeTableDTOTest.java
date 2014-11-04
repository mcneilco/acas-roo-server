package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class CodeTableDTOTest {
	
	private static final Logger logger = LoggerFactory.getLogger(CodeTableDTOTest.class);

	
	public void mergesortTest() {
		List<CodeTableDTO> unsorted = new ArrayList<CodeTableDTO>();
		List<CodeTableDTO> sortedCheck = new ArrayList<CodeTableDTO>();
		CodeTableDTO one = new CodeTableDTO();
		one.setDisplayOrder(1);
		one.setName("one");
		CodeTableDTO two = new CodeTableDTO();
		two.setDisplayOrder(2);
		two.setName("two");
		CodeTableDTO three = new CodeTableDTO();
		three.setDisplayOrder(3);
		three.setName("three");
		CodeTableDTO a = new CodeTableDTO();
		a.setName("a");
		CodeTableDTO b = new CodeTableDTO();
		a.setName("b");
		CodeTableDTO c = new CodeTableDTO();
		a.setName("c");
		
		unsorted.add(c);
		unsorted.add(three);
		unsorted.add(a);
		unsorted.add(one);
		unsorted.add(two);
		unsorted.add(b);
		logger.info(unsorted.toString());
		
		sortedCheck.add(one);
		sortedCheck.add(two);
		sortedCheck.add(three);
		sortedCheck.add(a);
		sortedCheck.add(b);
		sortedCheck.add(c);
		
		List<CodeTableDTO> mergeSorted = CodeTableDTO.sortCodeTables(unsorted);
		logger.info(mergeSorted.toString());
		for (int i=0; i<sortedCheck.size(); i++){
			Assert.assertEquals(sortedCheck.get(i), mergeSorted.get(i));
		}
		
		
	}
}
