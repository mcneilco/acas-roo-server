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
}
