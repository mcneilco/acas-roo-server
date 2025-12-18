package com.labsynch.labseer.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.labsynch.labseer.dto.CodeTableDTO;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Configurable
@Entity
public class DDictValue {

    private static final Logger logger = LoggerFactory.getLogger(DDictValue.class);

    @NotNull
    @Size(max = 255)
    private String lsType;

    @NotNull
    @Size(max = 255)
    private String lsKind;

    @Size(max = 255)
    private String lsTypeAndKind;

    @Size(max = 256)
    private String shortName;

    @NotNull
    @Size(max = 512)
    private String labelText;

    @Size(max = 512)
    private String description;

    @Size(max = 512)
    private String comments;

    @NotNull
    private boolean ignored;

    private Integer displayOrder;

    @NotNull
    @Size(max = 255)
    private String codeName;

    @Id
    @SequenceGenerator(name = "dDictValueGen", sequenceName = "DDICT_VALUE_PKSEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "dDictValueGen")
    @Column(name = "id")
    private Long id;

    @Version
    @Column(name = "version")
    private Integer version;

    @PersistenceContext
    transient EntityManager entityManager;

    public DDictValue() {
    }

    public DDictValue(DDictValue dDict) {
        this.lsType = dDict.getLsType();
        this.lsKind = dDict.getLsKind();
        this.shortName = dDict.getShortName();
        this.labelText = dDict.getLabelText();
        this.description = dDict.getDescription();
        this.comments = dDict.getComments();
        this.displayOrder = dDict.getDisplayOrder();
        this.ignored = dDict.getIgnored();
    }

    public DDictValue(CodeTableDTO codeTableValue) {
        this.lsType = codeTableValue.getCodeType();
        this.lsKind = codeTableValue.getCodeKind();
        this.shortName = codeTableValue.getCode();
        this.labelText = codeTableValue.getName();
        this.displayOrder = codeTableValue.getDisplayOrder();
        this.description = codeTableValue.getDescription();
        this.comments = codeTableValue.getComments();
        this.ignored = codeTableValue.isIgnored();

    }

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

    public String getCodeName() {
        return this.codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public boolean getIgnored() {
        return this.ignored;
    }

    public static final EntityManager entityManager() {
        EntityManager em = new DDictValue().entityManager;
        if (em == null)
            throw new IllegalStateException(
                    "Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countDDictValues() {
        return entityManager().createQuery("SELECT COUNT(o) FROM DDictValue o", Long.class).getSingleResult();
    }

    public static List<com.labsynch.labseer.domain.DDictValue> findAllDDictValues() {
        return entityManager().createQuery("SELECT o FROM DDictValue o", DDictValue.class).getResultList();
    }

    public static com.labsynch.labseer.domain.DDictValue findDDictValue(Long id) {
        if (id == null)
            return null;
        return entityManager().find(DDictValue.class, id);
    }

    public static List<com.labsynch.labseer.domain.DDictValue> findDDictValueEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM DDictValue o", DDictValue.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public void persist() {
        this.setLsTypeAndKind(new StringBuilder().append(this.lsType).append("_").append(this.lsKind).toString());
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
            DDictValue attached = DDictValue.findDDictValue(this.id);
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
    public com.labsynch.labseer.domain.DDictValue merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        this.setLsTypeAndKind(new StringBuilder().append(this.lsType).append("_").append(this.lsKind).toString());
        DDictValue merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public String toJson() {
        return new JSONSerializer().exclude("*.class", "lsTypeAndKind").serialize(this);
    }

    public static com.labsynch.labseer.domain.DDictValue fromJsonToDDictValue(String json) {
        return new JSONDeserializer<DDictValue>().use(null, DDictValue.class).deserialize(json);
    }

    public static String toJsonArray(Collection<com.labsynch.labseer.domain.DDictValue> collection) {
        return new JSONSerializer().exclude("*.class", "lsTypeAndKind").serialize(collection);
    }

    public static Collection<com.labsynch.labseer.domain.DDictValue> fromJsonArrayToDDictValues(String json) {
        return new JSONDeserializer<List<DDictValue>>().use(null, ArrayList.class).use("values", DDictValue.class)
                .deserialize(json);
    }

    public static List<com.labsynch.labseer.dto.CodeTableDTO> getDDictValueCodeTableByKindEquals(String lsKind) {
        List<CodeTableDTO> codeTableList = new ArrayList<CodeTableDTO>();
        for (DDictValue val : DDictValue.findDDictValuesByLsKindEquals(lsKind).getResultList()) {
            if (!val.ignored) {
                CodeTableDTO codeTable = new CodeTableDTO();
                codeTable.setName(val.labelText);
                codeTable.setCode(val.getShortName());
                codeTable.setIgnored(val.ignored);
                codeTable.setDisplayOrder(val.displayOrder);
                codeTableList.add(codeTable);
            }
        }
        return codeTableList;
    }

    public static TypedQuery<DDictValue> findDDictValuesByLsKindEquals(String lsKind) {
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = Experiment.entityManager();
        TypedQuery<DDictValue> q = em.createQuery("SELECT o FROM DDictValue AS o WHERE o.lsKind = :lsKind",
                DDictValue.class);
        q.setParameter("lsKind", lsKind);
        return q;
    }

    @Transactional
    public static List<com.labsynch.labseer.dto.CodeTableDTO> getDDictCodeTable() {
        List<CodeTableDTO> codeTableList = new ArrayList<CodeTableDTO>();
        List<DDictValue> dDicts = DDictValue.findDDictValuesByIgnoredNot(true).getResultList();
        for (DDictValue val : dDicts) {
            CodeTableDTO codeTable = new CodeTableDTO(val);
            codeTableList.add(codeTable);
        }
        return codeTableList;
    }

    public static String[] getColumns() {
        String[] headerColumns = new String[] { "id", "codeName", "shortName", "lsType", "lsKind", "labelText",
                "description", "comments", "ignored", "displayOrder" };
        return headerColumns;
    }

    public static CellProcessor[] getProcessors() {
        final CellProcessor[] processors = new CellProcessor[] { new Optional(), new Optional(), new Optional(),
                new Optional(), new Optional(), new Optional(), new Optional(), new Optional(), new Optional(),
                new Optional() };
        return processors;
    }

    public static boolean validate(DDictValue dDict) {
        boolean validLsType = validateLsType(dDict.getLsType());
        boolean validLsKind = validateLsKind(dDict.getLsType(), dDict.getLsKind());
        if (validLsType && validLsKind) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean validateLsKind(String lsType, String lsKind) {
        int dDictKinds = DDictKind.findDDictKindsByLsTypeEqualsAndNameEquals(lsType, lsKind).getMaxResults();
        if (dDictKinds == 1) {
            return true;
        } else {
            logger.error("Did not validate the DDictKind");
            return false;
        }
    }

    private static boolean validateLsType(String lsType) {
        int dDictTypes = DDictType.findDDictTypesByNameEquals(lsType).getMaxResults();
        if (dDictTypes == 1) {
            return true;
        } else {
            logger.error("Did not validate the DDictType");
            return false;
        }
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "lsType",
            "lsKind", "lsTypeAndKind", "shortName", "labelText", "description", "comments", "ignored", "displayOrder",
            "codeName", "id", "version", "entityManager");

    public static List<DDictValue> findAllDDictValues(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM DDictValue o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, DDictValue.class).getResultList();
    }

    public static List<DDictValue> findDDictValueEntries(int firstResult, int maxResults, String sortFieldName,
            String sortOrder) {
        String jpaQuery = "SELECT o FROM DDictValue o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, DDictValue.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    public static Long countFindDDictValuesByCodeNameEquals(String codeName) {
        if (codeName == null || codeName.length() == 0)
            throw new IllegalArgumentException("The codeName argument is required");
        EntityManager em = DDictValue.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM DDictValue AS o WHERE o.codeName = :codeName", Long.class);
        q.setParameter("codeName", codeName);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindDDictValuesByIgnoredNot(boolean ignored) {
        EntityManager em = DDictValue.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM DDictValue AS o WHERE o.ignored IS NOT :ignored",
                Long.class);
        q.setParameter("ignored", ignored);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindDDictValuesByLabelTextLike(String labelText) {
        if (labelText == null || labelText.length() == 0)
            throw new IllegalArgumentException("The labelText argument is required");
        labelText = labelText.replace('*', '%');
        if (labelText.charAt(0) != '%') {
            labelText = "%" + labelText;
        }
        if (labelText.charAt(labelText.length() - 1) != '%') {
            labelText = labelText + "%";
        }
        EntityManager em = DDictValue.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM DDictValue AS o WHERE LOWER(o.labelText) LIKE LOWER(:labelText)", Long.class);
        q.setParameter("labelText", labelText);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindDDictValuesByLsKindEquals(String lsKind) {
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = DDictValue.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM DDictValue AS o WHERE o.lsKind = :lsKind", Long.class);
        q.setParameter("lsKind", lsKind);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindDDictValuesByLsTypeEquals(String lsType) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = DDictValue.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM DDictValue AS o WHERE o.lsType = :lsType", Long.class);
        q.setParameter("lsType", lsType);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindDDictValuesByLsTypeEqualsAndLsKindEquals(String lsType, String lsKind) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = DDictValue.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM DDictValue AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind", Long.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindDDictValuesByLsTypeEqualsAndLsKindEqualsAndShortNameEquals(String lsType, String lsKind,
            String shortName) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        if (shortName == null || shortName.length() == 0)
            throw new IllegalArgumentException("The shortName argument is required");
        EntityManager em = DDictValue.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM DDictValue AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind  AND o.shortName = :shortName",
                Long.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("shortName", shortName);
        return ((Long) q.getSingleResult());
    }

    public static TypedQuery<DDictValue> findDDictValuesByCodeNameEquals(String codeName) {
        if (codeName == null || codeName.length() == 0)
            throw new IllegalArgumentException("The codeName argument is required");
        EntityManager em = DDictValue.entityManager();
        TypedQuery<DDictValue> q = em.createQuery("SELECT o FROM DDictValue AS o WHERE o.codeName = :codeName",
                DDictValue.class);
        q.setParameter("codeName", codeName);
        return q;
    }

    public static TypedQuery<DDictValue> findDDictValuesByCodeNameEquals(String codeName, String sortFieldName,
            String sortOrder) {
        if (codeName == null || codeName.length() == 0)
            throw new IllegalArgumentException("The codeName argument is required");
        EntityManager em = DDictValue.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM DDictValue AS o WHERE o.codeName = :codeName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<DDictValue> q = em.createQuery(queryBuilder.toString(), DDictValue.class);
        q.setParameter("codeName", codeName);
        return q;
    }

    public static TypedQuery<DDictValue> findDDictValuesByIgnoredNot(boolean ignored) {
        EntityManager em = DDictValue.entityManager();
        TypedQuery<DDictValue> q = em.createQuery("SELECT o FROM DDictValue AS o WHERE o.ignored IS NOT :ignored",
                DDictValue.class);
        q.setParameter("ignored", ignored);
        return q;
    }

    public static TypedQuery<DDictValue> findDDictValuesByIgnoredNot(boolean ignored, String sortFieldName,
            String sortOrder) {
        EntityManager em = DDictValue.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM DDictValue AS o WHERE o.ignored IS NOT :ignored");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<DDictValue> q = em.createQuery(queryBuilder.toString(), DDictValue.class);
        q.setParameter("ignored", ignored);
        return q;
    }

    public static TypedQuery<DDictValue> findDDictValuesByLabelTextLike(String labelText) {
        if (labelText == null || labelText.length() == 0)
            throw new IllegalArgumentException("The labelText argument is required");
        labelText = labelText.replace('*', '%');
        if (labelText.charAt(0) != '%') {
            labelText = "%" + labelText;
        }
        if (labelText.charAt(labelText.length() - 1) != '%') {
            labelText = labelText + "%";
        }
        EntityManager em = DDictValue.entityManager();
        TypedQuery<DDictValue> q = em.createQuery(
                "SELECT o FROM DDictValue AS o WHERE LOWER(o.labelText) LIKE LOWER(:labelText)", DDictValue.class);
        q.setParameter("labelText", labelText);
        return q;
    }

    public static TypedQuery<DDictValue> findDDictValuesByLabelTextLike(String labelText, String sortFieldName,
            String sortOrder) {
        if (labelText == null || labelText.length() == 0)
            throw new IllegalArgumentException("The labelText argument is required");
        labelText = labelText.replace('*', '%');
        if (labelText.charAt(0) != '%') {
            labelText = "%" + labelText;
        }
        if (labelText.charAt(labelText.length() - 1) != '%') {
            labelText = labelText + "%";
        }
        EntityManager em = DDictValue.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM DDictValue AS o WHERE LOWER(o.labelText) LIKE LOWER(:labelText)");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<DDictValue> q = em.createQuery(queryBuilder.toString(), DDictValue.class);
        q.setParameter("labelText", labelText);
        return q;
    }

    public static TypedQuery<DDictValue> findDDictValuesByLsKindEquals(String lsKind, String sortFieldName,
            String sortOrder) {
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = DDictValue.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM DDictValue AS o WHERE o.lsKind = :lsKind");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<DDictValue> q = em.createQuery(queryBuilder.toString(), DDictValue.class);
        q.setParameter("lsKind", lsKind);
        return q;
    }

    public static TypedQuery<DDictValue> findDDictValuesByLsTypeEquals(String lsType) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = DDictValue.entityManager();
        TypedQuery<DDictValue> q = em.createQuery("SELECT o FROM DDictValue AS o WHERE o.lsType = :lsType",
                DDictValue.class);
        q.setParameter("lsType", lsType);
        return q;
    }

    public static TypedQuery<DDictValue> findDDictValuesByLsTypeEquals(String lsType, String sortFieldName,
            String sortOrder) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = DDictValue.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM DDictValue AS o WHERE o.lsType = :lsType");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<DDictValue> q = em.createQuery(queryBuilder.toString(), DDictValue.class);
        q.setParameter("lsType", lsType);
        return q;
    }

    public static TypedQuery<DDictValue> findDDictValuesByLsTypeEqualsAndLsKindEquals(String lsType, String lsKind) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = DDictValue.entityManager();
        TypedQuery<DDictValue> q = em.createQuery(
                "SELECT o FROM DDictValue AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind", DDictValue.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return q;
    }

    public static List<DDictValue> findDDictValuesByLsTypeEqualsAndLsKindEqualsAndLabelTextSearch(String lsType, String lsKind, String labelText, Integer maxHits) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = DDictValue.entityManager();

        // Format the search text to match any word in the label text
        // e.g. "word1 word2" -> "word1:* & word2:*"
        String[] parts = labelText.trim().split("\\s+");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i] + ":*";
        }
        String formattedLabelText = String.join(" & ", parts);
        
