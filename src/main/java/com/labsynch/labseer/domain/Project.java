package com.labsynch.labseer.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findProjectsByCodeEquals", "findProjectsByNameEquals" })
@RooJson
public class Project {


	@Size(max = 255)
	private String name;

	@Size(max = 255)
	private String code;

	public static void sortProjectsByName(ArrayList<Project> projectArray){
		Collections.sort(projectArray, new Comparator<Project>() {
		    public int compare(Project project1, Project project2) {
		        return project1.name.toUpperCase().compareTo(project2.name.toUpperCase());
		    }
		});
	}
	
}
