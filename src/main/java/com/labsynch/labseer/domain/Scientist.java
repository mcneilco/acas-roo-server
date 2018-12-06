package com.labsynch.labseer.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findScientistsByCodeEquals", "findScientistsByCodeLike" })
public class Scientist {

	private static final Logger logger = LoggerFactory.getLogger(Scientist.class);

    private String code;

    private String name;

    private Boolean isChemist;

    private Boolean isAdmin;

    private Boolean ignore;

	public static Scientist checkValidUser(String modifiedByUser) {
		List<Scientist> chemists = Scientist.findScientistsByCodeEquals(modifiedByUser).getResultList();
		Scientist validUser;		
		if (chemists.size() == 0){
			String errorMessage = "ERROR: Unable to find modifedByUser: " + modifiedByUser;
			logger.error(errorMessage);
			throw new RuntimeException(errorMessage);
		} else if (chemists.size() > 1){
			String errorMessage = "ERROR: Found multiple modifedByUser(s): " + modifiedByUser;
			logger.error("ERROR: Found multiple modifedByUser(s): " + modifiedByUser);				
			throw new RuntimeException(errorMessage);
		} else {
			validUser = chemists.get(0);
		}
		return validUser;
	}
	
	public static TypedQuery<Scientist> findScientistsBySearchTerm(String searchTerm) {
        if (searchTerm == null || searchTerm.length() == 0) throw new IllegalArgumentException("The searchTerm argument is required");
        searchTerm = searchTerm.replace('*', '%');
        if (searchTerm.charAt(0) != '%') {
        	searchTerm = "%" + searchTerm;
        }
        if (searchTerm.charAt(searchTerm.length() - 1) != '%') {
        	searchTerm = searchTerm + "%";
        }
        EntityManager em = Scientist.entityManager();
        TypedQuery<Scientist> q = em.createQuery("SELECT DISTINCT o FROM Scientist AS o WHERE (LOWER(o.code) LIKE LOWER(:searchTerm) OR LOWER(o.name) LIKE LOWER(:searchTerm))", Scientist.class);
        q.setParameter("searchTerm", searchTerm);
        return q;
    }
	
	public static List<Scientist> findScientistsWithLots() {
        EntityManager em = Scientist.entityManager();
        Query q = em.createNativeQuery("SELECT DISTINCT o.* FROM scientist o JOIN lot l ON l.chemist = o.id ", Scientist.class);
        return  q.getResultList();
    }

	
}