        // Native query to use full text search
        String sql = 
        "SELECT * " +
        "FROM ddict_value " +
        "WHERE ls_type = :lsType " +
        "AND ls_kind = :lsKind " +
        "AND to_tsvector('english', lower(label_text)) @@ to_tsquery('english', :formattedLabelText) " +
        "ORDER BY (lower(label_text) = :labelText) DESC, " +
        "ts_rank(to_tsvector('english', lower(label_text)), to_tsquery('english', :formattedLabelText)) DESC";

        Query q = em.createNativeQuery(sql, DDictValue.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("labelText", labelText.toLowerCase());
        q.setParameter("formattedLabelText", formattedLabelText);
        if (maxHits != null) {
            q = q.setMaxResults(maxHits);
        }

        @SuppressWarnings("unchecked")
        List<DDictValue> results = q.getResultList();
        return results;
    }

    public static TypedQuery<DDictValue> findDDictValuesByLsTypeEqualsAndLsKindEquals(String lsType, String lsKind,
            String sortFieldName, String sortOrder) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = DDictValue.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM DDictValue AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<DDictValue> q = em.createQuery(queryBuilder.toString(), DDictValue.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return q;
    }

    public static TypedQuery<DDictValue> findDDictValuesByLsTypeEqualsAndLsKindEqualsAndShortNameEquals(String lsType,
            String lsKind, String shortName) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        if (shortName == null || shortName.length() == 0)
            throw new IllegalArgumentException("The shortName argument is required");
        EntityManager em = DDictValue.entityManager();
        TypedQuery<DDictValue> q = em.createQuery(
                "SELECT o FROM DDictValue AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind  AND o.shortName = :shortName",
                DDictValue.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("shortName", shortName);
        return q;
    }

