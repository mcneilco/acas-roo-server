package com.labsynch.labseer.service;

/**
 * 
 */

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;

import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.domain.AuthorRole;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;



/**
 * 
 */
@SuppressWarnings("deprecation")
@Service("databaseAuthenticationProvider")
public class DatabaseAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
	
    private static final Logger logger = LoggerFactory.getLogger(DatabaseAuthenticationProvider.class);

	private String adminUser;
	private String adminPassword;
	
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
		String password = (String) authentication.getCredentials();
	    if (! StringUtils.hasText(password)) {
	      throw new BadCredentialsException("Please enter password");
	    }
	    messageDigestPasswordEncoder.setEncodeHashAsBase64(true);
//	    String encryptedPassword = messageDigestPasswordEncoder.encodePassword(password, null); 
	    String encryptedPassword = null;
		try {
			encryptedPassword = getBase64ShaHash(password);
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 

	    UserDetails user = null;
	    String expectedPassword = null;
	    List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
	    if (adminUser.equals(username)) {
	      // pseudo-user admin (ie not configured via Person)
	      expectedPassword = adminPassword; 
	      // authenticate admin
	      if (! encryptedPassword.equals(expectedPassword)) {
	        throw new BadCredentialsException("Invalid password");
	      }
	      // authorize admin
	      authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
	    } else {
	      try {
	    	TypedQuery<Author> query= Author.findAuthorsByUserName(username);
	    	if (query.getResultList().size() == 0){
	    		query = Author.findAuthorsByEmailAddress(username);
	    	}
	    	
	        Author targetUser = (Author) query.getSingleResult();
	        // authenticate the person
	        expectedPassword = targetUser.getPassword();
	        if (! StringUtils.hasText(expectedPassword)) {
	          throw new BadCredentialsException("No password for " + username + 
	            " set in database, contact administrator");
	        }
	        if (! encryptedPassword.equals(expectedPassword)) {
	          throw new BadCredentialsException("Invalid Password");
	        }
	        
	        // activate the account on the first login
	        // may decide to use this field to deactivate the account
	        // in that case -- throw an error if the account is not enabled
	        // must set the enabled flag through the admin interace.
	        
	        if (!targetUser.getLocked() && !targetUser.getEnabled()){
	        	targetUser.setEnabled(true);
	        	targetUser.setActivationDate(new Date());
	        	targetUser.merge();
	        }
	        
	        if (targetUser.getLocked()){
		          throw new BadCredentialsException("The user account: " + username + 
		  	            " is locked, please contact administrator");
	        }
	        
	        TypedQuery<AuthorRole> roleQuery=AuthorRole.findAuthorRolesByUserEntry(targetUser);
	        List<AuthorRole> userRoles = roleQuery.getResultList();
	        for(AuthorRole userRole:userRoles){
	        	authorities.add(new SimpleGrantedAuthority(userRole.getRoleEntry().getRoleName()));
	        }
	      } catch (NoResultException e) {
		        throw new BadCredentialsException("Invalid user");
	      } catch (EntityNotFoundException e) {
	        throw new BadCredentialsException("Invalid user");
	      } catch (NonUniqueResultException e) {
	        throw new BadCredentialsException(
	          "Non-unique user, contact administrator");
	      }
	    }
	    return new org.springframework.security.core.userdetails.User(
	      username,
	      password,
	      true, // enabled 
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
}
