package com.labsynch.labseer.domain;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import java.net.MalformedURLException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.Size;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
// import com.labsynch.labseer.dto.BatchCodeDependencyDTO;
// import com.labsynch.labseer.dto.CodeTableDTO;
// import com.labsynch.labseer.dto.DependencyCheckDTO;
// import com.labsynch.labseer.dto.CmpdRegBatchCodeDTO;
// import com.labsynch.labseer.dto.ContainerBatchCodeDTO;
// import com.labsynch.labseer.service.BulkLoadService;
import com.labsynch.labseer.dto.PurgeSaltDependencyCheckResponseDTO;
// import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
// import com.labsynch.labseer.utils.PropertiesUtilService;
// import com.labsynch.labseer.utils.SimpleUtil;
// import com.labsynch.labseer.service.ChemStructureService.StructureType;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@Configurable
@Transactional

public class Salt {

    @Column(columnDefinition = "text")
    private String molStructure;

    @Size(max = 255)
    private String name;

    @Column(columnDefinition = "text")
    private String originalStructure;

    @Size(max = 100)
    private String abbrev;

    private Double molWeight;

    @Size(max = 255)
    private String formula;

    private int cdId;

    private Boolean ignore;

    private int charge;

    public static TypedQuery<Salt> findSaltsByAbbrevEquals(String abbrev) {
        if (abbrev == null || abbrev.length() == 0)
            throw new IllegalArgumentException("The abbrev argument is required");
        EntityManager em = Salt.entityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Salt> criteria = criteriaBuilder.createQuery(Salt.class);
        Root<Salt> saltRoot = criteria.from(Salt.class);
        criteria.select(saltRoot);
        Predicate predicate = criteriaBuilder.equal(criteriaBuilder.upper(saltRoot.<String>get("abbrev")),
                abbrev.toUpperCase().trim());
        criteria.where(criteriaBuilder.and(predicate));
        TypedQuery<Salt> q = em.createQuery(criteria);
        return q;
    }

    public static TypedQuery<Salt> findSaltsByAbbrevEqualsAndNameEquals(String abbrev, String name) {
        if (abbrev == null || abbrev.length() == 0)
            throw new IllegalArgumentException("The abbrev argument is required");
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Salt.entityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Salt> criteria = criteriaBuilder.createQuery(Salt.class);
        Root<Salt> saltRoot = criteria.from(Salt.class);
        criteria.select(saltRoot);
        Predicate[] predicates = new Predicate[0];
        List<Predicate> predicateList = new ArrayList<Predicate>();
        Predicate predicate1 = criteriaBuilder.equal(criteriaBuilder.upper(saltRoot.<String>get("abbrev")),
                abbrev.toUpperCase().trim());
        Predicate predicate2 = criteriaBuilder.equal(criteriaBuilder.upper(saltRoot.<String>get("name")),
                name.toUpperCase().trim());
        predicateList.add(predicate1);
        predicateList.add(predicate2);
        predicates = predicateList.toArray(predicates);
        criteria.where(criteriaBuilder.and(predicates));
        TypedQuery<Salt> q = em.createQuery(criteria);
        return q;
    }

    public static TypedQuery<Salt> findSaltsByAbbrevLike(String abbrev) {
        if (abbrev == null || abbrev.length() == 0)
            throw new IllegalArgumentException("The abbrev argument is required");
        abbrev = abbrev.replace('*', '%');
        if (abbrev.charAt(0) != '%') {
            abbrev = "%" + abbrev;
        }
        if (abbrev.charAt(abbrev.length() - 1) != '%') {
            abbrev = abbrev + "%";
        }
        EntityManager em = Salt.entityManager();
        TypedQuery<Salt> q = em.createQuery("SELECT o FROM Salt AS o WHERE LOWER(o.abbrev) LIKE LOWER(:abbrev)",
                Salt.class);
        q.setParameter("abbrev", abbrev);
        return q;
    }

    public static TypedQuery<Salt> findSaltsByCdId(int cdId) {
        EntityManager em = Salt.entityManager();
        TypedQuery<Salt> q = em.createQuery("SELECT o FROM Salt AS o WHERE o.cdId = :cdId", Salt.class);
        q.setParameter("cdId", cdId);
        return q;
    }

