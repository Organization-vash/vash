package com.vash.entel.service;

import com.vash.entel.dto.NextPendingTicketResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;

public interface AttentionService {
    Optional<NextPendingTicketResponseDTO> getNextPendingTicket(Integer moduleId);
    ResponseEntity<Map<String, String>> acceptTicket();
    ResponseEntity<Map<String, String>> rejectTicket();
}
