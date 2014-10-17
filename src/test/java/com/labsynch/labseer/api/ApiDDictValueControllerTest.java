package com.labsynch.labseer.api;


import java.security.InvalidParameterException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static org.springframework.test.web.ModelAndViewAssert.assertViewName;
import static org.springframework.test.web.ModelAndViewAssert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.Model;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import org.junit.runners.JUnit4;
import org.springframework.mock.staticmock.MockStaticEntityMethods;

//Swap the default JUnit4 with the spring specific SpringJUnit4ClassRunner.
//This will allow spring to inject the application context
@RunWith(SpringJUnit4ClassRunner.class)
//Remove the MockStaticEntityMethods annotation
//Setup the configuration of the application context and the web mvc layer
@ContextConfiguration({"classpath:META-INF/spring/applicationContext*.xml", "file:src/main/webapp/WEB-INF/spring/webmvc-config-test.xml"})
public class ApiDDictValueControllerTest {
	
	private static final Logger logger = LoggerFactory.getLogger(ApiDDictValueControllerTest.class);


    @Autowired
    private ApplicationContext applicationContext;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private RequestMappingHandlerAdapter handlerAdapter;
    private ApiDDictValueController controller;
    
    @Before
    public void setUp() {
       request = new MockHttpServletRequest();
       response = new MockHttpServletResponse();
       
       handlerAdapter = applicationContext.getBean(RequestMappingHandlerAdapter.class);
       // I could get the controller from the context here
       controller = new ApiDDictValueController();
    }
    
    @Test
    public void testMethod() throws Exception {
    	
    	final String expectedMessage = "Hello jack, from the controller";
        final String requestUri = "/api/v1/ddictvalues/getvalues/csv";
        final String message;
        final Object handler;
        final HandlerMethod expectedHandlerMethod;
        final ModelAndView mav;
        final MockHttpServletRequest request;
        final MockHttpServletResponse response;

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();

        request.setRequestURI(requestUri);
        
        expectedHandlerMethod = new HandlerMethod(controller, "findByTypeAndKind", String.class, String.class, String.class);
        handler = this.getHandler(request);
        
        // For the most part we will be expecting HandlerMethod objects to be returned for our controllers.
        // Calling the to string method will print the complete method signature.
        Assert.assertEquals("Correct handler found for request url: " + requestUri, expectedHandlerMethod.toString(), handler.toString());
        
        // Handle the actual request
        mav = handlerAdapter.handle(request, response, handler);

        // Ensure that the right view is returned
        assertViewName(mav, "message-show");
        // Ensure that the view will receive the message object and that it is
        // a string
        message = assertAndReturnModelAttributeOfType(mav, "message", String.class);
        // We can test the message in case
        Assert.assertEquals(("Message returned was " + expectedMessage), expectedMessage, message);
        
    }

    /**
     * This method finds the handler for a given request URI. 
     * 
     * It will also ensure that the URI Parameters i.e. /context/test/{name} are added to the request
     * 
     * @param request
     * @return 
     * @throws Exception
     */
    private Object getHandler(MockHttpServletRequest request) throws Exception {
        HandlerExecutionChain chain = null;

        Map<String, HandlerMapping> map = applicationContext.getBeansOfType(HandlerMapping.class);
        Iterator<HandlerMapping> itt = map.values().iterator();

    	logger.info("attempting to iterate through the request " + request.toString() );
    	Set<String> keys = map.keySet();
    	for (String key : keys){
    		logger.info("here are the keys: " + key);
    	}
    	
        while (itt.hasNext()) {
        	logger.info("iterating through ------------");
            HandlerMapping mapping = itt.next();
            chain = mapping.getHandler(request);
            if (chain != null) {
                break;
            }

        }
        
        if (chain == null) {
            throw new InvalidParameterException("Unable to find handler for request URI: " + request.getRequestURI());
        }
        
        return chain.getHandler();
    }
}
