

package com.labsynch.labseer.service;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.ContainerLabel;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class ContainerLabelJsonServiceTests {
	
	private static final Logger logger = LoggerFactory.getLogger(ContainerLabelJsonServiceTests.class);
	

	
	//@Test
	//@Transactional
	public void CreateContainerLabelTest_1(){
		Container container = Container.findContainer(264L);
		ContainerLabel cl = new ContainerLabel();
		cl.setContainer(container);
		cl.setLabelText("just a test");
		cl.setLsType("name");
		cl.setLsKind("container name");
		cl.setPreferred(true);
		cl.setRecordedBy("me");
		cl.setRecordedDate(new Date());
		logger.info(cl.toJson());
		cl.persist();
		
		
//        Assert.assertNotNull("Find method for 'Protocol' illegally returned null for id '" + id + "'", id);
//        Assert.assertEquals("Find method for 'Author' returned the incorrect identifier", expectedId, id);		
	}
	
	@Test
	//@Transactional
	public void CreateContainerLabelTest_2(){
		String json = "[{\"container\":{\"id\":233735,\"version\":0},\"recordedBy\":\"smeyer\",\"labelText\":\"JH test 46_1\",\"lsType\":\"name\",\"lsKind\":\"container name\",\"lsTransaction\":13726,\"preferred\":true,\"imageFile\":null,\"labelTypeAndKind\":null,\"physicallyLabeled\":false,\"ignored\":false,\"recordedDate\":1384473207000,\"modifiedDate\":null,\"version\":null},{\"container\":{\"id\":233736,\"version\":0},\"recordedBy\":\"smeyer\",\"labelText\":\"JH test 46_2\",\"lsType\":\"name\",\"lsKind\":\"container name\",\"lsTransaction\":13726,\"preferred\":true,\"imageFile\":null,\"labelTypeAndKind\":null,\"physicallyLabeled\":false,\"ignored\":false,\"recordedDate\":1384473207000,\"modifiedDate\":null,\"version\":null},{\"container\":{\"id\":233737,\"version\":0},\"recordedBy\":\"smeyer\",\"labelText\":\"JH test 46_3\",\"lsType\":\"name\",\"lsKind\":\"container name\",\"lsTransaction\":13726,\"preferred\":true,\"imageFile\":null,\"labelTypeAndKind\":null,\"physicallyLabeled\":false,\"ignored\":false,\"recordedDate\":1384473207000,\"modifiedDate\":null,\"version\":null}]";
		StringReader sr = new StringReader(json);
		BufferedReader br = new BufferedReader(sr);
		
		for( Container container : Container.fromJsonArrayToContainers(br)){
			logger.warn(container.toJson());
			container.persist();
		}
	}


}
