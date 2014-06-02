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
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findLsThingStatesByLsTransactionEquals", "findLsThingStatesByLsThing"})
public class LsThingState extends AbstractState {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "lsthing_id")
    private LsThing lsThing;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lsState", fetch = FetchType.LAZY)
    private Set<LsThingValue> lsValues = new HashSet<LsThingValue>();

    public LsThingState() {
    }
    
    public LsThingState(com.labsynch.labseer.domain.LsThingState lsState) {
        super.setRecordedBy(lsState.getRecordedBy());
        super.setRecordedDate(lsState.getRecordedDate());
        super.setLsTransaction(lsState.getLsTransaction());
        super.setModifiedBy(lsState.getModifiedBy());
        super.setModifiedDate(lsState.getModifiedDate());
        super.setLsType(lsState.getLsType());
        super.setLsKind(lsState.getLsKind());
    }

	public static LsThingState update(LsThingState lsState) {
		LsThingState updatedLsThingState = LsThingState.findLsThingState(lsState.getId());
		if (updatedLsThingState != null){
			updatedLsThingState.setRecordedBy(lsState.getRecordedBy());
			updatedLsThingState.setRecordedDate(lsState.getRecordedDate());
			updatedLsThingState.setLsTransaction(lsState.getLsTransaction());
			updatedLsThingState.setModifiedBy(lsState.getModifiedBy());
			updatedLsThingState.setModifiedDate(new Date());
			updatedLsThingState.setLsType(lsState.getLsType());
			updatedLsThingState.setLsKind(lsState.getLsKind());
			updatedLsThingState.setIgnored(lsState.isIgnored());
			updatedLsThingState.merge();
			return updatedLsThingState;
		} else {
			return null;
		}
		
	}

    public String toJson() {
        return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
    
    public static LsThingState fromJsonToLsThingState(String json) {
        return new JSONDeserializer<LsThingState>().use(null, LsThingState.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
    }
    
    public static String toJsonArray(Collection<LsThingState> collection) {
        return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
    }
    
    public static Collection<LsThingState> fromJsonArrayToLsThingStates(String json) {
        return new JSONDeserializer<List<LsThingState>>().use(null, ArrayList.class).use("values", LsThingState.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
    }

    public static Collection<LsThingState> fromJsonArrayToLsThingStates(Reader json) {
        return new JSONDeserializer<List<LsThingState>>().use(null, ArrayList.class).use("values", LsThingState.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
    }
}
