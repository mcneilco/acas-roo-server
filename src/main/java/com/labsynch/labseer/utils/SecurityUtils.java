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


		if (principal instanceof LdapUserDetails) {
			principalUserName = ((LdapUserDetails)principal).getUsername();
			userDN =  ((LdapUserDetails)principal).getDn();

			logger.info("user Name " + principalUserName);
			logger.info("user userDN " + userDN);


		} else {
			principalUserName = ((User)principal).getUsername();
			logger.info("not LDAP user Name " + principalUserName);

		}


		Author author = new Author();
		author.setUserName(principalUserName);
		author.setFirstName(principalUserName);
		author.setLastName(principalUserName);
		author.setEmailAddress(principalUserName);
		author.setPassword(principalUserName);
		author.persist();

		logger.info("just created new user in the database: " + author.toJson());

		return author;
	}

}
