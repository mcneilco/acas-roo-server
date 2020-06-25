package com.labsynch.labseer.web;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.service.DatabaseAuthenticationProvider;
import com.labsynch.labseer.utils.PropertiesUtilService;

@RequestMapping("/forgotpassword/**")
@Controller
public class ForgotPasswordController {

    @Autowired
    private transient MailSender mailSender;

    private transient SimpleMailMessage simpleMailMessage;
    
    @Autowired
    private PropertiesUtilService propertiesUtilService;

	@Autowired
	private MessageDigestPasswordEncoder messageDigestPasswordEncoder;

    @ModelAttribute("forgotpasswordForm")
    public ForgotPasswordForm formBackingObject() {
        return new ForgotPasswordForm();
    }

    @RequestMapping(value = "/index", produces = "text/html")
    public String index() {
        return "forgotpassword/index";
    }

    @RequestMapping(value = "/thanks", produces = "text/html")
    public String thanks() {
        return "forgotpassword/thanks";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "text/html")
    public String update(@ModelAttribute("forgotpasswordForm") ForgotPasswordForm form, BindingResult result) {
        if (result.hasErrors()) {
        	return "forgotpassword/index";
        } else {
        	TypedQuery<Author> userQuery=Author.findAuthorsByEmailAddress(form.getEmailAddress());
        	if(null!=userQuery && userQuery.getMaxResults()>0){
        		Author User = userQuery.getSingleResult();
        		Random random = new Random(System.currentTimeMillis());
        		String newPassword = "pass"+random.nextLong();
//        		User.setPassword(messageDigestPasswordEncoder.encodePassword(newPassword, null));
        		
        		
	    	    String encryptedPassword = null;
	    		try {
	    			encryptedPassword = DatabaseAuthenticationProvider.getBase64ShaHash(newPassword);
	    		} catch (NoSuchAlgorithmException e1) {
	    			// TODO Auto-generated catch block
	    			e1.printStackTrace();
	    		} catch (UnsupportedEncodingException e1) {
	    			// TODO Auto-generated catch block
	    			e1.printStackTrace();
	    		} 
	            User.setPassword(encryptedPassword);
        		
        		User.merge();
        		SimpleMailMessage mail = new SimpleMailMessage();
        		mail.setTo(form.getEmailAddress());
        		mail.setSubject("ACAS Password Recovery");
        		mail.setText("Hi "+User.getFirstName()+",\n"
        				+ "You recently requested for your password to be reset. Your new password is "+newPassword+"\n"
        				+ "Please login to ACAS here and remember to change your password. "+ propertiesUtilService.getClientFullPath() + "\n"
        				+"Thank you, \nAdmin");
        		mailSender.send(mail);
        	}

            return "forgotpassword/thanks";
        }
    }

    public void sendMessage(String mailTo, String message) {
        simpleMailMessage.setTo(mailTo);
        simpleMailMessage.setText(message);
		if(propertiesUtilService.getEmailFromAddress() != null && !propertiesUtilService.getEmailFromAddress().equals("") ) simpleMailMessage.setFrom(propertiesUtilService.getEmailFromAddress());
        mailSender.send(simpleMailMessage);
    }
}
