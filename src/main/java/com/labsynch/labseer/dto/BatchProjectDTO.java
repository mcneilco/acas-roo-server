package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.utils.SimpleUtil;

@RooJavaBean
@RooToString
@RooJson
public class BatchProjectDTO {
	
	private static final Logger logger = LoggerFactory.getLogger(BatchProjectDTO.class);

    private String requestName;
    
    private String projectCode;
    
    public BatchProjectDTO(){
    	
    }
    
    public BatchProjectDTO(String requestName, String projectCode){
    	this.requestName = requestName;
    	this.projectCode = projectCode;
    }
    
    public static Collection<BatchProjectDTO> getBatchProjects(Collection<BatchProjectDTO> requestDTOs){
    	EntityManager em = Lot.entityManager();
    	List<String> batchCodes = new ArrayList<String>();
    	for (BatchProjectDTO requestDTO : requestDTOs){
    		batchCodes.add(requestDTO.getRequestName());
    	}
    	String queryString = "Select new com.labsynch.labseer.dto.BatchProjectDTO(lot.corpName, project.code) "
    			+ "FROM Lot lot "
    			+ "LEFT OUTER JOIN lot.project as project "
    			+ "WHERE ";
    	Collection<Query> queries = SimpleUtil.splitHqlInClause(em, queryString, "lot.corpName", batchCodes);
    	Collection<BatchProjectDTO> results = new ArrayList<BatchProjectDTO>();
		logger.debug("Querying for "+batchCodes.size()+" lot corpnames");
		for (Query q : queries){
			if (logger.isDebugEnabled()) logger.debug(q.unwrap(org.hibernate.Query.class).getQueryString());
			results.addAll(q.getResultList());
		}
		for (BatchProjectDTO result : results){
			if (result.getProjectCode() == null){
				result.setProjectCode("");
			}
		}
		return results;
    }
    
   
}
