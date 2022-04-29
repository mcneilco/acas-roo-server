package com.labsynch.labseer.dto;

import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ItxProtocolProtocol;
import com.labsynch.labseer.domain.LsTag;
import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.domain.ProtocolLabel;
import com.labsynch.labseer.domain.ProtocolState;
import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.ThingPage;
import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;

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
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
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
public class ProtocolDTO{

    private static final Logger logger = LoggerFactory.getLogger(ProtocolDTO.class);

    public ProtocolDTO(Protocol protocol) {
    	this.setShortDescription(protocol.getShortDescription());
    	this.setId(protocol.getId());
    	this.setVersion(protocol.getVersion());
    	this.setRecordedBy(protocol.getRecordedBy());
		this.setRecordedDate(protocol.getRecordedDate());
		this.setLsTransaction(protocol.getLsTransaction());
		this.setModifiedBy(protocol.getModifiedBy());
		this.setModifiedDate(protocol.getModifiedDate());
		this.setIgnored(protocol.isIgnored());
		this.setDeleted(protocol.isDeleted());
		this.setCodeName(protocol.getCodeName());
		this.setLsKind(protocol.getLsKind());
		this.setExperiments(protocol.getExperiments());
		this.setLsStates(protocol.getLsStates());
		this.setLsLabels(protocol.getLsLabels());
		this.setLsTags(protocol.getLsTags());
		this.setFirstProtocols(protocol.getFirstProtocols());
		this.setSecondProtocols(protocol.getSecondProtocols());
		this.setExperimentCount(countExperiments(protocol));
    }

    private String shortDescription;
    
	private Long id;
	
	private Integer version;
    
	private String lsType;

	private String lsKind;
	
	private String codeName;

	private String recordedBy;

	private Date recordedDate;

	private String modifiedBy;

	private Date modifiedDate;

	private boolean ignored;
	
	private boolean deleted;

	private Long lsTransaction;
	
	private Integer experimentCount;
    
	private Set<ThingPage> thingPage = new HashSet<ThingPage>();
	
    private Set<ProtocolState> lsStates = new HashSet<ProtocolState>();

    private Set<Experiment> experiments = new HashSet<Experiment>();

    private Set<ProtocolLabel> lsLabels = new HashSet<ProtocolLabel>();

    private Set<LsTag> lsTags = new HashSet<LsTag>();

    private Set<ItxProtocolProtocol> firstProtocols = new HashSet<ItxProtocolProtocol>();

    private Set<ItxProtocolProtocol> secondProtocols = new HashSet<ItxProtocolProtocol>();
    
    public static Collection<ProtocolDTO> convertCollectionToProtocolDTO (Collection<Protocol> protocols) {
    	Collection<ProtocolDTO> protocolDTOs = new HashSet<ProtocolDTO>();
    	for (Protocol protocol: protocols) {
    		ProtocolDTO protocolDTO = new ProtocolDTO(protocol);
    		protocolDTOs.add(protocolDTO);
    	}
    	return protocolDTOs;
    }

    @Transactional
    public String toJsonStub() {
        return new JSONSerializer().exclude("*.class", "lsStates.lsValues.lsState", "lsStates.protocol").include("lsTags", "lsLabels", "lsStates.lsValues").transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public String toPrettyJsonStub() {
        return new JSONSerializer().exclude("*.class", "lsStates.lsValues.lsState", "lsStates.protocol").include("lsTags", "lsLabels", "lsStates.lsValues").prettyPrint(true).transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public String toJson() {
        return new JSONSerializer().exclude("*.class", "lsStates.lsValues.lsState", "lsStates.protocol", "experiments.protocol").include("lsTags", "lsLabels", "lsStates.lsValues", "experiments.lsLabels", "experiments.lsStates.lsValues").prettyPrint(false).transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public String toPrettyJson() {
        return new JSONSerializer().exclude("*.class", "lsStates.lsValues.lsState", "lsStates.protocol", "experiments.protocol").include("lsTags", "lsLabels", "lsStates.lsValues", "experiments.lsLabels", "experiments.lsStates.lsValues").prettyPrint(true).transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public static String toPrettyJsonArray(Collection<ProtocolDTO> collection) {
        return new JSONSerializer().exclude("*.class", "lsStates.lsValues.lsState", "lsStates.protocol", "experiments.protocol").include("lsTags", "lsLabels", "lsStates.lsValues", "experiments.lsLabels", "experiments.lsStates.lsValues").prettyPrint(true).transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    @Transactional
    public static String toPrettyJsonArrayStub(Collection<ProtocolDTO> collection) {
        return new JSONSerializer().exclude("*.class", "lsStates.lsValues.lsState", "lsStates.protocol").include("lsTags", "lsLabels", "lsStates.lsValues").prettyPrint(true).transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    @Transactional
    public static String toJsonArray(Collection<ProtocolDTO> collection) {
        return new JSONSerializer().exclude("*.class").include("lsTags", "lsLabels", "lsStates.lsValues").transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    @Transactional
    public static String toJsonArrayStub(Collection<ProtocolDTO> collection) {
        return new JSONSerializer().exclude("*.class", "lsStates.lsValues.lsState", "lsStates.protocol", "experiments.protocol", "lsLabels.protocol").include("lsTags", "lsLabels", "lsStates.lsValues").transform(new ExcludeNulls(), void.class).serialize(collection);
    }
    
    private int countExperiments(Protocol protocol) {
    	Collection<Experiment> experiments = protocol.getExperiments();
    	int count = 0;
    	for (Experiment experiment : experiments) {
    		if (!experiment.isIgnored()) count++;
    	}
    	return count;
    }

	public String getShortDescription() {
        return this.shortDescription;
    }

	public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
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

	public String getCodeName() {
        return this.codeName;
    }

	public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

	public String getRecordedBy() {
        return this.recordedBy;
    }

	public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }

	public Date getRecordedDate() {
        return this.recordedDate;
    }

	public void setRecordedDate(Date recordedDate) {
        this.recordedDate = recordedDate;
    }

	public String getModifiedBy() {
        return this.modifiedBy;
    }

	public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

	public Date getModifiedDate() {
        return this.modifiedDate;
    }

	public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

	public boolean isIgnored() {
        return this.ignored;
    }

	public void setIgnored(boolean ignored) {
        this.ignored = ignored;
    }

	public boolean isDeleted() {
        return this.deleted;
    }

	public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

	public Long getLsTransaction() {
        return this.lsTransaction;
    }

	public void setLsTransaction(Long lsTransaction) {
        this.lsTransaction = lsTransaction;
    }

	public Integer getExperimentCount() {
        return this.experimentCount;
    }

	public void setExperimentCount(Integer experimentCount) {
        this.experimentCount = experimentCount;
    }

	public Set<ThingPage> getThingPage() {
        return this.thingPage;
    }

	public void setThingPage(Set<ThingPage> thingPage) {
        this.thingPage = thingPage;
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

	public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).setExcludeFieldNames("lsTags", "lsStates", "experiments", "lsLabels").toString();
    }

	public static ProtocolDTO fromJsonToProtocolDTO(String json) {
        return new JSONDeserializer<ProtocolDTO>()
        .use(null, ProtocolDTO.class).deserialize(json);
    }

	public static Collection<ProtocolDTO> fromJsonArrayToProtocoes(String json) {
        return new JSONDeserializer<List<ProtocolDTO>>()
        .use("values", ProtocolDTO.class).deserialize(json);
    }
}
