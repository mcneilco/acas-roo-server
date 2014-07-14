package com.labsynch.labseer.domain;

import com.labsynch.labseer.dto.AutoLabelDTO;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.service.AutoLabelService;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Index;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@Entity
@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(sequenceName = "DDICT_VALUE_PKSEQ", finders = { "findDDictValuesByLsTypeEqualsAndLsKindEquals", "findDDictValuesByLsTypeEquals", "findDDictValuesByLsKindEquals", "findDDictValuesByIgnoredNot" })
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

    @NotNull
    @Size(max = 512)
    private String labelText;

    @Size(max = 512)
    private String description;

    @Size(max = 512)
    private String comments;

    @NotNull
	public boolean ignored;

    private Integer displayOrder;

    @NotNull
    @Size(max = 255)
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

    public static List<CodeTableDTO> getDDictValueCodeTableByKindEquals(String lsKind) {
		List<CodeTableDTO> codeTableList = new ArrayList<CodeTableDTO>();
		for (DDictValue val : DDictValue.findDDictValuesByLsKindEquals(lsKind).getResultList()) {
			if (!val.ignored) {
				CodeTableDTO codeTable = new CodeTableDTO();
				codeTable.setName(val.labelText);
				codeTable.setCode(val.getCodeName());
				codeTable.setIgnored(val.ignored);
				codeTableList.add(codeTable);
			}
		}
		return codeTableList;
	}

	@Transactional
	public static List<com.labsynch.labseer.dto.CodeTableDTO> getDDictCodeTable() {
		List<CodeTableDTO> codeTableList = new ArrayList<CodeTableDTO>();
		List<DDictValue> dDicts = DDictValue.findDDictValuesByIgnoredNot(true).getResultList();
		for (DDictValue val : dDicts) {
			CodeTableDTO codeTable = new CodeTableDTO();
			codeTable.setName(val.labelText);
			codeTable.setCode(val.getCodeName());
			codeTable.setIgnored(val.ignored);
			codeTableList.add(codeTable);
		}
		return codeTableList;
	}
}
