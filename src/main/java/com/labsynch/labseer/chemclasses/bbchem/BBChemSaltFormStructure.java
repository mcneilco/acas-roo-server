package com.labsynch.labseer.chemclasses.bbchem;

import java.util.BitSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.labsynch.labseer.domain.AbstractBBChemStructure;
import com.labsynch.labseer.utils.SimpleUtil;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(sequenceName = "BBCHEM_STRUCTURE_PKSEQ", finders = { "findBBChemSaltFormStructuresByRegEquals", "findBBChemSaltFormStructuresByPreRegEquals"})
public class BBChemSaltFormStructure  extends AbstractBBChemStructure {

    public static List<BBChemSaltFormStructure> findBBChemSaltFormStructuresBySubstructure(BitSet substructure, int maxResults) {
        if (substructure == null || substructure.length() == 0) throw new IllegalArgumentException("The substructure argument is required");
        EntityManager em = BBChemSaltFormStructure.entityManager();
        String fingerprintString = SimpleUtil.bitSetToString(substructure);
        Query q = em.createNativeQuery("SELECT o.* FROM bbchem_salt_form_structure AS o WHERE (o.substructure \\& CAST(:fingerprintString AS bit(2048))) = CAST(:fingerprintString AS bit(2048)) ", BBChemSaltFormStructure.class);
        q.setParameter("fingerprintString", fingerprintString);
       if(maxResults > -1) {
			q.setMaxResults(maxResults);
		}
        return q.getResultList();
    }

}
