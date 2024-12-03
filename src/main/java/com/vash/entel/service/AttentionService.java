package com.vash.entel.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface AttentionService {
    void addServiceToAttention(Integer attentionId, Integer serviceId);
    void removeServiceFromAttention(Integer attentionId, Integer serviceId);
    ResponseEntity<Map<String, String>> finalizeAttention(Integer attentionId);

    byte[] generateExcelReport() throws IOException;
    List<Map<String, Object>> getReportData();

}

