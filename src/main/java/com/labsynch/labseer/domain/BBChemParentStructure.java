package com.labsynch.labseer.domain;

import java.util.BitSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.labsynch.labseer.utils.SimpleUtil;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(sequenceName = "BBCHEM_STRUCTURE_PKSEQ", finders = { "findBBChemParentStructuresByRegEquals", "findBBChemParentStructuresByPreRegEquals"})
public class BBChemParentStructure extends AbstractBBChemStructure {

    public static List<BBChemParentStructure> findBBChemParentStructuresBySubstructure(BitSet substructure, int maxResults) {
        if (substructure == null || substructure.length() == 0) throw new IllegalArgumentException("The substructure argument is required");
        EntityManager em = BBChemParentStructure.entityManager();
        String fingerprintString = SimpleUtil.bitSetToString(substructure);
        Query q = em.createNativeQuery("SELECT o.* FROM bbchem_parent_structure AS o WHERE (o.substructure \\& B'"+fingerprintString+"') = B'"+fingerprintString+"' ", BBChemParentStructure.class);
        if(maxResults > -1) {
			q.setMaxResults(maxResults);
		}
        return q.getResultList();
    }
}
