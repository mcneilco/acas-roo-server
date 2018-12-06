package com.labsynch.labseer.utils;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.userdetails.LdapUserDetails;

import com.labsynch.labseer.domain.Scientist;

public class SecurityUtil {

	static Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

	public static Scientist getLoginUser(){

		String chemistName = "BLANK";
		String userJson = null;
		Scientist chemist;

		try {
			//			SecurityContext context = SecurityContextHolder.getContext();
			//			logger.debug("context is " + context);
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			logger.debug("authentication: " + auth);

			if (auth != null){
				@SuppressWarnings("unchecked")
				Collection<GrantedAuthority> auths = (Collection<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
				for (GrantedAuthority ga : auths){
					logger.debug("granted auth: " + ga);
				}	
				
				String userName = SecurityContextHolder.getContext().getAuthentication().getName();
				Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();
				Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

				String principalUserName = null;
				String userDN = null;
				if (principal instanceof LdapUserDetails) {
					principalUserName = ((LdapUserDetails)principal).getUsername();
					userDN =  ((LdapUserDetails)principal).getDn();
				} else {
					principalUserName = principal.toString();
				}
				logger.debug("user Name " + principalUserName);
				logger.debug("user userDN " + userDN);
				logger.debug("username is " + userName);
				logger.debug("principal is " + principal);
				logger.debug("details is " + details);

				if (userName.equalsIgnoreCase("isoutsource3")){
					chemistName = "cchemist";
				} else {
					chemistName = userName.toLowerCase();
				}

			}

			chemist = Scientist.findScientistsByCodeEquals(chemistName).getSingleResult();
			userJson = chemist.toJson();

		} catch (EmptyResultDataAccessException e){
			logger.error("unable to find the user: " + chemistName);
			try {
				chemistName = "adminUser";
				chemist = Scientist.findScientistsByCodeEquals(chemistName).getSingleResult();
				logger.debug("using the admin user");
			} catch (EmptyResultDataAccessException e2){
				logger.error("creating the user adminUser");
				chemistName = "adminUser";
				chemist = createUser(chemistName);
			}
		}

		return chemist;

	}

	public static void updateUserInfo(String loginName) {
		loginName = loginName.toLowerCase();
		logger.debug("in the security update class. User name = " + loginName);
		String userJson = null;
		Scientist chemist;
		try {
			chemist = Scientist.findScientistsByCodeEquals(loginName).getSingleResult();
			userJson = chemist.toJson();
			logger.debug(userJson);
		} catch (EmptyResultDataAccessException e){
			logger.debug("did not find the new chemist. create the new entry");
			chemist = createUser(loginName);
		}
	}

	public static void updateUserInfo(String loginName, String fullName) {
		loginName = loginName.toLowerCase();
		logger.debug("in the security update class. User name = " + loginName);
		String userJson = null;
		Scientist chemist;
		try {
			chemist = Scientist.findScientistsByCodeEquals(loginName).getSingleResult();
			userJson = chemist.toJson();
			logger.debug(userJson);
		} catch (EmptyResultDataAccessException e){
			logger.debug("did not find the new chemist. create the new entry");
			chemist = createUser(loginName, fullName);
		}
	}

	public static Scientist createUser(String loginName) {
		logger.debug("in the security update class. Creating new user name = " + loginName);
		Scientist chemist = new Scientist();
		chemist.setCode(loginName);
		chemist.setName(loginName);
		if (Configuration.getConfigInfo().getServerSettings().isNewUserIsChemist()){
			chemist.setIsChemist(true);
		}else{
			chemist.setIsChemist(false);
		}
		chemist.persist();
		return chemist;

	}

	public static Scientist createUser(String loginName, String fullName) {
		logger.debug("in the security update class. Creating new user name = " + loginName);
		Scientist chemist = new Scientist();
		chemist.setCode(loginName);
		chemist.setName(fullName);
		if (Configuration.getConfigInfo().getServerSettings().isNewUserIsChemist()){
			chemist.setIsChemist(true);
		}else{
			chemist.setIsChemist(false);
		}
		chemist.persist();
		return chemist;

	}

}