    public static TypedQuery<Salt> findSaltsByNameEquals(String name) {
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Salt.entityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Salt> criteria = criteriaBuilder.createQuery(Salt.class);
        Root<Salt> saltRoot = criteria.from(Salt.class);
        criteria.select(saltRoot);
        Predicate predicate = criteriaBuilder.equal(criteriaBuilder.upper(saltRoot.<String>get("name")),
                name.toUpperCase().trim());
        criteria.where(criteriaBuilder.and(predicate));
        TypedQuery<Salt> q = em.createQuery(criteria);
        return q;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Version
    @Column(name = "version")
    private Integer version;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public static TypedQuery<Salt> findSaltsBySearchTerm(String searchTerm) {
        if (searchTerm == null || searchTerm.length() == 0)
            throw new IllegalArgumentException("The searchTerm argument is required");
        searchTerm = searchTerm.replace('*', '%');
        if (searchTerm.charAt(0) != '%') {
            searchTerm = "%" + searchTerm;
        }
        if (searchTerm.charAt(searchTerm.length() - 1) != '%') {
            searchTerm = searchTerm + "%";
        }
        EntityManager em = Salt.entityManager();
        TypedQuery<Salt> q = em.createQuery(
                "SELECT DISTINCT o FROM Salt AS o WHERE (LOWER(o.abbrev) LIKE LOWER(:searchTerm) OR LOWER(o.name) LIKE LOWER(:searchTerm))",
                Salt.class);
        q.setParameter("searchTerm", searchTerm);
        return q;
    }

    public static Long countFindSaltsByAbbrevEquals(String abbrev) {
        if (abbrev == null || abbrev.length() == 0)
            throw new IllegalArgumentException("The abbrev argument is required");
        EntityManager em = Salt.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Salt AS o WHERE o.abbrev = :abbrev", Long.class);
        q.setParameter("abbrev", abbrev);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindSaltsByAbbrevEqualsAndNameEquals(String abbrev, String name) {
        if (abbrev == null || abbrev.length() == 0)
            throw new IllegalArgumentException("The abbrev argument is required");
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Salt.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Salt AS o WHERE o.abbrev = :abbrev  AND o.name = :name",
                Long.class);
        q.setParameter("abbrev", abbrev);
        q.setParameter("name", name);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindSaltsByAbbrevLike(String abbrev) {
        if (abbrev == null || abbrev.length() == 0)
            throw new IllegalArgumentException("The abbrev argument is required");
        abbrev = abbrev.replace('*', '%');
        if (abbrev.charAt(0) != '%') {
            abbrev = "%" + abbrev;
        }
        if (abbrev.charAt(abbrev.length() - 1) != '%') {
            abbrev = abbrev + "%";
        }
        EntityManager em = Salt.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Salt AS o WHERE LOWER(o.abbrev) LIKE LOWER(:abbrev)",
                Long.class);
        q.setParameter("abbrev", abbrev);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindSaltsByCdId(int cdId) {
        EntityManager em = Salt.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Salt AS o WHERE o.cdId = :cdId", Long.class);
        q.setParameter("cdId", cdId);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindSaltsByNameEquals(String name) {
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Salt.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Salt AS o WHERE o.name = :name", Long.class);
        q.setParameter("name", name);
        return ((Long) q.getSingleResult());
    }

    public static TypedQuery<Salt> findSaltsByAbbrevEquals(String abbrev, String sortFieldName, String sortOrder) {
        if (abbrev == null || abbrev.length() == 0)
            throw new IllegalArgumentException("The abbrev argument is required");
        EntityManager em = Salt.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Salt AS o WHERE o.abbrev = :abbrev");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Salt> q = em.createQuery(queryBuilder.toString(), Salt.class);
        q.setParameter("abbrev", abbrev);
        return q;
    }

    public static TypedQuery<Salt> findSaltsByAbbrevEqualsAndNameEquals(String abbrev, String name,
            String sortFieldName, String sortOrder) {
        if (abbrev == null || abbrev.length() == 0)
            throw new IllegalArgumentException("The abbrev argument is required");
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Salt.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM Salt AS o WHERE o.abbrev = :abbrev  AND o.name = :name");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Salt> q = em.createQuery(queryBuilder.toString(), Salt.class);
        q.setParameter("abbrev", abbrev);
        q.setParameter("name", name);
        return q;
    }

    public static TypedQuery<Salt> findSaltsByAbbrevLike(String abbrev, String sortFieldName, String sortOrder) {
        if (abbrev == null || abbrev.length() == 0)
            throw new IllegalArgumentException("The abbrev argument is required");
        abbrev = abbrev.replace('*', '%');
        if (abbrev.charAt(0) != '%') {
            abbrev = "%" + abbrev;
        }
        if (abbrev.charAt(abbrev.length() - 1) != '%') {
            abbrev = abbrev + "%";
        }
        EntityManager em = Salt.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM Salt AS o WHERE LOWER(o.abbrev) LIKE LOWER(:abbrev)");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Salt> q = em.createQuery(queryBuilder.toString(), Salt.class);
        q.setParameter("abbrev", abbrev);
        return q;
    }

    public static TypedQuery<Salt> findSaltsByCdId(int cdId, String sortFieldName, String sortOrder) {
        EntityManager em = Salt.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Salt AS o WHERE o.cdId = :cdId");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Salt> q = em.createQuery(queryBuilder.toString(), Salt.class);
        q.setParameter("cdId", cdId);
        return q;
    }

    public static TypedQuery<Salt> findSaltsByNameEquals(String name, String sortFieldName, String sortOrder) {
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Salt.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Salt AS o WHERE o.name = :name");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Salt> q = em.createQuery(queryBuilder.toString(), Salt.class);
        q.setParameter("name", name);
        return q;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @PersistenceContext
    transient EntityManager entityManager;

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("molStructure", "name",
            "originalStructure", "abbrev", "molWeight", "formula", "cdId", "ignore", "charge");

    public static final EntityManager entityManager() {
        EntityManager em = new Salt().entityManager;
        if (em == null)
            throw new IllegalStateException(
                    "Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countSalts() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Salt o", Long.class).getSingleResult();
    }

    public static List<Salt> findAllSalts() {
        return entityManager().createQuery("SELECT o FROM Salt o", Salt.class).getResultList();
    }

    public static List<Salt> findAllSalts(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Salt o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Salt.class).getResultList();
    }

    public static Salt findSalt(Long id) {
        if (id == null)
            return null;
        return entityManager().find(Salt.class, id);
    }

    public static List<Salt> findSaltEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Salt o", Salt.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    public static List<Salt> findSaltEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Salt o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Salt.class).setFirstResult(firstResult).setMaxResults(maxResults)
                .getResultList();
    }

    // public Map<String, HashSet<String>> checkACASDependencies(
	// 		Map<String, HashSet<String>> acasDependencies) throws MalformedURLException, IOException {
	// 	// here we make an external request to the ACAS Roo server to check for
	// 	// dependent experimental data
	// 	String url = propertiesUtilService.getAcasURL() + "compounds/checkBatchDependencies";
	// 	BatchCodeDependencyDTO request = new BatchCodeDependencyDTO(acasDependencies.keySet());
	// 	String json = request.toJson();
	// 	String responseJson = SimpleUtil.postRequestToExternalServer(url, json, null);
	// 	BatchCodeDependencyDTO responseDTO = BatchCodeDependencyDTO.fromJsonToBatchCodeDependencyDTO(responseJson);
	// 	if (responseDTO.getLinkedDataExists()) {
	// 		for (CodeTableDTO experimentCodeTable : responseDTO.getLinkedExperiments()) {
	// 			String experimentCodeAndName = experimentCodeTable.getCode() + " ( " + experimentCodeTable.getName()
	// 					+ " )";
	// 			if (acasDependencies.containsKey(experimentCodeTable.getComments())) {
	// 				acasDependencies.get(experimentCodeTable.getComments()).add(experimentCodeAndName);
	// 			} else {
	// 				HashSet<String> codes = new HashSet<String>();
	// 				codes.add(experimentCodeAndName);
	// 				acasDependencies.put(experimentCodeTable.getComments(), codes);
	// 			}
	// 		}
	// 	}
	// 	return acasDependencies;
	// }

    // private Collection<ContainerBatchCodeDTO> checkDependentACASContainers(Set<String> batchCodes)
	// 		throws MalformedURLException, IOException {
	// 	String url = propertiesUtilService.getAcasURL() + "containers/getContainerDTOsByBatchCodes";
	// 	String json = (new JSONSerializer()).serialize(batchCodes);
	// 	String responseJson = SimpleUtil.postRequestToExternalServer(url, json, null);
	// 	Collection<ContainerBatchCodeDTO> responseDTOs = ContainerBatchCodeDTO
	// 			.fromJsonArrayToContainerBatchCoes(responseJson);
	// 	return responseDTOs;

	// }

	// Method to Check for Dependent Data 
    public PurgeSaltDependencyCheckResponseDTO checkDependentData()
    {
        Map<String, HashSet<String>> acasDependencies = new HashMap<String, HashSet<String>>();
		Map<String, HashSet<String>> cmpdRegDependencies = new HashMap<String, HashSet<String>>();
		HashSet<String> dependentSingleRegLots = new HashSet<String>();
		int numberOfParents = 0;
		int numberOfSaltForms = 0;
		int numberOfLots = 0;
		Collection<Parent> parents = Parent.findParentsByCdId(this.getCdId()).getResultList();
		numberOfParents = parents.size();
		String manuallyRegistered = "Manually Registered";
		for (Parent parent : parents) {
			acasDependencies.put(parent.getCorpName(), new HashSet<String>());
			if (parent.getSaltForms() != null) {
				for (SaltForm saltForm : parent.getSaltForms()) {
					acasDependencies.put(saltForm.getCorpName(), new HashSet<String>());
					if (saltForm.getCdId() != this.getCdId()) {
						if (cmpdRegDependencies.containsKey(parent.getCorpName())) {
							cmpdRegDependencies.get(parent.getCorpName()).add(String.valueOf(saltForm.getCdId()));
						} else {
							HashSet<String> dependentSalts = new HashSet<String>();
							dependentSalts.add(String.valueOf(saltForm.getCdId()));
							cmpdRegDependencies.put(parent.getCorpName(), dependentSalts);
						}
					}
					if (saltForm.getLots() != null) {
						for (Lot lot : saltForm.getLots()) {
							acasDependencies.put(lot.getCorpName(), new HashSet<String>());
							if (lot.getSaltForm() == null) {
								dependentSingleRegLots.add(lot.getCorpName());
							} else if (lot.getSaltForm().getCdId() != this.getCdId()) {
								if (cmpdRegDependencies.containsKey(parent.getCorpName())) {
									cmpdRegDependencies.get(parent.getCorpName())
											.add(String.valueOf(lot.getSaltForm().getCdId()));
								} else {
									HashSet<String> dependentSalts = new HashSet<String>();
									dependentSalts.add(String.valueOf(lot.getSaltForm().getCdId()));
									cmpdRegDependencies.put(parent.getCorpName(), dependentSalts);
								}
							}
						}
					}
				}
			}
		}
		parents.clear();

		Collection<SaltForm> saltForms = SaltForm.findSaltFormsByCdId(this.getCdId()).getResultList();
		numberOfSaltForms = saltForms.size();
		for (SaltForm saltForm : saltForms) {
			acasDependencies.put(saltForm.getCorpName(), new HashSet<String>());
			if (saltForm.getLots() != null) {
				for (Lot lot : saltForm.getLots()) {
					acasDependencies.put(lot.getCorpName(), new HashSet<String>());
					if (lot.getSaltForm() == null) {
						dependentSingleRegLots.add(lot.getCorpName());
					} else if (lot.getSaltForm().getCdId() != this.getCdId()) {
						if (cmpdRegDependencies.containsKey(saltForm.getCorpName())) {
							cmpdRegDependencies.get(saltForm.getCorpName()).add(String.valueOf(lot.getSaltForm().getCdId()));
						} else {
							HashSet<String> dependentSalts = new HashSet<String>();
							dependentSalts.add(String.valueOf(lot.getSaltForm().getCdId()));
							cmpdRegDependencies.put(saltForm.getCorpName(), dependentSalts);
						}
					}
				}
			}
		}
		saltForms.clear();

		// Check for all the vials in ACAS that reference lots being purged
		Integer numberOfDependentContainers = 0;
		// Collection<ContainerBatchCodeDTO> dependentContainers = null;
		// if (!acasDependencies.isEmpty()) {
		// 	try {
		// 		dependentContainers = checkDependentACASContainers(acasDependencies.keySet());
		// 		numberOfDependentContainers = dependentContainers.size();
		// 	} catch (Exception e) {
		// 	}
		// }
		// Then check for data dependencies in ACAS.
		// if (!acasDependencies.isEmpty()) {
		// 	// check dependencies differently if config to check by barcode is enabled
		// 	if (propertiesUtilService.getCheckACASDependenciesByContainerCode()) {
		// 		try {
		// 			Map<String, HashSet<String>> acasContainerDependencies = new HashMap<String, HashSet<String>>();
		// 			for (ContainerBatchCodeDTO container : dependentContainers) {
		// 				acasContainerDependencies.put(container.getContainerCodeName(), new HashSet<String>());
		// 			}
		// 			acasContainerDependencies = checkACASDependencies(acasContainerDependencies);
		// 			for (ContainerBatchCodeDTO containerBatchDTO : dependentContainers) {
		// 				HashSet<String> currentDependencies = acasDependencies.get(containerBatchDTO.getBatchCode());
		// 				currentDependencies
		// 						.addAll(acasContainerDependencies.get(containerBatchDTO.getContainerCodeName()));
		// 				acasDependencies.put(containerBatchDTO.getBatchCode(), currentDependencies);
		// 			}
		// 		} catch (Exception e) {
		// 		}
		// 	} else {
		// 		try {
		// 			acasDependencies = checkACASDependencies(acasDependencies);
		// 		} catch (Exception e) {
		// 		}
		// 	}
		// }

		HashSet<String> dependentFiles = new HashSet<String>();
		for (HashSet<String> dependentSet : cmpdRegDependencies.values()) {
			for (String dependent : dependentSet) {
				dependentFiles.add(dependent);
			}
		}
		HashSet<String> dependentExperiments = new HashSet<String>();
		for (HashSet<String> dependentSet : acasDependencies.values()) {
			for (String dependent : dependentSet) {
				dependentExperiments.add(dependent);
			}
		}

		if (!dependentFiles.isEmpty() || !dependentExperiments.isEmpty() || !dependentSingleRegLots.isEmpty()) {
			String summary = "This salt is referenced by " + String.valueOf(dependentExperiments.size()) + " experiments and " + String.valueOf(dependentSingleRegLots.size()) + " lots. ";
			summary = summary + "This salt cannot be deleted. ";
            return new PurgeSaltDependencyCheckResponseDTO(summary, false);
		} else {
			String summary = "There were no lot dependencies found for this salt.";
			return new PurgeSaltDependencyCheckResponseDTO(summary, true);
		}

    }

    @Transactional
    public void persist() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

    @Transactional
    public void remove() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Salt attached = Salt.findSalt(this.id);
            this.entityManager.remove(attached);
        }
    }

