package com.vash.entel.service;

import com.vash.entel.dto.NextPendingTicketResponseDTO;
import com.vash.entel.model.entity.Ticket_code;
import com.vash.entel.model.entity.WaitingQueue;
import com.vash.entel.model.enums.AttentionStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;

public interface WaitingQueueService {
    Optional<NextPendingTicketResponseDTO> getNextPendingTicket(Integer moduleId);
    ResponseEntity<Map<String, String>> acceptTicket();
    ResponseEntity<Map<String, String>> rejectTicket();
    void addTicketToQueue(Ticket_code ticketCode);
}
