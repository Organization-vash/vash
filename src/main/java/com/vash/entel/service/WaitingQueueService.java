package com.vash.entel.service;

import com.vash.entel.model.entity.Ticket_code;
import com.vash.entel.model.entity.WaitingQueue;
import com.vash.entel.model.enums.AttentionStatus;

import java.util.Optional;

public interface WaitingQueueService {
    Optional<WaitingQueue> getNextPendingTicket();
    void updateWaitingQueueStatus(WaitingQueue waitingQueue, AttentionStatus status);
    void addTicketToQueue(Ticket_code ticketCode);
}
