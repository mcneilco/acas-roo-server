package com.labsynch.labseer.service;

/**
 * 
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.domain.AuthorRole;
import com.labsynch.labseer.utils.PropertiesUtilService;



/**
 * 
 */
@SuppressWarnings("deprecation")
@Service("acasAuthenticationProvider")
public class ACASAuthenticationProvider extends
AbstractUserDetailsAuthenticationProvider {

	private static final Logger logger = LoggerFactory.getLogger(ACASAuthenticationProvider.class);

	private String adminUser;

	private String adminPassword;


	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Autowired
	private MessageDigestPasswordEncoder messageDigestPasswordEncoder;

	public void setAdminUser(String adminUser) {
		this.adminUser = adminUser;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.authentication.dao.
	 * AbstractUserDetailsAuthenticationProvider
	 * #additionalAuthenticationChecks(org
	 * .springframework.security.core.userdetails.UserDetails,
	 * org.springframework
	 * .security.authentication.UsernamePasswordAuthenticationToken)
	 */
	@Override
	@Transactional
	protected void additionalAuthenticationChecks(UserDetails arg0,
			UsernamePasswordAuthenticationToken arg1)
					throws AuthenticationException {
		return;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.authentication.dao.
	 * AbstractUserDetailsAuthenticationProvider#retrieveUser(java.lang.String,
	 * org
	 * .springframework.security.authentication.UsernamePasswordAuthenticationToken
	 * )
	 */
	@Override
	@Transactional
	protected UserDetails retrieveUser(String username,
			UsernamePasswordAuthenticationToken authentication)
					throws AuthenticationException {
		logger.debug( "Inside retrieveUser");
		logger.debug("here is the current auth strategy: " + propertiesUtilService.getAuthStrategy());
		logger.debug("here is the current username: " + username);



		String password = (String) authentication.getCredentials();
		if (! StringUtils.hasText(password)) {
			throw new BadCredentialsException("Please enter password");
		}

		messageDigestPasswordEncoder.setEncodeHashAsBase64(true);
		String encryptedPassword = null;
		try {
			if (propertiesUtilService.getAuthStrategy().equalsIgnoreCase("properties")){
				encryptedPassword = getBase64ShaHash(password);
			} else {
				encryptedPassword = getBase64ShaHash(password);
			}
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 

		UserDetails user = null;
		String expectedPassword = null;
		Boolean enabled = true;
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		if (adminUser == null){
			logger.info("the default adminUser is null");
		}
		
		if (adminUser != null && adminUser.equals(username)) {
			// pseudo-user admin (ie not configured via Person)
			expectedPassword = adminPassword; 
			// authenticate admin
			if (! encryptedPassword.equals(expectedPassword)) {
				throw new BadCredentialsException("Invalid password");
			}
			// authorize admin
			authorities.add(new GrantedAuthorityImpl("ROLE_ADMIN"));

		} else {

			try {

				if (propertiesUtilService.getAuthStrategy().equalsIgnoreCase("properties")){
					expectedPassword = getPropertiesPassword(username);

				}	else {

					TypedQuery<Author> query= Author.findAuthorsByUserName(username);
					if (query.getResultList().size() == 0){
						query = Author.findAuthorsByEmailAddress(username);
					}

					Author targetUser = (Author) query.getSingleResult();

					expectedPassword = targetUser.getPassword();

					TypedQuery<AuthorRole> roleQuery=AuthorRole.findAuthorRolesByUserEntry(targetUser);
					List<AuthorRole> userRoles = roleQuery.getResultList();
					for(AuthorRole userRole:userRoles){
						authorities.add(new GrantedAuthorityImpl(userRole.getRoleEntry().getRoleName()));
					}
					enabled = targetUser.getEnabled();
					if (!enabled) {
						throw new DisabledException("Account has not been activated.");
					}

				}
				// authenticate the person

				if (! StringUtils.hasText(expectedPassword)) {
					throw new BadCredentialsException("No password for " + username + 
							" set in database, contact administrator");
				}
				if (! encryptedPassword.equals(expectedPassword)) {
					throw new BadCredentialsException("Invalid Password");
				}

			} catch (EmptyResultDataAccessException e) {
				throw new BadCredentialsException("Invalid user");
			} catch (EntityNotFoundException e) {
				throw new BadCredentialsException("Invalid user");
			} catch (NonUniqueResultException e) {
				throw new BadCredentialsException(
						"Non-unique user, please contact an administrator");
			} catch (IOException e) {
				throw new BadCredentialsException(
						"Unable to open the users properties file, please contact an administrator");
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DisabledException e) {
				e.printStackTrace();
			}
		}
		
		return new org.springframework.security.core.userdetails.User(
				username,
				password,
				enabled, // enabled 
				true, // account not expired
				true, // credentials not expired 
				true, // account not locked
				authorities
				);
	}

	public static String getBase64ShaHash(final String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		final MessageDigest md = MessageDigest.getInstance("SHA");
		md.update(str.getBytes("UTF-8"));
		final byte[] bites = md.digest();

		final Base64 base64 = new Base64();
		final String encoded = base64.encodeAsString(bites);
		logger.debug("new encoded password: " + encoded);
		return encoded; 
	}

	public static String getBase64Sha256Hash(final String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		final MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(str.getBytes("UTF-8"));
		final byte[] bites = md.digest();

		final Base64 base64 = new Base64();
		final String encoded = base64.encodeAsString(bites);
		logger.debug("new encoded password: " + encoded);
		return encoded; 
	}

	public String getPropertiesPassword(String userName) throws IOException, NoSuchAlgorithmException {

		String password = null;

		Properties properties = new Properties();
		FileInputStream propertyStream = new FileInputStream(propertiesUtilService.getSecurityProperties()); 
		properties.load(propertyStream);
		propertyStream.close();

		String encryptedPassword = null;
		String passwordString = properties.getProperty(userName);
		if (passwordString != null && passwordString.length() > 0){
			List<String> passwordList = new ArrayList<String>();
			String[] parsedEntry = passwordString.split("\\{SHA\\}|,");
			for (String word : parsedEntry){
				logger.debug("parsed word: " + word);
				passwordList.add(word);
			}

			if (passwordString.contains("{SHA}")){
				logger.debug("the entry starts with {SHA}");
				logger.debug("here is the encrypted password: " + passwordList.get(1));
				encryptedPassword = passwordList.get(1);
			} else {
				logger.debug("the entry is a simple password");
				logger.debug("here is the simple password: " + passwordList.get(0));
				password = passwordList.get(0);
				// encrypt the password to compare
				encryptedPassword = getBase64ShaHash(password);
			}
		}

		return encryptedPassword;
	}
	
}
