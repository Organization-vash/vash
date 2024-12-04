package com.vash.entel.service;


import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface SurveyReportService {
    List<Map<String, Object>> getSurveyReport();
    byte[] generatePieChart() throws IOException;
    byte[] generateBarChart() throws IOException;
    byte[] generateExcelReport() throws IOException;
}
