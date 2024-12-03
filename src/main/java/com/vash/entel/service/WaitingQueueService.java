package com.vash.entel.service;

import com.vash.entel.dto.NextPendingTicketResponseDTO;
import com.vash.entel.model.entity.Ticket_code;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;

public interface WaitingQueueService {
    Optional<NextPendingTicketResponseDTO> getNextPendingTicket(Integer moduleId);
    ResponseEntity<Map<String, String>> acceptTicket(Integer moduleId);
    ResponseEntity<Map<String, String>> rejectTicket();
    void addTicketToQueue(Ticket_code ticketCode);
    Optional<Integer> getLastAcceptedTicketId();
}
