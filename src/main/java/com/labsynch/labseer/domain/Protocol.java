package com.labsynch.labseer.domain;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Size;

import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.utils.ExcludeNulls;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Configurable
@Entity
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
    @JoinTable(name = "PROTOCOL_TAG", joinColumns = {
            @javax.persistence.JoinColumn(name = "protocol_id") }, inverseJoinColumns = {
                    @javax.persistence.JoinColumn(name = "tag_id") })
    private Set<LsTag> lsTags = new HashSet<LsTag>();

    @OneToMany(cascade = { javax.persistence.CascadeType.PERSIST,
            javax.persistence.CascadeType.MERGE }, mappedBy = "secondProtocol", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<ItxProtocolProtocol> firstProtocols = new HashSet<ItxProtocolProtocol>();

    @OneToMany(cascade = { javax.persistence.CascadeType.PERSIST,
            javax.persistence.CascadeType.MERGE }, mappedBy = "firstProtocol", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<ItxProtocolProtocol> secondProtocols = new HashSet<ItxProtocolProtocol>();

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
                logger.debug("udpating tags: " + lsTag);
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
        logger.debug("attempting to merge protocol");
        if (logger.isDebugEnabled())
            logger.debug(updatedProtocol.toPrettyJson());
        updatedProtocol.merge();
        logger.debug("successfully merged protocol");
        return updatedProtocol;
    }

    public static List<com.labsynch.labseer.domain.Protocol> findProtocolByProtocolName(String protocolName) {
        List<ProtocolLabel> foundProtocolLabels = ProtocolLabel.findProtocolLabelsByName(protocolName).getResultList();
        List<Protocol> protocolList = new ArrayList<Protocol>();
        for (ProtocolLabel protocolLabel : foundProtocolLabels) {
            Protocol protocol = Protocol.findProtocol(protocolLabel.getProtocol().getId());
            if (!protocol.isIgnored())
                protocolList.add(protocol);
        }
        return protocolList;
    }

    @Transactional
    public String toJsonStub() {
        return new JSONSerializer().exclude("*.class", "lsStates.lsValues.lsState", "lsStates.protocol")
                .include("lsTags", "lsLabels", "lsStates.lsValues").transform(new ExcludeNulls(), void.class)
                .serialize(this);
    }

    @Transactional
    public String toPrettyJsonStub() {
        return new JSONSerializer().exclude("*.class", "lsStates.lsValues.lsState", "lsStates.protocol")
                .include("lsTags", "lsLabels", "lsStates.lsValues").prettyPrint(true)
                .transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class", "lsStates.lsValues.lsState", "lsStates.protocol", "experiment.protocol")
                .include("lsTags", "lsLabels", "lsStates.lsValues", "experiments.lsLabels",
                        "experiments.lsStates.lsValues")
                .prettyPrint(false).transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public String toPrettyJson() {
        return new JSONSerializer()
                .exclude("*.class", "lsStates.lsValues.lsState", "lsStates.protocol", "experiment.protocol")
                .include("lsTags", "lsLabels", "lsStates.lsValues", "experiments.lsLabels",
                        "experiments.lsStates.lsValues")
                .prettyPrint(true).transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public static String toJsonArray(Collection<com.labsynch.labseer.domain.Protocol> collection, boolean prettyJson,
            boolean includeExperiments) {
        if (includeExperiments) {
            if (prettyJson) {
                return toPrettyJsonArray(collection);
            } else {
                return toJsonArray(collection);
            }
        } else {
            if (prettyJson) {
                return toPrettyJsonArrayStub(collection);
            } else {
                return toJsonArrayStub(collection);
            }
        }
    }

    @Transactional
    public static String toPrettyJsonArray(Collection<com.labsynch.labseer.domain.Protocol> collection) {
        return new JSONSerializer()
                .exclude("*.class", "lsStates.lsValues.lsState", "lsStates.protocol", "experiment.protocol")
                .include("lsTags", "lsLabels", "lsStates.lsValues", "experiments.lsLabels",
                        "experiments.lsStates.lsValues")
                .prettyPrint(true).transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    @Transactional
    public static String toPrettyJsonArrayStub(Collection<com.labsynch.labseer.domain.Protocol> collection) {
        return new JSONSerializer().exclude("*.class", "lsStates.lsValues.lsState", "lsStates.protocol")
                .include("lsTags", "lsLabels", "lsStates.lsValues").prettyPrint(true)
                .transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    @Transactional
    public static String toJsonArray(Collection<com.labsynch.labseer.domain.Protocol> collection) {
        return new JSONSerializer().exclude("*.class").include("lsTags", "lsLabels", "lsStates.lsValues")
                .transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    @Transactional
    public static String toJsonArrayStub(Collection<com.labsynch.labseer.domain.Protocol> collection) {
        return new JSONSerializer().exclude("*.class").include("lsTags", "lsLabels", "lsStates.lsValues")
                .transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    public static com.labsynch.labseer.domain.Protocol fromJsonToProtocol(String json) {
        return new JSONDeserializer<Protocol>().use(null, Protocol.class).deserialize(json);
    }

    public static Collection<com.labsynch.labseer.domain.Protocol> fromJsonArrayToProtocols(String json) {
        return new JSONDeserializer<List<Protocol>>().use(null, ArrayList.class).use("values", Protocol.class)
                .deserialize(json);
    }

    public static Collection<com.labsynch.labseer.domain.Protocol> fromJsonArrayToProtocols(Reader json) {
        return new JSONDeserializer<List<Protocol>>().use(null, ArrayList.class).use("values", Protocol.class)
                .deserialize(json);
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

    public static com.labsynch.labseer.domain.Protocol findProtocol(Long id) {
        if (id == null)
            return null;
        else if (entityManager().find(Protocol.class, id) == null)
            return null;
        return entityManager().find(Protocol.class, id);
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
                codeTable.setId(protocol.getId());
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
                codeTable.setId(protocolLabel.getProtocol().getId());
                codeTable.setIgnored(protocolLabel.isIgnored());
                codeTableList.add(codeTable);
            }
        }
        return codeTableList;
    }

    public static List<com.labsynch.labseer.dto.CodeTableDTO> getProtocolCodeTableByKindEquals(String lsKind) {
        List<CodeTableDTO> codeTableList = new ArrayList<CodeTableDTO>();
        for (Protocol protocol : Protocol.findProtocolsByLsKindEquals(lsKind).getResultList()) {
            if (!protocol.isIgnored()) {
                CodeTableDTO codeTable = new CodeTableDTO();
                codeTable.setName(protocol.findPreferredName());
                codeTable.setCode(protocol.getCodeName());
                codeTable.setId(protocol.getId());
                codeTable.setIgnored(protocol.isIgnored());
                codeTableList.add(codeTable);
            }
        }
        return codeTableList;
    }

    public String findPreferredName() {
        if (this.getId() == null) {
            logger.debug("attempting to get the protocol preferred name -- but it is null");
            return " ";
        } else {
            List<ProtocolLabel> labels = ProtocolLabel.findProtocolPreferredName(this.getId()).getResultList();
            if (labels.size() == 1) {
                return labels.get(0).getLabelText();
            } else if (labels.size() > 1) {
                logger.error("ERROR: found mulitiple preferred names: " + labels.size());
                return labels.get(0).getLabelText();
            } else {
                logger.error("ERROR: no preferred name found");
                return " ";
            }
        }
    }

    public static Long countFindProtocolsByCodeNameEquals(String codeName) {
        if (codeName == null || codeName.length() == 0)
            throw new IllegalArgumentException("The codeName argument is required");
        EntityManager em = Protocol.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Protocol AS o WHERE o.codeName = :codeName", Long.class);
        q.setParameter("codeName", codeName);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindProtocolsByCodeNameEqualsAndIgnoredNot(String codeName, boolean ignored) {
        if (codeName == null || codeName.length() == 0)
            throw new IllegalArgumentException("The codeName argument is required");
        EntityManager em = Protocol.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM Protocol AS o WHERE o.codeName = :codeName  AND o.ignored IS NOT :ignored",
                Long.class);
        q.setParameter("codeName", codeName);
        q.setParameter("ignored", ignored);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindProtocolsByCodeNameLike(String codeName) {
        if (codeName == null || codeName.length() == 0)
            throw new IllegalArgumentException("The codeName argument is required");
        codeName = codeName.replace('*', '%');
        if (codeName.charAt(0) != '%') {
            codeName = "%" + codeName;
        }
        if (codeName.charAt(codeName.length() - 1) != '%') {
            codeName = codeName + "%";
        }
        EntityManager em = Protocol.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM Protocol AS o WHERE LOWER(o.codeName) LIKE LOWER(:codeName)", Long.class);
        q.setParameter("codeName", codeName);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindProtocolsByIgnoredNot(boolean ignored) {
        EntityManager em = Protocol.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Protocol AS o WHERE o.ignored IS NOT :ignored", Long.class);
        q.setParameter("ignored", ignored);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindProtocolsByLsKindEquals(String lsKind) {
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = Protocol.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Protocol AS o WHERE o.lsKind = :lsKind", Long.class);
        q.setParameter("lsKind", lsKind);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindProtocolsByLsKindLike(String lsKind) {
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        lsKind = lsKind.replace('*', '%');
        if (lsKind.charAt(0) != '%') {
            lsKind = "%" + lsKind;
        }
        if (lsKind.charAt(lsKind.length() - 1) != '%') {
            lsKind = lsKind + "%";
        }
        EntityManager em = Protocol.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Protocol AS o WHERE LOWER(o.lsKind) LIKE LOWER(:lsKind)",
                Long.class);
        q.setParameter("lsKind", lsKind);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindProtocolsByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null)
            throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = Protocol.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Protocol AS o WHERE o.lsTransaction = :lsTransaction",
                Long.class);
        q.setParameter("lsTransaction", lsTransaction);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindProtocolsByLsTypeAndKindEquals(String lsTypeAndKind) {
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0)
            throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = Protocol.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Protocol AS o WHERE o.lsTypeAndKind = :lsTypeAndKind",
                Long.class);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindProtocolsByLsTypeEquals(String lsType) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = Protocol.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Protocol AS o WHERE o.lsType = :lsType", Long.class);
        q.setParameter("lsType", lsType);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindProtocolsByLsTypeEqualsAndLsKindEquals(String lsType, String lsKind) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = Protocol.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM Protocol AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind", Long.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindProtocolsByLsTypeLike(String lsType) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        lsType = lsType.replace('*', '%');
        if (lsType.charAt(0) != '%') {
            lsType = "%" + lsType;
        }
        if (lsType.charAt(lsType.length() - 1) != '%') {
            lsType = lsType + "%";
        }
        EntityManager em = Protocol.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Protocol AS o WHERE LOWER(o.lsType) LIKE LOWER(:lsType)",
                Long.class);
        q.setParameter("lsType", lsType);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindProtocolsByRecordedByLike(String recordedBy) {
        if (recordedBy == null || recordedBy.length() == 0)
            throw new IllegalArgumentException("The recordedBy argument is required");
        recordedBy = recordedBy.replace('*', '%');
        if (recordedBy.charAt(0) != '%') {
            recordedBy = "%" + recordedBy;
        }
        if (recordedBy.charAt(recordedBy.length() - 1) != '%') {
            recordedBy = recordedBy + "%";
        }
        EntityManager em = Protocol.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM Protocol AS o WHERE LOWER(o.recordedBy) LIKE LOWER(:recordedBy)", Long.class);
        q.setParameter("recordedBy", recordedBy);
        return ((Long) q.getSingleResult());
    }

    public static TypedQuery<Protocol> findProtocolsByCodeNameEquals(String codeName) {
        if (codeName == null || codeName.length() == 0)
            throw new IllegalArgumentException("The codeName argument is required");
        EntityManager em = Protocol.entityManager();
        TypedQuery<Protocol> q = em.createQuery("SELECT o FROM Protocol AS o WHERE o.codeName = :codeName",
                Protocol.class);
        q.setParameter("codeName", codeName);
        return q;
    }

    public static TypedQuery<Protocol> findProtocolsByCodeNameEquals(String codeName, String sortFieldName,
            String sortOrder) {
        if (codeName == null || codeName.length() == 0)
            throw new IllegalArgumentException("The codeName argument is required");
        EntityManager em = Protocol.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Protocol AS o WHERE o.codeName = :codeName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Protocol> q = em.createQuery(queryBuilder.toString(), Protocol.class);
        q.setParameter("codeName", codeName);
        return q;
    }

    public static TypedQuery<Protocol> findProtocolsByCodeNameEqualsAndIgnoredNot(String codeName, boolean ignored) {
        if (codeName == null || codeName.length() == 0)
            throw new IllegalArgumentException("The codeName argument is required");
        EntityManager em = Protocol.entityManager();
        TypedQuery<Protocol> q = em.createQuery(
                "SELECT o FROM Protocol AS o WHERE o.codeName = :codeName  AND o.ignored IS NOT :ignored",
                Protocol.class);
        q.setParameter("codeName", codeName);
        q.setParameter("ignored", ignored);
        return q;
    }

    public static TypedQuery<Protocol> findProtocolsByCodeNameEqualsAndIgnoredNot(String codeName, boolean ignored,
            String sortFieldName, String sortOrder) {
        if (codeName == null || codeName.length() == 0)
            throw new IllegalArgumentException("The codeName argument is required");
        EntityManager em = Protocol.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM Protocol AS o WHERE o.codeName = :codeName  AND o.ignored IS NOT :ignored");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Protocol> q = em.createQuery(queryBuilder.toString(), Protocol.class);
        q.setParameter("codeName", codeName);
        q.setParameter("ignored", ignored);
        return q;
    }

    public static TypedQuery<Protocol> findProtocolsByCodeNameLike(String codeName) {
        if (codeName == null || codeName.length() == 0)
            throw new IllegalArgumentException("The codeName argument is required");
        codeName = codeName.replace('*', '%');
        if (codeName.charAt(0) != '%') {
            codeName = "%" + codeName;
        }
        if (codeName.charAt(codeName.length() - 1) != '%') {
            codeName = codeName + "%";
        }
        EntityManager em = Protocol.entityManager();
        TypedQuery<Protocol> q = em.createQuery(
                "SELECT o FROM Protocol AS o WHERE LOWER(o.codeName) LIKE LOWER(:codeName)", Protocol.class);
        q.setParameter("codeName", codeName);
        return q;
    }

    public static TypedQuery<Protocol> findProtocolsByCodeNameLike(String codeName, String sortFieldName,
            String sortOrder) {
        if (codeName == null || codeName.length() == 0)
            throw new IllegalArgumentException("The codeName argument is required");
        codeName = codeName.replace('*', '%');
        if (codeName.charAt(0) != '%') {
            codeName = "%" + codeName;
        }
        if (codeName.charAt(codeName.length() - 1) != '%') {
            codeName = codeName + "%";
        }
        EntityManager em = Protocol.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM Protocol AS o WHERE LOWER(o.codeName) LIKE LOWER(:codeName)");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Protocol> q = em.createQuery(queryBuilder.toString(), Protocol.class);
        q.setParameter("codeName", codeName);
        return q;
    }

    public static TypedQuery<Protocol> findProtocolsByIgnoredNot(boolean ignored) {
        EntityManager em = Protocol.entityManager();
        TypedQuery<Protocol> q = em.createQuery("SELECT o FROM Protocol AS o WHERE o.ignored IS NOT :ignored",
                Protocol.class);
        q.setParameter("ignored", ignored);
        return q;
    }

    public static TypedQuery<Protocol> findProtocolsByIgnoredNot(boolean ignored, String sortFieldName,
            String sortOrder) {
        EntityManager em = Protocol.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Protocol AS o WHERE o.ignored IS NOT :ignored");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Protocol> q = em.createQuery(queryBuilder.toString(), Protocol.class);
        q.setParameter("ignored", ignored);
        return q;
    }

    public static TypedQuery<Protocol> findProtocolsByLsKindEquals(String lsKind) {
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = Protocol.entityManager();
        TypedQuery<Protocol> q = em.createQuery("SELECT o FROM Protocol AS o WHERE o.lsKind = :lsKind", Protocol.class);
        q.setParameter("lsKind", lsKind);
        return q;
    }

    public static TypedQuery<Protocol> findProtocolsByLsKindEquals(String lsKind, String sortFieldName,
            String sortOrder) {
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = Protocol.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Protocol AS o WHERE o.lsKind = :lsKind");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Protocol> q = em.createQuery(queryBuilder.toString(), Protocol.class);
        q.setParameter("lsKind", lsKind);
        return q;
    }

    public static TypedQuery<Protocol> findProtocolsByLsKindLike(String lsKind) {
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        lsKind = lsKind.replace('*', '%');
        if (lsKind.charAt(0) != '%') {
            lsKind = "%" + lsKind;
        }
        if (lsKind.charAt(lsKind.length() - 1) != '%') {
            lsKind = lsKind + "%";
        }
        EntityManager em = Protocol.entityManager();
        TypedQuery<Protocol> q = em.createQuery("SELECT o FROM Protocol AS o WHERE LOWER(o.lsKind) LIKE LOWER(:lsKind)",
                Protocol.class);
        q.setParameter("lsKind", lsKind);
        return q;
    }

    public static TypedQuery<Protocol> findProtocolsByLsKindLike(String lsKind, String sortFieldName,
            String sortOrder) {
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        lsKind = lsKind.replace('*', '%');
        if (lsKind.charAt(0) != '%') {
            lsKind = "%" + lsKind;
        }
        if (lsKind.charAt(lsKind.length() - 1) != '%') {
            lsKind = lsKind + "%";
        }
        EntityManager em = Protocol.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM Protocol AS o WHERE LOWER(o.lsKind) LIKE LOWER(:lsKind)");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Protocol> q = em.createQuery(queryBuilder.toString(), Protocol.class);
        q.setParameter("lsKind", lsKind);
        return q;
    }

    public static TypedQuery<Protocol> findProtocolsByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null)
            throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = Protocol.entityManager();
        TypedQuery<Protocol> q = em.createQuery("SELECT o FROM Protocol AS o WHERE o.lsTransaction = :lsTransaction",
                Protocol.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

    public static TypedQuery<Protocol> findProtocolsByLsTransactionEquals(Long lsTransaction, String sortFieldName,
            String sortOrder) {
        if (lsTransaction == null)
            throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = Protocol.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM Protocol AS o WHERE o.lsTransaction = :lsTransaction");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Protocol> q = em.createQuery(queryBuilder.toString(), Protocol.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

    public static TypedQuery<Protocol> findProtocolsByLsTypeAndKindEquals(String lsTypeAndKind) {
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0)
            throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = Protocol.entityManager();
        TypedQuery<Protocol> q = em.createQuery("SELECT o FROM Protocol AS o WHERE o.lsTypeAndKind = :lsTypeAndKind",
                Protocol.class);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        return q;
    }

    public static TypedQuery<Protocol> findProtocolsByLsTypeAndKindEquals(String lsTypeAndKind, String sortFieldName,
            String sortOrder) {
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0)
            throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = Protocol.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM Protocol AS o WHERE o.lsTypeAndKind = :lsTypeAndKind");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Protocol> q = em.createQuery(queryBuilder.toString(), Protocol.class);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        return q;
    }

    public static TypedQuery<Protocol> findProtocolsByLsTypeEquals(String lsType) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = Protocol.entityManager();
        TypedQuery<Protocol> q = em.createQuery("SELECT o FROM Protocol AS o WHERE o.lsType = :lsType", Protocol.class);
        q.setParameter("lsType", lsType);
        return q;
    }

    public static TypedQuery<Protocol> findProtocolsByLsTypeEquals(String lsType, String sortFieldName,
            String sortOrder) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = Protocol.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Protocol AS o WHERE o.lsType = :lsType");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Protocol> q = em.createQuery(queryBuilder.toString(), Protocol.class);
        q.setParameter("lsType", lsType);
        return q;
    }

    public static TypedQuery<Protocol> findProtocolsByLsTypeEqualsAndLsKindEquals(String lsType, String lsKind) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = Protocol.entityManager();
        TypedQuery<Protocol> q = em.createQuery(
                "SELECT o FROM Protocol AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind", Protocol.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return q;
    }

    public static TypedQuery<Protocol> findProtocolsByLsTypeEqualsAndLsKindEquals(String lsType, String lsKind,
            String sortFieldName, String sortOrder) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = Protocol.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM Protocol AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Protocol> q = em.createQuery(queryBuilder.toString(), Protocol.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return q;
    }

    public static TypedQuery<Protocol> findProtocolsByLsTypeLike(String lsType) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        lsType = lsType.replace('*', '%');
        if (lsType.charAt(0) != '%') {
            lsType = "%" + lsType;
        }
        if (lsType.charAt(lsType.length() - 1) != '%') {
            lsType = lsType + "%";
        }
        EntityManager em = Protocol.entityManager();
        TypedQuery<Protocol> q = em.createQuery("SELECT o FROM Protocol AS o WHERE LOWER(o.lsType) LIKE LOWER(:lsType)",
                Protocol.class);
        q.setParameter("lsType", lsType);
        return q;
    }

    public static TypedQuery<Protocol> findProtocolsByLsTypeLike(String lsType, String sortFieldName,
            String sortOrder) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        lsType = lsType.replace('*', '%');
        if (lsType.charAt(0) != '%') {
            lsType = "%" + lsType;
        }
        if (lsType.charAt(lsType.length() - 1) != '%') {
            lsType = lsType + "%";
        }
        EntityManager em = Protocol.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM Protocol AS o WHERE LOWER(o.lsType) LIKE LOWER(:lsType)");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Protocol> q = em.createQuery(queryBuilder.toString(), Protocol.class);
        q.setParameter("lsType", lsType);
        return q;
    }

    public static TypedQuery<Protocol> findProtocolsByRecordedByLike(String recordedBy) {
        if (recordedBy == null || recordedBy.length() == 0)
            throw new IllegalArgumentException("The recordedBy argument is required");
        recordedBy = recordedBy.replace('*', '%');
        if (recordedBy.charAt(0) != '%') {
            recordedBy = "%" + recordedBy;
        }
        if (recordedBy.charAt(recordedBy.length() - 1) != '%') {
            recordedBy = recordedBy + "%";
        }
        EntityManager em = Protocol.entityManager();
        TypedQuery<Protocol> q = em.createQuery(
                "SELECT o FROM Protocol AS o WHERE LOWER(o.recordedBy) LIKE LOWER(:recordedBy)", Protocol.class);
        q.setParameter("recordedBy", recordedBy);
        return q;
    }

    public static TypedQuery<Protocol> findProtocolsByRecordedByLike(String recordedBy, String sortFieldName,
            String sortOrder) {
        if (recordedBy == null || recordedBy.length() == 0)
            throw new IllegalArgumentException("The recordedBy argument is required");
        recordedBy = recordedBy.replace('*', '%');
        if (recordedBy.charAt(0) != '%') {
            recordedBy = "%" + recordedBy;
        }
        if (recordedBy.charAt(recordedBy.length() - 1) != '%') {
            recordedBy = recordedBy + "%";
        }
        EntityManager em = Protocol.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM Protocol AS o WHERE LOWER(o.recordedBy) LIKE LOWER(:recordedBy)");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Protocol> q = em.createQuery(queryBuilder.toString(), Protocol.class);
        q.setParameter("recordedBy", recordedBy);
        return q;
    }

    public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .setExcludeFieldNames("lsTags", "lsStates", "experiments", "lsLabels").toString();
    }

    public String getShortDescription() {
        return this.shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public Set<ProtocolState> getLsStates() {
        return this.lsStates;
    }

    public void setLsStates(Set<ProtocolState> lsStates) {
        this.lsStates = lsStates;
    }

    public Set<Experiment> getExperiments() {
        return this.experiments;
    }

    public void setExperiments(Set<Experiment> experiments) {
        this.experiments = experiments;
    }

    public Set<ProtocolLabel> getLsLabels() {
        return this.lsLabels;
    }

    public void setLsLabels(Set<ProtocolLabel> lsLabels) {
        this.lsLabels = lsLabels;
    }

    public Set<LsTag> getLsTags() {
        return this.lsTags;
    }

    public void setLsTags(Set<LsTag> lsTags) {
        this.lsTags = lsTags;
    }

    public Set<ItxProtocolProtocol> getFirstProtocols() {
        return this.firstProtocols;
    }

    public void setFirstProtocols(Set<ItxProtocolProtocol> firstProtocols) {
        this.firstProtocols = firstProtocols;
    }

    public Set<ItxProtocolProtocol> getSecondProtocols() {
        return this.secondProtocols;
    }

    public void setSecondProtocols(Set<ItxProtocolProtocol> secondProtocols) {
        this.secondProtocols = secondProtocols;
    }

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger",
            "shortDescription", "lsStates", "experiments", "lsLabels", "lsTags", "firstProtocols", "secondProtocols");

    public static long countProtocols() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Protocol o", Long.class).getSingleResult();
    }

    public static List<Protocol> findAllProtocols() {
        return entityManager().createQuery("SELECT o FROM Protocol o", Protocol.class).getResultList();
    }

    public static List<Protocol> findAllProtocols(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Protocol o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Protocol.class).getResultList();
    }

    public static List<Protocol> findProtocolEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Protocol o", Protocol.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    public static List<Protocol> findProtocolEntries(int firstResult, int maxResults, String sortFieldName,
            String sortOrder) {
        String jpaQuery = "SELECT o FROM Protocol o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Protocol.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public Protocol merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        Protocol merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