    public static TypedQuery<DDictValue> findDDictValuesByLsTypeEqualsAndLsKindEqualsAndShortNameEquals(String lsType,
            String lsKind, String shortName, String sortFieldName, String sortOrder) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        if (shortName == null || shortName.length() == 0)
            throw new IllegalArgumentException("The shortName argument is required");
        EntityManager em = DDictValue.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM DDictValue AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind  AND o.shortName = :shortName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<DDictValue> q = em.createQuery(queryBuilder.toString(), DDictValue.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("shortName", shortName);
        return q;
    }

    public String getLsType() {
        return this.lsType;
    }

    public void setLsType(String lsType) {
        this.lsType = lsType;
    }

    public String getLsKind() {
        return this.lsKind;
    }

    public void setLsKind(String lsKind) {
        this.lsKind = lsKind;
    }

    public String getLsTypeAndKind() {
        return this.lsTypeAndKind;
    }

    public void setLsTypeAndKind(String lsTypeAndKind) {
        this.lsTypeAndKind = lsTypeAndKind;
    }

    public String getShortName() {
        return this.shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLabelText() {
        return this.labelText;
    }

    public void setLabelText(String labelText) {
        this.labelText = labelText;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isIgnored() {
        return this.ignored;
    }

    public void setIgnored(boolean ignored) {
        this.ignored = ignored;
    }

    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}
