package com.labsynch.labseer.domain;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findContainersByLsTypeEqualsAndLsKindEquals", "findContainersByLsTypeEquals", "findContainersByLsKindEquals" })
@RooJson
public class Container extends AbstractThing {

	private Long locationId;
	private Integer rowIndex;
	private Integer columnIndex;
	
	public Container() {
		
	}

	//constructor to instantiate a new Container from nested json objects
	public Container (Container container){
		this.setRecordedBy(container.getRecordedBy());
		this.setRecordedDate(container.getRecordedDate());
		this.setLsTransaction(container.getLsTransaction());
		this.setModifiedBy(container.getModifiedBy());
		this.setModifiedDate(container.getModifiedDate());
		this.setCodeName(container.getCodeName());
		this.setLsKind(container.getLsKind());
		this.setLsType(container.getLsType());
		this.setLsTypeAndKind(container.getLsTypeAndKind());
		this.setSubjects(container.getSubjects());
		this.locationId = container.getLocationId();
		this.rowIndex = container.getRowIndex();
		this.columnIndex = container.getColumnIndex();
	}

	public static Container update(Container container) {
		Container updatedContainer = Container.findContainer(container.getId());
		updatedContainer.setRecordedBy(container.getRecordedBy());
		updatedContainer.setRecordedDate(container.getRecordedDate());
		updatedContainer.setLsTransaction(container.getLsTransaction());
		updatedContainer.setModifiedBy(container.getModifiedBy());
		updatedContainer.setModifiedDate(new Date());
		updatedContainer.setCodeName(container.getCodeName());
		updatedContainer.setLsKind(container.getLsKind());
		updatedContainer.setLsType(container.getLsType());
		updatedContainer.setLsTypeAndKind(container.getLsTypeAndKind());
		updatedContainer.setLocationId(container.getLocationId());
		updatedContainer.setRowIndex(container.getRowIndex());
		updatedContainer.setColumnIndex(container.getColumnIndex());
		updatedContainer.merge();
		return updatedContainer;

	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "container", fetch =  FetchType.LAZY)
	private Set<ContainerLabel> lsLabels = new HashSet<ContainerLabel>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "container", fetch =  FetchType.LAZY)
	private Set<ContainerState> lsStates = new HashSet<ContainerState>();

	@OneToMany(cascade = {}, mappedBy = "secondContainer", fetch =  FetchType.LAZY)
	private Set<ItxContainerContainer> firstContainers = new HashSet<ItxContainerContainer>();

	@OneToMany(cascade = {}, mappedBy = "firstContainer", fetch =  FetchType.LAZY)
	private Set<ItxContainerContainer> secondContainers = new HashSet<ItxContainerContainer>();

	@OneToMany(cascade = {}, mappedBy = "container", fetch =  FetchType.LAZY)
	private Set<ItxSubjectContainer> subjects = new HashSet<ItxSubjectContainer>();

	public String toJson() {
		return new JSONSerializer().exclude("*.class")
				.include("lsStates.lsValues", "lsLabels")
				.transform(new ExcludeNulls(), void.class)       		
				.serialize(this);
	}

	public String toJsonStub() {
		return new JSONSerializer().exclude("*.class")
				.transform(new ExcludeNulls(), void.class)
				.prettyPrint(true)
				.serialize(this);
	}
	
	@Transactional
    public String toJsonWithNestedFull() {
        return new JSONSerializer().exclude("*.class").include("lsTags", "lsLabels", "lsStates.lsValues", "firstContainers.firstContainer.lsStates.lsValues","firstContainers.firstContainer.lsLabels", "secondContainers.secondContainer.lsStates.lsValues","secondContainers.secondContainer.lsLabels","firstContainers.lsStates.lsValues","firstContainers.lsLabels","secondContainers.lsStates.lsValues","secondContainers.lsLabels").transform(new ExcludeNulls(), void.class).serialize(this);
    }
	
	public static Container fromJsonToContainer(String json) {
		return new JSONDeserializer<Container>().
				use(null, Container.class).
				
				deserialize(json);
	}

	public static String toFullJsonArray(Collection<Container> collection) {
		return new JSONSerializer()
			.exclude("*.class")
			.include("lsStates.lsValues", "lsLabels")
			.transform(new ExcludeNulls(), void.class)
			.serialize(collection);
	}
	
	@Transactional
    public static String toJsonArrayWithNestedFull(Collection<com.labsynch.labseer.domain.Container> collection) {
        return new JSONSerializer().exclude("*.class").include("lsTags", "lsLabels", "lsStates.lsValues", "firstContainers.firstContainer.lsStates.lsValues","secondContainers.secondContainer.lsStates.lsValues","firstContainers.firstContainer.lsLabels","secondContainers.secondContainer.lsLabels","firstContainers.lsStates.lsValues","secondContainers.lsStates.lsValues","firstContainers.lsLabels","secondContainers.lsLabels").transform(new ExcludeNulls(), void.class).serialize(collection);
    }
	
	public static String toJsonArray(Collection<Container> collection) {
		return new JSONSerializer()
			.exclude("*.class")
			.include("lsStates.lsValues", "lsLabels")
			.transform(new ExcludeNulls(), void.class)
			.serialize(collection);
	}

	public static String toJsonArrayStatesStub(Collection<Container> collection) {
		return new JSONSerializer()
			.exclude("*.class")
			.include("lsStates")
			.transform(new ExcludeNulls(), void.class)
			.serialize(collection);
	}
	
	public static String toJsonArrayStub(Collection<Container> collection) {
		return new JSONSerializer()
			.exclude("*.class")
			.transform(new ExcludeNulls(), void.class)
			.serialize(collection);
	}

	public static Collection<Container> fromJsonArrayToContainers(String json) {
		return new JSONDeserializer<List<Container>>().
				use(null, ArrayList.class).
				use("values", Container.class).        		
				
				deserialize(json);
	}
	
	public static Collection<Container> fromJsonArrayToContainers(Reader json) {
		return new JSONDeserializer<List<Container>>().
				use(null, ArrayList.class).
				use("values", Container.class).        		
				
				deserialize(json);
	}

	public static long countContainers() {
		return entityManager().createQuery("SELECT COUNT(o) FROM Container o", Long.class).getSingleResult();
	}

	public static List<Container> findAllContainers() {
		return entityManager().createQuery("SELECT o FROM Container o", Container.class).getResultList();
	}

	public static Container findContainer(Long id) {
		if (id == null) return null;
		return entityManager().find(Container.class, id);
	}

	@Transactional
	public static List<Container> findContainerByContainerLabel(String containerName){    	
		List<ContainerLabel> foundContainerLabels = ContainerLabel.findContainerLabelsByLabelTextEqualsAndIgnoredNot(containerName, true).getResultList();		
		List<Container> containerList = new ArrayList<Container>();
		for (ContainerLabel containerLabel : foundContainerLabels){
			Container container = Container.findContainer(containerLabel.getContainer().getId());
			containerList.add(container);
		}			
		return containerList;
	}

	@Transactional
	public static List<Container> findContainersByContainerLabels(String containerNamesList){
		List<Container> containerList = new ArrayList<Container>();
		String[] containerNames = containerNamesList.split(",");
		for (String containerName : containerNames){
			List<ContainerLabel> foundContainerLabels = ContainerLabel.findContainerLabelsByLabelTextEqualsAndIgnoredNot(containerName, true).getResultList();		
			for (ContainerLabel containerLabel : foundContainerLabels){
				Container container = Container.findContainer(containerLabel.getContainer().getId());
				containerList.add(container);
			}
		}			
		return containerList;    	
	}

	public static List<Container> findContainerEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM Container o", Container.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
	}

	@Transactional
	public Container merge() {
		if (this.entityManager == null) this.entityManager = entityManager();
		Container merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}
	
	public static Container findContainerByCodeNameEquals(String codeName) {
        if (codeName == null || codeName.length() == 0) throw new IllegalArgumentException("The codeName argument is required");
        EntityManager em = Container.entityManager();
        TypedQuery<Container> q = em.createQuery("SELECT o FROM Container AS o WHERE o.codeName = :codeName", Container.class);
        q.setParameter("codeName", codeName);
        return q.getSingleResult();
    }

	public static TypedQuery<Container> findContainerByLabelText(String containerType, String containerKind, String labelText) {
        if (containerType == null || containerType.length() == 0) throw new IllegalArgumentException("The containerType argument is required");
        if (containerKind == null || containerKind.length() == 0) throw new IllegalArgumentException("The containerKind argument is required");
		if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        
        boolean ignored = true;
        EntityManager em = Container.entityManager();
		String query = "SELECT DISTINCT o FROM Container o " +
				"JOIN o.lsLabels ll with ll.ignored IS NOT :ignored AND ll.labelText = :labelText " +
				"WHERE o.ignored IS NOT :ignored " +
				"AND o.lsType = :containerType " +
				"AND o.lsKind = :containerKind ";
        
        TypedQuery<Container> q = em.createQuery(query, Container.class);
        q.setParameter("containerType", containerType);
        q.setParameter("containerKind", containerKind);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);
        
        return q;
	}
	
	public static TypedQuery<Container> findContainerByLabelText(String containerType, String containerKind, String labelType, String labelKind, String labelText) {
        if (containerType == null || containerType.length() == 0) throw new IllegalArgumentException("The containerType argument is required");
        if (containerKind == null || containerKind.length() == 0) throw new IllegalArgumentException("The containerKind argument is required");
        if (labelType == null || labelType.length() == 0) throw new IllegalArgumentException("The labelType argument is required");
        if (labelKind == null || labelKind.length() == 0) throw new IllegalArgumentException("The labelKind argument is required");
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        
        boolean ignored = true;
        
        EntityManager em = Container.entityManager();
		String query = "SELECT DISTINCT o FROM Container o " +
				"JOIN o.lsLabels ll " +
				"WHERE o.ignored IS NOT :ignored " +
				"AND o.lsType = :containerType " +
				"AND o.lsKind = :containerKind " +
				"AND ll.ignored IS NOT :ignored AND ll.lsType = :labelType AND ll.lsKind = :labelKind AND ll.labelText = :labelText";
        
        TypedQuery<Container> q = em.createQuery(query, Container.class);
        q.setParameter("containerType", containerType);
        q.setParameter("containerKind", containerKind);
        q.setParameter("labelType", labelType);        
        q.setParameter("labelKind", labelKind);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);
        
        return q;
	}
	
	public static TypedQuery<Container> findContainerByLabelTextAndLabelTypeKind(String labelType, String labelKind, String labelText) {
        if (labelType == null || labelType.length() == 0) throw new IllegalArgumentException("The labelType argument is required");
        if (labelKind == null || labelKind.length() == 0) throw new IllegalArgumentException("The labelKind argument is required");
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        
        boolean ignored = true;
        
        EntityManager em = Container.entityManager();
		String query = "SELECT DISTINCT o FROM Container o " +
				"JOIN o.lsLabels ll " +
				"WHERE o.ignored IS NOT :ignored " +
				"AND ll.ignored IS NOT :ignored AND ll.lsType = :labelType AND ll.lsKind = :labelKind AND ll.labelText = :labelText";
        
        TypedQuery<Container> q = em.createQuery(query, Container.class);
        q.setParameter("labelType", labelType);        
        q.setParameter("labelKind", labelKind);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);
        
        return q;
	}

}
