package com.labsynch.labseer.api;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.labsynch.labseer.domain.SolutionUnit;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.path.PathBuilder;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(value = {"/api/v1/solutionUnits"})
@Controller
public class ApiSolutionUnitController {

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        SolutionUnit solutionunit = SolutionUnit.findSolutionUnit(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text; charset=utf-8");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
        headers.add("Pragma", "no-cache"); //HTTP 1.0
        headers.setExpires(0); // Expire the cache
        if (solutionunit == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(solutionunit.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text; charset=utf-8");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
        headers.add("Pragma", "no-cache"); //HTTP 1.0
        headers.setExpires(0); // Expire the cache
        return new ResponseEntity<String>(SolutionUnit.toJsonArray(SolutionUnit.findAllSolutionUnits()), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json) {
        SolutionUnit.fromJsonToSolutionUnit(json).persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text; charset=utf-8");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
        headers.add("Pragma", "no-cache"); //HTTP 1.0
        headers.setExpires(0); // Expire the cache
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        for (SolutionUnit solutionUnit : SolutionUnit.fromJsonArrayToSolutionUnits(json)) {
            solutionUnit.persist();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text; charset=utf-8");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
        headers.add("Pragma", "no-cache"); //HTTP 1.0
        headers.setExpires(0); // Expire the cache
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text; charset=utf-8");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
        headers.add("Pragma", "no-cache"); //HTTP 1.0
        headers.setExpires(0); // Expire the cache
        if (SolutionUnit.fromJsonToSolutionUnit(json).merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text; charset=utf-8");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
        headers.add("Pragma", "no-cache"); //HTTP 1.0
        headers.setExpires(0); // Expire the cache
        for (SolutionUnit solutionUnit : SolutionUnit.fromJsonArrayToSolutionUnits(json)) {
            if (solutionUnit.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
        SolutionUnit solutionunit = SolutionUnit.findSolutionUnit(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text; charset=utf-8");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
        headers.add("Pragma", "no-cache"); //HTTP 1.0
        headers.setExpires(0); // Expire the cache
        if (solutionunit == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        solutionunit.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<String> getOptions() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
        headers.add("Pragma", "no-cache"); //HTTP 1.0
        headers.setExpires(0); // Expire the cache
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

	@Autowired
    public ConversionService conversionService_dtt;

	@Autowired
    public MessageSource messageSource_dtt;

	public BeanWrapper beanWrapper;


	public Map<String, String> populateParametersMap(HttpServletRequest request) {
        Map<String, Object> params;
        if (request == null) {
            params = Collections.emptyMap();
        } else {
            params = new HashMap<String, Object>(request.getParameterMap());
        }
        
        Map<String, String> allParams = new HashMap<String, String>(params.size());
        
        String value;
        Object objValue;
        for (String key : params.keySet()) {
            objValue = params.get(key);
            if (objValue instanceof String[]) {
                value = ((String[]) objValue)[0];
            } else {
                value = (String) objValue;
            }
            allParams.put(key, value);
        }
        return allParams;
    }

	public Map<String, Object> getPropertyMap(SolutionUnit SolutionUnit, Enumeration<Map<String, String>> propertyNames) {
        Map<String, Object> propertyValuesMap = new HashMap<String, Object>();
        
        // If no entity or properties given, return empty Map
        if(SolutionUnit == null || propertyNames == null) {
            return propertyValuesMap;
        }
        
        List<String> properties = new ArrayList<String>();
        CollectionUtils.addAll(properties, propertyNames);
        
        // There must be at least one property name, otherwise return empty Map
        if (properties.isEmpty()) {
            return propertyValuesMap;
        }
        
        // Iterate over given properties to get each property value
        BeanWrapper entityBean = new BeanWrapperImpl(SolutionUnit);
        for (String propertyName : properties) {
            if (entityBean.isReadableProperty(propertyName)) {
                Object propertyValue = null;
                try {
                    propertyValue = entityBean.getPropertyValue(propertyName);
                } catch (Exception e){
                    // TODO log warning
                    continue;
                }
                propertyValuesMap.put(propertyName, propertyValue);
            }
        }
        return propertyValuesMap;
    }

	@ResponseBody
    @RequestMapping(headers = "Accept=application/json", params = "getColumnType")
    public String getColumnType(Model uiModel, HttpServletRequest request, @RequestParam(value = "_columnName_", required = false) String columnName) {
        // Getting all declared fields
        boolean fieldExists = false;
        Field attr = null;
        for(Field field : SolutionUnit.class.getDeclaredFields()){
            if(field.getName().equals(columnName)){
                attr = field;
                fieldExists = true;
                break;
            }
        }
        // If current field not exists on entity, find on superclass
        if(!fieldExists){
            if(SolutionUnit.class.getSuperclass() != null){
                for(Field field : SolutionUnit.class.getSuperclass().getDeclaredFields()){
                    if(field.getName().equals(columnName)){
                        attr = field;
                        fieldExists = true;
                        break;
                    }
                }
            }
        }
        if(fieldExists){
            // Getting field type
            Object fieldType = null;
            if (attr != null) {
                fieldType = attr.getType();
                String type = fieldType.toString();
                // Returning value based on type
                if ("String".equals(type)){
                    return "{\"columnType\": \"string\"}";
                } else if ("float".equals(type) || type.contains("Float")){
                    return "{\"columnType\": \"number\"}";
                } else if ("short".equals(type) || type.contains("Short")){
                    return "{\"columnType\": \"number\"}";
                } else if ("long".equals(type) || type.contains("Long")){
                    return "{\"columnType\": \"number\"}";
                } else if ("double".equals(type) || type.contains("Double")){
                    return "{\"columnType\": \"number\"}";
                } else if ("int".equals(type) || type.contains("Integer")){
                    return "{\"columnType\": \"number\"}";
                } else if ("Date".equals(type)){
                    return "{\"columnType\": \"date\"}";
                } else if ("boolean".equals(type) || type.contains("Boolean")){
                    return "{\"columnType\": \"boolean\"}";
                } else {
                    // Returning by default
                    return "{\"columnType\": \"undefined\"}";
                }
            }
        }
        // Returning by default
        return "{\"columnType\": \"undefined\"}";
    }

	@ResponseBody
    @RequestMapping(headers = "Accept=application/json", params = "geti18nText")
    public String geti18nText(Model uiModel, HttpServletRequest request, @RequestParam(value = "_locale_", required = false) String locale) {
        // Getting current locale
        Locale defaultLocale = new Locale(locale);
        // Building JSON response
        StringBuilder json = new StringBuilder();
        json.append("\"all_isnull\": \"" + messageSource_dtt.getMessage("global.filters.operations.all.isnull", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"all_notnull\": \"" + messageSource_dtt.getMessage("global.filters.operations.all.notnull", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"boolean_false\": \"" + messageSource_dtt.getMessage("global.filters.operations.boolean.false", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"boolean_true\": \"" + messageSource_dtt.getMessage("global.filters.operations.boolean.true", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"date_between\": \"" + messageSource_dtt.getMessage("global.filters.operations.date.between", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"date_date\": \"" + messageSource_dtt.getMessage("global.filters.operations.date.date", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"date_day\": \"" + messageSource_dtt.getMessage("global.filters.operations.date.day", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"date_month\": \"" + messageSource_dtt.getMessage("global.filters.operations.date.month", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"date_year\": \"" + messageSource_dtt.getMessage("global.filters.operations.date.year", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"number_between\": \"" + messageSource_dtt.getMessage("global.filters.operations.number.between", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"string_contains\": \"" + messageSource_dtt.getMessage("global.filters.operations.string.contains", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"string_ends\": \"" + messageSource_dtt.getMessage("global.filters.operations.string.ends", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"string_isempty\": \"" + messageSource_dtt.getMessage("global.filters.operations.string.isempty", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"string_isnotempty\": \"" + messageSource_dtt.getMessage("global.filters.operations.string.isnotempty", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"string_starts\": \"" + messageSource_dtt.getMessage("global.filters.operations.string.starts", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"button_find\": \"" + messageSource_dtt.getMessage("button_find", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_all_isnull\": \"" + messageSource_dtt.getMessage("help.all.isnull", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_all_notnull\": \"" + messageSource_dtt.getMessage("help.all.notnull", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_boolean_false\": \"" + messageSource_dtt.getMessage("help.boolean.false", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_boolean_true\": \"" + messageSource_dtt.getMessage("help.boolean.true", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_date_between\": \"" + messageSource_dtt.getMessage("help.date.between", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_date_date\": \"" + messageSource_dtt.getMessage("help.date.date", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_date_day\": \"" + messageSource_dtt.getMessage("help.date.day", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_date_month\": \"" + messageSource_dtt.getMessage("help.date.month", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_date_year\": \"" + messageSource_dtt.getMessage("help.date.year", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_number_between\": \"" + messageSource_dtt.getMessage("help.number.between", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_number_eq\": \"" + messageSource_dtt.getMessage("help.number.eq", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_number_neq\": \"" + messageSource_dtt.getMessage("help.number.neq", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_number_gt\": \"" + messageSource_dtt.getMessage("help.number.gt", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_number_lt\": \"" + messageSource_dtt.getMessage("help.number.lt", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_number_goe\": \"" + messageSource_dtt.getMessage("help.number.goe", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_number_loe\": \"" + messageSource_dtt.getMessage("help.number.loe", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_string_contains\": \"" + messageSource_dtt.getMessage("help.string.contains", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_string_ends\": \"" + messageSource_dtt.getMessage("help.string.ends", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_string_isempty\": \"" + messageSource_dtt.getMessage("help.string.isempty", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_string_isnotempty\": \"" + messageSource_dtt.getMessage("help.string.isnotempty", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_string_starts\": \"" + messageSource_dtt.getMessage("help.string.starts", null, defaultLocale) + "\"");
        json.append("}");
        // return JSON with locale strings
        return json.toString();
    }

	public List<SolutionUnit> findSolutionUnitsByParameters(SolutionUnit SolutionUnit, Enumeration<Map<String, String>> propertyNames) {
        // Gets propertyMap
        Map<String, Object> propertyMap = getPropertyMap(SolutionUnit, propertyNames);
        
        // if there is a filter
        if (!propertyMap.isEmpty()) {
            // Prepare a predicate
            BooleanBuilder baseFilterPredicate = new BooleanBuilder();
            
            // Base filter. Using BooleanBuilder, a cascading builder for
            // Predicate expressions
            PathBuilder<SolutionUnit> entity = new PathBuilder<SolutionUnit>(SolutionUnit.class, "entity");
            
            // Build base filter
            for (String key : propertyMap.keySet()) {
                baseFilterPredicate.and(entity.get(key).eq(propertyMap.get(key)));
            }
            
            // Create a query with filter
            JPAQuery query = new JPAQuery(SolutionUnit.entityManager());
            query = query.from(entity);
            
            // execute query
            return query.where(baseFilterPredicate).list(entity);
        }
        
        // no filter: return all elements
        return SolutionUnit.findAllSolutionUnits();
    }
}
