package com.labsynch.labseer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import com.labsynch.labseer.utils.PropertiesUtilService;
import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import com.mangofactory.swagger.models.dto.ApiInfo;
 
//@Configuration
@EnableSwagger
public class SwaggerConfig {
	private SpringSwaggerConfig springSwaggerConfig;
	
	@Autowired
	private PropertiesUtilService propertiesUtilService;
	
	   @Autowired
	   public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig) {
	      this.springSwaggerConfig = springSwaggerConfig;
	   }

	   @Bean //Don't forget the @Bean annotation
	   public SwaggerSpringMvcPlugin customImplementation(){
	      return new SwaggerSpringMvcPlugin(this.springSwaggerConfig)
	            .apiInfo(apiInfo())
	            .enable(propertiesUtilService.getEnableSwagger())
	            .includePatterns("/api.*");
	   }

	    private ApiInfo apiInfo() {
	      ApiInfo apiInfo = new ApiInfo(
	              "ACAS API",
	              "API for ACAS",
	              "ACAS API terms of service",
	              "mail2ACAS@gmail.com",
	              "ACAS API License Type",
	              "ACAS API License URL"
	        );
	      return apiInfo;
	    }
}