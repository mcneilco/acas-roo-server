package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.domain.TreatmentGroupValue;
import com.labsynch.labseer.utils.ExcludeNulls;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class CmpdRegBatchCodeDTO {

	public CmpdRegBatchCodeDTO() {
	}

	public CmpdRegBatchCodeDTO(Collection<String> batchCodes) {
		this.setBatchCodes(batchCodes);
	}

	public CmpdRegBatchCodeDTO(String corpName) {
		this.setBatchCodes(Collections.singletonList(corpName));
    }

	private static final Logger logger = LoggerFactory.getLogger(CmpdRegBatchCodeDTO.class);

	private Collection<String> batchCodes;

	private Boolean linkedDataExists;

	private Collection<CmpdRegBatchCodeExperimentDTO> linkedExperiments;

	private Collection<ContainerBatchCodeDTO> linkedContainers;

	private Collection<ErrorMessageDTO> errors;

	private String summary;

	public String toJson() {
		return new JSONSerializer().exclude("*.class").include("linkedExperiments.*", "batchCodes.*", "linkedContainers.*")
				.transform(new ExcludeNulls(), void.class).serialize(this);
	}

	@Transactional
	public void checkForDependentData() {
		linkedDataExists = false;
		linkedExperiments = new HashSet<CmpdRegBatchCodeExperimentDTO>();
		errors = new HashSet<ErrorMessageDTO>();
		try {
			if (countExperimentValueBatchCodes() > 0) {
				linkedDataExists = true;
				logger.debug("Found Experiment values referencing provided batch codes. Retrieving experiment info.");
				addUniqueExperiments(findExperimentCodeTableDTOsFromExperimentValueBatchCodes());
			}
		} catch (Exception e) {
			ErrorMessageDTO error = new ErrorMessageDTO();
			error.setLevel("error");
			error.setMessage(e.getMessage());
			errors.add(error);
		}
		if (countAnalysisGroupValueBatchCodes() > 0) {
			linkedDataExists = true;
			logger.debug("Found AnalysisGroup values referencing provided batch codes. Retrieving experiment info.");
			addUniqueExperiments(findExperimentCodeTableDTOsFromAnalysisGroupValueBatchCodes());
		}
		if (countTreatmentGroupValueBatchCodes() > 0) {
			linkedDataExists = true;
			logger.debug("Found TreatmentGroup values referencing provided batch codes. Retrieving experiment info.");
			addUniqueExperiments(findExperimentCodeTableDTOsFromTreatmentGroupValueBatchCodes());
		}
		if (countSubjectValueBatchCodes() > 0) {
			linkedDataExists = true;
			logger.debug("Found Subject values referencing provided batch codes. Retrieving experiment info.");
			addUniqueExperiments(findExperimentCodeTableDTOsFromSubjectValueBatchCodes());
		}
		if (countProtocolValueBatchCodes() > 0) {
			linkedDataExists = true;
			logger.debug("Found Protocol values referencing provided batch codes.");
		}
		if (countLsThingValueBatchCodes() > 0) {
			linkedDataExists = true;
			logger.debug("Found LsThing values referencing provided batch codes.");
		}
		if (countContainerValueBatchCodes() > 0) {
			linkedDataExists = true;
			logger.debug("Found Container values referencing provided batch codes. Retrieving container info.");
		}
		// dedupeLinkedExperiments();
	}

	private Collection<CmpdRegBatchCodeExperimentDTO> findExperimentCodeTableDTOsFromExperimentValueBatchCodes() {
		EntityManager em = SubjectValue.entityManager();
		String sql = "SELECT DISTINCT NEW MAP(p.codeName as protcolCode, e.codeName as experimentCode, el.labelText as experimentName, ev.codeValue as comments, count(ev.id) as description) "
				+ "FROM ExperimentValue ev "
				+ "JOIN ev.lsState as es "
				+ "JOIN es.experiment as e "
				+ "LEFT OUTER JOIN e.lsLabels as el "
				+ "JOIN e.parent as p "
				+ "WHERE el.lsKind = 'experiment name' "
				+ "AND ev.lsKind = 'batch code' "
				+ "AND ev.ignored = false "
				+ "AND es.ignored = false "
				+ "AND e.ignored = false "
				+ "AND el.ignored = false "
				+ "AND ev.codeValue IN :batchCodes "
				+ "GROUP BY e.codeName, el.labelText, ev.codeValue";

		TypedQuery<Map> q = em.createQuery(sql, Map.class);
		q.setParameter("batchCodes", this.batchCodes);

		Collection<CmpdRegBatchCodeExperimentDTO> cmpdRegBatchCodeExperimentDTOs = new HashSet<CmpdRegBatchCodeExperimentDTO>();
		for (Map<String, String> map : q.getResultList()) {
			CmpdRegBatchCodeExperimentDTO cmpdRegBatchCodeExperimentDTO = new CmpdRegBatchCodeExperimentDTO();
			cmpdRegBatchCodeExperimentDTO.setProtocolCode(map.get("protocolCode"));
			cmpdRegBatchCodeExperimentDTO.setExperimentCode(map.get("experimentCode"));
			cmpdRegBatchCodeExperimentDTO.setExperimentName(map.get("experimentName"));
			cmpdRegBatchCodeExperimentDTO.setComments(map.get("comments"));
			cmpdRegBatchCodeExperimentDTO.setDescription(map.get("description"));
			cmpdRegBatchCodeExperimentDTOs.add(cmpdRegBatchCodeExperimentDTO);
		}
		return cmpdRegBatchCodeExperimentDTOs;
	}

	private Collection<CmpdRegBatchCodeExperimentDTO> findExperimentCodeTableDTOsFromAnalysisGroupValueBatchCodes() {
		EntityManager em = SubjectValue.entityManager();
		String sql = "SELECT DISTINCT NEW MAP(p.codeName as protocolCode, e.codeName as experimentCode, el.labelText as experimentName, agv.codeValue as comments, count(agv2.id) || ' results' as description) "
				+ "FROM AnalysisGroupValue agv "
				+ "JOIN agv.lsState as ags "
				+ "JOIN ags.analysisGroup as ag "
				+ "JOIN ag.experiments as e "
				+ "LEFT OUTER JOIN e.lsLabels as el ON el.ignored = false "
				+ "LEFT OUTER JOIN ags.lsValues agv2 ON agv2.lsKind <> 'batch code' AND agv2.ignored = false "
				+ "JOIN e.protocol as p "
				+ "WHERE el.lsKind = 'experiment name' "
				+ "AND agv.lsType = 'codeValue' "
				+ "AND agv.lsKind = 'batch code' "
				+ "AND agv.ignored = false "
				+ "AND ags.ignored = false "
				+ "AND ag.ignored = false "
				+ "AND e.ignored = false "
				+ "AND agv.codeValue IN :batchCodes "
				+ "GROUP BY p.codeName, e.codeName, el.labelText, agv.codeValue";

		TypedQuery<Map> q = em.createQuery(sql, Map.class);
		q.setParameter("batchCodes", this.batchCodes);

		Collection<CmpdRegBatchCodeExperimentDTO> cmpdRegBatchCodeExperimentDTOs = new HashSet<CmpdRegBatchCodeExperimentDTO>();
		for (Map<String, String> map : q.getResultList()) {
			CmpdRegBatchCodeExperimentDTO cmpdRegBatchCodeExperimentDTO = new CmpdRegBatchCodeExperimentDTO();
			cmpdRegBatchCodeExperimentDTO.setProtocolCode(map.get("protocolCode"));
			cmpdRegBatchCodeExperimentDTO.setExperimentCode(map.get("experimentCode"));
			cmpdRegBatchCodeExperimentDTO.setExperimentName(map.get("experimentName"));
			cmpdRegBatchCodeExperimentDTO.setComments(map.get("comments"));
			cmpdRegBatchCodeExperimentDTO.setDescription(String.valueOf(map.get("description")));
			cmpdRegBatchCodeExperimentDTOs.add(cmpdRegBatchCodeExperimentDTO);
		}
		return cmpdRegBatchCodeExperimentDTOs;
	}

	private Collection<CmpdRegBatchCodeExperimentDTO> findExperimentCodeTableDTOsFromSubjectValueBatchCodes() {
		EntityManager em = SubjectValue.entityManager();
		String sql = "SELECT DISTINCT NEW MAP(p.codeName as protocolCode, e.codeName as experimentCode, el.labelText as experimentName, sv.codeValue as comments, count(sv2.id) || ' raw results' as description) "
				+ "FROM SubjectValue sv "
				+ "JOIN sv.lsState as ss "
				+ "JOIN ss.subject as s "
				+ "JOIN s.treatmentGroups as tg "
				+ "JOIN tg.analysisGroups as ag "
				+ "JOIN ag.experiments as e "
				+ "LEFT OUTER JOIN e.lsLabels as el ON el.ignored = false "
				+ "LEFT OUTER JOIN ss.lsValues as sv2 ON sv2.lsKind <> 'batch code' AND sv2.ignored = false AND ss.lsType = 'data' and ss.lsKind = 'results' "
				+ "JOIN p.protocol as p"
				+ "WHERE el.lsKind = 'experiment name' "
				+ "AND sv.lsKind = 'batch code' "
				+ "AND sv.ignored = false "
				+ "AND ss.ignored = false "
				+ "AND s.ignored = false "
				+ "AND tg.ignored = false "
				+ "AND ag.ignored = false "
				+ "AND e.ignored = false "
				+ "AND sv.codeValue IN :batchCodes "
				+ "GROUP BY e.codeName, el.labelText, sv.codeValue";

		TypedQuery<Map> q = em.createQuery(sql, Map.class);
		q.setParameter("batchCodes", this.batchCodes);

		Collection<CmpdRegBatchCodeExperimentDTO> cmpdRegBatchCodeExperimentDTOs = new HashSet<CmpdRegBatchCodeExperimentDTO>();
		for (Map<String, String> map : q.getResultList()) {
			CmpdRegBatchCodeExperimentDTO cmpdRegBatchCodeExperimentDTO = new CmpdRegBatchCodeExperimentDTO();
			cmpdRegBatchCodeExperimentDTO.setProtocolCode(map.get("protocolCode"));
			cmpdRegBatchCodeExperimentDTO.setExperimentCode(map.get("experimentCode"));
			cmpdRegBatchCodeExperimentDTO.setExperimentName(map.get("experimentName"));
			cmpdRegBatchCodeExperimentDTO.setComments(map.get("comments"));
			cmpdRegBatchCodeExperimentDTO.setDescription(map.get("description"));
			cmpdRegBatchCodeExperimentDTOs.add(cmpdRegBatchCodeExperimentDTO);
		}
		return cmpdRegBatchCodeExperimentDTOs;
	}

	private Collection<CmpdRegBatchCodeExperimentDTO> findExperimentCodeTableDTOsFromTreatmentGroupValueBatchCodes() {
		EntityManager em = SubjectValue.entityManager();
		String sql = "SELECT DISTINCT NEW MAP(p.codeName as protocolCode, e.codeName as experimentCode, el.labelText as experimentName, tgv.codeValue as comments) "
				+ "FROM TreatmentGroupValue tgv "
				+ "JOIN tgv.lsState as tgs "
				+ "JOIN tgs.treatmentGroup as tg "
				+ "JOIN tg.analysisGroups as ag "
				+ "JOIN ag.experiments as e "
				+ "LEFT OUTER JOIN e.lsLabels as el "
				+ "JOIN e.protocol as p "
				+ "WHERE el.lsKind = 'experiment name' "
				+ "AND tgv.lsType = 'codeValue' "
				+ "AND tgv.lsKind = 'batch code' "
				+ "AND tgv.ignored = false "
				+ "AND tgs.ignored = false "
				+ "AND tg.ignored = false "
				+ "AND ag.ignored = false "
				+ "AND e.ignored = false "
				+ "AND el.ignored = false "
				+ "AND tgv.codeValue IN :batchCodes "
				+ "GROUP BY e.codeName, el.labelText, tgv.codeValue";

		TypedQuery<Map> q = em.createQuery(sql, Map.class);
		q.setParameter("batchCodes", this.batchCodes);

		Collection<CmpdRegBatchCodeExperimentDTO> cmpdRegBatchCodeExperimentDTOs = new HashSet<CmpdRegBatchCodeExperimentDTO>();
		for (Map<String, String> map : q.getResultList()) {
			CmpdRegBatchCodeExperimentDTO cmpdRegBatchCodeExperimentDTO = new CmpdRegBatchCodeExperimentDTO();
			cmpdRegBatchCodeExperimentDTO.setProtocolCode(map.get("protocolCode"));
			cmpdRegBatchCodeExperimentDTO.setExperimentCode(map.get("code"));
			cmpdRegBatchCodeExperimentDTO.setExperimentName(map.get("name"));
			cmpdRegBatchCodeExperimentDTO.setComments(map.get("comments"));
			cmpdRegBatchCodeExperimentDTOs.add(cmpdRegBatchCodeExperimentDTO);
		}
		return cmpdRegBatchCodeExperimentDTOs;
	}

	private int countSubjectValueBatchCodes() {
		EntityManager em = SubjectValue.entityManager();
		String sql = "SELECT COUNT(*) "
				+ "FROM SubjectValue sv "
				+ "JOIN sv.lsState as ss "
				+ "JOIN ss.subject as s "
				+ "JOIN s.treatmentGroups as tg "
				+ "JOIN tg.analysisGroups as ag "
				+ "JOIN ag.experiments as e "
				+ "WHERE sv.lsType = 'codeValue' "
				+ "AND sv.lsKind = 'batch code' "
				+ "AND sv.ignored = false "
				+ "AND ss.ignored = false "
				+ "AND s.ignored = false "
				+ "AND tg.ignored = false "
				+ "AND ag.ignored = false "
				+ "AND e.ignored = false "
				+ "AND sv.codeValue IN :batchCodes";

		TypedQuery<Long> q = em.createQuery(sql, Long.class);
		q.setParameter("batchCodes", this.batchCodes);
		return q.getSingleResult().intValue();
	}

	private int countTreatmentGroupValueBatchCodes() {
		EntityManager em = TreatmentGroupValue.entityManager();
		String sql = "SELECT COUNT(*) "
				+ "FROM TreatmentGroupValue tgv "
				+ "JOIN tgv.lsState as tgs "
				+ "JOIN tgs.treatmentGroup as tg "
				+ "JOIN tg.analysisGroups as ag "
				+ "JOIN ag.experiments as e "
				+ "WHERE tgv.lsType = 'codeValue' "
				+ "AND tgv.lsKind = 'batch code' "
				+ "AND tgv.ignored = false "
				+ "AND tgs.ignored = false "
				+ "AND tg.ignored = false "
				+ "AND ag.ignored = false "
				+ "AND e.ignored = false "
				+ "AND tgv.codeValue IN :batchCodes";

		TypedQuery<Long> q = em.createQuery(sql, Long.class);
		q.setParameter("batchCodes", this.batchCodes);
		return q.getSingleResult().intValue();
	}

	private int countAnalysisGroupValueBatchCodes() {
		EntityManager em = AnalysisGroupValue.entityManager();
		String sql = "SELECT COUNT(*) "
				+ "FROM AnalysisGroupValue agv "
				+ "JOIN agv.lsState as ags "
				+ "JOIN ags.analysisGroup as ag "
				+ "JOIN ag.experiments as e "
				+ "WHERE agv.lsType = 'codeValue' "
				+ "AND agv.lsKind = 'batch code' "
				+ "AND agv.ignored = false "
				+ "AND ags.ignored = false "
				+ "AND ag.ignored = false "
				+ "AND e.ignored = false "
				+ "AND agv.codeValue IN :batchCodes";

		TypedQuery<Long> q = em.createQuery(sql, Long.class);
		q.setParameter("batchCodes", this.batchCodes);
		return q.getSingleResult().intValue();
	}

	private int countExperimentValueBatchCodes() {
		EntityManager em = TreatmentGroupValue.entityManager();
		String sql = "SELECT COUNT(*) "
				+ "FROM ExperimentValue ev "
				+ "JOIN ev.lsState as es "
				+ "JOIN es.experiment as e "
				+ "WHERE ev.lsType = 'codeValue' "
				+ "AND ev.lsKind = 'batch code' "
				+ "AND ev.ignored = false "
				+ "AND es.ignored = false "
				+ "AND e.ignored = false "
				+ "AND ev.codeValue IN :batchCodes";

		TypedQuery<Long> q = em.createQuery(sql, Long.class);
		q.setParameter("batchCodes", batchCodes);
		return q.getSingleResult().intValue();
	}

	private int countLsThingValueBatchCodes() {
		EntityManager em = TreatmentGroupValue.entityManager();
		String sql = "SELECT COUNT(*) "
				+ "FROM LsThingValue lstv "
				+ "JOIN lstv.lsState as lsts "
				+ "JOIN lsts.lsThing as lst "
				+ "WHERE lstv.lsType = 'codeValue' "
				+ "AND lstv.lsKind = 'batch code' "
				+ "AND lstv.ignored = false "
				+ "AND lsts.ignored = false "
				+ "AND lst.ignored = false "
				+ "AND lstv.codeValue IN :batchCodes";

		TypedQuery<Long> q = em.createQuery(sql, Long.class);
		q.setParameter("batchCodes", batchCodes);
		return q.getSingleResult().intValue();
	}

	private int countContainerValueBatchCodes() {
		EntityManager em = TreatmentGroupValue.entityManager();
		String sql = "SELECT COUNT(*) "
				+ "FROM ContainerValue cv "
				+ "JOIN cv.lsState as cs "
				+ "JOIN cs.container as c "
				+ "WHERE cv.lsType = 'codeValue' "
				+ "AND cv.lsKind = 'batch code' "
				+ "AND cv.ignored = false "
				+ "AND cs.ignored = false "
				+ "AND c.ignored = false "
				+ "AND cv.codeValue IN :batchCodes";

		TypedQuery<Long> q = em.createQuery(sql, Long.class);
		q.setParameter("batchCodes", batchCodes);
		return q.getSingleResult().intValue();
	}

	private int countProtocolValueBatchCodes() {
		EntityManager em = TreatmentGroupValue.entityManager();
		String sql = "SELECT COUNT(*) "
				+ "FROM ProtocolValue pv "
				+ "JOIN pv.lsState as ps "
				+ "JOIN ps.protocol as p "
				+ "WHERE pv.lsType = 'codeValue' "
				+ "AND pv.lsKind = 'batch code' "
				+ "AND pv.ignored = false "
				+ "AND ps.ignored = false "
				+ "AND p.ignored = false "
				+ "AND pv.codeValue IN :batchCodes";

		TypedQuery<Long> q = em.createQuery(sql, Long.class);
		q.setParameter("batchCodes", batchCodes);
		return q.getSingleResult().intValue();
	}

	private void addUniqueExperiments(Collection<CmpdRegBatchCodeExperimentDTO> cmpdRegBatchCodeExperimentDTOs) {
		for (CmpdRegBatchCodeExperimentDTO cmpdRegBatchCodeExperimentDTO : cmpdRegBatchCodeExperimentDTOs) {
			Boolean addIt = true;
			for(CmpdRegBatchCodeExperimentDTO experimentCodeTable : linkedExperiments) {
				if(cmpdRegBatchCodeExperimentDTO.getExperimentCode().equals(experimentCodeTable.getExperimentCode())) {
					addIt = false;
					if(cmpdRegBatchCodeExperimentDTO.getDescription() != null) {
						if(experimentCodeTable.getDescription() == null) {
							// Just set the description
							experimentCodeTable.setDescription(cmpdRegBatchCodeExperimentDTO.getDescription());
						} else {
							// Add the description to the existing description
							experimentCodeTable.setDescription(experimentCodeTable.getDescription() + " and " + cmpdRegBatchCodeExperimentDTO.getDescription());
						}
					}
					break;
				}
			}
			if(addIt) {
				linkedExperiments.add(cmpdRegBatchCodeExperimentDTO);
			}
		}
	}

	public Collection<String> getBatchCodes() {
		return this.batchCodes;
	}

	public void setBatchCodes(Collection<String> batchCodes) {
		this.batchCodes = batchCodes;
	}

	public Boolean getLinkedDataExists() {
		return this.linkedDataExists;
	}

	public void setLinkedDataExists(Boolean linkedDataExists) {
		this.linkedDataExists = linkedDataExists;
	}

	public Collection<CmpdRegBatchCodeExperimentDTO> getLinkedExperiments() {
		return this.linkedExperiments;
	}

	public void setLinkedExperiments(Collection<CmpdRegBatchCodeExperimentDTO> linkedExperiments) {
		this.linkedExperiments = linkedExperiments;
	}

	public Collection<ContainerBatchCodeDTO> getLinkedContainers() {
		return this.linkedContainers;
	}

	public void setLinkedContainers(Collection<ContainerBatchCodeDTO> linkedContainers) {
		this.linkedContainers = linkedContainers;
	}

	public Collection<ErrorMessageDTO> getErrors() {
		return this.errors;
	}

	public void setErrors(Collection<ErrorMessageDTO> errors) {
		this.errors = errors;
	}

	public static CmpdRegBatchCodeDTO fromJsonToCmpdRegBatchCodeDTO(String json) {
		return new JSONDeserializer<CmpdRegBatchCodeDTO>()
				.use(null, CmpdRegBatchCodeDTO.class).deserialize(json);
	}

	public static String toJsonArray(Collection<CmpdRegBatchCodeDTO> collection) {
		return new JSONSerializer()
				.exclude("*.class").serialize(collection);
	}

	public static String toJsonArray(Collection<CmpdRegBatchCodeDTO> collection, String[] fields) {
		return new JSONSerializer()
				.include(fields).exclude("*.class").serialize(collection);
	}

	public static Collection<CmpdRegBatchCodeDTO> fromJsonArrayToCmpdRegBatchCoes(String json) {
		return new JSONDeserializer<List<CmpdRegBatchCodeDTO>>()
				.use("values", CmpdRegBatchCodeDTO.class).deserialize(json);
	}

	public String toString() {
		setSummary(getSummary());
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getSummary() {
		String msg = "";
		if(getLinkedDataExists()) {
			msg +=  getLinkedExperiments().size() + " linked experiments: " + getLinkedExperiments().stream().map(e -> e.getExperimentCode() + '(' + e.getExperimentName() + ")").collect(Collectors.joining(", "));
			if(getLinkedContainers().size() > 0) {
				msg += getLinkedContainers().size() + " linked containers: " + getLinkedContainers().stream().map(c -> c.getContainerBarcode()).collect(Collectors.toList());
			}
		}
		return msg;
	}
}
