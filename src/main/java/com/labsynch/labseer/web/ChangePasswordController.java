package com.labsynch.labseer.web;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.service.DatabaseAuthenticationProvider;


@RequestMapping("/changepassword/**")
@Controller
public class ChangePasswordController {
	
    private static final Logger logger = LoggerFactory.getLogger(ChangePasswordController.class);

	
	@Autowired
	private ChangePasswordValidator validator;

	@Autowired
	private MessageDigestPasswordEncoder messageDigestPasswordEncoder;

	@ModelAttribute("changePasswordForm")
	public ChangePasswordForm formBackingObject() {
		return new ChangePasswordForm();
	}

	@RequestMapping(value = "/index", produces = "text/html")
	public String index() {
		if (SecurityContextHolder.getContext().getAuthentication()
				.isAuthenticated()) {
			return "changepassword/index";
		} else {
			return "login";
		}
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = "text/html")
	public String update(
			@ModelAttribute("changePasswordForm") ChangePasswordForm form,
			BindingResult result) {
		validator.validate(form, result);
		if (result.hasErrors()) {
			return "changepassword/index"; // back to form
		} else {
			if (SecurityContextHolder.getContext().getAuthentication()
					.isAuthenticated()) {
				UserDetails userDetails = (UserDetails) SecurityContextHolder
						.getContext().getAuthentication().getPrincipal();
				String newPassword = form.getNewPassword();
				Query query = Author
						.findAuthorsByUserName(userDetails.getUsername());
				Author person = (Author) query.getSingleResult();
				
//			    messageDigestPasswordEncoder.setEncodeHashAsBase64(true);
//				person.setPassword(messageDigestPasswordEncoder.encodePassword(newPassword, null));
				
				
	    	    String encryptedPassword = null;
	    		try {
	    			encryptedPassword = DatabaseAuthenticationProvider.getBase64ShaHash(newPassword);
	    		} catch (NoSuchAlgorithmException e1) {
	    			// TODO Auto-generated catch block
	    			e1.printStackTrace();
	    		} catch (UnsupportedEncodingException e1) {
	    			// TODO Auto-generated catch block
	    			e1.printStackTrace();
	    		} catch (Exception e){
	    			logger.error("general error:" + e.toString());
	    		}
	            person.setPassword(encryptedPassword);
	            logger.debug("changing password");
	            logger.debug(person.toJson());
				
				person.merge();
				return "changepassword/thanks";
			} else {
				return "login";
			}
		}
	}

	@RequestMapping(value = "/thanks", produces = "text/html")
	public String thanks() {
		return "changepassword/thanks";
	}

}
