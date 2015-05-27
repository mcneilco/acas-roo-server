package com.labsynch.labseer.utils;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.ldap.userdetails.LdapUserDetails;

import com.labsynch.labseer.domain.Author;

public class SecurityUtils {

	static Logger logger = LoggerFactory.getLogger(SecurityUtils.class);

	public static void updateAuthorInfo(Authentication authentication) {

		String userName = authentication.getName();

		List<Author> authors = Author.findAuthorsByUserName(userName).getResultList();
		Author author;
		if (authors.size() > 1){
			logger.error("ERROR. Found multiple authors with the same user name");
			author = authors.get(0);
		} else if (authors.size() == 1){
			author = authors.get(0);
		} else {
			author = createAuthor(authentication);
		}

	}

	private static Author createAuthor(Authentication authentication) {

		Object principal = authentication.getPrincipal();

		String principalUserName = null;
		String userDN = null;
		
		Author author = new Author();

		if (principal instanceof LdapUserDetails) {
			LdapUserDetails ldapPrincipal = (LdapUserDetails) principal;
			principalUserName = ldapPrincipal.getUsername();
			logger.debug("username: " + principalUserName);
			String dn = ldapPrincipal.getDn();
			int beginIndex = dn.indexOf("cn=") + 3;
			int endIndex = dn.indexOf(",");
			String fullName = dn.substring(beginIndex, endIndex);
			logger.debug("fullName: " + fullName);
			String firstName = fullName.split(" ",2)[0];
			String lastName = fullName.split(" ",2)[1];
			author.setUserName(principalUserName);
			author.setFirstName(firstName);
			author.setLastName(lastName);
			author.setEmailAddress(principalUserName);
			author.setPassword(principalUserName);
			author.persist();

		} else {
			principalUserName = ((User)principal).getUsername();
			logger.info("not LDAP user Name " + principalUserName);
			author.setUserName(principalUserName);
			author.setFirstName(principalUserName);
			author.setLastName(principalUserName);
			author.setEmailAddress(principalUserName);
			author.setPassword(principalUserName);
			author.persist();
		}
		

		logger.info("just created new user in the database: " + author.toJson());

		return author;
	}

}
