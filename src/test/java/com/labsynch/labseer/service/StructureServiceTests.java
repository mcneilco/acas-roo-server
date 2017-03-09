

package com.labsynch.labseer.service;

import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.Structure;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class StructureServiceTests {

	private static final Logger logger = LoggerFactory.getLogger(StructureServiceTests.class);

	@Autowired
	private StructureService structureService;

	@Test
	@Transactional
	public void substructureSearch(){
		String queryMol= "\n  Mrv1641110051619032D          \n\n  5  5  0  0  0  0            999 V2000\n   -0.0446    0.6125    0.0000 O   0  0  0  0  0  0  0  0  0  0  0  0\n   -0.7121    0.1274    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n   -0.4572   -0.6572    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n    0.3679   -0.6572    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n    0.6228    0.1274    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n  2  3  1  0  0  0  0\n  3  4  1  0  0  0  0\n  4  5  1  0  0  0  0\n  1  2  1  0  0  0  0\n  1  5  1  0  0  0  0\nM  END\n";
		Collection<Structure> searchResults = structureService.searchStructures(queryMol, "SUBSTRUCTURE", 10, null);
		Assert.assertFalse(searchResults.isEmpty());
		logger.debug(Structure.toJsonArray(searchResults));
		
		queryMol = "\n  MJ160418                      \n\n 20 23  0  0  0  0  0  0  0  0999 V2000\n   -0.9620   -1.3787    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n   -1.2169   -2.1634    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n   -2.0419   -2.1634    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n   -2.2968   -1.3787    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n   -1.6294   -0.8938    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n   -1.6294   -0.0688    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n   -0.9620    0.4160    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n   -1.2169    1.2006    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n   -2.0419    1.2006    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n   -2.2968    0.4160    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n   -0.1773    0.1611    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n    0.4900    0.6460    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n    1.1574    0.1611    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n    0.9025   -0.6235    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n    0.0775   -0.6235    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n    0.4900    1.4710    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n   -0.1773    1.9559    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n    0.0775    2.7405    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n    0.9025    2.7405    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n    1.1574    1.9559    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n 18 19  1  0  0  0  0\n 17 18  1  0  0  0  0\n 16 17  1  0  0  0  0\n 19 20  1  0  0  0  0\n 16 20  1  0  0  0  0\n 12 16  1  0  0  0  0\n 11 12  1  0  0  0  0\n  7 11  1  0  0  0  0\n  7  8  1  0  0  0  0\n  8  9  1  0  0  0  0\n  9 10  1  0  0  0  0\n  6  7  1  0  0  0  0\n  6 10  1  0  0  0  0\n  5  6  1  0  0  0  0\n  4  5  1  0  0  0  0\n  3  4  1  0  0  0  0\n  2  3  1  0  0  0  0\n  1  5  1  0  0  0  0\n  1  2  1  0  0  0  0\n 11 15  1  0  0  0  0\n 14 15  1  0  0  0  0\n 12 13  1  0  0  0  0\n 13 14  1  0  0  0  0\nM  END\n";
		searchResults = structureService.searchStructures(queryMol, "SUBSTRUCTURE", 10, null);
		Assert.assertTrue(searchResults.isEmpty());
	}
	
	@Test
	@Transactional
	public void similaritySearch(){
		//same - pass
		String sameStructure = "\n  Mrv1641110051619032D          \n\n  5  5  0  0  0  0            999 V2000\n   -0.0446    0.6125    0.0000 O   0  0  0  0  0  0  0  0  0  0  0  0\n   -0.7121    0.1274    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n   -0.4572   -0.6572    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n    0.3679   -0.6572    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n    0.6228    0.1274    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n  2  3  1  0  0  0  0\n  3  4  1  0  0  0  0\n  4  5  1  0  0  0  0\n  1  2  1  0  0  0  0\n  1  5  1  0  0  0  0\nM  END\n";
		Float similarity = new Float("0.90");
		Collection<Structure> searchResults = structureService.searchStructures(sameStructure, "SIMILARITY", null, similarity);
		Assert.assertFalse(searchResults.isEmpty());
		logger.debug(Structure.toJsonArray(searchResults));
		//similar structure - high similarity threshold - fail
		String similarStructure = "\n  MJ160418                      \n\n  5  4  0  0  0  0  0  0  0  0999 V2000\n    0.1116    1.9740    0.0000 O   0  0  0  0  0  0  0  0  0  0  0  0\n   -0.5557    1.4890    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n   -0.3008    0.7044    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n    0.5241    0.7044    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n    0.8596    1.4581    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n  2  3  1  0  0  0  0\n  3  4  1  0  0  0  0\n  1  2  1  0  0  0  0\n  4  5  1  0  0  0  0\nM  END\n";
		similarity = new Float("0.90");
		searchResults = structureService.searchStructures(similarStructure, "SIMILARITY", null, similarity);
		Assert.assertTrue(searchResults.isEmpty());
		//lower threshold - pass
		similarity = new Float("0.70");
		searchResults = structureService.searchStructures(similarStructure, "SIMILARITY", null, similarity);
		Assert.assertFalse(searchResults.isEmpty());
		logger.debug(Structure.toJsonArray(searchResults));
	}
	
	@Test
	@Transactional
	public void exactSearch(){
		String sameStructure = "\n  Mrv1641110051619032D          \n\n  5  5  0  0  0  0            999 V2000\n   -0.0446    0.6125    0.0000 O   0  0  0  0  0  0  0  0  0  0  0  0\n   -0.7121    0.1274    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n   -0.4572   -0.6572    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n    0.3679   -0.6572    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n    0.6228    0.1274    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n  2  3  1  0  0  0  0\n  3  4  1  0  0  0  0\n  4  5  1  0  0  0  0\n  1  2  1  0  0  0  0\n  1  5  1  0  0  0  0\nM  END\n";
		Collection<Structure> searchResults = structureService.searchStructures(sameStructure, "EXACT", 10, null);
		Assert.assertFalse(searchResults.isEmpty());
		logger.debug(Structure.toJsonArray(searchResults));
	}


}