    @Transactional
    public void flush() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        this.entityManager.flush();
    }

    @Transactional
    public void clear() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        this.entityManager.clear();
    }

    @Transactional
    public Salt merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        Salt merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public String getMolStructure() {
        return this.molStructure;
    }

    public void setMolStructure(String molStructure) {
        this.molStructure = molStructure;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalStructure() {
        return this.originalStructure;
    }

    public void setOriginalStructure(String originalStructure) {
        this.originalStructure = originalStructure;
    }

    public String getAbbrev() {
        return this.abbrev;
    }

    public void setAbbrev(String abbrev) {
        this.abbrev = abbrev;
    }

    public Double getMolWeight() {
        return this.molWeight;
    }

    public void setMolWeight(Double molWeight) {
        this.molWeight = molWeight;
    }

    public String getFormula() {
        return this.formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public int getCdId() {
        return this.cdId;
    }

    public void setCdId(int cdId) {
        this.cdId = cdId;
    }

    public Boolean getIgnore() {
        return this.ignore;
    }

    public void setIgnore(Boolean ignore) {
        this.ignore = ignore;
    }

    public int getCharge() {
        return this.charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }

    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(this);
    }

    public static Salt fromJsonToSalt(String json) {
        return new JSONDeserializer<Salt>()
                .use(null, Salt.class).deserialize(json);
    }

    public static String toJsonArray(Collection<Salt> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<Salt> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<Salt> fromJsonArrayToSalts(String json) {
        return new JSONDeserializer<List<Salt>>()
                .use("values", Salt.class).deserialize(json);
    }
}
