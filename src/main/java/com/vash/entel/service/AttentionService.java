package com.vash.entel.service;

import com.vash.entel.dto.NextPendingTicketResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface AttentionService {
    Optional<NextPendingTicketResponseDTO> getNextPendingTicket(Integer moduleId);
    ResponseEntity<String> acceptTicket();
    ResponseEntity<String> rejectTicket();
}
