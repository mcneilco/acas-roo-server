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

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findContainerLabelsByLsTransactionEquals","findContainerLabelsByLabelTextEqualsAndIgnoredNot", "findContainerLabelsByContainerAndIgnoredNot" })
public class ContainerLabel extends AbstractLabel {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "container_id")
    private Container container;

    
    public ContainerLabel(com.labsynch.labseer.domain.ContainerLabel containerLabel) {
        super.setLsType(containerLabel.getLsType());
        super.setLsKind(containerLabel.getLsKind());
        super.setLsTypeAndKind(containerLabel.getLsType() + "_" + containerLabel.getLsKind());
        super.setLabelText(containerLabel.getLabelText());
        super.setPreferred(containerLabel.isPreferred());
        super.setLsTransaction(containerLabel.getLsTransaction());
        super.setRecordedBy(containerLabel.getRecordedBy());
        super.setRecordedDate(containerLabel.getRecordedDate());
        super.setPhysicallyLabled(containerLabel.isPhysicallyLabled());
    }

    public static ContainerLabel update(ContainerLabel containerLabel) {
    	ContainerLabel updatedContainerLabel = ContainerLabel.findContainerLabel(containerLabel.getId());
    	updatedContainerLabel.setLsType(containerLabel.getLsType());
    	updatedContainerLabel.setLsKind(containerLabel.getLsKind());
    	updatedContainerLabel.setLsTypeAndKind(containerLabel.getLsType() + "_" + containerLabel.getLsKind());
    	updatedContainerLabel.setLabelText(containerLabel.getLabelText());
    	updatedContainerLabel.setPreferred(containerLabel.isPreferred());
    	updatedContainerLabel.setLsTransaction(containerLabel.getLsTransaction());
    	updatedContainerLabel.setRecordedBy(containerLabel.getRecordedBy());
    	updatedContainerLabel.setRecordedDate(containerLabel.getRecordedDate());
    	updatedContainerLabel.setModifiedDate(new Date());
    	updatedContainerLabel.setPhysicallyLabled(containerLabel.isPhysicallyLabled());
    	updatedContainerLabel.setIgnored(containerLabel.isIgnored());
//    	updatedContainerLabel.merge();
    	return updatedContainerLabel;
    }
     

	public static TypedQuery<ContainerLabel> findContainerLabelsByContainerAndIgnoredNot(Container container, boolean ignored) {
        if (container == null) throw new IllegalArgumentException("The container argument is required");
        EntityManager em = ContainerLabel.entityManager();
        TypedQuery<ContainerLabel> q = em.createQuery("SELECT o FROM ContainerLabel AS o WHERE o.container = :container AND o.ignored IS NOT :ignored", ContainerLabel.class);
        q.setParameter("container", container);
        q.setParameter("ignored", ignored);
        return q;
    }

	public static TypedQuery<ContainerLabel> findContainerLabelsByLabelTextEqualsAndIgnoredNot(String labelText, boolean ignored) {
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        EntityManager em = ContainerLabel.entityManager();
        TypedQuery<ContainerLabel> q = em.createQuery("SELECT o FROM ContainerLabel AS o WHERE o.labelText = :labelText  AND o.ignored IS NOT :ignored", ContainerLabel.class);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);
        return q;
    }
	
    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public static ContainerLabel fromJsonToContainerLabel(String json) {
        return new JSONDeserializer<ContainerLabel>().use(null, ContainerLabel.class).deserialize(json);
    }
    
    public static String toJsonArray(Collection<ContainerLabel> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static Collection<ContainerLabel> fromJsonArrayToContainerLabels(String json) {
        return new JSONDeserializer<List<ContainerLabel>>().use(null, ArrayList.class).use("values", ContainerLabel.class).deserialize(json);
    }
    
    public static Collection<ContainerLabel> fromJsonArrayToContainerLabels(Reader json) {
        return new JSONDeserializer<List<ContainerLabel>>().use(null, ArrayList.class).use("values", ContainerLabel.class).deserialize(json);
    }
}
