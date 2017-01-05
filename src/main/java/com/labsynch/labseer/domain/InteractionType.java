package com.labsynch.labseer.domain;

import java.util.List;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(sequenceName = "INTERACTION_TYPE_PKSEQ", finders = { "findInteractionTypesByTypeNameEquals", "findInteractionTypesByTypeNameEqualsAndTypeVerbEquals" })
@RooJson
public class InteractionType {

    private static final Logger logger = LoggerFactory.getLogger(InteractionType.class);

    @NotNull
    @Column(unique = true)
    @Size(max = 128)
    private String typeName;

    @NotNull
    @Size(max = 128)
    private String typeVerb;

    @Transactional
    public static com.labsynch.labseer.domain.InteractionType getOrCreate(String typeVerb, String typeName) {
        InteractionType itxType = null;
        List<InteractionType> itxTypes = InteractionType.findInteractionTypesByTypeNameEqualsAndTypeVerbEquals(typeName, typeVerb).getResultList();
        if (itxTypes.size() == 0) {
            itxType = new InteractionType();
            itxType.setTypeVerb(typeVerb);
            itxType.setTypeName(typeName);
            itxType.persist();
        } else if (itxTypes.size() == 1) {
            itxType = itxTypes.get(0);
        } else if (itxTypes.size() > 1) {
            logger.error("ERROR: Found multiple interaction types for " + typeName);
            throw new RuntimeException("ERROR: Found multiple interaction types for " + typeName);
        }
        return itxType;
    }
}
