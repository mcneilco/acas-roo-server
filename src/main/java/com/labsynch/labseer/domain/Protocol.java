package com.labsynch.labseer.domain;

import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;
import com.labsynch.labseer.web.ProtocolController;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@RooJavaBean
@RooToString(excludeFields = { "lsTags", "lsStates", "experiments", "lsLabels" })
@RooJson
@RooJpaActiveRecord(finders = { "findProtocolsByCodeNameEquals", "findProtocolsByIgnoredNot", "findProtocolsByLsKindEquals", 
		"findProtocolsByLsTypeEquals", "findProtocolsByLsTypeEqualsAndLsKindEquals", "findProtocolsByLsTypeAndKindEquals", 
"findProtocolsByLsTransactionEquals" })
public class Protocol extends AbstractThing {

	private static final Logger logger = LoggerFactory.getLogger(Protocol.class);


	@Size(max = 1000)
	private String shortDescription;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "protocol")
	private Set<ProtocolState> lsStates = new HashSet<ProtocolState>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "protocol", fetch = FetchType.LAZY)
	private Set<Experiment> experiments = new HashSet<Experiment>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "protocol")
	private Set<ProtocolLabel> lsLabels = new HashSet<ProtocolLabel>();

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "PROTOCOL_TAG", joinColumns = { @javax.persistence.JoinColumn(name = "protocol_id") }, inverseJoinColumns = { @javax.persistence.JoinColumn(name = "tag_id") })
	private Set<LsTag> lsTags = new HashSet<LsTag>();

	public Protocol(com.labsynch.labseer.domain.Protocol protocol) {
		super.setRecordedBy(protocol.getRecordedBy());
		super.setRecordedDate(protocol.getRecordedDate());
		super.setLsTransaction(protocol.getLsTransaction());
		super.setModifiedBy(protocol.getModifiedBy());
		super.setModifiedDate(protocol.getModifiedDate());
		super.setIgnored(protocol.isIgnored());
		super.setCodeName(protocol.getCodeName());
		super.setLsType(protocol.getLsType());
		super.setLsKind(protocol.getLsKind());
		super.setLsTypeAndKind(protocol.getLsTypeAndKind());
		this.shortDescription = protocol.getShortDescription();
		if (protocol.getLsTags() != null) {
			for (LsTag lsTag : protocol.getLsTags()) {
				List<LsTag> queryTags = LsTag.findLsTagsByTagTextEquals(lsTag.getTagText()).getResultList();
				if (queryTags.size() < 1) {
					LsTag newLsTag = new LsTag(lsTag);
					newLsTag.persist();
					this.getLsTags().add(newLsTag);
				} else {
					this.getLsTags().add(queryTags.get(0));
				}
			}
		}
	}

	public Protocol() {
	}

	public static com.labsynch.labseer.domain.Protocol update(com.labsynch.labseer.domain.Protocol protocol) {
		Protocol updatedProtocol = Protocol.findProtocol(protocol.getId());
		updatedProtocol.setRecordedBy(protocol.getRecordedBy());
		updatedProtocol.setRecordedDate(protocol.getRecordedDate());
		updatedProtocol.setLsTransaction(protocol.getLsTransaction());
		updatedProtocol.setModifiedBy(protocol.getModifiedBy());
		updatedProtocol.setModifiedDate(new Date());
		updatedProtocol.setIgnored(protocol.isIgnored());
		updatedProtocol.setCodeName(protocol.getCodeName());
		updatedProtocol.setLsType(protocol.getLsType());
		updatedProtocol.setLsKind(protocol.getLsKind());
		updatedProtocol.setLsTypeAndKind(protocol.getLsTypeAndKind());
		updatedProtocol.shortDescription = protocol.getShortDescription();
		if (updatedProtocol.getLsTags() != null) {
			updatedProtocol.getLsTags().clear();
		}
		if (protocol.getLsTags() != null) {
			for (LsTag lsTag : protocol.getLsTags()) {
				List<LsTag> queryTags = LsTag.findLsTagsByTagTextEquals(lsTag.getTagText()).getResultList();
				if (queryTags.size() < 1) {
					LsTag newLsTag = new LsTag(lsTag);
					newLsTag.persist();
					updatedProtocol.getLsTags().add(newLsTag);
				} else {
					updatedProtocol.getLsTags().add(queryTags.get(0));
				}
			}
		}
		updatedProtocol.merge();
		return updatedProtocol;
	}

	public static List<com.labsynch.labseer.domain.Protocol> findProtocolByProtocolName(String protocolName) {
		List<ProtocolLabel> foundProtocolLabels = ProtocolLabel.findProtocolLabelsByName(protocolName).getResultList();
		List<Protocol> protocolList = new ArrayList<Protocol>();
		for (ProtocolLabel protocolLabel : foundProtocolLabels) {
			Protocol protocol = Protocol.findProtocol(protocolLabel.getProtocol().getId());
			protocolList.add(protocol);
		}
		return protocolList;
	}

	@Transactional
	public String toJsonStub() {
		return new JSONSerializer()
		.exclude("*.class", "lsStates.lsValues.lsState", "lsStates.protocol")
		.include("lsTags", "lsLabels", "lsStates.lsValues")
		.transform(new ExcludeNulls(), void.class).serialize(this);
	}

	@Transactional
	public String toPrettyJsonStub() {
		return new JSONSerializer()
		.exclude("*.class", "lsStates.lsValues.lsState", "lsStates.protocol")
		.include("lsTags", "lsLabels", "lsStates.lsValues")
		.prettyPrint(true)
		.transform(new ExcludeNulls(), void.class).serialize(this);
	}


	@Transactional
	public String toJson() {
		return new JSONSerializer()
		.exclude("*.class", "lsStates.lsValues.lsState", "lsStates.protocol",
				"experiment.protocol")
				.include("lsTags", "lsLabels", "lsStates.lsValues",
						"experiments.lsLabels", "experiments.lsStates.lsValues")
						.prettyPrint(false)
						.transform(new ExcludeNulls(), void.class).serialize(this);
	}


	@Transactional
	public String toPrettyJson() {
		return new JSONSerializer()
		.exclude("*.class", "lsStates.lsValues.lsState", "lsStates.protocol",
				"experiment.protocol")
				.include("lsTags", "lsLabels", "lsStates.lsValues",
						"experiments.lsLabels", "experiments.lsStates.lsValues")
						.prettyPrint(true)
						.transform(new ExcludeNulls(), void.class).serialize(this);
	}

	@Transactional
	public static String toJsonArray(Collection<com.labsynch.labseer.domain.Protocol> collection, boolean prettyJson, boolean includeExperiments) {

		if (includeExperiments){
			if (prettyJson){
				return toPrettyJsonArray(collection);

			} else {
				return toJsonArray(collection);

			}
		} else {
			if (prettyJson){
				return toPrettyJsonArrayStub(collection);

			} else {
				return toJsonArrayStub(collection);

			}
		}

	}

	@Transactional
	public static String toPrettyJsonArray(Collection<com.labsynch.labseer.domain.Protocol> collection) {
		return new JSONSerializer()
		.exclude("*.class", "lsStates.lsValues.lsState", "lsStates.protocol",
				"experiment.protocol")
				.include("lsTags", "lsLabels", "lsStates.lsValues",
						"experiments.lsLabels", "experiments.lsStates.lsValues")
						.prettyPrint(true)
						.transform(new ExcludeNulls(), void.class).serialize(collection);
	}

	@Transactional
	public static String toPrettyJsonArrayStub(Collection<com.labsynch.labseer.domain.Protocol> collection) {
		return new JSONSerializer()
		.exclude("*.class", "lsStates.lsValues.lsState", "lsStates.protocol")
		.include("lsTags", "lsLabels", "lsStates.lsValues")
		.prettyPrint(true)
		.transform(new ExcludeNulls(), void.class).serialize(collection);
	}

	@Transactional
	public static String toJsonArray(Collection<com.labsynch.labseer.domain.Protocol> collection) {
		return new JSONSerializer()
		.exclude("*.class")
		.include("lsTags", "lsLabels", "lsStates.lsValues")
		.transform(new ExcludeNulls(), void.class).serialize(collection);
	}

	@Transactional
	public static String toJsonArrayStub(Collection<com.labsynch.labseer.domain.Protocol> collection) {
		return new JSONSerializer()
		.exclude("*.class")
		.include("lsTags", "lsLabels", "lsStates.lsValues")
		.transform(new ExcludeNulls(), void.class).serialize(collection);
	}


	public static com.labsynch.labseer.domain.Protocol fromJsonToProtocol(String json) {
		return new JSONDeserializer<Protocol>().use(null, Protocol.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
	}


	public static Collection<com.labsynch.labseer.domain.Protocol> fromJsonArrayToProtocols(String json) {
		return new JSONDeserializer<List<Protocol>>().use(null, ArrayList.class).use("values", Protocol.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
	}

	public static Collection<com.labsynch.labseer.domain.Protocol> fromJsonArrayToProtocols(Reader json) {
		return new JSONDeserializer<List<Protocol>>().use(null, ArrayList.class).use("values", Protocol.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
	}

	@Transactional
	public static Collection<com.labsynch.labseer.domain.Protocol> findProtocolByName(String name) {
		List<ProtocolLabel> foundProtocolLabels = ProtocolLabel.findProtocolLabelsByName(name).getResultList();
		Collection<Protocol> protocolList = new HashSet<Protocol>();
		for (ProtocolLabel protocolLabel : foundProtocolLabels) {
			Protocol protocol = Protocol.findProtocol(protocolLabel.getProtocol().getId());
			protocolList.add(protocol);
		}
		List<Protocol> protocols = Protocol.findProtocolsByCodeNameEquals(name).getResultList();
		for (Protocol protocol : protocols) {
			protocolList.add(protocol);
		}
		return protocolList;
	}

	@Transactional
	public static List<com.labsynch.labseer.dto.CodeTableDTO> getProtocolCodeTable() {
		List<CodeTableDTO> codeTableList = new ArrayList<CodeTableDTO>();
		List<Protocol> protocols = Protocol.findProtocolsByIgnoredNot(true).getResultList();
		for (Protocol protocol : protocols) {
			for (ProtocolLabel protocolLabel : ProtocolLabel.findProtocolNames(protocol).getResultList()) {
				CodeTableDTO codeTable = new CodeTableDTO();
				codeTable.setName(protocolLabel.getLabelText());
				codeTable.setCode(protocol.getCodeName());
				codeTable.setIgnored(protocol.isIgnored());
				codeTableList.add(codeTable);
			}
		}
		return codeTableList;
	}

	public static List<com.labsynch.labseer.dto.CodeTableDTO> getProtocolCodeTableByNameLike(String protocolName) {
		List<CodeTableDTO> codeTableList = new ArrayList<CodeTableDTO>();
		for (ProtocolLabel protocolLabel : ProtocolLabel.findProtocolLabelsByNameLike(protocolName).getResultList()) {
			if (!protocolLabel.getProtocol().isIgnored()) {
				CodeTableDTO codeTable = new CodeTableDTO();
				codeTable.setName(protocolLabel.getLabelText());
				codeTable.setCode(protocolLabel.getProtocol().getCodeName());
				codeTable.setIgnored(protocolLabel.isIgnored());
				codeTableList.add(codeTable);
			}
		}
		return codeTableList;
	}

	public static List<CodeTableDTO> getProtocolCodeTableByKindEquals(String lsKind) {
		List<CodeTableDTO> codeTableList = new ArrayList<CodeTableDTO>();
		for (Protocol protocol : Protocol.findProtocolsByLsKindEquals(lsKind).getResultList()) {
			if (!protocol.isIgnored()) {
				CodeTableDTO codeTable = new CodeTableDTO();
				codeTable.setName(protocol.findPreferredName());
				codeTable.setCode(protocol.getCodeName());
				codeTable.setIgnored(protocol.isIgnored());
				codeTableList.add(codeTable);
			}
		}
		return codeTableList;
	}

	public String findPreferredName() {
		if (this.getId() == null){
			logger.debug("attempting to get the protocol preferred name -- but it is null");
			return " ";
		} else {
			List<ProtocolLabel> labels = ProtocolLabel.findProtocolPreferredName(this.getId()).getResultList();
			if (labels.size() == 1){
				return labels.get(0).getLabelText();
			} else if (labels.size() > 1){
				logger.error("ERROR: found mulitiple preferred names: " + labels.size());
				return labels.get(0).getLabelText();
			} else {
				logger.error("ERROR: no preferred name found");
				return " ";
			}			
		}

	}
}
