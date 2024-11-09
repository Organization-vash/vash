package com.vash.entel.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface AttentionService {
    ResponseEntity<Map<String, String>> finalizeAttention(Integer attentionId);
}
