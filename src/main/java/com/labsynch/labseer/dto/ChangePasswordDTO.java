package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class ChangePasswordDTO {
	
	private String username;
	
	private String oldPassword;
	
	private String newPassword;
	
	private String newPasswordAgain;
	
	public ChangePasswordDTO(){
	}

	public String getUsername() {
        return this.username;
    }

	public void setUsername(String username) {
        this.username = username;
    }

	public String getOldPassword() {
        return this.oldPassword;
    }

	public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

	public String getNewPassword() {
        return this.newPassword;
    }

	public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

	public String getNewPasswordAgain() {
        return this.newPasswordAgain;
    }

	public void setNewPasswordAgain(String newPasswordAgain) {
        this.newPasswordAgain = newPasswordAgain;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static ChangePasswordDTO fromJsonToChangePasswordDTO(String json) {
        return new JSONDeserializer<ChangePasswordDTO>()
        .use(null, ChangePasswordDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ChangePasswordDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<ChangePasswordDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<ChangePasswordDTO> fromJsonArrayToChangePasswoes(String json) {
        return new JSONDeserializer<List<ChangePasswordDTO>>()
        .use("values", ChangePasswordDTO.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}


