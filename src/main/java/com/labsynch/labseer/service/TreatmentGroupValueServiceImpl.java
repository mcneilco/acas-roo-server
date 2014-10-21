package com.labsynch.labseer.service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.labsynch.labseer.domain.AbstractValue;
import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.domain.ExperimentValue;
import com.labsynch.labseer.domain.TreatmentGroupValue;
import com.labsynch.labseer.dto.TreatmentGroupValueDTO;


@Service
@Transactional
public class TreatmentGroupValueServiceImpl implements TreatmentGroupValueService {

	@Override
	public List<TreatmentGroupValue> getTreatmentGroupValuesByExperimentIdAndStateTypeKind(
			Long experimentId, String stateType, String stateKind) {
		
		List<TreatmentGroupValue> treatmentGroupValues = TreatmentGroupValue.findTreatmentGroupValuesByExptIDAndStateTypeKind(experimentId, stateType, stateKind).getResultList();

		return treatmentGroupValues;
	}
	
	public List<TreatmentGroupValue> getTreatmentGroupValuesByExperimentIdAndStateTypeKindAndValueTypeKind(Long experimentId, String stateType,
			String stateKind, String valueType, String valueKind) {
		
		List<TreatmentGroupValue> treatmentGroupValues = TreatmentGroupValue.findTreatmentGroupValuesByExptIDAndStateTypeKindAndValueTypeKind(experimentId, stateType,
				stateKind, valueType, valueKind).getResultList();
		
		return treatmentGroupValues;
	}

	@Override
	public String getCsvList(List<TreatmentGroupValue> treatmentGroupValues) {
		StringWriter outFile = new StringWriter();
		ICsvBeanWriter beanWriter = null;
		try {
			beanWriter = new CsvBeanWriter(outFile, CsvPreference.TAB_PREFERENCE);
			final String[] header = TreatmentGroupValue.getColumns();
			final CellProcessor[] processors = TreatmentGroupValue.getProcessors();
			beanWriter.writeHeader(header);
			for (final TreatmentGroupValue treatmentGroupValue : treatmentGroupValues) {
				Collection<TreatmentGroupValueDTO> treatmentGroupValueDTOs = treatmentGroupValue.makeDTOsByAnalysisGroupIds();
				for (TreatmentGroupValueDTO treatmentGroupValueDTO : treatmentGroupValueDTOs) {
					beanWriter.write(treatmentGroupValueDTO, header, processors);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (beanWriter != null) {
				try {
					beanWriter.close();
					outFile.flush();
					outFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return outFile.toString();
	}

	@Override
	public List<TreatmentGroupValue> getTreatmentGroupValuesByAnalysisGroupIdAndStateTypeKind(
			Long analysisGroupId, String stateType, String stateKind) {
		
		List<TreatmentGroupValue> treatmentGroupValues = TreatmentGroupValue.findTreatmentGroupValuesByAnalysisGroupIDAndStateTypeKind(analysisGroupId, stateType, stateKind).getResultList();

		return treatmentGroupValues;
	}


}
