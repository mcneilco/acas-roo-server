package com.labsynch.labseer.domain;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findProtocolLabelsByProtocol", "findProtocolLabelsByLsTransactionEquals", "findProtocolLabelsByLabelTextEqualsAndIgnoredNot", "findProtocolLabelsByLabelTextLike" })
public class ProtocolLabel extends AbstractLabel {
	
	private static final Logger logger = LoggerFactory.getLogger(ProtocolLabel.class);


    @NotNull
    @ManyToOne
    @JoinColumn(name = "protocol_id")
    private Protocol protocol;

    public ProtocolLabel(com.labsynch.labseer.domain.ProtocolLabel protocolLabel) {
        super.setLsType(protocolLabel.getLsType());
        super.setLsKind(protocolLabel.getLsKind());
        super.setLsTypeAndKind(protocolLabel.getLsType() + "_" + protocolLabel.getLsKind());
        super.setLabelText(protocolLabel.getLabelText());
        super.setPreferred(protocolLabel.isPreferred());
        super.setLsTransaction(protocolLabel.getLsTransaction());
        super.setRecordedBy(protocolLabel.getRecordedBy());
        super.setRecordedDate(protocolLabel.getRecordedDate());
        super.setPhysicallyLabled(protocolLabel.isPhysicallyLabled());
    }

    public static com.labsynch.labseer.domain.ProtocolLabel update(com.labsynch.labseer.domain.ProtocolLabel protocolLabel) {
        ProtocolLabel updatedProtocolLabel = ProtocolLabel.findProtocolLabel(protocolLabel.getId());
        updatedProtocolLabel.setLsType(protocolLabel.getLsType());
        updatedProtocolLabel.setLsKind(protocolLabel.getLsKind());
        updatedProtocolLabel.setLsTypeAndKind(protocolLabel.getLsType() + "_" + protocolLabel.getLsKind());
        updatedProtocolLabel.setLabelText(protocolLabel.getLabelText());
        updatedProtocolLabel.setPreferred(protocolLabel.isPreferred());
        updatedProtocolLabel.setLsTransaction(protocolLabel.getLsTransaction());
        updatedProtocolLabel.setRecordedBy(protocolLabel.getRecordedBy());
        updatedProtocolLabel.setRecordedDate(protocolLabel.getRecordedDate());
        updatedProtocolLabel.setModifiedDate(new Date());
        updatedProtocolLabel.setPhysicallyLabled(protocolLabel.isPhysicallyLabled());
        updatedProtocolLabel.setIgnored(protocolLabel.isIgnored());
        updatedProtocolLabel.merge();
        return updatedProtocolLabel;
    }
    
    public static ProtocolLabel findProtocolLabel(Long id) {
        if (id == null) return null;
        return entityManager().find(ProtocolLabel.class, id);
    }
    
	public static TypedQuery<ProtocolLabel> findProtocolPreferredName(Long protocolId) {
		
		if (protocolId == null || protocolId == 0) {
			return null;
		} 

		String labelType = "name";
		String labelKind = "protocol name";
		boolean preferred = true;
		boolean ignored = true;

		TypedQuery<ProtocolLabel> q = findProtocolPreferredName(protocolId, labelType, labelKind,  preferred,  ignored);
		return q;
	}
	
	private static TypedQuery<ProtocolLabel> findProtocolPreferredName(
			Long protocolId, String labelType, String labelKind,
			boolean preferred, boolean ignored) {
		
		if (protocolId == null || protocolId == 0) throw new IllegalArgumentException("The protocolId argument is required");
		if (labelType == null || labelType.length() == 0) throw new IllegalArgumentException("The labelType argument is required");
		if (labelKind == null || labelKind.length() == 0) throw new IllegalArgumentException("The labelKind argument is required");
		
		EntityManager em = ProtocolLabel.entityManager();
		String query = "SELECT o FROM ProtocolLabel AS o WHERE o.lsType = :labelType  "
				+ "AND o.lsKind = :labelKind "
				+ "AND o.protocol.id = :protocolId "
				+ "AND o.preferred = :preferred AND o.ignored IS NOT :ignored";
		logger.debug("sql query " + query);
		TypedQuery<ProtocolLabel> q = em.createQuery(query, ProtocolLabel.class);
		q.setParameter("protocolId", protocolId);
		q.setParameter("labelType", labelType);
		q.setParameter("labelKind", labelKind);
		q.setParameter("preferred", preferred);
		q.setParameter("ignored", ignored);
		return q;
	}

    
    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public static ProtocolLabel fromJsonToProtocolLabel(String json) {
        return new JSONDeserializer<ProtocolLabel>().use(null, ProtocolLabel.class).deserialize(json);
    }
    
