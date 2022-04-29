package com.labsynch.labseer.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.ContainerType;
import com.labsynch.labseer.domain.DDictType;
import com.labsynch.labseer.domain.ExperimentType;
import com.labsynch.labseer.domain.InteractionType;
import com.labsynch.labseer.domain.LabelType;
import com.labsynch.labseer.domain.OperatorType;
import com.labsynch.labseer.domain.ProtocolType;
import com.labsynch.labseer.domain.StateType;
import com.labsynch.labseer.domain.ThingType;
import com.labsynch.labseer.domain.UnitType;
import com.labsynch.labseer.domain.ValueType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Transactional
@RequestMapping("api/v1")
@Controller

public class ApiTypeController {

    private static final Logger logger = LoggerFactory.getLogger(ApiTypeController.class);

    @RequestMapping(value = "/ddicttypes/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showDDictTypeJson(@PathVariable("id") Long id) {
        DDictType DDictType_ = DDictType.findDDictType(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (DDictType_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(DDictType_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/ddicttypes", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listDDictTypeJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<DDictType> result = DDictType.findAllDDictTypes();
        return new ResponseEntity<String>(DDictType.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/ddicttypes", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createDDictTypeFromJson(@RequestBody DDictType DDictType_) {
        DDictType_.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(DDictType_.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/ddicttypes/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createDDictTypeFromJsonArray(@RequestBody List<DDictType> dDictTypes) {
        Collection<DDictType> newDDictTypes = new ArrayList<DDictType>();
        for (DDictType DDictType_ : dDictTypes) {
            DDictType_.persist();
            newDDictTypes.add(DDictType_);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(DDictType.toJsonArray(newDDictTypes), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/ddicttypes/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateDDictTypeFromJson(@RequestBody DDictType DDictType_) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (DDictType_.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(DDictType_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/ddicttypes/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateDDictTypeFromJsonArray(@RequestBody List<DDictType> dDictTypes) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<DDictType> newDDictTypes = new ArrayList<DDictType>();
        for (DDictType DDictType_ : dDictTypes) {
            if (DDictType_.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            newDDictTypes.add(DDictType_);
        }
        return new ResponseEntity<String>(DDictType.toJsonArray(newDDictTypes), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/ddicttypes/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteDDictTypeFromJson(@PathVariable("id") Long id) {
        DDictType DDictType_ = DDictType.findDDictType(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (DDictType_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        DDictType_.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/containertypes/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showContainerTypeJson(@PathVariable("id") Long id) {
        ContainerType ContainerType_ = ContainerType.findContainerType(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (ContainerType_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ContainerType_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/containertypes", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listContainerTypeJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<ContainerType> result = ContainerType.findAllContainerTypes();
        return new ResponseEntity<String>(ContainerType.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/containertypes", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createContainerTypeFromJson(@RequestBody ContainerType ContainerType_) {
        ContainerType_.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(ContainerType_.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/containertypes/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createContainerTypeFromJsonArray(@RequestBody List<ContainerType> containerTypes) {
        Collection<ContainerType> newContainerTypes = new ArrayList<ContainerType>();
        for (ContainerType ContainerType_ : containerTypes) {
            ContainerType_.persist();
            newContainerTypes.add(ContainerType_);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(ContainerType.toJsonArray(newContainerTypes), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/containertypes/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateContainerTypeFromJson(@RequestBody ContainerType ContainerType_) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ContainerType_.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ContainerType_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/containertypes/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateContainerTypeFromJsonArray(@RequestBody List<ContainerType> containerTypes) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<ContainerType> newContainerTypes = new ArrayList<ContainerType>();
        for (ContainerType ContainerType_ : containerTypes) {
            if (ContainerType_.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            newContainerTypes.add(ContainerType_);
        }
        return new ResponseEntity<String>(ContainerType.toJsonArray(newContainerTypes), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/containertypes/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteContainerTypeFromJson(@PathVariable("id") Long id) {
        ContainerType ContainerType_ = ContainerType.findContainerType(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ContainerType_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        ContainerType_.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/experimenttypes/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showExperimentTypeJson(@PathVariable("id") Long id) {
        ExperimentType ExperimentType_ = ExperimentType.findExperimentType(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (ExperimentType_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ExperimentType_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/experimenttypes", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listExperimentTypeJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<ExperimentType> result = ExperimentType.findAllExperimentTypes();
        return new ResponseEntity<String>(ExperimentType.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/experimenttypes", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createExperimentTypeFromJson(@RequestBody ExperimentType ExperimentType_) {
        ExperimentType_.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(ExperimentType_.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/experimenttypes/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createExperimentTypeFromJsonArray(@RequestBody List<ExperimentType> experimentTypes) {
        Collection<ExperimentType> newExperimentTypes = new ArrayList<ExperimentType>();
        for (ExperimentType ExperimentType_ : experimentTypes) {
            ExperimentType_.persist();
            newExperimentTypes.add(ExperimentType_);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(ExperimentType.toJsonArray(newExperimentTypes), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/experimenttypes/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateExperimentTypeFromJson(@RequestBody ExperimentType ExperimentType_) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ExperimentType_.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ExperimentType_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/experimenttypes/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateExperimentTypeFromJsonArray(@RequestBody List<ExperimentType> experimentTypes) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<ExperimentType> newExperimentTypes = new ArrayList<ExperimentType>();
        for (ExperimentType ExperimentType_ : experimentTypes) {
            if (ExperimentType_.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            newExperimentTypes.add(ExperimentType_);
        }
        return new ResponseEntity<String>(ExperimentType.toJsonArray(newExperimentTypes), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/experimenttypes/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteExperimentTypeFromJson(@PathVariable("id") Long id) {
        ExperimentType ExperimentType_ = ExperimentType.findExperimentType(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ExperimentType_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        ExperimentType_.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/labeltypes/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showLabelTypeJson(@PathVariable("id") Long id) {
        LabelType LabelType_ = LabelType.findLabelType(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (LabelType_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(LabelType_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/labeltypes", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listLabelTypeJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<LabelType> result = LabelType.findAllLabelTypes();
        return new ResponseEntity<String>(LabelType.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/labeltypes", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createLabelTypeFromJson(@RequestBody LabelType LabelType_) {
        LabelType_.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(LabelType_.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/labeltypes/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createLabelTypeFromJsonArray(@RequestBody List<LabelType> labelTypes) {
        Collection<LabelType> newLabelTypes = new ArrayList<LabelType>();
        for (LabelType LabelType_ : labelTypes) {
            LabelType_.persist();
            newLabelTypes.add(LabelType_);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(LabelType.toJsonArray(newLabelTypes), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/labeltypes/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateLabelTypeFromJson(@RequestBody LabelType LabelType_) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (LabelType_.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(LabelType_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/labeltypes/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateLabelTypeFromJsonArray(@RequestBody List<LabelType> labelTypes) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<LabelType> newLabelTypes = new ArrayList<LabelType>();
        for (LabelType LabelType_ : labelTypes) {
            if (LabelType_.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            newLabelTypes.add(LabelType_);
        }
        return new ResponseEntity<String>(LabelType.toJsonArray(newLabelTypes), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/labeltypes/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteLabelTypeFromJson(@PathVariable("id") Long id) {
        LabelType LabelType_ = LabelType.findLabelType(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (LabelType_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        LabelType_.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/operatortypes/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showOperatorTypeJson(@PathVariable("id") Long id) {
        OperatorType OperatorType_ = OperatorType.findOperatorType(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (OperatorType_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(OperatorType_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/operatortypes", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listOperatorTypeJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<OperatorType> result = OperatorType.findAllOperatorTypes();
        return new ResponseEntity<String>(OperatorType.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/operatortypes", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createOperatorTypeFromJson(@RequestBody OperatorType OperatorType_) {
        OperatorType_.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(OperatorType_.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/operatortypes/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createOperatorTypeFromJsonArray(@RequestBody List<OperatorType> operatorTypes) {
        Collection<OperatorType> newOperatorTypes = new ArrayList<OperatorType>();
        for (OperatorType OperatorType_ : operatorTypes) {
            OperatorType_.persist();
            newOperatorTypes.add(OperatorType_);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(OperatorType.toJsonArray(newOperatorTypes), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/operatortypes/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateOperatorTypeFromJson(@RequestBody OperatorType OperatorType_) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (OperatorType_.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(OperatorType_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/operatortypes/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateOperatorTypeFromJsonArray(@RequestBody List<OperatorType> operatorTypes) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<OperatorType> newOperatorTypes = new ArrayList<OperatorType>();
        for (OperatorType OperatorType_ : operatorTypes) {
            if (OperatorType_.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            newOperatorTypes.add(OperatorType_);
        }
        return new ResponseEntity<String>(OperatorType.toJsonArray(newOperatorTypes), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/operatortypes/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteOperatorTypeFromJson(@PathVariable("id") Long id) {
        OperatorType OperatorType_ = OperatorType.findOperatorType(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (OperatorType_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        OperatorType_.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/protocoltypes/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showProtocolTypeJson(@PathVariable("id") Long id) {
        ProtocolType ProtocolType_ = ProtocolType.findProtocolType(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (ProtocolType_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ProtocolType_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/protocoltypes", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listProtocolTypeJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<ProtocolType> result = ProtocolType.findAllProtocolTypes();
        return new ResponseEntity<String>(ProtocolType.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/protocoltypes", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createProtocolTypeFromJson(@RequestBody ProtocolType ProtocolType_) {
        ProtocolType_.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(ProtocolType_.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/protocoltypes/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createProtocolTypeFromJsonArray(@RequestBody List<ProtocolType> protocolTypes) {
        Collection<ProtocolType> newProtocolTypes = new ArrayList<ProtocolType>();
        for (ProtocolType ProtocolType_ : protocolTypes) {
            ProtocolType_.persist();
            newProtocolTypes.add(ProtocolType_);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(ProtocolType.toJsonArray(newProtocolTypes), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/protocoltypes/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateProtocolTypeFromJson(@RequestBody ProtocolType ProtocolType_) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ProtocolType_.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ProtocolType_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/protocoltypes/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateProtocolTypeFromJsonArray(@RequestBody List<ProtocolType> protocolTypes) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<ProtocolType> newProtocolTypes = new ArrayList<ProtocolType>();
        for (ProtocolType ProtocolType_ : protocolTypes) {
            if (ProtocolType_.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            newProtocolTypes.add(ProtocolType_);
        }
        return new ResponseEntity<String>(ProtocolType.toJsonArray(newProtocolTypes), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/protocoltypes/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteProtocolTypeFromJson(@PathVariable("id") Long id) {
        ProtocolType ProtocolType_ = ProtocolType.findProtocolType(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ProtocolType_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        ProtocolType_.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/statetypes/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showStateTypeJson(@PathVariable("id") Long id) {
        StateType StateType_ = StateType.findStateType(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (StateType_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(StateType_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/statetypes", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listStateTypeJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<StateType> result = StateType.findAllStateTypes();
        return new ResponseEntity<String>(StateType.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/statetypes", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createStateTypeFromJson(@RequestBody StateType StateType_) {
        StateType_.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(StateType_.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/statetypes/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createStateTypeFromJsonArray(@RequestBody List<StateType> stateTypes) {
        Collection<StateType> newStateTypes = new ArrayList<StateType>();
        for (StateType StateType_ : stateTypes) {
            StateType_.persist();
            newStateTypes.add(StateType_);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(StateType.toJsonArray(newStateTypes), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/statetypes/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateStateTypeFromJson(@RequestBody StateType StateType_) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (StateType_.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(StateType_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/statetypes/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateStateTypeFromJsonArray(@RequestBody List<StateType> stateTypes) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<StateType> newStateTypes = new ArrayList<StateType>();
        for (StateType StateType_ : stateTypes) {
            if (StateType_.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            newStateTypes.add(StateType_);
        }
        return new ResponseEntity<String>(StateType.toJsonArray(newStateTypes), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/statetypes/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteStateTypeFromJson(@PathVariable("id") Long id) {
        StateType StateType_ = StateType.findStateType(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (StateType_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        StateType_.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/thingtypes/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showThingTypeJson(@PathVariable("id") Long id) {
        ThingType ThingType_ = ThingType.findThingType(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (ThingType_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ThingType_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/thingtypes", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listThingTypeJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<ThingType> result = ThingType.findAllThingTypes();
        return new ResponseEntity<String>(ThingType.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/thingtypes", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createThingTypeFromJson(@RequestBody ThingType ThingType_) {
        ThingType_.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(ThingType_.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/thingtypes/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createThingTypeFromJsonArray(@RequestBody List<ThingType> thingTypes) {
        Collection<ThingType> newThingTypes = new ArrayList<ThingType>();
        for (ThingType ThingType_ : thingTypes) {
            ThingType_.persist();
            newThingTypes.add(ThingType_);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(ThingType.toJsonArray(newThingTypes), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/thingtypes/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateThingTypeFromJson(@RequestBody ThingType ThingType_) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ThingType_.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ThingType_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/thingtypes/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateThingTypeFromJsonArray(@RequestBody List<ThingType> thingTypes) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<ThingType> newThingTypes = new ArrayList<ThingType>();
        for (ThingType ThingType_ : thingTypes) {
            if (ThingType_.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            newThingTypes.add(ThingType_);
        }
        return new ResponseEntity<String>(ThingType.toJsonArray(newThingTypes), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/thingtypes/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteThingTypeFromJson(@PathVariable("id") Long id) {
        ThingType ThingType_ = ThingType.findThingType(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ThingType_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        ThingType_.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/unittypes/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showUnitTypeJson(@PathVariable("id") Long id) {
        UnitType UnitType_ = UnitType.findUnitType(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (UnitType_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(UnitType_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/unittypes", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listUnitTypeJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<UnitType> result = UnitType.findAllUnitTypes();
        return new ResponseEntity<String>(UnitType.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/unittypes", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createUnitTypeFromJson(@RequestBody UnitType UnitType_) {
        UnitType_.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(UnitType_.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/unittypes/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createUnitTypeFromJsonArray(@RequestBody List<UnitType> unitTypes) {
        Collection<UnitType> newUnitTypes = new ArrayList<UnitType>();
        for (UnitType UnitType_ : unitTypes) {
            UnitType_.persist();
            unitTypes.add(UnitType_);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(UnitType.toJsonArray(newUnitTypes), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/unittypes/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateUnitTypeFromJson(@RequestBody UnitType UnitType_) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (UnitType_.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(UnitType_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/unittypes/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateUnitTypeFromJsonArray(@RequestBody List<UnitType> unitTypes) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<UnitType> newUnitTypes = new ArrayList<UnitType>();
        for (UnitType UnitType_ : unitTypes) {
            if (UnitType_.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            unitTypes.add(UnitType_);
        }
        return new ResponseEntity<String>(UnitType.toJsonArray(newUnitTypes), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/unittypes/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteUnitTypeFromJson(@PathVariable("id") Long id) {
        UnitType UnitType_ = UnitType.findUnitType(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (UnitType_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        UnitType_.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/valuetypes/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showValueTypeJson(@PathVariable("id") Long id) {
        ValueType ValueType_ = ValueType.findValueType(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (ValueType_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ValueType_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/valuetypes", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listValueTypeJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<ValueType> result = ValueType.findAllValueTypes();
        return new ResponseEntity<String>(ValueType.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/valuetypes", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createValueTypeFromJson(@RequestBody ValueType ValueType_) {
        ValueType_.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(ValueType_.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/valuetypes/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createValueTypeFromJsonArray(@RequestBody List<ValueType> valueTypes) {
        Collection<ValueType> newValueTypes = new ArrayList<ValueType>();
        for (ValueType ValueType_ : valueTypes) {
            ValueType_.persist();
            newValueTypes.add(ValueType_);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(ValueType.toJsonArray(newValueTypes), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/valuetypes/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateValueTypeFromJson(@RequestBody ValueType ValueType_) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ValueType_.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ValueType_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/valuetypes/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateValueTypeFromJsonArray(@RequestBody List<ValueType> valueTypes) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<ValueType> newValueTypes = new ArrayList<ValueType>();
        for (ValueType ValueType_ : valueTypes) {
            if (ValueType_.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            newValueTypes.add(ValueType_);
        }
        return new ResponseEntity<String>(ValueType.toJsonArray(newValueTypes), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/valuetypes/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteValueTypeFromJson(@PathVariable("id") Long id) {
        ValueType ValueType_ = ValueType.findValueType(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ValueType_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        ValueType_.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/interactiontypes/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showInteractionTypeJson(@PathVariable("id") Long id) {
        InteractionType InteractionType_ = InteractionType.findInteractionType(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (InteractionType_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(InteractionType_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/interactiontypes", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listInteractionTypeJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<InteractionType> result = InteractionType.findAllInteractionTypes();
        return new ResponseEntity<String>(InteractionType.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/interactiontypes", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createInteractionTypeFromJson(@RequestBody InteractionType InteractionType_) {
        InteractionType_.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(InteractionType_.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/interactiontypes/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createInteractionTypeFromJsonArray(
            @RequestBody List<InteractionType> interactionTypes) {
        Collection<InteractionType> newInteractionTypes = new ArrayList<InteractionType>();
        for (InteractionType InteractionType_ : interactionTypes) {
            InteractionType_.persist();
            interactionTypes.add(InteractionType_);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(InteractionType.toJsonArray(newInteractionTypes), headers,
                HttpStatus.CREATED);
    }

    @RequestMapping(value = "/interactiontypes/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateInteractionTypeFromJson(@RequestBody InteractionType InteractionType_) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (InteractionType_.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(InteractionType_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/interactiontypes/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateInteractionTypeFromJsonArray(
            @RequestBody List<InteractionType> interactionTypes) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<InteractionType> newInteractionTypes = new ArrayList<InteractionType>();
        for (InteractionType InteractionType_ : interactionTypes) {
            if (InteractionType_.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            newInteractionTypes.add(InteractionType_);
        }
        return new ResponseEntity<String>(InteractionType.toJsonArray(newInteractionTypes), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/interactiontypes/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteInteractionTypeFromJson(@PathVariable("id") Long id) {
        InteractionType InteractionType_ = InteractionType.findInteractionType(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (InteractionType_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        InteractionType_.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
}
