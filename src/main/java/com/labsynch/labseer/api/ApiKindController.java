package com.labsynch.labseer.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.ContainerKind;
import com.labsynch.labseer.domain.DDictKind;
import com.labsynch.labseer.domain.ExperimentKind;
import com.labsynch.labseer.domain.InteractionKind;
import com.labsynch.labseer.domain.LabelKind;
import com.labsynch.labseer.domain.OperatorKind;
import com.labsynch.labseer.domain.ProtocolKind;
import com.labsynch.labseer.domain.StateKind;
import com.labsynch.labseer.domain.ThingKind;
import com.labsynch.labseer.domain.UnitKind;
import com.labsynch.labseer.domain.ValueKind;

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

public class ApiKindController {

    private static final Logger logger = LoggerFactory.getLogger(ApiKindController.class);

    @RequestMapping(value = "/ddictkinds/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showDDictKindJson(@PathVariable("id") Long id) {
        DDictKind DDictKind_ = DDictKind.findDDictKind(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (DDictKind_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(DDictKind_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/ddictkinds", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listDDictKindJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<DDictKind> result = DDictKind.findAllDDictKinds();
        return new ResponseEntity<String>(DDictKind.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/ddictkinds", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createDDictKindFromJson(@RequestBody DDictKind DDictKind_) {
        DDictKind_.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(DDictKind_.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/ddictkinds/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createDDictKindFromJsonArray(@RequestBody List<DDictKind> dDictKinds) {
        Collection<DDictKind> newDDictKinds = new ArrayList<DDictKind>();
        for (DDictKind DDictKind_ : dDictKinds) {
            DDictKind_.persist();
            newDDictKinds.add(DDictKind_);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(DDictKind.toJsonArray(newDDictKinds), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/ddictkinds/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateDDictKindFromJson(@RequestBody DDictKind DDictKind_) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (DDictKind_.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(DDictKind_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/ddictkinds/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateDDictKindFromJsonArray(@RequestBody List<DDictKind> dDictKinds) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<DDictKind> newDDictKinds = new ArrayList<DDictKind>();
        for (DDictKind DDictKind_ : dDictKinds) {
            if (DDictKind_.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            newDDictKinds.add(DDictKind_);
        }
        return new ResponseEntity<String>(DDictKind.toJsonArray(newDDictKinds), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/ddictkinds/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteDDictKindFromJson(@PathVariable("id") Long id) {
        DDictKind DDictKind_ = DDictKind.findDDictKind(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (DDictKind_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        DDictKind_.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/containerkinds/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showContainerKindJson(@PathVariable("id") Long id) {
        ContainerKind ContainerKind_ = ContainerKind.findContainerKind(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (ContainerKind_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ContainerKind_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/containerkinds", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listContainerKindJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<ContainerKind> result = ContainerKind.findAllContainerKinds();
        return new ResponseEntity<String>(ContainerKind.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/containerkinds", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createContainerKindFromJson(@RequestBody ContainerKind ContainerKind_) {
        ContainerKind_.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(ContainerKind_.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/containerkinds/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createContainerKindFromJsonArray(@RequestBody List<ContainerKind> containerKinds) {
        Collection<ContainerKind> newContainerKinds = new ArrayList<ContainerKind>();
        for (ContainerKind ContainerKind_ : containerKinds) {
            ContainerKind_.persist();
            newContainerKinds.add(ContainerKind_);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(ContainerKind.toJsonArray(newContainerKinds), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/containerkinds/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateContainerKindFromJson(@RequestBody ContainerKind ContainerKind_) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ContainerKind_.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ContainerKind_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/containerkinds/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateContainerKindFromJsonArray(@RequestBody List<ContainerKind> containerKinds) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<ContainerKind> newContainerKinds = new ArrayList<ContainerKind>();
        for (ContainerKind ContainerKind_ : containerKinds) {
            if (ContainerKind_.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            newContainerKinds.add(ContainerKind_);
        }
        return new ResponseEntity<String>(ContainerKind.toJsonArray(newContainerKinds), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/containerkinds/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteContainerKindFromJson(@PathVariable("id") Long id) {
        ContainerKind ContainerKind_ = ContainerKind.findContainerKind(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ContainerKind_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        ContainerKind_.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/experimentkinds/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showExperimentKindJson(@PathVariable("id") Long id) {
        ExperimentKind ExperimentKind_ = ExperimentKind.findExperimentKind(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (ExperimentKind_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ExperimentKind_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/experimentkinds", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listExperimentKindJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<ExperimentKind> result = ExperimentKind.findAllExperimentKinds();
        return new ResponseEntity<String>(ExperimentKind.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/experimentkinds", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createExperimentKindFromJson(@RequestBody ExperimentKind ExperimentKind_) {
        ExperimentKind_.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(ExperimentKind_.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/experimentkinds/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createExperimentKindFromJsonArray(@RequestBody List<ExperimentKind> experimentKinds) {
        Collection<ExperimentKind> newExperimentKinds = new ArrayList<ExperimentKind>();
        for (ExperimentKind ExperimentKind_ : experimentKinds) {
            ExperimentKind_.persist();
            newExperimentKinds.add(ExperimentKind_);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(ExperimentKind.toJsonArray(newExperimentKinds), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/experimentkinds/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateExperimentKindFromJson(@RequestBody ExperimentKind ExperimentKind_) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ExperimentKind_.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ExperimentKind_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/experimentkinds/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateExperimentKindFromJsonArray(@RequestBody List<ExperimentKind> experimentKinds) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<ExperimentKind> newExperimentKinds = new ArrayList<ExperimentKind>();
        for (ExperimentKind ExperimentKind_ : experimentKinds) {
            if (ExperimentKind_.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            newExperimentKinds.add(ExperimentKind_);
        }
        return new ResponseEntity<String>(ExperimentKind.toJsonArray(newExperimentKinds), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/experimentkinds/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteExperimentKindFromJson(@PathVariable("id") Long id) {
        ExperimentKind ExperimentKind_ = ExperimentKind.findExperimentKind(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ExperimentKind_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        ExperimentKind_.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/labelkinds/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showLabelKindJson(@PathVariable("id") Long id) {
        LabelKind LabelKind_ = LabelKind.findLabelKind(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (LabelKind_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(LabelKind_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/labelkinds", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listLabelKindJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<LabelKind> result = LabelKind.findAllLabelKinds();
        return new ResponseEntity<String>(LabelKind.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/labelkinds", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createLabelKindFromJson(@RequestBody LabelKind LabelKind_) {
        LabelKind_.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(LabelKind_.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/labelkinds/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createLabelKindFromJsonArray(@RequestBody List<LabelKind> labelKinds) {
        Collection<LabelKind> newLabelKinds = new ArrayList<LabelKind>();
        for (LabelKind LabelKind_ : labelKinds) {
            LabelKind_.persist();
            newLabelKinds.add(LabelKind_);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(LabelKind.toJsonArray(newLabelKinds), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/labelkinds/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateLabelKindFromJson(@RequestBody LabelKind LabelKind_) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (LabelKind_.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(LabelKind_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/labelkinds/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateLabelKindFromJsonArray(@RequestBody List<LabelKind> labelKinds) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<LabelKind> newLabelKinds = new ArrayList<LabelKind>();
        for (LabelKind LabelKind_ : labelKinds) {
            if (LabelKind_.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            newLabelKinds.add(LabelKind_);
        }
        return new ResponseEntity<String>(LabelKind.toJsonArray(newLabelKinds), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/labelkinds/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteLabelKindFromJson(@PathVariable("id") Long id) {
        LabelKind LabelKind_ = LabelKind.findLabelKind(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (LabelKind_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        LabelKind_.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/operatorkinds/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showOperatorKindJson(@PathVariable("id") Long id) {
        OperatorKind OperatorKind_ = OperatorKind.findOperatorKind(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (OperatorKind_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(OperatorKind_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/operatorkinds", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listOperatorKindJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<OperatorKind> result = OperatorKind.findAllOperatorKinds();
        return new ResponseEntity<String>(OperatorKind.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/operatorkinds", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createOperatorKindFromJson(@RequestBody OperatorKind OperatorKind_) {
        OperatorKind_.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(OperatorKind_.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/operatorkinds/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createOperatorKindFromJsonArray(@RequestBody List<OperatorKind> operatorKinds) {
        Collection<OperatorKind> newOperatorKinds = new ArrayList<OperatorKind>();
        for (OperatorKind OperatorKind_ : operatorKinds) {
            OperatorKind_.persist();
            newOperatorKinds.add(OperatorKind_);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(OperatorKind.toJsonArray(newOperatorKinds), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/operatorkinds/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateOperatorKindFromJson(@RequestBody OperatorKind OperatorKind_) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (OperatorKind_.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(OperatorKind_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/operatorkinds/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateOperatorKindFromJsonArray(@RequestBody List<OperatorKind> operatorKinds) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<OperatorKind> newOperatorKinds = new ArrayList<OperatorKind>();
        for (OperatorKind OperatorKind_ : operatorKinds) {
            if (OperatorKind_.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            newOperatorKinds.add(OperatorKind_);
        }
        return new ResponseEntity<String>(OperatorKind.toJsonArray(newOperatorKinds), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/operatorkinds/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteOperatorKindFromJson(@PathVariable("id") Long id) {
        OperatorKind OperatorKind_ = OperatorKind.findOperatorKind(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (OperatorKind_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        OperatorKind_.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/protocolkinds/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showProtocolKindJson(@PathVariable("id") Long id) {
        ProtocolKind ProtocolKind_ = ProtocolKind.findProtocolKind(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (ProtocolKind_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ProtocolKind_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/protocolkinds", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listProtocolKindJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<ProtocolKind> result = ProtocolKind.findAllProtocolKinds();
        return new ResponseEntity<String>(ProtocolKind.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/protocolkinds", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createProtocolKindFromJson(@RequestBody ProtocolKind ProtocolKind_) {
        ProtocolKind_.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(ProtocolKind_.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/protocolkinds/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createProtocolKindFromJsonArray(@RequestBody List<ProtocolKind> protocolKinds) {
        Collection<ProtocolKind> newProtocolKinds = new ArrayList<ProtocolKind>();
        for (ProtocolKind ProtocolKind_ : protocolKinds) {
            ProtocolKind_.persist();
            newProtocolKinds.add(ProtocolKind_);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(ProtocolKind.toJsonArray(newProtocolKinds), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/protocolkinds/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateProtocolKindFromJson(@RequestBody ProtocolKind ProtocolKind_) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ProtocolKind_.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ProtocolKind_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/protocolkinds/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateProtocolKindFromJsonArray(@RequestBody List<ProtocolKind> protocolKinds) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<ProtocolKind> newProtocolKinds = new ArrayList<ProtocolKind>();
        for (ProtocolKind ProtocolKind_ : protocolKinds) {
            if (ProtocolKind_.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            newProtocolKinds.add(ProtocolKind_);
        }
        return new ResponseEntity<String>(ProtocolKind.toJsonArray(newProtocolKinds), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/protocolkinds/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteProtocolKindFromJson(@PathVariable("id") Long id) {
        ProtocolKind ProtocolKind_ = ProtocolKind.findProtocolKind(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ProtocolKind_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        ProtocolKind_.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/statekinds/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showStateKindJson(@PathVariable("id") Long id) {
        StateKind StateKind_ = StateKind.findStateKind(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (StateKind_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(StateKind_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/statekinds", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listStateKindJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<StateKind> result = StateKind.findAllStateKinds();
        return new ResponseEntity<String>(StateKind.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/statekinds", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createStateKindFromJson(@RequestBody StateKind StateKind_) {
        StateKind_.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(StateKind_.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/statekinds/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createStateKindFromJsonArray(@RequestBody List<StateKind> stateKinds) {
        Collection<StateKind> newStateKinds = new ArrayList<StateKind>();
        for (StateKind StateKind_ : stateKinds) {
            StateKind_.persist();
            newStateKinds.add(StateKind_);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(StateKind.toJsonArray(newStateKinds), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/statekinds/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateStateKindFromJson(@RequestBody StateKind StateKind_) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (StateKind_.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(StateKind_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/statekinds/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateStateKindFromJsonArray(@RequestBody List<StateKind> stateKinds) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<StateKind> newStateKinds = new ArrayList<StateKind>();
        for (StateKind StateKind_ : stateKinds) {
            if (StateKind_.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            newStateKinds.add(StateKind_);
        }
        return new ResponseEntity<String>(StateKind.toJsonArray(newStateKinds), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/statekinds/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteStateKindFromJson(@PathVariable("id") Long id) {
        StateKind StateKind_ = StateKind.findStateKind(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (StateKind_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        StateKind_.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/thingkinds/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showThingKindJson(@PathVariable("id") Long id) {
        ThingKind ThingKind_ = ThingKind.findThingKind(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (ThingKind_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ThingKind_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/thingkinds", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listThingKindJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<ThingKind> result = ThingKind.findAllThingKinds();
        return new ResponseEntity<String>(ThingKind.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/thingkinds", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createThingKindFromJson(@RequestBody ThingKind ThingKind_) {
        ThingKind_.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(ThingKind_.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/thingkinds/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createThingKindFromJsonArray(@RequestBody List<ThingKind> thingKinds) {
        Collection<ThingKind> newThingKinds = new ArrayList<ThingKind>();
        for (ThingKind ThingKind_ : thingKinds) {
            ThingKind_.persist();
            newThingKinds.add(ThingKind_);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(ThingKind.toJsonArray(newThingKinds), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/thingkinds/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateThingKindFromJson(@RequestBody ThingKind ThingKind_) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ThingKind_.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ThingKind_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/thingkinds/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateThingKindFromJsonArray(@RequestBody List<ThingKind> thingKinds) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<ThingKind> newThingKinds = new ArrayList<ThingKind>();
        for (ThingKind ThingKind_ : thingKinds) {
            if (ThingKind_.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            newThingKinds.add(ThingKind_);
        }
        return new ResponseEntity<String>(ThingKind.toJsonArray(newThingKinds), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/thingkinds/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteThingKindFromJson(@PathVariable("id") Long id) {
        ThingKind ThingKind_ = ThingKind.findThingKind(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ThingKind_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        ThingKind_.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/unitkinds/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showUnitKindJson(@PathVariable("id") Long id) {
        UnitKind UnitKind_ = UnitKind.findUnitKind(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (UnitKind_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(UnitKind_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/unitkinds", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listUnitKindJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<UnitKind> result = UnitKind.findAllUnitKinds();
        return new ResponseEntity<String>(UnitKind.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/unitkinds", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createUnitKindFromJson(@RequestBody UnitKind UnitKind_) {
        UnitKind_.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(UnitKind_.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/unitkinds/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createUnitKindFromJsonArray(@RequestBody List<UnitKind> unitKinds) {
        Collection<UnitKind> newUnitKinds = new ArrayList<UnitKind>();
        for (UnitKind UnitKind_ : unitKinds) {
            UnitKind_.persist();
            unitKinds.add(UnitKind_);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(UnitKind.toJsonArray(newUnitKinds), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/unitkinds/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateUnitKindFromJson(@RequestBody UnitKind UnitKind_) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (UnitKind_.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(UnitKind_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/unitkinds/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateUnitKindFromJsonArray(@RequestBody List<UnitKind> unitKinds) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<UnitKind> newUnitKinds = new ArrayList<UnitKind>();
        for (UnitKind UnitKind_ : unitKinds) {
            if (UnitKind_.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            newUnitKinds.add(UnitKind_);
        }
        return new ResponseEntity<String>(UnitKind.toJsonArray(newUnitKinds), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/unitkinds/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteUnitKindFromJson(@PathVariable("id") Long id) {
        UnitKind UnitKind_ = UnitKind.findUnitKind(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (UnitKind_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        UnitKind_.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/valuekinds/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showValueKindJson(@PathVariable("id") Long id) {
        ValueKind ValueKind_ = ValueKind.findValueKind(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (ValueKind_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ValueKind_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/valuekinds", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listValueKindJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<ValueKind> result = ValueKind.findAllValueKinds();
        return new ResponseEntity<String>(ValueKind.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/valuekinds", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createValueKindFromJson(@RequestBody ValueKind ValueKind_) {
        ValueKind_.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(ValueKind_.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/valuekinds/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createValueKindFromJsonArray(@RequestBody List<ValueKind> valueKinds) {
        Collection<ValueKind> newValueKinds = new ArrayList<ValueKind>();
        for (ValueKind ValueKind_ : valueKinds) {
            ValueKind_.persist();
            newValueKinds.add(ValueKind_);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(ValueKind.toJsonArray(newValueKinds), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/valuekinds/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateValueKindFromJson(@RequestBody ValueKind ValueKind_) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ValueKind_.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ValueKind_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/valuekinds/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateValueKindFromJsonArray(@RequestBody List<ValueKind> valueKinds) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<ValueKind> newValueKinds = new ArrayList<ValueKind>();
        for (ValueKind ValueKind_ : valueKinds) {
            if (ValueKind_.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            newValueKinds.add(ValueKind_);
        }
        return new ResponseEntity<String>(ValueKind.toJsonArray(newValueKinds), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/valuekinds/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteValueKindFromJson(@PathVariable("id") Long id) {
        ValueKind ValueKind_ = ValueKind.findValueKind(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ValueKind_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        ValueKind_.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/interactionkinds/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showInteractionKindJson(@PathVariable("id") Long id) {
        InteractionKind InteractionKind_ = InteractionKind.findInteractionKind(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (InteractionKind_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(InteractionKind_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/interactionkinds", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listInteractionKindJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<InteractionKind> result = InteractionKind.findAllInteractionKinds();
        return new ResponseEntity<String>(InteractionKind.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/interactionkinds", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createInteractionKindFromJson(@RequestBody InteractionKind InteractionKind_) {
        InteractionKind_.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(InteractionKind_.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/interactionkinds/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createInteractionKindFromJsonArray(
            @RequestBody List<InteractionKind> interactionKinds) {
        Collection<InteractionKind> newInteractionKinds = new ArrayList<InteractionKind>();
        for (InteractionKind InteractionKind_ : interactionKinds) {
            InteractionKind_.persist();
            newInteractionKinds.add(InteractionKind_);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(InteractionKind.toJsonArray(newInteractionKinds), headers,
                HttpStatus.CREATED);
    }

    @RequestMapping(value = "/interactionkinds/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateInteractionKindFromJson(@RequestBody InteractionKind InteractionKind_) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (InteractionKind_.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(InteractionKind_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/interactionkinds/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateInteractionKindFromJsonArray(
            @RequestBody List<InteractionKind> interactionKinds) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<InteractionKind> newInteractionKinds = new ArrayList<InteractionKind>();
        for (InteractionKind InteractionKind_ : interactionKinds) {
            if (InteractionKind_.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            newInteractionKinds.add(InteractionKind_);
        }
        return new ResponseEntity<String>(InteractionKind.toJsonArray(newInteractionKinds), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/interactionkinds/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteInteractionKindFromJson(@PathVariable("id") Long id) {
        InteractionKind InteractionKind_ = InteractionKind.findInteractionKind(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (InteractionKind_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        InteractionKind_.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
}
