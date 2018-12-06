package com.labsynch.labseer.domain;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@RooJson
public class PreDef_CorpName {

	
	private static final Logger logger = LoggerFactory.getLogger(PreDef_CorpName.class);

    @org.hibernate.annotations.Index(name="PreDef_CorpNumber_IDX")
	private long corpNumber;
	
	@Size(max = 64)
    @org.hibernate.annotations.Index(name="PreDef_CorpName_IDX")
	private String corpName;

	@Size(max = 255)
	private String comment;

    @org.hibernate.annotations.Index(name="PreDef_Used_IDX")
	private Boolean used;

    @org.hibernate.annotations.Index(name="PreDef_Skip_IDX")
	private Boolean skip;
    
    public static PreDef_CorpName findNextCorpName(){
        EntityManager em = PreDef_CorpName.entityManager();
        TypedQuery<PreDef_CorpName> q = em.createQuery("SELECT o FROM PreDef_CorpName AS o WHERE o.skip = false AND o.used = false ORDER BY o asc", PreDef_CorpName.class);
        q.setMaxResults(1);
        return q.getSingleResult();
    	
    }

}
