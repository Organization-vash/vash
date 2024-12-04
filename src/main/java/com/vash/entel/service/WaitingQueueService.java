package com.vash.entel.service;

import com.vash.entel.dto.NextPendingTicketResponseDTO;
import com.vash.entel.model.entity.Ticket_code;
import com.vash.entel.model.entity.WaitingQueue;
import com.vash.entel.model.enums.AttentionStatus;

import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;
import java.util.List;
public interface WaitingQueueService {
    Optional<NextPendingTicketResponseDTO> getNextPendingTicket(Integer moduleId);
    ResponseEntity<Map<String, String>> acceptTicket(Integer moduleId);
    ResponseEntity<Map<String, String>> rejectTicket();
    void addTicketToQueue(Ticket_code ticketCode);
    void addDerivateToQueue(Ticket_code ticketCode);
    Optional<Integer> getLastAcceptedTicketId();
    WaitingQueue findFirstByAttentionStatusOrderByCreatedAtAsc(AttentionStatus status);
    // Agregar al servicio WaitingQueueService
    List<WaitingQueue> getTicketsInWaitingState();
    List<Map<String, Object>> getTicketsInAttendingState();

}
