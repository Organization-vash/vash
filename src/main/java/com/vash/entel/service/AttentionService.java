package com.vash.entel.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface AttentionService {
    ResponseEntity<Map<String, String>> finalizeAttention(Integer attentionId);
    byte[] generateExcelReport() throws IOException;
    List<Map<String, Object>> getReportData();

}
