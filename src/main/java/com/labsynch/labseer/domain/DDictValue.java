package com.labsynch.labseer.domain;

import com.labsynch.labseer.dto.CodeTableDTO;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.SequenceGenerator;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Index;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;

@Entity
@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(sequenceName = "DDICT_VALUE_PKSEQ", finders = { "findDDictValuesByCodeNameEquals", 
		"findDDictValuesByLsTypeEqualsAndLsKindEquals", "findDDictValuesByLsTypeEquals", 
		"findDDictValuesByLsTypeEqualsAndLsKindEqualsAndShortNameEquals",
		"findDDictValuesByLsKindEquals", "findDDictValuesByIgnoredNot", "findDDictValuesByLabelTextLike" })
public class DDictValue {

    private static final Logger logger = LoggerFactory.getLogger(DDictValue.class);
    
    @NotNull
    @Index(name = "DD_VALUE_TYPE_IDX")
    @Size(max = 255)
    private String lsType;

    @NotNull
    @Index(name = "DD_VALUE_KIND_IDX")
    @Size(max = 255)
    private String lsKind;

    @Size(max = 255)
    @Index(name = "DD_VALUE_TK_IDX")
    private String lsTypeAndKind;

    @Size(max = 256)
    @Index(name = "DD_VALUE_SNAME_IDX")
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
    @Index(name = "DD_VALUE_CODE_IDX")
    private String codeName;

    @Id
    @SequenceGenerator(name = "dDictValueGen", sequenceName = "DDICT_VALUE_PKSEQ")
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
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countDDictValues() {
        return entityManager().createQuery("SELECT COUNT(o) FROM DDictValue o", Long.class).getSingleResult();
    }

    public static List<com.labsynch.labseer.domain.DDictValue> findAllDDictValues() {
        return entityManager().createQuery("SELECT o FROM DDictValue o", DDictValue.class).getResultList();
    }

    public static com.labsynch.labseer.domain.DDictValue findDDictValue(Long id) {
        if (id == null) return null;
        return entityManager().find(DDictValue.class, id);
    }

    public static List<com.labsynch.labseer.domain.DDictValue> findDDictValueEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM DDictValue o", DDictValue.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public void persist() {
        this.setLsTypeAndKind(new StringBuilder().append(this.lsType).append("_").append(this.lsKind).toString());
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

    @Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            DDictValue attached = DDictValue.findDDictValue(this.id);
            this.entityManager.remove(attached);
        }
    }

    @Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

    @Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

    @Transactional
    public com.labsynch.labseer.domain.DDictValue merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
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
        return new JSONDeserializer<List<DDictValue>>().use(null, ArrayList.class).use("values", DDictValue.class).deserialize(json);
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
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = Experiment.entityManager();
        TypedQuery<DDictValue> q = em.createQuery("SELECT o FROM DDictValue AS o WHERE o.lsKind = :lsKind", DDictValue.class);
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
        String[] headerColumns = new String[] { "id", "codeName", "shortName", "lsType", "lsKind", "labelText", "description", "comments", "ignored", "displayOrder" };
        return headerColumns;
    }

    public static CellProcessor[] getProcessors() {
        final CellProcessor[] processors = new CellProcessor[] { new Optional(), new Optional(), new Optional(), new Optional(), new Optional(), new Optional(), new Optional(), new Optional(), new Optional(), new Optional() };
        return processors;
    }

	public static boolean validate(DDictValue dDict) {
		boolean validLsType = validateLsType(dDict.getLsType());
		boolean validLsKind = validateLsKind(dDict.getLsType(), dDict.getLsKind());
		if (validLsType && validLsKind){
			return true;
		} else {
			return false;
		}
	}

	private static boolean validateLsKind(String lsType, String lsKind) {
		int dDictKinds = DDictKind.findDDictKindsByLsTypeEqualsAndNameEquals(lsType, lsKind).getMaxResults();
		if (dDictKinds == 1){
			return true;
		} else {
			logger.error("Did not validate the DDictKind");
			return false;
		}
	}
	

	private static boolean validateLsType(String lsType) {
		int dDictTypes = DDictType.findDDictTypesByNameEquals(lsType).getMaxResults();
		if (dDictTypes == 1){
			return true;
		} else {
			logger.error("Did not validate the DDictType");
			return false;
		}
	}
}
