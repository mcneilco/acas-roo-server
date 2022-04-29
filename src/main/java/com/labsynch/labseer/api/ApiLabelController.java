package com.labsynch.labseer.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.AnalysisGroupLabel;
import com.labsynch.labseer.domain.ContainerLabel;
import com.labsynch.labseer.domain.ExperimentLabel;
import com.labsynch.labseer.domain.LsThingLabel;
import com.labsynch.labseer.domain.ProtocolLabel;
import com.labsynch.labseer.domain.SubjectLabel;
import com.labsynch.labseer.domain.TreatmentGroupLabel;

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

public class ApiLabelController {

    private static final Logger logger = LoggerFactory.getLogger(ApiLabelController.class);

    @RequestMapping(value = "/protocollabels/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showProtocolLabelJson(@PathVariable("id") Long id) {
        ProtocolLabel ProtocolLabel_ = ProtocolLabel.findProtocolLabel(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (ProtocolLabel_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ProtocolLabel_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/protocollabels", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listProtocolLabelJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<ProtocolLabel> result = ProtocolLabel.findAllProtocolLabels();
        return new ResponseEntity<String>(ProtocolLabel.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/protocollabels", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createProtocolLabelFromJson(@RequestBody ProtocolLabel ProtocolLabel_) {
        ProtocolLabel_.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(ProtocolLabel_.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/protocollabels/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createProtocolLabelFromJsonArray(@RequestBody List<ProtocolLabel> protocolLabels) {
        Collection<ProtocolLabel> newProtocolLabels = new ArrayList<ProtocolLabel>();
        for (ProtocolLabel ProtocolLabel_ : protocolLabels) {
            ProtocolLabel_.persist();
            newProtocolLabels.add(ProtocolLabel_);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(ProtocolLabel.toJsonArray(newProtocolLabels), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/protocollabels/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateProtocolLabelFromJson(@RequestBody ProtocolLabel ProtocolLabel_) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ProtocolLabel_.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ProtocolLabel_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/protocollabels/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateProtocolLabelFromJsonArray(@RequestBody List<ProtocolLabel> protocolLabels) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<ProtocolLabel> newProtocolLabels = new ArrayList<ProtocolLabel>();
        for (ProtocolLabel ProtocolLabel_ : protocolLabels) {
            if (ProtocolLabel_.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            newProtocolLabels.add(ProtocolLabel_);
        }
        return new ResponseEntity<String>(ProtocolLabel.toJsonArray(newProtocolLabels), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/protocollabels/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteProtocolLabelFromJson(@PathVariable("id") Long id) {
        ProtocolLabel ProtocolLabel_ = ProtocolLabel.findProtocolLabel(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ProtocolLabel_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        ProtocolLabel_.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/experimentlabels/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showExperimentLabelJson(@PathVariable("id") Long id) {
        ExperimentLabel ExperimentLabel_ = ExperimentLabel.findExperimentLabel(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (ExperimentLabel_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ExperimentLabel_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/experimentlabels", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listExperimentLabelJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<ExperimentLabel> result = ExperimentLabel.findAllExperimentLabels();
        return new ResponseEntity<String>(ExperimentLabel.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/experimentlabels", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createExperimentLabelFromJson(@RequestBody ExperimentLabel ExperimentLabel_) {
        ExperimentLabel_.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(ExperimentLabel_.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/experimentlabels/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createExperimentLabelFromJsonArray(
            @RequestBody List<ExperimentLabel> experimentLabels) {
        Collection<ExperimentLabel> newExperimentLabels = new ArrayList<ExperimentLabel>();
        for (ExperimentLabel ExperimentLabel_ : experimentLabels) {
            ExperimentLabel_.persist();
            newExperimentLabels.add(ExperimentLabel_);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(ExperimentLabel.toJsonArray(newExperimentLabels), headers,
                HttpStatus.CREATED);
    }

    @RequestMapping(value = "/experimentlabels/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateExperimentLabelFromJson(@RequestBody ExperimentLabel ExperimentLabel_) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ExperimentLabel_.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ExperimentLabel_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/experimentlabels/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateExperimentLabelFromJsonArray(
            @RequestBody List<ExperimentLabel> experimentLabels) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<ExperimentLabel> newExperimentLabels = new ArrayList<ExperimentLabel>();
        for (ExperimentLabel ExperimentLabel_ : experimentLabels) {
            if (ExperimentLabel_.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            newExperimentLabels.add(ExperimentLabel_);
        }
        return new ResponseEntity<String>(ExperimentLabel.toJsonArray(newExperimentLabels), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/experimentlabels/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteExperimentLabelFromJson(@PathVariable("id") Long id) {
        ExperimentLabel ExperimentLabel_ = ExperimentLabel.findExperimentLabel(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ExperimentLabel_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        ExperimentLabel_.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/analysisgrouplabels/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showAnalysisGroupLabelJson(@PathVariable("id") Long id) {
        AnalysisGroupLabel AnalysisGroupLabel_ = AnalysisGroupLabel.findAnalysisGroupLabel(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (AnalysisGroupLabel_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(AnalysisGroupLabel_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/analysisgrouplabels", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listAnalysisGroupLabelJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<AnalysisGroupLabel> result = AnalysisGroupLabel.findAllAnalysisGroupLabels();
        return new ResponseEntity<String>(AnalysisGroupLabel.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/analysisgrouplabels", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createAnalysisGroupLabelFromJson(
            @RequestBody AnalysisGroupLabel AnalysisGroupLabel_) {
        AnalysisGroupLabel_.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(AnalysisGroupLabel_.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/analysisgrouplabels/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createAnalysisGroupLabelFromJsonArray(
            @RequestBody List<AnalysisGroupLabel> analysisGroupLabels) {
        Collection<AnalysisGroupLabel> newAnalysisGroupLabels = new ArrayList<AnalysisGroupLabel>();
        for (AnalysisGroupLabel AnalysisGroupLabel_ : analysisGroupLabels) {
            AnalysisGroupLabel_.persist();
            newAnalysisGroupLabels.add(AnalysisGroupLabel_);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(AnalysisGroupLabel.toJsonArray(newAnalysisGroupLabels), headers,
                HttpStatus.CREATED);
    }

    @RequestMapping(value = "/analysisgrouplabels/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateAnalysisGroupLabelFromJson(
            @RequestBody AnalysisGroupLabel AnalysisGroupLabel_) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (AnalysisGroupLabel_.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(AnalysisGroupLabel_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/analysisgrouplabels/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateAnalysisGroupLabelFromJsonArray(
            @RequestBody List<AnalysisGroupLabel> analysisGroupLabels) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<AnalysisGroupLabel> newAnalysisGroupLabels = new ArrayList<AnalysisGroupLabel>();
        for (AnalysisGroupLabel AnalysisGroupLabel_ : analysisGroupLabels) {
            if (AnalysisGroupLabel_.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            newAnalysisGroupLabels.add(AnalysisGroupLabel_);
        }
        return new ResponseEntity<String>(AnalysisGroupLabel.toJsonArray(newAnalysisGroupLabels), headers,
                HttpStatus.OK);
    }

    @RequestMapping(value = "/analysisgrouplabels/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteAnalysisGroupLabelFromJson(@PathVariable("id") Long id) {
        AnalysisGroupLabel AnalysisGroupLabel_ = AnalysisGroupLabel.findAnalysisGroupLabel(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (AnalysisGroupLabel_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        AnalysisGroupLabel_.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/treatmentgrouplabels/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showTreatmentGroupLabelJson(@PathVariable("id") Long id) {
        TreatmentGroupLabel TreatmentGroupLabel_ = TreatmentGroupLabel.findTreatmentGroupLabel(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (TreatmentGroupLabel_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(TreatmentGroupLabel_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/treatmentgrouplabels", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listTreatmentGroupLabelJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<TreatmentGroupLabel> result = TreatmentGroupLabel.findAllTreatmentGroupLabels();
        return new ResponseEntity<String>(TreatmentGroupLabel.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/treatmentgrouplabels", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createTreatmentGroupLabelFromJson(
            @RequestBody TreatmentGroupLabel TreatmentGroupLabel_) {
        TreatmentGroupLabel_.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(TreatmentGroupLabel_.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/treatmentgrouplabels/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createTreatmentGroupLabelFromJsonArray(
            @RequestBody List<TreatmentGroupLabel> treatmentGroupLabels) {
        Collection<TreatmentGroupLabel> newTreatmentGroupLabels = new ArrayList<TreatmentGroupLabel>();
        for (TreatmentGroupLabel TreatmentGroupLabel_ : treatmentGroupLabels) {
            TreatmentGroupLabel_.persist();
            newTreatmentGroupLabels.add(TreatmentGroupLabel_);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(TreatmentGroupLabel.toJsonArray(newTreatmentGroupLabels), headers,
                HttpStatus.CREATED);
    }

    @RequestMapping(value = "/treatmentgrouplabels/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateTreatmentGroupLabelFromJson(
            @RequestBody TreatmentGroupLabel TreatmentGroupLabel_) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (TreatmentGroupLabel_.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(TreatmentGroupLabel_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/treatmentgrouplabels/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateTreatmentGroupLabelFromJsonArray(
            @RequestBody List<TreatmentGroupLabel> treatmentGroupLabels) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<TreatmentGroupLabel> newTreatmentGroupLabels = new ArrayList<TreatmentGroupLabel>();
        for (TreatmentGroupLabel TreatmentGroupLabel_ : treatmentGroupLabels) {
            if (TreatmentGroupLabel_.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            newTreatmentGroupLabels.add(TreatmentGroupLabel_);
        }
        return new ResponseEntity<String>(TreatmentGroupLabel.toJsonArray(newTreatmentGroupLabels), headers,
                HttpStatus.OK);
    }

    @RequestMapping(value = "/treatmentgrouplabels/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteTreatmentGroupLabelFromJson(@PathVariable("id") Long id) {
        TreatmentGroupLabel TreatmentGroupLabel_ = TreatmentGroupLabel.findTreatmentGroupLabel(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (TreatmentGroupLabel_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        TreatmentGroupLabel_.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/subjectlabels/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showSubjectLabelJson(@PathVariable("id") Long id) {
        SubjectLabel SubjectLabel_ = SubjectLabel.findSubjectLabel(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (SubjectLabel_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(SubjectLabel_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/subjectlabels", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listSubjectLabelJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<SubjectLabel> result = SubjectLabel.findAllSubjectLabels();
        return new ResponseEntity<String>(SubjectLabel.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/subjectlabels", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createSubjectLabelFromJson(@RequestBody SubjectLabel SubjectLabel_) {
        SubjectLabel_.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(SubjectLabel_.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/subjectlabels/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createSubjectLabelFromJsonArray(@RequestBody List<SubjectLabel> subjectLabels) {
        Collection<SubjectLabel> newSubjectLabels = new ArrayList<SubjectLabel>();
        for (SubjectLabel SubjectLabel_ : subjectLabels) {
            SubjectLabel_.persist();
            newSubjectLabels.add(SubjectLabel_);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(SubjectLabel.toJsonArray(newSubjectLabels), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/subjectlabels/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateSubjectLabelFromJson(@RequestBody SubjectLabel SubjectLabel_) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (SubjectLabel_.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(SubjectLabel_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/subjectlabels/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateSubjectLabelFromJsonArray(@RequestBody List<SubjectLabel> subjectLabels) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<SubjectLabel> newSubjectLabels = new ArrayList<SubjectLabel>();
        for (SubjectLabel SubjectLabel_ : subjectLabels) {
            if (SubjectLabel_.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            newSubjectLabels.add(SubjectLabel_);
        }
        return new ResponseEntity<String>(SubjectLabel.toJsonArray(newSubjectLabels), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/subjectlabels/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteSubjectLabelFromJson(@PathVariable("id") Long id) {
        SubjectLabel SubjectLabel_ = SubjectLabel.findSubjectLabel(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (SubjectLabel_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        SubjectLabel_.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/lsthinglabels/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showLsThingLabelJson(@PathVariable("id") Long id) {
        LsThingLabel LsThingLabel_ = LsThingLabel.findLsThingLabel(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (LsThingLabel_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(LsThingLabel_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/lsthinglabels", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listLsThingLabelJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<LsThingLabel> result = LsThingLabel.findAllLsThingLabels();
        return new ResponseEntity<String>(LsThingLabel.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/lsthinglabels", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createLsThingLabelFromJson(@RequestBody LsThingLabel LsThingLabel_) {
        LsThingLabel_.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(LsThingLabel_.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/lsthinglabels/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createLsThingLabelFromJsonArray(@RequestBody List<LsThingLabel> lsThingLabels) {
        Collection<LsThingLabel> newLsThingLabels = new ArrayList<LsThingLabel>();
        for (LsThingLabel LsThingLabel_ : lsThingLabels) {
            LsThingLabel_.persist();
            newLsThingLabels.add(LsThingLabel_);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(LsThingLabel.toJsonArray(newLsThingLabels), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/lsthinglabels/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateLsThingLabelFromJson(@RequestBody LsThingLabel LsThingLabel_) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (LsThingLabel_.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(LsThingLabel_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/lsthinglabels/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateLsThingLabelFromJsonArray(@RequestBody List<LsThingLabel> lsThingLabels) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<LsThingLabel> newLsThingLabels = new ArrayList<LsThingLabel>();
        for (LsThingLabel LsThingLabel_ : lsThingLabels) {
            if (LsThingLabel_.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            newLsThingLabels.add(LsThingLabel_);
        }
        return new ResponseEntity<String>(LsThingLabel.toJsonArray(newLsThingLabels), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/lsthinglabels/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteLsThingLabelFromJson(@PathVariable("id") Long id) {
        LsThingLabel LsThingLabel_ = LsThingLabel.findLsThingLabel(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (LsThingLabel_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        LsThingLabel_.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/containerlabels/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showContainerLabelJson(@PathVariable("id") Long id) {
        ContainerLabel ContainerLabel_ = ContainerLabel.findContainerLabel(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (ContainerLabel_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ContainerLabel_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/containerlabels", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listContainerLabelJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<ContainerLabel> result = ContainerLabel.findAllContainerLabels();
        return new ResponseEntity<String>(ContainerLabel.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/containerlabels", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createContainerLabelFromJson(@RequestBody ContainerLabel ContainerLabel_) {
        ContainerLabel_.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(ContainerLabel_.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/containerlabels/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createContainerLabelFromJsonArray(@RequestBody List<ContainerLabel> containerLabels) {
        Collection<ContainerLabel> newContainerLabels = new ArrayList<ContainerLabel>();
        for (ContainerLabel ContainerLabel_ : containerLabels) {
            ContainerLabel_.persist();
            newContainerLabels.add(ContainerLabel_);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(ContainerLabel.toJsonArray(newContainerLabels), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/containerlabels/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateContainerLabelFromJson(@RequestBody ContainerLabel ContainerLabel_) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ContainerLabel_.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ContainerLabel_.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/containerlabels/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateContainerLabelFromJsonArray(@RequestBody List<ContainerLabel> containerLabels) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<ContainerLabel> newContainerLabels = new ArrayList<ContainerLabel>();
        for (ContainerLabel ContainerLabel_ : containerLabels) {
            if (ContainerLabel_.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            newContainerLabels.add(ContainerLabel_);
        }
        return new ResponseEntity<String>(ContainerLabel.toJsonArray(newContainerLabels), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/containerlabels/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteContainerLabelFromJson(@PathVariable("id") Long id) {
        ContainerLabel ContainerLabel_ = ContainerLabel.findContainerLabel(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ContainerLabel_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        ContainerLabel_.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

}
