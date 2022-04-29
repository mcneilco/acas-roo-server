package com.labsynch.labseer.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import com.labsynch.labseer.domain.SubjectState;

import org.apache.commons.io.IOUtils;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class ReadResourceFileTest {
	
	private static final Logger logger = LoggerFactory.getLogger(ReadResourceFileTest.class);
	
	//@Test
	@Transactional
	public void Read_Test_1() throws IOException{
		logger.debug("run initial setup");
		String testFileName = "1500subjectstates.json";
		InputStream is = ReadResourceFileTest.class.getClassLoader().getResourceAsStream(testFileName);
//	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//	    logger.debug(IOUtils.toString(is));
	    String json = IOUtils.toString(is, "UTF-8");
	    
//	    String json = "[{\"comments\":null,\"id\":null,\"ignored\":false,\"lsKind\":\"test compound treatment\",\"lsTransaction\":119,\"lsType\":\"data\",\"lsTypeAndKind\":\"data_test compound treatment\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"\",\"recordedDate\":1376612488000,\"subject\":{\"codeName\":\"SUBJ-00000316\",\"id\":534,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":119,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"\",\"recordedDate\":1376612488000,\"treatmentGroup\":{\"analysisGroup\":{\"codeName\":\"AG-00000238\",\"experiment\":{\"codeName\":\"EXPT-00000032\",\"id\":525,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":119,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"protocol\":{\"codeName\":\"PROT-00000004\",\"id\":48,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":11,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"\",\"recordedDate\":1376546949000,\"shortDescription\":\"protocol created by generic data parser\",\"version\":1},\"recordedBy\":\"\",\"recordedDate\":1376612486000,\"shortDescription\":\"experiment created by generic data parser\",\"version\":1},\"id\":526,\"ignored\":false,\"lsKind\":\"Dose Response\",\"lsTransaction\":119,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_Dose Response\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"\",\"recordedDate\":1376612488000,\"version\":0},\"codeName\":\"TG-00000202\",\"id\":533,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":119,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"\",\"recordedDate\":1376612488000,\"version\":0},\"version\":0},\"version\":0},{\"comments\":null,\"id\":null,\"ignored\":false,\"lsKind\":\"results\",\"lsTransaction\":119,\"lsType\":\"data\",\"lsTypeAndKind\":\"data_results\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"\",\"recordedDate\":1376612488000,\"subject\":{\"codeName\":\"SUBJ-00000316\",\"id\":534,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":119,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"\",\"recordedDate\":1376612488000,\"treatmentGroup\":{\"analysisGroup\":{\"codeName\":\"AG-00000238\",\"experiment\":{\"codeName\":\"EXPT-00000032\",\"id\":525,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":119,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"protocol\":{\"codeName\":\"PROT-00000004\",\"id\":48,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":11,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"\",\"recordedDate\":1376546949000,\"shortDescription\":\"protocol created by generic data parser\",\"version\":1},\"recordedBy\":\"\",\"recordedDate\":1376612486000,\"shortDescription\":\"experiment created by generic data parser\",\"version\":1},\"id\":526,\"ignored\":false,\"lsKind\":\"Dose Response\",\"lsTransaction\":119,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_Dose Response\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"\",\"recordedDate\":1376612488000,\"version\":0},\"codeName\":\"TG-00000202\",\"id\":533,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":119,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"\",\"recordedDate\":1376612488000,\"version\":0},\"version\":0},\"version\":0}]";
	    
	    Collection<SubjectState> subjectStates = SubjectState.fromJsonArrayToSubjectStates(json);
	    logger.debug("number of subject states: " + subjectStates.size());
	    
	    long startTime = System.currentTimeMillis();
	    
	    int batchSize = 100;
	    int i = 0;
	    for (SubjectState s : subjectStates){
		    logger.debug(s.toJson());
		    s.persist();
		    if ( i % batchSize == 0 ) { // same as the JDBC batch size
		    	s.flush();
		    	s.clear();
		    }
		    i++;
	    }
	    
	    long endTime = System.currentTimeMillis();
	    long runTime = endTime - startTime;
	    logger.debug("total run time: " + runTime);  //20,550 for 250; 114252 for 1500
	    
//	    Subject subject = Subject.findSubject(534L);
//	    Collection<SubjectState> subjectStates = SubjectState.findSubjectStatesBySubject(subject).getResultList();
//	    logger.debug("Number of all subject States is: " + SubjectState.countSubjectStates());
//	    logger.debug("Number of only subject States is: " + subjectStates.size());
//	    logger.debug(SubjectState.toJsonArray(subjectStates));


	}


}