    public static String toJsonArray(Collection<ProtocolLabel> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static Collection<ProtocolLabel> fromJsonArrayToProtocolLabels(String json) {
        return new JSONDeserializer<List<ProtocolLabel>>().use(null, ArrayList.class).use("values", ProtocolLabel.class).deserialize(json);
    }
    
    public static Collection<ProtocolLabel> fromJsonArrayToProtocolLabels(Reader json) {
        return new JSONDeserializer<List<ProtocolLabel>>().use(null, ArrayList.class).use("values", ProtocolLabel.class).deserialize(json);
    }
    

    public static TypedQuery<com.labsynch.labseer.domain.ProtocolLabel> findProtocolNames(Protocol protocol) {
        String labelType = "name";
        String labelKind = "protocol name";
        boolean preferred = true;
        if (protocol == null) throw new IllegalArgumentException("The protocol argument is required");
        EntityManager em = ProtocolLabel.entityManager();
        TypedQuery<ProtocolLabel> q = em.createQuery("SELECT o FROM ProtocolLabel AS o WHERE o.protocol = :protocol " + "AND o.lsType = :labelType " + "AND o.lsKind = :labelKind " + "AND o.preferred = :preferred", ProtocolLabel.class);
        q.setParameter("protocol", protocol);
        q.setParameter("labelType", labelType);
        q.setParameter("labelKind", labelKind);
        q.setParameter("preferred", preferred);
        return q;
    }

    public static TypedQuery<com.labsynch.labseer.domain.ProtocolLabel> findProtocolNames(Protocol protocol, boolean ignored) {
        String labelType = "name";
        String labelKind = "protocol name";
        boolean preferred = true;
        if (protocol == null) throw new IllegalArgumentException("The protocol argument is required");
        EntityManager em = ProtocolLabel.entityManager();
        TypedQuery<ProtocolLabel> q = em.createQuery("SELECT o FROM ProtocolLabel AS o WHERE o.protocol = :protocol " + "AND o.lsType = :labelType " + "AND o.lsKind = :labelKind " + "AND o.preferred = :preferred AND o.ignored IS NOT :ignored", ProtocolLabel.class);
        q.setParameter("protocol", protocol);
        q.setParameter("labelType", labelType);
        q.setParameter("labelKind", labelKind);
        q.setParameter("preferred", preferred);
        q.setParameter("ignored", ignored);
        return q;
    }

    public static TypedQuery<com.labsynch.labseer.domain.ProtocolLabel> findProtocolLabelsByName(String labelText) {
        String labelType = "name";
        String labelKind = "protocol name";
        boolean preferred = true;
        boolean ignored = true;
        TypedQuery<ProtocolLabel> q = findProtocolLabelsByName(labelText, labelType, labelKind, preferred, ignored);
        return q;
    }

    public static TypedQuery<com.labsynch.labseer.domain.ProtocolLabel> findProtocolLabelsByName(String labelText, String labelType, String labelKind, boolean preferred, boolean ignored) {
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        if (labelType == null || labelType.length() == 0) throw new IllegalArgumentException("The labelType argument is required");
        if (labelKind == null || labelKind.length() == 0) throw new IllegalArgumentException("The labelKind argument is required");
        EntityManager em = ProtocolLabel.entityManager();
        TypedQuery<ProtocolLabel> q = em.createQuery("SELECT o FROM ProtocolLabel AS o WHERE o.lsType = :labelType  " + "AND o.lsKind = :labelKind " + "AND o.labelText = :labelText " + "AND o.preferred = :preferred AND o.ignored IS NOT :ignored", ProtocolLabel.class);
        q.setParameter("labelText", labelText);
        q.setParameter("labelType", labelType);
        q.setParameter("labelKind", labelKind);
        q.setParameter("preferred", preferred);
        q.setParameter("ignored", ignored);
        return q;
    }
    
    public static TypedQuery<com.labsynch.labseer.domain.ProtocolLabel> findProtocolLabelsByNameLike(String labelText) {
        String labelType = "name";
        String labelKind = "protocol name";
        boolean preferred = true;
        boolean ignored = true;
        TypedQuery<ProtocolLabel> q = findProtocolLabelsByNameLike(labelText, labelType, labelKind, preferred, ignored);
        return q;
    }

    public static TypedQuery<ProtocolLabel> findProtocolLabelsByNameLike(String labelText, String labelType, String labelKind, boolean preferred, boolean ignored) {
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        if (labelType == null || labelType.length() == 0) throw new IllegalArgumentException("The labelType argument is required");
        if (labelKind == null || labelKind.length() == 0) throw new IllegalArgumentException("The labelKind argument is required");
        EntityManager em = ProtocolLabel.entityManager();
        
        labelText = labelText.replace('*', '%');
        if (labelText.charAt(0) != '%') {
            labelText = "%" + labelText;
        }
        if (labelText.charAt(labelText.length() - 1) != '%') {
            labelText = labelText + "%";
        }
        
        TypedQuery<ProtocolLabel> q = em.createQuery("SELECT o FROM ProtocolLabel AS o WHERE o.lsType = :labelType  " 
        								+ "AND o.lsKind = :labelKind " 
        								+ "AND LOWER(o.labelText) LIKE LOWER(:labelText) " 
        								+ "AND o.preferred = :preferred AND o.ignored IS NOT :ignored", ProtocolLabel.class);
        q.setParameter("labelText", labelText);
        q.setParameter("labelType", labelType);
        q.setParameter("labelKind", labelKind);
        q.setParameter("preferred", preferred);
        q.setParameter("ignored", ignored);
        return q;
    }
}
