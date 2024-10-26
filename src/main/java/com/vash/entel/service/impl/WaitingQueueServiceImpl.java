package com.vash.entel.service.impl;

import com.vash.entel.model.entity.Ticket_code;
import com.vash.entel.model.entity.WaitingQueue;
import com.vash.entel.model.enums.AttentionStatus;
import com.vash.entel.repository.WaitingQueueRepository;
import com.vash.entel.service.WaitingQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class WaitingQueueServiceImpl implements WaitingQueueService {
    private final WaitingQueueRepository waitingQueueRepository;

    @Override
    public Optional<WaitingQueue> getNextPendingTicket() {
        return Optional.ofNullable(waitingQueueRepository.findFirstByAttentionStatusOrderByCreatedAtAsc(AttentionStatus.WAITING));
    }

    @Override
    public void updateWaitingQueueStatus(WaitingQueue waitingQueue, AttentionStatus status) {
        waitingQueue.setAttentionStatus(status);
        waitingQueueRepository.save(waitingQueue);
    }

    @Override
    public void addTicketToQueue(Ticket_code ticketCode) {
        WaitingQueue waitingQueue = new WaitingQueue();
        waitingQueue.setTicketCode(ticketCode);
        waitingQueue.setCustomer(ticketCode.getCustomer());
        waitingQueue.setCreatedAt(LocalDateTime.now());
        waitingQueue.setAttentionStatus(AttentionStatus.WAITING);

        waitingQueueRepository.save(waitingQueue);
    }
}
