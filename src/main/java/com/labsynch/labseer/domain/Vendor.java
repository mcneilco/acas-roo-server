package com.labsynch.labseer.domain;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findVendorsByCodeLike", "findVendorsByCodeEquals", "findVendorsByNameEquals", "findVendorsByNameLike" })
public class Vendor {

    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String code;

    
    public static TypedQuery<Vendor> findVendorsBySearchTerm(String searchTerm) {
        if (searchTerm == null || searchTerm.length() == 0) throw new IllegalArgumentException("The searchTerm argument is required");
        searchTerm = searchTerm.replace('*', '%');
        if (searchTerm.charAt(0) != '%') {
        	searchTerm = "%" + searchTerm;
        }
        if (searchTerm.charAt(searchTerm.length() - 1) != '%') {
        	searchTerm = searchTerm + "%";
        }
        EntityManager em = Vendor.entityManager();
        TypedQuery<Vendor> q = em.createQuery("SELECT DISTINCT o FROM Vendor AS o WHERE (LOWER(o.code) LIKE LOWER(:searchTerm) OR LOWER(o.name) LIKE LOWER(:searchTerm))", Vendor.class);
        q.setParameter("searchTerm", searchTerm);
        return q;
    }
}
