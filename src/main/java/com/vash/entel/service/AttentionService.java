package com.vash.entel.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface AttentionService {
    void addServiceToAttention(Integer attentionId, Integer serviceId);
    void removeServiceFromAttention(Integer attentionId, Integer serviceId);
    ResponseEntity<Map<String, String>> finalizeAttention(Integer attentionId);
}
