package com.labsynch.labseer.web;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;

import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.domain.AuthorRole;
import com.labsynch.labseer.domain.LsRole;
import com.labsynch.labseer.service.DatabaseAuthenticationProvider;
import com.labsynch.labseer.utils.PropertiesUtilService;


@RequestMapping("/signup/**")
@Controller
public class SignUpController {
	
    private static final Logger logger = LoggerFactory.getLogger(SignUpController.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;

    @Autowired
    private SignUpValidator validator;

    @Autowired
    private transient MailSender mailSender;

    private transient SimpleMailMessage simpleMailMessage;

	@Autowired
	private MessageDigestPasswordEncoder messageDigestPasswordEncoder;

    @ModelAttribute("Author")
    public UserRegistrationForm formBackingObject() {
        return new UserRegistrationForm();
    }

    @RequestMapping(params = "form", method = RequestMethod.GET, produces = "text/html")
    public String createForm(Model model) {
    	UserRegistrationForm form = new UserRegistrationForm();
        model.addAttribute("Author", form);
        model.addAttribute("captcha_form",form.getReCaptchaHtml());
        return "signup/index";
    }
    
    @RequestMapping(params = "activate", method = RequestMethod.GET, produces = "text/html")
    public String activateUser(@RequestParam(value = "activate", required = true) String activationKey, @RequestParam(value = "emailAddress", required = true) String emailAddress,Model model) {
        TypedQuery<Author> query = Author.findAuthorsByActivationKeyAndEmailAddress(activationKey, emailAddress);
        Author User=query.getSingleResult();
        if(null!=User){
        	User.setActivationDate(new Date());
        	User.setEnabled(true);
        	User.merge();
        	String redirectUrl=propertiesUtilService.getClientPath();
        	return "redirect:"+redirectUrl;
        }
        else{
        	return "signup/error";
        }

    }

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid UserRegistrationForm userRegistration, BindingResult result, Model model, HttpServletRequest request) {
        validator.validate(userRegistration, result);
        if (result.hasErrors()) {
        	logger.error("validation errors " + result.toString());
            return createForm(model);
        } else {
            Random random = new Random(System.currentTimeMillis());
            String activationKey = "activationKey:" + random.nextInt();
            logger.info("activation key is :" + activationKey);

            Author author = new Author();
            author.setActivationDate(null);
            author.setRecordedDate(new Date());
            author.setEmailAddress(userRegistration.getEmailAddress());
            author.setFirstName(userRegistration.getFirstName());
            author.setLastName(userRegistration.getLastName());
            author.setUserName(userRegistration.getUserName());
            
//    	    messageDigestPasswordEncoder.setEncodeHashAsBase64(true);
//            author.setPassword(messageDigestPasswordEncoder.encodePassword(userRegistration.getPassword(), null));
            
    	    String encryptedPassword = null;
    		try {
    			encryptedPassword = DatabaseAuthenticationProvider.getBase64ShaHash(userRegistration.getPassword());
    		} catch (NoSuchAlgorithmException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		} catch (UnsupportedEncodingException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		} 
            author.setPassword(encryptedPassword);

            author.setActivationKey(activationKey);
            author.setEnabled(false);
            author.setLocked(false);
            
            logger.info("about to save author: " + author.toJson());
            author.persist();
            
            addAuthorToUserRole(author);
            
            
            SimpleMailMessage mail = new SimpleMailMessage();
    		mail.setTo(author.getEmailAddress());
    		mail.setSubject("User Activation");
    		
//    		mail.setText("Hi "+ author.getFirstName() + ",\n. You just registered with us. Please click on this link to activate your account - <a href=\"" + propertiesUtilService.getHostPath() +  "signup?emailAddress="+author.getEmailAddress()+"&activate="+activationKey+"\">Activate Link</a>. \n Thanks Typical Security Admin");
    		mail.setText("Hi "+ author.getFirstName() + ",\nPlease click on the following link to activate your account: " + propertiesUtilService.getHostPath()+"signup?emailAddress="+author.getEmailAddress()+"&activate="+activationKey +  "\nThank you, \nACAS Admin");
            mailSender.send(mail);
            return "signup/thanks";
        }
    }

    private void addAuthorToUserRole(Author author) {
    	LsRole userRole = LsRole.getOrCreateRole("user");
    	AuthorRole authorRole = AuthorRole.getOrCreateAuthorRole(author, userRole);
    	logger.debug("author added to the user role " + authorRole.toJson());
    	
	}

	@RequestMapping(value = "/index", produces = "text/html")
    public String index() {
        return "signup/index";
    }

    @RequestMapping(value = "/thanks", produces = "text/html")
    public String thanks() {
        return "signup/thanks";
    }
    @RequestMapping(value = "/error", produces = "text/html")
    public String error() {
        return "signup/error";
    }

    public void sendMessage(String mailTo, String message) {
        simpleMailMessage.setTo(mailTo);
        simpleMailMessage.setText(message);
        mailSender.send(simpleMailMessage);
    }
}
