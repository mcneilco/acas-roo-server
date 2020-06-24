package com.labsynch.labseer.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.domain.AuthorRole;
import com.labsynch.labseer.domain.LsRole;
import com.labsynch.labseer.dto.AnalysisGroupValueBaseDTO;
import com.labsynch.labseer.dto.BatchCodeDTO;
import com.labsynch.labseer.dto.ExperimentFilterDTO;
import com.labsynch.labseer.dto.JSTreeNodeDTO;
import com.labsynch.labseer.service.ExperimentService;

import flexjson.JSONDeserializer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class ReadPropsTest {
	
	private static final Logger logger = LoggerFactory.getLogger(ReadPropsTest.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Autowired
	private ExperimentService experimentService;
	
    @Autowired
    private transient MailSender mailSender;

    private transient SimpleMailMessage simpleMailMessage;
	
	
	@Test
	public void Read_Test_2() {
		logger.info("here is the current fetch size: " + propertiesUtilService.getFetchSize());
	}
	
	@Test
	public void Read_Test_3() {
		logger.info("here is the current batch size: " + propertiesUtilService.getBatchSize());
	}
	
	//@Test
	public void Author_Test4() {
		Author auth = new Author();
		auth.setFirstName("guytest");
		auth.setLastName("oshiro");
		auth.setUserName("goshirotest");
		auth.setPassword("apple");
		auth.setEmailAddress("guy@test.com");
		auth.persist();
		logger.info(auth.toJson());
		
	}
	
	//@Test
	public void Author_Test5() {
        SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo("guy@mcneilco.com");
		mail.setSubject("User Activaton");
		
		mail.setText("Hi -- testing the email send function");
		if(propertiesUtilService.getEmailFromAddress() != null) mail.setFrom(propertiesUtilService.getEmailFromAddress());

        mailSender.send(mail);
		
	}
	
	//@Test
	public void AuthorRole_Test6() {
		List<Author> authors = Author.findAllAuthors();
		List<LsRole> lsRoles = LsRole.findAllLsRoles();
		if (authors.size() > 0 && lsRoles.size() > 0){
			Author author = Author.findAllAuthors().get(0);
			LsRole lsRole = LsRole.findAllLsRoles().get(0);
			AuthorRole authorRole = new AuthorRole();
			authorRole.setUserEntry(author);
			authorRole.setRoleEntry(lsRole);
			authorRole.persist();
			logger.info("persisted the authorRole: " + authorRole.toJson());
		}
	}
	
	//@Test
	public void basic_Test7() {
		
		String valueType = "codeValue";
		String valueKind = "batch code";		
		String stateType = "data";
		String stateKind = "Dose Response";
		String experimentCode = "EXPT-00000037";
//		List<AnalysisGroupValueBaseDTO> agvs = AnalysisGroupValue.findAnalysisGroupValueByLsTypeKindAndExptCodeAll(lsType, lsKind, experimentCode).getResultList();
		List<AnalysisGroupValueBaseDTO> agvs = AnalysisGroupValue.findAnalysisGroupValueExptCodeAndStateTypeKindAndValueTypeKind(experimentCode, stateType, stateKind, valueType, valueKind).getResultList();
		logger.info("number of agvs found: " + agvs.size());
		for (AnalysisGroupValueBaseDTO ag : agvs){
			logger.info(ag.toJson());
		}
	}
	
	//@Test
	@Transactional
	public void getExperiments_test8(){
		String json = "[{\"batchCode\":\"GENE-000002\"},{\"batchCode\":\"GENE-000003\"},{\"batchCode\":\"GENE-000011\"}]";
        Collection<BatchCodeDTO> batchCodes = BatchCodeDTO.fromJsonArrayToBatchCoes(json);
		Collection<String> codeValues = new HashSet<String>();
        for (BatchCodeDTO batchCode : batchCodes){
        	codeValues.add(batchCode.getBatchCode());
        	logger.info("current batchCode: " + batchCode.getBatchCode());
        }
        
        Collection<JSTreeNodeDTO> results = experimentService.getExperimentNodes(codeValues);
        logger.info(JSTreeNodeDTO.toJsonArray(results));
	}
	
	@Test
	@Transactional
	public void getExperimentFilters_test9(){
		String json = "[\"EXPT-00000003\"]";
        Collection<String> experimentCodes = new JSONDeserializer<List<String>>().use(null, ArrayList.class).use("values", String.class).deserialize(json);
        Collection<ExperimentFilterDTO> results = experimentService.getExperimentFilters(experimentCodes);
        logger.info(ExperimentFilterDTO.toJsonArray(results));

		
	}
	
}
