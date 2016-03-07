package com.labsynch.labseer.domain;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;

@RooJavaBean
@RooToString
@RooJson
@Transactional
@RooJpaActiveRecord(finders = { "findAuthorLabelsByAuthor", "findAuthorLabelsByLsTransactionEquals" })
public class AuthorLabel extends AbstractLabel {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    public AuthorLabel(AuthorLabel authorLabel) {
        super.setLsType(authorLabel.getLsType());
        super.setLsKind(authorLabel.getLsKind());
        super.setLsTypeAndKind(authorLabel.getLsType() + "_" + authorLabel.getLsKind());
        super.setLabelText(authorLabel.getLabelText());
        super.setPreferred(authorLabel.isPreferred());
        super.setLsTransaction(authorLabel.getLsTransaction());
        super.setRecordedBy(authorLabel.getRecordedBy());
        super.setRecordedDate(authorLabel.getRecordedDate());
        super.setPhysicallyLabled(authorLabel.isPhysicallyLabled());
    }
 
    public static AuthorLabel update(AuthorLabel authorLabel) {
    	AuthorLabel updatedAuthorLabel = AuthorLabel.findAuthorLabel(authorLabel.getId());
    	updatedAuthorLabel.setLsType(authorLabel.getLsType());
    	updatedAuthorLabel.setLsKind(authorLabel.getLsKind());
    	updatedAuthorLabel.setLsTypeAndKind(authorLabel.getLsType() + "_" + authorLabel.getLsKind());
    	updatedAuthorLabel.setLabelText(authorLabel.getLabelText());
    	updatedAuthorLabel.setPreferred(authorLabel.isPreferred());
    	updatedAuthorLabel.setLsTransaction(authorLabel.getLsTransaction());
    	updatedAuthorLabel.setRecordedBy(authorLabel.getRecordedBy());
    	updatedAuthorLabel.setRecordedDate(authorLabel.getRecordedDate());
    	updatedAuthorLabel.setModifiedDate(new Date());
    	updatedAuthorLabel.setPhysicallyLabled(authorLabel.isPhysicallyLabled());
    	updatedAuthorLabel.merge();
    	return updatedAuthorLabel;
    }
    
    public static Collection<AuthorLabel> fromJsonArrayToAuthorLabels(String json) {
        return new JSONDeserializer<List<AuthorLabel>>().use(null, ArrayList.class).use("values", AuthorLabel.class).deserialize(json);
    }
    
    public static Collection<AuthorLabel> fromJsonArrayToAuthorLabels(Reader json) {
        return new JSONDeserializer<List<AuthorLabel>>().use(null, ArrayList.class).use("values", AuthorLabel.class).deserialize(json);
    }
    
    
}
