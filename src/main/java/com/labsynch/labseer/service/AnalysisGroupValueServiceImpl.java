package com.labsynch.labseer.service;

import java.io.IOException;
import java.io.StringWriter;
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


@Service
@Transactional
public class AnalysisGroupValueServiceImpl implements AnalysisGroupValueService {

	@Override
	public List<AnalysisGroupValue> getAnalysisGroupValuesByExperimentIdAndStateTypeKind(
			Long experimentId, String stateType, String stateKind) {
		
		List<AnalysisGroupValue> analysisGroupValues = AnalysisGroupValue.findAnalysisGroupValuesByExptIDAndStateTypeKind(experimentId, stateType, stateKind).getResultList();

		return analysisGroupValues;
	}
	
	public List<AnalysisGroupValue> getAnalysisGroupValuesByExperimentIdAndStateTypeKindAndValueTypeKind(Long experimentId, String stateType,
			String stateKind, String valueType, String valueKind) {
		
		List<AnalysisGroupValue> analysisGroupValues = AnalysisGroupValue.findAnalysisGroupValuesByExptIDAndStateTypeKindAndValueTypeKind(experimentId, stateType,
				stateKind, valueType, valueKind).getResultList();
		
		return analysisGroupValues;
	}
	
	public List<AnalysisGroupValue> getAnalysisGroupValuesByAnalysisGroupIdAndStateTypeKind(Long analysisGroupId, String stateType, String stateKind) {
		List<AnalysisGroupValue> analysisGroupValues = AnalysisGroupValue.findAnalysisGroupValuesByAnalysisGroupIDAndStateTypeKind(analysisGroupId, stateType, stateKind).getResultList();

		return analysisGroupValues;
	}

	@Override
	public String getCsvList(List<AnalysisGroupValue> analysisGroupValues) {
		StringWriter outFile = new StringWriter();
		ICsvBeanWriter beanWriter = null;
		try {
			beanWriter = new CsvBeanWriter(outFile, CsvPreference.TAB_PREFERENCE);
			final String[] header = AnalysisGroupValue.getColumns();
			final CellProcessor[] processors = AnalysisGroupValue.getProcessors();
			beanWriter.writeHeader(header);
			for (final AnalysisGroupValue analysisGroupValue : analysisGroupValues) {
				beanWriter.write(analysisGroupValue, header, processors);
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
	public List<AnalysisGroupValue> getAnalysisGroupValuesByAnalysiGroupIdAndStateTypeKindAndValueTypeKind(
			Long analysisGroupId, String stateType, String stateKind,
			String valueType, String valueKind) {
		
		List<AnalysisGroupValue> analysisGroupValues = AnalysisGroupValue.findAnalysisGroupValuesByAnalysisGroupIDAndStateTypeKindAndValueTypeKind(analysisGroupId, stateType,
				stateKind, valueType, valueKind).getResultList();
		
		return analysisGroupValues;
	}
}
