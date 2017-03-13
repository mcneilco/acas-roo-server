package com.labsynch.labseer.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.domain.AuthorLabel;
import com.labsynch.labseer.domain.AuthorRole;
import com.labsynch.labseer.domain.AuthorState;
import com.labsynch.labseer.domain.AuthorValue;
import com.labsynch.labseer.domain.LsRole;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.LsThingLabel;
import com.labsynch.labseer.domain.LsThingState;
import com.labsynch.labseer.domain.LsThingValue;
import com.labsynch.labseer.dto.AuthGroupsAndProjectsDTO;
import com.labsynch.labseer.dto.AuthGroupsDTO;
import com.labsynch.labseer.dto.AuthProjectGroupsDTO;
import com.labsynch.labseer.dto.AuthorBrowserQueryDTO;
import com.labsynch.labseer.dto.AuthorQueryDTO;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.ItxQueryDTO;
import com.labsynch.labseer.dto.LabelQueryDTO;
import com.labsynch.labseer.dto.ValueQueryDTO;
import com.labsynch.labseer.utils.PropertiesUtilService;
import com.labsynch.labseer.utils.SimpleUtil;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

	@Autowired
	private AutoLabelService autoLabelService;

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	private static final Logger logger = LoggerFactory.getLogger(AuthorServiceImpl.class);


	@Override
	public CodeTableDTO getAuthorCodeTable(Author author) {
		CodeTableDTO codeTable = new CodeTableDTO();
		codeTable.setName(author.getFirstName() + " " + author.getLastName());
		codeTable.setCode(author.getUserName());
		codeTable.setId(author.getId());
		codeTable.setIgnored(false);
		return codeTable;
	}


	@Override
	public List<CodeTableDTO> convertToCodeTables(List<Author> authors) {
		List<CodeTableDTO> codeTableList = new ArrayList<CodeTableDTO>();
		for (Author author : authors) {
			CodeTableDTO codeTable = getAuthorCodeTable(author);
			codeTableList.add(codeTable);
		}
		return codeTableList;	
	}

	@Override
	public Collection<Author> findAuthorsByAuthorRoleName(String authorRoleName){
		List<LsRole> roleEntries = LsRole.findLsRolesByRoleNameEquals(authorRoleName).getResultList();
		Collection<Author> authors = new HashSet<Author>();
		for (LsRole roleEntry : roleEntries){
			Collection<AuthorRole> authorRoles = AuthorRole.findAuthorRolesByRoleEntry(roleEntry).getResultList();
			for (AuthorRole authorRole : authorRoles){
				authors.add(authorRole.getUserEntry());
			}	
		}
		return authors;
	}

	@Override
	public Collection<LsThing> getUserProjects(String userName	){
		Collection<LsThing> projectThings = new HashSet<LsThing>();
		List<Author> authors = Author.findAuthorsByUserName(userName).getResultList();
		Author author = null;
		if (authors.size() == 0){
			logger.error("No author found by name: " + userName);
		} else if (authors.size() > 1) {
			logger.error("Found multiple authors by name: " + userName);
			author = authors.get(0);
		} else {
			author = authors.get(0);
		}
		if (author != null){
			if (propertiesUtilService.getEnableProjectRoles()){
				Set<AuthorRole> roles = author.getAuthorRoles();
				for (AuthorRole role : roles){
					LsRole entry = role.getRoleEntry();
					if (entry.getLsType()!= null && entry.getLsType().equalsIgnoreCase("Project")){
						projectThings.addAll(LsThing.findLsThingsByCodeNameEquals(entry.getLsKind()).getResultList());
					}else if (entry.getRoleName().equals(propertiesUtilService.getAcasAdminRole())){
						Collection<LsThing> allProjects = LsThing.findLsThingsByLsTypeEqualsAndLsKindEquals("project", "project").getResultList();
						projectThings.addAll(allProjects);
					}
				}
			}else{
				Collection<LsThing> allProjects = LsThing.findLsThingsByLsTypeEqualsAndLsKindEquals("project", "project").getResultList();
				projectThings.addAll(allProjects);
			}
		}

		return projectThings;

	}

	@Override
	public AuthGroupsAndProjectsDTO getAuthGroupsAndProjects(){
		AuthGroupsAndProjectsDTO agp = new AuthGroupsAndProjectsDTO();
		Collection<AuthProjectGroupsDTO> authProjectsGroups = new ArrayList<AuthProjectGroupsDTO>();
		Collection<AuthGroupsDTO> authGroups = new HashSet<AuthGroupsDTO>() ;
		List<LsRole> allRoles = LsRole.findAllLsRoles();
		AuthGroupsDTO ag;
		Collection<String> members;

		for (LsRole lsRole : allRoles){
			ag = new AuthGroupsDTO();
			members = new HashSet<String>();
			ag.setName(new StringBuilder().append(lsRole.getLsType()).append("_").append(lsRole.getLsKind()).append("_").append(lsRole.getRoleName()).toString());
			Set<AuthorRole> authorRoles = lsRole.getAuthorRoles();
			for (AuthorRole authorRole : authorRoles){
				members.add(authorRole.getUserEntry().getUserName());
			}
			ag.setMembers(members);
			logger.info(ag.toJson());
			authGroups.add(ag);			
		}

		//Collection<LsThing> 
//		Collection<LsThing> projectCollection = LsThing.findLsThingsByLsTypeEqualsAndLsKindEquals("project", "project").getResultList();
		List<LsThing> projectList = LsThing.findLsThingsByLsTypeEqualsAndLsKindEquals("project", "project").getResultList();
		
		Collection<LsThing> projectCollection = new ArrayList<LsThing>(projectList);		
		AuthProjectGroupsDTO authProjectGroup;
		Collection<String> groups;
		List<LsRole> projectRoles;
		Set<LsThingLabel> projectLabels;
		String projectName = null;
		String projectAlias = null;
		boolean active = true;
		boolean isRestricted = true;

		for (LsThing project : projectCollection){
			if (!project.isIgnored()){
				projectRoles = LsRole.findLsRolesByLsTypeEqualsAndLsKindEquals("Project", project.getCodeName()).getResultList();
				groups = new HashSet<String>();
				for (LsRole projectRole : projectRoles){
					groups.add(new StringBuilder().append(projectRole.getLsType()).append("_").append(projectRole.getLsKind()).append("_").append(projectRole.getRoleName()).toString());
				}
				List<LsRole> acasAdminRoles = LsRole.findLsRolesByRoleNameEquals(propertiesUtilService.getAcasAdminRole()).getResultList();
				for (LsRole acasAdminRole : acasAdminRoles){
					groups.add(new StringBuilder().append(acasAdminRole.getLsType()).append("_").append(acasAdminRole.getLsKind()).append("_").append(acasAdminRole.getRoleName()).toString());
				}

				projectLabels = project.getLsLabels();
				for (LsThingLabel projectLabel : projectLabels){
					if (!projectLabel.isIgnored()){
						if (projectLabel.getLsType().equalsIgnoreCase("name")){
							if (projectLabel.getLsKind().equalsIgnoreCase("Project Name")){
								projectName = projectLabel.getLabelText();
							} else if (projectLabel.getLsKind().equalsIgnoreCase("Project Alias")){
								projectAlias = projectLabel.getLabelText();
							}						
						}
					}
				}
				for (LsThingState projectState : project.getLsStates()){
					for (LsThingValue projectValue : projectState.getLsValues()){
						if (!projectValue.isIgnored()){
							if (projectValue.getLsKind().equalsIgnoreCase("project status")){
								if (projectValue.getCodeValue().equalsIgnoreCase("Active")) active = true;
								else if (projectValue.getCodeValue().equalsIgnoreCase("Inactive")) active = false;
							}else if (projectValue.getLsKind().equalsIgnoreCase("is restricted")){
								if (projectValue.getCodeValue().equalsIgnoreCase("true")) isRestricted = true;
								else if (projectValue.getCodeValue().equalsIgnoreCase("false")) isRestricted = false;
							}
						}
					}
				}

				authProjectGroup = new AuthProjectGroupsDTO();
				authProjectGroup.setId(project.getId());				
				authProjectGroup.setCode(project.getCodeName());
				authProjectGroup.setName(projectName);
				authProjectGroup.setAlias(projectAlias);
				authProjectGroup.setActive(active);
				authProjectGroup.setIsRestricted(isRestricted);
				authProjectGroup.setGroups(groups);
				authProjectsGroups.add(authProjectGroup);
			}
		}

		logger.debug(AuthProjectGroupsDTO.toJsonArray(authProjectsGroups));
		agp.setGroups(authGroups);
		agp.setProjects(authProjectsGroups);

		return agp;

	}

	@Override
	public CodeTableDTO getProjectCodeTable(LsThing project) {
		CodeTableDTO codeTable = new CodeTableDTO();
		codeTable.setName(LsThingLabel.pickBestLabel(project.getLsLabels()).getLabelText());
		codeTable.setCode(project.getCodeName());
		codeTable.setIgnored(project.isIgnored());
		return codeTable;
	}


	@Override
	public List<CodeTableDTO> convertProjectsToCodeTables(Collection<LsThing> projects) {
		List<CodeTableDTO> codeTableList = new ArrayList<CodeTableDTO>();
		for (LsThing project : projects) {
			CodeTableDTO codeTable = getProjectCodeTable(project);
			codeTableList.add(codeTable);
		}
		return codeTableList;	
	}


	@Override
	public Collection<Author> findAuthorsByRoleTypeAndRoleKindAndRoleName(
			String roleType, String roleKind, String roleName) {
		List<LsRole> roleEntries = LsRole.findLsRolesByLsTypeEqualsAndLsKindEqualsAndRoleNameEquals(roleType, roleKind, roleName).getResultList();
		Collection<Author> authors = new HashSet<Author>();
		for (LsRole roleEntry : roleEntries){
			Collection<AuthorRole> authorRoles = AuthorRole.findAuthorRolesByRoleEntry(roleEntry).getResultList();
			for (AuthorRole authorRole : authorRoles){
				authors.add(authorRole.getUserEntry());
			}	
		}
		return authors;
	}

	@Override
	@Transactional
	public Author saveAuthor(Author author) {
		if (logger.isDebugEnabled()) logger.debug("incoming meta author: " + author.toJson() + "\n");
		Author newAuthor = new Author(author);
		if (newAuthor.getCodeName() == null){
			newAuthor.setCodeName(autoLabelService.getAuthorCodeName());
		}
		if (newAuthor.getLsType() == null) newAuthor.setLsType("default");
		if (newAuthor.getLsKind() == null) newAuthor.setLsKind("default");
		if (newAuthor.getLsTypeAndKind() == null) newAuthor.setLsTypeAndKind(newAuthor.getLsType()+"_"+newAuthor.getLsKind());
		newAuthor.persist();
		if (author.getLsLabels() != null){
			Set<AuthorLabel> lsLabels = new HashSet<AuthorLabel>();
			for(AuthorLabel authorLabel : author.getLsLabels()){
				AuthorLabel newAuthorLabel = new AuthorLabel(authorLabel);
				newAuthorLabel.setAuthor(newAuthor);
				if (logger.isDebugEnabled()) logger.debug("here is the newAuthorLabel before save: " + newAuthorLabel.toJson());
				newAuthorLabel.persist();
				lsLabels.add(newAuthorLabel);
			}
			newAuthor.setLsLabels(lsLabels);
		} else {
			if (logger.isDebugEnabled()) logger.debug("No author labels to save");	
		}

		if (author.getLsStates() != null){
			Set<AuthorState> lsStates = new HashSet<AuthorState>();
			for(AuthorState lsState : author.getLsStates()){
				AuthorState newLsState = new AuthorState(lsState);
				newLsState.setAuthor(newAuthor);
				if (logger.isDebugEnabled()) logger.debug("here is the newLsState before save: " + newLsState.toJson());
				newLsState.persist();
				if (logger.isDebugEnabled()) logger.debug("persisted the newLsState: " + newLsState.toJson());
				if (lsState.getLsValues() != null){
					Set<AuthorValue> lsValues = new HashSet<AuthorValue>();
					for(AuthorValue authorValue : lsState.getLsValues()){
						if (logger.isDebugEnabled()) logger.debug("authorValue: " + authorValue.toJson());
						AuthorValue newAuthorValue = AuthorValue.create(authorValue);
						newAuthorValue.setLsState(newLsState);
						newAuthorValue.persist();
						lsValues.add(newAuthorValue);
						if (logger.isDebugEnabled()) logger.debug("persisted the authorValue: " + newAuthorValue.toJson());
					}
					newLsState.setLsValues(lsValues);
				} else {
					if (logger.isDebugEnabled()) logger.debug("No author values to save");
				}
				lsStates.add(newLsState);
			}
			newAuthor.setLsStates(lsStates);
		}

		return Author.findAuthor(newAuthor.getId());
	}


	@Override
	public Author updateAuthor(Author author) {
		logger.info("incoming meta author: " + author.toJson() + "\n");
		Author updatedAuthor = Author.update(author);
		if (author.getLsLabels() != null){
			for(AuthorLabel authorLabel : author.getLsLabels()){
				if (authorLabel.getId() == null){
					AuthorLabel newAuthorLabel = new AuthorLabel(authorLabel);
					newAuthorLabel.setAuthor(updatedAuthor);
					newAuthorLabel.persist();
					updatedAuthor.getLsLabels().add(newAuthorLabel);
				} else {
					AuthorLabel updatedLabel = AuthorLabel.update(authorLabel);
					updatedAuthor.getLsLabels().add(updatedLabel);
				}
			}	
		} else {
			logger.info("No author labels to update");	
		}
		updateLsStates(author, updatedAuthor);
		return Author.findAuthor(updatedAuthor.getId());
	}

	public void updateLsStates(Author jsonAuthor, Author updatedAuthor){
		if(jsonAuthor.getLsStates() != null){
			for(AuthorState lsThingState : jsonAuthor.getLsStates()){
				AuthorState updatedAuthorState;
				if (lsThingState.getId() == null){
					updatedAuthorState = new AuthorState(lsThingState);
					updatedAuthorState.setAuthor(updatedAuthor);
					updatedAuthorState.persist();
					updatedAuthor.getLsStates().add(updatedAuthorState);
					if (logger.isDebugEnabled()) logger.debug("persisted new lsThing state " + updatedAuthorState.getId());

				} else {
					updatedAuthorState = AuthorState.update(lsThingState);
					updatedAuthor.getLsStates().add(updatedAuthorState);

					if (logger.isDebugEnabled()) logger.debug("updated lsThing state " + updatedAuthorState.getId());

				}
				if (lsThingState.getLsValues() != null){
					for(AuthorValue lsThingValue : lsThingState.getLsValues()){
						if (lsThingValue.getLsState() == null) lsThingValue.setLsState(updatedAuthorState);
						AuthorValue updatedAuthorValue;
						if (lsThingValue.getId() == null){
							updatedAuthorValue = AuthorValue.create(lsThingValue);
							updatedAuthorValue.setLsState(AuthorState.findAuthorState(updatedAuthorState.getId()));
							updatedAuthorValue.persist();
							updatedAuthorState.getLsValues().add(updatedAuthorValue);
						} else {
							updatedAuthorValue = AuthorValue.update(lsThingValue);
							if (logger.isDebugEnabled()) logger.debug("updated lsThing value " + updatedAuthorValue.getId());
						}
						if (logger.isDebugEnabled()) logger.debug("checking lsThingValue " + updatedAuthorValue.toJson());

					}	
				} else {
					if (logger.isDebugEnabled()) logger.debug("No lsThing values to update");
				}
			}
		}
	}

	@Override
	public Author getOrCreateAuthor(Author author) {
		String userName = author.getUserName();
		try{
			Author foundAuthor = Author.findAuthorsByUserName(userName).getSingleResult();
			return foundAuthor;
		}catch(EmptyResultDataAccessException e){
			logger.debug("Author "+userName+" not found. Creating new author");
			return saveAuthor(author);
		}
	}
	
	@Override
	public Collection<Author> getAuthorsByIds(Collection<Long> authorIds){
		Collection<Author> authors = new ArrayList<Author>();
		for (Long id : authorIds){
			authors.add(Author.findAuthor(id));
		}
		return authors;
	}
	
	@Override
	public Collection<Long> searchAuthorIdsByQueryDTO(AuthorQueryDTO query) throws Exception{
		List<Long> authorIdList = new ArrayList<Long>();	
		EntityManager em = Author.entityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
		Root<Author> thing = criteria.from(Author.class);
		List<Predicate> predicateList = buildPredicatesForQueryDTO(criteriaBuilder, criteria, thing, query);
		if (query.getLsType() != null){
			Predicate thingType = criteriaBuilder.equal(thing.<String>get("lsType"), query.getLsType());
			predicateList.add(thingType);
		}
		if (query.getLsKind() != null){
			Predicate thingKind = criteriaBuilder.equal(thing.<String>get("lsKind"), query.getLsKind());
			predicateList.add(thingKind);
		}
		Predicate[] predicates = new Predicate[0];
		//gather all predicates
		predicates = predicateList.toArray(predicates);
		criteria.where(criteriaBuilder.and(predicates));
		TypedQuery<Long> q = em.createQuery(criteria);
		logger.debug(q.unwrap(org.hibernate.Query.class).getQueryString());
		authorIdList = q.getResultList();
		logger.debug("Found "+authorIdList.size()+" results.");
		return authorIdList;
	}
	
	private List<Predicate> buildPredicatesForQueryDTO(CriteriaBuilder criteriaBuilder, CriteriaQuery<Long> criteria, Root<Author> thing, AuthorQueryDTO query) throws Exception{
		
		criteria.select(thing.<Long>get("id"));
		criteria.distinct(true);
		List<Predicate> predicateList = new ArrayList<Predicate>();
		//root author properties
		
		//recordedDates
		if (query.getRecordedDateGreaterThan() != null && query.getRecordedDateLessThan() != null){
			try{
				Predicate createdDateBetween = criteriaBuilder.between(thing.<Date>get("recordedDate"), query.getRecordedDateGreaterThan(), query.getRecordedDateLessThan());
				predicateList.add(createdDateBetween);
			}catch (Exception e){
				logger.error("Caught exception trying to parse "+query.getRecordedDateGreaterThan()+" or "+query.getRecordedDateLessThan()+" as a date.",e);
				throw new Exception("Caught exception trying to parse "+query.getRecordedDateGreaterThan()+" or "+query.getRecordedDateLessThan()+" as a date.",e);
			}
		}
		else if (query.getRecordedDateGreaterThan() != null){
			try{
				Predicate createdDateFrom = criteriaBuilder.greaterThanOrEqualTo(thing.<Date>get("recordedDate"), query.getRecordedDateGreaterThan());
				predicateList.add(createdDateFrom);
			}catch (Exception e){
				logger.error("Caught exception trying to parse "+query.getRecordedDateGreaterThan()+" as a date.",e);
				throw new Exception("Caught exception trying to parse "+query.getRecordedDateGreaterThan()+" as a date.",e);
			}
		}
		else if (query.getRecordedDateLessThan() != null){
			try{
				Predicate createdDateTo = criteriaBuilder.lessThanOrEqualTo(thing.<Date>get("recordedDate"), query.getRecordedDateLessThan());
				predicateList.add(createdDateTo);
			}catch (Exception e){
				logger.error("Caught exception trying to parse "+query.getRecordedDateLessThan()+" as a date.",e);
				throw new Exception("Caught exception trying to parse "+query.getRecordedDateLessThan()+" as a date.",e);
			}
		}
		if (query.getRecordedBy() != null){
			Predicate recordedBy = criteriaBuilder.like(thing.<String>get("recordedBy"), '%'+query.getRecordedBy()+'%');
			predicateList.add(recordedBy);
		}
		if (query.getFirstName() != null){
			Predicate firstName = criteriaBuilder.like(thing.<String>get("firstName"), '%'+query.getFirstName()+'%');
			predicateList.add(firstName);
		}
		if (query.getLastName() != null){
			Predicate lastName = criteriaBuilder.like(thing.<String>get("lastName"), '%'+query.getLastName()+'%');
			predicateList.add(lastName);
		}
		if (query.getUserName() != null){
			Predicate userName = criteriaBuilder.like(thing.<String>get("userName"), '%'+query.getUserName()+'%');
			predicateList.add(userName);
		}
		
		//values
		if (query.getValues() != null){
			for (ValueQueryDTO valueQuery : query.getValues()){
				List<Predicate> valuePredicatesList = new ArrayList<Predicate>();
				Join<Author, AuthorState> state = thing.join("lsStates");
				Join<AuthorState, AuthorValue> value = state.join("lsValues");
				
				Predicate stateNotIgn = criteriaBuilder.isFalse(state.<Boolean>get("ignored"));
				Predicate valueNotIgn = criteriaBuilder.isFalse(value.<Boolean>get("ignored"));
				valuePredicatesList.add(stateNotIgn);
				valuePredicatesList.add(valueNotIgn);
				
				if (valueQuery.getStateType() != null){
					Predicate stateType = criteriaBuilder.equal(state.<String>get("lsType"),valueQuery.getStateType());
					valuePredicatesList.add(stateType);
				}
				if (valueQuery.getStateKind() != null){
					Predicate stateKind = criteriaBuilder.equal(state.<String>get("lsKind"),valueQuery.getStateKind());
					valuePredicatesList.add(stateKind);
				}
				if (valueQuery.getValueType() != null){
					Predicate valueType = criteriaBuilder.equal(value.<String>get("lsType"),valueQuery.getValueType());
					valuePredicatesList.add(valueType);
				}
				if (valueQuery.getValueKind() != null){
					Predicate valueKind = criteriaBuilder.equal(value.<String>get("lsKind"),valueQuery.getValueKind());
					valuePredicatesList.add(valueKind);
				}
				if (valueQuery.getValue() != null){
					if (valueQuery.getValueType() == null){
						logger.error("valueType must be specified if value is specified!");
						throw new Exception("valueType must be specified if value is specified!");
					}else if (valueQuery.getValueType().equalsIgnoreCase("dateValue")){
						String postgresTimeUnit = "day";
						Expression<Date> dateTruncExpr = criteriaBuilder.function("date_trunc", Date.class, criteriaBuilder.literal(postgresTimeUnit), value.<Date>get("dateValue"));
						Calendar cal = Calendar.getInstance(); // locale-specific
						boolean parsedTime = false;
						if (SimpleUtil.isNumeric(valueQuery.getValue())){
							cal.setTimeInMillis(Long.valueOf(valueQuery.getValue()));
							parsedTime = true;
						}else{
							try{
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
								cal.setTime(sdf.parse(valueQuery.getValue()));
								parsedTime = true;
							}catch (Exception e){
								logger.warn("Failed to parse date in Author generic query for value",e);
							}
						}
						cal.set(Calendar.HOUR_OF_DAY, 0);
						cal.set(Calendar.MINUTE, 0);
						cal.set(Calendar.SECOND, 0);
						cal.set(Calendar.MILLISECOND, 0);
						long time = cal.getTimeInMillis();
						Date queryDate = new Date(time);
						Predicate valueLike = criteriaBuilder.equal(dateTruncExpr, queryDate);
						if (parsedTime) valuePredicatesList.add(valueLike);
					}else{
						//only works with string value types: stringValue, codeValue, fileValue, clobValue
						Predicate valueLike = criteriaBuilder.like(value.<String>get(valueQuery.getValueType()), '%' + valueQuery.getValue() + '%');
						valuePredicatesList.add(valueLike);
					}
				}
				//gather predicates with AND
				Predicate[] valuePredicates = new Predicate[0];
				valuePredicates = valuePredicatesList.toArray(valuePredicates);
				predicateList.add(criteriaBuilder.and(valuePredicates));
			}
		}
		
		//labels
		if (query.getLabels() != null){
			for (LabelQueryDTO queryLabel : query.getLabels()){
				Join<Author, AuthorLabel> label = thing.join("lsLabels");
				List<Predicate> labelPredicatesList = new ArrayList<Predicate>();
				if (queryLabel.getLabelType() != null){
					Predicate labelType = criteriaBuilder.equal(label.<String>get("lsType"), queryLabel.getLabelType());
					labelPredicatesList.add(labelType);
				}
				if (queryLabel.getLabelKind() != null){
					Predicate labelKind = criteriaBuilder.equal(label.<String>get("lsKind"), queryLabel.getLabelKind());
					labelPredicatesList.add(labelKind);
				}
				if (queryLabel.getLabelText() != null){
					Predicate labelText = criteriaBuilder.like(label.<String>get("labelText"), '%'+queryLabel.getLabelText()+'%');
					labelPredicatesList.add(labelText);
				}
				//gather labels
				Predicate[] labelPredicates = new Predicate[0];
				labelPredicates = labelPredicatesList.toArray(labelPredicates);
				predicateList.add(criteriaBuilder.and(labelPredicates));
			}
		}
		return predicateList;
	}


	@Override
	public Collection<Long> searchAuthorIdsByBrowserQueryDTO(
			AuthorBrowserQueryDTO query) throws Exception{
		List<Long> authorIdList = new ArrayList<Long>();	
		EntityManager em = Author.entityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
		Root<Author> thing = criteria.from(Author.class);
		List<Predicate> metaPredicateList = new ArrayList<Predicate>();
		//split query string into terms
		String queryString = query.getQueryString().replaceAll("\\*", "%");
		List<String> splitQuery = SimpleUtil.splitSearchString(queryString);
		logger.debug("Number of search terms: " + splitQuery.size());
		//for each search term, construct a queryDTO with that term filled in every search position of the passed in queryDTO
		for (String searchTerm : splitQuery){
			AuthorQueryDTO queryDTO = new AuthorQueryDTO(query.getQueryDTO());
			if (queryDTO.getValues() != null){
				for (ValueQueryDTO value : queryDTO.getValues()){
					value.setValue(searchTerm);
				}
			}
			if (queryDTO.getLabels() != null){
				for (LabelQueryDTO label : queryDTO.getLabels()){
					label.setLabelText(searchTerm);
				}
			}
			queryDTO.setFirstName(searchTerm);
			queryDTO.setLastName(searchTerm);
			queryDTO.setUserName(searchTerm);
			//get a list of predicates for that queryDTO, OR them all together, then add to the meta list
			List<Predicate> predicateList = buildPredicatesForQueryDTO(criteriaBuilder, criteria, thing, queryDTO);
			Predicate[] predicates = new Predicate[0];
			predicates = predicateList.toArray(predicates);
			Predicate searchTermPredicate = criteriaBuilder.or(predicates);
			metaPredicateList.add(searchTermPredicate);
		}
		//add in thingType and thingKind as required at top level
		if (query.getQueryDTO().getLsType() != null){
			Predicate thingType = criteriaBuilder.equal(thing.<String>get("lsType"), query.getQueryDTO().getLsType());
			metaPredicateList.add(thingType);
		}
		if (query.getQueryDTO().getLsKind() != null){
			Predicate thingKind = criteriaBuilder.equal(thing.<String>get("lsKind"), query.getQueryDTO().getLsKind());
			metaPredicateList.add(thingKind);
		}
		//gather the predicates for each search term, and AND them all together
		Predicate[] metaPredicates = new Predicate[0];
		metaPredicates = metaPredicateList.toArray(metaPredicates);
		criteria.where(criteriaBuilder.and(metaPredicates));
		TypedQuery<Long> q = em.createQuery(criteria);
		logger.debug(q.unwrap(org.hibernate.Query.class).getQueryString());
		authorIdList = q.getResultList();
		logger.debug("Found "+authorIdList.size()+" results.");
		return authorIdList;
	}

}
