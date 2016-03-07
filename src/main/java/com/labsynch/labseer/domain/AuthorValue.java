package com.labsynch.labseer.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.utils.CustomBigDecimalFactory;

import flexjson.JSONDeserializer;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findAuthorValuesByLsState", "findAuthorValuesByLsTransactionEquals", "findAuthorValuesByCodeValueEquals", "findAuthorValuesByIgnoredNotAndCodeValueEquals", "findAuthorValuesByLsTypeEqualsAndLsKindEquals", "findAuthorValuesByLsTypeEqualsAndLsKindEqualsAndStringValueLikeAndIgnoredNot" })
public class AuthorValue extends AbstractValue {

    private static final Logger logger = LoggerFactory.getLogger(AuthorValue.class);

    @NotNull
    @ManyToOne
    @JoinColumn(name = "analysis_state_id")
    private AuthorState lsState;

	public Long getStateId() {
		return this.lsState.getId();
	}
	
	public boolean getIgnored() {
		return this.isIgnored();
	}
	
	public boolean getDeleted() {
		return this.isDeleted();
	}
	
	public boolean getPublicData() {
		return this.isPublicData();
	}
	
	public String getStateType() {
		return this.lsState.getLsType();
	}
	
	public String getStateKind() {
		return this.lsState.getLsKind();
	}
	
	public Long getAuthorId() {
		return this.lsState.getAuthor().getId();
	}
	
	public String getAuthorCode() {
		return this.lsState.getAuthor().getCodeName();
	}
	

    public static long countAuthorValues() {
        return entityManager().createQuery("SELECT COUNT(o) FROM AuthorValue o", Long.class).getSingleResult();
    }
    
    public static AuthorValue create(AuthorValue authorValue) {
    	AuthorValue newAuthorValue = new JSONDeserializer<AuthorValue>().use(null, AuthorValue.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).deserializeInto(authorValue.toJson(), 
        				new AuthorValue());	
    
        return newAuthorValue;
    }
    
    public static AuthorValue update(AuthorValue authorValue) {
        AuthorValue updatedAuthorValue = new JSONDeserializer<AuthorValue>().use(null, ArrayList.class).use("values", AuthorValue.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserializeInto(authorValue.toJson(), AuthorValue.findAuthorValue(authorValue.getId()));
        updatedAuthorValue.setModifiedDate(new Date());
        updatedAuthorValue.merge();
        return updatedAuthorValue;
    }
	
}
