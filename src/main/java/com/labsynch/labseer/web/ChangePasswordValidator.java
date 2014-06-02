/**
 * 
 */
package com.labsynch.labseer.web;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.service.DatabaseAuthenticationProvider;

/**
 * @author rohit
 * 
 */
@Service("changePasswordValidator")
public class ChangePasswordValidator implements Validator {
	
    private static final Logger logger = LoggerFactory.getLogger(ChangePasswordValidator.class);


	@Autowired
	private MessageDigestPasswordEncoder messageDigestPasswordEncoder;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return ChangePasswordForm.class.equals(clazz);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 * org.springframework.validation.Errors)
	 */
	@Override
	public void validate(Object target, Errors errors) {
		ChangePasswordForm form = (ChangePasswordForm) target;

		try {
			if (SecurityContextHolder.getContext().getAuthentication()
					.isAuthenticated()) {
				UserDetails userDetails = (UserDetails) SecurityContextHolder
						.getContext().getAuthentication().getPrincipal();
				Query query = Author
						.findAuthorsByUserName(userDetails.getUsername());
				if(null!=query){
					Author person = (Author) query.getSingleResult();
					String storedPassword = person.getPassword();
					String currentPassword = form.getOldPassword();
//					if (!messageDigestPasswordEncoder.isPasswordValid(storedPassword, currentPassword, null)) {
//						errors.rejectValue("oldPassword",
//								"changepassword.invalidpassword");
//					}
					
		    	    String encryptedPassword = null;
		    		try {
		    			encryptedPassword = DatabaseAuthenticationProvider.getBase64ShaHash(currentPassword);
		    		} catch (NoSuchAlgorithmException e1) {
		    			// TODO Auto-generated catch block
		    			e1.printStackTrace();
		    		} catch (UnsupportedEncodingException e1) {
		    			// TODO Auto-generated catch block
		    			e1.printStackTrace();
		    		} catch (Exception e){
		    			logger.error("general error:" + e.toString());
		    		}
					
		    		logger.debug("stored password: " + storedPassword);
		    		logger.debug("encryptedPassword password: " + encryptedPassword);
		    		
		    		
					if (!storedPassword.equals(encryptedPassword)) {
						errors.rejectValue("oldPassword",
								"changepassword.invalidpassword");
					}
					
					String newPassword = form.getNewPassword();
					String newPasswordAgain = form.getNewPasswordAgain();
					if (!newPassword.equals(newPasswordAgain)) {
						errors.reject("changepassword.passwordsnomatch");
					}
				}
			}
		} catch (EntityNotFoundException e) {
			errors.rejectValue("emailAddress",
					"changepassword.invalidemailaddress");
		} catch (NonUniqueResultException e) {
			errors.rejectValue("emailAddress",
					"changepassword.duplicateemailaddress");
		}
	}

}
